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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComBl.BlAufgaben;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlPasswortPruefen.
 */
public class CtrlPasswortPruefen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf anz zu pruefen. */
    @FXML
    private TextField tfAnzZuPruefen;

    /** The tf anz geprueft. */
    @FXML
    private TextField tfAnzGeprueft;

    /** The btn zwischen speichern. */
    @FXML
    private Button btnZwischenSpeichern;

    /** The btn speichern beenden. */
    @FXML
    private Button btnSpeichernBeenden;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn annehmen. */
    @FXML
    private Button btnAnnehmen;

    /** The btn ablehnen. */
    @FXML
    private Button btnAblehnen;

    /** The tf aktionaersnummer. */
    @FXML
    private TextField tfAktionaersnummer;

    /** The tf name 3. */
    @FXML
    private TextField tfName3;

    /** The tf nachname. */
    @FXML
    private TextField tfNachname;

    /** The tf name 1. */
    @FXML
    private TextField tfName1;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf name 2. */
    @FXML
    private TextField tfName2;

    /** The tf anforderung strasse. */
    @FXML
    private TextField tfAnforderungStrasse;

    /** The tf anforderung ort. */
    @FXML
    private TextField tfAnforderungOrt;

    /** The tf strasse. */
    @FXML
    private TextField tfStrasse;

    /** The tf postfach. */
    @FXML
    private TextField tfPostfach;

    /** The tf postleitzahl. */
    @FXML
    private TextField tfPostleitzahl;

    /** The tf postleitzahl postfach. */
    @FXML
    private TextField tfPostleitzahlPostfach;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf strasse versand. */
    @FXML
    private TextField tfStrasseVersand;

    /** The tf postfach versand. */
    @FXML
    private TextField tfPostfachVersand;

    /** The tf postleitzahl versand. */
    @FXML
    private TextField tfPostleitzahlVersand;

    /** The tf postleitzahl postfach versand. */
    @FXML
    private TextField tfPostleitzahlPostfachVersand;

    /** The tf ort versand. */
    @FXML
    private TextField tfOrtVersand;

    /** The tf adresszeile 1. */
    @FXML
    private TextField tfAdresszeile1;

    /** The tf adresszeile 2. */
    @FXML
    private TextField tfAdresszeile2;

    /** The tf adresszeile 3. */
    @FXML
    private TextField tfAdresszeile3;

    /** The tf adresszeile 4. */
    @FXML
    private TextField tfAdresszeile4;

    /** The tf adresszeile 5. */
    @FXML
    private TextField tfAdresszeile5;

    /** The tf adresszeile 6. */
    @FXML
    private TextField tfAdresszeile6;

    /** The tf adresszeile 7. */
    @FXML
    private TextField tfAdresszeile7;

    /** The tf adresszeile 8. */
    @FXML
    private TextField tfAdresszeile8;

    /** The tf adresszeile 9. */
    @FXML
    private TextField tfAdresszeile9;

    /** The tf adresszeile 10. */
    @FXML
    private TextField tfAdresszeile10;

    /** ********Ab hier individuell***********. */

    private DbBundle lDbBundle = null;

    /** The bl aufgaben. */
    private BlAufgaben blAufgaben = null;

    /** The anzahl noch zu pruefen. */
    private int anzahlNochZuPruefen = 0;

    /** The anzahl geprueft. */
    private int anzahlGeprueft = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tfAnzZuPruefen != null
                : "fx:id=\"tfAnzZuPruefen\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAnzGeprueft != null
                : "fx:id=\"tfAnzGeprueft\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert btnZwischenSpeichern != null
                : "fx:id=\"btnZwischenSpeichern\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert btnSpeichernBeenden != null
                : "fx:id=\"btnSpeichernBeenden\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfName3 != null : "fx:id=\"tfName3\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfNachname != null
                : "fx:id=\"tfNachname\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfName1 != null : "fx:id=\"tfName1\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfVorname != null : "fx:id=\"tfVorname\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfName2 != null : "fx:id=\"tfName2\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAnforderungStrasse != null
                : "fx:id=\"tfAnforderungStrasse\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAnforderungOrt != null
                : "fx:id=\"tfAnforderungOrt\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfStrasse != null : "fx:id=\"tfStrasse\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostfach != null
                : "fx:id=\"tfPostfach\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostleitzahl != null
                : "fx:id=\"tfPostleitzahl\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostleitzahlPostfach != null
                : "fx:id=\"tfPostleitzahlPostfach\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfStrasseVersand != null
                : "fx:id=\"tfStrasseVersand\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostfachVersand != null
                : "fx:id=\"tfPostfachVersand\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostleitzahlVersand != null
                : "fx:id=\"tfPostleitzahlVersand\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfPostleitzahlPostfachVersand != null
                : "fx:id=\"tfPostleitzahlPostfachVersand\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfOrtVersand != null
                : "fx:id=\"tfOrtVersand\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile1 != null
                : "fx:id=\"tfAdresszeile1\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile2 != null
                : "fx:id=\"tfAdresszeile2\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile3 != null
                : "fx:id=\"tfAdresszeile3\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile4 != null
                : "fx:id=\"tfAdresszeile4\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile5 != null
                : "fx:id=\"tfAdresszeile5\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile6 != null
                : "fx:id=\"tfAdresszeile6\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile7 != null
                : "fx:id=\"tfAdresszeile7\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile8 != null
                : "fx:id=\"tfAdresszeile8\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile9 != null
                : "fx:id=\"tfAdresszeile9\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";
        assert tfAdresszeile10 != null
                : "fx:id=\"tfAdresszeile10\" was not injected: check your FXML file 'PasswortPruefen.fxml'.";

        /********** Ab hier individuell *******************/

        lDbBundle = new DbBundle();

        blAufgaben = new BlAufgaben(false, lDbBundle);
        blAufgaben.pw_vorbereitenPruefen();
        anzahlNochZuPruefen = blAufgaben.rcAufgabenListe.size();
        anzahlGeprueft = 0;
        updateAnzahl();
        zeigeNaechstenZuPruefendenAn();
    }

    /**
     * **************Eingabe**************************************.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn speichern beenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernBeenden(ActionEvent event) {
        blAufgaben.pw_speicherePruefergebnis();
        eigeneStage.hide();
    }

    /**
     * On btn zwischen speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnZwischenSpeichern(ActionEvent event) {
        blAufgaben.pw_speicherePruefergebnis();

        blAufgaben = new BlAufgaben(false, lDbBundle);
        blAufgaben.pw_vorbereitenPruefen();
        anzahlNochZuPruefen = blAufgaben.rcAufgabenListe.size();
        anzahlGeprueft = 0;
        updateAnzahl();
        zeigeNaechstenZuPruefendenAn();

    }

    /**
     * On btn ablehnen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAblehnen(ActionEvent event) {
        blAufgaben.pw_setzeAufAbgelehnt(anzahlGeprueft);
        anzahlGeprueft++;
        anzahlNochZuPruefen--;
        updateAnzahl();
        zeigeNaechstenZuPruefendenAn();
    }

    /**
     * On btn annehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAnnehmen(ActionEvent event) {
        blAufgaben.pw_setzeAufGeprueft(anzahlGeprueft);

        anzahlGeprueft++;
        anzahlNochZuPruefen--;
        updateAnzahl();
        zeigeNaechstenZuPruefendenAn();
    }

    /**
     * *************Anzeigefunktionen**************************.
     */
    private void updateAnzahl() {
        tfAnzZuPruefen.setText(Integer.toString(anzahlNochZuPruefen));
        tfAnzGeprueft.setText(Integer.toString(anzahlGeprueft));
    }

    /**
     * Zeige naechsten zu pruefenden an.
     */
    private void zeigeNaechstenZuPruefendenAn() {
        if (anzahlGeprueft >= blAufgaben.rcAufgabenListe.size()) {
            leereAnzeige();
            btnAnnehmen.setDisable(true);
            btnAblehnen.setDisable(true);
            return;
        }
        EclAufgaben lAufgaben = blAufgaben.rcAufgabenListe.get(anzahlGeprueft);
        EclAktienregister lAktienregister = blAufgaben.rcAktienregisterListe.get(anzahlGeprueft);
        EclPersonenNatJur lPersonenNatJur = blAufgaben.rcPersonNatJurListe.get(anzahlGeprueft);

        if (lPersonenNatJur == null) {/*Aktienregistereintrag zum Prüfen*/
            tfAktionaersnummer.setText(lAktienregister.aktionaersnummer);
            tfName3.setText(lAktienregister.name3);
            tfNachname.setText(lAktienregister.nachname);
            tfName1.setText(lAktienregister.name1);
            tfVorname.setText(lAktienregister.vorname);
            tfName2.setText(lAktienregister.name2);

            tfAnforderungStrasse.setText(lAufgaben.argument[1]);
            tfAnforderungOrt.setText(lAufgaben.argument[2]);

            tfStrasse.setText(lAktienregister.strasse);
            tfPostfach.setText(lAktienregister.postfach);
            tfPostleitzahl.setText(lAktienregister.postleitzahl);
            tfPostleitzahlPostfach.setText(lAktienregister.postleitzahlPostfach);
            tfOrt.setText(lAktienregister.ort);
            tfStrasseVersand.setText(lAktienregister.strasseVersand);
            tfPostfachVersand.setText(lAktienregister.postfachVersand);
            tfPostleitzahlVersand.setText(lAktienregister.postleitzahlVersand);
            tfPostleitzahlPostfachVersand.setText(lAktienregister.postleitzahlPostfachVersand);
            tfOrtVersand.setText(lAktienregister.ortVersand);

            tfAdresszeile1.setText(lAktienregister.adresszeile1);
            tfAdresszeile2.setText(lAktienregister.adresszeile2);
            tfAdresszeile3.setText(lAktienregister.adresszeile3);
            tfAdresszeile4.setText(lAktienregister.adresszeile4);
            tfAdresszeile5.setText(lAktienregister.adresszeile5);
            tfAdresszeile6.setText(lAktienregister.adresszeile6);
            tfAdresszeile7.setText(lAktienregister.adresszeile7);
            tfAdresszeile8.setText(lAktienregister.adresszeile8);
            tfAdresszeile9.setText(lAktienregister.adresszeile9);
            tfAdresszeile10.setText(lAktienregister.adresszeile10);
        } else {
            /*Sonstige Kennung zum Prüfen*/
            tfAktionaersnummer.setText(lPersonenNatJur.loginKennung);
            tfName3.setText("");
            tfNachname.setText(lPersonenNatJur.name);
            tfName1.setText("");
            tfVorname.setText(lPersonenNatJur.vorname);
            tfName2.setText("");

            tfAnforderungStrasse.setText(lAufgaben.argument[1]);
            tfAnforderungOrt.setText(lAufgaben.argument[2]);

            tfStrasse.setText(lPersonenNatJur.strasse);
            tfPostfach.setText("");
            tfPostleitzahl.setText(lPersonenNatJur.plz);
            tfPostleitzahlPostfach.setText("");
            tfOrt.setText(lPersonenNatJur.ort);
            tfStrasseVersand.setText("");
            tfPostfachVersand.setText("");
            tfPostleitzahlVersand.setText("");
            tfPostleitzahlPostfachVersand.setText("");
            tfOrtVersand.setText("");

            tfAdresszeile1.setText("");
            tfAdresszeile2.setText("");
            tfAdresszeile3.setText("");
            tfAdresszeile4.setText("");
            tfAdresszeile5.setText("");
            tfAdresszeile6.setText("");
            tfAdresszeile7.setText("");
            tfAdresszeile8.setText("");
            tfAdresszeile9.setText("");
            tfAdresszeile10.setText("");

        }

    }

    /**
     * Leere anzeige.
     */
    private void leereAnzeige() {
        tfAktionaersnummer.setText("");
        tfName3.setText("");
        tfNachname.setText("");
        tfName1.setText("");
        tfVorname.setText("");
        tfName2.setText("");

        tfAnforderungStrasse.setText("");
        tfAnforderungOrt.setText("");

        tfStrasse.setText("");
        tfPostfach.setText("");
        tfPostleitzahl.setText("");
        tfPostleitzahlPostfach.setText("");
        tfOrt.setText("");
        tfStrasseVersand.setText("");
        tfPostfachVersand.setText("");
        tfPostleitzahlVersand.setText("");
        tfPostleitzahlPostfachVersand.setText("");
        tfOrtVersand.setText("");

        tfAdresszeile1.setText("");
        tfAdresszeile2.setText("");
        tfAdresszeile3.setText("");
        tfAdresszeile4.setText("");
        tfAdresszeile5.setText("");
        tfAdresszeile6.setText("");
        tfAdresszeile7.setText("");
        tfAdresszeile8.setText("");
        tfAdresszeile9.setText("");
        tfAdresszeile10.setText("");

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
