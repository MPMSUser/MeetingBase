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
package de.meetingapps.meetingclient.meetingFrontOffice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClient.BlClientPraesenzBuchen;
import de.meetingapps.meetingclient.meetingClient.BlParameterUeberWebService;
import de.meetingapps.meetingclient.meetingClient.ClBonPrinter;
import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.CInjects;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungAktionaerLesen;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungAktionaerLesenRC;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungSpeichern;
import de.meetingapps.meetingportal.meetComWE.WEAbstimmungSpeichernRC;
import de.meetingapps.meetingportal.meetComWE.WEAutoTest;
import de.meetingapps.meetingportal.meetComWE.WEAutoTestRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrage;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrageRC;
import de.meetingapps.meetingportal.meetComWE.WETabletRuecksetzen;
import de.meetingapps.meetingportal.meetComWE.WETabletRuecksetzenRC;
import de.meetingapps.meetingportal.meetComWE.WETabletSetzeStatus;
import de.meetingapps.meetingportal.meetComWE.WETabletSetzeStatusRC;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//1920x1280
/*Testablauf
 * > Präsente Karte 501
 * > Präsente Karte wiederholen 501
 * 
 * > Noch nie präsente Karte buchen 502
 * > Noch nie präsente Karte wiederholen 502
 * 
 * > Noch nie präsente Karte mit Vertreter buchen 503
 * > Noch nie präsente Karte mit Vertreter wiederholen 503
 * 
 * > Abgagangene Karte buchen 504
 * > Abgagangene Karte wiederholen 504
 * 
 * > Abgagangene Karte mit Vertreter buchen 505
 * > Abgagangene Karte mit Vertreter wiederholen 505
 * 
 * > Ehemals selbst präsente Karte, die an SRV HV gegeben hat 506
 * 
 * > Karte in Sammelkarte SRV Papier 507
 * 
 * 
 * > Karte 508, die neue EK zugewiesen bekommen hat, sowohl mit neuer als auch mit alter Karte buchen
 * 
 * > PendingPool:
 * >> Karte buchen, die nach Pending Pool zugegangen ist 509
 * >> Karte buchen, die nach Pending Pool abgegangen ist 510
 * 
 
 */

/**
 * The Class CtrlAbstimmungsStage.
 */
