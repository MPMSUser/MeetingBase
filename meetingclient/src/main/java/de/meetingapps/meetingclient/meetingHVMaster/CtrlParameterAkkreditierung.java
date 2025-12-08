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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterAkkreditierung.
 */
public class CtrlParameterAkkreditierung extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane annzeige. */
    @FXML
    private TabPane tbPaneAnnzeige;

    /** The tp allgemein. */
    @FXML
    private Tab tpAllgemein;

    /** The cb eintrittskarte wird auch fuer abgang wiederzugang verwendet. */
    @FXML
    private CheckBox cbEintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet;

    /** The cb eintrittskarte wird stimmkarte. */
    @FXML
    private CheckBox cbEintrittskarteWirdStimmkarte;

    /** The tf position vertretername. */
    @FXML
    private TextField tfPositionVertretername;

    /** The tf delay art. */
    @FXML
    private TextField tfDelayArt;

    /** The cb label auch fuer app ident. */
    @FXML
    private CheckBox cbLabelAuchFuerAppIdent;

    /** The tf formular nach erstzugang. */
    @FXML
    private TextField tfFormularNachErstzugang;

    /** The tf zusaetzliches initialpasswort bei erstzugang. */
    @FXML
    private TextField tfZusaetzlichesInitialpasswortBeiErstzugang;

    /** The cb gaeste karten haben wiederzugang abgangs code. */
    @FXML
    private CheckBox cbGaesteKartenHabenWiederzugangAbgangsCode;

    /** The tf bei zugang stornieren aus sammel automatisch. */
    @FXML
    private TextField tfBeiZugangStornierenAusSammelAutomatisch;

    /** The tf bei zugang selbst vertreter ignorieren zulaessig. */
    @FXML
    private TextField tfBeiZugangSelbstVertreterIgnorierenZulaessig;

    /** The tf ungepruefte karten nicht buchen. */
    @FXML
    private TextField tfUngepruefteKartenNichtBuchen;

    /** The tf service desk set nr. */
    @FXML
    private TextField tfServiceDeskSetNr;

    /** The tf auszugebende stimmkarten in meldung anzeigen. */
    @FXML
    private TextField tfAuszugebendeStimmkartenInMeldungAnzeigen;

    /** The cb sk offenlegung SRV. */
    @FXML
    private CheckBox cbSkOffenlegungSRV;

    /** The cb sk offenlegung briefwahl. */
    @FXML
    private CheckBox cbSkOffenlegungBriefwahl;

    /** The cb sk offenlegung KIAV. */
    @FXML
    private CheckBox cbSkOffenlegungKIAV;

    /** The cb sk offenlegung dauer. */
    @FXML
    private CheckBox cbSkOffenlegungDauer;

    /** The cb sk offenlegung orga. */
    @FXML
    private CheckBox cbSkOffenlegungOrga;

    /** The tp stimmblockzuordnung. */
    @FXML
    private Tab tpStimmblockzuordnung;

    /** The gp stimmblockzuordnung. */
    @FXML
    private GridPane gpStimmblockzuordnung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The lbl fehlermeldung. */
    @FXML
    private Label lblFehlermeldung;

    /** *************Ab hier individuelle Deklarationen******************. */

    private CheckBox[][] cbPPraesenzStimmkarteAktiv = null;

    /** The cb P praesenz stimmkarten zuordnen gattung. */
    private CheckBox[][] cbPPraesenzStimmkartenZuordnenGattung = null;

    /** The cb P praesenz stimmkarten zuordnen app gattung. */
    private CheckBox[][] cbPPraesenzStimmkartenZuordnenAppGattung = null;

    /** The tf P praesenz stimmkarten zuordnen gattung text. */
    private TextField[][] tfPPraesenzStimmkartenZuordnenGattungText = null;

    /** The cb P praesenz stimmkarte tausch beliebig. */
    private CheckBox[][] cbPPraesenzStimmkarteTauschBeliebig = null;

    /** The cb P praesenz stimmkarte nachdruck service desk. */
    private CheckBox[][] cbPPraesenzStimmkarteNachdruckServiceDesk = null;

    /** The tf P praesenz stimmkarte formularnummer. */
    private TextField[][] tfPPraesenzStimmkarteFormularnummer = null;

    /** The tf P praesenz stimmkarte drucker bei erstzugang. */
    private TextField[][] tfPPraesenzStimmkarteDruckerBeiErstzugang = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert tpAllgemein != null : "fx:id=\"tpAllgemein\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert cbEintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet != null : "fx:id=\"cbEintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert cbEintrittskarteWirdStimmkarte != null : "fx:id=\"cbEintrittskarteWirdStimmkarte\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert tfPositionVertretername != null : "fx:id=\"tfPositionVertretername\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert tfDelayArt != null : "fx:id=\"tfDelayArt\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert cbLabelAuchFuerAppIdent != null : "fx:id=\"cbLabelAuchFuerAppIdent\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert cbGaesteKartenHabenWiederzugangAbgangsCode != null : "fx:id=\"cbGaesteKartenHabenWiederzugangAbgangsCode\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert tpStimmblockzuordnung != null : "fx:id=\"tpStimmblockzuordnung\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert gpStimmblockzuordnung != null : "fx:id=\"gpStimmblockzuordnung\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        assert lblFehlermeldung != null : "fx:id=\"lblFehlermeldung\" was not injected: check your FXML file 'ParameterAkkreditierung.fxml'.";
        
        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
            }
        });

        /****** Allgemein ********/
        cbEintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet
                .setSelected(ParamS.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet);
        cbEintrittskarteWirdStimmkarte.setSelected(ParamS.param.paramAkkreditierung.eintrittskarteWirdStimmkarte);

        tfPositionVertretername.setText(Integer.toString(ParamS.param.paramAkkreditierung.positionVertretername));
        tfDelayArt.setText(Integer.toString(ParamS.param.paramAkkreditierung.delayArt));

        cbLabelAuchFuerAppIdent.setSelected(ParamS.param.paramAkkreditierung.labelAuchFuerAppIdent);

        tfFormularNachErstzugang.setText(Integer.toString(ParamS.param.paramAkkreditierung.formularNachErstzugang));
        tfZusaetzlichesInitialpasswortBeiErstzugang
                .setText(Integer.toString(ParamS.param.paramAkkreditierung.zusaetzlichesInitialpasswortBeiErstzugang));

        cbGaesteKartenHabenWiederzugangAbgangsCode
                .setSelected(ParamS.param.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode);

        tfBeiZugangStornierenAusSammelAutomatisch
                .setText(Integer.toString(ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch));

        tfBeiZugangSelbstVertreterIgnorierenZulaessig.setText(
                Integer.toString(ParamS.param.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig));
        tfUngepruefteKartenNichtBuchen
                .setText(Integer.toString(ParamS.param.paramAkkreditierung.ungepruefteKartenNichtBuchen));
        tfServiceDeskSetNr.setText(Integer.toString(ParamS.param.paramAkkreditierung.serviceDeskSetNr));
        tfAuszugebendeStimmkartenInMeldungAnzeigen
                .setText(Integer.toString(ParamS.param.paramAkkreditierung.auszugebendeStimmkartenInMeldungAnzeigen));

        /********************* Stimmblockzuordnung **********************/

        cbPPraesenzStimmkarteAktiv = new CheckBox[5][5];
        cbPPraesenzStimmkartenZuordnenGattung = new CheckBox[5][5];
        cbPPraesenzStimmkartenZuordnenAppGattung = new CheckBox[5][5];
        tfPPraesenzStimmkartenZuordnenGattungText = new TextField[5][5];

        cbPPraesenzStimmkarteTauschBeliebig = new CheckBox[5][5];
        cbPPraesenzStimmkarteNachdruckServiceDesk = new CheckBox[5][5];
        tfPPraesenzStimmkarteFormularnummer = new TextField[5][5];
        tfPPraesenzStimmkarteDruckerBeiErstzugang = new TextField[5][5];

        for (int i = 1; i <= 5; i++) { /*Für alle Gattungen*/

            if (ParamS.param.paramBasis.getGattungAktiv(i)) {

                Label lGattungBez = new Label();
                lGattungBez.setText(ParamS.param.paramBasis.getGattungBezeichnung(i));
                gpStimmblockzuordnung.add(lGattungBez, i, 0);

                Label lGattungBezKurz = new Label();
                lGattungBezKurz.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(i));
                gpStimmblockzuordnung.add(lGattungBezKurz, i, 1);
            }

            for (int i1 = 0; i1 < 5; i1++) { //Für alle Zuordnungen
                cbPPraesenzStimmkarteAktiv[i - 1][i1] = new CheckBox();
                cbPPraesenzStimmkartenZuordnenGattung[i - 1][i1] = new CheckBox();
                cbPPraesenzStimmkartenZuordnenAppGattung[i - 1][i1] = new CheckBox();
                tfPPraesenzStimmkartenZuordnenGattungText[i - 1][i1] = new TextField();
                cbPPraesenzStimmkarteTauschBeliebig[i - 1][i1] = new CheckBox();
                cbPPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1] = new CheckBox();
                tfPPraesenzStimmkarteFormularnummer[i - 1][i1] = new TextField();
                tfPPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1] = new TextField();

                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[i - 1][i1] == 1) {
                    cbPPraesenzStimmkarteAktiv[i - 1][i1].setSelected(true);
                }
                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i - 1][i1] == 1) {
                    cbPPraesenzStimmkartenZuordnenGattung[i - 1][i1].setSelected(true);
                }
                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i - 1][i1] == 1) {
                    cbPPraesenzStimmkartenZuordnenAppGattung[i - 1][i1].setSelected(true);
                }
                tfPPraesenzStimmkartenZuordnenGattungText[i - 1][i1]
                        .setText(ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[i - 1][i1]);

                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteTauschBeliebig[i - 1][i1] == 1) {
                    cbPPraesenzStimmkarteTauschBeliebig[i - 1][i1].setSelected(true);
                }
                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1] == 1) {
                    cbPPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1].setSelected(true);
                }
                tfPPraesenzStimmkarteFormularnummer[i - 1][i1]
                        .setText(ParamS.param.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[i - 1][i1]);
                tfPPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1]
                        .setText(ParamS.param.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1]);

                if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                    VBox lVBox = new VBox();
                    lVBox.getChildren().add(cbPPraesenzStimmkarteAktiv[i - 1][i1]);
                    lVBox.getChildren().add(cbPPraesenzStimmkartenZuordnenGattung[i - 1][i1]);
                    lVBox.getChildren().add(cbPPraesenzStimmkartenZuordnenAppGattung[i - 1][i1]);
                    if (i1 < 4) {
                        lVBox.getChildren().add(tfPPraesenzStimmkartenZuordnenGattungText[i - 1][i1]);

                        lVBox.getChildren().add(tfPPraesenzStimmkarteFormularnummer[i - 1][i1]);
                        lVBox.getChildren().add(cbPPraesenzStimmkarteTauschBeliebig[i - 1][i1]);
                        lVBox.getChildren().add(cbPPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1]);
                        lVBox.getChildren().add(tfPPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1]);
                    }
                    gpStimmblockzuordnung.add(lVBox, i, i1 + 2);
                }
            }
        }

        if (ParamS.param.paramAkkreditierung.skOffenlegungSRV == 1) {
            cbSkOffenlegungSRV.setSelected(true);
        }
        cbSkOffenlegungBriefwahl.setSelected(true);
        if (ParamS.param.paramAkkreditierung.skOffenlegungKIAV == 1) {
            cbSkOffenlegungKIAV.setSelected(true);
        }
        if (ParamS.param.paramAkkreditierung.skOffenlegungDauer == 1) {
            cbSkOffenlegungDauer.setSelected(true);
        }
        if (ParamS.param.paramAkkreditierung.skOffenlegungOrga == 1) {
            cbSkOffenlegungOrga.setSelected(true);
        }

    }

    /************************Logik***************************************************/
    private boolean speichernParameter() {

        /**************** Prüfen ***********************/
        String lFehlertext = "";
        pruefe12(tfPositionVertretername, "Position Vertretername");
        pruefe123(tfDelayArt, "Delay Art");
        pruefe01(tfFormularNachErstzugang, "Formular bei Zugang");
        pruefe01(tfDelayArt, "Delay Art");
        pruefe012(tfZusaetzlichesInitialpasswortBeiErstzugang, "neues Initialpasswort bei Zugang");
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 0; i1 < 5; i1++) {
                pruefeLaenge(tfPPraesenzStimmkartenZuordnenGattungText[i - 1][i1], "Bezeichnung", 40);
            }
        }
        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return false;
        }

        /****** Allgemein ********/
        ParamS.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet = cbEintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet
                .isSelected();
        ParamS.param.paramAkkreditierung.eintrittskarteWirdStimmkarte = cbEintrittskarteWirdStimmkarte.isSelected();

        ParamS.param.paramAkkreditierung.positionVertretername = Integer.parseInt(tfPositionVertretername.getText());
        ParamS.param.paramAkkreditierung.delayArt = Integer.parseInt(tfDelayArt.getText());

        ParamS.param.paramAkkreditierung.labelAuchFuerAppIdent = cbLabelAuchFuerAppIdent.isSelected();

        ParamS.param.paramAkkreditierung.formularNachErstzugang = Integer.parseInt(tfFormularNachErstzugang.getText());
        ParamS.param.paramAkkreditierung.zusaetzlichesInitialpasswortBeiErstzugang = Integer
                .parseInt(tfZusaetzlichesInitialpasswortBeiErstzugang.getText());

        ParamS.param.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode = cbGaesteKartenHabenWiederzugangAbgangsCode
                .isSelected();

        ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch = Integer
                .parseInt(tfBeiZugangStornierenAusSammelAutomatisch.getText());

        ParamS.param.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig = Integer
                .parseInt(tfBeiZugangSelbstVertreterIgnorierenZulaessig.getText());
        ParamS.param.paramAkkreditierung.ungepruefteKartenNichtBuchen = Integer
                .parseInt(tfUngepruefteKartenNichtBuchen.getText());
        ParamS.param.paramAkkreditierung.serviceDeskSetNr = Integer.parseInt(tfServiceDeskSetNr.getText());
        ParamS.param.paramAkkreditierung.auszugebendeStimmkartenInMeldungAnzeigen = Integer
                .parseInt(tfAuszugebendeStimmkartenInMeldungAnzeigen.getText());

        /********************* Stimmblockzuordnung **********************/

        for (int i = 1; i <= 5; i++) { /*Für alle Gattungen*/
            for (int i1 = 0; i1 < 5; i1++) { //Für alle Zuordnungen
                if (cbPPraesenzStimmkarteAktiv[i - 1][i1].isSelected()) {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[i - 1][i1] = 1;
                } else {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[i - 1][i1] = 0;
                }
                if (cbPPraesenzStimmkartenZuordnenGattung[i - 1][i1].isSelected()) {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i - 1][i1] = 1;
                } else {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i - 1][i1] = 0;
                }

                if (cbPPraesenzStimmkartenZuordnenAppGattung[i - 1][i1].isSelected()) {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i - 1][i1] = 1;
                } else {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i - 1][i1] = 0;
                }

                ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[i
                        - 1][i1] = tfPPraesenzStimmkartenZuordnenGattungText[i - 1][i1].getText();

                if (cbPPraesenzStimmkarteTauschBeliebig[i - 1][i1].isSelected()) {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteTauschBeliebig[i - 1][i1] = 1;
                } else {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteTauschBeliebig[i - 1][i1] = 0;
                }

                if (cbPPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1].isSelected()) {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1] = 1;
                } else {
                    ParamS.param.paramAkkreditierung.pPraesenzStimmkarteNachdruckServiceDesk[i - 1][i1] = 0;
                }

                ParamS.param.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[i
                        - 1][i1] = tfPPraesenzStimmkarteFormularnummer[i - 1][i1].getText();

                String hDruckernummer = tfPPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1].getText().trim();
                if (hDruckernummer.length() > 2) {
                    hDruckernummer = "";
                }
                if (!hDruckernummer.isEmpty()) {
                    if (hDruckernummer.length() < 2) {
                        hDruckernummer = "0" + hDruckernummer;
                    }
                }
                ParamS.param.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[i - 1][i1] = hDruckernummer;

            }

        }

        /*************** Offenlegung *************************/
        ParamS.param.paramAkkreditierung.skOffenlegungSRV = 0;
        if (cbSkOffenlegungSRV.isSelected()) {
            ParamS.param.paramAkkreditierung.skOffenlegungSRV = 1;
        }

        ParamS.param.paramAkkreditierung.skOffenlegungKIAV = 0;
        if (cbSkOffenlegungKIAV.isSelected()) {
            ParamS.param.paramAkkreditierung.skOffenlegungKIAV = 1;
        }

        ParamS.param.paramAkkreditierung.skOffenlegungDauer = 0;
        if (cbSkOffenlegungDauer.isSelected()) {
            ParamS.param.paramAkkreditierung.skOffenlegungDauer = 1;
        }

        ParamS.param.paramAkkreditierung.skOffenlegungOrga = 0;
        if (cbSkOffenlegungOrga.isSelected()) {
            ParamS.param.paramAkkreditierung.skOffenlegungOrga = 1;
        }

        /************ Speichern ******/

        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        }

        return true;
    }
    
    /**************************Anzeigefunktionen***************************************/

    public void init() {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;
    }

    /********************Aktionen auf Oberfläche*************************/
    @FXML
    void clickedSpeichern(ActionEvent event) {

        boolean rc = speichernParameter();

        if (rc == false) {
            return;
        }

        eigeneStage.hide();

    }

    /**
     * On tp changed.
     *
     * @param event the event
     */
    @FXML
    void onTpChanged(MouseEvent event) {

    }

}
