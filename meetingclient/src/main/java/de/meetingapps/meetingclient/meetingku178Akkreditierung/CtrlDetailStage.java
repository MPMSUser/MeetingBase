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

/*Testfälle
 * EK 36 = Vertreter1 Aktionär 5
 * EK 37 = Vertreter2 Aktionär 5
 */

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingBestand.CtrlEintrittskarteDrucken;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComBl.BlStimmkartendruck;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclServiceDeskWorkflow;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstServicedesk;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusClientGet;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusClientGetRC;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**Quick-Situationen und Buttons:
 * 
 * Grundsätzlich: 
 * > wenn Zutrittsdokument nicht vorgelegt werden kann, dann Personalausweis vorzeigen lassen.
 * > Wenn Vertreter, dann nur als Vertreter anerkennen wenn Vollmacht vorhanden ist, ansonsten als Dritten behandeln
 * > Dritter: nur Gastkarte ausstellen
 * 
 * Situationen für Aktionär oder Vertreter:
 * ----------------------------------------
 * (1) Aktionär ist nicht angemeldet
 *      Leider ist der Bestand nicht angemeldet. Darf ich Ihnen eine Gastkarte (ohne Rede- und Stimmrecht) ausstellen?
 *          Gastkarte für Aktionär ausstellen (B)
 *          Gastkarte für Vertreter ausstellen (A)
 *      Wenn nein: -> Supervisor
 *          
 * (2) Aktionär ist angemeldet, nicht präsent, Briefwahl/SRV erteilt
 *      Wollen Sie Ihre Briefwahl / Vollmacht an .... widerrufen und teilnehmen / abstimmen?
 *          Briefwahl/SRV widerrufen (C)
 *          Kann Zutrittsdokument vorgelegt werden? Wenn nein, Ersatz-Eintrittskarte ausstellen (D)
 *      Soll Ihre Briefwahl / Vollmacht an .... weiter bestehen und Sie nehmen als Gast teil? 
 *          Gastkarte für Aktionär ausstellen (B)
 *          Gastkarte für Vertreter ausstellen (A)
 *          
 * (3) Aktionär ist angemeldet, nicht präsent
 *      Kann eine Zutrittskarte vorgelegt werden?
 *           Ersatzkarte ausstellen (D)
 *           
 * (4) Aktionär ist angemeldet, bereits präsent
 *       Haben Sie Ihre Stimmunterlagen verloren?
 *              Ersatzstimmkarte ausstellen? (E)
 *              
 * (5) Aktionär ist nicht gefunden
 *          Gastkarte für neuen Teilnehmer ausstellen (A)
 *          
 * (6) Gast ist nicht präsent oder präsent (aber unterschiedliche Texte)
 *          Erstzkarte ausstellen (Gast)
 *          Falls präsent: Neue Gastkarte für diesen Gast ausstellen
 *      
 * 
 * Buttons:
 * --------
 * (A) Gastkarte mit neuen Zugangsdaten eingeben
 * (B) Gastkarte für Aktionär ausstellen
 * (C) Briefwahl/SRV widerrufen
 * (D) Ersatzkarte Aktionär ausstellen
 *          Alle zugeordneten EKs anzeigen. Falls mehrere, dann auswählzwang welche storniert werden soll.
 * (E) Ersatzstimmkarte ausstellen
 * (F) Ersatzkarte Gast ausstellen
 * 
 * 
 * Anzeige "Standard":
 * -------------------
 * Im Oberen Bereich gefundene Daten:
 * ..................................
 * Gast:
 * > Ek-Nr, Name, Vorname, Straße, Ort, Aktienzahl
 * 
 * Nicht-Angemeldeter Aktionär:
 * > Aktionärsdaten: Aktionärsnummer, Name, Vorname, Straße, Ort, Aktienzahl
 * 
 * Angemeldeter Aktionär:
 * > Aktionärsdaten: Aktionärsnummer (nicht bei Inhaberaktien!), Name, Vorname, Straße, Ort, Aktienzahl
 * > Hinweis "weitere Anmeldungen vorhanden"
 * > In div. Sammelkarten (soweit vorhanden)
 * > Zutrittskarten (soweit vorhanden)
 *
 * Im unteren Bereich: Obige Dialoge, mit Weiter-Button. Verschiedene Tabs.
 * Tabs:
 * > Abfrage wie oben (mit Weiter-Button)
 * > Daten für Gast eingeben (mit Abbrechen / Speichern-Button)
 * 
 *
 *
 */

public class CtrlDetailStage extends CtrlRoot {

    /** The log drucken. */
    int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb standard. */
    @FXML
    private Tab tbStandard;

    /** The btn fertig standard. */
    @FXML
    private Button btnFertigStandard;

    /** The btn button 1. */
    @FXML
    private Button btnButton1;

    /** The btn button 2. */
    @FXML
    private Button btnButton2;

    /** The btn button 5. */
    @FXML
    private Button btnButton5;

    /** The btn button 3. */
    @FXML
    private Button btnButton3;

    /** The btn button 4. */
    @FXML
    private Button btnButton4;

    /** The btn button 6. */
    @FXML
    private Button btnButton6;

    /** The btn button 7. */
    @FXML
    private Button btnButton7;

    /** The btn button 8. */
    @FXML
    private Button btnButton8;

    /** The btn button 9. */
    @FXML
    private Button btnButton9;

    /** The btn button 10. */
    @FXML
    private Button btnButton10;

    /** The tt button 1. */
    @FXML
    private Tooltip ttButton1;

    /** The tt button 2. */
    @FXML
    private Tooltip ttButton2;

    /** The tt button 3. */
    @FXML
    private Tooltip ttButton3;

    /** The tt button 4. */
    @FXML
    private Tooltip ttButton4;

    /** The tt button 5. */
    @FXML
    private Tooltip ttButton5;

    /** The tt button 6. */
    @FXML
    private Tooltip ttButton6;

    /** The tt button 7. */
    @FXML
    private Tooltip ttButton7;

    /** The tt button 8. */
    @FXML
    private Tooltip ttButton8;

    /** The tt button 9. */
    @FXML
    private Tooltip ttButton9;

    /** The tt button 10. */
    @FXML
    private Tooltip ttButton10;

    /** The scpn standard. */
    @FXML
    private ScrollPane scpnStandard;

    /** The tp verarbeitung. */
    @FXML
    private TabPane tpVerarbeitung;

    /** The tbn ausfuehren. */
    @FXML
    private AnchorPane tbnAusfuehren;

    /** The btn auswahl weiter. */
    @FXML
    private Button btnAuswahlWeiter;

    /** The lbl auswahl text. */
    @FXML
    private Label lblAuswahlText;

    /** The rb auswahl 1. */
    @FXML
    private RadioButton rbAuswahl1;

    /** The tg auswahl. */
    @FXML
    private ToggleGroup tgAuswahl;

    /** The rb auswahl 2. */
    @FXML
    private RadioButton rbAuswahl2;

    /** The rb auswahl 3. */
    @FXML
    private RadioButton rbAuswahl3;

    /** The rb auswahl 4. */
    @FXML
    private RadioButton rbAuswahl4;

    /** The rb auswahl 5. */
    @FXML
    private RadioButton rbAuswahl5;

    /** The rb auswahl 6. */
    @FXML
    private RadioButton rbAuswahl6;

    /** The rb auswahl 7. */
    @FXML
    private RadioButton rbAuswahl7;

    /** The rb auswahl 8. */
    @FXML
    private RadioButton rbAuswahl8;

    /** The rb auswahl 9. */
    @FXML
    private RadioButton rbAuswahl9;

    /** The rb auswahl 10. */
    @FXML
    private RadioButton rbAuswahl10;

    /** The btn gastkarte ausstellen. */
    @FXML
    private Button btnGastkarteAusstellen;

    /** The tb detail. */
    @FXML
    private Tab tbDetail;

    /** The btn fertig detail. */
    @FXML
    private Button btnFertigDetail;

    /** The scpn detail. */
    @FXML
    private ScrollPane scpnDetail;

    /** The tb auswahl. */
    @FXML
    private Tab tbAuswahl;

    /** The tb gastkarte. */
    @FXML
    private Tab tbGastkarte;

    /** The h V anzeige. */
    /*++++++++++Ab hier indivdiuelle FXML-Elemente+++++++*/
    private VBox hVAnzeige = null;

    /**
     * Dient zur Anzeige der Art des angezeigten Satzes und den Anzeigestatus. Dabei
     * wird labelArtInhalt immer angezeigt, und dazu dann jeweils anzeigeStatus des
     * entsprechenden Workflow-Elements ersetzt
     */
    private Label labelArtInhalt = new Label("");

    /** The label art inhalt basis. */
    private String labelArtInhaltBasis = "";

    /**
     * Unklar wofür.
     *
     * @param event the event
     */
    @FXML
    void onBtnFertigDetail(ActionEvent event) {
        /*AAAAA ServiceDesk*/
    }

    /**
     * ************************Statusdaten des angezeigten
     * Satzes***********************.
     */

    /*++++++Analog zu EclServiceDeskWorkflow++++++++++++++++*/
    private int istGast = 0;

    /** The ist aktionaer. */
    private int istAktionaer = 0;

    /** The ist sammelkarte. */
    private int istSammelkarte = 0;

    /** The ist angemeldeter aktionaer. */
    private int istAngemeldeterAktionaer = 0;

    /** The ist nullbestand. */
    private int istNullbestand = 0;

    /** The ist gerade praesent. */
    private int istGeradePraesent = 0;

    /** The ist gerade selbst praesent. */
    private int istGeradeSelbstPraesent = 0;

    /** The ist gerade vertreten praesent. */
    private int istGeradeVertretenPraesent = 0;

    /** The war praesent. */
    private int warPraesent = 0;

    /** The ist gerade virtuell praesent. */
    private int istGeradeVirtuellPraesent = 0;

    /** The ist gerade virtuell selbst praesent. */
    private int istGeradeVirtuellSelbstPraesent = 0;

    /** The ist gerade virtuell vertreten praesent. */
    private int istGeradeVirtuellVertretenPraesent = 0;

    /** The war virtuell praesent. */
    private int warVirtuellPraesent = 0;

    /** The ist aktionaer in sammelkarte. */
    private int istAktionaerInSammelkarte = 0;

    /** The ist in briefwahlsammelkarte. */
    private int istInBriefwahlsammelkarte = 0;

    /** The ist in SR vsammelkarte. */
    private int istInSRVsammelkarte = 0;

    /** The ist in KIA vsammelkarte. */
    private int istInKIAVsammelkarte = 0;

    /** The ist in dauervollmachtsammelkarte. */
    private int istInDauervollmachtsammelkarte = 0;

    /** The ist in orgasammelkarte. */
    private int istInOrgasammelkarte = 0;

    /** The hat E kzugeordnet. */
    private int hatEKzugeordnet = 0;

    /** The hat S kzugeordnet. */
    private int hatSKzugeordnet = 0;

    /** The hat vollmacht erteilt. */
    private int hatVollmachtErteilt = 0;

    /** The es gibt stimmkarten zum zuordnen. */
    private int esGibtStimmkartenZumZuordnen = 0;

    /** The es gibt stimmkarten zum drucken. */
    private int esGibtStimmkartenZumDrucken = 0;

    /** The anz nicht stornierte karten. */
    /*+++++++++Beim Einlesen weitere ermittelte Werte*/
    private int anzNichtStornierteKarten = 0;

    /** The anz stornierte karten. */
    private int anzStornierteKarten = 0;

    /** The anz nicht stornierte stimmkarten. */
    private int anzNichtStornierteStimmkarten = 0;

    /** The anz stornierte stimmkarten. */
    private int anzStornierteStimmkarten = 0;

    /** The anz vertreter. */
    private int anzVertreter = 0;

    /** The anz vertreter storniert. */
    private int anzVertreterStorniert = 0;

    /** The aktuell gattung. */
    private int aktuellGattung = 0;

    /** The aktuell zugeordnete meldung neu. */
    private EclZugeordneteMeldungNeu aktuellZugeordneteMeldungNeu = null;

    /** The aktuell meldung. */
    private EclMeldung aktuellMeldung = null;

    /** The aktuell zugeordnete willenserklaerungen list. */
    private List<EclWillenserklaerungStatusNeu> aktuellZugeordneteWillenserklaerungenList = null;

    /** The erste EK nr. */
    private String ersteEKNr = "";

    /** The erste EK nr neben. */
    private String ersteEKNrNeben = "";

    /** Speziell für Sammelkarten. */
    private EclMeldung[] aktuellSammelMeldung = null;

    /** The aktuell sammel zutrittskarten array. */
    private EclZutrittskarten[][] aktuellSammelZutrittskartenArray = null;

    /** The aktuell sammel stimmkarten array. */
    private EclStimmkarten[][] aktuellSammelStimmkartenArray = null;

    /** The aktuell sammel willens erkl vollmachten an dritte array. */
    private EclWillensErklVollmachtenAnDritte[][] aktuellSammelWillensErklVollmachtenAnDritteArray = null;

    /** The wert antwort. */
    /*+++++++++++++Aktuelle Werte der beantworten Fragen++++++++++++++++*/
    private int[] wertAntwort = new int[10];

    /*++++++++++Aktuelle Workflowaktion+++++++++++*/
    /** Kann auch null sein => keine passende Aktion gefunden!. */
    private EclServiceDeskWorkflow aktuelleServiceDeskWorkflowAktion = null;

    /** The auszufuehrende aktion. */
    private int auszufuehrendeAktion = 0;

    /** ++++++++Aktuelles Recht+++++++++++++++. */
    /*AAAAA ServciceDesk noch zu belegen*/
    private int aktuellesRecht = 1;

    /** The ausfuehren aktionen mit aktuellem recht vorhanden. */
    private int ausfuehrenAktionenMitAktuellemRechtVorhanden = 0;

    /** The ausfuehren aktionen mit anderem recht vorhanden. */
    private int ausfuehrenAktionenMitAnderemRechtVorhanden = 0;

    /** The buttons mit aktuellem recht vorhanden. */
    private int buttonsMitAktuellemRechtVorhanden = 0;

    /** The buttons mit anderem recht vorhanden. */
    private int buttonsMitAnderemRechtVorhanden = 0;

    /**
     * ********************StatusDaten des angezeigten
     * Satzes**************************.
     */

    private EclBesitzJeKennung aktuellBesitzJeKennung = null;

    /** The aktuell besitz AR eintrag. */
    private EclBesitzAREintrag aktuellBesitzAREintrag = null;

    /** The aktuell aktienregister eintrag. */
    private EclAktienregister aktuellAktienregisterEintrag = null;

