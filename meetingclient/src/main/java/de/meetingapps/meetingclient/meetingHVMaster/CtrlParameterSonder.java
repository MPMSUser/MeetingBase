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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterSonder.
 */
public class CtrlParameterSonder extends CtrlRoot {

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

    /** The tf zweiter mandant lfd nr. */
    @FXML
    private TextField tfZweiterMandantLfdNr;

    /** The tf zweiter mandant DB. */
    @FXML
    private TextField tfZweiterMandantDB;

    /** The tf zweiter mandant nr. */
    @FXML
    private TextField tfZweiterMandantNr;

    /** The tf zweiter mandant HV jahr. */
    @FXML
    private TextField tfZweiterMandantHVJahr;

    /** The tp portal app. */
    @FXML
    private Tab tpPortalApp;

    /** The cb ip tracking. */
    @FXML
    private CheckBox cbIpTracking;

    /** The tf variante dialogablauf. */
    @FXML
    private TextField tfVarianteDialogablauf;

    /** The tf dialogveranstaltung. */
    @FXML
    private TextField tfDialogveranstaltung;

    /** The tf beiratswahl. */
    @FXML
    private TextField tfBeiratswahl;

    /** The tf generalversammlung. */
    @FXML
    private TextField tfGeneralversammlung;

    /** The tf generalversammlung briefwahl. */
    @FXML
    private TextField tfGeneralversammlungBriefwahl;

    /** The tf generalversammlung teilnahme. */
    @FXML
    private TextField tfGeneralversammlungTeilnahme;

    /** The tf generalversammlung gast. */
    @FXML
    private TextField tfGeneralversammlungGast;

    /** The tf einstellungen. */
    @FXML
    private TextField tfEinstellungen;

    /** The tf unterlagen. */
    @FXML
    private TextField tfUnterlagen;

    /** The tf dialogveranstaltung aktiv. */
    @FXML
    private TextField tfDialogveranstaltungAktiv;

    /** The tf verfahren passwort vergessen ablauf. */
    @FXML
    private TextField tfVerfahrenPasswortVergessenAblauf;

    /** The tf nur raw live abstimmung. */
    @FXML
    private TextField tfNurRawLiveAbstimmung;

    /** The tf anzeige stimmen. */
    @FXML
    private TextField tfAnzeigeStimmen;

    /** The tf veranstaltung mail verschicken. */
    @FXML
    private TextField tfVeranstaltungMailVerschicken;

    /** The tf veranstaltung personenzahl eingeben. */
    @FXML
    private TextField tfVeranstaltungPersonenzahlEingeben;

    /** The tf veranstaltung mehrfach auswaehlbar je gruppe. */
    @FXML
    private TextField tfVeranstaltungMehrfachAuswaehlbarJeGruppe;

    /** The cb veranstaltungen aktiv fuer gattung 1. */
    @FXML
    private CheckBox cbVeranstaltungenAktivFuerGattung1;

    /** The cb veranstaltungen aktiv fuer gattung 2. */
    @FXML
    private CheckBox cbVeranstaltungenAktivFuerGattung2;

    /** The cb veranstaltungen aktiv fuer gattung 3. */
    @FXML
    private CheckBox cbVeranstaltungenAktivFuerGattung3;

    /** The cb veranstaltungen aktiv fuer gattung 4. */
    @FXML
    private CheckBox cbVeranstaltungenAktivFuerGattung4;

    /** The cb veranstaltungen aktiv fuer gattung 5. */
    @FXML
    private CheckBox cbVeranstaltungenAktivFuerGattung5;

    /** The tf veranstaltungen aktiv fuer gattung. */
    @FXML
    private TextField tfVeranstaltungenAktivFuerGattung;

    /** The tf freiwilling anmelden praesenz oder online. */
    @FXML
    private TextField tfFreiwillingAnmeldenPraesenzOderOnline;

    /** The tf freiwillige anmeldung EK druck moeglich. */
    @FXML
    private TextField tfFreiwilligeAnmeldungEKDruckMoeglich;

