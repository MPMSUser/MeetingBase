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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.meetingapps.meetingclient.meetingBestand.CtrlVollmachtWeisung;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComBl.BlSuchen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Allgemein verwendbarer Dialog zum Suchen.
 * 
 * Schnittstellen-Definition siehe vor init();
 */
public class CtrlSuchen extends CtrlRoot {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane listPane;

    @FXML
    private VBox functionBox;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The ta suchbegriffe. */
    @FXML
    private TextField taSuchbegriffe;

    /** The lbl durchsucht werden. */
    @FXML
    private Label lblDurchsuchtWerden;

    /** The btn register nummer. */
    @FXML
    private Button btnRegisterNummer;

    /** The btn EK nummer. */
    @FXML
    private Button btnEKNummer;

    /** The btn SK nummer. */
    @FXML
    private Button btnSKNummer;

    /** The btn melde ident. */
    @FXML
    private Button btnMeldeIdent;

    /** The btn sammel ident. */
    @FXML
    private Button btnSammelIdent;

    /** The btn name aktionaer. */
    @FXML
    private Button btnNameAktionaer;

    /** The btn name vertreter. */
    @FXML
    private Button btnNameVertreter;

    /** The btn name aktionaer vertreter. */
    @FXML
    private Button btnNameAktionaerVertreter;

    /** The btn QR code. */
    @FXML
    private Button btnQRCode;

    /** The btn suchbegriff leeren. */
    @FXML
    private Button btnSuchbegriffLeeren;

    /** The scpn ergebnis. */
    @FXML
    private ScrollPane scpnErgebnis;

    /** The btn funktion 1. */
    @FXML
    private Button btnFunktion1;

    /** The btn funktion 2. */
    @FXML
    private Button btnFunktion2;

    /** The btn funktion 3. */
    @FXML
    private Button btnFunktion3;

    /** The btn funktion 4. */
    @FXML
    private Button btnFunktion4;

    /** The btn funktion 5. */
    @FXML
    private Button btnFunktion5;

    /** The btn funktion 6. */
    @FXML
    private Button btnFunktion6;

    /** The btn sammelkarte buchen. */
    @FXML
    private Button btnSammelkarteBuchen;

    /** ****Ab hier individuell**********. */

    private DbBundle lDbBundle = null;

    /** The bl suchen. */
    private BlSuchen blSuchen = null;

    /** The table alle. */
    private TableView<MSuchlaufErgebnis> tableAlle = new TableView<>();

    /** The list alle. */
    private ObservableList<MSuchlaufErgebnis> listAlle = null;

    /** The sonstigen vertreter anzeigen. */
    private boolean sonstigenVertreterAnzeigen = false;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        if (!funktionsButton1.isEmpty()) {
            btnFunktion1.setText(funktionsButton1);
        } else {
            btnFunktion1.setVisible(false);
            btnFunktion1.setManaged(false);
        }
        if (!funktionsButton2.isEmpty()) {
            btnFunktion2.setText(funktionsButton2);
        } else {
            btnFunktion2.setVisible(false);
            btnFunktion2.setManaged(false);
        }
        if (!funktionsButton3.isEmpty()) {
            btnFunktion3.setText(funktionsButton3);
        } else {
            btnFunktion3.setVisible(false);
            btnFunktion3.setManaged(false);
        }
        if (!funktionsButton4.isEmpty()) {
            btnFunktion4.setText(funktionsButton4);
        } else {
            btnFunktion4.setVisible(false);
            btnFunktion4.setManaged(false);
        }
        if (!funktionsButton5.isEmpty()) {
            btnFunktion5.setText(funktionsButton5);
        } else {
            btnFunktion5.setVisible(false);
            btnFunktion5.setManaged(false);
        }
        if (!funktionsButton6.isEmpty()) {
            btnFunktion6.setText(funktionsButton6);
        } else {
            btnFunktion6.setVisible(false);
            btnFunktion6.setManaged(false);
        }

        if (!funktionsButtonSammelkarte) {
            btnSammelkarteBuchen.setVisible(false);
            btnSammelkarteBuchen.setManaged(false);
        }

