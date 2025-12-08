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

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclKonfigAuswertung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstAutoDrucker;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungAusgabeWeg;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungFunktion;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungSortierung;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
/************************Hinweise / Dokumentation zu "Auto-Druckern"**********************************************************
 * 
 * Global werden gespeichert:
 * > 100 Autodrucker, jeweils Bezeichnung
 * ParamServer.autoDrucker
 * 
 * Je Mandant werden gespeichert:
 * > 100 Auto-Verwendungsmöglichkeiten
 * ParamBasis.autoDruckerVerwendung
 * 
 * 
 * In Tab Auto-Drucker erfolgt:
 * > Pflege der 100 Autodrucker
 * > Button "Anlegen für diesen Arbeitsplatz"
 * > Button "Testen für diesen Arbeitsplatz"
 * 
 * Konkrete Anwendungsbeispiele:
 * > Bühnendrucker, Abstimmungsergebnisse etc.
 *      Auswertedrucker 1 = DruckerNr 11, Auswertedrucker 2=DruckerNr 12, Bühnendrucker=DruckerNr 13, 
 *      Werden auf den Auswerte-PCs entsprechend agelegt und den Auswertejobs berücksichtigt.
 * > "AdHoc-Drucker" für jeden Zugangs-PC
 *      DruckerNr 12. Muß dann (leider) auf jedem Front-Office-PC angelegt werden, jeder Front-Office-PC erhält dabei "seinen
 *      individuellen Drucker".
 * > Gästekarten / Ersatzeintrittskarten für Sonderschalter
 *      DruckerNr 21=Schacht 1, DruckerNr22=Schacht 2.
 *      Muß dann (leider) auf jedem Sonderschalter-PC angelegt werden, analog zum Front-Office-PC.
 *      
 * Das "Leider" relavtiviert sich: das Anlegen ist im wesentlichen ein Testdruck, bei dem der Drucker dann automatisch abgespeichert/Zugeordnet wird.
 * Und einen Testdruck muß man eh bei jeder Konstellation mal machen ...
 * 
 * */

