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
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
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
 * The Class CtrlEineEintrittskarteVollmacht.
 */
public class CtrlEineEintrittskarteVollmacht extends CtrlRootWK {

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
    
    @FXML
    private TextField tfVertreterStrasse;

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
        initialisierungAllgemein();

        /*Vorbereiten, dass Vertreterdaten automatisch in Vertreterzeilen übernommen werden*/
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
        
        tfVertreterStrasse.textProperty().addListener((observable, oldValue, newValue) -> {
            tfAdressZeile3.setText(newValue);
        });

        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
            /*zu bestehender Anmeldung zusätzliche EK*/
            tfAktionaersnummer.setText(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
            tfNameVorname.setText(eclTLoginDatenM.liefereNameVornameTitel());
            tfOrt.setText(eclTLoginDatenM.ort);
            tfAktien.setText(eclZugeordneteMeldung.aktionaerStimmenDE);
            setzeStatusVerarbeitung();
        }

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
     * Speichern funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean speichern_Funktion() {

        String hVertreterOrt = tfVertreterOrt.getText().trim();
        hVertreterOrt = CaString.trimZahlen(hVertreterOrt).trim();

        if (tfVertreterName.getText() == null || tfVertreterName.getText().isEmpty()) {
            fehlerMeldungSpeichern = "Bitte Vertretername eingeben!";
            return false;

        }

        if (hVertreterOrt == null || hVertreterOrt.isEmpty()) {
            fehlerMeldungSpeichern = "Bitte Vertreterort eingeben!";
            return false;
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

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEEintrittskarte weEintrittskarte = new WEEintrittskarte();
        weEintrittskarte.setVollmachtName(tfVertreterName.getText().trim());
        weEintrittskarte.setVollmachtVorname(tfVertreterVorname.getText().trim());
        weEintrittskarte.setVollmachtOrt(hVertreterOrt);

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

        weEintrittskarte.setWeLoginVerify(weLoginVerify);
        weEintrittskarte.ausgewaehlteHauptAktion = hauptFunktion;
        weEintrittskarte.ausgewaehlteAktion = KonstPortalAktion.EINE_EK_MIT_VOLLMACHT;

        weEintrittskarte.eintrittskarteVersandart = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER;
        if (!weEintrittskarte.getEintrittskarteAbweichendeAdresse1().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse2().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse3().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse4().isEmpty()
                || !weEintrittskarte.getEintrittskarteAbweichendeAdresse5().isEmpty()) {
            weEintrittskarte.eintrittskarteVersandart = KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN;

        }

        weEintrittskarte.anmeldeAktionaersnummer = eclTLoginDatenM.eclAktienregister.aktionaersnummer;
        weEintrittskarte.eclZugeordneteMeldung = eclZugeordneteMeldung;

        WEEintrittskarteRC weEintrittskarteRC = wsClient.eintrittskarte(weEintrittskarte);

        int rc = weEintrittskarteRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
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

        fehlerMeldungSpeichern = "Eintrittskarte Nr " + weEintrittskarteRC.getZutrittsIdent() + " ausgestellt. ";

        return true;
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage           the eigene stage
     * @param funktion               the funktion
     * @param pEclTLoginDaten        the ecl T login daten
     * @param pEclZugeordneteMeldung the ecl zugeordnete meldung
     */
    public void init(Stage pEigeneStage, int funktion, EclTLoginDatenM pEclTLoginDaten,
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
        eigeneStage = pEigeneStage;
        hauptFunktion = funktion;
        eclTLoginDatenM = pEclTLoginDaten;
        eclZugeordneteMeldung = pEclZugeordneteMeldung;
    }

    /**
     * Setze status aktionaersnummer eingeben funktion.
     */
    @Override
    void setzeStatusAktionaersnummerEingeben_Funktion() {
        tfVertreterName.setText("");
        tfVertreterVorname.setText("");
        tfVertreterStrasse.setText("");
        tfVertreterOrt.setText("");
        tfAdressZeile1.setText("");
        tfAdressZeile2.setText("");
        tfAdressZeile3.setText("");
        tfAdressZeile4.setText("");
        tfAdressZeile5.setText("");
        lblHinweise.setText("");

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
