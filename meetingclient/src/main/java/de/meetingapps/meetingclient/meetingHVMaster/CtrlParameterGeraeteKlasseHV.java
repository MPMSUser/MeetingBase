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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 * The Class CtrlParameterGeraeteKlasseHV.
 */
public class CtrlParameterGeraeteKlasseHV {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The combo aktives set. */
    @FXML
    private ComboBox<EclGeraeteSet> comboAktivesSet;

    /** The btn set aktivieren. */
    @FXML
    private Button btnSetAktivieren;

    /** The tf bezeichnung aktiv. */
    @FXML
    private TextField tfBezeichnungAktiv;

    /** The tf ausfuehrliche beschreibung ausgewaehlt. */
    @FXML
    private TextArea tfAusfuehrlicheBeschreibungAusgewaehlt;

    /** The btn tablets zuruecksetzen. */
    @FXML
    private Button btnTabletsZuruecksetzen;

    /** The btn tablets status anzeigen. */
    @FXML
    private Button btnTabletsStatusAnzeigen;

    /** The btn tablets status ausblenden. */
    @FXML
    private Button btnTabletsStatusAusblenden;

    /** The scrpn vorhandene klassen. */
    @FXML
    private ScrollPane scrpnVorhandeneKlassen;

    /** The combo mandant. */
    @FXML
    private ComboBox<EclEmittenten> comboMandant;

    /** The tf HV jahr. */
    @FXML
    private TextField tfHVJahr;

    /** The tf HV nummer. */
    @FXML
    private TextField tfHVNummer;

    /** The tf datenbereich. */
    @FXML
    private TextField tfDatenbereich;

    /** The cb hochformat. */
    @FXML
    private CheckBox cbHochformat;

    /** The btn aktivieren. */
    @FXML
    private Button btnAktivieren;

    /** The l mandant. */
    @FXML
    private Label lMandant;

    /** The l HV jahr. */
    @FXML
    private Label lHVJahr;

    /** The l HV jahr 1. */
    @FXML
    private Label lHVJahr1;

    /** The l HV nummer. */
    @FXML
    private Label lHVNummer;

    /** The l HV nummer 1. */
    @FXML
    private Label lHVNummer1;

    /** The l datenbereich. */
    @FXML
    private Label lDatenbereich;

    /** The l datenbereich 1. */
    @FXML
    private Label lDatenbereich1;

    /** The l tablet hoch. */
    @FXML
    private Label lTabletHoch;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The fehler. */
    private int fehler = 0;

    /** Wird von refresh_verfuegbareGeraetesets gefüllt. */
    private EclGeraeteSet[] arrayGeraeteSet = null;

    /** Wird von refresh_Klassen gefüllt. */
    private int[] klassen = null;

    /** The cb auswaehlen. */
    @FXML
    private CheckBox[] cbAuswaehlen = null;

    /** The set aktivieren. */
    private boolean setAktivieren = false;

    /** Geräte-Set, das gerade in Bearbeitung (Pflege) ist. */
    EclGeraeteSet eclGeraeteSetInBearbeitung = null;

    /** The aktives set. */
    private int aktivesSet = 0;

    /** Liste der Mandanten, zur Auswahl. Nur Nr. und (Kurz)Bezeichnung */
    List<EclEmittenten> listEclEmittenten = new LinkedList<EclEmittenten>();

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert comboAktivesSet != null : "fx:id=\"comboAktivesSet\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert btnSetAktivieren != null : "fx:id=\"btnSetAktivieren\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert tfBezeichnungAktiv != null : "fx:id=\"tfBezeichnungAktiv\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert tfAusfuehrlicheBeschreibungAusgewaehlt != null : "fx:id=\"tfAusfuehrlicheBeschreibungAusgewaehlt\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert btnTabletsZuruecksetzen != null : "fx:id=\"btnTabletsZuruecksetzen\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert btnTabletsStatusAnzeigen != null : "fx:id=\"btnTabletsStatusAnzeigen\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert btnTabletsStatusAusblenden != null : "fx:id=\"btnTabletsStatusAusblenden\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert scrpnVorhandeneKlassen != null : "fx:id=\"scrpnVorhandeneKlassen\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lMandant != null : "fx:id=\"lMandant\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert comboMandant != null : "fx:id=\"comboMandant\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lHVJahr != null : "fx:id=\"lHVJahr\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert tfHVJahr != null : "fx:id=\"tfHVJahr\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lHVJahr1 != null : "fx:id=\"lHVJahr1\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lHVNummer != null : "fx:id=\"lHVNummer\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert tfHVNummer != null : "fx:id=\"tfHVNummer\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lHVNummer1 != null : "fx:id=\"lHVNummer1\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lDatenbereich != null : "fx:id=\"lDatenbereich\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert tfDatenbereich != null : "fx:id=\"tfDatenbereich\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lDatenbereich1 != null : "fx:id=\"lDatenbereich1\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert lTabletHoch != null : "fx:id=\"lTabletHoch\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert cbHochformat != null : "fx:id=\"cbHochformat\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";
        assert btnAktivieren != null : "fx:id=\"btnAktivieren\" was not injected: check your FXML file 'ParameterGeraeteKlasseHV.fxml'.";

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                //                		CaZeigeHinweis zeigeHinweis=new CaZeigeHinweis();
                //                		zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        fehler = 0;
        /*Alle verfügbaren Gerätesets anzeigen*/
        refresh_verfuegbareGeraetesets();