    /**
     * ********************Unklar, wofür dies verwendet wird***********************.
     */

    /*Noch zu prüfen, ob der Fall, bei dem diese Variable verwendet wird, überhaupt
     * jemals auftragen kann (stornierte Meldung ...)
     */
    private boolean aktuellMeldungStorniert = false;

    /*====================================Ab hier alt====================================*/

    /**
     * Wird jeweils gefüllit mit KONST_FUNKTION_* Verwendet wird nur[1] bis [10]!.
     */
    private int funktionButton[] = new int[11];

    /** The funktion select. */
    private int funktionSelect[] = new int[11];

    /**
     * Anzeige erfolgt in Funktionsauswahl, jenachdem welcher Selekt-Button gewählt
     * wurde Verwendet wird nur [1] bis [10].
     */
    private String[] hinweisTextAktion = new String[11];

    /**
     * Clear status daten.
     */
    private void clearStatusDaten() {
        istGast = 0;
        istAktionaer = 0;
        istSammelkarte = 0;
        istAngemeldeterAktionaer = 0;
        istNullbestand = 0;

        istGeradePraesent = 0;
        istGeradeSelbstPraesent = 0;
        istGeradeVertretenPraesent = 0;
        warPraesent = 0;

        istGeradeVirtuellPraesent = 0;
        istGeradeVirtuellSelbstPraesent = 0;
        istGeradeVirtuellVertretenPraesent = 0;
        warVirtuellPraesent = 0;

        istAktionaerInSammelkarte = 0;
        istInBriefwahlsammelkarte = 0;
        istInSRVsammelkarte = 0;
        istInKIAVsammelkarte = 0;
        istInDauervollmachtsammelkarte = 0;
        istInOrgasammelkarte = 0;

        hatEKzugeordnet = 0;
        hatSKzugeordnet = 0;

        hatVollmachtErteilt = 0;

        esGibtStimmkartenZumZuordnen = 0;
        esGibtStimmkartenZumDrucken = 0;

        for (int i = 0; i < 10; i++) {
            wertAntwort[i] = 0;
        }

        /** ab hier alt */
        anzNichtStornierteKarten = 0;
        anzStornierteKarten = 0;
        anzVertreter = 0;
        anzVertreterStorniert = 0;

        aktuellBesitzJeKennung = null;
        aktuellBesitzAREintrag = null;
        aktuellAktienregisterEintrag = null;
        aktuellZugeordneteMeldungNeu = null;
        aktuellMeldung = null;
        aktuellGattung = 0;
        aktuellMeldungStorniert = false;
        aktuellZugeordneteWillenserklaerungenList = null;

        clearStatusFunktionsbereichanzeige();
    }

    /**
     * Clear status funktionsbereichanzeige.
     */
    private void clearStatusFunktionsbereichanzeige() {
        for (int i = 1; i <= 10; i++) {
            funktionButton[i] = 0;
            funktionSelect[i] = 0;
            hinweisTextAktion[i] = "";
            //@formatter:off
            switch (i) {
            case 1:btnButton1.setVisible(false);rbAuswahl1.setVisible(false);break;
            case 2:btnButton2.setVisible(false);rbAuswahl2.setVisible(false);break;
            case 3:btnButton3.setVisible(false);rbAuswahl3.setVisible(false);break;
            case 4:btnButton4.setVisible(false);rbAuswahl4.setVisible(false);break;
            case 5:btnButton5.setVisible(false);rbAuswahl5.setVisible(false);break;
            case 6:btnButton6.setVisible(false);rbAuswahl6.setVisible(false);break;
            case 7:btnButton7.setVisible(false);rbAuswahl7.setVisible(false);break;
            case 8:btnButton8.setVisible(false);rbAuswahl8.setVisible(false);break;
            case 9:btnButton9.setVisible(false);rbAuswahl9.setVisible(false);break;
            case 10:btnButton10.setVisible(false);rbAuswahl10.setVisible(false);break;
            }
            //@formatter:on
        }
        zeigeLabelArt("");
        rbAuswahl1.setSelected(true);
        lblAuswahlText.setText("");

    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tbStandard != null : "fx:id=\"tbStandard\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnFertigStandard != null : "fx:id=\"btnFertigStandard\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton1 != null : "fx:id=\"btnButton1\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton2 != null : "fx:id=\"btnButton2\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton5 != null : "fx:id=\"btnButton5\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton3 != null : "fx:id=\"btnButton3\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton4 != null : "fx:id=\"btnButton4\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton6 != null : "fx:id=\"btnButton6\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton7 != null : "fx:id=\"btnButton7\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton8 != null : "fx:id=\"btnButton8\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton9 != null : "fx:id=\"btnButton9\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnButton10 != null : "fx:id=\"btnButton10\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert scpnStandard != null : "fx:id=\"scpnStandard\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert tbDetail != null : "fx:id=\"tbDetail\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert btnFertigDetail != null : "fx:id=\"btnFertigDetail\" was not injected: check your FXML file 'DetailStage.fxml'.";
        assert scpnDetail != null : "fx:id=\"scpnDetail\" was not injected: check your FXML file 'DetailStage.fxml'.";

        aktiviereTabAuswahl();

        clearStatusDaten();
        /** Anzeigen */
        if (gewaehltAktienregister[0] == null || gewaehltAktienregister[0].aktionaersnummer == null
                || gewaehltAktienregister[0].aktionaersnummer.isEmpty()) {
            /*Kein Aktienregistereintrag vorhanden => Gast oder Sammelkarte*/
            if (gewaehltMeldungen[0].klasse == 0) {
                /*Gast anzeigen*/
                istGast = 1;
            } else {
                /*Sammelkarte anzeigen*/
                istSammelkarte = 1;
            }
        } else {
            /*Aktionär anzeigen*/
            CaBug.druckeLog("Aktionär ausgewählt", logDrucken, 10);
            istAktionaer = 1;
            if (gewaehltMeldungen[0] == null || gewaehltMeldungen[0].aktionaersnummer == null
                    || gewaehltMeldungen[0].aktionaersnummer.isEmpty()) {
                CaBug.druckeLog("Aktionär ohne Anmeldung ausgewählt", logDrucken, 10);
                istAngemeldeterAktionaer = 0;
            } else {
                CaBug.druckeLog("Aktionär mit Anmeldung ausgewählt", logDrucken, 10);
                istAngemeldeterAktionaer = 1;
            }
        }
        datenEinlesenAnzeigenWorkflowElementAuswaehlenUndAnzeigen();
    }

    /**
     * Daten einlesen anzeigen workflow element auswaehlen und anzeigen.
     */
    private void datenEinlesenAnzeigenWorkflowElementAuswaehlenUndAnzeigen() {
        zeigeArea();
        aktionErmittelnUndAnzeigen();
    }

    /**
     * Aktion ermitteln und anzeigen.
     */
    private void aktionErmittelnUndAnzeigen() {
        clearStatusFunktionsbereichanzeige();
        ermittleAktionsanzeige();
        funktionsanzeige();
        zeigeLabelArt(aktuelleServiceDeskWorkflowAktion.anzeigeStatus);
    }

    /**
     * Zeige area.
     */
    private void zeigeArea() {
        if (istGast == 1) {
            /*Gast anzeigen*/
            CaBug.druckeLog("Gast ausgewählt", logDrucken, 10);
            zeigeAreaGast();
        }
        if (istSammelkarte == 1) {
            CaBug.druckeLog("Sammelkarte ausgewählt", logDrucken, 10);
            zeigeAreaSammelkarte();
        }
        if (istAktionaer == 1) {
            /*Aktionär anzeigen*/
            CaBug.druckeLog("Aktionär ausgewählt", logDrucken, 10);
            if (istAngemeldeterAktionaer == 0) {
                zeigeAreaAktionaerOhneMeldung();
            } else {
                CaBug.druckeLog("Aktionär mit Anmeldung ausgewählt", logDrucken, 10);
                zeigeAreaAktionaerMitMeldung();
            }
        }

        /*Abfrage-Fenster füllen*/
        aktiviereTabAuswahl();

    }

