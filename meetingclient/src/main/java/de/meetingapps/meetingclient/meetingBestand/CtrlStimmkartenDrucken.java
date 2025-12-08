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
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterNummerAufbereiten;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlStimmkartendruck;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungBatch;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComHVParam.ParamAkkreditierung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGet;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGetRC;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskartenDrucklaufEinzelGet;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskartenDrucklaufGetRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlStimmkartenDrucken.
 */
public class CtrlStimmkartenDrucken {

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

    /** The selektiert. */
    private int[] selektiert = null;

    /** Anzahl der Sätze in den Arrays. */
    private int anzSaetze = 0;

    /** Anzahl Sätze selektiert (ermittelt während tatsächlichem Drucken. */
    private int anzSaetzeSelektiert = 0;

    /** The durchgefuehrter drucklauf. */
    private int durchgefuehrterDrucklauf;

    /** 1=Gäste, 2=Aktionäre. */
    private int gaesteOderAktionaereSelektion = 2;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /*???????????????Ab hier neu???????????????????*/

    /** The grid pane. */
    @FXML
    private GridPane gridPane;

    /** The tg nicht gedruckt. */
    @FXML
    private RadioButton tgNichtGedruckt;

    /** The tgg gedruckt. */
    @FXML
    private ToggleGroup tggGedruckt;

    /** The tg wiederholung. */
    @FXML
    private RadioButton tgWiederholung;

    /** The tgg geprueft. */
    @FXML
    private ToggleGroup tggGeprueft;

    /** The lb ek sk nr. */
    @FXML
    private Label lbEkSkNr;

    /** The tf drucklauf gewaehlt. */
    @FXML
    private TextField tfDrucklaufGewaehlt;

    /** The tf nachricht. */
    @FXML
    private TextField tfNachricht;

    /** The btn drucklauf. */
    @FXML
    private Button btnDrucklauf;

    /** The btn anzahl ohne EK ermitteln. */
    @FXML
    private Button btnAnzahlOhneEKErmitteln;

    /** The lbl anzahl aktionaere ohne EK. */
    @FXML
    private Label lblAnzahlAktionaereOhneEK;

    /** The btn aktionaere mit EK versehen. */
    @FXML
    private Button btnAktionaereMitEKVersehen;

    /** The lbl stimmkarte. */
    @FXML
    private Label lblStimmkarte;

    /** The lb tauschart. */
    @FXML
    private Label lbTauschart;

    /** The lb von nr. */
    @FXML
    private Label lbVonNr;

    /** The tf von nr. */
    @FXML
    private TextField tfVonNr;

    /** The lb bis nr. */
    @FXML
    private Label lbBisNr;

    /** The tf bis nr. */
    @FXML
    private TextField tfBisNr;

    /** The lb selektion. */
    @FXML
    private Label lbSelektion;

    /** The lb reihenfolge. */
    @FXML
    private Label lbReihenfolge;

    /** The btn drucken. */
    @FXML
    private Button btnDrucken;

    /** The tg alle. */
    @FXML
    private RadioButton tgAlle;

    /** The lbl drucklauf. */
    @FXML
    private Label lblDrucklauf;

    /** The cb aktionaere nicht in sammelkarten. */
    @FXML
    private CheckBox cbAktionaereNichtInSammelkarten;

    /** The cb aktionaere in sammelkarten. */
    @FXML
    private CheckBox cbAktionaereInSammelkarten;

    /** The cb sammelkarten. */
    @FXML
    private CheckBox cbSammelkarten;

    /** The tg alphabet. */
    @FXML
    private RadioButton tgAlphabet;

    /** The tgg reihenfolge. */
    @FXML
    private ToggleGroup tggReihenfolge;

    /** The tg EK nr. */
    @FXML
    private RadioButton tgEKNr;

    /** The cb gattung. */
    /*Individuelle FX-Elemente*/
    @FXML
    private ComboBox<CbElement> cbGattung;

    /** The cb A gattung. */
    private CbAllgemein cbAGattung = null;

    /** The cb stimmkarte. */
    @FXML
    private ComboBox<CbElement> cbStimmkarte;

    /** The cb A stimmkarte. */
    private CbAllgemein cbAStimmkarte = null;

    /** The gewaehlt gattung. */
    /*In Drop-Box ausgewählte Wert. -1 => noch nichts gewählt*/
    public int gewaehltGattung = -1;

