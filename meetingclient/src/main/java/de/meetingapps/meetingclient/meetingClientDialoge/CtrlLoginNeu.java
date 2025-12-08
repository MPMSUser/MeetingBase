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
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientAllg.CaPruefeLogin;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingHVMaster.CtrlMandantAnlegen;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubCtrlLogin;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlLoginNeu.
 */
public class CtrlLoginNeu extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf kennung. */
    @FXML
    private TextField tfKennung;

    /** The tf passwort. */
    @FXML
    private PasswordField tfPasswort;

    /** The cb mandant. */
    @FXML
    private ComboBox<CbElement> cbMandant;

    /** The cb mandant allgemein. */
    private CbAllgemein cbMandantAllgemein = null;

    /** The cb HV jahr. */
    @FXML
    private ComboBox<CbElement> cbHVJahr;

    /** The cb HV jahr allgemein. */
    private CbAllgemein cbHVJahrAllgemein = null;

    /** The cb HV nummer. */
    @FXML
    private ComboBox<CbElement> cbHVNummer;

    /** The cb HV nummer allgemein. */
    private CbAllgemein cbHVNummerAllgemein = null;

    /** The cb datenbereich. */
    @FXML
    private ComboBox<CbElement> cbDatenbereich;

    /** The cb datenbereich allgemein. */
    private CbAllgemein cbDatenbereichAllgemein = null;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The btn neue HV. */
    @FXML
    private Button btnNeueHV;

    /** The stub ctrl login. */
    private StubCtrlLogin stubCtrlLogin = null;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** Array aller Mandanten - Basis für die folgenden Listen. */
    private EclEmittenten[] mandantenArray = null;

    /**
     * 0=noch nichts ausgewählt 
     * 1=Emittent ausgewählt 
     * 2=HV-Jahr ausgewählt
     * 3=HV-Nummer ausgewählt 
     * 4=Datenbankbereich ausgewählt.
     */
    private int auswahlstufe = 0;

    /** The gewaehlter emittent. */
    private int gewaehlterEmittent = 0;

    /** The gewaehltes HV jahr. */
    private int gewaehltesHVJahr = 0;

    /** The gewaehlte HV nummer. */
    private String gewaehlteHVNummer = "";

    /** Liste aller Emittenten - d.h. komprimiert, gleiche Jahre etc. eliminiert */
    private List<EclEmittenten> emittentenListe = null;

    /** The hv jahr liste. */
    private List<Integer> hvJahrListe = null;

    /** The hv nummer liste. */
    private List<String> hvNummerListe = null;

    /** The datenbereich liste. */
    private List<String> datenbereichListe = null;

    /** The l param geraet. */
    private ParamGeraet lParamGeraet = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfKennung != null : "fx:id=\"tfKennung\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfPasswort != null : "fx:id=\"tfPasswort\" was not injected: check your FXML file 'Login.fxml'.";
        assert cbMandant != null : "fx:id=\"cbMandant\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnStarten != null : "fx:id=\"btnStarten\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnNeueHV != null : "fx:id=\"btnNeueHV\" was not injected: check your FXML file 'Login.fxml'.";

        /************* Ab hier individuell ********************************************/
        lParamGeraet = ParamS.paramGeraet;

        cbMandant.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 != 0) {
                    gewaehlterEmittent = neuerWert.ident1;
                    fuelleHVJahr();
                }
            }
        });

        cbHVJahr.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 != 0) {
                    gewaehltesHVJahr = neuerWert.ident1;
                    fuelleHVNummerListe();
                }
            }
        });

        cbHVNummer.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 != 0) {
                    gewaehlteHVNummer = neuerWert.anzeige;
                    fuelleDatenbereichListe();
                }
            }
        });

        CaBug.druckeLog("A", logDrucken, 10);
        dbBundle = new DbBundle();
        stubCtrlLogin = new StubCtrlLogin(false, dbBundle);

        initFuerEmittentenAuswahl();
        CaBug.druckeLog("B", logDrucken, 10);

        /* Kennung, Passwort */
        if (!lParamGeraet.festgelegterBenutzername.isEmpty() || nurMandantenwechsel) {/* Keine Auswahl */
            if (nurMandantenwechsel == false) {
                tfKennung.setText(lParamGeraet.festgelegterBenutzername);
            } else {
                tfKennung.setText(ParamS.eclUserLogin.kennung); // User dann bereits eingeloggt
                Platform.runLater(() -> cbMandant.requestFocus());
            }
            if (lParamGeraet.festgelegteBenutzernameFix == true || nurMandantenwechsel) {
                tfKennung.setEditable(false);
                tfPasswort.setEditable(false);
            }
        }
        if ((lParamGeraet.festgelegterBenutzername.isEmpty() || lParamGeraet.festgelegteBenutzernameFix == false)
                && nurMandantenwechsel == false) {
            Platform.runLater(() -> tfKennung.requestFocus());
        }
        CaBug.druckeLog("C", logDrucken, 10);

        ComboBoxZusatz.configureCbElement(cbMandant);
        btnNeueHV.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));

        CaBug.druckeLog("D", logDrucken, 10);

        cbMandant.setStyle("-fx-font-size: 15");

    }

    /**
     * Inits the fuer emittenten auswahl.
     */
    private void initFuerEmittentenAuswahl() {

        /** Alle Mandanten einlesen */
        stubCtrlLogin.liefereMandantenArray();
        mandantenArray = stubCtrlLogin.rcMandantenArray;

        auswahlstufe = 0;
        aktiviereAuswahlstufe();
    }

    /**
     * Aktiviere auswahlstufe.
     */
    private void aktiviereAuswahlstufe() {
        switch (auswahlstufe) {
        case 0: {
            cbMandant.setDisable(false);
            cbHVJahr.setDisable(true);
            cbHVNummer.setDisable(true);
            cbDatenbereich.setDisable(true);
            fuelleEmittenten();
            break;
        }
        }
    }

    /**
     * Fuelle emittenten.
     */
    private void fuelleEmittenten() {
        cbHVJahr.setDisable(true);
        cbHVJahr.getItems().clear();
        cbHVNummer.setDisable(true);
        cbHVNummer.getItems().clear();
        cbDatenbereich.setDisable(true);
        cbDatenbereich.getItems().clear();

        BvMandanten bvMandanten = new BvMandanten();
        emittentenListe = bvMandanten.liefereEmittentenListeFuerNrAuswahl(mandantenArray);

        cbMandantAllgemein = new CbAllgemein(cbMandant);
        for (int i = 0; i < emittentenListe.size(); i++) {
            CbElement lElement = new CbElement();
            lElement.ident1 = emittentenListe.get(i).mandant;
            lElement.anzeige = CaString.fuelleLinksNull(Integer.toString(emittentenListe.get(i).mandant), 3) + " "
                    + emittentenListe.get(i).bezeichnungKurz;
            if (lParamGeraet.festgelegterMandant == lElement.ident1) {
                cbMandantAllgemein.addElementAusgewaehlt(lElement);
            } else {
                if (lParamGeraet.festgelegterMandantIstFix == false || lParamGeraet.festgelegterMandant == 0) {
                    cbMandantAllgemein.addElement(lElement);
                }
            }
        }
    }

    /**
     * Fuelle HV jahr.
     */
    private void fuelleHVJahr() {
        cbHVJahr.setDisable(false);
        cbHVNummer.setDisable(true);
        cbHVNummer.getItems().clear();
        cbDatenbereich.setDisable(true);
        cbDatenbereich.getItems().clear();

        BvMandanten bvMandanten = new BvMandanten();
        hvJahrListe = bvMandanten.liefereHVJahrListe(mandantenArray, gewaehlterEmittent);

        cbHVJahrAllgemein = new CbAllgemein(cbHVJahr);
        for (int i = 0; i < hvJahrListe.size(); i++) {
            CbElement lElement = new CbElement();
            lElement.ident1 = hvJahrListe.get(i);
            lElement.anzeige = CaString.fuelleLinksNull(Integer.toString(hvJahrListe.get(i)), 4);
            if (lParamGeraet.festgelegtesJahr == lElement.ident1) {
                cbHVJahrAllgemein.addElementAusgewaehlt(lElement);
            } else {
                if (lParamGeraet.festgelegtesJahrIstFix == false || lParamGeraet.festgelegtesJahr == 0) {
                    cbHVJahrAllgemein.addElement(lElement);
                }
            }
        }

    }

    /**
     * Fuelle HV nummer liste.
     */
    private void fuelleHVNummerListe() {
        cbHVNummer.setDisable(false);
        cbDatenbereich.setDisable(true);
        cbDatenbereich.getItems().clear();

        BvMandanten bvMandanten = new BvMandanten();
        hvNummerListe = bvMandanten.liefereHVNummerListe(mandantenArray, gewaehlterEmittent, gewaehltesHVJahr);

        cbHVNummerAllgemein = new CbAllgemein(cbHVNummer);
        for (int i = 0; i < hvNummerListe.size(); i++) {
            CbElement lElement = new CbElement();
            lElement.ident1 = i + 1;
            lElement.anzeige = hvNummerListe.get(i);
            if (lParamGeraet.festgelegteHVNummer.contentEquals(lElement.anzeige)) {
                cbHVNummerAllgemein.addElementAusgewaehlt(lElement);
            } else {
                if (lParamGeraet.festgelegteHVNummerFix == false || lParamGeraet.festgelegteHVNummer.equals("")) {
                    cbHVNummerAllgemein.addElement(lElement);
                }
            }
        }
    }

    /**
     * Fuelle datenbereich liste.
     */
    private void fuelleDatenbereichListe() {
        cbDatenbereich.setDisable(false);

        BvMandanten bvMandanten = new BvMandanten();
        datenbereichListe = bvMandanten.liefereDatenbankListe(mandantenArray, gewaehlterEmittent, gewaehltesHVJahr,
                gewaehlteHVNummer);

        cbDatenbereichAllgemein = new CbAllgemein(cbDatenbereich);
        for (int i = 0; i < datenbereichListe.size(); i++) {
            CbElement lElement = new CbElement();
            lElement.ident1 = i;
            lElement.anzeige = datenbereichListe.get(i);
            if (lParamGeraet.festgelegteDatenbank.contentEquals(lElement.anzeige)) {
                cbDatenbereichAllgemein.addElementAusgewaehlt(lElement);
            } else {
                if (lParamGeraet.festgelegteDatenbankFix == false || lParamGeraet.festgelegteDatenbank.equals("")) {
                    cbDatenbereichAllgemein.addElement(lElement);
                }
            }
        }
    }

    /************** Logik *******************/

    /* afFalscheKennung 
     * afPasswortFalsch
     *
     * afHVNichtVorhanden 
     * afHVMitDieserKennungNichtZulaessig 
     * afKennungGesperrt
     * afNeuesPasswortErforderlich
     *
     * 1 = erfolgreich
     */
    private int pruefeLogin(boolean mitMandantHolen, boolean pNurKennungPruefen) {
        boolean pNurMandantPruefen = false;

        if (nurMandantenwechsel) {
            pNurMandantPruefen = true;
        }

        int rc = 0;

        CaPruefeLogin caPruefeLogin = new CaPruefeLogin();
        rc = caPruefeLogin.pruefeLogin(tfKennung.getText(), tfPasswort.getText(), pNurMandantPruefen,
                pNurKennungPruefen);
        CaBug.druckeLog("rc=" + rc, logDrucken, 10);
        eclUserLogin = caPruefeLogin.rcUserLogin;
        ParamS.eclUserLogin = eclUserLogin;
        eclEmittent = caPruefeLogin.rcEmittent;
        ParamS.eclEmittent = caPruefeLogin.rcEmittent;
        if (rc < 0) {
            /* Passwort ändern erforderlich */
            if (rc == CaFehler.afNeuesPasswortErforderlich) {
                this.fehlerMeldung("Passwort-Änderung erforderlich!");
                lassePasswortAendern();
                tfKennung.setText("");
                tfPasswort.setText("");
                tfKennung.requestFocus();
                return rc;
            }

            /* Fehlersituation */
            String fehlerText = "";
            switch (rc) {
            case CaFehler.afFalscheKennung:
            case CaFehler.afPasswortFalsch:
                fehlerText = "Kennung oder Passwort falsch!";
                break;
            case CaFehler.afKennungGesperrt:
                fehlerText = "Kennung gesperrt!";
                break;
            case CaFehler.afHVNichtVorhanden:
                fehlerText = "Ausgewählter Emittent / HV nicht vorhanden!";
                break;
            case CaFehler.afHVMitDieserKennungNichtZulaessig:
                fehlerText = "Ausgewählter Emittent / HV darf mit dieser Kennung nicht bearbeitet werden!";
                break;
            }
            this.fehlerMeldung(fehlerText);

            return rc;
        }

        return 1;
    }

    /**
     * Lasse passwort aendern.
     */
    private void lassePasswortAendern() {
        if (nurMandantenwechsel) {
            return;
        }
        Stage zwischenDialog = new Stage();
        CaIcon.standard(zwischenDialog);

        CtrlLoginPasswortAendern controllerDialog = new CtrlLoginPasswortAendern();

        controllerDialog.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerDialog,
                "/de/meetingapps/meetingclient/meetingClientDialoge/LoginPasswortAendern.fxml", 700, 400,
                "Ändern Passwort", true);
    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        fortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * Btn starten clicked.
     *
     * @param event the event
     */
    @FXML
    void btnStartenClicked(ActionEvent event) {
        CbElement lCbElement = null;

        /* Eingaben überprüfen */
        if (cbMandant.getValue() == null || cbMandant.getValue().ident1 == 0) {
            this.fehlerMeldung("Bitte Mandant auswählen");
            Platform.runLater(() -> cbMandant.requestFocus());
            return;
        }

        lCbElement = cbHVJahr.getValue();
        if (lCbElement == null) {
            this.fehlerMeldung("Bitte HV-Jahr auswählen");
            Platform.runLater(() -> cbHVJahr.requestFocus());
            return;
        }

        lCbElement = cbHVNummer.getValue();
        if (lCbElement == null) {
            this.fehlerMeldung("Bitte HV-Nummer auswählen");
            Platform.runLater(() -> cbHVNummer.requestFocus());
            return;
        }

        lCbElement = cbDatenbereich.getValue();
        if (lCbElement == null) {
            this.fehlerMeldung("Bitte Datenbereich auswählen");
            Platform.runLater(() -> cbDatenbereich.requestFocus());
            return;
        }

        /* Variablen belegen */
        ParamS.clGlobalVar.mandant = cbMandant.getValue().ident1;
        ParamS.clGlobalVar.hvJahr = cbHVJahr.getValue().ident1;
        ParamS.clGlobalVar.hvNummer = cbHVNummer.getValue().anzeige;
        ParamS.clGlobalVar.datenbereich = cbDatenbereich.getValue().anzeige;

        int rc = pruefeLogin(true, false);
        if (rc == 1) {
            fortsetzen = true;
            eigeneStage.hide();
        }

        return;
    }

    /**
     * Btn neue HV clicked.
     *
     * @param event the event
     */
    @FXML
    void btnNeueHVClicked(ActionEvent event) {

        /* Kennung abprüfen */
        int rc = pruefeLogin(true, true);
        if (rc != 1) {
            return;
        }
        if (eclUserLogin.pruefe_mandantenBearbeiten_mandantenAnlegen() == false) {
            this.fehlerMeldung("Mandanten anlegen mit dieser Kennung nicht zulässig");
            return;
        }

        ParamS.clGlobalVar.mandant = 0; // Zurücksetzen, damit in neuer Mandant nichts angezeigt wird
        /* Neuen Mandant anlegen */
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlMandantAnlegen controllerFenster = new CtrlMandantAnlegen();

        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/MandantAnlegen.fxml", 1600, 700,
                "Mandant anlegen", true);

        initFuerEmittentenAuswahl();
        return;
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** Übergabewerte. */

    /**
     * =true => wurde aufgerufen aus Mandantenwechsel heraus - User nicht abfragen /
     * überprüfen / verändern
     */
    public boolean nurMandantenwechsel = false;

    /** **************** Return-Werte für aufrufende Routine *************. */
    public boolean fortsetzen = false;

    /** Nur falls nurMandantenwechsel=false. */
    public EclUserLogin eclUserLogin = null;

    /** Nur falls nurMandantenwechsel=false. */
    public EclEmittenten eclEmittent = null;

    /**
     * ParamS.clGlobalVar.mandant etc. sind auf die neuen Werte gesetzt
     *
     * @param pEigeneStage the eigene stage
     */

    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