    /**
     * Gast-Bereich anzeigen.
     */
    private void zeigeAreaGast() {

        clearStatusDaten();
        istGast = 1;

        /**
         * Status des Gastes abfragen. Erforderlich für Detailanzeige, da "alte" EKs
         * dann auch gefunden werden
         */
        WELoginVerify weLoginVerify = new WELoginVerify();
        weLoginVerify.setKennung(gewaehltMeldungen[0].loginKennung);

        WETeilnehmerStatusClientGet weTeilnehmerStatusClientGet = new WETeilnehmerStatusClientGet();
        weTeilnehmerStatusClientGet.setWeLoginVerify(weLoginVerify);

        WSClient wsClient = new WSClient();
        WETeilnehmerStatusClientGetRC weTeilnehmerStatusClientGetRC = wsClient
                .teilnehmerStatusClientGet(weTeilnehmerStatusClientGet);

        List<EclBesitzJeKennung> besitzJeKennungListe = weTeilnehmerStatusClientGetRC.besitzJeKennungListe;

        aktuellZugeordneteMeldungNeu = besitzJeKennungListe.get(0).zugeordneteMeldungenEigeneGastkartenListe.get(0);
        aktuellMeldung = aktuellZugeordneteMeldungNeu.eclMeldung;

        aktuellZugeordneteWillenserklaerungenList = aktuellZugeordneteMeldungNeu.zugeordneteWillenserklaerungenList;

        hVAnzeige = new VBox(5);

        //        hVAnzeige.setStyle("-fx-border-color:black");

        /*Art der angezeigten Karte*/
        GridPane grpnArt = new GridPane();
        grpnArt.setStyle("-fx-border-color:black");
        grpnArt.setVgap(5);
        grpnArt.setHgap(15);
        grpnArt.setPrefWidth(1450);
        int zeile = 0;
        Label labelArt = new Label("Art:");
        grpnArt.add(labelArt, 0, zeile);
        labelArtInhaltBasis = "Gast";
        zeigeLabelArt("");
        grpnArt.add(labelArtInhalt, 1, zeile);
        hVAnzeige.getChildren().add(grpnArt);

        /*Adressdaten der Gastkarte*/
        GridPane grpnGastAdresse = new GridPane();
        grpnGastAdresse.setStyle("-fx-border-color:black");
        grpnGastAdresse.setVgap(5);
        grpnGastAdresse.setHgap(15);
        grpnGastAdresse.setPrefWidth(1450);
        zeile = 0;

        Label labelName = new Label("Name:");
        grpnGastAdresse.add(labelName, 0, zeile);
        Label inhaltName = new Label(aktuellMeldung.name);
        grpnGastAdresse.add(inhaltName, 1, zeile);

        Label labelVorname = new Label("Vorname:");
        grpnGastAdresse.add(labelVorname, 2, zeile);
        Label inhaltVorname = new Label(aktuellMeldung.vorname);
        grpnGastAdresse.add(inhaltVorname, 3, zeile);
        zeile++;

        Label labelStrasse = new Label("Straße:");
        grpnGastAdresse.add(labelStrasse, 0, zeile);
        Label inhaltStrasse = new Label(aktuellMeldung.strasse);
        grpnGastAdresse.add(inhaltStrasse, 1, zeile);

        Label labelOrt = new Label("Ort:");
        grpnGastAdresse.add(labelOrt, 2, zeile);
        Label inhaltOrt = new Label(aktuellMeldung.ort);
        grpnGastAdresse.add(inhaltOrt, 3, zeile);
        zeile++;

        hVAnzeige.getChildren().add(grpnGastAdresse);

        /*Präsenzstatus der Gastkarte*/
        /*Adressdaten der Gastkarte*/
        GridPane grpnGastPraesenz = new GridPane();
        grpnGastPraesenz.setStyle("-fx-border-color:black");
        grpnGastPraesenz.setVgap(5);
        grpnGastPraesenz.setHgap(15);
        grpnGastPraesenz.setPrefWidth(1450);
        zeile = 0;

        Label labelPraesenz = new Label("Präsenz-Status:");
        grpnGastPraesenz.add(labelPraesenz, 0, zeile);
        String praesenzInhalt = "noch nicht präsent";
        switch (aktuellMeldung.statusPraesenz) {
        case 1:
            praesenzInhalt = "ist anwesend";
            istGeradePraesent = 1;
            istGeradeSelbstPraesent = 1;
            break;
        case 2:
            praesenzInhalt = "war anwesend";
            warPraesent = 1;
            break;
        }
        Label inhaltPraesenz = new Label(praesenzInhalt);
        grpnGastPraesenz.add(inhaltPraesenz, 1, zeile);

        hVAnzeige.getChildren().add(grpnGastPraesenz);

        /*Eintrittskarten der Gastkarte*/

        GridPane grpnGastKarten = new GridPane();
        grpnGastKarten.setStyle("-fx-border-color:black");
        grpnGastKarten.setVgap(5);
        grpnGastKarten.setHgap(15);
        zeile = 0;

        /*Nicht stornierte Karten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert == false) {
                Label labelGastEK = new Label("Gastkarten-Nr:");
                grpnGastKarten.add(labelGastEK, 0, zeile);
                Label inhaltGastEK = new Label(iZugeordneteWillenserklaerungen.zutrittsIdent);
                grpnGastKarten.add(inhaltGastEK, 1, zeile);

                anzNichtStornierteKarten++;

                zeile++;
            }
        }
        if (anzNichtStornierteKarten > 0) {
            hatEKzugeordnet = 1;
        }

        /*Stornierte Karten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert) {
                Label labelGastEK = new Label("Gastkarten-Nr:");
                grpnGastKarten.add(labelGastEK, 0, zeile);
                Label inhaltGastEK = new Label(iZugeordneteWillenserklaerungen.zutrittsIdent);
                grpnGastKarten.add(inhaltGastEK, 1, zeile);
                Label statusGastEK = new Label("storniert");
                grpnGastKarten.add(statusGastEK, 2, zeile);

                anzStornierteKarten++;

                zeile++;
            }
        }

        hVAnzeige.getChildren().add(grpnGastKarten);

        scpnStandard.setContent(hVAnzeige);

    }

    /**
     * Zeige area sammelkarte.
     */
    private void zeigeAreaSammelkarte() {

        clearStatusDaten();
        istSammelkarte = 1;

        /** Status der Sammelkarte abfragen */
        DbBundle lDbBundle = new DbBundle();
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, lDbBundle);

        blSammelkarten.holeSammelkartenDaten(false, gewaehltMeldungen[0].meldungsIdent);
        aktuellSammelMeldung = blSammelkarten.rcSammelMeldung;
        aktuellSammelZutrittskartenArray = blSammelkarten.rcZutrittskartenArray;
        aktuellSammelStimmkartenArray = blSammelkarten.rcStimmkartenArray;
        aktuellSammelWillensErklVollmachtenAnDritteArray = blSammelkarten.rcWillensErklVollmachtenAnDritteArray;

        aktuellGattung = aktuellSammelMeldung[0].liefereGattung();
        hVAnzeige = new VBox(5);

        //        hVAnzeige.setStyle("-fx-border-color:black");

        /*Art der angezeigten Karte*/
        GridPane grpnArt = new GridPane();
        grpnArt.setStyle("-fx-border-color:black");
        grpnArt.setVgap(5);
        grpnArt.setHgap(15);
        grpnArt.setPrefWidth(1450);
        int zeile = 0;
        Label labelArt = new Label("Art:");
        grpnArt.add(labelArt, 0, zeile);
        Label inhaltArt = new Label("Sammelkarte");
        grpnArt.add(inhaltArt, 1, zeile);
        hVAnzeige.getChildren().add(grpnArt);

        /*Grund- und Adressdaten des Aktionärs*/
        GridPane grpnGrundUndAdresseDaten = new GridPane();
        grpnGrundUndAdresseDaten.setStyle("-fx-border-color:black");
        grpnGrundUndAdresseDaten.setVgap(5);
        grpnGrundUndAdresseDaten.setHgap(15);
        grpnGrundUndAdresseDaten.setPrefWidth(1450);
        zeile = 0;

        Label labelNummer = new Label("Sammelkarten-Ident:");
        grpnGrundUndAdresseDaten.add(labelNummer, 0, zeile);
        Label inhaltNummer = new Label(Integer.toString(aktuellSammelMeldung[0].meldungsIdent));
        grpnGrundUndAdresseDaten.add(inhaltNummer, 1, zeile);
        zeile++;

        Label labelName = new Label("Bezeichnung:");
        grpnGrundUndAdresseDaten.add(labelName, 0, zeile);
        Label inhaltName = new Label(aktuellSammelMeldung[0].name);
        grpnGrundUndAdresseDaten.add(inhaltName, 1, zeile);

        Label labelVorname = new Label("Kommentar:");
        grpnGrundUndAdresseDaten.add(labelVorname, 2, zeile);
        Label inhaltVorname = new Label(aktuellSammelMeldung[0].zusatzfeld2);
        grpnGrundUndAdresseDaten.add(inhaltVorname, 3, zeile);
        zeile++;

        Label labelOrt = new Label("Ort:");
        grpnGrundUndAdresseDaten.add(labelOrt, 0, zeile);
        Label inhaltOrt = new Label(aktuellSammelMeldung[0].ort);
        grpnGrundUndAdresseDaten.add(inhaltOrt, 1, zeile);
        zeile++;

        Label labelAktienGesamt = new Label("Aktien:");
        grpnGrundUndAdresseDaten.add(labelAktienGesamt, 0, zeile);
        Label inhaltAktienGesamt = null;
        long aktienGesamt = aktuellSammelMeldung[0].stueckAktien;
        inhaltAktienGesamt = new Label(CaString.toStringDE(aktienGesamt));
        grpnGrundUndAdresseDaten.add(inhaltAktienGesamt, 1, zeile);

        Label labelGattung = new Label("Gattung:");
        grpnGrundUndAdresseDaten.add(labelGattung, 2, zeile);
        Label inhaltGattung = new Label(ParamS.param.paramBasis.getGattungBezeichnung(aktuellGattung));
        grpnGrundUndAdresseDaten.add(inhaltGattung, 3, zeile);
        zeile++;

        hVAnzeige.getChildren().add(grpnGrundUndAdresseDaten);

        /*Präsenzstatus*/
        GridPane grpnPraesenz = new GridPane();
        grpnPraesenz.setStyle("-fx-border-color:black");
        grpnPraesenz.setVgap(5);
        grpnPraesenz.setHgap(15);
        grpnPraesenz.setPrefWidth(1450);
        zeile = 0;

        Label labelPraesenz = new Label("Präsenz-Status:");
        grpnPraesenz.add(labelPraesenz, 0, zeile);
        String praesenzInhalt = "noch nicht präsent";
        switch (aktuellSammelMeldung[0].statusPraesenz) {
        case 1:
            praesenzInhalt = "ist anwesend";
            istGeradePraesent = 1;
            break;
        case 2:
            praesenzInhalt = "war anwesend";
            warPraesent = 1;
            break;
        }
        Label inhaltPraesenz = new Label(praesenzInhalt);
        grpnPraesenz.add(inhaltPraesenz, 1, zeile);

        hVAnzeige.getChildren().add(grpnPraesenz);

        /*Eintrittskarten*/

        GridPane grpnKarten = new GridPane();
        grpnKarten.setStyle("-fx-border-color:black");
        grpnKarten.setVgap(5);
        grpnKarten.setHgap(15);
        zeile = 0;

        /*Nicht stornierte Karten*/
        for (EclZutrittskarten iZutrittskarten : aktuellSammelZutrittskartenArray[0]) {
            if (iZutrittskarten.zutrittsIdentWurdeGesperrt() == false) {
                Label labelEK = new Label("Eintrittskarten-Nr:");
                grpnKarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(iZutrittskarten.zutrittsIdent);
                grpnKarten.add(inhaltEK, 1, zeile);

                anzNichtStornierteKarten++;

                zeile++;
            }
        }
        if (anzNichtStornierteKarten > 0) {
            hatEKzugeordnet = 1;
        }

        /*Stornierte Karten*/
        for (EclZutrittskarten iZutrittskarten : aktuellSammelZutrittskartenArray[0]) {
            if (iZutrittskarten.zutrittsIdentWurdeGesperrt() == true) {
                Label labelEK = new Label("Eintrittskarten-Nr:");
                grpnKarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(iZutrittskarten.zutrittsIdent);
                grpnKarten.add(inhaltEK, 1, zeile);
                Label statusEK = new Label("storniert");
                grpnKarten.add(statusEK, 2, zeile);

                anzStornierteKarten++;
                zeile++;
            }
        }

        hVAnzeige.getChildren().add(grpnKarten);

        zeigeStimmkartenSammelkartenAn();

        /*Vertreter*/

        if (aktuellSammelWillensErklVollmachtenAnDritteArray[0] != null) {
            GridPane grpnVertreter = new GridPane();
            grpnVertreter.setStyle("-fx-border-color:black");
            grpnVertreter.setVgap(5);
            grpnVertreter.setHgap(15);
            zeile = 0;

            /*Nicht stornierte Vertreter*/
            for (EclWillensErklVollmachtenAnDritte iWillensErklVollmachtenAnDritte : aktuellSammelWillensErklVollmachtenAnDritteArray[0]) {
                if (iWillensErklVollmachtenAnDritte.wurdeStorniert == false) {
                    Label labelVertreter = new Label("Bevollmächtigter:");
                    grpnVertreter.add(labelVertreter, 0, zeile);
                    Label inhaltVertreter = new Label(
                            iWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.liefereNameKommaTitelVorname() + ", "
                                    + iWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ort);
                    grpnVertreter.add(inhaltVertreter, 1, zeile);

                    anzVertreter++;

                    zeile++;
                }
            }

            if (anzVertreter > 0) {
                hatVollmachtErteilt = 1;
            }

            /*Stornierte Vertreter*/
            for (EclWillensErklVollmachtenAnDritte iWillensErklVollmachtenAnDritte : aktuellSammelWillensErklVollmachtenAnDritteArray[0]) {
                if (iWillensErklVollmachtenAnDritte.wurdeStorniert == true) {
                    Label labelVertreter = new Label("Bevollmächtigter:");
                    grpnVertreter.add(labelVertreter, 0, zeile);
                    Label inhaltVertreter = new Label(
                            iWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.liefereNameKommaTitelVorname() + ", "
                                    + iWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ort);
                    grpnVertreter.add(inhaltVertreter, 1, zeile);

                    Label statusVertreter = new Label("storniert");
                    grpnVertreter.add(statusVertreter, 2, zeile);

                    anzVertreterStorniert++;

                    zeile++;
                }
            }
            hVAnzeige.getChildren().add(grpnVertreter);
        }

        /*Stimmkarten-Zuordnung belegen*/
        ermittleStimmkartenZuordnenDrucken();

        scpnStandard.setContent(hVAnzeige);
    }

    /**
     * Zeige area aktionaer ohne meldung.
     */
    private void zeigeAreaAktionaerOhneMeldung() {
        clearStatusDaten();
        istAktionaer = 1;

        /**
         * Status der Meldung abfragen. Wobei der Status für den gesamten Aktionär
         * geholt wird
         */
        WELoginVerify weLoginVerify = new WELoginVerify();
        weLoginVerify.setKennung(gewaehltAktienregister[0].aktionaersnummer);

        WETeilnehmerStatusClientGet weTeilnehmerStatusClientGet = new WETeilnehmerStatusClientGet();
        weTeilnehmerStatusClientGet.setWeLoginVerify(weLoginVerify);

        WSClient wsClient = new WSClient();
        WETeilnehmerStatusClientGetRC weTeilnehmerStatusClientGetRC = wsClient
                .teilnehmerStatusClientGet(weTeilnehmerStatusClientGet);

        List<EclBesitzJeKennung> besitzJeKennungListe = weTeilnehmerStatusClientGetRC.besitzJeKennungListe;

        aktuellBesitzJeKennung = null;
        aktuellBesitzAREintrag = null;
        for (EclBesitzJeKennung iEclBesitzJeKennung : besitzJeKennungListe) {
            if (iEclBesitzJeKennung.eigenerAREintragListe != null) {
                for (EclBesitzAREintrag iEclBesitzAREintrag : iEclBesitzJeKennung.eigenerAREintragListe) {
                    if (iEclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent == gewaehltAktienregister[0].aktienregisterIdent) {
                        aktuellBesitzJeKennung = iEclBesitzJeKennung;
                        aktuellBesitzAREintrag = iEclBesitzAREintrag;
                        aktuellAktienregisterEintrag = iEclBesitzAREintrag.aktienregisterEintrag;
                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu : aktuellBesitzAREintrag.zugeordneteMeldungenListe) {
                            if (iEclZugeordneteMeldungNeu.meldungsIdent == gewaehltMeldungen[0].meldungsIdent) {
                                aktuellZugeordneteMeldungNeu = iEclZugeordneteMeldungNeu;
                            }
                        }
                    }
                }
            }
        }
        if (aktuellBesitzJeKennung == null) {
            CaBug.drucke("001");
        }

        aktuellGattung = aktuellAktienregisterEintrag.getGattungId();

        hVAnzeige = new VBox(5);

        //        hVAnzeige.setStyle("-fx-border-color:black");

        GridPane grpnArt = new GridPane();
        grpnArt.setStyle("-fx-border-color:black");
        grpnArt.setVgap(5);
        grpnArt.setHgap(15);
        grpnArt.setPrefWidth(1450);
        int zeile = 0;
        Label labelArt = new Label("Art:");
        grpnArt.add(labelArt, 0, zeile);
        Label inhaltArt = new Label("Nicht-Angemeldeter Aktionär");
        grpnArt.add(inhaltArt, 1, zeile);
        hVAnzeige.getChildren().add(grpnArt);

        /*Grund- und Adressdaten des Aktionärs*/
        GridPane grpnGrundUndAdresseDaten = new GridPane();
        grpnGrundUndAdresseDaten.setStyle("-fx-border-color:black");
        grpnGrundUndAdresseDaten.setVgap(5);
        grpnGrundUndAdresseDaten.setHgap(15);
        grpnGrundUndAdresseDaten.setPrefWidth(1450);
        zeile = 0;

        Label labelNummer = null;
        if (aktuellAktienregisterEintrag.namensInhaberAktien == 1) {
            labelNummer = new Label("Aktionärsnummer:");
        } else {
            labelNummer = new Label("Eintrittskartennummer (bei Anmeldung):");
        }
        grpnGrundUndAdresseDaten.add(labelNummer, 0, zeile);
        Label inhaltNummer = new Label(aktuellAktienregisterEintrag.aktionaersnummer);
        grpnGrundUndAdresseDaten.add(inhaltNummer, 1, zeile);
        zeile++;

        Label labelName = new Label("Name:");
        grpnGrundUndAdresseDaten.add(labelName, 0, zeile);
        Label inhaltName = new Label(aktuellAktienregisterEintrag.liefereName());
        grpnGrundUndAdresseDaten.add(inhaltName, 1, zeile);

        Label labelVorname = new Label("Vorname:");
        grpnGrundUndAdresseDaten.add(labelVorname, 2, zeile);
        Label inhaltVorname = new Label(aktuellAktienregisterEintrag.vorname);
        grpnGrundUndAdresseDaten.add(inhaltVorname, 3, zeile);
        zeile++;

        Label labelStrasse = new Label("Straße:");
        grpnGrundUndAdresseDaten.add(labelStrasse, 0, zeile);
        Label inhaltStrasse = new Label(aktuellAktienregisterEintrag.strasse);
        grpnGrundUndAdresseDaten.add(inhaltStrasse, 1, zeile);

        Label labelOrt = new Label("Ort:");
        grpnGrundUndAdresseDaten.add(labelOrt, 2, zeile);
        Label inhaltOrt = new Label(aktuellAktienregisterEintrag.ort);
        grpnGrundUndAdresseDaten.add(inhaltOrt, 3, zeile);
        zeile++;

        Label labelAktienGesamt = new Label("Aktien gesamt für diesen Aktionärseintrag:");
        grpnGrundUndAdresseDaten.add(labelAktienGesamt, 0, zeile);
        Label inhaltAktienGesamt = null;
        long aktienGesamt = aktuellAktienregisterEintrag.stueckAktien;
        if (aktienGesamt > 0) {
            inhaltAktienGesamt = new Label(CaString.toStringDE(aktienGesamt));
        } else {
            inhaltAktienGesamt = new Label("Nullbestand - keine Aktien!");
            istNullbestand = 1;
        }
        grpnGrundUndAdresseDaten.add(inhaltAktienGesamt, 1, zeile);

        Label labelGattung = new Label("Gattung:");
        grpnGrundUndAdresseDaten.add(labelGattung, 2, zeile);
        Label inhaltGattung = new Label(ParamS.param.paramBasis.getGattungBezeichnung(aktuellGattung));
        grpnGrundUndAdresseDaten.add(inhaltGattung, 3, zeile);
        zeile++;

