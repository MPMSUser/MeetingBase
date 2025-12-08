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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootWeisungenNeu;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerdatenLesen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Class CtrlHVVollmachtWeisungSRV.
 */
public class CtrlHVVollmachtWeisungSRV extends CtrlRootWeisungenNeu {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf stimmkartennummer. */
    @FXML
    private TextField tfStimmkartennummer;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The btn scan lesen. */
    @FXML
    private Button btnScanLesen;

    /** The tf name vorname. */
    @FXML
    private TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The btn einlesen. */
    @FXML
    private Button btnEinlesen;

    /** The tf zuletzt bearbeitet. */
    @FXML
    private TextField tfZuletztBearbeitet;

    /** The btn alle ja. */
    @FXML
    private Button btnAlleJa;

    /** The btn alle nein. */
    @FXML
    private Button btnAlleNein;

    /** The btn alle enthaltung. */
    @FXML
    private Button btnAlleEnthaltung;

    /** The btn alle nicht teilnahme. */
    @FXML
    private Button btnAlleNichtTeilnahme;

    /** The btn alle ungueltig. */
    @FXML
    private Button btnAlleUngueltig;

    /** The btn alle zuruecksetzen. */
    @FXML
    private Button btnAlleZuruecksetzen;

    /** The rb auto. */
    @FXML
    private RadioButton rbAuto;

    /** The rb stimmkarten nr. */
    @FXML
    private RadioButton rbStimmkartenNr;

    /** The tg karten nr. */
    @FXML
    private ToggleGroup tgKartenNr;

    /** The rb eintrittskarten nr. */
    @FXML
    private RadioButton rbEintrittskartenNr;

    /** **Ab hier individuell****. */

    /****************** Individuelle Anfang *****************/

    private int meldungsIdent = 0;

    /** The letzte bearbeitete nummer. */
    private String letzteBearbeiteteNummer = "";

    /** The person nat jur ident. */
    int personNatJurIdent = 0;

    /** The ecl meldung. */
    private EclMeldung eclMeldung = null;

    /**
     * ***************Individuell Ende***********************.
     */

    @FXML
    void initialize() {
        assert tfStimmkartennummer != null : "fx:id=\"tfStimmkartennummer\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnScanLesen != null : "fx:id=\"btnScanLesen\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert tfNameVorname != null : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnEinlesen != null : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert tfZuletztBearbeitet != null : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert scrpnWeisungen != null : "fx:id=\"scrpnWeisungen\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAlleJa != null : "fx:id=\"btnAlleJa\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAlleNein != null : "fx:id=\"btnAlleNein\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAlleEnthaltung != null : "fx:id=\"btnAlleEnthaltung\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAlleUngueltig != null : "fx:id=\"btnAlleUngueltig\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert btnAlleZuruecksetzen != null : "fx:id=\"btnAlleZuruecksetzen\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert rbStimmkartenNr != null : "fx:id=\"rbStimmkartenNr\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert tgKartenNr != null : "fx:id=\"tgKartenNr\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert rbEintrittskartenNr != null : "fx:id=\"rbEintrittskartenNr\" was not injected: check your FXML file 'HVVollmachtWeisungSRV.fxml'.";
        assert cbKIAV != null : "fx:id=\"cbKIAV\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        
        /*************** Ab hier individuell **********************************/
        skIst = KonstSkIst.srvHV;
        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        lDbBundle = new DbBundle();

        blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(false, lDbBundle);
        blAbstimmungenWeisungen.leseAgendaFuerWeisungenVerlassenHV();

        blSammelkarten = new BlSammelkarten(false, lDbBundle);
        blSammelkarten.leseSammelkartenlisteFuerWeisungen(KonstSkIst.srv, 3);

        rbAuto.setSelected(true);

        if (lDbBundle.mindestensEineStimmkarteWirdZugeordnet() == false) {
            rbStimmkartenNr.setVisible(false);
        }

        setzeStatusAktionaersnummerEingeben();
    }

    /**
     * Cb KIAV changed.
     *
     * @param event the event
     */
    @FXML
    void cbKIAVChanged(ActionEvent event) {
        kiavChanged();
    }

