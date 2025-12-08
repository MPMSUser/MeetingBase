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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEAnmeldung;
import de.meetingapps.meetingportal.meetComWE.WEAnmeldungRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlAnmeldung.
 */
public class CtrlAnmeldung extends CtrlRootWK {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The quelle. */
    String quelle = "";

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfNameVorname != null
                : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfOrt != null
                : "fx:id=\"tfOrt\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfAktien != null
                : "fx:id=\"tfAktien\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert btnEinlesen != null
                : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfFehlermeldung != null
                : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'EineEintrittskarteSelbst.fxml'.";
        assert btnTextDateiLesen != null
                : "fx:id=\"btnTextDateiLesen\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        assert btnScanLesen != null
                : "fx:id=\"btnScanLesen\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        assert tfDateiname != null
                : "fx:id=\"tfDateiname\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        assert btnDateiWaehlen != null
                : "fx:id=\"btnDateiWaehlen\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";

        /*************** Ab hier individuell **********************************/

        initialisierungAllgemein();

    }

    /** The scan datei pfad. */
    private String scanDateiPfad = "C:";

    /**
     * Clicked datei waehlen.
     *
     * @param event the event
     */
    @FXML
    void clickedDateiWaehlen(ActionEvent event) {

        scanDateiPfad = "C:";

        try {

            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(scanDateiPfad));
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files", "*.txt");
            fc.getExtensionFilters().add(filter);
            File f = fc.showOpenDialog(null);

            scanDateiPfad = f.getPath();
            tfDateiname.setText(f.getPath());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Pruefe eingaben funktion.
     *
     * @return true, if successful
     */
    boolean pruefeEingaben_Funktion() {
        return true;
    }

    /**
     * Speichern funktion.
     *
     * @return true, if successful
     */
    boolean speichern_Funktion() {
        int rc = 0;

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEAnmeldung weAnmeldung = new WEAnmeldung();
        weAnmeldung.setWeLoginVerify(weLoginVerify);

        weAnmeldung.anmeldeAktionaersnummer = eclTLoginDatenM.eclAktienregister.aktionaersnummer;

        weAnmeldung.setQuelle(quelle);

        WEAnmeldungRC weAnmeldungRC = wsClient.anmeldung(weAnmeldung);

        rc = weAnmeldungRC.getRc();
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
            return false;
        }

        fehlerMeldungSpeichern = "Anmeldung durchgeführt ";

        return true;

    }

    /**
     * Clicked scan lesen.
     *
     * @param event the event
     */
    @FXML
    void clickedScanLesen(ActionEvent event) {

        String fehlertext = "";
        boolean rc = false;
        if (pruefeAllgemeineEingaben() == false) {
            return;
        }

        String importDateiname = tfDateiname.getText();
        int gesamtanzahl = 0;
        int fehleranzahl = 0;

        DbBundle lDbBundle = new DbBundle();
        CaDateiWrite protokollDatei = null;
        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(lDbBundle, "eintrittskartenScannen");
        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        protokollDatei.ausgabe("Scan-Datei : " + importDateiname);
        protokollDatei.newline();

        try {

            FileReader fr = new FileReader(importDateiname);
            BufferedReader br = new BufferedReader(fr);

            try {
                String zeile = null;
                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    if (!zeile.isEmpty()) {

                        String[] zeileSplit = zeile.split(";");
                        String aktionaersnummer = zeileSplit[0].replace('"', ' ').trim();
                        if (!aktionaersnummer.equals("Barcode")) {

                            gesamtanzahl++;
                            fehlerMeldungSpeichern = "";

                            tfAktionaersnummer.setText(aktionaersnummer);
                            String datei = zeileSplit[1].replace('"', ' ').trim();
                            String position = zeileSplit[2].replace('"', ' ').trim();
                            String ek = zeileSplit[3].replace('"', ' ').trim();
                            String vollmacht = zeileSplit[4].replace('"', ' ').trim();
                            vollmacht = "0";
                            String briefwahlVdg = zeileSplit[5].replace('"', ' ').trim();

                            quelle = importDateiname + "#" + datei + "#" + position;

                            if (ek.equals("0") && vollmacht.equals("0") && briefwahlVdg.equals("0")) { // 000
                                rc = false; // aussteuern
                            } else if (ek.equals("0") && vollmacht.equals("0")
                                    && (briefwahlVdg.equals("1") || briefwahlVdg.equals("2"))) { // 001
                                if (briefwahlVdg.equals("1")) {
                                    rc = false; // Funktion Briefwahl
                                } else if (briefwahlVdg.equals("2")) {
                                    rc = false; // Funktion SRV
                                }
                            } else if ((ek.equals("1") || ek.equals("2"))) { // 1 egal egal
                                if (ek.equals("1")) {
                                    rc = true; // Funktion eine EK
                                } else if (ek.equals("2")) {
                                    rc = false;// Funktion zwei EKs
                                }
                            } else {
                                rc = false; // aussteuern
                            }

                            if (rc == false) {
                                protokollDatei.ausgabe("Aussteuern Position " + position + ", " + aktionaersnummer
                                        + " : falsche Willenserklärung");
                                protokollDatei.newline();
                                fehleranzahl++;
                            } else { /*Nun Speichern*/
                                rc = einlesen();
                                if (rc == false) {
                                    protokollDatei.ausgabe("Fehler bei Position " + position + ", " + aktionaersnummer
                                            + " :" + fehlerMeldungSpeichern);
                                    protokollDatei.newline();
                                    fehleranzahl++;
                                } else {
                                    rc = speichern_Funktion();
                                    if (rc == false) {
                                        protokollDatei.ausgabe("Fehler bei Position " + position + ", "
                                                + aktionaersnummer + " :" + fehlerMeldungSpeichern);
                                        protokollDatei.newline();
                                        fehleranzahl++;
                                    } else {
                                        protokollDatei.ausgabe("Ok: Position " + position + ", " + aktionaersnummer
                                                + " " + fehlerMeldungSpeichern);
                                        protokollDatei.newline();
                                    }
                                }
                            }
                        }
                    }
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

        } catch (FileNotFoundException ex) {
            fehlertext = "Datei nicht gefunden!";
        }

        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();
        protokollDatei.ausgabe("Anzahl verarbeiteter Sätze insgesamt=" + Integer.toString(gesamtanzahl));
        protokollDatei.newline();

        protokollDatei.ausgabe("Anzahl Fehler=" + Integer.toString(fehleranzahl));
        protokollDatei.newline();
        protokollDatei.schliessen();

        fehlertext = fehlertext + " Protokoll in " + protokollDatei.dateiname;
        tfFehlermeldung.setText(fehlertext);
        quelle = "";

    }

    /**
     * Clicked text datei lesen.
     *
     * @param event the event
     */
    @FXML
    void clickedTextDateiLesen(ActionEvent event) {
        if (pruefeAllgemeineEingaben() == false) {
            return;
        }

        /*TODO _Anmeldeablauf Import: Funktion textdatei_einlesen bei "eine Eintrittskarte selbst" noch nicht implementiert*/

    }

    /**
     * Setze status aktionaersnummer eingeben funktion.
     */
    void setzeStatusAktionaersnummerEingeben_Funktion() {

    }

    /**
     * Setze status verarbeitung funktion.
     */
    void setzeStatusVerarbeitung_Funktion() {

    }

    /**
     * Setze status abbruch funktion.
     */
    void setzeStatusAbbruch_Funktion() {

    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
        hauptFunktion = KonstPortalAktion.HAUPT_NEUANMELDUNG;
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