    /** The tf freiwillige anmeldung nur papier. */
    @FXML

    private TextField tfFreiwilligeAnmeldungNurPapier;

    /** The tf freiwillige anmeldung mit vertretereingabe. */
    @FXML
    private TextField tfFreiwilligeAnmeldungMitVertretereingabe;

    /** The tf freiwillige anmeldung zwei personen fuer minderjaehrige moeglich. */
    @FXML
    private TextField tfFreiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich;

    /** The cb freiwillige anmeldung aktiv fuer gattung 1. */
    @FXML
    private CheckBox cbFreiwilligeAnmeldungAktivFuerGattung1;

    /** The cb freiwillige anmeldung aktiv fuer gattung 2. */
    @FXML
    private CheckBox cbFreiwilligeAnmeldungAktivFuerGattung2;

    /** The cb freiwillige anmeldung aktiv fuer gattung 3. */
    @FXML
    private CheckBox cbFreiwilligeAnmeldungAktivFuerGattung3;

    /** The cb freiwillige anmeldung aktiv fuer gattung 4. */
    @FXML
    private CheckBox cbFreiwilligeAnmeldungAktivFuerGattung4;

    /** The cb freiwillige anmeldung aktiv fuer gattung 5. */
    @FXML
    private CheckBox cbFreiwilligeAnmeldungAktivFuerGattung5;

    /** The tf online teilnahme gast in separatem menue. */
    @FXML
    private TextField tfOnlineTeilnahmeGastInSeparatemMenue;

    /** The tf online teilnahme nur fuer freiwillig angemeldete. */
    @FXML
    private TextField tfOnlineTeilnahmeNurFuerFreiwilligAngemeldete;

    /** The tf online abstimmung berechtigung separat pruefen. */
    @FXML
    private TextField tfOnlineAbstimmungBerechtigungSeparatPruefen;

    /** The tf hybrid teilnahme aktiv. */
    @FXML
    private TextField tfHybridTeilnahmeAktiv;

    /** The tf register anbindung. */
    @FXML
    private TextField tfRegisterAnbindung;

    /** The tf register anbindung oberflaeche. */
    @FXML
    private TextField tfRegisterAnbindungOberflaeche;

    /** The tf register anbindung check bei login. */
    @FXML
    private TextField tfRegisterAnbindungCheckBeiLogin;

    /** The tf bestaetigen hinweis P 1. */
    @FXML
    private TextField tfBestaetigenHinweisP1;

    /** The tf bestaetigen hinweis P 2. */
    @FXML
    private TextField tfBestaetigenHinweisP2;

    /** The tf in HV portal keine einstellungen fuer aktionaere. */
    @FXML
    private TextField tfInHVPortalKeineEinstellungenFuerAktionaere;

    /** The tf in HV portal keine email und kein passwort fuer aktionaere. */
    @FXML
    private TextField tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere;

    /** The tf register anbindung zur HV. */
    @FXML
    private TextField tfRegisterAnbindungZurHV;

    /** The tf register anbindung von HV. */
    @FXML
    private TextField tfRegisterAnbindungVonHV;

    /** The tf api client id. */
    @FXML
    private TextField tfApi_client_id;

    /** The tf api client secret. */
    @FXML
    private TextField tfApi_client_secret;

    /** The tf api base url. */
    @FXML
    private TextField tfApi_base_url;

    /** The tf api key id. */
    @FXML
    private TextField tfApi_key_id;

    /** The tf api jwt expiration time. */
    @FXML
    private TextField tfApi_jwt_expiration_time;

    /** The tf api key name. */
    @FXML
    private TextField tfApi_key_name;

    /** The tf api ping url. */
    @FXML
    private TextField tfApi_ping_url;

    /** The tf api ku178 url. */
    @FXML
    private TextField tfApiku178_url;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /**
     * *************Ab hier individuelle Deklarationen******************.
     */

