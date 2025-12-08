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
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteKorrigieren;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlEineEintrittskarteKorrigieren.
 */
public class CtrlEineEintrittskarteKorrigieren extends CtrlRootWK {

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

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfFehlermeldung != null
                : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfNameVorname != null
                : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfOrt != null
                : "fx:id=\"tfOrt\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAktien != null
                : "fx:id=\"tfAktien\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfVertreterName != null
                : "fx:id=\"tfVertreterName\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfVertreterVorname != null
                : "fx:id=\"tfVertreterVorname\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfVertreterOrt != null
                : "fx:id=\"tfVertreterOrt\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAdressZeile1 != null
                : "fx:id=\"tfAdressZeile1\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAdressZeile2 != null
                : "fx:id=\"tfAdressZeile2\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAdressZeile3 != null
                : "fx:id=\"tfAdressZeile3\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAdressZeile4 != null
                : "fx:id=\"tfAdressZeile4\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";
        assert tfAdressZeile5 != null
                : "fx:id=\"tfAdressZeile5\" was not injected: check your FXML file 'EineEintrittskarteKorrigieren.fxml'.";

        /*************** Ab hier individuell **********************************/

        initialisierungAllgemein();

        /*zu bestehender Anmeldung zusätzliche EK*/
        tfAktionaersnummer.setText(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
        tfNameVorname.setText(eclTLoginDatenM.liefereNameVornameTitel());
        tfOrt.setText(eclTLoginDatenM.ort);
        tfAktien.setText(eclZugeordneteMeldung.aktionaerStimmenDE);
        setzeStatusVerarbeitung();

        /*In personNatJur steht immer Vertretername - falls vorhanden*/
        if (eclPersonenNatJur.ident != 0) {
            tfVertreterName.setText(eclPersonenNatJur.name);
            tfVertreterVorname.setText(eclPersonenNatJur.vorname);
            tfVertreterOrt.setText(eclPersonenNatJur.ort);
            vertreterPflegen = 1;
        }

        if (eclWillenserklaerung.willenserklaerung != KonstWillenserklaerung.vollmachtAnDritte) {
            /*Dann EK, ggf. mit Versandadresse?*/
            if (eclWillenserklaerungZusatz.versandartEK == 2) {
                tfAdressZeile1.setText(eclWillenserklaerungZusatz.versandadresse1);
                tfAdressZeile2.setText(eclWillenserklaerungZusatz.versandadresse2);
                tfAdressZeile3.setText(eclWillenserklaerungZusatz.versandadresse3);
                tfAdressZeile4.setText(eclWillenserklaerungZusatz.versandadresse4);
                tfAdressZeile5.setText(eclWillenserklaerungZusatz.versandadresse5);

                versandadressePflegen = 1;
            }
        }

        setzeStatusVerarbeitung();
        hauptFunktion = KonstPortalAktion.HAUPT_BEREITSANGEMELDET;
    }

    /**
     * Pruefe eingaben funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean pruefeEingaben_Funktion() {
        int l;

        if (vertreterPflegen == 1) {
            if (tfVertreterName.getText() == null || tfVertreterName.getText().isEmpty()) {
                tfFehlermeldung.setText("Bitte Vertretername eingeben!");
                return false;

            }

            if (tfVertreterOrt.getText() == null || tfVertreterOrt.getText().isEmpty()) {
                tfFehlermeldung.setText("Bitte Vertreterort eingeben!");
                return false;
            }

            l = CaString.laenge(tfVertreterName.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Vertretername zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfVertreterVorname.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Vertretervorname zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfVertreterOrt.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Vertreterort zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
        }

        if (versandadressePflegen == 1) {
            l = CaString.laenge(tfAdressZeile1.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Adresszeile 1 zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfAdressZeile2.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Adresszeile 2 zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfAdressZeile3.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Adresszeile 3 zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfAdressZeile4.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Adresszeile 4 zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
            l = CaString.laenge(tfAdressZeile5.getText());
            if (l > 80) {
                tfFehlermeldung.setText("Adresszeile 5 zu lang: " + l + " tatsächlich, 80 erlaubt!");
                return false;
            }
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

        if (vertreterPflegen == 1) {
            eclPersonenNatJur.name = tfVertreterName.getText();
            eclPersonenNatJur.vorname = tfVertreterVorname.getText();
            eclPersonenNatJur.ort = tfVertreterOrt.getText();
        }

        if (versandadressePflegen == 1) {
            eclWillenserklaerungZusatz.versandadresse1 = tfAdressZeile1.getText();
            eclWillenserklaerungZusatz.versandadresse2 = tfAdressZeile2.getText();
            eclWillenserklaerungZusatz.versandadresse3 = tfAdressZeile3.getText();
            eclWillenserklaerungZusatz.versandadresse4 = tfAdressZeile4.getText();
            eclWillenserklaerungZusatz.versandadresse5 = tfAdressZeile5.getText();
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        WEEintrittskarteKorrigieren weEintrittskarteKorrigieren = new WEEintrittskarteKorrigieren();
        weEintrittskarteKorrigieren.setWeLoginVerify(weLoginVerify);
        weEintrittskarteKorrigieren.setWillenserklaerung(eclWillenserklaerung);
        weEintrittskarteKorrigieren.setWillenserklaerungZusatz(eclWillenserklaerungZusatz);
        weEintrittskarteKorrigieren.setWillenserklaerung2(eclWillenserklaerung2);
        weEintrittskarteKorrigieren.setWillenserklaerungZusatz2(eclWillenserklaerungZusatz2);
        weEintrittskarteKorrigieren.setPersonNatJur(eclPersonenNatJur);

        WEEintrittskarteKorrigieren weEintrittskarteKorrigierenRC = wsClient
                .eintrittskarteKorrigierenPUT(weEintrittskarteKorrigieren);

        int rc = weEintrittskarteKorrigierenRC.getRc();
        if (rc < 0) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv:
                fehlerMeldungSpeichern = "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.";
                break;
            default:
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                break;
            }
            setzeStatusAbbruch();
            return false;
        }
        return true;
    }

    /**
     * Setze status aktionaersnummer eingeben funktion.
     */
    @Override
    void setzeStatusAktionaersnummerEingeben_Funktion() {
        tfVertreterName.setText("");
        tfVertreterVorname.setText("");
        tfVertreterOrt.setText("");
        tfAdressZeile1.setText("");
        tfAdressZeile2.setText("");
        tfAdressZeile3.setText("");
        tfAdressZeile4.setText("");
        tfAdressZeile5.setText("");

    }

