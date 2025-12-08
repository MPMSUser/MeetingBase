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
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkartenStapelbuchenBestaetigung.
 */
public class CtrlSammelkartenStapelbuchenBestaetigung extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl ueberschrift. */
    @FXML
    private Label lblUeberschrift;

    /** The scpn aktionaere. */
    @FXML
    private ScrollPane scpnAktionaere;

    /** The btn ausfuehren. */
    @FXML
    private Button btnAusfuehren;

    /** The lbl text. */
    @FXML
    private Label lblText;

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        rc = false;
        eigeneStage.hide();
    }

    /**
     * Clicked ausfuehren.
     *
     * @param event the event
     */
    @FXML
    void clickedAusfuehren(ActionEvent event) {
        rc = true;
        eigeneStage.hide();
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SammelkartenStapelbuchenBestaetigung.fxml'.";
        assert lblUeberschrift != null
                : "fx:id=\"lblUeberschrift\" was not injected: check your FXML file 'SammelkartenStapelbuchenBestaetigung.fxml'.";
        assert scpnAktionaere != null
                : "fx:id=\"scpnAktionaere\" was not injected: check your FXML file 'SammelkartenStapelbuchenBestaetigung.fxml'.";
        assert btnAusfuehren != null
                : "fx:id=\"btnAusfuehren\" was not injected: check your FXML file 'SammelkartenStapelbuchenBestaetigung.fxml'.";
        assert lblText != null
                : "fx:id=\"lblText\" was not injected: check your FXML file 'SammelkartenStapelbuchenBestaetigung.fxml'.";

        fuelleFelder();
    }

    /**
     * Fuelle felder.
     */
    private void fuelleFelder() {
        lblUeberschrift.setText(ueberschrift);
        lblText.setText(bestaetigungsText);

        ObservableList<EclMeldungMitAktionaersWeisung> meldungMitAktionaersWeisung = null;
        meldungMitAktionaersWeisung = FXCollections.observableArrayList();
        for (int i = 0; i < ausgewaehlteAktionaereMeldungWeisung.length; i++) {
            meldungMitAktionaersWeisung.add(ausgewaehlteMeldungMitAktionaersWeisung[i]);
        }

        CtrlSammelkartenUebergreifend ctrlSammelkartenUebergreifend = new CtrlSammelkartenUebergreifend();

        TableView<EclMeldungMitAktionaersWeisung> tableAktionaere = ctrlSammelkartenUebergreifend
                .vorbereitenTableViewAktionaere(blAbstimmungenWeisungenErfassen, gattung);
        tableAktionaere.setPrefHeight(420);
        tableAktionaere.setPrefWidth(1130);
        tableAktionaere.setItems(meldungMitAktionaersWeisung);
        scpnAktionaere.setContent(tableAktionaere);

    }

    /** *********************************Logiken********************************. */

    private String ueberschrift = null;

    /** The bestaetigungs text. */
    private String bestaetigungsText = null;

    /** The ausgewaehlte aktionaere meldung weisung. */
    private EclWeisungMeldung[] ausgewaehlteAktionaereMeldungWeisung = null;

    /** The ausgewaehlte meldung mit aktionaers weisung. */
    private EclMeldungMitAktionaersWeisung[] ausgewaehlteMeldungMitAktionaersWeisung = null;

    /** The bl abstimmungen weisungen erfassen. */
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /** The gattung. */
    int gattung = 0;

    /** Return-Wert. true=ausführen, false=beenden */
    public boolean rc = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage                             the eigene stage
     * @param pUeberschrift                            the ueberschrift
     * @param pBestaetigungsText                       the bestaetigungs text
     * @param pAusgewaehlteAktionaereMeldungWeisung    the ausgewaehlte aktionaere
     *                                                 meldung weisung
     * @param pAusgewaehlteMeldungMitAktionaersWeisung the ausgewaehlte meldung mit
     *                                                 aktionaers weisung
     * @param pBlAbstimmungenWeisungenErfassen         the bl abstimmungen weisungen
     *                                                 erfassen
     * @param pGattung                                 the gattung
     */
    public void init(Stage pEigeneStage, String pUeberschrift, String pBestaetigungsText,
            EclWeisungMeldung[] pAusgewaehlteAktionaereMeldungWeisung,
            EclMeldungMitAktionaersWeisung[] pAusgewaehlteMeldungMitAktionaersWeisung,
            BlAbstimmungenWeisungen pBlAbstimmungenWeisungenErfassen, int pGattung) {
        eigeneStage = pEigeneStage;
        ueberschrift = pUeberschrift;
        bestaetigungsText = pBestaetigungsText;
        ausgewaehlteAktionaereMeldungWeisung = pAusgewaehlteAktionaereMeldungWeisung;
        ausgewaehlteMeldungMitAktionaersWeisung = pAusgewaehlteMeldungMitAktionaersWeisung;
        gattung = pGattung;
        blAbstimmungenWeisungenErfassen = pBlAbstimmungenWeisungenErfassen;
    }

}
