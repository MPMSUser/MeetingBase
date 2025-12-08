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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrDrucklaufAuswaehlen.
 */
public class CtrDrucklaufAuswaehlen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scrpn suchen. */
    @FXML
    private ScrollPane scrpnSuchen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn uebernehmen. */
    @FXML
    private Button btnUebernehmen;

    /** The cb alle anzeigen. */
    @FXML
    private CheckBox cbAlleAnzeigen;

    /** The tg. */
    ToggleGroup tg;

    /** The rb. */
    RadioButton[] rb;

    /** The grpn ergebnis. */
    GridPane grpnErgebnis = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert scrpnSuchen != null
                : "fx:id=\"scrpnSuchen\" was not injected: check your FXML file 'SuchenDrucklauf.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SuchenDrucklauf.fxml'.";
        assert btnUebernehmen != null
                : "fx:id=\"btnUebernehmen\" was not injected: check your FXML file 'SuchenDrucklauf.fxml'.";
        assert cbAlleAnzeigen != null
                : "fx:id=\"cbAlleAnzeigen\" was not injected: check your FXML file 'SuchenDrucklauf.fxml'.";

        if (verarbeitungslaufArt != 1) {
            cbAlleAnzeigen.setVisible(false);
        }
        cbAlleAnzeigen.setSelected(false);

        cbAlleAnzeigen.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected,
                    Boolean isNowSelected) {
                if (isNowSelected) {
                    // ...
                } else {
                    // ...
                }
                anzeigenListe();
            }
        });

        anzeigenListe();
    }

    /**
     * Anzeigen liste.
     */
    private void anzeigenListe() {
        grpnErgebnis = new GridPane();
        grpnErgebnis.setVgap(5);
        grpnErgebnis.setHgap(15);

        if (drucklaufListe == null || drucklaufListe.length == 0) {
            Label hLabelFehler = new Label("Keine Druckläufe vorhanden!");
            grpnErgebnis.add(hLabelFehler, 0, 0);
            scrpnSuchen.setContent(grpnErgebnis);
            return;
        }

        tg = new ToggleGroup();
        int zeile = 0;
        rb = new RadioButton[drucklaufListe.length];

        for (int i = 0; i < drucklaufListe.length; i++) {

            rb[i] = new RadioButton();
            if (cbAlleAnzeigen.isSelected() == true || drucklaufListe[i].gedruckt == 1 || verarbeitungslaufArt != 1) {
                rb[i].setToggleGroup(tg);
                grpnErgebnis.add(rb[i], 0, zeile);
                Label hLabel1 = new Label();
                hLabel1.setText(Integer.toString(drucklaufListe[i].drucklaufNr));
                grpnErgebnis.add(hLabel1, 1, zeile);
                Label hLabel2 = new Label();
                hLabel2.setText(drucklaufListe[i].erzeugtAm);
                grpnErgebnis.add(hLabel2, 2, zeile);

                if (verarbeitungslaufArt == 1) {
                    Label hLabel3 = new Label();
                    if (drucklaufListe[i].nurGepruefteVersandadressen == 1) {
                        hLabel3.setText("nur geprüfte");
                    } else {
                        hLabel3.setText("alle");
                    }
                    grpnErgebnis.add(hLabel3, 3, zeile);

                    Label hLabel4 = new Label();
                    switch (drucklaufListe[i].landSelektion) {
                    case 0: {
                        hLabel4.setText("alle");
                        break;
                    }
                    case 1: {
                        hLabel4.setText("nur Inland");
                        break;
                    }
                    case 2: {
                        hLabel4.setText("nur Ausland");
                        break;
                    }
                    }
                    grpnErgebnis.add(hLabel4, 4, zeile);

                    Label hLabel5 = new Label();
                    switch (drucklaufListe[i].wegSelektion) {
                    case 0: {
                        hLabel5.setText("alle");
                        break;
                    }
                    case 1: {
                        hLabel5.setText("nur Portal");
                        break;
                    }
                    case 2: {
                        hLabel5.setText("nur Anmeldestelle");
                        break;
                    }
                    }
                    grpnErgebnis.add(hLabel5, 5, zeile);

                    Label hLabel6 = new Label();
                    switch (drucklaufListe[i].gaesteOderAktionaereSelektion) {
                    case 1: {
                        hLabel6.setText("Gäste");
                        break;
                    }
                    case 2: {
                        hLabel6.setText("Aktionäre");
                        break;
                    }
                    }
                    grpnErgebnis.add(hLabel6, 6, zeile);

                    Label hLabel7 = new Label();
                    hLabel7.setText("Anzahl=" + Integer.toString(drucklaufListe[i].anzahlSaetze));
                    grpnErgebnis.add(hLabel7, 7, zeile);

                    Label hLabel8 = new Label();
                    hLabel8.setText("Von=" + drucklaufListe[i].ersterAktionaer);
                    grpnErgebnis.add(hLabel8, 8, zeile);

                    Label hLabel9 = new Label();
                    hLabel9.setText("Bis=" + drucklaufListe[i].letzterAktionaer);
                    grpnErgebnis.add(hLabel9, 9, zeile);

                    Label hLabel10 = new Label();
                    switch (drucklaufListe[i].gedruckt) {
                    case 0: {
                        hLabel10.setText("nicht gedruckt");
                        break;
                    }
                    case 1: {
                        hLabel10.setText("gedruckt");
                        break;
                    }
                    }
                    grpnErgebnis.add(hLabel10, 10, zeile);
                }
                if (verarbeitungslaufArt != 1) {
                    Label hLabel7 = new Label();
                    hLabel7.setText("Anzahl=" + Integer.toString(drucklaufListe[i].anzahlSaetze));
                    grpnErgebnis.add(hLabel7, 3, zeile);
                }

                zeile++;
            }

        }
        scrpnSuchen.setContent(grpnErgebnis);

    }

    /**
     * Clicked uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void clickedUebernehmen(ActionEvent event) {

        int gef = -1;
        for (int i = 0; i < drucklaufListe.length; i++) {
            if (rb[i].isSelected()) {
                gef = i;
            }
        }
        if (gef != -1) {
            verarbeitungslaufNr = Integer.toString(drucklaufListe[gef].drucklaufNr);
            eigeneStage.hide();
        } else {
            verarbeitungslaufNr = "";
        }

    }

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        verarbeitungslaufNr = "";
        eigeneStage.hide();
    }

    /** The verarbeitungslauf nr. */
    public String verarbeitungslaufNr = "";

    /** 1=Eintrittskartendruck; 2=Kontrolliste Weisungen. */
    public int verarbeitungslaufArt = 1;

    /** The drucklauf liste. */
    public EclDrucklauf[] drucklaufListe = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
        verarbeitungslaufNr = "";

    }

}