public class CtrlParameterKonfigAuswertung extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /**
     * *****************************Auto-Drucker*****************************************.
     */
    @FXML
    private Button btnSpeichernAutoDrucker;

    /** The scrpn auto drucker. */
    @FXML
    private ScrollPane scrpnAutoDrucker;

    /** The grid pn auto drucker. */
    private GridPane gridPnAutoDrucker;

    /** The tf drucker name. */
    private TextField[] tfDruckerName;

    /** The btn anlegen. */
    private Button[] btnAnlegen;

    /** The btn testen. */
    private Button[] btnTesten;

    /**
     * ************************Auto-Druckausgabe*********************************************.
     */
    @FXML
    private Button btnSpeichernAutoDruckausgabe;

    /** The scrpn auto druckausgabe. */
    @FXML
    private ScrollPane scrpnAutoDruckausgabe;

    /** The grid pn auto druckausgabe. */
    private GridPane gridPnAutoDruckausgabe;

    /** The tf drucker nummer. */
    private TextField[] tfDruckerNummer;

    /** The btn testen funktion. */
    private Button[] btnTestenFunktion;

    /**
     * **************************************Stapel-Auswertung**************************************************.
     */

    @FXML
    private Button btnSpeichern;

    /** The scr pn. */
    @FXML
    private ScrollPane scrPn;

    /** The btn neuer eintrag. */
    @FXML
    private Button btnNeuerEintrag;

    /** The btn refresh. */
    @FXML
    private Button btnRefresh;

    /** The grid pn. */
    private GridPane gridPn;

    /** The cb fuer funktion. */
    private ComboBox<String>[] cbFuerFunktion;

    /** The tf nr. */
    private TextField[] tfNr;

    /** The tf position in lauf. */
    private TextField[] tfPositionInLauf;

    /** The cb ausgeloeste funktion. */
    private ComboBox<String>[] cbAusgeloesteFunktion;

    /** The tf ausgeloeste form nr. */
    private TextField[] tfAusgeloesteFormNr;

    /** The cb sortierung. */
    private ComboBox<String>[] cbSortierung;

    /** The cb gattung. */
    private ComboBox<String>[] cbGattung;

    /** The cb ausgabe weg. */
    private ComboBox<String>[] cbAusgabeWeg;

    /** The tf dateiname pdf. */
    private TextField[] tfDateinamePdf;

    /** The tf ttext fuer formular 1. */
    private TextField[] tfTtextFuerFormular1;

    /** The tf ttext fuer formular 2. */
    private TextField[] tfTtextFuerFormular2;

    /** The l konfig auswertung array. */
    EclKonfigAuswertung[] lKonfigAuswertungArray = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterKonfigAuswertung.fxml'.";
        assert btnNeuerEintrag != null
                : "fx:id=\"btnNeuerEintrag\" was not injected: check your FXML file 'ParameterKonfigAuswertung.fxml'.";
        assert btnRefresh != null
                : "fx:id=\"btnRefresh\" was not injected: check your FXML file 'ParameterKonfigAuswertung.fxml'.";

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
                //                speichernParameter();
            }
        });

        refresh_pane();

        refresh_pane_autoDrucker();
        refresh_pane_autoDruckausgabe();
    }

    /*******************************Auto-Drucker******************************************/
    private void refresh_pane_autoDrucker() {

        tfDruckerName = new TextField[101];
        btnAnlegen = new Button[101];
        btnTesten = new Button[101];

        gridPnAutoDrucker = new GridPane();
        gridPnAutoDrucker.setVgap(5);
        gridPnAutoDrucker.setHgap(15);
        gridPnAutoDrucker.setGridLinesVisible(true);

        Label lbu0 = new Label();
        lbu0.setText("Nr");
        gridPnAutoDrucker.add(lbu0, 0, 0);

        Label lbu1 = new Label();
        lbu1.setText("Bezeichnung");
        gridPnAutoDrucker.add(lbu1, 1, 0);

        for (int i = 1; i <= 100; i++) {
            Label lNr = new Label();
            lNr.setText(Integer.toString(i));
            gridPnAutoDrucker.add(lNr, 0, i);

            tfDruckerName[i] = new TextField();
            tfDruckerName[i].setText(ParamS.paramServer.autoDrucker[i]);
            gridPnAutoDrucker.add(tfDruckerName[i], 1, i);

            btnAnlegen[i] = new Button();
            btnAnlegen[i].setText("Drucker anlegen");
            btnAnlegen[i].setOnAction(e -> {
                clickedAnlegen(e);
            });
            gridPnAutoDrucker.add(btnAnlegen[i], 2, i);

            btnTesten[i] = new Button();
            btnTesten[i].setText("Drucker Testen");
            btnTesten[i].setOnAction(e -> {
                clickedTesten(e);
            });
            gridPnAutoDrucker.add(btnTesten[i], 3, i);

        }

        scrpnAutoDrucker.setContent(gridPnAutoDrucker);

    }

    /**
     * Clicked anlegen.
     *
     * @param event the event
     */
    @FXML
    void clickedAnlegen(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        int gef = -1;
        for (int i = 1; i < btnAnlegen.length; i++) {
            if (event.getSource() == btnAnlegen[i]) {
                gef = i;
            }
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.druckerWiederverwendet = 1;
        rpDrucken.druckerWiederverwendetNummer = gef;

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.testDruckerzuordnung("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Testart", "Druckerzuordnung");
        rpVariablen.fuelleVariable(rpDrucken, "Arbeitsplatznummer",
                Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz));
        rpVariablen.fuelleVariable(rpDrucken, "Druckernummer", Integer.toString(gef));
        rpVariablen.fuelleVariable(rpDrucken, "Druckerbezeichnung", tfDruckerName[gef].getText());
        rpVariablen.fuelleVariable(rpDrucken, "Funktion", "");

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        lDbBundle.closeAll();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Drucker " + Integer.toString(gef) + " Arbeitsplatz "
                + Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz) + " eingerichtet.");
    }

    /**
     * Clicked testen.
     *
     * @param event the event
     */
    @FXML
    void clickedTesten(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        int gef = -1;
        for (int i = 1; i < btnTesten.length; i++) {
            if (event.getSource() == btnTesten[i]) {
                gef = i;
            }
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.setzeAutoDrucker(gef);

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.testDruckerzuordnung("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Testart", "Druckerzuordnung");
        rpVariablen.fuelleVariable(rpDrucken, "Arbeitsplatznummer",
                Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz));
        rpVariablen.fuelleVariable(rpDrucken, "Druckernummer", Integer.toString(gef));
        rpVariablen.fuelleVariable(rpDrucken, "Druckerbezeichnung", tfDruckerName[gef].getText());
        rpVariablen.fuelleVariable(rpDrucken, "Funktion", "");

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        lDbBundle.closeAll();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Drucker " + Integer.toString(gef) + " Arbeitsplatz "
                + Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz) + " getestet.");

    }

    /**
     * Clicked speichern auto drucker.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichernAutoDrucker(ActionEvent event) {
        for (int i = 1; i <= 100; i++) {
            ParamS.paramServer.autoDrucker[i] = CaString.trunc(tfDruckerName[i].getText().trim(), 110);
        }

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbParameter.updateServer_all();
        lDbBundle.closeAll();

        eigeneStage.hide();
    }

    /**************************Auto-Druckausgabe**********************************************/

    private String liefereFunktionsbezeichnung(int pI) {
        return KonstAutoDrucker.liefereTextFuerFunktion(pI);
    }

    /**
     * Refresh pane auto druckausgabe.
     */
    private void refresh_pane_autoDruckausgabe() {

        tfDruckerNummer = new TextField[101];
        btnTestenFunktion = new Button[101];

        gridPnAutoDruckausgabe = new GridPane();
        gridPnAutoDruckausgabe.setVgap(5);
        gridPnAutoDruckausgabe.setHgap(15);
        gridPnAutoDruckausgabe.setGridLinesVisible(true);

        Label lbu0 = new Label();
        lbu0.setText("Nr");
        gridPnAutoDruckausgabe.add(lbu0, 0, 0);

        Label lbu1 = new Label();
        lbu1.setText("Funktion");
        gridPnAutoDruckausgabe.add(lbu1, 1, 0);

        Label lbu2 = new Label();
        lbu2.setText("Ausgabe-Druckernr");
        gridPnAutoDruckausgabe.add(lbu2, 2, 0);

        for (int i = 1; i <= 100; i++) {
            Label lNr = new Label();
            lNr.setText(Integer.toString(i));
            gridPnAutoDruckausgabe.add(lNr, 0, i);

            Label lFunktion = new Label();
            lFunktion.setText(liefereFunktionsbezeichnung(i));
            gridPnAutoDruckausgabe.add(lFunktion, 1, i);

            tfDruckerNummer[i] = new TextField();
            tfDruckerNummer[i].setText(Integer.toString(ParamS.param.paramBasis.autoDruckerVerwendung[i]));
            gridPnAutoDruckausgabe.add(tfDruckerNummer[i], 2, i);

            btnTestenFunktion[i] = new Button();
            btnTestenFunktion[i].setText("Drucker Testen");
            btnTestenFunktion[i].setOnAction(e -> {
                clickedTestenFunktion(e);
            });
            gridPnAutoDruckausgabe.add(btnTestenFunktion[i], 3, i);

        }

        scrpnAutoDruckausgabe.setContent(gridPnAutoDruckausgabe);

    }

    /**
     * Clicked testen funktion.
     *
     * @param event the event
     */
    @FXML
    void clickedTestenFunktion(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        int gef = -1;
        for (int i = 1; i < btnTestenFunktion.length; i++) {
            if (event.getSource() == btnTestenFunktion[i]) {
                gef = i;
            }
        }

        String hString = tfDruckerNummer[gef].getText().trim();
        int autoDruckerNummer = 0;
        if (CaString.isNummernNegativ(hString)) {
            autoDruckerNummer = Integer.parseInt(hString);
            CaBug.druckeLog("gef=" + gef + " " + hString, logDrucken, 10);
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.setzeAutoDrucker(autoDruckerNummer);

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.testDruckerzuordnung("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Testart", "Funktionszuordnung");
        rpVariablen.fuelleVariable(rpDrucken, "Arbeitsplatznummer",
                Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz));
        rpVariablen.fuelleVariable(rpDrucken, "Druckernummer", Integer.toString(autoDruckerNummer));
        rpVariablen.fuelleVariable(rpDrucken, "Druckerbezeichnung",
                lDbBundle.paramServer.autoDrucker[autoDruckerNummer]);
        rpVariablen.fuelleVariable(rpDrucken, "Funktion", liefereFunktionsbezeichnung(gef));

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        lDbBundle.closeAll();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Drucker " + Integer.toString(autoDruckerNummer) + " Arbeitsplatz "
                + Integer.toString(lDbBundle.clGlobalVar.arbeitsplatz) + " getestet.");

    }

    /**
     * Clicked speichern auto druckausgabe.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichernAutoDruckausgabe(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 3);
        for (int i = 1; i <= 100; i++) {
            String hString = tfDruckerNummer[i].getText().trim();
            int hNummer = 0;
            if (CaString.isNummernNegativ(hString)) {
                hNummer = Integer.parseInt(hString);
                CaBug.druckeLog("i=" + i + " " + hNummer, logDrucken, 10);
            }
            ParamS.param.paramBasis.autoDruckerVerwendung[i] = hNummer;
        }
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        } else {

        }
        eigeneStage.hide();
    }

    /****************************************Stapel-Auswertung***************************************************/
    @SuppressWarnings("unchecked")
    private void refresh_pane() {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbKonfigAuswertung.readAll();
        lKonfigAuswertungArray = lDbBundle.dbKonfigAuswertung.ergebnisArray;
        lDbBundle.closeAll();

        gridPn = new GridPane();
        gridPn.setVgap(5);
        gridPn.setHgap(15);
        gridPn.setGridLinesVisible(true);

        Label lbu0 = new Label();
        lbu0.setText("Ident");
        gridPn.add(lbu0, 0, 0);
        Label lbu1 = new Label();
        lbu1.setText("Funktion / Nr. / Pos");
        gridPn.add(lbu1, 1, 0);
        Label lbu2 = new Label();
        lbu2.setText("ausgelöste Funktion");
        gridPn.add(lbu2, 2, 0);
        Label lbu3 = new Label();
        lbu3.setText("Form Nr");
        gridPn.add(lbu3, 3, 0);
        Label lbu4 = new Label();
        lbu4.setText("Sortierung / Gattung");
        gridPn.add(lbu4, 4, 0);
        Label lbu5 = new Label();
        lbu5.setText("Ausgabeweg / PDF-Name");
        gridPn.add(lbu5, 5, 0);
        Label lbu6 = new Label();
        lbu6.setText("Text 1 / Text 2");
        gridPn.add(lbu6, 6, 0);

        if (lKonfigAuswertungArray != null && lKonfigAuswertungArray.length > 0) {
            int anzahl = lKonfigAuswertungArray.length;
            cbFuerFunktion = new ComboBox[anzahl];
            tfNr = new TextField[anzahl];
            tfPositionInLauf = new TextField[anzahl];
            cbAusgeloesteFunktion = new ComboBox[anzahl];
            tfAusgeloesteFormNr = new TextField[anzahl];
            cbSortierung = new ComboBox[anzahl];
            cbGattung = new ComboBox[anzahl];

            cbAusgabeWeg = new ComboBox[anzahl];
            tfDateinamePdf = new TextField[anzahl];
            tfTtextFuerFormular1 = new TextField[anzahl];
            tfTtextFuerFormular2 = new TextField[anzahl];

            for (int i = 0; i < anzahl; i++) {
                EclKonfigAuswertung lKonfigAuswertung = lKonfigAuswertungArray[i];

                VBox vbox0 = new VBox();

                Label lb0a = new Label();
                lb0a.setText(Integer.toString(lKonfigAuswertung.ident));
                vbox0.getChildren().add(lb0a);
                gridPn.add(vbox0, 0, i + 1);

                VBox vbox1 = new VBox();

                cbFuerFunktion[i] = new ComboBox<String>();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungArt.anzahl; i1++) {
                    cbFuerFunktion[i].getItems().addAll(KonstKonfigAuswertungArt.getText(i1));
                    if (i1 == lKonfigAuswertung.fuerFunktion) {
                        cbFuerFunktion[i].setValue(KonstKonfigAuswertungArt.getText(i1));
                    }
                }
                vbox1.getChildren().add(cbFuerFunktion[i]);

                tfNr[i] = new TextField();
                tfNr[i].setText(Integer.toString(lKonfigAuswertung.nr));
                vbox1.getChildren().add(tfNr[i]);

                tfPositionInLauf[i] = new TextField();
                tfPositionInLauf[i].setText(Integer.toString(lKonfigAuswertung.positionInLauf));
                vbox1.getChildren().add(tfPositionInLauf[i]);

                gridPn.add(vbox1, 1, i + 1);

                VBox vbox2 = new VBox();

                cbAusgeloesteFunktion[i] = new ComboBox<String>();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungFunktion.anzahl; i1++) {
                    cbAusgeloesteFunktion[i].getItems().addAll(KonstKonfigAuswertungFunktion.getText(i1));
                    if (i1 == lKonfigAuswertung.ausgeloesteFunktion) {
                        cbAusgeloesteFunktion[i].setValue(KonstKonfigAuswertungFunktion.getText(i1));
                    }
                }
                vbox2.getChildren().add(cbAusgeloesteFunktion[i]);

                gridPn.add(vbox2, 2, i + 1);

                VBox vbox3 = new VBox();

                tfAusgeloesteFormNr[i] = new TextField();
                tfAusgeloesteFormNr[i].setText(Integer.toString(lKonfigAuswertung.ausgeloesteFormNr));
                vbox3.getChildren().add(tfAusgeloesteFormNr[i]);

                gridPn.add(vbox3, 3, i + 1);

                VBox vbox4 = new VBox();

                cbSortierung[i] = new ComboBox<String>();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungSortierung.anzahl; i1++) {
                    cbSortierung[i].getItems().addAll(KonstKonfigAuswertungSortierung.getText(i1));
                    if (i1 == lKonfigAuswertung.sortierung) {
                        cbSortierung[i].setValue(KonstKonfigAuswertungSortierung.getText(i1));
                    }
                }
                vbox4.getChildren().add(cbSortierung[i]);

                cbGattung[i] = new ComboBox<String>();
                for (int i1 = 0; i1 <= 5; i1++) {
                    cbGattung[i].getItems().addAll(Integer.toString(i1));
                    if (i1 == lKonfigAuswertung.gattung) {
                        cbGattung[i].setValue(Integer.toString(i1));
                    }
                }
                vbox4.getChildren().add(cbGattung[i]);

                gridPn.add(vbox4, 4, i + 1);

                VBox vbox5 = new VBox();

                cbAusgabeWeg[i] = new ComboBox<String>();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungAusgabeWeg.anzahl; i1++) {
                    if (!KonstKonfigAuswertungAusgabeWeg.getText(i1).isEmpty()) {
                        cbAusgabeWeg[i].getItems().addAll(KonstKonfigAuswertungAusgabeWeg.getText(i1));
                        if (i1 == lKonfigAuswertung.ausgabeWeg) {
                            cbAusgabeWeg[i].setValue(KonstKonfigAuswertungAusgabeWeg.getText(i1));
                        }
                    }
                }
                vbox5.getChildren().add(cbAusgabeWeg[i]);

                tfDateinamePdf[i] = new TextField();
                tfDateinamePdf[i].setText(lKonfigAuswertung.dateinamePdf);
                vbox5.getChildren().add(tfDateinamePdf[i]);

                gridPn.add(vbox5, 5, i + 1);

                VBox vbox6 = new VBox();

                tfTtextFuerFormular1[i] = new TextField();
                tfTtextFuerFormular1[i].setText(lKonfigAuswertung.textFuerFormular1);
                vbox6.getChildren().add(tfTtextFuerFormular1[i]);

                tfTtextFuerFormular2[i] = new TextField();
                tfTtextFuerFormular2[i].setText(lKonfigAuswertung.textFuerFormular2);
                vbox6.getChildren().add(tfTtextFuerFormular2[i]);

                gridPn.add(vbox6, 6, i + 1);

            }
        }

        scrPn.setContent(gridPn);

    }

    /**
     * Speichern.
     */
    private void speichern() {
        if (lKonfigAuswertungArray != null && lKonfigAuswertungArray.length > 0) {
            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAll();

            int anzahl = lKonfigAuswertungArray.length;

            for (int i = 0; i < anzahl; i++) {
                EclKonfigAuswertung lKonfigAuswertung = lKonfigAuswertungArray[i];

                String hText = "";

                hText = cbFuerFunktion[i].getValue();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungArt.anzahl; i1++) {
                    if (hText.equals(KonstKonfigAuswertungArt.getText(i1))) {
                        lKonfigAuswertung.fuerFunktion = i1;
                    }
                }

                hText = tfNr[i].getText();
                if (!CaString.isNummern(hText)) {
                    hText = "0";
                }
                lKonfigAuswertung.nr = Integer.parseInt(hText);

                hText = tfPositionInLauf[i].getText();
                if (!CaString.isNummern(hText)) {
                    hText = "0";
                }
                lKonfigAuswertung.positionInLauf = Integer.parseInt(hText);

                hText = cbAusgeloesteFunktion[i].getValue();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungFunktion.anzahl; i1++) {
                    if (hText.equals(KonstKonfigAuswertungFunktion.getText(i1))) {
                        lKonfigAuswertung.ausgeloesteFunktion = i1;
                    }
                }

                hText = tfAusgeloesteFormNr[i].getText();
                if (!CaString.isNummern(hText)) {
                    hText = "0";
                }
                lKonfigAuswertung.ausgeloesteFormNr = Integer.parseInt(hText);

                hText = cbSortierung[i].getValue();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungSortierung.anzahl; i1++) {
                    if (hText.equals(KonstKonfigAuswertungSortierung.getText(i1))) {
                        lKonfigAuswertung.sortierung = i1;
                    }
                }

                hText = cbGattung[i].getValue();
                for (int i1 = 0; i1 <= 5; i1++) {
                    if (hText.equals(Integer.toString(i1))) {
                        lKonfigAuswertung.gattung = i1;
                    }
                }

                hText = cbAusgabeWeg[i].getValue();
                for (int i1 = 0; i1 <= KonstKonfigAuswertungAusgabeWeg.anzahl; i1++) {
                    if (hText.equals(KonstKonfigAuswertungAusgabeWeg.getText(i1))) {
                        lKonfigAuswertung.ausgabeWeg = i1;
                    }
                }

                hText = tfDateinamePdf[i].getText();
                hText = CaString.trunc(hText, 190);
                lKonfigAuswertung.dateinamePdf = hText;

                hText = tfTtextFuerFormular1[i].getText();
                hText = CaString.trunc(hText, 190);
                lKonfigAuswertung.textFuerFormular1 = hText;

                hText = tfTtextFuerFormular2[i].getText();
                hText = CaString.trunc(hText, 190);
                lKonfigAuswertung.textFuerFormular2 = hText;

                lDbBundle.dbKonfigAuswertung.update(lKonfigAuswertung);
            }

            lDbBundle.closeAll();

        }

    }

    /**
     * Clicked neuer eintrag.
     *
     * @param event the event
     */
    @FXML
    void clickedNeuerEintrag(ActionEvent event) {
        EclKonfigAuswertung lKonfigAuswertung = new EclKonfigAuswertung();

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbKonfigAuswertung.insert(lKonfigAuswertung);
        lDbBundle.closeAll();

        refresh_pane();
    }

    /**
     * Clicked refresh.
     *
     * @param event the event
     */
    @FXML
    void clickedRefresh(ActionEvent event) {
        speichern();
        refresh_pane();
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        speichern();
        eigeneStage.hide();
    }

    /**********************************Übergreifend*******************************/
    public void init() {

        /*Öffnen, um Parameter (Global) ggf. neu einzulesen*/
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.closeAll();

        /** HV-Parameter sicherheitshalber neu holen */
        lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        stubParameter.leseEmittent(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.eclEmittent = stubParameter.rcEmittent;
    }

}
