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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterImport;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Class CtrlMeldedaten.
 */
public class CtrlMeldedaten extends CtrlRoot {

    /** The root pane. */
    @FXML
    private AnchorPane rootPane;

    /** The grid meldung. */
    @FXML
    private GridPane gridMeldung;

    /** The lbl manuelle nr. */
    @FXML
    private Label lblManuelleNr;

    /** The lbl anr. */
    @FXML
    private Label lblAnr;

    /** The tf anr. */
    @FXML
    private TextField tfAnr;

    /** The check manuelle nr. */
    @FXML
    private CheckBox checkManuelleNr;

    /** The cb anrede. */
    @FXML
    private ComboBox<String> cbAnrede;

    /** The lbl titel. */
    @FXML
    private Label lblTitel;

    /** The tf titel. */
    @FXML
    private TextField tfTitel;

    /** The lbl nachname. */
    @FXML
    private Label lblNachname;

    /** The tf nachname. */
    @FXML
    private TextField tfNachname;

    /** The lbl vorname. */
    @FXML
    private Label lblVorname;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The lbl name 3. */
    @FXML
    private Label lblName3;

    /** The tf name 3. */
    @FXML
    private TextField tfName3;

    /** The tf adresszusatz. */
    @FXML
    private TextField tfAdresszusatz;

    /** The tf strasse. */
    @FXML
    private TextField tfStrasse;

    /** The tf plz. */
    @FXML
    private TextField tfPlz;

    /** The tf postfach. */
    @FXML
    private TextField tfPostfach;

    /** The tf plz postfach. */
    @FXML
    private TextField tfPlzPostfach;

    /** The lbl ort. */
    @FXML
    private Label lblOrt;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The cb land. */
    @FXML
    private ComboBox<EclStaaten> cbLand;

    /** The lbl aktien. */
    @FXML
    private Label lblAktien;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The lbl gattung. */
    @FXML
    private Label lblGattung;

    /** The cb gattung. */
    @FXML
    private ComboBox<Integer> cbGattung;

    /** The cb isin. */
    @FXML
    private ComboBox<EclIsin> cbIsin;

    /** The lbl besitz. */
    @FXML
    private Label lblBesitz;

    /** The cb besitz. */
    @FXML
    private ComboBox<String> cbBesitz;

    /** The tf gruppe. */
    @FXML
    private TextField tfGruppe;

    /** The check versandadresse. */
    @FXML
    private CheckBox checkVersandadresse;

    /** The check jur person. */
    @FXML
    private CheckBox checkJurPerson;

    /** The check personengemeinschaft. */
    @FXML
    private CheckBox checkPersonengemeinschaft;

    /** The accordion. */
    @FXML
    private Accordion accordion;

    /** The check adresse. */
    @FXML
    private CheckBox checkAdresse;

    /** The tf adresse 1. */
    @FXML
    private TextField tfAdresse1;

    /** The tf adresse 2. */
    @FXML
    private TextField tfAdresse2;

    /** The tf adresse 3. */
    @FXML
    private TextField tfAdresse3;

    /** The tf adresse 4. */
    @FXML
    private TextField tfAdresse4;

    /** The tf adresse 5. */
    @FXML
    private TextField tfAdresse5;

    /** The tf adresse 6. */
    @FXML
    private TextField tfAdresse6;

    /** The grid versand. */
    @FXML
    private GridPane gridVersand;

    /** The cb anrede versand. */
    @FXML
    private ComboBox<String> cbAnredeVersand;

    /** The lbl titel versand. */
    @FXML
    private Label lblTitelVersand;

    /** The tf titel versand. */
    @FXML
    private TextField tfTitelVersand;

    /** The lbl nachname versand. */
    @FXML
    private Label lblNachnameVersand;

    /** The tf nachname versand. */
    @FXML
    private TextField tfNachnameVersand;

    /** The lbl vorname versand. */
    @FXML
    private Label lblVornameVersand;

    /** The tf vorname versand. */
    @FXML
    private TextField tfVornameVersand;

    /** The lbl name 3 versand. */
    @FXML
    private Label lblName3Versand;

    /** The tf name 3 versand. */
    @FXML
    private TextField tfName3Versand;

    /** The tf adresszusatz versand. */
    @FXML
    private TextField tfAdresszusatzVersand;

    /** The tf strasse versand. */
    @FXML
    private TextField tfStrasseVersand;

    /** The tf plz versand. */
    @FXML
    private TextField tfPlzVersand;

    /** The tf postfach versand. */
    @FXML
    private TextField tfPostfachVersand;

    /** The tf plz postfach versand. */
    @FXML
    private TextField tfPlzPostfachVersand;

    /** The lbl ort versand. */
    @FXML
    private Label lblOrtVersand;

