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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientAllg.CaPruefeLogin;
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
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELeseEmittenten;
import de.meetingapps.meetingportal.meetComWE.WELeseEmittentenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * The Class CtrlLogin.
 */
public class CtrlLogin extends CtrlRoot {

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
    private ComboBox<EclEmittenten> cbMandant;

    /** The tf HV jahr. */
    @FXML
    private TextField tfHVJahr;

    /** The tf HV nummer. */
    @FXML
    private TextField tfHVNummer;

    /** The tf datenbereich. */
    @FXML
    private TextField tfDatenbereich;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The btn neue HV. */
    @FXML
    private Button btnNeueHV;

    //    private StubCtrlLogin stubCtrlLogin = null;
    //    private DbBundle dbBundle = null;

    /** Array aller Mandanten - Basis für die folgenden Listen. */
    //    private EclEmittenten[] mandantenArray = null;

    /** Liste aller Emittenten - d.h. komprimiert, gleiche Jahre etc. eliminiert */
    private List<EclEmittenten> emittentenListe = null;

    //    private List<String> hvJahrListe = null;
    //    private List<String> hvNummerListe = null;
    //    private List<String> datenbereichListe = null;

    /** The l param geraet. */
    private ParamGeraet lParamGeraet = null;

    /** The gef. */
    int gef = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfKennung != null : "fx:id=\"tfKennung\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfPasswort != null : "fx:id=\"tfPasswort\" was not injected: check your FXML file 'Login.fxml'.";
        assert cbMandant != null : "fx:id=\"cbMandant\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfHVJahr != null : "fx:id=\"tfHVJahr\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfHVNummer != null : "fx:id=\"tfHVNummer\" was not injected: check your FXML file 'Login.fxml'.";
        assert tfDatenbereich != null : "fx:id=\"tfDatenbereich\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnStarten != null : "fx:id=\"btnStarten\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnNeueHV != null : "fx:id=\"btnNeueHV\" was not injected: check your FXML file 'Login.fxml'.";

        /************* Ab hier individuell ********************************************/

        //        dbBundle = new DbBundle();
        //        stubCtrlLogin = new StubCtrlLogin(false, dbBundle);

        leseEmittenten();

        lParamGeraet = ParamS.paramGeraet;
        gef = 0;

