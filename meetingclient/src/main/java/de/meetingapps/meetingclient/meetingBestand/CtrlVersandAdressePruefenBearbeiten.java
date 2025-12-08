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
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEVersandAdresseKorrigieren;
import de.meetingapps.meetingportal.meetComWE.WEVersandAdresseKorrigierenRC;
import de.meetingapps.meetingportal.meetComWE.WEVersandAdressePruefenGet;
import de.meetingapps.meetingportal.meetComWE.WEVersandAdressePruefenGetRC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The Class CtrlVersandAdressePruefenBearbeiten.
 */
public class CtrlVersandAdressePruefenBearbeiten {

    /** The nur internet. */
    private boolean nurInternet;

    /** The nur noch nicht geprueft. */
    private boolean nurNochNichtGeprueft;

    /** The nur abweichende eingegeben. */
    private boolean nurAbweichendeEingegeben;

    /** The anz saetze. */
    private int anzSaetze = 0;

    /** The angezeigter satz. */
    private int angezeigterSatz = 0;

    /** The ergebnis array meldung. */
    private EclMeldung ergebnisArrayMeldung[] = null;

    /** The ergebnis array personen nat jur versandadresse. */
    private EclPersonenNatJurVersandadresse ergebnisArrayPersonenNatJurVersandadresse[] = null;

    /** The ergebnis array willenserklaerung. */
    private EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;

    /** The ergebnis array willenserklaerung zusatz. */
    private EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;

    /** The ergebnis array anrede. */
    private EclAnrede ergebnisArrayAnrede[] = null;

    /** The ergebnis array staaten. */
    private EclStaaten ergebnisArrayStaaten[] = null;

    /** The ergebnis array aktienregister. */
    private EclAktienregister ergebnisArrayAktienregister[] = null;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** The btn speichern weiter. */
    @FXML
    private Button btnSpeichernWeiter;

    /** The btn verwerfen weiter. */
    @FXML
    private Button btnVerwerfenWeiter;

    /** The tf EA anrede. */
    @FXML
    private TextField tfEAAnrede;

    /** The tf EA titel. */
    @FXML
    private TextField tfEATitel;

    /** The tf EA name. */
    @FXML
    private TextField tfEAName;

    /** The tf EA vorname. */
    @FXML
    private TextField tfEAVorname;

    /** The tf EA strasse. */
    @FXML
    private TextField tfEAStrasse;

    /** The tf EAPLZ. */
    @FXML
    private TextField tfEAPLZ;

    /** The tf EA ort. */
    @FXML
    private TextField tfEAOrt;

    /** The tf EA land. */
    @FXML
    private TextField tfEALand;

    /** The tf EV name. */
    @FXML
    private TextField tfEVName;

    /** The tf EV vorname. */
    @FXML
    private TextField tfEVVorname;

    /** The tf EV strasse. */
    @FXML
    private TextField tfEVStrasse;

    /** The tf EVPLZ. */
    @FXML
    private TextField tfEVPLZ;

    /** The tf EV ort. */
    @FXML
    private TextField tfEVOrt;

    /** The tf EV land. */
    @FXML
    private TextField tfEVLand;

    /** The tf MV zeile 1. */
    @FXML
    private TextField tfMVZeile1;

    /** The tf MV zeile 2. */
    @FXML
    private TextField tfMVZeile2;

    /** The tf MV zeile 3. */
    @FXML
    private TextField tfMVZeile3;

    /** The tf MV zeile 4. */
    @FXML
    private TextField tfMVZeile4;

    /** The tf MV zeile 5. */
    @FXML
    private TextField tfMVZeile5;

    /** The tf KV zeile 1. */
    @FXML
    private TextField tfKVZeile1;

    /** The tf KV zeile 2. */
    @FXML
    private TextField tfKVZeile2;

    /** The tf KV zeile 3. */
    @FXML
    private TextField tfKVZeile3;

    /** The tf KV zeile 4. */
    @FXML
    private TextField tfKVZeile4;

    /** The tf KV zeile 5. */
    @FXML
    private TextField tfKVZeile5;

    /** The tg eintragung aktionaer. */
    @FXML
    private RadioButton tgEintragungAktionaer;

    /** The tgg wahl. */
    @FXML
    private ToggleGroup tggWahl;

