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
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteKlasse;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 * The Class CtrlParameterGeraeteSetKlasse.
 */
public class CtrlParameterGeraeteSetKlasse {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The combo set. */
    @FXML
    private ComboBox<EclGeraeteSet> comboSet;

    /** The tf bezeichnung set. */
    @FXML
    private TextField tfBezeichnungSet;

    /** The tf ausfuehrliche beschreibung set. */
    @FXML
    private TextArea tfAusfuehrlicheBeschreibungSet;

    /** The tf von arbeitsplatz. */
    @FXML
    private TextField tfVonArbeitsplatz;

    /** The tf bis arbeitsplatz. */
    @FXML
    private TextField tfBisArbeitsplatz;

    /** The combo klasse. */
    @FXML
    private ComboBox<EclGeraeteKlasse> comboKlasse;

    /** The btn zuordnen. */
    @FXML
    private Button btnZuordnen;

    /** The btn zuordnung loeschen. */
    @FXML
    private Button btnZuordnungLoeschen;

    /** The tf bezeichnung klasse. */
    @FXML
    private TextField tfBezeichnungKlasse;

    /** The tf ausfuehrliche beschreibung klasse. */
    @FXML
    private TextArea tfAusfuehrlicheBeschreibungKlasse;

    /** The scrpn vorhandene zuordnungen. */
    @FXML
    private ScrollPane scrpnVorhandeneZuordnungen;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The fehler. */
    private int fehler = 0;

    /** Wird von refresh_verfuegbareGeraetesets gefüllt. */
    private EclGeraeteSet[] arrayGeraeteSet = null;

    /** Wird von refresh_verfuegbareKlassen gefüllt. */
    private EclGeraeteKlasse[] arrayGeraeteKlassen = null;

    /** Geräte-Set, das gerade in Bearbeitung (Pflege) ist. */
    private EclGeraeteSet eclGeraeteSetInBearbeitung = null;

    /** Geräte-Klasse, die gerade in Bearbeitung (Pflege) ist. */
    private EclGeraeteKlasse eclGeraeteKlasse = null;

    /** The aktives set. */
    private int aktivesSet = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert comboSet != null : "fx:id=\"comboSet\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfBezeichnungSet != null : "fx:id=\"tfBezeichnungSet\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfAusfuehrlicheBeschreibungSet != null : "fx:id=\"tfAusfuehrlicheBeschreibungSet\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfVonArbeitsplatz != null : "fx:id=\"tfVonArbeitsplatz\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfBisArbeitsplatz != null : "fx:id=\"tfBisArbeitsplatz\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert comboKlasse != null : "fx:id=\"comboKlasse\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert btnZuordnen != null : "fx:id=\"btnZuordnen\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert btnZuordnungLoeschen != null : "fx:id=\"btnZuordnungLoeschen\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfBezeichnungKlasse != null : "fx:id=\"tfBezeichnungKlasse\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert tfAusfuehrlicheBeschreibungKlasse != null : "fx:id=\"tfAusfuehrlicheBeschreibungKlasse\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        assert scrpnVorhandeneZuordnungen != null : "fx:id=\"scrpnVorhandeneZuordnungen\" was not injected: check your FXML file 'ParameterGeraeteSetKlasse.fxml'.";
        
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
        /*Alle verfügbaren Gerätesets einlesen und Drop-Box füllen*/
        refresh_verfuegbareGeraetesets();
        zeigeGewaehltesSet();
        refresh_verfuegbareKlassen();
        zeigeZuordnungenZuSet();