    /**
     * Setze status verarbeitung funktion.
     */
    @Override
    void setzeStatusVerarbeitung_Funktion() {
        if (vertreterPflegen == 0) {
            tfVertreterName.setDisable(true);
            tfVertreterVorname.setDisable(true);
            tfVertreterOrt.setDisable(true);
        } else {
            tfVertreterName.setDisable(false);
            tfVertreterVorname.setDisable(false);
            tfVertreterOrt.setDisable(false);
        }

        if (versandadressePflegen == 0) {
            tfAdressZeile1.setDisable(true);
            tfAdressZeile2.setDisable(true);
            tfAdressZeile3.setDisable(true);
            tfAdressZeile4.setDisable(true);
            tfAdressZeile5.setDisable(true);
        } else {
            tfAdressZeile1.setDisable(false);
            tfAdressZeile2.setDisable(false);
            tfAdressZeile3.setDisable(false);
            tfAdressZeile4.setDisable(false);
            tfAdressZeile5.setDisable(false);
        }
    }

    /**
     * Setze status abbruch funktion.
     */
    @Override
    void setzeStatusAbbruch_Funktion() {
    }

    /** The ecl willenserklaerung. */
    private EclWillenserklaerung eclWillenserklaerung = null;

    /** The ecl willenserklaerung zusatz. */
    private EclWillenserklaerungZusatz eclWillenserklaerungZusatz = null;

    /** The ecl willenserklaerung 2. */
    private EclWillenserklaerung eclWillenserklaerung2 = null;

    /** The ecl willenserklaerung zusatz 2. */
    private EclWillenserklaerungZusatz eclWillenserklaerungZusatz2 = null;

    /** The ecl personen nat jur. */
    private EclPersonenNatJur eclPersonenNatJur = null;

    /** The vertreter pflegen. */
    private int vertreterPflegen = 0;

    /** The versandadresse pflegen. */
    private int versandadressePflegen = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage                 the eigene stage
     * @param pEclTLoginDaten              the ecl T login daten
     * @param zugeordneteMeldung           the zugeordnete meldung
     * @param pEclWillenserklaerung        the ecl willenserklaerung
     * @param pEclWillenserklaerungZusatz  the ecl willenserklaerung zusatz
     * @param pEclWillenserklaerung2       the ecl willenserklaerung 2
     * @param pEclWillenserklaerungZusatz2 the ecl willenserklaerung zusatz 2
     * @param pEclPersonenNatJur           the ecl personen nat jur
     */
    public void init(Stage pEigeneStage, EclTLoginDatenM pEclTLoginDaten,
            EclZugeordneteMeldungNeu zugeordneteMeldung,
            EclWillenserklaerung pEclWillenserklaerung, EclWillenserklaerungZusatz pEclWillenserklaerungZusatz,
            EclWillenserklaerung pEclWillenserklaerung2, EclWillenserklaerungZusatz pEclWillenserklaerungZusatz2,
            EclPersonenNatJur pEclPersonenNatJur) {
        eigeneStage = pEigeneStage;
        hauptFunktion = 2;
        eclTLoginDatenM = pEclTLoginDaten;
        eclZugeordneteMeldung = zugeordneteMeldung;
        eclWillenserklaerung = pEclWillenserklaerung;
        eclWillenserklaerungZusatz = pEclWillenserklaerungZusatz;
        eclWillenserklaerung2 = pEclWillenserklaerung2;
        eclWillenserklaerungZusatz2 = pEclWillenserklaerungZusatz2;
        eclPersonenNatJur = pEclPersonenNatJur;
    }

    /**
     * Pruefe nach einlesen funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean pruefeNachEinlesen_Funktion() {
        return true;
    }

}