public class CtrlAbstimmungsStage extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;
    
    @FXML
    private Pane rootPane;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn stimme abgeben. */
    @FXML
    private Button btnStimmeAbgeben;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn vor. */
    @FXML
    private Button btnVor;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** The btn durchgefuehrt. */
    @FXML
    private Button btnDurchgefuehrt;

    /** The tf scan feld. */
    @FXML
    private TextField tfScanFeld;

    /** The lbl fehler meldung. */
    @FXML
    private Label lblFehlerMeldung;

    /** The lbl grosse meldung. */
    @FXML
    private Label lblGrosseMeldung;

    /** The btn abstimmungs modus. */
    @FXML
    private Button btnAbstimmungsModus;

    /** The pn scroll pane. */
    @FXML
    private ScrollPane pnScrollPane;

    /** The pane grid pane. */
    @FXML
    private GridPane paneGridPane;

    /** The ws client. */
    private WSClient wsClient = null;

    //    public WEAbstimmungLeseAktivenAbstimmungsblock weAbstimmungLeseAktivenAbstimmungsblock=null;
    //    public WEAbstimmungLeseAktivenAbstimmungsblockRC weAbstimmungLeseAktivenAbstimmungsblockRC=null;
    /** The we abstimmung aktionaer lesen RC. */
    //    public WEStimmkartenblockGetRC weStimmkartenblockGetRC=null;
    private WEAbstimmungAktionaerLesenRC[] weAbstimmungAktionaerLesenRC = null;

    /** The we abstimmung aktionaer lesen. */
    private WEAbstimmungAktionaerLesen weAbstimmungAktionaerLesen = null;

    /** The angezeigte seite. */
    private int angezeigteSeite = 0;

    /** Anzahl der Abstimmungen auf der angezeigten Seite. */
    private int anzAbstimmungenSeite = 0;

    /** The lbl agenda TOP. */
    private Label[] lblAgendaTOP = null;

    /** The lbl agenda text. */
    private Label[] lblAgendaText = null;

    /** The btn agenda ja. */
    private Button[] btnAgendaJa = null;

    /** The btn agenda nein. */
    private Button[] btnAgendaNein = null;

    /** The btn agenda enthaltung. */
    private Button[] btnAgendaEnthaltung = null;

    /** The stimmart erste spalte. */
    private int stimmartErsteSpalte = -1;

    /** The btn gesamt ja. */
    private Button btnGesamtJa = null;

    /** The btn gesamt nein. */
    private Button btnGesamtNein = null;

    /** The btn gesamt enthaltung. */
    private Button btnGesamtEnthaltung = null;

    /** The scan string. */
    String scanString = "";

    /** Anzahl der Abstimmungen in Agenda. */
    int anzAgenda = 0;

    /**1 = Bereit zum Scannen
     * 2 = Stimmen markieren
     * 3 = Stimme wurde abgegeben (Quittungsanzeige)
     * 
     * Für Persoenliches Tablet:
     * 11 = Noch nicht zugeordnet
     * 12 = Bereit zur nächsten Abstimmung
     * 13 = Offline-Betrieb - retry
     * 14 = Derzeit keine Abstimmung aktiv
     *  
     */
    int statusAnzeige = 0;

    /** The offline betrieb. */
    private boolean offlineBetrieb = false;

    /**++++++++++++++++++++++++++++++++++++++++++Für persönliches Tablet:+++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public boolean tabletIstPersoenlichesTablet = false;

    /**
     * +++Die folgenden Werte sind nur belegt, wenn
     * tabletIstPersoenlichesTablet==true++.
     */
    /** Zordnung ist bereits erfolgt */
    public boolean tabletIstBereitsZugordnet = false;

    /**
     * Nummer, die für die Abgabe der Abstimmung verwendet wird Kann leer sein, dann
     * noch nicht zugeordnet.
     */
    public String nummerIdentifikation = "";

    /** Anzeige (für Teilnehmername). */
    public String anzeigeName = "";
    
    private StackPane loading;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnStimmeAbgeben != null : "fx:id=\"btnStimmeAbgeben\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert tfScanFeld != null : "fx:id=\"tfScanFeld\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert lblFehlerMeldung != null : "fx:id=\"lblFehlerMeldung\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert btnAbstimmungsModus != null : "fx:id=\"btnAbstimmungsModus\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert pnScrollPane != null : "fx:id=\"pnScrollPane\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert paneGridPane != null : "fx:id=\"paneGridPane\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";
        assert btnDurchgefuehrt != null : "fx:id=\"btnDurchgefuehrt\" was not injected: check your FXML file 'AbstimmungsStage.fxml'.";

        /**********************
         * Ab hier individuell
         **************************************************/
        if (btnZurueck == null) {
            btnZurueck = new Button();
        }
        if (btnVor == null) {
            btnVor = new Button();
        }

        wsClient = new WSClient();
        
        /** Prüfen, ob Rücksetzen der Abstimmungsdaten erforderlich */
        WETabletRuecksetzen weTabletRuecksetzen = new WETabletRuecksetzen();
        WELoginVerify weLoginVerify = new WELoginVerify();
        weTabletRuecksetzen.setWeLoginVerify(weLoginVerify);
        weTabletRuecksetzen.geraeteNummer = ParamS.clGlobalVar.arbeitsplatz;
        WETabletRuecksetzenRC weTabletRuecksetzenRC = wsClient.tabletRuecksetzenPruefen(weTabletRuecksetzen);
        if (weTabletRuecksetzenRC.ruecksetzen == true) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Abstimmungsdaten zurücksetzen!?", "Ja", "Nein");
            if (brc == true) {
                CaDateiVerwaltung caDateiVerwaltung = new CaDateiVerwaltung();
                for (int i = 1; i <= 2; i++) {
                    String pfad = "";
                    if (i == 1) {
                        pfad = ParamS.clGlobalVar.lwPfadSicherung1;
                    } else {
                        pfad = ParamS.clGlobalVar.lwPfadSicherung2;
                    }
                    if (!pfad.isEmpty()) {
                        String dateiname = pfad + "\\abstimmung" + Integer.toString(ParamS.clGlobalVar.arbeitsplatz)
                                + ".txt";
                        caDateiVerwaltung.deleteDatei(dateiname);
                    }

                }
                weTabletRuecksetzenRC = wsClient.tabletRuecksetzen(weTabletRuecksetzen);
            }
        }

        
        versucheSetzeStatus();

        tfScanFeld.focusedProperty().addListener((o, oV, nV) -> {
            // Trigger wenn nicht selektiert und scanFeld aktuell aktiv
            if(!nV && !tfScanFeld.isDisable()) {
                tfScanFeld.requestFocus();
            }
        });

        if (ParamS.paramGeraet.abstimmungTabletTestmodus) {
            for (int i = 1; i < 30 * 10; i++) {/*Takt 2 Sekunden; *30 = 1 Minute, 10 Minuten lang*/
                if (offlineBetrieb == false) {
                    doEinlesen();

                    long zeitStempel = CaDatumZeit.zeitStempelMS();
                    DbBundle lDbBundle = new DbBundle();
                    boolean brc = stimmeAbgebenEinzelnerAktionaer(lDbBundle, zeitStempel);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        if (offlineBetrieb) {
            statusAnzeige = 13;
            setzeAnzeige();
        }

        if (ParamS.paramGeraet.abtimmungTabletFullScreen) {
            eigeneStage.setMaximized(true);
        } else {
            eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (e.isAltDown() && e.isShiftDown() && e.getCode().equals(KeyCode.F)) {
                    eigeneStage.setFullScreen(!eigeneStage.isFullScreen());
                }
            });
        }
        Platform.runLater(() -> tfScanFeld.requestFocus());
        loading = LoadingScreen.createLoadingScreen(rootPane);
    }

    /**
     * Versuche setze status.
     */
    private void versucheSetzeStatus() {
        offlineBetrieb = false;

        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        WETabletSetzeStatus weTabletSetzeStatus = new WETabletSetzeStatus();
        weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        weTabletSetzeStatus.tabletNummer = ParamS.clGlobalVar.arbeitsplatz;
        weTabletSetzeStatus.setWeLoginVerify(weLoginVerify);
        WETabletSetzeStatusRC weTabletSetzeStatusRC = wsClient.tabletSetzeStatusAbstimmungAuf(weTabletSetzeStatus);
        if (weTabletSetzeStatusRC.rc == -99999) {
            offlineBetrieb = true;
        }

        if (tabletIstPersoenlichesTablet == false || offlineBetrieb == false) {
            tabletIstPersoenlichesTablet = weTabletSetzeStatusRC.tabletIstPersoenlichesTablet;
            tabletIstBereitsZugordnet = weTabletSetzeStatusRC.tabletIstBereitsZugordnet;
            nummerIdentifikation = weTabletSetzeStatusRC.nummerIdentifikation;
            anzeigeName = weTabletSetzeStatusRC.anzeigeName;
        }

        if (tabletIstPersoenlichesTablet == false) {
            statusAnzeige = 1;
        } else {
            if (offlineBetrieb) {
                statusAnzeige = 13;
            } else {
                if (tabletIstBereitsZugordnet) {
                    statusAnzeige = 12;
                } else {
                    statusAnzeige = 11;
                }
            }
        }
        setzeAnzeige();

    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        clearFehlermeldung();
        testModus = false;

        if (tabletIstPersoenlichesTablet) {
            statusAnzeige = 12;
            setzeAnzeige();
            return;
        }
        if (anzahlAktionaereGelesen == 1) {
            statusAnzeige = 1;
            setzeAnzeige();
            tfScanFeld.requestFocus();
        } else {
            setzeAnzeigeAktionaersauswahl();
        }
    }

    /**
     * On btn vor.
     *
     * @param event the event
     */
    @FXML
    void onBtnVor(ActionEvent event) {
        if (angezeigteSeite < CInjects.tabletBlaetternSeitenanzahl) {
            angezeigteSeite++;
        }
        statusAnzeige = 2;
        setzeAnzeige();
    }

    /**
     * On btn zurueck.
     *
     * @param event the event
     */
    @FXML
    void onBtnZurueck(ActionEvent event) {
        if (angezeigteSeite > 1) {
            angezeigteSeite--;
        }
        statusAnzeige = 2;
        setzeAnzeige();
    }

    /**
     * On btn stimme abgeben.
     *
     * @param event the event
     */
    @FXML
    void onBtnStimmeAbgeben(ActionEvent event) {
        new Thread(abstimmenTask()).start();
    }
    
    private Task<Void> abstimmenTask() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                abstimmen();
                return null;
            }
        };
        
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> {
            loading.setVisible(false);
            setzeAnzeige();
        });
        task.setOnFailed(e -> {
            loading.setVisible(false);
            setzeAnzeige();
            task.getException().printStackTrace();
        });
        
        return task;
    }
    
    void abstimmen() {

        if (testModus == true) {
            statusAnzeige = 1;
            testModus = false;
            Platform.runLater(() -> {
                tfScanFeld.requestFocus();
                clearFehlermeldung();
                });
            return;
        }

        long zeitStempel = CaDatumZeit.zeitStempelMS();
        DbBundle lDbBundle = new DbBundle();

        if (aktuelleStimmabgabeFuerAlleDurchfuehren == false) { // Stimmabgabe für einen einzelnen Aktionär
            boolean brc = stimmeAbgebenEinzelnerAktionaer(lDbBundle, zeitStempel);
            if (brc == false) {
                return;
            }
        }

        else {//Stimmabgabe für mehrere Aktionäre
            scanString = stimmblocknummer[offsetAktionaerAktuelleVerarbeitung];
            boolean brc = stimmeAbgebenEinzelnerAktionaer(lDbBundle, zeitStempel);
            if (brc == false) {
                return;
            }

            int hoffsetAktionaerAktuelleVerarbeitung = offsetAktionaerAktuelleVerarbeitung;

            /** Stimmen in spätere, unbearbeitete Einzelaktionäre übertragen */
            for (int i = hoffsetAktionaerAktuelleVerarbeitung + 1; i < anzahlAktionaereGelesen; i++) {
                if (verarbeitetKennzeichen[i] == 0) {
                    anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

                    for (int j = 0; j < anzAgenda; j++) {
                        weAbstimmungAktionaerLesenRC[i].abstimmart[j] = weAbstimmungAktionaerLesenRC[hoffsetAktionaerAktuelleVerarbeitung].abstimmart[j];
                    }

                    offsetAktionaerAktuelleVerarbeitung = i;
                    scanString = stimmblocknummer[offsetAktionaerAktuelleVerarbeitung];
                    boolean brc1 = stimmeAbgebenEinzelnerAktionaer(lDbBundle, zeitStempel);
                    if (brc1 == false) {
                        return;
                    }
                    verarbeitetKennzeichen[i] = 1;
                }
            }

            offsetAktionaerAktuelleVerarbeitung = hoffsetAktionaerAktuelleVerarbeitung;
        }
        statusAnzeige = 3;
    }

    /**
     * Stimme abgeben einzelner aktionaer.
     *
     * @param lDbBundle   the l db bundle
     * @param zeitStempel the zeit stempel
     * @return true, if successful
     */
    private boolean stimmeAbgebenEinzelnerAktionaer(DbBundle lDbBundle, long zeitStempel) {
        speichernAufKarte(lDbBundle, 1, zeitStempel);
        speichernAufKarte(lDbBundle, 2, zeitStempel);

        boolean rcb = stimmenUebertragen(zeitStempel, true);
        if (rcb == false) {
            /*Kann eigentlich nicht auftreten, außer es hat sich zwischen
             * Einlesen der Stimmkarte und dem Speichern am Status was geändert
             */

            clearFehlermeldung();
            if (tabletIstPersoenlichesTablet == false) {
                statusAnzeige = 1;
            } else {
                /*Statusanzeige wurde schon in stimmenUebertragen gesetzt*/
            }
            return false;
        }

        if (ParamS.paramGeraet.bonDruckerIstZugeordnet) {
            ClBonPrinter bonPrinter = new ClBonPrinter();
            bonPrinter.votes = "Nr " + tfScanFeld.getText() + "\n";
            for (int i = 0; i < anzAgenda; i++) {
                bonPrinter.votes = bonPrinter.votes + "TOP "
                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer
                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummerindex + " ";
                switch (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i]) {
                case 0:
                case 1: {
                    bonPrinter.votes = bonPrinter.votes + "Ja";
                    break;
                }
                case 2: {
                    bonPrinter.votes = bonPrinter.votes + "Nein";
                    break;
                }
                case 3: {
                    bonPrinter.votes = bonPrinter.votes + "Enthaltung";
                    break;
                }
                }

                bonPrinter.votes = bonPrinter.votes + "\n";
            }
            bonPrinter.votes = bonPrinter.votes + "Pruefzahl A: " + Long.toString(zeitStempel) + "\n";
            bonPrinter.drucken();
        }
        return true;
    }

    /**
     * Persoenliches tablet abstimmung starten.
     */
    private void persoenlichesTabletAbstimmungStarten() {
        boolean brc = CInjects.lesePraesenzAbstimmungsDaten(wsClient);
        if (CInjects.verbindungsabbruch == true) {
            statusAnzeige = 13;
            setzeAnzeige();
            return;
        }
        if (CInjects.praesenzAbstimmungsdatenEingelesen == false
                || CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblockIstElektronischAktiv == false) {
            statusAnzeige = 14;
            setzeAnzeige();
            return;

        }

        doEinlesen();
        if (offlineBetrieb) {
            statusAnzeige = 13;
            setzeAnzeige();
        }
        return;
    }

    /**
     * On btn durchgefuehrt.
     *
     * @param event the event
     */
    @FXML
    void onBtnDurchgefuehrt(ActionEvent event) {

        if (statusAnzeige == 11) {
            /*Persönliches Tablet - Noch nicht zugeordnet. Retry*/
            versucheSetzeStatus();
            return;
        }

        if (statusAnzeige == 13) {
            /*Persönliches Tablet - offline, neuer Versuch*/
            versucheSetzeStatus();
            return;
        }

        if (statusAnzeige == 12) {
            /*Persönliches Tablet - Abstimmung starten ...*/
            persoenlichesTabletAbstimmungStarten();
            return;
        }

        if (statusAnzeige == 14) {
            /*Persönliches Tablet - Abstimmung war noch nicht aktiv - erneut versuchen*/
            persoenlichesTabletAbstimmungStarten();
            return;
        }

        clearFehlermeldung();
        verarbeitetKennzeichen[offsetAktionaerAktuelleVerarbeitung] = 1;

        if (tabletIstPersoenlichesTablet) {
            statusAnzeige = 12;
            setzeAnzeige();
            return;
        }
        if (anzahlAktionaereGelesen == 1) {
            statusAnzeige = 1;
            setzeAnzeige();
            tfScanFeld.requestFocus();
        } else {
            setzeAnzeigeAktionaersauswahl();
        }
    }

    /**
     * Stimmen uebertragen.
     *
     * @param zeitStempel            the zeit stempel
     * @param pSpeichernAufJedenFall the speichern auf jeden fall
     * @return true, if successful
     */
    private boolean stimmenUebertragen(long zeitStempel, boolean pSpeichernAufJedenFall) {

        //    	boolean rcb=false;
        if ((weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].rc == -4
                || weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].rc == -5)
                && offlineBetrieb == false) {
            /*TODO #Konsolidieren Tablet Stimmabgabe nicht präsent
             * Derzeit nur zwei Varianten unterstützt: entweder von vornherein ablehnen
             * (dann kommt man gar nicht an diese Stelle)
             * oder
             * Einfach übertragen ohne weitere Meldungen
             */
            //    		rcb=doBtnEinlesen(weAbstimmungAktionaerLesen.stimmblockNummer);
            //    		if (offlineBetrieb==false){
            //    			if (rcb==false){
            //    				lblFehlerMeldung.setText("Karte ist nicht präsent, und kann nicht präsent gesetzt werden => Coach!");
            //    				return false;
            //    			}
            //    			switch (zugangsFunktion){
            //    			/**=4 => Bevollmächtigter aktionaerVollmachtenAktuelleAbfrage Erstzugang
            //    			 * =3 => Selbst Erstzugang
            //    			 * =8 => Widerzugang
            //    			 */
            //    			case 4:
            //    			case 3:{
            //    				/*Präsenzbuchung speichern - Erstzugang*/
            //    				lStimmkartennummer="1"+nummerErstScan;
            //    				buchen_erstzugang();
            //    				break;
            //    			}
            //    			case 8:{ /*WiederzugangOhneFrage => Wiederzugang durchführen*/
            //    				buchen_wiederzugang();
            //    				break;
            //    			}
            //
            //    			}
            //    		}
            //    		clearDatenPraesenz();

        }

        /*Nochmal checken, ob Abstimmung möglich*/
        if (tabletIstPersoenlichesTablet) {
            boolean brc = CInjects.lesePraesenzAbstimmungsDaten(wsClient);
            if (CInjects.verbindungsabbruch == true) {
                statusAnzeige = 13;
                return false;
            }
            if (CInjects.praesenzAbstimmungsdatenEingelesen == false
                    || CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblockIstElektronischAktiv == false) {
                statusAnzeige = 14;
                return false;

            }

        }

        WEAbstimmungSpeichern weAbstimmungSpeichern = new WEAbstimmungSpeichern();
        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        weAbstimmungSpeichern.setWeLoginVerify(weLoginVerify);
        weAbstimmungSpeichern.meldungsIdent = weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].meldungsIdent;
        weAbstimmungSpeichern.zeitstempelraw = zeitStempel;
        weAbstimmungSpeichern.speichernAufJedenFall = pSpeichernAufJedenFall;

        if (offlineBetrieb == false) {
            weAbstimmungSpeichern.abstimmart = weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart;
            WEAbstimmungSpeichernRC weAbstimmungSpeichernRC = wsClient.abstimmungSpeichern(weAbstimmungSpeichern);
            if (weAbstimmungSpeichernRC.rc == -99999) {
                offlineBetrieb = true;
                if (tabletIstPersoenlichesTablet) {
                    statusAnzeige = 13;
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * On key pressed scan feld.
     *
     * @param event the event
     */
    /*ENTER abfangen im ScanFeld*/
    @FXML
    void onKeyPressedScanFeld(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            scanString = tfScanFeld.getText();
            doEinlesen();
        }

    }

    /** The scan nummer. */
    private String scanNummer = "";

    /** The stimmblocknummer. */
    String stimmblocknummer[] = null;

    /**
     * 0 = noch keine Verarbeitung; 1=bereits verarbeitet / abgestimmt; -1=ungültig.
     */
    private int verarbeitetKennzeichen[] = null;

    /** The kartenklasse. */
    private int kartenklasse = 0;

    /** ****Kennzeichen für AppIdent verarbeitet**********. */
    /** True => es wurde eine AppIdent gelesen, ansonsten eine normale Stimmkarte */
    private boolean appIdentGelesen = false;

    /** Anzahl Aktionäre in Nummernform - gelesene Ids. */
    private int anzahlAktionaereGelesen = 0;

    /**
     * die Nummer innerhalb z.B. stimmblocknummer, für die gerade die Stimmabgabe
     * durchgeführt wird. Falls "alle gleich" ausgewählt wurde, steht dies auf der
     * Nummer mit der niedrigsten noch nicht verarbeiteten Offset, und gleichzeitig
     * aktuelleStimmabgabeFuerAlleDurchfuehren auf true.
     */
    private int offsetAktionaerAktuelleVerarbeitung = -1;

    /** true => für alle gleichartige Abstimmung eingeben. */
    private boolean aktuelleStimmabgabeFuerAlleDurchfuehren = false;

    /** The test modus. */
    private boolean testModus = false;

    /** The auto test modus. */
    private boolean autoTestModus = false;

    /**
     * Auto testen ablauf.
     */
    private void autoTestenAblauf() {
        while (autoTestModus) {
            WEAutoTest weAutoTest = new WEAutoTest();
            WELoginVerify weLoginVerify = new WELoginVerify();
            weAutoTest.setWeLoginVerify(weLoginVerify);
            weAutoTest.geraeteNummer = ParamS.clGlobalVar.arbeitsplatz;
            WEAutoTestRC weAutoTestRC = wsClient.autoTest(weAutoTest);
            if (weAutoTestRC.rc == -99999 || weAutoTestRC.scanString.isEmpty()) {
                autoTestModus = false;
                return;
            }
            tfScanFeld.setText(weAutoTestRC.scanString);

            doEinlesen();

            System.out.println("anzahlAktionaereGelesen=" + anzahlAktionaereGelesen);
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                offsetAktionaerAktuelleVerarbeitung = i;
                aktuelleStimmabgabeFuerAlleDurchfuehren = false;
                statusAnzeige = 2;
                angezeigteSeite = 1;
                setzeAnzeige();
                ActionEvent hEvent = null;
                onBtnStimmeAbgeben(hEvent);
                onBtnDurchgefuehrt(hEvent);
            }
        }

    }

    /**
     * Verarbeiten der eingelesenen (Stimmkarten)Nummer. Die Verarbeitung muß so
     * erfolgen, dass sie auch Offline möglich ist. Deshalb wird die Nummernform
     * lokal dekodiert.
     */
    private void doEinlesen() {
        CaBug.druckeLog("A", logDrucken, 10);

        scanNummer = tfScanFeld.getText();

        testModus = false;
        if (scanNummer.length() > 7) {
            /*Checken, ob Steuerungscode*/
            if (scanNummer.substring(0, 7).equals("STEUERG")) {
                String hGattung = scanNummer.substring(7, 8);
                System.out.println("hGattung=" + hGattung);
                if (!CaString.isNummern(hGattung)) {
                    return;
                }
                //    			int gattung=Integer.parseInt(hGattung);
                testModus = true;
                appIdentGelesen = false;
                anzahlAktionaereGelesen = 1;

                BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
                int rc = lBlInitHVWebServices.holeHVParameter();

                if (rc < 1) {
                    offlineBetrieb = true;
                } else {
                    offlineBetrieb = false;
                }
                if (offlineBetrieb) {
                    lblFehlerMeldung.setText("Offline-Betrieb");
                } else {
                    lblFehlerMeldung.setText("Verbindung erfolgreich - TO refresh durchgeführt");
                }

                weAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC[1];
                weAbstimmungAktionaerLesenRC[0] = new WEAbstimmungAktionaerLesenRC();
                weAbstimmungAktionaerLesenRC[0].abstimmart = new int[anzAgenda];
                aktuelleStimmabgabeFuerAlleDurchfuehren = false;
                offsetAktionaerAktuelleVerarbeitung = 0;
                for (int i1 = 0; i1 < anzAgenda; i1++) {
                    weAbstimmungAktionaerLesenRC[0].abstimmart[i1] = 0;
                }
                statusAnzeige = 2;
                angezeigteSeite = 1;
                setzeAnzeige();
                return;
            }

            if (scanNummer.substring(0, 7).equals("MANDANT")) {
                /** MANDANTMMMJJJJND */
                ParamS.clGlobalVar.mandant = Integer.parseInt(scanNummer.substring(7, 10));
                ParamS.clGlobalVar.hvJahr = Integer.parseInt(scanNummer.substring(10, 14));
                ParamS.clGlobalVar.hvNummer = scanNummer.substring(14, 15);
                ParamS.clGlobalVar.datenbereich = scanNummer.substring(15, 16);

                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Mandant=" + ParamS.clGlobalVar.mandant);

                CaBug.druckeLog("A Mandant=" + ParamS.clGlobalVar.mandant + " hvJahr=" + ParamS.clGlobalVar.hvJahr +
                        " hvNummer=" + ParamS.clGlobalVar.hvNummer + " datenbereich=" + ParamS.clGlobalVar.datenbereich,
                        logDrucken, 3);

                CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
                int rc = caLeseParameterNeu.leseHVParameter();
                if (rc < 1) { // Fehlerbehandlung
                    caZeigeHinweis = new CaZeigeHinweis();
                    caZeigeHinweis.zeige(eigeneStage, "Systemfehler " + "Fehler " + CaFehler.getFehlertext(rc, 0));
                }

                //              int gattung=Integer.parseInt(hGattung);
                testModus = true;
                appIdentGelesen = false;
                anzahlAktionaereGelesen = 1;

                BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
                rc = lBlInitHVWebServices.holeHVParameter();

                if (rc < 1) {
                    offlineBetrieb = true;
                } else {
                    offlineBetrieb = false;
                }
                if (offlineBetrieb) {
                    lblFehlerMeldung.setText("Offline-Betrieb");
                    CaBug.druckeLog("Offline-Betrieb", logDrucken, 10);
                } else {
                    lblFehlerMeldung.setText("Verbindung erfolgreich - TO refresh durchgeführt");
                    CaBug.druckeLog("Refresh durchgeführt", logDrucken, 10);
                }

                CaBug.druckeLog("B Mandant=" + ParamS.clGlobalVar.mandant + " hvJahr=" + ParamS.clGlobalVar.hvJahr +
                        " hvNummer=" + ParamS.clGlobalVar.hvNummer + " datenbereich=" + ParamS.clGlobalVar.datenbereich,
                        logDrucken, 3);

                weAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC[1];
                weAbstimmungAktionaerLesenRC[0] = new WEAbstimmungAktionaerLesenRC();
                weAbstimmungAktionaerLesenRC[0].abstimmart = new int[anzAgenda];
                aktuelleStimmabgabeFuerAlleDurchfuehren = false;
                offsetAktionaerAktuelleVerarbeitung = 0;
                for (int i1 = 0; i1 < anzAgenda; i1++) {
                    weAbstimmungAktionaerLesenRC[0].abstimmart[i1] = 0;
                }
                statusAnzeige = 2;
                angezeigteSeite = 1;
                setzeAnzeige();
                CaBug.druckeLog("C Mandant=" + ParamS.clGlobalVar.mandant + " hvJahr=" + ParamS.clGlobalVar.hvJahr +
                        " hvNummer=" + ParamS.clGlobalVar.hvNummer + " datenbereich=" + ParamS.clGlobalVar.datenbereich,
                        logDrucken, 3);
                return;

            }

            if (scanNummer.equals("AUTOTEST")) {
                autoTestModus = true;
                autoTestenAblauf();
                return;
            }
        }

        DbBundle lDbBundle = new DbBundle();
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int rc = blNummernformen.dekodiere(scanNummer, KonstKartenklasse.unbekannt);
        System.out.println("doEinlesen Fehler rc=" + rc);
        if (rc < 0) {
            String fehlertext = "";
            switch (rc) {
            case CaFehler.pmNummernformUngueltig:
            case CaFehler.pmNummernformAktionsnummerUngueltig:
                fehlertext = "Ungültige Nummer!";
                break;
            case CaFehler.pmNummernformMandantUngueltig:
                fehlertext = "Stimmkarte von falscher Hauptversammlung!";
                break;
            case CaFehler.pfXyNichtImZulaessigenNummernkreis:
                fehlertext = "Stimmkarte nicht im zulässigen Nummernkreis!";
                break;
            }
            zeigeFehlermeldungUndEnde(fehlertext);
            return;
        }

        CaBug.druckeLog("B", logDrucken, 10);

        /*globale Variablen für AppIdent-Verarbeitung belegen*/
        if (blNummernformen.rcIstAppIdent) {
            appIdentGelesen = true;
        } else {
            appIdentGelesen = false;
        }
        anzahlAktionaereGelesen = blNummernformen.rcIdentifikationsnummer.size();

        kartenklasse = blNummernformen.rcKartenklasse;
        if (kartenklasse != KonstKartenklasse.stimmkartennummer && !blNummernformen.rcIstAppIdent
                && ParamS.param.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig == 0) {
            zeigeFehlermeldungUndEnde("Kein Abstimmungsbarcode!");
            return;
        }

        int stimmkartennummer = blNummernformen.rcStimmkartennummer;
        if (ParamS.paramGeraet.nurGueltigeStimmkartenNummerBeiTabletAbstimmung && !blNummernformen.rcIstAppIdent) {
            System.out.println("AAAAA");
            int gef = -1;
            for (int i = 0; i < CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length; i++) {
                if (stimmkartennummer == CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock[i].nummerDerStimmkarte) {
                    gef = 1;
                }
            }
            if (gef == -1) {
                zeigeFehlermeldungUndEnde("Falsche Stimmkartennummer!");
                return;
            }
        }

        CaBug.druckeLog("C", logDrucken, 10);

        stimmblocknummer = new String[anzahlAktionaereGelesen];
        verarbeitetKennzeichen = new int[anzahlAktionaereGelesen];
        weAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC[anzahlAktionaereGelesen];
        offsetAktionaerAktuelleVerarbeitung = -1;
        aktuelleStimmabgabeFuerAlleDurchfuehren = false;

        boolean abstimmungenNeuLesen=false;

        for (int i = 0; i < anzahlAktionaereGelesen; i++) {

            stimmblocknummer[i] = blNummernformen.rcIdentifikationsnummer.get(i);
            verarbeitetKennzeichen[i] = 0;

            weAbstimmungAktionaerLesen = new WEAbstimmungAktionaerLesen();
            WELoginVerify weLoginVerify = new WELoginVerify();
            ;
            weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
            weAbstimmungAktionaerLesen.setWeLoginVerify(weLoginVerify);

            if (offlineBetrieb == false) {
                weAbstimmungAktionaerLesen.stimmblockNummer = stimmblocknummer[i];
                weAbstimmungAktionaerLesen.kartenklasse = kartenklasse;
                if (kartenklasse == KonstKartenklasse.eintrittskartennummer) {
                    weAbstimmungAktionaerLesen.neben = blNummernformen.rcIdentifikationsnummerNeben.get(i);
                }
                System.out.println("Stimmblocknummer[i]" + stimmblocknummer[i]);
                weAbstimmungAktionaerLesenRC[i] = wsClient.abstimmungAktionaerLesen(weAbstimmungAktionaerLesen);
                if (weAbstimmungAktionaerLesenRC[i].rc == -99999) {
                    offlineBetrieb = true;
                    if (tabletIstPersoenlichesTablet) {
                        return;
                    }
                    /** Eingabe muß wiederholt werden - deshalb erst mal leeren */
                    tfScanFeld.setText("");
                    setzeAnzeige(); //Damit Button blau wird
                    return;
                }
                if (weAbstimmungAktionaerLesenRC[i].abstimmungsVersion!=CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungsVersion) {
                    CaBug.druckeLog("Alte Abstimmungsversion="+CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungsVersion+" neue Abstimmungsversion="+weAbstimmungAktionaerLesenRC[i].abstimmungsVersion, logDrucken, 3);
                    abstimmungenNeuLesen=true;
                }
           }
            if (offlineBetrieb == true) {
                weAbstimmungAktionaerLesenRC[i] = new WEAbstimmungAktionaerLesenRC();
                /*Falls in Nummernform eine allgemeine Information enthalten war, dann diese anzeigen*/
                String hString = "";
                if (blNummernformen.rcSonstigeInformation != null &&
                        blNummernformen.rcSonstigeInformation.size() > i) {
                    blNummernformen.rcSonstigeInformation.get(i);
                }
                weAbstimmungAktionaerLesenRC[i].name = hString;
                
            }

            /*-1=gesperrt
             *-2=nicht vorhanden
             *-3=in Sammelkarte
             *-4=nicht präsent
             *-5=noch nie präsent gewesen
             *-6=Sammelkarte mit Weisung - könnte theoretisch weiterverarbeitet werden (quasi nur eine Warnung)
             */
            if (weAbstimmungAktionaerLesenRC[i].rc == -1 || weAbstimmungAktionaerLesenRC[i].rc == -2) {
                if (appIdentGelesen == false || anzahlAktionaereGelesen == 1) {
                    if (weAbstimmungAktionaerLesenRC[i].rc == -1) {
                        zeigeFehlermeldungUndEnde("Stimmkartennummer gesperrt!");
                        return;
                    }
                    if (weAbstimmungAktionaerLesenRC[i].rc == -2) {
                        zeigeFehlermeldungUndEnde("Stimmkartennummer nicht vorhanden!");
                        return;
                    }
                }
                /*Hier: mehrere Aktionäre gelesen => keine globale Fehlermeldung, sondern nur als
                 * "gesperrt" anzeigen
                 */
                verarbeitetKennzeichen[i] = -1;

                /*Noch zu überprüfen: wie werden bei ku178 die Sachen gesperrt?
                 * Was wird dann geliefert?*/
                if (weAbstimmungAktionaerLesenRC[i].name.isEmpty()) {
                    weAbstimmungAktionaerLesenRC[i].name = blNummernformen.rcSonstigeInformation.get(i);
                }

            }

            if (weAbstimmungAktionaerLesenRC[i].rc == -3) {
                zeigeFehlermeldungUndEnde("Stimmkartennummer in Sammelkarte!");
                return;
            }
            if (weAbstimmungAktionaerLesenRC[i].rc == -4 || weAbstimmungAktionaerLesenRC[i].rc == -5) {
                /*Nicht präsent / nicht präsent gewesen - Verarbeitung je nach Parameter*/
                if (ParamS.param.paramAbstimmungParameter.abstimmenTabletNichtPraesenteWerdenPraesentGesetzt == 0) {
                    zeigeFehlermeldungUndEnde(
                            "Abstimmung kann auf diesem Gerät nicht erfaßt werden! Coach verständigen!");
                    return;
                }
            }

            if (weAbstimmungAktionaerLesenRC[i].rc == -6) {
                zeigeFehlermeldungUndEnde("Sammelkarte mit Weisung!");
                return;
            }
        }
        if (abstimmungenNeuLesen) {
            CaBug.druckeLog("Abstimmungen müssen neu gelesen werden", logDrucken, 3);
            BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
            rc=lBlInitHVWebServices.holeAbstimmungen(wsClient);
            
        }

        CaBug.druckeLog("D", logDrucken, 10);

        clearFehlermeldung();
        String angezeigteNamen = "";
        String hStimmblocknummer = "";
        for (int i = 0; i < anzahlAktionaereGelesen; i++) {
            if (offlineBetrieb == false) {
                angezeigteNamen += weAbstimmungAktionaerLesenRC[i].name + " " + weAbstimmungAktionaerLesenRC[i].vorname
                        + " " + weAbstimmungAktionaerLesenRC[i].ort + "; ";
            }

            else {
                angezeigteNamen += weAbstimmungAktionaerLesenRC[i].name + "; ";
                //    			weAbstimmungAktionaerLesenRC[i]=new WEAbstimmungAktionaerLesenRC();
                weAbstimmungAktionaerLesenRC[i].abstimmart = new int[anzAgenda];
                for (int i1 = 0; i1 < anzAgenda; i1++) {
                    weAbstimmungAktionaerLesenRC[i].abstimmart[i1] = 0;
                }
            }
            //    		Für Seminar deaktiviert
            //    		if (blNummernformen.rcIstAppIdent && !ParamSpezial.ku178(ParamS.clGlobalVar.mandant)){
            //    			int positionInAbstimmungsString=0;
            //    			/*Für alle Stimmkarten im elektronischen (übertragenen) Stimmblock*/
            //    			for (int i1=0;i1<CInjects.weStimmkartenblockGetRC.abstimmungenListeM.getStimmkartenBlockM().length;i1++){
            //    				/*Für alle Abstimmungen in der jeweiligen Stimmkarten*/
            //    				for (int i2=0;i2<CInjects.weStimmkartenblockGetRC.abstimmungenListeM.getStimmkartenBlockM()[i1].getAbstimmungenListeM().size();i2++){
            //    					EclAbstimmungM lAbstimmungM=CInjects.weStimmkartenblockGetRC.abstimmungenListeM.getStimmkartenBlockM()[i1].getAbstimmungenListeM().get(i2);
            //    					/*Nun suchen, ob bzw. an welcher Position in der Tablet-Abstimmungliste die Abstimmung mit lAbstimmungM.ident steht. Dann
            //    					 * Zeichen dorthin ggf. übertragen*/
            //   				        for (int i3=0;i3<anzAgenda;i3++){
            //   				        	if (lAbstimmungM.getIdent()==CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i3].ident){
            //   				        		/*Übertragen*/
            //   				        		String stimmartUebertragen=blNummernformen.rcAbstimmung.get(i).substring(positionInAbstimmungsString, positionInAbstimmungsString+1);
            //   				        		int iStimmartUebertragen=0;
            //   				        		switch (stimmartUebertragen){
            //   				        		case "J":iStimmartUebertragen=KonstStimmart.ja;break;
            //				        		case "N":iStimmartUebertragen=KonstStimmart.nein;break;
            //   				        		case "E":iStimmartUebertragen=KonstStimmart.enthaltung;break;
            //  				        		}
            //   				        		weAbstimmungAktionaerLesenRC[i].abstimmart[i3]=iStimmartUebertragen;
            //   				        	}
            //   				        }
            //   				     positionInAbstimmungsString++;
            //
            //    				}
            //    				positionInAbstimmungsString++; /*; nach jeder Stimmkarte*/
            //    				
            //    			}
            //    		}
            hStimmblocknummer += stimmblocknummer[i];
        }

        CaBug.druckeLog("E", logDrucken, 10);

        if (anzahlAktionaereGelesen == 1) {
            offsetAktionaerAktuelleVerarbeitung = 0;
            aktuelleStimmabgabeFuerAlleDurchfuehren = false;
            /********* Nur 1 Nummer gelesen - diese normal verarbeiten **************/
            if (offlineBetrieb == false) {
                lblFehlerMeldung.setText(angezeigteNamen);
            }

            tfScanFeld.setText(hStimmblocknummer);

            statusAnzeige = 2;
            angezeigteSeite = 1;
            setzeAnzeige();
        }

        else {
            /************ Mehrere Nummern gelesen - Vorauswahl anzeigen ***********/
            setzeAnzeigeAktionaersauswahl();
        }

        CaBug.druckeLog("F", logDrucken, 10);

        if (offlineBetrieb == true && tabletIstPersoenlichesTablet) {
            statusAnzeige = 13;
            setzeAnzeige();
        }

        CaBug.druckeLog("G", logDrucken, 10);

    }

    /**
     * Clear fehlermeldung.
     */
    private void clearFehlermeldung() {
        lblFehlerMeldung.setText("");
        return;

    }

    /**
     * Zeige fehlermeldung und ende.
     *
     * @param fehlerMeldung the fehler meldung
     */
    private void zeigeFehlermeldungUndEnde(String fehlerMeldung) {
        lblFehlerMeldung.setText(fehlerMeldung);
        tfScanFeld.setText("");
        tfScanFeld.requestFocus();
        return;

    }

    /**
     * Clicked ja.
     *
     * @param event the event
     */
    @FXML
    void clickedJa(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }
        int gef = -1;
        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            if (event.getSource() == btnAgendaJa[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(gef);
            if (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] == KonstStimmart.ja) {
                btnAgendaJa[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = 0;
            } else {
                int gruppe = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].zuAbstimmungsgruppe;
                if (gruppe != 0) {
                    int anzMaxGruppe = ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[gruppe];
                    int anzJaGruppe = 0;
                    for (int i = 0; i < anzAgenda; i++) {
                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].zuAbstimmungsgruppe == gruppe
                                && weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i] == KonstStimmart.ja) {
                            anzJaGruppe++;
                        }
                    }
                    if (anzJaGruppe >= anzMaxGruppe) {
                        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                        caZeigeHinweis.zeige(eigeneStage,
                                "Die maximale Anzahl Stimmen für diesen TOP ist bereits erreicht!");
                        return;
                    }
                }

                btnAgendaJa[gef].setStyle("-fx-background-color: #"
                        + ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa + "; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.ja;
                btnAgendaNein[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                btnAgendaEnthaltung[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);

            }
        }
    }

    /**
     * Clicked nein.
     *
     * @param event the event
     */
    @FXML
    void clickedNein(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }

        int gef = -1;
        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            if (event.getSource() == btnAgendaNein[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(gef);
            if (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] == KonstStimmart.nein) {
                btnAgendaNein[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = 0;
            } else {
                btnAgendaNein[gef]
                        .setStyle("-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein
                                + "; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.nein;
                btnAgendaJa[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                btnAgendaEnthaltung[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);

            }
        }

    }

    /**
     * Clicked enthaltung.
     *
     * @param event the event
     */
    @FXML
    void clickedEnthaltung(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }

        int gef = -1;
        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            if (event.getSource() == btnAgendaEnthaltung[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(gef);
            if (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] == KonstStimmart.enthaltung) {
                btnAgendaEnthaltung[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = 0;
            } else {
                btnAgendaEnthaltung[gef].setStyle(
                        "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung
                                + "; " + schriftartAbstimmungsButtons);
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.enthaltung;
                btnAgendaJa[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
                btnAgendaNein[gef].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);

            }
        }

    }

    /**
     * Clicked gesamt ja.
     *
     * @param event the event
     */
    @FXML
    void clickedGesamtJa(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }

        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);
            btnAgendaJa[i].setStyle("-fx-background-color: #00ff00; " + schriftartAbstimmungsButtons);
            weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.ja;
            btnAgendaEnthaltung[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
            btnAgendaNein[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
        }

        if (ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle == 1) {
            for (int i = 0; i < anzAgenda; i++) {
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i] = KonstStimmart.ja;
            }
        }
    }

    /**
     * Clicked gesamt nein.
     *
     * @param event the event
     */
    @FXML
    void clickedGesamtNein(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }
        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);
            btnAgendaNein[i].setStyle("-fx-background-color: #ff0000; " + schriftartAbstimmungsButtons);
            weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.nein;
            btnAgendaEnthaltung[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
            btnAgendaJa[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
        }
        if (ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle == 1) {
            for (int i = 0; i < anzAgenda; i++) {
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i] = KonstStimmart.nein;
            }
        }
    }

    /**
     * Clicked gesamt enthaltung.
     *
     * @param event the event
     */
    @FXML
    void clickedGesamtEnthaltung(ActionEvent event) {
        if (statusAnzeige != 2) {
            return;
        }
        for (int i = 0; i < anzAbstimmungenSeite; i++) {
            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);
            btnAgendaEnthaltung[i].setStyle("-fx-background-color: #fcf800; " + schriftartAbstimmungsButtons);
            weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock] = KonstStimmart.enthaltung;
            btnAgendaNein[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
            btnAgendaJa[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
        }
        if (ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle == 1) {
            for (int i = 0; i < anzAgenda; i++) {
                weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i] = KonstStimmart.enthaltung;
            }
        }
    }

    /** The button breite. */
    private int buttonBreite = 0;

    /** The maximale breite top text. */
    private int maximaleBreiteTopText = 0;

    /** The schriftart abstimmungs buttons. */
    private String schriftartAbstimmungsButtons = "";

    /** The schriftart gesamt buttons. */
    private String schriftartGesamtButtons = "";

    /** The schriftart text. */
    private String schriftartText = "";

    /**
     * Setze anzeige.
     */
    private void setzeAnzeige() {

        /*Scanfeld anzeige, dan möglicherweise von setzeAnzeigeAktionaersauswahl
         * verborgen
         */
        tfScanFeld.setVisible(true);

        int anzahlButtons = 0;
        int buttonBreiteDif = 0;
        if (statusAnzeige == 2 || statusAnzeige == 3) {
            anzahlButtons = CInjects.spaltenFuerStimmabgabeJeSeite[angezeigteSeite];
            if (statusAnzeige == 3) {
                anzahlButtons = 1;
            }
            if (ParamS.paramGeraet.abstimmungTabletHochformat) {
                /*Gesamtbreite=719*/
                buttonBreite = 120;
                buttonBreiteDif = 120 + 2 * 20;
                //				maximaleBreiteTopText=280+2*buttonBreiteDif-(anzahlButtons*buttonBreiteDif);
                maximaleBreiteTopText = 719 - 2 * 20 - (anzahlButtons * buttonBreiteDif);
                schriftartAbstimmungsButtons = "-fx-font: 16px 'System'";

                schriftartGesamtButtons = "-fx-font: 16px 'System'";
                //			schriftartGesamtButtons="-fx-font: 12px 'System'";

                //			schriftartText="-fx-font: 16px 'System'";
                schriftartText = "-fx-font: 16px 'System'; -fx-font-weight: bold";
            } else {
                /*Gesamtbreite=1085*/
                buttonBreite = 200;
                buttonBreiteDif = 200 + 2 * 20;
                //				maximaleBreiteTopText=560+2*buttonBreiteDif-(anzahlButtons*buttonBreiteDif);
                maximaleBreiteTopText = 1085 - 2 * 20 - (anzahlButtons * buttonBreiteDif);
                schriftartAbstimmungsButtons = "-fx-font: 24px 'System'";
                schriftartGesamtButtons = "-fx-font: 18px 'System'";

                //			schriftartText="-fx-font: 24px 'System'";
                schriftartText = "-fx-font: 22px 'System'; -fx-font-weight: bold";
            }
        }

        lblGrosseMeldung.setDisable(true);
        lblGrosseMeldung.setVisible(false);

        if (tabletIstPersoenlichesTablet == false) {
            btnDurchgefuehrt.setText("Ihre Stimmabgabe wurde erfolgreich gespeichert!");
            tfScanFeld.setVisible(true);
            pnScrollPane.setVisible(true);
        }

        if (statusAnzeige == 1) { /*Bereit zum Scannen*/

            tfScanFeld.setEditable(true);
            tfScanFeld.setDisable(false);
            tfScanFeld.setText("");
            btnAbstimmungsModus.setVisible(true);
            if (offlineBetrieb) {
                btnAbstimmungsModus.setStyle("-fx-background-color: #0080ff");
            }
            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);
            btnDurchgefuehrt.setVisible(false);
            btnDurchgefuehrt.setDisable(true);
            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            /** Abstimmungs-Bereich löschen */
            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);

            return;
        }

        if (statusAnzeige == 2) { /*Stimme markieren*/

            int gattungen[] = { 0, 0, 0, 0, 0, 0 };
            /*AAAAA nur temporär: wenn keine Gattung, dann Gattung 1 auf true setzen
             * Ist bei Offline-Modus der Fall - hier muß aktuell Gattung in der
             * Nummernform kodiert sein. Das muß noch erweitert werden, dass - wenn
             * nicht kodiert - die Gattung abhängig vom Nummernkreis festgelegt wird.*/
            CaBug.druckeLog("Gattung ermitteln", logDrucken, 10);
            boolean gattungGefunden = false;
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                gattungen[weAbstimmungAktionaerLesenRC[i].gattungId] = 1;
                if (weAbstimmungAktionaerLesenRC[i].gattungId != 0) {
                    CaBug.druckeLog("GattungId=" + weAbstimmungAktionaerLesenRC[i].gattungId, logDrucken, 10);
                    gattungGefunden = true;
                }
            }
            if (gattungGefunden == false) {
                gattungen[1] = 1;
            }

            tfScanFeld.setEditable(false);
            tfScanFeld.setDisable(true);
            btnAbstimmungsModus.setVisible(false);
            if (offlineBetrieb) {
                btnAbstimmungsModus.setStyle("-fx-background-color: #0080ff");
            }
            btnStimmeAbgeben.setVisible(true);
            btnStimmeAbgeben.setStyle("-fx-background-color: #A7A7A7");
            btnAbbrechen.setVisible(true);

            btnDurchgefuehrt.setVisible(false);
            btnDurchgefuehrt.setDisable(true);

            if (angezeigteSeite == 1) {
                btnZurueck.setVisible(false);
            } else {
                btnZurueck.setVisible(true);
            }

            if (angezeigteSeite < CInjects.tabletBlaetternSeitenanzahl) {
                btnVor.setVisible(true);
            } else {
                btnVor.setVisible(false);
            }

            /************* Nun Agenda anzeigen ***************/

            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;
            anzAbstimmungenSeite = CInjects.tabletBlaetternVerweis[angezeigteSeite].size();
            int offsetFuerButtons = CInjects.spaltenFuerStimmabgabeJeSeite[angezeigteSeite];

            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            lblAgendaTOP = new Label[anzAbstimmungenSeite];
            lblAgendaText = new Label[anzAbstimmungenSeite];
            btnAgendaJa = new Button[anzAbstimmungenSeite];
            btnAgendaNein = new Button[anzAbstimmungenSeite];
            btnAgendaEnthaltung = new Button[anzAbstimmungenSeite];

            btnGesamtJa = new Button("Alles Ja");
            btnGesamtJa.setOnAction(e -> {
                clickedGesamtJa(e);
            });
            btnGesamtJa.setStyle(
                    "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungJa
                            + "; " + schriftartGesamtButtons);
            btnGesamtJa.setWrapText(true);
            btnGesamtJa.setPrefWidth(buttonBreite);
            btnGesamtJa.setPrefHeight(85);

            btnGesamtNein = new Button("Alles Nein");
            btnGesamtNein.setOnAction(e -> {
                clickedGesamtNein(e);
            });
            btnGesamtNein.setStyle(
                    "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungNein
                            + "; " + schriftartGesamtButtons);
            btnGesamtNein.setWrapText(true);
            btnGesamtNein.setPrefWidth(buttonBreite);
            btnGesamtNein.setPrefHeight(85);

            btnGesamtEnthaltung = new Button("Alles Enthaltung");
            btnGesamtEnthaltung.setOnAction(e -> {
                clickedGesamtEnthaltung(e);
            });
            btnGesamtEnthaltung.setStyle("-fx-background-color: #"
                    + ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungEnthaltung + "; "
                    + schriftartGesamtButtons);
            btnGesamtEnthaltung.setWrapText(true);
            btnGesamtEnthaltung.setTextAlignment(TextAlignment.CENTER);
            btnGesamtEnthaltung.setPrefWidth(buttonBreite);
            btnGesamtEnthaltung.setPrefHeight(85);

            int zeilenOffset = 0;

            if (ParamS.param.paramAbstimmungParameter.beiTabletAllesJa == 1
                    || ParamS.param.paramAbstimmungParameter.beiTabletAllesNein == 1
                    || ParamS.param.paramAbstimmungParameter.beiTabletAllesEnthaltung == 1) {
                zeilenOffset = 1;
                int offsetGesamt = 0;
                if (ParamS.param.paramAbstimmungParameter.beiTabletAllesJa == 1) {
                    paneGridPane.add(btnGesamtJa, offsetGesamt, 0);
                    offsetGesamt++;
                }

                if (ParamS.param.paramAbstimmungParameter.beiTabletAllesNein == 1) {
                    paneGridPane.add(btnGesamtNein, offsetGesamt, 0);
                    offsetGesamt++;
                }

                if (ParamS.param.paramAbstimmungParameter.beiTabletAllesEnthaltung == 1) {
                    paneGridPane.add(btnGesamtEnthaltung, offsetGesamt, 0);
                    offsetGesamt++;
                }
                if (offsetGesamt > anzahlButtons) {
                    maximaleBreiteTopText = maximaleBreiteTopText - (offsetGesamt - anzahlButtons) * buttonBreiteDif;
                }

                /*Ggf. Text anzeigen*/
                if (ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen == 1) {
                    String vonTOP = "";
                    String bisTOP = "";
                    if (ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle == 0) {
                        /*Für Seite*/
                        int gef = -1;
                        for (int i = 0; i < anzAbstimmungenSeite; i++) {
                            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);
                            String hTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummer;
                            if (!hTOP.isEmpty() && gef == -1) {
                                vonTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummer
                                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummerindex;
                                gef = i;
                            }
                        }
                        gef = -1;
                        for (int i = anzAbstimmungenSeite - 1; i >= 0; i--) {
                            int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);
                            String hTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummer;
                            if (!hTOP.isEmpty() && gef == -1) {
                                bisTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummer
                                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[anzAbstimmungenSeite
                                                - 1].nummerindex;
                                gef = i;
                            }
                        }
                    } else {
                        /*Für alle*/
                        int gef = -1;
                        for (int i = 0; i < anzAgenda; i++) {
                            String hTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer;
                            if (!hTOP.isEmpty() && gef == -1) {
                                vonTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer
                                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummerindex;
                                gef = i;
                            }
                        }
                        gef = -1;
                        for (int i = anzAgenda - 1; i >= 0; i--) {
                            String hTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer;
                            if (!hTOP.isEmpty() && gef == -1) {
                                bisTOP = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer
                                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[anzAbstimmungenSeite
                                                - 1].nummerindex;
                                gef = i;
                            }
                        }
                    }
                    String anzeigeText = "Einheitliche Stimmabgabe zu TOP " + vonTOP + " bis " + bisTOP;
                    Label lblAnzeigeText = new Label();
                    lblAnzeigeText.setText(anzeigeText);
                    lblAnzeigeText.setWrapText(true);
                    lblAnzeigeText.setMaxWidth(maximaleBreiteTopText);
                    lblAnzeigeText.setStyle(schriftartText);
                    paneGridPane.add(lblAnzeigeText, offsetFuerButtons + 1, 0);

                }
            }

            int zeileAngezeigteAbstimmungen = 0;

            for (int i = 0; i < anzAbstimmungenSeite; i++) {

                int offsetInBlock = (int) CInjects.tabletBlaetternVerweis[angezeigteSeite].get(i);

                boolean diesenTopAbstimmen = false;
                for (int i1 = 1; i1 <= 5; i1++) {
                    if (gattungen[i1] == 1) {
                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].stimmberechtigteGattungen[i1
                                - 1] == 1) {
                            diesenTopAbstimmen = true;
                        }
                    }
                }

                boolean buttonsVeraendern = false;
                String topVeraendern = "";
                String jaVeraendern = "";
                String neinVeraendern = "";

                String hTOPText = "";
                if (ParamS.param.paramAbstimmungParameter.beiTabletTextKurzOderLang == 2) {
                    hTOPText = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].anzeigeBezeichnungLang;
                } else {
                    hTOPText = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].anzeigeBezeichnungKurz;
                }
                if (hTOPText.length() > 1) {
                    if (hTOPText.substring(0, 1).equals("#")) {
                        String[] beschriftungen = hTOPText.split(";"); /* #;Toptext;Ja;Nein*/
                        if (beschriftungen.length == 4) {
                            buttonsVeraendern = true;
                            topVeraendern = beschriftungen[1];
                            jaVeraendern = beschriftungen[2];
                            neinVeraendern = beschriftungen[3];
                        }
                    }
                }
                //              
                lblAgendaTOP[i] = new Label();
                lblAgendaTOP[i]
                        .setText(CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummer
                                + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].nummerindex);
                lblAgendaTOP[i].setStyle(schriftartText);

                lblAgendaText[i] = new Label();
                if (buttonsVeraendern == false) {
                    if (ParamS.param.paramAbstimmungParameter.beiTabletTextKurzOderLang == 2) {
                        lblAgendaText[i].setText(
                                CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].anzeigeBezeichnungLang);
                    } else {
                        lblAgendaText[i].setText(
                                CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].anzeigeBezeichnungKurz);
                    }
                } else {
                    lblAgendaText[i].setText(topVeraendern);
                }
                lblAgendaText[i].setWrapText(true);
                lblAgendaText[i].setMaxWidth(maximaleBreiteTopText);
                lblAgendaText[i].setStyle(schriftartText);

                if (diesenTopAbstimmen) {
                    paneGridPane.add(lblAgendaTOP[i], offsetFuerButtons, zeileAngezeigteAbstimmungen + zeilenOffset);
                    paneGridPane.add(lblAgendaText[i], offsetFuerButtons + 1,
                            zeileAngezeigteAbstimmungen + zeilenOffset);
                }

                if (buttonsVeraendern == false) {
                    btnAgendaJa[i] = new Button("Ja");
                } else {
                    btnAgendaJa[i] = new Button(jaVeraendern);
                }
                btnAgendaJa[i].setOnAction(e -> {
                    clickedJa(e);
                });
                btnAgendaJa[i].setStyle(
                        "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertJa
                                + ";" + schriftartAbstimmungsButtons);
                btnAgendaJa[i].setPrefWidth(buttonBreite);

                if (buttonsVeraendern == false) {
                    btnAgendaNein[i] = new Button("Nein");
                } else {
                    btnAgendaNein[i] = new Button(neinVeraendern);
                }
                btnAgendaNein[i].setOnAction(e -> {
                    clickedNein(e);
                });
                btnAgendaNein[i].setStyle(
                        "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertNein
                                + ";" + schriftartAbstimmungsButtons);
                btnAgendaNein[i].setPrefWidth(buttonBreite);

                btnAgendaEnthaltung[i] = new Button("Enthaltung");
                btnAgendaEnthaltung[i].setOnAction(e -> {
                    clickedEnthaltung(e);
                });
                btnAgendaEnthaltung[i].setStyle("-fx-background-color: #"
                        + ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertEnthaltung + ";"
                        + schriftartAbstimmungsButtons);
                btnAgendaEnthaltung[i].setPrefWidth(buttonBreite);

                if (diesenTopAbstimmen) {
                    if (!(CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].identWeisungssatz == -1)) {

                        int offset = 0;
                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].tabletJa == 1) {
                            if (offset == 0) {
                                stimmartErsteSpalte = KonstStimmart.ja;
                            }
                            paneGridPane.add(btnAgendaJa[i], offset, zeileAngezeigteAbstimmungen + zeilenOffset);
                            offset++;
                        }

                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].tabletNein == 1) {
                            if (offset == 0) {
                                stimmartErsteSpalte = KonstStimmart.nein;
                            }
                            paneGridPane.add(btnAgendaNein[i], offset, zeileAngezeigteAbstimmungen + zeilenOffset);
                            offset++;
                        }

                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[offsetInBlock].tabletEnthaltung == 1) {
                            if (offset == 0) {
                                stimmartErsteSpalte = KonstStimmart.enthaltung;
                            }
                            paneGridPane.add(btnAgendaEnthaltung[i], offset,
                                    zeileAngezeigteAbstimmungen + zeilenOffset);
                            offset++;
                        }

                    }
                }

                CaBug.druckeLog("offsetAktionaerAktuelleVerarbeitung=" + offsetAktionaerAktuelleVerarbeitung +
                        " offsetInBlock=" + offsetInBlock, logDrucken, 10);
                switch (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[offsetInBlock]) {
                case KonstStimmart.ja:
                    btnAgendaJa[i]
                            .setStyle("-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa
                                    + "; " + schriftartAbstimmungsButtons);
                    break;
                case KonstStimmart.nein:
                    btnAgendaNein[i].setStyle(
                            "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein + "; "
                                    + schriftartAbstimmungsButtons);
                    break;
                case KonstStimmart.enthaltung:
                    btnAgendaEnthaltung[i].setStyle(
                            "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung
                                    + "; " + schriftartAbstimmungsButtons);
                    break;
                }
                if (diesenTopAbstimmen) {
                    zeileAngezeigteAbstimmungen++;
                }

            }

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(true);

            btnStimmeAbgeben.requestFocus();
            return;
        }

        if (statusAnzeige == 3) { /*Stimme wurde abgegeben*/

            int gattungen[] = { 0, 0, 0, 0, 0, 0 };
            /*AAAAA nur temporär: wenn keine Gattung, dann Gattung 1 auf true setzen
             * Ist bei Offline-Modus der Fall - hier muß aktuell Gattung in der
             * Nummernform kodiert sein. Das muß noch erweitert werden, dass - wenn
             * nicht kodiert - die Gattung abhängig vom Nummernkreis festgelegt wird.*/
            CaBug.druckeLog("Gattung ermitteln", logDrucken, 10);
            boolean gattungGefunden = false;
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                gattungen[weAbstimmungAktionaerLesenRC[i].gattungId] = 1;
                if (weAbstimmungAktionaerLesenRC[i].gattungId != 0) {
                    CaBug.druckeLog("GattungId=" + weAbstimmungAktionaerLesenRC[i].gattungId, logDrucken, 10);
                    gattungGefunden = true;
                }
            }
            if (gattungGefunden == false) {
                gattungen[1] = 1;
            }

            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnVor.setVisible(false);
            btnZurueck.setVisible(false);

            if (tabletIstPersoenlichesTablet == true) {
                btnDurchgefuehrt.setText("Stimmabgabe erfolgreich - Weiter");
            } else {
                btnDurchgefuehrt.setStyle("-fx-background-color: #00ff00");
            }

            btnDurchgefuehrt.setVisible(true);
            btnDurchgefuehrt.setDisable(false);

            btnDurchgefuehrt.requestFocus();

            btnGesamtJa.setVisible(false);
            btnGesamtNein.setVisible(false);
            btnGesamtEnthaltung.setVisible(false);

            /************* Nun Agenda anzeigen ***************/

            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            lblAgendaTOP = new Label[anzAgenda];
            lblAgendaText = new Label[anzAgenda];

            int zeileAngezeigteAbstimmungen = 0;

            for (int i = 0; i < anzAgenda; i++) {

                boolean diesenTopAbstimmen = false;
                for (int i1 = 1; i1 <= 5; i1++) {
                    if (gattungen[i1] == 1) {
                        if (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].stimmberechtigteGattungen[i1
                                - 1] == 1) {
                            diesenTopAbstimmen = true;
                        }
                    }
                }

                boolean buttonsVeraendern = false;
                String topVeraendern = "";
                String jaVeraendern = "";
                String neinVeraendern = "";

                String hTOPText = "";
                if (ParamS.param.paramAbstimmungParameter.beiTabletTextKurzOderLang == 2) {
                    hTOPText = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].anzeigeBezeichnungLang;
                } else {
                    hTOPText = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].anzeigeBezeichnungKurz;
                }
                if (hTOPText.length() > 1) {
                    if (hTOPText.substring(0, 1).equals("#")) {
                        String[] beschriftungen = hTOPText.split(";"); /* #;Toptext;Ja;Nein*/
                        if (beschriftungen.length == 4) {
                            buttonsVeraendern = true;
                            topVeraendern = beschriftungen[1];
                            jaVeraendern = beschriftungen[2];
                            neinVeraendern = beschriftungen[3];
                        }
                    }
                }

                lblAgendaTOP[i] = new Label();
                lblAgendaTOP[i].setText(CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummer
                        + CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].nummerindex);
                lblAgendaTOP[i].setStyle(schriftartText);

                lblAgendaText[i] = new Label();
                if (buttonsVeraendern == false) {
                    if (ParamS.param.paramAbstimmungParameter.beiTabletTextKurzOderLang == 2) {
                        lblAgendaText[i].setText(
                                CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].anzeigeBezeichnungLang);
                    } else {
                        lblAgendaText[i].setText(
                                CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].anzeigeBezeichnungKurz);
                    }
                } else {
                    lblAgendaText[i].setText(topVeraendern);
                }
                lblAgendaText[i].setWrapText(true);
                lblAgendaText[i].setMaxWidth(maximaleBreiteTopText);
                lblAgendaText[i].setStyle(schriftartText);

                if (diesenTopAbstimmen) {
                    paneGridPane.add(lblAgendaTOP[i], 1, zeileAngezeigteAbstimmungen);
                    paneGridPane.add(lblAgendaText[i], 2, zeileAngezeigteAbstimmungen);
                }

                Button hButton = new Button();
                ;
                boolean enthaltung = false;
                switch (weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i]) {

                case KonstStimmart.ja: {
                    hButton.setStyle("-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa
                            + "; " + schriftartAbstimmungsButtons);
                    if (buttonsVeraendern == false) {
                        hButton.setText("Ja");
                    } else {
                        hButton.setText(jaVeraendern);
                    }
                    break;
                }
                case KonstStimmart.nein: {
                    hButton.setStyle(
                            "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein + "; "
                                    + schriftartAbstimmungsButtons);
                    if (buttonsVeraendern == false) {
                        hButton.setText("Nein");
                    } else {
                        hButton.setText(neinVeraendern);
                    }
                    break;
                }
                case KonstStimmart.enthaltung: {
                    hButton.setStyle(
                            "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung
                                    + "; " + schriftartAbstimmungsButtons);
                    hButton.setText("Enthaltung");
                    enthaltung = true;
                    break;
                }
                default: {
                    switch (CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].stimmenAuswerten) {
                    case -1: {
                        hButton.setText("Enthaltung");
                        hButton.setStyle("-fx-background-color: #"
                                + ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung + "; "
                                + schriftartAbstimmungsButtons);
                        enthaltung = true;
                        break;
                    }
                    case KonstStimmart.ja: {
                        hButton.setText("Ja");
                        hButton.setStyle(
                                "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa
                                        + "; " + schriftartAbstimmungsButtons);
                        break;
                    }
                    case KonstStimmart.nein: {
                        hButton.setText("Nein");
                        hButton.setStyle(
                                "-fx-background-color: #" + ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein
                                        + "; " + schriftartAbstimmungsButtons);
                        break;
                    }
                    }
                }
                }
                hButton.setPrefWidth(buttonBreite);

                if (diesenTopAbstimmen) {
                    if (!(CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].identWeisungssatz == -1)) {
                        if (enthaltung == false
                                || CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen[i].zuAbstimmungsgruppe == 0) {
                            paneGridPane.add(hButton, 0, zeileAngezeigteAbstimmungen);
                        }
                    }
                }

                if (diesenTopAbstimmen) {
                    zeileAngezeigteAbstimmungen++;
                }

            }
            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(true);

        }

        if (statusAnzeige == 11) { /*Persönliches Tablet - noch nicht zugeordnet*/

            tfScanFeld.setEditable(false);
            tfScanFeld.setDisable(true);
            tfScanFeld.setText(Integer.toString(ParamS.clGlobalVar.arbeitsplatz));

            lblFehlerMeldung.setText("");

            btnAbstimmungsModus.setVisible(false);
            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);

            lblGrosseMeldung.setText("Noch nicht zugeordnet");
            lblGrosseMeldung.setVisible(true);

            btnDurchgefuehrt.setVisible(true);
            btnDurchgefuehrt.setDisable(false);
            btnDurchgefuehrt.setText("Erneut Versuchen");

            //            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            /** Abstimmungs-Bereich löschen */
            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(false);

            return;
        }

        if (statusAnzeige == 12) { /*Bereit zur Abstimmung*/

            tfScanFeld.setEditable(false);
            tfScanFeld.setDisable(true);
            tfScanFeld.setText(Integer.toString(ParamS.clGlobalVar.arbeitsplatz));

            lblFehlerMeldung.setText(anzeigeName);

            btnAbstimmungsModus.setVisible(false);
            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);

            lblGrosseMeldung.setText(
                    "Bitte beginnen Sie mit der Abstimmung/Wahl sobald sie von der Versammlungsleitung eröffnet wurde.");
            lblGrosseMeldung.setVisible(true);

            btnDurchgefuehrt.setVisible(true);
            btnDurchgefuehrt.setDisable(false);
            btnDurchgefuehrt.setText("Abstimmung/Wahl beginnen");

            //            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            /** Abstimmungs-Bereich löschen */
            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(false);

            return;
        }

        if (statusAnzeige == 13) { /*offline*/

            tfScanFeld.setEditable(false);
            tfScanFeld.setDisable(true);
            tfScanFeld.setText(Integer.toString(ParamS.clGlobalVar.arbeitsplatz));

            lblFehlerMeldung.setText("");

            btnAbstimmungsModus.setVisible(false);
            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);

            lblGrosseMeldung.setText(
                    "Das Tablet hat derzeit keine Verbindung. Bitte versuchen Sie es erneut. Wenn die Meldung dann immer noch auftritt wenden Sie sich bitte an das Support-Personal.");
            lblGrosseMeldung.setVisible(true);

            btnDurchgefuehrt.setVisible(true);
            btnDurchgefuehrt.setDisable(false);
            btnDurchgefuehrt.setText("Erneut versuchen");

            //            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            /** Abstimmungs-Bereich löschen */
            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(false);

            return;
        }

        if (statusAnzeige == 14) { /*Derzeit ist keine Abstimmung aktiv*/

            tfScanFeld.setEditable(false);
            tfScanFeld.setDisable(true);
            tfScanFeld.setText(Integer.toString(ParamS.clGlobalVar.arbeitsplatz));

            lblFehlerMeldung.setText(anzeigeName);

            btnAbstimmungsModus.setVisible(false);
            btnStimmeAbgeben.setVisible(false);
            btnAbbrechen.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);

            lblGrosseMeldung.setText(
                    "Derzeit ist keine Abstimmung/Wahl geöffnet. Bitte beginnen Sie mit der Abstimmung/Wahl sobald sie von der Versammlungsleitung eröffnet wurde.");
            lblGrosseMeldung.setVisible(true);

            btnDurchgefuehrt.setVisible(true);
            btnDurchgefuehrt.setDisable(false);
            btnDurchgefuehrt.setText("Abstimmung/Wahl beginnen");

            //            anzAgenda = CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock.length;

            /** Abstimmungs-Bereich löschen */
            paneGridPane = new GridPane();
            paneGridPane.setVgap(50);
            paneGridPane.setHgap(20);

            pnScrollPane.setStyle("-fx-background-color:transparent;");
            pnScrollPane.setContent(paneGridPane);
            pnScrollPane.setVvalue(0);
            pnScrollPane.setVisible(false);

            return;
        }

    }

    /** The btn auswaehlen aktionaer. */
    private Button[] btnAuswaehlenAktionaer = null;

    /** The btn fertig. */
    private Button btnFertig = null;

    /** The btn alle gleich. */
    private Button btnAlleGleich = null;

    /**
     * *************Anzeige: Auswahl der Aktionäre************************.
     */
    private void setzeAnzeigeAktionaersauswahl() {

        /*++++Werte ermitteln, Buttons vorbelegen++++++++*/

        if (ParamS.paramGeraet.abstimmungTabletHochformat) {
            /*Gesamtbreite=719*/
            buttonBreite = 120;
            maximaleBreiteTopText = 719 - 2 * 20 - (160);
            schriftartAbstimmungsButtons = "-fx-font: 16px 'System'";
            schriftartGesamtButtons = "-fx-font: 16px 'System'";
            schriftartText = "-fx-font: 16px 'System'; -fx-font-weight: bold";
        } else {
            /*Gesamtbreite=1085*/
            buttonBreite = 200;
            maximaleBreiteTopText = 1085 - 2 * 20 - (240);
            schriftartAbstimmungsButtons = "-fx-font: 24px 'System'";
            schriftartGesamtButtons = "-fx-font: 18px 'System'";
            schriftartText = "-fx-font: 22px 'System'; -fx-font-weight: bold";
        }

        boolean zuBearbeitendeVorhanden = false;
        int zuBearbeitendeAnzahl = 0;
        boolean fertigeVorhanden = false;
        boolean gesperrteVorhanden = false;

        btnAuswaehlenAktionaer = new Button[anzahlAktionaereGelesen];

        for (int i = 0; i < anzahlAktionaereGelesen; i++) {
            switch (verarbeitetKennzeichen[i]) {
            case 0:
                zuBearbeitendeVorhanden = true;
                zuBearbeitendeAnzahl++;
                break;
            case 1:
                fertigeVorhanden = true;
                break;
            case -1:
                gesperrteVorhanden = true;
                break;
            }

            btnAuswaehlenAktionaer[i] = new Button("Abstimmen");
            btnAuswaehlenAktionaer[i].setOnAction(e -> {
                clickedAbstimmenAktionaer(e);
            });
            btnAuswaehlenAktionaer[i].setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
            btnAuswaehlenAktionaer[i].setPrefWidth(buttonBreite);

        }

        btnFertig = new Button("Stimmabgabe beenden");
        btnFertig.setOnAction(e -> {
            clickedStimmabgabeAuswahlBeenden(e);
        });
        btnFertig.setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
        btnFertig.setPrefWidth(buttonBreite + maximaleBreiteTopText);

        btnAlleGleich = new Button("Einheitliche Stimmabgabe für alle aufgelisteten Aktionäre");
        btnAlleGleich.setOnAction(e -> {
            clickedStimmabgabeEinheitlich(e);
        });
        btnAlleGleich.setStyle("-fx-background-color: #A7A7A7; " + schriftartAbstimmungsButtons);
        btnAlleGleich.setPrefWidth(buttonBreite + maximaleBreiteTopText);

        /*+++Anzeige zurücksetzen++++*/
        lblFehlerMeldung.setText("");
        tfScanFeld.setVisible(false);
        btnAbstimmungsModus.setVisible(false);
        btnDurchgefuehrt.setVisible(false);

        btnStimmeAbgeben.setVisible(false);
        btnAbbrechen.setVisible(false);
        btnZurueck.setVisible(false);
        btnVor.setVisible(false);

        paneGridPane = new GridPane();
        paneGridPane.setVgap(20);
        paneGridPane.setHgap(20);

        int offsetZeileInGrid = 0;

        /*+++++Button Fertig++++*/
        paneGridPane.add(btnFertig, 0, offsetZeileInGrid, 2, 1);
        offsetZeileInGrid++;

        if (zuBearbeitendeVorhanden) {
            /******** Zwischenüberschrift - bearbeitet anzeigen **********/
            Label lblUeberschriftBearbeiten = new Label();
            lblUeberschriftBearbeiten.setText("Bitte führen Sie für folgende Aktionäre die Stimmabgabe durch:");
            lblUeberschriftBearbeiten.setWrapText(true);
            lblUeberschriftBearbeiten.setMaxWidth(buttonBreite + maximaleBreiteTopText);
            lblUeberschriftBearbeiten.setStyle(schriftartText);
            paneGridPane.add(lblUeberschriftBearbeiten, 0, offsetZeileInGrid, 2, 1);
            offsetZeileInGrid++;

            /*++++Button für alle folgenden einheitlich abgeben++++++*/
            if (zuBearbeitendeAnzahl > 1) {
                paneGridPane.add(btnAlleGleich, 0, offsetZeileInGrid, 2, 1);
                offsetZeileInGrid++;
            }

            /*++++Unbearbeitete Aktionäre anzeigen+++++*/
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                if (verarbeitetKennzeichen[i] == 0) {
                    paneGridPane.add(btnAuswaehlenAktionaer[i], 0, offsetZeileInGrid);

                    Label lblAktionaer = new Label();
                    lblAktionaer.setText(weAbstimmungAktionaerLesenRC[i].name + " "
                            + weAbstimmungAktionaerLesenRC[i].vorname + " " + weAbstimmungAktionaerLesenRC[i].ort);
                    lblAktionaer.setWrapText(true);
                    lblAktionaer.setMaxWidth(maximaleBreiteTopText);
                    lblAktionaer.setStyle(schriftartText);
                    paneGridPane.add(lblAktionaer, 1, offsetZeileInGrid);

                    offsetZeileInGrid++;
                }
            }
        } else {
            /******** Zwischenüberschrift - für alle abgegeben **********/
            Label lblUeberschriftAlleAbgegeben = new Label();
            lblUeberschriftAlleAbgegeben.setText("Sie haben bereits für alle Aktionäre die Stimmabgabe durchgeführt.");
            lblUeberschriftAlleAbgegeben.setWrapText(true);
            lblUeberschriftAlleAbgegeben.setMaxWidth(buttonBreite + maximaleBreiteTopText);
            lblUeberschriftAlleAbgegeben.setStyle(schriftartText);
            paneGridPane.add(lblUeberschriftAlleAbgegeben, 0, offsetZeileInGrid, 2, 1);
            offsetZeileInGrid++;

        }

        if (fertigeVorhanden) {
            /********* Zwischenüberschrift - bearbeitet anzeigen ********/
            Label lblUeberschriftBereitsAbgegeben = new Label();
            lblUeberschriftBereitsAbgegeben
                    .setText("Für folgende Aktionäre haben Sie die Stimmabgabe bereits durchgeführt:");
            lblUeberschriftBereitsAbgegeben.setWrapText(true);
            lblUeberschriftBereitsAbgegeben.setMaxWidth(buttonBreite + maximaleBreiteTopText);
            lblUeberschriftBereitsAbgegeben.setStyle(schriftartText);
            paneGridPane.add(lblUeberschriftBereitsAbgegeben, 0, offsetZeileInGrid, 2, 1);
            offsetZeileInGrid++;

            /*+++bearbeitete Aktionäre anzeigen++++++++++*/
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                if (verarbeitetKennzeichen[i] == 1) {
                    Label lblAktionaer = new Label();
                    lblAktionaer.setText(weAbstimmungAktionaerLesenRC[i].name + " "
                            + weAbstimmungAktionaerLesenRC[i].vorname + " " + weAbstimmungAktionaerLesenRC[i].ort);
                    lblAktionaer.setWrapText(true);
                    lblAktionaer.setMaxWidth(maximaleBreiteTopText);
                    lblAktionaer.setStyle(schriftartText);
                    paneGridPane.add(lblAktionaer, 1, offsetZeileInGrid);

                    offsetZeileInGrid++;
                }
            }
        }

        if (gesperrteVorhanden) {
            /********* Zwischenüberschrift - gesperrte anzeigen ********/
            Label lblUeberschriftAlleGesperrten = new Label();
            lblUeberschriftAlleGesperrten.setText("Für folgende Aktionäre können Sie keine Stimmabgabe durchführen:");
            lblUeberschriftAlleGesperrten.setWrapText(true);
            lblUeberschriftAlleGesperrten.setMaxWidth(buttonBreite + maximaleBreiteTopText);
            lblUeberschriftAlleGesperrten.setStyle(schriftartText);
            paneGridPane.add(lblUeberschriftAlleGesperrten, 0, offsetZeileInGrid, 2, 1);
            offsetZeileInGrid++;

            /*+++gesperrte Aktionäre anzeigen++++++++++*/
            for (int i = 0; i < anzahlAktionaereGelesen; i++) {
                if (verarbeitetKennzeichen[i] == -1) {
                    Label lblAktionaer = new Label();
                    lblAktionaer.setText(weAbstimmungAktionaerLesenRC[i].name + " "
                            + weAbstimmungAktionaerLesenRC[i].vorname + " " + weAbstimmungAktionaerLesenRC[i].ort);
                    lblAktionaer.setWrapText(true);
                    lblAktionaer.setMaxWidth(maximaleBreiteTopText);
                    lblAktionaer.setStyle(schriftartText);
                    paneGridPane.add(lblAktionaer, 1, offsetZeileInGrid);

                    offsetZeileInGrid++;
                }
            }
        }

        pnScrollPane.setStyle("-fx-background-color:transparent;");
        pnScrollPane.setContent(paneGridPane);
        pnScrollPane.setVvalue(0);

    }

    /**
     * Clicked abstimmen aktionaer.
     *
     * @param event the event
     */
    @FXML
    void clickedAbstimmenAktionaer(ActionEvent event) {
        int gef = -1;
        for (int i = 0; i < anzahlAktionaereGelesen; i++) {
            if (event.getSource() == btnAuswaehlenAktionaer[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            offsetAktionaerAktuelleVerarbeitung = gef;
            aktuelleStimmabgabeFuerAlleDurchfuehren = false;

            scanString = stimmblocknummer[offsetAktionaerAktuelleVerarbeitung];

            clearFehlermeldung();
            String angezeigteNamen = "";
            String hStimmblocknummer = "";
            angezeigteNamen = weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].name + " "
                    + weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].vorname + " "
                    + weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].ort + "; ";
            lblFehlerMeldung.setText(angezeigteNamen);
            tfScanFeld.setText(hStimmblocknummer);

            if (tabletIstPersoenlichesTablet) {
                statusAnzeige = 12;
                angezeigteSeite = 1;
            } else {
                statusAnzeige = 2;
                angezeigteSeite = 1;
            }
            setzeAnzeige();
        }
    }

    /**
     * Clicked stimmabgabe auswahl beenden.
     *
     * @param event the event
     */
    @FXML
    void clickedStimmabgabeAuswahlBeenden(ActionEvent event) {
        clearFehlermeldung();
        statusAnzeige = 1;
        setzeAnzeige();
        tfScanFeld.requestFocus();

    }

    /**
     * Clicked stimmabgabe einheitlich.
     *
     * @param event the event
     */
    @FXML
    void clickedStimmabgabeEinheitlich(ActionEvent event) {
        aktuelleStimmabgabeFuerAlleDurchfuehren = false;
        offsetAktionaerAktuelleVerarbeitung = -1;

        int gef = -1;
        for (int i = 0; i < anzahlAktionaereGelesen; i++) {
            if (verarbeitetKennzeichen[i] == 0 && gef == -1) {
                gef = i;
            }
        }

        if (gef != -1) {
            offsetAktionaerAktuelleVerarbeitung = gef;
            aktuelleStimmabgabeFuerAlleDurchfuehren = true;

            clearFehlermeldung();
            lblFehlerMeldung.setText("Einheitliche Stimmabgabe");
            tfScanFeld.setText("");

            statusAnzeige = 2;
            angezeigteSeite = 1;
            setzeAnzeige();
        }

    }

    /**
     * On btn abstimmungs modus.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbstimmungsModus(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlWechseln controllerFenster = new CtrlWechseln();

        controllerFenster.init(neuerDialog, "Beenden Abstimmungsmodus?");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wechseln.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHautStage.onBtnAbstimmungsModus 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, controllerFenster.width, controllerFenster.height);
        neuerDialog.setTitle("Wechseln");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        boolean ausgewaehlt = controllerFenster.ausgewaehlt;

        if (ausgewaehlt) {

            WETabletSetzeStatus weTabletSetzeStatus = new WETabletSetzeStatus();
            WELoginVerify weLoginVerify = new WELoginVerify();
            ;
            weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
            weTabletSetzeStatus.tabletNummer = ParamS.clGlobalVar.arbeitsplatz;
            weTabletSetzeStatus.setWeLoginVerify(weLoginVerify);
            if (offlineBetrieb == false) {

                WETabletSetzeStatusRC weTabletSetzeStatusRC = wsClient
                        .tabletSetzeStatusAbstimmungBeendet(weTabletSetzeStatus);
                if (weTabletSetzeStatusRC.rc == -99999) {
                    offlineBetrieb = true;
                }
            }
            while (offlineBetrieb) {

                int rc = zeigeOfflineWarnung();
                if (rc == 0 || rc == 2) {
                    return;
                } /*Button retry ansonsten gedrückt*/

                /*Neuer Übertragungsversuch*/
                retryUebertragen();
                System.out.println("Neuer Übertragungsversuch");

            }

            eigeneStage.hide();
        }

    }

    /**
     * Kartennummer=1 oder 2; return ohne Aktionär, wenn Pfad in Parameter leer ist.
     *
     * @param lDbBundle    the l db bundle
     * @param kartenNummer the karten nummer
     * @param zeitStempel  the zeit stempel
     */
    private void speichernAufKarte(DbBundle lDbBundle, int kartenNummer, long zeitStempel) {
        String pfad = "";
        if (kartenNummer == 1) {
            pfad = ParamS.clGlobalVar.lwPfadSicherung1;
        } else {
            pfad = ParamS.clGlobalVar.lwPfadSicherung2;
        }
        if (pfad.isEmpty()) {
            return;
        }

        String dateiname = pfad + "\\abstimmung" + Integer.toString(ParamS.clGlobalVar.arbeitsplatz);
        CaDateiWrite sicherungsdatei = new CaDateiWrite();
        //    	String dateiname="abstimmung"+Integer.toString(ClGlobalVar.arbeitsplatz);
        sicherungsdatei.dateiart = ".txt";
        sicherungsdatei.trennzeichen = ';';
        sicherungsdatei.oeffneNameExplizitAppend(lDbBundle, dateiname);
        sicherungsdatei.ausgabe(scanString);
        sicherungsdatei.ausgabe(Long.toString(zeitStempel));
        sicherungsdatei.ausgabe(
                Integer.toString(CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblock.ident));

        for (int i = 0; i < anzAgenda; i++) {
            sicherungsdatei.ausgabe(
                    Integer.toString(weAbstimmungAktionaerLesenRC[offsetAktionaerAktuelleVerarbeitung].abstimmart[i]));
        }
        sicherungsdatei.newline();
        sicherungsdatei.schliessen();

    }

    /**
     * Retry uebertragen.
     */
    private void retryUebertragen() {
        offlineBetrieb = false;

        String fehlertext = "";

        wsClient = new WSClient();

        String importDateiname = ParamS.clGlobalVar.lwPfadSicherung1 + "\\abstimmung"
                + Integer.toString(ParamS.clGlobalVar.arbeitsplatz) + ".txt";

        try {

            FileReader fr = new FileReader(importDateiname);
            BufferedReader br = new BufferedReader(fr);

            try {
                String zeile = null;
                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    if (!zeile.isEmpty()) {
                        System.out.println("zeile gelesen");
                        weAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC[1];
                        weAbstimmungAktionaerLesenRC[0] = new WEAbstimmungAktionaerLesenRC();
                        weAbstimmungAktionaerLesenRC[0].abstimmart = new int[anzAgenda];
                        offsetAktionaerAktuelleVerarbeitung = 0;
                        aktuelleStimmabgabeFuerAlleDurchfuehren = false;

                        weAbstimmungAktionaerLesen = new WEAbstimmungAktionaerLesen();

                        String[] zeileSplit = zeile.split(";");
                        if (Integer.parseInt(
                                zeileSplit[2]) == CInjects.weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblock.ident) {
                            String aktionaersnummer = zeileSplit[0].replace('"', ' ').trim();

                            //						String hStimmkartennummer=aktionaersnummer.substring(1, 6);

                            /*Erst mal Aktionär einlesen*/

                            DbBundle lDbBundle = new DbBundle();
                            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                            int rc = blNummernformen.dekodiere(aktionaersnummer, KonstKartenklasse.unbekannt);
                            if (rc < 0) {
                                CaBug.drucke("CtrlAbstimmungsStage.retryUebertragen 001");
                            }
                            kartenklasse = blNummernformen.rcKartenklasse;
                            stimmblocknummer = new String[blNummernformen.rcIdentifikationsnummer.size()];
                            stimmblocknummer[0] = blNummernformen.rcIdentifikationsnummer.get(0);

                            weAbstimmungAktionaerLesen.stimmblockNummer = stimmblocknummer[0];
                            weAbstimmungAktionaerLesen.kartenklasse = kartenklasse;
                            if (kartenklasse == KonstKartenklasse.eintrittskartennummer) {
                                weAbstimmungAktionaerLesen.neben = blNummernformen.rcIdentifikationsnummerNeben.get(0);
                            }

                            WELoginVerify weLoginVerify = new WELoginVerify();
                            ;
                            weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
                            weAbstimmungAktionaerLesen.setWeLoginVerify(weLoginVerify);

                            weAbstimmungAktionaerLesenRC[0] = wsClient
                                    .abstimmungAktionaerLesen(weAbstimmungAktionaerLesen);
                            if (weAbstimmungAktionaerLesenRC[0].rc == -99999) {
                                offlineBetrieb = true;
                                br.close();
                                fr.close();
                                return;
                            }

                            if (weAbstimmungAktionaerLesenRC[0].rc != -1 && weAbstimmungAktionaerLesenRC[0].rc != -2
                                    && weAbstimmungAktionaerLesenRC[0].rc != -3) {
                                /*Nun Abstimmung speichern*/

                                for (int j = 0; j < anzAgenda; j++) {
                                    weAbstimmungAktionaerLesenRC[0].abstimmart[j] = Integer
                                            .parseInt(zeileSplit[j + 3].replace('"', ' ').trim());
                                }

                                long zeitstempel = Long.parseLong(zeileSplit[1].replace('"', ' ').trim());
                                System.out.println("CtrlAbstimmungsStage stimmenUebertragen");
                                stimmenUebertragen(zeitstempel, false);
                                if (offlineBetrieb == true) {
                                    br.close();
                                    fr.close();
                                    return;
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

    }

    /**
     * Zeige offline warnung.
     *
     * @return the int
     */
    /* 0 = nichts gedrückt, abgebrochen
     * 1 = retry gedrückt
     * 2 = zurück gedrückt 
     */
    int zeigeOfflineWarnung() {
        Stage neuerDialog = new Stage();

        CtrlOffline controllerFenster = new CtrlOffline();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("OfflineStage.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHautStage.zeigeOfflineWarnung 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Wechseln");
        neuerDialog.setScene(scene);
        neuerDialog.initStyle(StageStyle.UNDECORATED);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
        if (controllerFenster.retrygedrueckt) {
            return 1;
        }
        if (controllerFenster.fortsetzengedrueckt) {
            return 2;
        }
        return 0;

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

    /**
     * ************************************Ab hier
     * Präsenzbuchungsfunktionen********************************************************.
     */

    /** Labeldaten */
    String lekNummer = "";

    /** The lsk nummer. */
    String lskNummer = "";

    /** The lsk nummer bar. */
    String lskNummerBar = "";

    /** The lvorname aktionaer. */
    String lvornameAktionaer = "";

    /** The lnachname aktionaer. */
    String lnachnameAktionaer = "";

    /** The lort aktionaer. */
    String lortAktionaer = "";

    /** The lvorname vertreter. */
    String lvornameVertreter = "";

    /** The lname vertreter. */
    String lnameVertreter = "";

    /** The lort vertreter. */
    String lortVertreter = "";

    /** The laktienzahl. */
    String laktienzahl = "";

    /** Zuletzt gebucht. */
    String zuletztGebucht = "";

    /**
     * ****************Für "normale" Zugangs/Abgangbuchungen*********************.
     */
    EclMeldung lEclMeldung = null;

    /** The aktionaer vollmachten. */
    EclWillensErklVollmachtenAnDritte[] aktionaerVollmachten = null;

    /** The aktionaer vollmachten aktuelle abfrage. */
    int aktionaerVollmachtenAktuelleAbfrage = 0;

    /**
     * Für Zugang: vorbestimmte Peron (d.h. nur diese darf zugehen!) (siehe
     * WEPraesenzStatusabfrageRC)
     */
    private int vorbestimmtePersonNatJur = 0;

    /** The l stimmkartennummer. */
    //	String lEintrittskartennummer=null;
    String lStimmkartennummer = null;

    /** The aktion. */
    int aktion = 0;

    /** bei Abgangsbuchung: auch Button Vertreterwechsel anbieten. */
    int auchVollmachtwechsel = 0;

    /**
     * Bei Abgangsbuchung: es wurde Erstzugangs-oder WiederzugangsKarte eingelesen.
     */
    int zugangskarteGelesen = 0;

    /** Für die Anzeige von bereits von zugeordneten EKs. */
    private EclZutrittsIdent zutrittsIdent = null;

    /** The zutritts ident kartenklasse. */
    private int zutrittsIdentKartenklasse = 0;

    /**
     * Für die Eingabe von zuzuordnenden Stimmkarten. Entweder bereits zugeordnete
     * (zum Anzeigen), oder leer (einzugeben), oder - nach der Eingabemaske -
     * gefüllt
     */
    private String[] stimmkarten = null;
    /**
     * wie WEPraesenzStatusAbfrageRC.stimmkartenZuordnungenAktionaer, aber für die
     * tatsächliche Person die Wiederzugeht / Erstzugeht: ==0 => keine Zuordnung
     * erforderlich ==1 => Eingabe erforderlich; ==-1 => bereits zugeordnet
     */
    private int[] stimmkartenEingeben = null;
    /**
     * Generell - Stimmkartenzuordnung muß noch eingegeben werden (true) (bzw. beim
     * Speichern: wurden nei eingegeben
     */
    private boolean stimmkarteEingebenErforderlich = false;

    //	int stimmkartenzuordnen=0;

    /**
     * Nach Personenabfrage/Eingabe gefüllt: -1 => Aktionär selbst >1 =>
     * personNatJurIdent aus aktionaerVollmachten =0 => neuer Vertreter, der in
     * vertreterName, vertreterVorname, vertreterOrt steht, bzw. auch
     * personalNatJurVertreterNeueVollmacht>0 Bei >=0 immer auch vertreterName,
     * vertreterVorname, vertreterOrt gefüllt (für Anzeige zu verwenden)
     */
    private int personNatJurVertreter = 0;

    /** The vertreter name. */
    private String vertreterName = "";

    /** The vertreter vorname. */
    private String vertreterVorname = "";

    /** The vertreter ort. */
    private String vertreterOrt = "";

    /** The person nat jur vertreter neue vollmacht. */
    private int personNatJurVertreterNeueVollmacht = 0;

    /** The nummer erst scan. */
    String nummerErstScan = "";

    /** The gwe praesenz statusabfrage RC. */
    /*Enthält den Statusabfrage-RC*/
    WEPraesenzStatusabfrageRC gwePraesenzStatusabfrageRC = null;

    /**
     * Aktion enthalten.
     *
     * @param aktionsListe the aktions liste
     * @param aktion       the aktion
     * @return true, if successful
     */
    private boolean aktionEnthalten(List<Integer> aktionsListe, int aktion) {
        int i;
        if (aktionsListe == null) {
            return false;
        }
        for (i = 0; i < aktionsListe.size(); i++) {
            if (aktionsListe.get(i) == aktion) {
                return true;
            }
        }

        return false;
    }

    /**
     * Clear daten praesenz.
     */
    private void clearDatenPraesenz() {
        lEclMeldung = null;
        aktionaerVollmachten = null;
        aktionaerVollmachtenAktuelleAbfrage = 0;
        //		lEintrittskartennummer=null;
        lStimmkartennummer = null;

        vorbestimmtePersonNatJur = 0;

        aktion = 0;
        auchVollmachtwechsel = 0;
        zugangskarteGelesen = 0;

        zutrittsIdent = new EclZutrittsIdent();
        zutrittsIdentKartenklasse = 0;

        stimmkarten = null;
        stimmkarten = new String[] { "", "", "", "", "" };
        stimmkartenEingeben = new int[] { 0, 0, 0, 0, 0 };

        stimmkarteEingebenErforderlich = false;
        //		letzteZuzuordnendeStimmkarte=-1;

        personNatJurVertreter = 0;
        vertreterName = "";
        vertreterVorname = "";
        vertreterOrt = "";
        personNatJurVertreterNeueVollmacht = 0;

        nummerErstScan = "";

    }

    /**
     * Ausgabe test.
     *
     * @param wePraesenzStatusabfrageRC the we praesenz statusabfrage RC
     */
    private void ausgabe_test(WEPraesenzStatusabfrageRC wePraesenzStatusabfrageRC) {
        System.out.println(CaFehler.getFehlertext(wePraesenzStatusabfrageRC.rc, 0));
        if (wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformUngueltig
                || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformAktionsnummerUngueltig) {
            return;
        }
        System.out.println("Kartenklasse=" + wePraesenzStatusabfrageRC.rcKartenklasse);
        System.out.println("Kartenart=" + wePraesenzStatusabfrageRC.rcKartenart);
        System.out.println("Länge der Identifikationen=" + wePraesenzStatusabfrageRC.identifikationsnummer.size());

        int i, i1;
        for (i = 0; i < wePraesenzStatusabfrageRC.identifikationsnummer.size(); i++) {
            System.out.println(i + " Identifikation =" + wePraesenzStatusabfrageRC.identifikationsnummer.get(i));
            System.out.println(
                    i + " IdentifikationNeben =" + wePraesenzStatusabfrageRC.identifikationsnummerNeben.get(i));
            System.out.println(i + " kartenklasseZuIdentifikationsnummer "
                    + wePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(i));
            System.out.println(
                    i + " rcZuIdentifikationsnummer " + wePraesenzStatusabfrageRC.rcZuIdentifikationsnummer.get(i));
            for (i1 = 0; i1 < wePraesenzStatusabfrageRC.erforderlicheAktionen.get(i).size(); i1++) {
                System.out.println(i + " erforderlicheAktionen " + KonstWillenserklaerung
                        .getText(wePraesenzStatusabfrageRC.erforderlicheAktionen.get(i).get(i1)));
            }
            for (i1 = 0; i1 < wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i).size(); i1++) {
                System.out.println(i + " zulaessigeFunktionen " + KonstWillenserklaerung
                        .getText(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i).get(i1)));
            }
        }

    }

    /**
     * *************Hier: Funktionen zum Ausführen der Willenserklärungen
     * (praesenzBuchen)***************.
     */
    private WEPraesenzBuchenRC lWEPraesenzBuchenRC = null;

    /** The bl client praesenz buchen. */
    private BlClientPraesenzBuchen blClientPraesenzBuchen = null;

    /**
     * Buchen bl init.
     */
    private void buchen_blInit() {
        if (wsClient == null) {
            wsClient = new WSClient();
        }

        blClientPraesenzBuchen = new BlClientPraesenzBuchen();
        blClientPraesenzBuchen.initialisieren(wsClient);
    }

    /**
     * Fehlerbehandlung.
     */
    private void buchen_fehlerbehandlung() {
        /*Buchung durchgeführt*/

        lekNummer = "";
        lskNummer = "";
        lskNummerBar = "";
        lvornameAktionaer = "";
        lnachnameAktionaer = "";
        lortAktionaer = "";
        lvornameVertreter = "";
        lnameVertreter = "";
        lortVertreter = "";
        laktienzahl = "";
        return;

    }

    /**
     * **********************Buchen Erstzugang******************************.
     */
    private void buchen_erstzugang() {
        if (offlineBetrieb == true) {
            return;
        }
        buchen_blInit();
        blClientPraesenzBuchen.buchen_erstzugang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, 0);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        if (lWEPraesenzBuchenRC.rc == -99999) {
            offlineBetrieb = true;
            return;
        }
        buchen_fehlerbehandlung();
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * **********************Buchen Wiederzugang****************.
     */
    private void buchen_wiederzugang() {
        if (offlineBetrieb == true) {
            return;
        }

        buchen_blInit();
        blClientPraesenzBuchen.buchen_wiederzugang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, vorbestimmtePersonNatJur);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        /*Hinweis: Falls Karte in Sammelkarte, und sonstige erforderliche Aktionen => Sonderschalter*/
        if (lWEPraesenzBuchenRC.rc == -99999) {
            offlineBetrieb = true;
            return;
        }

        buchen_fehlerbehandlung();
    }

    /**
     * =4 => Bevollmächtigter aktionaerVollmachtenAktuelleAbfrage Erstzugang =3 =>
     * Selbst Erstzugang =8 => Widerzugang.
     */

    int zugangsFunktion = 0;

    /**
     * Do btn einlesen.
     *
     * @param nummerScanErsatz the nummer scan ersatz
     * @return true, if successful
     */
    private boolean doBtnEinlesen(String nummerScanErsatz) {
        int i1;

        if (offlineBetrieb == true) {
            return false;
        }

        nummerErstScan = nummerScanErsatz;
        while (nummerErstScan.length() < 5) {
            nummerErstScan = "0" + nummerErstScan;
        }

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WEPraesenzStatusabfrage wePraesenzStatusabfrage = new WEPraesenzStatusabfrage();
        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(201); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
        wePraesenzStatusabfrage.setWeLoginVerify(weLoginVerify);

        if (weAbstimmungAktionaerLesenRC[0].rc == -5) {/*Noch nie präsent*/
            wePraesenzStatusabfrage.identifikationString = "8" + nummerErstScan;
        } else {
            wePraesenzStatusabfrage.identifikationString = "5" + nummerErstScan;
        }
        wePraesenzStatusabfrage.programmFunktionHV = 1;

        WEPraesenzStatusabfrageRC wePraesenzStatusabfrageRC = wsClient.praesenzStatusabfrage(wePraesenzStatusabfrage);
        if (wePraesenzStatusabfrageRC.rc == -99999) {
            offlineBetrieb = true;
            return false;
        }
        gwePraesenzStatusabfrageRC = wePraesenzStatusabfrageRC;
        ausgabe_test(wePraesenzStatusabfrageRC);

        if (wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformUngueltig) {
            System.out.println("Ungültiges Format!");
            return false;
        }
        if (wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformAktionsnummerUngueltig) {
            System.out.println("Ungültige Aktionsnummer!");
            return false;
        }
        if (wePraesenzStatusabfrageRC.rc == CaFehler.pmZutrittsIdentIstStorniert) {
            System.out.println("Eintrittskarte gesperrt => Sonderschalter!");
            return false;
        }
        if (wePraesenzStatusabfrageRC.rc != 1) {
            System.out.println("Fehler " + wePraesenzStatusabfrageRC.rc + " => Sonderschalter!");
            return false;
        }

        if (wePraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.appIdent) { /*TODO #9 App*/

        } else {/*Normaler Barcode gelesen*/
            if (wePraesenzStatusabfrageRC.identifikationsnummer.size() != 1) {
                System.out.println("Ungültiges Format!");
                return false;
            }
            if (wePraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.eintrittskartennummer) {
                /**************** Eintrittskarte wurde gelesen ********************************/
                if (wePraesenzStatusabfrageRC.rc == CaFehler.pfXyNichtVorhanden) {
                    System.out.println("Eintrittskarte unbekannt!");
                    return false;
                }
                lEclMeldung = wePraesenzStatusabfrageRC.meldungen.get(0);
                /*TODO #9 gehört eigentlich in Businesslogik*/
                if (lEclMeldung.stimmen == 0 && lEclMeldung.meldungstyp != 2) {
                    System.out.println("Eintrittskarte mit 0-Bestand => Sonderschalter!");
                    return false;
                }

                vorbestimmtePersonNatJur = wePraesenzStatusabfrageRC.vorbestimmtePersonNatJur.get(0);

                zutrittsIdent.zutrittsIdent = wePraesenzStatusabfrageRC.identifikationsnummer.get(0);
                zutrittsIdent.zutrittsIdentNeben = wePraesenzStatusabfrageRC.identifikationsnummerNeben.get(0);
                zutrittsIdentKartenklasse = wePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(0);
                aktionaerVollmachten = wePraesenzStatusabfrageRC.aktionaerVollmachten.get(0);
                if (wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0) != null
                        && wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size() != 0) {
                    for (i1 = 0; i1 < wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size(); i1++) {
                        System.out.println("0 erforderlicheAktionen " + KonstWillenserklaerung
                                .getText(wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).get(i1)));
                    }
                    System.out.println("Sonderaktionen erforderlich => Sonderschalter!");
                    return false;
                }

                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.erstzugang) == true) {
                    System.out.println("Erstzugang!");
                    if (aktionaerVollmachten != null && aktionaerVollmachten.length > 0) {
                        //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
                        int jgef = -1;
                        for (int j = 0; j < aktionaerVollmachten.length; j++) {
                            if (aktionaerVollmachten[j].wurdeStorniert == false) {
                                if (jgef == -1) {
                                    jgef = j;
                                }
                            }
                        }
                        if (jgef != -1) {
                            aktionaerVollmachtenAktuelleAbfrage = jgef; //
                            zugangsFunktion = 4;
                            setzeAnzeige();
                            return true; //
                        }
                    }
                    zugangsFunktion = 3;
                    setzeAnzeige();
                    return true;
                    //						aktion=IntWillenserklaerung.erstzugang;
                    //						if (aktionaerVollmachten!=null && aktionaerVollmachten.length>0){
                    //							aktionaerVollmachtenAktuelleAbfrage=0;
                    //							statusMaske=4;setzeAnzeige();return;
                    //						}
                    //						statusMaske=3;setzeAnzeige();return;	
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.abgang) == true) {
                    System.out.println("Systemfehler");
                    return false;
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.wiederzugang_nurSelbePerson) == true) {
                    aktion = KonstWillenserklaerung.wiederzugang_nurSelbePerson;
                    zugangsFunktion = 8;
                    setzeAnzeige();
                    return true;
                }

                System.out.println("Funktion am Akkreditierungsschalter nicht unterstützt - Sonderschalter!");
                return false;

            }
            if (wePraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.stimmkartennummer) {
                /**************** Stimmkarte wurde gelesen ********************************/
                if (wePraesenzStatusabfrageRC.rc == CaFehler.pfXyNichtVorhanden) {
                    System.out.println("Stimmkarte unbekannt!");
                    return false;
                }
                if (wePraesenzStatusabfrageRC.rc < 0) {
                    System.out.println("Fehler " + wePraesenzStatusabfrageRC.rc + " "
                            + CaFehler.getFehlertext(wePraesenzStatusabfrageRC.rc, 0));
                    return false;
                }

                lEclMeldung = wePraesenzStatusabfrageRC.meldungen.get(0);

                vorbestimmtePersonNatJur = wePraesenzStatusabfrageRC.vorbestimmtePersonNatJur.get(0);

                zutrittsIdent.zutrittsIdent = wePraesenzStatusabfrageRC.identifikationsnummer.get(0);
                zutrittsIdent.zutrittsIdentNeben = wePraesenzStatusabfrageRC.identifikationsnummerNeben.get(0);
                zutrittsIdentKartenklasse = wePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(0);
                lStimmkartennummer = lEclMeldung.stimmkarte;
                aktionaerVollmachten = wePraesenzStatusabfrageRC.aktionaerVollmachten.get(0);
                if (wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0) != null
                        && wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size() != 0) {
                    for (i1 = 0; i1 < wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size(); i1++) {
                        System.out.println("0 erforderlicheAktionen " + KonstWillenserklaerung
                                .getText(wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).get(i1)));
                    }
                    System.out.println("Sonderaktionen erforderlich => Sonderschalter!");
                    return false;
                }

                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.erstzugang) == true) {
                    aktion = KonstWillenserklaerung.erstzugang;
                    System.out.println("Erstzugang!");
                    if (aktionaerVollmachten != null && aktionaerVollmachten.length > 0) {
                        //VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
                        int jgef = -1;
                        for (int j = 0; j < aktionaerVollmachten.length; j++) {
                            System.out.println(aktionaerVollmachten[j].bevollmaechtigtePerson.name);
                            System.out.println(aktionaerVollmachten[j].wurdeStorniert);

                            if (aktionaerVollmachten[j].wurdeStorniert == false) {
                                if (jgef == -1) {
                                    jgef = j;
                                }
                            }
                        }
                        if (jgef != -1) {
                            aktionaerVollmachtenAktuelleAbfrage = jgef; //
                            zugangsFunktion = 4;
                            setzeAnzeige();
                            return true; //
                        }
                    }
                    zugangsFunktion = 3;
                    setzeAnzeige();
                    return true;
                }

                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.abgang) == true) {
                    aktion = KonstWillenserklaerung.abgang;
                    if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                            KonstWillenserklaerung.vertreterwechsel) == true) {
                        auchVollmachtwechsel = 1;
                    } else {
                        auchVollmachtwechsel = 0;
                    }
                    if (wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang
                            || wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                        zugangskarteGelesen = 1;
                    } else {
                        zugangskarteGelesen = 0;
                    }

                    if (nummerErstScan.substring(0, 1).compareTo("4") != 0) {
                        System.out.println(
                                "Nur Verlassen der Hauptversammlung möglich - Bitte Abgangsbarcode scannen oder Sonderschalter!");
                        return false;

                    }

                    zugangsFunktion = 7;
                    setzeAnzeige();
                    return true;
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.wiederzugang_nurSelbePerson) == true
                        || aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                                KonstWillenserklaerung.wiederZugangFallsStimmaterialVorhanden) == true) {
                    aktion = KonstWillenserklaerung.wiederzugang_nurSelbePerson;
                    zugangsFunktion = 8;
                    setzeAnzeige();
                    return true;
                }

                System.out.println("Funktion am Akkreditierungsschalter nicht unterstützt - Sonderschalter!");
                return false;

            }
        }
        return false;
        //			if (wePraesenzStatusabfrageRC.rc!=1){lblFehlerMeldung.setText(CaFehler.getFehlertext(wePraesenzStatusabfrageRC.rc, 0));return;}
    }

}
