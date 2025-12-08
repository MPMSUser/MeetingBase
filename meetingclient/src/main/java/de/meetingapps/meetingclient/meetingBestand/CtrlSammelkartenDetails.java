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
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstFarben;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkartenDetails.
 */
public class CtrlSammelkartenDetails extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn umbuchen. */
    @FXML
    private Button btnUmbuchen;

    @FXML
    private Button btnAktivieren;

    /** The tab pane gesamt. */
    @FXML
    private TabPane tabPaneGesamt;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The tab details. */
    @FXML
    private Tab tabDetails;

    /** The tf ident. */
    @FXML
    private TextField tfIdent;

    /** The cb aktiv. */
    @FXML
    private CheckBox cbAktiv;

    /** The tf bezeichnung. */
    @FXML
    private TextField tfBezeichnung;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf kommentar. */
    @FXML
    private TextField tfKommentar;

    /** The tf gattung lang. */
    @FXML
    private TextField tfGattungLang;

    /** The cb KIAV. */
    @FXML
    private RadioButton cbKIAV;

    /** The tg sammelkartenart. */
    @FXML
    private ToggleGroup tgSammelkartenart;

    /** The cb SRV. */
    @FXML
    private RadioButton cbSRV;

    /** The cb orga. */
    @FXML
    private RadioButton cbOrga;

    /** The cb briefwahl. */
    @FXML
    private RadioButton cbBriefwahl;

    /** The cb dauervollmacht. */
    @FXML
    private RadioButton cbDauervollmacht;

    /** The tf gruppe. */
    @FXML
    private TextField tfGruppe;

    /** The cb ohne weisung. */
    @FXML
    private CheckBox cbOhneWeisung;

    /** The cb dedizierte weisung. */
    @FXML
    private CheckBox cbDedizierteWeisung;

    /** The cb im internet. */
    @FXML
    private CheckBox cbImInternet;

    /** The cb im papier. */
    @FXML
    private CheckBox cbImPapier;

    /** The cb verlassen HV. */
    @FXML
    private CheckBox cbVerlassenHV;

    /** The cb vertreter anbieten. */
    @FXML
    private CheckBox cbVertreterAnbieten;

    /** The tg weitergabe. */
    @FXML
    private ToggleGroup tgWeitergabe;

    /** The cb weitergabe in gesamtsumme. */
    @FXML
    private RadioButton cbWeitergabeInGesamtsumme;

    /** The cb weitergabe je sammelkarten summe. */
    @FXML
    private RadioButton cbWeitergabeJeSammelkartenSumme;

    /** The cb weitergabe nicht erlaubt. */
    @FXML
    private RadioButton cbWeitergabeNichtErlaubt;

    /** The cb weitergabe uneingeschraenkt. */
    @FXML
    private RadioButton cbWeitergabeUneingeschraenkt;

    /** The tg offenlegung. */
    @FXML
    private ToggleGroup tgOffenlegung;

    /** The cb mit offenlegung. */
    @FXML
    private RadioButton cbMitOffenlegung;

    /** The cb ohne offenlegung. */
    @FXML
    private RadioButton cbOhneOffenlegung;

    /** The cb offenlegung wie parameter. */
    @FXML
    private RadioButton cbOffenlegungWieParameter;

    /** The tab vertreter eks. */
    @FXML
    private Tab tabVertreterEks;

    /** The pn vertreter. */
    @FXML
    private ScrollPane pnVertreter;

    /** The btn vertreter. */
    @FXML
    private Button btnVertreter;

    /** The btn neue eintrittskarte. */
    @FXML
    private Button btnNeueEintrittskarte;

    /** The pn eintrittskarten. */
    @FXML
    private ScrollPane pnEintrittskarten;

    /** The pn stimmkarten. */
    @FXML
    private ScrollPane pnStimmkarten;

    /** The tab weisungssummen. */
    @FXML
    private Tab tabWeisungssummen;

    /** The scpn weisungen. */
    @FXML
    private ScrollPane scpnWeisungen;

    /** The tab aktiv. */
    @FXML
    private Tab tabAktiv;

    /** The scpn meldungen aktiv. */
    @FXML
    private ScrollPane scpnMeldungenAktiv;

    /** The tab inaktiv. */
    @FXML
    private Tab tabInaktiv;

    /** The scpn meldungen inaktiv. */
    @FXML
    private ScrollPane scpnMeldungenInaktiv;

    /** The tab widerrufen. */
    @FXML
    private Tab tabWiderrufen;

    /** The scpn meldungen widerrufen. */
    @FXML
    private ScrollPane scpnMeldungenWiderrufen;

    /** The tag geaendert. */
    @FXML
    private Tab tagGeaendert;

    /** The scpn meldungen geaendert. */
    @FXML
    private ScrollPane scpnMeldungenGeaendert;

    /** The cb gattung nr. */
    @FXML
    private ComboBox<String> cbGattungNr;

    /** The cb gattung kuerzel. */
    @FXML
    private ComboBox<String> cbGattungKuerzel;

    /** The btn SRV. */
    @FXML
    private Button btnSRV;

    /** Ab hier individuell. */
    TableView<MSammelkarten> tableSammelkarten = null;

    /** The list sammelkarten. */
    ObservableList<MSammelkarten> listSammelkarten = null;

    /** The table aktionaere aktiv. */
    TableView<EclMeldungMitAktionaersWeisung> tableAktionaereAktiv = null;

    TableView<EclMeldungMitAktionaersWeisung> tableAktionaereInaktiv = null;

    /** The db bundle. */
    DbBundle dbBundle = null;

    /** The aktuelle sammel ident. */
    private int aktuelleSammelIdent = 0;

    /** The aktuelle sammel meldung. */
    private EclMeldung aktuelleSammelMeldung = null;

    /** The aktuelle M sammelkarte. */
    private MSammelkarten aktuelleMSammelkarte = null;

    /** The aktuelle gattung. */
    private int aktuelleGattung = 0;

    /** The zutrittskarten liste. */
    private EclZutrittskarten[] zutrittskartenListe = null;

    /** The btn stornieren zutrittskarte. */
    private Button[] btnStornierenZutrittskarte = null;

    /** The btn drucken zutrittskarte. */
    private Button[] btnDruckenZutrittskarte = null;

    /** The stimmkarten liste. */
    private EclStimmkarten[] stimmkartenListe = null;

    /** The willens erkl vollmachten an dritte. */
    private EclWillensErklVollmachtenAnDritte[] willensErklVollmachtenAnDritte = null;

    /** The btn stornieren vollmacht. */
    private Button[] btnStornierenVollmacht = null;

    /** The weisungen sammelkopf. */
    private EclWeisungMeldung weisungenSammelkopf = null;

    /** The aktionaers summen. */
    private EclWeisungMeldung aktionaersSummen = null;

    /** The aktionaere aktiv. */
    private List<EclMeldungMitAktionaersWeisung> aktionaereAktiv = null;

    /** The aktionaere inaktiv. */
    private List<EclMeldungMitAktionaersWeisung> aktionaereInaktiv = null;

    /** The aktionaere widerrufen. */
    private List<EclMeldungMitAktionaersWeisung> aktionaereWiderrufen = null;

    /** The aktionaere geaendert. */
    private List<EclMeldungMitAktionaersWeisung> aktionaereGeaendert = null;

    /** The list aktionaere aktiv. */
    private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereAktiv = null;

    /** The list aktionaere inaktiv. */
    private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereInaktiv = null;

    /** The list aktionaere widerrufen. */
    private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereWiderrufen = null;

    /** The list aktionaere geaendert. */
    private ObservableList<EclMeldungMitAktionaersWeisung> listAktionaereGeaendert = null;

    /**
     * Steht normalerweise auf true; sobald in einem der Gattungsfelder ein neuer
     * Wert ausgewählt wurde, und der entsprechende Listener zum ändern der anderen
     * Gattungsfelder getriggert wurde, wird das auf false gesetzt, damit kein
     * "Endlos-Gegenseitiger-Update" passiert; anschließend wieder auf true.
     */
    private boolean gattungUpdaten = false;

    /** The bl abstimmungen weisungen erfassen. */
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert scpnUebersicht != null
                : "fx:id=\"scpnUebersicht\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabDetails != null
                : "fx:id=\"tabDetails\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfIdent != null : "fx:id=\"tfIdent\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbAktiv != null : "fx:id=\"cbAktiv\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfBezeichnung != null
                : "fx:id=\"tfBezeichnung\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfKommentar != null
                : "fx:id=\"tfKommentar\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbGattungNr != null
                : "fx:id=\"cbGattungNr\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbGattungKuerzel != null
                : "fx:id=\"cbGattungKuerzel\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfGattungLang != null
                : "fx:id=\"tfGattungLang\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbKIAV != null : "fx:id=\"cbKIAV\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tgSammelkartenart != null
                : "fx:id=\"tgSammelkartenart\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbSRV != null : "fx:id=\"cbSRV\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbOrga != null : "fx:id=\"cbOrga\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbBriefwahl != null
                : "fx:id=\"cbBriefwahl\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbDauervollmacht != null
                : "fx:id=\"cbDauervollmacht\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tfGruppe != null
                : "fx:id=\"tfGruppe\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbOhneWeisung != null
                : "fx:id=\"cbOhneWeisung\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbDedizierteWeisung != null
                : "fx:id=\"cbDedizierteWeisung\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbImInternet != null
                : "fx:id=\"cbImInternet\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbImPapier != null
                : "fx:id=\"cbImPapier\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbVerlassenHV != null
                : "fx:id=\"cbVerlassenHV\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbVertreterAnbieten != null
                : "fx:id=\"cbVertreterAnbieten\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbWeitergabeInGesamtsumme != null
                : "fx:id=\"cbWeitergabeInGesamtsumme\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbWeitergabeJeSammelkartenSumme != null
                : "fx:id=\"cbWeitergabeJeSammelkartenSumme\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbWeitergabeNichtErlaubt != null
                : "fx:id=\"cbWeitergabeNichtErlaubt\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbWeitergabeUneingeschraenkt != null
                : "fx:id=\"cbWeitergabeUneingeschraenkt\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbMitOffenlegung != null
                : "fx:id=\"cbMitOffenlegung\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbOhneOffenlegung != null
                : "fx:id=\"cbOhneOffenlegung\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert cbOffenlegungWieParameter != null
                : "fx:id=\"cbOffenlegungWieParameter\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabVertreterEks != null
                : "fx:id=\"tabVertreterEks\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabWeisungssummen != null
                : "fx:id=\"tabWeisungssummen\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabAktiv != null
                : "fx:id=\"tabAktiv\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabInaktiv != null
                : "fx:id=\"tabInaktiv\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tabWiderrufen != null
                : "fx:id=\"tabWiderrufen\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";
        assert tagGeaendert != null
                : "fx:id=\"tagGeaendert\" was not injected: check your FXML file 'SammelkartenDetails.fxml'.";

        /** Ab hier individuell */

        dbBundle = new DbBundle();

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(false, dbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

        btnUmbuchen.setVisible(false);
        btnAktivieren.setVisible(false);

        fuelleCbGattung();

        if (aktuelleFunktion != 1) {
            int anz = zeigeSammelkartenInTableView();
            if (anz == 0) {
                /* Darf nicht vorkommen! */
                fehlerMeldung("Sammelkarte nicht vorhanden!");
                eigeneStage.hide();
                return;
            }
            fuelleSammelkartenZusatzdaten();

            fuelleTabDetails();
            fuelleTabVertreterEK();
            fuelleTabWeisungssummen();
            fuelleTabsAktionaere();

            sperreTabDetails();
            if (aktuelleFunktion != 3) {
                sperreTabVertreterEK();
            }
            sperreTabWeisungssummen();
            sperreTabsAktionaere();
            if (aktuelleFunktion == 2) {
                tabVertreterEks.setDisable(true);
                tabWeisungssummen.setDisable(true);
                tabAktiv.setDisable(true);
                tabInaktiv.setDisable(true);
                tabWiderrufen.setDisable(true);
                tagGeaendert.setDisable(true);
            }
            if (aktuelleFunktion == 3) {
                SingleSelectionModel<Tab> selectionModel = tabPaneGesamt.getSelectionModel();
                selectionModel.select(tabVertreterEks);
            }
            if (aktuelleFunktion == 5) {
                tabDetails.setDisable(true);
                tabVertreterEks.setDisable(true);
                tabWeisungssummen.setDisable(true);
                tabInaktiv.setDisable(true);
                tabWiderrufen.setDisable(true);
                tagGeaendert.setDisable(true);

                SingleSelectionModel<Tab> selectionModel = tabPaneGesamt.getSelectionModel();
                selectionModel.select(tabAktiv);

                btnUmbuchen.setVisible(true);

            }
            if (aktuelleFunktion == 6) {
                tabDetails.setDisable(true);
                tabVertreterEks.setDisable(true);
                tabWeisungssummen.setDisable(true);
                tabAktiv.setDisable(true);
                tabInaktiv.setDisable(false);
                tabWiderrufen.setDisable(true);
                tagGeaendert.setDisable(true);

                SingleSelectionModel<Tab> selectionModel = tabPaneGesamt.getSelectionModel();
                selectionModel.select(tabInaktiv);

                btnAktivieren.setVisible(true);

            }
        } else {
            /* Neuanlage: Felder leeren, Sammelkarte vorbelegen */
            aktuelleSammelIdent = 0;
            aktuelleSammelMeldung = new EclMeldung();
            aktuelleMSammelkarte = new MSammelkarten(aktuelleSammelMeldung, dbBundle);
            bereiteVorNeueSammelkarte();
            fuelleTabDetails();
            tabVertreterEks.setDisable(true);
            tabWeisungssummen.setDisable(true);
            tabAktiv.setDisable(true);
            tabInaktiv.setDisable(true);
            tabWiderrufen.setDisable(true);
            tagGeaendert.setDisable(true);
            btnSRV.setDisable(false);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tfBezeichnung.requestFocus();
                }
            });

        }

        setzeListener();

    }

    /**
     * Fuelle cb gattung.
     */
    private void fuelleCbGattung() {
        cbGattungNr.getItems().clear();
        cbGattungKuerzel.getItems().clear();
        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                String hString = "";
                hString = Integer.toString(i);
                cbGattungNr.getItems().addAll(hString);
                cbGattungKuerzel.getItems().addAll(ParamS.param.paramBasis.getGattungBezeichnungKurz(i));
            }
        }
    }

    /**
     * Zeigt aktuelleSammelkarte in Header-Übersicht an. dbBundle muß geöffnet sein
     *
     * @return the int
     */
    private int zeigeSammelkartenInTableView() {

        boolean mitFeldAktiv = false;

        CtrlSammelkartenUebergreifend ctrlSammelkartenZeigeListe = new CtrlSammelkartenUebergreifend();

        /** Table-View vorbereiten */
        tableSammelkarten = ctrlSammelkartenZeigeListe.vorbereitenTableViewSammelkarten();
        tableSammelkarten.prefHeightProperty().bind(scpnUebersicht.heightProperty());
        tableSammelkarten.prefWidthProperty().bind(scpnUebersicht.widthProperty());

        /** Daten einlesen */
        int anzahlSammelkarten = ctrlSammelkartenZeigeListe.holeSammelkartenDaten(mitFeldAktiv, aktuelleSammelIdent);

        if (anzahlSammelkarten == 0) {
            return 0;
        } else {
            listSammelkarten = ctrlSammelkartenZeigeListe.rcListSammelkarten;
            tableSammelkarten.setItems(listSammelkarten);
            scpnUebersicht.setContent(tableSammelkarten);

            aktuelleSammelMeldung = ctrlSammelkartenZeigeListe.rcSammelMeldung[0];
            aktuelleMSammelkarte = listSammelkarten.get(0);
            zutrittskartenListe = ctrlSammelkartenZeigeListe.rcZutrittskartenArray[0];
            stimmkartenListe = ctrlSammelkartenZeigeListe.rcStimmkartenArray[0];
            willensErklVollmachtenAnDritte = ctrlSammelkartenZeigeListe.rcWillensErklVollmachtenAnDritteArray[0];
        }
        return 1;

    }

    /**
     * Füllt die Tab Details aus aktuelleSammelMeldung.
     */
    private void fuelleTabDetails() {

        if (aktuelleSammelMeldung.meldungsIdent != 0) {
            tfIdent.setText(Integer.toString(aktuelleSammelMeldung.meldungsIdent));
        } else {
            tfIdent.setText("(neu)");
        }
        tfIdent.setEditable(false);

        if (aktuelleSammelMeldung.meldungAktiv == 1) {
            cbAktiv.setSelected(true);
        }

        tfBezeichnung.setText(aktuelleSammelMeldung.name);
        tfOrt.setText(aktuelleSammelMeldung.ort);
        tfKommentar.setText(aktuelleSammelMeldung.zusatzfeld2);
        cbGattungNr.setValue(Integer.toString(aktuelleSammelMeldung.gattung));
        cbGattungKuerzel.setValue(ParamS.param.paramBasis.getGattungBezeichnungKurz(aktuelleSammelMeldung.gattung));

        tfGattungLang.setText(ParamS.param.paramBasis.getGattungBezeichnung(aktuelleSammelMeldung.gattung));
        tfGattungLang.setEditable(false);

        switch (aktuelleSammelMeldung.skIst) {
        case 1:/* KIAV */
            cbKIAV.setSelected(true);
            break;
        case 2:/* SRV */
            cbSRV.setSelected(true);
            break;
        case 3:/* Orga */
            cbOrga.setSelected(true);
            break;
        case 4:/* Briefwahl */
            cbBriefwahl.setSelected(true);
            break;
        case 5:/* Dauer */
            cbDauervollmacht.setSelected(true);
            break;
        default:
            break;
        }

        tfGruppe.setText(Integer.toString(aktuelleSammelMeldung.gruppe));

        if ((aktuelleSammelMeldung.skWeisungsartZulaessig & 2) == 2) {
            cbOhneWeisung.setSelected(true);
        } else {
            cbOhneWeisung.setSelected(false);
        }
        if ((aktuelleSammelMeldung.skWeisungsartZulaessig & 4) == 4) {
            cbDedizierteWeisung.setSelected(true);
        } else {
            cbDedizierteWeisung.setSelected(false);
        }

        if (aktuelleSammelMeldung.skBuchbarInternet == 1) {
            cbImInternet.setSelected(true);
        } else {
            cbImInternet.setSelected(false);
        }
        if (aktuelleSammelMeldung.skBuchbarPapier == 1) {
            cbImPapier.setSelected(true);
        } else {
            cbImPapier.setSelected(false);
        }
        if (aktuelleSammelMeldung.skBuchbarHV == 1) {
            cbVerlassenHV.setSelected(true);
        } else {
            cbVerlassenHV.setSelected(false);
        }

        if (aktuelleSammelMeldung.skBuchbarVollmachtDritteHV == 1) {
            cbVertreterAnbieten.setSelected(true);
        } else {
            cbVertreterAnbieten.setSelected(false);
        }

        switch (aktuelleSammelMeldung.zusatzfeld1) {
        case "":
            cbWeitergabeUneingeschraenkt.setSelected(true);
            break;
        case "1":
            cbWeitergabeInGesamtsumme.setSelected(true);
            break;
        case "2":
            cbWeitergabeJeSammelkartenSumme.setSelected(true);
            break;
        case "3":
            cbWeitergabeNichtErlaubt.setSelected(true);
            break;
        default:
            break;
        }

        switch (aktuelleSammelMeldung.skOffenlegung) {
        case 0:/* wie Parameter */
            cbOffenlegungWieParameter.setSelected(true);
            break;
        case -1:/* keine Offenlegung */
            cbOhneOffenlegung.setSelected(true);
            break;
        case 1:/* Offenlegung */
            cbMitOffenlegung.setSelected(true);
            break;
        default:
        }

    }

    /**
     * Sperre tab details.
     */
    private void sperreTabDetails() {

        if (aktuelleFunktion != 2 || sammelWarPraesent() || sammelHatAktionaere()) {
            cbAktiv.setDisable(true);
            cbAktiv.setStyle("-fx-opacity: 1");
        }

        if (aktuelleFunktion != 2) {
            tfBezeichnung.setEditable(false);
            tfOrt.setEditable(false);

            tfKommentar.setEditable(false);
        }

        if (aktuelleFunktion != 2 || sammelWarPraesent() || sammelHatAktionaere()) {
            ComboBoxZusatz.sperre(cbGattungNr);
            ComboBoxZusatz.sperre(cbGattungKuerzel);
        }

        if (aktuelleFunktion != 2 || sammelWarPraesent() || sammelHatAktionaere()) {
            cbKIAV.setDisable(true);
            cbKIAV.setStyle("-fx-opacity: 1");

            cbSRV.setDisable(true);
            cbSRV.setStyle("-fx-opacity: 1");

            cbOrga.setDisable(true);
            cbOrga.setStyle("-fx-opacity: 1");

            cbBriefwahl.setDisable(true);
            cbBriefwahl.setStyle("-fx-opacity: 1");

            cbDauervollmacht.setDisable(true);
            cbDauervollmacht.setStyle("-fx-opacity: 1");
        }

        if (aktuelleFunktion != 2) {
            tfGruppe.setEditable(false);
        }

        if (aktuelleFunktion != 2 || sammelWarPraesent() || sammelHatAktionaere()) {
            cbOhneWeisung.setDisable(true);
            cbOhneWeisung.setStyle("-fx-opacity: 1");

            cbDedizierteWeisung.setDisable(true);
            cbDedizierteWeisung.setStyle("-fx-opacity: 1");
        }

        if (aktuelleFunktion != 2) {

            cbImInternet.setDisable(true);
            cbImInternet.setStyle("-fx-opacity: 1");

            cbImPapier.setDisable(true);
            cbImPapier.setStyle("-fx-opacity: 1");

            cbVerlassenHV.setDisable(true);
            cbVerlassenHV.setStyle("-fx-opacity: 1");

            cbVertreterAnbieten.setDisable(true);
            cbVertreterAnbieten.setStyle("-fx-opacity: 1");

            cbWeitergabeUneingeschraenkt.setDisable(true);
            cbWeitergabeUneingeschraenkt.setStyle("-fx-opacity: 1");

            cbWeitergabeInGesamtsumme.setDisable(true);
            cbWeitergabeInGesamtsumme.setStyle("-fx-opacity: 1");

            cbWeitergabeJeSammelkartenSumme.setDisable(true);
            cbWeitergabeJeSammelkartenSumme.setStyle("-fx-opacity: 1");

            cbWeitergabeNichtErlaubt.setDisable(true);
            cbWeitergabeNichtErlaubt.setStyle("-fx-opacity: 1");
        }

        if (aktuelleFunktion != 2 || sammelWarPraesent()) {

            cbOffenlegungWieParameter.setDisable(true);
            cbOffenlegungWieParameter.setStyle("-fx-opacity: 1");

            cbOhneOffenlegung.setDisable(true);
            cbOhneOffenlegung.setStyle("-fx-opacity: 1");

            cbMitOffenlegung.setDisable(true);
            cbMitOffenlegung.setStyle("-fx-opacity: 1");
        }
    }

    /**
     * Fuelle tab vertreter EK.
     */
    private void fuelleTabVertreterEK() {
        /* Vertreter-Pane */
        MeetingGridPane lVertreterGridPane = new MeetingGridPane();
        if (willensErklVollmachtenAnDritte != null) {
            int anz = willensErklVollmachtenAnDritte.length;
            btnStornierenVollmacht = new Button[anz];
            for (int i = 0; i < anz; i++) {
                EclWillensErklVollmachtenAnDritte lWillensErklVollmachtenAnDritte = willensErklVollmachtenAnDritte[i];

                btnStornierenVollmacht[i] = new Button("Stornieren");
                btnStornierenVollmacht[i].setOnAction(e -> {
                    clickedStornierenVollmacht(e);
                });

                if (lWillensErklVollmachtenAnDritte.wurdeStorniert) {
                    Label lStorniert = new Label("storniert");
                    lVertreterGridPane.addMeeting(lStorniert, 0, i + 1);
                } else {
                    lVertreterGridPane.addMeeting(btnStornierenVollmacht[i], 0, i + 1);
                }

                Label vertreterName = new Label();
                vertreterName.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.name);
                lVertreterGridPane.addMeeting(vertreterName, 1, i + 1);

                Label vertreterVorName = new Label();
                vertreterVorName.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.vorname);
                lVertreterGridPane.addMeeting(vertreterVorName, 2, i + 1);

                Label vertreterOrt = new Label();
                vertreterOrt.setText(lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ort);
                lVertreterGridPane.addMeeting(vertreterOrt, 3, i + 1);
            }

            String[] ueberschrift = { "", "Name", "Vorname", "Ort" };
            lVertreterGridPane.setzeUeberschrift(ueberschrift, pnVertreter);
        }

        /* Eintrittskarten-Pane */
        MeetingGridPane lEintrittskartenGridPane = new MeetingGridPane();
        if (zutrittskartenListe != null) {
            int anz = zutrittskartenListe.length;
            btnStornierenZutrittskarte = new Button[anz];
            btnDruckenZutrittskarte = new Button[anz];
            for (int i = 0; i < anz; i++) {
                EclZutrittskarten lZutrittskarten = zutrittskartenListe[i];

                btnStornierenZutrittskarte[i] = new Button("Sperren");
                btnStornierenZutrittskarte[i].setOnAction(e -> {
                    clickedSperrenEK(e);
                });

                btnDruckenZutrittskarte[i] = new Button("Drucken");
                btnDruckenZutrittskarte[i].setOnAction(e -> {
                    clickedDruckenEK(e);
                });
                if (lZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                    Label lStorniert = new Label("gesperrt");
                    lEintrittskartenGridPane.addMeeting(lStorniert, 0, i + 1);
                } else {
                    lEintrittskartenGridPane.addMeeting(btnStornierenZutrittskarte[i], 0, i + 1);
                    lEintrittskartenGridPane.addMeeting(btnDruckenZutrittskarte[i], 1, i + 1);
                }

                Label lZutrittsIdent = new Label();
                lZutrittsIdent.setText(lZutrittskarten.zutrittsIdent);
                lEintrittskartenGridPane.addMeeting(lZutrittsIdent, 2, i + 1);

                Label lZutrittsIdentNeben = new Label();
                lZutrittsIdentNeben.setText(lZutrittskarten.zutrittsIdentNeben);
                lEintrittskartenGridPane.addMeeting(lZutrittsIdentNeben, 3, i + 1);
            }

        }

        pnEintrittskarten.setContent(lEintrittskartenGridPane);

        /* Stimmkarten-Pane */
        MeetingGridPane lStimmkartenGridPane = new MeetingGridPane();
        if (stimmkartenListe != null) {
            int anz = stimmkartenListe.length;
            for (int i = 0; i < anz; i++) {
                EclStimmkarten lStimmkarten = stimmkartenListe[i];

                if (lStimmkarten.stimmkarteWurdeGesperrt()) {
                    Label lStorniert = new Label("gesperrt");
                    lStimmkartenGridPane.addMeeting(lStorniert, 0, i + 1);
                }

                Label lStimmkarte = new Label();
                lStimmkarte.setText(lStimmkarten.stimmkarte);
                lStimmkartenGridPane.addMeeting(lStimmkarte, 1, i + 1);
            }
        }

        pnStimmkarten.setContent(lStimmkartenGridPane);
    }

    /**
     * Sperre tab vertreter EK.
     */
    private void sperreTabVertreterEK() {
        /* Vertreter-Pane */
        btnVertreter.setDisable(true);
        if (willensErklVollmachtenAnDritte != null) {
            int anz = willensErklVollmachtenAnDritte.length;
            for (int i = 0; i < anz; i++) {
                btnStornierenVollmacht[i].setDisable(true);
            }
        }

        /* Eintrittskarten-Pane */
        btnNeueEintrittskarte.setDisable(true);
        if (zutrittskartenListe != null) {
            int anz = zutrittskartenListe.length;
            for (int i = 0; i < anz; i++) {
                btnStornierenZutrittskarte[i].setDisable(true);
                btnDruckenZutrittskarte[i].setDisable(true);
            }
        }

        /* Stimmkarten-Pane - derzeit nichts zum Deaktivieren */

    }

    /** The fuelle tab weisungssummen zeile. */
    int fuelleTabWeisungssummenZeile = 1;

    /**
     * Fuelle tab weisungssummen.
     */
    private void fuelleTabWeisungssummen() {
        aktuelleGattung = aktuelleSammelMeldung.liefereGattung();

        MeetingGridPane lWeisungenGridPane = new MeetingGridPane();
        fuelleTabWeisungssummenZeile = 1;

        fuelleTabWeisungssummenZeigeBereich(lWeisungenGridPane, 1);
        fuelleTabWeisungssummenZeigeBereich(lWeisungenGridPane, 2);

        String[] ueberschrift = new String[10];
        int spalte = 2;
        for (int i1 = 0; i1 <= 9; i1++) {
            if (i1 != KonstStimmart.splitLiegtVor && i1 != KonstStimmart.stimmartDepcrecated) {
                ueberschrift[spalte] = KonstStimmart.getText(i1);
                spalte++;
            }
        }
        lWeisungenGridPane.setzeUeberschrift(ueberschrift, scpnWeisungen);

    }

    /**
     * pArt= 1 => "Normale" Agenda 2 => Gegenanträge.
     *
     * @param lWeisungenGridPane the l weisungen grid pane
     * @param pArt               the art
     */
    private void fuelleTabWeisungssummenZeigeBereich(MeetingGridPane lWeisungenGridPane, int pArt) {

        int anzAgenda = 0;
        if (pArt == 1) {
            anzAgenda = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        } else {
            anzAgenda = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);
        }

        boolean agendaGegenantraegeGetrennt = ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;

        if (agendaGegenantraegeGetrennt) {
            Label zUberschrift = new Label();
            if (pArt == 1) {
                zUberschrift.setText("Normale Agenda");
            } else {
                zUberschrift.setText("Gegenanträge");
            }
            lWeisungenGridPane.addMeeting(zUberschrift, 1, fuelleTabWeisungssummenZeile);
            fuelleTabWeisungssummenZeile++;
        }

        for (int i = 0; i < anzAgenda; i++) {
            EclAbstimmung lAbstimmung = null;
            if (pArt == 1) {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][i];
            } else {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][i];
            }

            Label lblAgendaTOP = new Label();
            lblAgendaTOP.setText(lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey);
            lWeisungenGridPane.addMeeting(lblAgendaTOP, 0, fuelleTabWeisungssummenZeile);
            GridPane.setValignment(lblAgendaTOP, VPos.TOP);

            Label lblAgendaText = new Label();
            lblAgendaText.setText(lAbstimmung.kurzBezeichnung);
            lblAgendaText.setWrapText(true);
            lblAgendaText.setMaxWidth(400);
            lWeisungenGridPane.addMeeting(lblAgendaText, 1, fuelleTabWeisungssummenZeile);
            GridPane.setValignment(lblAgendaText, VPos.TOP);

            if (!lAbstimmung.liefereIstUeberschift()) {
                int abstimmungsPosition = lAbstimmung.identWeisungssatz;

                /* Erst ermitteln, ob Fehler in gesamter Summe - denn dann ganze Zeile rot! */
                boolean fehlerSummeGesamt = false; // Quersumme der Split-Weisungen dieses TOPs stimmen nicht mit Anzahl
                                                   // Stimmen in Sammelkarte überein
                long summeGesamt = 0;
                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor) {
                        summeGesamt += weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                    }
                }
                if (summeGesamt != aktuelleSammelMeldung.stimmen) {
                    fehlerSummeGesamt = true;
                }

                int spalte = 2;
                for (int i1 = 0; i1 <= 9; i1++) {
                    if (i1 != KonstStimmart.splitLiegtVor && i1 != KonstStimmart.stimmartDepcrecated) {
                        long wertSammelkarte = weisungenSammelkopf.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        long wertAktionaere = aktionaersSummen.weisungMeldungSplit.abgabe[abstimmungsPosition][i1];
                        boolean fehlerTOP = false;
                        if (wertSammelkarte != wertAktionaere) {
                            fehlerTOP = true;
                        }
                        Label lblWert = new Label(CaString.toStringDE(wertSammelkarte));
                        if (fehlerTOP) {
                            lblWert.setTextFill(Color.web(KonstFarben.rot));
                        }
                        lWeisungenGridPane.addMeeting(lblWert, spalte, fuelleTabWeisungssummenZeile);
                        spalte++;
                    }
                }
                if (fehlerSummeGesamt) {
                    Label lblFehler = new Label("Fehler Quersumme=" + CaString.toStringDE(summeGesamt));
                    lblFehler.setTextFill(Color.web(KonstFarben.rot));
                    lWeisungenGridPane.addMeeting(lblFehler, spalte, fuelleTabWeisungssummenZeile);
                }

            }
            fuelleTabWeisungssummenZeile++;

        }

    }

    /**
     * Sperre tab weisungssummen.
     */
    private void sperreTabWeisungssummen() {
        /* Platzhalterfunktion :-) - nichts zu sperren, da nix zu ändern ... */
    }

    /**
     * Fuelle tabs aktionaere.
     */
    private void fuelleTabsAktionaere() {

        listAktionaereAktiv = FXCollections.observableArrayList();
        System.out.println("aktionaereAktiv.size()=" + aktionaereAktiv.size());
        for (int i = 0; i < aktionaereAktiv.size(); i++) {
            aktionaereAktiv.get(i).belegeAbgabeText();
            listAktionaereAktiv.add(aktionaereAktiv.get(i));
        }
        listAktionaereInaktiv = FXCollections.observableArrayList();
        for (int i = 0; i < aktionaereInaktiv.size(); i++) {
            aktionaereInaktiv.get(i).belegeAbgabeText();
            listAktionaereInaktiv.add(aktionaereInaktiv.get(i));
        }
        listAktionaereWiderrufen = FXCollections.observableArrayList();
        for (int i = 0; i < aktionaereWiderrufen.size(); i++) {
            aktionaereWiderrufen.get(i).belegeAbgabeText();
            listAktionaereWiderrufen.add(aktionaereWiderrufen.get(i));
        }
        listAktionaereGeaendert = FXCollections.observableArrayList();
        for (int i = 0; i < aktionaereGeaendert.size(); i++) {
            aktionaereGeaendert.get(i).belegeAbgabeText();
            listAktionaereGeaendert.add(aktionaereGeaendert.get(i));
        }

        fuelleTabsAktionareEinzeln(1, scpnMeldungenAktiv);
        fuelleTabsAktionareEinzeln(2, scpnMeldungenInaktiv);
        fuelleTabsAktionareEinzeln(3, scpnMeldungenWiderrufen);
        fuelleTabsAktionareEinzeln(4, scpnMeldungenGeaendert);
    }

    /**
     * pArt= 1=aktive 2=inaktive 3=widerrufen 4=Geaendert.
     *
     * @param pArt            the art
     * @param pPaneAktionaere the pane aktionaere
     */
    private void fuelleTabsAktionareEinzeln(int pArt, ScrollPane pPaneAktionaere) {

        CtrlSammelkartenUebergreifend ctrlSammelkartenUebergreifend = new CtrlSammelkartenUebergreifend();
        TableView<EclMeldungMitAktionaersWeisung> tableAktionaere = ctrlSammelkartenUebergreifend
                .vorbereitenTableViewAktionaere(blAbstimmungenWeisungenErfassen,
                        aktuelleSammelMeldung.liefereGattung());

        tableAktionaere.setPrefHeight(500);
        tableAktionaere.setPrefWidth(1464);

        pPaneAktionaere.setContent(tableAktionaere);

        switch (pArt) {
        case 1:
            tableAktionaere.setItems(listAktionaereAktiv);
            tableAktionaere.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableAktionaereAktiv = tableAktionaere;
            break;
        case 2:
            tableAktionaere.setItems(listAktionaereInaktiv);
            tableAktionaere.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableAktionaereInaktiv = tableAktionaere;
            break;
        case 3:
            tableAktionaere.setItems(listAktionaereWiderrufen);
            break;
        case 4:
            tableAktionaere.setItems(listAktionaereGeaendert);
            break;
        default:
            break;
        }
    }

    /**
     * Sperre tabs aktionaere.
     */
    private void sperreTabsAktionaere() {
        /* Platzhalterfunktion :-) - nichts zu sperren, da nix zu ändern ... */
    }

    /**
     * ****************************** Eingabereaktionen
     * ******************************.
     *
     * @param event the event
     */

    @FXML
    void onBtnSpeichern(ActionEvent event) {
        /* Prüfen */
        lFehlertext = "";
        pruefeNichtLeer(tfBezeichnung, "Bezeichnung der Sammelkarte");
        pruefeLaenge(tfBezeichnung, "Bezeichnung der Sammelkarte", 80);

        pruefeNichtLeer(tfOrt, "Ort");
        pruefeLaenge(tfOrt, "Ort", 80);

        pruefeLaenge(tfKommentar, "Kommentar", 40);

        if (cbKIAV.isSelected() == false && cbSRV.isSelected() == false && cbOrga.isSelected() == false
                && cbBriefwahl.isSelected() == false && cbDauervollmacht.isSelected() == false) {
            lFehlertext = "Sammelkartenart auswählen!";
        }

        pruefeZahlOderLeer(tfGruppe, "Gruppe");

        if (cbOhneWeisung.isSelected() == false && cbDedizierteWeisung.isSelected() == false) {
            lFehlertext = "Unterstützte Weisungstyp auswählen!";
        }

        if (cbOhneWeisung.isSelected() == true && cbDedizierteWeisung.isSelected() == true) {
            lFehlertext = "Derzeit nur mit oder ohne Weisung zulässig, nicht beides!";
        }

        // if (cbImInternet.isSelected()==false && cbImPapier.isSelected()==false &&
        // cbVerlassenHV.isSelected()==false) {
        // lFehlertext="Verwendung auswählen!";
        // }

        // if (cbOffenlegungWieParameter.isSelected()) {
        // lFehlertext="Offenlegung wie Parameter wird derzeit noch nicht unterstützt!";
        // }

        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }

        if (aktuelleFunktion == 1) {/* Neu-Anlage */

            /* Speichern */
            dbBundle = new DbBundle();

            BlSammelkarten blSammelkarte = new BlSammelkarten(false, dbBundle);
            blSammelkarte.neueSammelkarte();

            blSammelkarte.meldung = aktuelleSammelMeldung;

            blSammelkarte.neueSammelkarteSpeichern();

            eigeneStage.hide();

            return;
        }

        if (aktuelleFunktion == 2) { /* Ändern */
            dbBundle = new DbBundle();
            dbBundle.openAll();
            int rc = dbBundle.dbMeldungen.update(aktuelleSammelMeldung);
            if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                this.fehlerMeldung("Geänderte Daten konnten nicht gespeichert werden - von anderem Benutzer geändert!");
            }
            dbBundle.closeAll();
            eigeneStage.hide();
            return;
        }

        eigeneStage.hide();
    }

    /**
     * Clicked stornieren vollmacht.
     *
     * @param event the event
     */
    @FXML
    void clickedStornierenVollmacht(ActionEvent event) {
        int rc = findeButton(btnStornierenVollmacht, event);
        EclWillensErklVollmachtenAnDritte lWillensErklVollmachtenAnDritte = willensErklVollmachtenAnDritte[rc];

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Vollmacht " + lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.name + " stornieren?", "Ja",
                "Nein");
        if (brc) {
            dbBundle = new DbBundle();
            BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
            blSammelkarten.storniereBestehenderVertreterFuerSammelkarte(aktuelleSammelIdent,
                    lWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ident);

            fuelleSammelkartenZusatzdaten();
            zeigeSammelkartenInTableView();
            fuelleTabVertreterEK();
        }

    }

    /**
     * Clicked sperren EK.
     *
     * @param event the event
     */
    @FXML
    void clickedSperrenEK(ActionEvent event) {
        int rc = findeButton(btnStornierenZutrittskarte, event);
        EclZutrittskarten lZutrittskarte = zutrittskartenListe[rc];

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Eintrittskarte " + lZutrittskarte.zutrittsIdent + " sperren?", "Ja", "Nein");
        if (!brc) {
            return;
        }
        if (aktuelleSammelMeldung.statusWarPraesenz == 1) {
            brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                    "Achtung! Sammelkarte ist oder war bereits präsent! Eintrittskarte dennoch sperren???", "Ja",
                    "Nein");
        }
        if (brc) {
            dbBundle = new DbBundle();
            BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
            blSammelkarten.sperreEKderSammelkarte(aktuelleSammelIdent, lZutrittskarte.zutrittsIdent);

            fuelleSammelkartenZusatzdaten();
            zeigeSammelkartenInTableView();
            fuelleTabVertreterEK();
        }
    }

    /**
     * Clicked drucken EK.
     *
     * @param event the event
     */
    void clickedDruckenEK(ActionEvent event) {
        this.fehlerMeldung(
                "Derzeit noch nicht implementiert. Zum Drucken einer Eintrittskarte für eine Sammelkarte bitte das Modul Servicedesk benutzen!");
    }

    /**
     * On btn SRV.
     *
     * @param event the event
     */
    @FXML
    void onBtnSRV(ActionEvent event) {
        tfBezeichnung.setText("Stimmrechtsvertreter");
        tfOrt.setText(ParamS.eclEmittent.hvOrt != null ? ParamS.eclEmittent.hvOrt : "");
        cbSRV.setSelected(true);
        tfGruppe.setText("9001");
        cbDedizierteWeisung.setSelected(true);
        tfKommentar.requestFocus();
    }

    /**
     * On btn vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnVertreter(ActionEvent event) {
        Stage newStage = new Stage();
        CtrlSammelkartenNeuerVertreter controllerFenster = new CtrlSammelkartenNeuerVertreter();
        controllerFenster.init(newStage, aktuelleSammelMeldung, willensErklVollmachtenAnDritte);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenNeuerVertreter.fxml", 1100, 680,
                "Neuer Vertreter", true);

        fuelleSammelkartenZusatzdaten();
        zeigeSammelkartenInTableView();
        fuelleTabVertreterEK();

    }

    /**
     * On btn neue eintrittskarte.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeueEintrittskarte(ActionEvent event) {
        Stage newStage = new Stage();
        CtrlSammelkartenNeueEK controllerFenster = new CtrlSammelkartenNeueEK();
        controllerFenster.init(newStage, aktuelleSammelMeldung);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenNeueEK.fxml", 920, 340, "Neue Eintrittskarte",
                true);

        fuelleSammelkartenZusatzdaten();
        zeigeSammelkartenInTableView();
        fuelleTabVertreterEK();

    }

    /**
     * Clicked umbuchen.
     *
     * @param event the event
     */
    @FXML
    void clickedUmbuchen(ActionEvent event) {
        ObservableList<Integer> ausgewaehlteAktionaereIndex = null;
        ausgewaehlteAktionaereIndex = this.tableAktionaereAktiv.getSelectionModel().getSelectedIndices();
        int anzahlAusgewaehlteAktionaere = ausgewaehlteAktionaereIndex.size();
        if (anzahlAusgewaehlteAktionaere == 0) {
            this.fehlerMeldung("Bitte umzubuchende Aktionäre auswählen!");
            return;
        }
        int[] ausgewaehlteAktionaereMeldung = new int[anzahlAusgewaehlteAktionaere];
        EclWeisungMeldung[] ausgewaehlteAktionaereMeldungWeisung = new EclWeisungMeldung[anzahlAusgewaehlteAktionaere];
        EclMeldungMitAktionaersWeisung[] ausgewaehlteMeldungMitAktionaersWeisung = new EclMeldungMitAktionaersWeisung[anzahlAusgewaehlteAktionaere];
        for (int i = 0; i < anzahlAusgewaehlteAktionaere; i++) {
            ausgewaehlteMeldungMitAktionaersWeisung[i] = listAktionaereAktiv.get(ausgewaehlteAktionaereIndex.get(i));
            ausgewaehlteAktionaereMeldung[i] = listAktionaereAktiv
                    .get(ausgewaehlteAktionaereIndex.get(i)).meldungsIdent;
            ausgewaehlteAktionaereMeldungWeisung[i] = listAktionaereAktiv
                    .get(ausgewaehlteAktionaereIndex.get(i)).eclWeisungMeldung;
        }

        Stage newStage = new Stage();
        CtrlSammelkartenStapelbuchen controllerFenster = new CtrlSammelkartenStapelbuchen();
        /*
         * Hinweis: die Argumente haben einige Informationen redundant. Wurde gemacht,
         * damit nicht alles nochmal getestet werden muß ...
         */
        controllerFenster.init(newStage, aktuelleSammelMeldung, ausgewaehlteAktionaereMeldung,
                ausgewaehlteAktionaereMeldungWeisung, ausgewaehlteMeldungMitAktionaersWeisung,
                blAbstimmungenWeisungenErfassen);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenStapelbuchen.fxml", 1200, 720,
                "Stapel-Buchung", true);

        fuelleSammelkartenZusatzdaten();
        zeigeSammelkartenInTableView();
        fuelleTabsAktionaere();

    }

    
    @FXML
    void clickedAktivieren(ActionEvent event) {
        ObservableList<Integer> ausgewaehlteAktionaereIndex = null;
        ausgewaehlteAktionaereIndex = this.tableAktionaereInaktiv.getSelectionModel().getSelectedIndices();
        int anzahlAusgewaehlteAktionaere = ausgewaehlteAktionaereIndex.size();
        if (anzahlAusgewaehlteAktionaere == 0) {
            this.fehlerMeldung("Bitte zu aktivierende Aktionäre auswählen!");
            return;
        }
        
        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
        boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage, "Achtung - Einträge werden aktiviert; evtl. andere Einträge der selben Aktionäre werden deaktiviert", "Aktivieren", "Abbrechen");
        if (brc==false) {
            return;
        }
        
        int[] ausgewaehlteAktionaereMeldung = new int[anzahlAusgewaehlteAktionaere];
        EclWeisungMeldung[] ausgewaehlteAktionaereMeldungWeisung = new EclWeisungMeldung[anzahlAusgewaehlteAktionaere];
        EclMeldungMitAktionaersWeisung[] ausgewaehlteMeldungMitAktionaersWeisung = new EclMeldungMitAktionaersWeisung[anzahlAusgewaehlteAktionaere];
        for (int i = 0; i < anzahlAusgewaehlteAktionaere; i++) {
            ausgewaehlteMeldungMitAktionaersWeisung[i] = listAktionaereInaktiv.get(ausgewaehlteAktionaereIndex.get(i));
            ausgewaehlteAktionaereMeldung[i] = listAktionaereInaktiv
                    .get(ausgewaehlteAktionaereIndex.get(i)).meldungsIdent;
            ausgewaehlteAktionaereMeldungWeisung[i] = listAktionaereInaktiv
                    .get(ausgewaehlteAktionaereIndex.get(i)).eclWeisungMeldung;
            System.out.println("i="+i+" ident="+ausgewaehlteAktionaereMeldungWeisung[i].meldungsIdent);
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.aktivierenSammelkarteMeldungArray(aktuelleSammelMeldung.meldungsIdent,
                aktuelleSammelMeldung.skIst, ausgewaehlteAktionaereMeldung,
                ausgewaehlteAktionaereMeldungWeisung);

        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Achtung - Fehler aufgetreten - Aktionäre überprüfen!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Aktivierung durchgeführt");
        }
        eigeneStage.hide();

    }

    
    
    /***************EingabeListener************************************** 
     * Grundsätzlich: Listener
     * für die Auswahl der Gattung überträgt die Eingabe von Gattungsnummer/
     * Gattungskürzel / Gattungslangtext jeweils in das andere Feld.
     *
     * Alle Listener: > aktualisieren immer aktuelleSammelMeldung > aktuelaisieren
     * im Änderungsmodus auch aktuelleMSammelkarteM und zeigt diese neu in Kopfzeile an
     */
    private void setzeListener() {

        cbAktiv.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                int hAktiv = 0;
                if (cbAktiv.isSelected()) {
                    hAktiv = 1;
                }
                aktuelleSammelMeldung.meldungAktiv = hAktiv;
                aktuelleMSammelkarte.belegeMeldungAktiv(aktuelleSammelMeldung);
                zeigeListSammelkartenNeuAn();
            }
        });

        tfBezeichnung.textProperty().addListener((obs, oldText, newText) -> {
            aktuelleSammelMeldung.name = tfBezeichnung.getText();
            aktuelleMSammelkarte.belegeNameOrt(aktuelleSammelMeldung);
            zeigeListSammelkartenNeuAn();
        });
        tfOrt.textProperty().addListener((obs, oldText, newText) -> {
            aktuelleSammelMeldung.ort = tfOrt.getText();
            aktuelleMSammelkarte.belegeNameOrt(aktuelleSammelMeldung);
            zeigeListSammelkartenNeuAn();
        });
        tfKommentar.textProperty().addListener((obs, oldText, newText) -> {
            aktuelleSammelMeldung.zusatzfeld2 = tfKommentar.getText();
            aktuelleMSammelkarte.kommentar = aktuelleSammelMeldung.zusatzfeld2;
            zeigeListSammelkartenNeuAn();
        });

        cbGattungNr.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
                if (neuerWert == null) {
                    return;
                } /* Entsteht, wenn COmbobox neu aufgebaut wird */
                if (gattungUpdaten && CaString.isNummern(neuerWert)) {
                    gattungUpdaten = false;
                    int hGattung = Integer.parseInt(neuerWert);
                    cbGattungKuerzel.setValue(ParamS.param.paramBasis.getGattungBezeichnungKurz(hGattung));
                    tfGattungLang.setText(ParamS.param.paramBasis.getGattungBezeichnung(hGattung));
                    gattungUpdaten = true;

                    aktuelleSammelMeldung.gattung = hGattung;
                    aktuelleMSammelkarte.gattungString = ParamS.param.paramBasis.getGattungBezeichnungKurz(hGattung);
                    zeigeListSammelkartenNeuAn();
                }
            }
        });
        cbGattungKuerzel.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
                if (neuerWert == null) {
                    return;
                } /* Entsteht, wenn COmbobox neu aufgebaut wird */
                if (gattungUpdaten && !neuerWert.isEmpty()) {
                    gattungUpdaten = false;
                    int hGattung = 0;
                    for (int i = 1; i <= 5; i++) {
                        if (ParamS.param.paramBasis.getGattungBezeichnungKurz(i).equals(neuerWert)) {
                            hGattung = i;
                        }
                    }
                    if (hGattung != 0) {
                        cbGattungNr.setValue(Integer.toString(hGattung));
                        tfGattungLang.setText(ParamS.param.paramBasis.getGattungBezeichnung(hGattung));

                        aktuelleSammelMeldung.gattung = hGattung;
                        aktuelleMSammelkarte.gattungString = ParamS.param.paramBasis
                                .getGattungBezeichnungKurz(hGattung);
                        zeigeListSammelkartenNeuAn();
                    }
                    gattungUpdaten = true;
                }
            }
        });
        gattungUpdaten = true;

        cbKIAV.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedSammelkartenArt();
            }
        });
        cbSRV.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedSammelkartenArt();
            }
        });
        cbOrga.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedSammelkartenArt();
            }
        });
        cbBriefwahl.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedSammelkartenArt();
            }
        });
        cbDauervollmacht.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedSammelkartenArt();
            }
        });

        tfGruppe.textProperty().addListener((obs, oldText, newText) -> {
            if (CaString.isNummern(tfGruppe.getText())) {
                aktuelleSammelMeldung.gruppe = Integer.parseInt(tfGruppe.getText());
                aktuelleMSammelkarte.gruppe = aktuelleSammelMeldung.gruppe;
                zeigeListSammelkartenNeuAn();
            }
        });

        cbOhneWeisung.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedCbWeisung();
            }
        });
        cbDedizierteWeisung.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedCbWeisung();
            }
        });

        cbImInternet.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (cbImInternet.isSelected()) {
                    aktuelleSammelMeldung.skBuchbarInternet = 1;
                } else {
                    aktuelleSammelMeldung.skBuchbarInternet = 0;
                }
                aktuelleMSammelkarte.belegeSkBuchbarInternetString(aktuelleSammelMeldung);
                zeigeListSammelkartenNeuAn();
            }
        });
        cbImPapier.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (cbImInternet.isSelected()) {
                    aktuelleSammelMeldung.skBuchbarPapier = 1;
                } else {
                    aktuelleSammelMeldung.skBuchbarPapier = 0;
                }
                aktuelleMSammelkarte.belegeSkBuchbarPapierString(aktuelleSammelMeldung);
                zeigeListSammelkartenNeuAn();
            }
        });
        cbVerlassenHV.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (cbVerlassenHV.isSelected()) {
                    aktuelleSammelMeldung.skBuchbarHV = 1;
                } else {
                    aktuelleSammelMeldung.skBuchbarHV = 0;
                }
                aktuelleMSammelkarte.belegeSkBuchbarHV(aktuelleSammelMeldung);
                zeigeListSammelkartenNeuAn();
            }
        });
        cbVertreterAnbieten.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (cbVertreterAnbieten.isSelected()) {
                    aktuelleSammelMeldung.skBuchbarVollmachtDritteHV = 1;
                } else {
                    aktuelleSammelMeldung.skBuchbarVollmachtDritteHV = 0;
                }
                aktuelleMSammelkarte.belegeSkBuchbarVollmachtDritteHV(aktuelleSammelMeldung);
                zeigeListSammelkartenNeuAn();
            }
        });

        cbWeitergabeInGesamtsumme.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedWeitergabeWeisungen();
            }
        });
        cbWeitergabeJeSammelkartenSumme.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedWeitergabeWeisungen();
            }
        });
        cbWeitergabeNichtErlaubt.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedWeitergabeWeisungen();
            }
        });
        cbWeitergabeUneingeschraenkt.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedWeitergabeWeisungen();
            }
        });

        cbOffenlegungWieParameter.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedOffenlegung();
            }
        });
        cbOhneOffenlegung.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedOffenlegung();
            }
        });
        cbMitOffenlegung.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                changedOffenlegung();
            }
        });

    }

    /**
     * Zeige list sammelkarten neu an.
     */
    private void zeigeListSammelkartenNeuAn() {
        /* Nur, wenn nicht im Neuaufnahme-Modus ist */
        if (aktuelleFunktion != 1) {
            listSammelkarten.clear();
            listSammelkarten.add(aktuelleMSammelkarte);
        }
    }

    /**
     * Changed cb weisung.
     */
    private void changedCbWeisung() {
        int hWeisungsartZulaessig = 1;
        if (cbOhneWeisung.isSelected()) {
            hWeisungsartZulaessig = (hWeisungsartZulaessig | 2);
        }
        if (cbDedizierteWeisung.isSelected()) {
            hWeisungsartZulaessig = (hWeisungsartZulaessig | 4);
        }
        aktuelleSammelMeldung.skWeisungsartZulaessig = hWeisungsartZulaessig;
        aktuelleMSammelkarte.skWeisungsartZulaessig = aktuelleSammelMeldung.skWeisungsartZulaessig;
        aktuelleMSammelkarte.belegeSkWeisungsartZulaessigString(aktuelleSammelMeldung);
        zeigeListSammelkartenNeuAn();
    }

    /**
     * Changed sammelkarten art.
     */
    private void changedSammelkartenArt() {
        if (cbKIAV.isSelected()) {
            aktuelleSammelMeldung.skIst = 1;
        }
        if (cbSRV.isSelected()) {
            aktuelleSammelMeldung.skIst = 2;
        }
        if (cbOrga.isSelected()) {
            aktuelleSammelMeldung.skIst = 3;
        }
        if (cbBriefwahl.isSelected()) {
            aktuelleSammelMeldung.skIst = 4;
        }
        if (cbDauervollmacht.isSelected()) {
            aktuelleSammelMeldung.skIst = 5;
        }

        aktuelleMSammelkarte.skIst = aktuelleSammelMeldung.skIst;
        aktuelleMSammelkarte.belegeSkIstString(aktuelleSammelMeldung);
        zeigeListSammelkartenNeuAn();
    }

    /**
     * Changed weitergabe weisungen.
     */
    private void changedWeitergabeWeisungen() {
        if (cbWeitergabeInGesamtsumme.isSelected()) {
            aktuelleSammelMeldung.zusatzfeld1 = "1";
        }
        if (cbWeitergabeJeSammelkartenSumme.isSelected()) {
            aktuelleSammelMeldung.zusatzfeld1 = "2";
        }
        if (cbWeitergabeNichtErlaubt.isSelected()) {
            aktuelleSammelMeldung.zusatzfeld1 = "3";
        }
        if (cbWeitergabeUneingeschraenkt.isSelected()) {
            aktuelleSammelMeldung.zusatzfeld1 = "";
        }

        aktuelleMSammelkarte.belegeVerdecktString(aktuelleSammelMeldung);
        zeigeListSammelkartenNeuAn();
    }

    /**
     * Changed offenlegung.
     */
    private void changedOffenlegung() {
        if (cbMitOffenlegung.isSelected()) {
            aktuelleSammelMeldung.skOffenlegung = 1;
        }
        if (cbOhneOffenlegung.isSelected()) {
            aktuelleSammelMeldung.skOffenlegung = -1;
        }
        if (cbOffenlegungWieParameter.isSelected()) {
            aktuelleSammelMeldung.skOffenlegung = 0;
        }

        aktuelleMSammelkarte.belegeSkOffenlegungString(aktuelleSammelMeldung);
        zeigeListSammelkartenNeuAn();
    }

    /**
     * *************** Logik *********************************.
     */

    /**
     * Liest für die Sammelkarte aktuelleSammelMeldung ein: > Kopfweisungen der
     * Sammelkarte (weisungenSammelkopf) > Summen der Weisungen aus den aktiven
     * Aktionären (aktionaersSummen) > zugeordnete AKtionäre in aktionaereAktiv,
     * aktionaereInaktiv, aktionaereWiderrufen, aktionaereGeaendert
     */
    private void fuelleSammelkartenZusatzdaten() {
        dbBundle = new DbBundle();
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        blSammelkarten.leseKopfWeisungUndAktionaereZuSammelkarte(aktuelleSammelMeldung);
        weisungenSammelkopf = blSammelkarten.rcWeisungenSammelkopf;
        aktionaersSummen = blSammelkarten.rcAktionaersSummen;

        aktionaereAktiv = blSammelkarten.aktionaereAktiv;
        aktionaereInaktiv = blSammelkarten.aktionaereInaktiv;
        aktionaereWiderrufen = blSammelkarten.aktionaereWiderrufen;
        aktionaereGeaendert = blSammelkarten.aktionaereGeaendert;
    }

    /**
     * Bereite vor neue sammelkarte.
     */
    private void bereiteVorNeueSammelkarte() {
        aktuelleSammelMeldung.meldungAktiv = 1;
        aktuelleSammelMeldung.gattung = 1;
        aktuelleSammelMeldung.zusatzfeld1 = "";
        aktuelleSammelMeldung.skBuchbarPapier = 0;
        aktuelleSammelMeldung.skOffenlegung = 0;
    }

    /**
     * Sammel war praesent.
     *
     * @return true, if successful
     */
    private boolean sammelWarPraesent() {
        if (aktuelleSammelMeldung.statusWarPraesenz == 1) {
            return true;
        }
        return false;
    }

    /**
     * Sammel hat aktionaere.
     *
     * @return true, if successful
     */
    private boolean sammelHatAktionaere() {
        if (aktionaereAktiv == null || aktionaereAktiv.size() > 0) {
            return true;
        }
        return false;
    }

    /** *************** Initialisierung ****************************. */

    /**
     * 1=Neue Sammelkarte anlegen 
     * 2=Sammelkartendaten ändern
     * 3=Vertreter/Eintrittskarte zuordnen / ändern etc. 
     * 4=Details anzeigen
     * 5=Stapel-Umbuchung
     */
    private int aktuelleFunktion = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pMeldeIdent  the melde ident
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pMeldeIdent, int pFunktion) {
        eigeneStage = pEigeneStage;
        aktuelleSammelIdent = pMeldeIdent;
        aktuelleFunktion = pFunktion;
    }

}
