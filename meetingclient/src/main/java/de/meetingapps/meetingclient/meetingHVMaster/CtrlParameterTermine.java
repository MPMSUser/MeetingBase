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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MaxLengthTextField;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvDatenbank;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterTermine.
 */
public class CtrlParameterTermine extends CtrlRoot {

    /** The sp anzeige. */
    @FXML
    private ScrollPane spAnzeige;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The tf ident. */
    @FXML
    private TextField tfIdent;

    /** The btn loeschen. */
    @FXML
    private Button btnLoeschen;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn standard. */
    @FXML
    private Button btnStandard;

    /** The btn 2021 ergaenzen. */
    @FXML
    private Button btn2021Ergaenzen;

    /** The btn 2022 ergaenzen. */
    @FXML
    private Button btn2022Ergaenzen;

    /** **************Ab hier individuell***********************. */

    private MeetingGridPane grpnDaten = null;

    /** The lbl ident termin. */
    private Label[] lblIdentTermin = null;

    /** The cb technisch erfoderlich. */
    private CheckBox[] cbTechnischErfoderlich = null;

    /** The tf datum. */
    private MaxLengthTextField[] tfDatum = null;

    /** The tf zeit. */
    private MaxLengthTextField[] tfZeit = null;

    /** The tf datum zeit portal DE. */
    private MaxLengthTextField[] tfDatumZeitPortalDE = null;

    /** The tf datum zeit portal EN. */
    private MaxLengthTextField[] tfDatumZeitPortalEN = null;

    /** The tf beschreibung. */
    private MaxLengthTextField[] tfBeschreibung = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The termine array. */
    private EclTermine[] termineArray = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert spAnzeige != null : "fx:id=\"spAnzeige\" was not injected: check your FXML file 'ParameterTermine.fxml'.";
        assert btnNeu != null : "fx:id=\"btnNeu\" was not injected: check your FXML file 'ParameterTermine.fxml'.";
        assert tfIdent != null : "fx:id=\"tfIdent\" was not injected: check your FXML file 'ParameterTermine.fxml'.";
        assert btnLoeschen != null : "fx:id=\"btnLoeschen\" was not injected: check your FXML file 'ParameterTermine.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterTermine.fxml'.";
        assert btnStandard != null : "fx:id=\"btnStandard\" was not injected: check your FXML file 'ParameterTermine.fxml'.";