    /** The gewaehlt stimmkarte. */
    public int gewaehltStimmkarte = -1;

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
        assert tggGeprueft != null
                : "fx:id=\"tggGeprueft\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert btnDrucken != null
                : "fx:id=\"btnDrucken\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfDrucklaufGewaehlt != null
                : "fx:id=\"tfDrucklaufGewaehlt\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";
        assert tfNachricht != null
                : "fx:id=\"tfNachricht\" was not injected: check your FXML file 'EintrittskartenDrucken.fxml'.";

        /*************** Ab hier individuell *************************/

        initMaskenfelder();

        cbGattung.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 != 0) {
                    gewaehltGattung = neuerWert.ident1;
                    gewaehltStimmkarte = -1;
                    CaBug.druckeLog("cbGattung Changed auf " + gewaehltGattung, logDrucken, 10);
                    initialisiereCbStimmkarten();
                    aktiviereMaskenfelder();
                }
            }
        });

        cbStimmkarte.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                gewaehltStimmkarte = neuerWert.ident1;
                CaBug.druckeLog("cbStimmkarte Changed auf " + gewaehltStimmkarte, logDrucken, 10);
                aktiviereMaskenfelder();
            }
        });

        /*Ab hier alt*/

        WSClient wsClient = new WSClient();
        WEDrucklaeufeGet weDrucklaeufeGet = new WEDrucklaeufeGet();
        weDrucklaeufeGet.drucklaufArt = 1;
        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weDrucklaeufeGet.setWeLoginVerify(weLoginVerify);

        WEDrucklaeufeGetRC weDrucklaeufeGetRC = wsClient.drucklaeufeGet(weDrucklaeufeGet);

        if (ParamS.param.paramBasis.ekSeitenzahl == 2) {
            //            rbGesamtPDF.setDisable(true);
        }

        cbGattung.prefWidthProperty().bind(eigeneStage.widthProperty().divide(3));
        cbStimmkarte.prefWidthProperty().bind(eigeneStage.widthProperty().divide(3));
    }

    /**
     * Do btn anzahl ohne EK ermitteln.
     *
     * @param event the event
     */
    @FXML
    void doBtnAnzahlOhneEKErmitteln(ActionEvent event) {

        DbBundle dbBundle = new DbBundle();
        BlWillenserklaerungBatch blWillenserklaerungBatch = new BlWillenserklaerungBatch(false, dbBundle);
        blWillenserklaerungBatch.liefereAnzahlAngemeldeteOhneEK();
        lblAnzahlAktionaereOhneEK.setText(Integer.toString(blWillenserklaerungBatch.rcAnzahlAngemeldeteOhneEK));

    }

    /**
     * Do btn aktionaere mit EK versehen.
     *
     * @param event the event
     */
    @FXML
    void doBtnAktionaereMitEKVersehen(ActionEvent event) {
        DbBundle dbBundle = new DbBundle();
        BlWillenserklaerungBatch blWillenserklaerungBatch = new BlWillenserklaerungBatch(false, dbBundle);
        blWillenserklaerungBatch.erzeugeEKFuerAlleAngemeldeten();
        int anzEKAusgestellt = blWillenserklaerungBatch.rcAnzahlAngemeldeteOhneEK;

        blWillenserklaerungBatch.liefereAnzahlAngemeldeteOhneEK();
        lblAnzahlAktionaereOhneEK.setText(Integer.toString(blWillenserklaerungBatch.rcAnzahlAngemeldeteOhneEK));

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Anzahl ausgestellte Eintrittskarten: " + Integer.toString(anzEKAusgestellt));
    }

    /**
     * On btn drucklauf.
     *
     * @param event the event
     */
    @FXML
    void onBtnDrucklauf(ActionEvent event) {
        /*XXX*/

        //        Stage neuerDialog = new Stage();
        //        CaIcon.bestandsverwaltung(neuerDialog);
        //
        //        CtrDrucklaufAuswaehlen controllerFenster = new CtrDrucklaufAuswaehlen();
        //
        //        controllerFenster.init(neuerDialog);
        //        controllerFenster.drucklaufListe = drucklaufArray;
        //        controllerFenster.verarbeitungslaufArt = KonstVerarbeitungslaufArt.eintrittskartendruck;
        //
        //        CaController caController = new CaController();
        //        caController.open(neuerDialog, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/DrucklaufAuswaehlen.fxml", 1200, 850,
        //                "Drucklauf auswählen", true);
        //
        //        if (controllerFenster.verarbeitungslaufNr.isEmpty()) {
        //            return;
        //        } else {
        //            tfDrucklaufGewaehlt.setText(controllerFenster.verarbeitungslaufNr);
        //            tgWiederholung.setSelected(true);
        //            return;
        //        }

    }

    /** The tausch beliebig. */
    private boolean tauschBeliebig = false;

    /** The selektion aktionaere nicht in sammelkarten. */
    private boolean selektionAktionaereNichtInSammelkarten = false;

    /** The selektion aktionaere in sammelkarten. */
    private boolean selektionAktionaereInSammelkarten = false;

    /** The selektion sammelkarten. */
    private boolean selektionSammelkarten = false;

    /**
     * On btn drucken.
     *
     * @param event the event
     */
    @FXML
    void onBtnDrucken(ActionEvent event) {
        CaBug.druckeLog("Start", logDrucken, 10);

        ParamAkkreditierung lParamAkkreditierung = ParamS.param.paramAkkreditierung;

        tauschBeliebig = lParamAkkreditierung.pPraesenzStimmkarteTauschBeliebig[gewaehltGattung - 1][gewaehltStimmkarte
                - 1] == 1;

        drucken(tfVonNr.getText().trim(), tfBisNr.getText().trim(), tauschBeliebig,
                cbAktionaereNichtInSammelkarten.isSelected(), cbAktionaereInSammelkarten.isSelected(),
                cbSammelkarten.isSelected(), tgAlphabet.isSelected());

    }

    /**
     * Drucken.
     *
     * @param ek the ek
     */
    public void drucken(String ek) {

        drucken(ek, ek, false, true, true, true, false);

    }

    /**
     * Drucken.
     *
     * @param vonEk                                  the von ek
     * @param bisEk                                  the bis ek
     * @param tauschbeliebig                         the tauschbeliebig
     * @param selektionAktionaereNichtInSammelkarten the selektion aktionaere nicht
     *                                               in sammelkarten
     * @param selektionAktionaereInSammelkarten      the selektion aktionaere in
     *                                               sammelkarten
     * @param selektionSammelkarten                  the selektion sammelkarten
     * @param sortAlphabet                           the sort alphabet
     */
    public void drucken(String vonEk, String bisEk, Boolean tauschbeliebig,
            Boolean selektionAktionaereNichtInSammelkarten, Boolean selektionAktionaereInSammelkarten,
            Boolean selektionSammelkarten, Boolean sortAlphabet) {

        if (!tauschBeliebig) {

            if (!selektionAktionaereNichtInSammelkarten && !selektionAktionaereInSammelkarten
                    && !selektionSammelkarten) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Bitte eine Selektion aktivieren");
                return;
            }
        }

        int vonNr = 0;
        if (!vonEk.isEmpty()) {
            if (!CaString.isNummern(vonEk)) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Nichts oder eine gültige Zahl bei Von Nr eingeben");
                return;
            }
            vonNr = Integer.parseInt(vonEk);
        }

        int bisNr = 0;
        if (!bisEk.isEmpty()) {
            if (!CaString.isNummern(bisEk)) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Nichts oder eine gültige Zahl bei Bis Nr eingeben");
                return;
            }
            bisNr = Integer.parseInt(bisEk);
        }

        if (bisNr < vonNr) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bis Nr muß leer sein oder größer als Von Nr");
            return;
        }
        if ((bisNr == 0 || vonNr == 0) && tauschBeliebig) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Von Nr und Bis Nr dürfen nicht leer sein");
            return;
        }

        DbBundle lDbBundle = new DbBundle();

        BlStimmkartendruck blStimmkartendruck = new BlStimmkartendruck(false, lDbBundle);
        String formularNumer = ParamS.param.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[gewaehltGattung
                - 1][gewaehltStimmkarte - 1];

        CaBug.druckeLog("bei Bestimmung Formularnummer gewaehltGattung=" + gewaehltGattung + " gewaehltStimmkarte="
                + gewaehltStimmkarte, logDrucken, 10);

        CaBug.druckeLog("formularNumer=" + formularNumer, logDrucken, 10);
        if (!tauschBeliebig) {
            /*Ausgestellte Eintrittskarten holen*/
            int selektion = 0;
            if (selektionAktionaereNichtInSammelkarten) {
                selektion = (selektion | BlStimmkartendruck.KONST_SELEKTION_AKTIONAERE_NICHT_IN_SAMMELKARTEN);
            }
            if (selektionAktionaereInSammelkarten) {
                selektion = (selektion | BlStimmkartendruck.KONST_SELEKTION_AKTIONAERE_IN_SAMMELKARTEN);
            }
            if (selektionSammelkarten) {
                selektion = (selektion | BlStimmkartendruck.KONST_SELEKTION_SAMMELKARTEN);
            }
            int sortierung = sortAlphabet ? 2 : 1;

            CaBug.druckeLog("selektion=" + selektion + " vonNr=" + vonNr + " bisNr=" + bisNr, logDrucken, 10);

            blStimmkartendruck.holeMeldedatenFuerTausch1Zu1(gewaehltGattung, BlStimmkartendruck.KONST_DRUCKLAUF_ALLE,
                    selektion, vonNr, bisNr, 0, sortierung);
            CaBug.druckeLog("blStimmkartendruck.rcMeldungsliste.size()=" + blStimmkartendruck.rcMeldungsliste.size(),
                    logDrucken, 10);

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.initFormular(lDbBundle);

            RpVariablen rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.stimmkarten(formularNumer, rpDrucken);

            boolean bRc = rpDrucken.startFormular();
            if (bRc == false) {
                CaBug.druckeLog("startFormular false", logDrucken, 10);
                return;
            }

            CaBug.druckeLog("Vor Aufruf blStimmkartendruck.drucke1Zu1 gewaehltGattung=" + gewaehltGattung
                    + " gewaehltStimmkarte=" + gewaehltStimmkarte, logDrucken, 10);
            blStimmkartendruck.drucke1Zu1(rpDrucken, rpVariablen, gewaehltGattung, gewaehltStimmkarte);

            rpDrucken.endeFormular();

        } else {
            /*Tausch beliebig*/
            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.initFormular(lDbBundle);

            RpVariablen rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.stimmkarten(formularNumer, rpDrucken);

            boolean bRc = rpDrucken.startFormular();
            if (bRc == false) {
                return;
            }

            blStimmkartendruck.druckeTauschBeliebig(rpDrucken, rpVariablen, gewaehltGattung, gewaehltStimmkarte, vonNr,
                    bisNr);

            rpDrucken.endeFormular();
        }

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

                if (gaesteOderAktionaereSelektion == 2) {
                    ekDrucke(rpDrucken, rpVariablen, i, einzelDruck);
                } else {
                    gkDrucke(rpDrucken, rpVariablen, i, einzelDruck);
                }

                if (druckVerlauf != 1) {
                    rpDrucken.endeFormular();
                }

            }
        }

        if (druckVerlauf == 1) {
            rpDrucken.endeFormular();
        }

        if (einzelDruck == false) {
            //            druckquittungErzeugen();
        }

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

        if (lEclWillenserklaerungZusatz.identVertreterPersonNatJur != 0) {
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", lEclLoginDaten1.loginKennung);
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

        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile1", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile2", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile3", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile4", "");
        rpVariablen.fuelleVariable(rpDrucken, "VersandadresseKurz.Zeile5", "");

    }

    /**
     * Gk drucke.
     *
     * @param rpDrucken   the rp drucken
     * @param rpVariablen the rp variablen
     * @param lfdNr       the lfd nr
     * @param einzelDruck the einzel druck
     */
    private void gkDrucke(RpDrucken rpDrucken, RpVariablen rpVariablen, int lfdNr, boolean einzelDruck) {

        //    	EclMeldung lEclMeldung=ergebnisArrayMeldung[lfdNr];
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

        //		Ll.LlDefineVariable(nLLJob_, "Versandadresse.Verwenden", Integer.toString(versandadresseverwenden));

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
        ergebnisArrayLoginDaten = weEintrittskartenDrucklaufGetRC.getErgebnisArrayLoginDaten();
        ergebnisArrayLoginDaten1 = weEintrittskartenDrucklaufGetRC.getErgebnisArrayLoginDaten1();

        eintrittskarteDrucken(true, 3);
        return;

    }

    /**
     * Aufzurufen bei Erstinitialisierung / Neu-Initialisierung der Maske.
     */
    private void initMaskenfelder() {
        gewaehltGattung = -1;
        gewaehltStimmkarte = -1;

        cbAGattung = new CbAllgemein(cbGattung);
        cbAStimmkarte = new CbAllgemein(cbStimmkarte);

        lbTauschart.setText("");

        tgAlle.setSelected(true);
        tfDrucklaufGewaehlt.setText("");
        tfVonNr.setText("");
        tfBisNr.setText("");

        cbAktionaereNichtInSammelkarten.setSelected(true);
        cbAktionaereInSammelkarten.setSelected(false);
        cbSammelkarten.setSelected(false);

        tgAlphabet.setSelected(true);

        /*DropDown Gattung füllen*/
        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                CbElement hGattung = new CbElement();
                hGattung.ident1 = i;
                hGattung.anzeige = ParamS.param.paramBasis.getGattungBezeichnung(i);
                cbAGattung.addElement(hGattung);
            }
        }

        aktiviereMaskenfelder();
    }

    /**
     * Stimmkarten-Dropdown füllen.
     */
    private void initialisiereCbStimmkarten() {
        if (gewaehltGattung < 1) {
            CaBug.drucke("001");
            return;
        }

        cbAStimmkarte = new CbAllgemein(cbStimmkarte);

        ParamAkkreditierung lParamAkkreditierung = ParamS.param.paramAkkreditierung;
        for (int i = 0; i < 4; i++) {
            if (lParamAkkreditierung.pPraesenzStimmkarteAktiv[gewaehltGattung - 1][i] == 1) {
                CbElement hStimmkarte = new CbElement();
                hStimmkarte.ident1 = i + 1;
                hStimmkarte.anzeige = lParamAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[gewaehltGattung
                        - 1][i];
                cbAStimmkarte.addElement(hStimmkarte);
            }
        }
    }

    /**
     * Nach Veränderung der Drop-Down-Felder Gattung / Stimmkarte aufzurufen.
     */
    private void aktiviereMaskenfelder() {
        if (gewaehltGattung == -1) {
            lblStimmkarte.setVisible(false);
            cbStimmkarte.setVisible(false);
        } else {
            lblStimmkarte.setVisible(true);
            cbStimmkarte.setVisible(true);
        }
        if (gewaehltGattung == -1 || gewaehltStimmkarte == -1) {
            /*Restliche Felder ausblenden - das wars dann */
            lbTauschart.setVisible(false);
            lblDrucklauf.setVisible(false);
            tgNichtGedruckt.setVisible(false);
            tgWiederholung.setVisible(false);
            btnDrucklauf.setVisible(false);
            tfDrucklaufGewaehlt.setVisible(false);
            tgAlle.setVisible(false);

            lbEkSkNr.setVisible(false);
            lbVonNr.setVisible(false);
            tfVonNr.setVisible(false);
            lbBisNr.setVisible(false);
            tfBisNr.setVisible(false);

            lbSelektion.setVisible(false);
            cbAktionaereNichtInSammelkarten.setVisible(false);
            cbAktionaereInSammelkarten.setVisible(false);
            cbSammelkarten.setVisible(false);

            lbReihenfolge.setVisible(false);
            tgAlphabet.setVisible(false);
            tgEKNr.setVisible(false);

            btnDrucken.setVisible(false);
            return;
        }

        /*Hier: sowohl Gattung als auch Stimmkarte ist ausgewählt*/
        ParamAkkreditierung lParamAkkreditierung = ParamS.param.paramAkkreditierung;

        if (lParamAkkreditierung.pPraesenzStimmkarteTauschBeliebig[gewaehltGattung - 1][gewaehltStimmkarte - 1] == 1) {
            lbTauschart.setText("Tausch beliebig");
            /*XXX*/
            lblDrucklauf.setVisible(false);
            tgNichtGedruckt.setVisible(false);
            tgWiederholung.setVisible(false);
            btnDrucklauf.setVisible(false);
            tfDrucklaufGewaehlt.setVisible(false);
            tgAlle.setVisible(false);

            lbEkSkNr.setText("Stimmkarten-Nr");

            lbSelektion.setVisible(false);
            cbAktionaereNichtInSammelkarten.setVisible(false);
            cbAktionaereInSammelkarten.setVisible(false);
            cbSammelkarten.setVisible(false);

            lbReihenfolge.setVisible(false);
            tgAlphabet.setVisible(false);
            tgEKNr.setVisible(false);
        } else {
            lbTauschart.setText("Tausch 1:1");
            /*XXX*/
            lblDrucklauf.setVisible(true);
            tgNichtGedruckt.setVisible(false);
            tgWiederholung.setVisible(false);
            btnDrucklauf.setVisible(false);
            tfDrucklaufGewaehlt.setVisible(false);
            tgAlle.setVisible(true);

            lbEkSkNr.setText("Eintrittskarten-Nr");

            lbSelektion.setVisible(true);
            cbAktionaereNichtInSammelkarten.setVisible(true);
            cbAktionaereInSammelkarten.setVisible(true);
            cbSammelkarten.setVisible(true);

            lbReihenfolge.setVisible(true);
            tgAlphabet.setVisible(true);
            tgEKNr.setVisible(true);
        }

        lbTauschart.setVisible(true);

        lbEkSkNr.setVisible(true);
        lbVonNr.setVisible(true);
        tfVonNr.setVisible(true);
        lbBisNr.setVisible(true);
        tfBisNr.setVisible(true);

        btnDrucken.setVisible(true);

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
