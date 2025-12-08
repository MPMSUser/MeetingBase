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
package de.meetingapps.meetingclient.meetingKontrolle;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Class CtrlAktionaersStatus.
 */
public class CtrlAktionaersStatus {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The btn anzeige loeschen. */
    @FXML
    private Button btnAnzeigeLoeschen;

    /** The tf stimmkarte. */
    @FXML
    private TextField tfStimmkarte;

    /** The tf eintrittskarte. */
    @FXML
    private TextField tfEintrittskarte;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The tf praesenzstatus. */
    @FXML
    private TextField tfPraesenzstatus;

    /** The tf ist sammelkarte. */
    @FXML
    private TextField tfIstSammelkarte;

    /** The tf in sammelkarte ident. */
    @FXML
    private TextField tfInSammelkarteIdent;

    /** The tf in sammelkarte bezeichnung. */
    @FXML
    private TextField tfInSammelkarteBezeichnung;

    /** The tf in sammelkarte eintrittskarte. */
    @FXML
    private TextField tfInSammelkarteEintrittskarte;

    /** The tf vertreter name. */
    @FXML
    private TextField tfVertreterName;

    /** The tf vertreter vorname. */
    @FXML
    private TextField tfVertreterVorname;

    /** The tf vertreter ort. */
    @FXML
    private TextField tfVertreterOrt;

    /** The tf fehler. */
    @FXML
    private TextArea tfFehler;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert btnAnzeigeLoeschen != null : "fx:id=\"btnAnzeigeLoeschen\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfStimmkarte != null : "fx:id=\"tfStimmkarte\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfEintrittskarte != null : "fx:id=\"tfEintrittskarte\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfVorname != null : "fx:id=\"tfVorname\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfPraesenzstatus != null : "fx:id=\"tfPraesenzstatus\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfIstSammelkarte != null : "fx:id=\"tfIstSammelkarte\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfInSammelkarteIdent != null : "fx:id=\"tfInSammelkarteIdent\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfInSammelkarteBezeichnung != null : "fx:id=\"tfInSammelkarteBezeichnung\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfInSammelkarteEintrittskarte != null : "fx:id=\"tfInSammelkarteEintrittskarte\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfVertreterName != null : "fx:id=\"tfVertreterName\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfVertreterVorname != null : "fx:id=\"tfVertreterVorname\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfVertreterOrt != null : "fx:id=\"tfVertreterOrt\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";
        assert tfFehler != null : "fx:id=\"tfFehler\" was not injected: check your FXML file 'AktionaersStatus.fxml'.";

        tfStimmkarte.requestFocus();

