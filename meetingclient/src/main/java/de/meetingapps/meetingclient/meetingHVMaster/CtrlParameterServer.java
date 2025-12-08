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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterServer.
 */
public class CtrlParameterServer {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl fehlermeldung. */
    @FXML
    private Label lblFehlermeldung;

    /** The tf server bezeichnung. */
    @FXML
    private TextField tfServerBezeichnung;

    /** The tf server art. */
    @FXML
    private TextField tfServerArt;

    /** The tf db server ident. */
    @FXML
    private TextField tfDbServerIdent;

    @FXML
    private TextField tfPortalInitialPasswortTestBestand;

    /** The tf local praefix link. */
    @FXML
    private TextField tfLocalPraefixLink;

    /** The tf local praefix link alternativ. */
    @FXML
    private TextField tfLocalPraefixLinkAlternativ;

    /** The tf subdomain praefix link. */
    @FXML
    private TextField tfSubdomainPraefixLink;

    /** The tf subdomain suffix link. */
    @FXML
    private TextField tfSubdomainSuffixLink;

    @FXML
    private TextField tfDomainLinkErsetzen;

    @FXML
    private TextField tfDomainLinkErsetzenDurch;

    /** The cb local intern passwort. */
    @FXML
    private CheckBox cbLocalInternPasswort;

    /** The cb standard portaltexte pflegbar. */
    @FXML
    private CheckBox cbStandardPortaltextePflegbar;

    /** The cb nummernformen pflegbar. */
    @FXML
    private CheckBox cbNummernformenPflegbar;

    /** The cb instis pflegbar. */
    @FXML
    private CheckBox cbInstisPflegbar;

    /** The cb passwort aging. */
    @FXML
    private CheckBox cbPasswortAging;

    /** The cb local intern passwort meeting. */
    @FXML
    private CheckBox cbLocalInternPasswortMeeting;

    /** The cb login gesperrt. */
    @FXML
    private CheckBox cbLoginGesperrt;

    /** The tf login gesperrt text deutsch. */
    @FXML
    private TextField tfLoginGesperrtTextDeutsch;

    /** The tf login gesperrt text englisch. */
    @FXML
    private TextField tfLoginGesperrtTextEnglisch;

    /** The tf verbund server adresse 1. */
    @FXML
    private TextField tfVerbundServerAdresse1;

    /** The cb verbund server aktiv 1. */
    @FXML
    private CheckBox cbVerbundServerAktiv1;

    /** The tf verbund server adresse 2. */
    @FXML
    private TextField tfVerbundServerAdresse2;

    /** The cb verbund server aktiv 2. */
    @FXML
    private CheckBox cbVerbundServerAktiv2;

    /** The tf verbund server adresse 3. */
    @FXML
    private TextField tfVerbundServerAdresse3;

    /** The cb verbund server aktiv 3. */
    @FXML
    private CheckBox cbVerbundServerAktiv3;

    /** The tf verbund server adresse 4. */
    @FXML
    private TextField tfVerbundServerAdresse4;

    /** The cb verbund server aktiv 4. */
    @FXML
    private CheckBox cbVerbundServerAktiv4;

    /** The tf verbund server adresse 5. */
    @FXML
    private TextField tfVerbundServerAdresse5;

    /** The cb verbund server aktiv 5. */
    @FXML
    private CheckBox cbVerbundServerAktiv5;

    /** The tf web sockets local host. */
    @FXML
    private TextField tfWebSocketsLocalHost;

    @FXML
    private TextField tfPraefixPfadVerzeichnisse;