        hVAnzeige.getChildren().add(grpnGrundUndAdresseDaten);

        scpnStandard.setContent(hVAnzeige);

    }

    /**
     * Zeige area aktionaer mit meldung.
     */
    private void zeigeAreaAktionaerMitMeldung() {
        clearStatusDaten();
        istAktionaer = 1;
        istAngemeldeterAktionaer = 1;

        /**
         * Status der Meldung abfragen. Wobei der Status für den gesamten Aktionär
         * geholt wird
         */
        WELoginVerify weLoginVerify = new WELoginVerify();
        weLoginVerify.setKennung(gewaehltAktienregister[0].aktionaersnummer);

        WETeilnehmerStatusClientGet weTeilnehmerStatusClientGet = new WETeilnehmerStatusClientGet();
        weTeilnehmerStatusClientGet.setWeLoginVerify(weLoginVerify);

        WSClient wsClient = new WSClient();
        WETeilnehmerStatusClientGetRC weTeilnehmerStatusClientGetRC = wsClient
                .teilnehmerStatusClientGet(weTeilnehmerStatusClientGet);

        List<EclBesitzJeKennung> besitzJeKennungListe = weTeilnehmerStatusClientGetRC.besitzJeKennungListe;

        aktuellBesitzJeKennung = null;
        aktuellBesitzAREintrag = null;
        for (EclBesitzJeKennung iEclBesitzJeKennung : besitzJeKennungListe) {
            if (iEclBesitzJeKennung.eigenerAREintragListe != null) {
                for (EclBesitzAREintrag iEclBesitzAREintrag : iEclBesitzJeKennung.eigenerAREintragListe) {
                    if (iEclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent == gewaehltAktienregister[0].aktienregisterIdent) {
                        aktuellBesitzJeKennung = iEclBesitzJeKennung;
                        aktuellBesitzAREintrag = iEclBesitzAREintrag;
                        aktuellAktienregisterEintrag = iEclBesitzAREintrag.aktienregisterEintrag;
                        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu : aktuellBesitzAREintrag.zugeordneteMeldungenListe) {
                            if (iEclZugeordneteMeldungNeu.meldungsIdent == gewaehltMeldungen[0].meldungsIdent) {
                                aktuellZugeordneteMeldungNeu = iEclZugeordneteMeldungNeu;
                            }
                        }
                    }
                }
            }
        }
        if (aktuellBesitzJeKennung == null) {
            CaBug.drucke("001");
        }

        aktuellMeldung = aktuellZugeordneteMeldungNeu.eclMeldung;
        aktuellZugeordneteWillenserklaerungenList = aktuellZugeordneteMeldungNeu.zugeordneteWillenserklaerungenList;

        aktuellGattung = aktuellMeldung.liefereGattung();

        hVAnzeige = new VBox(5);

        //        hVAnzeige.setStyle("-fx-border-color:black");

        GridPane grpnArt = new GridPane();
        grpnArt.setStyle("-fx-border-color:black");
        grpnArt.setVgap(5);
        grpnArt.setHgap(15);
        grpnArt.setPrefWidth(1450);
        int zeile = 0;
        Label labelArt = new Label("Art:");
        grpnArt.add(labelArt, 0, zeile);
        Label inhaltArt = new Label("Angemeldeter Aktionär");
        grpnArt.add(inhaltArt, 1, zeile);
        hVAnzeige.getChildren().add(grpnArt);

        /*Grund- und Adressdaten des Aktionärs*/
        GridPane grpnGrundUndAdresseDaten = new GridPane();
        grpnGrundUndAdresseDaten.setStyle("-fx-border-color:black");
        grpnGrundUndAdresseDaten.setVgap(5);
        grpnGrundUndAdresseDaten.setHgap(15);
        grpnGrundUndAdresseDaten.setPrefWidth(1450);
        zeile = 0;

        Label labelNummer = null;
        if (aktuellAktienregisterEintrag.namensInhaberAktien == 1) {
            labelNummer = new Label("Aktionärsnummer:");
        } else {
            labelNummer = new Label("Eintrittskartennummer (bei Anmeldung):");
        }
        grpnGrundUndAdresseDaten.add(labelNummer, 0, zeile);
        Label inhaltNummer = new Label(aktuellAktienregisterEintrag.aktionaersnummer);
        grpnGrundUndAdresseDaten.add(inhaltNummer, 1, zeile);
        zeile++;

        Label labelName = new Label("Name:");
        grpnGrundUndAdresseDaten.add(labelName, 0, zeile);
        Label inhaltName = new Label(aktuellAktienregisterEintrag.liefereName());
        grpnGrundUndAdresseDaten.add(inhaltName, 1, zeile);

        Label labelVorname = new Label("Vorname:");
        grpnGrundUndAdresseDaten.add(labelVorname, 2, zeile);
        Label inhaltVorname = new Label(aktuellAktienregisterEintrag.vorname);
        grpnGrundUndAdresseDaten.add(inhaltVorname, 3, zeile);
        zeile++;

        Label labelStrasse = new Label("Straße:");
        grpnGrundUndAdresseDaten.add(labelStrasse, 0, zeile);
        Label inhaltStrasse = new Label(aktuellAktienregisterEintrag.strasse);
        grpnGrundUndAdresseDaten.add(inhaltStrasse, 1, zeile);

        Label labelOrt = new Label("Ort:");
        grpnGrundUndAdresseDaten.add(labelOrt, 2, zeile);
        Label inhaltOrt = new Label(aktuellAktienregisterEintrag.ort);
        grpnGrundUndAdresseDaten.add(inhaltOrt, 3, zeile);
        zeile++;

        Label labelAktienGesamt = new Label("Aktien gesamt für diesen Aktionärseintrag:");
        grpnGrundUndAdresseDaten.add(labelAktienGesamt, 0, zeile);
        Label inhaltAktienGesamt = null;
        long aktienGesamt = aktuellAktienregisterEintrag.stueckAktien;
        if (aktienGesamt > 0) {
            inhaltAktienGesamt = new Label(CaString.toStringDE(aktienGesamt));
        } else {
            inhaltAktienGesamt = new Label("Nullbestand - keine Aktien!");
            istNullbestand = 1;
        }
        grpnGrundUndAdresseDaten.add(inhaltAktienGesamt, 1, zeile);

        Label labelGattung = new Label("Gattung:");
        grpnGrundUndAdresseDaten.add(labelGattung, 2, zeile);
        Label inhaltGattung = new Label(
                ParamS.param.paramBasis.getGattungBezeichnung(aktuellAktienregisterEintrag.getGattungId()));
        grpnGrundUndAdresseDaten.add(inhaltGattung, 3, zeile);
        zeile++;

        Label labelAktienMeldung = new Label("Aktien dieser Meldung:");
        grpnGrundUndAdresseDaten.add(labelAktienMeldung, 0, zeile);
        Label inhaltAktienMeldung = null;
        long aktienMeldung = aktuellMeldung.stueckAktien;
        aktuellMeldungStorniert = (aktuellMeldung.meldungAktiv == 2);
        if (aktuellMeldungStorniert) {
            inhaltAktienMeldung = new Label("Meldung ist storniert!");
        } else {
            if (aktienMeldung > 0) {
                inhaltAktienMeldung = new Label(CaString.toStringDE(aktienMeldung));
            } else {
                inhaltAktienMeldung = new Label("Nullbestand - keine Aktien!");
                aktuellMeldungStorniert = true;
            }
        }
        grpnGrundUndAdresseDaten.add(inhaltAktienMeldung, 1, zeile);
        zeile++;

        hVAnzeige.getChildren().add(grpnGrundUndAdresseDaten);

        if (aktuellMeldung.meldungEnthaltenInSammelkarte > 0) {
            /*In Sammelkarte*/
            istAktionaerInSammelkarte = 1;

            /*Weisungs-/Sammelkartenstatus*/
            GridPane grpnSammelStatus = new GridPane();
            grpnSammelStatus.setStyle("-fx-border-color:black");
            grpnSammelStatus.setVgap(5);
            grpnSammelStatus.setHgap(15);
            grpnSammelStatus.setPrefWidth(1450);
            zeile = 0;

            Label labelSammel = new Label("Meldung ist in Sammelkarte:");
            grpnSammelStatus.add(labelSammel, 0, zeile);
            String sammelInhalt = "";
            switch (aktuellMeldung.meldungEnthaltenInSammelkarteArt) {
            case KonstSkIst.kiav:
                sammelInhalt = "KIAV (Bank/Schutzvereinigung)";
                istInKIAVsammelkarte = 1;
                break;
            case KonstSkIst.srv:
                sammelInhalt = "Stimmrechtsvertreter (vor der HV)";
                istInSRVsammelkarte = 1;
                break;
            case KonstSkIst.srvHV:
                sammelInhalt = "Stimmrechtsvertreter (beim Verlasssen der HV)";
                istInSRVsammelkarte = 1;
                break;
            case KonstSkIst.organisatorisch:
                sammelInhalt = "Organisatorisch";
                istInOrgasammelkarte = 1;
                break;
            case KonstSkIst.briefwahl:
                sammelInhalt = "Briefwahl";
                istInBriefwahlsammelkarte = 1;
                break;
            case KonstSkIst.dauervollmacht:
                sammelInhalt = "Dauervollmacht (Bank)";
                istInDauervollmachtsammelkarte = 1;
                break;
            }
            Label inhaltSammel = new Label(sammelInhalt);
            grpnSammelStatus.add(inhaltSammel, 1, zeile);

            hVAnzeige.getChildren().add(grpnSammelStatus);
        } else {
            zeigePraesenzstatusAn();
        }

        zeigeEintrittskartenAn();
        zeigeStimmkartenAn();
        zeigeVertreterAn();

        scpnStandard.setContent(hVAnzeige);

        ermittleStimmkartenZuordnenDrucken();

    }

    /**
     * Zeige praesenzstatus an.
     */
    private void zeigePraesenzstatusAn() {
        /*Präsenzstatus*/
        GridPane grpnPraesenz = new GridPane();
        grpnPraesenz.setStyle("-fx-border-color:black");
        grpnPraesenz.setVgap(5);
        grpnPraesenz.setHgap(15);
        grpnPraesenz.setPrefWidth(1450);
        int zeile = 0;

        Label labelPraesenz = new Label("Präsenz-Status:");
        grpnPraesenz.add(labelPraesenz, 0, zeile);
        String praesenzInhalt = "noch nicht präsent";
        switch (aktuellMeldung.statusPraesenz) {
        case 1:
            praesenzInhalt = "ist anwesend";
            istGeradePraesent = 1;
            if (aktuellMeldung.vertreterIdent != 0) {
                istGeradeVertretenPraesent = 1;
            } else {
                istGeradeSelbstPraesent = 1;
            }
            break;
        case 2:
            praesenzInhalt = "war anwesend";
            warPraesent = 1;
            break;
        }
        Label inhaltPraesenz = new Label(praesenzInhalt);
        grpnPraesenz.add(inhaltPraesenz, 1, zeile);

        hVAnzeige.getChildren().add(grpnPraesenz);

    }

    /**
     * Zeige eintrittskarten an.
     */
    private void zeigeEintrittskartenAn() {
        /*Eintrittskarten*/

        GridPane grpnKarten = new GridPane();
        grpnKarten.setStyle("-fx-border-color:black");
        grpnKarten.setVgap(5);
        grpnKarten.setHgap(15);
        int zeile = 0;

        /*Nicht stornierte Karten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert == false) {
                Label labelEK = new Label("Eintrittskarten-Nr:");
                grpnKarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(iZugeordneteWillenserklaerungen.zutrittsIdent);
                grpnKarten.add(inhaltEK, 1, zeile);

                if (anzNichtStornierteKarten == 0) {
                    ersteEKNr = iZugeordneteWillenserklaerungen.zutrittsIdent;
                    ersteEKNrNeben = iZugeordneteWillenserklaerungen.zutrittsIdentNeben;
                }
                anzNichtStornierteKarten++;

                zeile++;
            }
        }
        if (anzNichtStornierteKarten > 0) {
            hatEKzugeordnet = 1;
        }
        /*Stornierte Karten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert) {
                Label labelEK = new Label("Eintrittskarten-Nr:");
                grpnKarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(iZugeordneteWillenserklaerungen.zutrittsIdent);
                grpnKarten.add(inhaltEK, 1, zeile);
                Label statusEK = new Label("storniert");
                grpnKarten.add(statusEK, 2, zeile);

                anzStornierteKarten++;

                zeile++;
            }
        }

        hVAnzeige.getChildren().add(grpnKarten);

    }

    /**
     * Zeige stimmkarten sammelkarten an.
     */
    private void zeigeStimmkartenSammelkartenAn() {
        /*Stimmkarten*/

        GridPane grpnStimmkarten = new GridPane();
        grpnStimmkarten.setStyle("-fx-border-color:black");
        grpnStimmkarten.setVgap(5);
        grpnStimmkarten.setHgap(15);
        int zeile = 0;

        /*Nicht stornierte Stimmkarten*/
        for (EclStimmkarten iStimmkarten : aktuellSammelStimmkartenArray[0]) {
            if (iStimmkarten.stimmkarteWurdeGesperrt() == false) {
                Label labelEK = new Label("Stimmkarten-Nr:");
                grpnStimmkarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(
                        iStimmkarten.stimmkarte);
                grpnStimmkarten.add(inhaltEK, 1, zeile);

                anzNichtStornierteStimmkarten++;

                zeile++;
            }
        }
        if (anzNichtStornierteStimmkarten > 0) {
            hatSKzugeordnet = 1;
        }
        /*Stornierte Stimmkarten*/
        for (EclStimmkarten iStimmkarten : aktuellSammelStimmkartenArray[0]) {
            if (iStimmkarten.stimmkarteWurdeGesperrt() == true) {
                Label labelEK = new Label("Stimmkarten-Nr:");
                grpnStimmkarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(
                        iStimmkarten.stimmkarte);
                grpnStimmkarten.add(inhaltEK, 1, zeile);
                Label statusEK = new Label("storniert");
                grpnStimmkarten.add(statusEK, 2, zeile);

                anzStornierteStimmkarten++;

                zeile++;
            }
        }

        hVAnzeige.getChildren().add(grpnStimmkarten);

    }

    /**
     * Zeige stimmkarten an.
     */
    private void zeigeStimmkartenAn() {
        /*Stimmkarten*/

        GridPane grpnStimmkarten = new GridPane();
        grpnStimmkarten.setStyle("-fx-border-color:black");
        grpnStimmkarten.setVgap(5);
        grpnStimmkarten.setHgap(15);
        int zeile = 0;

        /*Nicht stornierte Stimmkarten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueStimmkarteZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert == false) {
                Label labelEK = new Label("Stimmkarten-Nr:");
                grpnStimmkarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(
                        iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte1 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte2 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte3 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte4 + " ");
                grpnStimmkarten.add(inhaltEK, 1, zeile);

                anzNichtStornierteStimmkarten++;

                zeile++;
            }
        }
        if (anzNichtStornierteStimmkarten > 0) {
            hatSKzugeordnet = 1;
        }
        /*Stornierte Stimmkarten*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.neueStimmkarteZuMeldung
                    && iZugeordneteWillenserklaerungen.storniert) {
                Label labelEK = new Label("Stimmkarten-Nr:");
                grpnStimmkarten.add(labelEK, 0, zeile);
                Label inhaltEK = new Label(
                        iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte1 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte2 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte3 + " " +
                                iZugeordneteWillenserklaerungen.eclWillenserklaerung.stimmkarte4 + " ");
                grpnStimmkarten.add(inhaltEK, 1, zeile);
                Label statusEK = new Label("storniert");
                grpnStimmkarten.add(statusEK, 2, zeile);

                anzStornierteStimmkarten++;

                zeile++;
            }
        }

        hVAnzeige.getChildren().add(grpnStimmkarten);

    }

    /**
     * Zeige vertreter an.
     */
    private void zeigeVertreterAn() {
        /*Vertreter*/

        GridPane grpnVertreter = new GridPane();
        grpnVertreter.setStyle("-fx-border-color:black");
        grpnVertreter.setVgap(5);
        grpnVertreter.setHgap(15);
        int zeile = 0;

        /*Nicht stornierte Vertreter*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.vollmachtAnDritte
                    && iZugeordneteWillenserklaerungen.storniert == false) {
                Label labelVertreter = new Label("Bevollmächtigter:");
                grpnVertreter.add(labelVertreter, 0, zeile);
                Label inhaltVertreter = new Label(
                        iZugeordneteWillenserklaerungen.eclPersonenNatJurVertreter.liefereNameKommaTitelVorname() + ", "
                                + iZugeordneteWillenserklaerungen.eclPersonenNatJurVertreter.ort);
                grpnVertreter.add(inhaltVertreter, 1, zeile);

                anzVertreter++;

                zeile++;
            }
        }
        if (anzVertreter > 0) {
            hatVollmachtErteilt = 1;
        }
        /*Stornierte Vertreter*/
        for (EclWillenserklaerungStatusNeu iZugeordneteWillenserklaerungen : aktuellZugeordneteWillenserklaerungenList) {
            if (iZugeordneteWillenserklaerungen.willenserklaerung == KonstWillenserklaerung.vollmachtAnDritte
                    && iZugeordneteWillenserklaerungen.storniert) {
                Label labelVertreter = new Label("Bevollmächtigter:");
                grpnVertreter.add(labelVertreter, 0, zeile);
                Label inhaltVertreter = new Label(
                        iZugeordneteWillenserklaerungen.eclPersonenNatJurVertreter.liefereNameKommaTitelVorname() + ", "
                                + iZugeordneteWillenserklaerungen.eclPersonenNatJurVertreter.ort);
                grpnVertreter.add(inhaltVertreter, 1, zeile);

                Label statusVertreter = new Label("storniert");
                grpnVertreter.add(statusVertreter, 2, zeile);

                anzVertreterStorniert++;

                zeile++;
            }
        }
        hVAnzeige.getChildren().add(grpnVertreter);

    }

    /**
     * Zeige label art.
     *
     * @param pAnzeigeStatus the anzeige status
     */
    private void zeigeLabelArt(String pAnzeigeStatus) {
        String hInhalt = labelArtInhaltBasis;
        if (!pAnzeigeStatus.trim().isEmpty()) {
            hInhalt = hInhalt + "; " + pAnzeigeStatus;
        }
        labelArtInhalt.setText(hInhalt);
        labelArtInhalt.setWrapText(true);
    }

    /**
     * Ermittle stimmkarten zuordnen drucken.
     */
    private void ermittleStimmkartenZuordnenDrucken() {
        if (ParamS.param.paramAkkreditierung.stimmkartenZumZuordnenVorhanden(aktuellGattung)) {
            esGibtStimmkartenZumZuordnen = 1;
        }
        if (ParamS.param.paramAkkreditierung.stimmkartenZumDruckenAmServiceDeskVorhanden(aktuellGattung)) {
            esGibtStimmkartenZumDrucken = 1;
        }
    }

    /**
     * Ermittle aktionsanzeige.
     */
    private void ermittleAktionsanzeige() {
        List<EclServiceDeskWorkflow> serviceDeskWorkflowList = ParamS.paramServer
                .liefereServiceDeskWorkflowZuSet(ParamS.param.paramAkkreditierung.serviceDeskSetNr);
        aktuelleServiceDeskWorkflowAktion = null;
        for (EclServiceDeskWorkflow iServiceDeskWorkflow : serviceDeskWorkflowList) {
            CaBug.druckeLog("Prüfe Aktions-Nummer " + iServiceDeskWorkflow.ident, logDrucken, 10);
            if (pruefeObWorkflowAktionErfuellt(iServiceDeskWorkflow)) {
                aktuelleServiceDeskWorkflowAktion = iServiceDeskWorkflow;
                return;
            }
        }
    }

    /**
     * Pruefe ob workflow aktion erfuellt.
     *
     * @param pEclServiceDeskWorkflow the ecl service desk workflow
     * @return true, if successful
     */
    private boolean pruefeObWorkflowAktionErfuellt(EclServiceDeskWorkflow pEclServiceDeskWorkflow) {
        if (pEclServiceDeskWorkflow.istAktiv == -1) {
            return false;
        }
        CaBug.druckeLog("WorkflowAktion ist aktiv", logDrucken, 10);
        CaBug.druckeLog("Bedingungen: istGast=" + istGast, logDrucken, 10);
        //      @formatter:off
        if (pruefeWert(pEclServiceDeskWorkflow.istGast, istGast)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istAktionaer, istAktionaer)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istSammelkarte, istSammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istAngemeldeterAktionaer, istAngemeldeterAktionaer)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istNullbestand, istNullbestand)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradePraesent, istGeradePraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradeSelbstPraesent, istGeradeSelbstPraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradeVertretenPraesent, istGeradeVertretenPraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.warPraesent, warPraesent)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradeVirtuellPraesent, istGeradeVirtuellPraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradeVirtuellSelbstPraesent, istGeradeVirtuellSelbstPraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istGeradeVirtuellVertretenPraesent, istGeradeVirtuellVertretenPraesent)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.warVirtuellPraesent, warVirtuellPraesent)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.istAktionaerInSammelkarte, istAktionaerInSammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istInBriefwahlsammelkarte, istInBriefwahlsammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istInSRVsammelkarte, istInSRVsammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istInKIAVsammelkarte, istInKIAVsammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istInDauervollmachtsammelkarte, istInDauervollmachtsammelkarte)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.istInOrgasammelkarte, istInOrgasammelkarte)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.hatEKzugeordnet, hatEKzugeordnet)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.hatSKzugeordnet, hatSKzugeordnet)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.hatVollmachtErteilt, hatVollmachtErteilt)==false) {return false;}
        
        if (pruefeWert(pEclServiceDeskWorkflow.esGibtStimmkartenZumZuordnen, esGibtStimmkartenZumZuordnen)==false) {return false;}
        if (pruefeWert(pEclServiceDeskWorkflow.esGibtStimmkartenZumDrucken, esGibtStimmkartenZumDrucken)==false) {return false;}
 
        CaBug.druckeLog("Nun noch Antwortstatus überprüfen", logDrucken, 10);
        for (int i=0;i<10;i++) {
            if (pruefeWertAntwort(pEclServiceDeskWorkflow.wertAntwort[i], wertAntwort[i])==false) {return false;}
        }
       //     @formatter:on
        CaBug.druckeLog("Return True", logDrucken, 10);
        return true;
    }

    /**
     * Pruefe wert.
     *
     * @param pWertAusWorkflowAktion the wert aus workflow aktion
     * @param pLokalerWert           the lokaler wert
     * @return true, if successful
     */
    private boolean pruefeWert(int pWertAusWorkflowAktion, int pLokalerWert) {
        if (pWertAusWorkflowAktion == 0) {
            return true;
        }
        if (pWertAusWorkflowAktion == 1 && pLokalerWert != 1) {
            return false;
        }
        if (pWertAusWorkflowAktion == -1 && pLokalerWert == 1) {
            return false;
        }
        return true;
    }

    /**
     * Pruefe wert antwort.
     *
     * @param pWertAusWorkflowAktion the wert aus workflow aktion
     * @param pLokalerWert           the lokaler wert
     * @return true, if successful
     */
    private boolean pruefeWertAntwort(int pWertAusWorkflowAktion, int pLokalerWert) {
        CaBug.druckeLog("pWertAusWorkflowAktion=" + pWertAusWorkflowAktion + " pLokalerWert=" + pLokalerWert,
                logDrucken, 10);
        if (pWertAusWorkflowAktion == 0) {
            return true;
        }
        if (pWertAusWorkflowAktion > 0 && pLokalerWert != pWertAusWorkflowAktion) {
            return false;
        }
        if (pWertAusWorkflowAktion == -1 && pLokalerWert != 0) {
            return false;
        }
        return true;
    }

    /**
     * Funktionsanzeige.
     */
    private void funktionsanzeige() {
        if (aktuelleServiceDeskWorkflowAktion == null) {
            CaBug.druckeLog("Keine Aktion gefunden", logDrucken, 10);
            aktiviereNurLabel("Keine Aktionen verfügbar - bitte ggf. Coach verständigen");
            return;
        }
        CaBug.druckeLog("Aktion gefunden mit Ident=" + aktuelleServiceDeskWorkflowAktion.ident, logDrucken, 10);
        if (aktuelleServiceDeskWorkflowAktion.frageInAntwortSchreiben != -1) {
            /** Zwischenfrage stellen */
            CaBug.druckeLog("Zwischenfrage stellen", logDrucken, 10);
            for (int i = 0; i < 5; i++) {
                if (aktuelleServiceDeskWorkflowAktion.frage[i].trim().isEmpty() == false) {
                    CaBug.druckeLog("aktiviereFunktionSelektion i=" + i, logDrucken, 10);
                    //@formatter:off
                    aktiviereFunktionSelektion(i+1,
                            i+1, 
                            aktuelleServiceDeskWorkflowAktion.frage[i],
                            aktuelleServiceDeskWorkflowAktion.frageEinleitungsText
                            );
                    //@formatter:on
                }
            }
        } else {
            if (aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrage > 0) {
                /** Ausführen-Aktion anzeigen */
                CaBug.druckeLog("AusführenAktion anzeigen", logDrucken, 10);
                ausfuehrenAktionenMitAktuellemRechtVorhanden = 0;
                ausfuehrenAktionenMitAnderemRechtVorhanden = 0;

                for (int i = 0; i < 10; i++) {
                    if (aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrageFunktion[i] != 0) {
                        if (pruefeObBerechtigt(
                                aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrageErforderlichesRecht[i])) {
                           //@formatter:off
                           aktiviereFunktionSelektion(aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrageFunktion[i],
                                   i+1, 
                                   aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrageText[i],
                                   aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrageHinweis[i]
                                   );
                           //@formatter:on
                            ausfuehrenAktionenMitAktuellemRechtVorhanden++;
                        } else {
                            ausfuehrenAktionenMitAnderemRechtVorhanden++;
                        }
                    }
                }
            }
        }

        /*Nun noch Buttons anzeigen*/
        buttonsMitAktuellemRechtVorhanden = 0;
        buttonsMitAnderemRechtVorhanden = 0;

        for (int i = 0; i < 10; i++) {
            if (aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonFunktion[i] != 0) {
                if (pruefeObBerechtigt(aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonErforderlichesRecht[i])) {
                   //@formatter:off
                   aktiviereFunktionsButton(aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonFunktion[i],
                           i+1, 
                           aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonText[i],
                           aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonHinweis[i]
                           );
                   //@formatter:on
                    buttonsMitAktuellemRechtVorhanden++;
                } else {
                    buttonsMitAnderemRechtVorhanden++;
                }
            }
        }

    }

    /**
     * Pruefe ob berechtigt.
     *
     * @param pErforderlichesRecht the erforderliches recht
     * @return true, if successful
     */
    private boolean pruefeObBerechtigt(int pErforderlichesRecht) {
        /*AAAAAA Rechte-Management noch zu implementieren*/
        return true;
        //        if (pErforderlichesRecht==aktuellesRecht) {return true;}
        //        return false;
    }

    /**
     * Aktiviere funktion selektion.
     *
     * @param pFunktionsauswahl the funktionsauswahl
     * @param pFunktionsNr      the funktions nr
     * @param pSelectText       the select text
     * @param pHinweisText      the hinweis text
     */
    private void aktiviereFunktionSelektion(int pFunktionsauswahl,
            int pFunktionsNr, String pSelectText, String pHinweisText) {

        funktionSelect[pFunktionsNr] = pFunktionsauswahl;
        hinweisTextAktion[pFunktionsNr] = pHinweisText;
        //@formatter:off
        switch (pFunktionsNr) {
        case 1:rbAuswahl1.setVisible(true);rbAuswahl1.setText(pSelectText);break;
        case 2:rbAuswahl2.setVisible(true);rbAuswahl2.setText(pSelectText);break;
        case 3:rbAuswahl3.setVisible(true);rbAuswahl3.setText(pSelectText);break;
        case 4:rbAuswahl4.setVisible(true);rbAuswahl4.setText(pSelectText);break;
        case 5:rbAuswahl5.setVisible(true);rbAuswahl5.setText(pSelectText);break;
        case 6:rbAuswahl6.setVisible(true);rbAuswahl6.setText(pSelectText);break;
        case 7:rbAuswahl7.setVisible(true);rbAuswahl7.setText(pSelectText);break;
        case 8:rbAuswahl8.setVisible(true);rbAuswahl8.setText(pSelectText);break;
        case 9:rbAuswahl9.setVisible(true);rbAuswahl9.setText(pSelectText);break;
        case 10:rbAuswahl10.setVisible(true);rbAuswahl10.setText(pSelectText);break;
        }
        //@formatter:on
        if (pFunktionsNr == 1) {
            lblAuswahlText.setText(hinweisTextAktion[pFunktionsNr]);
        }
    }

    /**
     * Aktiviere funktions button.
     *
     * @param pFunktionsauswahl the funktionsauswahl
     * @param pButtonNr         the button nr
     * @param pButtonText       the button text
     * @param pButtonHinweis    the button hinweis
     */
    private void aktiviereFunktionsButton(int pFunktionsauswahl,
            int pButtonNr, String pButtonText, String pButtonHinweis) {

        funktionButton[pButtonNr] = pFunktionsauswahl;
        //@formatter:off
        switch (pButtonNr) {
        case 1: btnButton1.setText(pButtonText);btnButton1.setVisible(true);ttButton1.setText(pButtonHinweis);break;
        case 2: btnButton2.setText(pButtonText);btnButton2.setVisible(true);ttButton2.setText(pButtonHinweis);break;
        case 3: btnButton3.setText(pButtonText);btnButton3.setVisible(true);ttButton3.setText(pButtonHinweis);break;
        case 4: btnButton4.setText(pButtonText);btnButton4.setVisible(true);ttButton4.setText(pButtonHinweis);break;
        case 5: btnButton5.setText(pButtonText);btnButton5.setVisible(true);ttButton5.setText(pButtonHinweis);break;
        case 6: btnButton6.setText(pButtonText);btnButton6.setVisible(true);ttButton6.setText(pButtonHinweis);break;
        case 7: btnButton7.setText(pButtonText);btnButton7.setVisible(true);ttButton7.setText(pButtonHinweis);break;
        case 8: btnButton8.setText(pButtonText);btnButton8.setVisible(true);ttButton8.setText(pButtonHinweis);break;
        case 9: btnButton9.setText(pButtonText);btnButton9.setVisible(true);ttButton9.setText(pButtonHinweis);break;
        case 10: btnButton10.setText(pButtonText);btnButton10.setVisible(true);ttButton10.setText(pButtonHinweis);break;
        }
        //@formatter:on

    }

    /**
     * Aktiviere nur label.
     *
     * @param pHinweisText the hinweis text
     */
    private void aktiviereNurLabel(String pHinweisText) {
        lblAuswahlText.setText(pHinweisText);
    }

    /**
     * Aktiviere tab auswahl.
     */
    private void aktiviereTabAuswahl() {
        tbAuswahl.setDisable(false);
        tbGastkarte.setDisable(true);

        tpVerarbeitung.getSelectionModel().select(0);

    }

    /**
     * On btn fertig standard.
     *
     * @param event the event
     */
    /*=====================================Oberflächen-Events==========================================*/
    @FXML
    void onBtnFertigStandard(ActionEvent event) {

        eigeneStage.hide();
    }

    /**
     * On rb auswahl action.
     *
     * @param event the event
     */
    @FXML
    void onRbAuswahlAction(ActionEvent event) {
        if (rbAuswahl1.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[1]);
        }
        if (rbAuswahl2.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[2]);
        }
        if (rbAuswahl3.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[3]);
        }
        if (rbAuswahl4.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[4]);
        }
        if (rbAuswahl5.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[5]);
        }
        if (rbAuswahl6.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[6]);
        }
        if (rbAuswahl7.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[7]);
        }
        if (rbAuswahl8.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[8]);
        }
        if (rbAuswahl9.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[9]);
        }
        if (rbAuswahl10.isSelected()) {
            lblAuswahlText.setText(hinweisTextAktion[10]);
        }
    }

    /**
     * Die über RadioButtons (rbAuswahl*) ausgewählte FUnktion wird ausgeführt.
     *
     * @param event the event
     */
    @FXML
    void onBtnAuswahlWeiter(ActionEvent event) {
        if (aktuelleServiceDeskWorkflowAktion == null) {
            eigeneStage.hide();
            return;
        }

        if (aktuelleServiceDeskWorkflowAktion.ausfuehrenAbfrage == -1 &&
                aktuelleServiceDeskWorkflowAktion.frageInAntwortSchreiben == -1) {
            eigeneStage.hide();
        }

        int ausgewaehlteAktion = 0;
//      @formatter:off
        if (rbAuswahl1.isSelected()) {ausgewaehlteAktion=1;}
        if (rbAuswahl2.isSelected()) {ausgewaehlteAktion=2;}
        if (rbAuswahl3.isSelected()) {ausgewaehlteAktion=3;}
        if (rbAuswahl4.isSelected()) {ausgewaehlteAktion=4;}
        if (rbAuswahl5.isSelected()) {ausgewaehlteAktion=5;}
        if (rbAuswahl6.isSelected()) {ausgewaehlteAktion=6;}
        if (rbAuswahl7.isSelected()) {ausgewaehlteAktion=7;}
        if (rbAuswahl8.isSelected()) {ausgewaehlteAktion=8;}
        if (rbAuswahl9.isSelected()) {ausgewaehlteAktion=9;}
        if (rbAuswahl10.isSelected()) {ausgewaehlteAktion=10;}
//      @formatter:on

        if (aktuelleServiceDeskWorkflowAktion.frageInAntwortSchreiben != -1) {
            /*Frage mit Fortsetzung*/
            wertAntwort[aktuelleServiceDeskWorkflowAktion.frageInAntwortSchreiben] = ausgewaehlteAktion;
            aktionErmittelnUndAnzeigen();
            return;
        }

        /*Aktion ausführen*/
        auszufuehrendeAktion = aktuelleServiceDeskWorkflowAktion.ausfuehrenButtonFunktion[ausgewaehlteAktion - 1];

        boolean brc = ausfuehrenAktion();
        if (brc == false) {
            return;
        }
        datenEinlesenAnzeigenWorkflowElementAuswaehlenUndAnzeigen();
        return;

    }

    /**
     * On btn button 1.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton1(ActionEvent event) {
        ausfuehrenButton(1);
    }

    /**
     * On btn button 2.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton2(ActionEvent event) {
        ausfuehrenButton(2);
    }

    /**
     * On btn button 3.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton3(ActionEvent event) {
        ausfuehrenButton(3);
    }

    /**
     * On btn button 4.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton4(ActionEvent event) {
        ausfuehrenButton(4);
    }

    /**
     * On btn button 5.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton5(ActionEvent event) {
        ausfuehrenButton(5);
    }

    /**
     * On btn button 6.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton6(ActionEvent event) {
        ausfuehrenButton(6);
    }

    /**
     * On btn button 7.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton7(ActionEvent event) {
        ausfuehrenButton(7);
    }

    /**
     * On btn button 8.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton8(ActionEvent event) {
        ausfuehrenButton(8);
    }

    /**
     * On btn button 9.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton9(ActionEvent event) {
        ausfuehrenButton(9);
    }

    /**
     * On btn button 10.
     *
     * @param event the event
     */
    @FXML
    void onBtnButton10(ActionEvent event) {
        ausfuehrenButton(10);
    }

    /**
     * Ausfuehren button.
     *
     * @param pButtonNr the button nr
     */
    private void ausfuehrenButton(int pButtonNr) {
        auszufuehrendeAktion = funktionButton[pButtonNr];
        ausfuehrenAktion();

    }

    /**
     * Ausfuehren aktion.
     *
     * @return true, if successful
     */
    private boolean ausfuehrenAktion() {
        CaBug.druckeLog("Funktion ausführen:" + auszufuehrendeAktion, logDrucken, 10);
        switch (auszufuehrendeAktion) {
        case KonstServicedesk.FUNKTION_GAST_GASTKARTENDRUCK_WIEDERHOLEN:
            funktionGastGastkartendruckWiederholen();
            break;
        case KonstServicedesk.KONST_FUNKTION_EK_DRUCK_WIEDERHOLEN:
            funktionEKDruckWiederholen();
            break;
        case KonstServicedesk.KONST_FUNKTION_SK_DRUCK_ERSTZUGANG_WIEDERHOLEN:
            funktionSKDruckErstzugangWiederholen();
            break;
        }
        /*AAAAA ServiceDesk*/
        return false;

        //        switch (aktiveFunktionsauswahl) {
        //        case KONST_FUNKTIONSAUSWAHL_GASTKARTE_PRAESENT:
        //        case KONST_FUNKTIONSAUSWAHL_GASTKARTE_NICHT_PRAESENT:
        //            switch (lFunktionsnummer) {
        //            case KONST_FUNKTION_ERSATZKARTE_GAST_AUSSTELLEN:
        //                ausfuehrenStornoBisherigeUndAusstellenErsatzGastkarte();
        //                return;
        //            case KONST_FUNKTION_GASTKARTE_FUER_GAST_AUSSTELLEN:
        //                ausfuehrenZusaetzlicheGastkarte();
        //                return;
        //            }
        //        }

    }

    /**
     * Funktion gast gastkartendruck wiederholen.
     */
    private void funktionGastGastkartendruckWiederholen() {
        DbBundle lDbBundle = new DbBundle();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();

        BlGastkarte blGastkarte = new BlGastkarte(lDbBundle);

        blGastkarte.initRpDrucken(rpDrucken);

        blGastkarte.rcDruckenInPDF = false;

        blGastkarte.pVersandart = 5;

        blGastkarte.rcZutrittsIdent = aktuellMeldung.zutrittsIdent;
        blGastkarte.rcZutrittsIdentNeben = "00";

        blGastkarte.pVersandadresse = new String[1];
        blGastkarte.pVersandadresse[0] = "";

        blGastkarte.pGast = aktuellMeldung;
        blGastkarte.drucken(lDbBundle);
        return;

    }

    /**
     * Funktion EK druck wiederholen.
     */
    private void funktionEKDruckWiederholen() {
        if (anzNichtStornierteKarten == 0) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Funktion nicht möglich - keine Eintrittskarte zugeordnet");
            return;
        }
        String ekNummer = ersteEKNr;
        String ekNummerNeben = ersteEKNrNeben;

        CtrlEintrittskarteDrucken eintrittskarteDrucken = new CtrlEintrittskarteDrucken();
        eintrittskarteDrucken.einzelDruck(ekNummer, ekNummerNeben);

    }

    /**
     * Funktion SK druck erstzugang wiederholen.
     */
    private void funktionSKDruckErstzugangWiederholen() {

        DbBundle lDbBundle = new DbBundle(); //nur für Druck-Parameter

        int gattung = aktuellMeldung.liefereGattung();
        CaBug.druckeLog("Gattung=" + gattung, logDrucken, 10);
        String hEkNummer = ersteEKNr;
        //        String hEkNummerNeben = ersteEKNrNeben;

        if (hEkNummer.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Funktion nicht möglich - keine Eintrittskarte zugeordnet");
            return;
        }

        int ekNummer = Integer.parseInt(hEkNummer);

        BlStimmkartendruck blStimmkartendruck = new BlStimmkartendruck(false, lDbBundle);
        blStimmkartendruck.holeMeldedatenFuerTausch1Zu1(0, BlStimmkartendruck.KONST_DRUCKLAUF_ALLE,
                BlStimmkartendruck.KONST_SELEKTION_AKTIONAERE_NICHT_IN_SAMMELKARTEN
                        | BlStimmkartendruck.KONST_SELEKTION_AKTIONAERE_IN_SAMMELKARTEN
                        | BlStimmkartendruck.KONST_SELEKTION_SAMMELKARTEN,
                ekNummer, ekNummer, 0, 1);
        if (blStimmkartendruck.rcMeldungsliste.isEmpty() || blStimmkartendruck.rcMeldungsliste.size() == 0) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Funktion nicht möglich - keine Meldungen gefunden");
            return;
        }

        for (int i = 1; i <= 4; i++) {
            CaBug.druckeLog("i=" + i, logDrucken, 10);
            int sNrOffset = i - 1;
            if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[gattung - 1][sNrOffset] == 1) {
                String hDrucker = ParamS.param.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[gattung
                        - 1][sNrOffset];
                CaBug.druckeLog("Aktive Stimmkarte gefunden hDrucker=" + hDrucker, logDrucken, 10);
                if (!hDrucker.isEmpty()) {
                    int druckerNr = Integer.parseInt(hDrucker);
                    if (druckerNr != 0) {

                        CaBug.druckeLog("Stimmkarte drucken", logDrucken, 10);
                        RpDrucken rpDrucken = new RpDrucken();
                        rpDrucken.initClientDrucke();
                        rpDrucken.setzeAutoDrucker(druckerNr);

                        rpDrucken.initFormular(lDbBundle);
                        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
                        rpVariablen
                                .stimmkarten(ParamS.param.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[gattung
                                        - 1][sNrOffset], rpDrucken);

                        rpDrucken.startFormular();
                        blStimmkartendruck.drucke1Zu1(rpDrucken, rpVariablen, gattung, i);
                        rpDrucken.endeFormular();
                    }

                }
            }

        }

    }

    /** The gewaehlt aktienregister. */
    public EclAktienregister[] gewaehltAktienregister = null;

    /** The gewaehlt meldungen. */
    public EclMeldung[] gewaehltMeldungen = null;

    /** The gewaehlt sonstige vollmacht. */
    public String[] gewaehltSonstigeVollmacht = null;

    /** Siehe KonstServicedesk.PERSONENART_ */
    public int ausgewaehltePersonenart = 0;

    //    public EclSuchergebnis gewaehlteEclSuchergebnis = null;
    //    public boolean mitAktionaer = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

    /******************************
     * Ab hier alte Reste, die in Regeln überführt werden müssen/können
     *****/
    //    private void alteFUnktionsanzeigeGast() {
    //        if (istPraesent == false) {
    //           aktiveFunktionsauswahl = KONST_FUNKTIONSAUSWAHL_GASTKARTE_NICHT_PRAESENT;
    //           aktiviereFunktion(KONST_FUNKTION_ERSATZKARTE_GAST_AUSSTELLEN,
    //                   1, "(1) Ersatzkarte für Gast ausstellen",
    //                   1, "(1) Ausstellen einer Ersatz-Gastkarte. Stornieren aller bisherigen Gastkarten",
    //                   ""
    //                   );
    //       } else {
    //           aktiveFunktionsauswahl = KONST_FUNKTIONSAUSWAHL_GASTKARTE_PRAESENT;
    //           aktiviereFunktion(KONST_FUNKTION_ERSATZKARTE_GAST_AUSSTELLEN,
    //                   1, "(1) Ersatzkarte für Gast ausstellen",
    //                   1, "(1) Die Gastkarte ist bereits präsent - sind Sie das, und haben Ihre Gastkarte verloren? Ich stelle Ihnen dann eine Ersatz-Gastkarte aus",
    //                   "Der Gast ist bereits präsent. Diese Funktion darf nur ausgeführt werden, "
    //                           + "wenn der Gast bestätigt, dass er der bereits präsente Gast ist. "
    //                           + "Andernfalls ist eine neue (zusätzliche) Gastkarte auszustellen (2)"
    //                   );
    //           aktiviereFunktion(KONST_FUNKTION_GASTKARTE_FUER_GAST_AUSSTELLEN,
    //                   4, "(2) Neue (zusätzliche) Gastkarte für diesen Gast ausstellen",
    //                   2, "(2) Ausstellen einer neuen (zusätzlichen) Gastkarte für diesen Teilnehmer",
    //                   "Wenn möglicherweise die ursprüngliche Gastkarte durch einen anderen Teilnehmer verwendet wird, "
    //                           + "ist mit dieser Funktion eine neue zusätzliche Gastkarte auszustellen; "
    //                           + "die bisher bereits ausgestellte Gastkarte bleibt gültig"
    //                   );
    //       }
    //       
    //   }
    //   
    //    private void alteFunktionsanzeigeSammelkarte(){
    //        if (istPraesent == false) {
    //            if (warPraesent_Alt==false) {
    //                /*---------------Nicht präsent, und war auch noch nie präsent-----------------------
    //                 * > Zugangs-EKs ausdrucken (Ersatzkarte, oder Nachdruck)
    //                 * > Stimmkarte drucken, wenn nicht beim Erstzugang gedruckt wird.
    //                 */
    //                aktiveFunktionsauswahl = KONST_FUNKTIONSAUSWAHL_SAMMELKARTE_NOCH_NIE_PRAESENT;
    //                
    //                if (anzNichtStornierteKarten==0) {
    //                    /*Sammelkarte noch nie präsent, noch keine EK zugeordent*/
    //                    aktiviereFunktion(KONST_FUNKTION_EK_NEUE_ZUORDNEN_UND_AUSDRUCKEN,
    //                            1, "(1) EK zuordnen und ausdrucken",
    //                            1, "(1) Die Sammelkarte ist noch nicht präsent, es wurde keine Eintrittskarte ausgestellt."
    //                                    + " Neue Eintrittskarte zuordnen und ausdrucken.",
    //                                    ""
    //                            );
    //                    if (pruefeObStimmkarteFuerGattungZuDruckenVorHV(aktuellGattung)) {
    //                        aktiviereFunktion(KONST_FUNKTION_SK_NACHDRUCK_VOR_HV,
    //                                1, "(2) Vorbereitete Stimmkarten ausdrucken",
    //                                1, "(2) Wiederholung Ausdruck der vorbereiteten Stimmkarte(n)",
    //                                        ""
    //                                );
    //                    }
    //                }
    //                else {
    //                    /*Sammelkarte noch nie präsent, EK zugeordnet*/
    //                    aktiviereFunktion(KONST_FUNKTION_EK_DRUCK_WIEDERHOLEN,
    //                            1, "(1) Zugeordnete Eintrittskarte ausdrucken",
    //                            1, "(1) Zugeordnete Eintrittskarte ausdrucken",
    //                            "Der Sammelkarte ist bereits eine EK zugeordnet. Diese wird erneut ausgedruckt. Funktion nur verwenden, "
    //                                    + "wenn Mißbrauch ausgeschlossen ist (genauer: die Eintrittskarte noch nicht ausgehändigt wurde), "
    //                                    + "sonst Ersatzkarte ausstellen (2)"
    //                            );
    //                    aktiviereFunktion(KONST_FUNKTION_EK_ERSATZ_AUSSTELLEN,
    //                            2, "(2) Ersatz-Eintrittskarte erstellen und ausdrucken",
    //                            2, "(2) Stornierung der bisherigen Eintrittskarten, ausstellen und drucken einer Ersatzkarte",
    //                            "Diese Funktion nutzen, wenn möglicherweise die bisher zugeordnete Eintrittskarte verloren wurde."
    //
    //                            );
    //                    if (pruefeObStimmkarteFuerGattungZuDruckenVorHV(aktuellGattung)) {
    //                        aktiviereFunktion(KONST_FUNKTION_SK_NACHDRUCK_VOR_HV,
    //                                3, "(3) Vorbereitete Stimmkarten ausdrucken",
    //                                3, "(3) Wiederholung Ausdruck der vorbereiteten Stimmkarte(n)",
    //                                        "Wenn eine vor der HV ausgedruckte / vorbereitete Stimmkarte nicht vorhanden ist, "
    //                                        + "kann diese hier erneut ausgedruckt werden (wenn die Befürchtung besteht,"
    //                                        + "dass diese Stimmkarte versehentlich anderweitig verwendet wurde,"
    //                                        + "dann vorher mit (2) eine Ersatzkarte ausstellen und dann mit dieser "
    //                                        + "Funktion ggf. eine neue Stimmkarte erstellen)"
    //                                );
    //                    }
    //
    //                }
    //            }
    //            else { //warPraesent==true
    //                /*-----------aktuell nicht präsent, aber war schon mal präsent-------------*/
    //                aktiveFunktionsauswahl = KONST_FUNKTIONSAUSWAHL_SAMMELKARTE_NICHT_PRAESENT_ABER_WAR_SCHON_PRAESENT;
    //                funktionenFuerSammelkarte_Praesent_WarPraesent();
    //           }
    //        } else {
    //            /*Sammelkarte präsent*/
    //            aktiveFunktionsauswahl = KONST_FUNKTIONSAUSWAHL_SAMMELKARTE_PRAESENT;
    //            funktionenFuerSammelkarte_Praesent_WarPraesent();
    //        }
    //
    //   }
    //
    //    private void funktionenFuerSammelkarte_Praesent_WarPraesent(){
    //        int lfdFunktionsnummer=0;
    //        /* Falls keine Stimmkarten für diese Gattung, dann Zugangskarte drucken
    //         * Sonst Stimmkarten drucken
    //         * > Stimmkarten wiederholt ausdrucken, die beim Erstzugang gedruckt werden.
    //         * > Stimmkarte wiederholt drucken, die nicht beim Erstzugang gedruckt wird
    //         * > Stimmkarten ersetzen
    //         */
    //        if (pruefeObStimmkarteFuerGattungAktiv(aktuellGattung)==false) {
    //            lfdFunktionsnummer++;
    //            aktiviereFunktion(KONST_FUNKTION_EK_DRUCK_WIEDERHOLEN,
    //                    1, "(1) Zugeordnete Eintrittskarte ausdrucken",
    //                    1, "(1) Zugeordnete Eintrittskarte ausdrucken",
    //                    "Der Sammelkarte ist bereits eine EK zugeordnet. Diese wird erneut ausgedruckt. Funktion nur verwenden, "
    //                            + "wenn Mißbrauch ausgeschlossen ist (genauer: die Eintrittskarte noch nicht ausgehändigt wurde), "
    //                            + "sonst Ersatzkarte ausstellen (2)"
    //                    );
    //            
    //            lfdFunktionsnummer++;
    //            aktiviereFunktion(KONST_FUNKTION_EK_ERSATZ_AUSSTELLEN,
    //                    2, "(2) Ersatz-Eintrittskarte erstellen und ausdrucken",
    //                    2, "(2) Stornierung der bisherigen Eintrittskarten, ausstellen und drucken einer Ersatzkarte",
    //                    "Diese Funktion nutzen, wenn möglicherweise die bisher zugeordnete Eintrittskarte verloren wurde."
    //
    //                    );
    //        }
    //        else {
    //            int funktionErsetzenHatLfdNr=lfdFunktionsnummer+1;
    //            if (pruefeObStimmkarteFuerGattungZuDruckenVorHV(aktuellGattung)) {
    //                funktionErsetzenHatLfdNr++;
    //            }
    //            if (pruefeObStimmkarteFuerGattungZuDruckenBeiErstzugang(aktuellGattung)) {
    //                funktionErsetzenHatLfdNr++;
    //            }
    //            
    //            /*Es gibt Stimmkarten*/
    //            if (pruefeObStimmkarteFuerGattungZuDruckenVorHV(aktuellGattung)) {
