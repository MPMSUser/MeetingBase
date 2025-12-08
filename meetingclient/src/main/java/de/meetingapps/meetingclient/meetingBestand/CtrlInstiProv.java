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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclInstiProv;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELaufInstiProvStarten;
import de.meetingapps.meetingportal.meetComWE.WELaufInstiProvStartenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * The Class CtrlInstiProv.
 */
public class CtrlInstiProv extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf erklaerung datum. */
    @FXML
    private TextField tfErklaerungDatum;

    /** The tf erklaerung zeit. */
    @FXML
    private TextField tfErklaerungZeit;

    /** The cb eingangs weg. */
    @FXML
    private ComboBox<String> cbEingangsWeg;

    /** The tf import datei. */
    @FXML
    private TextField tfImportDatei;

    /** The cb fix anmelden. */
    @FXML
    private CheckBox cbFixAnmelden;

    /** The cb vorrangig fremdebesitz. */
    @FXML
    private CheckBox cbVorrangigFremdebesitz;

    /** The tf melde ident sammelkarte. */
    @FXML
    private TextField tfMeldeIdentSammelkarte;

    /** The btn importieren. */
    @FXML
    private Button btnImportieren;

    /** The tf verarbeitungslauf. */
    @FXML
    private TextField tfVerarbeitungslauf;

    /** The btn testen. */
    @FXML
    private Button btnTesten;

    /** The btn verarbeiten. */
    @FXML
    private Button btnVerarbeiten;

    /** The btn verarbeiten nur weisung. */
    @FXML
    private Button btnVerarbeitenNurWeisung;

    /** The list insti prov. */
    private List<EclInstiProv> listInstiProv = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert tfImportDatei != null
                : "fx:id=\"tfImportDatei\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert cbFixAnmelden != null
                : "fx:id=\"cbFixAnmelden\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert cbVorrangigFremdebesitz != null
                : "fx:id=\"cbVorrangigFremdebesitz\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert tfMeldeIdentSammelkarte != null
                : "fx:id=\"tfMeldeIdentSammelkarte\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert btnImportieren != null
                : "fx:id=\"btnImportieren\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert tfVerarbeitungslauf != null
                : "fx:id=\"tfVerarbeitungslauf\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert btnTesten != null : "fx:id=\"btnTesten\" was not injected: check your FXML file 'InstiProv.fxml'.";
        assert btnVerarbeiten != null
                : "fx:id=\"btnVerarbeiten\" was not injected: check your FXML file 'InstiProv.fxml'.";

        /********** Ab hier individuell *******************/

        /*Eingangsweg setzen*/
        SClErfassungsDaten.initErfassungsfelder(tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

    }

    /**
     * On btn importieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnImportieren(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle(); //nur für parameter
        /***********
         * Importierende Datei in listInstiProv ablegen
         ***********************/
        /*Aufbau der Datei:
         * aktionärsnummer
         * aktionärsname (kann leer sein)
         * aktienanzahl (kann leer oder 0 sein => 0)
         * anschließend einzelne Markierungen
         * 
         * Erste Zeile = Überschrift wird ignoriert
         */

        listInstiProv = new LinkedList<EclInstiProv>();

        String importDateiname = tfImportDatei.getText();

        int sammelIdent = Integer.parseInt(tfMeldeIdentSammelkarte.getText());

        int sollFixAnmeldung = 0;
        if (cbFixAnmelden.isSelected()) {
            sollFixAnmeldung = 1;
        }

        int sollVorrangFremdbesitz = 0;
        if (cbVorrangigFremdebesitz.isSelected()) {
            sollVorrangFremdbesitz = 1;
        }

        try {

            FileReader fr = new FileReader(importDateiname);
            BufferedReader br = new BufferedReader(fr);

            try {
                String zeile = null;
                zeile = br.readLine(); //Überschriftszeile ignorieren

                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    if (!zeile.isEmpty()) {
                        EclInstiProv neueInstiProv = new EclInstiProv();
                        String[] zeileSplit = zeile.split(";");

                        neueInstiProv.quellDateiname = importDateiname;
                        String hAktionaersnummer = zeileSplit[0].replace('"', ' ').trim();
                        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                        hAktionaersnummer = blNummernformen.aktienregisterNraufbereitenFuerIntern(hAktionaersnummer);
                        neueInstiProv.aktionaersnummer = hAktionaersnummer;

                        neueInstiProv.aktionaersname = zeileSplit[1].replace('"', ' ').trim();
                        neueInstiProv.aktienzahl = Long.parseLong(zeileSplit[2].replace('"', ' ').trim());

                        for (int i = 3; i < zeileSplit.length; i++) {
                            String stimmart = zeileSplit[i].replace('"', ' ').trim();
                            int stimmabgabe = 0;

                            if (stimmart.equalsIgnoreCase("J") || stimmart.equalsIgnoreCase("Y")
                                    || stimmart.equalsIgnoreCase("F")
                                    || stimmart.equalsIgnoreCase("JA") || stimmart.equalsIgnoreCase("YES")
                                    || stimmart.equalsIgnoreCase("FOR")) {
                                stimmabgabe = KonstStimmart.ja;
                            }

                            if (stimmart.equalsIgnoreCase("N") || stimmart.equalsIgnoreCase("NEIN")
                                    || stimmart.equalsIgnoreCase("NO") || stimmart.equalsIgnoreCase("AGAINST")
                                    || stimmart.equalsIgnoreCase("AGST")) {
                                stimmabgabe = KonstStimmart.nein;
                            }

                            if (stimmart.equalsIgnoreCase("E") || stimmart.equalsIgnoreCase("A")
                                    || stimmart.equalsIgnoreCase("ENTHALTUNG")
                                    || stimmart.equalsIgnoreCase("ABSTAIN")
                                    || stimmart.equalsIgnoreCase("ASTN")) {
                                stimmabgabe = KonstStimmart.enthaltung;
                            }

                            neueInstiProv.einzelMarkierungen[i - 2] = stimmabgabe;
                        }
                        neueInstiProv.sollGewaehlteSammelkarteIdent = sammelIdent;
                        neueInstiProv.sollFixAnmeldung = sollFixAnmeldung;
                        neueInstiProv.sollVorrangFremdbesitz = sollVorrangFremdbesitz;
                        listInstiProv.add(neueInstiProv);
                    }
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Datei nicht gefunden!");
        }

        /***************
         * Nun die importierten Datensätze an den Server übertragen zum dortigen
         * Einspielen
         *******/
        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WELaufInstiProvStarten weLaufInstiProvStarten = new WELaufInstiProvStarten();
        weLaufInstiProvStarten.setWeLoginVerify(weLoginVerify);
        weLaufInstiProvStarten.funktion = 1;
        weLaufInstiProvStarten.listInstiProv = listInstiProv;

        WELaufInstiProvStartenRC weLaufInstiProvStartenRC = wsClient.laufInstiProvStarten(weLaufInstiProvStarten);

        if (weLaufInstiProvStartenRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufInstiProvStartenRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufInstiProvStartenRC.rc, 0) + " beim InstiProvLauf-Start");
        } else {
            this.fehlerMeldung("InstiProvLauf-Lauf mit Nummer " + Integer.toString(weLaufInstiProvStartenRC.laufNummer)
                    + " gestartet");
        }

    }

    /**
     * On btn testen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTesten(ActionEvent event) {
        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WELaufInstiProvStarten weLaufInstiProvStarten = new WELaufInstiProvStarten();
        weLaufInstiProvStarten.setWeLoginVerify(weLoginVerify);
        weLaufInstiProvStarten.funktion = 2;
        weLaufInstiProvStarten.laufNr = Integer.parseInt(tfVerarbeitungslauf.getText());
        WELaufInstiProvStartenRC weLaufInstiProvStartenRC = wsClient.laufInstiProvStarten(weLaufInstiProvStarten);

        if (weLaufInstiProvStartenRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufInstiProvStartenRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufInstiProvStartenRC.rc, 0) + " beim InstiProvLauf-Start");
        } else {
            this.fehlerMeldung("InstiProvLauf-Lauf mit Nummer " + Integer.toString(weLaufInstiProvStartenRC.laufNummer)
                    + " Test gestartet");
        }

    }

    /**
     * On btn verarbeiten.
     *
     * @param event the event
     */
    @FXML
    void onBtnVerarbeiten(ActionEvent event) {
        verarbeiten(false);
    }

    /**
     * On btn verarbeiten nur weisung.
     *
     * @param event the event
     */
    @FXML
    void onBtnVerarbeitenNurWeisung(ActionEvent event) {
        verarbeiten(true);
    }

    /**
     * Verarbeiten.
     *
     * @param nurWeisungen the nur weisungen
     */
    private void verarbeiten(boolean nurWeisungen) {
        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WELaufInstiProvStarten weLaufInstiProvStarten = new WELaufInstiProvStarten();
        weLaufInstiProvStarten.setWeLoginVerify(weLoginVerify);
        if (nurWeisungen == false) {
            weLaufInstiProvStarten.funktion = 3;
        } else {
            weLaufInstiProvStarten.funktion = 4;
        }
        weLaufInstiProvStarten.laufNr = Integer.parseInt(tfVerarbeitungslauf.getText());

        WELaufInstiProvStartenRC weLaufInstiProvStartenRC = wsClient.laufInstiProvStarten(weLaufInstiProvStarten);

        if (weLaufInstiProvStartenRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufInstiProvStartenRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufInstiProvStartenRC.rc, 0) + " beim InstiProvLauf-Start");
        } else {
            this.fehlerMeldung("InstiProvLauf-Lauf mit Nummer " + Integer.toString(weLaufInstiProvStartenRC.laufNummer)
                    + " Verarbeitung gestartet");
        }

    }

    /**
     * Inits the.
     */
    public void init() {
    }

}
