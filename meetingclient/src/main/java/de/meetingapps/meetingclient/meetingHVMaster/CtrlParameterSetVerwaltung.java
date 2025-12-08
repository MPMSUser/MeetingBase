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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclParameterSet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubParameterSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterSetVerwaltung.
 */
public class CtrlParameterSetVerwaltung extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf beschreibung. */
    @FXML
    private TextArea tfBeschreibung;

    /** The lbl angelegt von. */
    @FXML
    private Label lblAngelegtVon;

    /** The tf angelegt von. */
    @FXML
    private TextField tfAngelegtVon;

    /** The lbl angelegt am. */
    @FXML
    private Label lblAngelegtAm;

    /** The tf angelegt am. */
    @FXML
    private TextField tfAngelegtAm;

    /** The lbl geaendert von. */
    @FXML
    private Label lblGeaendertVon;

    /** The tf geaendert von. */
    @FXML
    private TextField tfGeaendertVon;

    /** The lbl geaendert am. */
    @FXML
    private Label lblGeaendertAm;

    /** The tf geaendert am. */
    @FXML
    private TextField tfGeaendertAm;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The rect emittent. */
    @FXML
    private Rectangle rectEmittent;

    /** The lbl emittent. */
    @FXML
    private Label lblEmittent;

    /** The lbl bezeichnung emittent. */
    @FXML
    private Label lblBezeichnungEmittent;

    /** The lbl jahr emittent. */
    @FXML
    private Label lblJahrEmittent;

    /** The lbl HV nummer emittent. */
    @FXML
    private Label lblHVNummerEmittent;

    /** The tf bezeichnung emittent. */
    @FXML
    private TextField tfBezeichnungEmittent;

    /** The tf jahr emittent. */
    @FXML
    private TextField tfJahrEmittent;

    /** The tf HV nummer emittent. */
    @FXML
    private TextField tfHVNummerEmittent;

    /** The lbl datenbereich emittent. */
    @FXML
    private Label lblDatenbereichEmittent;

    /** The tf datenbereich emittent. */
    @FXML
    private TextField tfDatenbereichEmittent;

    /** *Ab hier individuell*********. */

    @FXML
    private ComboBox<CbElement> cbSet;

    /** The cb set allgemein. */
    private CbAllgemein cbSetAllgemein = null;

    /** The cb emittent. */
    @FXML
    private ComboBox<CbElement> cbEmittent;

    /** The cb emittent allgemein. */
    private CbAllgemein cbEmittentAllgemein = null;

    /**
     * 1=Speichern aktuelle Parameter als Parameter-Set 2=Abruf eines Parameter-Sets
     * bei Neuanlage eines Mandanten.
     */
    private int funktion = 0;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** The stub parameter set. */
    private StubParameterSet stubParameterSet = null;

    /** The parameter set array. */
    private EclParameterSet[] parameterSetArray = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert cbSet != null : "fx:id=\"cbSet\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert tfBeschreibung != null : "fx:id=\"tfBeschreibung\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert lblAngelegtVon != null : "fx:id=\"lblAngelegtVon\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert tfAngelegtVon != null : "fx:id=\"tfAngelegtVon\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert lblAngelegtAm != null : "fx:id=\"lblAngelegtAm\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert tfAngelegtAm != null : "fx:id=\"tfAngelegtAm\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert lblGeaendertVon != null : "fx:id=\"lblGeaendertVon\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert tfGeaendertVon != null : "fx:id=\"tfGeaendertVon\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert lblGeaendertAm != null : "fx:id=\"lblGeaendertAm\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert tfGeaendertAm != null : "fx:id=\"tfGeaendertAm\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterSetVerwaltung.fxml'.";
        
        /************* Individuell *********************/

        dbBundle = new DbBundle();
        stubParameterSet = new StubParameterSet(false, dbBundle);

        parameterSetArray = stubParameterSet.leseParameterSets();
        cbSetAllgemein = new CbAllgemein(cbSet);

        if (funktion == 1) {//Nur bei Speichern eines Sets
            CbElement lElement = new CbElement();
            lElement.ident1 = -1;
            lElement.anzeige = "Neues Set anlegen";
            cbSetAllgemein.addElementAusgewaehlt(lElement);
        } else {
            CbElement lElement = new CbElement();
            lElement.ident1 = -1;
            lElement.anzeige = "Parameter-Set oder Mandant auswählen";
            cbSetAllgemein.addElementAusgewaehlt(lElement);
        }

        for (int i = 0; i < parameterSetArray.length; i++) {
            CbElement lElement = new CbElement();
            EclParameterSet lParameterSet = parameterSetArray[i];
            lElement.ident1 = lParameterSet.ident;
            lElement.anzeige = "(" + Integer.toString(lParameterSet.ident) + ") " + lParameterSet.beschreibung;
            cbSetAllgemein.addElement(lElement);
        }

        tfAngelegtVon.setEditable(false);
        tfAngelegtAm.setEditable(false);
        tfGeaendertVon.setEditable(false);
        tfGeaendertAm.setEditable(false);

        cbSet.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 == -1) {//Auf "Neu" gestellt - sonstige textfelder löschen
                    tfBeschreibung.setText("");
                    tfAngelegtVon.setText("");
                    tfAngelegtAm.setText("");
                    tfGeaendertVon.setText("");
                    tfGeaendertAm.setText("");
                } else { //Auf vorhandenes Set gestellt
                    int gef = -1;
                    gef = liefereOffsetZuIdentInParmaeterSetArray(neuerWert.ident1);
                    tfBeschreibung.setText(parameterSetArray[gef].beschreibung);
                    tfAngelegtVon.setText(parameterSetArray[gef].angelegtVonUserLoginText);
                    tfAngelegtAm.setText(CaDatumZeit.DatumZeitStringFuerAnzeige(parameterSetArray[gef].angelegtAm));
                    tfGeaendertVon.setText(parameterSetArray[gef].letzteAenderungVonUserLoginText);
                    tfGeaendertAm
                            .setText(CaDatumZeit.DatumZeitStringFuerAnzeige(parameterSetArray[gef].letzteAenderungAm));
                }
            }
        });

        if (funktion == 1) {
            /*Emittenten-Felder ausblenden*/
            rectEmittent.setVisible(false);
            cbEmittent.setVisible(false);
            lblEmittent.setVisible(false);
            lblBezeichnungEmittent.setVisible(false);
            tfBezeichnungEmittent.setVisible(false);
            lblJahrEmittent.setVisible(false);
            tfJahrEmittent.setVisible(false);
            lblHVNummerEmittent.setVisible(false);
            tfHVNummerEmittent.setVisible(false);
            lblDatenbereichEmittent.setVisible(false);
            tfDatenbereichEmittent.setVisible(false);
        }

        if (funktion == 2) {/*Mandanten-Combobox füllen*/
            cbEmittentAllgemein = new CbAllgemein(cbEmittent);

            CbElement lElement = new CbElement();
            lElement.ident1 = -1;
            lElement.anzeige = "Parameter-Set oder Mandant auswählen";
            cbEmittentAllgemein.addElementAusgewaehlt(lElement);

            for (int i = 0; i < mandantenArray.length; i++) {
                lElement = new CbElement();
                EclEmittenten lEmittent = mandantenArray[i];
                lElement.sIdent1 = CaString.fuelleLinksNull(Integer.toString(lEmittent.mandant), 3) + " "
                        + Integer.toString(lEmittent.hvJahr) + " " + lEmittent.hvNummer + " " + lEmittent.dbArt;
                lElement.ident1 = i;
                lElement.anzeige = lElement.sIdent1 + " " + lEmittent.bezeichnungKurz;
                cbEmittentAllgemein.addElement(lElement);
            }

            cbEmittent.valueProperty().addListener(new ChangeListener<CbElement>() {
                @Override
                public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                        CbElement neuerWert) {
                    if (neuerWert == null) {
                        return;
                    }
                    if (neuerWert.ident1 == -1) {//Auf "Neu" gestellt - sonstige textfelder löschen
                        tfBezeichnungEmittent.setText("");
                        tfJahrEmittent.setText("");
                        tfHVNummerEmittent.setText("");
                        tfDatenbereichEmittent.setText("");
                    } else { //Auf vorhandenes Set gestellt
                        EclEmittenten lEmittent = mandantenArray[neuerWert.ident1];
                        tfBezeichnungEmittent.setText(lEmittent.bezeichnungKurz);
                        tfJahrEmittent.setText(Integer.toString(lEmittent.hvJahr));
                        tfHVNummerEmittent.setText(lEmittent.hvNummer);
                        tfDatenbereichEmittent.setText(lEmittent.dbArt);
                    }
                }
            });

            tfBezeichnungEmittent.setEditable(false);
            tfJahrEmittent.setEditable(false);
            tfHVNummerEmittent.setEditable(false);
            tfDatenbereichEmittent.setEditable(false);
        }

    }

    /*****************Reagieren auf Clicks*****************/
    
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        rc = false;
        eigeneStage.hide();
    }

    /**
     * On btn speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichern(ActionEvent event) {
        CbElement gewaehltElement = cbSet.getValue();
        int parameterSetIdent = -1;
        if (gewaehltElement == null) {
            this.fehlerMeldung("Bitte Parameter Set auswählen!");
            return;
        }

        String beschreibung = CaString.entferneSteuerzeichen(tfBeschreibung.getText());
        if (beschreibung.length() > 400) {
            this.fehlerMeldung(
                    "Beschreibung zu lang - maximal 400 Zeichen (Ist: " + beschreibung.length() + " Zeichen)");
            return;
        }

        if (funktion == 1) {//Speichern Set
            if (gewaehltElement.ident1 == -1) { //Neuanlage
                EclParameterSet lParameterSet = new EclParameterSet();
                lParameterSet.beschreibung = beschreibung;
                lParameterSet.angelegtVonUserID = ParamS.clGlobalVar.benutzernr;
                lParameterSet.angelegtAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
                lParameterSet.letzteAenderungVonUserID = ParamS.clGlobalVar.benutzernr;
                lParameterSet.letzteAenderungAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
                parameterSetIdent = stubParameterSet.insertTblParameterSet(lParameterSet);
            } else { //bestehendes Set überschreiben
                int offset = liefereOffsetZuIdentInParmaeterSetArray(gewaehltElement.ident1);
                EclParameterSet lParameterSet = parameterSetArray[offset];
                lParameterSet.beschreibung = beschreibung;
                lParameterSet.letzteAenderungVonUserID = ParamS.clGlobalVar.benutzernr;
                lParameterSet.letzteAenderungAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
                parameterSetIdent = lParameterSet.ident;
                stubParameterSet.updateTblParameterSet(lParameterSet);

            }
            stubParameterSet.speichereParameterZuParameterSet(parameterSetIdent);
            rc = true;
            eigeneStage.hide();

        } else {//Laden Set bei Mandanten-Neuanlage
            CbElement cbElementSet = cbSet.getValue();
            CbElement cbElementMandant = cbEmittent.getValue();
            if ((cbElementSet.ident1 == -1 && cbElementMandant.ident1 == -1)
                    || (cbElementSet.ident1 != -1 && cbElementMandant.ident1 != -1)) {
                this.fehlerMeldung("Bitte Parameter-Set oder Mandant auswählen!");
                return;
            }
            if (cbElementSet.ident1 != -1) {
                int offsetParameterSet = liefereOffsetZuIdentInParmaeterSetArray(cbElementSet.ident1);
                gewaehltesParameterSet = parameterSetArray[offsetParameterSet];
                //    			System.out.println("gewaehltesParameterSet.ident"+gewaehltesParameterSet.ident);
                gewaehlterMandant = null;
            } else {
                gewaehlterMandant = mandantenArray[cbElementMandant.ident1];
                gewaehltesParameterSet = null;
            }
            rc = true;
            eigeneStage.hide();
        }
    }

    /*************Logiken*************************/
    
    public int liefereOffsetZuIdentInParmaeterSetArray(int ident) {
        int gef = -1;
        for (int i = 0; i < parameterSetArray.length; i++) {
            if (parameterSetArray[i].ident == ident) {
                gef = i;
            }
        }
        return gef;
    }

    /******************Initialisierung******************/

    /*Eingabewerte für funktion==2*/
    public EclEmittenten[] mandantenArray = null;

    /** The rc. */
    /*Ausgabewerte für funktion=2*/
    public boolean rc = false;

    /** The gewaehlter mandant. */
    /*Einer von beiden ist dann ungleich null => das ist dann der ausgewählte*/
    public EclEmittenten gewaehlterMandant = null;

    /** The gewaehltes parameter set. */
    public EclParameterSet gewaehltesParameterSet = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
        funktion = pFunktion;
    }

}