//                //@formatter:off
//                lfdFunktionsnummer++;
//                aktiviereFunktion(KONST_FUNKTION_SK_NACHDRUCK_VOR_HV,
//                        lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Vorbereitete Stimmkarten ausdrucken",
//                        lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Wiederholung Ausdruck der vorbereiteten Stimmkarte(n)",
//                        "Verwendung nur wenn Stimmkarte sicher noch nicht ausgegeben und damit Mißbrauch "
//                                + "ausgeschlossen werden kann, ansonsten komplettes Stimmmaterial ersetzen mit ("+Integer.toString(funktionErsetzenHatLfdNr)+")"
//                        );
//                //@formatter:on
    //            }
    //            if (pruefeObStimmkarteFuerGattungZuDruckenBeiErstzugang(aktuellGattung)) {
//                //@formatter:off
//                lfdFunktionsnummer++;
//                aktiviereFunktion(KONST_FUNKTION_SK_NACHDRUCK_BEI_ERSTZUGANG,
//                        lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Bei Erstzugang erstellte Stimmkarte ausdrucken",
//                        lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Wiederholung Ausdruck der bei Erstzugang erstellten Stimmkarte(n)",
//                        "Verwendung nur wenn Stimmkarte sicher noch nicht ausgegeben und damit Mißbrauch "
//                                + "ausgeschlossen werden kann, ansonsten komplettes Stimmmaterial ersetzen mit ("+Integer.toString(funktionErsetzenHatLfdNr)+")"
//                        );
//                //@formatter:on
    //            }
