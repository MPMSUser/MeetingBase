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
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEVollmachtDritteGet;
import de.meetingapps.meetingportal.meetComWE.WEVollmachtDritteGetRC;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlVollmachtAnDritte.
 */
public class CtrlVollmachtAnDritte extends CtrlRootWK {

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

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfFehlermeldung != null
                : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfNameVorname != null
                : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert btnEinlesen != null
                : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfVertreterName != null
                : "fx:id=\"tfVertreterName\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfVertreterVorname != null
                : "fx:id=\"tfVertreterVorname\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";
        assert tfVertreterOrt != null
                : "fx:id=\"tfVertreterOrt\" was not injected: check your FXML file 'VollmachtAnDritte.fxml'.";

        /*************** Ab hier individuell **********************************/
        initialisierungAllgemein();

        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
            /*zu bestehender Anmeldung zusätzliche EK*/
            tfAktionaersnummer.setText(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
            tfNameVorname.setText(eclTLoginDatenM.liefereNameVornameTitel());
            tfOrt.setText(eclTLoginDatenM.ort);
            tfAktien.setText(eclZugeordneteMeldung.aktionaerStimmenDE);
            setzeStatusVerarbeitung();
        }
        Platform.runLater(() -> tfVertreterName.requestFocus());
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

    /**
     * Pruefe eingaben funktion.
     *
     * @return true, if successful
     */
    @Override
    boolean pruefeEingaben_Funktion() {
        if (tfVertreterName.getText() == null || tfVertreterName.getText().isBlank()) {
            tfFehlermeldung.setText("Bitte Vertretername eingeben!");
            tfVertreterName.requestFocus();
            return false;
        } else if (tfVertreterOrt.getText() == null || tfVertreterOrt.getText().isBlank()) {
            tfFehlermeldung.setText("Bitte Vertreterort eingeben!");
            tfVertreterOrt.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Speichern funktion.
     *
     * @return true, if successful
     */
    boolean speichern_Funktion() {

        int l;
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

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEVollmachtDritteGet weVollmachtDritteGet = new WEVollmachtDritteGet();
        weVollmachtDritteGet.setVollmachtName(tfVertreterName.getText().trim());
        weVollmachtDritteGet.setVollmachtVorname(tfVertreterVorname.getText().trim());
        weVollmachtDritteGet.setVollmachtOrt(tfVertreterOrt.getText().trim());

        weVollmachtDritteGet.setWeLoginVerify(weLoginVerify);

        weVollmachtDritteGet.setAusgewaehlteHauptAktion(hauptFunktion);
        weVollmachtDritteGet.setAusgewaehlteAktion(KonstPortalAktion.VOLLMACHT_DRITTE);

        weVollmachtDritteGet.setEclZugeordneteMeldung(eclZugeordneteMeldung);

        WEVollmachtDritteGetRC weVollmachtDritteGetRC = wsClient.vollmachtDritteGet(weVollmachtDritteGet);

        int rc = weVollmachtDritteGetRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv: {
                tfFehlermeldung.setText(
                        "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            return false;
        }

        tfFehlermeldung.setText("Vertreter gespeichert. ");

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
