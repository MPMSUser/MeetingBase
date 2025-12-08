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
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclNummernForm;
import de.meetingapps.meetingportal.meetComEntities.EclNummernFormSet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterNummernformen.
 */
public class CtrlParameterNummernformen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The pn nummernformen. */
    @FXML
    private ScrollPane pnNummernformen;

    /** The tf HV ident. */
    @FXML
    private TextField tfHVIdent;

    /** The tf kontrollzahl 3. */
    @FXML
    private TextField tfKontrollzahl3;

    /** The tf kontrollzahl 2. */
    @FXML
    private TextField tfKontrollzahl2;

    /** The tf kontrollzahl 1. */
    @FXML
    private TextField tfKontrollzahl1;

    /** The tf EK laenge. */
    @FXML
    private TextField tfEKLaenge;

    /** The cb EK alpha. */
    @FXML
    private CheckBox cbEKAlpha;

    /** The tf EK von gesamt. */
    @FXML
    private TextField tfEKVonGesamt;

    /** The tf EK bis gesamt. */
    @FXML
    private TextField tfEKBisGesamt;

    /** The tf EK sammelk von. */
    @FXML
    private TextField tfEKSammelkVon;

    /** The tf EK sammelk bis. */
    @FXML
    private TextField tfEKSammelkBis;

    /** The tf GK laenge. */
    @FXML
    private TextField tfGKLaenge;

    /** The cb GK alpha. */
    @FXML
    private CheckBox cbGKAlpha;

    /** The tf GK von gesamt. */
    @FXML
    private TextField tfGKVonGesamt;

    /** The tf GK bis gesamt. */
    @FXML
    private TextField tfGKBisGesamt;

    /** The tf GK von manuell. */
    @FXML
    private TextField tfGKVonManuell;

    /** The tf GK bis manuell. */
    @FXML
    private TextField tfGKBisManuell;

    /** The tf GK von auto. */
    @FXML
    private TextField tfGKVonAuto;

    /** The tf GK bis auto. */
    @FXML
    private TextField tfGKBisAuto;

    /** The tf SK laenge. */
    @FXML
    private TextField tfSKLaenge;

    /** The cb SK alpha. */
    @FXML
    private CheckBox cbSKAlpha;

    /** The tf SK von gesamt. */
    @FXML
    private TextField tfSKVonGesamt;

    /** The tf SK bis gesamt. */
    @FXML
    private TextField tfSKBisGesamt;

    /** The tf SKS laenge. */
    @FXML
    private TextField tfSKSLaenge;

    /** The cb SKS alpha. */
    @FXML
    private CheckBox cbSKSAlpha;

    /** The tf SKS von gesamt. */
    @FXML
    private TextField tfSKSVonGesamt;

    /** The tf SKS bis gesamt. */
    @FXML
    private TextField tfSKSBisGesamt;

    /** The tf PI laenge. */
    @FXML
    private TextField tfPILaenge;

    /** The grid pane eintrittskarten sub. */
    @FXML
    private GridPane gridPaneEintrittskartenSub;

    /** The lb gattung 1 EK. */
    @FXML
    private Label lbGattung1EK;

    /** The lb gattung 2 EK. */
    @FXML
    private Label lbGattung2EK;

    /** The lb gattung 3 EK. */
    @FXML
    private Label lbGattung3EK;

    /** The lb gattung 4 EK. */
    @FXML
    private Label lbGattung4EK;

    /** The lb gattung 5 EK. */
    @FXML
    private Label lbGattung5EK;

    /** The grid pane stimmkarten sub. */
    @FXML
    private GridPane gridPaneStimmkartenSub;

    /** The lb gattung 1 SK. */
    @FXML
    private Label lbGattung1SK;

    /** The lb gattung 2 SK. */
    @FXML
    private Label lbGattung2SK;

    /** The lb gattung 3 SK. */
    @FXML
    private Label lbGattung3SK;

    /** The lb gattung 4 SK. */
    @FXML
    private Label lbGattung4SK;

    /** The lb gattung 5 SK. */
    @FXML
    private Label lbGattung5SK;

    /** The tf ja. */
    @FXML
    private TextField tfJa;

    /** The tf nein. */
    @FXML
    private TextField tfNein;

    /** The tf enthaltung. */
    @FXML
    private TextField tfEnthaltung;

    /** The rb pruefziffer EKSK. */
    @FXML
    private RadioButton rbPruefzifferEKSK;

    /** The tg pruefziffer teil. */
    @FXML
    private ToggleGroup tgPruefzifferTeil;

    /** The rb pruefziffer gesamt. */
    @FXML
    private RadioButton rbPruefzifferGesamt;

    /** The rb pruefziffer 1. */
    @FXML
    private RadioButton rbPruefziffer1;

    /** The tg pruefziffer berechnung. */
    @FXML
    private ToggleGroup tgPruefzifferBerechnung;

    /** The rb pruefziffer 2. */
    @FXML
    private RadioButton rbPruefziffer2;

    /** The rb pruefziffer 3. */
    @FXML
    private RadioButton rbPruefziffer3;

    /** The rb pruefziffer 4. */
    @FXML
    private RadioButton rbPruefziffer4;

    /** The pn kombi. */
    @FXML
    private ScrollPane pnKombi;

    /** The tf eintrittskartennummer. */
    @FXML
    private TextField tfEintrittskartennummer;

    /** The tf gastkartennummer. */
    @FXML
    private TextField tfGastkartennummer;

    /** The tf stimmkartennummer. */
    @FXML
    private TextField tfStimmkartennummer;

    /** The tf stimmkartennummer second. */
    @FXML
    private TextField tfStimmkartennummerSecond;

    /** The tf app ident. */
    @FXML
    private TextField tfAppIdent;

    /** The tf personen ident. */
    @FXML
    private TextField tfPersonenIdent;

    /** The tf eintrittskartennummer mit index. */
    @FXML
    private TextField tfEintrittskartennummerMitIndex;

    /** The tf erstzugang. */
    @FXML
    private TextField tfErstzugang;

    /** The tf stimmkarten etikett. */
    @FXML
    private TextField tfStimmkartenEtikett;

    /** The tf wiederzugang. */
    @FXML
    private TextField tfWiederzugang;

    /** The tf abgang. */
    @FXML
    private TextField tfAbgang;

    /** The tf vollmacht an dritte erteilen. */
    @FXML
    private TextField tfVollmachtAnDritteErteilen;

    /** The tf vollmacht an dritte empfangen. */
    @FXML
    private TextField tfVollmachtAnDritteEmpfangen;

    /** The tf stimmabschnittsnummer. */
    @FXML
    private TextField tfStimmabschnittsnummer;

    /** The tf vollmacht weisung SRV. */
    @FXML
    private TextField tfVollmachtWeisungSRV;

    /** The pn klasse art. */
    @FXML
    private ScrollPane pnKlasseArt;

    /** The combo aktives set. */
    @FXML
    private ComboBox<String> comboAktivesSet;

    /** The tf beschreibung. */
    @FXML
    private TextArea tfBeschreibung;

    /** The rb einstellig. */
    @FXML
    private RadioButton rbEinstellig;

    /** The tg klassifizierung. */
    @FXML
    private ToggleGroup tgKlassifizierung;

    /** The rb zweistellig. */
    @FXML
    private RadioButton rbZweistellig;

    /** The btn neue nummernform. */
    @FXML
    private Button btnNeueNummernform;

    /** The btn aendern nummernform. */
    @FXML
    private Button btnAendernNummernform;

    /** The tf mandant. */
    @FXML
    private TextField tfMandant;

    /** The tf fehlermeldung. */
    @FXML
    private TextField tfFehlermeldung;

    /** The btn speichern set. */
    @FXML
    private Button btnSpeichernSet;

    /** The btn neues set. */
    @FXML
    private Button btnNeuesSet;

    /** The btn test nummernform. */
    @FXML
    private Button btnTestNummernform;

    /** Grid-Pane für die Anzeige der verfügbaren einzelnen Nummernformen. */
    private GridPane grpnNummernForm = null;

    /** Einzelne Nummernform ist für Aktion ausgewählt. */
    private CheckBox[] cbNummernformAusgewaehlt = null;

    /** Grid-Pane für die Anzeige der Kombi-Code-Zuordnung. */
    private GridPane grpnKombiCode = null;

    /** The lb kombi code klassen. */
    private Label[] lbKombiCodeKlassen = null;

    /** The lb kombi code arten. */
    private Label[] lbKombiCodeArten = null;

    /** The tf kombi code. */
    private TextField[][] tfKombiCode = null;

    /** Grid-Pane für die Anzeige der Klasse-Art-Nummernform-Zuordnung. */
    private GridPane grpnKlasseArtNummernform = null;

    /** The lb klasse art nummernform klassen. */
    private Label[] lbKlasseArtNummernformKlassen = null;

    /** The lb klasse art nummernform arten. */
    private Label[] lbKlasseArtNummernformArten = null;

    /** The tf klasse art nummernform. */
    private TextField[][] tfKlasseArtNummernform = null;

    /** Textfelder für die Eintrittskarten-Sub-Nummern. */
    private TextField[][] tfEKSubKreisVon = null;

    /** The tf EK sub kreis bis. */
    private TextField[][] tfEKSubKreisBis = null;

    /** Textfelder für die Stimmkarten-Sub-Nummern. */
    private TextField[][] tfSKSubKreisVon = null;

    /** The tf SK sub kreis bis. */
    private TextField[][] tfSKSubKreisBis = null;

    /** Angezeigte Nummernformen. */
    private EclNummernForm[] eclNummernFormen = null;

    /** Angezeigtes / aktives Set. */
    private EclNummernFormSet eclNummernFormSet = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert pnNummernformen != null : "fx:id=\"pnNummernformen\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfHVIdent != null : "fx:id=\"tfHVIdent\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfKontrollzahl3 != null : "fx:id=\"tfKontrollzahl3\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfKontrollzahl2 != null : "fx:id=\"tfKontrollzahl2\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfKontrollzahl1 != null : "fx:id=\"tfKontrollzahl1\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEKLaenge != null : "fx:id=\"tfEKLaenge\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert cbEKAlpha != null : "fx:id=\"cbEKAlpha\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEKVonGesamt != null : "fx:id=\"tfEKVonGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEKBisGesamt != null : "fx:id=\"tfEKBisGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEKSammelkVon != null : "fx:id=\"tfEKSammelkVon\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEKSammelkBis != null : "fx:id=\"tfEKSammelkBis\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKLaenge != null : "fx:id=\"tfGKLaenge\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert cbGKAlpha != null : "fx:id=\"cbGKAlpha\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKVonGesamt != null : "fx:id=\"tfGKVonGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKBisGesamt != null : "fx:id=\"tfGKBisGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKVonManuell != null : "fx:id=\"tfGKVonManuell\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKBisManuell != null : "fx:id=\"tfGKBisManuell\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKVonAuto != null : "fx:id=\"tfGKVonAuto\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGKBisAuto != null : "fx:id=\"tfGKBisAuto\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKLaenge != null : "fx:id=\"tfSKLaenge\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert cbSKAlpha != null : "fx:id=\"cbSKAlpha\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKVonGesamt != null : "fx:id=\"tfSKVonGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKBisGesamt != null : "fx:id=\"tfSKBisGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKSLaenge != null : "fx:id=\"tfSKSLaenge\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert cbSKSAlpha != null : "fx:id=\"cbSKSAlpha\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKSVonGesamt != null : "fx:id=\"tfSKSVonGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfSKSBisGesamt != null : "fx:id=\"tfSKSBisGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfPILaenge != null : "fx:id=\"tfPILaenge\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert gridPaneEintrittskartenSub != null : "fx:id=\"gridPaneEintrittskartenSub\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung1EK != null : "fx:id=\"lbGattung1EK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung2EK != null : "fx:id=\"lbGattung2EK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung3EK != null : "fx:id=\"lbGattung3EK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung4EK != null : "fx:id=\"lbGattung4EK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung5EK != null : "fx:id=\"lbGattung5EK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert gridPaneStimmkartenSub != null : "fx:id=\"gridPaneStimmkartenSub\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung1SK != null : "fx:id=\"lbGattung1SK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung2SK != null : "fx:id=\"lbGattung2SK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung3SK != null : "fx:id=\"lbGattung3SK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung4SK != null : "fx:id=\"lbGattung4SK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert lbGattung5SK != null : "fx:id=\"lbGattung5SK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfJa != null : "fx:id=\"tfJa\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfNein != null : "fx:id=\"tfNein\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEnthaltung != null : "fx:id=\"tfEnthaltung\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefzifferEKSK != null : "fx:id=\"rbPruefzifferEKSK\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tgPruefzifferTeil != null : "fx:id=\"tgPrüfzifferTeil\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefzifferGesamt != null : "fx:id=\"rbPruefzifferGesamt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefziffer1 != null : "fx:id=\"rbPruefziffer1\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tgPruefzifferBerechnung != null : "fx:id=\"tgPrüfzifferBerechnung\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefziffer2 != null : "fx:id=\"rbPruefziffer2\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefziffer3 != null : "fx:id=\"rbPruefziffer3\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbPruefziffer4 != null : "fx:id=\"rbPruefziffer4\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert pnKombi != null : "fx:id=\"pnKombi\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEintrittskartennummer != null : "fx:id=\"tfEintrittskartennummer\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfGastkartennummer != null : "fx:id=\"tfGastkartennummer\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfStimmkartennummer != null : "fx:id=\"tfStimmkartennummer\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfStimmkartennummerSecond != null : "fx:id=\"tfStimmkartennummerSecond\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfAppIdent != null : "fx:id=\"tfAppIdent\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfPersonenIdent != null : "fx:id=\"tfPersonenIdent\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfEintrittskartennummerMitIndex != null : "fx:id=\"tfEintrittskartennummerMitIndex\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfErstzugang != null : "fx:id=\"tfErstzugang\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfStimmkartenEtikett != null : "fx:id=\"tfStimmkartenEtikett\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfWiederzugang != null : "fx:id=\"tfWiederzugang\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfAbgang != null : "fx:id=\"tfAbgang\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfVollmachtAnDritteErteilen != null : "fx:id=\"tfVollmachtAnDritteErteilen\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfVollmachtAnDritteEmpfangen != null : "fx:id=\"tfVollmachtAnDritteEmpfangen\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfStimmabschnittsnummer != null : "fx:id=\"tfStimmabschnittsnummer\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfVollmachtWeisungSRV != null : "fx:id=\"tfVollmachtWeisungSRV\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert pnKlasseArt != null : "fx:id=\"pnKlasseArt\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert comboAktivesSet != null : "fx:id=\"comboAktivesSet\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfBeschreibung != null : "fx:id=\"tfBeschreibung\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbEinstellig != null : "fx:id=\"rbEinstellig\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tgKlassifizierung != null : "fx:id=\"tgKlassifizierung\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert rbZweistellig != null : "fx:id=\"rbZweistellig\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert btnNeueNummernform != null : "fx:id=\"btnNeueNummernform\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert btnAendernNummernform != null : "fx:id=\"btnAendernNummernform\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfMandant != null : "fx:id=\"tfMandant\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert tfFehlermeldung != null : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert btnSpeichernSet != null : "fx:id=\"btnSpeichernSet\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";
        assert btnNeuesSet != null : "fx:id=\"btnNeuesSet\" was not injected: check your FXML file 'ParameterNummernformen.fxml'.";

        DbBundle lDbBundle = new DbBundle();

        tfSKSubKreisVon = new TextField[6][6];
        tfSKSubKreisBis = new TextField[6][6];
        for (int i = 1; i <= 5; i++) {
            String inhalt = "";
            if (lDbBundle.param.paramBasis.gattungAktiv[i - 1]) {
                inhalt = lDbBundle.param.paramBasis.gattungBezeichnung[i - 1];
            } else {
                inhalt = "G" + Integer.toString(i);
            }
            switch (i) {
            case 1:
                lbGattung1SK.setText(inhalt);
                break;
            case 2:
                lbGattung2SK.setText(inhalt);
                break;
            case 3:
                lbGattung3SK.setText(inhalt);
                break;
            case 4:
                lbGattung4SK.setText(inhalt);
                break;
            case 5:
                lbGattung5SK.setText(inhalt);
                break;
            }
        }
        for (int i = 1; i <= 5; i++) {
            tfSKSubKreisVon[i] = new TextField[6];
            tfSKSubKreisBis[i] = new TextField[6];
            for (int i1 = 1; i1 <= 5; i1++) {
                tfSKSubKreisVon[i][i1] = new TextField();
                tfSKSubKreisBis[i][i1] = new TextField();
                //                if (lDbBundle.param.paramBasis.gattungAktiv[i - 1]) {
                gridPaneStimmkartenSub.add(tfSKSubKreisVon[i][i1], i1, (i * 2) - 1);
                gridPaneStimmkartenSub.add(tfSKSubKreisBis[i][i1], i1, (i * 2));
                //                }
            }
        }

        tfEKSubKreisVon = new TextField[6][3];
        tfEKSubKreisBis = new TextField[6][3];
        for (int i = 1; i <= 5; i++) {
            String inhalt = "";
            if (lDbBundle.param.paramBasis.gattungAktiv[i - 1]) {
                inhalt = lDbBundle.param.paramBasis.gattungBezeichnung[i - 1];
            } else {
                inhalt = "G" + Integer.toString(i);
            }
            switch (i) {
            case 1:
                lbGattung1EK.setText(inhalt);
                break;
            case 2:
                lbGattung2EK.setText(inhalt);
                break;
            case 3:
                lbGattung3EK.setText(inhalt);
                break;
            case 4:
                lbGattung4EK.setText(inhalt);
                break;
            case 5:
                lbGattung5EK.setText(inhalt);
                break;
            }
        }
        for (int i = 1; i <= 5; i++) {
            tfEKSubKreisVon[i] = new TextField[3];
            tfEKSubKreisBis[i] = new TextField[3];
            for (int i1 = 1; i1 <= 2; i1++) {
                tfEKSubKreisVon[i][i1] = new TextField();
                tfEKSubKreisBis[i][i1] = new TextField();
                //                if (lDbBundle.param.paramBasis.gattungAktiv[i - 1]) {
                gridPaneEintrittskartenSub.add(tfEKSubKreisVon[i][i1], i1, (i * 2) - 1);
                gridPaneEintrittskartenSub.add(tfEKSubKreisBis[i][i1], i1, (i * 2));
                //                }
            }
        }

        zeigeNummernformen();
        zeigeNummernformSets();

        comboAktivesSet.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
                if (neuerWert == null || neuerWert.isEmpty()) {
                    return;
                }
                int hIndex = neuerWert.lastIndexOf('(');
                String identString = neuerWert.substring(hIndex + 1, neuerWert.length() - 1);
                int setIdent = Integer.parseInt(identString);
                DbBundle lDbBundle = new DbBundle();
                lDbBundle.openAll();
                EclNummernFormSet lNummernFormSet = new EclNummernFormSet();
                lNummernFormSet.ident = setIdent;
                lDbBundle.dbNummernFormSet.read(lNummernFormSet);
                eclNummernFormSet = lDbBundle.dbNummernFormSet.ergebnisArray[0];
                ParamS.param.paramNummernformen.ident = setIdent;
                lDbBundle.closeAll();
                zeigeSet();
            }
        });

    }

    /*************************Bildschirmaktionen*************************************/
    @FXML
    void onBtnAendernNummernform(ActionEvent event) {
        clearFehlermeldung();
        
        if (ParamS.paramServer.nummernformenPflegbar==0) {
            zeigeFehlermeldung("Nummernformen können auf diesem Server nicht geändert werden!");
            return;
        }

        int gef = -1;
        if (cbNummernformAusgewaehlt != null) {
            for (int i = 0; i < cbNummernformAusgewaehlt.length; i++) {
                if (cbNummernformAusgewaehlt[i].isSelected()) {
                    if (gef == -1) {
                        gef = i;
                    } else {
                        gef = -2;
                    }
                }
            }
        }
        if (gef == -1) {
            zeigeFehlermeldung("Bitte wählen Sie eine Nummernform zum Ändern aus!");
            return;
        }
        if (gef == -2) {
            zeigeFehlermeldung("Bitte wählen Sie nur eine Nummernform zum Ändern aus!");
            return;
        }

        rufeDetailNummernformAuf(eclNummernFormen[gef]);

        zeigeNummernformen();

    }

    /**
     * On btn neue nummernform.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeueNummernform(ActionEvent event) {
        clearFehlermeldung();

        if (ParamS.paramServer.nummernformenPflegbar==0) {
            zeigeFehlermeldung("Nummernformen können auf diesem Server nicht geändert werden!");
            return;
        }

        rufeDetailNummernformAuf(null);

        zeigeNummernformen();

    }

    /**
     * On btn neues set.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeuesSet(ActionEvent event) {
        clearFehlermeldung();

        if (ParamS.paramServer.nummernformenPflegbar==0) {
            zeigeFehlermeldung("Nummernformen können auf diesem Server nicht geändert werden!");
            return;
        }

        Stage neuerDialog = new Stage();

        CtrlParameterNummernformenNeuesSet controllerFenster = new CtrlParameterNummernformenNeuesSet();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterNummernformenNeuesSet.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.onBtnNeuesSet 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 600, 500);
        neuerDialog.setTitle("Neues Nummernform-Set");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        zeigeNummernformSets();
    }

    /**
     * Rufe detail nummernform auf.
     *
     * @param pNummernForm the nummern form
     */
    private void rufeDetailNummernformAuf(EclNummernForm pNummernForm) {

        Stage neuerDialog = new Stage();

        CtrlParameterNummernformenEinzelneForm controllerFenster = new CtrlParameterNummernformenEinzelneForm();

        int funktion = 0;
        if (pNummernForm == null) {
            funktion = 1;
            /*Neu*/} else {
            funktion = 2;
            /*Ändern*/}

        controllerFenster.init(neuerDialog, funktion);
        controllerFenster.zuBearbeitendeNummernForm = pNummernForm;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterNummernformEinzelneForm.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.rufeDetailNummernformAuf 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 600, 500);
        if (funktion == 1) {
            neuerDialog.setTitle("Neue Nummernform");
        } else {
            neuerDialog.setTitle("Ändern Nummernform");
        }
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Pruefe gesamt.
     *
     * @param pNummernkreis the nummernkreis
     * @return true, if successful
     */
    private boolean pruefeGesamt(int pNummernkreis) {
        if (eclNummernFormSet.vonKartennummerGesamt[pNummernkreis] == 0
                && eclNummernFormSet.bisKartennummerGesamt[pNummernkreis] == 0) {
            if (pNummernkreis == KonstKartenklasse.eintrittskartennummer) {
                zeigeFehlermeldung("von / bis Gesamt-Nummernkreis Eintrittskarten-Nr zwingend!");
                return false;
            }
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisKartennummerGesamt[pNummernkreis] == 0
                || eclNummernFormSet.vonKartennummerGesamt[pNummernkreis] == 0 ||
                /*von >= bis*/
                eclNummernFormSet.vonKartennummerGesamt[pNummernkreis] >= eclNummernFormSet.bisKartennummerGesamt[pNummernkreis]) {
            zeigeFehlermeldung(
                    "von / bis Gesamt-Nummernkreis " + KonstKartenklasse.getText(pNummernkreis) + " inkonsistent!");
            return false;
        }

        return true;
    }

    /**
     * Wird nur für Gastkartennummer aufgerufen.
     *
     * @param pNummernkreis the nummernkreis
     * @return true, if successful
     */
    private boolean pruefeManuell(int pNummernkreis) {
        if (eclNummernFormSet.vonKartennummerManuell[pNummernkreis] == 0
                && eclNummernFormSet.bisKartennummerManuell[pNummernkreis] == 0) {
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisKartennummerManuell[pNummernkreis] == 0
                || eclNummernFormSet.vonKartennummerManuell[pNummernkreis] == 0 ||
                /*von >= bis*/
                eclNummernFormSet.vonKartennummerManuell[pNummernkreis] >= eclNummernFormSet.bisKartennummerManuell[pNummernkreis]
                ||
                /*Innerhalb Gesamt*/
                eclNummernFormSet.vonKartennummerManuell[pNummernkreis] < eclNummernFormSet.vonKartennummerGesamt[pNummernkreis]
                || eclNummernFormSet.bisKartennummerManuell[pNummernkreis] > eclNummernFormSet.bisKartennummerGesamt[pNummernkreis]
                ||
                /*Nicht innerhalb Auto*/
                (eclNummernFormSet.bisKartennummerManuell[pNummernkreis] >= eclNummernFormSet.vonKartennummerAuto[pNummernkreis]
                        && eclNummernFormSet.bisKartennummerManuell[pNummernkreis] <= eclNummernFormSet.bisKartennummerAuto[pNummernkreis])
                || (eclNummernFormSet.vonKartennummerManuell[pNummernkreis] >= eclNummernFormSet.vonKartennummerAuto[pNummernkreis]
                        && eclNummernFormSet.vonKartennummerManuell[pNummernkreis] <= eclNummernFormSet.bisKartennummerAuto[pNummernkreis])) {
            zeigeFehlermeldung(
                    "von / bis Manuell-Nummernkreis " + KonstKartenklasse.getText(pNummernkreis) + " inkonsistent!");
            return false;
        }

        return true;
    }

    /**
     * Wird nur für Gastkartennummer aufgerufen.
     *
     * @param pNummernkreis the nummernkreis
     * @return true, if successful
     */
    private boolean pruefeAuto(int pNummernkreis) {
        if (eclNummernFormSet.vonKartennummerAuto[pNummernkreis] == 0
                && eclNummernFormSet.bisKartennummerAuto[pNummernkreis] == 0) {
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisKartennummerAuto[pNummernkreis] == 0
                || eclNummernFormSet.vonKartennummerAuto[pNummernkreis] == 0 ||
                /*von >= bis*/
                eclNummernFormSet.vonKartennummerAuto[pNummernkreis] >= eclNummernFormSet.bisKartennummerAuto[pNummernkreis]
                ||
                /*Innerhalb Gesamt*/
                eclNummernFormSet.vonKartennummerAuto[pNummernkreis] < eclNummernFormSet.vonKartennummerGesamt[pNummernkreis]
                || eclNummernFormSet.bisKartennummerAuto[pNummernkreis] > eclNummernFormSet.bisKartennummerGesamt[pNummernkreis]
                /*Nicht innerhalb Manuell*/
                || (eclNummernFormSet.bisKartennummerAuto[pNummernkreis] >= eclNummernFormSet.vonKartennummerManuell[pNummernkreis]
                        && eclNummernFormSet.bisKartennummerAuto[pNummernkreis] <= eclNummernFormSet.bisKartennummerManuell[pNummernkreis])
                || (eclNummernFormSet.vonKartennummerAuto[pNummernkreis] >= eclNummernFormSet.vonKartennummerManuell[pNummernkreis]
                        && eclNummernFormSet.vonKartennummerAuto[pNummernkreis] <= eclNummernFormSet.bisKartennummerManuell[pNummernkreis])) {
            zeigeFehlermeldung(
                    "von / bis Auto-Nummernkreis " + KonstKartenklasse.getText(pNummernkreis) + " inkonsistent!");
            return false;
        }

        return true;
    }

    /**
     * Pruefe sammel.
     *
     * @return true, if successful
     */
    private boolean pruefeSammel() {
        if (eclNummernFormSet.vonSammelkartennummer == 0 && eclNummernFormSet.bisSammelkartennummer == 0) {
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisSammelkartennummer == 0 || eclNummernFormSet.vonSammelkartennummer == 0 ||
        /*von >= bis*/
                eclNummernFormSet.vonSammelkartennummer >= eclNummernFormSet.bisSammelkartennummer ||
                /*Innerhalb Gesamt*/
                eclNummernFormSet.vonSammelkartennummer < eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]
                || eclNummernFormSet.bisSammelkartennummer > eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]) {
            zeigeFehlermeldung("von / bis Sammel-Nummernkreis inkonsistent!");
            return false;
        }

        for (int i = 1; i <= 5; i++) {/*Sammelkreis in Subnummernkreis?*/
            //Sammelkarte muß wohl sehr wohl in Subnummernkreis "manuell" vorhanden sein!
            //    		if (
            //        			(eclNummernFormSet.bisSammelkartennummer>=eclNummernFormSet.vonSubEintrittskartennummer[i][1] && eclNummernFormSet.bisSammelkartennummer<=eclNummernFormSet.bisSubEintrittskartennummer[i][1])
            //        			||
            //        			(eclNummernFormSet.vonSammelkartennummer>=eclNummernFormSet.vonSubEintrittskartennummer[i][1] && eclNummernFormSet.vonSammelkartennummer<=eclNummernFormSet.bisSubEintrittskartennummer[i][1])
            //   				){
            //        		zeigeFehlermeldung("von / bis Sammel-Nummernkreis inkonsistent!"); return false;
            //    		}
            if ((eclNummernFormSet.bisSammelkartennummer >= eclNummernFormSet.vonSubEintrittskartennummer[i][2]
                    && eclNummernFormSet.bisSammelkartennummer <= eclNummernFormSet.bisSubEintrittskartennummer[i][2])
                    || (eclNummernFormSet.vonSammelkartennummer >= eclNummernFormSet.vonSubEintrittskartennummer[i][2]
                            && eclNummernFormSet.vonSammelkartennummer <= eclNummernFormSet.bisSubEintrittskartennummer[i][2])) {
                zeigeFehlermeldung("von / bis Sammel-Nummernkreis inkonsistent!");
                return false;
            }

        }
        return true;
    }

    /**
     * Pruefe sub eintrittskartenkreis.
     *
     * @param pGattung  the gattung
     * @param pSubkreis the subkreis
     * @return true, if successful
     */
    private boolean pruefeSubEintrittskartenkreis(int pGattung, int pSubkreis) {
        if (eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] == 0
                && eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis] == 0) {
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis] == 0
                || eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] == 0 ||
                /*von >= bis*/
                eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] >= eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis]
                ||
                /*Innerhalb Gesamt*/
                eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] < eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]
                || eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis] > eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]) {
            zeigeFehlermeldung("von / bis SubEintrittskarten-Nummernkreis " + Integer.toString(pSubkreis) + " "
                    + ParamS.param.paramBasis.gattungBezeichnung[pGattung - 1] + " inkonsistent!");
            return false;
        }

        /*Nicht innerhalb anderer Sub-Nummernkreise*/
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 2; i1++) {
                if (i != pGattung || i1 != pSubkreis) {
                    if ((eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis] >= eclNummernFormSet.vonSubEintrittskartennummer[i][i1]
                            && eclNummernFormSet.bisSubEintrittskartennummer[pGattung][pSubkreis] <= eclNummernFormSet.bisSubEintrittskartennummer[i][i1])
                            || (eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] >= eclNummernFormSet.vonSubEintrittskartennummer[i][i1]
                                    && eclNummernFormSet.vonSubEintrittskartennummer[pGattung][pSubkreis] <= eclNummernFormSet.bisSubEintrittskartennummer[i][i1])) {
                        zeigeFehlermeldung("von / bis SubEintrittskarten-Nummernkreis " + Integer.toString(pSubkreis)
                                + " " + ParamS.param.paramBasis.gattungBezeichnung[pGattung - 1] + " inkonsistent!");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Pruefe sub stimmkartenkreis.
     *
     * @param pGattung  the gattung
     * @param pSubkreis the subkreis
     * @return true, if successful
     */
    private boolean pruefeSubStimmkartenkreis(int pGattung, int pSubkreis) {
        if (eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] == 0
                && eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis] == 0) {
            return true;
        }

        if ( /*Von oder Bis = 0?*/
        eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis] == 0
                || eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] == 0 ||
                /*von >= bis*/
                eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] >= eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis]
                ||
                /*Innerhalb Gesamt*/
                eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] < eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.stimmkartennummer]
                || eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis] > eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.stimmkartennummer]) {
            zeigeFehlermeldung("von / bis Substimmkarten-Nummernkreis " + Integer.toString(pSubkreis) + " "
                    + ParamS.param.paramBasis.gattungBezeichnung[pGattung - 1] + " inkonsistent!");
            return false;
        }

        /*Nicht innerhalb anderer Sub-Nummernkreise*/
        if (ParamSpezial.ku168(ParamS.clGlobalVar.mandant)) {
            return true;
        } //Bei ku168: Subnummernkreise Stimmkarten müssen identisch sein können.
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 5; i1++) {
                if (i != pGattung || i1 != pSubkreis) {
                    if ((eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis] >= eclNummernFormSet.vonSubStimmkartennummer[i][i1]
                            && eclNummernFormSet.bisSubStimmkartennummer[pGattung][pSubkreis] <= eclNummernFormSet.bisSubStimmkartennummer[i][i1])
                            || (eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] >= eclNummernFormSet.vonSubStimmkartennummer[i][i1]
                                    && eclNummernFormSet.vonSubStimmkartennummer[pGattung][pSubkreis] <= eclNummernFormSet.bisSubStimmkartennummer[i][i1])) {
                        zeigeFehlermeldung("von / bis Substimmkarten-Nummernkreis " + Integer.toString(pSubkreis) + " "
                                + ParamS.param.paramBasis.gattungBezeichnung[pGattung - 1] + " inkonsistent!");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * On btn test nummernform.
     *
     * @param event the event
     */
    @FXML
    void onBtnTestNummernform(ActionEvent event) {
        boolean rc = speichern();
        if (rc == false) {
            return;
        }

        Stage neuerDialog = new Stage();

        CtrlParameterNummernformenTesten controllerFenster = new CtrlParameterNummernformenTesten();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterNummernformTesten.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.onBtnTestNummernform 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1160, 740);
        neuerDialog.setTitle("Nummernform Testen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * On btn speichern set.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernSet(ActionEvent event) {
        speichern();
    }

    /**
     * Speichern.
     *
     * @return true, if successful
     */
    private boolean speichern() {
        String hString = "";

        clearFehlermeldung();
        if (ParamS.paramServer.nummernformenPflegbar==0) {
            zeigeFehlermeldung("Nummernformen können auf diesem Server nicht geändert werden! Gespeichert werden nur Änderungen bei der Set-Auswahl und bei den mandantenabhängigen Parametern");
        }

        /*Titel-Bereich*/
        eclNummernFormSet.beschreibung = tfBeschreibung.getText();
        if (rbEinstellig.isSelected()) {
            eclNummernFormSet.klassifizierung = 1;
        } else {
            eclNummernFormSet.klassifizierung = 2;
        }

        /*Mandantenabhängige Nummernformparameter*/
        ParamS.param.paramPruefzahlen.identifikationsNummer = tfHVIdent.getText().trim();
        ParamS.param.paramPruefzahlen.laengeIdentifikationsNummer = ParamS.param.paramPruefzahlen.identifikationsNummer
                .length();

        ParamS.param.paramPruefzahlen.dreistelligeKontrollzahl = tfKontrollzahl3.getText().trim();
        if (ParamS.param.paramPruefzahlen.dreistelligeKontrollzahl.length() > 3) {
            ParamS.param.paramPruefzahlen.dreistelligeKontrollzahl = ParamS.param.paramPruefzahlen.dreistelligeKontrollzahl
                    .substring(0, 3);
        }
        ParamS.param.paramPruefzahlen.zweistelligeKontrollzahl = tfKontrollzahl2.getText().trim();
        if (ParamS.param.paramPruefzahlen.zweistelligeKontrollzahl.length() > 2) {
            ParamS.param.paramPruefzahlen.zweistelligeKontrollzahl = ParamS.param.paramPruefzahlen.zweistelligeKontrollzahl
                    .substring(0, 2);
        }
        ParamS.param.paramPruefzahlen.einstelligeKontrollzahl = tfKontrollzahl1.getText().trim();
        if (ParamS.param.paramPruefzahlen.einstelligeKontrollzahl.length() > 1) {
            ParamS.param.paramPruefzahlen.einstelligeKontrollzahl = ParamS.param.paramPruefzahlen.einstelligeKontrollzahl
                    .substring(0, 1);
        }

        /*Nummernkreise*/

        /*Länge*/
        hString = tfEKLaenge.getText();
        if (hString.length() < 1 || hString.length() > 2 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Eintrittskarten-Länge unzulässig!");
            return false;
        }
        eclNummernFormSet.laengeKartennummer[KonstKartenklasse.eintrittskartennummer] = Integer.parseInt(hString);
        if (eclNummernFormSet.laengeKartennummer[KonstKartenklasse.eintrittskartennummer] < 3
                || eclNummernFormSet.laengeKartennummer[KonstKartenklasse.eintrittskartennummer] > 8) {
            zeigeFehlermeldung("Eintrittskarten-Länge muß zwischen 3 und 8 liegen!");
            return false;
        }

        hString = tfGKLaenge.getText();
        if (hString.length() < 1 || hString.length() > 2 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkarten-Länge unzulässig!");
            return false;
        }
        eclNummernFormSet.laengeKartennummer[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);
        if (eclNummernFormSet.laengeKartennummer[KonstKartenklasse.gastkartennummer] < 3
                || eclNummernFormSet.laengeKartennummer[KonstKartenklasse.gastkartennummer] > 8) {
            zeigeFehlermeldung("Gastkarten-Länge muß zwischen 3 und 8 liegen!");
            return false;
        }

        hString = tfSKLaenge.getText();
        if (hString.length() < 1 || hString.length() > 2 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Stimmkarten-Länge unzulässig!");
            return false;
        }
        eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummer] = Integer.parseInt(hString);
        if (eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummer] < 3
                || eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummer] > 8) {
            zeigeFehlermeldung("Stimmkarten-Länge muß zwischen 3 und 8 liegen!");
            return false;
        }

        hString = tfSKSLaenge.getText();
        if (hString.length() < 1 || hString.length() > 2 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Voter-Länge unzulässig!");
            return false;
        }
        eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummerSecond] = Integer.parseInt(hString);

        hString = tfPILaenge.getText();
        if (hString.length() < 1 || hString.length() > 2 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("PersönlicheIdent-Länge unzulässig!");
            return false;
        }
        eclNummernFormSet.laengeKartennummer[KonstKartenklasse.personenIdent] = Integer.parseInt(hString);

        /*IstNumerisch*/
        if (cbEKAlpha.isSelected()) {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.eintrittskartennummer] = false;
        } else {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.eintrittskartennummer] = true;
        }
        if (cbGKAlpha.isSelected()) {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.gastkartennummer] = false;
        } else {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.gastkartennummer] = true;
        }
        if (cbSKAlpha.isSelected()) {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummer] = false;
        } else {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummer] = true;
        }
        if (cbSKSAlpha.isSelected()) {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummerSecond] = false;
        } else {
            eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummerSecond] = true;
        }

        boolean boolgef = false;
        for (int i = 0; i < 8; i++) {
            if (eclNummernFormSet.istNumerisch[i] == false) {
                boolgef = true;
            }
        }
        if (boolgef) {
            zeigeFehlermeldung(
                    "Warnung! Für einen Nummernkreis wurde eine Alphanumerische Kombination zugelassen! Eine automatische Erkennung von Nummern ist damit nicht möglich!");
        }

        /*VonGesamt*/
        hString = tfEKVonGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Eintrittskartenkreis VON GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.eintrittskartennummer] = Integer.parseInt(hString);

        hString = tfGKVonGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis VON GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        hString = tfSKVonGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Stimmkartenkreis VON GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.stimmkartennummer] = Integer.parseInt(hString);

        hString = tfSKSVonGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Voterkreis VON GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.stimmkartennummerSecond] = Integer.parseInt(hString);

        /*BisGesamt*/
        hString = tfEKBisGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Eintrittskartenkreis BIS GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.eintrittskartennummer] = Integer.parseInt(hString);

        hString = tfGKBisGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis BIS GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        hString = tfSKBisGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Stimmkartenkreis BIS GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.stimmkartennummer] = Integer.parseInt(hString);

        hString = tfSKSBisGesamt.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Voterkreis BIS4 GESAMT unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.stimmkartennummerSecond] = Integer.parseInt(hString);

        /*VonManuell*/
        hString = tfGKVonManuell.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis VON Manuell unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerManuell[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        /*BisManuell*/
        hString = tfGKBisManuell.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis BIS Manuell unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerManuell[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        /*VonAuto*/
        hString = tfGKVonAuto.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis VON Auto unzulässig!");
            return false;
        }
        eclNummernFormSet.vonKartennummerAuto[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        /*BisAuto*/
        hString = tfGKBisAuto.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis BIS Auto unzulässig!");
            return false;
        }
        eclNummernFormSet.bisKartennummerAuto[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        /*Sammelkarten von / bis*/
        hString = tfEKSammelkVon.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Sammelkarte Von unzulässig!");
            return false;
        }
        eclNummernFormSet.vonSammelkartennummer = Integer.parseInt(hString);

        hString = tfEKSammelkBis.getText();
        if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Gastkartenkreis BIS Auto unzulässig!");
            return false;
        }
        eclNummernFormSet.bisSammelkartennummer = Integer.parseInt(hString);

        /*Eintrittskarten-Subkreise*/
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 2; i1++) {
                //                if (ParamS.param.paramBasis.gattungAktiv[i - 1]) {
                hString = tfEKSubKreisVon[i][i1].getText();
                //                } else {
                //                    hString = "0";
                //                }
                if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
                    zeigeFehlermeldung("EintrittskartenSub-Kreis " + ParamS.param.paramBasis.gattungBezeichnung[i - 1]
                            + " VON unzulässig!");
                    return false;
                }
                eclNummernFormSet.vonSubEintrittskartennummer[i][i1] = Integer.parseInt(hString);

                //                if (ParamS.param.paramBasis.gattungAktiv[i - 1]) {
                hString = tfEKSubKreisBis[i][i1].getText();
                //                }
                if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
                    zeigeFehlermeldung("EintrittskartenSub-Kreis " + ParamS.param.paramBasis.gattungBezeichnung[i - 1]
                            + " BIS unzulässig!");
                    return false;
                }
                eclNummernFormSet.bisSubEintrittskartennummer[i][i1] = Integer.parseInt(hString);
            }
        }

        /*Stimmkarten-Subkreise*/
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 5; i1++) {
                //                if (ParamS.param.paramBasis.gattungAktiv[i - 1]) {
                hString = tfSKSubKreisVon[i][i1].getText();
                //                } else {
                //                    hString = "0";
                //                }
                if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
                    zeigeFehlermeldung("StimmkartenSub-Kreis " + ParamS.param.paramBasis.gattungBezeichnung[i - 1]
                            + " VON unzulässig!");
                    return false;
                }
                eclNummernFormSet.vonSubStimmkartennummer[i][i1] = Integer.parseInt(hString);

                //                if (ParamS.param.paramBasis.gattungAktiv[i - 1]) {
                hString = tfSKSubKreisBis[i][i1].getText();
                //                }
                if (hString.length() < 1 || hString.length() > 9 || !CaString.isNummern(hString)) {
                    zeigeFehlermeldung("StimmkartenSub-Kreis " + ParamS.param.paramBasis.gattungBezeichnung[i - 1]
                            + " BIS unzulässig!");
                    return false;
                }
                eclNummernFormSet.bisSubStimmkartennummer[i][i1] = Integer.parseInt(hString);
            }
        }

        /*Nun Abhängigkeiten untereinander prüfen*/
        if (!pruefeGesamt(KonstKartenklasse.eintrittskartennummer)) {
            return false;
        }
        if (!pruefeGesamt(KonstKartenklasse.gastkartennummer)) {
            return false;
        }
        if (!pruefeGesamt(KonstKartenklasse.stimmkartennummer)) {
            return false;
        }
        if (!pruefeGesamt(KonstKartenklasse.stimmkartennummerSecond)) {
            return false;
        }

        if (!pruefeManuell(KonstKartenklasse.gastkartennummer)) {
            return false;
        }
        if (!pruefeAuto(KonstKartenklasse.gastkartennummer)) {
            return false;
        }

        pruefeSammel();

        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 2; i1++) {
                if (!pruefeSubEintrittskartenkreis(i, i1)) {
                    return false;
                }

            }
        }

        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 5; i1++) {
                if (!pruefeSubStimmkartenkreis(i, i1)) {
                    return false;
                }

            }
        }

        /*Nummernformparameter*/
        hString = tfJa.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Ja-Kodierung unzulässig!");
            return false;
        }
        eclNummernFormSet.kodierungJa = Integer.parseInt(hString);

        hString = tfNein.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Nein-Kodierung unzulässig!");
            return false;
        }
        eclNummernFormSet.kodierungNein = Integer.parseInt(hString);

        hString = tfEnthaltung.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Enthaltung-Kodierung unzulässig!");
            return false;
        }
        eclNummernFormSet.kodierungEnthaltung = Integer.parseInt(hString);

        if (eclNummernFormSet.kodierungJa != 0) {
            if (eclNummernFormSet.kodierungJa == eclNummernFormSet.kodierungNein
                    || eclNummernFormSet.kodierungJa == eclNummernFormSet.kodierungEnthaltung) {
                zeigeFehlermeldung("Stimm-Kodierung doppelt verwendet!");
                return false;
            }
        }
        if (eclNummernFormSet.kodierungNein != 0) {
            if (eclNummernFormSet.kodierungNein == eclNummernFormSet.kodierungEnthaltung) {
                zeigeFehlermeldung("Stimm-Kodierung doppelt verwendet!");
                return false;
            }
        }

        int hpruefungTeil = 0;
        if (rbPruefzifferEKSK.isSelected()) {
            hpruefungTeil = 1;
        }
        if (rbPruefzifferGesamt.isSelected()) {
            hpruefungTeil = 2;
        }
        int hpruefungVerfahren = 0;
        if (rbPruefziffer1.isSelected()) {
            hpruefungVerfahren = 1;
        }
        if (rbPruefziffer2.isSelected()) {
            hpruefungVerfahren = 2;
        }
        if (rbPruefziffer3.isSelected()) {
            hpruefungVerfahren = 3;
        }
        if (rbPruefziffer4.isSelected()) {
            hpruefungVerfahren = 4;
        }
        if (hpruefungTeil == 2) {
            hpruefungVerfahren += 10;
        }

        eclNummernFormSet.berechnungsVerfahrenPruefziffer = hpruefungVerfahren;
        ParamS.param.paramPruefzahlen.berechnungsVerfahrenPruefziffer = hpruefungVerfahren;

        /*Klasse, Art*/
        hString = tfEintrittskartennummer.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Eintrittskartennummer unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.eintrittskartennummer] = Integer.parseInt(hString);

        hString = tfGastkartennummer.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Gastkartennummer unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.gastkartennummer] = Integer.parseInt(hString);

        hString = tfStimmkartennummer.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Stimmkartennummer unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.stimmkartennummer] = Integer.parseInt(hString);

        hString = tfStimmkartennummerSecond.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Voternummer unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.stimmkartennummerSecond] = Integer.parseInt(hString);

        hString = tfAppIdent.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung App-Identifikation unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.appIdent] = Integer.parseInt(hString);

        hString = tfPersonenIdent.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Persönliche Ident unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.personenIdent] = Integer.parseInt(hString);

        hString = tfEintrittskartennummerMitIndex.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Eintrittskartennummer mit Index unzulässig!");
            return false;
        }
        eclNummernFormSet.klasseZuCode[KonstKartenklasse.eintrittskartennummerNeben] = Integer.parseInt(hString);

        for (int i = 1; i < 7; i++) {
            for (int i1 = i + 1; i1 <= 7; i1++) {
                if (eclNummernFormSet.klasseZuCode[i] == eclNummernFormSet.klasseZuCode[i1]) {
                    zeigeFehlermeldung("Kodierung Kartenklasse nicht eindeutig!");
                    return false;
                }
            }
        }

        hString = tfErstzugang.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Erstzugang unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.erstzugang] = Integer.parseInt(hString);

        hString = tfStimmkartenEtikett.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Stimmkartenetikett unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.stimmkartenEtikett] = Integer.parseInt(hString);

        hString = tfWiederzugang.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Wiederzugang unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.wiederzugang] = Integer.parseInt(hString);

        hString = tfAbgang.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Abgang unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.abgang] = Integer.parseInt(hString);

        hString = tfVollmachtAnDritteErteilen.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Vollmacht an Dritte erteilen unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.vollmachtAnDritteErteilen] = Integer.parseInt(hString);

        hString = tfVollmachtAnDritteEmpfangen.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Vollmacht an Dritte empfangen unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.vollmachtAnDritteEmpfangen] = Integer.parseInt(hString);

        hString = tfStimmabschnittsnummer.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung Stimmabschnittsnumme unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.stimmabschnittsnummer] = Integer.parseInt(hString);

        hString = tfVollmachtWeisungSRV.getText();
        if (hString.length() != 1 || !CaString.isNummern(hString)) {
            zeigeFehlermeldung("Kodierung VollmachtWeisung an SRV unzulässig!");
            return false;
        }
        eclNummernFormSet.artZuCode[KonstKartenart.vollmachtWeisungSRV] = Integer.parseInt(hString);

        for (int i = 1; i < 8; i++) {
            for (int i1 = i + 1; i1 <= 8; i1++) {
                if (eclNummernFormSet.artZuCode[i] == eclNummernFormSet.artZuCode[i1]) {
                    zeigeFehlermeldung("Kodierung Kartenart nicht eindeutig!");
                    return false;
                }
            }
        }

        /*KombiCode*/
        int anzVerwendet[] = new int[10];
        for (int i = 0; i < 10; i++) {
            anzVerwendet[i] = 0;
        }
        for (int i = 1; i <= 7; i++) {
            for (int i1 = 1; i1 <= 8; i1++) {
                hString = tfKombiCode[i][i1].getText().trim();
                if (hString.isEmpty()) {
                    eclNummernFormSet.kombiZuCode[i][i1] = -1;
                } else {
                    if (hString.length() != 1 || !CaString.isNummern(hString)) {
                        zeigeFehlermeldung("Kombi-Code unzulässig!");
                        return false;
                    }
                    int hInt = Integer.parseInt(hString);
                    anzVerwendet[hInt]++;
                    eclNummernFormSet.kombiZuCode[i][i1] = hInt;
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if (anzVerwendet[i] > 1) {
                zeigeFehlermeldung("Kombi-Code " + Integer.toString(i) + " mehrfach verwendet!");
                return false;
            }
        }

        /*KlasseArtNummernform*/
        for (int i = 1; i <= 7; i++) {
            for (int i1 = 0; i1 <= 8; i1++) {
                hString = tfKlasseArtNummernform[i][i1].getText().trim();
                if (hString.isEmpty()) {
                    eclNummernFormSet.nummernformZuKlasseArt[i][i1] = -1;
                } else {
                    if (hString.length() > 5 || !CaString.isNummern(hString)) {
                        zeigeFehlermeldung("Nummernform-Zuordnung bei Klasse/Art unzulässig!");
                        return false;
                    }
                    int hInt = Integer.parseInt(hString);
                    eclNummernFormSet.nummernformZuKlasseArt[i][i1] = hInt;
                    int gef = -1;
                    for (int i2 = 0; i2 < eclNummernFormen.length; i2++) {
                        if (eclNummernFormen[i2].ident == hInt) {
                            gef = i2;
                        }

                    }
                    if (gef == -1) {
                        zeigeFehlermeldung("Nummernform bei Klasse " + Integer.toString(i) + " / Art "
                                + Integer.toString(i1) + " unzulässig!");
                        return false;
                    }
                }
            }
        }

        DbBundle lDbBundle = new DbBundle();

        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        } else {
            if (ParamS.paramServer.nummernformenPflegbar==1) {
                lDbBundle.openAll();
                lDbBundle.dbNummernFormSet.update(eclNummernFormSet);
                lDbBundle.closeAll();
            }

            /** HV-Parameter sicherheitshalber neu holen */
            stubParameter = new StubParameter(false, lDbBundle);
            stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                    lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
            ParamS.param = stubParameter.rcHVParam;

        }

        return true;
    }

    /**************************Anzeigefunktionen***************************************/
    
    private void clearFehlermeldung() {
        tfFehlermeldung.setText("");
    }

    /**
     * Zeige fehlermeldung.
     *
     * @param meldung the meldung
     */
    private void zeigeFehlermeldung(String meldung) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, meldung);
    }

    /**
     * Zeige nummernformen.
     */
    private void zeigeNummernformen() {

        grpnNummernForm = new GridPane();
        grpnNummernForm.setVgap(5);
        grpnNummernForm.setHgap(15);

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbNummernForm.read_all();
        lDbBundle.closeAll();

        eclNummernFormen = lDbBundle.dbNummernForm.ergebnisArray;
        int anzNummernformen = lDbBundle.dbNummernForm.anzErgebnis();
        if (anzNummernformen > 0) {

            Label uLabel0 = new Label("Ident");
            grpnNummernForm.add(uLabel0, 0, 0);

            Label uLabel1 = new Label("Sel.");
            grpnNummernForm.add(uLabel1, 1, 0);

            Label uLabel2 = new Label("Format");
            grpnNummernForm.add(uLabel2, 2, 0);

            Label uLabel3 = new Label("Mandant");
            grpnNummernForm.add(uLabel3, 3, 0);

            Label uLabel4 = new Label("Gel.");
            grpnNummernForm.add(uLabel4, 4, 0);

            Label uLabel5 = new Label("ers.d.");
            grpnNummernForm.add(uLabel5, 5, 0);

            Label uLabel6 = new Label("Beschreibung");
            grpnNummernForm.add(uLabel6, 6, 0);

            cbNummernformAusgewaehlt = new CheckBox[anzNummernformen];
            for (int i = 0; i < anzNummernformen; i++) {
                EclNummernForm lNummernForm = lDbBundle.dbNummernForm.ergebnisArray[i];

                Label hLabel0 = new Label(Integer.toString(lNummernForm.ident));
                grpnNummernForm.add(hLabel0, 0, i + 1);

                cbNummernformAusgewaehlt[i] = new CheckBox();
                grpnNummernForm.add(cbNummernformAusgewaehlt[i], 1, i + 1);

                String anzeigeString = "";
                String kodierung = lNummernForm.kodierung;
                if (!kodierung.isEmpty()) {
                    for (int i1 = 0; i1 < kodierung.length(); i1++) {
                        String zeichen = kodierung.substring(i1, i1 + 1);
                        switch (zeichen) {
                        case "a":
                            anzeigeString += "a";
                            break;
                        case "b":
                            anzeigeString += "b";
                            break;
                        case "c":
                            anzeigeString += "c";
                            break;
                        case "d":
                            anzeigeString += "d";
                            break;
                        case "e":
                            anzeigeString += "eeeeee";
                            break;
                        case "f":
                            anzeigeString += "fff";
                            break;
                        case "g":
                            anzeigeString += "ggg";
                            break;
                        case "h":
                            anzeigeString += "hh";
                            break;
                        case "i":
                            anzeigeString += "i";
                            break;
                        case "j":
                            anzeigeString += "jj";
                            break;
                        case "k":
                            anzeigeString += "k";
                            break;
                        case "l":
                            anzeigeString += "l";
                            break;
                        case "m":
                            anzeigeString += "mmmmm";
                            break;
                        case "n":
                            anzeigeString += "nn";
                            break;
                        case "o":
                            anzeigeString += "o";
                            break;
                        case "p":
                            anzeigeString += "p";
                            break;
                        case "q":
                            anzeigeString += "q";
                            break;
                        default:
                            anzeigeString += "<" + zeichen + " Fehler>";
                            break;
                        }
                    }
                }

                Label hLabel2 = new Label(anzeigeString);
                grpnNummernForm.add(hLabel2, 2, i + 1);

                Label hLabel3 = new Label(Integer.toString(lNummernForm.mandant));
                grpnNummernForm.add(hLabel3, 3, i + 1);

                Label hLabel4 = new Label(Integer.toString(lNummernForm.geloescht));
                grpnNummernForm.add(hLabel4, 4, i + 1);

                Label hLabel5 = new Label(Integer.toString(lNummernForm.ersetztDurch));
                grpnNummernForm.add(hLabel5, 5, i + 1);

                Label hLabel6 = new Label(lNummernForm.beschreibung);
                grpnNummernForm.add(hLabel6, 6, i + 1);

            }
        } else {
            cbNummernformAusgewaehlt = null;
        }

        pnNummernformen.setContent(grpnNummernForm);

    }

    /**
     * Zeige nummernform sets.
     */
    private void zeigeNummernformSets() {
        comboAktivesSet.getItems().clear();
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbNummernFormSet.read_all();
        int anz = lDbBundle.dbNummernFormSet.anzErgebnis();
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclNummernFormSet lNummernFormSet = lDbBundle.dbNummernFormSet.ergebnisArray[i];
                String hString = lNummernFormSet.name;
                hString = hString + " (" + Integer.toString(lNummernFormSet.ident) + ")";
                comboAktivesSet.getItems().addAll(hString);
                if (lNummernFormSet.ident == ParamS.param.paramNummernformen.ident) {
                    eclNummernFormSet = lNummernFormSet;
                    zeigeSet();
                    comboAktivesSet.setValue(hString);

                }
                ;

            }
        }

        lDbBundle.closeAll();

    }

    /**
     * Zeige set.
     */
    private void zeigeSet() {
        /*Titel-Bereich*/
        tfMandant.setText(Integer.toString(eclNummernFormSet.mandant));
        tfBeschreibung.setText(eclNummernFormSet.beschreibung);
        if (eclNummernFormSet.klassifizierung == 1) {
            rbEinstellig.setSelected(true);
        } else {
            rbZweistellig.setSelected(true);
        }

        /*Mandantenabhängige Nummernformparameter*/
        tfHVIdent.setText(ParamS.param.paramPruefzahlen.identifikationsNummer);
        tfKontrollzahl3.setText(ParamS.param.paramPruefzahlen.dreistelligeKontrollzahl);
        tfKontrollzahl2.setText(ParamS.param.paramPruefzahlen.zweistelligeKontrollzahl);
        tfKontrollzahl1.setText(ParamS.param.paramPruefzahlen.einstelligeKontrollzahl);

        /*Nummernkreise*/

        /*Länge*/
        tfEKLaenge.setText(
                Integer.toString(eclNummernFormSet.laengeKartennummer[KonstKartenklasse.eintrittskartennummer]));
        tfGKLaenge.setText(Integer.toString(eclNummernFormSet.laengeKartennummer[KonstKartenklasse.gastkartennummer]));
        tfSKLaenge.setText(Integer.toString(eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummer]));
        tfSKSLaenge.setText(
                Integer.toString(eclNummernFormSet.laengeKartennummer[KonstKartenklasse.stimmkartennummerSecond]));
        tfPILaenge.setText(Integer.toString(eclNummernFormSet.laengeKartennummer[KonstKartenklasse.personenIdent]));

        /*IstNumerisch*/
        if (eclNummernFormSet.istNumerisch[KonstKartenklasse.eintrittskartennummer] == false) {
            cbEKAlpha.setSelected(true);
        } else {
            cbEKAlpha.setSelected(false);
        }
        if (eclNummernFormSet.istNumerisch[KonstKartenklasse.gastkartennummer] == false) {
            cbGKAlpha.setSelected(true);
        } else {
            cbGKAlpha.setSelected(false);
        }
        if (eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummer] == false) {
            cbSKAlpha.setSelected(true);
        } else {
            cbSKAlpha.setSelected(false);
        }
        if (eclNummernFormSet.istNumerisch[KonstKartenklasse.stimmkartennummerSecond] == false) {
            cbSKSAlpha.setSelected(true);
        } else {
            cbSKSAlpha.setSelected(false);
        }

        /*VonGesamt*/
        tfEKVonGesamt.setText(
                Integer.toString(eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]));
        tfGKVonGesamt
                .setText(Integer.toString(eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.gastkartennummer]));
        tfSKVonGesamt.setText(
                Integer.toString(eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.stimmkartennummer]));
        tfSKSVonGesamt.setText(
                Integer.toString(eclNummernFormSet.vonKartennummerGesamt[KonstKartenklasse.stimmkartennummerSecond]));

        /*BisGesamt*/
        tfEKBisGesamt.setText(
                Integer.toString(eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.eintrittskartennummer]));
        tfGKBisGesamt
                .setText(Integer.toString(eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.gastkartennummer]));
        tfSKBisGesamt.setText(
                Integer.toString(eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.stimmkartennummer]));
        tfSKSBisGesamt.setText(
                Integer.toString(eclNummernFormSet.bisKartennummerGesamt[KonstKartenklasse.stimmkartennummerSecond]));

        /*VonManuell*/
        tfGKVonManuell.setText(
                Integer.toString(eclNummernFormSet.vonKartennummerManuell[KonstKartenklasse.gastkartennummer]));

        /*BisManuell*/
        tfGKBisManuell.setText(
                Integer.toString(eclNummernFormSet.bisKartennummerManuell[KonstKartenklasse.gastkartennummer]));

        /*VonAuto*/
        tfGKVonAuto
                .setText(Integer.toString(eclNummernFormSet.vonKartennummerAuto[KonstKartenklasse.gastkartennummer]));

        /*BisAuto*/
        tfGKBisAuto
                .setText(Integer.toString(eclNummernFormSet.bisKartennummerAuto[KonstKartenklasse.gastkartennummer]));

        /*Sammelkarten*/
        tfEKSammelkVon.setText(Integer.toString(eclNummernFormSet.vonSammelkartennummer));
        tfEKSammelkBis.setText(Integer.toString(eclNummernFormSet.bisSammelkartennummer));

        /*Eintrittskarten-Subkreise*/
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 2; i1++) {
                tfEKSubKreisVon[i][i1].setText(Integer.toString(eclNummernFormSet.vonSubEintrittskartennummer[i][i1]));
                tfEKSubKreisBis[i][i1].setText(Integer.toString(eclNummernFormSet.bisSubEintrittskartennummer[i][i1]));
                ;
            }
        }

        /*Stimmkarten-Subkreise*/
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 5; i1++) {
                tfSKSubKreisVon[i][i1].setText(Integer.toString(eclNummernFormSet.vonSubStimmkartennummer[i][i1]));
                tfSKSubKreisBis[i][i1].setText(Integer.toString(eclNummernFormSet.bisSubStimmkartennummer[i][i1]));
                ;
            }
        }

        /*Nummernformparameter*/
        tfJa.setText(Integer.toString(eclNummernFormSet.kodierungJa));
        tfNein.setText(Integer.toString(eclNummernFormSet.kodierungNein));
        tfEnthaltung.setText(Integer.toString(eclNummernFormSet.kodierungEnthaltung));

        int hpruefungTeil = 1;
        int hpruefungVerfahren = eclNummernFormSet.berechnungsVerfahrenPruefziffer;
        if (hpruefungVerfahren > 10) {
            hpruefungVerfahren -= 10;
            hpruefungTeil = 2;
        }
        switch (hpruefungVerfahren) {
        case 1:
            rbPruefziffer1.setSelected(true);
            break;
        case 2:
            rbPruefziffer2.setSelected(true);
            break;
        case 3:
            rbPruefziffer3.setSelected(true);
            break;
        case 4:
            rbPruefziffer4.setSelected(true);
            break;
        }
        switch (hpruefungTeil) {
        case 1:
            rbPruefzifferEKSK.setSelected(true);
            break;
        case 2:
            rbPruefzifferGesamt.setSelected(true);
            break;
        }

        /*Klasse, Art*/
        tfEintrittskartennummer
                .setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.eintrittskartennummer]));
        tfGastkartennummer
                .setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.gastkartennummer]));
        tfStimmkartennummer
                .setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.stimmkartennummer]));
        tfStimmkartennummerSecond
                .setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.stimmkartennummerSecond]));
        tfAppIdent.setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.appIdent]));
        tfPersonenIdent.setText(Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.personenIdent]));
        tfEintrittskartennummerMitIndex.setText(
                Integer.toString(eclNummernFormSet.klasseZuCode[KonstKartenklasse.eintrittskartennummerNeben]));

        tfErstzugang.setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.erstzugang]));
        tfStimmkartenEtikett.setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.stimmkartenEtikett]));
        tfWiederzugang.setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.wiederzugang]));
        tfAbgang.setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.abgang]));
        tfVollmachtAnDritteErteilen
                .setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.vollmachtAnDritteErteilen]));
        tfVollmachtAnDritteEmpfangen
                .setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.vollmachtAnDritteEmpfangen]));
        tfStimmabschnittsnummer
                .setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.stimmabschnittsnummer]));
        tfVollmachtWeisungSRV
                .setText(Integer.toString(eclNummernFormSet.artZuCode[KonstKartenart.vollmachtWeisungSRV]));

        /*KombiCode*/
        grpnKombiCode = new GridPane();
        grpnKombiCode.setVgap(5);
        grpnKombiCode.setHgap(15);

        lbKombiCodeKlassen = new Label[10];
        for (int i = 1; i <= 7; i++) {
            lbKombiCodeKlassen[i] = new Label();
            switch (i) {
            case 1:
                lbKombiCodeKlassen[i].setText("Eintrittskartennummer");
                break;
            case 2:
                lbKombiCodeKlassen[i].setText("Gastkartennummer");
                break;
            case 3:
                lbKombiCodeKlassen[i].setText("Stimmkartennummer");
                break;
            case 4:
                lbKombiCodeKlassen[i].setText("Voternummer");
                break;
            case 5:
                lbKombiCodeKlassen[i].setText("App-Identifikation");
                break;
            case 6:
                lbKombiCodeKlassen[i].setText("Persönliche Ident");
                break;
            case 7:
                lbKombiCodeKlassen[i].setText("Eintrittskartennummer mit Index");
                break;
            }
            grpnKombiCode.add(lbKombiCodeKlassen[i], 0, i);
        }

        lbKombiCodeArten = new Label[10];
        for (int i = 1; i <= 8; i++) {
            lbKombiCodeArten[i] = new Label();
            switch (i) {
            case 1:
                lbKombiCodeArten[i].setText("Erstzug.");
                break;
            case 2:
                lbKombiCodeArten[i].setText("SK-Etikett");
                break;
            case 3:
                lbKombiCodeArten[i].setText("Wiederz.");
                break;
            case 4:
                lbKombiCodeArten[i].setText("Abgang");
                break;
            case 5:
                lbKombiCodeArten[i].setText("Vollm.Dri.ert.");
                break;
            case 6:
                lbKombiCodeArten[i].setText("Vollm.Dri.empf.");
                break;
            case 7:
                lbKombiCodeArten[i].setText("Stimmabschnittsnr");
                break;
            case 8:
                lbKombiCodeArten[i].setText("Vollm./Weis. SRV");
                break;
            }
            grpnKombiCode.add(lbKombiCodeArten[i], i, 0);
        }

        tfKombiCode = new TextField[10][];
        for (int i = 1; i <= 7; i++) {
            tfKombiCode[i] = new TextField[10];
            for (int i1 = 1; i1 <= 8; i1++) {
                tfKombiCode[i][i1] = new TextField();
                tfKombiCode[i][i1].setPrefWidth(10);
                if (eclNummernFormSet.kombiZuCode[i][i1] != -1) {
                    tfKombiCode[i][i1].setText(Integer.toString(eclNummernFormSet.kombiZuCode[i][i1]));
                } else {
                    tfKombiCode[i][i1].setText("");
                }
                grpnKombiCode.add(tfKombiCode[i][i1], i1, i);

            }

        }
        pnKombi.setContent(grpnKombiCode);

        /*KlasseArtNummernform*/
        grpnKlasseArtNummernform = new GridPane();
        grpnKlasseArtNummernform.setVgap(5);
        grpnKlasseArtNummernform.setHgap(15);

        lbKlasseArtNummernformKlassen = new Label[10];
        for (int i = 1; i <= 7; i++) {
            lbKlasseArtNummernformKlassen[i] = new Label();
            switch (i) {
            case 1:
                lbKlasseArtNummernformKlassen[i].setText("Eintrittskartennummer");
                break;
            case 2:
                lbKlasseArtNummernformKlassen[i].setText("Gastkartennummer");
                break;
            case 3:
                lbKlasseArtNummernformKlassen[i].setText("Stimmkartennummer");
                break;
            case 4:
                lbKlasseArtNummernformKlassen[i].setText("Voternummer");
                break;
            case 5:
                lbKlasseArtNummernformKlassen[i].setText("App-Identifikation");
                break;
            case 6:
                lbKlasseArtNummernformKlassen[i].setText("Persönliche Ident");
                break;
            case 7:
                lbKlasseArtNummernformKlassen[i].setText("Eintrittskartennummer mit Index");
                break;
            }
            grpnKlasseArtNummernform.add(lbKlasseArtNummernformKlassen[i], 0, i);
        }

        lbKlasseArtNummernformArten = new Label[10];
        for (int i = 0; i <= 8; i++) {
            lbKlasseArtNummernformArten[i] = new Label();
            switch (i) {
            case 0:
                lbKlasseArtNummernformArten[i].setText("Superform");
                break;
            case 1:
                lbKlasseArtNummernformArten[i].setText("Erstzug.");
                break;
            case 2:
                lbKlasseArtNummernformArten[i].setText("SK-Etikett");
                break;
            case 3:
                lbKlasseArtNummernformArten[i].setText("Wiederz.");
                break;
            case 4:
                lbKlasseArtNummernformArten[i].setText("Abgang");
                break;
            case 5:
                lbKlasseArtNummernformArten[i].setText("Vollm.Dri.ert.");
                break;
            case 6:
                lbKlasseArtNummernformArten[i].setText("Vollm.Dri.empf.");
                break;
            case 7:
                lbKlasseArtNummernformArten[i].setText("Stimmabschnittsnr");
                break;
            case 8:
                lbKlasseArtNummernformArten[i].setText("Vollm./Weis. SRV");
                break;
            }
            grpnKlasseArtNummernform.add(lbKlasseArtNummernformArten[i], i + 1, 0);
        }

        tfKlasseArtNummernform = new TextField[10][];
        for (int i = 1; i <= 7; i++) {
            tfKlasseArtNummernform[i] = new TextField[10];
            for (int i1 = 0; i1 <= 8; i1++) {
                tfKlasseArtNummernform[i][i1] = new TextField();
                tfKlasseArtNummernform[i][i1].setPrefWidth(10);
                if (eclNummernFormSet.nummernformZuKlasseArt[i][i1] != -1) {
                    tfKlasseArtNummernform[i][i1]
                            .setText(Integer.toString(eclNummernFormSet.nummernformZuKlasseArt[i][i1]));
                } else {
                    tfKlasseArtNummernform[i][i1].setText("");
                }
                grpnKlasseArtNummernform.add(tfKlasseArtNummernform[i][i1], i1 + 1, i);

            }

        }
        pnKlasseArt.setContent(grpnKlasseArtNummernform);

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

}
