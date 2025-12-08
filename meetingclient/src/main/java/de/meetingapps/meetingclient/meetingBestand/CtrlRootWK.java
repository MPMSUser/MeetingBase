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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Root-Klasse für Abgabe Willenserklärungen durch Anmeldestelle.
 */
public abstract class CtrlRootWK {

    /** The tf aktionaersnummer. */
    @FXML
    TextField tfAktionaersnummer;

    /** The tf fehlermeldung. */
    @FXML
    TextArea tfFehlermeldung;

    /** The lbl hinweise. */
    @FXML
    Label lblHinweise;

    /** The tf erklaerung datum. */
    @FXML
    TextField tfErklaerungDatum;

    /** The tf erklaerung zeit. */
    @FXML
    TextField tfErklaerungZeit;

    /** The cb eingangs weg. */
    @FXML
    ComboBox<String> cbEingangsWeg;

    /** The btn speichern. */
    @FXML
    Button btnSpeichern;

    /** The btn abbruch. */
    @FXML
    Button btnAbbruch;

    /** The tf name vorname. */
    @FXML
    TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    TextField tfOrt;

    /** The tf aktien. */
    @FXML
    TextField tfAktien;

    /** The btn einlesen. */
    @FXML
    Button btnEinlesen;

    /** The tf zuletzt bearbeitet. */
    @FXML
    TextField tfZuletztBearbeitet;

    /** The btn text datei lesen. */
    @FXML
    Button btnTextDateiLesen;

    /** The btn scan lesen. */
    @FXML
    Button btnScanLesen;

    /** The tf dateiname. */
    @FXML
    TextField tfDateiname;

    /** The btn datei waehlen. */
    @FXML
    Button btnDateiWaehlen;

    /** The ecl T login daten M. */
    EclTLoginDatenM eclTLoginDatenM = null;

    /** The ecl zugeordnete meldung. */
    EclZugeordneteMeldungNeu eclZugeordneteMeldung = null;

    /**
     * 1=komplette Neuanemldung; 2=Eintrittskarte zu bestehender Meldung Siehe
     * KonstPortalAktion.
     */
    int hauptFunktion = 0;

    /** The fehler meldung speichern. */
    String fehlerMeldungSpeichern = "";

    /** The ws client. */
    WSClient wsClient = null;

    /** The eigene stage. */
    Stage eigeneStage;

    /**
     * Initialisierung allgemein.
     */
    /*++++++++++++++++++++Initialisierung+++++++++++++++++++++++*/
    void initialisierungAllgemein() {
        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        /*Eingangsweg setzen*/
        SClErfassungsDaten.initErfassungsfelder(tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

    }

    /**
     * Clicked einlesen.
     *
     * @param event the event
     */
    /*+++++++++++++++++++++++++Reaktionen auf Oberfläche++++++++++++++++*/
    @FXML
    void clickedEinlesen(ActionEvent event) {
        doEinlesen();
    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        setzeStatusAktionaersnummerEingeben();
        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
            eigeneStage.hide();
        }

    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {

        wsClient = null;

        if (pruefeAllgemeineEingaben() == false) {
            return;
        }

        if (pruefeEingaben_Funktion() == false) {
            return;
        }

        boolean rc = speichern_Funktion();
        if (rc == false) {
            tfFehlermeldung.setText(fehlerMeldungSpeichern);
            return;
        }

        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
            eigeneStage.hide();
        }

        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet
                .setText(eclTLoginDatenM.anmeldeKennungFuerAnzeige + " " + eclTLoginDatenM.liefereVornameNameTitel());
        tfFehlermeldung.setText(fehlerMeldungSpeichern);

        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
            eigeneStage.hide();
        }

