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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungErteilen;
import de.meetingapps.meetingportal.meetComWE.WEWeisungErteilenRC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlMassentest.
 */
public class CtrlMassentest {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /** The tf offset. */
    @FXML
    private TextField tfOffset;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The tf anzahl. */
    @FXML
    private TextField tfAnzahl;

    /** The lbl start. */
    @FXML
    private Label lblStart;

    /** The lbl zwischenstand. */
    @FXML
    private Label lblZwischenstand;

    /** The lbl ende. */
    @FXML
    private Label lblEnde;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnStarten != null : "fx:id=\"btnStarten\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert tfOffset != null : "fx:id=\"tfOffset\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert tfAnzahl != null : "fx:id=\"tfAnzahl\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert lblStart != null : "fx:id=\"lblStart\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert lblZwischenstand != null
                : "fx:id=\"lblZwischenstand\" was not injected: check your FXML file 'Massentest.fxml'.";
        assert lblEnde != null : "fx:id=\"lblEnde\" was not injected: check your FXML file 'Massentest.fxml'.";

        /************* Ab hier individuell ********************************************/

    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        eigeneStage.hide();
    }

    /** The anzahl. */
    private int anzahl = 0;

    /** The offset. */
    private int offset = 0;

    /**
     * Btn starten clicked.
     *
     * @param event the event
     */
    @FXML
    void btnStartenClicked(ActionEvent event) {
        int rc;

        lblStart.setText("");
        if (!CaString.isNummern(tfAnzahl.getText())) {
            lblStart.setText("Anzahl gültig eingeben");
            return;
        }
        if (!CaString.isNummern(tfOffset.getText())) {
            lblStart.setText("Offset gültig eingeben");
            return;
        }
        anzahl = Integer.parseInt(tfAnzahl.getText());
        offset = Integer.parseInt(tfOffset.getText());
        lblStart.setText(CaDatumZeit.DatumZeitStringFuerDatenbank());
        eigeneStage.show();
        int i;
        for (i = 1 + offset; i < anzahl + offset; i++) {

            WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;
            WSClient wsClient = new WSClient();
            WELogin weLogin = new WELogin();
            weLogin.setKennung(Integer.toString(i));
            WELoginCheck weLoginCheck = new WELoginCheck();
            weLoginCheck.weLogin = weLogin;

            EclTLoginDatenM eclTLoginDatenM = null;
            WELoginCheckRC weLoginCheckRC = null;
            weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
            eclTLoginDatenM = weLoginCheckRC.eclTLoginDatenM;

            /**************************
             * Aktionärs-Status holen
             *****************************************/

            wsClient = new WSClient();
            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
            rc = weTeilnehmerStatusGetRC.getRc();
            if (rc != 1) {
            }

            /**************** Briefwahl speichern *****************************************/

            weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            int quelle = 1;
            weLoginVerify.setEingabeQuelle(quelle); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
            weLoginVerify.setErteiltZeitpunkt(CaDatumZeit.DatumZeitStringFuerDatenbank());

            List<String> lWeisungenAgenda = new LinkedList<String>();
            List<String> lWeisungenGegenantraege = new LinkedList<String>();
            String hString = "";
            int i1;
            for (i1 = 0; i1 < 10; i1++) {
                hString = "-";
                lWeisungenAgenda.add(hString);
            }
            for (i1 = 0; i1 < 10; i1++) {
                hString = "-";
                lWeisungenGegenantraege.add(hString);
            }

            WEWeisungErteilen weWeisungErteilen = new WEWeisungErteilen();
            weWeisungErteilen.setWeLoginVerify(weLoginVerify);

            weWeisungErteilen.setAusgewaehlteHauptAktion(1);

            weWeisungErteilen.setAusgewaehlteAktion(5);

            weWeisungErteilen.setAnmeldeAktionaersnummer(eclTLoginDatenM.eclAktienregister.aktionaersnummer);

            weWeisungErteilen.setWeisungenAgenda(lWeisungenAgenda);

            weWeisungErteilen.setWeisungenGegenantraege(lWeisungenAgenda);

            WEWeisungErteilenRC weWeisungErteilenRC = wsClient.weisungErteilen(weWeisungErteilen);

            rc = weWeisungErteilenRC.getRc();
            if ((i - offset) % 100 == 0) {
                lblZwischenstand.setText((i - offset) + "Briefwahl " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }

        }

        lblEnde.setText(CaDatumZeit.DatumZeitStringFuerDatenbank());

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
        anzahl = 0;
        offset = 0;

    }

}
