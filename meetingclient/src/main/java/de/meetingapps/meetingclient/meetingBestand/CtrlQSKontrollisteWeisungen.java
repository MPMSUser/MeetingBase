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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootDrucklauf;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComReports.RepSammelkarten;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlQSKontrollisteWeisungen.
 */
public class CtrlQSKontrollisteWeisungen extends CtrlRootDrucklauf {

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /*Masken-spezifische Auswahl setzen*/
        auswahlReportausgabe = true;
        auswahlReportausgabeVarianten = false;
        auswahlCSV = true;
        auswahlCSVVarianten = false;

        auswahlDrucklaufAlleZulaessig = false;

        verarbeitungslaufArt = KonstVerarbeitungslaufArt.kontrollisteWeisungen;

        /*Allgemeine Initialisierung*/

        initializeRootDrucklaufFxml();

    }

    /**
     * On btn ausfuehren.
     *
     * @param event the event
     */
    @Override
    @FXML
    public void onBtnAusfuehren(ActionEvent event) {

        boolean brc = doAusfuehrenEingabeVerarbeiten();
        if (!brc) {
            return;
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.initListe(lDbBundle);

        RepSammelkarten repSammelkarten = new RepSammelkarten(false, lDbBundle,
                KonstWeisungserfassungSicht.internWeisungsreports);
        int rc = repSammelkarten.weisungKonstrolleDrucklauf(rcAusgabeReportGewaehlt, rcAusgabeCSVGewaehlt, rpDrucken,
                rcDruckvariante, rcDrucklaufNr, ParamS.clGlobalVar.arbeitsplatz, ParamS.clGlobalVar.benutzernr);

        System.out.println("rc=" + rc);
        if (rc == 0) {
            fehlerMeldung("Keine Sätze zum Drucken gefunden!");
            return;
        }

        if (rcAusgabeCSVGewaehlt) {
            GridPane grpnErgebnis = new GridPane();
            grpnErgebnis.setVgap(5);
            grpnErgebnis.setHgap(15);

            for (int i = 0; i < repSammelkarten.rcListeDerExportDateien.size(); i++) {
                Label hLabel = new Label(repSammelkarten.rcListeDerExportDateien.get(i));
                grpnErgebnis.add(hLabel, 0, i);

                Button btn = new Button();
                btn.setGraphic(new FontIcon(FontAwesomeSolid.FOLDER));
                final int x = i;
                btn.setOnAction(o -> {
                    try {
                        Desktop.getDesktop().open(new File(repSammelkarten.rcListeDerExportDateien.get(x)).getParentFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                grpnErgebnis.add(btn, 1, i);
            }
            doAusfuehrenErgebnisAnzeigen(grpnErgebnis);
        }

        doAusfuehrenBeendet();
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
