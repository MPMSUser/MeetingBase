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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlAbstimmungEingabe.
 */
public class CtrlAbstimmungEingabe {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf stimmkarten. */
    @FXML
    private TextField tfStimmkarten;

    /** The tf fehlermeldung. */
    @FXML
    private TextArea tfFehlermeldung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The btn text datei lesen. */
    @FXML
    private Button btnTextDateiLesen;

    /** The btn scan lesen. */
    @FXML
    private Button btnScanLesen;

    /** The tf dateiname. */
    @FXML
    private TextField tfDateiname;

    /** The btn datei waehlen. */
    @FXML
    private Button btnDateiWaehlen;

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

    /** The scrpn weisungen. */
    @FXML
    private ScrollPane scrpnWeisungen;

    /** The btn alle ja. */
    @FXML
    private Button btnAlleJa;

    /** The btn alle nein. */
    @FXML
    private Button btnAlleNein;

    /** The btn alle enthaltung. */
    @FXML
    private Button btnAlleEnthaltung;

    /** The btn alle ungueltig. */
    @FXML
    private Button btnAlleUngueltig;

    /** The btn alle zuruecksetzen. */
    @FXML
    private Button btnAlleZuruecksetzen;

    /** The tf abstimmblock. */
    @FXML
    private TextField tfAbstimmblock;

    /** ****************Individuelle Anfang****************. */
    GridPane grpnWeisungen = null;

    /** The lbl agenda TOP. */
    Label[] lblAgendaTOP = null;

    /** The lbl gegenantraege TOP. */
    Label[] lblGegenantraegeTOP = null;

    /** The lbl agenda text. */
    Label[] lblAgendaText = null;

    /** The lbl gegenantraege text. */
    Label[] lblGegenantraegeText = null;

    /** The btn agenda zuruecksetzen. */
    Button[] btnAgendaZuruecksetzen = null;

    /** The btn gegenantraege zuruecksetzen. */
    Button[] btnGegenantraegeZuruecksetzen = null;

    /** The tg agenda TOP. */
    ToggleGroup[] tgAgendaTOP = null;

    /** The tg gegenantraege TOP. */
    ToggleGroup[] tgGegenantraegeTOP = null;

    /** The rb agenda ja. */
    RadioButton[] rbAgendaJa = null;

    /** The rb gegenantraege ja. */
    RadioButton[] rbGegenantraegeJa = null;

    /** The rb agenda nein. */
    RadioButton[] rbAgendaNein = null;

    /** The rb gegenantraege nein. */
    RadioButton[] rbGegenantraegeNein = null;

    /** The rb agenda enthaltung. */
    RadioButton[] rbAgendaEnthaltung = null;

    /** The rb gegenantraege enthaltung. */
    RadioButton[] rbGegenantraegeEnthaltung = null;

    /** The rb agenda ungueltig. */
    RadioButton[] rbAgendaUngueltig = null;

    /** The rb gegenantraege ungueltig. */
    RadioButton[] rbGegenantraegeUngueltig = null;

    /** Anzahl der Abstimmungen in Agenda. */
    int anzAgenda = 0;

    /** Anzahl der Abstimmungen in Gegenanträge. */
    int anzGegenantraege = 0;

    /** 4 = Vollmacht/Weisung an SRV 5 = Briefwahl. */
    int ausgewaehlteFunktion = 0;

    /** The quelle. */
    String quelle = "";

    /** The bl abstimmung. */
    private BlAbstimmung blAbstimmung = null;

    /** The meldung. */
    private EclMeldung meldung = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /**
     * ***************Individuell Ende***********************.
     */