        /*Aktives Geräteset anzeigen und Auswahlmöglichkeit*/
        refresh_aktivesSet();

        /*Klassen anzeigen, zu Set*/
        refresh_Klassen();

        fuelleMandantenAuswahl();

        comboAktivesSet.valueProperty().addListener(new ChangeListener<EclGeraeteSet>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, EclGeraeteSet alterWert,
                    EclGeraeteSet neuerWert) {
                if (neuerWert == null || neuerWert.ident == 0) {
                    return;
                }
                int setIdent = neuerWert.ident;
                fuelleTextZeilen_aktivesSet(neuerWert);
                if (aktivesSet == setIdent) {
                    btnSetAktivieren.setVisible(false);
                    setAktivieren = false;
                } else {
                    btnSetAktivieren.setVisible(true);
                    setAktivieren = true;
                }
            }
        });

        tfBezeichnungAktiv.setEditable(false);
        tfAusfuehrlicheBeschreibungAusgewaehlt.setEditable(false);
        //       
        //        setzePflegeModusAuf0();

        lDbBundle.closeAll();
        if (fehler < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, CaFehler.getFehlertext(fehler, 0));
        }

        btnTabletsStatusAusblenden.setVisible(false);

    }

    /**
     * Refresh verfuegbare geraetesets.
     */
    private void refresh_verfuegbareGeraetesets() {
        lDbBundle.dbGeraeteSet.read_all();
        int anzGeraeteSets = lDbBundle.dbGeraeteSet.anzErgebnis();
        arrayGeraeteSet = null;

        if (anzGeraeteSets > 0) {
            arrayGeraeteSet = lDbBundle.dbGeraeteSet.ergebnisArray;
        }
    }

    /**
     * Refresh aktives set.
     */
    private void refresh_aktivesSet() {
        fehler = 0;
        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            aktivesSet = Integer.parseInt(lDbBundle.dbParameter.ergebnisPosition(0).wert);
        }

        if (aktivesSet != 0) {
            lDbBundle.dbGeraeteSet.read(aktivesSet);
            if (lDbBundle.dbGeraeteSet.anzErgebnis() > 0) {
                fuelleTextZeilen_aktivesSet(lDbBundle.dbGeraeteSet.ergebnisPosition(0));
            } else {
                fehler = CaFehler.sysGeraeteSetNichtVorhanden;
            }
        } else {
            tfBezeichnungAktiv.setText("");
            tfAusfuehrlicheBeschreibungAusgewaehlt.setText("");
        }

        StringConverter<EclGeraeteSet> sc = new StringConverter<EclGeraeteSet>() {

            @Override
            public String toString(EclGeraeteSet pGeraeteSet) {
                return Integer.toString(pGeraeteSet.ident) + " " + pGeraeteSet.kurzBeschreibung;
            }

            @Override
            public EclGeraeteSet fromString(String string) {
                return null;
            }
        };
        comboAktivesSet.setConverter(sc);
        comboAktivesSet.getSelectionModel().clearSelection();
        comboAktivesSet.getItems().clear();
        if (arrayGeraeteSet != null) {
            for (int i = 0; i < arrayGeraeteSet.length; i++) {
                comboAktivesSet.getItems().add(arrayGeraeteSet[i]);
                if (aktivesSet == arrayGeraeteSet[i].ident) {
                    comboAktivesSet.setValue(arrayGeraeteSet[i]);
                }
            }
        }

        btnSetAktivieren.setVisible(false);

    }

    /**
     * Fuelle mandanten auswahl.
     */
    private void fuelleMandantenAuswahl() {
        BvMandanten lBvMandanten = new BvMandanten();
        listEclEmittenten = lBvMandanten.liefereEmittentenListeFuerNrAuswahl(lDbBundle);

        StringConverter<EclEmittenten> sc = new StringConverter<EclEmittenten>() {
            @Override
            public String toString(EclEmittenten pEmittent) {
                return Integer.toString(pEmittent.mandant) + " " + pEmittent.bezeichnungKurz;
            }

            @Override
            public EclEmittenten fromString(String string) {
                return null;
            }
        };
        comboMandant.setConverter(sc);
        comboMandant.getSelectionModel().clearSelection();
        comboMandant.getItems().clear();
        EclEmittenten lEmittent = new EclEmittenten();
        lEmittent.mandant = 0;
        lEmittent.bezeichnungKurz = "keine Voreinstellung";
        comboMandant.getItems().add(lEmittent);
        comboMandant.setValue(lEmittent);
        for (int i = 0; i < listEclEmittenten.size(); i++) {
            comboMandant.getItems().add(listEclEmittenten.get(i));
        }

    }

    /**
     * Fuelle text zeilen aktives set.
     *
     * @param pGeraeteSet the geraete set
     */
    private void fuelleTextZeilen_aktivesSet(EclGeraeteSet pGeraeteSet) {
        tfBezeichnungAktiv.setText(pGeraeteSet.kurzBeschreibung);
        tfAusfuehrlicheBeschreibungAusgewaehlt.setText(pGeraeteSet.beschreibung);
    }

    /************************Logik***************************************************/
    private void speichernGeraeteSet() {
        int neuesAktivesSet = comboAktivesSet.getValue().ident;
        if (neuesAktivesSet == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültiges Geräteset auswählen!");
            return;
        }
        lDbBundle.openAll();

        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            lParameter = lDbBundle.dbParameter.ergebnisPosition(0);
            lParameter.wert = Integer.toString(neuesAktivesSet);
            lDbBundle.dbParameter.updateServer(lParameter);
        } else {
            lParameter = new EclParameter();
            lParameter.mandant = 0;
            lParameter.ident = 51;
            lParameter.wert = Integer.toString(neuesAktivesSet);
            lParameter.beschreibung = "geraeteSetIdent";
            lDbBundle.dbParameter.insertServer(lParameter);
        }

        aktivesSet = neuesAktivesSet;

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();
        bvReload.setReloadParameterServer();

        refresh_Klassen();

        lDbBundle.closeAll();
        btnSetAktivieren.setVisible(false);
        setAktivieren = false;
    }

    /**
     * Refresh klassen.
     */
    private void refresh_Klassen() {
        scrpnVorhandeneKlassen.setContent(null);
        if (aktivesSet != 0) {
            lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all_nurKlasseIdentUnique(aktivesSet);
            int anz = lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis();
            if (anz > 0) {
                GridPane grpnGeraeteKlassen = new GridPane();
                grpnGeraeteKlassen.setVgap(5);
                grpnGeraeteKlassen.setHgap(5);

                cbAuswaehlen = new CheckBox[anz];
                klassen = new int[anz];

                for (int i = 0; i < anz; i++) {
                    int lGerateKlasseIdent = lDbBundle.dbGeraeteKlasseSetZuordnung
                            .ergebnisPosition(i).geraeteKlasseIdent;
                    lDbBundle.dbParameter.readGerateKlasse_all(lGerateKlasseIdent);
                    ParamGeraet lParamGeraeteKlasse = lDbBundle.dbParameter.ergParamGeraet;

                    cbAuswaehlen[i] = new CheckBox();
                    grpnGeraeteKlassen.add(cbAuswaehlen[i], 0, i);

                    klassen[i] = lParamGeraeteKlasse.identKlasse;
                    Label lIdent = new Label();
                    lIdent.setText("Klasse " + Integer.toString(lParamGeraeteKlasse.identKlasse));
                    grpnGeraeteKlassen.add(lIdent, 1, i);
                    Label lMandant = new Label();
                    lMandant.setText("Emittent " + Integer.toString(lParamGeraeteKlasse.festgelegterMandant));
                    grpnGeraeteKlassen.add(lMandant, 2, i);
                    Label lJahr = new Label();
                    lJahr.setText("Jahr " + Integer.toString(lParamGeraeteKlasse.festgelegtesJahr));
                    grpnGeraeteKlassen.add(lJahr, 3, i);
                    Label lHVNr = new Label();
                    lHVNr.setText("HV-Nr " + lParamGeraeteKlasse.festgelegteHVNummer);
                    grpnGeraeteKlassen.add(lHVNr, 4, i);
                    Label lDBBereich = new Label();
                    lDBBereich.setText("DBBereich " + lParamGeraeteKlasse.festgelegteDatenbank);
                    grpnGeraeteKlassen.add(lDBBereich, 5, i);

                    Label lQH = new Label();
                    if (lParamGeraeteKlasse.abstimmungTabletHochformat) {
                        lQH.setText("Hochformat");
                    } else {
                        lQH.setText("Querformat");
                    }
                    grpnGeraeteKlassen.add(lQH, 6, i);

                    Label lBezeichnung = new Label();
                    lBezeichnung.setText(lParamGeraeteKlasse.beschreibungKlasse);
                    grpnGeraeteKlassen.add(lBezeichnung, 7, i);

                }
                scrpnVorhandeneKlassen.setContent(grpnGeraeteKlassen);

            }

        }
    }

    /**
     * Funktionen zulaessig.
     *
     * @return true, if successful
     */
    private boolean funktionenZulaessig() {
        if (setAktivieren) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte erst Set aktivieren!");
            return false;
        }
        return true;
    }

    /**************************Anzeigefunktionen***************************************/
    
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /**
     * Verberge mandant.
     */
    private void verbergeMandant() {
        lMandant.setVisible(false);
        comboMandant.setVisible(false);
        lHVJahr.setVisible(false);
        tfHVJahr.setVisible(false);
        lHVJahr1.setVisible(false);
        lHVNummer.setVisible(false);
        tfHVNummer.setVisible(false);
        lHVNummer1.setVisible(false);
        lDatenbereich.setVisible(false);
        tfDatenbereich.setVisible(false);
        lDatenbereich1.setVisible(false);
        lTabletHoch.setVisible(false);
        cbHochformat.setVisible(false);
        btnAktivieren.setVisible(false);

        btnTabletsStatusAusblenden.setVisible(true);
    }

    /**
     * Zeige mandant.
     */
    private void zeigeMandant() {
        lMandant.setVisible(true);
        comboMandant.setVisible(true);
        lHVJahr.setVisible(true);
        tfHVJahr.setVisible(true);
        lHVJahr1.setVisible(true);
        lHVNummer.setVisible(true);
        tfHVNummer.setVisible(true);
        lHVNummer1.setVisible(true);
        lDatenbereich.setVisible(true);
        tfDatenbereich.setVisible(true);
        lDatenbereich1.setVisible(true);
        lTabletHoch.setVisible(true);
        cbHochformat.setVisible(true);
        btnAktivieren.setVisible(true);

        btnTabletsStatusAusblenden.setVisible(false);
        refresh_Klassen();

    }

    /********************Aktionen auf Oberfläche*************************/
    
    @FXML
    void clickedTabletsStatusAnzeigen(ActionEvent event) {
        if (!funktionenZulaessig()) {
            return;
        }
        lDbBundle.openAll();
        verbergeMandant();

        scrpnVorhandeneKlassen.setContent(null);

        lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all(aktivesSet);
        int anz = lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis();
        if (anz > 0) {

            GridPane grpnGeraeteKlassen = new GridPane();
            grpnGeraeteKlassen.setVgap(5);
            grpnGeraeteKlassen.setHgap(5);

            for (int i = 0; i < anz; i++) {
                EclGeraetKlasseSetZuordnung lZuordnung = lDbBundle.dbGeraeteKlasseSetZuordnung.ergebnisPosition(i);
                Label lIdent = new Label();
                lIdent.setText("Gerät " + Integer.toString(lZuordnung.geraeteNummer));
                grpnGeraeteKlassen.add(lIdent, 0, i);
                Label lStatus = new Label();
                if (lZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart == 1) {
                    lStatus.setText("Neustart offen!");
                } else {
                    lStatus.setText("erledigt");
                }
                grpnGeraeteKlassen.add(lStatus, 1, i);

            }
            scrpnVorhandeneKlassen.setContent(grpnGeraeteKlassen);

        }

        lDbBundle.closeAll();

    }

    /**
     * Clicked tablets status ausblenden.
     *
     * @param event the event
     */
    @FXML
    void clickedTabletsStatusAusblenden(ActionEvent event) {
        if (!funktionenZulaessig()) {
            return;
        }
        lDbBundle.openAll();
        zeigeMandant();
        lDbBundle.closeAll();
    }

    /**
     * Clicked set aktivieren.
     *
     * @param event the event
     */
    @FXML
    void clickedSetAktivieren(ActionEvent event) {
        speichernGeraeteSet();
    }

    /**
     * Clicked aktivieren.
     *
     * @param event the event
     */
    @FXML
    void clickedAktivieren(ActionEvent event) {
        String hString = "";

        if (!funktionenZulaessig()) {
            return;
        }

        hString = tfHVJahr.getText();
        if (!hString.isEmpty() && (!CaString.isNummern(hString) || Integer.parseInt(hString) < 1990
                || Integer.parseInt(hString) > 2099)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "HV-Jahr muß eine Zahl zwischen 1990 und 2099 oder leer sein!");
            tfHVJahr.requestFocus();
        }

        hString = tfHVNummer.getText();
        if (!hString.isEmpty() && (hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Lfd. HV-Nummer muß zwischen A und Z oder leer sein!");
            tfHVNummer.requestFocus();
        }

        hString = tfDatenbereich.getText();
        if (!hString.isEmpty() && (hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Datenbereich muß zwischen A und Z oder leer sein!");
            tfDatenbereich.requestFocus();
        }

        lDbBundle.openAll();
        int gef = 0;
        for (int i = 0; i < klassen.length; i++) {
            if (cbAuswaehlen[i].isSelected()) {
                gef = 1;
                cbAuswaehlen[i].setSelected(false);

                lDbBundle.dbParameter.readGerateKlasse_all(klassen[i]);
                ParamGeraet lParamGeraet = lDbBundle.dbParameter.ergParamGeraet;

                lParamGeraet.festgelegterMandant = comboMandant.getValue().mandant;
                hString = tfHVJahr.getText();
                if (hString.isEmpty()) {
                    lParamGeraet.festgelegtesJahr = 0;
                } else {
                    lParamGeraet.festgelegtesJahr = Integer.parseInt(hString);
                }

                lParamGeraet.festgelegteHVNummer = tfHVNummer.getText();
                lParamGeraet.festgelegteDatenbank = tfDatenbereich.getText();
                lParamGeraet.abstimmungTabletHochformat = cbHochformat.isSelected();
                lDbBundle.dbParameter.updateGeraete_all(lParamGeraet);
            }

        }

        if (gef == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte wählen Sie eine oder mehrere Klassen aus!");
            lDbBundle.closeAll();
            return;
        }

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();
        bvReload.setReloadParameterServer();

        refresh_Klassen();

        lDbBundle.closeAll();

    }

    /**
     * Clicked tablets zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedTabletsZuruecksetzen(ActionEvent event) {
        if (!funktionenZulaessig()) {
            return;
        }

        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        boolean bRc = zeigeHinweis.zeige2Buttons(eigeneStage,
                "Achtung - mit dieser Funktion werden die Tablets veranlaßt, lokale Sicherungsdateien zu löschen, z.B. nach durchgeführter Schulung. Fortsetzen?",
                "Ja", "Nein");
        if (bRc == false) {
            return;
        }
        lDbBundle.openAll();

        lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all(aktivesSet);
        int anz = lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis();
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclGeraetKlasseSetZuordnung lZuordnung = lDbBundle.dbGeraeteKlasseSetZuordnung.ergebnisPosition(i);
                lZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart = 1;
                lDbBundle.dbGeraeteKlasseSetZuordnung.update(lZuordnung);
            }

        }

        lDbBundle.closeAll();
        if (anz > 0) {
            zeigeHinweis.zeige(eigeneStage,
                    "Tablets zum Rücksetzen veranlaßt. Bitte auf den Tablets betterMeeting neu starten!");
        } else {
            zeigeHinweis.zeige(eigeneStage, "Keine Geräte für dieses Set vorhanden!");
        }

    }

    //    @FXML
    //    void onKeyPressedSetNummer(KeyEvent event) {
    //		if (event.getCode() == KeyCode.ENTER && pflegeModus==0)  {
    //			einlesenAnzeigenSetZurBearbeitung();
    //		}
    //
    //    }
    //
    //    @FXML
    //    void clickedBearbeiten(ActionEvent event) {
    //		einlesenAnzeigenSetZurBearbeitung();
    //    }
    //
    //    @FXML
    //    void clickedLoeschen(ActionEvent event) {
    //    	loescheSetInBearbeitung();
    //    }
    //
    //
    //    @FXML
    //    void clickedNeuesSet(ActionEvent event) {
    //    	neuesSetVorbereiten();
    //    }

}
