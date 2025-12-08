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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingBestand.SClErfassungsDaten;
import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlEmittentenUebersicht;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginPasswortAendern;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingclient.meetingDesign.DlAufrufDesigner;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComBl.BlPersonenZaehlung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {
    
    private boolean achtungTest() {
        boolean brc=true;
        
        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
        
        brc=caZeigeHinweis.zeige2Buttons(eigeneStage, "Achtung, noch nicht getestet! Verwendung nur duch Eingeweide! Eltern haften für ihre Kinder", "trotzdem benutzen", "Abbruch");
        
        return brc;
    }
    
    
    @FXML
    private AnchorPane rootPane;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The mno datei. */
    @FXML
    private Menu mnoDatei;

    /** The mn emittenten uebersicht. */
    @FXML
    private MenuItem mnEmittentenUebersicht;

    /** The mn modulauswahl. */
    @FXML
    private MenuItem mnModulauswahl;

    /** The mn mandant wechseln. */
    @FXML
    private MenuItem mnMandantWechseln;

    /** The mn beenden. */
    @FXML
    private MenuItem mnBeenden;

    /** The mno HV praesenz. */
    @FXML
    private Menu mnoHVPraesenz;

    /** The mn div aktionaer. */
    @FXML
    private MenuItem mnDivAktionaer;

    /** The mn inaktive weisungen. */
    @FXML
    private MenuItem mnInaktiveWeisungen;

    /** The mn weisungen konsolidieren. */
    @FXML
    private MenuItem mnWeisungenKonsolidieren;

    /** The mn SRV. */
    @FXML
    private MenuItem mnSRV;

    /** The mn praesenzsummen. */
    @FXML
    private MenuItem mnPraesenzsummen;

    /** The mn protokoll. */
    @FXML
    private MenuItem mnProtokoll;

    /** The mn delay. */
    @FXML
    private MenuItem mnDelay;

    /** The mn praesenz feststellen. */
    @FXML
    private MenuItem mnPraesenzFeststellen;

    /** The mn praesenzliste drucken. */
    @FXML
    private MenuItem mnPraesenzlisteDrucken;

    /** The mn elek teilnehmerverzeichnis versorgen. */
    @FXML
    private MenuItem mnElekTeilnehmerverzeichnisVersorgen;

    /** The mn elek teilnehmerverzeichnis anzeigen. */
    @FXML
    private MenuItem mnElekTeilnehmerverzeichnisAnzeigen;

    /** The mn verein versorgen. */
    @FXML
    private MenuItem mnVereinVersorgen;

    /** The mn personen zaehlung. */
    @FXML
    private MenuItem mnPersonenZaehlung;

    /** The mn verzeichnis verein. */
    @FXML
    private MenuItem mnVerzeichnisVerein;

    /** The mn uebersicht praesenz. */
    @FXML
    private MenuItem mnUebersichtPraesenz;

    /** The mno HV abstimmung. */
    @FXML
    private Menu mnoHVAbstimmung;

    /** The mn abstimmung ablauf. */
    @FXML
    private MenuItem mnAbstimmungAblauf;

    /** The mn abstimmung archiv. */
    @FXML
    private MenuItem mnAbstimmungArchiv;

    /** The mn weisungen kopieren. */
    @FXML
    private MenuItem mnWeisungenKopieren;

    /** The mn sammelkarten kopf weisungen. */
    @FXML
    private MenuItem mnSammelkartenKopfWeisungen;

    /** The mn abstimmung eingabe. */
    @FXML
    private MenuItem mnAbstimmungEingabe;

    /** The mn uebersicht SRV. */
    @FXML
    private MenuItem mnUebersichtSRV;

    /** The mn abstimmungsverhalten. */
    @FXML
    private MenuItem mnAbstimmungsverhalten;

    /** The mno konfiguration. */
    @FXML
    private Menu mnoKonfiguration;

    /** The mn user login. */
    @FXML
    private MenuItem mnUserLogin;

    /** The mn user profile. */
    @FXML
    private MenuItem mnUserProfile;

    /** The mn parameter server. */
    @FXML
    private MenuItem mnParameterServer;

    /** The mn geraete set. */
    @FXML
    private MenuItem mnGeraeteSet;

    /** The mn geraete klasse. */
    @FXML
    private MenuItem mnGeraeteKlasse;

    /** The mn geraete klasse HV. */
    @FXML
    private MenuItem mnGeraeteKlasseHV;

    /** The mn geraete zuordnung. */
    @FXML
    private MenuItem mnGeraeteZuordnung;

    /** The mn geraete label printer. */
    @FXML
    private MenuItem mnGeraeteLabelPrinter;

    /** The mn parameter basis. */
    @FXML
    private MenuItem mnParameterBasis;

    @FXML
    private MenuItem mnParameterPresetsAlpha;

    
    /** The mn parameter basis. */
    @FXML
    private MenuItem mnParameterGrundeinstellungen;

    /** The mn parameter basis. */
    @FXML
    private MenuItem mnParameterAuswahl;

    /** The mn parameter basis. */
    @FXML
    private MenuItem mnNeueParameter;

    /** The mn parameter nummernformen. */
    @FXML
    private MenuItem mnParameterPresets;

    /** The mn parameter nummernformen. */
    @FXML
    private MenuItem mnParameterNummernformen;

    /** The mn parameter emittent. */
    @FXML
    private MenuItem mnParameterEmittent;

    /** The mn parameter termine. */
    @FXML
    private MenuItem mnParameterTermine;

    /** The mn parameter portal app. */
    @FXML
    private MenuItem mnParameterPortalApp;

    /** The mn parameter akkreditierung. */
    @FXML
    private MenuItem mnParameterAkkreditierung;

    /** The mn parameter praesenzliste. */
    @FXML
    private MenuItem mnParameterPraesenzliste;

    /** The mn parameter abstimmungen parameter. */
    @FXML
    private MenuItem mnParameterAbstimmungenParameter;

    /** The mn parameter konfig auswertung. */
    @FXML
    private MenuItem mnParameterKonfigAuswertung;

    /** The mn import wortmeldetisch. */
    @FXML
    private MenuItem mnImportWortmeldetisch;

    /** The mn parameter gaeste. */
    @FXML
    private MenuItem mnParameterGaeste;

    /** The mn parameter abstimmungen. */
    @FXML
    private MenuItem mnParameterAbstimmungen;

    /** The mn parameter sonder. */
    @FXML
    private MenuItem mnParameterSonder;

    /** The mn designer. */
    @FXML
    private MenuItem mnDesigner;

    /** The mn parameter set speichern. */
    @FXML
    private MenuItem mnParameterSetSpeichern;

    /** The mno testen. */
    @FXML
    private Menu mnoTesten;

    /** The mn HV zuruecksetzen. */
    @FXML
    private MenuItem mnHVZuruecksetzen;

    /** The mn abstimmung zuruecksetzen. */
    @FXML
    private MenuItem mnAbstimmungZuruecksetzen;

    /** The mn massen zu abgang. */
    @FXML
    private MenuItem mnMassenZuAbgang;

    /** The mn bildschirm aufloesung. */
    @FXML
    private MenuItem mnBildschirmAufloesung;

    /** The mn modul test. */
    @FXML
    private MenuItem mnModulTest;

    /** The mno admin. */
    @FXML
    private Menu mnoAdmin;

    /** The mn admin reorg idents. */
    @FXML
    private MenuItem mnAdminReorgIdents;

    /** The mn admin div. */
    @FXML
    private MenuItem mnAdminDiv;

    /** The mno verwaltung. */
    @FXML
    private Menu mnoVerwaltung;

    /** The mn passwort aendern. */
    @FXML
    private MenuItem mnPasswortAendern;

    /** The mn neuen mandant anlegen. */
    @FXML
    private MenuItem mnNeuenMandantAnlegen;

    @FXML
    private Menu mnoMailing;

    @FXML
    private MenuItem mnMailingParameter;

    @FXML
    private MenuItem mnMailingVersand;

    @FXML
    private Menu mnoSRD;

    @FXML
    private MenuItem mnSRDNotification;

    @FXML
    private MenuItem mnSRDCancallation;

    @FXML
    private MenuItem mnSRDResult;

    /** The lbl mandant. */
    @FXML
    private Label lblMandant;

    /** The lbl consultant. */
    @FXML
    private Label lblConsultant;

    /** The lbl daten status. */
    @FXML
    private Label lblDatenStatus;

    /** The lbl server. */
    @FXML
    private Label lblServer;
    
    @FXML
    private GridPane hvAnsicht;

    @FXML
    private CheckBox cbAnsicht;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        zeigeUser();

        if (ParamS.eclUserLogin.pruefe_hvMaster_praesenzAbstimmung() == false) {
            mnoHVAbstimmung.setVisible(false);
            mnoHVAbstimmung.setDisable(true);
            mnoHVPraesenz.setVisible(false);
            mnoHVPraesenz.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_kennungsPflegeEingeschraenkt() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_kennungsPflegeVollstaendig() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_geraeteParameter() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_nummernformen() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_mandantenParameter() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_pflegeAbstimmungen() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_moduleDesigner() == false) {
            mnoKonfiguration.setVisible(false);
            mnoKonfiguration.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_kennungsPflegeEingeschraenkt() == false
                && ParamS.eclUserLogin.pruefe_hvMaster_kennungsPflegeVollstaendig() == false) {
            mnUserLogin.setVisible(false);
            mnUserLogin.setDisable(true);

            mnUserProfile.setVisible(false);
            mnUserProfile.setDisable(true);

        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_geraeteParameter() == false) {
            mnParameterServer.setVisible(false);
            mnParameterServer.setDisable(true);
            mnGeraeteSet.setVisible(false);
            mnGeraeteSet.setDisable(true);
            mnGeraeteKlasse.setVisible(false);
            mnGeraeteKlasse.setDisable(true);
            mnGeraeteKlasseHV.setVisible(false);
            mnGeraeteKlasseHV.setDisable(true);
            mnGeraeteZuordnung.setVisible(false);
            mnGeraeteZuordnung.setDisable(true);
            mnGeraeteLabelPrinter.setVisible(false);
            mnGeraeteLabelPrinter.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_nummernformen() == false) {
            mnParameterNummernformen.setVisible(false);
            mnParameterNummernformen.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_mandantenParameter() == false) {
            mnParameterBasis.setVisible(false);
            mnParameterBasis.setDisable(true);
            mnParameterPresetsAlpha.setVisible(false);
            mnParameterPresetsAlpha.setDisable(true);
            mnParameterGrundeinstellungen.setVisible(false);
            mnParameterGrundeinstellungen.setDisable(true);
            mnParameterAuswahl.setVisible(false);
            mnParameterAuswahl.setDisable(true);
            
            mnNeueParameter.setVisible(false);
            mnNeueParameter.setDisable(true);
            mnParameterPresets.setVisible(false);
            mnParameterPresets.setDisable(true);
            
            mnParameterEmittent.setVisible(false);
            mnParameterEmittent.setDisable(true);
            mnParameterTermine.setVisible(false);
            mnParameterTermine.setDisable(true);
            mnParameterPortalApp.setVisible(false);
            mnParameterPortalApp.setDisable(true);
            mnParameterAkkreditierung.setVisible(false);
            mnParameterAkkreditierung.setDisable(true);
            mnParameterSonder.setVisible(false);
            mnParameterSonder.setDisable(true);
            mnParameterPraesenzliste.setVisible(false);
            mnParameterPraesenzliste.setDisable(true);
            mnParameterAbstimmungenParameter.setVisible(false);
            mnParameterAbstimmungenParameter.setDisable(true);
            mnParameterKonfigAuswertung.setVisible(false);
            mnParameterKonfigAuswertung.setDisable(true);
            mnParameterGaeste.setVisible(false);
            mnParameterGaeste.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_pflegeAbstimmungen() == false) {
            mnParameterAbstimmungen.setVisible(false);
            mnParameterAbstimmungen.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_moduleDesigner() == false) {
            mnDesigner.setVisible(false);
            mnDesigner.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_hvMaster_entwicklerTools() == false) {
            mnoTesten.setVisible(false);
            mnoTesten.setDisable(true);
            mnoAdmin.setVisible(false);
            mnoAdmin.setDisable(true);
        }

        if (ParamS.eclUserLogin.pruefe_mandantenBearbeiten_mandantenAnlegen() == false) {
            mnNeuenMandantAnlegen.setVisible(false);
            mnNeuenMandantAnlegen.setDisable(true);
        }

        if (ParamS.clGlobalVar.datenbankPfadNr == -1) {
            /*Alle Auswahlpunkte auf False setzen, die nicht nur über Datenbank erreichbar sind*/
            mnMandantWechseln.setVisible(false);
            mnMandantWechseln.setDisable(true);

            mnDivAktionaer.setVisible(false);
            mnDivAktionaer.setDisable(true);
            mnInaktiveWeisungen.setVisible(false);
            mnInaktiveWeisungen.setDisable(true);
            mnWeisungenKonsolidieren.setVisible(false);
            mnWeisungenKonsolidieren.setDisable(true);
            mnSRV.setVisible(false);
            mnSRV.setDisable(true);
            mnPraesenzsummen.setVisible(false);
            mnPraesenzsummen.setDisable(true);
            mnProtokoll.setVisible(false);
            mnProtokoll.setDisable(true);
            mnDelay.setVisible(false);
            mnDelay.setDisable(true);
            mnPraesenzFeststellen.setVisible(false);
            mnPraesenzFeststellen.setDisable(true);
            mnPraesenzlisteDrucken.setVisible(false);
            mnPraesenzlisteDrucken.setDisable(true);
            mnElekTeilnehmerverzeichnisVersorgen.setVisible(false);
            mnElekTeilnehmerverzeichnisVersorgen.setDisable(true);
            mnElekTeilnehmerverzeichnisAnzeigen.setVisible(false);
            mnElekTeilnehmerverzeichnisAnzeigen.setDisable(true);
            mnVereinVersorgen.setVisible(false);
            mnVereinVersorgen.setDisable(true);
            mnPersonenZaehlung.setVisible(false);
            mnPersonenZaehlung.setDisable(true);
            mnVerzeichnisVerein.setVisible(false);
            mnVerzeichnisVerein.setDisable(true);

            mnAbstimmungAblauf.setVisible(false);
            mnAbstimmungAblauf.setDisable(true);
            mnAbstimmungArchiv.setVisible(false);
            mnAbstimmungArchiv.setDisable(true);
            mnWeisungenKopieren.setVisible(false);
            mnWeisungenKopieren.setDisable(true);
            mnAbstimmungEingabe.setVisible(false);
            mnAbstimmungEingabe.setDisable(true);
            mnUebersichtSRV.setVisible(false);
            mnUebersichtSRV.setDisable(true);
            mnAbstimmungsverhalten.setVisible(false);
            mnAbstimmungsverhalten.setDisable(true);
            mnAbstimmungZuruecksetzen.setVisible(false);
            mnAbstimmungZuruecksetzen.setDisable(true);

            mnUserLogin.setVisible(false);
            mnUserLogin.setDisable(true);
            mnUserProfile.setVisible(false);
            mnUserProfile.setDisable(true);

            mnParameterServer.setVisible(false);
            mnParameterServer.setDisable(true);
            mnGeraeteSet.setVisible(false);
            mnGeraeteSet.setDisable(true);
            mnGeraeteKlasse.setVisible(false);
            mnGeraeteKlasse.setDisable(true);
            mnGeraeteKlasseHV.setVisible(false);
            mnGeraeteKlasseHV.setDisable(true);
            mnGeraeteZuordnung.setVisible(false);
            mnGeraeteZuordnung.setDisable(true);
            mnGeraeteLabelPrinter.setVisible(false);
            mnGeraeteLabelPrinter.setDisable(true);
            mnParameterBasis.setVisible(false);
            mnParameterBasis.setDisable(true);
            mnParameterPresetsAlpha.setVisible(false);
            mnParameterPresetsAlpha.setDisable(true);
            mnParameterGrundeinstellungen.setVisible(false);
            mnParameterGrundeinstellungen.setDisable(true);
            mnParameterAuswahl.setVisible(false);
            mnParameterAuswahl.setDisable(true);
            
            mnNeueParameter.setVisible(false);
            mnNeueParameter.setDisable(true);
            mnParameterPresets.setVisible(false);
            mnParameterPresets.setDisable(true);
            
            mnParameterNummernformen.setVisible(false);
            mnParameterNummernformen.setDisable(true);
            mnParameterEmittent.setVisible(false);
            mnParameterEmittent.setDisable(true);
            mnParameterTermine.setVisible(false);
            mnParameterTermine.setDisable(true);
            //            mnParameterPortalApp.setVisible(false);mnParameterPortalApp.setDisable(true);
            mnParameterAkkreditierung.setVisible(false);
            mnParameterAkkreditierung.setDisable(true);
            mnParameterPraesenzliste.setVisible(false);
            mnParameterPraesenzliste.setDisable(true);
            mnParameterAbstimmungenParameter.setVisible(false);
            mnParameterAbstimmungenParameter.setDisable(true);
            mnParameterKonfigAuswertung.setVisible(false);
            mnParameterKonfigAuswertung.setDisable(true);
            mnParameterGaeste.setVisible(false);
            mnParameterGaeste.setDisable(true);
            mnParameterSonder.setVisible(false);
            mnParameterSonder.setDisable(true);
            //            mnParameterAbstimmungen.setVisible(false);mnParameterAbstimmungen.setDisable(true);
            mnDesigner.setVisible(false);
            mnDesigner.setDisable(true);
            mnParameterSetSpeichern.setVisible(false);
            mnParameterSetSpeichern.setDisable(true);

            mnHVZuruecksetzen.setVisible(false);
            mnHVZuruecksetzen.setDisable(true);
            mnMassenZuAbgang.setVisible(false);
            mnMassenZuAbgang.setDisable(true);
            //            mnBildschirmAufloesung.setVisible(false);mnBildschirmAufloesung.setDisable(true);
            //            mnModulTest.setVisible(false);mnModulTest.setDisable(true);

            mnAdminReorgIdents.setVisible(false);
            mnAdminReorgIdents.setDisable(true);
            mnAdminDiv.setVisible(false);
            mnAdminDiv.setDisable(true);

            mnPasswortAendern.setVisible(false);
            mnPasswortAendern.setDisable(true);
            mnNeuenMandantAnlegen.setVisible(false);
            mnNeuenMandantAnlegen.setDisable(true);

        }

        createHvAnsicht();

        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.H) {
                onCbAnsicht(new ActionEvent());
            } else if (e.getCode() == KeyCode.M) {
                gewModulauswahl(new ActionEvent());
            } else if (e.getCode() == KeyCode.W) {
                gewMandantWechseln(new ActionEvent());
            }
        });
        ObjectActions.createInfoButton(rootPane,
                new String[] { "H - HV-Ansicht", "M - Modulauswahl", "W - Mandant Wechseln" }, 30.0, 5.0);
    }

    /**
     * Zeige user.
     */
    private void zeigeUser() {
        String anzeigeText = "Mandant=" + ParamS.clGlobalVar.getMandantString() + " Laufwerk="
                + ParamS.clGlobalVar.lwPfadAllgemein;
        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            anzeigeText += " webServicePfadNr=" + ParamS.clGlobalVar.webServicePfadNr + " webServicePfadzurAuswahl="
                    + ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr];
        }
        if (ParamS.clGlobalVar.datenbankPfadNr != -1) {
            anzeigeText += " Datenbankpfad="
                    + ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamS.clGlobalVar.datenbankPfadNr];
        }
        lblMandant.setText(anzeigeText);

        lblConsultant.setText(ParamS.param.paramBasis.mailConsultant);

        if (ParamS.eclEmittent.liefereDatenbestandIstProduktiv() == false) {
            lblDatenStatus.setText("Achtung - Testdaten");
        } else {
            lblDatenStatus.setText("");
        }

        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            lblServer.setText(ParamInterneKommunikation.webServiceInfo[ParamS.clGlobalVar.webServicePfadNr]);
        } else {
            lblServer
                    .setText(ParamInterneKommunikation.datenbankPfadZurAuswahlText[ParamS.clGlobalVar.datenbankPfadNr]);
        }

    }

    /**
     * Gew emittenten uebersicht.
     *
     * @param event the event
     */
    @FXML
    void gewEmittentenUebersicht(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        CtrlEmittentenUebersicht controllerFenster = new CtrlEmittentenUebersicht();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/EmittentenUebersicht.fxml", 1000, 680,
                "Emittenten Übersicht", true);
    }

    /**
     * Gew modulauswahl.
     *
     * @param event the event
     */
    @FXML
    void gewModulauswahl(ActionEvent event) {
        CaOpenWindow.openModulauswahl(eigeneStage);
    }

    /**
     * Gew mandant wechseln.
     *
     * @param event the event
     */
    @FXML
    void gewMandantWechseln(ActionEvent event) {

        Stage zwischenDialog = new Stage();
        CaIcon.master(zwischenDialog);

        CtrlLoginNeu controllerDialog = new CtrlLoginNeu();
        controllerDialog.nurMandantenwechsel = true;

        controllerDialog.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerDialog,
                "/de/meetingapps/meetingclient/meetingClientDialoge/LoginNeu.fxml", 700, 400, "Mandanten-Auswahl",
                true);

        boolean bRC = controllerDialog.fortsetzen;

        if (bRC == false) {

        } else {
            /* ParamS.clGlobalVar.mandant etc. sind schon vom Controller belegt */
            CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
            int rc = caLeseParameterNeu.leseHVParameter();
            if (rc < 1) { // Fehlerbehandlung
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Systemfehler " + "Fehler " + CaFehler.getFehlertext(rc, 0));
            }

            zeigeUser();
            CaController caEigenerController = new CaController();
            caEigenerController.setzeTitel(eigeneStage, eigenerTitel, eigenerTitelMitMandant);

        }
    }

    /**
     * Gew beenden.
     *
     * @param event the event
     */
    @FXML
    void gewBeenden(ActionEvent event) {
        System.out.println("Ende");
        eigeneStage.hide();
    }

    /**
     * Gew praesenzsummen.
     *
     * @param event the event
     */
    @FXML
    void gewPraesenzsummen(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlPraesenzSummen controllerFenster = new CtrlPraesenzSummen();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/PraesenzSummen.fxml", 1000, 700, "Anzeige Präsenzsummen",
                true);
    }

    /**
     * Gew delay.
     *
     * @param event the event
     */
    @FXML
    void gewDelay(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlPraesenzDelay controllerFenster = new CtrlPraesenzDelay();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PraesenzDelay.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewDelay 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Verwalten Präsenz-Delay");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew protokolle.
     *
     * @param event the event
     */
    @FXML
    void gewProtokolle(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlProtokolleDrucken controllerFenster = new CtrlProtokolleDrucken();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PraesenzProtokolleDrucken.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewProtokolle 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Präsenzprotokolle Drucken");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew praesenz feststellen.
     *
     * @param event the event
     */
    @FXML
    void gewPraesenzFeststellen(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlPraesenzFeststellen controllerFenster = new CtrlPraesenzFeststellen();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/PraesenzFeststellen.fxml", 600, 400,
                "Präsenz feststellen", true);
    }

    /**
     * Gew praesenzliste drucken.
     *
     * @param event the event
     */
    @FXML
    void gewPraesenzlisteDrucken(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlPraesenzlisteDrucken controllerFenster = new CtrlPraesenzlisteDrucken();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/PraesenzlisteDrucken.fxml", 800, 600,
                "Präsenz feststellen", true);

    }

    /**
     * Gew HV zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void gewHVZuruecksetzen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlPraesenzZuruecksetzen controllerFenster = new CtrlPraesenzZuruecksetzen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PraesenzZuruecksetzen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewHVZuruecksetzen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 600);
        neuerDialog.setTitle("Präsenz Zurücksetzen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew elek teilnehmerverzeichnis anzeigen.
     *
     * @param event the event
     */
    @FXML
    void gewElekTeilnehmerverzeichnisAnzeigen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlElekTeilnehmerverzAnzeigen controllerFenster = new CtrlElekTeilnehmerverzAnzeigen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ElekTeilnehmerverzAnzeigen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewElekTeilnehmerverzeichnisAnzeigen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Elektronisches Teilnehmerverzeichnis Anzeigen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew elek teilnehmerverzeichnis versorgen.
     *
     * @param event the event
     */
    @FXML
    void gewElekTeilnehmerverzeichnisVersorgen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlElekTeilnehmerverzVersorgen controllerFenster = new CtrlElekTeilnehmerverzVersorgen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ElekTeilnehmerverzVersorgen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewElekTeilnehmerverzeichnisVersorgen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Elektronisches Teilnehmerverzeichnis Versorgen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew verein versorgen.
     *
     * @param event the event
     */
    @FXML
    void gewVereinVersorgen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlVereinVersorgen controllerFenster = new CtrlVereinVersorgen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VereinVersorgen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewVereinVersorgen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Vereinsstatistik Versorgen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew personen zaehlung.
     *
     * @param event the event
     */
    @FXML
    void gewPersonenZaehlung(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlPersonenZaehlung blPersonenZaehlung = new BlPersonenZaehlung(lDbBundle);
        int anz = blPersonenZaehlung.zaehleTeilnehmerInsgesamt();
        lDbBundle.closeAll();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Aktionäre und Vertreter jemals präsent=" + Integer.toString(anz));
        // Stage neuerDialog=new Stage();
        //
        // CtrlVereinVersorgen controllerFenster=
        // new CtrlVereinVersorgen();
        //
        // controllerFenster.init(neuerDialog);
        //
        // FXMLLoader loader = new FXMLLoader(
        // getClass().getResource(
        // "VereinVersorgen.fxml"
        // )
        // );
        // loader.setController(controllerFenster);
        // Parent mainPane=null;
        // try {
        // mainPane = (Parent) loader.load();
        // } catch (IOException e) {
        // CaBug.drucke("CtrlHauptStage.gewVereinVersorgen 001");
        // e.printStackTrace();
        // }
        // Scene scene=new Scene(mainPane,1200,850);
        // neuerDialog.setTitle("Vereinsstatistik Versorgen");
        // neuerDialog.setScene(scene);
        // neuerDialog.initModality(Modality.APPLICATION_MODAL);
        // neuerDialog.showAndWait();

    }

    /**
     * Gew verzeichnis verein.
     *
     * @param event the event
     */
    @FXML
    void gewVerzeichnisVerein(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlVerzeichnisVerein controllerFenster = new CtrlVerzeichnisVerein();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VerzeichnisVerein.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 700);
        neuerDialog.setTitle("Verzeichnis Verein");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew uebersicht praesenz.
     *
     * @param event the event
     */
    @FXML
    void gewUebersichtPraesenz(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlPraesenzliste controllerFenster = new CtrlPraesenzliste();
        controllerFenster.init(neuerDialog, 0);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/Praesenzliste.fxml", 1400, 700, "Übersicht Präsenz",
                true);
    }

    /**
     * Gew abstimmung ablauf.
     *
     * @param event the event
     */
    @FXML
    void gewAbstimmungAblauf(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlAbstimmungAblauf controllerFenster = new CtrlAbstimmungAblauf();
        controllerFenster.init(neuerDialog, 0);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/AbstimmungAblauf.fxml", 1500, 800, "Abstimmungsablauf",
                true);
    }

    /**
     * Gew abstimmung archiv.
     *
     * @param event the event
     */
    @FXML
    void gewAbstimmungArchiv(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlAbstimmungArchiv controllerFenster = new CtrlAbstimmungArchiv();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/AbstimmungArchiv.fxml", 870, 640, "Abstimmungs-Archiv",
                true);
    }

    /**
     * Gew weisungen kopieren.
     *
     * @param event the event
     */
    @FXML
    void gewWeisungenKopieren(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlWeisungenKopieren controllerFenster = new CtrlWeisungenKopieren();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/WeisungenKopieren.fxml", 870, 640, "Weisungen Kopieren",
                true);
    }

    /**
     * Gew abstimmung eingabe.
     *
     * @param event the event
     */
    @FXML
    void gewAbstimmungEingabe(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlAbstimmungEingabe controllerFenster = new CtrlAbstimmungEingabe();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AbstimmungEingabe.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewAbstimmungEingabe 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Eingabe Abstimmung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew abstimmung zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void gewAbstimmungZuruecksetzen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlAbstimmungZuruecksetzen controllerFenster = new CtrlAbstimmungZuruecksetzen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AbstimmungZuruecksetzen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewAbstimmungZuruecksetzen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 600, 400);
        neuerDialog.setTitle("Abstimmung Zurücksetzen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew abstimmungsverhalten.
     *
     * @param event the event
     */
    @FXML
    void gewAbstimmungsverhalten(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.leseAktivenAbstimmungsblock();
        blAbstimmung.fuelleAbstimmungsverhalten(1, true, true);

        lDbBundle.closeAll();
    }

    /**
     * Gew SRV.
     *
     * @param event the event
     */
    @FXML
    void gewSRV(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlHVVollmachtWeisungSRV controllerFenster = new CtrlHVVollmachtWeisungSRV();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/HVVollmachtWeisungSRV.fxml", 1500, 800,
                "Vollmacht/Weisung beim Verlassen HV erteilen", true);
    }

    /**
     * Gew uebersicht SRV.
     *
     * @param event the event
     */
    @FXML
    void gewUebersichtSRV(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlUebersichtSRV controllerFenster = new CtrlUebersichtSRV();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UebersichtSRV.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewUebersichtSRV 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Übersicht SRV für Bestätigung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew div aktionaer.
     *
     * @param event the event
     */
    @FXML
    void gewDivAktionaer(ActionEvent event) {
        CtrlDivAktionaer ctrl = new CtrlDivAktionaer();
        openController(ctrl, "DivAktionaer.fxml", 1200, 800, "Div Tools für Aktionär");
    }

    /**
     * Gew inaktive weisungen.
     *
     * @param event the event
     */
    @FXML
    void gewInaktiveWeisungen(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbWeisungMeldung.leseInaktive();
        int anz = lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();
        System.out.println("anz=" + anz);
        EclWeisungMeldung[] lWeisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
        for (int i = 0; i < anz; i++) {
            int meldeIdent = lWeisungMeldungArray[i].meldungsIdent;
            lDbBundle.dbWeisungMeldung.leseAktiveZuMeldung(meldeIdent);
            int anzAktive = lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();

            lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
            System.out.println("Meldeident=" + meldeIdent + " anzAktive=" + anzAktive + " Nr="
                    + lDbBundle.dbMeldungen.meldungenArray[0].aktionaersnummer);

        }
        lDbBundle.closeAll();
        System.out.println("Fertig");

    }

    /**
     * Gew weisungen konsolidieren.
     *
     * @param event the event
     */
    @FXML
    void gewWeisungenKonsolidieren(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();

        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Doppelte Sammelkarten bereinigen", "Weiter",
                "Abbruch");
        if (brc == false) {
            return;
        }

        /*Konsistenz Sammelkartenzuordnung*/
        DbBundle pDbBundle = new DbBundle();
        pDbBundle.openAll();
        pDbBundle.dbMeldungZuSammelkarte.leseAlleAktiven();
        int anzSammelkartenzuordnungen = pDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();
        if (anzSammelkartenzuordnungen > 0) {
            EclMeldungZuSammelkarte meldungZuSammelkarteVorherige = null;
            for (int i = 0; i < anzSammelkartenzuordnungen; i++) {
                EclMeldungZuSammelkarte meldungZuSammelkarteAktuell = pDbBundle.dbMeldungZuSammelkarte
                        .meldungZuSammelkarteGefunden(i);
                if (meldungZuSammelkarteVorherige != null) {
                    if (meldungZuSammelkarteVorherige.meldungsIdent == meldungZuSammelkarteAktuell.meldungsIdent) {

                        CaBug.druckeInfo("MeldungsIdent " + meldungZuSammelkarteVorherige.meldungsIdent + " bereinigt");

                        /*Vorherigen Satz,m der in meldungZuSammelkarteVorherige steht, auf inaktiv setzen*/
                        meldungZuSammelkarteVorherige.aktiv = 0;
                        pDbBundle.dbMeldungZuSammelkarte.update(meldungZuSammelkarteVorherige);
                    }
                }
                meldungZuSammelkarteVorherige = meldungZuSammelkarteAktuell;
            }
        }

        pDbBundle.closeAll();
        caZeigeHinweis.zeige(eigeneStage, "Doppelte Sammelkarten wurden bereinigt");

    }

    /**
     * Gew massen zu abgang.
     *
     * @param event the event
     */
    @FXML
    void gewMassenZuAbgang(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlMassenZuAbgang controllerFenster = new CtrlMassenZuAbgang();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MassenZuAbgang.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewMassenZuAbgang 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 850);
        neuerDialog.setTitle("Massen-Zu/Abgang");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew bildschirm aufloesung.
     *
     * @param event the event
     */
    @FXML
    void gewBildschirmAufloesung(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlBildschirmAufloesung controllerFenster = new CtrlBildschirmAufloesung();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BildschirmAufloesung.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewBildschirmAufloesung 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 600, 500);
        neuerDialog.setTitle("Bildschirmauflösung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew modul test.
     *
     * @param event the event
     */
    @FXML
    void gewModulTest(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlModulTest controllerFenster = new CtrlModulTest();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModulTest.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewModulTest 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Modul-Test");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew user login.
     *
     * @param event the event
     */
    @FXML
    void gewUserLogin(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlLoginUser controllerFenster = new CtrlLoginUser();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/LoginUser.fxml", 1440, 800, "Benutzer-Kennungen", true);
    }

    /**
     * Gew user profile.
     *
     * @param event the event
     */
    @FXML
    void gewUserProfile(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlProfileUser controllerFenster = new CtrlProfileUser();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ProfileUser.fxml", 1440, 800, "Profile", true);
    }

    /**
     * Gew parameter server.
     *
     * @param event the event
     */
    @FXML
    void gewParameterServer(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterServer controllerFenster = new CtrlParameterServer();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterServer.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewParameterServer 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Parameter Server-Einstellungen ");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew geraete set.
     *
     * @param event the event
     */
    @FXML
    void gewGeraeteSet(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterGeraeteSets controllerFenster = new CtrlParameterGeraeteSets();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterGeraeteSets.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewGeraeteSet 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Geräte-Sets");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew geraete klasse.
     *
     * @param event the event
     */
    @FXML
    void gewGeraeteKlasse(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterGeraeteKlassen controllerFenster = new CtrlParameterGeraeteKlassen();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterGeraeteKlassen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewGeraeteKlasse 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Parameter Klassen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew geraete klasse HV.
     *
     * @param event the event
     */
    @FXML
    void gewGeraeteKlasseHV(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterGeraeteKlasseHV controllerFenster = new CtrlParameterGeraeteKlasseHV();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterGeraeteKlasseHV.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewGeraeteKlasseHV 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1150, 800);
        neuerDialog.setTitle("Parameter Geräteklassen HV-Shortcuts ");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew geraete zuordnung.
     *
     * @param event the event
     */
    @FXML
    void gewGeraeteZuordnung(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterGeraeteSetKlasse controllerFenster = new CtrlParameterGeraeteSetKlasse();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterGeraeteSetKlasse.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewGeraeteZuordnung 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1150, 800);
        neuerDialog.setTitle("Parameter Geräte-Zuordnung ");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew geraete label printer.
     *
     * @param event the event
     */
    @FXML
    void gewGeraeteLabelPrinter(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CtrlParameterGeraeteLabelPrinter controllerFenster = new CtrlParameterGeraeteLabelPrinter();
        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterGeraeteLabelPrinter.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewGeraeteLabelPrinter 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 800);
        neuerDialog.setTitle("Parameter Geräte-Label-Printer ");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew parameter basis.
     *
     * @param event the event
     */
    @FXML
    void gewParameterBasis(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterBasis controllerFenster = new CtrlParameterBasis();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterBasis.fxml", 1150, 640,
                "Parameter Basiseinstellungen", true);
        zeigeUser();

    }

    @FXML
    void gewParameterPresets(ActionEvent event) {
        boolean brc=achtungTest();
        if (brc==false) {
            return;
        }
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterPresets controllerFenster = new CtrlParameterPresets();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterPresets.fxml", 1500, 760,
                "Parameter Presets", true);
        zeigeUser();

    }

    
    @FXML
    void gewParameterGrundeinstellungen(ActionEvent event) {
        boolean brc=achtungTest();
        if (brc==false) {
            return;
        }
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterGrundeinstellungen controllerFenster = new CtrlParameterGrundeinstellungen();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterGrundeinstellungen.fxml", 1500, 760,
                "Parameter Basiseinstellungen", true);
        zeigeUser();

    }

    @FXML
    void gewParameterAuswahl(ActionEvent event) {
        boolean brc=achtungTest();
        if (brc==false) {
            return;
        }
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterAuswahl controllerFenster = new CtrlParameterAuswahl();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAuswahl.fxml", 1500, 760,
                "Parameter Auswahl", true);
        zeigeUser();

    }

    
    @FXML
    void gewPresetsNeueParameter(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterPresetsGruppe controllerFenster = new CtrlParameterPresetsGruppe();
        controllerFenster.init(neuerDialog, 9999);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterPresetGruppe.fxml", 1500, 760,
                "Neue Parameter", true);
        zeigeUser();

    }

    
    /**
     * Open controller.
     *
     * @param pController the controller
     * @param pFxml       the fxml
     * @param pBreite     the breite
     * @param pHoehe      the hoehe
     * @param pTitel      the titel
     */
    private void openController(CtrlRoot pController, String pFxml, int pBreite, int pHoehe, String pTitel) {

        Stage neuerDialog = new Stage();
        pController.eigeneStage = neuerDialog;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(pFxml));
        loader.setController(pController);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.openController 001 " + pFxml);
            e.printStackTrace();
        }

        Scene scene = new Scene(mainPane, pBreite, pHoehe);

        final String[] mandant = eigeneStage.getTitle().split(" - ");

        neuerDialog.setTitle(pTitel + (mandant.length > 1 ? " - " + mandant[1] : ""));
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew parameter emittent.
     *
     * @param event the event
     */
    @FXML
    void gewParameterEmittent(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterEmittent controllerFenster = new CtrlParameterEmittent();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterEmittent.fxml", 1120, 720, "Parameter Emittent",
                true);
        zeigeUser();
    }

    /**
     * Gew parameter termine.
     *
     * @param event the event
     */
    @FXML
    void gewParameterTermine(ActionEvent event) {

        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterTermine controllerFenster = new CtrlParameterTermine();
        controllerFenster.init();

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterTermine.fxml", 1500, 760, "Termine", true);
    }

    /**
     * Gew parameter akkreditierung.
     *
     * @param event the event
     */
    @FXML
    void gewParameterAkkreditierung(ActionEvent event) {
        CtrlParameterAkkreditierung controllerFenster = new CtrlParameterAkkreditierung();
        controllerFenster.init();
        openController(controllerFenster, "ParameterAkkreditierung.fxml", 1200, 680, "Parameter Akkreditierung");
    }

    /**
     * Gew parameter sonder.
     *
     * @param event the event
     */
    @FXML
    void gewParameterSonder(ActionEvent event) {
        CtrlParameterSonder controllerFenster = new CtrlParameterSonder();
        controllerFenster.init();
        openController(controllerFenster, "ParameterSonder.fxml", 1500, 800, "Parameter Sonderabläufe");
    }

    /**
     * Gew parameter praesenzliste.
     *
     * @param event the event
     */
    @FXML
    void gewParameterPraesenzliste(ActionEvent event) {
        CtrlParameterPraesenzliste controllerFenster = new CtrlParameterPraesenzliste();
        controllerFenster.init();
        openController(controllerFenster, "ParameterPraesenzliste.fxml", 1500, 800, "Parameter Präsenzliste");
    }

    /**
     * Gew parameter portal app.
     *
     * @param event the event
     */
    @FXML
    void gewParameterPortalApp(ActionEvent event) {
        CtrlParameterPortalApp controllerFenster = new CtrlParameterPortalApp();
        controllerFenster.init();
        openController(controllerFenster, "ParameterPortalApp.fxml", 1500, 760, "Parameter Portal App");
    }

    /**
     * Gew parameter konfig auswertung.
     *
     * @param event the event
     */
    @FXML
    void gewParameterKonfigAuswertung(ActionEvent event) {
        CtrlParameterKonfigAuswertung controllerFenster = new CtrlParameterKonfigAuswertung();
        controllerFenster.init();
        openController(controllerFenster, "ParameterKonfigAuswertung.fxml", 1500, 850,
                "Parameter Konfiguration Auswertungen");
    }

    /**
     * Gew import wortmeldetisch.
     *
     * @param event the event
     */
    @FXML
    void gewImportWortmeldetisch(ActionEvent event) {
        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        CtrlImportWortmeldetisch controllerFenster = new CtrlImportWortmeldetisch();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ImportWortmeldetisch.fxml", 1300, 730,
                "Import Wortmeldetisch", true);
    }

    /**
     * Gew parameter gaeste.
     *
     * @param event the event
     */
    @FXML
    void gewParameterGaeste(ActionEvent event) {
        CtrlParameterGaeste controllerFenster = new CtrlParameterGaeste();
        controllerFenster.init();
        openController(controllerFenster, "ParameterGaeste.fxml", 1500, 850, "Parameter Gäste");
    }

    /**
     * Gew parameter nummernformen.
     *
     * @param event the event
     */
    @FXML
    void gewParameterNummernformen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlParameterNummernformen controllerFenster = new CtrlParameterNummernformen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParameterNummernformen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewParameterNummernformen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1500, 800);
        neuerDialog.setTitle("Parameter Nummernformen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew parameter abstimmungen parameter.
     *
     * @param event the event
     */
    @FXML
    void gewParameterAbstimmungenParameter(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterAbstimmungsParameter controllerFenster = new CtrlParameterAbstimmungsParameter();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungsParameter.fxml", 1545, 810,
                "Abstimmungsparameter", true);
    }

    /**
     * Gew parameter abstimmungen.
     *
     * @param event the event
     */
    @FXML
    void gewParameterAbstimmungen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterAbstimmung controllerFenster = new CtrlParameterAbstimmung();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmung.fxml", 1500, 760, "Abstimmungen",
                true);

    }

    /**
     * Gew designer.
     *
     * @param event the event
     */
    @FXML
    void gewDesigner(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        DlAufrufDesigner controllerFenster = new DlAufrufDesigner();

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingDesign/DlAufrufDesigner.fxml", 1200, 720, "MeetingDesign", true);
    }

    /**
     * Gew parameter set speichern.
     *
     * @param event the event
     */
    @FXML
    void gewParameterSetSpeichern(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterSetVerwaltung controllerFenster = new CtrlParameterSetVerwaltung();
        controllerFenster.init(neuerDialog, 1);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterSetVerwaltung.fxml", 1500, 700,
                "Parameter als Set speichern", true);
    }

    /**
     * Gew admin reorg idents.
     *
     * @param event the event
     */
    @FXML
    void gewAdminReorgIdents(ActionEvent event) {
        CaZeigeHinweis caZeige = new CaZeigeHinweis();
        caZeige.zeige(eigeneStage, "Zum Reorg Identseinfach klicken, und dann warten");
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);
        bvDatenbank.reorgIdents();
        lDbBundle.closeAll();

        caZeige.zeige(eigeneStage, "Reorg Idents beendet");

    }

    /**
     * Gew admin div.
     *
     * @param event the event
     */
    @FXML
    void gewAdminDiv(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlAdminDiv controllerFenster = new CtrlAdminDiv();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDiv.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewAdminDiv 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Div. Administrator-Tools");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew passwort aendern.
     *
     * @param event the event
     */
    @FXML
    void gewPasswortAendern(ActionEvent event) {
        Stage zwischenDialog = new Stage();

        CtrlLoginPasswortAendern controllerDialog = new CtrlLoginPasswortAendern();

        controllerDialog.init(zwischenDialog);

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/LoginPasswortAendern.fxml"));
        loader1.setController(controllerDialog);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlLogin.lassePasswortAendern 002");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 700, 400);
        zwischenDialog.setTitle("Ändern Passwort");
        zwischenDialog.setScene(scene1);
        zwischenDialog.initModality(Modality.APPLICATION_MODAL);
        zwischenDialog.showAndWait();
    }

    /**
     * Gew neuen mandant anlegen.
     *
     * @param event the event
     */
    @FXML
    void gewNeuenMandantAnlegen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlMandantAnlegen controllerFenster = new CtrlMandantAnlegen();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/MandantAnlegen.fxml", 1600, 700, "Mandant anlegen",
                true);

        String hTitel = "clientMeeting HV Master - " + Integer.toString(ParamS.clGlobalVar.mandant) + "/"
                + Integer.toString(ParamS.clGlobalVar.hvJahr) + ParamS.clGlobalVar.hvNummer + "/"
                + ParamS.clGlobalVar.datenbereich + " " + ParamS.eclEmittent.bezeichnungKurz;
        eigeneStage.setTitle(hTitel);

    }

    @FXML
    void gewMailingParameter(ActionEvent event) {
        CtrlMailingParameter controllerFenster = new CtrlMailingParameter();
        openController(controllerFenster, "MailingParameter.fxml", 1200, 680, "Mailing Parameter");
    }

    @FXML
    void gewMailingVersand(ActionEvent event) {
        CtrlMailingVersand ctrl = new CtrlMailingVersand();
        openController(ctrl, "MailingVersand.fxml", ctrl.width, ctrl.height, "E-Mail Versand");
    }

    @FXML
    void gewSRDNotification(ActionEvent event) {
        CtrlSRDNotification ctrl = new CtrlSRDNotification();
        openController(ctrl, "SRDNotification.fxml", ctrl.width, ctrl.height, "SRD II - Notification");
    }

    @FXML
    void gewSRDCancallation(ActionEvent event) {
        CtrlSRDCancallation ctrl = new CtrlSRDCancallation();
        openController(ctrl, "SRDCancallation.fxml", ctrl.width, ctrl.height, "SRD II - Cancallation");
    }

    @FXML
    void gewSRDResult(ActionEvent event) {
        CtrlSRDResult ctrl = new CtrlSRDResult();
        openController(ctrl, "SRDResult.fxml", ctrl.width, ctrl.height, "SRD II - Result Dissemination");
    }

    final Map<MenuItem, Boolean> map = new HashMap<>();

    private void createHvAnsicht() {

        Button btn1 = createButton("Präsenz Übersicht", new FontIcon(FontAwesomeSolid.CHART_PIE),
                (mnoHVPraesenz.isVisible() && mnUebersichtPraesenz.isVisible()));
        btn1.setOnAction(e -> gewUebersichtPraesenz(new ActionEvent()));
        hvAnsicht.add(btn1, 0, 0);

        btn1 = createButton("Präsenz feststellen", null,
                (mnoHVPraesenz.isVisible() && mnPraesenzFeststellen.isVisible()));
        btn1.setOnAction(e -> gewPraesenzFeststellen(new ActionEvent()));
        hvAnsicht.add(btn1, 1, 0);

        btn1 = createButton("Präsenz Drucken", null, (mnoHVPraesenz.isVisible() && mnPraesenzlisteDrucken.isVisible()));
        btn1.setOnAction(e -> gewPraesenzlisteDrucken(new ActionEvent()));
        hvAnsicht.add(btn1, 2, 0);

        btn1 = createButton("Diverses für Aktionär", null, (mnoHVPraesenz.isVisible() && mnDivAktionaer.isVisible()));
        btn1.setOnAction(e -> gewDivAktionaer(new ActionEvent()));
        hvAnsicht.add(btn1, 3, 0);

        final Boolean bestandsverwaltung = (ParamS.paramGeraet.moduleBestandsverwaltung == true
                && ParamS.eclUserLogin.pruefe_modul_moduleBestandsverwaltung() == true
                && ParamS.clGlobalVar.webServicePfadNr != -1);

        btn1 = createButton("Vollmacht/Weisung an SRV", null, (mnoHVPraesenz.isVisible() && mnSRV.isVisible()));
        btn1.setOnAction(e -> gewSRV(new ActionEvent()));
        hvAnsicht.add(btn1, 0, 1);

        if (bestandsverwaltung)
            SClErfassungsDaten.init();

        btn1 = createButton("Bestand", new FontIcon(FontAwesomeSolid.ADDRESS_BOOK), bestandsverwaltung);
        btn1.setOnAction(e -> gewAktienregister(new ActionEvent()));
        hvAnsicht.add(btn1, 1, 1);

        btn1 = createButton("Sammelkarten", new FontIcon(FontAwesomeSolid.LIST_ALT), bestandsverwaltung);
        btn1.setOnAction(e -> gewSammelkarten(new ActionEvent()));
        hvAnsicht.add(btn1, 2, 1);

        btn1 = createButton("Einzelfall", new FontIcon(FontAwesomeSolid.USER), bestandsverwaltung);
        btn1.setOnAction(e -> gewEinzelfall(new ActionEvent()));
        hvAnsicht.add(btn1, 3, 1);

        btn1 = createButton("Formulare", new FontIcon(FontAwesomeSolid.EDIT),
                mnoKonfiguration.isVisible() && mnDesigner.isVisible());
        btn1.setOnAction(e -> gewDesigner(new ActionEvent()));
        hvAnsicht.add(btn1, 0, 2);

        btn1 = createButton("Abstimmung\nParameter", new FontIcon(FontAwesomeSolid.COG),
                (mnoKonfiguration.isVisible() && mnParameterAbstimmungen.isVisible()));
        btn1.setOnAction(e -> gewParameterAbstimmungen(new ActionEvent()));
        hvAnsicht.add(btn1, 1, 2);

        btn1 = createButton("Abstimmungsablauf\nAuswertung", new FontIcon(FontAwesomeSolid.CALCULATOR),
                (mnoHVAbstimmung.isVisible() && mnAbstimmungAblauf.isVisible()));
        btn1.setOnAction(e -> gewAbstimmungAblauf(new ActionEvent()));
        hvAnsicht.add(btn1, 2, 2);

        map.put(mnoAdmin, mnoAdmin.isVisible());
        map.put(mnoTesten, mnoTesten.isVisible());
        map.put(mnoVerwaltung, mnoVerwaltung.isVisible());
        map.put(mnoMailing, mnoMailing.isVisible());
        map.put(mnoSRD, mnoSRD.isVisible());

        map.put(mnPraesenzsummen, mnPraesenzsummen.isVisible());
        map.put(mnProtokoll, mnProtokoll.isVisible());
        map.put(mnDelay, mnDelay.isVisible());
        map.put(mnElekTeilnehmerverzeichnisVersorgen, mnElekTeilnehmerverzeichnisVersorgen.isVisible());
        map.put(mnElekTeilnehmerverzeichnisAnzeigen, mnElekTeilnehmerverzeichnisAnzeigen.isVisible());
        map.put(mnVereinVersorgen, mnVereinVersorgen.isVisible());
        map.put(mnVerzeichnisVerein, mnVerzeichnisVerein.isVisible());
        map.put(mnPersonenZaehlung, mnPersonenZaehlung.isVisible());

        map.put(mnAbstimmungEingabe, mnAbstimmungEingabe.isVisible());
        map.put(mnUebersichtSRV, mnUebersichtSRV.isVisible());
        map.put(mnAbstimmungsverhalten, mnAbstimmungsverhalten.isVisible());

        map.put(mnUserLogin, mnUserLogin.isVisible());
        map.put(mnUserProfile, mnUserProfile.isVisible());
        map.put(mnParameterServer, mnParameterServer.isVisible());
        map.put(mnGeraeteSet, mnGeraeteSet.isVisible());
        map.put(mnGeraeteKlasse, mnGeraeteKlasse.isVisible());
        map.put(mnGeraeteZuordnung, mnGeraeteZuordnung.isVisible());
        map.put(mnGeraeteLabelPrinter, mnGeraeteLabelPrinter.isVisible());
        map.put(mnParameterSetSpeichern, mnParameterSetSpeichern.isVisible());
        map.put(mnImportWortmeldetisch, mnImportWortmeldetisch.isVisible());

    }

    private Button createButton(String text, FontIcon icon, Boolean available) {

        final Button btn = new Button(text);

        if (icon != null) {
            btn.setGraphicTextGap(6);
            btn.setGraphic(icon);
        }

        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setDisable(!available);

        return btn;
    }

    @FXML
    void onCbAnsicht(ActionEvent event) {
        hvAnsicht.setVisible(!hvAnsicht.isVisible());
        cbAnsicht.setSelected(hvAnsicht.isVisible());

        for (var m : map.entrySet()) {
            if (m.getValue()) {
                m.getKey().setVisible(!m.getKey().isVisible());
            }
        }
    }

    void gewAktienregister(ActionEvent event) {
        de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage tmp = new de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage();
        tmp.gewAktienregister(new ActionEvent());
    }

    void gewSammelkarten(ActionEvent event) {
        de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage tmp = new de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage();
        tmp.gewSammelkarten(new ActionEvent());
    }

    void gewEinzelfall(ActionEvent event) {
        de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage tmp = new de.meetingapps.meetingclient.meetingBestand.CtrlHauptStage();
        tmp.gewEinzelfall(new ActionEvent());
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }
}