        if (suchenNachInterneIdent == false) {
            btnMeldeIdent.setDisable(true);
            btnMeldeIdent.setVisible(false);
            btnSammelIdent.setDisable(true);
            btnSammelIdent.setVisible(false);
        }
        if (suchenNurNachGastMeldungen == true) {
            btnRegisterNummer.setDisable(true);
            btnRegisterNummer.setVisible(false);
            btnSKNummer.setDisable(true);
            btnSKNummer.setVisible(false);
            btnNameAktionaer.setDisable(true);
            btnNameAktionaer.setVisible(false);
            btnNameVertreter.setDisable(true);
            btnNameVertreter.setVisible(false);
            btnQRCode.setDisable(true);
            btnQRCode.setVisible(false);
        }

        String durchsuchtWerden = "Berücksichtigt werden: ";
        String durchsuchtWerdenNicht = "";
        if (durchsuchenNurGaeste == false) {
            durchsuchtWerden += "Aktionäre (nicht in Sammelk.); ";
        } else {
            durchsuchtWerdenNicht += "Aktionäre; ";
        }
        if (durchsuchenSammelkarten && suchenNurNachGastMeldungen == false) {
            durchsuchtWerden += "Sammelkarten; ";
        } else {
            durchsuchtWerdenNicht += "Sammelkarten; ";
        }
        if (durchsuchenInSammelkarten && suchenNurNachGastMeldungen == false) {
            durchsuchtWerden += "Meldungen in Sammelkarten; ";
        } else {
            durchsuchtWerdenNicht += "Meldungen in Sammelkarten;";
        }

        if (durchsuchenGaeste) {
            durchsuchtWerden += "Gäste;";
        } else {
            durchsuchtWerdenNicht += "Gäste;";
        }

        if (!durchsuchtWerdenNicht.isEmpty()) {
            durchsuchtWerdenNicht = " Nicht Berücksichtigt werden: " + durchsuchtWerdenNicht;
        }
        lblDurchsuchtWerden.setText(durchsuchtWerden + durchsuchtWerdenNicht);

        lDbBundle = new DbBundle();

        Platform.runLater(() -> taSuchbegriffe.requestFocus());

        tableAlle.setRowFactory(tv -> {
            TableRow<MSuchlaufErgebnis> row = new TableRow<>();
            row.setOnMouseClicked(x -> {
                if (x.getClickCount() == 2 && !row.isEmpty() && !funktionsButton1.isEmpty()) {
                    weiterVerarbeiten(1);
                }
            });
            return row;
        });

