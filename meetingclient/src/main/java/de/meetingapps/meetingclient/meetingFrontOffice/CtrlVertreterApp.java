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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlVertreterApp.
 */
public class CtrlVertreterApp {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scrpn vertreter. */
    @FXML
    private ScrollPane scrpnVertreter;

    /** The btn vollmacht speichern. */
    @FXML
    private Button btnVollmachtSpeichern;

    /** The btn letzte vollmacht. */
    @FXML
    private Button btnLetzteVollmacht;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The lbl name. */
    @FXML
    private Label lblName;

    /** The lbl vorname. */
    @FXML
    private Label lblVorname;

    /** The lbl ort. */
    @FXML
    private Label lblOrt;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The lbl fehler meldung. */
    @FXML
    private Label lblFehlerMeldung;

    /** The grpn vertreter. */
    private GridPane grpnVertreter = null;

    /** The btn vollmacht dritte. */
    private Button btnVollmachtDritte[] = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert scrpnVertreter != null : "fx:id=\"scrpnVertreter\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert btnVollmachtSpeichern != null : "fx:id=\"btnVollmachtSpeichern\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert btnLetzteVollmacht != null : "fx:id=\"btnLetzteVollmacht\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert lblName != null : "fx:id=\"lblName\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert lblVorname != null : "fx:id=\"lblVorname\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert lblOrt != null : "fx:id=\"lblOrt\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert tfVorname != null : "fx:id=\"tfVorname\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'VertreterApp.fxml'.";
        assert lblFehlerMeldung != null : "fx:id=\"lblFehlerMeldung\" was not injected: check your FXML file 'VertreterApp.fxml'.";

        if (!letzterVertreterMoeglich) {
            btnLetzteVollmacht.setVisible(false);
        }
        vertreterNameLabel();
        vertreterVornameLabel();
        zeigeVollmachtenDritteScroll();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vertreterVornameRequestFocus();
            }
        });
    }

    /**
     * On btn vollmacht speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnVollmachtSpeichern(ActionEvent event) {
        if (vertreterNameGet().isEmpty()) {
            lblFehlerMeldung.setText("Bitte Vertretername eingeben!");
            return;
        }
        if (vertreterVornameNameGet().isEmpty()) {
            lblFehlerMeldung.setText("Bitte Vertretervorname eingeben!");
            return;
        }
        if (tfOrt.getText().isEmpty()) {
            lblFehlerMeldung.setText("Bitte Vertreterort!");
            return;
        }

        vertreterIdent = 0;
        vertreterName = vertreterNameGet();
        vertreterVorname = vertreterVornameNameGet();
        vertreterOrt = tfOrt.getText();
        abbruch = false;
        eigeneStage.hide();
    }

    /**
     * On btn abbruch.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbruch(ActionEvent event) {
        abbruch = true;
        eigeneStage.hide();
    }

    /**
     * On btn letzte vollmacht.
     *
     * @param event the event
     */
    @FXML
    void onBtnLetzteVollmacht(ActionEvent event) {
        abbruch = false;
        eigeneStage.hide();
    }

    /**
     * Clicked btn vollmacht dritte.
     *
     * @param event the event
     */
    void clickedBtnVollmachtDritte(ActionEvent event) {
        int lfdVertreter = -1;
        for (int i = 0; i < btnVollmachtDritte.length; i++) {
            if (event.getSource() == btnVollmachtDritte[i]) {
                lfdVertreter = i;
            }
        }

        if (lfdVertreter != -1) {
            vertreterName = vertreterliste[lfdVertreter].bevollmaechtigtePerson.name;
            vertreterVorname = vertreterliste[lfdVertreter].bevollmaechtigtePerson.vorname;
            vertreterOrt = vertreterliste[lfdVertreter].bevollmaechtigtePerson.ort;
            vertreterIdent = vertreterliste[lfdVertreter].bevollmaechtigtePerson.ident;
            abbruch = false;
            eigeneStage.hide();
        }

        //		personNatJurVertreter=0;
        //		personNatJurVertreterNeueVollmacht=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterIdent;
        //		vertreterName=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterName;
        //		vertreterVorname=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterVorname;
        //		vertreterOrt=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterOrt;
        //		
        //		linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
    }

    /**
     * Zeige vollmachten dritte scroll.
     */
    private void zeigeVollmachtenDritteScroll() {
        if (vertreterliste == null) {
            return;
        }
        grpnVertreter = new GridPane();
        grpnVertreter.setVgap(5);
        grpnVertreter.setHgap(15);

        int anzVollmachtenDritte = vertreterliste.length;
        btnVollmachtDritte = new Button[anzVollmachtenDritte];
        for (int j = 0; j < anzVollmachtenDritte; j++) {
            EclWillensErklVollmachtenAnDritte vollmachtDritte = vertreterliste[j];
            btnVollmachtDritte[j] = new Button(
                    vollmachtDritte.bevollmaechtigtePerson.name + ", " + vollmachtDritte.bevollmaechtigtePerson.vorname
                            + "; " + vollmachtDritte.bevollmaechtigtePerson.ort);
            btnVollmachtDritte[j].setWrapText(true);
            btnVollmachtDritte[j].setMaxWidth(200);
            btnVollmachtDritte[j].setPrefHeight(51);
            btnVollmachtDritte[j].setOnAction(e -> {
                clickedBtnVollmachtDritte(e);
            });
            grpnVertreter.add(btnVollmachtDritte[j], 0, j + 2);
        }
        scrpnVertreter.setContent(grpnVertreter);

    }

    /**
     * *********Verwaltung der Vertreterfelder**********.
     *
     * @return the string
     */

    private String vertreterNameGet() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            return tfName.getText();
        } else {
            return tfVorname.getText();
        }
    }

    /**
     * Vertreter name label.
     */
    private void vertreterNameLabel() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            lblName.setText("Name");
        } else {
            lblVorname.setText("Name");
        }
    }

    /**
     * Vertreter vorname name get.
     *
     * @return the string
     */
    private String vertreterVornameNameGet() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            return tfVorname.getText();
        } else {
            return tfName.getText();
        }
    }

    /**
     * Vertreter vorname label.
     */
    private void vertreterVornameLabel() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            lblVorname.setText("Vorname");
        } else {
            lblName.setText("Vorname");
        }
    }

    /**
     * Vertreter vorname request focus.
     */
    private void vertreterVornameRequestFocus() {
        //		if (ParamS.param.paramAkkreditierung.positionVertretername==1){
        //			tfVorname.requestFocus();
        //		}
        //		else{
        tfName.requestFocus();
        //		}

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The vertreterliste. */
    /*bereits vorgespeicherte Vollmachten*/
    public EclWillensErklVollmachtenAnDritte[] vertreterliste = null;

    /** The letzter vertreter moeglich. */
    /*=true => letzten Vertreter übernehmen mit anzeigen*/
    public boolean letzterVertreterMoeglich = false;

    /** The vertreter name. */
    public String vertreterName = "";

    /** The vertreter vorname. */
    public String vertreterVorname = "";

    /** The vertreter ort. */
    public String vertreterOrt = "";

    /** The vertreter ident. */
    public int vertreterIdent = 0;

    /** The abbruch. */
    /*Return-Werte*/
    public boolean abbruch = true;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
