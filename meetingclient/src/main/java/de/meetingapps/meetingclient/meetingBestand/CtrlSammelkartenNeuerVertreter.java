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
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclVertreter;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkartenNeuerVertreter.
 */
public class CtrlSammelkartenNeuerVertreter extends CtrlRoot {

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

    /** The scpn bisherige vertreter. */
    @FXML
    private ScrollPane scpnBisherigeVertreter;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The btn bestehenden vertreter zuordnen. */
    private Button[] btnBestehendenVertreterZuordnen = null;

    /** The db bundle. */
    DbBundle dbBundle = null;

    /** The vertreter liste. */
    private List<EclVertreter> vertreterListe = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnZuordnen != null
                : "fx:id=\"btnZuordnen\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";
        assert scpnBisherigeVertreter != null
                : "fx:id=\"scpnBisherigeVertreter\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";
        assert tfName != null
                : "fx:id=\"tfName\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";
        assert tfVorname != null
                : "fx:id=\"tfVorname\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";
        assert tfOrt != null
                : "fx:id=\"tfOrt\" was not injected: check your FXML file 'SammelkartenNeuerVertreter.fxml'.";

        /*************** Ab hier individuell **********************************/
        dbBundle = new DbBundle();
        leseSammelkartenVertreter();
        zeigeSammelkartenVertreter();
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
        this.pruefeNichtLeerUndLaenge(tfName, "Vertreter Name", 80);
        this.pruefeNichtLeerUndLaenge(tfVorname, "Vertreter Vorname", 80);
        this.pruefeNichtLeerUndLaenge(tfOrt, "Vertreter Ort", 80);
        if (!this.lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return;
        }
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.neuerVertreterFuerSammelkarte(aktuelleSammelMeldung.meldungsIdent, tfName.getText(),
                tfVorname.getText(), tfOrt.getText());
        if (rc < 1) {
            fehlerMeldung(CaFehler.getFehlertext(rc, 0));
        }
        eigeneStage.hide();
    }

    /**
     * Clicked vertreter verwenden.
     *
     * @param event the event
     */
    @FXML
    void clickedVertreterVerwenden(ActionEvent event) {
        int buttonNr = this.findeButton(btnBestehendenVertreterZuordnen, event);
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.neuerBestehenderVertreterFuerSammelkarte(aktuelleSammelMeldung.meldungsIdent,
                vertreterListe.get(buttonNr).vertreter.ident);
        if (rc < 1) {
            fehlerMeldung(CaFehler.getFehlertext(rc, 0));
        }
        eigeneStage.hide();
    }

    /**
     * ******************Oberflächen******************************************.
     */
    private void zeigeSammelkartenVertreter() {
        if (vertreterListe == null) {
            return;
        }
        int anzVertreter = vertreterListe.size();
        if (anzVertreter == 0) {
            return;
        }

        MeetingGridPane lVertreterGridPane = new MeetingGridPane();
        btnBestehendenVertreterZuordnen = new Button[anzVertreter];
        int zeile = 0;
        for (int i = 0; i < anzVertreter; i++) {
            btnBestehendenVertreterZuordnen[i] = new Button("Verwenden");
            btnBestehendenVertreterZuordnen[i].setOnAction(e -> {
                clickedVertreterVerwenden(e);
            });

            if (!vertreterIdentSchonZugeordnet(vertreterListe.get(i).vertreter.ident)) {
                lVertreterGridPane.addMeeting(btnBestehendenVertreterZuordnen[i], 0, zeile);

                Label vertreterName = new Label();
                vertreterName.setText(vertreterListe.get(i).vertreter.name);
                lVertreterGridPane.addMeeting(vertreterName, 1, zeile);

                Label vertreterVorname = new Label();
                vertreterVorname.setText(vertreterListe.get(i).vertreter.vorname);
                lVertreterGridPane.addMeeting(vertreterVorname, 2, zeile);

                Label vertreterOrt = new Label();
                vertreterOrt.setText(vertreterListe.get(i).vertreter.ort);
                lVertreterGridPane.addMeeting(vertreterOrt, 3, zeile);

                Label inSammelkarten = new Label();
                inSammelkarten.setText(vertreterListe.get(i).bemerkung);
                lVertreterGridPane.addMeeting(inSammelkarten, 4, zeile);
                zeile++;
            }
        }

        scpnBisherigeVertreter.setContent(lVertreterGridPane);

    }

    /**
     * *********************************Logiken********************************.
     */
    private void leseSammelkartenVertreter() {
        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        blSammelkarten.leseVertreterAllerSammelkarten();
        vertreterListe = blSammelkarten.rcVertreterListe;
    }

    /**
     * Vertreter ident schon zugeordnet.
     *
     * @param pIdent the ident
     * @return true, if successful
     */
    private boolean vertreterIdentSchonZugeordnet(int pIdent) {
        if (willensErklVollmachtenAnDritte != null) {
            for (int i = 0; i < willensErklVollmachtenAnDritte.length; i++) {
                if (!willensErklVollmachtenAnDritte[i].wurdeStorniert) {
                    if (willensErklVollmachtenAnDritte[i].bevollmaechtigtePerson.ident == pIdent) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** The aktuelle sammel meldung. */
    private EclMeldung aktuelleSammelMeldung = null;

    /** The willens erkl vollmachten an dritte. */
    private EclWillensErklVollmachtenAnDritte[] willensErklVollmachtenAnDritte = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage                    the eigene stage
     * @param pSammelMeldung                  the sammel meldung
     * @param pWillensErklVollmachtenAnDritte the willens erkl vollmachten an dritte
     */
    public void init(Stage pEigeneStage, EclMeldung pSammelMeldung,
            EclWillensErklVollmachtenAnDritte[] pWillensErklVollmachtenAnDritte) {
        eigeneStage = pEigeneStage;
        aktuelleSammelMeldung = pSammelMeldung;
        willensErklVollmachtenAnDritte = pWillensErklVollmachtenAnDritte;
    }

}
