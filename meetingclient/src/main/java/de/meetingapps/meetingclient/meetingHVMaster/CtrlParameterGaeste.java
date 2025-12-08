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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterGaeste.
 */
public class CtrlParameterGaeste extends CtrlRoot {

    //    private int logDrucken = 3;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane annzeige. */
    @FXML
    private TabPane tbPaneAnnzeige;

    /** The tp login. */
    @FXML
    private Tab tpLogin;

    /** The tf mail verschicken GK. */
    @FXML
    private TextField tfMailVerschickenGK;

    /** The tf bcc adresse 1. */
    @FXML
    private TextField tfBccAdresse1;

    /** The tf bcc adresse 2. */
    @FXML
    private TextField tfBccAdresse2;

    /** The tf button speichern anzeigen. */
    @FXML
    private TextField tfButtonSpeichernAnzeigen;

    /** The tf button speichern drucken anzeigen. */
    @FXML
    private TextField tfButtonSpeichernDruckenAnzeigen;

    /** The tp allgemein. */
    @FXML
    private Tab tpAllgemein;

    /** The tf feld vorname verwenden. */
    @FXML
    private TextField tfFeldVornameVerwendenPortal;

    /** The tf feld zusatz 1 verwenden. */
    @FXML
    private TextField tfFeldZusatz1VerwendenPortal;

    /** The tf feld zusatz 2 verwenden. */
    @FXML
    private TextField tfFeldZusatz2VerwendenPortal;

    /** The tf feld land verwenden. */
    @FXML
    private TextField tfFeldLandVerwendenPortal;

    /** The tf feld zu haenden verwenden. */
    @FXML
    private TextField tfFeldZuHaendenVerwendenPortal;

    /** The tf feld ort verwenden. */
    @FXML
    private TextField tfFeldOrtVerwendenPortal;

    /** The tf feld mailadresse verwenden. */
    @FXML
    private TextField tfFeldMailadresseVerwendenPortal;



    /** The tf feld anrede verwenden. */
    @FXML
    private TextField tfFeldAnredeVerwendenPortal;

    /** The tf feld titel verwenden. */
    @FXML
    private TextField tfFeldTitelVerwendenPortal;

    /** The tf feld name verwenden. */
    @FXML
    private TextField tfFeldNameVerwendenPortal;

    /** The tf feld adelstitel verwenden. */
    @FXML
    private TextField tfFeldAdelstitelVerwendenPortal;

 
    /** The tf feld kommunikationssprache verwenden. */
    @FXML
    private TextField tfFeldKommunikationsspracheVerwendenPortal;

    /** The tf feld PLZ verwenden. */
    @FXML
    private TextField tfFeldPLZVerwendenPortal;

    /** The tf feld strasse verwenden. */
    @FXML
    private TextField tfFeldStrasseVerwendenPortal;

    /** The tf feld vorname verwenden. */
    @FXML
    private TextField tfFeldVornameVerwenden;

    /** The tf feld zusatz 1 verwenden. */
    @FXML
    private TextField tfFeldZusatz1Verwenden;

    /** The tf feld zusatz 2 verwenden. */
    @FXML
    private TextField tfFeldZusatz2Verwenden;

    /** The tf feld land verwenden. */
    @FXML
    private TextField tfFeldLandVerwenden;

    /** The tf feld zu haenden verwenden. */
    @FXML
    private TextField tfFeldZuHaendenVerwenden;

    /** The tf feld ort verwenden. */
    @FXML
    private TextField tfFeldOrtVerwenden;

    /** The tf feld mailadresse verwenden. */
    @FXML
    private TextField tfFeldMailadresseVerwenden;

    /** The tf feld gruppe verwenden. */
    @FXML
    private TextField tfFeldGruppeVerwenden;

    /** The tf feld vip verwenden. */
    @FXML
    private TextField tfFeldVipVerwenden;

    /** The tf feld zu haenden bezeichnung. */
    @FXML
    private TextField tfFeldZuHaendenBezeichnung;

    /** The tf feld anrede verwenden. */
    @FXML
    private TextField tfFeldAnredeVerwenden;

    /** The tf feld titel verwenden. */
    @FXML
    private TextField tfFeldTitelVerwenden;

    /** The tf feld name verwenden. */
    @FXML
    private TextField tfFeldNameVerwenden;

    /** The tf feld zusatz 1 bezeichnung. */
    @FXML
    private TextField tfFeldZusatz1Bezeichnung;

    /** The tf feld adelstitel verwenden. */
    @FXML
    private TextField tfFeldAdelstitelVerwenden;

    /** The tf feld ausstellungsgrund verwenden. */
    @FXML
    private TextField tfFeldAusstellungsgrundVerwenden;

    /** The tf feld kommunikationssprache verwenden. */
    @FXML
    private TextField tfFeldKommunikationsspracheVerwenden;

