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
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 * The Class CtrlParameterGeraeteSets.
 */
public class CtrlParameterGeraeteSets {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scrpn vorhandene sets. */
    @FXML
    private ScrollPane scrpnVorhandeneSets;

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

    /** The tf set. */
    @FXML
    private TextField tfSet;

    /** The btn bearbeiten. */
    @FXML
    private Button btnBearbeiten;

    /** The btn neues set. */
    @FXML
    private Button btnNeuesSet;

    /** The tf bezeichnung. */
    @FXML
    private TextField tfBezeichnung;

    /** The tf ausfuehrliche beschreibung. */
    @FXML
    private TextArea tfAusfuehrlicheBeschreibung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn loeschen. */
    @FXML
    private Button btnLoeschen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The fehler. */
    private int fehler = 0;

    /** Wird von refresh_verfuegbareGeraetesets gefüllt. */
    private EclGeraeteSet[] arrayGeraeteSet = null;

    /**Pflegemodus für Bearbeitung Geräteset.
     * 0 = nichts in Bearbeitung
     * 1 = Neuaufnahme
     * 2 = Ändern
     * 3 = Löschen
     */
    private int pflegeModus = 0;

    /** Geräte-Set, das gerade in Bearbeitung (Pflege) ist. */
    EclGeraeteSet eclGeraeteSetInBearbeitung = null;

