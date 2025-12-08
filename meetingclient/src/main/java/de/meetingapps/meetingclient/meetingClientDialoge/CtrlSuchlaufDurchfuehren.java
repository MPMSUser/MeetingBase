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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingBestand.CtrlInstiPflegeZuordnung;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComBl.BlSuchlauf;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufVerwendung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlSuchlaufDurchfuehren.
 */
public class CtrlSuchlaufDurchfuehren extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn speichern beenden. */
    @FXML
    private Button btnSpeichernBeenden;

    /** The tab pane gesamt. */
    @FXML
    private TabPane tabPaneGesamt;

    /** The tab veraenderung. */
    @FXML
    private Tab tabVeraenderung;

    /** The scpn veraenderungen. */
    @FXML
    private ScrollPane scpnVeraenderungen;

    /** The tab alle. */
    @FXML
    private Tab tabAlle;

    /** The scpn alle. */
    @FXML
    private ScrollPane scpnAlle;

    /** The tab unbearbeitete. */
    @FXML
    private Tab tabUnbearbeitete;

    /** The scpn unbearbeitete. */
    @FXML
    private ScrollPane scpnUnbearbeitete;

    /** The tab verarbeitete. */
    @FXML
    private Tab tabVerarbeitete;

    /** The scpn verarbeitete. */
    @FXML
    private ScrollPane scpnVerarbeitete;

    /** The tab ausgeblendete. */
    @FXML
    private Tab tabAusgeblendete;

    /** The scpn ausgeblendete. */
    @FXML
    private ScrollPane scpnAusgeblendete;

    /** The btn aktualisieren. */
    @FXML
    private Button btnAktualisieren;

    /** The ta suchbegriffe. */
    @FXML
    private TextArea taSuchbegriffe;

    /** The btn ausgewaehlte verarbeiten 1. */
    @FXML
    private Button btnAusgewaehlteVerarbeiten1;

    /** The btn ausgewaehlte ausblenden. */
    @FXML
    private Button btnAusgewaehlteAusblenden;

    /** The lbl suche in. */
    @FXML
    private Label lblSucheIn;

    /** The lbl aktion. */
    @FXML
    private Label lblAktion;

    /** The btn einzelsuche durchfuehren. */
    @FXML
    private Button btnEinzelsucheDurchfuehren;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn ausgewaehlte einblenden. */
    @FXML
    private Button btnAusgewaehlteEinblenden;

    /** The tf ausgeblendet weil. */
    @FXML
    private TextField tfAusgeblendetWeil;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn ausgewaehlte verarbeiten 2. */
    @FXML
    private Button btnAusgewaehlteVerarbeiten2;

    /** The btn ausgewaehlte verarbeiten 3. */
    @FXML
    private Button btnAusgewaehlteVerarbeiten3;

    /** ****Ab hier individuell**********. */
    private TableView<MSuchlaufErgebnis> tableAlle = null;

    /** The list alle. */
    private ObservableList<MSuchlaufErgebnis> listAlle = null;

    /** The table veraenderung. */
    private TableView<MSuchlaufErgebnis> tableVeraenderung = null;

    /** The list veraenderung. */
    private ObservableList<MSuchlaufErgebnis> listVeraenderung = null;

    /** The table unbearbeitete. */
    private TableView<MSuchlaufErgebnis> tableUnbearbeitete = null;

    /** The list unbearbeitete. */
    private ObservableList<MSuchlaufErgebnis> listUnbearbeitete = null;

    /** The table verarbeitete. */
    private TableView<MSuchlaufErgebnis> tableVerarbeitete = null;

    /** The list verarbeitete. */
    private ObservableList<MSuchlaufErgebnis> listVerarbeitete = null;

    /** The table ausgeblendete. */
    private TableView<MSuchlaufErgebnis> tableAusgeblendete = null;

    /** The list ausgeblendete. */
    private ObservableList<MSuchlaufErgebnis> listAusgeblendete = null;

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /*+++++Funktionsspezifische Parameter+++++*/
    /** Institutionelle Zuordnung. */
    private long rcAktienTeilbestand = 0;

    /** The rc user login zugeordnet. */
    private boolean[] rcUserLoginZugeordnet = null;

    /** The rc beschreibung. */
    private String rcBeschreibung = "";

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSpeichernBeenden != null
                : "fx:id=\"btnSpeichernBeenden\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabPaneGesamt != null
                : "fx:id=\"tabPaneGesamt\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabVeraenderung != null
                : "fx:id=\"tabVeraenderung\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert scpnVeraenderungen != null
                : "fx:id=\"scpnVeraenderungen\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabAlle != null
                : "fx:id=\"tabAlle\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert scpnAlle != null
                : "fx:id=\"scpnAlle\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabUnbearbeitete != null
                : "fx:id=\"tabUnbearbeitete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert scpnUnbearbeitete != null
                : "fx:id=\"scpnUnbearbeitete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabVerarbeitete != null
                : "fx:id=\"tabVerarbeitete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert scpnVerarbeitete != null
                : "fx:id=\"scpnVerarbeitete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tabAusgeblendete != null
                : "fx:id=\"tabAusgeblendete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert scpnAusgeblendete != null
                : "fx:id=\"scpnAusgeblendete\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAktualisieren != null
                : "fx:id=\"btnAktualisieren\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert taSuchbegriffe != null
                : "fx:id=\"taSuchbegriffe\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAusgewaehlteVerarbeiten1 != null
                : "fx:id=\"btnAusgewaehlteVerarbeiten1\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAusgewaehlteAusblenden != null
                : "fx:id=\"btnAusgewaehlteAusblenden\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert lblSucheIn != null
                : "fx:id=\"lblSucheIn\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert lblAktion != null
                : "fx:id=\"lblAktion\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnEinzelsucheDurchfuehren != null
                : "fx:id=\"btnEinzelsucheDurchfuehren\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAusgewaehlteEinblenden != null
                : "fx:id=\"btnAusgewaehlteEinblenden\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert tfAusgeblendetWeil != null
                : "fx:id=\"tfAusgeblendetWeil\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAusgewaehlteVerarbeiten2 != null
                : "fx:id=\"btnAusgewaehlteVerarbeiten2\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";
        assert btnAusgewaehlteVerarbeiten3 != null
                : "fx:id=\"btnAusgewaehlteVerarbeiten3\" was not injected: check your FXML file 'SuchlaufDurchfuehren.fxml'.";

        /*************** Ab hier individuell ******************************/

        lDbBundle = new DbBundle();

        if (!buttonVerarbeiten1.isEmpty()) {
            btnAusgewaehlteVerarbeiten1.setText(buttonVerarbeiten1);
        } else {
            btnAusgewaehlteVerarbeiten1.setVisible(false);
        }
        if (!buttonVerarbeiten2.isEmpty()) {
            btnAusgewaehlteVerarbeiten2.setText(buttonVerarbeiten2);
        } else {
            btnAusgewaehlteVerarbeiten2.setVisible(false);
        }
        if (!buttonVerarbeiten3.isEmpty()) {
            btnAusgewaehlteVerarbeiten2.setText(buttonVerarbeiten3);
        } else {
            btnAusgewaehlteVerarbeiten3.setVisible(false);
        }

        taSuchbegriffe.setEditable(false);
        zeigeTitelBereichAn();

        initialisierungLeseAnzeigeListen();
        zeigeScrollViews();

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                if (frageObAbbruch() == false) {
                    event.consume();
                }
            }
        });

    }

    /**
     * ********************Reaktionen auf Click*********************************.
     *
     * @param event the event
     */

    @FXML
    void onBtnSpeichern(ActionEvent event) {
        blSuchlauf.speichern(false);
    }

    /**
     * On btn speichern beenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernBeenden(ActionEvent event) {
        blSuchlauf.speichern(true);
        eigeneStage.hide();
    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        if (frageObAbbruch() == true) {
            eigeneStage.hide();
        }
    }

    /**
     * On btn aktualisieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnAktualisieren(ActionEvent event) {
        //    	blSuchlauf.testClassBlSuchlaufVertreterFuerSuchErgebnisListe();

        int aktienregisterOderMeldungen = 1;
        if (suchlaufMeldungen == true) {
            aktienregisterOderMeldungen = 2;
        }

        int nurAktionaereOderNurVertreterOderAktionaereVertreter = 0;
        if (suchlaufAktionaersname && suchlaufVertretername) {
            nurAktionaereOderNurVertreterOderAktionaereVertreter = 3;
        } else {
            if (suchlaufAktionaersname) {
                nurAktionaereOderNurVertreterOderAktionaereVertreter = 1;
            } else {
                nurAktionaereOderNurVertreterOderAktionaereVertreter = 2;
            }
        }

        blSuchlauf.sucheNachSuchbegriffen(aktienregisterOderMeldungen,
                nurAktionaereOderNurVertreterOderAktionaereVertreter, durchsuchenSammelkarten,
                durchsuchenInSammelkarten, durchsuchenGaeste, auchInaktiveAufnehmen);
        initialisierungObservableListenAlle();
        zeigeScrollViews();

    }

    /**
     * On btn einzelsuche durchfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnEinzelsucheDurchfuehren(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.standard(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "In Suchlaufergebnis übernehmen";
        controllerFenster.mehrfachAuswahlZulaessig = true;

        controllerFenster.durchsuchenSammelkarten = durchsuchenSammelkarten;
        controllerFenster.durchsuchenInSammelkarten = durchsuchenInSammelkarten;
        controllerFenster.durchsuchenGaeste = durchsuchenGaeste;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, titel, true);

        if (controllerFenster.rcFortsetzen) {
            int anz = controllerFenster.rcAktienregister.length;
            for (int i = 0; i < anz; i++) {
                System.out.println(controllerFenster.rcAktienregister[i].aktionaersnummer);
            }

            blSuchlauf.ergaenzeAktienregisterArray = controllerFenster.rcAktienregister;
            blSuchlauf.ergaenzeMeldungenArray = controllerFenster.rcMeldungen;
            blSuchlauf.ergaenzeVollmacht = controllerFenster.rcSonstigeVollmacht;
            blSuchlauf.ergaenzeSuchlaufErgebnis(controllerFenster.rcSuchlaufSuchbegriffArt,
                    controllerFenster.rcSuchbegriff);
            initialisierungObservableListenAlle();
            zeigeScrollViews();
        }
    }

    /**
     * On btn ausgewaehlte ausblenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteAusblenden(ActionEvent event) {
        ausgewaehlteAufarbeiten(1);
    }

    /**
     * On btn ausgewaehlte einblenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteEinblenden(ActionEvent event) {
        ausgewaehlteAufarbeiten(-1);
    }

    /**
     * On btn ausgewaehlte verarbeiten 1.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeiten1(ActionEvent event) {
        ausgewaehlteAufarbeiten(101);
    }

    /**
     * On btn ausgewaehlte verarbeiten 2.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeiten2(ActionEvent event) {
        ausgewaehlteAufarbeiten(102);
    }

    /**
     * On btn ausgewaehlte verarbeiten 3.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeiten3(ActionEvent event) {
        ausgewaehlteAufarbeiten(103);
    }

    /**
     * On btn ausgewaehlte verarbeiten stornieren 1.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeitenStornieren1(ActionEvent event) {
        ausgewaehlteAufarbeiten(-101);
    }

    /**
     * On btn ausgewaehlte verarbeiten stornieren 2.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeitenStornieren2(ActionEvent event) {
        ausgewaehlteAufarbeiten(-102);
    }

    /**
     * On btn ausgewaehlte verarbeiten stornieren 3.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusgewaehlteVerarbeitenStornieren3(ActionEvent event) {
        ausgewaehlteAufarbeiten(-103);
    }

    /**
     * **************Anzeigefunktionen*******************************.
     *
     * @return true, if successful
     */

    private boolean frageObAbbruch() {
        if (blSuchlauf.pruefenObSpeichernErforderlich() == false) {
            return true;
        }
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        return caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Achtung! Beim Beenden des Programmes gehen alle seit dem letzten Speichern durchgeführten Änderungen verloren!",
                "Ohne Speichern beenden", "Suchlaufbearbeitung fortsetzen");

    }

    /**
     * Zeige titel bereich an.
     */
    private void zeigeTitelBereichAn() {

        lblAktion.setText(aktionsTitel);

        String sucheNach = "Suche nach ";
        if (suchlaufMeldungen == false) {
            sucheNach += "Aktionärsname";
        } else {
            if (suchlaufAktionaersname && suchlaufVertretername && suchlaufMeldungen == true) {
                sucheNach += "Aktionärs-/Vertretername";
            } else {
                if (suchlaufAktionaersname) {
                    sucheNach += "Aktionärsname";
                } else {
                    sucheNach += "Vertretername";
                }
            }
        }
        lblSucheIn.setText(sucheNach);

        taSuchbegriffe.setText(blSuchlauf.suchlaufBegriffe);

        //    	if (suchlaufRegister==false) {
        //    		btnNameRegister.setDisable(true);
        //
        //    	}
        //    	if (suchlaufMeldungen==false) {
        //    		btnEKNummer.setDisable(true);
        //    		btnSKNummer.setDisable(true);
        //    		btnMeldeIdent.setDisable(true);
        //    		btnNameAktionaer.setDisable(true);
        //    		btnNameVertreter.setDisable(true);
        //    		btnNameAktionaerVertreter.setDisable(true);
        //    	}
    }

    /**
     * Zeige scroll views.
     */
    private void zeigeScrollViews() {
        int breite = 1800;
        int hoehe = 530;

        tableAlle = baueGrundAnsichtOhneInhalte();
        tableAlle.setPrefHeight(hoehe);
        tableAlle.setPrefWidth(breite);
        if (listAlle.size() == 0) {
            System.out.println("listAlle size 0");
            scpnAlle.setContent(null);
        } else {
            tableAlle.setItems(listAlle);
            scpnAlle.setContent(tableAlle);
        }
        tableAlle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableVeraenderung = baueGrundAnsichtOhneInhalte();
        tableVeraenderung.setPrefHeight(hoehe);
        tableVeraenderung.setPrefWidth(breite);
        if (listVeraenderung.size() == 0) {
            System.out.println("listVeraenderung size 0");
            scpnVeraenderungen.setContent(null);
        } else {
            tableVeraenderung.setItems(listVeraenderung);
            scpnVeraenderungen.setContent(tableVeraenderung);
        }
        tableVeraenderung.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableUnbearbeitete = baueGrundAnsichtOhneInhalte();
        tableUnbearbeitete.setPrefHeight(hoehe);
        tableUnbearbeitete.setPrefWidth(breite);
        if (listUnbearbeitete.size() == 0) {
            System.out.println("listUnbearbeitete size 0");
            scpnUnbearbeitete.setContent(null);
        } else {
            tableUnbearbeitete.setItems(listUnbearbeitete);
            scpnUnbearbeitete.setContent(tableUnbearbeitete);
        }
        tableUnbearbeitete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableVerarbeitete = baueGrundAnsichtOhneInhalte();
        tableVerarbeitete.setPrefHeight(hoehe);
        tableVerarbeitete.setPrefWidth(breite);
        if (listVerarbeitete.size() == 0) {
            System.out.println("listVerarbeitete size 0");
            scpnVerarbeitete.setContent(null);
        } else {
            tableVerarbeitete.setItems(listVerarbeitete);
            scpnVerarbeitete.setContent(tableVerarbeitete);
        }
        tableVerarbeitete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableAusgeblendete = baueGrundAnsichtOhneInhalte();
        tableAusgeblendete.setPrefHeight(hoehe);
        tableAusgeblendete.setPrefWidth(breite);
        if (listAusgeblendete.size() == 0) {
            System.out.println("listAusgeblendete size 0");
            scpnAusgeblendete.setContent(null);
        } else {
            tableAusgeblendete.setItems(listAusgeblendete);
            scpnAusgeblendete.setContent(tableAusgeblendete);
        }
        tableAusgeblendete.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    /**
     * Baue grund ansicht ohne inhalte.
     *
     * @return the table view
     */
    private TableView<MSuchlaufErgebnis> baueGrundAnsichtOhneInhalte() {
        CtrlSuchenAllgemein ctrlSuchenAllgemein = new CtrlSuchenAllgemein();
        TableView<MSuchlaufErgebnis> tableNeu = ctrlSuchenAllgemein.baueGrundAnsichtTableViewOhneInhalte(1, true, true,
                suchlaufMeldungen, true);
        return tableNeu;
    }

    /**
     * ********************Logiken************************************.
     */

    /** Erst-Initialisierung */
    private void initialisierungLeseAnzeigeListen() {
        blSuchlauf.leseSuchlaufErgebnisEin();
        initialisierungObservableListenAlle();
    }

    /**
     * Initialisierung observable listen alle.
     */
    private void initialisierungObservableListenAlle() {
        blSuchlauf.belegeDetailErgebnisse();
        for (int i = 1; i <= 5; i++) {
            initialisierungObservableListenEinzeln(i);
        }

    }

    /**
     * pArt 
     * =1 => tableAlle 
     * =2 => tableVeraenderung 
     * =3 => tableUnbearbeitete 
     * =4 => tableVerarbeitete 
     * =5 => tableAusgeblendete.
     *
     * @param pArt the art
     */
    private void initialisierungObservableListenEinzeln(int pArt) {
        ObservableList<MSuchlaufErgebnis> listZwischen = FXCollections.observableArrayList();

        List<EclSuchlaufErgebnis> suchlaufZwischenListe = null;
        List<EclAktienregister> aktienregisterZwischenListe = null;
        List<EclMeldung> meldungZwischenListe = null;

        switch (pArt) {
        case 1:
            suchlaufZwischenListe = blSuchlauf.suchlaufErgebnisAlleListe;
            aktienregisterZwischenListe = blSuchlauf.aktienregisterAlleListe;
            meldungZwischenListe = blSuchlauf.meldungAlleListe;
            break;
        case 2:
            suchlaufZwischenListe = blSuchlauf.suchlaufErgebnisVeraenderungListe;
            aktienregisterZwischenListe = blSuchlauf.aktienregisterVeraenderungListe;
            meldungZwischenListe = blSuchlauf.meldungVeraenderungListe;
            break;
        case 3:
            suchlaufZwischenListe = blSuchlauf.suchlaufErgebnisUnbearbeiteteListe;
            aktienregisterZwischenListe = blSuchlauf.aktienregisterUnbearbeiteteListe;
            meldungZwischenListe = blSuchlauf.meldungUnbearbeiteteListe;
            break;
        case 4:
            suchlaufZwischenListe = blSuchlauf.suchlaufVerarbeiteteListe;
            aktienregisterZwischenListe = blSuchlauf.aktienregisterVerarbeiteteListe;
            meldungZwischenListe = blSuchlauf.meldungVerarbeiteteListe;
            break;
        case 5:
            suchlaufZwischenListe = blSuchlauf.suchlaufErgebnisAusgeblendeteListe;
            aktienregisterZwischenListe = blSuchlauf.aktienregisterAusgeblendeteListe;
            meldungZwischenListe = blSuchlauf.meldungAusgeblendeteListe;
            break;
        }

        for (int i = 0; i < suchlaufZwischenListe.size(); i++) {
            MSuchlaufErgebnis lMSuchlaufErgebnis = null;
            if (suchlaufRegister) {
                lMSuchlaufErgebnis = new MSuchlaufErgebnis(suchlaufZwischenListe.get(i),
                        aktienregisterZwischenListe.get(i), i);
            } else {
                lMSuchlaufErgebnis = new MSuchlaufErgebnis(suchlaufZwischenListe.get(i), meldungZwischenListe.get(i),
                        i);
            }
            listZwischen.add(lMSuchlaufErgebnis);
        }

        switch (pArt) {
        case 1:
            listAlle = listZwischen;
            break;
        case 2:
            listVeraenderung = listZwischen;
            break;
        case 3:
            listUnbearbeitete = listZwischen;
            break;
        case 4:
            listVerarbeitete = listZwischen;
            break;
        case 5:
            listAusgeblendete = listZwischen;
            break;
        }
    }

    /** The ausgewaehlt ausgeblendete. */
    private boolean ausgewaehltAusgeblendete = false;

    /** The ausgewaehlt verarbeitete. */
    private boolean ausgewaehltVerarbeitete = false;

    /** The ausgewaehlt eingeblendete. */
    private boolean ausgewaehltEingeblendete = false;

    /** The ausgewaehlt unverarbeitete. */
    private boolean ausgewaehltUnverarbeitete = false;

    /** The ausgewaehlt inaktive. */
    private boolean ausgewaehltInaktive = false;

    /**
     * buttonArt: 1=Ausblenden -1=Einblenden 101/102/103=Verarbeiten1/2/3
     * -101/-102/-103=Verarbeiten Rückgängig1/2/3.
     *
     * @param buttonArt the button art
     */
    private void ausgewaehlteAufarbeiten(int buttonArt) {
        int tabNr = tabPaneGesamt.getSelectionModel().getSelectedIndex();

        /***********Schritt 1: ausgewhälte Elemente in ausgewaehlteMSuchlaufArray ablegen, und grundsätzliche *****************
         * - übergreifende - Prüfungen durchführen.
         *
         */
        ObservableList<Integer> ausgewaehlteAktionaereIndex = null;
        ObservableList<MSuchlaufErgebnis> listAktiv = null;

        switch (tabNr) {
        case 0:
            ausgewaehlteAktionaereIndex = tableVeraenderung.getSelectionModel().getSelectedIndices();
            listAktiv = listVeraenderung;
            break;
        case 1:
            ausgewaehlteAktionaereIndex = tableAlle.getSelectionModel().getSelectedIndices();
            listAktiv = listAlle;
            break;
        case 2:
            ausgewaehlteAktionaereIndex = tableUnbearbeitete.getSelectionModel().getSelectedIndices();
            listAktiv = listUnbearbeitete;
            break;
        case 3:
            ausgewaehlteAktionaereIndex = tableVerarbeitete.getSelectionModel().getSelectedIndices();
            listAktiv = listVerarbeitete;
            break;
        case 4:
            ausgewaehlteAktionaereIndex = tableAusgeblendete.getSelectionModel().getSelectedIndices();
            listAktiv = listAusgeblendete;
            break;
        }

        int anzahlAusgewaehlteAktionaere = ausgewaehlteAktionaereIndex.size();
        if (anzahlAusgewaehlteAktionaere == 0) {
            this.fehlerMeldung("Bitte zu verarbeitende Aktionäre auswählen!");
            return;
        }

        ausgewaehltAusgeblendete = false;
        ausgewaehltVerarbeitete = false;
        ausgewaehltEingeblendete = false;
        ausgewaehltUnverarbeitete = false;
        ausgewaehltInaktive = false;

        MSuchlaufErgebnis[] ausgewaehlteMSuchlaufArray = new MSuchlaufErgebnis[anzahlAusgewaehlteAktionaere];

        for (int i = 0; i < anzahlAusgewaehlteAktionaere; i++) {
            MSuchlaufErgebnis lSuchlaufErgebnis = listAktiv.get(ausgewaehlteAktionaereIndex.get(i));
            ausgewaehlteMSuchlaufArray[i] = lSuchlaufErgebnis;

            if (lSuchlaufErgebnis.wurdeVerarbeitet == 1) {
                ausgewaehltVerarbeitete = true;
            } else {
                ausgewaehltUnverarbeitete = true;
            }
            if (lSuchlaufErgebnis.wurdeAusSucheAusgegrenzt == 1) {
                ausgewaehltAusgeblendete = true;
            } else {
                ausgewaehltEingeblendete = true;
            }
            if (lSuchlaufErgebnis.istGesperrt) {
                ausgewaehltInaktive = true;
            }
        }

        if (buttonArt == 1) {
            /*Ausblenden*/
            if (ausgewaehltAusgeblendete) {
                fehlerMeldung("Ausblenden nicht möglich - Ausgeblendete selektiert!");
                return;
            }
            if (ausgewaehltVerarbeitete) {
                fehlerMeldung("Ausblenden nicht möglich - Verarbeitete selektiert!");
                return;
            }
        }
        if (buttonArt == -1) {
            /*Einblenden*/
            if (ausgewaehltEingeblendete) {
                fehlerMeldung("Einblenden nicht möglich - Eingeblendete selektiert!");
                return;
            }
        }
        if (buttonArt == 2) {
            /*Verarbeiten*/
            if (ausgewaehltInaktive && suchlaufMeldungen && verarbeitenGesperrte == false) {
                fehlerMeldung("Verarbeiten nicht möglich - Gesperrte Meldungen selektiert!");
                return;
            }
            if (ausgewaehltVerarbeitete && verarbeiteteDuerfenNochmalVerarbeitetWerden == false) {
                fehlerMeldung("Verarbeiten nicht möglich - Verarbeitete selektiert!");
                return;
            }
            if (ausgewaehltAusgeblendete) {
                fehlerMeldung("Verarbeiten nicht möglich - Ausgeblendete selektiert!");
                return;
            }
        }
        if (buttonArt == -2) {
            /*Verarbeiten Rückgängig*/
            /*Verarbeiten*/
            if (ausgewaehltInaktive && suchlaufMeldungen && verarbeitenStornierenGesperrte == false) {
                fehlerMeldung("Verarbeiten Stornieren nicht möglich - Gesperrte Meldungen selektiert!");
                return;
            }
            if (ausgewaehltUnverarbeitete) {
                fehlerMeldung("Verarbeiten Stornieren nicht möglich - Unverarbeitete selektiert!");
                return;
            }
        }

        /*************************Schritt 2: ausgewähltes Suchlaufergebnis füllen**********************************/
        /** Nun Verarbeitung durchführen */
        blSuchlauf.ausgewaehlteInit();
        for (int i = 0; i < anzahlAusgewaehlteAktionaere; i++) {
            MSuchlaufErgebnis lMSuchlaufErgebnis = listAktiv.get(ausgewaehlteAktionaereIndex.get(i));
            blSuchlauf.ausgewaehlteTrageElementEin(lMSuchlaufErgebnis.identSuchlaufErgebnis);
        }

        if (buttonArt == 1) {
            blSuchlauf.setzeSuchlaufAufAusgeblendet(tfAusgeblendetWeil.getText());
        }
        if (buttonArt == -1) {
            blSuchlauf.setzeSuchlaufAufEingeblendet();
        }
        if (buttonArt >= 100) {
            verarbeitungDurchfuehren(buttonArt);
        }
        if (buttonArt <= -100) {
            verarbeitungRueckgaengig(buttonArt);
        }

        /*Anzeige Refreshen*/
        initialisierungObservableListenAlle();
        zeigeScrollViews();
    }

    /**
     * ***************************Hier der Teil, in dem Verarbeitungsfunktionen
     * gemäß dem aufrufenden Modul aufgerufen werden müssen***.
     *
     * @param buttonArt the button art
     */
    private void verarbeitungDurchfuehren(int buttonArt) {
        switch (aktuelleFunktion) {
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister:
            if (buttonArt == 101) {
                verarbeitung_bestandsZuordnungInstiAktienregister();
            } else {
                verarbeitung_bestandsZuordnungInstiAktienregister_teilBestand();
            }
            break;
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen:
            verarbeitung_bestandsZuordnungInstiMeldungen();
            break;
        }
    }

    /**
     * Verarbeitung rueckgaengig.
     *
     * @param buttonArt the button art
     */
    private void verarbeitungRueckgaengig(int buttonArt) {
        switch (aktuelleFunktion) {
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister:
            verarbeitungRueckgaengig_bestandsZuordnungInstiAktienregister();
            break;
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen:
            verarbeitungRueckgaengig_bestandsZuordnungInstiMeldungen();
            break;
        }

    }

    /*++++++++++++Einzelfunktionen++++++++++++++++++++++++++++++++++++++++++*/

    /**
     * Verarbeitung bestands zuordnung insti aktienregister.
     */
    private void verarbeitung_bestandsZuordnungInstiAktienregister() {
        boolean brc = rufe_verarbeitung_BestandsZuordnung_auf(false, true);
        if (!brc) {
            return;
        }

        /**Zum Verarbeiten steht nun bereit (gilt auch für
         * > verarbeitung_bestandsZuordnungInstiAktienregister_teilBestand
         * )
         * rcAktienTeilbestand : -1 => Gesamtbestand wird zugeordnet
         * rcBeschreibung: Text für die Zuordnung
         * blInsti.rcUserLoginZuInsti: alle UserLogins de jeweiligen Insti
         * rcUserLoginZugeordnet: true, wenn entsprechender User gemäß
         *      blInsti.rcUserLoginZuInsti zugeordnet werden soll
         * blSuchlauf.suchlaufErgebnisAusgewaehltListe/aktienregisterAusgewaehltListe/meldungAusgewaehltListe:
         *      Ausgewählte Suchlaufeinträge, die verarbeitet werden sollen.
         */
        blSuchlauf.ordneBestandZu(aktuelleInsti, 1, false, -1, rcBeschreibung, blInsti.rcUserLoginZuInsti,
                rcUserLoginZugeordnet);
    }

    /**
     * Verarbeitung bestands zuordnung insti aktienregister teil bestand.
     */
    private void verarbeitung_bestandsZuordnungInstiAktienregister_teilBestand() {
        if (blSuchlauf.suchlaufErgebnisAusgewaehltListe.size() > 1) {
            this.fehlerMeldung("Teilbestands-Zuordnung: nur 1 Ergebnis darf ausgewählt werden!");
            return;
        }
        boolean brc = rufe_verarbeitung_BestandsZuordnung_auf(true, true);
        if (!brc) {
            return;
        }
        blSuchlauf.ordneBestandZu(aktuelleInsti, 1, true, rcAktienTeilbestand, rcBeschreibung,
                blInsti.rcUserLoginZuInsti, rcUserLoginZugeordnet);
    }

    /**
     * Verarbeitung rueckgaengig bestands zuordnung insti aktienregister.
     */
    private void verarbeitungRueckgaengig_bestandsZuordnungInstiAktienregister() {
        /*XXX*/
    }

    /**
     * Verarbeitung bestands zuordnung insti meldungen.
     */
    private void verarbeitung_bestandsZuordnungInstiMeldungen() {
        boolean brc = rufe_verarbeitung_BestandsZuordnung_auf(false, false);
        if (!brc) {
            return;
        }
        blSuchlauf.ordneBestandZu(aktuelleInsti, 2, false, -1, rcBeschreibung, blInsti.rcUserLoginZuInsti,
                rcUserLoginZugeordnet);
    }

    /**
     * Verarbeitung rueckgaengig bestands zuordnung insti meldungen.
     */
    private void verarbeitungRueckgaengig_bestandsZuordnungInstiMeldungen() {
        /*XXX*/
    }

    /**
     * Rufe verarbeitung bestands zuordnung auf.
     *
     * @param pTeilBestand             the teil bestand
     * @param pAktienregisterZuordnung the aktienregister zuordnung
     * @return true, if successful
     */
    private boolean rufe_verarbeitung_BestandsZuordnung_auf(boolean pTeilBestand, boolean pAktienregisterZuordnung) {
        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        long lAktien = 0;
        int lAktienregisterIdent = 0;
        if (pTeilBestand) {
            lAktien = blSuchlauf.aktienregisterAusgewaehltListe.get(0).stimmen;
            lAktienregisterIdent = blSuchlauf.aktienregisterAusgewaehltListe.get(0).aktienregisterIdent;
        }

        CtrlInstiPflegeZuordnung controllerFenster = new CtrlInstiPflegeZuordnung();
        controllerFenster.init(newStage, pTeilBestand, pAktienregisterZuordnung, blInsti, aktuelleInsti,
                lAktienregisterIdent, lAktien);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/InstiPflegeZuordnung.fxml", 1280, 720,
                "Zuordnung zu Institutionellem", true);

        if (!controllerFenster.rcFortsetzen) {
            return false;
        }

        rcAktienTeilbestand = controllerFenster.rcAktienTeilbestand;
        rcUserLoginZugeordnet = controllerFenster.rcUserLoginZugeordnet;
        rcBeschreibung = controllerFenster.rcBeschreibung;

        return true;
    }

    /** ***************Initialisierung****************************. */

    public String titel = "Suchen";

    /** The verarbeitete duerfen nochmal verarbeitet werden. */
    public boolean verarbeiteteDuerfenNochmalVerarbeitetWerden = false;

    /** Wenn true, dann dürfen auch gesperrte Meldungen verarbeitet werden. */
    public boolean verarbeitenGesperrte = false;

    /**
     * Wenn true, dann dürfen auch gesperrte Meldungen, die bereits verarbeitet
     * sind, wieder "Verarbeitung-Storniert" werden.
     */
    public boolean verarbeitenStornierenGesperrte = false;

    /** The button verarbeiten 1. */
    public String buttonVerarbeiten1 = "";

    /** The button verarbeiten 2. */
    public String buttonVerarbeiten2 = "";

    /** The button verarbeiten 3. */
    public String buttonVerarbeiten3 = "";

    /*+++++Funktionsspezifische Parameter+++++*/
    /** Institutionelle Zuordnung. */
    public BlInsti blInsti = null;

    /** The aktuelle insti. */
    public EclInsti aktuelleInsti = null;

    /** Siehe KonstSuchlaufVerwendung. */
    private int aktuelleFunktion = 0;

    /** The suchlauf aktionaersname. */
    private boolean suchlaufAktionaersname = false;

    /** The suchlauf vertretername. */
    private boolean suchlaufVertretername = false;

    /** The suchlauf register. */
    private boolean suchlaufRegister = false;

    /** The suchlauf meldungen. */
    private boolean suchlaufMeldungen = false;

    /** The durchsuchen sammelkarten. */
    private boolean durchsuchenSammelkarten = false;

    /** The durchsuchen in sammelkarten. */
    private boolean durchsuchenInSammelkarten = true;

    /** The durchsuchen gaeste. */
    private boolean durchsuchenGaeste = false;

    /** The auch inaktive aufnehmen. */
    private boolean auchInaktiveAufnehmen = false;

    /** The aktions titel. */
    private String aktionsTitel = "";

    /** The bl suchlauf. */
    private BlSuchlauf blSuchlauf = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     * @param pBlSuchlauf  the bl suchlauf
     */
    public void init(Stage pEigeneStage, int pFunktion, BlSuchlauf pBlSuchlauf) {
        eigeneStage = pEigeneStage;
        aktuelleFunktion = pFunktion;
        blSuchlauf = pBlSuchlauf;

        suchlaufRegister = KonstSuchlaufVerwendung.isRegisterSuche(aktuelleFunktion);
        suchlaufMeldungen = KonstSuchlaufVerwendung.isMeldebestandSuche(aktuelleFunktion);

        switch (aktuelleFunktion) {
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister:
            suchlaufAktionaersname = true;
            suchlaufVertretername = false;

            /*Eigentlich Irrelevant, nur der Vollständigkeit halber*/
            durchsuchenSammelkarten = false;
            durchsuchenInSammelkarten = false;
            durchsuchenGaeste = false;
            auchInaktiveAufnehmen = false;

            aktionsTitel = "Aus Register: " + blSuchlauf.eclSuchlaufDefinition.bezeichnung;
            break;
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen:
            suchlaufAktionaersname = true;
            suchlaufVertretername = false;

            durchsuchenSammelkarten = false;
            durchsuchenInSammelkarten = true;
            durchsuchenGaeste = false;
            auchInaktiveAufnehmen = false;

            aktionsTitel = "Aus Meldungen: " + blSuchlauf.eclSuchlaufDefinition.bezeichnung;
            break;
        }

    }

}
