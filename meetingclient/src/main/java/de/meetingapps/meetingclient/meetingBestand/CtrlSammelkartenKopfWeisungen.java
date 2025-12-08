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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlSammelkartenSummenKorrektur;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * The Class CtrlSammelkartenKopfWeisungen.
 */
public class CtrlSammelkartenKopfWeisungen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scpn uebersicht. */
    @FXML
    private ScrollPane scpnUebersicht;

    /** The tf sammel ident. */
    @FXML
    private TextField tfSammelIdent;

    /** The btn einlesen. */
    @FXML
    private Button btnEinlesen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The tf sammel name. */
    @FXML
    private TextField tfSammelName;

    /** The tf summe aktien sammelkarte. */
    @FXML
    private TextField tfSummeAktienSammelkarte;

    /** The tf summe aktien einzelaktionaere. */
    @FXML
    private TextField tfSummeAktienEinzelaktionaere;

    /** The btn summe uebernehmen. */
    @FXML
    private Button btnSummeUebernehmen;

    /** The btn alles uebernehmen. */
    @FXML
    private Button btnAllesUebernehmen;

    /** The btn refresh checksummen. */
    @FXML
    private Button btnRefreshChecksummen;

    /** The tf stimmen kopf. */
    private TextField[][] tfStimmenKopf = null;

    /** The tf check summe kopf. */
    private TextField[] tfCheckSummeKopf = null;

    /** The tf stimmen aktionaere. */
    private TextField[][] tfStimmenAktionaere = null;

    /** The tf check summe aktionaere. */
    private TextField[] tfCheckSummeAktionaere = null;

    /** The btn einzel in top uebernehmen. */
    private Button[] btnEinzelInTopUebernehmen = null;

    private CheckBox[] cbNichtBerechnen = null;
    
    /** The grpn uebersicht. */
    private GridPane grpnUebersicht = null;

    /** The db bundle. */
    DbBundle dbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert scpnUebersicht != null
                : "fx:id=\"scpnUebersicht\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert tfSammelIdent != null
                : "fx:id=\"tfSammelIdent\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnEinlesen != null
                : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert tfSammelName != null
                : "fx:id=\"tfSammelName\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert tfSummeAktienSammelkarte != null
                : "fx:id=\"tfSummeAktienSammelkarte\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert tfSummeAktienEinzelaktionaere != null
                : "fx:id=\"tfSummeAktienEinzelaktionaere\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnSummeUebernehmen != null
                : "fx:id=\"btnSummeUebernehmen\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnAllesUebernehmen != null
                : "fx:id=\"btnAllesUebernehmen\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";
        assert btnRefreshChecksummen != null
                : "fx:id=\"btnRefreshChecksummen\" was not injected: check your FXML file 'SammelkartenKopfWeisungen.fxml'.";

        /********** Ab hier individuell *******************/

        dbBundle = new DbBundle();

        einlesen();
    }

    /** The aktuelle sammel ident. */
    private int aktuelleSammelIdent = 0;

    /** The l sammel meldung. */
    private EclMeldung lSammelMeldung = null;

    /** The l sammel weisung meldung. */
    private EclWeisungMeldung lSammelWeisungMeldung = null;

    /** The abstimmungsliste. */
    private EclAbstimmung[] abstimmungsliste = null;

    /** The summe aktionaere gesamt. */
    private long summeAktionaereGesamt = 0;

    /** The summen aktionaere. */
    private long[][] summenAktionaere = null;

    /** The abstimmungsliste gegen. */
    private EclAbstimmung[] abstimmungslisteGegen = null;

    /** The summen aktionaere gegen. */
    private long[][] summenAktionaereGegen = null;

    /**
     * On btn einlesen.
     *
     * @param event the event
     */
    @FXML
    void onBtnEinlesen(ActionEvent event) {
    }

    /**
     * Einlesen.
     */
    private void einlesen() {
        int rc = 0;

        tfSammelIdent.setText(Integer.toString(aktuelleSammelIdent));

        dbBundle = new DbBundle();
        dbBundle.openAll();

        BlSammelkartenSummenKorrektur blSammelkartenSummenKorrektur = new BlSammelkartenSummenKorrektur(dbBundle);

        blSammelkartenSummenKorrektur.einlesenAbstimmungsliste();
        rc = blSammelkartenSummenKorrektur.einlesenEinerSammelkarte(aktuelleSammelIdent);
        if (rc < 1) {
            dbBundle.closeAll();
            fehlerMeldung("Meldung nicht vorhanden!");
            eigeneStage.hide();
            return;
        }
        if (rc == -2) {
            dbBundle.closeAll();
            fehlerMeldung("Meldung ist keine Sammelkarte!");
            eigeneStage.hide();
            return;
        }

        lSammelMeldung = blSammelkartenSummenKorrektur.lSammelMeldung;

        abstimmungsliste = blSammelkartenSummenKorrektur.abstimmungsliste;
        abstimmungslisteGegen = blSammelkartenSummenKorrektur.abstimmungslisteGegen;

        lSammelWeisungMeldung = blSammelkartenSummenKorrektur.lSammelWeisungMeldung;

        summeAktionaereGesamt = blSammelkartenSummenKorrektur.summeAktionaereGesamt;
        summenAktionaere = blSammelkartenSummenKorrektur.summenAktionaere;
        summenAktionaereGegen = blSammelkartenSummenKorrektur.summenAktionaereGegen;

        dbBundle.closeAll();
        zeigeSammelkarte();
    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichern(ActionEvent event) {
        if (pruefeEingaben() == false) {
            return;
        }

        /*Summe*/
        lSammelMeldung.stueckAktien = Long.parseLong(tfSummeAktienSammelkarte.getText());
        lSammelMeldung.stimmen = Long.parseLong(tfSummeAktienSammelkarte.getText());

        /*Weisung*/
        for (int i = 0; i < abstimmungsliste.length; i++) {
            int pos = abstimmungsliste[i].identWeisungssatz;
            if (pos != -1) {
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart] = Long
                                .parseLong(tfStimmenKopf[i][stimmart].getText());
                    }
                }
                if (cbNichtBerechnen[i].isSelected()) {
                    lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]=1;
                }
                else {
                    lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]=0;
                }

            }
        }
        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            if (pos != -1) {
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart] = Long
                                .parseLong(tfStimmenKopf[i + abstimmungsliste.length][stimmart].getText());
                    }
                }

            }
        }

        dbBundle.openAll();
        BlSammelkartenSummenKorrektur blSammelkartenSummenKorrektur = new BlSammelkartenSummenKorrektur(dbBundle);
        blSammelkartenSummenKorrektur.lSammelMeldung = lSammelMeldung;

        blSammelkartenSummenKorrektur.lSammelWeisungMeldung = lSammelWeisungMeldung;

        blSammelkartenSummenKorrektur.zurueckschreibenEinerSammelkarte();

        dbBundle.closeAll();
        eigeneStage.hide();
    }

    /**
     * On btn alles uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAllesUebernehmen(ActionEvent event) {
        if (pruefeEingaben() == false) {
            return;
        }
        uebernehmeSumme();
        for (int i = 0; i < abstimmungsliste.length; i++) {
            int pos = abstimmungsliste[i].identWeisungssatz;
            if (pos != -1) {
                uebernehmeTop(i);
            }
        }
        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            if (pos != -1) {
                uebernehmeGegen(i);
            }
        }
        refreshCheckSummen();
    }

    /**
     * On btn summe uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSummeUebernehmen(ActionEvent event) {
        if (pruefeEingaben() == false) {
            return;
        }
        uebernehmeSumme();
        refreshCheckSummen();
    }

    /**
     * On btn refresh checksummen.
     *
     * @param event the event
     */
    @FXML
    void onBtnRefreshChecksummen(ActionEvent event) {
        if (pruefeEingaben() == false) {
            return;
        }
        refreshCheckSummen();
    }

    /**
     * Clicked einzel top uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void clickedEinzelTopUebernehmen(ActionEvent event) {
        if (pruefeEingaben() == false) {
            return;
        }
        int gef = -1;
        for (int i = 0; i < abstimmungsliste.length; i++) {
            if (event.getSource() == btnEinzelInTopUebernehmen[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            uebernehmeTop(gef);
        }

        gef = -1;
        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            if (event.getSource() == btnEinzelInTopUebernehmen[i]) {
                gef = i;
            }
        }
        if (gef != -1) {
            uebernehmeGegen(gef);
        }

        refreshCheckSummen();
    }

    /**
     * Zeige sammelkarte.
     */
    private void zeigeSammelkarte() {
        tfSammelIdent.setEditable(false);
        tfSammelName.setText(lSammelMeldung.name);
        tfSummeAktienSammelkarte.setText(Long.toString(lSammelMeldung.stueckAktien));
        tfSummeAktienEinzelaktionaere.setText(Long.toString(summeAktionaereGesamt));

        tfStimmenKopf = new TextField[abstimmungsliste.length + abstimmungslisteGegen.length][10];
        tfStimmenAktionaere = new TextField[abstimmungsliste.length + abstimmungslisteGegen.length][10];
        btnEinzelInTopUebernehmen = new Button[abstimmungsliste.length + abstimmungslisteGegen.length];
        cbNichtBerechnen = new CheckBox[abstimmungsliste.length + abstimmungslisteGegen.length];
        tfCheckSummeKopf = new TextField[abstimmungsliste.length + abstimmungslisteGegen.length];
        tfCheckSummeAktionaere = new TextField[abstimmungsliste.length + abstimmungslisteGegen.length];

        grpnUebersicht = new GridPane();
        grpnUebersicht.setVgap(5);
        grpnUebersicht.setHgap(15);

        for (int stimmart = 0; stimmart <= 9; stimmart++) {
            if (stimmart != KonstStimmart.splitLiegtVor) {
                Label luStimmart = new Label();
                luStimmart.setText(KonstStimmart.getText(stimmart));
                grpnUebersicht.add(luStimmart, stimmart + 5, 0);
            }

        }

        for (int i = 0; i < abstimmungsliste.length; i++) {
            Label lTopIndex = new Label();
            lTopIndex.setText(abstimmungsliste[i].nummer + " " + abstimmungsliste[i].nummerindex);
            grpnUebersicht.add(lTopIndex, 0, i * 2 + 1);

            Label lTopBezeichnung = new Label();
            lTopBezeichnung.setText(abstimmungsliste[i].kurzBezeichnung);
            grpnUebersicht.add(lTopBezeichnung, 1, i * 2 + 1);
            int pos = abstimmungsliste[i].identWeisungssatz;
            btnEinzelInTopUebernehmen[i] = new Button();
            cbNichtBerechnen[i] = new CheckBox();
            if (pos != -1) {
                Label lKopf = new Label();
                lKopf.setText("Kopf Sammelkarte");
                grpnUebersicht.add(lKopf, 2, i * 2 + 1);

                Label lSummeEinzelne = new Label();
                lSummeEinzelne.setText("Summe Aktionäre");
                grpnUebersicht.add(lSummeEinzelne, 2, i * 2 + 2);

                btnEinzelInTopUebernehmen[i].setText("Aktionär übernehmen");
                grpnUebersicht.add(btnEinzelInTopUebernehmen[i], 3, i * 2 + 1);
                btnEinzelInTopUebernehmen[i].setOnAction(e -> {
                    clickedEinzelTopUebernehmen(e);
                });

                HBox hBox=new HBox();
                cbNichtBerechnen[i].setSelected(lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]==1);
                hBox.getChildren().add(cbNichtBerechnen[i]);
                Label hLabel=new Label("Nicht berechnen");
                hBox.getChildren().add(hLabel);
                grpnUebersicht.add(hBox, 3, i * 2 + 2);
                /*AAAAA*/
                
                tfCheckSummeKopf[i] = new TextField();
                tfCheckSummeKopf[i].setEditable(false);
                tfCheckSummeKopf[i].setText("");
                grpnUebersicht.add(tfCheckSummeKopf[i], 4, i * 2 + 1);

                tfCheckSummeAktionaere[i] = new TextField();
                tfCheckSummeAktionaere[i].setEditable(false);
                tfCheckSummeAktionaere[i].setText("");
                grpnUebersicht.add(tfCheckSummeAktionaere[i], 4, i * 2 + 2);

                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        tfStimmenKopf[i][stimmart] = new TextField();
                        tfStimmenKopf[i][stimmart].setText(
                                Long.toString(lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart]));
                        grpnUebersicht.add(tfStimmenKopf[i][stimmart], 5 + stimmart, i * 2 + 1);

                        tfStimmenAktionaere[i][stimmart] = new TextField();
                        tfStimmenAktionaere[i][stimmart].setEditable(false);
                        tfStimmenAktionaere[i][stimmart].setText(Long.toString(summenAktionaere[i][stimmart]));
                        grpnUebersicht.add(tfStimmenAktionaere[i][stimmart], 5 + stimmart, i * 2 + 2);
                    }
                }

            }

        }

        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            Label lTopIndex = new Label();
            lTopIndex.setText(abstimmungslisteGegen[i].nummer + " " + abstimmungslisteGegen[i].nummerindex);
            grpnUebersicht.add(lTopIndex, 0, i * 2 + 1 + (abstimmungsliste.length * 2));

            Label lTopBezeichnung = new Label();
            lTopBezeichnung.setText(abstimmungslisteGegen[i].kurzBezeichnung);
            grpnUebersicht.add(lTopBezeichnung, 1, i * 2 + 1 + (abstimmungsliste.length * 2));
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            btnEinzelInTopUebernehmen[i + abstimmungsliste.length] = new Button();
            if (pos != -1) {
                Label lKopf = new Label();
                lKopf.setText("Kopf Sammelkarte");
                grpnUebersicht.add(lKopf, 2, i * 2 + 1 + (abstimmungsliste.length * 2));

                Label lSummeEinzelne = new Label();
                lSummeEinzelne.setText("Summe Aktionäre");
                grpnUebersicht.add(lSummeEinzelne, 2, i * 2 + 2 + (abstimmungsliste.length * 2));

                btnEinzelInTopUebernehmen[i + abstimmungsliste.length].setText("Aktionär übernehmen");
                grpnUebersicht.add(btnEinzelInTopUebernehmen[i + abstimmungsliste.length], 3,
                        i * 2 + 1 + (abstimmungsliste.length * 2));
                btnEinzelInTopUebernehmen[i + abstimmungsliste.length].setOnAction(e -> {
                    clickedEinzelTopUebernehmen(e);
                });

                HBox hBox=new HBox();
                cbNichtBerechnen[i + abstimmungsliste.length].setSelected(lSammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[pos]==1);
                hBox.getChildren().add(cbNichtBerechnen[i + abstimmungsliste.length]);
                Label hLabel=new Label("*Nicht berechnen");
                hBox.getChildren().add(hLabel);
                grpnUebersicht.add(hBox, 3, i * 2 + 2+ (abstimmungsliste.length * 2));
                /*AAAAA*/

                tfCheckSummeKopf[i + abstimmungsliste.length] = new TextField();
                tfCheckSummeKopf[i + abstimmungsliste.length].setEditable(false);
                tfCheckSummeKopf[i + abstimmungsliste.length].setText("");
                grpnUebersicht.add(tfCheckSummeKopf[i + abstimmungsliste.length], 4,
                        i * 2 + 1 + (2 * abstimmungsliste.length));

                tfCheckSummeAktionaere[i + abstimmungsliste.length] = new TextField();
                tfCheckSummeAktionaere[i + abstimmungsliste.length].setEditable(false);
                tfCheckSummeAktionaere[i + abstimmungsliste.length].setText("");
                grpnUebersicht.add(tfCheckSummeAktionaere[i + abstimmungsliste.length], 4,
                        i * 2 + 2 + (abstimmungsliste.length * 2));

                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        tfStimmenKopf[i + abstimmungsliste.length][stimmart] = new TextField();
                        tfStimmenKopf[i + abstimmungsliste.length][stimmart].setText(
                                Long.toString(lSammelWeisungMeldung.weisungMeldungSplit.abgabe[pos][stimmart]));
                        grpnUebersicht.add(tfStimmenKopf[i + abstimmungsliste.length][stimmart], 5 + stimmart,
                                i * 2 + 1 + (abstimmungsliste.length * 2));

                        tfStimmenAktionaere[i + abstimmungsliste.length][stimmart] = new TextField();
                        tfStimmenAktionaere[i + abstimmungsliste.length][stimmart].setEditable(false);
                        tfStimmenAktionaere[i + abstimmungsliste.length][stimmart]
                                .setText(Long.toString(summenAktionaereGegen[i][stimmart]));
                        grpnUebersicht.add(tfStimmenAktionaere[i + abstimmungsliste.length][stimmart], 5 + stimmart,
                                i * 2 + 2 + (abstimmungsliste.length * 2));
                    }
                }

            }

        }

        refreshCheckSummen();

        scpnUebersicht.setContent(grpnUebersicht);

        btnEinlesen.setVisible(false);
        btnAbbrechen.setVisible(true);
        btnSpeichern.setVisible(true);
        btnSummeUebernehmen.setVisible(true);
        btnAllesUebernehmen.setVisible(true);

    }

    /**
     * Refresh die Quersummen je TOP.
     */
    private void refreshCheckSummen() {
        for (int i = 0; i < abstimmungsliste.length; i++) {
            int pos = abstimmungsliste[i].identWeisungssatz;
            if (pos != -1) {
                long quersummeKopf = 0;
                long quersummeAktionaere = 0;
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        quersummeKopf += Long.parseLong(tfStimmenKopf[i][stimmart].getText());
                        quersummeAktionaere += Long.parseLong(tfStimmenAktionaere[i][stimmart].getText());
                    }
                }
                tfCheckSummeKopf[i].setText(Long.toString(quersummeKopf));
                tfCheckSummeAktionaere[i].setText(Long.toString(quersummeAktionaere));
            }

        }

        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            if (pos != -1) {
                long quersummeKopf = 0;
                long quersummeAktionaere = 0;
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        quersummeKopf += Long.parseLong(tfStimmenKopf[i + abstimmungsliste.length][stimmart].getText());
                        quersummeAktionaere += Long
                                .parseLong(tfStimmenAktionaere[i + abstimmungsliste.length][stimmart].getText());
                    }
                }
                tfCheckSummeKopf[i + abstimmungsliste.length].setText(Long.toString(quersummeKopf));
                tfCheckSummeAktionaere[i + abstimmungsliste.length].setText(Long.toString(quersummeAktionaere));
            }

        }

    }

    /**
     * Pruefe eingaben.
     *
     * @return true, if successful
     */
    private boolean pruefeEingaben() {
        for (int i = 0; i < abstimmungsliste.length; i++) {
            int pos = abstimmungsliste[i].identWeisungssatz;
            if (pos != -1) {
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        if (!CaString.isNummern(tfStimmenKopf[i][stimmart].getText())) {
                            this.fehlerMeldung(
                                    "Wert bei " + abstimmungsliste[i].nummer + " " + abstimmungsliste[i].nummerindex
                                            + " " + KonstStimmart.getText(stimmart) + " enthält ungültige Zeichen");
                            return false;
                        }
                    }
                }
            }

        }

        for (int i = 0; i < abstimmungslisteGegen.length; i++) {
            int pos = abstimmungslisteGegen[i].identWeisungssatz;
            if (pos != -1) {
                for (int stimmart = 0; stimmart <= 9; stimmart++) {
                    if (stimmart != KonstStimmart.splitLiegtVor) {
                        if (!CaString.isNummern(tfStimmenKopf[i + abstimmungsliste.length][stimmart].getText())) {
                            this.fehlerMeldung(
                                    "Wert bei " + abstimmungslisteGegen[i].nummer + " "
                                            + abstimmungslisteGegen[i].nummerindex
                                            + " " + KonstStimmart.getText(stimmart) + " enthält ungültige Zeichen");
                            return false;
                        }
                    }
                }
            }

        }

        return true;

    }

    /**
     * pOffset bezogen auf abstimmungsliste.
     *
     * @param pOffset the offset
     */
    private void uebernehmeTop(int pOffset) {
        for (int stimmart = 0; stimmart <= 9; stimmart++) {
            if (stimmart != KonstStimmart.splitLiegtVor) {
                tfStimmenKopf[pOffset][stimmart].setText(tfStimmenAktionaere[pOffset][stimmart].getText());
            }
        }

    }

    /**
     * pOffset bezogen auf abstimmungslisteGegen.
     *
     * @param pOffset the offset
     */
    private void uebernehmeGegen(int pOffset) {
        for (int stimmart = 0; stimmart <= 9; stimmart++) {
            if (stimmart != KonstStimmart.splitLiegtVor) {
                tfStimmenKopf[pOffset + abstimmungsliste.length][stimmart]
                        .setText(tfStimmenAktionaere[pOffset + abstimmungsliste.length][stimmart].getText());
            }
        }

    }

    /**
     * Uebernehme summe.
     */
    private void uebernehmeSumme() {
        tfSummeAktienSammelkarte.setText(tfSummeAktienEinzelaktionaere.getText());
    }

    /**
     * MeldeIdent=ausgewählte Sammelkarte.
     *
     * @param meldeIdent the melde ident
     */
    public void init(int meldeIdent) {
        aktuelleSammelIdent = meldeIdent;
    }

}
