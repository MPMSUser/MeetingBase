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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrDrucklaufAuswaehlen;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterNummerAufbereiten;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlZutrittsIdent;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGet;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGetRC;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteGedruckt;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskartenDrucklaufEinzelGet;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskartenDrucklaufGet;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskartenDrucklaufGetRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The Class CtrlEintrittskarteDrucken.
 */
public class CtrlEintrittskarteDrucken {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The ergebnis array meldung. */
    private EclMeldung ergebnisArrayMeldung[] = null;

    /** The ergebnis array personen nat jur. */
    private EclPersonenNatJur ergebnisArrayPersonenNatJur[] = null;

    /** The ergebnis array willenserklaerung. */
    private EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;

    /** The ergebnis array willenserklaerung zusatz. */
    private EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;

    /** The ergebnis array anrede. */
    private EclAnrede ergebnisArrayAnrede[] = null;

    /** The ergebnis array staaten. */
    private EclStaaten ergebnisArrayStaaten[] = null;

    /** The ergebnis array personen nat jur 1. */
    private EclPersonenNatJur ergebnisArrayPersonenNatJur1[] = null;

    /** The ergebnis array aktienregister. */
    private EclAktienregister ergebnisArrayAktienregister[] = null;

    /** The ergebnis array login daten. */
    private EclLoginDaten ergebnisArrayLoginDaten[] = null;

    /** The ergebnis array login daten 1. */
    private EclLoginDaten ergebnisArrayLoginDaten1[] = null;

    /**
     * Global, damit auf die gespeicherten Daten später - z.B. für Druckliste
     * Protokoll - zugegriffen werden kann
     */
    private WEEintrittskartenDrucklaufGet weEintrittskartenDrucklaufGet = null;

    /** The we eintrittskarten drucklauf get RC. */
    private WEEintrittskartenDrucklaufGetRC weEintrittskartenDrucklaufGetRC = null;

    /** The selektiert. */
    private int[] selektiert = null;

    /** Anzahl der Sätze in den Arrays. */
    private int anzSaetze = 0;

    /** Anzahl Sätze selektiert (ermittelt während tatsächlichem Drucken. */
    private int anzSaetzeSelektiert = 0;

    /** Anzahl Sätze tatsächlich - geliefert vom Server. */
    private int anzSaetzeTatsaechlich = 0;

    /** The durchgefuehrter drucklauf. */
    private int durchgefuehrterDrucklauf;

    /** 1=Gäste, 2=Aktionäre. */
    private int gaesteOderAktionaereSelektion = 2;

    /** The drucklauf array. */
    private EclDrucklauf[] drucklaufArray = null;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tg nicht gedruckt. */
    @FXML
    private RadioButton tgNichtGedruckt;

    /** The tgg gedruckt. */
    @FXML
    private ToggleGroup tggGedruckt;

    /** The tg wiederholung. */
    @FXML
    private RadioButton tgWiederholung;

    /** The tg geprueft. */
    @FXML
    private RadioButton tgGeprueft;

    /** The tgg geprueft. */
    @FXML
    private ToggleGroup tggGeprueft;

    /** The tg geprueft alle. */
    @FXML
    private RadioButton tgGeprueftAlle;

    /** The btn aufbereiten. */
    @FXML
    private Button btnAufbereiten;

    /** The tf anzahl drucken. */
    @FXML
    private TextField tfAnzahlDrucken;

    /** The btn drucken. */
    @FXML
    private Button btnDrucken;

    /** The tf nr drucklauf. */
    @FXML
    private TextField tfNrDrucklauf;

    /** The tf drucklauf gewaehlt. */
    @FXML
    private TextField tfDrucklaufGewaehlt;

    /** The tf nachricht. */
    @FXML
    private TextField tfNachricht;

    /** The tg land inland. */
    @FXML
    private RadioButton tgLandInland;

    /** The tgg land. */
    @FXML
    private ToggleGroup tggLand;

    /** The tg land ausland. */
    @FXML
    private RadioButton tgLandAusland;

    /** The tg land alle. */
    @FXML
    private RadioButton tgLandAlle;

    /** The tg weg portal. */
    @FXML
    private RadioButton tgWegPortal;

    /** The tgg weg. */
    @FXML
    private ToggleGroup tggWeg;

    /** The tg weg anmeldestelle. */
    @FXML
    private RadioButton tgWegAnmeldestelle;

    /** The tg weg alle. */
    @FXML
    private RadioButton tgWegAlle;

    /** The tg aktionaere. */
    @FXML
    private RadioButton tgAktionaere;

    /** The tg gast aktionaer. */
    @FXML
    private ToggleGroup tgGastAktionaer;

    /** The tg gaeste. */
    @FXML
    private RadioButton tgGaeste;

    /** The ta erster aktionaer. */
    @FXML
    private TextArea taErsterAktionaer;

    /** The ta letzter aktionaer. */
    @FXML
    private TextArea taLetzterAktionaer;

    /** The btn drucklauf. */
    @FXML
    private Button btnDrucklauf;

    /** The tf von EK. */
    @FXML
    private TextField tfVonEK;

    /** The tf bis EK. */
    @FXML
    private TextField tfBisEK;

    /** The rb gesamt PDF. */
    @FXML
    private RadioButton rbGesamtPDF;

    /** The tgg druck in. */
    @FXML
    private ToggleGroup tggDruckIn;

    /** The rb einzel PDF. */
    @FXML
    private RadioButton rbEinzelPDF;

    /** The rb direkt druck. */
    @FXML
    private RadioButton rbDirektDruck;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tgNichtGedruckt != null
                : "fx:id=\"tgNichtGedruckt\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tggGedruckt != null
                : "fx:id=\"tggGedruckt\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgWiederholung != null
                : "fx:id=\"tgWiederholung\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgGeprueft != null
                : "fx:id=\"tgGeprueft\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tggGeprueft != null
                : "fx:id=\"tggGeprueft\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgGeprueftAlle != null
                : "fx:id=\"tgGeprueftAlle\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert btnAufbereiten != null
                : "fx:id=\"btnAufbereiten\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfAnzahlDrucken != null
                : "fx:id=\"tfAnzahlDrucken\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert btnDrucken != null
                : "fx:id=\"btnDrucken\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfNrDrucklauf != null
                : "fx:id=\"tfNrDrucklauf\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfDrucklaufGewaehlt != null
                : "fx:id=\"tfDrucklaufGewaehlt\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfNachricht != null
                : "fx:id=\"tfNachricht\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgLandInland != null
                : "fx:id=\"tgLandInland\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tggLand != null
                : "fx:id=\"tggLand\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgLandAusland != null
                : "fx:id=\"tgLandAusland\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgLandAlle != null
                : "fx:id=\"tgLandAlle\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgWegPortal != null
                : "fx:id=\"tgWegPortal\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tggWeg != null
                : "fx:id=\"tggWeg\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgWegAnmeldestelle != null
                : "fx:id=\"tgWegAnmeldestelle\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgWegAlle != null
                : "fx:id=\"tgWegAlle\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgAktionaere != null
                : "fx:id=\"tgAktionaere\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgGastAktionaer != null
                : "fx:id=\"tgGastAktionaer\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tgGaeste != null
                : "fx:id=\"tgGaeste\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert taErsterAktionaer != null
                : "fx:id=\"taErsterAktionaer\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert taLetzterAktionaer != null
                : "fx:id=\"taLetzterAktionaer\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";

        /*************** Ab hier individuell *************************/
        btnDrucken.setDisable(true);

        WSClient wsClient = new WSClient();
        WEDrucklaeufeGet weDrucklaeufeGet = new WEDrucklaeufeGet();
        weDrucklaeufeGet.drucklaufArt = 1;
        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weDrucklaeufeGet.setWeLoginVerify(weLoginVerify);