    /** The tg eintragung versandadresse. */
    @FXML
    private RadioButton tgEintragungVersandadresse;

    /** The tg manuelle versandadresse. */
    @FXML
    private RadioButton tgManuelleVersandadresse;

    /** The tg korrigierte versandadresse. */
    @FXML
    private RadioButton tgKorrigierteVersandadresse;

    /** The tf meldung. */
    @FXML
    private TextArea tfMeldung;

    /** The tf satz in bearbeitung. */
    @FXML
    private TextField tfSatzInBearbeitung;

    /** The tf saetze insgesamt. */
    @FXML
    private TextField tfSaetzeInsgesamt;

    /** The tf aktionaersnummer. */
    @FXML
    private TextField tfAktionaersnummer;

    /** The tf melde ident. */
    @FXML
    private TextField tfMeldeIdent;

    /** The tf EA zusatz 2. */
    @FXML
    private TextField tfEAZusatz2;

    /** The tf EA zusatz 1. */
    @FXML
    private TextField tfEAZusatz1;

    /** The tf EV titel. */
    @FXML
    private TextField tfEVTitel;

    /** The tf EV anrede. */
    @FXML
    private TextField tfEVAnrede;

    /** The tf EV name 2. */
    @FXML
    private TextField tfEVName2;

    /** The tf EV name 3. */
    @FXML
    private TextField tfEVName3;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnZurueck != null
                : "fx:id=\"btnZurueck\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert btnSpeichernWeiter != null
                : "fx:id=\"btnSpeichernWeiter\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert btnVerwerfenWeiter != null
                : "fx:id=\"btnVerwerfenWeiter\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAAnrede != null
                : "fx:id=\"tfEAAnrede\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEATitel != null
                : "fx:id=\"tfEATitel\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAName != null
                : "fx:id=\"tfEAName\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAVorname != null
                : "fx:id=\"tfEAVorname\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAStrasse != null
                : "fx:id=\"tfEAStrasse\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAPLZ != null
                : "fx:id=\"tfEAPLZ\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAOrt != null
                : "fx:id=\"tfEAOrt\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEALand != null
                : "fx:id=\"tfEALand\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVName != null
                : "fx:id=\"tfEVName\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVVorname != null
                : "fx:id=\"tfEVVorname\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVStrasse != null
                : "fx:id=\"tfEVStrasse\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVPLZ != null
                : "fx:id=\"tfEVPLZ\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVOrt != null
                : "fx:id=\"tfEVOrt\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVLand != null
                : "fx:id=\"tfEVLand\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMVZeile1 != null
                : "fx:id=\"tfMVZeile1\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMVZeile2 != null
                : "fx:id=\"tfMVZeile2\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMVZeile3 != null
                : "fx:id=\"tfMVZeile3\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMVZeile4 != null
                : "fx:id=\"tfMVZeile4\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMVZeile5 != null
                : "fx:id=\"tfMVZeile5\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfKVZeile1 != null
                : "fx:id=\"tfKVZeile1\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfKVZeile2 != null
                : "fx:id=\"tfKVZeile2\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfKVZeile3 != null
                : "fx:id=\"tfKVZeile3\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfKVZeile4 != null
                : "fx:id=\"tfKVZeile4\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfKVZeile5 != null
                : "fx:id=\"tfKVZeile5\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tgEintragungAktionaer != null
                : "fx:id=\"tgEintragungAktionaer\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tggWahl != null
                : "fx:id=\"tggWahl\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tgEintragungVersandadresse != null
                : "fx:id=\"tgEintragungVersandadresse\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tgManuelleVersandadresse != null
                : "fx:id=\"tgManuelleVersandadresse\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tgKorrigierteVersandadresse != null
                : "fx:id=\"tgKorrigierteVersandadresse\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMeldung != null
                : "fx:id=\"tfMeldung\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfSatzInBearbeitung != null
                : "fx:id=\"tfSatzInBearbeitung\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfSaetzeInsgesamt != null
                : "fx:id=\"tfSaetzeInsgesamt\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfMeldeIdent != null
                : "fx:id=\"tfMeldeIdent\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAZusatz2 != null
                : "fx:id=\"tfEAZusatz2\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEAZusatz1 != null
                : "fx:id=\"tfEAZusatz1\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVTitel != null
                : "fx:id=\"tfEVTitel\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVAnrede != null
                : "fx:id=\"tfEVAnrede\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVName2 != null
                : "fx:id=\"tfEVName2\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";
        assert tfEVName3 != null
                : "fx:id=\"tfEVName3\" was not injected: check your FXML file 'VersandAdressePruefenBearbeiten.fxml'.";

