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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkartenNeueEK.
 */
public class CtrlSammelkartenNeueEK extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn zuordnen. */
    @FXML
    private Button btnZuordnen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The tf ek nr. */
    @FXML
    private TextField tfEkNr;

    /** The tf manuell von. */
    @FXML
    private TextField tfManuellVon;

    /** The tf manuell bis. */
    @FXML
    private TextField tfManuellBis;

    /** The tf sannek von. */
    @FXML
    private TextField tfSannekVon;

    /** The tf sammel bis. */
    @FXML
    private TextField tfSammelBis;

    /** The btn suche EK. */
    @FXML
    private Button btnSucheEK;

    /** The db bundle. */
    DbBundle dbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert btnZuordnen != null
                : "fx:id=\"btnZuordnen\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert tfEkNr != null : "fx:id=\"tfEkNr\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert btnSucheEK != null
                : "fx:id=\"btnSucheEK\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert tfManuellVon != null
                : "fx:id=\"tfManuellVon\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert tfManuellBis != null
                : "fx:id=\"tfManuellBis\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert tfSannekVon != null
                : "fx:id=\"tfSannekVon\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";
        assert tfSammelBis != null
                : "fx:id=\"tfSammelBis\" was not injected: check your FXML file 'SammelkartenNeueEK.fxml'.";

        /*************** Ab hier individuell **********************************/
        dbBundle = new DbBundle();
        zeigeNummernkreise();
    }

    /**
     * *************Click-Reaktionen****************.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Clicked btn zuordnen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnZuordnen(ActionEvent event) {
        lFehlertext = "";
        this.pruefeNichtLeerUndLaenge(tfEkNr, "Eintrittskarte", 15);
        this.pruefeZahlOderLeer(tfEkNr, "Eintrittskarte");
        if (!this.lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.neueEKFuerSammelkarte(aktuelleSammelMeldung.meldungsIdent, tfEkNr.getText());
        if (rc < 1) {
            fehlerMeldung(CaFehler.getFehlertext(rc, 0));
            return;
        }
        eigeneStage.hide();
    }

    /**
     * Clicked suche EK.
     *
     * @param event the event
     */
    @FXML
    void clickedSucheEK(ActionEvent event) {
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        String neueKartenNr = blSammelkarten.sucheNeueEKFuerSammelkarte();
        if (neueKartenNr.isEmpty()) {
            fehlerMeldung("EK-Nr. kann nicht automatisch vergeben werden! Bitte Nummernkreis überprüfen!");
            return;
        }
        tfEkNr.setText(neueKartenNr);
    }

    /**
     * ******************Oberflächen******************************************.
     */

    private void zeigeNummernkreise() {
        tfManuellVon.setText(
                Integer.toString(ParamS.param.paramNummernkreise.vonSubEintrittskartennummer[aktuelleGattung][1]));
        tfManuellBis.setText(
                Integer.toString(ParamS.param.paramNummernkreise.bisSubEintrittskartennummer[aktuelleGattung][1]));

        tfSannekVon.setText(Integer.toString(ParamS.param.paramNummernkreise.vonSammelkartennummer));
        tfSammelBis.setText(Integer.toString(ParamS.param.paramNummernkreise.bisSammelkartennummer));
    }

    /** *********************************Logiken********************************. */

    private EclMeldung aktuelleSammelMeldung = null;

    /** The aktuelle gattung. */
    private int aktuelleGattung = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage   the eigene stage
     * @param pSammelMeldung the sammel meldung
     */
    public void init(Stage pEigeneStage, EclMeldung pSammelMeldung) {
        eigeneStage = pEigeneStage;
        aktuelleSammelMeldung = pSammelMeldung;
        aktuelleGattung = aktuelleSammelMeldung.liefereGattung();
    }

}