        /**************** Ab hier individuell ************************/
        lDbBundle = new DbBundle();
        leseTermine();
        zeigeAn();

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
                //                speichernParameter();
            }
        });

    }

    /************Verarbeiten Clicks*********************/
    
    @FXML
    void onBtnSpeichern(ActionEvent event) {
        uebertrageAnzeigeInArray();
        schreibeTermine();
        eigeneStage.hide();
    }

    /**
     * On btn standard.
     *
     * @param event the event
     */
    @FXML
    void onBtnStandard(ActionEvent event) {
        lFehlertext = "";
        this.pruefeZahlNichtLeerLaenge(tfIdent, "Ident", 1);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung("In Feld Ident Variante eingeben: 1=feste Datums, 2=Ende Rede, 3=Ende HV");
            return;
        }
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        Boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Achtung - alle bestehenden Termine werden gelöscht!",
                "Weiter", "Abbrechen");
        if (brc == false) {
            return;
        }

        lDbBundle.openAll();
        lDbBundle.dbTermine.deleteAll();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);
        bvDatenbank.initialisiereTermine(Integer.parseInt(tfIdent.getText()));
        lDbBundle.dbParameter.readHVParam_all();
        lDbBundle.param = lDbBundle.dbParameter.ergHVParam;

        /*Hinweis: Menü- und Kontaktanfrage-Themen werden beim Client nicht benötigt 
         * und deshalb hier nicht geladen.
         */

        ParamS.param = lDbBundle.param;
        lDbBundle.closeAll();
        leseTermine();
        zeigeAn();
    }

    /**
     * On btn 2021 ergaenzen.
     *
     * @param event the event
     */
    @FXML
    void onBtn2021Ergaenzen(ActionEvent event) {
        lDbBundle.openAll();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);
        bvDatenbank.initialisiereTermineErgaenze2021();
        lDbBundle.closeAll();
        leseTermine();
        zeigeAn();
    }

    /**
     * On btn 2022 ergaenzen.
     *
     * @param event the event
     */
    @FXML
    void onBtn2022Ergaenzen(ActionEvent event) {
        lDbBundle.openAll();
        BvDatenbank bvDatenbank = new BvDatenbank(lDbBundle);
        bvDatenbank.initialisiereTermineErgaenze2022();
        lDbBundle.closeAll();
        leseTermine();
        zeigeAn();
    }

    /**
     * On btn neu.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeu(ActionEvent event) {
        lFehlertext = "";
        this.pruefeZahlNichtLeerLaenge(tfIdent, "Ident", 4);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        lDbBundle.openAll();
        int hIdent = Integer.parseInt(tfIdent.getText());
        EclTermine lTermin = new EclTermine();
        lTermin.identTermin = hIdent;
        lTermin.mandant = lDbBundle.clGlobalVar.mandant;
        lDbBundle.dbTermine.insert(lTermin);
        lDbBundle.closeAll();
        leseTermine();
        zeigeAn();
    }

    /**
     * On btn loeschen.
     *
     * @param event the event
     */
    @FXML
    void onBtnLoeschen(ActionEvent event) {
        lFehlertext = "";
        this.pruefeZahlNichtLeerLaenge(tfIdent, "Ident", 4);
        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        lDbBundle.openAll();
        int hIdent = Integer.parseInt(tfIdent.getText());
        lDbBundle.dbTermine.delete(hIdent);
        lDbBundle.closeAll();
        leseTermine();
        zeigeAn();

    }

    /********Anzeigefunktionen***************/
    
    private void zeigeAn() {
        grpnDaten = new MeetingGridPane();
        if (termineArray != null) {
            int anz = termineArray.length;
            lblIdentTermin = new Label[anz];
            cbTechnischErfoderlich = new CheckBox[anz];
            tfDatum = new MaxLengthTextField[anz];
            tfZeit = new MaxLengthTextField[anz];
            tfDatumZeitPortalDE = new MaxLengthTextField[anz];
            tfDatumZeitPortalEN = new MaxLengthTextField[anz];
            tfBeschreibung = new MaxLengthTextField[anz];

            for (int i = 0; i < anz; i++) {
                int spalte = 0;
                EclTermine lTermin = termineArray[i];

                lblIdentTermin[i] = new Label();
                lblIdentTermin[i].setText(Integer.toString(lTermin.identTermin));
                grpnDaten.addMeeting(lblIdentTermin[i], spalte, i + 1);
                spalte++;

                cbTechnischErfoderlich[i] = new CheckBox();
                if (lTermin.technischErforderlicherTermin == 1) {
                    cbTechnischErfoderlich[i].setSelected(true);
                }
                grpnDaten.addMeeting(cbTechnischErfoderlich[i], spalte, i + 1);
                spalte++;

                tfDatum[i] = new MaxLengthTextField(10);
                tfDatum[i].setText(CaDatumZeit.datumJJJJMMTTzuNormal(lTermin.terminDatum));
                tfDatum[i].setMinWidth(30);
                grpnDaten.addMeeting(tfDatum[i], spalte, i + 1);
                spalte++;

                tfZeit[i] = new MaxLengthTextField(8);
                tfZeit[i].setText(lTermin.terminZeit);
                tfZeit[i].setMinWidth(13);
                grpnDaten.addMeeting(tfZeit[i], spalte, i + 1);
                spalte++;

                tfBeschreibung[i] = new MaxLengthTextField(2000);
                tfBeschreibung[i].setText(lTermin.beschreibung);
                tfBeschreibung[i].setMinWidth(700);
                if (lTermin.identTermin > 100 && lTermin.identTermin <= 120) {
                    tfBeschreibung[i].setText(lDbBundle.param.paramPortalServer.phasenNamen[lTermin.identTermin - 100]);
                    tfBeschreibung[i].setEditable(false);
                }
                grpnDaten.addMeeting(tfBeschreibung[i], spalte, i + 1);
                spalte++;

                tfDatumZeitPortalDE[i] = new MaxLengthTextField(100);
                tfDatumZeitPortalDE[i].setText(lTermin.textDatumZeitFuerPortalDE);
                tfDatumZeitPortalDE[i].setMinWidth(700);
                grpnDaten.addMeeting(tfDatumZeitPortalDE[i], spalte, i + 1);
                spalte++;

                tfDatumZeitPortalEN[i] = new MaxLengthTextField(100);
                tfDatumZeitPortalEN[i].setText(lTermin.textDatumZeitFuerPortalEN);
                tfDatumZeitPortalEN[i].setMinWidth(700);
                grpnDaten.addMeeting(tfDatumZeitPortalEN[i], spalte, i + 1);
                spalte++;

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("Technisch");
        ueberschriftList.add("TT.MM.JJJJ");
        ueberschriftList.add("HH:MM:SS");
        ueberschriftList.add("Beschreibung");
        ueberschriftList.add("Text Datum/Zeit für Portal DE");
        ueberschriftList.add("Text Datum/Zeit für Portal EN");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }

        grpnDaten.setzeUeberschrift(uberschriftString, spAnzeige);

    }

    /**
     * Uebertrage anzeige in array.
     */
    private void uebertrageAnzeigeInArray() {
        if (termineArray != null) {
            int anz = termineArray.length;
            for (int i = 0; i < anz; i++) {
                EclTermine lTermin = termineArray[i];

                if (cbTechnischErfoderlich[i].isSelected()) {
                    lTermin.technischErforderlicherTermin = 1;
                } else {
                    lTermin.technischErforderlicherTermin = 0;
                }

                lTermin.terminDatum = CaDatumZeit.datumNormalZuJJJJMMTT(tfDatum[i].getText());
                lTermin.terminZeit = tfZeit[i].getText();
                lTermin.beschreibung = tfBeschreibung[i].getText();
                lTermin.textDatumZeitFuerPortalDE = tfDatumZeitPortalDE[i].getText();
                lTermin.textDatumZeitFuerPortalEN = tfDatumZeitPortalEN[i].getText();
            }
        }
    }

    /****************Logik***************************/
    private void leseTermine() {
        lDbBundle.openAll();
        lDbBundle.dbTermine.readAll(0);
        termineArray = lDbBundle.dbTermine.ergebnisArray;
        lDbBundle.closeAll();
    }

    /**
     * Schreibe termine.
     */
    private void schreibeTermine() {
        lDbBundle.openAll();
        lDbBundle.dbTermine.deleteAll();
        if (termineArray != null) {
            for (int i = 0; i < termineArray.length; i++) {
                System.out.println("i=" + i + " ident=" + termineArray[i].identTermin);
                termineArray[i].mandant = lDbBundle.clGlobalVar.mandant;
                lDbBundle.dbTermine.insert(termineArray[i]);
            }
        }

        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameter(lDbBundle.clGlobalVar.mandant);

        lDbBundle.closeAll();
    }

    /**
     * Inits the.
     */
    public void init() {
    }

}
