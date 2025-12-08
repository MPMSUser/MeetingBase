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

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstHVArtSchluessel;
import de.meetingapps.meetingportal.meetComKonst.KonstTermine;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterEmittent.
 */
public class CtrlParameterEmittent extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The tab pane. */
    @FXML
    private TabPane tabPane;

    /** The tf bezeichnung kurz. */
    /*
     * Tab Emittent
     */
    @FXML
    private TextField tfBezeichnungKurz;

    /** The tf bezeichnung lang. */
    @FXML
    private TextField tfBezeichnungLang;

    /** The cb portal. */
    @FXML
    private ComboBox<String> cbPortal;

    /** The cb app. */
    @FXML
    private ComboBox<String> cbApp;

    /** The cb formular. */
    @FXML
    private ComboBox<String> cbFormular;

    /** The tf abteilung. */
    @FXML
    private TextField tfAbteilung;

    /** The tf strasse. */
    @FXML
    private TextField tfStrasse;

    /** The tf plz. */
    @FXML
    private TextField tfPlz;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The cb land. */
    @FXML
    private ComboBox<EclStaaten> cbLand;

    /** The lbl Pfad. */
    @FXML
    private Label lblPfad;

    /** The tf pfad. */
    @FXML
    private TextField tfPfad;

    /** The btn pfad. */
    @FXML
    private Button btnPfad;

    /** The tf consultant. */
    @FXML
    private TextField tfConsultant;

    /** The tf telefon. */
    @FXML
    private TextField tfTelefon;

    /** The tf fax. */
    @FXML
    private TextField tfFax;

    /** The tf email. */
    @FXML
    private TextField tfEmail;

    /** The scroll pane isin. */
    @FXML
    private ScrollPane scrollPane_Isin;

    /** The h box scroll pane. */
    @FXML
    private HBox hBoxScrollPane;

    /** The cb art. */
    /*
     * Tab Hauptversammlung
     */
    @FXML
    private ComboBox<String> cbArt;

    /** The tf HV location. */
    @FXML
    private TextField tfHVLocation;

    /** The tf HV strasse. */
    @FXML
    private TextField tfHVStrasse;

    /** The tf HV plz. */
    @FXML
    private TextField tfHVPlz;

    /** The tf HV ort. */
    @FXML
    private TextField tfHVOrt;

    /** The cb HV land. */
    @FXML
    private ComboBox<EclStaaten> cbHVLand;

    /** The tf kuerzel. */
    @FXML
    private TextField tfBundesanzeiger;

    /** The dp HV datum. */
    @FXML
    private DatePicker dpHVDatum;

    /** The tf HV beginn. */
    @FXML
    private TextField tfHVBeginn;

    /** The tf HV einlass. */
    @FXML
    private TextField tfHVEinlass;

    /** The dp HV letzter anm. */
    @FXML
    private DatePicker dpHVLetzterAnm;

    /** The cb ber consultant. */
    @FXML
    private CheckBox cbBerConsultant;

    /** The cb ber anmelde erfassung. */
    @FXML
    private CheckBox cbBerAnmeldeErfassung;

    /** The cb ber anmelde master. */
    @FXML
    private CheckBox cbBerAnmeldeMaster;

    /** The cb ber administrator. */
    @FXML
    private CheckBox cbBerAdministrator;

    /** The cb ber hotline. */
    @FXML
    private CheckBox cbBerHotline;

    /** The gp kapital. */
    /*
     * Tab Kapital
     */
    @FXML
    private GridPane gpKapital;

    /** The btn kapital refresh. */
    @FXML
    private Button btnKapitalRefresh;

    /** The btn beenden. */
    /*
     * Übergreifend
     */
    @FXML
    private Button btnBeenden;

    /** The flow fehler. */
    @FXML
    private TextFlow flowFehler;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** ************* Ab hier individuelle Deklarationen ******************. */

    private TextField[] tfKapitalStueckAktien = new TextField[6];

    /** The tf kapital stueck aktien euro. */
    private TextField[] tfKapitalStueckAktienEuro = new TextField[6];

    /** The tf kapital vermindert stueck aktien. */
    private TextField[] tfKapitalVermindertStueckAktien = new TextField[6];

    /** The tf kapital vermindert stueck aktien euro. */
    private TextField[] tfKapitalVermindertStueckAktienEuro = new TextField[6];

    /** The tf kapital wert einer aktie. */
    private TextField[] tfKapitalWertEinerAktie = new TextField[6];

    /** The tf kapital anzahl nachkommastellen kapital. */
    private TextField[] tfKapitalAnzahlNachkommastellenKapital = new TextField[6];

    /** The cb haupt isin. */
    private List<ComboBox<EclIsin>> cbHauptIsin = new LinkedList<>();

    /** The tf eintrittskarte neu vergeben. */
    private TextField[] tfEintrittskarteNeuVergeben = new TextField[6];

    /** The tf zugang moeglich. */
    private TextField[] tfZugangMoeglich = new TextField[6];

    /** The tf eigene aktien. */
    private TextField[] tfEigeneAktien = new TextField[6];

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /** The kurz. */
    private final String KURZ = "kurz";

    /** The lang. */
    private final String LANG = "lang";

    /** The isin gattung map. */
    private Map<TextField, Integer> isinGattungMap = new HashMap<>();

    /** The isin list. */
    private List<EclIsin> isinList = null;

    /** The isin delete list. */
    private List<EclIsin> isinDeleteList = null;

    /** The isin insert list. */
    private List<EclIsin> isinInsertList = null;

    /** The aendung kapital. */
    private Boolean aendungKapital = false;

    /** The emittent. */
    private EclEmittenten emittent = null;

    /** The letzter anmeldetag. */
    private EclTermine letzterAnmeldetag;

    /** The id letzter anm. */
    final private int idLetzterAnm = 6;

    /** The hv tag. */
    private EclTermine hvTag;

    /** The id hv tag. */
    final private int idHvTag = 7;

    /** The hv einlass. */
    private EclTermine hvEinlass;

    /** The id hv einlass. */
    final private int idHvEinlass = 9;

    /** The bl reg. */
    private BlAktienregister blReg;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        lDbBundle.dbStaaten.readAll(1);
        List<EclStaaten> staaten = lDbBundle.dbStaaten.ergebnis();

        lDbBundle.dbTermine.read(idLetzterAnm);
        letzterAnmeldetag = lDbBundle.dbTermine.anzErgebnis() > 0 ? lDbBundle.dbTermine.ergebnisArray[0] : null;

        lDbBundle.dbTermine.read(idHvTag);
        hvTag = lDbBundle.dbTermine.anzErgebnis() > 0 ? lDbBundle.dbTermine.ergebnisArray[0] : null;

        lDbBundle.dbTermine.read(idHvEinlass);
        hvEinlass = lDbBundle.dbTermine.anzErgebnis() > 0 ? lDbBundle.dbTermine.ergebnisArray[0] : null;

        lDbBundle.closeAll();

        /**
         * Zur Sicherheit nochmals "Raw" einlesen, damit evtl. vorhandene
         * Puffer-Konflikte nicht zum Tragen kommen!
         */
        DbBundle l1DbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, l1DbBundle);
        stubParameter.leseHVParam_all(l1DbBundle.clGlobalVar.mandant, l1DbBundle.clGlobalVar.hvJahr,
                l1DbBundle.clGlobalVar.hvNummer, l1DbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        stubParameter.leseEmittent(l1DbBundle.clGlobalVar.mandant, l1DbBundle.clGlobalVar.hvJahr,
                l1DbBundle.clGlobalVar.hvNummer, l1DbBundle.clGlobalVar.datenbereich);
        ParamS.eclEmittent = stubParameter.rcEmittent;

        //      Tab Hauptversammlung
        cbArt.getItems().setAll(KonstHVArtSchluessel.liefereListeFuerAnzeige());
        cbArt.setValue(KonstHVArtSchluessel.liefereBezeichnungFuerAuswahl(ParamS.eclEmittent.hvArtSchluessel));

        tfHVLocation.setText(ParamS.eclEmittent.veranstaltungGebäude);
        tfLengthValidator(tfHVLocation, 100, "Veranstaltungsort");
        tfHVStrasse.setText(ParamS.eclEmittent.veranstaltungStrasse);
        tfLengthValidator(tfHVStrasse, 80, "Strasse");
        tfHVPlz.setText(ParamS.eclEmittent.veranstaltungPostleitzahl);
        tfLengthValidator(tfHVPlz, 80, "PLZ");
        tfHVOrt.setText(ParamS.eclEmittent.hvOrt);
        tfLengthValidator(tfHVOrt, 80, "HV Ort");
        cbHVLand.setValue(staaten.stream().filter(e -> e.getId() == ParamS.eclEmittent.veranstaltungStaatId).findFirst()
                .orElse(null));
        ComboBoxZusatz.configureStaatenBox(cbHVLand, staaten);
        
        tfBundesanzeiger.setText(ParamS.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom);
        tfLengthValidator(tfBundesanzeiger, 200, "Bundesanzeiger - Datum");

        //      Termine
        if (!ParamS.eclEmittent.hvDatum.isBlank())
            dpHVDatum.setValue(LocalDate.parse(ParamS.eclEmittent.hvDatum, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        evaluateDate(dpHVDatum);

        tfHVBeginn.setText(hvTag != null ? hvTag.terminZeit : "");
        evaluateTime(tfHVBeginn);
        tfHVEinlass.setText(hvEinlass != null ? hvEinlass.terminZeit : "");
        evaluateTime(tfHVEinlass);

        if (letzterAnmeldetag != null && !letzterAnmeldetag.terminDatum.isBlank())
            dpHVLetzterAnm.setValue(LocalDate.parse(letzterAnmeldetag.terminDatum, toLocalDate()));
        evaluateDate(dpHVLetzterAnm);

        //      Zugriff
        cbBerConsultant.setSelected(ParamS.eclEmittent.inAuswahlConsultant());
        cbBerAnmeldeErfassung.setSelected(ParamS.eclEmittent.inAuswahlAnmeldestelleErfassung());
        cbBerAnmeldeMaster.setSelected(ParamS.eclEmittent.inAuswahlAnmeldestelleMaster());
        cbBerAdministrator.setSelected(ParamS.eclEmittent.inAuswahlAdministrator());
        cbBerHotline.setSelected(ParamS.eclEmittent.inAuswahlHotline());

        //      Tab Emittent
        tfBezeichnungKurz.setText(ParamS.eclEmittent.bezeichnungKurz);
        tfLengthValidator(tfBezeichnungKurz, 80, "Bezeichnung Kurz");
        tfBezeichnungLang.setText(ParamS.eclEmittent.bezeichnungLang);
        tfLengthValidator(tfBezeichnungLang, 200, "Bezeichnung Lang");

        cbPortal.getItems().setAll(KURZ, LANG);
        cbPortal.setValue(ParamS.eclEmittent.bezeichnungsArtPortal == 1 ? KURZ : LANG);
        cbPortal.setDisable(!ParamS.param.paramModuleKonfigurierbar.aktionaersportal);

        cbApp.getItems().setAll(KURZ, LANG);
        cbApp.setValue(ParamS.eclEmittent.bezeichnungsArtApp == 1 ? KURZ : LANG);
        cbApp.setDisable(!ParamS.param.paramModuleKonfigurierbar.hvApp);

        cbFormular.getItems().setAll(KURZ, LANG);
        cbFormular.setValue(ParamS.eclEmittent.bezeichnungsArtFormulare == 1 ? KURZ : LANG);

        tfAbteilung.setText(ParamS.eclEmittent.abteilung);
        tfLengthValidator(tfAbteilung, 100, "Abteilung");
        tfStrasse.setText(ParamS.eclEmittent.strasseGesellschaft);
        tfLengthValidator(tfStrasse, 100, "Strasse");
        tfPlz.setText(ParamS.eclEmittent.postleitzahl);
        tfLengthValidator(tfPlz, 10, "PLZ");
        tfOrt.setText(ParamS.eclEmittent.ort);
        tfLengthValidator(tfOrt, 100, "Ort");
        cbLand.setValue(staaten.stream().filter(e -> e.getId() == ParamS.eclEmittent.staatId).findFirst().orElse(null));
        ComboBoxZusatz.configureStaatenBox(cbLand, staaten);
        lblPfad.setText("Pfad: " + ParamS.clGlobalVar.lwPfadKundenordnerBasis);
        tfPfad.setText(ParamS.eclEmittent.pfadErgKundenOrdner);
        tfLengthValidator(tfPfad, 80, "Pfad Kundenorder");

        tfConsultant.setText(ParamS.param.paramBasis.mailConsultant);
        tfTelefon.setText(ParamS.eclEmittent.telefon);
        tfLengthValidator(tfTelefon, 80, "Telefon");
        tfFax.setText(ParamS.eclEmittent.fax);
        tfLengthValidator(tfFax, 80, "Fax");
        tfEmail.setText(ParamS.eclEmittent.email);
        tfLengthValidator(tfEmail, 80, "E-Mail");

        //        scrollPane_Isin.prefWidthProperty().bind(tabPane.widthProperty().divide(2).subtract(45));
        //        scrollPane_Isin.prefHeightProperty().bind(tabPane.heightProperty().divide(3));

        /********************* Kapital **********************/
        for (int i = 1; i <= 5; i++) {
            cbHauptIsin.add(new ComboBox<EclIsin>());
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {

                Label lGattungBez = new Label();
                lGattungBez.setText(ParamS.param.paramBasis.getGattungBezeichnung(i));
                gpKapital.add(lGattungBez, i, 0);

                Label lGattungBezKurz = new Label();
                lGattungBezKurz.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(i));
                gpKapital.add(lGattungBezKurz, i, 1);

                tfKapitalStueckAktien[i] = new TextField();
                gpKapital.add(tfKapitalStueckAktien[i], i, 2);
                GridPane.setMargin(tfKapitalStueckAktien[i], new Insets(5));

                tfKapitalStueckAktienEuro[i] = new TextField();
                tfKapitalStueckAktienEuro[i].setEditable(false);
                gpKapital.add(tfKapitalStueckAktienEuro[i], i, 3);
                GridPane.setMargin(tfKapitalStueckAktienEuro[i], new Insets(5));

                tfKapitalVermindertStueckAktien[i] = new TextField();
                gpKapital.add(tfKapitalVermindertStueckAktien[i], i, 4);
                GridPane.setMargin(tfKapitalVermindertStueckAktien[i], new Insets(5));

                tfKapitalVermindertStueckAktienEuro[i] = new TextField();
                tfKapitalVermindertStueckAktienEuro[i].setEditable(false);
                gpKapital.add(tfKapitalVermindertStueckAktienEuro[i], i, 5);
                GridPane.setMargin(tfKapitalVermindertStueckAktienEuro[i], new Insets(5));

                tfKapitalWertEinerAktie[i] = new TextField();
                gpKapital.add(tfKapitalWertEinerAktie[i], i, 6);
                GridPane.setMargin(tfKapitalWertEinerAktie[i], new Insets(5));

                tfKapitalAnzahlNachkommastellenKapital[i] = new TextField();
                gpKapital.add(tfKapitalAnzahlNachkommastellenKapital[i], i, 7);
                GridPane.setMargin(tfKapitalAnzahlNachkommastellenKapital[i], new Insets(5));

                final int x = i - 1;

                cbHauptIsin.get(x).setMaxWidth(Double.MAX_VALUE);
                cbHauptIsin.get(x).setConverter(ComboBoxZusatz.convertIsin(cbHauptIsin.get(i - 1)));
                cbHauptIsin.get(x).setOnKeyPressed(e -> {
                    if (e.getCode().equals(KeyCode.DELETE))
                        cbHauptIsin.get(x).setValue(null);
                });
                gpKapital.add(cbHauptIsin.get(x), i, 8);
                GridPane.setMargin(cbHauptIsin.get(x), new Insets(5));

                tfEintrittskarteNeuVergeben[i] = new TextField();
                tfEintrittskarteNeuVergeben[i]
                        .setText(ParamS.param.paramBasis.liefereEintrittskarteNeuVergeben(i) ? "1" : "0");
                gpKapital.add(tfEintrittskarteNeuVergeben[i], i, 9);
                GridPane.setMargin(tfEintrittskarteNeuVergeben[i], new Insets(5));

                tfZugangMoeglich[i] = new TextField();
                tfZugangMoeglich[i].setText(ParamS.param.paramBasis.liefereZugangMoeglich(i) ? "1" : "0");
                gpKapital.add(tfZugangMoeglich[i], i, 10);
                GridPane.setMargin(tfZugangMoeglich[i], new Insets(5));

                tfEigeneAktien[i] = new TextField();
                gpKapital.add(tfEigeneAktien[i], i, 11);
                GridPane.setMargin(tfEigeneAktien[i], new Insets(5));

            } else {
                gpKapital.getColumnConstraints().remove(gpKapital.getColumnCount() - 1);
            }
        }

        blReg = new BlAktienregister(false, lDbBundle);
        blReg.readAktienregisterIsin();

        fuelleIsinBox();

    }

    /**
     * On speichern.
     *
     * @param event the event
     */
    @FXML
    void onSpeichern(ActionEvent event) {

        flowFehler.getChildren().clear();

        /**************** Prüfen ***********************/
        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                pruefe01(tfEintrittskarteNeuVergeben[i], "Immer neue Eintrittskartennummer vergeben");
                pruefe01(tfZugangMoeglich[i], "Zugang möglich");
            }
        }

        if (lFehlertext.isEmpty()) {
            lFehlertext = checkKapital();
        }

        if (!lFehlertext.isEmpty()) {
            fehlerMeldung(lFehlertext);
            return;
        }

        /*
         * Emittent erstellen auf Grundlage des bestehenden 
         * und mit Feldern der Oberfäche initialisieren
         */
        EclEmittenten tmpEmittent = getValues(new EclEmittenten(emittent));
        //      Wenn update = false > dann gibt es keine Änderung
        Boolean update = false;

        if (aendungKapital || !emittent.equals(tmpEmittent) || checkSonstige())
            update = true;

        Boolean cTermine = checkTermine();

        if (cTermine) {
            Platform.runLater(() -> flowFehler.getChildren().add(new Text("Termin Änderung wurde gespeichert.")));
            lDbBundle.openAllOhneParameterCheck();

            if (hvTag == null) {
                if (!tfHVBeginn.getText().isBlank())
                    lDbBundle.dbTermine.insert(erstelleTermin(idHvTag, "", tfHVBeginn.getText().trim()));
            } else if (!hvTag.terminZeit.equals(tfHVBeginn.getText().trim()) || !hvTag.terminDatum
                    .equals(dpHVDatum.getValue() == null ? "" : dpHVDatum.getValue().format(toLocalDate()))) {
                hvTag.terminDatum = dpHVDatum.getValue() == null ? "" : dpHVDatum.getValue().format(toLocalDate());
                hvTag.terminZeit = tfHVBeginn.getText().trim();
                lDbBundle.dbTermine.update(hvTag);
            }

            if (hvEinlass == null) {
                if (!tfHVEinlass.getText().isBlank())
                    lDbBundle.dbTermine.insert(erstelleTermin(idHvEinlass, "", tfHVEinlass.getText().trim()));
            } else if (!hvEinlass.terminZeit.equals(tfHVEinlass.getText().trim())) {
                hvEinlass.terminZeit = tfHVEinlass.getText().trim();
                lDbBundle.dbTermine.update(hvEinlass);
            }

            if (letzterAnmeldetag == null) {
                if (dpHVLetzterAnm.getValue() != null)
                    lDbBundle.dbTermine
                            .insert(erstelleTermin(idLetzterAnm, dpHVLetzterAnm.getValue().format(toLocalDate()), ""));
            } else if (!letzterAnmeldetag.terminDatum
                    .equals(dpHVLetzterAnm.getValue() == null ? "" : dpHVLetzterAnm.getValue().format(toLocalDate()))) {
                letzterAnmeldetag.terminDatum = dpHVLetzterAnm.getValue() == null ? ""
                        : dpHVLetzterAnm.getValue().format(toLocalDate());
                lDbBundle.dbTermine.update(letzterAnmeldetag);
            }

            lDbBundle.closeAll();
        }

        if (checkIsin()) {
            Platform.runLater(() -> flowFehler.getChildren().add(new Text("ISIN Änderung wurde gespeichert.")));

            lDbBundle.openAllOhneParameterCheck();
            lDbBundle.openWeitere();

            for (EclIsin i : isinDeleteList) {
                lDbBundle.dbIsin.delete(i.isin);

                for (var cb : cbHauptIsin) {
                    if (cb.getValue() != null && cb.getValue().equals(i)) {
                        cb.setValue(null);
                        update = true;
                    }
                }
            }

            for (EclIsin i : isinInsertList)
                lDbBundle.dbIsin.insert(i);

            lDbBundle.dbIsin.readAll();
            isinList = lDbBundle.dbIsin.ergebnis();

            lDbBundle.closeAll();

            if (!update) {
                /*Hinweis: wenn tatsächlich nur die Isin geändert wurde, dann ist hier der Return
                 * ok, da Isin nicht im Puffer enthalten sind.
                 */
                fuelleIsinBox();
                return;
            }
        }

        if (!update) {
            if (!cTermine)
                Platform.runLater(() -> flowFehler.getChildren().add(new Text("Keine Änderungen.")));
            return;
        }

        ParamS.param.paramBasis.mailConsultant = tfConsultant.getText().trim();
        ParamS.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom = tfBundesanzeiger.getText().trim();

        for (int i = 0; i < 5; i++) {
            EclIsin isinCb = new EclIsin(i + 1,
                    cbHauptIsin.get(i).getValue() == null ? "" : cbHauptIsin.get(i).getValue().isin);
            EclIsin isinParam = new EclIsin(i + 1, ParamS.param.paramBasis.isin[i]);

            if (!isinList.contains(isinParam) || !isinCb.equals(isinParam)) {
                ParamS.param.paramBasis.isin[i] = cbHauptIsin.get(i).getValue() == null ? ""
                        : cbHauptIsin.get(i).getValue().isin;
                if (ParamS.param.paramBasis.isin[i].isBlank())
                    cbHauptIsin.get(i).setValue(null);
            }
        }

        //      Speichern Emittent
        if (!emittent.equals(tmpEmittent)) {
            /*Hier raus, da sonst ggf. durch Open ParamS.eclEmittent wieder überschrieben wird!
            ParamS.eclEmittent = tmpEmittent;*/
            lDbBundle.openAllOhneParameterCheck();

            /*Hier rein - nach open und ggf. Puffer-Überschreibung*/

            ParamS.eclEmittent = tmpEmittent;

            //            Alt: Veränderung von anderem User nicht abgefangen
            //            if (lDbBundle.dbEmittenten.update(ParamS.eclEmittent) == 1)
            //                emittent = ParamS.eclEmittent;

            int rc = lDbBundle.dbEmittenten.update(ParamS.eclEmittent);
            lDbBundle.closeAll();
            if (rc == 1) {
                emittent = ParamS.eclEmittent;
            } else {
                CaBug.druckeLog("Emittent nicht speicherbar", logDrucken, 10);
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Emittent nicht speicherbar. Bitte Programm beenden und neue starten und EIngaben überprüfen");
            }

        }

        //      Kapital
        speichereKapital();

        CaBug.druckeLog("Nun noch Speichern Parameter", logDrucken, 10);

        //      Parameter
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.updateHVParam_all(ParamS.param);

        fuelleIsinBox();

        Platform.runLater(() -> flowFehler.getChildren().add(new Text("Änderungen wurden gespeichert.")));

        return;
    }

    /**
     * Check sonstige.
     *
     * @return the boolean
     */
    /*
     * Bei Änderung > true
     */
    private Boolean checkSonstige() {

        //      Änderung bei der HauptISIN
        for (int i = 0; i < 5; i++) {

            EclIsin isinCb = new EclIsin(i + 1,
                    cbHauptIsin.get(i).getValue() == null ? "" : cbHauptIsin.get(i).getValue().isin);
            EclIsin isinParam = new EclIsin(i + 1, ParamS.param.paramBasis.isin[i]);

            if (!isinCb.equals(isinParam))
                return true;
        }

        if (!ParamS.param.paramBasis.mailConsultant.equals(tfConsultant.getText().trim()))
            return true;
        if (!ParamS.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom.equals(tfBundesanzeiger.getText().trim()))
            return true;

        return false;
    }

    /**
     * Check termine.
     *
     * @return the boolean
     */
    private Boolean checkTermine() {

        if (hvTag == null) {
            if (!tfHVBeginn.getText().isBlank())
                return true;
        } else if (!hvTag.terminZeit.equals(tfHVBeginn.getText().trim())
                || !ParamS.eclEmittent.hvDatum.equals(dpHVDatum.getValue() == null ? ""
                        : dpHVDatum.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))))
            return true;

        if (hvEinlass == null) {
            if (!tfHVEinlass.getText().isBlank())
                return true;
        } else if (!hvEinlass.terminZeit.equals(tfHVEinlass.getText().trim()))
            return true;

        if (letzterAnmeldetag == null) {
            if (dpHVLetzterAnm.getValue() != null)
                return true;
        } else if (!letzterAnmeldetag.terminDatum
                .equals(dpHVLetzterAnm.getValue() == null ? "" : dpHVLetzterAnm.getValue().format(toLocalDate())))
            return true;

        return false;
    }

    /**
     * Check isin.
     *
     * @return the boolean
     */
    /*
     *  Prüfen ob eine Änderung an den ISINs gemacht wurde
     *  Einlesen der Oberfläche und vergleich mit der Bestandsliste 
     */
    private Boolean checkIsin() {

        isinInsertList = new ArrayList<>();
        isinDeleteList = new ArrayList<>(isinList);

        for (var entry : isinGattungMap.entrySet()) {
            final EclIsin isin = new EclIsin(entry.getValue(), entry.getKey().getText().trim());

            if (!isin.isin.isBlank()) {
                if (isinList.contains(isin))
                    isinDeleteList.remove(isin);
                else
                    isinInsertList.add(isin);
            }
        }
        return isinInsertList.size() > 0 || isinDeleteList.size() > 0;
    }

    /**
     * Check kapital.
     *
     * @return the string
     */
    /* 
     * Checken der Kapitalwerte. "", wenn alles ok. Ansonsten Fehlertext 
     * Prüfen ob im Tab Kapital eine Änderung vorliegt dann aenderungKapital = true
     */
    private String checkKapital() {

        aendungKapital = false;
        String fehlerText = "";

        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                if (!CaString.isNummern(tfKapitalStueckAktien[i].getText().trim()))
                    fehlerText = "Kapital Aktien ungültig!";
                else if (!String.valueOf(ParamS.param.paramBasis.getGrundkapitalStueck(i))
                        .equals(tfKapitalStueckAktien[i].getText().trim()))
                    aendungKapital = true;

                if (!CaString.isNummern(tfKapitalVermindertStueckAktien[i].getText().trim()))
                    fehlerText = "Kapital Vermindert Aktien ungültig!";
                else if (!String.valueOf(ParamS.param.paramBasis.getGrundkapitalVermindertStueck(i))
                        .equals(tfKapitalVermindertStueckAktien[i].getText().trim()))
                    aendungKapital = true;

                if (!CaString.isNummernKommaPunkt(tfKapitalWertEinerAktie[i].getText().trim()))
                    fehlerText = "Kapitalwert einer Aktien ungültig!";
                else if (!String.valueOf(ParamS.param.paramBasis.getWertEinerAktieString(i))
                        .equals(tfKapitalWertEinerAktie[i].getText().trim()))
                    aendungKapital = true;

                if (!CaString.isNummern(tfKapitalAnzahlNachkommastellenKapital[i].getText().trim()))
                    fehlerText = "Anzahl Nachkommastellen ungültig!";
                else if (!String.valueOf(ParamS.param.paramBasis.getAnzahlNachkommastellenKapital(i))
                        .equals(tfKapitalAnzahlNachkommastellenKapital[i].getText().trim()))
                    aendungKapital = true;

                if (ParamS.param.paramBasis.eintrittskarteNeuVergeben[i
                        - 1] != (tfEintrittskarteNeuVergeben[i].getText().equals("1") ? true : false))
                    aendungKapital = true;

                if (ParamS.param.paramBasis.zugangMoeglich[i - 1] != (tfZugangMoeglich[i].getText().equals("1") ? true
                        : false))
                    aendungKapital = true;

                if (!CaString.isNummern(tfEigeneAktien[i].getText().trim()))
                    fehlerText = "Eigene Aktien ungültig!";
                else if (!String.valueOf(ParamS.param.paramBasis.getGrundkapitalEigeneAktienStueck(i))
                        .equals(tfEigeneAktien[i].getText().trim()))
                    aendungKapital = true;
            }
        }
        return fehlerText;
    }

    /**
     * Speichere kapital.
     */
    private void speichereKapital() {
        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                ParamS.param.paramBasis.grundkapitalStueck[i - 1] = CaString
                        .longParseLong(CaString.eingabeZuIntern(tfKapitalStueckAktien[i].getText().trim()));
                ParamS.param.paramBasis.grundkapitalVermindertStueck[i - 1] = CaString
                        .longParseLong(CaString.eingabeZuIntern(tfKapitalVermindertStueckAktien[i].getText().trim()));
                ParamS.param.paramBasis.wertEinerAktie[i - 1] = CaString
                        .doubleParseDouble(CaString.eingabeZuIntern(tfKapitalWertEinerAktie[i].getText().trim()));
                ParamS.param.paramBasis.anzahlNachkommastellenKapital[i - 1] = CaString.integerParseInt(
                        CaString.eingabeZuIntern(tfKapitalAnzahlNachkommastellenKapital[i].getText().trim()));
                ParamS.param.paramBasis.eintrittskarteNeuVergeben[i - 1] = tfEintrittskarteNeuVergeben[i].getText()
                        .equals("1");
                ParamS.param.paramBasis.zugangMoeglich[i - 1] = tfZugangMoeglich[i].getText().equals("1");
                ParamS.param.paramBasis.grundkapitalEigeneAktienStueck[i - 1] = CaString
                        .longParseLong(tfEigeneAktien[i].getText().trim());
            }
        }
    }

    /**
     * On pfad.
     *
     * @param event the event
     */
    @FXML
    void onPfad(ActionEvent event) {

        final String verzBasis = ParamS.paramGeraet.lwPfadKundenordnerBasis;
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(new File(verzBasis).exists() ? verzBasis : "C:\\"));

        File f = dc.showDialog(eigeneStage);

        flowFehler.getChildren().clear();

        if (f != null && f.getPath().contains(verzBasis)) {
            tfPfad.setText(f.getPath().contains(verzBasis) ? f.getPath().substring(verzBasis.length()) : f.getPath());
        } else {
            flowFehler.getChildren().add(new Text("Verzeichnus muss unter " + verzBasis + " sein."));
        }

    }

    /**
     * On beenden.
     *
     * @param event the event
     */
    /*
     * Abfragen ob eine Änderung gemacht wurde und evtl. Speichern
     */
    @FXML
    void onBeenden(ActionEvent event) {

        String f = checkKapital();

        if (!f.isEmpty()) {
            fehlerMeldung(f);
            return;
        }

        String hauptIsin = "";
        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                if (cbHauptIsin.get(i - 1).getValue() == null)
                    hauptIsin = "leer";
            }
        }

        if (!hauptIsin.isBlank()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Warnung");
            alert.setHeaderText("Mindestens eine HauptISIN wurde nicht ausgewählt");
            alert.setContentText("Ist das OK?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                hauptIsin = "";
            }
        }

        EclEmittenten tmpEmittent = getValues(new EclEmittenten(emittent));

        if (f.isBlank() && (aendungKapital || checkIsin() || !emittent.equals(tmpEmittent) || checkSonstige()
                || checkTermine())) {

            Alert confirmation = new Alert(AlertType.CONFIRMATION, "Sollen Ihre Änderungen gespeichert werden?",
                    new ButtonType("Speichern", ButtonData.YES), new ButtonType("Nicht Speichern", ButtonData.NO),
                    new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get().getButtonData() == ButtonData.YES)
                onSpeichern(new ActionEvent());
            else if (result.get().getButtonData() == ButtonData.CANCEL_CLOSE)
                return;

        }
        if (hauptIsin.isBlank())
            eigeneStage.hide();
    }

    /**
     * Gets the values.
     *
     * @param emittent the emittent
     * @return the values
     */
    private EclEmittenten getValues(EclEmittenten emittent) {

        //      Hauptversammlung
        emittent.hvArtSchluessel = KonstHVArtSchluessel.liefereIntFuerDatenbank(cbArt.getValue());
        emittent.veranstaltungGebäude = tfHVLocation.getText().trim();
        emittent.veranstaltungStrasse = tfHVStrasse.getText().trim();
        emittent.veranstaltungPostleitzahl = tfHVPlz.getText().trim();
        emittent.hvOrt = tfHVOrt.getText().trim();
        emittent.veranstaltungStaatId = cbHVLand.getValue() == null ? 0 : cbHVLand.getValue().getId();
        emittent.hvDatum = dpHVDatum.getValue() == null ? ""
                : dpHVDatum.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        int hBerechtigung = 0;
        if (cbBerConsultant.isSelected()) {
            hBerechtigung = (hBerechtigung | 1);
        }
        if (cbBerAnmeldeErfassung.isSelected()) {
            hBerechtigung = (hBerechtigung | 2);
        }
        if (cbBerAnmeldeMaster.isSelected()) {
            hBerechtigung = (hBerechtigung | 4);
        }
        if (cbBerAdministrator.isSelected()) {
            hBerechtigung = (hBerechtigung | 8);
        }
        if (cbBerHotline.isSelected()) {
            hBerechtigung = (hBerechtigung | 16);
        }
        emittent.inAuswahl = hBerechtigung;

        //      Gesellschaft
        emittent.bezeichnungKurz = tfBezeichnungKurz.getText().trim();
        emittent.bezeichnungLang = tfBezeichnungLang.getText().trim();
        emittent.bezeichnungsArtApp = cbApp.getValue().equals(KURZ) ? 1 : 2;
        emittent.bezeichnungsArtFormulare = cbFormular.getValue().equals(KURZ) ? 1 : 2;
        emittent.bezeichnungsArtPortal = cbPortal.getValue().equals(KURZ) ? 1 : 2;
        emittent.abteilung = tfAbteilung.getText().trim();
        emittent.strasseGesellschaft = tfStrasse.getText().trim();
        emittent.postleitzahl = tfPlz.getText().trim();
        emittent.ort = tfOrt.getText().trim();
        emittent.staatId = cbLand.getValue() == null ? 0 : cbLand.getValue().getId();
        emittent.pfadErgKundenOrdner = tfPfad.getText().trim();
        emittent.telefon = tfTelefon.getText().trim();
        emittent.fax = tfFax.getText().trim();
        emittent.email = tfEmail.getText().trim();

        return emittent;
    }

    /**
     * ***** Interne Funktionen zum Zwischenspeichern / Wiederherstellen der
     * Kapitalwerte ***.
     */
    private long[] grundkapitalStueck = { 0, 0, 0, 0, 0 };

    /** The grundkapital vermindert stueck. */
    private long[] grundkapitalVermindertStueck = { 0, 0, 0, 0, 0 };

    /** The wert einer aktie. */
    private double[] wertEinerAktie = { 0.0, 0.0, 0.0, 0.0, 0.0 };

    /** The anzahl nachkommastellen kapital. */
    private int[] anzahlNachkommastellenKapital = { 2, 2, 2, 2, 2 };

    private long[] eigeneAktien = { 0, 0, 0, 0, 0 };

    /** Speichert ParamS.param.paramBasis in den lokalen Zwischenpuffer */
    private void paramToPuffer() {
        for (int i = 0; i < 5; i++) {
            grundkapitalStueck[i] = ParamS.param.paramBasis.grundkapitalStueck[i];
            grundkapitalVermindertStueck[i] = ParamS.param.paramBasis.grundkapitalVermindertStueck[i];
            wertEinerAktie[i] = ParamS.param.paramBasis.wertEinerAktie[i];
            anzahlNachkommastellenKapital[i] = ParamS.param.paramBasis.anzahlNachkommastellenKapital[i];
            eigeneAktien[i] = ParamS.param.paramBasis.grundkapitalEigeneAktienStueck[i];
        }
    }

    /** Speichert den lokalen Zwischenpuffer in ParamS.param.paramBasis */
    private void pufferToParam() {
        for (int i = 0; i < 5; i++) {
            ParamS.param.paramBasis.grundkapitalStueck[i] = grundkapitalStueck[i];
            ParamS.param.paramBasis.grundkapitalVermindertStueck[i] = grundkapitalVermindertStueck[i];
            ParamS.param.paramBasis.wertEinerAktie[i] = wertEinerAktie[i];
            ParamS.param.paramBasis.anzahlNachkommastellenKapital[i] = anzahlNachkommastellenKapital[i];
            ParamS.param.paramBasis.grundkapitalEigeneAktienStueck[i] = eigeneAktien[i];
        }
    }

    /**
     * ****************** Aktionen auf Oberfläche ************************.
     */

    private void fuelleIsinBox() {

        lDbBundle.openAll();
        lDbBundle.openWeitere();

        lDbBundle.dbIsin.readAll();
        isinList = lDbBundle.dbIsin.ergebnis();

        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                final int x = i;
                cbHauptIsin.get(i - 1).getItems()
                        .setAll(isinList.stream().filter(e -> e.gaettungId == x).collect(Collectors.toList()));
                fuelleKapital();
            }
        }

        lDbBundle.closeAll();

        hBoxScrollPane.getChildren().clear();
        isinGattungMap.clear();

        for (int i = 1; i <= ParamS.param.paramBasis.anzahlGattungen; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {

                final int x = i;

                List<EclIsin> tmpList = isinList.stream().filter(e -> e.gaettungId == x).collect(Collectors.toList());

                VBox isinBox = new VBox();
                isinBox.setMinWidth(100);
                HBox.setHgrow(isinBox, Priority.ALWAYS);
                isinBox.setSpacing(15);
                Label lblGattung = new Label("Gattung " + x);
                lblGattung.setMaxWidth(Double.MAX_VALUE);
                lblGattung.setAlignment(Pos.CENTER);
                lblGattung.setFont(Font.font("System", FontWeight.BOLD, 12.0));
                isinBox.getChildren().add(lblGattung);

                Button btnNew = new Button("Weitere ISIN", new FontIcon(FontAwesomeSolid.PLUS));
                btnNew.setMaxWidth(Double.MAX_VALUE);
                btnNew.setOnAction(e -> isinBox.getChildren().add(isinBox.getChildren().size() - 1,
                        createHBox(x, "", false, isinBox)));

                for (EclIsin isin : tmpList)
                    isinBox.getChildren().add(createHBox(x, isin.isin, false, isinBox));

                isinBox.getChildren().add(btnNew);

                hBoxScrollPane.getChildren().add(isinBox);
            }
        }
    }

    /**
     * Creates the H box.
     *
     * @param gattung the gattung
     * @param isin    the isin
     * @param main    the main
     * @param vBox    the v box
     * @return the h box
     */
    private HBox createHBox(int gattung, String isin, Boolean main, VBox vBox) {

        final EclIsin eclIsin = new EclIsin(gattung, isin);

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        TextField tf = new TextField(isin);
        tf.setEditable(!blReg.isinList.contains(eclIsin));
        tfLengthValidator(tf, 12, "ISIN");
        HBox.setHgrow(tf, Priority.ALWAYS);
        isinGattungMap.put(tf, gattung);

        Button btn = new Button("", new FontIcon(FontAwesomeRegular.TRASH_ALT));
        btn.setDisable(blReg.isinList.contains(eclIsin));
        btn.setOnAction(e -> {
            vBox.getChildren().remove(box);
            isinGattungMap.remove(tf);
        });

        if (ParamS.param.paramBasis.getIsin(gattung).equals(isin)
                && !ParamS.param.paramBasis.getIsin(gattung).isBlank()) {
            FontIcon icon = new FontIcon(FontAwesomeSolid.CHECK);
            icon.setIconColor(Paint.valueOf("1676F3"));
            box.getChildren().addAll(btn, tf, icon);
        } else {
            box.getChildren().addAll(btn, tf);
        }
        return box;
    }

    /**
     * Evaluate date.
     *
     * @param dp the dp
     */
    private void evaluateDate(DatePicker dp) {

        //      Checkt ob Änderungen vorhanden und mit Feld kompatibel ohne den Listener werden Änderungen mit mit Enter oder per Maus gesetzt.
        dp.focusedProperty().addListener((o, oV, nV) -> {
            if (!nV) {
                if (dp.getEditor().getText().matches("(3[01]|[12][0-9]|0[1-9])\\.(1[012]|0[1-9])\\.(\\d{4})"))
                    dp.setValue(LocalDate.parse(dp.getEditor().getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                else
                    dp.getEditor().setText(dp.getConverter().toString(dp.getValue()));
            }
        });

        dp.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("[0-9\\.]*") || newText.length() > 11)
                dp.getEditor().setText(oldText);
            if (newText.isBlank())
                dp.setValue(null);
        });
    }

    /**
     * Evaluate time.
     *
     * @param tf the tf
     */
    //  Eingabe check z.B. 1000 > 10:00
    private void evaluateTime(TextField tf) {
        tf.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.matches("\\d{4}"))
                tf.setText(newText.substring(0, 2) + ":" + newText.substring(2, 4) + ":" + "00");
            if (!newText.matches("[0-9\\:]*") || newText.length() > 8)
                tf.setText(oldText);
        });
    }

    /**
     * Fuelle kapital.
     */
    private void fuelleKapital() {

        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                tfKapitalStueckAktien[i].setText(Long.toString(ParamS.param.paramBasis.getGrundkapitalStueck(i)));
                tfKapitalStueckAktienEuro[i].setText(ParamS.param.paramBasis.getGrundkapitalStueckEuroString(i));
                tfKapitalVermindertStueckAktien[i]
                        .setText(Long.toString(ParamS.param.paramBasis.getGrundkapitalVermindertStueck(i)));
                tfKapitalVermindertStueckAktienEuro[i]
                        .setText(ParamS.param.paramBasis.getGrundkapitalVermindertEuroString(i));
                tfKapitalWertEinerAktie[i].setText(ParamS.param.paramBasis.getWertEinerAktieString(i));
                cbHauptIsin.get(i - 1).setValue(ParamS.param.paramBasis.getIsin(i).isBlank() ? null
                        : new EclIsin(i, ParamS.param.paramBasis.getIsin(i)));
                tfKapitalAnzahlNachkommastellenKapital[i]
                        .setText(Integer.toString(ParamS.param.paramBasis.getAnzahlNachkommastellenKapital(i)));
                tfEigeneAktien[i].setText(Long.toString(ParamS.param.paramBasis.getGrundkapitalEigeneAktienStueck(i)));
            }
        }
    }

    /**
     * Tf length validator.
     *
     * @param tf   the tf
     * @param max  the max
     * @param feld the feld
     */
    /*
     * Funktion zur Validierung der TextFeld laengen
     */
    private void tfLengthValidator(TextField tf, int max, String feld) {
        tf.textProperty().addListener((o, oV, nV) -> {
            if (nV.length() > max) {
                tf.setText(oV);

                Text f = new Text(feld);
                f.setStyle("-fx-font-weight: bold");

                flowFehler.getChildren().addAll(new Text("Das Feld: "), f,
                        new Text(" darf maximal " + max + " Zeichen lang sein!"));

            } else {
                flowFehler.getChildren().clear();
            }
        });
    }

    /**
     * Clicked btn kapital refresh.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnKapitalRefresh(ActionEvent event) {
        String lFehlertext = checkKapital();
        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return;
        }

        paramToPuffer();
        speichereKapital();
        fuelleKapital();
        pufferToParam();
    }

    /**
     * Erstelle termin.
     *
     * @param identTermin the ident termin
     * @param datum       the datum
     * @param zeit        the zeit
     * @return the ecl termine
     */
    private EclTermine erstelleTermin(int identTermin, String datum, String zeit) {
        return new EclTermine(ParamS.param.mandant, identTermin, 0, 0, 0, 1, datum, zeit, "", "",
                KonstTermine.getText(identTermin));
    }

    /**
     * To local date.
     *
     * @return the date time formatter
     */
    private DateTimeFormatter toLocalDate() {
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

        emittent = ParamS.eclEmittent;
    }
}
