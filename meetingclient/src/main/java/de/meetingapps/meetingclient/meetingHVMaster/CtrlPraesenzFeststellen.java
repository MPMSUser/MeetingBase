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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzFeststellen;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzGesamtabgang;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenNummer;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzFeststellen.
 */
public class CtrlPraesenzFeststellen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn praesenz feststellen. */
    @FXML
    private Button btnPraesenzFeststellen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl aktuelle praesenz nr. */
    @FXML
    private Label lblAktuellePraesenzNr;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /** The btn drucken. */
    @FXML
    private Button btnDrucken;

    /** The btn gesamtabgang. */
    @FXML
    private Button btnGesamtabgang;

    /** The lbl sammelkarten. */
    @FXML
    private Label lblSammelkarten;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The srv karten. */
    private List<EclMeldung> srvKarten = new ArrayList<>();

    /** The feststellen. */
    private int feststellen = 0;

    /**
     * On btn gesamtabgang.
     *
     * @param event the event
     */
    @FXML
    void onBtnGesamtabgang(ActionEvent event) {

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Gesamtabgang durchführen? Achtung - unwiderruflich! Vorher Datensicherung durchführen!",
                "Gesamtabgang durchführen", "Abbrechen");
        if (brc == false) {
            return;
        }

        /*Gesamtabgang ausführen*/
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlPraesenzGesamtabgang blPraesenzGesamtabgang = new BlPraesenzGesamtabgang(lDbBundle);
        blPraesenzGesamtabgang.gesamtabgangAusfuehren();
        lDbBundle.closeAll();
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnPraesenzFeststellen != null : "fx:id=\"btnPraesenzFeststellen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert lblAktuellePraesenzNr != null : "fx:id=\"lblAktuellePraesenzNr\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        
        /********************************Ab hier individuell*********************************************/
        
        btnDrucken.setGraphic(new FontIcon(FontAwesomeSolid.PRINT));

        refreshInhalt();

        lblMeldung.setText("");

    }

    /**
     * Refresh inhalt.
     */
    private void refreshInhalt() {
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernFuerBuchung();

        BlSammelkarten blSammel = new BlSammelkarten(false, lDbBundle);
        blSammel.holeSammelkartenMeldeDaten(true, 0);

        if (blSammel.rcSammelMeldung != null) {
            srvKarten = Arrays.asList(blSammel.rcSammelMeldung);
            srvKarten = srvKarten.stream()
                    .filter(e -> e.skIst == KonstSkIst.srv && e.stueckAktien > 0 && e.statusPraesenz != 1)
                    .collect(Collectors.toList());
        }

        lblSammelkarten.setText(srvKarten.isEmpty() ? "Alle SRV-Karten präsent" : "Nicht alle SRV-Karten präsent");
        lblSammelkarten.setTextFill(srvKarten.isEmpty() ? Color.valueOf("#0a7637") : Color.BLACK);

        lDbBundle.closeAll();

        String hText = "";
        if (lDbBundle.clGlobalVar.zuVerzeichnisNr1 == -1) {
            hText = "Erstpräsenz";
        } else {
            hText = Integer.toString(lDbBundle.clGlobalVar.zuVerzeichnisNr1) + ". Nachtrag";
        }
        lblAktuellePraesenzNr.setText(hText);
    }

    /**
     * Clicked btn praesenz feststellen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnPraesenzFeststellen(ActionEvent event) {

        if (feststellen > 0) {
            if (!CustomAlert.confirmAlert("Bitte Bestätigen", "Nächste Präsenz: " + lblAktuellePraesenzNr.getText(),
                    "Präsenz wirklich feststellen?")) {
                return;
            }
        }

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        if (lDbBundle.clGlobalVar.zuVerzeichnisNr1 == -1 && !srvKarten.isEmpty()) {
            if (!CustomAlert.confirmAlert("Bitte Bestätigen", "Erstpräsenz trotzdem feststellen?",
                    "Einige SRV-Karten mit Aktien sind nicht präsent")) {
                lDbBundle.closeAll();
                return;
            }
        }

        BlPraesenzFeststellen blPraesenzFeststellen = new BlPraesenzFeststellen();
        blPraesenzFeststellen.feststellen(lDbBundle, 1);

        lDbBundle.closeAll();
        refreshInhalt();
        lblMeldung.setText("Präsenz festgestellt");
        feststellen++;

    }

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {
        eigeneStage.close();
    }

    /**
     * On btn drucken.
     *
     * @param event the event
     */
    @FXML
    void onBtnDrucken(ActionEvent event) {
        Platform.runLater(() -> eigeneStage.close());

        CtrlHauptStage ctrl = new CtrlHauptStage();
        ctrl.gewPraesenzlisteDrucken(new ActionEvent());
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

    }

}