        /**************** Ab hier individuell ***********************************/
        tfSaetzeInsgesamt.setText(Integer.toString(anzSaetze));
        tfSatzInBearbeitung.setText(Integer.toString(angezeigterSatz));

        tfKVZeile1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfKVZeile1.getText().isEmpty()) {
                tgKorrigierteVersandadresse.setSelected(true);
            }
        });

        tfKVZeile2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfKVZeile2.getText().isEmpty()) {
                tgKorrigierteVersandadresse.setSelected(true);
            }
        });

        tfKVZeile3.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfKVZeile3.getText().isEmpty()) {
                tgKorrigierteVersandadresse.setSelected(true);
            }
        });

        tfKVZeile4.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfKVZeile4.getText().isEmpty()) {
                tgKorrigierteVersandadresse.setSelected(true);
            }
        });

        tfKVZeile5.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!tfKVZeile5.getText().isEmpty()) {
                tgKorrigierteVersandadresse.setSelected(true);
            }
        });

        satzAnzeigen();

    }

    /**
     * Clicked speichern weiter.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichernWeiter(ActionEvent event) {
        if (angezeigterSatz > anzSaetze) {
            return;
        }

        /*Speichern*/

        if (tgKorrigierteVersandadresse.isSelected() == true) {
            if (tfKVZeile1.getText().isEmpty() && tfKVZeile2.getText().isEmpty() && tfKVZeile3.getText().isEmpty()
                    && tfKVZeile4.getText().isEmpty() && tfKVZeile5.getText().isEmpty()) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Korrigierte Versandadresse markiert, aber nicht gefüllt!");
                return;
            }
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresseUeberprueft = 2;
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEKUeberprueft = 99;
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse1Ueberprueft = tfKVZeile1.getText();
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse2Ueberprueft = tfKVZeile2.getText();
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse3Ueberprueft = tfKVZeile3.getText();
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse4Ueberprueft = tfKVZeile4.getText();
            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse5Ueberprueft = tfKVZeile5.getText();
        } else {

            if (!tfKVZeile1.getText().isEmpty() || !tfKVZeile2.getText().isEmpty() || !tfKVZeile3.getText().isEmpty()
                    || !tfKVZeile4.getText().isEmpty() || !tfKVZeile5.getText().isEmpty()) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Korrigierte Versandadresse nicht markiert, aber gefüllt!");
                return;
            }

            ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresseUeberprueft = 1;
            if (tgEintragungAktionaer.isSelected() == true) {
                if (ergebnisArrayPersonenNatJurVersandadresse[angezeigterSatz - 1].versandAbweichend == 1) {
                    ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEKUeberprueft = 98;
                } else {
                    ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEKUeberprueft = 1;
                }
            }
            if (tgEintragungVersandadresse.isSelected() == true) {
                ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEKUeberprueft = 1;
            }
            if (tgManuelleVersandadresse.isSelected() == true) {
                ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEKUeberprueft = 2;
            }

            /*Nun prüfen, ob tatsächlich durch das Korrigieren eine neue Versandadresse ausgewählt wurde*/
            if (ergebnisArrayWillenserklaerungZusatz[angezeigterSatz
                    - 1].versandartEKUeberprueft != ergebnisArrayWillenserklaerungZusatz[angezeigterSatz
                            - 1].versandartEK) {
                ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresseUeberprueft = 2;
            } else {
                ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresseUeberprueft = 1;
            }

        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/

        WEVersandAdresseKorrigieren weVersandAdresseKorrigieren = new WEVersandAdresseKorrigieren();
        weVersandAdresseKorrigieren.setWeLoginVerify(weLoginVerify);
        weVersandAdresseKorrigieren.setWillenserklaerung(ergebnisArrayWillenserklaerung[angezeigterSatz - 1]);
        weVersandAdresseKorrigieren
                .setWillenserklaerungZusatz(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1]);

        WEVersandAdresseKorrigierenRC weVersandAdresseKorrigierenRC = wsClient
                .versandAdresseKorrigieren(weVersandAdresseKorrigieren);
        if (weVersandAdresseKorrigierenRC.getRc() < 1) {
            tfMeldung.setText(
                    "Speichern dieses Satzes nicht möglich wg. Fehlercode " + weVersandAdresseKorrigierenRC.getRc()
                            + CaFehler.getFehlertext(weVersandAdresseKorrigierenRC.getRc(), 0)
                            + ". Bitte abbrechen und wiederholen.");
            return;
        }

        angezeigterSatz++;
        satzAnzeigen();
    }

    /**
     * Clicked verwerfen weiter.
     *
     * @param event the event
     */
    @FXML
    void clickedVerwerfenWeiter(ActionEvent event) {
        if (angezeigterSatz > anzSaetze) {
            return;
        }
        angezeigterSatz++;
        satzAnzeigen();
    }

    /**
     * Clicked zurueck.
     *
     * @param event the event
     */
    @FXML
    void clickedZurueck(ActionEvent event) {
        if (angezeigterSatz <= 1) {
            return;
        }
        angezeigterSatz--;
        satzAnzeigen();

    }

    /**
     * Satz anzeigen.
     */
    private void satzAnzeigen() {
        tfEAAnrede.setText("");
        tfEATitel.setText("");
        tfEAName.setText("");
        tfEAVorname.setText("");
        tfEAZusatz2.setText("");
        tfEAZusatz1.setText("");
        tfEAStrasse.setText("");
        tfEAPLZ.setText("");
        tfEAOrt.setText("");
        tfEALand.setText("");

        tfEVTitel.setText("");
        tfEVAnrede.setText("");

        tfEVName2.setText("");
        tfEVName3.setText("");
        tfEVName.setText("");
        tfEVVorname.setText("");
        tfEVStrasse.setText("");
        tfEVPLZ.setText("");
        tfEVOrt.setText("");
        tfEVLand.setText("");
        tfMVZeile1.setText("");
        tfMVZeile2.setText("");
        tfMVZeile3.setText("");
        tfMVZeile4.setText("");
        tfMVZeile5.setText("");
        tfKVZeile1.setText("");
        tfKVZeile2.setText("");
        tfKVZeile3.setText("");
        tfKVZeile4.setText("");
        tfKVZeile5.setText("");
        tfMeldung.setText("");
        tfAktionaersnummer.setText("");
        tfMeldeIdent.setText("");

        tgEintragungAktionaer.setDisable(true);
        tgEintragungAktionaer.setSelected(false);
        tgEintragungVersandadresse.setDisable(false);
        tgEintragungVersandadresse.setSelected(false);
        tgManuelleVersandadresse.setDisable(false);
        tgManuelleVersandadresse.setSelected(false);
        tgKorrigierteVersandadresse.setDisable(false);
        tgKorrigierteVersandadresse.setSelected(false);

        tfSatzInBearbeitung.setText(Integer.toString(angezeigterSatz));

        if (anzSaetze == 0) {
            tfMeldung.setText("Keine Sätze zum Prüfen vorhanden!");
            return;
        }
        if (angezeigterSatz > anzSaetze) {
            tfMeldung.setText("Keine weiteren Sätze zum Bearbeiten!");
            return;
        }

        /*Eingetragener Aktionär immer vorhanden!*/
        tfEAAnrede.setText(ergebnisArrayAnrede[angezeigterSatz - 1].anredentext);
        if (ergebnisArrayAktienregister[angezeigterSatz - 1].istJuristischePerson == 1) {
            tfEATitel.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].name1);
            tfEAName.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].name2);
            tfEAVorname.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].name3);
        } else {
            tfEATitel.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].titel);
            tfEAName.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].nachname);
            tfEAVorname.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].vorname);
        }
        //    	tfEAZusatz1.setText(ergebnisArrayPersonenNatJur[angezeigterSatz-1].zusatz1);
        //    	tfEAZusatz2.setText(ergebnisArrayPersonenNatJur[angezeigterSatz-1].zusatz2);
        tfEAStrasse.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].strasse);
        tfEAPLZ.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].postleitzahl);
        tfEAOrt.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].ort);
        tfEALand.setText(ergebnisArrayStaaten[angezeigterSatz - 1].nameDE);
        tfAktionaersnummer.setText(ergebnisArrayMeldung[angezeigterSatz - 1].aktionaersnummer);
        tfMeldeIdent.setText(Integer.toString(ergebnisArrayMeldung[angezeigterSatz - 1].meldungsIdent));
        //        tgEintragungAktionaer.setSelected(true);

        /*Versandadresse im Aktienregister - immer vorhanden*/
        tfEVAnrede.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile1);
        tfEVTitel.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile2);
        tfEVName.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile3);
        tfEVName2.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile4);
        tfEVName3.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile5);
        tfEVVorname.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile6);
        tfEVStrasse.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile7);
        tfEVPLZ.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile8);
        tfEVOrt.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile9);
        tfEVLand.setText(ergebnisArrayAktienregister[angezeigterSatz - 1].adresszeile10);
        tgEintragungVersandadresse.setSelected(true);

        if (ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandartEK == 2) {
            tfMVZeile1.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse1);
            tfMVZeile2.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse2);
            tfMVZeile3.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse3);
            tfMVZeile4.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse4);
            tfMVZeile5.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse5);
            tgManuelleVersandadresse.setSelected(true);
        } else {
            tgManuelleVersandadresse.setDisable(true);
        }

        tfKVZeile1.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse1Ueberprueft);
        tfKVZeile2.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse2Ueberprueft);
        tfKVZeile3.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse3Ueberprueft);
        tfKVZeile4.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse4Ueberprueft);
        tfKVZeile5.setText(ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresse5Ueberprueft);
        if (ergebnisArrayWillenserklaerungZusatz[angezeigterSatz - 1].versandadresseUeberprueft == 2) {
            tgKorrigierteVersandadresse.setSelected(true);

        }

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage              the eigene stage
     * @param pNurInternet              the nur internet
     * @param pNurNochNichtGeprueft     the nur noch nicht geprueft
     * @param pNurAbweichendeEingegeben the nur abweichende eingegeben
     */
    public void init(Stage pEigeneStage, boolean pNurInternet, boolean pNurNochNichtGeprueft,
            boolean pNurAbweichendeEingegeben) {

        eigeneStage = pEigeneStage;

        nurInternet = pNurInternet;
        nurNochNichtGeprueft = pNurNochNichtGeprueft;
        nurAbweichendeEingegeben = pNurAbweichendeEingegeben;

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/

        WEVersandAdressePruefenGet weVersandAdressePruefenGet = new WEVersandAdressePruefenGet();
        weVersandAdressePruefenGet.setWeLoginVerify(weLoginVerify);
        weVersandAdressePruefenGet.setNurInternet(nurInternet);
        weVersandAdressePruefenGet.setNurNochNichtGeprueft(nurNochNichtGeprueft);
        weVersandAdressePruefenGet.setNurAbweichendeEingegeben(nurAbweichendeEingegeben);

        WEVersandAdressePruefenGetRC weVersandAdressePruefenGetRC = wsClient
                .versandAdressePruefenGet(weVersandAdressePruefenGet);

        ergebnisArrayMeldung = weVersandAdressePruefenGetRC.getErgebnisArrayMeldung();
        ergebnisArrayPersonenNatJurVersandadresse = weVersandAdressePruefenGetRC
                .getErgebnisArrayPersonenNatJurVersandadresse();
        ergebnisArrayWillenserklaerung = weVersandAdressePruefenGetRC.getErgebnisArrayWillenserklaerung();
        ergebnisArrayWillenserklaerungZusatz = weVersandAdressePruefenGetRC.getErgebnisArrayWillenserklaerungZusatz();
        ergebnisArrayAnrede = weVersandAdressePruefenGetRC.getErgebnisArrayAnrede();
        ergebnisArrayStaaten = weVersandAdressePruefenGetRC.getErgebnisArrayStaaten();
        ergebnisArrayAktienregister = weVersandAdressePruefenGetRC.getErgebnisArrayAktienregister();

        anzSaetze = ergebnisArrayMeldung.length;
        if (anzSaetze == 0) {
            angezeigterSatz = 0;
        } else {
            angezeigterSatz = 1;
        }
    }

}
