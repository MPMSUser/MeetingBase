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
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzProtokoll;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlProtokolleDrucken.
 */
public class CtrlProtokolleDrucken extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn P protokoll drucken. */
    @FXML
    private Button btnPProtokollDrucken;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl praesenz nr drucken. */
    @FXML
    private Label lblPraesenzNrDrucken;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /** The tf arbeitsplatz nr. */
    @FXML
    private TextField tfArbeitsplatzNr;

    /** The tf protokoll nr. */
    @FXML
    private TextField tfProtokollNr;

    /** The tf formular nummer. */
    @FXML
    private TextField tfFormularNummer;


    /** The grid protokolle. */
    @FXML
    private GridPane gridProtokolle;

    /** The btn protokoll abschliessen. */
    @FXML
    private Button btnProtokollAbschliessen;

    /** The lbl praesenz nr drucken 1. */
    @FXML
    private Label lblPraesenzNrDrucken1;

    /** The tf protokoll schliessen. */
    @FXML
    private TextField tfProtokollSchliessen;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnPProtokollDrucken != null : "fx:id=\"btnPProtokollDrucken\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert lblPraesenzNrDrucken != null : "fx:id=\"lblPraesenzNrDrucken\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert tfArbeitsplatzNr != null : "fx:id=\"tfArbeitsplatzNr\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert tfProtokollNr != null : "fx:id=\"tfProtokollNr\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert tfFormularNummer != null : "fx:id=\"tfFormularNummer\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert gridProtokolle != null : "fx:id=\"gridProtokolle\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert btnProtokollAbschliessen != null : "fx:id=\"btnProtokollAbschliessen\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert lblPraesenzNrDrucken1 != null : "fx:id=\"lblPraesenzNrDrucken1\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";
        assert tfProtokollSchliessen != null : "fx:id=\"tfProtokollSchliessen\" was not injected: check your FXML file 'PraesenzProtokolleDrucken.fxml'.";

        /********************************Ab hier individuell*********************************************/

        refreshInhalt();
        tfArbeitsplatzNr.setText("9999");
        tfFormularNummer.setText("02");
        lblMeldung.setText("");

    }

    /**
     * Refresh inhalt.
     */
    private void refreshInhalt() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        BlPraesenzProtokoll blPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
        blPraesenzProtokoll.fuelleLetztesAbgeschlossenesProtokoll();

        Label hLabel1 = new Label();
        hLabel1.setText("Arbeitspl.");
        Label hLabel2 = new Label();
        hLabel2.setText("Letztes abgeschl. Protokoll");
        gridProtokolle.add(hLabel1, 0, 0);
        gridProtokolle.add(hLabel2, 1, 0);
        if (blPraesenzProtokoll.arbeitsplatznr != null) {
            for (int i = 0; i < blPraesenzProtokoll.arbeitsplatznr.length; i++) {
                Label hLabela = new Label();
                hLabela.setText(Integer.toString(blPraesenzProtokoll.arbeitsplatznr[i]));
                Label hLabelb = new Label();
                hLabelb.setText(Integer.toString(blPraesenzProtokoll.letztesAbgeschlossenesProtokoll[i]));
                gridProtokolle.add(hLabela, 0, i + 1);
                gridProtokolle.add(hLabelb, 1, i + 1);

            }
        }

        lDbBundle.closeAll();
    }

    /**
     * Clicked export navelian.
     *
     * @param event the event
     */
    @FXML
    void clickedExportNavelian(ActionEvent event) {

        int i;

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initListe(lDbBundle);

        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(lDbBundle, "praesenzExport");

        dateiExport.ausgabe(
                "WillenserklIdent;WillenserklArt;WillenserklBez;Eintrittskarte;Stimmkarte;AktionaerName;AktionaerVorname;AktionaerOrt;VertreterName;VertreterVorname;VertreterOrt");
        dateiExport.newline();
        int rc = lDbBundle.dbJoined.read_PraesenzExport();

        if (rc > 0) {
            for (i = 0; i < rc; i++) {

                EclPraesenzliste lEclPraesenzliste = new EclPraesenzliste();
                lEclPraesenzliste = lDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);

                dateiExport.ausgabe(Integer.toString(lEclPraesenzliste.willenserklaerungIdent));
                dateiExport.ausgabe(Integer.toString(lEclPraesenzliste.willenserklaerung));
                dateiExport.ausgabe(KonstWillenserklaerung.getText(lEclPraesenzliste.willenserklaerung));
                dateiExport.ausgabe(lEclPraesenzliste.zutrittsIdent);
                dateiExport.ausgabe(lEclPraesenzliste.stimmkarte);
                dateiExport.ausgabe(lEclPraesenzliste.aktionaerName);
                dateiExport.ausgabe(lEclPraesenzliste.aktionaerVorname);
                dateiExport.ausgabe(lEclPraesenzliste.aktionaerOrt);
                if (lEclPraesenzliste.vertreterName != null) {
                    dateiExport.ausgabe(lEclPraesenzliste.vertreterName);
                    dateiExport.ausgabe(lEclPraesenzliste.vertreterVorname);
                    dateiExport.ausgabe(lEclPraesenzliste.vertreterOrt);
                } else {
                    dateiExport.ausgabe("");
                    dateiExport.ausgabe("");
                    dateiExport.ausgabe("");

                }

                dateiExport.newline();

            }
        }
        dateiExport.schliessen();

        lDbBundle.closeAll();
        return;

    }

    /**
     * Clicked btn protokoll drucken.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnProtokollDrucken(ActionEvent event) {

        int i;
        int arbeitsplpatzNr = 0;
        int protokollNr = 0;
        String lfdNummer = "01";

        String hString = tfArbeitsplatzNr.getText();
        if (!CaString.isNummern(hString)) {
            fehlerMeldung("Bitte gültige Arbeitsplatznummer eingeben!");
            return;
        }
        hString = tfProtokollNr.getText();
        if (!CaString.isNummern(hString)) {
            fehlerMeldung("Bitte gültige Protokollnummer eingeben!");
            return;
        }

        lfdNummer = tfFormularNummer.getText();

        arbeitsplpatzNr = Integer.parseInt(tfArbeitsplatzNr.getText());
        protokollNr = Integer.parseInt(tfProtokollNr.getText());

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.initListe(lDbBundle);

        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.praesenzprotokoll(lfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Protokoll.Arbeitsplatz", Integer.toString(arbeitsplpatzNr));
        rpVariablen.fuelleVariable(rpDrucken, "Protokoll.Nummer", Integer.toString(protokollNr));
        rpDrucken.startListe();

        int rc = lDbBundle.dbJoined.read_Praesenzprotokoll(arbeitsplpatzNr, protokollNr);
        System.out.println("Protokoll=" + arbeitsplpatzNr + " " + protokollNr);
        System.out.println("rc=" + rc);
        if (rc > 0) {
            for (i = 0; i < rc; i++) {

                EclPraesenzliste lEclPraesenzliste = new EclPraesenzliste();
                lEclPraesenzliste = lDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);

                rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.Ident",
                        Integer.toString(lEclPraesenzliste.willenserklaerungIdent));
                rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.Nr",
                        Integer.toString(lEclPraesenzliste.willenserklaerung));
                rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.Bezeichnung",
                        KonstWillenserklaerung.getText(lEclPraesenzliste.willenserklaerung));

                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.MeldeIdent",
                        Integer.toString(lEclPraesenzliste.meldeIdentAktionaer));
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Meldungstyp",
                        Integer.toString(lEclPraesenzliste.meldungstyp));
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lEclPraesenzliste.aktionaerName);
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lEclPraesenzliste.aktionaerVorname);
                rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lEclPraesenzliste.aktionaerOrt);

                rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Ident",
                        Integer.toString(lEclPraesenzliste.vertreterIdent));
                rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Name", lEclPraesenzliste.vertreterName);
                rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Vorname", lEclPraesenzliste.vertreterVorname);
                rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Ort", lEclPraesenzliste.vertreterOrt);

                rpVariablen.fuelleFeld(rpDrucken, "Nummern.Eintrittskarte", lEclPraesenzliste.zutrittsIdent);
                rpVariablen.fuelleFeld(rpDrucken, "Nummern.Stimmkarte", lEclPraesenzliste.stimmkarte);

                rpVariablen.fuelleFeld(rpDrucken, "Besitz.Stimmen", Long.toString(lEclPraesenzliste.stimmen));
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenDE", Long.toString(lEclPraesenzliste.stimmen));
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.Aktien", Long.toString(lEclPraesenzliste.aktien));
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.AktienDE", Long.toString(lEclPraesenzliste.aktien));

                rpVariablen.fuelleFeld(rpDrucken, "Besitz.BesitzartKuerzel", lEclPraesenzliste.besitzartKuerzel);
                rpVariablen.fuelleFeld(rpDrucken, "Besitz.Gattung", Integer.toString(lEclPraesenzliste.gattung));

                rpVariablen.fuelleFeld(rpDrucken, "Sammelkarte.Ident",
                        Integer.toString(lEclPraesenzliste.sammelkartenIdent));
                rpVariablen.fuelleFeld(rpDrucken, "Sammelkarte.Name", lEclPraesenzliste.sammelkartenName);
                rpVariablen.fuelleFeld(rpDrucken, "Sammelkarte.Vorname", lEclPraesenzliste.sammelkartenVorname);
                rpVariablen.fuelleFeld(rpDrucken, "Sammelkarte.Ort", lEclPraesenzliste.sammelkartenOrt);

                rpVariablen.fuelleFeld(rpDrucken, "Sammelkarte.Offenlegung",
                        Integer.toString(lEclPraesenzliste.skOffenlegung));

                rpVariablen.fuelleFeld(rpDrucken, "Sortierung.Name", lEclPraesenzliste.sortierName);

                if (lEclPraesenzliste.meldungstyp != 3) {
                    rpDrucken.druckenListe();
                }

            }
        }
        rpDrucken.endeListe();

        lDbBundle.closeAll();
        return;

    }

    /**
     * Clicked protokoll abschliessen.
     *
     * @param event the event
     */
    @FXML
    void clickedProtokollAbschliessen(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        BlPraesenzProtokoll blPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);

        String auswahl = tfProtokollSchliessen.getText();
        if (auswahl != null && !auswahl.isEmpty() && CaString.isNummern(auswahl)) {
            int arbeitsplatzNr = Integer.parseInt(auswahl);
            blPraesenzProtokoll.neuesProtokoll(arbeitsplatzNr);

        } else { /*Alle Arbeitsplätze*/
            blPraesenzProtokoll.neuesProtokollAlle();
        }

        lDbBundle.closeAll();

        refreshInhalt();

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
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
