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
package de.meetingapps.meetingclient.meetingBestand;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchlaufDurchfuehren;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComBl.BlSuchlauf;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiSubZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufBegriffe;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufVerwendung;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The Class CtrlInstiPflegeDetails.
 */
public class CtrlInstiPflegeDetails extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The tab pane gesamt. */
    @FXML
    private TabPane tabPaneGesamt;

    /** The tab details. */
    @FXML
    private Tab tabDetails;

    /** The tf ident. */
    @FXML
    private TextField tfIdent;

    /** The tf kurz bezeichnung. */
    @FXML
    private TextField tfKurzBezeichnung;

    /** The cb standard sammelkarten anlage aktiv. */
    @FXML
    private CheckBox cbStandardSammelkartenAnlageAktiv;

    /** The cb standard sammelkarte ohne weisung. */
    @FXML
    private CheckBox cbStandardSammelkarteOhneWeisung;

    /** The cb standard sammelkarte mit weisung. */
    @FXML
    private CheckBox cbStandardSammelkarteMitWeisung;

    /** The cb standard sammelkarte SRV. */
    @FXML
    private CheckBox cbStandardSammelkarteSRV;

    /** The cb standard sammelkarte briefwahl. */
    @FXML
    private CheckBox cbStandardSammelkarteBriefwahl;

    /** The cb standard sammelkarte dauervollmacht. */
    @FXML
    private CheckBox cbStandardSammelkarteDauervollmacht;

    /** The tg weitergabe weisungen. */
    @FXML
    private ToggleGroup tgWeitergabeWeisungen;

    /** The rb nur in gesammtsumme. */
    @FXML
    private RadioButton rbNurInGesammtsumme;

    /** The rb nur in summe je sammelkarte. */
    @FXML
    private RadioButton rbNurInSummeJeSammelkarte;

    /** The rb nicht erlaubt. */
    @FXML
    private RadioButton rbNichtErlaubt;

    /** The rb uneingeschraenkt. */
    @FXML
    private RadioButton rbUneingeschraenkt;

    /** The tf standard sammelkarte gruppennummer. */
    @FXML
    private TextField tfStandardSammelkarteGruppennummer;

    /** The ta suchlauf begriffe. */
    @FXML
    private TextArea taSuchlaufBegriffe;

    /** The tf standard melde name. */
    @FXML
    private TextField tfStandardMeldeName;

    /** The tf standard melde ort. */
    @FXML
    private TextField tfStandardMeldeOrt;

    /** The tf festadressen nummer. */
    @FXML
    private TextField tfFestadressenNummer;

    /** The tab kontakte kennungen. */
    @FXML
    private Tab tabKontakteKennungen;

    /** The pn kontakte. */
    @FXML
    private ScrollPane pnKontakte;

    /** The btn neuer kontakt. */
    @FXML
    private Button btnNeuerKontakt;

    /** The pn kennungen. */
    @FXML
    private ScrollPane pnKennungen;

    /** The btn kontakt pflegen. */
    @FXML
    private Button btnKontaktPflegen;

    /** The tab zugeordnete insti ueblich. */
    @FXML
    private Tab tabZugeordneteInstiUeblich;

    /** The scpn zugeordnete insti ueblich. */
    @FXML
    private ScrollPane scpnZugeordneteInstiUeblich;

    /** The btn pflegen ueblich. */
    @FXML
    private Button btnPflegenUeblich;

    /** The btn speichern ueblich. */
    @FXML
    private Button btnSpeichernUeblich;

    /** The tab bestaende. */
    @FXML
    private Tab tabBestaende;

    /** The scpn bestaende register. */
    @FXML
    private ScrollPane scpnBestaendeRegister;

    /** The scpn bestaende meldungen. */
    @FXML
    private ScrollPane scpnBestaendeMeldungen;

    /** The btn bestaende aendern register. */
    @FXML
    private Button btnBestaendeAendernRegister;

    /** The btn bestand manuell eingeben. */
    @FXML
    private Button btnBestandManuellEingeben;

    /** The btn sammel anmeldebogen. */
    @FXML
    private Button btnSammelAnmeldebogen;

    /** The btn bestaende aendern meldungen. */
    @FXML
    private Button btnBestaendeAendernMeldungen;

    /** The btn bestand importieren. */
    @FXML
    private Button btnBestandImportieren;

    /** The btn reg zuordnung stornieren. */
    @FXML
    private Button btnRegZuordnungStornieren;

    /** The btn meld zuordnung stornieren. */
    @FXML
    private Button btnMeldZuordnungStornieren;

    /** The tab zugeordnete insti tatsaechlich. */
    @FXML
    private Tab tabZugeordneteInstiTatsaechlich;

    /** The scpn zugeordnete insti tatsaechlich. */
    @FXML
    private ScrollPane scpnZugeordneteInstiTatsaechlich;

    /** The btn pflegen tatsaechlich. */
    @FXML
    private Button btnPflegenTatsaechlich;

    /** The btn speichern tatsaechlich. */
    @FXML
    private Button btnSpeichernTatsaechlich;

    /** Ab hier individuell. */
    TableView<MInsti> tableInsti = null;

    /** The list insti. */
    ObservableList<MInsti> listInsti = null;

    /** The table reg. */
    private TableView<MInstiBestandsZuordnung> tableReg = null;

    /** The list reg. */
    private ObservableList<MInstiBestandsZuordnung> listReg = null;

    /** The table meld. */
    private TableView<MInstiBestandsZuordnung> tableMeld = null;

    /** The list meld. */
    private ObservableList<MInstiBestandsZuordnung> listMeld = null;

    //	TableView<EclMeldungMitAktionaersWeisung> tableAktionaereAktiv=null;
    //
    /** The db bundle. */
    //
    DbBundle dbBundle = null;

    /** The bl insti. */
    BlInsti blInsti = null;

    /** The aktuelle insti ident. */
    private int aktuelleInstiIdent = 0;

    /** The aktuelle insti. */
    private EclInsti aktuelleInsti = null;
    //    private MInsti aktuelleMInsti = null;

    /** The suchlauf begriffe. */
    private EclSuchlaufBegriffe suchlaufBegriffe = null;

    /** The ecl insti sub zuordnung ueblich array. */
    private EclInstiSubZuordnung[] eclInstiSubZuordnungUeblichArray = null;

    /** The ecl insti sub zuordnung tatsaechlich array. */
    private EclInstiSubZuordnung[] eclInstiSubZuordnungTatsaechlichArray = null;

    /** The ecl insti alle. */
    private EclInsti[] eclInstiAlle = null;

    /** The cb box fuer insti alle. */
    private CheckBox[] cbBoxFuerInstiAlle = null;

    //
    //	private EclZutrittskarten[] zutrittskartenListe=null;
    //	private Button[] btnStornierenZutrittskarte=null;
    //	private Button[] btnDruckenZutrittskarte=null;
    //
    //	private EclStimmkarten[] stimmkartenListe=null;
    //
    //	private EclWillensErklVollmachtenAnDritte[] willensErklVollmachtenAnDritte=null;
    //	private Button[] btnStornierenVollmacht=null;
    //
    //	private EclWeisungMeldung weisungenSammelkopf=null;
    //	private EclWeisungMeldung aktionaersSummen=null;
    //
    //	private List <EclMeldungMitAktionaersWeisung> aktionaereAktiv=null;
    //	private List <EclMeldungMitAktionaersWeisung> aktionaereInaktiv=null;
    //	private List <EclMeldungMitAktionaersWeisung> aktionaereWiderrufen=null;
    //	private List <EclMeldungMitAktionaersWeisung> aktionaereGeaendert=null;
    //
    //	private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereAktiv = null;
    //	private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereInaktiv = null;
    //	private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereWiderrufen = null;
    //	private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereGeaendert = null;
    //
    //	/**Steht normalerweise auf true; sobald in einem der Gattungsfelder ein neuer
    //	 * Wert ausgewählt wurde, und der entsprechende Listener zum ändern der anderen
    //	 * Gattungsfelder getriggert wurde, wird das auf false gesetzt, damit kein
    //	 * "Endlos-Gegenseitiger-Update" passiert; anschließend wieder auf true.
    //	 */
    //	private boolean gattungUpdaten=false;
    //
    /**
     * Initialize.
     */
    //
    @FXML
    void initialize() {
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert scpnUebersicht != null
                : "fx:id=\"scpnUebersicht\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabPaneGesamt != null
                : "fx:id=\"tabPaneGesamt\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabDetails != null
                : "fx:id=\"tabDetails\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfIdent != null : "fx:id=\"tfIdent\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfKurzBezeichnung != null
                : "fx:id=\"tfKurzBezeichnung\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert cbStandardSammelkarteOhneWeisung != null
                : "fx:id=\"cbStandardSammelkarteOhneWeisung\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert cbStandardSammelkarteMitWeisung != null
                : "fx:id=\"cbStandardSammelkarteMitWeisung\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfStandardSammelkarteGruppennummer != null
                : "fx:id=\"tfStandardSammelkarteGruppennummer\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert taSuchlaufBegriffe != null
                : "fx:id=\"taSuchlaufBegriffe\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfStandardMeldeName != null
                : "fx:id=\"tfStandardMeldeName\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfStandardMeldeOrt != null
                : "fx:id=\"tfStandardMeldeOrt\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tfFestadressenNummer != null
                : "fx:id=\"tfFestadressenNummer\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabKontakteKennungen != null
                : "fx:id=\"tabKontakteKennungen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert pnKontakte != null
                : "fx:id=\"pnKontakte\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnNeuerKontakt != null
                : "fx:id=\"btnNeuerKontakt\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert pnKennungen != null
                : "fx:id=\"pnKennungen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnKontaktPflegen != null
                : "fx:id=\"btnKontaktPflegen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabZugeordneteInstiUeblich != null
                : "fx:id=\"tabZugeordneteInstiUeblich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert scpnZugeordneteInstiUeblich != null
                : "fx:id=\"scpnZugeordneteInstiUeblich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnPflegenUeblich != null
                : "fx:id=\"btnPflegenUeblich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnSpeichernUeblich != null
                : "fx:id=\"btnSpeichernUeblich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabBestaende != null
                : "fx:id=\"tabBestaende\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert scpnBestaendeRegister != null
                : "fx:id=\"scpnBestaendeRegister\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert scpnBestaendeMeldungen != null
                : "fx:id=\"scpnBestaendeMeldungen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnBestaendeAendernRegister != null
                : "fx:id=\"btnBestaendeAendernRegister\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnBestandManuellEingeben != null
                : "fx:id=\"btnBestandManuellEingeben\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnSammelAnmeldebogen != null
                : "fx:id=\"btnSammelAnmeldebogen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnBestaendeAendernMeldungen != null
                : "fx:id=\"btnBestaendeAendernMeldungen\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnBestandImportieren != null
                : "fx:id=\"btnBestandImportieren\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert tabZugeordneteInstiTatsaechlich != null
                : "fx:id=\"tabZugeordneteInstiTatsaechlich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert scpnZugeordneteInstiTatsaechlich != null
                : "fx:id=\"scpnZugeordneteInstiTatsaechlich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnPflegenTatsaechlich != null
                : "fx:id=\"btnPflegenTatsaechlich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";
        assert btnSpeichernTatsaechlich != null
                : "fx:id=\"btnSpeichernTatsaechlich\" was not injected: check your FXML file 'InstiPflegeDetails.fxml'.";

        /** Ab hier individuell */

        dbBundle = new DbBundle();
        blInsti = new BlInsti(false, dbBundle);

        if (aktuelleFunktion == 1 || aktuelleFunktion == 2) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung - Grund-Daten von Institutionellen werden Mandantenübergreifend gepflegt!");
        }

        CaBug.druckeLog("A", logDrucken, 10);
        if (aktuelleFunktion != 1) {
            refreshInhalte();
            CaBug.druckeLog("B", logDrucken, 10);

            if (aktuelleFunktion != 3) {
                CaBug.druckeLog("C", logDrucken, 10);
                disableTabBestaende();
                CaBug.druckeLog("D", logDrucken, 10);
                disableTabTatsaechliche();
                CaBug.druckeLog("E", logDrucken, 10);
            } else {
                sperreTabsInsti();
                tabPaneGesamt.getSelectionModel().select(tabBestaende);
            }
        } else {
            /*Neuanlage: Felder leeren, Insti vorbelegen*/
            aktuelleInstiIdent = 0;
            aktuelleInsti = new EclInsti();
            //            aktuelleMInsti = new MInsti(aktuelleInsti);
            bereiteVorNeueInsti();
            fuelleTabDetails();

            disableTabKontakteKennungen();
            disableTabUeblich();
            disableTabBestaende();
            disableTabTatsaechliche();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tfKurzBezeichnung.requestFocus();
                }
            });

        }

        btnSpeichernUeblich.setVisible(false);
        btnSpeichernTatsaechlich.setVisible(false);
    }

    //
    //	private void fuelleCbGattung() {
    //		cbGattungNr.getItems().clear();
    //		cbGattungKuerzel.getItems().clear();
    //		for (int i=1;i<=5;i++) {
    //			if (ParamS.param.paramBasis.getGattungAktiv(i)) {
    //				String hString="";
    //				hString=Integer.toString(i);
    //				cbGattungNr.getItems().addAll(hString);
    //				cbGattungKuerzel.getItems().addAll(ParamS.param.paramBasis.getGattungBezeichnungKurz(i));
    //			}
    //		}
    //	}
    //
    /**
     * Zeigt aktuelleInsti in Header-Übersicht an. dbBundle muß geöffnet sein
     *
     * @return the int
     */
    private int zeigeInstiInTableView() {

        CtrlInstiPflegeUebergreifend ctrlInstiZeigeListe = new CtrlInstiPflegeUebergreifend();

        /** Table-View vorbereiten */
        tableInsti = ctrlInstiZeigeListe.vorbereitenTableViewInsti();
        tableInsti.setPrefHeight(160);
        tableInsti.setPrefWidth(1464);

        /** Daten einlesen */
        int anzahlInsti = ctrlInstiZeigeListe.holeInstiDaten(aktuelleInstiIdent);

        if (anzahlInsti == 0) {
            return 0;
        } else {
            listInsti = ctrlInstiZeigeListe.rcListInsti;
            tableInsti.setItems(listInsti);
            scpnUebersicht.setContent(tableInsti);

            aktuelleInsti = ctrlInstiZeigeListe.rcInsti[0];
            //            aktuelleMInsti = listInsti.get(0);
            //			zutrittskartenListe=ctrlInstiZeigeListe.rcZutrittskartenArray[0];
            //			stimmkartenListe=ctrlInstiZeigeListe.rcStimmkartenArray[0];
            //			willensErklVollmachtenAnDritte=ctrlInstiZeigeListe.rcWillensErklVollmachtenAnDritteArray[0];
        }
        return 1;

    }

    /**
     * Füllt die Tab Details aus aktuelleInsti.
     */
    private void fuelleTabDetails() {

        if (aktuelleInsti.ident != 0) {
            tfIdent.setText(Integer.toString(aktuelleInsti.ident));
        } else {
            tfIdent.setText("(neu)");
        }
        tfIdent.setEditable(false);

        tfKurzBezeichnung.setText(aktuelleInsti.kurzBezeichnung);

        if (aktuelleInsti.standardSammelkartenAnlageAktiv == 1) {
            cbStandardSammelkartenAnlageAktiv.setSelected(true);
        } else {
            cbStandardSammelkartenAnlageAktiv.setSelected(false);
        }

        if (aktuelleInsti.standardSammelkarteMitWeisung == 1) {
            cbStandardSammelkarteMitWeisung.setSelected(true);
        } else {
            cbStandardSammelkarteMitWeisung.setSelected(false);
        }
        if (aktuelleInsti.standardSammelkarteOhneWeisung == 1) {
            cbStandardSammelkarteOhneWeisung.setSelected(true);
        } else {
            cbStandardSammelkarteOhneWeisung.setSelected(false);
        }
        if (aktuelleInsti.standardSammelkarteSRV == 1) {
            cbStandardSammelkarteSRV.setSelected(true);
        } else {
            cbStandardSammelkarteSRV.setSelected(false);
        }
        if (aktuelleInsti.standardSammelkarteBriefwahl == 1) {
            cbStandardSammelkarteBriefwahl.setSelected(true);
        } else {
            cbStandardSammelkarteBriefwahl.setSelected(false);
        }
        if (aktuelleInsti.standardSammelkarteDauervollmacht == 1) {
            cbStandardSammelkarteDauervollmacht.setSelected(true);
        } else {
            cbStandardSammelkarteDauervollmacht.setSelected(false);
        }

        switch (aktuelleInsti.weisungsWeitergabe) {
        case 1:
            rbNurInGesammtsumme.setSelected(true);
            break;
        case 2:
            rbNurInSummeJeSammelkarte.setSelected(true);
            break;
        case 3:
            rbNichtErlaubt.setSelected(true);
            break;
        default:
            rbUneingeschraenkt.setSelected(true);
            break;
        }

        tfStandardSammelkarteGruppennummer.setText(Integer.toString(aktuelleInsti.standardSammelkarteGruppennummer));

        taSuchlaufBegriffe.setText(aktuelleInsti.suchlaufBegriffe);

        tfStandardMeldeName.setText(aktuelleInsti.standardMeldeName);
        tfStandardMeldeOrt.setText(aktuelleInsti.standardMeldeOrt);

        tfFestadressenNummer.setText(Integer.toString(aktuelleInsti.festadressenNummer));

        fuelleSubZuordnungTab(eclInstiSubZuordnungUeblichArray, scpnZugeordneteInstiUeblich);
        fuelleSubZuordnungTab(eclInstiSubZuordnungTatsaechlichArray, scpnZugeordneteInstiTatsaechlich);

        fuelleInstiBestandsZuordnung(1, scpnBestaendeRegister);
        fuelleInstiBestandsZuordnung(2, scpnBestaendeMeldungen);

        fuelleKennungenTab();

    }

    /**
     * Voraussetzung: blInsti.fuelleInstiBestandsZuordnung(aktuelleInstiIdent) wurde
     * bereits aufgerufen.
     *
     * @param pRegisterOderMeldung the register oder meldung
     * @param pScpnBestaende       the scpn bestaende
     */
    private void fuelleInstiBestandsZuordnung(int pRegisterOderMeldung, ScrollPane pScpnBestaende) {

        /** Liste füllen */
        ObservableList<MInstiBestandsZuordnung> listZwischen = FXCollections.observableArrayList();

        EclInstiBestandsZuordnung[] lInstiBestandsZuordnung = null;
        EclAktienregister[] lAktienregister = null;
        EclMeldung[] lMeldung = null;
        EclUserLogin[] lUserLogin = null;
        if (pRegisterOderMeldung == 1) {
            lInstiBestandsZuordnung = blInsti.rcRegInstiBestandsZuordnung;
            lAktienregister = blInsti.rcRegAktienregister;
            lMeldung = blInsti.rcRegMeldung;
            lUserLogin = blInsti.rcRegUserLogin;
        } else {
            lInstiBestandsZuordnung = blInsti.rcMeldInstiBestandsZuordnung;
            lAktienregister = blInsti.rcMeldAktienregister;
            lMeldung = blInsti.rcMeldMeldung;
            lUserLogin = blInsti.rcMeldUserLogin;
        }
        int anz = 0;
        if (lInstiBestandsZuordnung != null) {
            anz = lInstiBestandsZuordnung.length;
        }
        for (int i = 0; i < anz; i++) {
            MInstiBestandsZuordnung lMInstiBestandsZuordnung = new MInstiBestandsZuordnung(lInstiBestandsZuordnung[i],
                    lAktienregister[i], lMeldung[i], lUserLogin[i], i);
            listZwischen.add(lMInstiBestandsZuordnung);
        }
        if (pRegisterOderMeldung == 1) {
            listReg = listZwischen;
        } else {
            listMeld = listZwischen;
        }

        /*Table füllen*/
        CtrlInstiTableViewZuordnung ctrlInstiTableViewZuordnung = new CtrlInstiTableViewZuordnung();

        TableView<MInstiBestandsZuordnung> tableNeu = null;
        if (pRegisterOderMeldung == 1) {
            tableNeu = ctrlInstiTableViewZuordnung.baueGrundAnsichtTableViewOhneInhalte(1, true, true, false, true);
            tableReg = tableNeu;
        } else {
            tableNeu = ctrlInstiTableViewZuordnung.baueGrundAnsichtTableViewOhneInhalte(1, true, false, true, true);
            tableMeld = tableNeu;
        }
        tableNeu.setPrefHeight(203);
        tableNeu.setPrefWidth(1464);
        if (listZwischen.size() == 0) {
            System.out.println("listVeraenderung size 0");
            pScpnBestaende.setContent(null);
        } else {
            tableNeu.setItems(listZwischen);
            pScpnBestaende.setContent(tableNeu);
        }
        tableNeu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Fuelle sub zuordnung tab.
     *
     * @param eclInstiSubZuordnungArray the ecl insti sub zuordnung array
     * @param scpnZugeordneteInsti      the scpn zugeordnete insti
     */
    private void fuelleSubZuordnungTab(EclInstiSubZuordnung[] eclInstiSubZuordnungArray,
            ScrollPane scpnZugeordneteInsti) {

        MeetingGridPane lSubZuordnungGridPane = new MeetingGridPane(1, 5, true, false, true, false);

        if (eclInstiSubZuordnungArray != null) {
            int anz = eclInstiSubZuordnungArray.length;
            for (int i = 0; i < anz; i++) {
                EclInstiSubZuordnung lInstiSubZuordnung = eclInstiSubZuordnungArray[i];
                Label lIdent = new Label(Integer.toString(lInstiSubZuordnung.identSubInsti));
                lSubZuordnungGridPane.addMeeting(lIdent, 0, i + 1);

                Label lBezeichnung = new Label(lInstiSubZuordnung.bezeichnungSubInsti);
                lSubZuordnungGridPane.addMeeting(lBezeichnung, 1, i + 1);

            }
            String[] ueberschrift = { "Ident", "Bezeichnung" };
            lSubZuordnungGridPane.setzeUeberschrift(ueberschrift, scpnZugeordneteInsti);

        }

    }

    /**
     * Fuelle sub zuordnung tab zum pflegen.
     *
     * @param eclInstiSubZuordnungArray the ecl insti sub zuordnung array
     * @param scpnZugeordneteInsti      the scpn zugeordnete insti
     */
    private void fuelleSubZuordnungTabZumPflegen(EclInstiSubZuordnung[] eclInstiSubZuordnungArray,
            ScrollPane scpnZugeordneteInsti) {

        fuelleInstiAlle();
        cbBoxFuerInstiAlle = new CheckBox[eclInstiAlle.length];

        MeetingGridPane lSubZuordnungGridPane = new MeetingGridPane(1, 5, true, false, true, false);

        int anzInstiAlle = eclInstiAlle.length;

        int anzZugeordnet = eclInstiSubZuordnungArray.length;

        for (int i = 0; i < anzInstiAlle; i++) {
            EclInsti lInsti = eclInstiAlle[i];
            cbBoxFuerInstiAlle[i] = new CheckBox();

            boolean bereitsEnthalten = false;
            for (int i1 = 0; i1 < anzZugeordnet; i1++) {
                if (eclInstiSubZuordnungArray[i1].identSubInsti == lInsti.ident) {
                    bereitsEnthalten = true;
                }
            }
            cbBoxFuerInstiAlle[i].setSelected(bereitsEnthalten);

            if (lInsti.ident != aktuelleInstiIdent) {
                lSubZuordnungGridPane.addMeeting(cbBoxFuerInstiAlle[i], 0, i + 1);
            } else {
                Label leer = new Label();
                lSubZuordnungGridPane.addMeeting(leer, 0, i + 1);

            }

            Label lIdent = new Label(Integer.toString(lInsti.ident));
            lSubZuordnungGridPane.addMeeting(lIdent, 1, i + 1);

            Label lBezeichnung = new Label(lInsti.kurzBezeichnung);
            lSubZuordnungGridPane.addMeeting(lBezeichnung, 2, i + 1);

        }

        String[] ueberschrift = { "", "Ident", "Bezeichnung" };
        lSubZuordnungGridPane.setzeUeberschrift(ueberschrift, scpnZugeordneteInsti);

    }

    /**
     * Fuelle kennungen tab.
     */
    private void fuelleKennungenTab() {
        MeetingGridPane lkennungenGridPane = new MeetingGridPane(1, 5, true, false, true, false);

        int anzKennungen = 0;
        if (blInsti.rcUserLoginZuInsti != null) {
            anzKennungen = blInsti.rcUserLoginZuInsti.length;
        }

        for (int i = 0; i < anzKennungen; i++) {
            EclUserLogin lUserLogin = blInsti.rcUserLoginZuInsti[i];

            Label lIdent = new Label(Integer.toString(lUserLogin.userLoginIdent));
            lkennungenGridPane.addMeeting(lIdent, 0, i + 1);

            Label lKennung = new Label(lUserLogin.kennung);
            lkennungenGridPane.addMeeting(lKennung, 1, i + 1);

            Label lName = new Label(lUserLogin.name);
            lkennungenGridPane.addMeeting(lName, 2, i + 1);

        }

        String[] ueberschrift = { "Ident", "Kennung", "Name" };
        lkennungenGridPane.setzeUeberschrift(ueberschrift, pnKennungen);

    }
    //
    //	private void sperreTabDetails() {
    //
    //		if (aktuelleFunktion!=2 || sammelWarPraesent() || sammelHatAktionaere()) {
    //			cbAktiv.setDisable(true);
    //			cbAktiv.setStyle("-fx-opacity: 1");
    //		}
    //
    //		if (aktuelleFunktion!=2) {
    //			tfBezeichnung.setEditable(false);
    //			tfOrt.setEditable(false);
    //
    //			tfKommentar.setEditable(false);
    //		}
    //
    //		if (aktuelleFunktion!=2 || sammelWarPraesent() || sammelHatAktionaere()) {
    //			ComboBoxZusatz.sperre(cbGattungNr);
    //			ComboBoxZusatz.sperre(cbGattungKuerzel);
    //		}
    //
    //		if (aktuelleFunktion!=2 || sammelWarPraesent() || sammelHatAktionaere()) {
    //			cbKIAV.setDisable(true);
    //			cbKIAV.setStyle("-fx-opacity: 1");
    //
    //			cbSRV.setDisable(true);
    //			cbSRV.setStyle("-fx-opacity: 1");
    //
    //			cbOrga.setDisable(true);
    //			cbOrga.setStyle("-fx-opacity: 1");
    //
    //			cbBriefwahl.setDisable(true);
    //			cbBriefwahl.setStyle("-fx-opacity: 1");
    //
    //			cbDauervollmacht.setDisable(true);
    //			cbDauervollmacht.setStyle("-fx-opacity: 1");
    //		}
    //
    //		if (aktuelleFunktion!=2) {
    //			tfGruppe.setEditable(false);
    //		}
    //
    //		if (aktuelleFunktion!=2 || sammelWarPraesent() || sammelHatAktionaere()) {
    //			cbOhneWeisung.setDisable(true);
    //			cbOhneWeisung.setStyle("-fx-opacity: 1");
    //
    //			cbDedizierteWeisung.setDisable(true);
    //			cbDedizierteWeisung.setStyle("-fx-opacity: 1");
    //		}
    //
    //		if (aktuelleFunktion!=2) {
    //
    //			cbImInternet.setDisable(true);
    //			cbImInternet.setStyle("-fx-opacity: 1");
    //
    //			cbImPapier.setDisable(true);
    //			cbImPapier.setStyle("-fx-opacity: 1");
    //
    //			cbVerlassenHV.setDisable(true);
    //			cbVerlassenHV.setStyle("-fx-opacity: 1");
    //
    //
    //			cbVertreterAnbieten.setDisable(true);
    //			cbVertreterAnbieten.setStyle("-fx-opacity: 1");
    //
    //
    //			cbWeitergabeUneingeschraenkt.setDisable(true);
    //			cbWeitergabeUneingeschraenkt.setStyle("-fx-opacity: 1");
    //
    //			cbWeitergabeInGesamtsumme.setDisable(true);
    //			cbWeitergabeInGesamtsumme.setStyle("-fx-opacity: 1");
    //
    //			cbWeitergabeJeSammelkartenSumme.setDisable(true);
    //			cbWeitergabeJeSammelkartenSumme.setStyle("-fx-opacity: 1");
    //
    //			cbWeitergabeNichtErlaubt.setDisable(true);
    //			cbWeitergabeNichtErlaubt.setStyle("-fx-opacity: 1");
    //		}
    //
    //		if (aktuelleFunktion!=2 || sammelWarPraesent()) {
    //
    //			cbOffenlegungWieParameter.setDisable(true);
    //			cbOffenlegungWieParameter.setStyle("-fx-opacity: 1");
    //
    //			cbOhneOffenlegung.setDisable(true);
    //			cbOhneOffenlegung.setStyle("-fx-opacity: 1");
    //
    //			cbMitOffenlegung.setDisable(true);
    //			cbMitOffenlegung.setStyle("-fx-opacity: 1");
    //		}
    //	}
    //
    //	private void fuelleTabVertreterEK() {
    //		/*Vertreter-Pane*/
    //		MeetingGridPane lVertreterGridPane=new MeetingGridPane();
    //		if (willensErklVollmachtenAnDritte!=null) {
    //			int anz=willensErklVollmachtenAnDritte.length;
    //			btnStornierenVollmacht=new Button[anz];
    //			for (int i=0;i<anz;i++) {
    //				EclWillensErklVollmachtenAnDritte lWillensErklVollmachtenAnDritte=willensErklVollmachtenAnDritte[i];
    //
    //				btnStornierenVollmacht[i]=new Button("Stornieren");
    //				btnStornierenVollmacht[i].setOnAction(e->{clickedStornierenVollmacht(e);});
    //
    //				if (lWillensErklVollmachtenAnDritte.wurdeStorniert) {
    //					Label lStorniert=new Label("storniert");
    //					lVertreterGridPane.addMeeting(lStorniert, 0, i+1);
    //				}
    //				else {
    //					lVertreterGridPane.addMeeting(btnStornierenVollmacht[i], 0, i+1);
    //				}
    //
    //
    //				Label vertreterName=new Label();
    //				vertreterName.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.name);
    //				lVertreterGridPane.addMeeting(vertreterName, 1, i+1);
    //
    //				Label vertreterVorName=new Label();
    //				vertreterVorName.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.vorname);
    //				lVertreterGridPane.addMeeting(vertreterVorName, 2, i+1);
    //
    //				Label vertreterOrt=new Label();
    //				vertreterOrt.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ort);
    //				lVertreterGridPane.addMeeting(vertreterOrt, 3, i+1);
    //			}
    //
    //			String[] ueberschrift= {"","Name","Vorname","Ort"};
    //			lVertreterGridPane.setzeUeberschrift(ueberschrift, pnVertreter);
    //		}
    //
    //
    //		/*Eintrittskarten-Pane*/
    //		MeetingGridPane lEintrittskartenGridPane=new MeetingGridPane();
    //		if (zutrittskartenListe!=null) {
    //			int anz=zutrittskartenListe.length;
    //			btnStornierenZutrittskarte=new Button[anz];
    //			btnDruckenZutrittskarte=new Button[anz];
    //			for (int i=0;i<anz;i++) {
    //				EclZutrittskarten lZutrittskarten=zutrittskartenListe[i];
    //
    //				btnStornierenZutrittskarte[i]=new Button("Sperren");
    //				btnStornierenZutrittskarte[i].setOnAction(e->{clickedSperrenEK(e);});
    //
    //				btnDruckenZutrittskarte[i]=new Button("Drucken");
    //				btnDruckenZutrittskarte[i].setOnAction(e->{clickedDruckenEK(e);});
    //				if (lZutrittskarten.zutrittsIdentWurdeGesperrt()) {
    //					Label lStorniert=new Label("gesperrt");
    //					lEintrittskartenGridPane.addMeeting(lStorniert, 0, i+1);
    //				}
    //				else {
    //					lEintrittskartenGridPane.addMeeting(btnStornierenZutrittskarte[i], 0, i+1);
    //					lEintrittskartenGridPane.addMeeting(btnDruckenZutrittskarte[i], 1, i+1);
    //				}
    //
    //
    //				Label lZutrittsIdent=new Label();
    //				lZutrittsIdent.setText(lZutrittskarten.zutrittsIdent);
    //				lEintrittskartenGridPane.addMeeting(lZutrittsIdent, 2, i+1);
    //
    //				Label lZutrittsIdentNeben=new Label();
    //				lZutrittsIdentNeben.setText(lZutrittskarten.zutrittsIdentNeben);
    //				lEintrittskartenGridPane.addMeeting(lZutrittsIdentNeben, 3, i+1);
    //			}
    //
    //		}
    //
    //		pnEintrittskarten.setContent(lEintrittskartenGridPane);
    //
    //		/*Stimmkarten-Pane*/
    //		MeetingGridPane lStimmkartenGridPane=new MeetingGridPane();
    //		if (stimmkartenListe!=null) {
    //			int anz=stimmkartenListe.length;
    //			for (int i=0;i<anz;i++) {
    //				EclStimmkarten lStimmkarten=stimmkartenListe[i];
    //
    //				if (lStimmkarten.stimmkarteWurdeGesperrt()) {
    //					Label lStorniert=new Label("gesperrt");
    //					lStimmkartenGridPane.addMeeting(lStorniert, 0, i+1);
    //				}
    //
    //
    //				Label lStimmkarte=new Label();
    //				lStimmkarte.setText(lStimmkarten.stimmkarte);
    //				lStimmkartenGridPane.addMeeting(lStimmkarte, 1, i+1);
    //			}
    //		}
    //
    //		pnStimmkarten.setContent(lStimmkartenGridPane);
    //	}
    //

    /**
     * Disable tab kontakte kennungen.
     */
    private void disableTabKontakteKennungen() {
        tabKontakteKennungen.setDisable(true);
    }

    /**
     * Disable tab tatsaechliche.
     */
    private void disableTabTatsaechliche() {
        tabZugeordneteInstiTatsaechlich.setDisable(true);
    }

    /**
     * Disable tab bestaende.
     */
    private void disableTabBestaende() {
        tabBestaende.setDisable(true);
    }

    /**
     * Disable tab ueblich.
     */
    private void disableTabUeblich() {
        tabZugeordneteInstiUeblich.setDisable(true);
    }

    /**
     * Sperre tabs insti.
     */
    private void sperreTabsInsti() {
        /*Tab Details*/
        tfKurzBezeichnung.setEditable(false);
        taSuchlaufBegriffe.setEditable(false);
        cbStandardSammelkarteOhneWeisung.setDisable(true);
        cbStandardSammelkarteSRV.setDisable(true);
        cbStandardSammelkarteBriefwahl.setDisable(true);
        cbStandardSammelkarteDauervollmacht.setDisable(true);
        cbStandardSammelkarteMitWeisung.setDisable(true);
        tfStandardSammelkarteGruppennummer.setEditable(false);
        tfStandardMeldeName.setEditable(false);
        tfStandardMeldeOrt.setEditable(false);
        tfFestadressenNummer.setEditable(false);

        /*Tab Kontakte*/
        btnNeuerKontakt.setDisable(true);
        btnKontaktPflegen.setDisable(true);

        /*Tab Zugeordnete Instis - Üblich*/
        btnPflegenUeblich.setVisible(false);

    }

    //	int fuelleTabWeisungssummenZeile=1;
    //	private void fuelleTabWeisungssummen() {
    //		int aktuelleGattung=aktuelleSammelMeldung.liefereGattung();
    //
    //		MeetingGridPane lWeisungenGridPane=new MeetingGridPane();
    //		fuelleTabWeisungssummenZeile=1;
    //
    //		fuelleTabWeisungssummenZeigeBereich(lWeisungenGridPane, CInjects.weisungsAgenda[aktuelleGattung].getAbstimmungenListeM(), 1);
    //		fuelleTabWeisungssummenZeigeBereich(lWeisungenGridPane, CInjects.weisungsAgenda[aktuelleGattung].getGegenantraegeListeM(), 2);
    //
    //		String[] ueberschrift= new String[12];
    //		int spalte=3;
    //		for (int i1=0;i1<=9;i1++){
    //   			if (i1!=KonstStimmart.splitLiegtVor) {
    //   				ueberschrift[spalte]=KonstStimmart.getText(i1);
    //   				spalte++;
    //  			}
    //		}
    //		lWeisungenGridPane.setzeUeberschrift(ueberschrift, scpnWeisungen);
    //
    //	}
    //
    //
    //	/**pArt=
    //	 * 	1 => "Normale" Agenda
    //	 *  2 => Gegenanträge
    //	 */
    //	private void fuelleTabWeisungssummenZeigeBereich(MeetingGridPane lWeisungenGridPane, List<EclAbstimmungM> abstimmungenListeM, int pArt) {
    //
    //		int anzahlAbstimmungen=abstimmungenListeM.size();
    //		if (pArt==2 && anzahlAbstimmungen>0) {//Bei Gegenanträgen: als erstes Überschriftszeile
    //			Label zUberschrift=new Label("Gegenanträge:");
    //          	lWeisungenGridPane.addMeeting(zUberschrift, 2, fuelleTabWeisungssummenZeile);
    //          	fuelleTabWeisungssummenZeile++;
    //		}
    //        for (int i=0;i<anzahlAbstimmungen/*CInjects.weisungsAgendaAnzAgenda[aktuelleGattung]*/;i++){
    //        	Label lblNummer=new Label(abstimmungenListeM.get(i).getNummer());
    //        	lWeisungenGridPane.addMeeting(lblNummer, 0, fuelleTabWeisungssummenZeile);
    //
    //        	Label lblNummerindex=new Label(abstimmungenListeM.get(i).getNummerindex());
    //        	lWeisungenGridPane.addMeeting(lblNummerindex, 1, fuelleTabWeisungssummenZeile);
    //
    //     		Label lblAgendaText=new Label(abstimmungenListeM.get(i).getAnzeigeBezeichnungLang());
    //     		lblAgendaText.setWrapText(true);
    //     		lblAgendaText.setMaxWidth(200);
    //           	lWeisungenGridPane.addMeeting(lblAgendaText, 2, fuelleTabWeisungssummenZeile);
    //
    //           	int abstimmungsPosition=abstimmungenListeM.get(i).getIdentWeisungssatz();
    //         	if (abstimmungsPosition!=-1) {
    //           		/*Erst ermitteln, ob Fehler in gesamter Summe - denn dann ganze Zeile rot!*/
    //          		boolean fehlerSummeGesamt=false; //Quersumme der Split-Weisungen dieses TOPs stimmen nicht mit Anzahl Stimmen in Sammelkarte überein
    //          		long summeGesamt=0;
    //           		for (int i1=0;i1<=9;i1++) {
    //           			if (i1!=KonstStimmart.splitLiegtVor) {
    //           				summeGesamt+=weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //            			}
    //           		}
    //           		if (summeGesamt!=aktuelleSammelMeldung.stimmen) {fehlerSummeGesamt=true;}
    //
    //           		int spalte=3;
    //           		for (int i1=0;i1<=9;i1++) {
    //           			if (i1!=KonstStimmart.splitLiegtVor) {
    //           				long wertSammelkarte=weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //           				long wertAktionaere=aktionaersSummen.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
    //           				boolean fehlerTOP=false;
    //           				if (wertSammelkarte!=wertAktionaere) {fehlerTOP=true;}
    //           				Label lblWert=new Label(CaString.toStringDE(wertSammelkarte));
    //           				if (fehlerTOP) {
    //           					lblWert.setTextFill(Color.web(KonstFarben.rot));
    //           				}
    //           	           	lWeisungenGridPane.addMeeting(lblWert, spalte, fuelleTabWeisungssummenZeile);
    //           	           	spalte++;
    //         			}
    //           		}
    //           		if (fehlerSummeGesamt) {
    //           			Label lblFehler=new Label("Fehler Quersumme="+CaString.toStringDE(summeGesamt));
    //           			lblFehler.setTextFill(Color.web(KonstFarben.rot));
    //           			lWeisungenGridPane.addMeeting(lblFehler, spalte, fuelleTabWeisungssummenZeile);
    //           		}
    //         	}
    //         	fuelleTabWeisungssummenZeile++;
    //       }
    //
    //	}
    //
    //
    //	private void sperreTabWeisungssummen() {
    //		/*Platzhalterfunktion :-) - nichts zu sperren, da nix zu ändern ...*/
    //	}
    //
    //
    //	private void fuelleTabsAktionaere() {
    //
    //		listAktionaereAktiv=FXCollections.observableArrayList();
    //		System.out.println("aktionaereAktiv.size()="+aktionaereAktiv.size());
    //		for (int i=0;i<aktionaereAktiv.size();i++) {
    //			aktionaereAktiv.get(i).belegeAbgabeText();
    //			listAktionaereAktiv.add(aktionaereAktiv.get(i));
    //		}
    //		listAktionaereInaktiv=FXCollections.observableArrayList();
    //		for (int i=0;i<aktionaereInaktiv.size();i++) {
    //			aktionaereInaktiv.get(i).belegeAbgabeText();
    //			listAktionaereInaktiv.add(aktionaereInaktiv.get(i));
    //		}
    //		listAktionaereWiderrufen=FXCollections.observableArrayList();
    //		for (int i=0;i<aktionaereWiderrufen.size();i++) {
    //			aktionaereWiderrufen.get(i).belegeAbgabeText();
    //			listAktionaereWiderrufen.add(aktionaereWiderrufen.get(i));
    //		}
    //		listAktionaereGeaendert=FXCollections.observableArrayList();
    //		for (int i=0;i<aktionaereGeaendert.size();i++) {
    //			aktionaereGeaendert.get(i).belegeAbgabeText();
    //			listAktionaereGeaendert.add(aktionaereGeaendert.get(i));
    //		}
    //
    //		fuelleTabsAktionareEinzeln(1, scpnMeldungenAktiv);
    //		fuelleTabsAktionareEinzeln(2, scpnMeldungenInaktiv);
    //		fuelleTabsAktionareEinzeln(3, scpnMeldungenWiderrufen);
    //		fuelleTabsAktionareEinzeln(4, scpnMeldungenGeaendert);
    //	}
    //
    //
    //
    //	/**pArt=
    //	 * 	1=aktive
    //	 * 	2=inaktive
    //	 * 	3=widerrufen
    //	 * 	4=Geaendert
    //	 */
    //	private void fuelleTabsAktionareEinzeln(int pArt, ScrollPane pPaneAktionaere) {
    //
    //		CtrlSammelkartenUebergreifend ctrlSammelkartenUebergreifend=new CtrlSammelkartenUebergreifend();
    //		TableView<EclMeldungMitAktionaersWeisung> tableAktionaere=ctrlSammelkartenUebergreifend.vorbereitenTableViewAktionaere(aktuelleSammelMeldung.liefereGattung());
    //
    //        tableAktionaere.setPrefHeight(500);
    //        tableAktionaere.setPrefWidth(1464);
    //
    //        pPaneAktionaere.setContent(tableAktionaere);
    //
    //        switch (pArt) {
    //        case 1:tableAktionaere.setItems(listAktionaereAktiv);
    //        tableAktionaere.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    //        tableAktionaereAktiv=tableAktionaere;
    //        break;
    //        case 2:tableAktionaere.setItems(listAktionaereInaktiv);break;
    //        case 3:tableAktionaere.setItems(listAktionaereWiderrufen);break;
    //        case 4:tableAktionaere.setItems(listAktionaereGeaendert);break;
    //        }
    //	}
    //
    //
    //	private void sperreTabsAktionaere() {
    //		/*Platzhalterfunktion :-) - nichts zu sperren, da nix zu ändern ...*/
    //	}
    //
    /**
     * ******************************Eingabereaktionen*******************************.
     *
     * @param event the event
     */

    @FXML
    /*TODO Instipflege: offen, ob hier noch gesperrt werden muß wenn Pflege auf diesem Server nicht zulässig*/
    void onBtnPflegenUeblich(ActionEvent event) {

        btnSpeichernUeblich.setVisible(true);
        btnPflegenUeblich.setVisible(false);
        fuelleSubZuordnungTabZumPflegen(eclInstiSubZuordnungUeblichArray, scpnZugeordneteInstiUeblich);
    }

    /**
     * On btn speichern ueblich.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernUeblich(ActionEvent event) {

        btnSpeichernUeblich.setVisible(false);
        btnPflegenUeblich.setVisible(true);

        List<EclInstiSubZuordnung> lInstiSubZuordnungListe = erzeugeZuordnungAusAnzeige(true);

        blInsti.speichereInstiSubZuordnung(true, aktuelleInstiIdent, lInstiSubZuordnungListe);
        refreshInhalte();
    }

    /**
     * On btn pflegen tatsaechlich.
     *
     * @param event the event
     */
    @FXML
    /*TODO Instipflege: offen, ob hier noch gesperrt werden muß wenn Pflege auf diesem Server nicht zulässig*/
    void onBtnPflegenTatsaechlich(ActionEvent event) {

        btnSpeichernTatsaechlich.setVisible(true);
        btnPflegenTatsaechlich.setVisible(false);
        if (eclInstiSubZuordnungTatsaechlichArray.length == 0) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                    "Keine Zuordnung vorhanden - Übliche Zuordnung übernehmen?", "Ja", "Nein");
            if (bRc == true) {
                eclInstiSubZuordnungTatsaechlichArray = eclInstiSubZuordnungUeblichArray;
            }
        }
        fuelleSubZuordnungTabZumPflegen(eclInstiSubZuordnungTatsaechlichArray, scpnZugeordneteInstiTatsaechlich);
    }

    /**
     * On btn speichern tatsaechlich.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernTatsaechlich(ActionEvent event) {

        btnSpeichernTatsaechlich.setVisible(false);
        btnPflegenTatsaechlich.setVisible(true);

        List<EclInstiSubZuordnung> lInstiSubZuordnungListe = erzeugeZuordnungAusAnzeige(false);

        blInsti.speichereInstiSubZuordnung(false, aktuelleInstiIdent, lInstiSubZuordnungListe);
        refreshInhalte();
    }

    /**
     * On btn bestaende aendern meldungen.
     *
     * @param event the event
     */
    @FXML
    void onBtnBestaendeAendernMeldungen(ActionEvent event) {
        rufeBestandsAendernAuf(KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen, "Zuordnung aus Meldebestand");
    }

    /**
     * On btn bestaende aendern register.
     *
     * @param event the event
     */
    @FXML
    void onBtnBestaendeAendernRegister(ActionEvent event) {
        rufeBestandsAendernAuf(KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister, "Zuordnung aus Register");
    }

    /**
     * On btn bestand importieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnBestandImportieren(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNichtFertig(eigeneStage);
    }

    /**
     * On btn bestand manuell eingeben.
     *
     * @param event the event
     */
    @FXML
    void onBtnBestandManuellEingeben(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNichtFertig(eigeneStage);
    }

    /**
     * On btn kontakt pflegen.
     *
     * @param event the event
     */
    @FXML
    void onBtnKontaktPflegen(ActionEvent event) {
        if (ParamS.paramServer.instisPflegbar==0) {
            zeigeFehlermeldung("Grund-Daten der Instis können auf diesem Server nicht geändert werden!");
            return;
        }

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNichtFertig(eigeneStage);
    }

    /**
     * On btn neuer kontakt.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeuerKontakt(ActionEvent event) {
        if (ParamS.paramServer.instisPflegbar==0) {
            zeigeFehlermeldung("Grund-Daten der Instis können auf diesem Server nicht geändert werden!");
            return;
        }

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNichtFertig(eigeneStage);
    }

    /**
     * On btn sammel anmeldebogen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelAnmeldebogen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlInstiPflegeDetailsDruckSammelAnmeldebogen controllerFenster = new CtrlInstiPflegeDetailsDruckSammelAnmeldebogen();

        controllerFenster.init(neuerDialog, aktuelleInsti);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/RootDrucklauf.fxml", 1140, 700,
                "Druck Sammelanmeldebogen", true);
    }

    /**
     * On btn reg zuordnung stornieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnRegZuordnungStornieren(ActionEvent event) {
        storniereAusTable(1);
    }

    /**
     * On btn meld zuordnung stornieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnMeldZuordnungStornieren(ActionEvent event) {
        storniereAusTable(2);
    }

    /**
     * Storniere aus table.
     *
     * @param registerOderMeldung the register oder meldung
     */
    private void storniereAusTable(int registerOderMeldung) {
        ObservableList<Integer> ausgewaehlteZuordnungenIndex = null;
        ObservableList<MInstiBestandsZuordnung> listAktiv = null;

        EclInstiBestandsZuordnung[] zuordnungQuelle = null;
        if (registerOderMeldung == 1) {
            ausgewaehlteZuordnungenIndex = tableReg.getSelectionModel().getSelectedIndices();
            listAktiv = listReg;
            zuordnungQuelle = blInsti.rcRegInstiBestandsZuordnung;

        } else {
            ausgewaehlteZuordnungenIndex = tableMeld.getSelectionModel().getSelectedIndices();
            listAktiv = listMeld;
            zuordnungQuelle = blInsti.rcMeldInstiBestandsZuordnung;
        }

        int anzahlAusgewaehlteZuordnungen = ausgewaehlteZuordnungenIndex.size();

        EclInstiBestandsZuordnung[] zuordnungenLoeschen = new EclInstiBestandsZuordnung[anzahlAusgewaehlteZuordnungen];
        for (int i = 0; i < anzahlAusgewaehlteZuordnungen; i++) {
            MInstiBestandsZuordnung lMInstiBestandsZuordnung = listAktiv.get(ausgewaehlteZuordnungenIndex.get(i));
            for (int i1 = 0; i1 < zuordnungQuelle.length; i1++) {
                EclInstiBestandsZuordnung lVergleichsZuordnung = zuordnungQuelle[i1];
                if (lVergleichsZuordnung.ident == lMInstiBestandsZuordnung.ident
                        && lVergleichsZuordnung.identUserLogin == lMInstiBestandsZuordnung.identUserLogin) {
                    zuordnungenLoeschen[i] = lVergleichsZuordnung;
                }
            }
        }

        blInsti.loeschenZuordnung(zuordnungenLoeschen);

        blInsti.fuelleInstiBestandsZuordnung(aktuelleInstiIdent);
        fuelleTabDetails();
    }

    /**
     * Rufe bestands aendern auf.
     *
     * @param pVerwendung the verwendung
     * @param pTitel      the titel
     */
    private void rufeBestandsAendernAuf(int pVerwendung, String pTitel) {
        BlSuchlauf blSuchlauf = new BlSuchlauf(false, dbBundle);
        int registerOderMeldung = 0;
        if (pVerwendung == KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister) {
            registerOderMeldung = 1;
        } else {
            registerOderMeldung = 2;
        }
        blSuchlauf.leseEinGgfAnlegeSuchlaufDefinition(aktuelleInsti, registerOderMeldung);

        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        CtrlSuchlaufDurchfuehren controllerFenster = new CtrlSuchlaufDurchfuehren();
        controllerFenster.titel = pTitel;
        controllerFenster.buttonVerarbeiten1 = "Verarbeiten: Komplett zuordnen";
        if (pVerwendung == KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister) {
            controllerFenster.buttonVerarbeiten2 = "Verarbeiten: Teilbestand zuordnen";
        }
        controllerFenster.blInsti = blInsti;
        controllerFenster.aktuelleInsti = aktuelleInsti;

        controllerFenster.init(newStage, pVerwendung, blSuchlauf);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/SuchlaufDurchfuehren.fxml", 1500, 760,
                pTitel, true);

        refreshInhalte();

    }

    /**
     * On btn speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichern(ActionEvent event) {
        if (ParamS.paramServer.instisPflegbar==0 && aktuelleFunktion!=3) {
            zeigeFehlermeldung("Grund-Daten der Instis können auf diesem Server nicht geändert werden!");
            return;
        }


        /*Prüfen*/
        lFehlertext = "";
        pruefeNichtLeer(tfKurzBezeichnung, "Kurzbezeichnung");
        pruefeLaenge(tfKurzBezeichnung, "Kurzbezeichnung", 80);

        pruefeZahlOderLeer(tfStandardSammelkarteGruppennummer, "Sammelkarte Gruppennummer");
        pruefeLaenge(taSuchlaufBegriffe, "Suchbegriffe", 200);

        pruefeLaenge(tfStandardMeldeName, "Standard-Meldename", 80);
        pruefeLaenge(tfStandardMeldeOrt, "Standard Melde-Ort", 80);

        pruefeZahlOderLeer(tfFestadressenNummer, "Festadressen-Nummer");

        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }

        aktuelleInsti.kurzBezeichnung = tfKurzBezeichnung.getText();

        if (cbStandardSammelkartenAnlageAktiv.isSelected()) {
            aktuelleInsti.standardSammelkartenAnlageAktiv = 1;
        } else {
            aktuelleInsti.standardSammelkartenAnlageAktiv = 0;
        }

        if (cbStandardSammelkarteMitWeisung.isSelected()) {
            aktuelleInsti.standardSammelkarteMitWeisung = 1;
        } else {
            aktuelleInsti.standardSammelkarteMitWeisung = 0;
        }
        if (cbStandardSammelkarteOhneWeisung.isSelected()) {
            aktuelleInsti.standardSammelkarteOhneWeisung = 1;
        } else {
            aktuelleInsti.standardSammelkarteOhneWeisung = 0;
        }
        if (cbStandardSammelkarteSRV.isSelected()) {
            aktuelleInsti.standardSammelkarteSRV = 1;
        } else {
            aktuelleInsti.standardSammelkarteSRV = 0;
        }
        if (cbStandardSammelkarteBriefwahl.isSelected()) {
            aktuelleInsti.standardSammelkarteBriefwahl = 1;
        } else {
            aktuelleInsti.standardSammelkarteBriefwahl = 0;
        }
        if (cbStandardSammelkarteDauervollmacht.isSelected()) {
            aktuelleInsti.standardSammelkarteDauervollmacht = 1;
        } else {
            aktuelleInsti.standardSammelkarteDauervollmacht = 0;
        }

        if (rbNurInGesammtsumme.isSelected()) {
            aktuelleInsti.weisungsWeitergabe = 1;
        }
        if (rbNurInSummeJeSammelkarte.isSelected()) {
            aktuelleInsti.weisungsWeitergabe = 2;
        }
        if (rbNichtErlaubt.isSelected()) {
            aktuelleInsti.weisungsWeitergabe = 3;
        }
        if (rbUneingeschraenkt.isSelected()) {
            aktuelleInsti.weisungsWeitergabe = 0;
        }

        if (!tfStandardSammelkarteGruppennummer.getText().isEmpty()) {
            aktuelleInsti.standardSammelkarteGruppennummer = Integer
                    .parseInt(tfStandardSammelkarteGruppennummer.getText());
        } else {
            aktuelleInsti.standardSammelkarteGruppennummer = 0;
        }

        aktuelleInsti.standardMeldeName = tfStandardMeldeName.getText();
        aktuelleInsti.standardMeldeOrt = tfStandardMeldeOrt.getText();

        if (!tfFestadressenNummer.getText().isEmpty()) {
            aktuelleInsti.festadressenNummer = Integer.parseInt(tfFestadressenNummer.getText());
        } else {
            aktuelleInsti.festadressenNummer = 0;
        }

        aktuelleInsti.suchlaufBegriffe = taSuchlaufBegriffe.getText();

        if (aktuelleFunktion == 1) {/*Neu-Anlage*/
            /*Speichern*/
            blInsti.neueInsti();
            blInsti.insti = aktuelleInsti;
            blInsti.neueInstiSpeichern();
            eigeneStage.hide();

            return;
        }

        if (aktuelleFunktion == 2) { /*Ändern*/
            int rc = blInsti.aendernInstiSpeichern(aktuelleInsti, suchlaufBegriffe);
            aktuelleInsti = blInsti.insti;
            suchlaufBegriffe = blInsti.rcSuchlaufBegriffe;
            if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                this.fehlerMeldung("Geänderte Daten konnten nicht gespeichert werden - von anderem Benutzer geändert!");
            }

            eigeneStage.hide();
            return;
        }

        eigeneStage.hide();
    }

    //	@FXML
    //	void clickedStornierenVollmacht(ActionEvent event) {
    //		int rc=findeButton(btnStornierenVollmacht, event);
    //		EclWillensErklVollmachtenAnDritte lWillensErklVollmachtenAnDritte=willensErklVollmachtenAnDritte[rc];
    //
    //		CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
    //		boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage,
    //				"Vollmacht "+lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.name+" stornieren?",
    //				"Ja", "Nein");
    //		if (brc) {
    //			dbBundle=new DbBundle();
    //			BlSammelkarten blSammelkarten=new BlSammelkarten(false, dbBundle);
    //			blSammelkarten.storniereBestehenderVertreterFuerSammelkarte(aktuelleSammelIdent, lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ident);
    //
    //			fuelleSammelkartenZusatzdaten();
    //			zeigeSammelkartenInTableView();
    //			fuelleTabVertreterEK();
    //		}
    //
    //
    //	}
    //
    //	@FXML
    //	void clickedSperrenEK(ActionEvent event) {
    //		int rc=findeButton(btnStornierenZutrittskarte, event);
    //		EclZutrittskarten lZutrittskarte=zutrittskartenListe[rc];
    //
    //		CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
    //		boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage,
    //				"Eintrittskarte "+lZutrittskarte.zutrittsIdent+" sperren?",
    //				"Ja", "Nein");
    //		if (!brc) {return;}
    //		if (aktuelleSammelMeldung.statusWarPraesenz==1) {
    //			brc=caZeigeHinweis.zeige2Buttons(eigeneStage,
    //					"Achtung! Sammelkarte ist oder war bereits präsent! Eintrittskarte dennoch sperren???",
    //					"Ja", "Nein");
    //		}
    //		if (brc) {
    //			dbBundle=new DbBundle();
    //			BlSammelkarten blSammelkarten=new BlSammelkarten(false, dbBundle);
    //			blSammelkarten.sperreEKderSammelkarte(aktuelleSammelIdent, lZutrittskarte.zutrittsIdent);
    //
    //			fuelleSammelkartenZusatzdaten();
    //			zeigeSammelkartenInTableView();
    //			fuelleTabVertreterEK();
    //		}
    //	}
    //
    //	void clickedDruckenEK(ActionEvent event) {
    //		this.fehlerMeldung("Derzeit noch nicht implementiert. Zum Drucken einer Eintrittskarte für eine Sammelkarte bitte das Modul Servicedesk benutzen!");
    //	}
    //
    //	@FXML
    //	void onBtnVertreter(ActionEvent event) {
    //		Stage newStage=new Stage();
    //		CtrlSammelkartenNeuerVertreter controllerFenster=
    //				new CtrlSammelkartenNeuerVertreter();
    //		controllerFenster.init(newStage, aktuelleSammelMeldung, willensErklVollmachtenAnDritte);
    //
    //		CaController caController=new CaController();
    //		caController.open(newStage, controllerFenster, "/meetingBestand/SammelkartenNeuerVertreter.fxml", 1100, 680, "Neuer Vertreter", true);
    //
    //		fuelleSammelkartenZusatzdaten();
    //		zeigeSammelkartenInTableView();
    //		fuelleTabVertreterEK();
    //
    //	}
    //
    //	@FXML
    //	void onBtnNeueEintrittskarte(ActionEvent event) {
    //		Stage newStage=new Stage();
    //		CtrlSammelkartenNeueEK controllerFenster=
    //				new CtrlSammelkartenNeueEK();
    //		controllerFenster.init(newStage, aktuelleSammelMeldung);
    //
    //		CaController caController=new CaController();
    //		caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/de/meetingapps/meetingclient/meetingBestand/SammelkartenNeueEK.fxml", 920, 340, "Neue Eintrittskarte", true);
    //
    //		fuelleSammelkartenZusatzdaten();
    //		zeigeSammelkartenInTableView();
    //		fuelleTabVertreterEK();
    //
    //	}
    //
    //
    //    @FXML
    //    void clickedUmbuchen(ActionEvent event) {
    //    	ObservableList<Integer> ausgewaehlteAktionaereIndex=null;
    //    	ausgewaehlteAktionaereIndex = this.tableAktionaereAktiv.getSelectionModel().getSelectedIndices();
    //    	int anzahlAusgewaehlteAktionaere=ausgewaehlteAktionaereIndex.size();
    //		if (anzahlAusgewaehlteAktionaere==0){
    //			this.fehlerMeldung("Bitte umzubuchende Aktionäre auswählen!");
    //			return;
    //		}
    //		int[] ausgewaehlteAktionaereMeldung=new int[anzahlAusgewaehlteAktionaere];
    //		EclWeisungMeldung[] ausgewaehlteAktionaereMeldungWeisung=new EclWeisungMeldung[anzahlAusgewaehlteAktionaere];
    //		EclMeldungMitAktionaersWeisung[] ausgewaehlteMeldungMitAktionaersWeisung=new EclMeldungMitAktionaersWeisung[anzahlAusgewaehlteAktionaere];
    //		for (int i=0;i<anzahlAusgewaehlteAktionaere;i++) {
    //			ausgewaehlteMeldungMitAktionaersWeisung[i]=listAktionaereAktiv.get(ausgewaehlteAktionaereIndex.get(i));
    //			ausgewaehlteAktionaereMeldung[i]=listAktionaereAktiv.get(ausgewaehlteAktionaereIndex.get(i)).meldungsIdent;
    //			ausgewaehlteAktionaereMeldungWeisung[i]=listAktionaereAktiv.get(ausgewaehlteAktionaereIndex.get(i)).eclWeisungMeldung;
    //		}
    //
    //		Stage newStage=new Stage();
    //		CtrlSammelkartenStapelbuchen controllerFenster=
    //				new CtrlSammelkartenStapelbuchen();
    //		/*Hinweis: die Argumente haben einige Informationen redundant. Wurde gemacht, damit nicht alles nochmal getestet werden muß ...*/
    //		controllerFenster.init(newStage, aktuelleSammelMeldung, ausgewaehlteAktionaereMeldung, ausgewaehlteAktionaereMeldungWeisung, ausgewaehlteMeldungMitAktionaersWeisung);
    //
    //		CaController caController=new CaController();
    //		caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/SammelkartenStapelbuchen.fxml", 1200, 720, "Stapel-Buchung", true);
    //
    //		fuelleSammelkartenZusatzdaten();
    //		zeigeSammelkartenInTableView();
    //		fuelleTabsAktionaere();
    //
    //
    //    }
    //
    //	/***************EingabeListener**************************************
    //	 * Grundsätzlich:
    //	 * Listener für die Auswahl der Gattung überträgt die Eingabe von Gattungsnummer/
    //	 * Gattungskürzel / Gattungslangtext jeweils in das andere Feld.
    //	 *
    //	 * Alle Listener:
    //	 * > aktualisieren immer aktuelleSammelMeldung
    //	 * > aktuelaisieren im Änderungsmodus auch aktuelleMSammelkarteM und zeigt diese
    //	 * 		neu in Kopfzeile an*/
    //	private void setzeListener() {
    //
    //		cbAktiv.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	int hAktiv=0;
    //		    	if (cbAktiv.isSelected()) {hAktiv=1;}
    //		    	aktuelleSammelMeldung.meldungAktiv=hAktiv;
    //		    	aktuelleMSammelkarte.belegeMeldungAktiv(aktuelleSammelMeldung);
    //		    	zeigeListSammelkartenNeuAn();
    //		    }
    //		});
    //
    //		tfBezeichnung.textProperty().addListener((obs, oldText, newText) -> {
    //			aktuelleSammelMeldung.name=tfBezeichnung.getText();
    //			aktuelleMSammelkarte.belegeNameOrt(aktuelleSammelMeldung);
    //			zeigeListSammelkartenNeuAn();
    //		});
    //		tfOrt.textProperty().addListener((obs, oldText, newText) -> {
    //			aktuelleSammelMeldung.ort=tfOrt.getText();
    //			aktuelleMSammelkarte.belegeNameOrt(aktuelleSammelMeldung);
    //			zeigeListSammelkartenNeuAn();
    //		});
    //		tfKommentar.textProperty().addListener((obs, oldText, newText) -> {
    //			aktuelleSammelMeldung.zusatzfeld2=tfKommentar.getText();
    //			aktuelleMSammelkarte.kommentar=aktuelleSammelMeldung.zusatzfeld2;
    //			zeigeListSammelkartenNeuAn();
    //		});
    //
    //		cbGattungNr.valueProperty().addListener(new ChangeListener<String>() {
    //			@Override
    //			public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
    //				if (neuerWert==null){return;} /*Entsteht, wenn COmbobox neu aufgebaut wird*/
    //				if (gattungUpdaten && CaString.isNummern(neuerWert)) {
    //					gattungUpdaten=false;
    //					int hGattung=Integer.parseInt(neuerWert);
    //					cbGattungKuerzel.setValue(ParamS.param.paramBasis.getGattungBezeichnungKurz(hGattung));
    //					tfGattungLang.setText(ParamS.param.paramBasis.getGattungBezeichnung(hGattung));
    //					gattungUpdaten=true;
    //
    //					aktuelleSammelMeldung.gattung=hGattung;
    //					aktuelleMSammelkarte.gattungString=ParamS.param.paramBasis.getGattungBezeichnungKurz(hGattung);
    //					zeigeListSammelkartenNeuAn();
    //				}
    //			}
    //		});
    //		cbGattungKuerzel.valueProperty().addListener(new ChangeListener<String>() {
    //            @Override
    //            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
    //            	if (neuerWert==null){return;} /*Entsteht, wenn COmbobox neu aufgebaut wird*/
    //            	if (gattungUpdaten && !neuerWert.isEmpty()) {
    //            		gattungUpdaten=false;
    //            		int hGattung=0;
    //            		for (int i=1;i<=5;i++) {
    //            			if (ParamS.param.paramBasis.getGattungBezeichnungKurz(i).equals(neuerWert)) {
    //            				hGattung=i;
    //            			}
    //            		}
    //            		if (hGattung!=0) {
    //            			cbGattungNr.setValue(Integer.toString(hGattung));
    //            			tfGattungLang.setText(ParamS.param.paramBasis.getGattungBezeichnung(hGattung));
    //
    //            			aktuelleSammelMeldung.gattung=hGattung;
    //            			aktuelleMSammelkarte.gattungString=ParamS.param.paramBasis.getGattungBezeichnungKurz(hGattung);
    //            			zeigeListSammelkartenNeuAn();
    //            		}
    //            		gattungUpdaten=true;
    //            	}
    //              }
    //        });
    //		gattungUpdaten=true;
    //
    //		cbKIAV.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	changedSammelkartenArt();
    //		    }
    //		});
    //		cbSRV.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	changedSammelkartenArt();
    //		    }
    //		});
    //		cbOrga.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	changedSammelkartenArt();
    //		    }
    //		});
    //		cbBriefwahl.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	changedSammelkartenArt();
    //		    }
    //		});
    //		cbDauervollmacht.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //		    	changedSammelkartenArt();
    //		    }
    //		});
    //
    //		tfGruppe.textProperty().addListener((obs, oldText, newText) -> {
    //			if (CaString.isNummern(tfGruppe.getText())) {
    //				aktuelleSammelMeldung.gruppe=Integer.parseInt(tfGruppe.getText());
    //				aktuelleMSammelkarte.gruppe=aktuelleSammelMeldung.gruppe;
    //				zeigeListSammelkartenNeuAn();
    //			}
    //		});
    //
    //		cbOhneWeisung.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedCbWeisung();
    //			}
    //		});
    //		cbDedizierteWeisung.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //		    @Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedCbWeisung();
    //			}
    //		});
    //
    //		cbImInternet.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				if (cbImInternet.isSelected()) {aktuelleSammelMeldung.skBuchbarInternet=1;}
    //				else {aktuelleSammelMeldung.skBuchbarInternet=0;}
    //				aktuelleMSammelkarte.belegeSkBuchbarInternetString(aktuelleSammelMeldung);
    //				zeigeListSammelkartenNeuAn();
    //			}
    //		});
    //		cbImPapier.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				if (cbImInternet.isSelected()) {aktuelleSammelMeldung.skBuchbarPapier=1;}
    //				else {aktuelleSammelMeldung.skBuchbarPapier=0;}
    //				aktuelleMSammelkarte.belegeSkBuchbarPapierString(aktuelleSammelMeldung);
    //				zeigeListSammelkartenNeuAn();
    //			}
    //		});
    //		cbVerlassenHV.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				if (cbVerlassenHV.isSelected()) {aktuelleSammelMeldung.skBuchbarHV=1;}
    //				else {aktuelleSammelMeldung.skBuchbarHV=0;}
    //				aktuelleMSammelkarte.belegeSkBuchbarHV(aktuelleSammelMeldung);
    //				zeigeListSammelkartenNeuAn();
    //			}
    //		});
    //		cbVertreterAnbieten.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				if (cbImInternet.isSelected()) {aktuelleSammelMeldung.skBuchbarVollmachtDritteHV=1;}
    //				else {aktuelleSammelMeldung.skBuchbarVollmachtDritteHV=0;}
    //				aktuelleMSammelkarte.belegeSkBuchbarVollmachtDritteHV(aktuelleSammelMeldung);
    //				zeigeListSammelkartenNeuAn();
    //			}
    //		});
    //
    //		cbWeitergabeInGesamtsumme.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedWeitergabeWeisungen();
    //			}
    //		});
    //		cbWeitergabeJeSammelkartenSumme.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedWeitergabeWeisungen();
    //			}
    //		});
    //		cbWeitergabeNichtErlaubt.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedWeitergabeWeisungen();
    //			}
    //		});
    //		cbWeitergabeUneingeschraenkt.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedWeitergabeWeisungen();
    //			}
    //		});
    //
    //		cbOffenlegungWieParameter.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedOffenlegung();
    //			}
    //		});
    //		cbOhneOffenlegung.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedOffenlegung();
    //			}
    //		});
    //		cbMitOffenlegung.selectedProperty().addListener(new ChangeListener<Boolean>() {
    //			@Override
    //			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
    //				changedOffenlegung();
    //			}
    //		});
    //
    //	}
    //
    //	private void zeigeListSammelkartenNeuAn() {
    //		/*Nur, wenn nicht im Neuaufnahme-Modus ist*/
    //		if (aktuelleFunktion!=1) {
    //			listSammelkarten.clear();
    //			listSammelkarten.add(aktuelleMSammelkarte);
    //		}
    //	}
    //
    //	private void changedCbWeisung() {
    //		int hWeisungsartZulaessig=1;
    //		if (cbOhneWeisung.isSelected()) {hWeisungsartZulaessig=(hWeisungsartZulaessig | 2);}
    //		if (cbDedizierteWeisung.isSelected()) {hWeisungsartZulaessig=(hWeisungsartZulaessig | 4);}
    //		aktuelleSammelMeldung.skWeisungsartZulaessig=hWeisungsartZulaessig;
    //		aktuelleMSammelkarte.skWeisungsartZulaessig=aktuelleSammelMeldung.skWeisungsartZulaessig;
    //		aktuelleMSammelkarte.belegeSkWeisungsartZulaessigString(aktuelleSammelMeldung);
    //		zeigeListSammelkartenNeuAn();
    //	}
    //
    //	private void changedSammelkartenArt() {
    //    	if (cbKIAV.isSelected()) {aktuelleSammelMeldung.skIst=1;}
    //    	if (cbSRV.isSelected()) {aktuelleSammelMeldung.skIst=2;}
    //    	if (cbOrga.isSelected()) {aktuelleSammelMeldung.skIst=3;}
    //    	if (cbBriefwahl.isSelected()) {aktuelleSammelMeldung.skIst=4;}
    //    	if (cbDauervollmacht.isSelected()) {aktuelleSammelMeldung.skIst=5;}
    //
    //    	aktuelleMSammelkarte.skIst=aktuelleSammelMeldung.skIst;
    //    	aktuelleMSammelkarte.belegeSkIstString(aktuelleSammelMeldung);
    //    	zeigeListSammelkartenNeuAn();
    //	}
    //
    //	private void changedWeitergabeWeisungen() {
    //    	if (cbWeitergabeInGesamtsumme.isSelected()) {aktuelleSammelMeldung.zusatzfeld1="1";}
    //    	if (cbWeitergabeJeSammelkartenSumme.isSelected()) {aktuelleSammelMeldung.zusatzfeld1="2";}
    //    	if (cbWeitergabeNichtErlaubt.isSelected()) {aktuelleSammelMeldung.zusatzfeld1="3";}
    //    	if (cbWeitergabeUneingeschraenkt.isSelected()) {aktuelleSammelMeldung.zusatzfeld1="";}
    //
    //    	aktuelleMSammelkarte.belegeVerdecktString(aktuelleSammelMeldung);
    //    	zeigeListSammelkartenNeuAn();
    //	}
    //
    //	private void changedOffenlegung() {
    //    	if (cbMitOffenlegung.isSelected()) {aktuelleSammelMeldung.skOffenlegung=1;}
    //    	if (cbOhneOffenlegung.isSelected()) {aktuelleSammelMeldung.skOffenlegung=-1;}
    //    	if (cbOffenlegungWieParameter.isSelected()) {aktuelleSammelMeldung.skOffenlegung=0;}
    //
    //    	aktuelleMSammelkarte.belegeSkOffenlegungString(aktuelleSammelMeldung);
    //    	zeigeListSammelkartenNeuAn();
    //	}
    //
    /**
     * ***************Logik*********************************.
     */

    /**
     * Liest für die Insti aktuelleInsti ein: > Suchbegriffe (suchlaufBegriffe)
     */
    private void fuelleInstiZusatzdaten() {
        blInsti.leseInstiZusatzdaten(aktuelleInsti);
        suchlaufBegriffe = blInsti.rcSuchlaufBegriffe;
        eclInstiSubZuordnungUeblichArray = blInsti.rcEclInstiSubZuordnungUeblichArray;
        eclInstiSubZuordnungTatsaechlichArray = blInsti.rcEclInstiSubZuordnungTatsaechlichArray;
        blInsti.leseInstiKennungen(aktuelleInsti);
        blInsti.fuelleInstiBestandsZuordnung(aktuelleInstiIdent);
    }

    /**
     * Bereite vor neue insti.
     */
    private void bereiteVorNeueInsti() {
    }

    /**
     * Refresh inhalte.
     */
    private void refreshInhalte() {
        int anz = zeigeInstiInTableView();
        if (anz == 0) {
            /*Darf nicht vorkommen!*/
            fehlerMeldung("Institutioneller nicht vorhanden!");
            eigeneStage.hide();
            return;
        }
        fuelleInstiZusatzdaten();
        aktuelleInsti.suchlaufBegriffe = suchlaufBegriffe.suchbegriffe;

        fuelleTabDetails();

    }

    /**
     * Fuelle insti alle.
     */
    private void fuelleInstiAlle() {
        blInsti.holeInstiDaten(0);
        eclInstiAlle = blInsti.rcInsti;
    }

    /**
     * Erzeuge zuordnung aus anzeige.
     *
     * @param pGlobal the global
     * @return the list
     */
    private List<EclInstiSubZuordnung> erzeugeZuordnungAusAnzeige(boolean pGlobal) {
        List<EclInstiSubZuordnung> lInstiSubZuordnungArray = new LinkedList<EclInstiSubZuordnung>();
        for (int i = 0; i < eclInstiAlle.length; i++) {
            if (cbBoxFuerInstiAlle[i].isSelected()) {
                EclInstiSubZuordnung lInstiSubZuordnung = new EclInstiSubZuordnung();
                if (!pGlobal) {
                    lInstiSubZuordnung.mandant = dbBundle.clGlobalVar.mandant;
                }
                lInstiSubZuordnung.identInsti = aktuelleInstiIdent;
                lInstiSubZuordnung.identSubInsti = eclInstiAlle[i].ident;
                lInstiSubZuordnungArray.add(lInstiSubZuordnung);
            }
        }
        return lInstiSubZuordnungArray;
    }

    //	private boolean sammelWarPraesent() {
    //		if (aktuelleSammelMeldung.statusWarPraesenz==1) {return true;}
    //		return false;
    //	}
    //
    //	private boolean sammelHatAktionaere() {
    //		if (aktionaereAktiv==null || aktionaereAktiv.size()>0) {return true;}
    //		return false;
    //	}
    //
    /** ***************Initialisierung****************************. */

    /**
     * 1=Neuen Insti anlegen 2=Insti ändern 3=Bestände verwalten
     */
    private int aktuelleFunktion = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pInstiIdent  the insti ident
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pInstiIdent, int pFunktion) {
        eigeneStage = pEigeneStage;
        aktuelleInstiIdent = pInstiIdent;
        aktuelleFunktion = pFunktion;
    }

    private void zeigeFehlermeldung(String meldung) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, meldung);
    }

}