    /** The tf ort versand. */
    @FXML
    private TextField tfOrtVersand;

    /** The cb land versand. */
    @FXML
    private ComboBox<EclStaaten> cbLandVersand;

    /** The box historie. */
    @FXML
    private HBox boxHistorie;

    /** The lbl info. */
    @FXML
    private Label lblInfo;

    /** The btn copy. */
    @FXML
    private Button btnCopy;

    /** The eintrag. */
    public EclAktienregister eintrag = null;

    /** The geandert. */
    public Boolean geandert = false;

    /** The aendern. */
    private Boolean aendern = true;

    /** The ek check. */
    //  Check ob in den EKs eine Lücke ist
    private Boolean ekCheck = false;

    /** The ek from. */
    //  EKs von
    private String ekFrom = "";

    /** The ek to. */
    //  EKs bis
    private String ekTo = "";

    /** The eks. */
    //  EKs einzeln
    private String eks = "";

    /** The isin. */
    private String isin = "";

    /** The copy string. */
    private String copyString = "";

    /** The besitzarten. */
    private final Set<String> besitzarten = new LinkedHashSet<>(List.of("", "E", "F", "V", "T"));

    /** The anreden. */
    private final Set<String> anreden = new LinkedHashSet<>(List.of("", "Herr", "Firma", "Frau", "Herr und Frau"));

    /** The gattungen. */
    private final Set<Integer> gattungen = new LinkedHashSet<>();

    /** The staaten. */
    private List<EclStaaten> staaten = null;

    /** The isin list. */
    private List<EclIsin> isinList = null;

    /** The check meldung. */
    private Map<TextField, Label> checkMeldung = new HashMap<>();

    /** The check versand. */
    private Map<TextField, Label> checkVersand = new HashMap<>();

    /** The historie list. */
    private List<EclAktienregister> historieList = new LinkedList<>();

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        for (int i = 0; i < ParamS.param.paramBasis.gattungAktiv.length; i++)
            if (ParamS.param.paramBasis.gattungAktiv[i])
                gattungen.add(i + 1);

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbStaaten.readAll(1);
        lDbBundle.dbIsin.readAll();

        staaten = lDbBundle.dbStaaten.ergebnis();
        isinList = lDbBundle.dbIsin.ergebnis();

        lDbBundle.closeAll();

        rootPane.widthProperty().addListener((o, oV, nV) -> {
            if (nV.doubleValue() > 800.0) {
                gridMeldung.prefWidthProperty().bind(rootPane.widthProperty().divide(3).multiply(2).subtract(30));
                accordion.prefWidthProperty().bind(rootPane.widthProperty().divide(3).subtract(15));
                accordion.setVisible(true);
            } else {
                gridMeldung.prefWidthProperty().bind(rootPane.widthProperty().subtract(30));
                accordion.setVisible(false);
            }
        });

        checkManuelleNr.selectedProperty().addListener((o, oV, nV) -> {
            if (cbGattung.getValue() != null)
                setLabelManuelleNr(cbGattung.getValue(), nV);
            tfAnr.setEditable(nV);
            tfAnr.clear();
            tfAnr.setPromptText(nV ? "Bitte Nummer eingeben" : "Wird automatisch vergeben");
            if (nV)
                checkMeldung.put(tfAnr, lblAnr);
            else
                checkMeldung.remove(tfAnr);
        });

        checkManuelleNr.setSelected(ParamS.param.paramBasis.namensaktienAktiv);
        checkManuelleNr.setDisable(ParamS.param.paramBasis.namensaktienAktiv);
        lblManuelleNr.setText("");

        tfAnr.setEditable(!aendern);
        cbAnrede.getItems().setAll(anreden);
        ObjectActions.filterComboBox(cbAnrede, null);
        checkMeldung.put(tfNachname, lblNachname);
        checkMeldung.put(tfOrt, lblOrt);
        configureStaatBox(cbLand, staaten);
        checkMeldung.put(tfAktien, lblAktien);

        cbGattung.getItems().setAll(gattungen);
        cbGattung.setDisable(aendern);

        cbIsin.getItems().setAll(aendern
                ? isinList.stream().filter(e -> e.gaettungId == eintrag.getGattungId()).collect(Collectors.toList())
                : isinList);
        cbIsin.setConverter(ComboBoxZusatz.convertIsin(cbIsin));

        if (!aendern) {
            cbIsin.getSelectionModel().selectedItemProperty()
                    .addListener((o, oV, nV) -> cbGattung.setValue(nV.gaettungId));
            cbGattung.getSelectionModel().selectedItemProperty().addListener((o, oV, nV) -> {
                cbIsin.setValue(ParamS.param.paramBasis.getIsin(nV).isBlank() ? null
                        : new EclIsin(nV, ParamS.param.paramBasis.getIsin(nV)));
                setLabelManuelleNr(nV, checkManuelleNr.isSelected());
            });
        }