    /** The aktives set. */
    private int aktivesSet = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert scrpnVorhandeneSets != null : "fx:id=\"scrpnVorhandeneSets\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert comboAktivesSet != null : "fx:id=\"comboAktivesSet\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnSetAktivieren != null : "fx:id=\"btnSetAktivieren\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert tfBezeichnungAktiv != null : "fx:id=\"tfBezeichnungAktiv\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert tfAusfuehrlicheBeschreibungAusgewaehlt != null : "fx:id=\"tfAusfuehrlicheBeschreibungAusgewaehlt\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert tfSet != null : "fx:id=\"tfSet\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnBearbeiten != null : "fx:id=\"btnBearbeiten\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnNeuesSet != null : "fx:id=\"btnNeuesSet\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert tfBezeichnung != null : "fx:id=\"tfBezeichnung\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert tfAusfuehrlicheBeschreibung != null : "fx:id=\"tfAusfuehrlicheBeschreibung\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnLoeschen != null : "fx:id=\"btnLoeschen\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterGeraeteSets.fxml'.";

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
                } else {
                    btnSetAktivieren.setVisible(true);
                }
            }
        });

        tfBezeichnungAktiv.setEditable(false);
        tfAusfuehrlicheBeschreibungAusgewaehlt.setEditable(false);

        setzePflegeModusAuf0();

        lDbBundle.closeAll();
        if (fehler < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, CaFehler.getFehlertext(fehler, 0));
        }
    }

    /**
     * Refresh verfuegbare geraetesets.
     */
    private void refresh_verfuegbareGeraetesets() {
        lDbBundle.dbGeraeteSet.read_all();
        int anzGeraeteSets = lDbBundle.dbGeraeteSet.anzErgebnis();
        scrpnVorhandeneSets.setContent(null);
        arrayGeraeteSet = null;

        if (anzGeraeteSets > 0) {
            GridPane grpnGeraeteSets = new GridPane();
            grpnGeraeteSets.setVgap(5);
            grpnGeraeteSets.setHgap(5);
            arrayGeraeteSet = lDbBundle.dbGeraeteSet.ergebnisArray;

            for (int i = 0; i < anzGeraeteSets; i++) {
                EclGeraeteSet lGeraeteSet = arrayGeraeteSet[i];
                Label lIdent = new Label();
                lIdent.setText(Integer.toString(lGeraeteSet.ident));
                grpnGeraeteSets.add(lIdent, 0, i);
                Label lKurzBeschreibung = new Label();
                lKurzBeschreibung.setText(lGeraeteSet.kurzBeschreibung);
                grpnGeraeteSets.add(lKurzBeschreibung, 1, i);
                Label lBeschreibung = new Label();
                lBeschreibung.setText(lGeraeteSet.beschreibung);
                grpnGeraeteSets.add(lBeschreibung, 2, i);
            }
            scrpnVorhandeneSets.setContent(grpnGeraeteSets);

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
        lDbBundle.openAllOhneParameterCheck();

        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            lParameter = lDbBundle.dbParameter.ergebnisPosition(0);
            lParameter.wert = Integer.toString(neuesAktivesSet);
            int rc = lDbBundle.dbParameter.updateServer(lParameter);
            System.out.println("rc=" + rc);
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

        lDbBundle.closeAll();
        btnSetAktivieren.setVisible(false);
    }

    /**
     * Pruefe set nr formale eingabe.
     *
     * @return true, if successful
     */
    private boolean pruefeSetNrFormaleEingabe() {
        String hEingabe = tfSet.getText();
        if (hEingabe.isEmpty() || CaString.isNummern(hEingabe) == false || hEingabe.length() > 4
                || Integer.parseInt(hEingabe) > 9999 || Integer.parseInt(hEingabe) < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte geben Sie eine gültige Set-Ident zwischen 1 und 9999 ein!");
            tfSet.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Einlesen anzeigen set zur bearbeitung.
     */
    private void einlesenAnzeigenSetZurBearbeitung() {
        String hEingabe = tfSet.getText();
        if (pruefeSetNrFormaleEingabe() == false) {
            return;
        }

        int hIdent = Integer.parseInt(hEingabe);
        lDbBundle.openAll();
        lDbBundle.dbGeraeteSet.read(hIdent);
        lDbBundle.closeAll();
        if (lDbBundle.dbGeraeteSet.anzErgebnis() == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Geräte-Set nicht vorhanden!");
            tfSet.requestFocus();
            return;
        }
        eclGeraeteSetInBearbeitung = lDbBundle.dbGeraeteSet.ergebnisPosition(0);
        tfBezeichnung.setText(eclGeraeteSetInBearbeitung.kurzBeschreibung);
        tfBezeichnung.setEditable(true);

        tfAusfuehrlicheBeschreibung.setText(eclGeraeteSetInBearbeitung.beschreibung);
        tfAusfuehrlicheBeschreibung.setEditable(true);

        tfSet.setEditable(false);

        pflegeModus = 2;
        btnSpeichern.setVisible(true);
        btnLoeschen.setVisible(true);
        btnAbbrechen.setVisible(true);

        btnBearbeiten.setVisible(false);
        btnNeuesSet.setVisible(false);

        tfBezeichnung.requestFocus();
    }

    /**
     * Sets the in bearbeitung speichern.
     */
    private void setInBearbeitungSpeichern() {
        int hLen = 0;
        hLen = tfBezeichnung.getText().length();
        if (hLen > 50) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Bezeichnung des Sets: maximal 50 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfBezeichnung.requestFocus();
            return;
        }
        hLen = tfAusfuehrlicheBeschreibung.getText().length();
        if (hLen > 500) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Ausführliche Beschreibung: maximal 500 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfBezeichnung.requestFocus();
            return;
        }

        eclGeraeteSetInBearbeitung.kurzBeschreibung = tfBezeichnung.getText();
        eclGeraeteSetInBearbeitung.beschreibung = tfAusfuehrlicheBeschreibung.getText();
        if (pflegeModus == 1) { /*Neuaufnahme*/
            lDbBundle.openAll();
            int rc = lDbBundle.dbGeraeteSet.insert(eclGeraeteSetInBearbeitung);
            refresh_verfuegbareGeraetesets();
            refresh_aktivesSet();
            BvReload bvReload = new BvReload(lDbBundle);
            bvReload.setReloadParameterGeraete();
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Geräte-Set kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        } else {/*Ändern*/
            lDbBundle.openAll();
            int rc = lDbBundle.dbGeraeteSet.update(eclGeraeteSetInBearbeitung);
            refresh_verfuegbareGeraetesets();
            refresh_aktivesSet();
            BvReload bvReload = new BvReload(lDbBundle);
            bvReload.setReloadParameterGeraete();
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Geräte-Set kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        }
        setzePflegeModusAuf0();
    }

    /**
     * Loesche set in bearbeitung.
     */
    void loescheSetInBearbeitung() {
        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        boolean brc = zeigeHinweis.zeige2Buttons(eigeneStage,
                "Soll Geräte-Set " + Integer.toString(eclGeraeteSetInBearbeitung.ident) + " "
                        + eclGeraeteSetInBearbeitung.kurzBeschreibung + " gelöscht werden?",
                "Ja", "Nein");
        if (brc == false) {
            return;
        }

        lDbBundle.openAll();

        /*Prüfen, ob Set aktiv ist*/
        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            if (Integer.parseInt(lDbBundle.dbParameter.ergebnisPosition(0).wert) == eclGeraeteSetInBearbeitung.ident) {
                zeigeHinweis.zeige(eigeneStage, "Geräte-Set kann nicht gelöscht werden, da Set aktiv ist!");
                lDbBundle.closeAll();
                return;
            }
        }

        lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all(eclGeraeteSetInBearbeitung.ident);
        if (lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis() > 0) {
            zeigeHinweis.zeige(eigeneStage,
                    "Geräte-Set kann nicht gelöscht werden, da dem Set noch Geräte zugeordnet sind!");
            lDbBundle.closeAll();
            return;
        }

        int rc = lDbBundle.dbGeraeteSet.delete(eclGeraeteSetInBearbeitung.ident);

        refresh_verfuegbareGeraetesets();
        refresh_aktivesSet();

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();

        lDbBundle.closeAll();
        if (rc < 1) {
            zeigeHinweis.zeige(eigeneStage,
                    "Geräte-Set kann nicht gelöscht werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
        }
        setzePflegeModusAuf0();

    }

    /**
     * Neues set vorbereiten.
     */
    private void neuesSetVorbereiten() {

        String hEingabe = tfSet.getText();
        if (pruefeSetNrFormaleEingabe() == false) {
            return;
        }

        int hIdent = Integer.parseInt(hEingabe);
        lDbBundle.openAll();
        lDbBundle.dbGeraeteSet.read(hIdent);
        lDbBundle.closeAll();
        if (lDbBundle.dbGeraeteSet.anzErgebnis() > 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Geräte-Set bereits vorhanden!");
            tfSet.requestFocus();
            return;
        }

        eclGeraeteSetInBearbeitung = new EclGeraeteSet();
        eclGeraeteSetInBearbeitung.ident = hIdent;

        tfBezeichnung.setEditable(true);
        tfAusfuehrlicheBeschreibung.setEditable(true);

        tfSet.setEditable(false);

        pflegeModus = 1;
        btnSpeichern.setVisible(true);
        btnLoeschen.setVisible(false);
        btnAbbrechen.setVisible(true);

        btnBearbeiten.setVisible(false);
        btnNeuesSet.setVisible(false);

        tfBezeichnung.requestFocus();

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
     * Setze pflege modus auf 0.
     */
    private void setzePflegeModusAuf0() {

        tfSet.setEditable(true);
        tfSet.setText("");

        btnBearbeiten.setVisible(true);
        btnNeuesSet.setVisible(true);

        tfBezeichnung.setEditable(false);
        tfBezeichnung.setText("");

        tfAusfuehrlicheBeschreibung.setEditable(false);
        tfAusfuehrlicheBeschreibung.setText("");

        btnSpeichern.setVisible(false);
        btnLoeschen.setVisible(false);
        btnAbbrechen.setVisible(false);

        pflegeModus = 0;
        tfSet.requestFocus();

    }

    /**
     * ******************Aktionen auf Oberfläche************************.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        setInBearbeitungSpeichern();
    }

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {

        setzePflegeModusAuf0();
    }

    /**
     * Clicked aktivieren.
     *
     * @param event the event
     */
    @FXML
    void clickedAktivieren(ActionEvent event) {
        speichernGeraeteSet();
    }

    /**
     * On key pressed set nummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedSetNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && pflegeModus == 0) {
            einlesenAnzeigenSetZurBearbeitung();
        }

    }

    /**
     * Clicked bearbeiten.
     *
     * @param event the event
     */
    @FXML
    void clickedBearbeiten(ActionEvent event) {
        einlesenAnzeigenSetZurBearbeitung();
    }

    /**
     * Clicked loeschen.
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschen(ActionEvent event) {
        loescheSetInBearbeitung();
    }

    /**
     * Clicked neues set.
     *
     * @param event the event
     */
    @FXML
    void clickedNeuesSet(ActionEvent event) {
        neuesSetVorbereiten();
    }

}