    /**
     * On key aktionaersnummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyAktionaersnummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doEinlesen();
        }
    }

    /**
     * Ausgewaehlte kartenklasse.
     *
     * @return the int
     */
    private int ausgewaehlteKartenklasse() {
        int vorauswahl = KonstKartenklasse.unbekannt;
        if (rbStimmkartenNr.isSelected()) {
            vorauswahl = KonstKartenklasse.stimmkartennummer;
        }
        if (rbEintrittskartenNr.isSelected()) {
            vorauswahl = KonstKartenklasse.eintrittskartennummer;
        }
        return vorauswahl;
    }

    /**
     * Clicked einlesen.
     *
     * @param event the event
     */
    @FXML
    void clickedEinlesen(ActionEvent event) {
        doEinlesen();
    }

    /**
     * Do einlesen.
     */
    private void doEinlesen() {

        int vorauswahl = ausgewaehlteKartenklasse();

        String eingabeString = tfStimmkartennummer.getText().trim();

        BlTeilnehmerdatenLesen blTeilnehmerdatenLesen = new BlTeilnehmerdatenLesen(false, lDbBundle);
        int rc = blTeilnehmerdatenLesen.leseMeldung(vorauswahl, eingabeString, null);
        fehlerMeldungSpeichern = blTeilnehmerdatenLesen.rcFehlerMeldungText;
        eclMeldung = blTeilnehmerdatenLesen.rcEclMeldung;
        personNatJurIdent = blTeilnehmerdatenLesen.rcPersonNatJurIdent;

        if (rc < 0) {
            this.fehlerMeldung(fehlerMeldungSpeichern);
            setzeStatusAktionaersnummerEingeben();
            return;
        } else {
            meldungsIdent = eclMeldung.meldungsIdent;
            aktuelleGattung = eclMeldung.liefereGattung();

            tfNameVorname.setText(eclMeldung.name + " " + eclMeldung.vorname);
            tfOrt.setText(eclMeldung.ort);
            tfAktien.setText(CaString.toStringDE(eclMeldung.stimmen));

            /** Nun passend zur Gattung SRVs und Abstimmungsagenda anzeigen */
            zeigeAbstimmungFuerGattungAnNeu(ParamS.param.paramAbstimmungParameter.weisungHVVorbelegungMit,
                    ParamS.param.paramAbstimmungParameter.weisungHVVorbelegungMit, false);
            boolean brc = zeigeKIAVFuerGattungAnNeu(true);
            if (brc == false) {
                setzeStatusAktionaersnummerEingeben();
                return;
            }

            setzeStatusVerarbeitung();
            letzteBearbeiteteNummer = eingabeString;
        }
    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        setzeStatusAktionaersnummerEingeben();
    }

    /**
     * Pruefe allgemeine eingaben.
     *
     * @return true, if successful
     */
    private boolean pruefeAllgemeineEingaben() {
        if (blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] == -1) {
            fehlerMeldung("Bitte Sammelkarte auswählen!");
            return false;
        }
        return true;
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        if (pruefeAllgemeineEingaben() == false) {
            CaBug.druckeLog("Fehler 1", logDrucken, 10);
            return;
        }
        if (pruefeWeisungenVollstaendig() == false) {
            CaBug.druckeLog("Fehler 2", logDrucken, 10);
            return;
        }
        if (pruefeGruppen(0, aktuelleGattung, false) == false) {
            CaBug.druckeLog("Fehler 3", logDrucken, 10);
            return;
        }
        CaBug.druckeLog("vor Speichern", logDrucken, 10);

        boolean rc = speichernNeu();
        if (rc == false) {
            fehlerMeldung(fehlerMeldungSpeichern);
            return;
        }

