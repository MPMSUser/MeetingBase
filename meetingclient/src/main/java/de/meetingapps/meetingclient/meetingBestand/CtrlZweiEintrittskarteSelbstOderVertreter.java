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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarte;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlZweiEintrittskarteSelbstOderVertreter.
 */
public class CtrlZweiEintrittskarteSelbstOderVertreter extends CtrlRootWK {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf vertreter name. */
    @FXML
    private TextField tfVertreterName;

    /** The tf vertreter vorname. */
    @FXML
    private TextField tfVertreterVorname;

    /** The tf vertreter ort. */
    @FXML
    private TextField tfVertreterOrt;

    /** The tf adress zeile 1. */
    @FXML
    private TextField tfAdressZeile1;

    /** The tf adress zeile 2. */
    @FXML
    private TextField tfAdressZeile2;

    /** The tf adress zeile 3. */
    @FXML
    private TextField tfAdressZeile3;

    /** The tf adress zeile 4. */
    @FXML
    private TextField tfAdressZeile4;

    /** The tf adress zeile 5. */
    @FXML
    private TextField tfAdressZeile5;

    /** The tf vertreter name 2. */
    @FXML
    private TextField tfVertreterName2;

    /** The tf vertreter vorname 2. */
    @FXML
    private TextField tfVertreterVorname2;

    /** The tf vertreter ort 2. */
    @FXML
    private TextField tfVertreterOrt2;

    /** The tf adress 2 zeile 1. */
    @FXML
    private TextField tfAdress2Zeile1;

    /** The tf adress 2 zeile 2. */
    @FXML
    private TextField tfAdress2Zeile2;

    /** The tf adress 2 zeile 3. */
    @FXML
    private TextField tfAdress2Zeile3;

    /** The tf adress 2 zeile 4. */
    @FXML
    private TextField tfAdress2Zeile4;

    /** The tf adress 2 zeile 5. */
    @FXML
    private TextField tfAdress2Zeile5;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfFehlermeldung != null
                : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfNameVorname != null
                : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfOrt != null
                : "fx:id=\"tfOrt\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAktien != null
                : "fx:id=\"tfAktien\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert btnEinlesen != null
                : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterName != null
                : "fx:id=\"tfVertreterName\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterVorname != null
                : "fx:id=\"tfVertreterVorname\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterOrt != null
                : "fx:id=\"tfVertreterOrt\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdressZeile1 != null
                : "fx:id=\"tfAdressZeile1\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdressZeile2 != null
                : "fx:id=\"tfAdressZeile2\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdressZeile3 != null
                : "fx:id=\"tfAdressZeile3\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdressZeile4 != null
                : "fx:id=\"tfAdressZeile4\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdressZeile5 != null
                : "fx:id=\"tfAdressZeile5\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterName2 != null
                : "fx:id=\"tfVertreterName2\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterVorname2 != null
                : "fx:id=\"tfVertreterVorname2\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfVertreterOrt2 != null
                : "fx:id=\"tfVertreterOrt2\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdress2Zeile1 != null
                : "fx:id=\"tfAdress2Zeile1\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdress2Zeile2 != null
                : "fx:id=\"tfAdress2Zeile2\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdress2Zeile3 != null
                : "fx:id=\"tfAdress2Zeile3\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdress2Zeile4 != null
                : "fx:id=\"tfAdress2Zeile4\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";
        assert tfAdress2Zeile5 != null
                : "fx:id=\"tfAdress2Zeile5\" was not injected: check your FXML file 'ZweiEintrittskarteSelbstOderVertreter.fxml'.";

        /*************** Ab hier individuell **********************************/
        initialisierungAllgemein();

