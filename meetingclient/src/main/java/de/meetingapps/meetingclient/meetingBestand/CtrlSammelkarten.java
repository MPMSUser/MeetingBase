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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlMeldungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComStub.StubMandantAnlegen;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkarten.
 */
public class CtrlSammelkarten extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn sammelkartendaten aendern. */
    @FXML
    private Button btnSammelkartendatenAendern;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The btn vertreter eintrittskarten. */
    @FXML
    private Button btnVertreterEintrittskarten;

    @FXML
    private Button btnInaktiveWeisungenAktivieren;

    
    /** The cb nur aktive anzeigen. */
    @FXML
    private CheckBox cbNurAktiveAnzeigen;

    /** The cb null bestaende. */
    @FXML
    private CheckBox cbNullBestaende;

    /** The btn details anzeigen. */
    @FXML
    private Button btnDetailsAnzeigen;

    /** The btn bearbeitung kopfweisungen. */
    @FXML
    private Button btnBearbeitungKopfweisungen;

    /** The btn konsistenzpruefung sammelkarten. */
    @FXML
    private Button btnKonsistenzpruefungSammelkarten;

    /** The btn stapel umbuchungen. */
    @FXML
    private Button btnStapelUmbuchungen;

    /** The btn neue sammelkarte anlegen. */
    @FXML
    private Button btnNeueSammelkarteAnlegen;

    /** The btn zuordnen EK alle. */
    @FXML
    private Button btnZuordnenEKAlle;

    /**
     * Clicked btn stapel umbuchungen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnStapelUmbuchungen(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }

        rufeDetailsAuf(5, "Aktionäre Umbuchen", selectedMeldeIdent, true);

    }

    /** The aktuelle sammel meldung. */
    private EclMeldung aktuelleSammelMeldung = null;

    /** The weisungen sammelkopf. */
    private EclWeisungMeldung weisungenSammelkopf = null;

    /** The aktionaers summen. */
    private EclWeisungMeldung aktionaersSummen = null;

    /** The sammel fehler. */
    private boolean sammelFehler = false;

    /** The bl abstimmungen weisungen erfassen. */
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /**
     * Clicked btn konsistenzpruefung sammelkarten.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnKonsistenzpruefungSammelkarten(ActionEvent event) {
        /**
         * TODO Konsistenprüfung sammelkarten: Hier derzeit nur eine temporäre Lösung.
         * gehört eigentlich in BlSammelkarten, aber die Tagesordnungsverwaltung ist
         * hierfür noch nicht richtig gelöst.
         */
        DbBundle lDbBundle = new DbBundle();

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, lDbBundle);
        blSammelkarten.holeSammelkartenDaten(true, 0);

        EclMeldung[] alleSammelkarten = blSammelkarten.rcSammelMeldung;

        int anzahlSammelkarten = alleSammelkarten.length;
        for (int i = 0; i < anzahlSammelkarten; i++) {
            aktuelleSammelMeldung = alleSammelkarten[i];
            sammelFehler = false;
            if (aktuelleSammelMeldung.meldungAktiv == 1) {
                blSammelkarten.leseKopfWeisungUndAktionaereZuSammelkarte(aktuelleSammelMeldung);

                int aktuelleGattung = aktuelleSammelMeldung.liefereGattung();

                weisungenSammelkopf = blSammelkarten.rcWeisungenSammelkopf;
                aktionaersSummen = blSammelkarten.rcAktionaersSummen;

                fuelleTabWeisungssummenZeigeBereich(aktuelleGattung, 1);
                fuelleTabWeisungssummenZeigeBereich(aktuelleGattung, 2);

            }
            if (sammelFehler) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Sammelident " + Integer.toString(aktuelleSammelMeldung.meldungsIdent) + " fehlerhaft!");
            }
        }

    }

    /**
     * pArt= 1 => "Normale" Agenda 2 => Gegenanträge.
     *
     * @param aktuelleGattung the aktuelle gattung
     * @param pArt            the art
     */
    private void fuelleTabWeisungssummenZeigeBereich(int aktuelleGattung, int pArt) {

        int anzahlAbstimmungen = 0;
        if (pArt == 1) {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        } else {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);
        }

        for (int i = 0; i < anzahlAbstimmungen/* CInjects.weisungsAgendaAnzAgenda[aktuelleGattung] */; i++) {
            EclAbstimmung lAbstimmung = null;
            if (pArt == 1) {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][i];
            } else {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][i];
            }

            int abstimmungsPosition = lAbstimmung.identWeisungssatz;
            if (abstimmungsPosition != -1) {
                /* Erst ermitteln, ob Fehler in gesamter Summe - denn dann ganze Zeile rot! */
                long summeGesamt = 0;
                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor) {
                        summeGesamt += weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                    }
                }
                if (summeGesamt != aktuelleSammelMeldung.stimmen) {
                    sammelFehler = true;
                }

                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor) {
                        long wertSammelkarte = weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        long wertAktionaere = aktionaersSummen.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        if (wertSammelkarte != wertAktionaere) {
                            sammelFehler = true;
                        }
                    }
                }
            }
        }

    }

    /** Ab hier individuell. */
    TableView<MSammelkarten> tableSammelkarten = null;

    /** The ctrl sammelkarten zeige liste. */
    CtrlSammelkartenUebergreifend ctrlSammelkartenZeigeListe = null;

    /** The map meldung. */
    Map<Integer, EclMeldung> mapMeldung = new HashMap<>();

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnNeueSammelkarteAnlegen != null
                : "fx:id=\"btnNeueSammelkarteAnlegen\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnSammelkartendatenAendern != null
                : "fx:id=\"btnSammelkartendatenAendern\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert scpnUebersicht != null
                : "fx:id=\"scpnUebersicht\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnVertreterEintrittskarten != null
                : "fx:id=\"btnVertreterEintrittskarten\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert cbNurAktiveAnzeigen != null
                : "fx:id=\"cbNurAktiveAnzeigen\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnDetailsAnzeigen != null
                : "fx:id=\"btnDetailsAnzeigen\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnBearbeitungKopfweisungen != null
                : "fx:id=\"btnBearbeitungKopfweisungen\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnKonsistenzpruefungSammelkarten != null
                : "fx:id=\"btnKonsistenzpruefungSammelkarten\" was not injected: check your FXML file 'Sammelkarten.fxml'.";
        assert btnStapelUmbuchungen != null
                : "fx:id=\"btnStapelUmbuchungen\" was not injected: check your FXML file 'Sammelkarten.fxml'.";

        /** Ab hier individuell */
        DbBundle dbBundle = new DbBundle();
        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(false, dbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

        ctrlSammelkartenZeigeListe = new CtrlSammelkartenUebergreifend();

        /** Table-View vorbereiten */
        tableSammelkarten = ctrlSammelkartenZeigeListe.vorbereitenTableViewSammelkarten();
        tableSammelkarten.prefHeightProperty().bind(scpnUebersicht.heightProperty());
        tableSammelkarten.prefWidthProperty().bind(scpnUebersicht.widthProperty());
        tableSammelkarten.setEditable(true);

        scpnUebersicht.setContent(tableSammelkarten);

        ContextMenu menu = new ContextMenu();

        MenuItem item1 = new MenuItem("Stimmkarte Drucken");
        MenuItem item2 = new MenuItem("Report Aktionäre");

        menu.getItems().addAll(item1, item2);

        tableSammelkarten.setContextMenu(menu);

        item1.setOnAction(e -> {
            MSammelkarten obj = tableSammelkarten.getSelectionModel().getSelectedItem();
            CtrlStimmkartenDrucken print = new CtrlStimmkartenDrucken();
            print.gewaehltGattung = 1;
            print.gewaehltStimmkarte = 1;
            print.drucken(obj.getZutrittsIdent());
        });

        item2.setDisable(true);

        tableSammelkarten.setRowFactory(tv -> {
            TableRow<MSammelkarten> row = new TableRow<>();
            row.setOnMouseClicked(x -> {
                if (x.getClickCount() == 2 && !row.isEmpty()) {
                    clickedBtnDetailsAnzeigen(new ActionEvent());
                }
            });
            return row;
        });

        zeigeSammelkartenInTableView();

    }

    /**
     * On cb nur aktive anzeigen.
     *
     * @param event the event
     */
    @FXML
    void onCbNurAktiveAnzeigen(ActionEvent event) {

        if (!cbNurAktiveAnzeigen.isSelected()) {
            TableColumn<MSammelkarten, Boolean> colMeldungAktivString = new TableColumn<MSammelkarten, Boolean>(
                    "Aktiv");
            colMeldungAktivString.setCellFactory(col -> new CheckBoxTableCell<>());
            colMeldungAktivString.setStyle("-fx-alignment: CENTER");
            colMeldungAktivString.setMinWidth(60.0);
            colMeldungAktivString.setPrefWidth(60.0);
            colMeldungAktivString.setMaxWidth(60.0);
            colMeldungAktivString.setCellValueFactory(cellData -> {
                MSammelkarten cellValue = cellData.getValue();
                BooleanProperty property = new SimpleBooleanProperty();
                property.set(cellValue.getAktiv());

                // Add listener to handler change
                property.addListener((observable, oldValue, newValue) -> {

                    BlMeldungen blMeldungen = new BlMeldungen(false, new DbBundle());

                    blMeldungen.eclMeldung = mapMeldung.get(cellValue.meldungsIdent);
                    blMeldungen.eclMeldung.meldungAktiv = newValue ? 1 : 0;

                    if (blMeldungen.updateMeldung(false) == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                        this.fehlerMeldung(
                                "Geänderte Daten konnten nicht gespeichert werden - von anderem Benutzer geändert!");
                    }
                });
                return property;
            });
            tableSammelkarten.getColumns().add(1, colMeldungAktivString);
        } else {
            tableSammelkarten.getColumns().remove(1);
        }

        zeigeSammelkartenInTableView();
    }

    /**
     * On cb null bestaende.
     *
     * @param event the event
     */
    @FXML
    void onCbNullBestaende(ActionEvent event) {
        zeigeSammelkartenInTableView();
    }

    /**
     * Clicked zuordnen EK alle.
     *
     * @param event the event
     */
    @FXML
    void clickedZuordnenEKAlle(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Allen aktiven Sammelkarten wird aus dem Sammelkarten-Nummernkreis automatisch"
                        + " eine Eintrittskarte zugeordnet. Achtung, dauert etwas!",
                "Weiter", "Abbrechen");
        if (!brc) {
            return;
        }

        DbBundle lDbBundle = new DbBundle();
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, lDbBundle);
        int rc = blSammelkarten.neueEKFuerAlleSammelkarten();
        if (rc == -1) {
            this.fehlerMeldung(
                    "Fehler bei Ausführung - bitte Eintrittskarten bei einzelnen Sammelkarten kontrollieren");
        }
        zeigeSammelkartenInTableView();
    }

    /**
     * *** Aufruf neuer Masken **.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnBearbeitungKopfweisungen(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }

        CtrlSammelkartenKopfWeisungen controllerFenster = new CtrlSammelkartenKopfWeisungen();
        controllerFenster.init(selectedMeldeIdent);

        Stage newStage = new Stage();
        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenKopfWeisungen.fxml", 1500, 760,
                "Sammelkarten Kopfweisungen", true);
        zeigeSammelkartenInTableView();

    }

    /**
     * Clicked btn sammelkartendaten aendern.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnSammelkartendatenAendern(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }

        rufeDetailsAuf(2, "Sammelkarten Ändern", selectedMeldeIdent, true);

    }

    /**
     * Clicked btn vertreter eintrittskarten.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnVertreterEintrittskarten(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }

        rufeDetailsAuf(3, "Sammelkarten Vertreter/Eintrittskarte zuordnen", selectedMeldeIdent, true);

    }

    @FXML
    void clickedBtnInaktiveWeisungenAktivieren(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }

        rufeDetailsAuf(6, "Inaktive Weisungen aktivieren", selectedMeldeIdent, true);

    }

    /**
     * Clicked btn details anzeigen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnDetailsAnzeigen(ActionEvent event) {
        int selectedMeldeIdent = liefereSelektierteSammelkarte();
        if (selectedMeldeIdent == -1) {
            return;
        }
        rufeDetailsAuf(4, "Sammelkarten Details anzeigen", selectedMeldeIdent, false);
    }

    /**
     * Clicked btn neue sammelkarte anlegen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnNeueSammelkarteAnlegen(ActionEvent event) {
        rufeDetailsAuf(1, "Sammelkarten Neuanlage", 0, true);
    }

    /**
     * On btn sammelkarten neu standard.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenNeuStandard(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Achtung - es werden alle (noch nicht angelegten) Standard-Sammelkarten angelegt!", "Fortsetzen",
                "Abbrechen");
        if (bRc == false) {
            return;
        }
        DbBundle lDbBundle = new DbBundle();
        StubMandantAnlegen stubMandantAnlegen = new StubMandantAnlegen(false, lDbBundle);
        stubMandantAnlegen.sammelkartenSandardAnlegen();
        zeigeSammelkartenInTableView();

    }

    /**
     * On btn sammelkarten neu standard ergaenzen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenNeuStandardErgaenzen(ActionEvent event) {
        /*
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Achtung - es wird (zusätzlich) die Sammelkarte für CITY Proxymity angelegt", "Fortsetzen", "Abbrechen");
        if (bRc == false) {
            return;
        }
        DbBundle lDbBundle = new DbBundle();
        StubMandantAnlegen stubMandantAnlegen = new StubMandantAnlegen(false, lDbBundle);
        stubMandantAnlegen.sammelkartenSandardAnlegenErgaenzen();
        zeigeSammelkartenInTableView();
        */
    }

    /**
     * funktion: 1=Neue Sammelkarte anlegen 2=Sammelkartendaten ändern
     * 3=Vertreter/Eintrittskarte zuordnen / ändern etc. 4=Details anzeigen
     * 5=Stapel-Umbuchung
     * 6=Inaktive Weisungen aktivieren
     * 
     * pRefresh=true: Übersichtsanzeige wird nach Rückkehr wieder aufgebaut
     *
     * @param pFunktion           the funktion
     * @param pHeaderText         the header text
     * @param pSelectedMeldeIdent the selected melde ident
     * @param pRefresh            the refresh
     */
    private void rufeDetailsAuf(int pFunktion, String pHeaderText, int pSelectedMeldeIdent, boolean pRefresh) {

        Stage newStage = new Stage();
        CtrlSammelkartenDetails controllerFenster = new CtrlSammelkartenDetails();
        controllerFenster.init(newStage, pSelectedMeldeIdent, pFunktion);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenDetails.fxml", 1500, 760, pHeaderText,
                true);

        if (pRefresh) {
            zeigeSammelkartenInTableView();
        }

    }

    /** Bereitet Sammelkarten-Übersicht auf. dbBundle muß geöffnet sein */
    private void zeigeSammelkartenInTableView() {

        /** Daten einlesen */
        int anzahlSammelkarten = ctrlSammelkartenZeigeListe.holeSammelkartenDaten(cbNurAktiveAnzeigen.isSelected(), 0);

        if (anzahlSammelkarten == 0) {
            if (cbNurAktiveAnzeigen.isSelected()) {
                fehlerMeldung("Keine aktiven Sammelkarten vorhanden!");
            } else {
                fehlerMeldung("Keine Sammelkarten vorhanden!");
            }
        } else {

            for (EclMeldung x : ctrlSammelkartenZeigeListe.rcSammelMeldung) {
                mapMeldung.put(x.meldungsIdent, x);
            }

            ObservableList<MSammelkarten> listSammelkarten = ctrlSammelkartenZeigeListe.rcListSammelkarten;

            List<MSammelkarten> tmpList = listSammelkarten.stream().filter(
                    e -> ((e.getStueckAktien() > 0 || !e.getSkBuchbarHVString().isBlank()) && cbNullBestaende.isSelected()) || !cbNullBestaende.isSelected())
                    .collect(Collectors.toList());

            tableSammelkarten.getItems().setAll(tmpList);
        }
    }

    /**
     * Prüfen, ob eine Sammelkarte selektiert wurde. -1 => keine Selektion, dann
     * auch gleich Fehlermeldung. sonst meldeIdent
     *
     * @return the int
     */
    private int liefereSelektierteSammelkarte() {

        MSammelkarten selected = this.tableSammelkarten.getSelectionModel().getSelectedItem();

        if (selected == null) {
            this.fehlerMeldung("Bitte erst Sammelkarte durch Anklicken auswählen!");
            return (-1);
        }

        return selected.getMeldungsIdent();
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }
}