//            //@formatter:off
//            lfdFunktionsnummer++;
//            String lHinweisText=                            "Soweit noch im Besitz des Teilnehmers werden bereits ausgehändigte Stimmkarten "
//                    + "einbehalten und ungültig gesetzt, neue Stimmkarten werden ausgestellt / zugeordnet.";
//            if (pruefeObStimmkarteTausch1Zu1(aktuellGattung)) {
//                lHinweisText=lHinweisText+" Aufgrund der 1:1 Zuordnung von Stimmkarten wird auch eine neue Eintrittskartennummer vergeben. ";
//            }
//            aktiviereFunktion(KONST_FUNKTION_SK_ERSETZEN,
//                    lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Ersatzstimmkarten ausstellen / zuordnen",
//                    lfdFunktionsnummer, "("+Integer.toString(lfdFunktionsnummer)+") Die bisher ausgegebenen / zugeordneten Stimmkarten ungültig setzen, neue Stimmkarten ausgeben",
//                    lHinweisText);
//            //@formatter:on
    //        }
    //       
    //       
    //    }

    //    /*******************************ab hier alte Funktionen - nicht mehr benötigt, nur noch zur Sicherheit**********/    
    //    @Deprecated
    //    private void aktiviereFunktion(int pFunktionsauswahl, 
    //            int pButtonNr, String pButtonText, 
    //            int pFunktionsNr, String pSelectText, String pHinweisText) {
    //        
    //        funktionButton[pButtonNr]=pFunktionsauswahl;