    @FXML
    void initialize() {

        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tpAllgemein != null : "fx:id=\"tpAllgemein\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tfZweiterMandantLfdNr != null : "fx:id=\"tfZweiterMandantLfdNr\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tfZweiterMandantDB != null : "fx:id=\"tfZweiterMandantDB\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tfZweiterMandantNr != null : "fx:id=\"tfZweiterMandantNr\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tfZweiterMandantHVJahr != null : "fx:id=\"tfZweiterMandantHVJahr\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tpPortalApp != null : "fx:id=\"tpPortalApp\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert cbIpTracking != null : "fx:id=\"cbIpTracking\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert tfVarianteDialogablauf != null : "fx:id=\"tfVarianteDialogablauf\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterSonder.fxml'.";
        
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
        tfZweiterMandantNr.setText(Integer.toString(ParamS.param.paramBasis.zweiterMandantNr));
        tfZweiterMandantHVJahr.setText(Integer.toString(ParamS.param.paramBasis.zweiterMandantHVJahr));
        tfZweiterMandantLfdNr.setText(ParamS.param.paramBasis.zweiterMandantHVNummer);
        tfZweiterMandantDB.setText(ParamS.param.paramBasis.zweiterMandantDatenbereich);

        tfVarianteDialogablauf.setText(Integer.toString(ParamS.param.paramPortal.varianteDialogablauf));

        tfDialogveranstaltung.setText(Integer.toString(ParamS.param.paramPortal.lfdHVDialogVeranstaltungenInMenue));
        tfBeiratswahl.setText(Integer.toString(ParamS.param.paramPortal.lfdHVBeiratswahlInMenue));
        tfGeneralversammlung.setText(Integer.toString(ParamS.param.paramPortal.lfdHVGeneralversammlungInMenue));
        tfGeneralversammlungBriefwahl
                .setText(Integer.toString(ParamS.param.paramPortal.lfdHVGeneralversammlungBriefwahlInMenue));
        tfGeneralversammlungTeilnahme
                .setText(Integer.toString(ParamS.param.paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue));
        tfGeneralversammlungGast
                .setText(Integer.toString(ParamS.param.paramPortal.lfdHVGeneralversammlungTeilnahmeGast));
        tfEinstellungen.setText(Integer.toString(ParamS.param.paramPortal.lfdHVEinstellungenInMenue));
        tfUnterlagen.setText(Integer.toString(ParamS.param.paramPortal.lfdHVUnterlagenInMenue));

        tfDialogveranstaltungAktiv.setText(Integer.toString(ParamS.param.paramPortal.dialogveranstaltungAktiv));

        cbIpTracking.setSelected((ParamS.param.paramPortal.loginIPTrackingAktiv == 1));

        tfVerfahrenPasswortVergessenAblauf
                .setText(Integer.toString(ParamS.param.paramPortal.verfahrenPasswortVergessenAblauf));
        tfNurRawLiveAbstimmung.setText(Integer.toString(ParamS.param.paramPortal.nurRawLiveAbstimmung));
        tfAnzeigeStimmen.setText(Integer.toString(ParamS.param.paramPortal.anzeigeStimmen));
        tfVeranstaltungMailVerschicken.setText(Integer.toString(ParamS.param.paramPortal.veranstaltungMailVerschicken));
        tfVeranstaltungPersonenzahlEingeben
                .setText(Integer.toString(ParamS.param.paramPortal.veranstaltungPersonenzahlEingeben));
        tfVeranstaltungMehrfachAuswaehlbarJeGruppe
                .setText(Integer.toString(ParamS.param.paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe));

        cbVeranstaltungenAktivFuerGattung1.setSelected(ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[0]);
        cbVeranstaltungenAktivFuerGattung2.setSelected(ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[1]);
        cbVeranstaltungenAktivFuerGattung3.setSelected(ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[2]);
        cbVeranstaltungenAktivFuerGattung4.setSelected(ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[3]);
        cbVeranstaltungenAktivFuerGattung5.setSelected(ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[4]);

        tfFreiwillingAnmeldenPraesenzOderOnline.setText(Integer.toString(ParamS.param.paramPortal.freiwillingAnmeldenPraesenzOderOnline));
        tfFreiwilligeAnmeldungEKDruckMoeglich.setText(Integer.toString(ParamS.param.paramPortal.freiwilligeAnmeldungEKDruckMoeglich));
        tfFreiwilligeAnmeldungNurPapier.setText(Integer.toString(ParamS.param.paramPortal.freiwilligeAnmeldungNurPapier));
        tfOnlineAbstimmungBerechtigungSeparatPruefen.setText(Integer.toString(ParamS.param.paramPortal.onlineAbstimmungBerechtigungSeparatPruefen));
        tfHybridTeilnahmeAktiv.setText(Integer.toString(ParamS.param.paramPortal.hybridTeilnahmeAktiv));
        tfFreiwilligeAnmeldungMitVertretereingabe.setText(Integer.toString(ParamS.param.paramPortal.freiwilligeAnmeldungMitVertretereingabe));
        tfFreiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich.setText(Integer.toString(ParamS.param.paramPortal.freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich));

        cbFreiwilligeAnmeldungAktivFuerGattung1.setSelected(ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[0]);
        cbFreiwilligeAnmeldungAktivFuerGattung2              .setSelected(ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[1]);
        cbFreiwilligeAnmeldungAktivFuerGattung3
                .setSelected(ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[2]);
        cbFreiwilligeAnmeldungAktivFuerGattung4
                .setSelected(ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[3]);
        cbFreiwilligeAnmeldungAktivFuerGattung5
                .setSelected(ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[4]);

        tfOnlineTeilnahmeGastInSeparatemMenue
                .setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeGastInSeparatemMenue));
        tfOnlineTeilnahmeNurFuerFreiwilligAngemeldete
                .setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete));

        tfRegisterAnbindung.setText(Integer.toString(ParamS.param.paramPortal.registerAnbindung));
        tfRegisterAnbindungOberflaeche.setText(Integer.toString(ParamS.param.paramPortal.registerAnbindungOberflaeche));
        tfRegisterAnbindungCheckBeiLogin
                .setText(Integer.toString(ParamS.param.paramPortal.registerAnbindungCheckBeiLogin));

        tfBestaetigenHinweisP1.setText(Integer.toString(ParamS.param.paramPortal.liefereBestaetigenHinweisWeiter(1)));
        tfBestaetigenHinweisP2.setText(Integer.toString(ParamS.param.paramPortal.liefereBestaetigenHinweisWeiter(2)));

        tfInHVPortalKeineEinstellungenFuerAktionaere
                .setText(Integer.toString(ParamS.param.paramPortal.inHVPortalKeineEinstellungenFuerAktionaere));
        tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere
                .setText(Integer.toString(ParamS.param.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere));

        tfRegisterAnbindungZurHV.setText(Integer.toString(ParamS.param.paramPortal.registerAnbindungZurHV));
        tfRegisterAnbindungVonHV.setText(Integer.toString(ParamS.param.paramPortal.registerAnbindungVonHV));

        tfApi_client_id.setText(ParamS.param.paramPortal.api_client_id);
        tfApi_client_secret.setText(ParamS.param.paramPortal.api_client_secret);
        tfApi_base_url.setText(ParamS.param.paramPortal.api_base_url);
        tfApi_key_id.setText(ParamS.param.paramPortal.api_key_id);
        tfApi_jwt_expiration_time.setText(Integer.toString(ParamS.param.paramPortal.api_jwt_expiration_time));
        tfApi_key_name.setText(ParamS.param.paramPortal.api_key_name);
        tfApi_ping_url.setText(ParamS.param.paramPortal.api_ping_url);
        tfApiku178_url.setText(ParamS.param.paramPortal.apiku178_url);

    }

    /************************Logik***************************************************/
    private boolean speichernParameter() {

        /**************** Prüfen ***********************/
        lFehlertext = "";
        pruefe01(tfVarianteDialogablauf, "Variante Dialogablauf");

        pruefeZahlAuchNegativNichtLeerLaenge(tfDialogveranstaltung, "Dialogversammlung", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfBeiratswahl, "Beiratswahl", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfGeneralversammlung, "Generalversammlung", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfGeneralversammlungBriefwahl, "Generalversammlung Briefwahl", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfGeneralversammlungTeilnahme, "Generalversammlung Teilnahme", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfGeneralversammlungGast, "Generalversammlung Teilnahme Gast", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfEinstellungen, "Einstellungen", 5);
        pruefeZahlAuchNegativNichtLeerLaenge(tfUnterlagen, "Unterlagen", 5);

        pruefe01(tfDialogveranstaltungAktiv, "Dialogveranstaltung aktiv");

        pruefe01(tfVerfahrenPasswortVergessenAblauf, "Passwortvergessen-Verfahren Ablauf");
        pruefe01(tfNurRawLiveAbstimmung, "Raw-Live-Abstimmung");
        pruefe01(tfAnzeigeStimmen, "Stimmenzahl anzeigen");
        pruefe0123(tfVeranstaltungMailVerschicken, "Veranstaltung Quittung");
        pruefe01(tfVeranstaltungPersonenzahlEingeben, "Veranstaltung Personenzahl");
        pruefe01(tfVeranstaltungMehrfachAuswaehlbarJeGruppe, "Veranstaltung Mehrfach auswählbar");
        pruefe01(tfFreiwillingAnmeldenPraesenzOderOnline, "Freiwillige Anmeldung");
        pruefe01(tfFreiwilligeAnmeldungEKDruckMoeglich, "Freiwillige Anmeldung - EK ermöglichen");
        pruefe01(tfFreiwilligeAnmeldungNurPapier, "Freiwillige Anmeldung - nur über Papier möglich");
        pruefe01(tfOnlineAbstimmungBerechtigungSeparatPruefen, "Online Abstimmung separate Berechtigung prüfen");
        pruefe01(tfHybridTeilnahmeAktiv, "MitgliederVersammlung Hybrid");
        pruefe0123(tfFreiwilligeAnmeldungMitVertretereingabe, "Freiwillige Anmeldung - mit Vertretereingabe");
        pruefe012(tfFreiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich,
                "Freiwillige Anmeldung - Zwei Personen für Minderjährige");
        pruefe01(tfOnlineTeilnahmeGastInSeparatemMenue, "Gäste OT separates Menü");
        pruefe01(tfOnlineTeilnahmeNurFuerFreiwilligAngemeldete, "Online-Teilnahme nur für Freiwillig angemeldete");
        pruefeZahlNichtLeerLaenge(tfZweiterMandantNr, "Zweiter Mandant - Nr", 5);
        pruefeZahlNichtLeerLaenge(tfZweiterMandantHVJahr, "Zweiter Mandant - HV-Jahr", 5);

        pruefe01(tfRegisterAnbindung, "Register-Anbindung aktiv");
        pruefe01(tfRegisterAnbindungOberflaeche, "Register-Anbindung Oberfläche");
        pruefe01(tfRegisterAnbindungCheckBeiLogin, "Register-Anbindung Check bei Login");

        pruefe01(tfBestaetigenHinweisP1, "Bestätigen Hinweis 1");
        pruefe01(tfBestaetigenHinweisP2, "Bestätigen Hinweis 2");

        pruefe01(tfInHVPortalKeineEinstellungenFuerAktionaere, "Keine Einstellungen für Aktionäre");
        pruefe01(tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere, "Keine E-Mail/Passwort für Aktionäre");

        pruefe01(tfRegisterAnbindungZurHV, "Registerportal zum HV-Portal");
        pruefe01(tfRegisterAnbindungVonHV, "Vom HV-Portal zum Registerportal");

        this.pruefeLaenge(tfApi_client_id, "api_client_id", 250);
        this.pruefeLaenge(tfApi_client_secret, "api_client_secret", 250);
        this.pruefeLaenge(tfApi_base_url, "api_base_url", 250);
        this.pruefeLaenge(tfApi_key_id, "api_key_id", 250);
        pruefeZahlNichtLeerLaenge(tfApi_jwt_expiration_time, "api_jwt_expiration_time", 7);

        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return false;
        }

        /****** Allgemein ********/
        ParamS.param.paramBasis.zweiterMandantNr = Integer.parseInt(tfZweiterMandantNr.getText());
        ParamS.param.paramBasis.zweiterMandantHVJahr = Integer.parseInt(tfZweiterMandantHVJahr.getText());
        ParamS.param.paramBasis.zweiterMandantHVNummer = tfZweiterMandantLfdNr.getText();
        ParamS.param.paramBasis.zweiterMandantDatenbereich = tfZweiterMandantDB.getText();

        /****** Portal *************/

        ParamS.param.paramPortal.varianteDialogablauf = Integer.parseInt(tfVarianteDialogablauf.getText());

        ParamS.param.paramPortal.lfdHVDialogVeranstaltungenInMenue = Integer.parseInt(tfDialogveranstaltung.getText());
        ParamS.param.paramPortal.lfdHVBeiratswahlInMenue = Integer.parseInt(tfBeiratswahl.getText());
        ParamS.param.paramPortal.lfdHVGeneralversammlungInMenue = Integer.parseInt(tfGeneralversammlung.getText());
        ParamS.param.paramPortal.lfdHVGeneralversammlungBriefwahlInMenue = Integer
                .parseInt(tfGeneralversammlungBriefwahl.getText());
        ParamS.param.paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue = Integer
                .parseInt(tfGeneralversammlungTeilnahme.getText());
        ParamS.param.paramPortal.lfdHVGeneralversammlungTeilnahmeGast = Integer
                .parseInt(tfGeneralversammlungGast.getText());
        ParamS.param.paramPortal.lfdHVEinstellungenInMenue = Integer.parseInt(tfEinstellungen.getText());
        ParamS.param.paramPortal.lfdHVUnterlagenInMenue = Integer.parseInt(tfUnterlagen.getText());

        ParamS.param.paramPortal.dialogveranstaltungAktiv = Integer.parseInt(tfDialogveranstaltungAktiv.getText());

        if (cbIpTracking.isSelected()) {
            ParamS.param.paramPortal.loginIPTrackingAktiv = 1;
        } else {
            ParamS.param.paramPortal.loginIPTrackingAktiv = 0;
        }

        ParamS.param.paramPortal.verfahrenPasswortVergessenAblauf = Integer
                .parseInt(tfVerfahrenPasswortVergessenAblauf.getText());
        ParamS.param.paramPortal.nurRawLiveAbstimmung = Integer.parseInt(tfNurRawLiveAbstimmung.getText());
        ParamS.param.paramPortal.anzeigeStimmen = Integer.parseInt(tfAnzeigeStimmen.getText());
        ParamS.param.paramPortal.veranstaltungMailVerschicken = Integer
                .parseInt(tfVeranstaltungMailVerschicken.getText());
        ParamS.param.paramPortal.veranstaltungPersonenzahlEingeben = Integer
                .parseInt(tfVeranstaltungPersonenzahlEingeben.getText());
        ParamS.param.paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe = Integer
                .parseInt(tfVeranstaltungMehrfachAuswaehlbarJeGruppe.getText());

        ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[0] = cbVeranstaltungenAktivFuerGattung1.isSelected();
        ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[1] = cbVeranstaltungenAktivFuerGattung2.isSelected();
        ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[2] = cbVeranstaltungenAktivFuerGattung3.isSelected();
        ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[3] = cbVeranstaltungenAktivFuerGattung4.isSelected();
        ParamS.param.paramPortal.veranstaltungenAktivFuerGattung[4] = cbVeranstaltungenAktivFuerGattung5.isSelected();

        ParamS.param.paramPortal.freiwillingAnmeldenPraesenzOderOnline = Integer
                .parseInt(tfFreiwillingAnmeldenPraesenzOderOnline.getText());
        ParamS.param.paramPortal.freiwilligeAnmeldungEKDruckMoeglich = Integer
                .parseInt(tfFreiwilligeAnmeldungEKDruckMoeglich.getText());
        ParamS.param.paramPortal.freiwilligeAnmeldungNurPapier = Integer
                .parseInt(tfFreiwilligeAnmeldungNurPapier.getText());
        ParamS.param.paramPortal.onlineAbstimmungBerechtigungSeparatPruefen = Integer
                .parseInt(tfOnlineAbstimmungBerechtigungSeparatPruefen.getText());
        ParamS.param.paramPortal.hybridTeilnahmeAktiv = Integer.parseInt(tfHybridTeilnahmeAktiv.getText());
        ParamS.param.paramPortal.freiwilligeAnmeldungMitVertretereingabe = Integer
                .parseInt(tfFreiwilligeAnmeldungMitVertretereingabe.getText());
        ParamS.param.paramPortal.freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich = Integer
                .parseInt(tfFreiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich.getText());

        ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[0] = cbFreiwilligeAnmeldungAktivFuerGattung1
                .isSelected();
        ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[1] = cbFreiwilligeAnmeldungAktivFuerGattung2
                .isSelected();
        ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[2] = cbFreiwilligeAnmeldungAktivFuerGattung3
                .isSelected();
        ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[3] = cbFreiwilligeAnmeldungAktivFuerGattung4
                .isSelected();
        ParamS.param.paramPortal.freiwilligeAnmeldungAktivFuerGattung[4] = cbFreiwilligeAnmeldungAktivFuerGattung5
                .isSelected();

        ParamS.param.paramPortal.onlineTeilnahmeGastInSeparatemMenue = Integer
                .parseInt(tfOnlineTeilnahmeGastInSeparatemMenue.getText());
        ParamS.param.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete = Integer
                .parseInt(tfOnlineTeilnahmeNurFuerFreiwilligAngemeldete.getText());

        ParamS.param.paramPortal.registerAnbindung = Integer.parseInt(tfRegisterAnbindung.getText());
        ParamS.param.paramPortal.registerAnbindungOberflaeche = Integer
                .parseInt(tfRegisterAnbindungOberflaeche.getText());
        ParamS.param.paramPortal.registerAnbindungCheckBeiLogin = Integer
                .parseInt(tfRegisterAnbindungCheckBeiLogin.getText());

        ParamS.param.paramPortal.schreibeBestaetigenHinweisWeitere(1,
                Integer.parseInt(tfBestaetigenHinweisP1.getText()));
        ParamS.param.paramPortal.schreibeBestaetigenHinweisWeitere(2,
                Integer.parseInt(tfBestaetigenHinweisP2.getText()));

        ParamS.param.paramPortal.inHVPortalKeineEinstellungenFuerAktionaere = Integer
                .parseInt(tfInHVPortalKeineEinstellungenFuerAktionaere.getText());
        ParamS.param.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere = Integer
                .parseInt(tfInHVPortalKeineEmailUndKeinPasswortFuerAktionaere.getText());

        ParamS.param.paramPortal.registerAnbindungZurHV = Integer.parseInt(tfRegisterAnbindungZurHV.getText());
        ParamS.param.paramPortal.registerAnbindungVonHV = Integer.parseInt(tfRegisterAnbindungVonHV.getText());

        ParamS.param.paramPortal.api_client_id = tfApi_client_id.getText();
        ParamS.param.paramPortal.api_client_secret = tfApi_client_secret.getText();
        ParamS.param.paramPortal.api_base_url = tfApi_base_url.getText();
        ParamS.param.paramPortal.api_key_id = tfApi_key_id.getText();
        ParamS.param.paramPortal.api_jwt_expiration_time = Integer.parseInt(tfApi_jwt_expiration_time.getText());
        ParamS.param.paramPortal.api_key_name = tfApi_key_name.getText();
        ParamS.param.paramPortal.api_ping_url = tfApi_ping_url.getText();
        ParamS.param.paramPortal.apiku178_url = tfApiku178_url.getText();

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