        /*Kennung, Passwort*/
        if (!lParamGeraet.festgelegterBenutzername.isEmpty() || nurMandantenwechsel) {/*Keine Auswahl*/
            if (nurMandantenwechsel == false) {
                tfKennung.setText(lParamGeraet.festgelegterBenutzername);
            } else {
                tfKennung.setText(ParamS.eclUserLogin.kennung); //User dann bereits eingeloggt
            }
            tfKennung.setEditable(false);
            tfPasswort.setEditable(false);
        } else {
            gef = 1;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tfKennung.requestFocus();
                }
            });
        }

        /*Emittent*/
        initEmittenten();

        /*HVJahr*/
        if (lParamGeraet.festgelegtesJahr != 0) {/*Keine Auswahl*/
            tfHVJahr.setText(Integer.toString(lParamGeraet.festgelegtesJahr));
            tfHVJahr.setEditable(false);
        } else {
            tfHVJahr.setText("2018");
            if (gef == 0) {
                gef = 1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tfHVJahr.requestFocus();
                    }
                });
            }
        }

        /*HVNummer*/
        if (!lParamGeraet.festgelegteHVNummer.isEmpty()) {/*Keine Auswahl*/
            tfHVNummer.setText(lParamGeraet.festgelegteHVNummer);
            tfHVNummer.setEditable(false);
        } else {
            tfHVNummer.setText("A");
            if (gef == 0) {
                gef = 1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tfHVNummer.requestFocus();
                    }
                });
            }
        }

        /*Datenbereich*/
        if (!lParamGeraet.festgelegteDatenbank.isEmpty()) {/*Keine Auswahl*/
            tfDatenbereich.setText(lParamGeraet.festgelegteDatenbank);
            tfDatenbereich.setEditable(false);
        } else {
            tfDatenbereich.setText("P");
            if (gef == 0) {
                gef = 1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tfDatenbereich.requestFocus();
                    }
                });
            }
        }
    }

    /**
     * Inits the emittenten.
     */
    private void initEmittenten() {
        StringConverter<EclEmittenten> sc = new StringConverter<EclEmittenten>() {
            @Override
            public String toString(EclEmittenten pEmittent) {
                return Integer.toString(pEmittent.mandant) + " " + pEmittent.bezeichnungKurz;
            }

            @Override
            public EclEmittenten fromString(String string) {
                return null;
            }
        };
        cbMandant.setConverter(sc);
        cbMandant.getSelectionModel().clearSelection();
        cbMandant.getItems().clear();

        if (lParamGeraet.festgelegterMandant > 0 && nurMandantenwechsel == false) {/*Keine Auswahl*/
            if (emittentenListe != null) {
                for (int i = 0; i < emittentenListe.size(); i++) {
                    if (lParamGeraet.festgelegterMandant == emittentenListe.get(i).mandant) {
                        cbMandant.getItems().add(emittentenListe.get(i));
                        cbMandant.setValue(emittentenListe.get(i));
                    }
                }
            }
        } else {
            if (emittentenListe != null) {
                for (int i = 0; i < emittentenListe.size(); i++) {
                    cbMandant.getItems().add(emittentenListe.get(i));
                }
            }
            if (gef == 0) {
                gef = 1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cbMandant.requestFocus();
                    }
                });
            }
        }

    }

    /**
     * ************Logik******************.
     */
    private void leseEmittenten() {
        if (ParamS.clGlobalVar.webServicePfadNr >= 0) {
            WELoginVerify weLoginVerify = null;
            WSClient wsClient = new WSClient();
            weLoginVerify = new WELoginVerify();
            WELeseEmittenten weLeseEmittenten = new WELeseEmittenten();
            weLeseEmittenten.weLoginVerify = weLoginVerify;
            WELeseEmittentenRC weLeseEmittentenRC = wsClient.leseEmittenten(weLeseEmittenten);
            emittentenListe = weLeseEmittentenRC.emittentenListe;
        } else {
            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAll();
            BvMandanten lBvMandanten = new BvMandanten();
            emittentenListe = lBvMandanten.liefereEmittentenListeFuerNrAuswahl(lDbBundle);
            lDbBundle.closeAll();
        }
    }

    /**
     * afFalscheKennung
     * afPasswortFalsch
     *
     * afHVNichtVorhanden
     * afHVMitDieserKennungNichtZulaessig
     * afKennungGesperrt
     * afNeuesPasswortErforderlich
     *
     *  1 = erfolgreich
     */
    private int pruefeLogin(boolean mitMandantHolen) {
        boolean pNurMandantPruefen = false;

        if (nurMandantenwechsel) {
            pNurMandantPruefen = true;
        }

        int rc = 0;

        CaPruefeLogin caPruefeLogin = new CaPruefeLogin();
        rc = caPruefeLogin.pruefeLogin(tfKennung.getText(), tfPasswort.getText(), pNurMandantPruefen, false);
        System.out.println("CtrLogin rc=" + rc);
        eclUserLogin = caPruefeLogin.rcUserLogin;
        ParamS.eclUserLogin = eclUserLogin;
        eclEmittent = caPruefeLogin.rcEmittent;
        ParamS.eclEmittent = caPruefeLogin.rcEmittent;
        if (rc < 0) {
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
        String hString = "";

        /*Eingaben überprüfen*/
        if (cbMandant.getValue() == null || cbMandant.getValue().mandant == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte Mandant auswählen!");
            cbMandant.requestFocus();
            return;
        }

        hString = tfHVJahr.getText();
        if (hString.isEmpty() || !CaString.isNummern(hString) || Integer.parseInt(hString) < 1990
                || Integer.parseInt(hString) > 2099) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "HV-Jahr muß eine Zahl zwischen 1990 und 2099 sein!");
            tfHVJahr.requestFocus();
            return;
        }

        hString = tfHVNummer.getText();
        if (hString.isEmpty() || hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Lfd. HV-Nummer muß zwischen A und Z sein!");
            tfHVNummer.requestFocus();
            return;
        }

        hString = tfDatenbereich.getText();
        if (hString.isEmpty() || hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Datenbereich muß zwischen A und Z sein!");
            tfDatenbereich.requestFocus();
            return;
        }

        /*Variablen belegen*/
        ParamS.clGlobalVar.mandant = cbMandant.getValue().mandant;
        ParamS.clGlobalVar.hvJahr = Integer.parseInt(tfHVJahr.getText());
        ParamS.clGlobalVar.hvNummer = tfHVNummer.getText();
        ParamS.clGlobalVar.datenbereich = tfDatenbereich.getText();

        int rc = pruefeLogin(true);
        if (rc == 1) {
            fortsetzen = true;
            eigeneStage.hide();
            return;
        }

        /*Passwort ändern erforderlich*/
        if (rc == CaFehler.afNeuesPasswortErforderlich) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Passwort-Änderung erforderlich!");
            lassePasswortAendern();
            tfKennung.setText("");
            tfPasswort.setText("");
            tfKennung.requestFocus();
            return;
        }

        /*Fehlersituation*/
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
        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        zeigeHinweis.zeige(eigeneStage, fehlerText);
        return;
    }

    /**
     * Btn neue HV clicked.
     *
     * @param event the event
     */
    @FXML
    void btnNeueHVClicked(ActionEvent event) {
        /*Neuen Mandant anlegen*/
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlMandantAnlegen controllerFenster = new CtrlMandantAnlegen();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/MandantAnlegen.fxml", 1600, 700,
                "Mandant anlegen", true);

        leseEmittenten();
        initEmittenten();
        return;
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** Übergabewerte. */

    /**
     * =true => wurde aufgerufen aus Mandantenwechsel heraus - User nicht abfragen / überprüfen / verändern
     */
    public boolean nurMandantenwechsel = false;

    /** ****************Return-Werte für aufrufende Routine*************. */
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