        functionBox.heightProperty()
                .addListener((o, oV, nV) -> AnchorPane.setBottomAnchor(listPane, nV.doubleValue() + 25.0));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        if (screenSize.getHeight() < 864.0)
            eigeneStage.setMaximized(true);
        
    }

    /**
     * ************Eingabe-Reaktionen******************************.
     *
     * @param event the event
     */

    @FXML
    private void onEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

            if (taSuchbegriffe.getText().isEmpty()) {
                this.fehlerMeldung("Bitte Suchbegriff eingeben!");
                return;
            }

            Alert a = new Alert(AlertType.CONFIRMATION);

            a.setTitle("Suchen");
            a.setHeaderText("Suchen");
            a.getButtonTypes().remove(0);

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20, 10, 20, 10));
            grid.setVgap(10);

            Map<Button, Button> map = new LinkedHashMap<>();

            /*
             * Prueft ob Text aus maximal 2 Buchstaben besteht
             */
            if (taSuchbegriffe.getText().matches("(.*?\\D){2,}")) {

                if (btnNameAktionaer.isVisible()) {
                    Button btn1 = new Button("Name Aktionär (Register)");
                    map.put(btn1, btnNameAktionaer);
                }
                if (btnNameVertreter.isVisible()) {
                    Button btn2 = new Button("Name Vertreter");
                    map.put(btn2, btnNameVertreter);
                }
                if (btnNameAktionaerVertreter.isVisible()) {
                    Button btn3 = new Button("Name (Meldung) oder akt. Vertr.");
                    map.put(btn3, btnNameAktionaerVertreter);
                }

            } else {

                if (btnRegisterNummer.isVisible()) {
                    Button btn4 = new Button("Register-Nummer");
                    map.put(btn4, btnRegisterNummer);
                }
                if (btnEKNummer.isVisible()) {
                    Button btn5 = new Button("Eintrittskartennummer");
                    map.put(btn5, btnEKNummer);
                }
                if (btnSKNummer.isVisible()) {
                    Button btn6 = new Button("Stimmkartennummer");
                    map.put(btn6, btnSKNummer);
                }
                if (btnMeldeIdent.isVisible()) {
                    Button btn7 = new Button("MeldeIdent");
                    map.put(btn7, btnMeldeIdent);
                }
                if (btnSammelIdent.isVisible()) {
                    Button btn8 = new Button("Sammelident");
                    map.put(btn8, btnSammelIdent);
                }
            }

            if (map.isEmpty()) {
                return;
            }

            configureGrid(a, grid, map);

            a.getDialogPane().setContent(grid);

            Platform.runLater(() -> map.keySet().iterator().next().requestFocus());

            a.showAndWait();

        }
    }

    /**
     * Configure grid.
     *
     * @param a    the a
     * @param grid the grid
     * @param map  the map
     */
    private void configureGrid(Alert a, GridPane grid, Map<Button, Button> map) {

        int y = 0;

        for (Button btn : map.keySet()) {

            btn.setPrefWidth(250);
            btn.setOnAction(e -> {
                a.hide();
                Platform.runLater(() -> map.get(btn).fire());
            });
            grid.add(btn, 0, y++);
        }
    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        rcFortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * On btn suchbegriff leeren.
     *
     * @param event the event
     */
    @FXML
    void onBtnSuchbegriffLeeren(ActionEvent event) {
        scpnErgebnis.setContent(null);
        taSuchbegriffe.setText("");
    }

    /**
     * On btn EK nummer.
     *
     * @param event the event
     */
    @FXML
    void onBtnEKNummer(ActionEvent event) {
        lFehlertext = "";
        pruefeZahlNichtLeerLaenge(taSuchbegriffe, "Suchbegriff",
                lDbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.eintrittskartennummer]);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        taSuchbegriffe.setText(
                blNummernformen.formatiereNr(taSuchbegriffe.getText(), KonstKartenklasse.eintrittskartennummer));
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.ekNummer);
    }

    /**
     * On btn melde ident.
     *
     * @param event the event
     */
    @FXML
    void onBtnMeldeIdent(ActionEvent event) {
        lFehlertext = "";
        pruefeZahlNichtLeerLaenge(taSuchbegriffe, "Suchbegriff", 8);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.meldeIdent);
    }

    /**
     * On btn sammel ident.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelIdent(ActionEvent event) {
        lFehlertext = "";
        pruefeZahlNichtLeerLaenge(taSuchbegriffe, "Suchbegriff", 8);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.sammelIdent);
    }

    /**
     * On btn name aktionaer.
     *
     * @param event the event
     */
    @FXML
    void onBtnNameAktionaer(ActionEvent event) {
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.nameAktionaer);
    }

    /**
     * On btn name aktionaer vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnNameAktionaerVertreter(ActionEvent event) {
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter);
    }

    /**
     * On btn name vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnNameVertreter(ActionEvent event) {
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.nameVertreter);
    }

    /**
     * On btn QR code.
     *
     * @param event the event
     */
    @FXML
    void onBtnQRCode(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeigeNichtFertig(eigeneStage);
        //    	sucheAusloesen(KonstSuchlaufSuchbegriffArt.qrCode);
    }

    /**
     * On btn register nummer.
     *
     * @param event the event
     */
    @FXML
    void onBtnRegisterNummer(ActionEvent event) {
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.registerNummer);
    }

    /**
     * On btn SK nummer.
     *
     * @param event the event
     */
    @FXML
    void onBtnSKNummer(ActionEvent event) {
        lFehlertext = "";
        pruefeZahlNichtLeerLaenge(taSuchbegriffe, "Suchbegriff",
                lDbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.stimmkartennummer]);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        taSuchbegriffe
                .setText(blNummernformen.formatiereNr(taSuchbegriffe.getText(), KonstKartenklasse.stimmkartennummer));
        sucheAusloesen(KonstSuchlaufSuchbegriffArt.skNummer);
    }

    /**
     * On btn funktion 1.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion1(ActionEvent event) {
        weiterVerarbeiten(1);
    }

    /**
     * On btn funktion 2.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion2(ActionEvent event) {
        weiterVerarbeiten(2);
    }

    /**
     * On btn funktion 3.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion3(ActionEvent event) {
        weiterVerarbeiten(3);
    }

    /**
     * On btn funktion 4.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion4(ActionEvent event) {
        weiterVerarbeiten(4);
    }

    /**
     * On btn funktion 5.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion5(ActionEvent event) {
        weiterVerarbeiten(5);
    }

    /**
     * On btn funktion 6.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktion6(ActionEvent event) {
        weiterVerarbeiten(6);
    }

    /**
     * On btn sammelkarte.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkarte(ActionEvent event) {
        weiterVerarbeiten(7);
    }

    /**
     * Suche ausloesen.
     *
     * @param pFunktion the funktion
     */
    private void sucheAusloesen(int pFunktion) {
        rcSuchlaufSuchbegriffArt = pFunktion;
        if (rcSuchlaufSuchbegriffArt == KonstSuchlaufSuchbegriffArt.nameVertreter) {
            sonstigenVertreterAnzeigen = true;
        } else {
            sonstigenVertreterAnzeigen = false;
        }
        rcSuchbegriff = taSuchbegriffe.getText();
        if (rcSuchbegriff.isEmpty()) {
            this.fehlerMeldung("Bitte Suchbegriff eingeben!");
            return;
        }

        blSuchen = new BlSuchen(false, lDbBundle);
        blSuchen.sucheAusfuehren(pFunktion, rcSuchbegriff, durchsuchenSammelkarten, durchsuchenInSammelkarten,
                durchsuchenGaeste, durchsuchenNurGaeste);
        if (blSuchen.rcAktienregister == null || blSuchen.rcAktienregister.length == 0) {
            this.fehlerMeldung("Nichts gefunden!");
            return;
        }

        initialisierungObservableListeNachSuchen();

        CtrlSuchenAllgemein ctrlSuchenAllgemein = new CtrlSuchenAllgemein();

        int lViewNr = 1;
        boolean lAnzeigeSuchlauf = false;
        boolean lAnzeigeRegister = true;
        boolean lAnzeigeMeldungen = true;
        boolean lAnzeigeSonstieVollmachten = sonstigenVertreterAnzeigen;

        tableAlle = ctrlSuchenAllgemein.baueGrundAnsichtTableViewOhneInhalte(lViewNr, lAnzeigeSuchlauf,
                lAnzeigeRegister, lAnzeigeMeldungen, lAnzeigeSonstieVollmachten);
        tableAlle.setPrefHeight(510);
        tableAlle.setPrefWidth(1790);
        if (blSuchen.rcAktienregister == null || blSuchen.rcAktienregister.length == 0) {
            scpnErgebnis.setContent(null);
        } else {
            tableAlle.setItems(listAlle);
            scpnErgebnis.setContent(tableAlle);
        }

        if (mehrfachAuswahlZulaessig) {
            tableAlle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        /*
         * Doppelklick fuehrt direkt Funktionsbutton 1 aus wenn verfügbar
         */
        if (!funktionsButton1.isEmpty()) {
            tableAlle.setRowFactory(tv -> {
                TableRow<MSuchlaufErgebnis> row = new TableRow<>();
                row.setOnMouseClicked(x -> {
                    if (x.getClickCount() == 2 && !row.isEmpty()) {
                        weiterVerarbeiten(1);
                    }
                });
                return row;
            });
        }
    }

    /**
     * Weiter verarbeiten.
     *
     * @param buttonNummer the button nummer
     */
    private void weiterVerarbeiten(int buttonNummer) {
        ObservableList<Integer> ausgewaehlteAktionaereIndex = null;
        if (tableAlle != null) {
            ausgewaehlteAktionaereIndex = tableAlle.getSelectionModel().getSelectedIndices();
        }
        int anzahlAusgewaehlteAktionaere = 0;
        if (ausgewaehlteAktionaereIndex != null) {
            anzahlAusgewaehlteAktionaere = ausgewaehlteAktionaereIndex.size();
        }
        if (anzahlAusgewaehlteAktionaere == 0) {
            if ((buttonNummer == 1 && funktionsButton1OhneAuswahlZulaessig == false)
                    || (buttonNummer == 2 && funktionsButton2OhneAuswahlZulaessig == false)
                    || (buttonNummer == 3 && funktionsButton3OhneAuswahlZulaessig == false)
                    || (buttonNummer == 4 && funktionsButton4OhneAuswahlZulaessig == false)
                    || (buttonNummer == 5 && funktionsButton5OhneAuswahlZulaessig == false)
                    || (buttonNummer == 6 && funktionsButton6OhneAuswahlZulaessig == false) || (buttonNummer == 7)) {
                this.fehlerMeldung("Bitte zu verarbeitende Aktionäre auswählen!");
                return;
            }
        }
        rcAktienregister = new EclAktienregister[anzahlAusgewaehlteAktionaere];
        rcMeldungen = new EclMeldung[anzahlAusgewaehlteAktionaere];
        rcSonstigeVollmacht = new String[anzahlAusgewaehlteAktionaere];

        boolean gefAngemeldete = false;
        boolean gefNichtAngemeldete = false;

        for (int i = 0; i < anzahlAusgewaehlteAktionaere; i++) {
            int offset = listAlle.get(ausgewaehlteAktionaereIndex.get(i)).laufendeNummerInArray;

            rcAktienregister[i] = blSuchen.rcAktienregister[offset];
            rcMeldungen[i] = blSuchen.rcMeldungen[offset];
            if (rcMeldungen[i].meldungsIdent == 0) {
                gefNichtAngemeldete = true;
            } else {
                gefAngemeldete = false;
            }
            rcSonstigeVollmacht[i] = blSuchen.rcSonstigeVollmacht[offset];

            if (gefNichtAngemeldete && selektionNichtAngemeldeteMoeglich == false) {
                this.fehlerMeldung("Nicht-Angemeldete Aktionäre dürfen nicht selektiert werden!");
                return;
            }
            if (gefAngemeldete && selektionAngemeldeteMoeglich == false) {
                this.fehlerMeldung("Angemeldete Aktionäre dürfen nicht selektiert werden!");
                return;
            }
        }

        if (buttonNummer == 7) {

            //          Sammelkarte auswählen eigene Funktion machen InhaberImport und hier genutzt
            BlSammelkarten sam = new BlSammelkarten(false, new DbBundle());
            sam.holeSammelkartenMeldeDaten(true, 0);
            List<EclMeldung> tmpList = Arrays.asList(sam.rcSammelMeldung).stream()
                    .filter(e -> e.gattung == rcAktienregister[0].getGattungId()).collect(Collectors.toList());

            List<String> sammelkarten = new ArrayList<>();
            tmpList.forEach(e -> sammelkarten.add(e.meldungsIdent + " - " + e.name + " - " + e.zusatzfeld2));

            ChoiceDialog<String> dialog = new ChoiceDialog<>(sammelkarten.get(0), sammelkarten);

            dialog.setTitle("Sammelkarte auswählen");
            dialog.setHeaderText("Es wurden Weisungen erkannt \n Bitte Sammelkarte auswählen");
            dialog.setContentText("Bitte auswählen:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                final String[] txt = result.get().split(" - ");

                EclMeldung sk = tmpList.stream().filter(e -> e.meldungsIdent == Integer.parseInt(txt[0])).findFirst()
                        .orElse(null);

                WSClient wsClient = new WSClient();
                WELogin weLogin = new WELogin();
                weLogin.setKennungArt(1);

                WELoginCheck weLoginCheck = new WELoginCheck();

                List<EclTLoginDatenM> listLoginDaten = new ArrayList<>();
                List<EclZugeordneteMeldungNeu> listMeldung = new ArrayList<>();

                for (var eintrag : rcMeldungen) {
                    if (eintrag.meldungEnthaltenInSammelkarte == 0) {

                        weLogin.setKennung(eintrag.aktionaersnummer);
                        weLoginCheck.weLogin = weLogin;

                        WELoginCheckRC weLoginCheckRC = wsClient.loginCheck(weLoginCheck);

                        listLoginDaten.add(weLoginCheckRC.eclTLoginDatenM);

                        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(weLoginCheckRC.eclTLoginDatenM);
                        WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
                        EclBesitzJeKennung eclBesitzJeKennung = weTeilnehmerStatusGetRC.besitzJeKennungListe.get(0);
                        EclBesitzAREintrag eclBesitzAREintrag = eclBesitzJeKennung.eigenerAREintragListe.get(0);

                        EclZugeordneteMeldungNeu tmp = eclBesitzAREintrag.zugeordneteMeldungenListe.stream()
                                .filter(e -> e.meldungsIdent == eintrag.meldungsIdent).findFirst().orElse(null);

                        if (tmp == null) {
                            return;
                        }
                        listMeldung.add(tmp);
                    }
                }

                if (listLoginDaten.isEmpty()) {
                    this.fehlerMeldung("Alle ausgewählten Aktionäre bereits in Sammelkarte!");
                    return;
                }

                Stage neuerDialog = new Stage();
                CaIcon.master(neuerDialog);

                CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

                controllerFenster.listLoginDaten = listLoginDaten;
                controllerFenster.listMeldung = listMeldung;

                controllerFenster.init(neuerDialog, findSkArt(sk.skIst), 2, listLoginDaten.get(0), listMeldung.get(0),
                        null);

                Platform.runLater(() -> {
                    for (var cb : controllerFenster.cbKIAV.getItems()) {
                        if (cb.ident1 == sk.meldungsIdent)
                            controllerFenster.cbKIAV.setValue(cb);
                    }
                });

                CaController caController = new CaController();
                caController.open(neuerDialog, controllerFenster,
                        "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                        "Vollmacht und Weisung", true);

            }
            taSuchbegriffe.requestFocus();
            return;
        }

        eigeneStage.hide();
        rcFortsetzen = true;
        rcFunktion = buttonNummer;
    }

    /**
     * Initialisierung observable liste nach suchen.
     */
    private void initialisierungObservableListeNachSuchen() {
        listAlle = FXCollections.observableArrayList();

        for (int i = 0; i < blSuchen.rcAktienregister.length; i++) {
            MSuchlaufErgebnis lMSuchlaufErgebnis = null;
            lMSuchlaufErgebnis = new MSuchlaufErgebnis(new EclSuchlaufErgebnis(), blSuchen.rcAktienregister[i],
                    blSuchen.rcMeldungen[i], blSuchen.rcSonstigeVollmacht[i], i);
            listAlle.add(lMSuchlaufErgebnis);
        }

    }

    /**
     * **********************************************Schnittstellendefinition************************************************************.
     */

    /**
     * Eingabeparameter: 6 Funktionsbuttons
     * 
     * Die Funktionsbuttons können aktiviert werden, um aus der Suchmaske heraus
     * unterschiedliche Funktionen auszulösen. Nach dem Ausführen der Such-Funktion
     * kann abgefragt werden, welcher Funktionsbutton gedrückt wurde.
     * 
     * *=1 bis 6 funktionsButton* = Beschriftung des Funktionsbuttons; wenn leer,
     * dann wird dieser Funktionsbutton nicht angezeigt.
     * funktionsButton*OhneAuswahlZulaessig = falls true, dann kann dieser
     * Funktionsbutton betätigt werden, ohne dass eine vorherige Auswahl aus dem
     * Suchergebnis erfolgt. false => eine vorherige Auswahl im Suchergebnis ist
     * erforderlich.
     * 
     * Dazugehöriger Returnwert: rcFunktion liefert die Nummer des Funktionsbuttons,
     * der gedrückt wurde.
     */
    public String funktionsButton1 = "";

    /** The funktions button 1 ohne auswahl zulaessig. */
    public boolean funktionsButton1OhneAuswahlZulaessig = false;

    /** The funktions button 2. */
    public String funktionsButton2 = "";

    /** The funktions button 2 ohne auswahl zulaessig. */
    public boolean funktionsButton2OhneAuswahlZulaessig = false;

    /** The funktions button 3. */
    public String funktionsButton3 = "";

    /** The funktions button 3 ohne auswahl zulaessig. */
    public boolean funktionsButton3OhneAuswahlZulaessig = false;

    /** The funktions button 4. */
    public String funktionsButton4 = "";

    /** The funktions button 4 ohne auswahl zulaessig. */
    public boolean funktionsButton4OhneAuswahlZulaessig = false;

    /** The funktions button 5. */
    public String funktionsButton5 = "";

    /** The funktions button 5 ohne auswahl zulaessig. */
    public boolean funktionsButton5OhneAuswahlZulaessig = false;

    /** The funktions button 6. */
    public String funktionsButton6 = "";

    /** The funktions button 6 ohne auswahl zulaessig. */
    public boolean funktionsButton6OhneAuswahlZulaessig = false;

    /** The funktions button sammelkarte. */
    public boolean funktionsButtonSammelkarte = false;

    /**
     * Eingabeparameter: =false => es kann nur ein Satz ausgewählt werden. =true =>
     * es können mehrere Sätze ausgewählt werden
     */
    public boolean mehrfachAuswahlZulaessig = false;

    /**
     * Eingabeparamter: true=> es können angemeldete Aktionäre ausgewählt werden.
     * false=> Angemeldete Aktionäre können nicht ausgewählt werden
     */
    public boolean selektionAngemeldeteMoeglich = true;

    /**
     * Eingabeparamter: true=> es können nicht-angemeldete Aktionäre ausgewählt
     * werden. false=> Nicht-Angemeldete Aktionäre können nicht ausgewählt werden
     */
    public boolean selektionNichtAngemeldeteMoeglich = true;

    /**
     * Eingabeparameter: über die folgenden 3 Parameter wird gesteuert, welche Sätze
     * durchsucht werden sollen. "normale" Aktionäre werden immer dementsprechend
     * immer durchsucht (Ausnahme: siehe durchsuchenNurGaeste). Sammelkarten,
     * Aktionäre in Sammelkarten und Gäste werden nur durchsucht, wenn der
     * entsprechende Parameter auf true steht.
     */
    public boolean durchsuchenSammelkarten = true;

    /** The durchsuchen in sammelkarten. */
    public boolean durchsuchenInSammelkarten = true;

    /** The durchsuchen gaeste. */
    public boolean durchsuchenGaeste = true;

    /**
     * Eingabeparameter: "hebelt" sozusagen die 3 obigen paremter (durchsuchen*)
     * aus. Denn wenn dieser auf true steht, werden ausschließlich Gäste durchsucht
     * - egal wie die obigen 3 Parameter stehen.
     */
    public boolean durchsuchenNurGaeste = false;

    /** Suchbuttons nach meldeIdent und sammelIdent sind aktiv. */
    public boolean suchenNachInterneIdent = true;

    /**
     * Aktienregister, Vollmachten-Such-Buttons werden ausgeblendet. Es kann nur
     * nach Eintrittskarten-Nummer und Name in Gast-Meldungen gesucht werden.
     */
    public boolean suchenNurNachGastMeldungen = false;

    /**
     * Wenn true, dann wird beim Suchen nach Registernummer die eingegebene Nummer
     * "vervollständigt" (mit hinten 0, falls {@literal <10}).
     */
    public boolean aktienregisterNummerAufbereiten = false;

    /**
     * Return-Wert: false => Button Abbrechen wurde gedrückt true => anderer Button
     * wurde gedrückt, Button-Nummer steht in rcFunktion.
     */
    public boolean rcFortsetzen = false;

    /**
     * Return-Wert: liefert die Nummer des Funktions-Buttons, die gedrückt wurde.
     */
    public int rcFunktion = 0;

    /**
     * Return-Wert: nach was wurde gesucht? rcSuchlaufSuchbegriffArt gibt zurück,
     * welcher Begriff zum Suchen verwendet wurde - siehe
     * KonstSuchlaufSuchbegriffArt. rcSuchbegriff gibt den Begriff zurück, nach dem
     * gesucht wurde.
     */
    public int rcSuchlaufSuchbegriffArt = 0;

    /** The rc suchbegriff. */
    public String rcSuchbegriff = "";

    /**
     * Return-Wert: Such-Ergebnis rcAktienregister[x]
     * 
     * rcSonsigeVollmacht[x] ist üblicherweise null, außer es wurde nach Vertretern
     * gesucht.
     */
    public EclAktienregister[] rcAktienregister = null;

    /** The rc meldungen. */
    public EclMeldung[] rcMeldungen = null;

    /** The rc sonstige vollmacht. */
    public String[] rcSonstigeVollmacht = null;

    /**
     * ***************Initialisierung****************************.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /**
     * Find sk art.
     *
     * @param skIst the sk ist
     * @return the int
     */
    public int findSkArt(int skIst) {

        return switch (skIst) {
        case KonstSkIst.kiav -> KonstPortalAktion.KIAV_MIT_WEISUNG_NEU;
        case KonstSkIst.srv -> KonstPortalAktion.SRV_NEU;
        case KonstSkIst.organisatorisch -> KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU;
        case KonstSkIst.briefwahl -> KonstPortalAktion.BRIEFWAHL_NEU;
        case KonstSkIst.dauervollmacht -> KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU;
        default -> 0;
        };
    }

}
