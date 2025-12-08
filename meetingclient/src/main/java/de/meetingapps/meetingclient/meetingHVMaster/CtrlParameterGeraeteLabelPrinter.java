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

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteLabelPrinter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterGeraeteLabelPrinter.
 */
public class CtrlParameterGeraeteLabelPrinter {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf arbeitsplatz. */
    @FXML
    private TextField tfArbeitsplatz;

    /** The btn zuordnen. */
    @FXML
    private Button btnZuordnen;

    /** The btn zuordnung loeschen. */
    @FXML
    private Button btnZuordnungLoeschen;

    /** The scrpn vorhandene zuordnungen. */
    @FXML
    private ScrollPane scrpnVorhandeneZuordnungen;

    /** The tf label printer IP. */
    @FXML
    private TextField tfLabelPrinterIP;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tfArbeitsplatz != null : "fx:id=\"tfArbeitsplatz\" was not injected: check your FXML file 'ParameterGeraeteLabelPrinter.fxml'.";
        assert btnZuordnen != null : "fx:id=\"btnZuordnen\" was not injected: check your FXML file 'ParameterGeraeteLabelPrinter.fxml'.";
        assert btnZuordnungLoeschen != null : "fx:id=\"btnZuordnungLoeschen\" was not injected: check your FXML file 'ParameterGeraeteLabelPrinter.fxml'.";
        assert scrpnVorhandeneZuordnungen != null : "fx:id=\"scrpnVorhandeneZuordnungen\" was not injected: check your FXML file 'ParameterGeraeteLabelPrinter.fxml'.";
        assert tfLabelPrinterIP != null : "fx:id=\"tfLabelPrinterIP\" was not injected: check your FXML file 'ParameterGeraeteLabelPrinter.fxml'.";

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                //                		CaZeigeHinweis zeigeHinweis=new CaZeigeHinweis();
                //                		zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        zeigeZuordnungenLabelPrinter();
        lDbBundle.closeAll();

    }

    /**
     * Zeige zuordnungen label printer.
     */
    private void zeigeZuordnungenLabelPrinter() {
        scrpnVorhandeneZuordnungen.setContent(null);

        lDbBundle.dbGeraeteLabelPrinter.read_all();
        int anzZuordnungen = lDbBundle.dbGeraeteLabelPrinter.anzErgebnis();
        if (anzZuordnungen > 0) {
            GridPane grpnZuordnungen = new GridPane();
            grpnZuordnungen.setVgap(5);
            grpnZuordnungen.setHgap(5);

            for (int i = 0; i < anzZuordnungen; i++) {

                EclGeraeteLabelPrinter lGeraeteLabelPrinter = lDbBundle.dbGeraeteLabelPrinter.ergebnisPosition(i);
                Label lGeraeteNr = new Label();
                lGeraeteNr.setText("Gerät " + Integer.toString(lGeraeteLabelPrinter.geraeteIdent));
                grpnZuordnungen.add(lGeraeteNr, 0, i);
                Label lIPNr = new Label();
                lIPNr.setText(lGeraeteLabelPrinter.ipLabelprinter);
                grpnZuordnungen.add(lIPNr, 1, i);
            }
            scrpnVorhandeneZuordnungen.setContent(grpnZuordnungen);

        }
    }

    /** The von geraet. */
    private int vonGeraet = 0;

    /**
     * Pruefe eingabe felder.
     *
     * @return true, if successful
     */
    private boolean pruefeEingabeFelder() {
        vonGeraet = 0;

        /*Von Gerät*/
        String hString = tfArbeitsplatz.getText();
        if (hString.isEmpty() || !CaString.isNummern(hString)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte gültige Arbeitsplatznummer eingeben!");
            return false;
        }
        vonGeraet = Integer.parseInt(hString);
        if (vonGeraet < 1 || vonGeraet > 9999) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Arbeitsplatznummer muß zwischen 1 und 9999 liegen!");
            return false;
        }

        /*Länge IP*/
        if (tfLabelPrinterIP.getText().length() > 50) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "IP-Adresse darf maximal 50 Zeichen haben!");
            return false;
        }

        return true;
    }

    /**************************Anzeigefunktionen***************************************/

    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /********************Aktionen auf Oberfläche*************************/
    
    @FXML
    void clickedZuordnen(ActionEvent event) {
        if (!pruefeEingabeFelder()) {
            return;
        }
        lDbBundle.openAll();

        lDbBundle.dbGeraeteLabelPrinter.delete(vonGeraet);
        EclGeraeteLabelPrinter lGeraeteLabelPrinter = new EclGeraeteLabelPrinter();
        lGeraeteLabelPrinter.geraeteIdent = vonGeraet;
        lGeraeteLabelPrinter.ipLabelprinter = tfLabelPrinterIP.getText();
        lDbBundle.dbGeraeteLabelPrinter.insert(lGeraeteLabelPrinter);

        tfArbeitsplatz.setText("");

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();

        zeigeZuordnungenLabelPrinter();
        lDbBundle.closeAll();

    }

    /**
     * Clicked zuordnung loeschen.
     *
     * @param event the event
     */
    @FXML
    void clickedZuordnungLoeschen(ActionEvent event) {
        if (!pruefeEingabeFelder()) {
            return;
        }

        lDbBundle.openAll();

        lDbBundle.dbGeraeteLabelPrinter.delete(vonGeraet);

        tfArbeitsplatz.setText("");

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();

        zeigeZuordnungenLabelPrinter();
        lDbBundle.closeAll();

    }

}