        cbBesitz.getItems().setAll(besitzarten);
        ObjectActions.filterComboBox(cbBesitz, null);
        cbAnredeVersand.getItems().setAll(anreden);
        ObjectActions.filterComboBox(cbAnredeVersand, null);

        checkVersand.put(tfNachnameVersand, lblNachnameVersand);
        checkVersand.put(tfOrtVersand, lblOrtVersand);
        configureStaatBox(cbLandVersand, staaten);

        accordion.setExpandedPane(accordion.getPanes().get(0));

        checkJurPerson.selectedProperty().addListener((o, oV, nV) -> {
            GridPane.setColumnSpan(cbAnrede, nV ? 5 : 1);
            lblTitel.setVisible(!nV);
            tfTitel.setVisible(!nV);
            lblNachname.setText(nV ? "Name 1 *" : "Nachname *");
            lblVorname.setText(nV ? "Name 2" : "Vorname");
            lblName3.setVisible(nV);
            tfName3.setVisible(nV);

            GridPane.setColumnSpan(cbAnredeVersand, nV ? 5 : 1);
            lblTitelVersand.setVisible(!nV);
            tfTitelVersand.setVisible(!nV);
            lblNachnameVersand.setText(nV ? "Name 1 *" : "Nachname *");
            lblVornameVersand.setText(nV ? "Name 2" : "Vorname");
            lblName3Versand.setVisible(nV);
            tfName3Versand.setVisible(nV);
        });

        setValues(eintrag);

        checkVersandadresse.selectedProperty().addListener((o, oV, nV) -> {
            if (nV)
                accordion.setExpandedPane(accordion.getPanes().get(1));
            checkVersand(1);
        });

        eigeneStage.setOnCloseRequest(e -> onBeenden(new ActionEvent()));

        btnCopy.setGraphic(new FontIcon(FontAwesomeSolid.CLIPBOARD));

        setValidator();

