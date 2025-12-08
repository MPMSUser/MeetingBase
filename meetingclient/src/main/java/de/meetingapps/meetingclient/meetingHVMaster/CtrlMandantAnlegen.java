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
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubMandantAnlegen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlMandantAnlegen.
 */
public class CtrlMandantAnlegen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn fertig. */
    @FXML
    private Button btnFertig;

    /** The tf mandanten nr. */
    @FXML
    private TextField tfMandantenNr;

    /** The tf HV jahr. */
    @FXML
    private TextField tfHVJahr;

    /** The tf lfd HV nummer. */
    @FXML
    private TextField tfLfdHVNummer;

    /** The tf datenbereich. */
    @FXML
    private TextField tfDatenbereich;

    /** The btn mandant anlegen. */
    @FXML
    private Button btnMandantAnlegen;

    /** The btn parameter uebernehmen. */
    @FXML
    private Button btnParameterUebernehmen;

    /** The btn basis module pflegen. */
    @FXML
    private Button btnBasisModulePflegen;

    /** The btn emittenten uebernehmen. */
    @FXML
    private Button btnEmittentenUebernehmen;

    /** The btn emittent pflegen. */
    @FXML
    private Button btnEmittentPflegen;

    /** The btn portal app texte uebernehmen. */
    @FXML
    private Button btnPortalAppTexteUebernehmen;

    /** The btn sammelkarten anlegen. */
    @FXML
    private Button btnSammelkartenAnlegen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The cb server. */
    @FXML
    private ComboBox<String> cbServer;

    /** *********Ab hier individuell******************. */
    @FXML
    private ComboBox<CbElement> cbEmittent;

    /** The cb emittent allgemein. */
    private CbAllgemein cbEmittentAllgemein = null;

    /** The cb uebernahme emittent. */
    @FXML
    private ComboBox<CbElement> cbUebernahmeEmittent;

    /** The cb uebernahme emittent allgemein. */
    private CbAllgemein cbUebernahmeEmittentAllgemein = null;

    /** The cb uebernahme portal app texte. */
    @FXML
    private ComboBox<CbElement> cbUebernahmePortalAppTexte;

    /** The cb uebernahme portal app texte allgemein. */
    private CbAllgemein cbUebernahmePortalAppTexteAllgemein = null;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** The stub mandant anlegen. */
    private StubMandantAnlegen stubMandantAnlegen = null;

    /** The emittenten liste. */
    public List<EclEmittenten> emittentenListe = null;

    /** The mandanten array. */
    public EclEmittenten[] mandantenArray = null;

    /** ***********Zum Zwischenspeichern Serverzugriff**************. */
    /*"generelle" = der allgemein gültige Zugriff außerhalb dieser Routinen
     *"holenVon"= der Server, von dem beim Kopieren die Daten geholt werden sollen
     */
    private int holenVonWebServicePfadNr = -1;

    /** The holen von datenbank pfad nr. */
    private int holenVonDatenbankPfadNr = -1;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert cbEmittent != null : "fx:id=\"cbEmittent\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnFertig != null : "fx:id=\"btnFertig\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert tfMandantenNr != null : "fx:id=\"tfMandantenNr\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert tfHVJahr != null : "fx:id=\"tfHVJahr\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert tfLfdHVNummer != null : "fx:id=\"tfLfdHVNummer\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert tfDatenbereich != null : "fx:id=\"tfDatenbereich\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnMandantAnlegen != null : "fx:id=\"btnMandantAnlegen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnParameterUebernehmen != null : "fx:id=\"btnParameterUebernehmen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnBasisModulePflegen != null : "fx:id=\"btnBasisModulePflegen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert cbUebernahmeEmittent != null : "fx:id=\"cbUebernahmeEmittent\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnEmittentenUebernehmen != null : "fx:id=\"btnEmittentenUebernehmen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnEmittentPflegen != null : "fx:id=\"btnEmittentPflegen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnSammelkartenAnlegen != null : "fx:id=\"btnSammelkartenAnlegen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'MandantAnlegen.fxml'.";

        /*********** Ab hier individuell ****************/

        dbBundle = new DbBundle();
        stubMandantAnlegen = new StubMandantAnlegen(false, dbBundle);

        /*Combo-Boxen füllen*/

        /*Server*/
        cbServer.getItems().removeAll(cbServer.getItems());
        for (int i = 0; i < ParamInterneKommunikation.auswahlTexte.length; i++) {
            String hString = ParamInterneKommunikation.auswahlTexte[i];
            if (!hString.equals("-")) {
                cbServer.getItems().add(hString);
                if (i == ParamS.clGlobalVar.webServicePfadNr) {
                    cbServer.getSelectionModel().select(hString);
                }
            }
        }

        holeEmittentenMandantenListe();

        aktiviereSchritt1();
    }

    /**
     * ***************Oberflächen******************.
     */
    private void aktiviereSchritt1() {
        btnParameterUebernehmen.setDisable(true);
        btnBasisModulePflegen.setDisable(true);

        cbUebernahmeEmittent.setDisable(true);
        btnEmittentenUebernehmen.setDisable(true);
        btnEmittentPflegen.setDisable(true);

        cbUebernahmePortalAppTexte.setDisable(true);
        btnPortalAppTexteUebernehmen.setDisable(true);

        btnSammelkartenAnlegen.setDisable(true);
        btnFertig.setDisable(true);
    }

    /**
     * Aktiviere schritt 2.
     */
    private void aktiviereSchritt2() {
        tfMandantenNr.setDisable(true);
        tfHVJahr.setDisable(true);
        tfLfdHVNummer.setDisable(true);
        tfDatenbereich.setDisable(true);
        cbEmittent.setDisable(true);
        btnMandantAnlegen.setDisable(true);

        btnParameterUebernehmen.setDisable(false);
        btnBasisModulePflegen.setDisable(false);
        cbUebernahmeEmittent.setDisable(false);
        btnEmittentenUebernehmen.setDisable(false);
        btnEmittentPflegen.setDisable(false);
        cbUebernahmePortalAppTexte.setDisable(false);
        btnPortalAppTexteUebernehmen.setDisable(false);
        btnSammelkartenAnlegen.setDisable(false);

        btnFertig.setDisable(false);
        btnAbbrechen.setDisable(true);
    }

    /**
     * Update fenster titel.
     */
    private void updateFensterTitel() {
        String hTitel = "Mandant anlegen - " + Integer.toString(ParamS.clGlobalVar.mandant) + "/"
                + Integer.toString(ParamS.clGlobalVar.hvJahr) + ParamS.clGlobalVar.hvNummer + "/"
                + ParamS.clGlobalVar.datenbereich + " " + ParamS.eclEmittent.bezeichnungKurz;
        eigeneStage.setTitle(hTitel);

    }

    /**
     * *********************Reaktionen auf Bedienung****************.
     *
     * @param event the event
     */

    @FXML
    void onBtnMandantAnlegen(ActionEvent event) {

        /*Eingabe überprüfen*/
        CbElement gewaehltEmittent = cbEmittent.getValue();
        String mandant = tfMandantenNr.getText().trim();
        if ((gewaehltEmittent.ident1 != -1 && !mandant.isEmpty())
                || (gewaehltEmittent.ident1 == -1 && mandant.isEmpty())) {
            fehlerMeldung("Entweder Mandanten-Nr eingeben oder bestehenden Emittent auswählen!");
            return;
        }

        this.lFehlertext = "";
        pruefeZahlOderLeer(tfMandantenNr, "Mandaten-Nr");
        this.pruefeNichtLeerUndLaenge(tfHVJahr, "HV-Jahr", 4);
        this.pruefeZahlVonBis(tfHVJahr, "HV-Jahr", 1900, 2999);
        this.pruefeLaenge(tfLfdHVNummer, "Lfd HV-Nummer", 1);
        this.pruefeLaenge(tfDatenbereich, "Datenbereich", 1);
        if (!lFehlertext.isEmpty()) {
            fehlerMeldung(lFehlertext);
            return;
        }

        int mandantenNr = gewaehltEmittent.ident1;
        if (mandantenNr == -1) {
            mandantenNr = Integer.parseInt(mandant);
        }
        if (mandantenNr < 1 || mandantenNr > 999) {
            fehlerMeldung("Mandanten-Nr muß zwischen 1 und 999 sein!");
            return;
        }

        int hvJahr = Integer.parseInt(tfHVJahr.getText());

        String lfdHVNummer = tfLfdHVNummer.getText().toUpperCase();
        if (lfdHVNummer.compareTo("A") < 0 || lfdHVNummer.compareTo("Z") > 0) {
            fehlerMeldung("Lfd HV-Nummer muß zwischen A und Z liegen!");
            return;
        }

        String datenbereich = tfDatenbereich.getText().toUpperCase();
        if (datenbereich.compareTo("A") < 0 || datenbereich.compareTo("Z") > 0) {
            fehlerMeldung("Datenbereich muß zwischen A und Z liegen!");
            return;
        }

        EclEmittenten neuerEmittent = new EclEmittenten();
        neuerEmittent.mandant = mandantenNr;
        neuerEmittent.hvJahr = hvJahr;
        neuerEmittent.hvNummer = lfdHVNummer;
        neuerEmittent.dbArt = datenbereich;

        int rc = stubMandantAnlegen.anlegenTablesUndEmittent(neuerEmittent);
        if (rc == CaFehler.pfdXyBereitsVorhanden) {
            fehlerMeldung("Mandant ist bereits vorhanden!");
            return;
        }

        /*Nun neuen Mandanten aktivieren*/
        ParamS.clGlobalVar.mandant = mandantenNr;
        ParamS.clGlobalVar.hvJahr = hvJahr;
        ParamS.clGlobalVar.hvNummer = lfdHVNummer;
        ParamS.clGlobalVar.datenbereich = datenbereich;

        dbBundle = new DbBundle();
        BvMandanten bvMandanten = new BvMandanten();
        bvMandanten.legePfadeNeuAn(dbBundle);

        leseParameterNeuEin();
        updateFensterTitel();

        /*nächsten Schritt aktivieren*/
        aktiviereSchritt2();
        ausgefuehrtMeldung("");
    }

    /**
     * On btn basis module pflegen.
     *
     * @param event the event
     */
    @FXML
    void onBtnBasisModulePflegen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterBasis controllerFenster = new CtrlParameterBasis();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterBasis.fxml", 1200, 850,
                "Parameter Basiseinstellungen", true);
        leseParameterNeuEin();
        updateFensterTitel();
    }

    /**
     * On btn parameter uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnParameterUebernehmen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterSetVerwaltung controllerFenster = new CtrlParameterSetVerwaltung();
        controllerFenster.init(neuerDialog, 2);
        controllerFenster.mandantenArray = mandantenArray;

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterSetVerwaltung.fxml", 1600, 700,
                "Parameter als Set speichern", true);

        if (controllerFenster.rc == true) {
            if (controllerFenster.gewaehltesParameterSet != null) {
                stubMandantAnlegen.verwendeParameterSet(controllerFenster.gewaehltesParameterSet);
            } else {
                stubMandantAnlegen.verwendeMandantHoleParameter(controllerFenster.gewaehlterMandant);
                stubMandantAnlegen.verwendeMandantSchreibeParameter();
            }
            leseParameterNeuEin();
            updateFensterTitel();
            ausgefuehrtMeldung("");
        }

    }

    /**
     * On btn emittent pflegen.
     *
     * @param event the event
     */
    @FXML
    void onBtnEmittentPflegen(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterEmittent controllerFenster = new CtrlParameterEmittent();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterEmittent.fxml", 1120, 720,
                "Parameter Emittent", true);

        leseParameterNeuEin();
        updateFensterTitel();

    }

    /**
     * On btn emittenten uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnEmittentenUebernehmen(ActionEvent event) {
        CbElement gewaehlterMandant = cbUebernahmeEmittent.getValue();
        if (gewaehlterMandant == null) {
            fehlerMeldung("Bitte Mandant zum Übernehmen auswählen!");
            return;
        }
        EclEmittenten gewaehlterEmittent = mandantenArray[gewaehlterMandant.ident1];
        stubMandantAnlegen.verwendeMandantHoleParameter(gewaehlterEmittent);
        stubMandantAnlegen.uebernehmeEmittentendaten(gewaehlterEmittent);
        leseParameterNeuEin();
        updateFensterTitel();
        ausgefuehrtMeldung("");
    }

    /**
     * On btn portal app texte uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnPortalAppTexteUebernehmen(ActionEvent event) {
        CbElement gewaehlterMandant = cbUebernahmePortalAppTexte.getValue();
        if (gewaehlterMandant == null) {
            fehlerMeldung("Bitte Mandant zum Übernehmen der Portal-/App-Texte auswählen!");
            return;
        }
        EclEmittenten gewaehlterEmittent = mandantenArray[gewaehlterMandant.ident1];
        stubMandantAnlegen.verwendeMandantHoleParameter(gewaehlterEmittent);
        stubMandantAnlegen.uebernehmePortalAppTexte();
        leseParameterNeuEin();
        updateFensterTitel();
        ausgefuehrtMeldung("");
    }

    /**
     * On btn sammelkarten anlegen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenAnlegen(ActionEvent event) {
        stubMandantAnlegen.sammelkartenSandardAnlegen();
        ausgefuehrtMeldung("");
    }

    /**
     * On btn fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnFertig(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Cb server changed.
     *
     * @param event the event
     */
    @FXML
    void cbServerChanged(ActionEvent event) {
        String erg = cbServer.getValue();
        int gef = 0;
        for (int i = 0; i < ParamInterneKommunikation.auswahlTexte.length; i++) {
            if (ParamInterneKommunikation.auswahlTexte[i].compareTo(erg) == 0) {
                gef = i;
            }
        }
        holenVonWebServicePfadNr = gef;
        holenVonDatenbankPfadNr = ParamInterneKommunikation.liefereDatenbankPfadNr(gef);

        /*Emittentenliste neu holen*/
        holeEmittentenMandantenListe();

    }

    /**
     * ************Logiken*****************.
     *
     * @return the int
     */
    private int leseParameterNeuEin() {
        CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
        int rc = caLeseParameterNeu.leseHVParameter();
        /** Ggf. zum Reload der aktuellen Parameter */
        if (ParamS.clGlobalVar.webServicePfadNr != -1) {
            dbBundle = new DbBundle();
        } else {
            dbBundle = new DbBundle();
            dbBundle.openAll();
            dbBundle.closeAll();
        }
        return rc;
    }

    /**
     * **************Emittenten-und Mandanten-Liste holen******************.
     */
    private void holeEmittentenMandantenListe() {
        if (holenVonWebServicePfadNr != -1) {
            stubMandantAnlegen.holenVonWebServicePfadNr = holenVonWebServicePfadNr;
            stubMandantAnlegen.holenVonDatenbankPfadNr = holenVonDatenbankPfadNr;
        }
        stubMandantAnlegen.holeEmittentenFuerAuswahl();
        emittentenListe = stubMandantAnlegen.emittentenListe;
        mandantenArray = stubMandantAnlegen.mandantenArray;

        cbEmittentAllgemein = new CbAllgemein(cbEmittent);

        CbElement lElementNeu = new CbElement();
        lElementNeu.ident1 = -1;
        lElementNeu.anzeige = "Neuer Emittent";
        cbEmittentAllgemein.addElementAusgewaehlt(lElementNeu);

        if (emittentenListe != null) {
            for (int i = 0; i < emittentenListe.size(); i++) {
                CbElement lElement = new CbElement();
                EclEmittenten lEmittent = emittentenListe.get(i);
                lElement.ident1 = lEmittent.mandant;
                lElement.anzeige = "(" + CaString.fuelleLinksNull(Integer.toString(lEmittent.mandant), 3) + ") "
                        + lEmittent.bezeichnungKurz;
                cbEmittentAllgemein.addElement(lElement);
            }
        }

        cbUebernahmeEmittentAllgemein = new CbAllgemein(cbUebernahmeEmittent);
        if (mandantenArray != null) {
            for (int i = 0; i < mandantenArray.length; i++) {
                CbElement lElement = new CbElement();
                EclEmittenten lEmittent = mandantenArray[i];
                lElement.sIdent1 = CaString.fuelleLinksNull(Integer.toString(lEmittent.mandant), 3) + " "
                        + Integer.toString(lEmittent.hvJahr) + " " + lEmittent.hvNummer + " " + lEmittent.dbArt;
                lElement.ident1 = i;
                lElement.anzeige = lElement.sIdent1 + " " + lEmittent.bezeichnungKurz;
                cbUebernahmeEmittentAllgemein.addElement(lElement);
            }
        }

        cbUebernahmePortalAppTexteAllgemein = new CbAllgemein(cbUebernahmePortalAppTexte);
        if (mandantenArray != null) {
            for (int i = 0; i < mandantenArray.length; i++) {
                CbElement lElement = new CbElement();
                EclEmittenten lEmittent = mandantenArray[i];
                lElement.sIdent1 = CaString.fuelleLinksNull(Integer.toString(lEmittent.mandant), 3) + " "
                        + Integer.toString(lEmittent.hvJahr) + " " + lEmittent.hvNummer + " " + lEmittent.dbArt;
                lElement.ident1 = i;
                lElement.anzeige = lElement.sIdent1 + " " + lEmittent.bezeichnungKurz;
                cbUebernahmePortalAppTexteAllgemein.addElement(lElement);
            }
        }

    }

    /**
     * ****************Initialisierung*****************.
     *
     * @param pEigeneStage the eigene stage
     */

    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