        lDbBundle = new DbBundle();

    }

    /** The l db bundle. */
    DbBundle lDbBundle = new DbBundle();

    /** The melde ident. */
    int meldeIdent = 0;

    /**
     * On key eintrittskarte.
     *
     * @param event the event
     */
    @FXML
    void onKeyEintrittskarte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doBtnEinlesenEK();
        }

    }

    /**
     * On key stimmkarte.
     *
     * @param event the event
     */
    @FXML
    void onKeyStimmkarte(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doBtnEinlesenSK();
        }

    }

    /**
     * Do btn einlesen EK.
     */
    private void doBtnEinlesenEK() {
        lDbBundle.openAll();

        String zutrittsIdent = tfEintrittskarte.getText().trim();

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int erg = blNummernformen.dekodiere(zutrittsIdent, KonstKartenklasse.eintrittskartennummer);
        if (erg < 0) {
            tfFehler.setText("Ungültiges Format");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }

        EclZutrittsIdent qfZutrittsIdent = new EclZutrittsIdent();
        qfZutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
        qfZutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);

        lDbBundle.dbZutrittskarten.read(qfZutrittsIdent, 1);
        if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
            tfFehler.setText("Eintrittskarte nicht vorhanden!");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        EclZutrittskarten eclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
        if (eclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
            tfFehler.setText("Eintrittskarte gesperrt!");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        meldeIdent = eclZutrittskarten.meldungsIdentAktionaer;

        tfFehler.setText("");

        anzeigen();

        lDbBundle.closeAll();
    }

    /**
     * Do btn einlesen SK.
     */
    private void doBtnEinlesenSK() {
        lDbBundle.openAll();

        String stimmkarteIdent = tfStimmkarte.getText().trim();

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int erg = blNummernformen.dekodiere(stimmkarteIdent, KonstKartenklasse.stimmkartennummer);
        if (erg < 0) {
            tfFehler.setText("Ungültiges Format");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        stimmkarteIdent = blNummernformen.rcIdentifikationsnummer.get(0);

        lDbBundle.dbStimmkarten.read(stimmkarteIdent);
        if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
            tfFehler.setText("Stimmkarte nicht vorhanden!");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
        if (eclStimmkarte.stimmkarteIstGesperrt == 2) {
            tfFehler.setText("Stimmkarte gesperrt!");
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        meldeIdent = eclStimmkarte.meldungsIdentAktionaer;

        tfFehler.setText("");

        anzeigen();
        lDbBundle.closeAll();
    }

    /**
     * Anzeigen.
     */
    private void anzeigen() {
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = meldeIdent;
        int anz = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (anz < 1) {
            lDbBundle.closeAll();
            anzeigeLoeschen();
            return;
        }
        lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        tfStimmkarte.setText(lMeldung.stimmkarte);
        tfEintrittskarte.setText(lMeldung.zutrittsIdent);
        tfName.setText(lMeldung.name);
        tfVorname.setText(lMeldung.vorname);
        tfOrt.setText(lMeldung.ort);
        tfAktien.setText(CaString.toStringDE(lMeldung.stimmen));
        tfVertreterName.setText(lMeldung.vertreterName);
        tfVertreterVorname.setText(lMeldung.vertreterVorname);
        tfVertreterOrt.setText(lMeldung.vertreterOrt);

        switch (lMeldung.statusPraesenz) {
        case 0:
            tfPraesenzstatus.setText("noch nicht anwesend");
            break;
        case 1:
            tfPraesenzstatus.setText("anwesend");
            break;
        case 2:
            tfPraesenzstatus.setText("war anwesend");
            break;
        }

        if (lMeldung.meldungstyp == 2) {
            tfIstSammelkarte.setText("Ist eine Sammelkarte");
        }
        if (lMeldung.meldungstyp == 3) {
            tfIstSammelkarte.setText("Ist in einer Sammelkarte");
            int sammelIdent = lMeldung.meldungEnthaltenInSammelkarte;
            EclMeldung lSammelMeldung = new EclMeldung();
            lSammelMeldung.meldungsIdent = sammelIdent;
            tfInSammelkarteIdent.setText(Integer.toString(sammelIdent));

            anz = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lSammelMeldung);
            lSammelMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            tfInSammelkarteBezeichnung.setText(lSammelMeldung.name);
            tfInSammelkarteEintrittskarte.setText(lSammelMeldung.zutrittsIdent);

        }

        tfStimmkarte.setEditable(false);
        tfEintrittskarte.setEditable(false);

    }

    /**
     * On btn anzeige loeschen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAnzeigeLoeschen(ActionEvent event) {
        anzeigeLoeschen();
    }

    /**
     * Anzeige loeschen.
     */
    private void anzeigeLoeschen() {
        tfStimmkarte.setText("");
        tfEintrittskarte.setText("");
        tfName.setText("");
        tfVorname.setText("");
        tfOrt.setText("");
        tfVertreterName.setText("");
        tfVertreterVorname.setText("");
        tfVertreterOrt.setText("");
        tfAktien.setText("");
        tfPraesenzstatus.setText("");
        tfIstSammelkarte.setText("");
        tfInSammelkarteIdent.setText("");
        tfInSammelkarteBezeichnung.setText("");
        tfInSammelkarteEintrittskarte.setText("");

        tfStimmkarte.setEditable(true);
        tfEintrittskarte.setEditable(true);

        tfStimmkarte.requestFocus();

    }

    /**
     * On btn schliessen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSchliessen(ActionEvent event) {
        eigeneStage.hide();
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