        eigeneStage.setOnShown(e -> {
            eigeneStage.getScene().setOnKeyPressed(x -> {
                final KeyCombination kb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);

                if (kb.match(x))
                    onSpeichern(new ActionEvent());
            });
        });
    }

    /**
     * Configure staat box.
     *
     * @param box     the box
     * @param staaten the staaten
     */
    private static void configureStaatBox(ComboBox<EclStaaten> box, List<EclStaaten> staaten) {

        ObservableList<EclStaaten> list = FXCollections.observableArrayList(staaten);
        FilteredList<EclStaaten> fList = new FilteredList<>(list, p -> true);
        box.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                box.setValue(null);
                box.getEditor().clear();
            }
        });

        box.getEditor().setOnKeyPressed(e -> {
            if (!e.getCode().equals(KeyCode.TAB))
                box.show();
        });

        box.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = box.getEditor();
            final EclStaaten selected = box.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                if (selected == null || !selected.getNameDE().equals(editor.getText())) {
                    fList.setPredicate(item -> {
                        return item.getNameDE().toLowerCase().startsWith(newValue.toLowerCase()) ? true : false;
                    });
                }
            });
        });

        box.setItems(fList);
        box.setConverter(ComboBoxZusatz.convertStaaten(box));
    }

    /**
     * Check meldung.
     *
     * @param f the f
     * @return true, if successful
     */
    private boolean checkMeldung(int f) {
        for (var entry : checkMeldung.entrySet()) {
            if (f == 1) {
                setLabelColor(entry.getValue(), entry.getKey().getText().isBlank());
                entry.getKey().textProperty().addListener((o, oV, nV) -> setLabelColor(entry.getValue(), nV.isBlank()));
            } else if (f == 2) {
                if (entry.getKey().getText().isBlank()) {
                    Platform.runLater(() -> entry.getKey().requestFocus());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check versand.
     *
     * @param f the f
     * @return true, if successful
     */
    private boolean checkVersand(int f) {
        for (var entry : checkVersand.entrySet()) {
            if (checkVersandadresse.isSelected()) {
                if (f == 1) {
                    setLabelColor(entry.getValue(), entry.getKey().getText().isBlank());
                    entry.getKey().textProperty().addListener((o, oV, nV) -> {
                        setLabelColor(entry.getValue(), nV.isBlank());
                        if (tfNachnameVersand.getText().isBlank() || tfOrtVersand.getText().isBlank()) {
                            FontIcon excl = new FontIcon(FontAwesomeSolid.EXCLAMATION_CIRCLE);
                            excl.setFill(Paint.valueOf("#991200"));
                            accordion.getPanes().get(1).setGraphic(excl);
                        } else {
                            FontIcon conf = new FontIcon(FontAwesomeSolid.CHECK_CIRCLE);
                            conf.setFill(Paint.valueOf("#388f10"));
                            accordion.getPanes().get(1).setGraphic(conf);
                        }
                    });
                } else if (f == 2) {
                    if (entry.getKey().getText().isBlank()) {
                        Platform.runLater(() -> entry.getKey().requestFocus());
                        return false;
                    }
                }
            } else {
                setLabelColor(entry.getValue(), false);
                accordion.getPanes().get(1).setGraphic(null);
            }
        }
        return true;
    }

    /**
     * Sets the label manuelle nr.
     *
     * @param gattung the gattung
     * @param check   the check
     */
    private void setLabelManuelleNr(int gattung, boolean check) {
        lblManuelleNr.setText(check ? "Manueller Nummernkreis für Gattung " + gattung + ": "
                + ParamS.param.paramNummernkreise.vonSubEintrittskartennummer[gattung][1] + " - "
                + ParamS.param.paramNummernkreise.bisSubEintrittskartennummer[gattung][1] : "");

        if (check) {
            tfAnr.textProperty().addListener((o, oV, nV) -> {
                if (nV.matches("\\d+") && nV.isBlank() && (Integer.valueOf(
                        tfAnr.getText()) > ParamS.param.paramNummernkreise.vonSubEintrittskartennummer[gattung][1]
                        && Integer.valueOf(tfAnr
                                .getText()) > ParamS.param.paramNummernkreise.bisSubEintrittskartennummer[gattung][1]))
                    tfAnr.setText(oV);
            });
        }
    }

    /**
     * Sets the label color.
     *
     * @param lbl   the lbl
     * @param blank the blank
     */
    private void setLabelColor(Label lbl, Boolean blank) {
        lbl.setStyle("-fx-text-fill: " + (blank ? "#991200" : "black"));
    }

    /**
     * Sets the validator.
     */
    private void setValidator() {
        ctrlValidator(null, tfAnr, 10, "Aktionärsnummer");
        ctrlValidator(null, tfTitel, 80, "Titel");
        ctrlValidator(null, tfTitelVersand, 80, "Titel - Versand");
        ctrlValidator(null, tfNachname, 80, "Nachname / Name 1");
        ctrlValidator(null, tfNachnameVersand, 80, "Nachname / Name 1 - Versand");
        ctrlValidator(null, tfVorname, 80, "Vorname / Name2 ");
        ctrlValidator(null, tfVornameVersand, 80, "Vorname / Name2 - Versand");
        ctrlValidator(null, tfName3, 200, "Name 3");
        ctrlValidator(null, tfName3Versand, 200, "Name 3 - Versand");
        ctrlValidator(null, tfAdresszusatz, 80, "Adresszusatz");
        ctrlValidator(null, tfAdresszusatzVersand, 80, "Adresszusatz - Versand");
        ctrlValidator(null, tfStrasse, 80, "Strasse");
        ctrlValidator(null, tfStrasseVersand, 80, "Strasse - Versand");
        ctrlValidator(null, tfPlz, 20, "PLZ");
        ctrlValidator(null, tfPlzVersand, 20, "PLZ - Versand");
        ctrlValidator(null, tfPostfach, 20, "Postfach");
        ctrlValidator(null, tfPostfachVersand, 20, "Postfach - Versand");
        ctrlValidator(null, tfPlzPostfach, 20, "PLZ Postfach");
        ctrlValidator(null, tfPlzPostfachVersand, 20, "PLZ Postfach - Versand");
        ctrlValidator(null, tfOrt, 80, "Ort");
        ctrlValidator(null, tfOrtVersand, 80, "Ort - Versand");
        ctrlValidator(cbLand, null, 0, "");
        ctrlValidator(null, tfAktien, 20, "Aktien");
    }

    /**
     * Ctrl validator.
     *
     * @param cb   the cb
     * @param tf   the tf
     * @param max  the max
     * @param feld the feld
     */
    private void ctrlValidator(ComboBox<?> cb, TextField tf, int max, String feld) {

        if (tf != null) {
            tf.textProperty().addListener((o, oV, nV) -> {

                if (feld.equals("Aktien") && !tf.getText().trim().matches("\\d*")) {
                    lblInfo.setText("Im Feld " + feld + " dürfen nur Zahlen eingetragen werden.");
                    setLabelColor(lblAktien, true);
                } else if (nV.length() > max) {
                    tf.setText(oV);
                    lblInfo.setText("Das Feld: " + feld + " darf maximal " + max + " Zeichen lang sein!");
                } else {
                    tf.setText(nV.replaceAll("�", ""));
                    lblInfo.setText("");
                }
            });
        }

        final Control c = tf != null ? tf : cb;

        c.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.TAB) || e.getCode().equals(KeyCode.ENTER))
                prepAdresse();
        });
    }

    /**
     * Prep adresse.
     */
    private void prepAdresse() {

        if (!checkAdresse.isSelected()) {

            BlAktienregisterImport blArImport = new BlAktienregisterImport(staaten);

            EclAktienregister tmpEintrag = getValues(eintrag);

            blArImport.helAdresszeilenAufbereiten(tmpEintrag, false);

            tfAdresse1.setText(tmpEintrag.getAdresszeile1());
            tfAdresse2.setText(tmpEintrag.getAdresszeile2());
            tfAdresse3.setText(tmpEintrag.getAdresszeile3());
            tfAdresse4.setText(tmpEintrag.getAdresszeile4());
            tfAdresse5.setText(tmpEintrag.getAdresszeile5());
            tfAdresse6.setText(tmpEintrag.getAdresszeile6());

        }
    }

    @FXML
    private void onMailversand(ActionEvent event) {

        if (ParamS.param.paramPortal.mailEingabeServiceline == 0) {
            CustomAlert.informationAlert("Parameter nicht gesetzt!",
                    "Parameter\nMaileingabe durch Serviceline = " + ParamS.param.paramPortal.mailEingabeServiceline,
                    "Kann unter\n> Parameter Portal App\n> Registr.Einstell.\n> Maileingabe durch Serviceline\ngeändert werden.");
            return;
        }

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlMailingdaten ctrl = new CtrlMailingdaten();
        ctrl.init(neuerDialog, eintrag.aktienregisterIdent);

        CaController caController = new CaController();
        caController.open(neuerDialog, ctrl, "/de/meetingapps/meetingclient/meetingBestand/Mailingdaten.fxml",
                ctrl.width, ctrl.height, "E-Mailversand", true);
    }

    /**
     * On beenden.
     *
     * @param event the event
     */
    @FXML
    private void onBeenden(ActionEvent event) {

        if (aendern && checkMeldung(2) && checkVersand(2)) {

            prepAdresse();
            EclAktienregister tmpEintrag = getValues(eintrag);

            if (!eintrag.equals(tmpEintrag)) {

                Alert confirmation = new Alert(AlertType.CONFIRMATION, "Sollen Ihre Änderungen gespeichert werden?",
                        new ButtonType("Speichern", ButtonData.YES), new ButtonType("Nicht Speichern", ButtonData.NO),
                        new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));

                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.get().getButtonData() == ButtonData.YES)
                    onSpeichern(new ActionEvent());
                else if (result.get().getButtonData() == ButtonData.CANCEL_CLOSE)
                    return;
            }
        }
        eigeneStage.hide();
    }

    /**
     * On bestand.
     *
     * @param event the event
     */
    @FXML
    private void onBestand(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlAktienregister controllerFenster = new CtrlAktienregister();
        controllerFenster.init(newStage, 1);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/Aktienregister.fxml", controllerFenster.width,
                controllerFenster.height, "Aktienregister", true);

        if (controllerFenster.eintrag != null) {
            eintrag = controllerFenster.eintrag;
            aendern = false;
            setValues(eintrag);
            Platform.runLater(() -> tfAktien.requestFocus());
        }
    }

    /**
     * On speichern.
     *
     * @param event the event
     */
    @FXML
    private void onSpeichern(ActionEvent event) {

        if (!checkMeldung(2))
            return;

        if (!checkVersand(2))
            return;

        // Namensaktien nicht neu anlegen
        if (!aendern && ParamS.param.paramBasis.namensaktienAktiv && !ParamS.param.paramBasis.inhaberaktienAktiv) {
            CustomAlert.informationAlert("Namensaktien", "Anlegen bei Namensaktien nicht verfügbar!", "");
            return;
        }

        prepAdresse();

        EclAktienregister tmpEintrag = getValues(eintrag);

        if (aendern) {
            if (!eintrag.equals(tmpEintrag)) {
                BlAktienregister blReg = new BlAktienregister(false, new DbBundle());
                if (blReg.updateDatensatz(tmpEintrag) == 1) {
                    tmpEintrag.db_version++;
                    updateInfo(false, tmpEintrag);
                    eintrag = tmpEintrag;
                    geandert = true;
                }
            }
        } else {
            if (tmpEintrag.getStueckAktien() == 0) {
                tfAktien.clear();
                Platform.runLater(() -> tfAktien.requestFocus());
                return;
            }
            BlAktienregister blReg = new BlAktienregister(false, new DbBundle());

            if (blReg.insertDatensatz(tmpEintrag, ParamS.param.paramBasis.inhaberaktienAktiv) == 1) {
                updateInfo(true, blReg.eintrag);
                tmpEintrag = new EclAktienregister();
                setValues(tmpEintrag);
                setCopyString(blReg.eintrag.getAktionaersnummer(), blReg.eintrag.getIsin());
            } else {
                lblInfo.setText("Aktionärsnummer bereits vorhanden.");
            }
        }
    }

    /**
     * On copy.
     *
     * @param event the event
     */
    @FXML
    private void onCopy(ActionEvent event) {

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copyString), null);

    }

    /**
     * Sets the copy string.
     *
     * @param anr  the anr
     * @param isin the isin
     */
    private void setCopyString(String anr, String isin) {

        if (eks.isBlank()) {
            ekFrom = anr;
            eks = anr;
        } else {

            final int lastEk = ekTo.isBlank() ? Integer.valueOf(ekFrom) : Integer.valueOf(ekTo.substring(3));

            if (!ekCheck && lastEk + 1 != Integer.valueOf(anr))
                ekCheck = true;

            ekTo = " - " + anr;
            eks += ", " + anr;
        }

        this.isin = this.isin.isBlank() ? isin : this.isin;

        btnCopy.setVisible(true);

        final String ekPart = ekTo.isBlank() ? " lautet " + eks : "n lauten " + (ekCheck ? eks : ekFrom + ekTo);

        copyString = "Sehr geehrte Damen und Herren,\n\n" + "Ihre Eintrittskartennummer" + ekPart + ".\n\n" + "ISIN: "
                + this.isin + " - " + ParamS.eclEmittent.bezeichnungKurz;

    }

    /**
     * Update info.
     *
     * @param insert  the insert
     * @param eintrag the eintrag
     */
    private void updateInfo(Boolean insert, EclAktienregister eintrag) {

        final KeyFrame kf1 = new KeyFrame(Duration.seconds(0), e -> {

            lblInfo.setText(insert ? "Aktionär wurde gespeichert." : "Änderungen wurden gespeichert");
            createHistorie(insert, eintrag);

        });
        final KeyFrame kf2 = new KeyFrame(Duration.seconds(5), e -> lblInfo.setText(""));
        final Timeline timeline = new Timeline(kf1, kf2);

        Platform.runLater(timeline::play);
    }

    /**
     * Creates the historie.
     *
     * @param insert  the insert
     * @param eintrag the eintrag
     */
    private void createHistorie(Boolean insert, EclAktienregister eintrag) {

        historieList.add(eintrag);

        VBox box = new VBox(1);
        box.setMinWidth(200);
        box.setPrefWidth(200);
        box.setPadding(new Insets(0, 5, 0, 5));
        box.setId(eintrag.getAktionaersnummer());

        final Label lblEk = new Label("Aktionärsnr.: " + eintrag.getAktionaersnummer());

        final Label lblName = new Label(eintrag.getNameKomplett());

        final Label lblAktien = new Label("Aktien: " + eintrag.getStueckAktien());

        box.getChildren().addAll(lblEk, lblName, lblAktien);
        box.setStyle("-fx-border-radius: 5; -fx-border-color: #231825");

        box.setOnMouseClicked(e -> {

            EclAktienregister ar = historieList.stream().filter(x -> x.getAktionaersnummer().equals(box.getId()))
                    .findFirst().orElse(null);

            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (e.getClickCount() == 2) {
                    aendern = true;
                    setValues(EclAktienregister.copyEntity(ar));
                }
            }

            if (e.getButton().equals(MouseButton.SECONDARY)) {
                ContextMenu menu = new ContextMenu();
                MenuItem item = new MenuItem("Duplizieren");
                item.setOnAction(a -> {
                    aendern = false;
                    setValues(EclAktienregister.copyEntity(ar));
                });
                menu.getItems().add(item);
                menu.show(box, e.getScreenX(), e.getScreenY() - 30);
            }
        });

        boxHistorie.getChildren().add(0, box);
    }

    /**
     * Sets the values.
     *
     * @param eintrag the new values
     */
    private void setValues(EclAktienregister eintrag) {

        this.eintrag = eintrag;

        if (!aendern) {
            eintrag.setAktionaersnummer("");
            eintrag.setAktienregisterIdent(0);
            eintrag.setPersonNatJur(0);
            eintrag.setStueckAktien(0);
            eintrag.setStimmen(0);
        }

        tfAnr.setText(aendern ? eintrag.getAktionaersnummer() : "");
        cbAnrede.setValue(cbAnrede.getItems().get(eintrag.getAnredeId()));
        tfTitel.setText(eintrag.getTitel());
        tfNachname.setText(eintrag.getIstJuristischePerson() == 1 ? eintrag.getName1() : eintrag.getNachname());
        tfVorname.setText(eintrag.getIstJuristischePerson() == 1 ? eintrag.getName2() : eintrag.getVorname());
        tfName3.setText(eintrag.getIstJuristischePerson() == 1 ? eintrag.getName3() : "");
        tfAdresszusatz.setText(eintrag.getZusatz());
        tfStrasse.setText(eintrag.getStrasse());
        tfPlz.setText(eintrag.getPostleitzahl());
        tfPostfach.setText(eintrag.getPostfach());
        tfPlzPostfach.setText(eintrag.getPostleitzahlPostfach());
        tfOrt.setText(eintrag.getOrt());
        cbLand.setValue(staaten.stream().filter(e -> e.getId() == eintrag.getStaatId()).findFirst().orElse(null));
        tfAktien.setText(!aendern ? "" : String.valueOf(eintrag.getStueckAktien()));

        cbGattung.setValue(aendern ? eintrag.getGattungId() : cbGattung.getItems().get(0));

        cbIsin.setValue(new EclIsin(cbGattung.getValue(),
                aendern ? eintrag.getIsin() : ParamS.param.paramBasis.getIsin(cbGattung.getValue())));

        cbBesitz.setValue(eintrag.getBesitzart().isBlank() ? "E" : eintrag.getBesitzart());
        tfGruppe.setText(String.valueOf(eintrag.gruppe));

        checkVersandadresse.setSelected(eintrag.getVersandAbweichend() == 1);
        checkJurPerson.setSelected(eintrag.getIstJuristischePerson() == 1);
        checkPersonengemeinschaft.setSelected(eintrag.getIstPersonengemeinschaft() == 1);

        tfAdresse1.setText(eintrag.getAdresszeile1());
        tfAdresse2.setText(eintrag.getAdresszeile2());
        tfAdresse3.setText(eintrag.getAdresszeile3());
        tfAdresse4.setText(eintrag.getAdresszeile4());
        tfAdresse5.setText(eintrag.getAdresszeile5());
        tfAdresse6.setText(eintrag.getAdresszeile6());

        cbAnredeVersand.setValue(cbAnredeVersand.getItems().get(eintrag.getAnredeIdVersand()));
        tfTitelVersand.setText(eintrag.getTitelVersand());
        tfNachnameVersand.setText(eintrag.getNachnameVersand());
        tfVornameVersand.setText(eintrag.getVornameVersand());
        tfName3Versand.setText(eintrag.getName3Versand());
        tfStrasseVersand.setText(eintrag.getStrasseVersand());
        tfPlzVersand.setText(eintrag.getPostleitzahlVersand());
        tfPostfachVersand.setText(eintrag.getPostfachVersand());
        tfPlzPostfachVersand.setText(eintrag.getPostleitzahlPostfachVersand());
        tfOrtVersand.setText(eintrag.getOrtVersand());
        cbLandVersand
                .setValue(staaten.stream().filter(e -> e.getId() == eintrag.getStaatId()).findFirst().orElse(null));

        checkMeldung(1);

        Platform.runLater(() -> cbAnrede.requestFocus());

    }

    /**
     * Gets the values.
     *
     * @param oldEintrag the eintrag
     * @return the values
     */
    private EclAktienregister getValues(EclAktienregister oldEintrag) {

        EclAktienregister newEintrag = new EclAktienregister();

        newEintrag.setAktionaersnummer(checkManuelleNr.isSelected() || aendern ? tfAnr.getText().trim() : "");
        newEintrag.setAnredeId(cbAnrede.getItems().indexOf(cbAnrede.getValue()));

        newEintrag.setTitel(checkJurPerson.isSelected() ? "" : tfTitel.getText().trim());
        newEintrag.setNachname(checkJurPerson.isSelected() ? "" : tfNachname.getText().trim());
        newEintrag.setVorname(checkJurPerson.isSelected() ? "" : tfVorname.getText().trim());

        newEintrag.setName1(checkJurPerson.isSelected() ? tfNachname.getText().trim() : "");
        newEintrag.setName2(checkJurPerson.isSelected() ? tfVorname.getText().trim() : "");
        newEintrag.setName3(checkJurPerson.isSelected() ? tfName3.getText().trim() : "");

        newEintrag.setZusatz(tfAdresszusatz.getText().trim());
        newEintrag.setStrasse(tfStrasse.getText());
        newEintrag.setPostleitzahl(tfPlz.getText());
        newEintrag.setPostfach(tfPostfach.getText());
        newEintrag.setPostleitzahlPostfach(tfPlzPostfachVersand.getText());
        newEintrag.setOrt(tfOrt.getText());
        newEintrag.setStaatId(cbLand.getValue().getId());

        /*
         * REGEX Wenn genau 5 Zahlen - Leerzeichen - Buchstaben
         * Split in PLZ und Ort
         */
        if (tfOrt.getText().matches("(\\d{5}\\s\\D+)")
                && (newEintrag.getStaatId() == 1 || newEintrag.getStaatId() == 56)) {
            final String[] split = tfOrt.getText().split(" ");
            newEintrag.setPostleitzahl(split[0]);
            newEintrag.setOrt(split[1]);
        }

        if (tfAktien.getText().trim().matches("\\d+")) {
            newEintrag.setStueckAktien(Long.parseLong(tfAktien.getText().trim()));
            newEintrag.setStimmen(Long.parseLong(tfAktien.getText().trim()));
        } else {
            tfAktien.clear();
        }

        newEintrag.setIsin(cbIsin.getValue().isin);
        newEintrag.setGattungId(cbGattung.getValue());
        newEintrag.setBesitzart(cbBesitz.getValue());
        newEintrag.setGruppe(Integer.parseInt(tfGruppe.getText()));
        newEintrag.setVersandAbweichend(checkVersandadresse.isSelected() ? 1 : 0);
        newEintrag.setIstJuristischePerson(checkJurPerson.isSelected() ? 1 : 0);
        newEintrag.setIstPersonengemeinschaft(checkPersonengemeinschaft.isSelected() ? 1 : 0);

        newEintrag.setAdresszeile1(tfAdresse1.getText());
        newEintrag.setAdresszeile2(tfAdresse2.getText());
        newEintrag.setAdresszeile3(tfAdresse3.getText());
        newEintrag.setAdresszeile4(tfAdresse4.getText());
        newEintrag.setAdresszeile5(tfAdresse5.getText());
        newEintrag.setAdresszeile6(tfAdresse6.getText());

        newEintrag.setNameKomplett(BlAktienregisterImport.nameKomplett(newEintrag));

        newEintrag.setAnredeIdVersand(cbAnredeVersand.getItems().indexOf(cbAnrede.getValue()));
        newEintrag.setTitelVersand(checkJurPerson.isSelected() ? "" : tfTitelVersand.getText());
        newEintrag.setNachnameVersand(checkJurPerson.isSelected() ? "" : tfNachnameVersand.getText());
        newEintrag.setVornameVersand(checkJurPerson.isSelected() ? "" : tfVornameVersand.getText());

        newEintrag.setName1Versand(checkJurPerson.isSelected() ? tfNachnameVersand.getText() : "");
        newEintrag.setName2Versand(checkJurPerson.isSelected() ? tfVornameVersand.getText() : "");
        newEintrag.setName3Versand(checkJurPerson.isSelected() ? tfName3Versand.getText() : "");
        newEintrag.setZusatzVersand(tfAdresszusatz.getText().trim());
        newEintrag.setStrasseVersand(tfStrasseVersand.getText());
        newEintrag.setPostleitzahlVersand(tfPlzVersand.getText());
        newEintrag.setPostfachVersand(tfPostfachVersand.getText());
        newEintrag.setPostleitzahlPostfachVersand(tfPlzPostfachVersand.getText());
        newEintrag.setOrtVersand(tfOrtVersand.getText());
        newEintrag.setStaatIdVersand(cbLandVersand.getValue().getId());

        //      Aus bestehenden Datensatz übernehmen        
        newEintrag.mandant = oldEintrag.mandant;
        newEintrag.aktienregisterIdent = oldEintrag.aktienregisterIdent;
        newEintrag.db_version = oldEintrag.db_version;
        newEintrag.personNatJur = oldEintrag.personNatJur;
        newEintrag.email = oldEintrag.email;
        newEintrag.stimmausschluss = oldEintrag.stimmausschluss;
        newEintrag.gruppe = oldEintrag.gruppe;
        newEintrag.englischerVersand = oldEintrag.englischerVersand;
        newEintrag.herkunftQuelle = oldEintrag.herkunftQuelle;
        newEintrag.herkunftQuelleLfd = oldEintrag.herkunftQuelleLfd;
        newEintrag.herkunftIdent = oldEintrag.herkunftIdent;
        newEintrag.versandNummer = oldEintrag.versandNummer;
        newEintrag.datenUpdate = oldEintrag.datenUpdate;

        newEintrag.setBestandGeaendert(newEintrag.getStueckAktien() != oldEintrag.getStueckAktien());
        newEintrag.setBesitzGeaendert(!newEintrag.getBesitzart().equals(oldEintrag.getBesitzart()));
        newEintrag.setPersonNatJurGeaendert(BlAktienregister.checkPersonUpdate(newEintrag, oldEintrag));

        return newEintrag;
    }

    /**
     * Inits the.
     *
     * @param pStage      the stage
     * @param pLoginDaten the login daten
     */
    public void init(Stage pStage, EclTLoginDatenM pLoginDaten) {
        eigeneStage = pStage;
        eintrag = pLoginDaten == null ? new EclAktienregister()
                : pLoginDaten.eclAktienregister == null ? new EclAktienregister() : pLoginDaten.eclAktienregister;
        aendern = eintrag.aktienregisterIdent != 0 ? true : false;
    }

}