    @FXML
    void initialize() {
        assert tfStimmkarten != null : "fx:id=\"tfStimmkarten\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfFehlermeldung != null : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnTextDateiLesen != null : "fx:id=\"btnTextDateiLesen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnScanLesen != null : "fx:id=\"btnScanLesen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfDateiname != null : "fx:id=\"tfDateiname\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnDateiWaehlen != null : "fx:id=\"btnDateiWaehlen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfNameVorname != null : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnEinlesen != null : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfZuletztBearbeitet != null : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert scrpnWeisungen != null : "fx:id=\"scrpnWeisungen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAlleJa != null : "fx:id=\"btnAlleJa\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAlleNein != null : "fx:id=\"btnAlleNein\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAlleEnthaltung != null : "fx:id=\"btnAlleEnthaltung\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAlleUngueltig != null : "fx:id=\"btnAlleUngueltig\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert btnAlleZuruecksetzen != null : "fx:id=\"btnAlleZuruecksetzen\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";
        assert tfAbstimmblock != null : "fx:id=\"tfAbstimmblock\" was not injected: check your FXML file 'AbstimmungEingabe.fxml'.";

        /*************** Ab hier individuell **********************************/
        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        boolean erg = true;

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        blAbstimmung = new BlAbstimmung(lDbBundle);
        erg = blAbstimmung.leseAktivenAbstimmungsblock();
        if (erg == false) {
            tfFehlermeldung.setText("Fehler beim Einlesen Abstimmungsblock");
            lDbBundle.closeAll();
            return;
        }

        tfAbstimmblock.setText(blAbstimmung.aktiverAbstimmungsblock.beschreibung);

        anzAgenda = blAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock();

        lblAgendaTOP = new Label[anzAgenda];
        lblAgendaText = new Label[anzAgenda];
        btnAgendaZuruecksetzen = new Button[anzAgenda];
        tgAgendaTOP = new ToggleGroup[anzAgenda];
        rbAgendaJa = new RadioButton[anzAgenda];
        rbAgendaNein = new RadioButton[anzAgenda];
        rbAgendaEnthaltung = new RadioButton[anzAgenda];
        rbAgendaUngueltig = new RadioButton[anzAgenda];

        grpnWeisungen = new GridPane();
        grpnWeisungen.setVgap(5);
        grpnWeisungen.setHgap(15);
        int i;

        for (i = 0; i < anzAgenda; i++) {

            lblAgendaTOP[i] = new Label();
            lblAgendaTOP[i].setText(blAbstimmung.abstimmungen[i].nummer + blAbstimmung.abstimmungen[i].nummerindex);
            grpnWeisungen.add(lblAgendaTOP[i], 0, i);

            lblAgendaText[i] = new Label();
            lblAgendaText[i].setText(blAbstimmung.abstimmungen[i].anzeigeBezeichnungKurz);
            lblAgendaText[i].setWrapText(true);
            lblAgendaText[i].setMaxWidth(400);
            grpnWeisungen.add(lblAgendaText[i], 1, i);

            tgAgendaTOP[i] = new ToggleGroup();

            rbAgendaJa[i] = new RadioButton("Ja");
            rbAgendaJa[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaJa = rbAgendaJa[i];

            rbAgendaNein[i] = new RadioButton("Nein");
            rbAgendaNein[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaNein = rbAgendaNein[i];

            rbAgendaEnthaltung[i] = new RadioButton("Enthaltung");
            rbAgendaEnthaltung[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaEnthaltung = rbAgendaEnthaltung[i];

            rbAgendaUngueltig[i] = new RadioButton("Ungültig");
            rbAgendaUngueltig[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaUngueltig = rbAgendaUngueltig[i];

            btnAgendaZuruecksetzen[i] = new Button("Zurücksetzen");
            btnAgendaZuruecksetzen[i].setOnAction(e -> {
                lrbAgendaJa.setSelected(false);
                lrbAgendaNein.setSelected(false);
                lrbAgendaEnthaltung.setSelected(false);
                lrbAgendaUngueltig.setSelected(false);
            });

            if (!(blAbstimmung.abstimmungen[i].identWeisungssatz == -1)) {
                grpnWeisungen.add(rbAgendaJa[i], 2, i);
                grpnWeisungen.add(rbAgendaNein[i], 3, i);
                grpnWeisungen.add(rbAgendaEnthaltung[i], 4, i);
                grpnWeisungen.add(rbAgendaUngueltig[i], 5, i);
                grpnWeisungen.add(btnAgendaZuruecksetzen[i], 6, i);

                //				if (ausgewaehlteFunktion==10 || ausgewaehlteFunktion==11 || ausgewaehlteFunktion==12 || ausgewaehlteFunktion==40){
                //					String hString=abstimmungenListeM.getAbstimmungenListeM().get(i).getGewaehlt();
                //					switch (hString){
                //					case "J":{rbAgendaJa[i].setSelected(true);break;}
                //					case "N":{rbAgendaNein[i].setSelected(true);break;}
                //					case "E":{rbAgendaEnthaltung[i].setSelected(true);break;}
                //					case "U":{rbAgendaUngueltig[i].setSelected(true);break;}
                //					}
                //				}

            }

        }

        lDbBundle.closeAll();
        scrpnWeisungen.setContent(grpnWeisungen);
        setzeStatusAktionaersnummerEingeben();

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

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        boolean rc = einlesen();
        lDbBundle.closeAll();

        if (rc == false) {
            tfFehlermeldung.setText(fehlerMeldungSpeichern);
            setzeStatusAbbruch();
            return;
        } else {
            if (fehlerMeldungSpeichern.isEmpty()) {
                tfFehlermeldung.setText("Stimmeingabe kann durchgeführt werden.");
            } else {
                tfFehlermeldung.setText(fehlerMeldungSpeichern);
            }
            setzeStatusVerarbeitung();
        }
    }

    /**
     * Einlesen.
     *
     * @return true, if successful
     */
    private boolean einlesen() {
        int rc;
        boolean rcBool = false;

        fehlerMeldungSpeichern = "";

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        rc = blNummernformen.dekodiere(tfStimmkarten.getText(), 0);
        if (rc < 0) {
            fehlerMeldungSpeichern = CaFehler.getFehlertext(rc, 0);
            return false;
        }
        if (blNummernformen.rcKartenklasse != 3) {
            fehlerMeldungSpeichern = "Keine Stimmkartennummer!";
            return false;
        }

        String sStimmkartennummer = blNummernformen.rcIdentifikationsnummer.get(0);
        rc = lDbBundle.dbStimmkarten.read(sStimmkartennummer);
        if (rc < 1) {
            fehlerMeldungSpeichern = "Stimmkartennummer nicht vorhanden!";
            return false;
        }
        EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
        if (eclStimmkarte.stimmkarteIstGesperrt_Delayed > 1) {
            fehlerMeldungSpeichern = "Stimmkartennummer gesperrt!";
            return false;
        }
        meldung = new EclMeldung();
        meldung.meldungsIdent = eclStimmkarte.meldungsIdentAktionaer;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(meldung);
        meldung = lDbBundle.dbMeldungen.meldungenArray[0];

        tfNameVorname.setText(meldung.name + " " + meldung.vorname);
        tfOrt.setText(meldung.ort);
        tfAktien.setText(CaString.toStringDE(meldung.stimmen));

        if (meldung.statusPraesenz_Delayed != 1) {
            fehlerMeldungSpeichern = "Achtung: Nicht präsent!";
        }
        if (meldung.meldungstyp == 3) {
            fehlerMeldungSpeichern = "Achtung: In Sammelkarte!";
        }

        blAbstimmung.initDb(lDbBundle);
        for (int i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }
        rcBool = blAbstimmung.liefereAktuelleAbstimmungZuMeldeIdent(meldung.meldungsIdent);
        if (rcBool == false) { /*Dann noch keine bishere Abstimmung vorhanden*/
            return true;
        }

        for (int i = 0; i < anzAgenda; i++) {/*Bisheriges Abstimmverhalten anzeigen*/
            int abstimmart = blAbstimmung.liefereAktuelleMarkierungZuAbstimmungsPosition(i);
            switch (abstimmart) {
            case KonstStimmart.ja: {
                rbAgendaJa[i].setSelected(true);
                break;
            }
            case KonstStimmart.nein: {
                rbAgendaNein[i].setSelected(true);
                break;
            }
            case KonstStimmart.enthaltung: {
                rbAgendaEnthaltung[i].setSelected(true);
                break;
            }
            case KonstStimmart.ungueltig: {
                rbAgendaUngueltig[i].setSelected(true);
                break;
            }
            }
        }

        return true;
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

        return true;
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        if (pruefeAllgemeineEingaben() == false) {
            return;
        }

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        boolean rc = speichern(KonstWillenserklaerungWeg.abstManuelleEingabe);
        lDbBundle.closeAll();
        if (rc == false) {
            tfFehlermeldung.setText(fehlerMeldungSpeichern);
            setzeStatusAbbruch();
            return;
        }

        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText(meldung.stimmkarte + " " + meldung.name + " " + meldung.vorname);

        return;

    }

    /** The fehler meldung speichern. */
    private String fehlerMeldungSpeichern = "";

    /**
     * Speichern.
     *
     * @return true, if successful
     */
    private boolean speichern(int pWeg) {

        blAbstimmung.initDb(lDbBundle);
        blAbstimmung.starteSpeichernFuerMeldung(meldung);

        for (int i = 0; i < anzAgenda; i++) {/*Bisheriges Abstimmverhalten anzeigen*/
            int abstimmart = 0;
            if (rbAgendaJa[i].isSelected()) {
                abstimmart = KonstStimmart.ja;
            }
            if (rbAgendaNein[i].isSelected()) {
                abstimmart = KonstStimmart.nein;
            }
            if (rbAgendaEnthaltung[i].isSelected()) {
                abstimmart = KonstStimmart.enthaltung;
            }
            if (rbAgendaUngueltig[i].isSelected()) {
                abstimmart = KonstStimmart.ungueltig;
            }
            blAbstimmung.setzeMarkierungZuAbstimmungsPosition(abstimmart, i, pWeg);
        }

        blAbstimmung.beendeSpeichernFuerMeldung();

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

        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        CaDateiWrite protokollDatei = null;
        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(lDbBundle, "stimmkartenScannen");
        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        protokollDatei.ausgabe("Scan-Datei : " + importDateiname);
        protokollDatei.newline();
        boolean tabletDatei = true;
        int tabletOffset = 1;
        try {

            FileReader fr = new FileReader(importDateiname);
            BufferedReader br = new BufferedReader(fr);

            try {
                String zeile = null;
                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    if (!zeile.isEmpty()) {

                        String[] zeileSplit = zeile.split(";");
                        String aktionaersnummer = zeileSplit[0].replace('"', ' ').trim();
                        if (aktionaersnummer.equals("Barcode")) {
                            tabletDatei = false;
                            tabletOffset = 0;
                        }

                        if (!aktionaersnummer.equals("Barcode")) {

                            gesamtanzahl++;
                            fehlerMeldungSpeichern = "";

                            String hStimmkartennummer = "";
                            String hStimmzettelnummer = "";
                            int gesamtMarkierung = 0; //1 => Gesamtmarkierung für die eingelesene Karte
                            rc = true;
                            if (aktionaersnummer.length() < 13) {
                                rc = false;
                            } else {
                                if (aktionaersnummer.substring(0, 1).compareTo("2") != 0) {
                                    rc = false;
                                }
                            }
                            if (rc == true) {
                                hStimmkartennummer = aktionaersnummer.substring(1, 6);
                                hStimmzettelnummer = aktionaersnummer.substring(10, 12);
                                switch (hStimmzettelnummer) {
                                //								case "01":
                                //								case "07":
                                //								case "08":{gesamtMarkierung=1;break;}
                                default: {
                                    gesamtMarkierung = 0;
                                    break;
                                }

                                }

                                /*Gesamtmarkierung bei
                                 * S1 = 01
                                 * V = 07
                                 * A = 08
                                 * 
                                 */
                            }

                            String[] topString = new String[zeileSplit.length + 5];
                            for (int j = 1; j < zeileSplit.length; j++) {
                                topString[j] = zeileSplit[j].replace('"', ' ').trim();
                            }

                            if (rc == false) {
                                protokollDatei.ausgabe("Aussteuern " + aktionaersnummer + " : falsches Nummernformat");
                                protokollDatei.newline();
                                fehleranzahl++;
                            } else { /*Nun Speichern*/
                                tfStimmkarten.setText(hStimmkartennummer);
                                rc = einlesen();
                                if (rc == false) {
                                    protokollDatei
                                            .ausgabe("Fehler bei " + aktionaersnummer + " :" + fehlerMeldungSpeichern);
                                    protokollDatei.newline();
                                    fehleranzahl++;
                                } else {
                                    if (!fehlerMeldungSpeichern.isEmpty()) {
                                        protokollDatei.ausgabe(
                                                "Warnung bei " + aktionaersnummer + " :" + fehlerMeldungSpeichern);
                                        protokollDatei.newline();

                                    }

                                    blAbstimmung.initDb(lDbBundle);
                                    if (tabletDatei == false) {
                                        blAbstimmung.starteSpeichernFuerMeldung(meldung);
                                    } else {
                                        String hZeitString = zeileSplit[1].replace('"', ' ').trim();
                                        long hZeitLong = Long.parseLong(hZeitString);
                                        blAbstimmung.starteSpeichernFuerMeldung(meldung, hZeitLong, false);
                                    }

                                    int positionGefunden = 0;
                                    String gesamtMarkierungStimmart = "";
                                    if (gesamtMarkierung == 1) {
                                        gesamtMarkierungStimmart = topString[1];

                                    }

                                    for (int j = 1; j < zeileSplit.length; j++) {
                                        topString[j] = zeileSplit[j].replace('"', ' ').trim();

                                        int weisungsPosition = blAbstimmung
                                                .liefereLfdAbstimmungsNrZuStimmkartenNrUndPosition(
                                                        Integer.parseInt(hStimmzettelnummer), j);
                                        if (weisungsPosition != -1) {
                                            positionGefunden = 1;
                                            int stimmart = -1;
                                            String hString = topString[j + gesamtMarkierung + tabletOffset];
                                            if (hString.compareTo("3") == 0 && gesamtMarkierung == 1) { //Gesamtmarkierung
                                                hString = gesamtMarkierungStimmart;
                                            }
                                            switch (hString) {
                                            case "0": {
                                                stimmart = KonstStimmart.nichtMarkiert;
                                                break;
                                            }
                                            case "1": {
                                                stimmart = KonstStimmart.ja;
                                                break;
                                            }
                                            case "2": {
                                                stimmart = KonstStimmart.nein;
                                                break;
                                            }
                                            case "3": {
                                                stimmart = KonstStimmart.enthaltung;
                                                break;
                                            }
                                            case "???": {
                                                stimmart = KonstStimmart.ungueltig;
                                                break;
                                            }
                                            }
                                            if (stimmart != -1) {
                                                blAbstimmung.setzeMarkierungZuAbstimmungsPosition(stimmart,
                                                        weisungsPosition, KonstWillenserklaerungWeg.abstStapelScanner);
                                            }

                                        }

                                    }
                                    blAbstimmung.beendeSpeichernFuerMeldung();

                                    if (positionGefunden == 0) {
                                        protokollDatei.ausgabe("Aussteuern " + aktionaersnummer
                                                + " : keine Markierungsposition passend!");
                                        protokollDatei.newline();
                                        fehleranzahl++;
                                    }
                                    protokollDatei.ausgabe("Ok: " + aktionaersnummer);
                                    protokollDatei.newline();
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
        lDbBundle.closeAll();

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
        /*Wohl nicht fertig geworden?*/

    }

    /**
     * Setze status aktionaersnummer eingeben.
     */
    private void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(true);
        tfStimmkarten.setText("");
        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        tfFehlermeldung.setText("");
        tfStimmkarten.setEditable(true);
        tfStimmkarten.requestFocus();

        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }

    }

    /**
     * Setze status verarbeitung.
     */
    private void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(false);
        btnAbbruch.setDisable(false);
        tfStimmkarten.setEditable(false);
        btnSpeichern.requestFocus();

        btnTextDateiLesen.setDisable(true);
        btnScanLesen.setDisable(true);
        tfDateiname.setDisable(true);
        btnDateiWaehlen.setDisable(true);

    }

    /**
     * Setze status abbruch.
     */
    private void setzeStatusAbbruch() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(false);
        tfStimmkarten.setEditable(false);
        btnAbbruch.requestFocus();

    }

    /**
     * Clicked alle ja.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleJa(ActionEvent event) {
        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(true);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }

    }

    /**
     * Clicked alle nein.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleNein(ActionEvent event) {
        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(true);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }

    }

    /**
     * Clicked alle enthaltung.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleEnthaltung(ActionEvent event) {
        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(true);
            rbAgendaUngueltig[i].setSelected(false);
        }

    }

    /**
     * Clicked alle ungueltig.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleUngueltig(ActionEvent event) {
        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(true);
        }

    }

    /**
     * Clicked alle zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleZuruecksetzen(ActionEvent event) {
        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Funktion: 4 = Vollmacht/Weisung an SRV 5 = Briefwahl.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