    @FXML
    private TextField tfTimeoutSperrlogik;

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert lblFehlermeldung != null : "fx:id=\"lblFehlermeldung\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfServerBezeichnung != null : "fx:id=\"tfServerBezeichnung\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfServerArt != null : "fx:id=\"tfServerArt\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfLocalPraefixLink != null : "fx:id=\"tfLocalPraefixLink\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfLocalPraefixLinkAlternativ != null : "fx:id=\"tfLocalPraefixLinkAlternativ\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfSubdomainPraefixLink != null : "fx:id=\"tfSubdomainPraefixLink\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert tfSubdomainSuffixLink != null : "fx:id=\"tfSubdomainSuffixLink\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert cbLocalInternPasswort != null : "fx:id=\"cbLocalInternPasswort\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        assert cbPasswortAging != null : "fx:id=\"cbPasswortAging\" was not injected: check your FXML file 'ParameterServer.fxml'.";
        

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        /****** paramServer ********/
        tfServerBezeichnung.setText(ParamS.paramServer.serverBezeichnung);
        tfServerArt.setText(Integer.toString(ParamS.paramServer.serverArt));
        tfDbServerIdent.setText(Integer.toString(ParamS.paramServer.dbServerIdent));
        tfPortalInitialPasswortTestBestand.setText(ParamS.paramServer.portalInitialPasswortTestBestand);
        tfLocalPraefixLink.setText(ParamS.paramServer.pLocalPraefixLink);
        tfLocalPraefixLinkAlternativ.setText(ParamS.paramServer.pLocalPraefixLinkAlternativ);
        tfSubdomainPraefixLink.setText(ParamS.paramServer.pSubdomainPraefixLink);
        tfSubdomainSuffixLink.setText(ParamS.paramServer.pSubdomainSuffixLink);
        
        tfDomainLinkErsetzen.setText(ParamS.paramServer.domainLinkErsetzen);
        tfDomainLinkErsetzenDurch.setText(ParamS.paramServer.domainLinkErsetzenDurch);

        if (ParamS.paramServer.pLocalInternPasswort == 0) {
            cbLocalInternPasswort.setSelected(true);
        } else {
            cbLocalInternPasswort.setSelected(false);
        }

        cbStandardPortaltextePflegbar.setSelected(ParamS.paramServer.standardPortaltextePflegbar == 1);
        cbNummernformenPflegbar.setSelected(ParamS.paramServer.nummernformenPflegbar == 1);
        cbInstisPflegbar.setSelected(ParamS.paramServer.instisPflegbar == 1);

        if (ParamS.paramServer.passwortAgingEin == 1) {
            cbPasswortAging.setSelected(true);
        } else {
            cbPasswortAging.setSelected(false);
        }
        if (ParamS.paramServer.pLocalInternPasswortMeeting == 0) {
            cbLocalInternPasswortMeeting.setSelected(true);
        } else {
            cbLocalInternPasswortMeeting.setSelected(false);
        }

        cbLoginGesperrt.setSelected(ParamS.paramServer.loginGesperrt == 1);
        tfLoginGesperrtTextDeutsch.setText(ParamS.paramServer.loginGesperrtTextDeutsch);
        tfLoginGesperrtTextEnglisch.setText(ParamS.paramServer.loginGesperrtTextEnglisch);

        cbVerbundServerAktiv1.setSelected(ParamS.paramServer.verbundServerAktiv[0]);
        tfVerbundServerAdresse1.setText(ParamS.paramServer.verbundServerAdresse[0]);
        cbVerbundServerAktiv2.setSelected(ParamS.paramServer.verbundServerAktiv[1]);
        tfVerbundServerAdresse2.setText(ParamS.paramServer.verbundServerAdresse[1]);
        cbVerbundServerAktiv3.setSelected(ParamS.paramServer.verbundServerAktiv[2]);
        tfVerbundServerAdresse3.setText(ParamS.paramServer.verbundServerAdresse[2]);
        cbVerbundServerAktiv4.setSelected(ParamS.paramServer.verbundServerAktiv[3]);
        tfVerbundServerAdresse4.setText(ParamS.paramServer.verbundServerAdresse[3]);
        cbVerbundServerAktiv5.setSelected(ParamS.paramServer.verbundServerAktiv[4]);
        tfVerbundServerAdresse5.setText(ParamS.paramServer.verbundServerAdresse[4]);
        tfWebSocketsLocalHost.setText(ParamS.paramServer.webSocketsLocalHost);

        tfPraefixPfadVerzeichnisse.setText(ParamS.paramServer.praefixPfadVerzeichnisse);
        tfTimeoutSperrlogik.setText(Long.toString(ParamS.paramServer.timeoutSperrlogik));

