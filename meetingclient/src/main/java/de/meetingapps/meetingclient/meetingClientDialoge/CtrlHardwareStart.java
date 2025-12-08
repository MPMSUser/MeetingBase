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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import de.meetingapps.meetingportal.meetComKonst.KonstFarben;
import de.meetingapps.meetingportal.meetComKonst.KonstServerart;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlHardwareStart.
 */
public class CtrlHardwareStart extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf server art. */
    @FXML
    private TextField tfServerArt;

    /** The tf server bezeichnung. */
    @FXML
    private TextField tfServerBezeichnung;

    /** The tf set nummer. */
    @FXML
    private TextField tfSetNummer;

    /** The tf set bezeichnung. */
    @FXML
    private TextField tfSetBezeichnung;

    /** The tf klassen nr. */
    @FXML
    private TextField tfKlassenNr;

    /** The tf klassen bezeichnung. */
    @FXML
    private TextField tfKlassenBezeichnung;

    /** The tf arbeitsplatz. */
    @FXML
    private TextField tfArbeitsplatz;

    /** The tf benutzer. */
    @FXML
    private TextField tfBenutzer;

    /** The tf ruecksetzen. */
    @FXML
    private TextField tfRuecksetzen;

    /** The tf DB nummer. */
    @FXML
    private TextField tfDBNummer;

    /** The tf DB text. */
    @FXML
    private TextField tfDBText;

    /** The tf DB pfad. */
    @FXML
    private TextField tfDBPfad;

    /** The tf web nummer. */
    @FXML
    private TextField tfWebNummer;

    /** The tf web text. */
    @FXML
    private TextField tfWebText;

    /** The tf web pfad. */
    @FXML
    private TextField tfWebPfad;

    /** The tf label printer IP. */
    @FXML
    private TextField tfLabelPrinterIP;

    /** The tf bon drucker aktiv. */
    @FXML
    private TextField tfBonDruckerAktiv;

    /** The tf arbeitspfad. */
    @FXML
    private TextField tfArbeitspfad;

    /** The tf sicherungspfad 1. */
    @FXML
    private TextField tfSicherungspfad1;

    /** The tf sicherungspfad 2. */
    @FXML
    private TextField tfSicherungspfad2;

    /** The tf zertifikatspfad. */
    @FXML
    private TextField tfZertifikatspfad;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The tf emittent nummer. */
    @FXML
    private TextField tfEmittentNummer;

    /** The tf emittent bezeichnung. */
    @FXML
    private TextField tfEmittentBezeichnung;

    /** The tf HV jahr. */
    @FXML
    private TextField tfHVJahr;

    /** The tf HV nummer. */
    @FXML
    private TextField tfHVNummer;

    /** The tf datenbereich. */
    @FXML
    private TextField tfDatenbereich;

    /** The tf version. */
    @FXML
    private TextField tfVersion;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfServerArt != null
                : "fx:id=\"tfServerArt\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfServerBezeichnung != null
                : "fx:id=\"tfServerBezeichnung\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfSetNummer != null
                : "fx:id=\"tfSetNummer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfSetBezeichnung != null
                : "fx:id=\"tfSetBezeichnung\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfKlassenNr != null
                : "fx:id=\"tfKlassenNr\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfKlassenBezeichnung != null
                : "fx:id=\"tfKlassenBezeichnung\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfArbeitsplatz != null
                : "fx:id=\"tfArbeitsplatz\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfBenutzer != null : "fx:id=\"tfBenutzer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfRuecksetzen != null
                : "fx:id=\"tfRuecksetzen\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfDBNummer != null : "fx:id=\"tfDBNummer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfDBText != null : "fx:id=\"tfDBText\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfDBPfad != null : "fx:id=\"tfDBPfad\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfWebNummer != null
                : "fx:id=\"tfWebNummer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfWebText != null : "fx:id=\"tfWebText\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfWebPfad != null : "fx:id=\"tfWebPfad\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfLabelPrinterIP != null
                : "fx:id=\"tfLabelPrinterIP\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfBonDruckerAktiv != null
                : "fx:id=\"tfBonDruckerAktiv\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfArbeitspfad != null
                : "fx:id=\"tfArbeitspfad\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfSicherungspfad1 != null
                : "fx:id=\"tfSicherungspfad1\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfSicherungspfad2 != null
                : "fx:id=\"tfSicherungspfad2\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfZertifikatspfad != null
                : "fx:id=\"tfZertifikatspfad\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert btnStarten != null : "fx:id=\"btnStarten\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfEmittentNummer != null
                : "fx:id=\"tfEmittentNummer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfEmittentBezeichnung != null
                : "fx:id=\"tfEmittentBezeichnung\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfHVJahr != null : "fx:id=\"tfHVJahr\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfHVNummer != null : "fx:id=\"tfHVNummer\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfDatenbereich != null
                : "fx:id=\"tfDatenbereich\" was not injected: check your FXML file 'HardwareStart.fxml'.";
        assert tfVersion != null : "fx:id=\"tfVersion\" was not injected: check your FXML file 'HardwareStart.fxml'.";

        /************* Ab hier individuell ********************************************/

        tfServerArt.setText(KonstServerart.getText(ParamS.paramGeraet.serverArt));
        switch (ParamS.paramGeraet.serverArt) {
        case KonstServerart.produktionsserverExternerHoster: {
            tfServerArt.setStyle("-fx-background-color: " + KonstFarben.programmstartExternerHoster + ";");
            break;
        }
        case KonstServerart.hvServer: {
            tfServerArt.setStyle("-fx-background-color: " + KonstFarben.programmstartHVServer + ";");
            break;
        }
        default: {
            tfServerArt.setStyle("-fx-background-color: " + KonstFarben.programmstartStandard + ";");
            break;
        }
        }
        tfServerBezeichnung.setText(ParamS.paramGeraet.serverBezeichnung);

        tfSetNummer.setText(Integer.toString(ParamS.eclGeraeteSet.ident));
        tfSetBezeichnung.setText(ParamS.eclGeraeteSet.kurzBeschreibung);
        tfKlassenNr.setText(Integer.toString(ParamS.paramGeraet.identKlasse));
        tfKlassenBezeichnung.setText(ParamS.paramGeraet.beschreibungKlasse);

        tfBenutzer.setText(ParamS.paramGeraet.festgelegterBenutzername);

        tfArbeitsplatz.setText(Integer.toString(ParamS.clGlobalVar.arbeitsplatz));

        if (ParamS.paramGeraet.lokalZuruecksetzen) {
            tfRuecksetzen.setText("Ja");
            tfRuecksetzen.setStyle("-fx-background-color: #ff0000; ");
        } else {
            tfRuecksetzen.setText("Nein");
        }

        if (ParamS.clGlobalVar.datenbankPfadNr >= 0) {
            tfDBNummer.setText(Integer.toString(ParamS.clGlobalVar.datenbankPfadNr));
            tfDBText.setText(ParamInterneKommunikation.datenbankPfadZurAuswahlText[ParamS.clGlobalVar.datenbankPfadNr]);
            tfDBPfad.setText(ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamS.clGlobalVar.datenbankPfadNr]);
        }

        if (ParamS.clGlobalVar.webServicePfadNr >= 0) {
            tfWebNummer.setText(Integer.toString(ParamS.clGlobalVar.webServicePfadNr));
            tfWebText.setText(ParamInterneKommunikation.webServicePfadZurAuswahlTest[ParamS.clGlobalVar.webServicePfadNr]);
            tfWebPfad.setText(ParamInterneKommunikation.webServicePfadZurAuswahl[ParamS.clGlobalVar.webServicePfadNr]);
        }

        tfLabelPrinterIP.setText(ParamS.paramGeraet.labelDruckerIPAdresse);

        if (ParamS.paramGeraet.bonDruckerIstZugeordnet) {
            tfBonDruckerAktiv.setText("Ja");
        } else {
            tfBonDruckerAktiv.setText("Nein");
        }

        tfArbeitspfad.setText(ParamS.paramGeraet.lwPfadAllgemein);
        tfSicherungspfad1.setText(ParamS.paramGeraet.lwPfadSicherung1);
        tfSicherungspfad2.setText(ParamS.paramGeraet.lwPfadSicherung2);
        tfZertifikatspfad.setText(ParamS.clGlobalVar.lwPfadZertifikat);

        tfEmittentNummer.setText(Integer.toString(ParamS.paramGeraet.festgelegterMandant));
        tfEmittentBezeichnung.setText(ParamS.paramGeraet.festgelegterMandantText);
        tfHVJahr.setText(Integer.toString(ParamS.paramGeraet.festgelegtesJahr));
        tfHVNummer.setText(ParamS.paramGeraet.festgelegteHVNummer);
        tfDatenbereich.setText(ParamS.paramGeraet.festgelegteDatenbank);

        tfVersion.setText("Client: " + SParamProgramm.programmVersion);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnStarten.requestFocus();
            }
        });

        btnStarten.setDefaultButton(true);

    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        fortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * Btn starten clicked.
     *
     * @param event the event
     */
    @FXML
    void btnStartenClicked(ActionEvent event) {
        fortsetzen = true;
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The fortsetzen. */
    public boolean fortsetzen = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