        return;

    }

    /**
     * Pruefe eingaben funktion.
     *
     * @return true, if successful
     */
    abstract boolean pruefeEingaben_Funktion();

    /**
     * Speichern funktion.
     *
     * @return true, if successful
     */
    abstract boolean speichern_Funktion();

    /**
     * On key aktionaersnummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyAktionaersnummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doEinlesen();
        }
    }

    /**
     * Do einlesen.
     */
    /*+++++++++++++++++++++Verarbeitungsfunktionen+++++++++++++++++++++++++++*/
    void doEinlesen() {

        wsClient = null;

        boolean rc = einlesen();

        if (rc == false) {
            tfFehlermeldung.setText(fehlerMeldungSpeichern);
            setzeStatusAbbruch();
            return;
        } else {
            tfFehlermeldung.setText("Anmeldung kann durchgeführt werden.");
            setzeStatusVerarbeitung();
        }
    }

    /**
     * Einlesen.
     *
     * @return true, if successful
     */
    boolean einlesen() {

        WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;

        fehlerMeldungSpeichern = "";

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELogin weLogin = new WELogin();

        /***********************
         * Aktionärsdaten grundsätzlich holen / überprüfen auf Existenz
         ************/
        /*Hinweise:
         * > mandant wird automatisch gesetzt aus ClGlobalVar.mandant - diese vorher belegen!
         * > user, uKennung, uPasswort werden automatisch gesetzt
         *
         */
        weLogin.setKennungArt(1); /* kennung enthält Aktionärsnummer*/
        if (tfAktionaersnummer.getText() == null || tfAktionaersnummer.getText().isEmpty()) {
            fehlerMeldungSpeichern = "Bitte Aktionärsnummer eingeben!";
            return false;
        }

        if (tfAktionaersnummer.getText().startsWith("S")) {
            fehlerMeldungSpeichern = "Bitte Aktionärsnummer eingeben!";
            return false;
        }

        DbBundle lDbBundle = new DbBundle();
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(lDbBundle);
        String aktionaersNummerInArbeit = blNummernformBasis
                .loginKennungAufbereitenFuerIntern(tfAktionaersnummer.getText());
        tfAktionaersnummer.setText(aktionaersNummerInArbeit);

        weLogin.setKennung(tfAktionaersnummer.getText());
        WELoginCheck weLoginCheck = new WELoginCheck();
        weLoginCheck.weLogin = weLogin;

        /*Die Rückgegebenen Aktionärsdaten speichern, da sie für weitere Aktionen
         * benötigt werden
         */
        WELoginCheckRC weLoginCheckRC = null;
        weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
        eclTLoginDatenM = weLoginCheckRC.eclTLoginDatenM;

        int rc = weLoginCheckRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afKennungUnbekannt:
                fehlerMeldungSpeichern = "Aktionärsnummer gibt es nicht!";
                break;
            default:
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                CaBug.drucke("001");
                break;
            }
            return false;
        }

        if (weLoginCheckRC.anzahlAktionaersnummernVorhanden > 1) {
            lblHinweise.setText("Achtung - mehrere Registereinträge zu dieser Aktionärsnummer");
        }

        tfNameVorname.setText(eclTLoginDatenM.liefereVornameNameTitel());
        tfOrt.setText(eclTLoginDatenM.ort);
        tfAktien.setText(eclTLoginDatenM.stimmenDE);

        /**************************
         * Aktionärs-Status holen
         *****************************************/
        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
        rc = weTeilnehmerStatusGetRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            default:
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                CaBug.drucke("002");
                break;
            }
            return false;
        }
        if (pruefeObSchonAngemeldet(weTeilnehmerStatusGetRC)) {
            fehlerMeldungSpeichern = "Aktionär ist bereits zur HV angemeldet - Eintrittskartenausstellung mit dieser Funktion nicht möglich!";
            return false;
        }

        if (!pruefeNachEinlesen_Funktion()) {
            return false;
        }
        return true;
    }

    /**
     * Pruefe nach einlesen funktion.
     *
     * @return true, if successful
     */
    abstract boolean pruefeNachEinlesen_Funktion();

    /**
     * Pruefe ob schon angemeldet.
     *
     * @param weTeilnehmerStatusGetRC the we teilnehmer status get RC
     * @return true, if successful
     */
    boolean pruefeObSchonAngemeldet(WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC) {
        if (weTeilnehmerStatusGetRC.besitzJeKennungListe == null) {
            CaBug.drucke("001");
            return false;
        }
        if (weTeilnehmerStatusGetRC.besitzJeKennungListe.size() < 1) {
            CaBug.drucke("002");
            return false;
        }
        EclBesitzJeKennung eclBesitzJeKennung = weTeilnehmerStatusGetRC.besitzJeKennungListe.get(0);

        if (eclBesitzJeKennung.eigenerAREintragVorhanden == false) {
            CaBug.drucke("003");
            return false;
        }
        if (eclBesitzJeKennung.eigenerAREintragListe.size() < 1) {
            CaBug.drucke("004");
            return false;
        }

        EclBesitzAREintrag eclBesitzAREintrag = eclBesitzJeKennung.eigenerAREintragListe.get(0);
        return eclBesitzAREintrag.angemeldet;
    }

    /*+++++++++++++++++++++++++++++Setzen des Bildschirminhalts auf den jeweiligen Status der Eingabe+++++++++++*/

    /**
     * Setze status aktionaersnummer eingeben.
     */
    void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(true);
        tfAktionaersnummer.setText("");
        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        tfFehlermeldung.setText("");
        tfAktionaersnummer.setEditable(true);
        tfAktionaersnummer.requestFocus();
        lblHinweise.setText("");

        if (btnTextDateiLesen != null) {
            btnTextDateiLesen.setDisable(false);
        }
        if (btnScanLesen != null) {
            btnScanLesen.setDisable(false);
        }
        if (tfDateiname != null) {
            tfDateiname.setDisable(false);
        }
        if (btnDateiWaehlen != null) {
            btnDateiWaehlen.setDisable(false);
        }

        setzeStatusAktionaersnummerEingeben_Funktion();
    }

    /**
     * Setze status aktionaersnummer eingeben funktion.
     */
    abstract void setzeStatusAktionaersnummerEingeben_Funktion();

    /**
     * Setze status abbruch.
     */
    void setzeStatusAbbruch() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(false);
        tfAktionaersnummer.setEditable(false);
        btnAbbruch.requestFocus();

        setzeStatusAbbruch_Funktion();
    }

    /**
     * Setze status abbruch funktion.
     */
    abstract void setzeStatusAbbruch_Funktion();

    /**
     * Setze status verarbeitung.
     */
    void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(false);
        btnAbbruch.setDisable(false);
        tfAktionaersnummer.setEditable(false);

        if (btnTextDateiLesen != null) {
            btnTextDateiLesen.setDisable(true);
        }
        if (btnScanLesen != null) {
            btnScanLesen.setDisable(true);
        }
        if (tfDateiname != null) {
            tfDateiname.setDisable(true);
        }
        if (btnDateiWaehlen != null) {
            btnDateiWaehlen.setDisable(true);
        }

        btnSpeichern.requestFocus();

        setzeStatusVerarbeitung_Funktion();

    }

    /**
     * Setze status verarbeitung funktion.
     */
    abstract void setzeStatusVerarbeitung_Funktion();

    /**
     * Pruefe allgemeine eingaben.
     *
     * @return true, if successful
     */
    boolean pruefeAllgemeineEingaben() {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return false;
        }
        return true;
    }

}