        CaBug.druckeLog("nach Speichern", logDrucken, 10);

        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText(letzteBearbeiteteNummer + " " + eclMeldung.name);
        return;
    }

    /** The fehler meldung speichern. */
    private String fehlerMeldungSpeichern = "";

    /**
     * Setze focus auf speichern.
     */
    protected void setzeFocusAufSpeichern() {
        btnSpeichern.requestFocus();
    }

    /**
     * Ausgewaehlte KIAV ausgewaehlteKIAV muß vorher gesetzt werden.
     *
     * @return true, if successful
     */
    private boolean speichernNeu() {

        blAbstimmungenWeisungen.initWeisungMeldung(1);
        weisungseingabenSpeichernDirekt();
        if (pruefeGruppen(0, aktuelleGattung, false) == false) {
            return false;
        }

        int[] meldeIdentArray = { meldungsIdent };
        int[] sammelIdentArray = {
                blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] };
        int[] rc = blAbstimmungenWeisungen.erzeugeSRVHV(sammelIdentArray, meldeIdentArray, null);

        if (rc[0] < 1) {
            fehlerMeldungSpeichern = CaFehler.getFehlertext(rc[0], 0);
            return false;
        }

        return true;
    }

    /**
     * Clicked scan lesen.
     *
     * @param event the event
     */
    @FXML
    void clickedScanLesen(ActionEvent event) {

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() {
                        /*************** Start-Sequenz ********************************/
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                btnScanLesen.setDisable(true);
                                btnEinlesen.setDisable(true);
                                initFehlerGrid();
                            }
                        });

                        int gesamtanzahl = 0;
                        int fehleranzahl = 0;

                        int anzahlInDatenbank = 0;
                        int offsetInDatenbank = 0;

                        /*Ergebnis= aktuellerScanLauf*/
                        int[] zulaessigeKartenarten = { 0, KonstKartenart.vollmachtWeisungSRV };
                        EclScan[] scanTable = blAbstimmungenWeisungen.scanDateiInit(
                                KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV, ausgewaehlteKartenklasse(),
                                zulaessigeKartenarten,
                                blSammelkarten.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard);
                        anzahlInDatenbank = scanTable.length;

                        CaDateiWrite protokollDatei = null;
                        protokollDatei = new CaDateiWrite();
                        protokollDatei.oeffne(lDbBundle, "weisungenScannen");
                        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Scan-Datei : Datenbank");
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Funktion : SRV Verlassen HV");
                        protokollDatei.newline();

                        int anzahlProBuendel = ParamS.paramServer.anzahlScanSRVHVproBuendel;

                        System.out.println("Vor Verarbeitung");
                        /*********************** Verarbeitung **************************/
                        while (offsetInDatenbank < anzahlInDatenbank) {
                            //    	            			System.out.println("offsetInDatenbank="+offsetInDatenbank+" anzahlInDatenbank="+anzahlInDatenbank);
                            int dif = anzahlInDatenbank - offsetInDatenbank;
                            if (dif > anzahlProBuendel) {
                                dif = anzahlProBuendel;
                            }
                            EclScan[] scanBuendel = new EclScan[dif];
                            for (int i = offsetInDatenbank; i < offsetInDatenbank + dif; i++) {
                                scanBuendel[i - offsetInDatenbank] = scanTable[i];
                            }

                            int[] rcArray = blAbstimmungenWeisungen.scanDateiVerarbeiten(scanBuendel);
                            //    	            			System.out.println("in While A rcArray.length="+rcArray.length);
                            for (int i = 0; i < rcArray.length; i++) {
                                System.out.println("i=" + i);
                                if (rcArray[i] < 1) {
                                    protokollDatei.ausgabe("Fehler bei " + blAbstimmungenWeisungen.rcGeleseneNummer[i]
                                            + " :" + blAbstimmungenWeisungen.rcFehlerMeldungString[i]);
                                    //    	            					protokollDatei.ausgabe("Fehler");
                                    protokollDatei.newline();
                                    fehleranzahl++;
                                } else {
                                    protokollDatei.ausgabe("Ok: " + blAbstimmungenWeisungen.rcGeleseneNummer[i]);
                                    //    	            					protokollDatei.ausgabe("OK");
                                    protokollDatei.newline();
                                }
                                gesamtanzahl++;
                            }
                            //    	            			System.out.println("in While B");

                            final int difFuerRunLater = dif;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < difFuerRunLater; i++) {
                                        eintragInFehlerGrid(blAbstimmungenWeisungen.rcGeleseneNummer[i],
                                                blAbstimmungenWeisungen.rcFehlerMeldungString[i]);
                                    }
                                }
                            });
                            //    	            			System.out.println("in While C");

                            offsetInDatenbank += dif;
                        }
                        //   	               	   		System.out.println("Ende-Verarbeitung");
                        /************** EndeSequenz *****************************************/

                        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
                        protokollDatei.newline();
                        protokollDatei
                                .ausgabe("Anzahl verarbeiteter Sätze insgesamt=" + Integer.toString(gesamtanzahl));
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Anzahl Fehler=" + Integer.toString(fehleranzahl));
                        protokollDatei.newline();
                        protokollDatei.schliessen();

                        final int gesamtanzahlFuerRunLater = gesamtanzahl;
                        final int fehleranzahlFuerRunLater = fehleranzahl;
                        final String protokollDateinameFuerRunLater = protokollDatei.dateiname;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                eintragInFehlerGrid("Fertig",
                                        "Anzahl gelesen: " + Integer.toString(gesamtanzahlFuerRunLater)
                                                + " Anzahl Fehler: " + Integer.toString(fehleranzahlFuerRunLater));
                                eintragInFehlerGrid("", " Protokoll in " + protokollDateinameFuerRunLater);
                                btnScanLesen.setDisable(false);
                                btnEinlesen.setDisable(false);
                            }
                        });

                        return null;
                    }
                };
            }

        };
        service.start();

        return;
    }

    /**
     * Setze status aktionaersnummer eingeben.
     */
    private void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(true);
        btnScanLesen.setDisable(false);

        btnAlleJa.setVisible(false);
        btnAlleNein.setVisible(false);
        btnAlleEnthaltung.setVisible(false);
        btnAlleNichtTeilnahme.setVisible(false);
        btnAlleUngueltig.setVisible(false);
        btnAlleZuruecksetzen.setVisible(false);

        tfStimmkartennummer.setText("");
        tfStimmkartennummer.setEditable(true);

        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");

        rbEintrittskartenNr.setDisable(false);
        rbStimmkartenNr.setDisable(false);

        tfStimmkartennummer.requestFocus();

        grpnWeisungen = new MeetingGridPane();
        scrpnWeisungen.setContent(grpnWeisungen);
    }

    /**
     * Zeige gesamt buttons.
     */
    @Override
    protected void zeigeGesamtButtons() {
        if (!scrpnWeisungen.isVisible()) {
            verbergeGesamtButtons();
            return;
        }
        if (buttonAlleJaAktivieren) {
            btnAlleJa.setVisible(true);
        }
        if (buttonAlleNeinAktivieren) {
            btnAlleNein.setVisible(true);
        }
        if (buttonAlleEnthaltungAktivieren) {
            btnAlleEnthaltung.setVisible(true);
        }
        if (buttonAlleNichtTeilnahmeAktivieren) {
            btnAlleNichtTeilnahme.setVisible(true);
        }
        if (buttonAlleUngueltigAktivieren) {
            btnAlleUngueltig.setVisible(true);
        }
        btnAlleZuruecksetzen.setVisible(true);
    }

    /**
     * Verberge gesamt buttons.
     */
    @Override
    protected void verbergeGesamtButtons() {
        btnAlleJa.setVisible(false);
        btnAlleNein.setVisible(false);
        btnAlleEnthaltung.setVisible(false);
        btnAlleNichtTeilnahme.setVisible(false);
        btnAlleUngueltig.setVisible(false);
        btnAlleZuruecksetzen.setVisible(false);
    }

    /**
     * Setze status verarbeitung.
     */
    private void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(false);
        btnAbbruch.setDisable(false);
        btnScanLesen.setDisable(true);

        zeigeGesamtButtons();

        tfStimmkartennummer.setEditable(false);

        rbEintrittskartenNr.setDisable(true);
        rbStimmkartenNr.setDisable(true);
    }

    /**
     * Clicked alle ja.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleJa(ActionEvent event) {
        markierenAlleJa();
    }

    /**
     * Clicked alle nein.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleNein(ActionEvent event) {
        markierenAlleNein();
    }

    /**
     * Clicked alle enthaltung.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleEnthaltung(ActionEvent event) {
        markierenAlleEnthaltung();
    }

    /**
     * Clicked alle nicht teilnahme.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleNichtTeilnahme(ActionEvent event) {
        markierenAlleNichtTeilnahme();
    }

    /**
     * Clicked alle ungueltig.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleUngueltig(ActionEvent event) {
        markierenAlleUngueltig();
    }

    /**
     * Clicked alle zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleZuruecksetzen(ActionEvent event) {
        markierenAlleZuruecksetzen();
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
        hauptFunktion = 0;
        ausgewaehlteFunktion = 0;
    }

}