        lDbBundle.closeAll();
    }

    /************************Logik***************************************************/
    
    private void speichernParameter() {
        /*Eingaben überprüfen*/
        String hText = "";

        hText = tfServerBezeichnung.getText().trim();
        if (hText.length() > 120) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Serverbezeichnung darf maximal 120 Stellen haben!");
            tfServerBezeichnung.requestFocus();
            return;
        }

        hText = tfServerArt.getText().trim();
        if (hText.compareTo("1") != 0 && hText.compareTo("2") != 0 && hText.compareTo("3") != 0
                && hText.compareTo("4") != 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültigen Wert für Server-Art eingeben!");
            tfServerArt.requestFocus();
            return;
        }

        hText = tfDbServerIdent.getText().trim();
        if (!CaString.isNummern(hText) || hText.length() > 7) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültigen Wert für Datenbank-Server-Nummer eingeben!");
            tfDbServerIdent.requestFocus();
            return;
        }

        hText = tfPortalInitialPasswortTestBestand.getText().trim();
        if (hText.length() > 30) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Initialpasswort Testdaten darf maximal 30 Stellen haben!");
            tfPortalInitialPasswortTestBestand.requestFocus();
            return;
        }

        hText = tfLocalPraefixLink.getText().trim();
        if (hText.length() > 120) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Präfix Portal Seiten darf maximal 120 Stellen haben!");
            tfLocalPraefixLink.requestFocus();
            return;
        }

        hText = tfLocalPraefixLinkAlternativ.getText().trim();
        if (hText.length() > 120) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Alternatives Präfix Portal Seiten darf maximal 120 Stellen haben!");
            tfLocalPraefixLinkAlternativ.requestFocus();
            return;
        }

        hText = tfSubdomainPraefixLink.getText().trim();
        if (hText.length() > 120) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Subdomain-Präfix darf maximal 120 Stellen haben!");
            tfSubdomainPraefixLink.requestFocus();
            return;
        }

        hText = tfSubdomainSuffixLink.getText().trim();
        if (hText.length() > 120) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Subdomain-Suffix darf maximal 120 Stellen haben!");
            tfSubdomainSuffixLink.requestFocus();
            return;
        }

        hText = tfDomainLinkErsetzen.getText().trim();
        if (hText.length() > 50) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Ersetzen darf maximal 50 Stellen haben!");
            tfDomainLinkErsetzen.requestFocus();
            return;
        }
        hText = tfDomainLinkErsetzenDurch.getText().trim();
        if (hText.length() > 50) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Ersetzen durch darf maximal 50 Stellen haben!");
            tfDomainLinkErsetzenDurch.requestFocus();
            return;
        }

        if (cbLocalInternPasswort.isSelected() && tfServerArt.getText().compareTo("3") != 0
                && tfServerArt.getText().compareTo("4") != 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Portalstart ohne Passwort nur bei Serverart 3 oder 4 zulässig!");
            cbLocalInternPasswort.requestFocus();
            return;
        }

        hText = tfLoginGesperrtTextDeutsch.getText().trim();
        if (hText.length() > 200) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Gesperrt-Text Deutsch darf maximal 200 Stellen haben!");
            tfLoginGesperrtTextDeutsch.requestFocus();
            return;
        }
        hText = tfLoginGesperrtTextEnglisch.getText().trim();
        if (hText.length() > 200) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Gesperrt-Text Englisch darf maximal 200 Stellen haben!");
            tfLoginGesperrtTextEnglisch.requestFocus();
            return;
        }

        hText = tfTimeoutSperrlogik.getText().trim();
        if (!CaString.isNummern(hText) || hText.length() > 10) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültigen Wert für Timeout Sperrlogik eingeben!");
            tfTimeoutSperrlogik.requestFocus();
            return;
        }

        //        if (cbPasswortAging.isSelected() == false && tfServerArt.getText().compareTo("1") == 0) {
        //            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        //            zeigeHinweis.zeige(eigeneStage, "Passwort-Aging bei Serverart 1 zwingend erforderlich!");
        //            cbPasswortAging.requestFocus();
        //            return;
        //        }

        ParamS.paramServer.serverBezeichnung = tfServerBezeichnung.getText().trim();
        ParamS.paramServer.serverArt = Integer.parseInt(tfServerArt.getText().trim());
        ParamS.paramServer.dbServerIdent = Integer.parseInt(tfDbServerIdent.getText().trim());
        ParamS.paramServer.pLocalPraefixLink = tfLocalPraefixLink.getText().trim();
        ParamS.paramServer.portalInitialPasswortTestBestand = tfPortalInitialPasswortTestBestand.getText().trim();
        ParamS.paramServer.pLocalPraefixLinkAlternativ = tfLocalPraefixLinkAlternativ.getText().trim();
        ParamS.paramServer.pSubdomainPraefixLink = tfSubdomainPraefixLink.getText().trim();
        ParamS.paramServer.pSubdomainSuffixLink = tfSubdomainSuffixLink.getText().trim();
        ParamS.paramServer.domainLinkErsetzen = tfDomainLinkErsetzen.getText().trim();
        ParamS.paramServer.domainLinkErsetzenDurch = tfDomainLinkErsetzenDurch.getText().trim();
        if (cbLocalInternPasswort.isSelected()) {
            ParamS.paramServer.pLocalInternPasswort = 0;
        } else {
            ParamS.paramServer.pLocalInternPasswort = 1;
        }

        if (cbStandardPortaltextePflegbar.isSelected()) {
            ParamS.paramServer.standardPortaltextePflegbar = 1;
        } else {
            ParamS.paramServer.standardPortaltextePflegbar = 0;
        }
        if (cbNummernformenPflegbar.isSelected()) {
            ParamS.paramServer.nummernformenPflegbar = 1;
        } else {
            ParamS.paramServer.nummernformenPflegbar = 0;
        }
        if (cbInstisPflegbar.isSelected()) {
            ParamS.paramServer.instisPflegbar = 1;
        } else {
            ParamS.paramServer.instisPflegbar = 0;
        }

        if (cbPasswortAging.isSelected()) {
            ParamS.paramServer.passwortAgingEin = 1;
        } else {
            ParamS.paramServer.passwortAgingEin = 0;
        }

        if (cbLocalInternPasswortMeeting.isSelected()) {
            ParamS.paramServer.pLocalInternPasswortMeeting = 0;
        } else {
            ParamS.paramServer.pLocalInternPasswortMeeting = 1;
        }

        if (cbLoginGesperrt.isSelected()) {
            ParamS.paramServer.loginGesperrt = 1;
        } else {
            ParamS.paramServer.loginGesperrt = 0;
        }
        ParamS.paramServer.loginGesperrtTextDeutsch = tfLoginGesperrtTextDeutsch.getText().trim();
        ParamS.paramServer.loginGesperrtTextEnglisch = tfLoginGesperrtTextEnglisch.getText().trim();

        ParamS.paramServer.verbundServerAktiv[0] = cbVerbundServerAktiv1.isSelected();
        ParamS.paramServer.verbundServerAdresse[0] = tfVerbundServerAdresse1.getText().trim();
        ParamS.paramServer.verbundServerAktiv[1] = cbVerbundServerAktiv2.isSelected();
        ParamS.paramServer.verbundServerAdresse[1] = tfVerbundServerAdresse2.getText().trim();
        ParamS.paramServer.verbundServerAktiv[2] = cbVerbundServerAktiv3.isSelected();
        ParamS.paramServer.verbundServerAdresse[2] = tfVerbundServerAdresse3.getText().trim();
        ParamS.paramServer.verbundServerAktiv[3] = cbVerbundServerAktiv4.isSelected();
        ParamS.paramServer.verbundServerAdresse[3] = tfVerbundServerAdresse4.getText().trim();
        ParamS.paramServer.verbundServerAktiv[4] = cbVerbundServerAktiv5.isSelected();
        ParamS.paramServer.verbundServerAdresse[4] = tfVerbundServerAdresse5.getText().trim();

        ParamS.paramServer.webSocketsLocalHost = tfWebSocketsLocalHost.getText().trim();

        ParamS.paramServer.praefixPfadVerzeichnisse = tfPraefixPfadVerzeichnisse.getText().trim();
        ParamS.paramServer.timeoutSperrlogik = Long.parseLong(tfTimeoutSperrlogik.getText().trim());

        lDbBundle.refreshParameterAusStatic();
        lDbBundle.openAllOhneParameterCheck();

        lDbBundle.dbParameter.updateServer_all();
        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterServer();
        lDbBundle.closeAll();

        eigeneStage.hide();

    }

    /**************************Anzeigefunktionen***************************************/
    
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /********************Aktionen auf Oberfläche*************************/
    
    @FXML
    void clickedSpeichern(ActionEvent event) {

        speichernParameter();

    }

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        eigeneStage.hide();

    }

}
