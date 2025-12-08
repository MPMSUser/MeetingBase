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
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsLauf;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsProtokoll;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungsArt;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungsStatus;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELaufInfoGet;
import de.meetingapps.meetingportal.meetComWE.WELaufInfoGetRC;
import de.meetingapps.meetingportal.meetComWE.WELaufScanStarten;
import de.meetingapps.meetingportal.meetComWE.WELaufScanStartenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * The Class CtrlStapelVerarbeitung.
 */
public class CtrlStapelVerarbeitung extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn scan lauf auswerten. */
    @FXML
    private Button btnScanLaufAuswerten;

    /** The btn verarbeitungs laeufe holen. */
    @FXML
    private Button btnVerarbeitungsLaeufeHolen;

    /** The btn protokoll in datei. */
    @FXML
    private Button btnProtokollInDatei;

    /** The tf erklaerung datum. */
    @FXML
    private TextField tfErklaerungDatum;

    /** The tf erklaerung zeit. */
    @FXML
    private TextField tfErklaerungZeit;

    /** The cb eingangs weg. */
    @FXML
    private ComboBox<String> cbEingangsWeg;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The grpn uebersicht. */
    private GridPane grpnUebersicht = null;

    /** The btn detail. */
    private Button[] btnDetail = null;

    /** The verarbeitungs laeufe. */
    private EclVerarbeitungsLauf[] verarbeitungsLaeufe = null;

    /** The verarbeitungs protokoll. */
    private EclVerarbeitungsProtokoll[] verarbeitungsProtokoll = null;

    /** The scanlauf. */
    private EclScan[] scanlauf = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'StapelVerarbeitung.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'StapelVerarbeitung.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'StapelVerarbeitung.fxml'.";

        /********** Ab hier individuell *******************/

        /*Eingangsweg setzen*/
        SClErfassungsDaten.initErfassungsfelder(tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        btnProtokollInDatei.setVisible(false);

    }

    /**
     * Clicked scan lauf auswerten.
     *
     * @param event the event
     */
    @FXML
    void clickedScanLaufAuswerten(ActionEvent event) {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            this.fehlerMeldung(fehlerString);
            return;
        }

        btnProtokollInDatei.setVisible(false);

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);
        System.out.println("weLoginVerify.getEingabeQuelle()=" + weLoginVerify.getEingabeQuelle());

        WELaufScanStarten weLaufScanStarten = new WELaufScanStarten();
        weLaufScanStarten.setWeLoginVerify(weLoginVerify);

        System.out.println("weLoginVerify.getEingabeQuelle()=" + weLoginVerify.getEingabeQuelle());

        WELaufScanStartenRC weLaufScanStartenRC = wsClient.laufScanStarten(weLaufScanStarten);

        if (weLaufScanStartenRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufScanStartenRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufScanStartenRC.rc, 0) + " beim Scanlauf-Start");
        } else {
            this.fehlerMeldung(
                    "Scan-Lauf mit Nummer " + Integer.toString(weLaufScanStartenRC.laufNummer) + " gestartet");
        }
    }

    /**
     * Clicked verarbeitungs laeufe holen.
     *
     * @param event the event
     */
    @FXML
    void clickedVerarbeitungsLaeufeHolen(ActionEvent event) {

        btnProtokollInDatei.setVisible(false);

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        WELaufInfoGet weLaufInfoGet = new WELaufInfoGet();
        weLaufInfoGet.setWeLoginVerify(weLoginVerify);
        weLaufInfoGet.angeforderteInfo = 0;

        WELaufInfoGetRC weLaufInfoGetRC = wsClient.laufInfoGet(weLaufInfoGet);

        if (weLaufInfoGetRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufInfoGetRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufInfoGetRC.rc, 0) + " beim Holen Verarbeitungsläufe");
            return;
        }

        verarbeitungsLaeufe = weLaufInfoGetRC.verarbeitungsLaeufe;
        int anzVerarbeitungsLaeufe = 0;
        if (verarbeitungsLaeufe != null) {
            anzVerarbeitungsLaeufe = verarbeitungsLaeufe.length;
        }
        btnDetail = new Button[anzVerarbeitungsLaeufe];

        grpnUebersicht = new GridPane();
        grpnUebersicht.setVgap(5);
        grpnUebersicht.setHgap(15);

        for (int i = 0; i < anzVerarbeitungsLaeufe; i++) {
            btnDetail[i] = new Button();
            btnDetail[i].setText("Details");
            btnDetail[i].setOnAction(e -> {
                clickedDetails(e);
            });
            grpnUebersicht.add(btnDetail[i], 0, i);

            Label laufNummer = new Label();
            laufNummer.setText(Integer.toString(verarbeitungsLaeufe[i].ident));
            grpnUebersicht.add(laufNummer, 1, i);

            Label laufArt = new Label();
            laufArt.setText(KonstVerarbeitungsArt.getText(verarbeitungsLaeufe[i].verarbeitungsArt));
            grpnUebersicht.add(laufArt, 2, i);

            Label laufStatus = new Label();
            laufStatus.setText(KonstVerarbeitungsStatus.getText(verarbeitungsLaeufe[i].statusDesLaufs));
            grpnUebersicht.add(laufStatus, 3, i);

            Label laufZeit = new Label();
            laufZeit.setText(verarbeitungsLaeufe[i].verarbeitungsZeit);
            grpnUebersicht.add(laufZeit, 4, i);

        }

        scpnUebersicht.setContent(grpnUebersicht);

    }

    /**
     * On btn protokoll in datei.
     *
     * @param event the event
     */
    @FXML
    void onBtnProtokollInDatei(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.trennzeichen = ';';
        caDateiWrite.oeffne(lDbBundle, "ScanLauf" + Integer.toString(verarbeitungsLaeufe[0].ident) + "_");

        int anzEintraege = 0;
        if (verarbeitungsProtokoll != null) {
            anzEintraege = verarbeitungsProtokoll.length;
        }

        caDateiWrite.ausgabe("Verarbeitungslauf " + Integer.toString(verarbeitungsLaeufe[0].ident));
        caDateiWrite.ausgabe("Mandant " + verarbeitungsLaeufe[0].mandant);
        caDateiWrite.ausgabe(verarbeitungsLaeufe[0].verarbeitungsZeit);
        caDateiWrite.newline();

        for (int i = 0; i < anzEintraege; i++) {

            caDateiWrite.ausgabe(scanlauf[i].barcode); //Aktionärsnummer

            String ergebnis = "";
            switch (verarbeitungsProtokoll[i].ergebnis) {
            case 1:
                ergebnis = "Verarbeitet";
                break;
            case 0:
                ergebnis = "Verarbeitet mit Warnung";
                break;
            case -1:
                ergebnis = "Fehler";
                break;
            default:
                break;
            }
            caDateiWrite.ausgabe(ergebnis);

            if (verarbeitungsProtokoll[i].ergebnis == 1 || verarbeitungsProtokoll[i].ergebnis == 0) {
                String codeVerarbeitet = "";
                codeVerarbeitet = Integer.toString(verarbeitungsProtokoll[i].codeVerarbeitet);
                caDateiWrite.ausgabe(codeVerarbeitet);

                String textVerarbeitet = "";
                textVerarbeitet = verarbeitungsProtokoll[i].textVerarbeitet;
                caDateiWrite.ausgabe(textVerarbeitet);
            }
            if (verarbeitungsProtokoll[i].ergebnis == -1 || verarbeitungsProtokoll[i].ergebnis == 0) {
                String codeFehler = "";
                codeFehler = Integer.toString(verarbeitungsProtokoll[i].codeFehler);
                caDateiWrite.ausgabe(codeFehler);

                String textFehler = "";
                textFehler = verarbeitungsProtokoll[i].textFehler;
                caDateiWrite.ausgabe(textFehler);
            }
            caDateiWrite.ausgabe(scanlauf[i].dateiname);
            caDateiWrite.newline();

        }

        caDateiWrite.schliessen();
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Protokolldatei " + caDateiWrite.dateiname);
    }

    /**
     * Clicked details.
     *
     * @param event the event
     */
    @FXML
    void clickedDetails(ActionEvent event) {
        int gef = -1;
        for (int i = 0; i < verarbeitungsLaeufe.length; i++) {
            if (event.getSource() == btnDetail[i]) {
                gef = i;
            }
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = new WELoginVerify();

        WELaufInfoGet weLaufInfoGet = new WELaufInfoGet();
        weLaufInfoGet.setWeLoginVerify(weLoginVerify);
        weLaufInfoGet.angeforderteInfo = verarbeitungsLaeufe[gef].ident;

        WELaufInfoGetRC weLaufInfoGetRC = wsClient.laufInfoGet(weLaufInfoGet);

        if (weLaufInfoGetRC.rc < 1) {
            this.fehlerMeldung("Fehler " + Integer.toString(weLaufInfoGetRC.rc) + " "
                    + CaFehler.getFehlertext(weLaufInfoGetRC.rc, 0) + " beim Holen Verarbeitungsläufe");
            return;
        }

        btnProtokollInDatei.setVisible(true);

        verarbeitungsLaeufe = weLaufInfoGetRC.verarbeitungsLaeufe; //In diesem Fall: nur 1 Verarbeitungslauf
        verarbeitungsProtokoll = weLaufInfoGetRC.verarbeitungsProtokoll;
        scanlauf = weLaufInfoGetRC.scanlauf;
        int anzEintraege = 0;
        int anzEintraegeScan = 0;
        if (verarbeitungsProtokoll != null) {
            anzEintraege = verarbeitungsProtokoll.length;

        }
        if (scanlauf != null) {
            anzEintraegeScan = scanlauf.length;
        }
        System.out.println("anzEintraege=" + anzEintraege + " anzEintraegeScan=" + anzEintraegeScan);

        grpnUebersicht = new GridPane();
        grpnUebersicht.setVgap(5);
        grpnUebersicht.setHgap(15);

        int zeile = 0;

        for (int i = 0; i < anzEintraege; i++) {

            Label aktionaersnummer = new Label();
            //            if (ParamSpezial.ku178(ParamS.clGlobalVar.mandant)) {
            //                scanlauf[i].barcode = CaString.fuelleLinksNull(scanlauf[i].barcode, 11);
            //                scanlauf[i].barcode = CaString.substring(scanlauf[i].barcode, 0, 10);
            //            }
            aktionaersnummer.setText(scanlauf[i].barcode);

            grpnUebersicht.add(aktionaersnummer, 0, zeile);

            Label ergebnis = new Label();
            switch (verarbeitungsProtokoll[i].ergebnis) {
            case 1:
                ergebnis.setText("Verarbeitet");
                break;
            case 0:
                ergebnis.setText("Verarbeitet mit Warnung");
                break;
            case -1:
                ergebnis.setText("Fehler");
                break;
            default:
                break;
            }
            grpnUebersicht.add(ergebnis, 1, zeile);

            if (verarbeitungsProtokoll[i].ergebnis == 1 || verarbeitungsProtokoll[i].ergebnis == 0) {
                Label codeVerarbeitet = new Label();
                codeVerarbeitet.setText(Integer.toString(verarbeitungsProtokoll[i].codeVerarbeitet));
                grpnUebersicht.add(codeVerarbeitet, 2, zeile);

                Label textVerarbeitet = new Label();
                textVerarbeitet.setText(verarbeitungsProtokoll[i].textVerarbeitet);
                grpnUebersicht.add(textVerarbeitet, 3, zeile);
            }
            if (verarbeitungsProtokoll[i].ergebnis == -1 || verarbeitungsProtokoll[i].ergebnis == 0) {
                Label codeFehler = new Label();
                codeFehler.setText(Integer.toString(verarbeitungsProtokoll[i].codeFehler));
                grpnUebersicht.add(codeFehler, 2, zeile);

                Label textFehler = new Label();
                textFehler.setText(verarbeitungsProtokoll[i].textFehler);
                grpnUebersicht.add(textFehler, 3, zeile);
            }

            Label dateiname = new Label();
            dateiname.setText(scanlauf[i].dateiname);
            grpnUebersicht.add(dateiname, 4, zeile);
            zeile++;
        }

        scpnUebersicht.setContent(grpnUebersicht);

    }

    /**
     * Inits the.
     */
    public void init() {
    }

}