        WEDrucklaeufeGetRC weDrucklaeufeGetRC = wsClient.drucklaeufeGet(weDrucklaeufeGet);

        drucklaufArray = weDrucklaeufeGetRC.drucklaufArray;

        if (ParamS.param.paramBasis.ekSeitenzahl == 2) {
            rbGesamtPDF.setDisable(true);
        }

    }

    /**
     * Disabled alle Buttons und Selektionsfelder, außer "Drucken". Aufzurufen nach
     * zur Verfügung-Stellung der Druckdaten - dann nur noch Drucken möglich, keine
     * Änderung der Selektion mehr.
     */
    void disableAuswahlButtons() {
        tgNichtGedruckt.setDisable(true);
        tgWiederholung.setDisable(true);

        tgGeprueft.setDisable(true);
        tgGeprueftAlle.setDisable(true);

        tgLandInland.setDisable(true);
        tgLandAusland.setDisable(true);
        tgLandAlle.setDisable(true);

        tgWegPortal.setDisable(true);
        tgWegAnmeldestelle.setDisable(true);
        tgWegAlle.setDisable(true);

        tgAktionaere.setDisable(true);
        tgGaeste.setDisable(true);

        tfDrucklaufGewaehlt.setDisable(true);

        btnDrucken.setDisable(false);
        btnAufbereiten.setDisable(true);

        btnDrucklauf.setDisable(true);

        //		rbGesamtPDF.setDisable(true);
        //		rbEinzelPDF.setDisable(true);
        //		rbDirektDruck.setDisable(true);
        tfVonEK.setDisable(true);
        tfBisEK.setDisable(true);
    }

    /**
     * On btn drucklauf.
     *
     * @param event the event
     */
    @FXML
    void onBtnDrucklauf(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrDrucklaufAuswaehlen controllerFenster = new CtrDrucklaufAuswaehlen();

        controllerFenster.init(neuerDialog);
        controllerFenster.drucklaufListe = drucklaufArray;
        controllerFenster.verarbeitungslaufArt = KonstVerarbeitungslaufArt.eintrittskartendruck;

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/DrucklaufAuswaehlen.fxml", 1200, 850,
                "Drucklauf auswählen", true);

        if (controllerFenster.verarbeitungslaufNr.isEmpty()) {
            return;
        } else {
            tfDrucklaufGewaehlt.setText(controllerFenster.verarbeitungslaufNr);
            tgWiederholung.setSelected(true);
            return;
        }

    }

    /**
     * Clicked aufbereiten.
     *
     * @param event the event
     */
    @FXML
    void clickedAufbereiten(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle(); //nur wg. Parameter
        String vonEk = tfVonEK.getText().trim();
        String bisEk = tfBisEK.getText().trim();

        if (!vonEk.isEmpty()) {
            if (!CaString.isNummern(vonEk)) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Von EK darf nur Ziffern enthalten");
                return;
            }
            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            vonEk = blNummernformen.formatiereEKNr(vonEk);
        }
        if (!bisEk.isEmpty()) {
            if (!CaString.isNummern(bisEk)) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Bis EK darf nur Ziffern enthalten");
                return;
            }
            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            bisEk = blNummernformen.formatiereEKNr(bisEk);
        }
        if ((!vonEk.isEmpty() && bisEk.isEmpty()) || (vonEk.isEmpty() && !bisEk.isEmpty())) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Von EK und Bis EK müssen beide gefüllt oder beide leer sein");
            return;
        }

        taErsterAktionaer.setText("");
        taLetzterAktionaer.setText("");

        /** 0=alle, 1=Deutschland, 2=Ausland */
        int landSelektion = 0;
        if (tgLandInland.isSelected()) {
            landSelektion = 1;
        }
        if (tgLandAusland.isSelected()) {
            landSelektion = 2;
        }

        /** 0=alle, 1=Portal, 2=Anmeldestelle */
        int wegSelektion = 0;
        if (tgWegPortal.isSelected()) {
            wegSelektion = 1;
        }
        if (tgWegAnmeldestelle.isSelected()) {
            wegSelektion = 2;
        }

        gaesteOderAktionaereSelektion = 2;
        if (tgGaeste.isSelected()) {
            gaesteOderAktionaereSelektion = 1;
        }

        int ekVersandFuerAlleImPortalAngefordertenSelektion=lDbBundle.param.paramPortal.ekVersandFuerAlleImPortalAngeforderten;
        
        WSClient wsClient = new WSClient();

        int nrGewaehlterDrucklauf = 0;
        weEintrittskartenDrucklaufGet = new WEEintrittskartenDrucklaufGet();
        weEintrittskartenDrucklaufGet.setLandSelektion(landSelektion);
        weEintrittskartenDrucklaufGet.setWegSelektion(wegSelektion);
        weEintrittskartenDrucklaufGet.setEkVersandFuerAlleImPortalAngefordertenSelektion(ekVersandFuerAlleImPortalAngefordertenSelektion);
        weEintrittskartenDrucklaufGet.setVonEkNummer(vonEk);
        weEintrittskartenDrucklaufGet.setBisEkNummer(bisEk);

        weEintrittskartenDrucklaufGet.setGastOderAktionaer(gaesteOderAktionaereSelektion);

        tfNachricht.setText("");
        if (tgWiederholung.isSelected()) {
            if (tfDrucklaufGewaehlt.getText() == null || tfDrucklaufGewaehlt.getText().isEmpty()
                    || CaString.isNummern(tfDrucklaufGewaehlt.getText()) == false) {
                tfNachricht.setText("Bitte eine gültige Drucklauf-Nummer eingeben!");
                return;
            } else {
                nrGewaehlterDrucklauf = Integer.parseInt(tfDrucklaufGewaehlt.getText());
            }
            weEintrittskartenDrucklaufGet.setErstDruck(false);
        } else {
            weEintrittskartenDrucklaufGet.setErstDruck(true);
            nrGewaehlterDrucklauf = 0;
        }

        weEintrittskartenDrucklaufGet.setDrucklauf(nrGewaehlterDrucklauf);

        if (tgGeprueft.isSelected()) {
            weEintrittskartenDrucklaufGet.setNurGepruefte(true); /*XXX War auch false. Aber dann unlogisch. Testen!!!!*/
        } else {
            weEintrittskartenDrucklaufGet.setNurGepruefte(false);
        }

        weEintrittskartenDrucklaufGet.setWegSelektion(wegSelektion);

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weEintrittskartenDrucklaufGet.setWeLoginVerify(weLoginVerify);

        weEintrittskartenDrucklaufGetRC = wsClient.eintrittskartenDrucklaufGet(weEintrittskartenDrucklaufGet);
        /*??????????????????????????????*/
        if (weEintrittskartenDrucklaufGetRC.getRc() < 1) {
            tfNachricht.setText("Fehler " + weEintrittskartenDrucklaufGetRC.getRc() + " "
                    + CaFehler.getFehlertext(weEintrittskartenDrucklaufGetRC.getRc(), 0) + "!");
            return;
        }

        tfNrDrucklauf.setText(Integer.toString(weEintrittskartenDrucklaufGetRC.getDrucklauf()));
        anzSaetze = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung().length;
        anzSaetzeTatsaechlich = weEintrittskartenDrucklaufGetRC.getAnzahlSaetzeTatsaechlich();

        tfAnzahlDrucken.setText(Integer.toString(anzSaetzeTatsaechlich));
        if (anzSaetze == 0) {
            tfNachricht.setText("Keine Eintrittskarten zum Drucken!");
            return;
        }

        ergebnisArrayMeldung = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung();
        ergebnisArrayPersonenNatJur = weEintrittskartenDrucklaufGetRC.getErgebnisArrayPersonenNatJur();
        ergebnisArrayWillenserklaerung = weEintrittskartenDrucklaufGetRC.getErgebnisArrayWillenserklaerung();
        ergebnisArrayWillenserklaerungZusatz = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayWillenserklaerungZusatz();
        ergebnisArrayAnrede = weEintrittskartenDrucklaufGetRC.getErgebnisArrayAnrede();
        ergebnisArrayStaaten = weEintrittskartenDrucklaufGetRC.getErgebnisArrayStaaten();
        ergebnisArrayPersonenNatJur1 = weEintrittskartenDrucklaufGetRC.getErgebnisArrayPersonenNatJur1();
        ergebnisArrayAktienregister = weEintrittskartenDrucklaufGetRC.getErgebnisArrayAktienregister();
        ergebnisArrayLoginDaten = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayLoginDaten();
        ergebnisArrayLoginDaten1 = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayLoginDaten1();
        selektiert = new int[anzSaetze];

        anzSaetzeSelektiert = 0;
        for (int i = 0; i < anzSaetze; i++) {
            selektiert[i] = 0;
        }

        int offsetErsterAktionaer = weEintrittskartenDrucklaufGetRC.getOffsetErsterAktionaer();
        int offsetLetzterAktionaer = weEintrittskartenDrucklaufGetRC.getOffsetLetzterAktionaer();

        if (offsetErsterAktionaer != -1) {
            String ersterAktionaer = "Aktionärsnummer="
                    + ergebnisArrayAktienregister[offsetErsterAktionaer].aktionaersnummer + " Aktionärsname="
                    + ergebnisArrayAktienregister[offsetErsterAktionaer].nameKomplett + " Eintrittskartennummer="
                    + ergebnisArrayMeldung[offsetErsterAktionaer].zutrittsIdent;
            taErsterAktionaer.setText(ersterAktionaer);
        }
        if (offsetLetzterAktionaer != -1) {
            String letzterAktionaer = "Aktionärsnummer="
                    + ergebnisArrayAktienregister[offsetLetzterAktionaer].aktionaersnummer + " Aktionärsname="
                    + ergebnisArrayAktienregister[offsetLetzterAktionaer].nameKomplett + " Eintrittskartennummer="
                    + ergebnisArrayMeldung[offsetLetzterAktionaer].zutrittsIdent;
            taLetzterAktionaer.setText(letzterAktionaer);
        }

        durchgefuehrterDrucklauf = weEintrittskartenDrucklaufGetRC.getDrucklauf();

        disableAuswahlButtons();

    }

    /**
     * Clicked drucken.
     *
     * @param event the event
     */
    @FXML
    void clickedDrucken(ActionEvent event) {
        //    	eintrittskarteDrucken_Alt(false);

        /*1=Gesamt-PDF; 2=EinzelPDF; 3=Direktdruck*/
        int druckVerlauf = 0;
        if (rbGesamtPDF.isSelected()) {
            druckVerlauf = 1;
        }
        if (rbEinzelPDF.isSelected()) {
            druckVerlauf = 2;
        }
        if (rbDirektDruck.isSelected()) {
            druckVerlauf = 3;
        }

        if (druckVerlauf == 0) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte Druckverlauf (PDF etc.) auswählen");
            return;
        }

        eintrittskarteDrucken(false, druckVerlauf);
    }

    /**
     * druckVerlauf: 1=Gesamt-PDF; 2=EinzelPDF; 3=Direktdruck.
     *
     * @param einzelDruck  the einzel druck
     * @param druckVerlauf the druck verlauf
     */
    private void eintrittskarteDrucken(boolean einzelDruck, int druckVerlauf) {

        boolean druckGestartet = false;

        int exportFormat = 6;

        DbBundle lDbBundle = new DbBundle(); //FÜr Parameter

        RpDrucken rpDrucken = null;
        RpVariablen rpVariablen = null;

        if (druckVerlauf == 1) {
            rpDrucken = new RpDrucken();
            rpDrucken.exportFormat = exportFormat;
            rpDrucken.exportAnzeigen = true;
            rpDrucken.druckbalkenAnzeigen = true;
            rpDrucken.exportDateinameAbfragen = false;
            rpDrucken.druckerAbfragen = false;
            rpDrucken.druckerWiederverwendet = 2;
            if (einzelDruck == false) {
                rpDrucken.exportDatei = "EKDruck" + Integer.toString(durchgefuehrterDrucklauf);
            } else {
                rpDrucken.exportDatei = "EKDruck" + CaDatumZeit.DatumZeitStringFuerDateiname();
            }

            rpDrucken.initFormular(lDbBundle);

            System.out.println("Datei=" + rpDrucken.drucklaufDatei);

            if (rpDrucken.rcFehler != 0) {
                tfNachricht.setText(CaFehler.getFehlertext(rpDrucken.rcFehler, 0));
                return;
            }

            /*Variablen füllen - sowie Dokumentvorlage*/
            rpVariablen = new RpVariablen(lDbBundle);
            if (gaesteOderAktionaereSelektion == 2) {
                rpVariablen.ekPapier(rpDrucken);
            } else {
                //    			RpDrucken lRpDrucken=new RpDrucken();
                rpVariablen.gkStapel("01", rpDrucken);

            }

            rpDrucken.dateinameLLQuelle = rpVariablen.dateiname;
            boolean bRc = rpDrucken.startFormular();
            if (rpDrucken.rcFehler != 0) {
                if (!einzelDruck) {
                    tfNachricht.setText(CaFehler.getFehlertext(rpDrucken.rcFehler, 0));
                }
                return;
            }
            if (bRc == false) {
                return;
            }

        }

        for (int i = 0; i < anzSaetze; i++) {
            /*Checken, ob Null an dieser Arraystelle*/
            if (ergebnisArrayMeldung[i] != null) {

                if (druckVerlauf != 1) {
                    if (druckVerlauf == 2) {
                        rpDrucken = new RpDrucken();
                        rpDrucken.exportFormat = exportFormat;
                        rpDrucken.exportAnzeigen = false;
                        rpDrucken.druckbalkenAnzeigen = true;
                        rpDrucken.exportDateinameAbfragen = false;
                        rpDrucken.druckerAbfragen = false;
                        rpDrucken.druckerWiederverwendet = 2;
                        if (einzelDruck == false) {
                            rpDrucken.exportDatei = "EKDruck" + Integer.toString(durchgefuehrterDrucklauf) + "_"
                                    + ergebnisArrayWillenserklaerung[i].zutrittsIdent;
                        } else {
                            rpDrucken.exportDatei = "EKDruck" + CaDatumZeit.DatumZeitStringFuerDateiname() + "_"
                                    + Integer.toString(i);
                        }
                    }
                    if (druckVerlauf == 3) {
                        /*Initialisieren*/
                        rpDrucken = new RpDrucken();
                        rpDrucken.initClientDrucke();
                        if (druckGestartet == true) {
                            rpDrucken.druckerWiederverwendet = 2;
                            rpDrucken.druckerAbfragen = false;
                        } else {
                            rpDrucken.druckerWiederverwendet = 1;
                            rpDrucken.druckerAbfragen = true;
                            druckGestartet = true;
                        }
                        rpDrucken.druckerWiederverwendetNummer = 999;
                    }

                    rpDrucken.initFormular(lDbBundle);

                    System.out.println("Datei=" + rpDrucken.drucklaufDatei);

                    if (rpDrucken.rcFehler != 0) {
                        tfNachricht.setText(CaFehler.getFehlertext(rpDrucken.rcFehler, 0));
                        return;
                    }

                    /*Variablen füllen - sowie Dokumentvorlage*/
                    rpVariablen = new RpVariablen(lDbBundle);
                    if (gaesteOderAktionaereSelektion == 2) {
                        rpVariablen.ekPapier(rpDrucken);
                    } else {
                        //    					RpDrucken lRpDrucken=new RpDrucken();
                        rpVariablen.gkStapel("01", rpDrucken);

                    }

                    rpDrucken.dateinameLLQuelle = rpVariablen.dateiname;
                    boolean bRc = rpDrucken.startFormular();
                    if (rpDrucken.rcFehler != 0) {
                        if (!einzelDruck) {
                            tfNachricht.setText(CaFehler.getFehlertext(rpDrucken.rcFehler, 0));
                        }
                        return;
                    }
                    if (bRc == false) {
                        return;
                    }
                } /*Ende druckVerlauf!=1*/

                rpVariablen.fuelleAllgemeineVariablen(rpDrucken);

                
                if (gaesteOderAktionaereSelektion == 2) {
                    ekDrucke(rpDrucken, rpVariablen, i, einzelDruck);
                } else {
                    gkDrucke(rpDrucken, rpVariablen, i, einzelDruck, lDbBundle);
                }
                rpDrucken.druckenFormularSatzwechsel();
                if (druckVerlauf != 1) {
                    rpDrucken.endeFormular();
                }

            }
        }

        if (druckVerlauf == 1) {
            rpDrucken.endeFormular();
        }

        if (einzelDruck == false) {
            druckquittungErzeugen();
        }

        CaBug.druckeLog("Ausgabe="+rpDrucken.drucklaufDatei, logDrucken, 3);
        
        if (!einzelDruck) {

            tfNachricht.setText(anzSaetzeSelektiert + " Eintrittskarten selektiert und gedruckt");
        }

    }

    //    private void zeilefuellenAktionaer(CmbtLL21 Ll, int nLLJob_, int verwendeteZeile, String inhalt){
    //    	switch (verwendeteZeile){
    //    	case 1:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile1", inhalt);break;}
    //    	case 2:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile2", inhalt);break;}
    //    	case 3:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile3", inhalt);break;}
    //    	case 4:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile4", inhalt);break;}
    //    	case 5:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile5", inhalt);break;}
    //    	case 6:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile6", inhalt);break;}
    //    	case 7:{Ll.LlDefineVariable(nLLJob_, "Aktionaer.Zeile7", inhalt);break;}
    //    	}
    //    }
    //
    //
    //    private void zeilefuellenVersand(CmbtLL21 Ll, int nLLJob_, int verwendeteZeile, String inhalt){
    //    	switch (verwendeteZeile){
    //    	case 1:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile1", inhalt);break;}
    //    	case 2:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile2", inhalt);break;}
    //    	case 3:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile3", inhalt);break;}
    //    	case 4:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile4", inhalt);break;}
    //    	case 5:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile5", inhalt);break;}
    //    	case 6:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile6", inhalt);break;}
    //    	case 7:{Ll.LlDefineVariable(nLLJob_, "EingetrageneVersandadresse.Zeile7", inhalt);break;}
    //    	}
    //    }

    //    private void eintrittskarteDrucken_Alt(boolean einzelDruck){
    //    	int i;
    //
    //    	CmbtLL21 Ll;
    //    	int nLLJob_=-1;
    //    	int hWnd_=0;
    //
    //    	Ll = new CmbtLL21();
    //    	Ll.LlSetDebug(0); /*Debugmodus auf nein*/
    //
    //    	/*Initialisieren*/
    //    	nLLJob_ = Ll.LlJobOpen(CmbtLL21.CMBTLANG_DEFAULT);
    //    	if (nLLJob_ == CmbtLL21.LL_ERR_BAD_JOBHANDLE)
    //    	{
    //    		if (!einzelDruck){
    //    			tfNachricht.setText("Job can't be initialized!");
    //    		}
    //    		return;
    //    	}
    //    	else if (nLLJob_ == CmbtLL21.LL_ERR_NO_LANG_DLL)
    //    	{
    //    		if (!einzelDruck){
    //    			tfNachricht.setText("Language file not found!\nEnsure that *.lng files can be found in your LuL DLL directory.");
    //    		}
    //    		return;
    //    	}
    //
    //    	Ll.LlSetOptionString(nLLJob_, CmbtLL21.LL_OPTIONSTR_LICENSINGINFO, "aWNdDg");
    //
    //
    //
    //    	DbBundle lDbBundle=new DbBundle(); /*Nur wegen mandant*/
    //    	/*Variablen füllen - sowie Dokumentvorlage*/
    //    	RpVariablen rpVariablen=new RpVariablen(lDbBundle);
    //    	if (gaesteOderAktionaereSelektion==2){
    //    		rpVariablen.ekPapier_alt(Ll, nLLJob_);
    //    	}
    //    	else{
    //    		RpDrucken lRpDrucken=new RpDrucken();
    //    		lRpDrucken.Ll=Ll;
    //    		lRpDrucken.nLLJob_=nLLJob_;
    //    		rpVariablen.gkStapel("01", lRpDrucken);
    //
    //    	}
    //    	/*Abfrage Drucker bereit*/
    //    	//Start printing
    //    	if (Ll.LlPrintWithBoxStart(
    //    			nLLJob_,
    //    			CmbtLL21.LL_PROJECT_LABEL,
    //    			rpVariablen.dateiname.toString(),
    //    			CmbtLL21.LL_PRINT_NORMAL, //Export statt NORMAL => Export ausgewählt
    //    			CmbtLL21.LL_BOXTYPE_NORMALMETER,
    //    			hWnd_,
    //    			"Drucken...") < 0)
    //    	{
    //    		if (!einzelDruck){
    //
    //    			tfNachricht.setText("Error While Printing.");
    //    		}
    //
    //    		//Close the List & Label job
    //    		Ll.LlJobClose(nLLJob_);
    //    		nLLJob_ = -1;
    //
    //    		return;
    //    	}
    //
    //    	System.out.println("Stelle B");
    //
    //    	//Predifined selections for print options dialog
    //    	Ll.LlPrintSetOption(
    //    			nLLJob_,
    //    			CmbtLL21.LL_PRNOPT_COPIES,
    //    			CmbtLL21.LL_COPIES_HIDE);
    //
    //    	Ll.LlPrintSetOption(
    //    			nLLJob_,
    //    			CmbtLL21.LL_PRNOPT_STARTPAGE,
    //    			1);
    //
    //    	if (Ll.LlPrintOptionsDialog(
    //    			nLLJob_,
    //    			hWnd_,
    //    			"Select printing options") < 0)
    //    	{
    //    		Ll.LlPrintEnd(nLLJob_, 0);
    //
    //    		//Close the List & Label job
    //    		Ll.LlJobClose(nLLJob_);
    //    		nLLJob_ = -1;
    //
    //    		return;
    //    	}
    //
    //
    //
    //    	//			if (Ll.LlPrintStart(
    //    	//			nLLJob_,
    //    	//			CmbtLL21.LL_PROJECT_LABEL,
    //    	//			rpVariablen.dateiname.toString(), /****************************************************/
    //    	//			CmbtLL21.LL_PRINT_EXPORT,
    //    	//			CmbtLL21.LL_BOXTYPE_NORMALMETER) < 0)
    //    	//	{
    //    	//		System.out.println("Error While Printing.");
    //    	//		Ll.LlJobClose(nLLJob_);nLLJob_ = -1;return false;
    //    	//	}
    //
    //
    //
    //    	for (i=0;i<anzSaetze;i++){
    //    		/*Checken, ob Null an dieser Arraystelle*/
    //    		if (ergebnisArrayMeldung[i]!=null){
    //    			if (gaesteOderAktionaereSelektion==2){
    //    				ekDrucke_alt(Ll, nLLJob_, i, einzelDruck);
    //    			}
    //    			else{
    //    				//	    				gkDrucke(Ll, nLLJob_, i, einzelDruck);
    //    			}
    //    		}
    //    	}
    //
    //    	//Ends printjob
    //    	Ll.LlPrintEnd(nLLJob_, 0);
    //
    //    	//Close the List & Label Job
    //    	Ll.LlJobClose(nLLJob_);
    //    	nLLJob_ = -1;
    //
    //    	if (einzelDruck==false){
    //    		druckquittungErzeugen();
    //    	}
    //
    //
    //    	if (!einzelDruck){
    //
    //    		tfNachricht.setText(anzSaetzeSelektiert+" Eintrittskarten selektiert und gedruckt");
    //    	}
    //
    //    }

    /**
     * Ek drucke.
     *
     * @param rpDrucken   the rp drucken
     * @param rpVariablen the rp variablen
     * @param lfdNr       the lfd nr
     * @param einzelDruck the einzel druck
     */
    void ekDrucke(RpDrucken rpDrucken, RpVariablen rpVariablen, int lfdNr, boolean einzelDruck) {

        EclMeldung lEclMeldung = ergebnisArrayMeldung[lfdNr];
        EclPersonenNatJur lEclPersonenNatJur = ergebnisArrayPersonenNatJur[lfdNr];
        //    	EclPersonenNatJurVersandadresse lEclPersonenNatJurVersandadresse=ergebnisArrayPersonenNatJurVersandadresse[lfdNr];
        EclWillenserklaerung lEclWillenserklaerung = ergebnisArrayWillenserklaerung[lfdNr];
        EclWillenserklaerungZusatz lEclWillenserklaerungZusatz = ergebnisArrayWillenserklaerungZusatz[lfdNr];
        EclAnrede lEclAnrede = ergebnisArrayAnrede[lfdNr];
        //		EclAnrede lEclAnredeVersand=ergebnisArrayAnredeVersand[lfdNr];
        EclStaaten lEclStaaten = ergebnisArrayStaaten[lfdNr];
        //		EclStaaten lEclStaaten1=ergebnisArrayStaaten1[lfdNr];
        EclPersonenNatJur lEclPersonenNatJur1 = ergebnisArrayPersonenNatJur1[lfdNr];
        EclAktienregister lEclAktienregister = ergebnisArrayAktienregister[lfdNr];
        /** Login-Daten Aktionär */
        EclLoginDaten lEclLoginDaten = ergebnisArrayLoginDaten[lfdNr];
        /** Login-DatenVertreter */
        EclLoginDaten lEclLoginDaten1 = ergebnisArrayLoginDaten1[lfdNr];

        //		Verlagert in Joined
        //		if (lEclWillenserklaerung.erteiltAufWeg>=21 && lEclWillenserklaerung.erteiltAufWeg<=29){
        //			if (wegSelektion==2){return;}
        //		}
        //		else{
        //			if (wegSelektion==1){return;}
        //		}

        if (lEclMeldung.meldungstyp == 3) {
            rpVariablen.fuelleVariable(rpDrucken, "Format.InSammelkarte", "1");
        } else {
            rpVariablen.fuelleVariable(rpDrucken, "Format.InSammelkarte", "0");
        }
        DbBundle lDbBundle = new DbBundle(); //Nur wg. Übertragung der Mandantennummer
        BlNummernformen blNummernform = new BlNummernformen(lDbBundle);

        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", BlNummernformen
                .verketteEKMitNeben(lEclWillenserklaerung.zutrittsIdent, lEclWillenserklaerung.zutrittsIdentNeben, 0));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", lEclWillenserklaerung.zutrittsIdent);

        /*TODO _Formulare: zutrittsIdent sowohl mit als auch ohne Neben zur Verfügung stellen (für Ersatzkarten!)*/
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernform
                .formatiereEKNrKomplett(lEclWillenserklaerung.zutrittsIdent, lEclWillenserklaerung.zutrittsIdentNeben));
        //		rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernform.formatiereEKNrKomplettOhneNeben(lEclWillenserklaerung.zutrittsIdent));
        if (lDbBundle.param.paramBasis.namensaktienAktiv) {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer",
                    BlAktienregisterNummerAufbereiten.aufbereitenFuerKlarschrift(lEclMeldung.aktionaersnummer));
        } else {
            rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer", lEclMeldung.aktionaersnummer);
        }
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", Long.toString(lEclMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(lEclMeldung.stimmen));
        rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(lEclMeldung.stimmen));

        /*Nun passwort und Zugangsdaten anzeigen*/
        /*Zugangsdaten des Aktionärs immer setzen*/
        String aInitialPasswort = lEclLoginDaten.lieferePasswortInitialClean();
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Passwort", aInitialPasswort);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KennungAlternativ", lEclLoginDaten.loginKennungAlternativ);

        /*Prophylaktisch setzen - wird ggf. im Falle einer Vollmacht überschrieben*/
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungVorrang", "0");

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", lEclPersonenNatJur.titel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", lEclPersonenNatJur.name);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", lEclPersonenNatJur.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", lEclPersonenNatJur.plz);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", lEclPersonenNatJur.ort);
        if (!lEclPersonenNatJur.land.isEmpty()) {
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Landeskuerzel", lEclStaaten.code);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Land", lEclStaaten.nameDE);
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", lEclPersonenNatJur.strasse);
        /*Anrede füllen*/
        int anredenNr = lEclPersonenNatJur.anrede;
        if (anredenNr != 0) {
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeDE", lEclAnrede.anredentext);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeEN", lEclAnrede.anredentextfremd);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeDE", lEclAnrede.anredenbrief);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeEN", lEclAnrede.anredenbrieffremd);
        }

        /*Kombi-Felder füllen*/
        String titelVornameName = "";
        if (lEclPersonenNatJur.titel.length() != 0) {
            titelVornameName = titelVornameName + lEclPersonenNatJur.titel + " ";
        }
        if (lEclPersonenNatJur.vorname.length() != 0) {
            titelVornameName = titelVornameName + lEclPersonenNatJur.vorname + " ";
        }
        titelVornameName = titelVornameName + lEclPersonenNatJur.name;
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", titelVornameName);

        String nameVornameTitel = "";
        nameVornameTitel = lEclPersonenNatJur.name;
        if (lEclPersonenNatJur.titel.length() != 0 || lEclPersonenNatJur.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + ",";
        }
        if (lEclPersonenNatJur.titel.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + lEclPersonenNatJur.titel;
        }
        if (lEclPersonenNatJur.vorname.length() != 0) {
            nameVornameTitel = nameVornameTitel + " " + lEclPersonenNatJur.vorname;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", nameVornameTitel);

        String kompletteAnredeDE = lEclAnrede.anredenbrief;
        String kompletteAnredeEN = lEclAnrede.anredenbrieffremd;
        if (lEclAnrede.istjuristischePerson != 1) {
            if (lEclPersonenNatJur.titel.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + lEclPersonenNatJur.titel;
                kompletteAnredeEN = kompletteAnredeEN + " " + lEclPersonenNatJur.titel;
            }
            if (lEclPersonenNatJur.name.length() != 0) {
                kompletteAnredeDE = kompletteAnredeDE + " " + lEclPersonenNatJur.name;
                kompletteAnredeEN = kompletteAnredeEN + " " + lEclPersonenNatJur.name;
            }
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeDE", kompletteAnredeDE);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeEN", kompletteAnredeEN);

        String besitzArtKuerzel = lEclMeldung.besitzart;
        String besitzArt = "", besitzArtEN = "";
        switch (besitzArtKuerzel) {
        case "E":
            besitzArt = "Eigenbesitz";
            besitzArtEN = "Proprietary Possession";
            break;
        case "F":
            besitzArt = "Fremdbesitz";
            besitzArtEN = "Minority Interests";
            break;
        case "V":
            besitzArt = "Vollmachtsbesitz";
            besitzArtEN = "Proxy Possession";
            break;
        default:
            break;
        }
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz",
                ParamS.param.paramBasis.getGattungBezeichnungKurz(lEclMeldung.liefereGattung()));

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Zusatzfeld3", lEclMeldung.zusatzfeld3);

        //		int verwendeteZeile=1;String aufbereitung="";
        //		aufbereitung=lEclAnrede.anredentext;
        //		if (!lEclPersonenNatJur.titel.isEmpty()){
        //			if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //			aufbereitung=aufbereitung+lEclPersonenNatJur.titel;
        //		}
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		aufbereitung=lEclPersonenNatJur.vorname;
        //		if (!lEclPersonenNatJur.name.isEmpty()){
        //			if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //			aufbereitung=aufbereitung+lEclPersonenNatJur.name;
        //		}
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		aufbereitung=lEclPersonenNatJur.zusatz1;
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		aufbereitung=lEclPersonenNatJur.zusatz2;
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		aufbereitung=lEclPersonenNatJur.strasse;
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		if (!lEclPersonenNatJur.plz.isEmpty()){
        //			if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //			aufbereitung=aufbereitung+lEclPersonenNatJur.plz;
        //		}
        //		if (!lEclPersonenNatJur.ort.isEmpty()){
        //			if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //			aufbereitung=aufbereitung+lEclPersonenNatJur.ort;
        //		}
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //		aufbereitung="";
        //		if (!lEclStaaten.code.isEmpty() && lEclStaaten.code.compareTo("DE")!=0){
        //			aufbereitung=lEclStaaten.nameDE.toUpperCase();
        //    		}
        //		if (!aufbereitung.isEmpty()){
        //			zeilefuellenAktionaer(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //			verwendeteZeile++;
        //		}
        //
        //
        //		for (int ii=verwendeteZeile;ii<=7;ii++){
        //			zeilefuellenAktionaer(Ll, nLLJob_, ii, "");
        //
        //		}
        //

        /**Aktiver Vertreter wird immer gefüllt, auch wenn die Vollmacht nicht direkt dieser
         * EK zugeordnet ist!*/
        rpVariablen.fuelleVariable(rpDrucken, "AktiverVertreter.Name", lEclMeldung.vertreterName);
        rpVariablen.fuelleVariable(rpDrucken, "AktiverVertreter.Vorname", lEclMeldung.vertreterVorname);
        rpVariablen.fuelleVariable(rpDrucken, "AktiverVertreter.Ort", lEclMeldung.vertreterOrt);
        

        
        if (lEclWillenserklaerungZusatz.identVertreterPersonNatJur != 0) {
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", lEclLoginDaten1.loginKennung);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungAlternativ",
                    lEclLoginDaten1.loginKennungAlternativ);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort", lEclLoginDaten1.lieferePasswortInitialClean());
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", lEclPersonenNatJur1.name);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", lEclPersonenNatJur1.vorname);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", lEclPersonenNatJur1.ort);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungVorrang", "1");
        } else {
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungAlternativ", "");
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort", "");
        }

        //		if (lEclPersonenNatJur.identVersandadresse!=0){
        //			versandadresseverwenden=2;
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.NameVersand", lEclPersonenNatJurVersandadresse.nameVersand);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.VornameVersand", lEclPersonenNatJurVersandadresse.vornameVersand);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StrasseVersand", lEclPersonenNatJurVersandadresse.strasseVersand);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.PostleitzahlVersand", lEclPersonenNatJurVersandadresse.postleitzahlVersand);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.OrtVersand", lEclPersonenNatJurVersandadresse.ortVersand);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StaatCodeVersand", lEclStaaten1.code);
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StaatNameDEVersand", lEclStaaten1.nameDE);
        //
        //
        //			verwendeteZeile=1;aufbereitung="";
        //			aufbereitung=lEclAnredeVersand.anredentext;
        //			if (!lEclPersonenNatJurVersandadresse.titelVersand.isEmpty()){
        //				if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //				aufbereitung=aufbereitung+lEclPersonenNatJurVersandadresse.titelVersand;
        //			}
        //			if (aufbereitung!=null && !aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //			aufbereitung="";
        //			aufbereitung=lEclPersonenNatJurVersandadresse.vornameVersand;
        //			if (!lEclPersonenNatJurVersandadresse.nameVersand.isEmpty()){
        //				if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //				aufbereitung=aufbereitung+lEclPersonenNatJurVersandadresse.nameVersand;
        //			}
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //			aufbereitung="";
        //			aufbereitung=lEclPersonenNatJurVersandadresse.name2Versand;
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //			aufbereitung="";
        //			aufbereitung=lEclPersonenNatJurVersandadresse.name3Versand;
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //			aufbereitung="";
        //			aufbereitung=lEclPersonenNatJurVersandadresse.strasseVersand;
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //			aufbereitung="";
        //			if (!lEclPersonenNatJurVersandadresse.postleitzahlVersand.isEmpty()){
        //				if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //				aufbereitung=aufbereitung+lEclPersonenNatJurVersandadresse.postleitzahlVersand;
        //			}
        //			if (!lEclPersonenNatJurVersandadresse.ortVersand.isEmpty()){
        //				if (!aufbereitung.isEmpty()){aufbereitung=aufbereitung+" ";}
        //				aufbereitung=aufbereitung+lEclPersonenNatJurVersandadresse.ortVersand;
        //			}
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //
        //			aufbereitung="";
        //			if (!lEclStaaten1.code.isEmpty() && lEclStaaten1.code.compareTo("DE")!=0){
        //				aufbereitung=lEclStaaten1.nameDE.toUpperCase();
        //				System.out.println("Land="+aufbereitung);
        //	    		}
        //			if (!aufbereitung.isEmpty()){
        //				zeilefuellenVersand(Ll, nLLJob_, verwendeteZeile, aufbereitung);
        //				verwendeteZeile++;
        //			}
        //
        //
        //
        //			for (int ii=verwendeteZeile;ii<=7;ii++){
        //				zeilefuellenVersand(Ll, nLLJob_, ii, "");
        //
        //			}
        //
        //		}
        //		else{
        //			versandadresseverwenden=1;
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.NameVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.VornameVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StrasseVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.PostleitzahlVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.OrtVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StaatCodeVersand", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.StaatNameDEVersand", "");
        //
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile1", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile2", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile3", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile4", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile5", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile6", "");
        //			rpVariablen.fuelleVariable(rpDrucken, "EingetrageneVersandadresse.Zeile7", "");
        //
        //		}
        System.out.println("lEclWillenserklaerungZusatz.versandartEK=" + lEclWillenserklaerungZusatz.versandartEK);
        if (lEclWillenserklaerungZusatz.versandartEK == 2) {/*Manuell eingegebene Versandadresse*/
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", lEclWillenserklaerungZusatz.versandadresse1);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", lEclWillenserklaerungZusatz.versandadresse2);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", lEclWillenserklaerungZusatz.versandadresse3);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", lEclWillenserklaerungZusatz.versandadresse4);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", lEclWillenserklaerungZusatz.versandadresse5);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", "");
        } else {
            System.out.println("Stelle A lEclAktienregister.adresszeile1=" + lEclAktienregister.adresszeile1);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", lEclAktienregister.adresszeile1);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", lEclAktienregister.adresszeile2);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", lEclAktienregister.adresszeile3);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", lEclAktienregister.adresszeile4);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", lEclAktienregister.adresszeile5);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", lEclAktienregister.adresszeile6);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", lEclAktienregister.adresszeile7);

        }

        //		System.out.println("versandadresseUeberprueft="+lEclWillenserklaerungZusatz.versandadresseUeberprueft);

        System.out.println("lEclWillenserklaerungZusatz.versandadresseUeberprueft="
                + lEclWillenserklaerungZusatz.versandadresseUeberprueft);
        System.out.println("lEclWillenserklaerungZusatz.versandartEKUeberprueft="
                + lEclWillenserklaerungZusatz.versandartEKUeberprueft);
        if (lEclWillenserklaerungZusatz.versandadresseUeberprueft == 2) {
            if (lEclWillenserklaerungZusatz.versandartEKUeberprueft == 1) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", lEclAktienregister.adresszeile1);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", lEclAktienregister.adresszeile2);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", lEclAktienregister.adresszeile3);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", lEclAktienregister.adresszeile4);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", lEclAktienregister.adresszeile5);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", lEclAktienregister.adresszeile6);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", lEclAktienregister.adresszeile7);
            }

            if (lEclWillenserklaerungZusatz.versandartEKUeberprueft == 2) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1",
                        lEclWillenserklaerungZusatz.versandadresse1);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2",
                        lEclWillenserklaerungZusatz.versandadresse2);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3",
                        lEclWillenserklaerungZusatz.versandadresse3);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4",
                        lEclWillenserklaerungZusatz.versandadresse4);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5",
                        lEclWillenserklaerungZusatz.versandadresse5);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", "");
            }

            //			if (lEclWillenserklaerungZusatz.versandartEKUeberprueft==2){versandadresseverwenden=3;}
            //			if (lEclWillenserklaerungZusatz.versandartEKUeberprueft==98){versandadresseverwenden=1;}
            if (lEclWillenserklaerungZusatz.versandartEKUeberprueft == 99) {
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1",
                        lEclWillenserklaerungZusatz.versandadresse1Ueberprueft);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2",
                        lEclWillenserklaerungZusatz.versandadresse2Ueberprueft);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3",
                        lEclWillenserklaerungZusatz.versandadresse3Ueberprueft);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4",
                        lEclWillenserklaerungZusatz.versandadresse4Ueberprueft);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5",
                        lEclWillenserklaerungZusatz.versandadresse5Ueberprueft);
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", "");
            }
        }

        //		System.out.println("Versandadresseverwenden="+versandadresseverwenden);

        //		private int landSelektion=0;
        //		Entfernt - Selektion erfolgt jetzt bereits auf dem Server
        //		switch (versandadresseverwenden){
        //		case 1:{/*Versandadresse.* - im Aktienregister hinterlegte Versandadresse*/
        //			if (lEclStaaten.code.compareTo("DE")==0){
        //				if (landSelektion==2){return;}
        //			}
        //			else{
        //				if (landSelektion==1){return;}
        //			}
        //			break;
        //		}
        //		case 2:{/*Versandadresse.* - im Aktienregister hinterlegte Versandadresse*/
        //			if (lEclStaaten1.code.compareTo("DE")==0){
        //				if (landSelektion==2){return;}
        //			}
        //			else{
        //				if (landSelektion==1){return;}
        //			}
        //			break;
        //		}
        //		case 3:{/*manuelle Versandadresse - im Zweifelfall ans Auslande*/
        //			if ((lEclStaaten1!=null && lEclStaaten1.code!=null && lEclStaaten1.code.compareTo("DE")==0) ||
        //					(lEclStaaten!=null && lEclStaaten.code!=null && lEclStaaten.code.compareTo("DE")==0)){
        //				if (landSelektion==2){return;}
        //			}
        //			else{
        //				if (landSelektion==1){return;}
        //			}
        //			break;
        //		}
        //
        //
        //		}

        //		rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Verwenden", Integer.toString(versandadresseverwenden));

        if (einzelDruck == false) {
            selektiert[lfdNr] = 1;
            anzSaetzeSelektiert++;
        }

        rpDrucken.druckenFormular();

        //		if (einzelDruck==false){
        //			druckquittungErzeugen();
        //		}

    }

    /**
     * Zeilefuellen gast.
     *
     * @param rpDrucken       the rp drucken
     * @param rpVariablen     the rp variablen
     * @param verwendeteZeile the verwendete zeile
     * @param inhalt          the inhalt
     */
    private void zeilefuellenGast(RpDrucken rpDrucken, RpVariablen rpVariablen, int verwendeteZeile, String inhalt) {
        switch (verwendeteZeile) {
        case 1:
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", inhalt);
            break;
        case 2:
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", inhalt);
            break;
        case 3:
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", inhalt);
            break;
        case 4:
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", inhalt);
            break;
        case 5:
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", inhalt);
            break;
        default:
            break;
        }
    }

    /**
     * Zeilefuellen gast kurz.
     *
     * @param rpDrucken       the rp drucken
     * @param rpVariablen     the rp variablen
     * @param verwendeteZeile the verwendete zeile
     * @param inhalt          the inhalt
     */
    private void zeilefuellenGastKurz(RpDrucken rpDrucken, RpVariablen rpVariablen, int verwendeteZeile,
            String inhalt) {
        switch (verwendeteZeile) {
        case 1:
            rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile1", inhalt);
            break;
        case 2:
            rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile2", inhalt);
            break;
        case 3:
            rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile3", inhalt);
            break;
        case 4:
            rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile4", inhalt);
            break;
        case 5:
            rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile5", inhalt);
            break;
        default:
        }
    }

    /**
     * Leere gast.
     *
     * @param rpDrucken   the rp drucken
     * @param rpVariablen the rp variablen
     */
    private void leereGast(RpDrucken rpDrucken, RpVariablen rpVariablen) {
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", "");
        rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");

        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile1", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile2", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile3", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile4", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile5", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile6", "");

    }

    /**
     * Gk drucke.
     *
     * @param rpDrucken   the rp drucken
     * @param rpVariablen the rp variablen
     * @param lfdNr       the lfd nr
     * @param einzelDruck the einzel druck
     */
    private void gkDrucke(RpDrucken rpDrucken, RpVariablen rpVariablen, 
            int lfdNr, boolean einzelDruck, DbBundle lDbBundle) {

        EclMeldung lEclMeldung=ergebnisArrayMeldung[lfdNr];
        EclPersonenNatJur lEclPersonenNatJur = ergebnisArrayPersonenNatJur[lfdNr];
        //    	EclPersonenNatJurVersandadresse lEclPersonenNatJurVersandadresse=ergebnisArrayPersonenNatJurVersandadresse[lfdNr];
        //		EclWillenserklaerung lEclWillenserklaerung=ergebnisArrayWillenserklaerung[lfdNr];
        //		EclWillenserklaerungZusatz lEclWillenserklaerungZusatz=ergebnisArrayWillenserklaerungZusatz[lfdNr];
        //		EclAnrede lEclAnrede=ergebnisArrayAnrede[lfdNr];
        //		EclAnrede lEclAnredeVersand=ergebnisArrayAnredeVersand[lfdNr];
        //		EclStaaten lEclStaaten=ergebnisArrayStaaten[lfdNr];
        //		EclStaaten lEclStaaten1=ergebnisArrayStaaten1[lfdNr];
        //		EclPersonenNatJur lEclPersonenNatJur1=ergebnisArrayPersonenNatJur1[lfdNr];
        //		EclAktienregister lEclAktienregister=ergebnisArrayAktienregister[lfdNr];
        //		EclAktienregisterLoginDaten lEclAktienregisterLoginDaten=ergebnisArrayAktienregisterLoginDaten[lfdNr];

        /*Selektion wird bereits auf Server durchgeführt*/
        //		if (lEclWillenserklaerung.erteiltAufWeg>=21 && lEclWillenserklaerung.erteiltAufWeg<=29){
        //			if (wegSelektion==2){return;}
        //		}
        //		else{
        //			if (wegSelektion==1){return;}
        //		}

        leereGast(rpDrucken, rpVariablen);

        if (ParamSpezial.ku287(ParamS.clGlobalVar.mandant)==false) {
            /*Versandadresse*/
            int verwendeteZeile = 1;
            String aufbereitung = "";

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.zuHdCo;
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGast(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.vorname;
            if (!lEclPersonenNatJur.name.isEmpty()) {
                if (!aufbereitung.isEmpty()) {
                    aufbereitung = aufbereitung + " ";
                }
                aufbereitung = aufbereitung + lEclPersonenNatJur.name;
            }
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGast(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.strasse;
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGast(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.plz;
            if (!lEclPersonenNatJur.ort.isEmpty()) {
                if (!aufbereitung.isEmpty()) {
                    aufbereitung = aufbereitung + " ";
                }
                aufbereitung = aufbereitung + lEclPersonenNatJur.ort;
            }
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGast(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            /*VersandadresseKurz*/
            verwendeteZeile = 1;
            aufbereitung = "";

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.vorname;
            if (!lEclPersonenNatJur.name.isEmpty()) {
                if (!aufbereitung.isEmpty()) {
                    aufbereitung = aufbereitung + " ";
                }
                aufbereitung = aufbereitung + lEclPersonenNatJur.name;
            }
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGastKurz(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.zuHdCo;
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGastKurz(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }

            aufbereitung = "";
            aufbereitung = lEclPersonenNatJur.plz;
            if (!lEclPersonenNatJur.ort.isEmpty()) {
                if (!aufbereitung.isEmpty()) {
                    aufbereitung = aufbereitung + " ";
                }
                aufbereitung = aufbereitung + lEclPersonenNatJur.ort;
            }
            if (!aufbereitung.isEmpty()) {
                zeilefuellenGastKurz(rpDrucken, rpVariablen, verwendeteZeile, aufbereitung);
                verwendeteZeile++;
            }
        }
        else {
            /*ku287*/
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", lEclPersonenNatJur.zusatz1);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", lEclPersonenNatJur.name);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", lEclPersonenNatJur.strasse);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", lEclPersonenNatJur.ort);
            rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", lEclPersonenNatJur.zusatz2);
        }
        //		Ll.LlDefineVariable(nLLJob_, "Versandadresse.Verwenden", Integer.toString(versandadresseverwenden));

        
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent",  BlZutrittsIdent.aufbereitenFuerAnzeige(lEclMeldung.zutrittsIdent, "00"));
        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereNrKomplett(
                lEclMeldung.zutrittsIdent, "00", KonstKartenklasse.gastkartennummer, KonstKartenart.erstzugang));
 
        
        if (einzelDruck == false) {
            selektiert[lfdNr] = 1;
            anzSaetzeSelektiert++;
        }

        rpDrucken.druckenFormular();
        rpDrucken.druckenFormularSatzwechsel();
        //		if (einzelDruck==false){
        //			druckquittungErzeugen();
        //		}

    }

    /**
     * Druckquittung erzeugen.
     */
    private void druckquittungErzeugen() {
        int i;

        WSClient wsClient = new WSClient();

        List<EclWillenserklaerung> listEclWillenserklaerung = new LinkedList<EclWillenserklaerung>();
        List<EclWillenserklaerungZusatz> listEclWillenserklaerungZusatz = new LinkedList<EclWillenserklaerungZusatz>();

        for (i = 0; i < anzSaetze; i++) {
            if (selektiert[i] == 1 && ergebnisArrayMeldung[i] != null) {
                CaBug.druckeLog("" + ergebnisArrayWillenserklaerung[i].willenserklaerungIdent, logDrucken, 10);
                listEclWillenserklaerung.add(ergebnisArrayWillenserklaerung[i]);
                listEclWillenserklaerungZusatz.add(ergebnisArrayWillenserklaerungZusatz[i]);
            }
        }

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/

        WEEintrittskarteGedruckt weEintrittskarteGedruckt = new WEEintrittskarteGedruckt();
        weEintrittskarteGedruckt.setWeLoginVerify(weLoginVerify);
        weEintrittskarteGedruckt.setListWillenserklaerung(listEclWillenserklaerung);
        weEintrittskarteGedruckt.setListWillenserklaerungZusatz(listEclWillenserklaerungZusatz);
        weEintrittskarteGedruckt.setDrucklauf(durchgefuehrterDrucklauf);

        wsClient.eintrittskarteGedruckt(weEintrittskarteGedruckt);

        //        	WEEintrittskarteGedrucktRC weEintrittskarteGedrucktRC=wsClient.eintrittskarteGedruckt(weEintrittskarteGedruckt);
        btnAufbereiten.setDisable(false);

    }

    /**
     * Einzel druck.
     *
     * @param ekNummer      the ek nummer
     * @param ekNummerNeben the ek nummer neben
     */
    public void einzelDruck(String ekNummer, String ekNummerNeben) {

        /**************
         * Drucklauf aufbereiten (für einzelne Eintrittskarte)
         ****************/
        /*Nur für Parameter*/
        DbBundle lDbBundle=new DbBundle();
        
        int ekVersandFuerAlleImPortalAngefordertenSelektion=lDbBundle.param.paramPortal.ekVersandFuerAlleImPortalAngeforderten;

        
        
        WSClient wsClient = new WSClient();

        WEEintrittskartenDrucklaufEinzelGet weEintrittskartenDrucklaufEinzelGet = new WEEintrittskartenDrucklaufEinzelGet();

        weEintrittskartenDrucklaufEinzelGet.setEkNummer(ekNummer);
        weEintrittskartenDrucklaufEinzelGet.setEkNummerNeben(ekNummerNeben);
        weEintrittskartenDrucklaufEinzelGet.setEkVersandFuerAlleImPortalAngefordertenSelektion(ekVersandFuerAlleImPortalAngefordertenSelektion);

        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weEintrittskartenDrucklaufEinzelGet.setWeLoginVerify(weLoginVerify);

        WEEintrittskartenDrucklaufGetRC weEintrittskartenDrucklaufGetRC = wsClient
                .eintrittskartenDrucklaufEinzelGet(weEintrittskartenDrucklaufEinzelGet);

        if (weEintrittskartenDrucklaufGetRC.getRc() < 1) {
            return;
        }

        anzSaetze = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung().length;
        if (anzSaetze == 0) {
            return;
        }

        ergebnisArrayMeldung = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung();
        ergebnisArrayPersonenNatJur = weEintrittskartenDrucklaufGetRC.getErgebnisArrayPersonenNatJur();
        ergebnisArrayWillenserklaerung = weEintrittskartenDrucklaufGetRC.getErgebnisArrayWillenserklaerung();
        ergebnisArrayWillenserklaerungZusatz = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayWillenserklaerungZusatz();
        ergebnisArrayAnrede = weEintrittskartenDrucklaufGetRC.getErgebnisArrayAnrede();
        ergebnisArrayStaaten = weEintrittskartenDrucklaufGetRC.getErgebnisArrayStaaten();
        ergebnisArrayPersonenNatJur1 = weEintrittskartenDrucklaufGetRC.getErgebnisArrayPersonenNatJur1();
        ergebnisArrayAktienregister = weEintrittskartenDrucklaufGetRC.getErgebnisArrayAktienregister();
        ergebnisArrayLoginDaten = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayLoginDaten();
        ergebnisArrayLoginDaten1 = weEintrittskartenDrucklaufGetRC
                .getErgebnisArrayLoginDaten1();

        eintrittskarteDrucken(true, 3);
        return;

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