//        //@formatter:off
//        switch (pButtonNr) {
//        case 1: btnButton1.setText(pButtonText);btnButton1.setVisible(true);break;
//        case 2: btnButton2.setText(pButtonText);btnButton2.setVisible(true);break;
//        case 3: btnButton3.setText(pButtonText);btnButton3.setVisible(true);break;
//        case 4: btnButton4.setText(pButtonText);btnButton4.setVisible(true);break;
//        case 5: btnButton5.setText(pButtonText);btnButton5.setVisible(true);break;
//        case 6: btnButton6.setText(pButtonText);btnButton6.setVisible(true);break;
//        case 7: btnButton7.setText(pButtonText);btnButton7.setVisible(true);break;
//        case 8: btnButton8.setText(pButtonText);btnButton8.setVisible(true);break;
//        case 9: btnButton9.setText(pButtonText);btnButton9.setVisible(true);break;
//        case 10: btnButton10.setText(pButtonText);btnButton10.setVisible(true);break;
//        }
//        //@formatter:on
    //        
    //        funktionSelect[pFunktionsNr]=pFunktionsauswahl;
    //        hinweisTextAktion[pFunktionsNr]=pHinweisText;
//        //@formatter:off
//        switch (pFunktionsNr) {
//        case 1:rbAuswahl1.setVisible(true);rbAuswahl1.setText(pSelectText);break;
//        case 2:rbAuswahl2.setVisible(true);rbAuswahl2.setText(pSelectText);break;
//        case 3:rbAuswahl3.setVisible(true);rbAuswahl3.setText(pSelectText);break;
//        case 4:rbAuswahl4.setVisible(true);rbAuswahl4.setText(pSelectText);break;
//        case 5:rbAuswahl5.setVisible(true);rbAuswahl5.setText(pSelectText);break;
//        case 6:rbAuswahl6.setVisible(true);rbAuswahl6.setText(pSelectText);break;
//        case 7:rbAuswahl7.setVisible(true);rbAuswahl7.setText(pSelectText);break;
//        case 8:rbAuswahl8.setVisible(true);rbAuswahl8.setText(pSelectText);break;
//        case 9:rbAuswahl9.setVisible(true);rbAuswahl9.setText(pSelectText);break;
//        case 10:rbAuswahl10.setVisible(true);rbAuswahl10.setText(pSelectText);break;
//        }
//        //@formatter:on
    //        if (pFunktionsNr==1) {
    //            lblAuswahlText.setText(hinweisTextAktion[pFunktionsNr]);
    //        }
    //    }

    /***************************
     * irgendwelche sonstigesn Reste
     *****************************/
    //  int rc=0;
    //  switch (aktiveFunktionsauswahl) {
    //  case KONST_FUNKTIONSAUSWAHL_GASTKARTE_PRAESENT:
    //  case KONST_FUNKTIONSAUSWAHL_GASTKARTE_NICHT_PRAESENT:
    //      if (rbAuswahl1.isSelected()) {
    //          rc=ausfuehrenStornoBisherigeUndAusstellenErsatzGastkarte();
    //          return;
    //      }
    //      if (rbAuswahl2.isSelected()) {
    //          rc=ausfuehrenZusaetzlicheGastkarte();
    //          return;
    //      }
    //  }
    //  /*AAAAAA*/

    //    /**> Alle bisherigen Gastkarten als Abgang Buchen, falls Präsent
    //     * > Alle bisherigen Gastkarten stornieren
    //     * > Neue Gastkarte ausstellen
    //     * > Falls Gast bereits präsent, dann Zugang buchen
    //     * @return
    //     */
    //    int ausfuehrenStornoBisherigeUndAusstellenErsatzGastkarte() {
    //        /*AAAAA ServiceDesk*/
    //        return 1;
    //    }
    //    
    //    /**> neue Gastkarte ausstellen
    //     * > Falls Gast bereits präsent, dann Zugang buchen
    //     */
    //    int ausfuehrenZusaetzlicheGastkarte() {
    //        /*AAAAA ServiceDesk*/
    //        return 1;
    //    }

    /**
     * ==============================Funktionen zum Ausführen
     ***************************************/

    //
    //    List<Button> btnWKNachdruck = null;
    //    List<Button> btnStimmkartenhdruck = null;
    //    List<Integer> meldungNachdruck = null;
    //    List<Integer> meldungWKNachdruck = null;
    //
    //    /************Globale Werte*************/
    //
    //    private WSClient wsClient = null;
    //    private DbBundle lDbBundle = null;
    //
    //    /**1=Eintrittskartennummer/Gastkartennummer
    //     * 2=Stimmkartennummer
    //     */
    //    int suchfunktion = 0;
    //    //    private String fehlertext = "";
    //
    //    int kartenklasse = 0;
    //
    //    //    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM = null;
    //    private EclBesitzJeKennung eclBesitzJeKennung=null;
    //    private List<EclZugeordneteMeldungNeu> zugeordneteMeldungenListe=null;
    //
    //
    //
    //
    //    @FXML
    //    void initialize() {
    //        assert scpnErgebnis != null : "fx:id=\"scpnErgebnis\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert btnErsatzkarteDrucken != null : "fx:id=\"btnErsatzkarteDrucken\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert btnFertig != null : "fx:id=\"btnFertig\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert tfNameVorname != null : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert tfAktionaersnummer != null : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'DetailStage.fxml'.";
    //
    //        /**********************Ab hier individuell**************************************************/
    //
    //        lDbBundle = new DbBundle();
    //        wsClient = new WSClient();
    //
    //        if (gewaehlteEclSuchergebnis.meldungKlasse == 0) {/*Gastkarte*/
    //            scpnErgebnis.setContent(null);
    //
    //            grpnErgebnis = new GridPane();
    //            grpnErgebnis.setVgap(5);
    //            grpnErgebnis.setHgap(15);
    //
    //            int zeile = 0;
    //
    //            Label hLabel0 = new Label();
    //            hLabel0.setText("Gastkarte");
    //            grpnErgebnis.add(hLabel0, 0, zeile++);
    //
    //            Label hLabel1 = new Label();
    //            String hString1 = gewaehlteEclSuchergebnis.zutrittsIdent;
    //            hLabel1.setText(hString1);
    //            grpnErgebnis.add(hLabel1, 0, zeile++);
    //
    //            Label hLabel2 = new Label();
    //            hLabel2.setText(gewaehlteEclSuchergebnis.aktionaerVorname + " " + gewaehlteEclSuchergebnis.aktionaerName);
    //            grpnErgebnis.add(hLabel2, 0, zeile++);
    //
    //            Label hLabel3 = new Label();
    //            hLabel3.setText(gewaehlteEclSuchergebnis.aktionaerOrt);
    //            grpnErgebnis.add(hLabel3, 0, zeile++);
    //
    //            scpnErgebnis.setContent(grpnErgebnis);
    //
    //        } else {
    //            neuEinlesenUndAnzeigen(gewaehlteEclSuchergebnis.aktionaersnummer);
    //
    //        }
    //
    //        if (gewaehlteEclSuchergebnis.zutrittsIdent == null || gewaehlteEclSuchergebnis.zutrittsIdent.isEmpty()
    //                || gewaehlteEclSuchergebnis.zutrittsIdentGeperrt == 1) {
    //            btnErsatzkarteDrucken.setVisible(false);
    //        }
    //
    //
    //
    //        /*Prüfen, ob Stimmkartendruck zulässig*/
    //        //        int gattung=lEclMeldung.gattung;
    //        //        
    //
    //
    //
    //
    //        Platform.runLater(new Runnable() {
    //            @Override
    //            public void run() {
    //            }
    //        });
    //
    //    }
    //
    //
    //    //    private EclTeilnehmerLoginM eclTeilnehmerLoginM = null;
    //    private EclTLoginDatenM eclTLoginDatenM=null;
    //    private WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;
    //
    //    private void neuEinlesenUndAnzeigen(String aktionaersnummer) {
    //
    //        System.out.println("aktionaersnummer=" + aktionaersnummer);
    //
    //        /*Test*/
    //        //       for (int i=0;i<2000;i++){
    //        //           System.out.println("------------------------i="+i);
    //
    //        WELogin weLogin = new WELogin();
    //
    //        /***********************Aktionärsdaten grundsätzlich holen / überprüfen auf Existenz************/
    //        /*Hinweise: 
    //         * > mandant wird automatisch gesetzt aus ClGlobalVar.mandant - diese vorher belegen!
    //         * > user, uKennung, uPasswort werden automatisch gesetzt
    //         * 
    //         */
    //        weLogin.setKennungArt(1); /* kennung enthält Aktionärsnummer*/
    //
    //        weLogin.setKennung(aktionaersnummer);
    //        WELoginCheck weLoginCheck = new WELoginCheck();
    //        weLoginCheck.weLogin = weLogin;
    //
    //        /*Die Rückgegebenen Aktionärsdaten speichern, da sie für weitere Aktionen
    //         * benötigt werden
    //         */
    //        WELoginCheckRC weLoginCheckRC = null;
    //        weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
    //        eclTLoginDatenM = weLoginCheckRC.eclTLoginDatenM;
    //
    //        int rc = weLoginCheckRC.getRc();
    //        if (rc < 1) { /*Fehlerbehandlung*/
    //            return;
    //        }
    //
    //        tfNameVorname.setText(eclTLoginDatenM.liefereNameVornameTitel());
    //        tfOrt.setText(eclTLoginDatenM.ort);
    //        tfAktien.setText(eclTLoginDatenM.stimmenDE);
    //        tfAktionaersnummer.setText(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
    //
    //        /**************************Aktionärs-Status holen*****************************************/
    //
    //        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);
    //
    //        weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
    //        rc = weTeilnehmerStatusGetRC.getRc();
    //
    //        //        eclZugeordneteMeldungListeM = weTeilnehmerStatusGetRC.getEclZugeordneteMeldungListeM();
    //        eclBesitzJeKennung=weTeilnehmerStatusGetRC.besitzJeKennungListe.get(0);
    //        zugeordneteMeldungenListe=eclBesitzJeKennung.eigenerAREintragListe.get(0).zugeordneteMeldungenListe;
    //
    //        if (rc < 1) { /*Fehlerbehandlung*/
    //            return;
    //        }
    //        /*Test Ende*/
    //        //       }
    //
    //        if (eclBesitzJeKennung.eigenerAREintragListe.get(0).angemeldet) { /*Status anzeigen*/
    //            bereiteTabStatus();
    //
    //            return;
    //        } else {/*Erstanmeldung*/
    //
    //            bereiteTabErstanmeldung();
    //
    //        }
    //
    //    }
    //
    //    private void bereiteTabErstanmeldung() {
    //
    //        grpnErstanmeldung = new GridPane();
    //        grpnErstanmeldung.setVgap(5);
    //        grpnErstanmeldung.setHgap(15);
    //
    //        scpnErgebnis.setContent(grpnErstanmeldung);
    //
    //    }
    //
    //    private void bereiteTabStatus() {
    //        int zeilenSpalte1 = 0;
    //        int zeilenSpalte2 = 0;
    //        int anzahlMeldungen = 0;
    //        long anzahlStimmen = 0;
    //
    //        grpnStatus = new GridPane();
    //        grpnStatus.setVgap(5);
    //        grpnStatus.setHgap(15);
    //
    //        anzahlMeldungen = eclBesitzJeKennung.eigenerAREintragListe.get(0).liefereAnzZugeordneteMeldungenEigeneAktien();
    //
    //        btnWKNachdruck = new LinkedList<Button>();
    //        btnStimmkartenhdruck = new LinkedList<Button>();
    //        meldungNachdruck = new LinkedList<Integer>();
    //        meldungWKNachdruck = new LinkedList<Integer>();
    //
    //        for (int i = 0; i < anzahlMeldungen; i++) {
    //            if (zeilenSpalte1 < zeilenSpalte2) {
    //                zeilenSpalte1 = zeilenSpalte2;
    //            }
    //            zeilenSpalte2 = zeilenSpalte1;
    //            EclZugeordneteMeldungNeu eclZugeordneteMeldung=zugeordneteMeldungenListe.get(i);
    //            //            EclZugeordneteMeldungM eclZugeordneteMeldungM = eclZugeordneteMeldungListeM
    //            //                    .getZugeordneteMeldungenEigeneAktienListeM().get(i);
    //            Label hLabel1 = new Label();
    //            hLabel1.setText(eclZugeordneteMeldung.aktionaerTitelVornameName);
    //            grpnStatus.add(hLabel1, 0, zeilenSpalte1);
    //            zeilenSpalte1++;
    //
    //            Label hLabel2 = new Label();
    //            hLabel2.setText(eclZugeordneteMeldung.aktionaerOrt);
    //            grpnStatus.add(hLabel2, 0, zeilenSpalte1);
    //            zeilenSpalte1++;
    //
    //            //            if (eclZugeordneteMeldungM.getIstPraesentNeu() > 0) {
    //            if (eclZugeordneteMeldung.statusPraesenz > 0) {
    //                Label hLabelP = new Label();
    //                hLabelP.setText("Ist oder war präsent");
    //                grpnStatus.add(hLabelP, 0, zeilenSpalte1);
    //                zeilenSpalte1++;
    //            }
    //            //            if (eclZugeordneteMeldungM.getKlasse() == 1) {
    //            if (eclZugeordneteMeldung.klasse == 1) {
    //                Label hLabel3 = new Label();
    //                hLabel3.setText("Aktionär mit " + eclZugeordneteMeldung.aktionaerStimmenDE + " Aktien");
    //                anzahlStimmen += eclZugeordneteMeldung.getStueckAktien();
    //                grpnStatus.add(hLabel3, 0, zeilenSpalte1);
    //                zeilenSpalte1++;
    //
    //                if (eclZugeordneteMeldung.zugeordneteWillenserklaerungenList != null) {
    //                    //                if (eclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM() != null) {
    //                    for (int i1 = 0; i1 < eclZugeordneteMeldung.zugeordneteWillenserklaerungenList.size(); i1++) {
    //                        //                   for (i1 = 0; i1 < eclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().size(); i1++) {
    //                        EclWillenserklaerungStatusNeu willenserklaerung = eclZugeordneteMeldung
    //                                .zugeordneteWillenserklaerungenList.get(i1);
    //                        //                        EclWillenserklaerungStatusM willenserklaerung = eclZugeordneteMeldungM
    //                        //                                .getZugeordneteWillenserklaerungenListM().get(i1);
    //                        if (willenserklaerung.isIstLeerDummy()) {
    //                            Label hwLabel1 = new Label();
    //                            hwLabel1.setText("Keine Willenserklärungen vorhanden");
    //                            grpnStatus.add(hwLabel1, 1, zeilenSpalte2);
    //                            zeilenSpalte2++;
    //
    //                        } else {
    //
    //                            if (!willenserklaerung.isStorniert()
    //                                    && CInjects.aendern/*willenserklaerung.isAendernIstZulaessig()*/
    //                                    && willenserklaerung
    //                                    .getWillenserklaerung() != KonstWillenserklaerung.anmeldungAusAktienregister
    //                                    && willenserklaerung
    //                                    .getWillenserklaerung() != KonstWillenserklaerung.neueZutrittsIdentZuMeldung
    //                                    && willenserklaerung
    //                                    .getWillenserklaerung() != KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte
    //                                    && willenserklaerung
    //                                    .getWillenserklaerung() != KonstWillenserklaerung.vollmachtAnDritte) {
    //
    //                            } else {
    //                                if (willenserklaerung
    //                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
    //                                        || willenserklaerung
    //                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte
    //                                        || willenserklaerung
    //                                        .getWillenserklaerung() == KonstWillenserklaerung.vollmachtAnDritte) {
    //
    //                                }
    //                                if (willenserklaerung
    //                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
    //                                        || willenserklaerung
    //                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
    //                                    Button hBtn3 = new Button();
    //                                    hBtn3.setText("Nachdruck");
    //                                    hBtn3.setOnAction(e -> {
    //                                        clickedNachdruck(e);
    //                                    });
    //                                    btnWKNachdruck.add(hBtn3);
    //
    //                                    Button hBtn5 = new Button();
    //                                    hBtn5.setText("Stimmkartendruck");
    //                                    hBtn5.setOnAction(e -> {
    //                                        clickedStimmkartendruck(e);
    //                                    });
    //                                    btnStimmkartenhdruck.add(hBtn5);
    //
    //                                    meldungNachdruck.add(i);
    //                                    meldungWKNachdruck.add(i1);
    //                                    if (!willenserklaerung.isStorniert() && CInjects.aendern) {
    //                                        grpnStatus.add(hBtn3, 4, zeilenSpalte2);
    //                                        grpnStatus.add(hBtn5, 5, zeilenSpalte2);
    //                                    }
    //
    //                                }
    //
    //                            }
    //
    //                            if (willenserklaerung.isStorniert()) {
    //                                Label hLbl = new Label();
    //                                hLbl.setText("Storniert");
    //                                grpnStatus.add(hLbl, 2, zeilenSpalte2);
    //                            }
    //
    //                            /*TODO Anzeige noch anders zu lösen*/
    //                            Label hwLabel2 = new Label();
    //                            hwLabel2.setText(KonstWillenserklaerung.getText(willenserklaerung.willenserklaerung));
    //                            grpnStatus.add(hwLabel2, 1, zeilenSpalte2);
    //                            zeilenSpalte2++;
    //
    //                            //                            for (i2 = 0; i2 < willenserklaerung.getTextListe().size(); i2++) {
    //                            //                                Label hwLabel2 = new Label();
    //                            //                                hwLabel2.setText(willenserklaerung.getTextListe().get(i2));
    //                            //                                grpnStatus.add(hwLabel2, 1, zeilenSpalte2);
    //                            //                                zeilenSpalte2++;
    //                            //                            }
    //                            zeilenSpalte2++;
    //                        }
    //                    }
    //                }
    //            }
    //
    //            Label hLabel4 = new Label();
    //            hLabel4.setText("");
    //            grpnStatus.add(hLabel4, 0, zeilenSpalte1);
    //            zeilenSpalte1++;
    //
    //        }
    //
    //        scpnErgebnis.setContent(grpnStatus);
    //
    //    }
    //
    //
    //
    //    @FXML
    //    void clickedStimmkartendruck(ActionEvent event) {
    //    }

}