        /*Vorbereiten, dass Vertreterdaten automatisch in Vertreterzeilen übernommen werden - Bereich 1*/
        tfVertreterName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfVertreterVorname.getText().isEmpty()) {
                tfAdressZeile2.setText(newValue);
            } else {
                tfAdressZeile2.setText(tfVertreterVorname.getText() + " " + newValue);
            }
        });

        tfVertreterVorname.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfVertreterVorname.getText().isEmpty()) {
                tfAdressZeile2.setText(tfVertreterName.getText());
            } else {
                tfAdressZeile2.setText(newValue + " " + tfVertreterName.getText());
            }
        });

        tfVertreterOrt.textProperty().addListener((observable, oldValue, newValue) -> {
            tfAdressZeile4.setText(newValue);
        });

        /*Vorbereiten, dass Vertreterdaten automatisch in Vertreterzeilen übernommen werden - Bereich 2*/
        tfVertreterName2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfVertreterVorname2.getText().isEmpty()) {
                tfAdress2Zeile2.setText(newValue);
            } else {
                tfAdress2Zeile2.setText(tfVertreterVorname2.getText() + " " + newValue);
            }
        });

        tfVertreterVorname2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfVertreterVorname2.getText().isEmpty()) {
                tfAdress2Zeile2.setText(tfVertreterName2.getText());
            } else {
                tfAdress2Zeile2.setText(newValue + " " + tfVertreterName2.getText());
            }
        });

        tfVertreterOrt2.textProperty().addListener((observable, oldValue, newValue) -> {
            tfAdress2Zeile4.setText(newValue);
        });

    }

    /**
     * Pruefe nach einlesen funktion.
     *
     * @return true, if successful
     */
    boolean pruefeNachEinlesen_Funktion() {
        if (eclTLoginDatenM.eclAktienregister.stimmen < 2) {
            fehlerMeldungSpeichern = "Aktionär hat zuwenig Aktien - Zwei Eintrittskarten nicht möglich!";
            return false;
        }

        return true;
    }

    /**
     * Speichern funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean speichern_Funktion() {

        String hVertreterOrt = tfVertreterOrt.getText().trim();
        hVertreterOrt = CaString.trimZahlen(hVertreterOrt).trim();

        String hVertreterOrt2 = tfVertreterOrt2.getText().trim();
        hVertreterOrt2 = CaString.trimZahlen(hVertreterOrt2).trim();

        if (!tfVertreterName.getText().isEmpty()) {
            if (hVertreterOrt.isEmpty()) {
                fehlerMeldungSpeichern = "Bitte Vertreterort eingeben!";
                return false;
            }
        }

        if (!hVertreterOrt.isEmpty()) {
            if (tfVertreterName.getText().isEmpty()) {
                fehlerMeldungSpeichern = "Bitte Vertretername eingeben!";
                return false;
            }
        }

        if (!tfVertreterName2.getText().isEmpty()) {
            if (hVertreterOrt2.isEmpty()) {
                fehlerMeldungSpeichern = "Bitte Vertreterort 2 eingeben!";
                return false;
            }
        }

        if (!hVertreterOrt2.isEmpty()) {
            if (tfVertreterName2.getText().isEmpty()) {
                fehlerMeldungSpeichern = "Bitte Vertretername 2 eingeben!";
                return false;
            }
        }

        int l;
        l = CaString.laenge(tfVertreterName.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertretername zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfVertreterVorname.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertretervorname zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(hVertreterOrt);
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertreterort zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdressZeile1.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adresszeile 1 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdressZeile2.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adresszeile 2 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdressZeile3.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adresszeile 3 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdressZeile4.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adresszeile 4 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdressZeile5.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adresszeile 5 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfVertreterName2.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertretername 2 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfVertreterVorname2.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertretervorname 2 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(hVertreterOrt2);
        if (l > 80) {
            fehlerMeldungSpeichern = "Vertreterort 2 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdress2Zeile1.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adress2zeile 1 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdress2Zeile2.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adress2zeile 2 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdress2Zeile3.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adress2zeile 3 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdress2Zeile4.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adress2zeile 4 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }
        l = CaString.laenge(tfAdress2Zeile5.getText());
        if (l > 80) {
            fehlerMeldungSpeichern = "Adress2zeile 5 zu lang: " + l + " tatsächlich, 80 erlaubt!";
            return false;
        }

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEEintrittskarte weEintrittskarte = new WEEintrittskarte();
        weEintrittskarte.setWeLoginVerify(weLoginVerify);

        weEintrittskarte.ausgewaehlteHauptAktion = hauptFunktion;
        weEintrittskarte.ausgewaehlteAktion = KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT;

        /*Eintrittskarte 1*/
        if (!tfVertreterName.getText().isEmpty()) {
            weEintrittskarte.setVollmachtName(tfVertreterName.getText().trim());
            weEintrittskarte.setVollmachtVorname(tfVertreterVorname.getText().trim());
            weEintrittskarte.setVollmachtOrt(hVertreterOrt.trim());
        }

        String[] zeilen1 = new String[6];
        zeilen1[1] = tfAdressZeile1.getText().trim();
        zeilen1[2] = tfAdressZeile2.getText().trim();
        zeilen1[3] = tfAdressZeile3.getText().trim();
        zeilen1[4] = tfAdressZeile4.getText().trim();
        zeilen1[5] = tfAdressZeile5.getText().trim();
        for (int i = 1; i <= 4; i++) {
            int gef = 0;
            while (gef <= 4 && zeilen1[i].isEmpty()) {
                gef++;
                for (int i1 = i; i1 <= 4; i1++) {
                    zeilen1[i1] = zeilen1[i1 + 1];
                }
            }
        }

        weEintrittskarte.setEintrittskarteAbweichendeAdresse1(zeilen1[1]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse2(zeilen1[2]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse3(zeilen1[3]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse4(zeilen1[4]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse5(zeilen1[5]);

        weEintrittskarte.eintrittskarteVersandart = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER;
        if (!weEintrittskarte.getEintrittskarteAbweichendeAdresse1().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse2().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse3().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse4().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse5().isEmpty()) {
            weEintrittskarte.eintrittskarteVersandart = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN;

        }

        /*Eintrittskarte 2*/
        if (!tfVertreterName2.getText().isEmpty()) {
            weEintrittskarte.setVollmachtName2(tfVertreterName2.getText().trim());
            weEintrittskarte.setVollmachtVorname2(tfVertreterVorname2.getText().trim());
            weEintrittskarte.setVollmachtOrt2(hVertreterOrt2.trim());
        }

        String[] zeilen2 = new String[6];
        zeilen2[1] = tfAdress2Zeile1.getText().trim();
        zeilen2[2] = tfAdress2Zeile2.getText().trim();
        zeilen2[3] = tfAdress2Zeile3.getText().trim();
        zeilen2[4] = tfAdress2Zeile4.getText().trim();
        zeilen2[5] = tfAdress2Zeile5.getText().trim();
        for (int i = 1; i <= 4; i++) {
            int gef = 0;
            while (gef <= 4 && zeilen2[i].isEmpty()) {
                gef++;
                for (int i1 = i; i1 <= 4; i1++) {
                    zeilen2[i1] = zeilen2[i1 + 1];
                }
            }
        }

        weEintrittskarte.setEintrittskarteAbweichendeAdresse12(zeilen2[1]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse22(zeilen2[2]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse32(zeilen2[3]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse42(zeilen2[4]);
        weEintrittskarte.setEintrittskarteAbweichendeAdresse52(zeilen2[5]);

        weEintrittskarte.eintrittskarteVersandart2 = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER;
        if (!weEintrittskarte.getEintrittskarteAbweichendeAdresse12().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse22().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse32().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse42().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse52().isEmpty()) {
            weEintrittskarte.eintrittskarteVersandart2 = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN;

        }

        weEintrittskarte.anmeldeAktionaersnummer = eclTLoginDatenM.eclAktienregister.aktionaersnummer;
        weEintrittskarte.eclZugeordneteMeldung = eclZugeordneteMeldung;

        WEEintrittskarteRC weEintrittskarteRC = wsClient.eintrittskarte(weEintrittskarte);

        int rc = weEintrittskarteRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv: {
                fehlerMeldungSpeichern = "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.";
                break;
            }
            default: {
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                break;
            }
            }
            setzeStatusAbbruch();
            return false;
        }

        fehlerMeldungSpeichern = "Eintrittskarte Nr " + weEintrittskarteRC.getZutrittsIdent() + " und "
                + weEintrittskarteRC.getZutrittsIdent2() + " ausgestellt. ";

        return true;
    }

    /**
     * Setze status aktionaersnummer eingeben.
     */
    void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(true);
        tfAktionaersnummer.setText("");
        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        tfFehlermeldung.setText("");
        tfAktionaersnummer.setEditable(true);
        tfAktionaersnummer.requestFocus();
        lblHinweise.setText("");

        tfVertreterName.setText("");
        tfVertreterVorname.setText("");
        tfVertreterOrt.setText("");
        tfAdressZeile1.setText("");
        tfAdressZeile2.setText("");
        tfAdressZeile3.setText("");
        tfAdressZeile4.setText("");
        tfAdressZeile5.setText("");

        tfVertreterName2.setText("");
        tfVertreterVorname2.setText("");
        tfVertreterOrt2.setText("");
        tfAdress2Zeile1.setText("");
        tfAdress2Zeile2.setText("");
        tfAdress2Zeile3.setText("");
        tfAdress2Zeile4.setText("");
        tfAdress2Zeile5.setText("");

    }

    /**
     * Setze status verarbeitung.
     */
    void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(false);
        btnAbbruch.setDisable(false);
        tfAktionaersnummer.setEditable(false);
        btnSpeichern.requestFocus();

    }

    /**
     * Setze status abbruch.
     */
    void setzeStatusAbbruch() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(false);
        tfAktionaersnummer.setEditable(false);
        btnAbbruch.requestFocus();

    }

    //   private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        //    	eigeneStage=pEigeneStage;

        hauptFunktion = KonstPortalAktion.HAUPT_NEUANMELDUNG;

    }

    /**
     * Pruefe eingaben funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean pruefeEingaben_Funktion() {
        return true;
    }

    /**
     * Setze status aktionaersnummer eingeben funktion.
     */
    @Override
    void setzeStatusAktionaersnummerEingeben_Funktion() {
    }

    /**
     * Setze status abbruch funktion.
     */
    @Override
    void setzeStatusAbbruch_Funktion() {
    }

    /**
     * Setze status verarbeitung funktion.
     */
    @Override
    void setzeStatusVerarbeitung_Funktion() {
    }

}