        lDbBundle.closeAll();
        if (fehler < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, CaFehler.getFehlertext(fehler, 0));
        }

        comboSet.valueProperty().addListener(new ChangeListener<EclGeraeteSet>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, EclGeraeteSet alterWert,
                    EclGeraeteSet neuerWert) {
                if (neuerWert == null || neuerWert.ident == 0) {
                    return;
                }
                zeigeGewaehltesSet();
                lDbBundle.openAll();
                zeigeZuordnungenZuSet();
                lDbBundle.closeAll();
            }
        });

        comboKlasse.valueProperty().addListener(new ChangeListener<EclGeraeteKlasse>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, EclGeraeteKlasse alterWert,
                    EclGeraeteKlasse neuerWert) {
                if (neuerWert == null || neuerWert.ident == 0) {
                    return;
                }
                zeigeGewaehlteKlasse();
            }
        });

    }

    /**
     * Refresh verfuegbare geraetesets.
     */
    private void refresh_verfuegbareGeraetesets() {
        /*Aktives Set ermitteln und später vorbelegen*/
        fehler = 0;
        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            aktivesSet = Integer.parseInt(lDbBundle.dbParameter.ergebnisPosition(0).wert);
        }

        /*Combo-Box vorbereiten*/
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
        comboSet.setConverter(sc);
        comboSet.getSelectionModel().clearSelection();
        comboSet.getItems().clear();

        lDbBundle.dbGeraeteSet.read_all();
        int anzGeraeteSets = lDbBundle.dbGeraeteSet.anzErgebnis();
        arrayGeraeteSet = null;

        if (anzGeraeteSets > 0) {
            arrayGeraeteSet = lDbBundle.dbGeraeteSet.ergebnisArray;
            for (int i = 0; i < anzGeraeteSets; i++) {
                comboSet.getItems().add(arrayGeraeteSet[i]);
                if (aktivesSet == arrayGeraeteSet[i].ident) {
                    comboSet.setValue(arrayGeraeteSet[i]);
                }
            }
        }
    }

    /**
     * Zeige gewaehltes set.
     */
    private void zeigeGewaehltesSet() {
        eclGeraeteSetInBearbeitung = comboSet.getValue();
        if (eclGeraeteSetInBearbeitung == null) {
            tfBezeichnungSet.setText("");
            tfAusfuehrlicheBeschreibungSet.setText("");
        } else {
            tfBezeichnungSet.setText(eclGeraeteSetInBearbeitung.kurzBeschreibung);
            tfAusfuehrlicheBeschreibungSet.setText(eclGeraeteSetInBearbeitung.beschreibung);
        }
    }

    /**
     * Refresh verfuegbare klassen.
     */
    private void refresh_verfuegbareKlassen() {

        /*Combo-Box vorbereiten*/
        StringConverter<EclGeraeteKlasse> sc = new StringConverter<EclGeraeteKlasse>() {

            @Override
            public String toString(EclGeraeteKlasse pGeraeteKlasse) {
                if (pGeraeteKlasse == null) {
                    return "";
                }
                return Integer.toString(pGeraeteKlasse.ident) + " " + pGeraeteKlasse.kurzBeschreibung;
            }

            @Override
            public EclGeraeteKlasse fromString(String string) {
                return null;
            }
        };
        comboKlasse.setConverter(sc);
        comboKlasse.getSelectionModel().clearSelection();
        comboKlasse.getItems().clear();

        lDbBundle.dbGeraeteKlasse.read_all();
        int anzGeraeteKlassen = lDbBundle.dbGeraeteKlasse.anzErgebnis();
        arrayGeraeteKlassen = null;

        if (anzGeraeteKlassen > 0) {
            arrayGeraeteKlassen = lDbBundle.dbGeraeteKlasse.ergebnisArray;
            for (int i = 0; i < anzGeraeteKlassen; i++) {
                comboKlasse.getItems().add(arrayGeraeteKlassen[i]);
            }
        }
    }

    /**
     * Zeige gewaehlte klasse.
     */
    private void zeigeGewaehlteKlasse() {
        eclGeraeteKlasse = comboKlasse.getValue();
        if (eclGeraeteKlasse == null) {
            tfBezeichnungKlasse.setText("");
            tfAusfuehrlicheBeschreibungKlasse.setText("");
        } else {
            tfBezeichnungKlasse.setText(eclGeraeteKlasse.kurzBeschreibung);
            tfAusfuehrlicheBeschreibungKlasse.setText(eclGeraeteKlasse.beschreibung);
        }
    }

    /**
     * Zeige zuordnungen zu set.
     */
    private void zeigeZuordnungenZuSet() {
        scrpnVorhandeneZuordnungen.setContent(null);

        int anzGeraeteKlassen = 0;
        if (arrayGeraeteKlassen != null) {
            anzGeraeteKlassen = arrayGeraeteKlassen.length;
        }

        if (eclGeraeteSetInBearbeitung != null) {
            lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all(eclGeraeteSetInBearbeitung.ident);
            int anzZuordnungen = lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis();
            if (anzZuordnungen > 0) {
                GridPane grpnZuordnungen = new GridPane();
                grpnZuordnungen.setVgap(5);
                grpnZuordnungen.setHgap(5);

                for (int i = 0; i < anzZuordnungen; i++) {

                    EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = lDbBundle.dbGeraeteKlasseSetZuordnung
                            .ergebnisPosition(i);
                    Label lGeraeteNr = new Label();
                    lGeraeteNr.setText("Gerät " + Integer.toString(lGeraetKlasseSetZuordnung.geraeteNummer));
                    grpnZuordnungen.add(lGeraeteNr, 0, i);
                    Label lKlasseNr = new Label();
                    lKlasseNr.setText("Klasse " + Integer.toString(lGeraetKlasseSetZuordnung.geraeteKlasseIdent));
                    grpnZuordnungen.add(lKlasseNr, 1, i);

                    for (int i1 = 0; i1 < anzGeraeteKlassen; i1++) {
                        if (arrayGeraeteKlassen[i1].ident == lGeraetKlasseSetZuordnung.geraeteKlasseIdent) {
                            Label lKurzBeschreibung = new Label();
                            lKurzBeschreibung.setText(arrayGeraeteKlassen[i1].kurzBeschreibung);
                            grpnZuordnungen.add(lKurzBeschreibung, 2, i);
                        }
                    }
                }
                scrpnVorhandeneZuordnungen.setContent(grpnZuordnungen);

            }
        }
    }

    /** The bis geraet. */
    private int vonGeraet = 0, bisGeraet = 0;

    /**
     * Pruefe eingabe felder.
     *
     * @param pAuchKlasse the auch klasse
     * @return true, if successful
     */
    private boolean pruefeEingabeFelder(boolean pAuchKlasse) {
        vonGeraet = 0;
        bisGeraet = 0;

        /*Set ausgewählt?*/
        if (comboSet.getValue() == null) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte Set auswählen!");
            return false;
        }

        if (pAuchKlasse) {
            /*Klasse ausgewählt?*/
            if (comboKlasse.getValue() == null) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage, "Bitte Klasse auswählen!");
                return false;
            }
        }

        /*Von Gerät*/
        String hString = tfVonArbeitsplatz.getText();
        if (hString.isEmpty() || !CaString.isNummern(hString)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültige Von-Arbeitsplatznummer eingeben!");
            return false;
        }
        vonGeraet = Integer.parseInt(hString);
        if (vonGeraet < 1 || vonGeraet > 9999) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Von-Arbeitsplatznummer muß zwischen 1 und 9999 liegen!");
            return false;
        }

        /*Bis Gerät*/
        hString = tfBisArbeitsplatz.getText();
        if (hString.isEmpty()) {
            bisGeraet = vonGeraet;
            return true;
        }
        if (!CaString.isNummern(hString)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültige Bis-Arbeitsplatznummer eingeben!");
            return false;
        }
        bisGeraet = Integer.parseInt(hString);
        if (bisGeraet < 1 || bisGeraet > 9999) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bis-Arbeitsplatznummer muß zwischen 1 und 9999 liegen!");
            return false;
        }
        if (bisGeraet < vonGeraet) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bis-Arbeitsplatznummer muß größer als Von-Arbeitsplatznummer sein!");
            return false;
        }

        return true;
    }

    /**
     * vonGeraet/bisGeraet müssen besetzt sein, lDbBundle offen.
     *
     * @return true, if successful
     */
    private boolean pruefeObZuordnungBereitsVorhanden() {

        int gewaehltesSet = comboSet.getValue().ident;

        for (int i = vonGeraet; i <= bisGeraet; i++) {
            lDbBundle.dbGeraeteKlasseSetZuordnung.read(gewaehltesSet, i);
            if (lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis() > 0) {
                return true;
            }
        }
        return false;
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

    /********************Aktionen auf Oberfläche*************************/

    @FXML
    void clickedZuordnen(ActionEvent event) {
        if (!pruefeEingabeFelder(true)) {
            return;
        }
        lDbBundle.openAll();
        if (pruefeObZuordnungBereitsVorhanden()) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            boolean bRc = zeigeHinweis.zeige2Buttons(eigeneStage,
                    "Zuordnung für (ein) Gerät bereits vorhanden, Fortsetzen?", "Ja", "Nein");
            if (bRc == false) {
                lDbBundle.closeAll();
                return;
            }
        }

        int gewaehltesSet = comboSet.getValue().ident;
        int gewaehlteKlasse = comboKlasse.getValue().ident;

        for (int i = vonGeraet; i <= bisGeraet; i++) {
            lDbBundle.dbGeraeteKlasseSetZuordnung.delete(gewaehltesSet, i);
            EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = new EclGeraetKlasseSetZuordnung();
            lGeraetKlasseSetZuordnung.geraeteSetIdent = gewaehltesSet;
            lGeraetKlasseSetZuordnung.geraeteNummer = i;
            lGeraetKlasseSetZuordnung.geraeteKlasseIdent = gewaehlteKlasse;

            lDbBundle.dbGeraeteKlasseSetZuordnung.insert(lGeraetKlasseSetZuordnung);
        }

        tfVonArbeitsplatz.setText("");
        tfBisArbeitsplatz.setText("");

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();

        zeigeZuordnungenZuSet();
        lDbBundle.closeAll();

    }

    /**
     * Clicked zuordnung loeschen.
     *
     * @param event the event
     */
    @FXML
    void clickedZuordnungLoeschen(ActionEvent event) {
        if (!pruefeEingabeFelder(false)) {
            return;
        }

        lDbBundle.openAll();

        int gewaehltesSet = comboSet.getValue().ident;

        for (int i = vonGeraet; i <= bisGeraet; i++) {
            lDbBundle.dbGeraeteKlasseSetZuordnung.delete(gewaehltesSet, i);
        }

        tfVonArbeitsplatz.setText("");
        tfBisArbeitsplatz.setText("");

        zeigeZuordnungenZuSet();
        lDbBundle.closeAll();

    }

}