    /** The tf feld PLZ verwenden. */
    @FXML
    private TextField tfFeldPLZVerwenden;

    /** The tf feld strasse verwenden. */
    @FXML
    private TextField tfFeldStrasseVerwenden;

    /** The tf feld zusatz 2 bezeichnung. */
    @FXML
    private TextField tfFeldZusatz2Bezeichnung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn beenden ohne speichern. */
    @FXML
    private Button btnBeendenOhneSpeichern;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tpLogin != null : "fx:id=\"tpLogin\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfMailVerschickenGK != null : "fx:id=\"tfMailVerschickenGK\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfBccAdresse1 != null : "fx:id=\"tfBccAdresse1\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfBccAdresse2 != null : "fx:id=\"tfBccAdresse2\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfButtonSpeichernAnzeigen != null : "fx:id=\"tfButtonSpeichernAnzeigen\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfButtonSpeichernDruckenAnzeigen != null : "fx:id=\"tfButtonSpeichernDruckenAnzeigen\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tpAllgemein != null : "fx:id=\"tpAllgemein\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldVornameVerwenden != null : "fx:id=\"tfFeldVornameVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZusatz1Verwenden != null : "fx:id=\"tfFeldZusatz1Verwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZusatz2Verwenden != null : "fx:id=\"tfFeldZusatz2Verwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldLandVerwenden != null : "fx:id=\"tfFeldLandVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZuHaendenVerwenden != null : "fx:id=\"tfFeldZuHaendenVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldOrtVerwenden != null : "fx:id=\"tfFeldOrtVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldMailadresseVerwenden != null : "fx:id=\"tfFeldMailadresseVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldGruppeVerwenden != null : "fx:id=\"tfFeldGruppeVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldVipVerwenden != null : "fx:id=\"tfFeldVipVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZuHaendenBezeichnung != null : "fx:id=\"tfFeldZuHaendenBezeichnung\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldAnredeVerwenden != null : "fx:id=\"tfFeldAnredeVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldTitelVerwenden != null : "fx:id=\"tfFeldTitelVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldNameVerwenden != null : "fx:id=\"tfFeldNameVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZusatz1Bezeichnung != null : "fx:id=\"tfFeldZusatz1Bezeichnung\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldAdelstitelVerwenden != null : "fx:id=\"tfFeldAdelstitelVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldAusstellungsgrundVerwenden != null : "fx:id=\"tfFeldAusstellungsgrundVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldKommunikationsspracheVerwenden != null : "fx:id=\"tfFeldKommunikationsspracheVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldPLZVerwenden != null : "fx:id=\"tfFeldPLZVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldStrasseVerwenden != null : "fx:id=\"tfFeldStrasseVerwenden\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert tfFeldZusatz2Bezeichnung != null : "fx:id=\"tfFeldZusatz2Bezeichnung\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";
        assert btnBeendenOhneSpeichern != null : "fx:id=\"btnBeendenOhneSpeichern\" was not injected: check your FXML file 'ParameterGaeste.fxml'.";

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
            }
        });

        /********************Eingabemaske - allgemeine Parameter**************************************/
        tfMailVerschickenGK.setText(Integer.toString(ParamS.param.paramGaesteModul.mailVerschickenGK));

        tfBccAdresse1.setText(ParamS.param.paramGaesteModul.bccAdresse1);
        tfBccAdresse2.setText(ParamS.param.paramGaesteModul.bccAdresse2);

        tfButtonSpeichernAnzeigen.setText(Integer.toString(ParamS.param.paramGaesteModul.buttonSpeichernAnzeigen));
        tfButtonSpeichernDruckenAnzeigen
                .setText(Integer.toString(ParamS.param.paramGaesteModul.buttonSpeichernDruckenAnzeigen));

        /*************** Felder ********************/
        tfFeldAnredeVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldAnredeVerwenden));
        tfFeldTitelVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldTitelVerwenden));
        tfFeldAdelstitelVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldAdelstitelVerwenden));
        tfFeldNameVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldNameVerwenden));
        tfFeldVornameVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldVornameVerwenden));
        tfFeldZuHaendenVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZuHaendenVerwenden));
        tfFeldZusatz1Verwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZusatz1Verwenden));
        tfFeldZusatz2Verwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZusatz2Verwenden));
        tfFeldStrasseVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldStrasseVerwenden));
        tfFeldLandVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldLandVerwenden));
        tfFeldPLZVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldPLZVerwenden));
        tfFeldOrtVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldOrtVerwenden));
        tfFeldMailadresseVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldMailadresseVerwenden));
        tfFeldKommunikationsspracheVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldKommunikationsspracheVerwenden));
        tfFeldGruppeVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldGruppeVerwenden));
        tfFeldAusstellungsgrundVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldAusstellungsgrundVerwenden));
        tfFeldVipVerwenden.setText(Integer.toString(ParamS.param.paramGaesteModul.feldVipVerwenden));

        tfFeldZuHaendenBezeichnung.setText(ParamS.param.paramGaesteModul.feldZuHaendenBezeichnung);
        tfFeldZusatz1Bezeichnung.setText(ParamS.param.paramGaesteModul.feldZusatz1Bezeichnung);
        tfFeldZusatz2Bezeichnung.setText(ParamS.param.paramGaesteModul.feldZusatz2Bezeichnung);

        tfFeldAnredeVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldAnredeVerwendenPortal));
        tfFeldTitelVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldTitelVerwendenPortal));
        tfFeldAdelstitelVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldAdelstitelVerwendenPortal));
        tfFeldNameVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldNameVerwendenPortal));
        tfFeldVornameVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldVornameVerwendenPortal));
        tfFeldZuHaendenVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZuHaendenVerwendenPortal));
        tfFeldZusatz1VerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZusatz1VerwendenPortal));
        tfFeldZusatz2VerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldZusatz2VerwendenPortal));
        tfFeldStrasseVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldStrasseVerwendenPortal));
        tfFeldLandVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldLandVerwendenPortal));
        tfFeldPLZVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldPLZVerwendenPortal));
        tfFeldOrtVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldOrtVerwendenPortal));
        tfFeldMailadresseVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldMailadresseVerwendenPortal));
        tfFeldKommunikationsspracheVerwendenPortal.setText(Integer.toString(ParamS.param.paramGaesteModul.feldKommunikationsspracheVerwendenPortal));

    }
    
    /************************Logik***************************************************/

    private boolean speichernParameter() {
        lFehlertext = "";

        /*================================Prüfen=============================================*/
        /********************Eingabemaske - allgemeine Parameter**************************************/
        pruefeZahlNichtLeerLaenge(tfMailVerschickenGK, "Mail verschicken", 1);

        pruefeLaenge(tfBccAdresse1, "Bcc Adresse 1", 80);
        pruefeLaenge(tfBccAdresse2, "Bcc Adresse 2", 80);

        pruefe01(tfButtonSpeichernAnzeigen, "Button Speichern anzeigen");
        pruefe01(tfButtonSpeichernDruckenAnzeigen, "Button Speichern Drucken anzeigen");

        pruefe012(tfFeldAnredeVerwenden, "Anrede");
        pruefe012(tfFeldTitelVerwenden, "Titel");
        pruefe012(tfFeldAdelstitelVerwenden, "Adelstitel");
        pruefe012(tfFeldNameVerwenden, "Name");
        pruefe012(tfFeldVornameVerwenden, "Vorname");
        pruefe012(tfFeldZuHaendenVerwenden, "zu Händen");
        pruefe012(tfFeldZusatz1Verwenden, "Zusatz1");
        pruefe012(tfFeldZusatz2Verwenden, "Zusatz2");
        pruefe012(tfFeldStrasseVerwenden, "Strasse");
        pruefe012(tfFeldLandVerwenden, "Land");
        pruefe012(tfFeldPLZVerwenden, "PLZ");
        pruefe012(tfFeldOrtVerwenden, "Ort");
        pruefe012(tfFeldMailadresseVerwenden, "Mailadresse");
        pruefe012(tfFeldKommunikationsspracheVerwenden, "Kommnunikationssprache");
        pruefe012(tfFeldGruppeVerwenden, "Gruppe");
        pruefe012(tfFeldAusstellungsgrundVerwenden, "Ausstellungsgrund");
        pruefe012(tfFeldVipVerwenden, "VIP");

        pruefeLaenge(tfFeldZuHaendenBezeichnung, "Bezeichnung zu Händen", 40);
        pruefeLaenge(tfFeldZusatz1Bezeichnung, "Bezeichnung Zusatz1", 40);
        pruefeLaenge(tfFeldZusatz2Bezeichnung, "Bezeichnung Zusatz2", 40);

        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return false;
        }

        /*============================Speichern===============================*/

        /******************** Allgemein ******************************/
        ParamS.param.paramGaesteModul.mailVerschickenGK = Integer.parseInt(tfMailVerschickenGK.getText());

        ParamS.param.paramGaesteModul.bccAdresse1 = tfBccAdresse1.getText();
        ParamS.param.paramGaesteModul.bccAdresse2 = tfBccAdresse2.getText();

        ParamS.param.paramGaesteModul.buttonSpeichernAnzeigen = Integer.parseInt(tfButtonSpeichernAnzeigen.getText());
        ParamS.param.paramGaesteModul.buttonSpeichernDruckenAnzeigen = Integer
                .parseInt(tfButtonSpeichernDruckenAnzeigen.getText());

        ParamS.param.paramGaesteModul.feldAnredeVerwenden = Integer.parseInt(tfFeldAnredeVerwenden.getText());
        ParamS.param.paramGaesteModul.feldTitelVerwenden = Integer.parseInt(tfFeldTitelVerwenden.getText());
        ParamS.param.paramGaesteModul.feldAdelstitelVerwenden = Integer.parseInt(tfFeldAdelstitelVerwenden.getText());
        ParamS.param.paramGaesteModul.feldNameVerwenden = Integer.parseInt(tfFeldNameVerwenden.getText());
        ParamS.param.paramGaesteModul.feldVornameVerwenden = Integer.parseInt(tfFeldVornameVerwenden.getText());
        ParamS.param.paramGaesteModul.feldZuHaendenVerwenden = Integer.parseInt(tfFeldZuHaendenVerwenden.getText());
        ParamS.param.paramGaesteModul.feldZusatz1Verwenden = Integer.parseInt(tfFeldZusatz1Verwenden.getText());
        ParamS.param.paramGaesteModul.feldZusatz2Verwenden = Integer.parseInt(tfFeldZusatz2Verwenden.getText());
        ParamS.param.paramGaesteModul.feldStrasseVerwenden = Integer.parseInt(tfFeldStrasseVerwenden.getText());
        ParamS.param.paramGaesteModul.feldLandVerwenden = Integer.parseInt(tfFeldLandVerwenden.getText());
        ParamS.param.paramGaesteModul.feldPLZVerwenden = Integer.parseInt(tfFeldPLZVerwenden.getText());
        ParamS.param.paramGaesteModul.feldOrtVerwenden = Integer.parseInt(tfFeldOrtVerwenden.getText());
        ParamS.param.paramGaesteModul.feldMailadresseVerwenden = Integer.parseInt(tfFeldMailadresseVerwenden.getText());
        ParamS.param.paramGaesteModul.feldKommunikationsspracheVerwenden = Integer.parseInt(tfFeldKommunikationsspracheVerwenden.getText());
        ParamS.param.paramGaesteModul.feldGruppeVerwenden = Integer.parseInt(tfFeldGruppeVerwenden.getText());
        ParamS.param.paramGaesteModul.feldAusstellungsgrundVerwenden = Integer.parseInt(tfFeldAusstellungsgrundVerwenden.getText());
        ParamS.param.paramGaesteModul.feldVipVerwenden = Integer.parseInt(tfFeldVipVerwenden.getText());

        ParamS.param.paramGaesteModul.feldAnredeVerwendenPortal = Integer.parseInt(tfFeldAnredeVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldTitelVerwendenPortal = Integer.parseInt(tfFeldTitelVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldAdelstitelVerwendenPortal = Integer.parseInt(tfFeldAdelstitelVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldNameVerwendenPortal = Integer.parseInt(tfFeldNameVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldVornameVerwendenPortal = Integer.parseInt(tfFeldVornameVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldZuHaendenVerwendenPortal = Integer.parseInt(tfFeldZuHaendenVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldZusatz1VerwendenPortal = Integer.parseInt(tfFeldZusatz1VerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldZusatz2VerwendenPortal = Integer.parseInt(tfFeldZusatz2VerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldStrasseVerwendenPortal = Integer.parseInt(tfFeldStrasseVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldLandVerwendenPortal = Integer.parseInt(tfFeldLandVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldPLZVerwendenPortal = Integer.parseInt(tfFeldPLZVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldOrtVerwendenPortal = Integer.parseInt(tfFeldOrtVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldMailadresseVerwendenPortal = Integer.parseInt(tfFeldMailadresseVerwendenPortal.getText());
        ParamS.param.paramGaesteModul.feldKommunikationsspracheVerwendenPortal = Integer.parseInt(tfFeldKommunikationsspracheVerwendenPortal.getText());

        ParamS.param.paramGaesteModul.feldZuHaendenBezeichnung = tfFeldZuHaendenBezeichnung.getText();
        ParamS.param.paramGaesteModul.feldZusatz1Bezeichnung = tfFeldZusatz1Bezeichnung.getText();
        ParamS.param.paramGaesteModul.feldZusatz2Bezeichnung = tfFeldZusatz2Bezeichnung.getText();

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
        lDbBundle.openAllOhneParameterCheck();
        lDbBundle.dbEmittenten.readEmittentHV(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.eclEmittent = lDbBundle.dbEmittenten.ergebnisPosition(0);
        lDbBundle.closeAll();

    }

    /*****************Event-Funktionen*************************/
    
    @FXML
    void clickedSpeichern(ActionEvent event) {

        boolean rc = speichernParameter();
        if (rc == true) {
            eigeneStage.hide();
        }

    }

    /**
     * Clicked beenden ohne speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedBeendenOhneSpeichern(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Achtung, Änderungen werden nicht gespeichert!");

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
