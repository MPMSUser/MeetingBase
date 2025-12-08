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

/**Maximal-Größe Fenster: 1850x950*/

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlEmittentenUebersicht;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginPasswortAendern;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;
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

    @FXML
    private AnchorPane rootPane;

    /** The location. */
    @FXML
    private URL location;

    /** The mn emitten uebersicht. */
    @FXML
    private MenuItem mnEmittenUebersicht;

    /** The mn mandant wechseln. */
    @FXML
    private MenuItem mnMandantWechseln;

    /** The mn beenden. */
    @FXML
    private MenuItem mnBeenden;

    /** The mn sammelkarten. */
    @FXML
    private MenuItem mnSammelkarten;

    /** The mn insti pflege. */
    @FXML
    private MenuItem mnInstiPflege;

    /** The mn einzelfall. */
    @FXML
    private MenuItem mnEinzelfall;

    /** The mn anmeldung. */
    @FXML
    private MenuItem mnAnmeldung;

    /** The mn 1 eintrittskarte selbst. */
    @FXML
    private MenuItem mn1EintrittskarteSelbst;

    /** The mn 1 eintrittskarte vertreter. */
    @FXML
    private MenuItem mn1EintrittskarteVertreter;

    /** The mn 2 eintrittskarte selbst. */
    @FXML
    private MenuItem mn2EintrittskarteSelbst;

    /** The mn 2 eintrittskarte selbst oder vertreter. */
    @FXML
    private MenuItem mn2EintrittskarteSelbstOderVertreter;

    /** The mn SRV. */
    @FXML
    private MenuItem mnSRV;

    /** The mn briefwahl. */
    @FXML
    private MenuItem mnBriefwahl;

    /** The mn KIAV. */
    @FXML
    private MenuItem mnKIAV;

    /** The mn dauervollmacht. */
    @FXML
    private MenuItem mnDauervollmacht;

    /** The mn orga. */
    @FXML
    private MenuItem mnOrga;

    /** The mn wek SRV. */
    @FXML
    private MenuItem mnWekSRV;

    /** The mn wek briefwahl. */
    @FXML
    private MenuItem mnWekBriefwahl;

    /** The mn sammelkarte zuordnen. */
    @FXML
    private MenuItem mnSammelkarteZuordnen;

    /** The mn stapelverarbeitung. */
    @FXML
    private MenuItem mnStapelverarbeitung;

    /** The mn insti prov. */
    @FXML
    private MenuItem mnInstiProv;

    /** The mn versandadresse pruefen. */
    @FXML
    private MenuItem mnVersandadressePruefen;

    /** The mn eintrittskkarten drucken. */
    @FXML
    private MenuItem mnEintrittskkartenDrucken;

    /** The mn stimmkarten drucken. */
    @FXML
    private MenuItem mnStimmkartenDrucken;

    /** The mn QS kontrolliste weisungen. */
    @FXML
    private MenuItem mnQSKontrollisteWeisungen;

    /** The mn personenprognose. */
    @FXML
    private MenuItem mnPersonenprognose;

    /** The mn QS weisungen bildschirm. */
    @FXML
    private MenuItem mnQSWeisungenBildschirm;

    /** The mn sperre null bestand. */
    @FXML
    private MenuItem mnSperreNullBestand;

    /** The mn passwort pruefen. */
    @FXML
    private MenuItem mnPasswortPruefen;

    /** The mn passwort drucken. */
    @FXML
    private MenuItem mnPasswortDrucken;


    /** The mn passwort aendern. */
    @FXML
    private MenuItem mnPasswortAendern;

    /** The mn InhaberImport import. */
    @FXML
    private MenuItem mnInhaberImportImport;

    /** The mn InhaberImport export. */
    @FXML
    private MenuItem mnInhaberImportExport;

    /** The mn InhaberImport protokoll. */
    @FXML
    private MenuItem mnInhaberImportProtokoll;

    /** The mn anforderungsstelle. */
    @FXML
    private MenuItem mnAnforderungsstelle;

    /** The mn import profil. */
    @FXML
    private MenuItem mnImportProfil;

    /** The mn eintrittskarten definition. */
    @FXML
    private MenuItem mnEintrittskartenDefinition;

    /** The mn aktienregister. */
    @FXML
    private MenuItem mnAktienregister;

    /** The lbl user. */
    @FXML
    private Label lblUser;

    /** The lbl consultant. */
    @FXML
    private Label lblConsultant;

    /** The lbl server. */
    @FXML
    private Label lblServer;

    /** The lbl daten status. */
    @FXML
    private Label lblDatenStatus;

    @FXML
    private Label lblImport;

    @FXML
    private GridPane hauptAnsicht;

    @FXML
    private CheckBox cbAnsicht;

    /**
     * ************************************ Sonstiges
     * **********************************.
     *
     * @param event the event
     */

    @FXML
    void gewWekBriefwahl(ActionEvent event) {

    }

    /**
     * Gew wek SRV.
     *
     * @param event the event
     */
    @FXML
    void gewWekSRV(ActionEvent event) {

    }

    /** The mn massentest. */
    @FXML
    private MenuItem mnMassentest;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        zeigeUser();
        zeigeImportZeit();

        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.H) {
                onCbAnsicht(new ActionEvent());
            } else if (e.getCode() == KeyCode.M) {
                gewModulauswahl(new ActionEvent());
            } else if (e.getCode() == KeyCode.W) {
                gewMandantWechseln(new ActionEvent());
            }
        });

        createHauptansicht();

        ObjectActions.createInfoButton(rootPane,
                new String[] { "H - Hauptansicht", "M - Modulauswahl", "W - Mandant Wechseln" }, 30.0, 5.0);
    }

    /**
     * Zeige user.
     */
    public void zeigeUser() {
        lblUser.setText(
                "Mandant=" + ParamS.clGlobalVar.mandant + " " + ParamS.eclEmittent.bezeichnungKurz + " Laufwerk="
                        + ParamS.clGlobalVar.lwPfadAllgemein + " User=" + ParamS.clGlobalVar.benutzernr + " WebService="
                        + ParamInterneKommunikation.webServicePfadZurAuswahlTest[ParamS.clGlobalVar.webServicePfadNr]);
        lblServer.setText(ParamInterneKommunikation.webServiceInfo[ParamS.clGlobalVar.webServicePfadNr]);

        lblConsultant.setText(ParamS.param.paramBasis.mailConsultant);

        if (ParamS.eclEmittent.liefereDatenbestandIstProduktiv() == false) {
            lblDatenStatus.setText("Achtung - Testdaten");
        } else {
            lblDatenStatus.setText("");
        }

    }

    private void zeigeImportZeit() {
        BlInhaberImport blInhaberImport = new BlInhaberImport(false, new DbBundle());
        blInhaberImport.readProtokoll();
        List<EclImportProtokoll> tmpList = blInhaberImport.importProtokoll.stream().filter(e -> e.getEkBis() == 0)
                .collect(Collectors.toList());

        tmpList.sort(Comparator.comparing(EclImportProtokoll::getUpdate).reversed());

        if (!tmpList.isEmpty()) {
            lblImport.setText("Letzter Registerimport: "
                    + CaDatumZeit.toGermanFormat(tmpList.get(tmpList.size() - 1).getUpdate()));
        } else {
            lblImport.setText("");
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
        CaIcon.bestandsverwaltung(zwischenDialog);

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
            // return rc;
            //
            // ParamS.eclEmittent=controllerDialog.eclEmittent;
            //
            // BlParameterUeberWebService lBlInitHVWebServices=new
            // BlParameterUeberWebService();
            // int rc=lBlInitHVWebServices.holeHVParameter();
            //
            if (rc < 1) { // Fehlerbehandlung
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Systemfehler " + "Fehler " + CaFehler.getFehlertext(rc, 0));
            }

            zeigeUser();
            zeigeImportZeit();
            CaController caEigenerController = new CaController();
            caEigenerController.setzeTitel(eigeneStage, eigenerTitel, eigenerTitelMitMandant);
        }
        return;
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
     * ***************************** Datenbasis
     * ********************************************.
     *
     * @param event the event
     */
    @FXML
    public void gewAktienregister(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlAktienregister controllerFenster = new CtrlAktienregister();
        controllerFenster.init(newStage, 2);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/Aktienregister.fxml", controllerFenster.width,
                controllerFenster.height, "Aktienregister", true);
    }

    /**
     * Gew insti pflege.
     *
     * @param event the event
     */
    @FXML
    void gewInstiPflege(ActionEvent event) {
        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlInstiPflege controllerFenster = new CtrlInstiPflege();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/InstiPflege.fxml",
                1500, 760, "Institutionelle Pflege", true);
    }

    /**
     * Gew sammelkarten.
     *
     * @param event the event
     */
    @FXML
    public void gewSammelkarten(ActionEvent event) {
        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlSammelkarten controllerFenster = new CtrlSammelkarten();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/Sammelkarten.fxml",
                1500, 760, "Sammelkarten", true);
    }


    /**
     * Gew einzelfall.
     *
     * @param event the event
     */
    @FXML
    public void gewEinzelfall(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlStatus controllerStatus = new CtrlStatus();
        controllerStatus.init(newStage, 0);

        CaController caController = new CaController();
        caController.open(newStage, controllerStatus, "/de/meetingapps/meetingclient/meetingBestand/Status.fxml", 1150,
                760, "Einzelfallbearbeitung", true);

    }

    /**
     * Gew anmeldungen.
     *
     * @param event the event
     */
    @FXML
    void gewAnmeldungen(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlAnmeldung controllerAnmeldung = new CtrlAnmeldung();

        controllerAnmeldung.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Anmeldung.fxml"));
        loader.setController(controllerAnmeldung);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Stapelerfassung - Nur Anmelden");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew 1 eintrittskarte selbst.
     *
     * @param event the event
     */
    @FXML
    void gew1EintrittskarteSelbst(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlEineEintrittskarteSelbst controllerEineEintrittskarteSelbst = new CtrlEineEintrittskarteSelbst();

        controllerEineEintrittskarteSelbst.init(neuerDialog, 1, null, null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EineEintrittskarteSelbst.fxml"));
        loader.setController(controllerEineEintrittskarteSelbst);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gew1EintrittskarteSelbst 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Stapelerfassung - Eine Eintrittskarte Selbst");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew 1 eintrittskarte vertreter.
     *
     * @param event the event
     */
    @FXML
    void gew1EintrittskarteVertreter(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlEineEintrittskarteVollmacht controllerEineEintrittskarteVollmacht = new CtrlEineEintrittskarteVollmacht();

        controllerEineEintrittskarteVollmacht.init(neuerDialog, 1, null, null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EineEintrittskarteVollmacht.fxml"));
        loader.setController(controllerEineEintrittskarteVollmacht);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gew1EintrittskarteVertreter 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Stapelerfassung - Eine Eintrittskarte Vollmacht");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew 2 eintrittskarte selbst.
     *
     * @param event the event
     */
    @FXML
    void gew2EintrittskarteSelbst(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlZweiEintrittskarteSelbst controllerZweiEintrittskarteSelbst = new CtrlZweiEintrittskarteSelbst();

        controllerZweiEintrittskarteSelbst.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ZweiEintrittskarteSelbst.fxml"));
        loader.setController(controllerZweiEintrittskarteSelbst);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gew2EintrittskarteSelbst 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Stapelerfassung - Zwei Eintrittskarten Selbst");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew 2 eintrittskarte selbst oder vertreter.
     *
     * @param event the event
     */
    @FXML
    void gew2EintrittskarteSelbstOderVertreter(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlZweiEintrittskarteSelbstOderVertreter controllerZweiEintrittskarteSelbstOderVertreter = new CtrlZweiEintrittskarteSelbstOderVertreter();

        controllerZweiEintrittskarteSelbstOderVertreter.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ZweiEintrittskarteSelbstOderVertreter.fxml"));
        loader.setController(controllerZweiEintrittskarteSelbstOderVertreter);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gew2EintrittskarteSelbstOderVertreter 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1250, 800);
        neuerDialog.setTitle("Stapelerfassung - Zwei Eintrittskarten Selbst oder Vertreter");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew SRV.
     *
     * @param event the event
     */
    @FXML
    void gewSRV(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 4, 1, null, null, null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Stapelerfassung - Vollmacht/Weisung Stimmrechtsvertreter", true);
    }

    /**
     * Gew briefwahl.
     *
     * @param event the event
     */
    @FXML
    void gewBriefwahl(ActionEvent event) {/* nutzt selbe Maske wie Stimmrechtsvertreter, nur mit anderem Init-Wert */
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 5, 1, null, null, null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Stapelerfassung - Briefwahl", true);
    }

    /**
     * Gew KIAV.
     *
     * @param event the event
     */
    @FXML
    void gewKIAV(ActionEvent event) {/* KIAV */
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 6, 1, null, null, null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Stapelerfassung - KIAV", true);
    }

    /**
     * Gew orga.
     *
     * @param event the event
     */
    @FXML
    void gewOrga(ActionEvent event) {/* Organisatorisch - selber Aufruf wie KIAV, nur andere Funktion */
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 35, 1, null, null, null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Stapelerfassung - Organisatorisch in Sammelkarte", true);
    }

    /**
     * Gew dauervollmacht.
     *
     * @param event the event
     */
    @FXML
    void gewDauervollmacht(ActionEvent event) {/* Dauervollmacht - selber Aufruf wie KIAV, nur andere Funktion */
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 31, 1, null, null, null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Stapelerfassung - Dauervollmacht", true);
    }

    /**
     * Gew sammelkarte zuordnen.
     *
     * @param event the event
     */
    @FXML
    void gewSammelkarteZuordnen(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);
        controllerFenster.durchsuchenInSammelkarten = false;
        controllerFenster.mehrfachAuswahlZulaessig = true;
        controllerFenster.selektionNichtAngemeldeteMoeglich = false;

        controllerFenster.funktionsButtonSammelkarte = true;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, "Suchen", true);

    }

    /**
     * Gew stapelverarbeitung.
     *
     * @param event the event
     */
    @FXML
    void gewStapelverarbeitung(ActionEvent event) {
        CtrlStapelVerarbeitung controllerFenster = new CtrlStapelVerarbeitung();
        controllerFenster.init();
        openController(controllerFenster, "StapelVerarbeitung.fxml", 1500, 700, "Stapelverarbeitung");
    }

    /**
     * Gew insti prov.
     *
     * @param event the event
     */
    @FXML
    void gewInstiProv(ActionEvent event) {
        CtrlInstiProv controllerFenster = new CtrlInstiProv();
        controllerFenster.init();
        openController(controllerFenster, "InstiProv.fxml", 1500, 850, "InstiProv");
    }

    /**
     * Gew versandadresse pruefen.
     *
     * @param event the event
     */
    @FXML
    void gewVersandadressePruefen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlVersandAdressePruefenAuswahl controllerVersandAdressePruefenAuswahl = new CtrlVersandAdressePruefenAuswahl();

        controllerVersandAdressePruefenAuswahl.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VersandAdressePruefenAuswahl.fxml"));
        loader.setController(controllerVersandAdressePruefenAuswahl);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewVersandadressePruefen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1100, 800);
        neuerDialog.setTitle("Prüfen Versandadresse");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew eintrittskarten drucken.
     *
     * @param event the event
     */
    @FXML
    void gewEintrittskartenDrucken(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlEintrittskarteDrucken ctrlEintrittskarteDrucken = new CtrlEintrittskarteDrucken();

        ctrlEintrittskarteDrucken.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EintrittskartenDrucken.fxml"));
        loader.setController(ctrlEintrittskarteDrucken);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewEintrittskartenDrucken 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1400, 800);
        neuerDialog.setTitle("Eintrittskarte Drucken");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew stimmkarten drucken.
     *
     * @param event the event
     */
    @FXML
    void gewStimmkartenDrucken(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlStimmkartenDrucken ctrlStimmkartenDrucken = new CtrlStimmkartenDrucken();

        ctrlStimmkartenDrucken.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StimmkartenDrucken.fxml"));
        loader.setController(ctrlStimmkartenDrucken);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1400, 800);
        neuerDialog.setTitle("Stimmkarten Drucken");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Gew QS kontrolliste weisungen.
     *
     * @param event the event
     */
    @FXML
    void gewQSKontrollisteWeisungen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlQSKontrollisteWeisungen controllerFenster = new CtrlQSKontrollisteWeisungen();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/RootDrucklauf.fxml", 1140, 700,
                "QS Kontrolliste Weisungen", true);
    }

    /**
     * Gew QS weisungen bildschirm.
     *
     * @param event the event
     */
    @FXML
    void gewQSWeisungenBildschirm(ActionEvent event) {
        CtrlQSWeisungenBildschirm controllerFenster = new CtrlQSWeisungenBildschirm();
        controllerFenster.init();
        openController(controllerFenster, "QSWeisungenBildschirm.fxml", 1500, 760, "QS Weisungen Bildschirm");
    }

    /**
     * Gew sperre null bestand.
     *
     * @param event the event
     */
    @FXML
    void gewSperreNullBestand(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlSperreNullBestand controllerFenster = new CtrlSperreNullBestand();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();

        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SperreNullBestand.fxml", 590, 500, "Sperren 0-Bestände",
                true);
    }

    /**
     * Gew passwort pruefen.
     *
     * @param event the event
     */
    @FXML
    void gewPasswortPruefen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlPasswortPruefen controllerFenster = new CtrlPasswortPruefen();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();

        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/PasswortPruefen.fxml", 1850, 950,
                "Passwort-Anforderungen prüfen", true);
    }

    /**
     * Gew passwort drucken.
     *
     * @param event the event
     */
    @FXML
    void gewPasswortDrucken(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrlPasswortDrucken controllerFenster = new CtrlPasswortDrucken();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/RootDrucklauf.fxml", 1140, 700,
                "Passwort-Anforderungen drucken", true);
    }


    /**
     * Gew personenprognose.
     *
     * @param event the event
     */
    @FXML
    void gewPersonenprognose(ActionEvent event) {
        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlPersonenprognose controllerPersonenprognose = new CtrlPersonenprognose();
        controllerPersonenprognose.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerPersonenprognose,
                "/de/meetingapps/meetingclient/meetingBestand/Personenprognose.fxml", 800, 500, "Personenprognose",
                true);

    }

    /**
     * Gew massentest.
     *
     * @param event the event
     */
    @FXML
    void gewMassentest(ActionEvent event) {
        Stage massentestdialog = new Stage();

        CtrlMassentest controllerMassentest = new CtrlMassentest();

        controllerMassentest.init(massentestdialog);

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("Massentest.fxml"));
        loader1.setController(controllerMassentest);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewMassentest 001");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 500, 300);
        massentestdialog.setTitle("Massentest Briefwahl");
        massentestdialog.setScene(scene1);
        massentestdialog.initModality(Modality.APPLICATION_MODAL);
        massentestdialog.showAndWait();

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
        neuerDialog.setTitle(pTitel);
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
    }

    /**
     * Gew InhaberImport import.
     *
     * @param event the event
     */
    @FXML
    void gewInhaberImportImport(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlInhaberImport ctrlInhaberImportImport = new CtrlInhaberImport();
        ctrlInhaberImportImport.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, ctrlInhaberImportImport, "/de/meetingapps/meetingclient/meetingBestand/InhaberImport.fxml", 1070,
                600, "InhaberImport Import", true);

    }

    /**
     * Gew InhaberImport export.
     *
     * @param event the event
     */
    @FXML
    void gewInhaberImportExport(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlInhaberImportExport ctrlInhaberImportExport = new CtrlInhaberImportExport();
        ctrlInhaberImportExport.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, ctrlInhaberImportExport, "/de/meetingapps/meetingclient/meetingBestand/InhaberImportExport.fxml",
                720, 480, "InhaberImport Export", true);

    }

    /**
     * Gew InhaberImport protokoll.
     *
     * @param event the event
     */
    @FXML
    void gewInhaberImportProtokoll(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlImportProtokoll controller = new CtrlImportProtokoll();
        controller.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controller, "/de/meetingapps/meetingclient/meetingBestand/ImportProtokoll.fxml",
                controller.width, controller.height, "InhaberImport Protokoll", true);

    }

    /**
     * Gew anforderungsstelle.
     *
     * @param event the event
     */
    @FXML
    void gewAnforderungsstelle(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlAnmeldestelle controllerFenster = new CtrlAnmeldestelle();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/Anmeldestelle.fxml", 1120, 720, "Anmeldestelle", true);

    }

    /**
     * Gew import profil.
     *
     * @param event the event
     */
    @FXML
    void gewImportProfil(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlImportProfil controllerFenster = new CtrlImportProfil();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/ImportProfil.fxml",
                1500, 760, "Import Profile", true);

    }

    /**
     * Gew eintrittskarten definition.
     *
     * @param event the event
     */
    @FXML
    void gewEintrittskartenDefinition(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlEintrittskartenDefinition controllerFenster = new CtrlEintrittskartenDefinition();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/EintrittskartenDefinition.fxml", 930, 600,
                "Eintrittskartendefinition", true);

    }

    private void createHauptansicht() {

        Button btn1 = createButton("Bestand", new FontIcon(FontAwesomeSolid.ADDRESS_BOOK), true);
        btn1.setOnAction(e -> gewAktienregister(new ActionEvent()));
        hauptAnsicht.add(btn1, 0, 0);

        btn1 = createButton("Sammelkarten", new FontIcon(FontAwesomeSolid.LIST_ALT), true);
        btn1.setOnAction(e -> gewSammelkarten(new ActionEvent()));
        hauptAnsicht.add(btn1, 1, 0);

        btn1 = createButton("Einzelfall", new FontIcon(FontAwesomeSolid.USER), true);
        btn1.setOnAction(e -> gewEinzelfall(new ActionEvent()));
        hauptAnsicht.add(btn1, 2, 0);

        btn1 = createButton("Import", new FontIcon(FontAwesomeSolid.FILE_IMPORT), true);
        btn1.setOnAction(e -> gewInhaberImportImport(new ActionEvent()));
        hauptAnsicht.add(btn1, 3, 0);

        btn1 = createButton("Eintrittskarte drucken", new FontIcon(FontAwesomeSolid.PRINT), true);
        btn1.setOnAction(e -> gewEintrittskartenDrucken(new ActionEvent()));
        hauptAnsicht.add(btn1, 0, 1);

        btn1 = createButton("Stimmkarten drucken", new FontIcon(FontAwesomeSolid.PRINT), true);
        btn1.setOnAction(e -> gewStimmkartenDrucken(new ActionEvent()));
        hauptAnsicht.add(btn1, 1, 1);

        btn1 = createButton("Kontrolliste Weisungen", new FontIcon(FontAwesomeSolid.CLIPBOARD_CHECK), true);
        btn1.setOnAction(e -> gewQSKontrollisteWeisungen(new ActionEvent()));
        hauptAnsicht.add(btn1, 2, 1);

        btn1 = createButton("Personenprognose", new FontIcon(FontAwesomeSolid.CHART_LINE), true);
        btn1.setOnAction(e -> gewPersonenprognose(new ActionEvent()));
        hauptAnsicht.add(btn1, 3, 1);
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
        hauptAnsicht.setVisible(!hauptAnsicht.isVisible());
        cbAnsicht.setSelected(hauptAnsicht.isVisible());
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
