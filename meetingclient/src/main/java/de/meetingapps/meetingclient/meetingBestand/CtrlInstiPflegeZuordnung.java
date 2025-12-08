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
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlInstiPflegeZuordnung.
 */
public class CtrlInstiPflegeZuordnung extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf zuordnung zu. */
    @FXML
    private TextField tfZuordnungZu;

    /** The btn durchfuehren. */
    @FXML
    private Button btnDurchfuehren;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The scpn benutzer. */
    @FXML
    private ScrollPane scpnBenutzer;

    /** The lbl aktien gesamt. */
    @FXML
    private Label lblAktienGesamt;

    /** The lbl aktien zuordnen. */
    @FXML
    private Label lblAktienZuordnen;

    /** The tf beschreibung. */
    @FXML
    private TextField tfBeschreibung;

    /** The tf aktien gesamt. */
    @FXML
    private TextField tfAktienGesamt;

    /** The tf aktien zuordnen. */
    @FXML
    private TextField tfAktienZuordnen;

    /** **********Individuell******************. */
    private CheckBox[] cbBoxFuerUser = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tfZuordnungZu != null
                : "fx:id=\"tfZuordnungZu\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert btnDurchfuehren != null
                : "fx:id=\"btnDurchfuehren\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert scpnBenutzer != null
                : "fx:id=\"scpnBenutzer\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert lblAktienGesamt != null
                : "fx:id=\"lblAktienGesamt\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert lblAktienZuordnen != null
                : "fx:id=\"lblAktienZuordnen\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert tfBeschreibung != null
                : "fx:id=\"tfBeschreibung\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert tfAktienGesamt != null
                : "fx:id=\"tfAktienGesamt\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";
        assert tfAktienZuordnen != null
                : "fx:id=\"tfAktienZuordnen\" was not injected: check your FXML file 'InstiPflegeZuordnung.fxml'.";

        if (teilBestand == false) {
            lblAktienGesamt.setVisible(false);
            tfAktienGesamt.setVisible(false);

            lblAktienZuordnen.setVisible(false);
            tfAktienZuordnen.setVisible(false);
        } else {
            tfAktienGesamt.setText(CaString.toStringDE(aktienGesamt));
        }

        tfZuordnungZu.setEditable(false);
        tfZuordnungZu.setText(aktuelleInsti.kurzBezeichnung);

        blInsti.leseInstiKennungen(aktuelleInsti);
        fuelleUser();

    }

    /**
     * ***************Reaktionen auf Oberfläche**************************.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        rcFortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * On btn durchfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnDurchfuehren(ActionEvent event) {
        rcAktienTeilbestand = -1;
        if (teilBestand == true) {
            lFehlertext = "";
            pruefeZahlNichtLeerLaenge(tfAktienZuordnen, "Aktien zuordnen", 12);
            pruefeLaenge(tfBeschreibung, "Beschreibung", 100);
            if (!lFehlertext.isEmpty()) {
                fehlerMeldung(lFehlertext);
                return;
            }
            rcAktienTeilbestand = Long.parseLong(tfAktienZuordnen.getText());
            blInsti.liefereZugeordneteAktienZahlFuerAktionaersnummer(aktuelleInsti.ident, aktienregisterIdent);
            if (blInsti.rcAktienzahl + rcAktienTeilbestand > aktienGesamt) {
                fehlerMeldung("Bereits " + CaString.toStringDE(blInsti.rcAktienzahl)
                        + " zugeordnet - nun zu viele Aktien zugeordnet!");
                return;
            }

        }

        rcBeschreibung = tfBeschreibung.getText();

        int anzUser = cbBoxFuerUser.length;
        rcUserLoginZugeordnet = new boolean[anzUser];
        for (int i = 0; i < anzUser; i++) {
            rcUserLoginZugeordnet[i] = cbBoxFuerUser[i].isSelected();
        }

        rcFortsetzen = true;
        eigeneStage.hide();
    }

    /**
     * *******Anzeigefunktionen**************.
     */
    void fuelleUser() {
        MeetingGridPane lKennungenGridPane = new MeetingGridPane(1, 5, true, false, true, false);

        if (blInsti.rcUserLoginZuInsti != null) {
            int anz = blInsti.rcUserLoginZuInsti.length;
            cbBoxFuerUser = new CheckBox[anz];

            for (int i = 0; i < anz; i++) {
                cbBoxFuerUser[i] = new CheckBox();
                lKennungenGridPane.addMeeting(cbBoxFuerUser[i], 0, i + 1);

                EclUserLogin lUserLogin = blInsti.rcUserLoginZuInsti[i];
                Label lIdent = new Label(Integer.toString(lUserLogin.userLoginIdent));
                lKennungenGridPane.addMeeting(lIdent, 1, i + 1);

                Label lKennung = new Label(lUserLogin.kennung);
                lKennungenGridPane.addMeeting(lKennung, 2, i + 1);

                Label lBenutzername = new Label(lUserLogin.name);
                lKennungenGridPane.addMeeting(lBenutzername, 3, i + 1);

            }
            String[] ueberschrift = { "", "Ident", "Kennung", "Name" };
            lKennungenGridPane.setzeUeberschrift(ueberschrift, scpnBenutzer);

        }

    }

    /**
     * *****************************Initialisierung*********************************************.
     */

    /*++++Rückgabe++++*/
    /**
     * true=> Zuordnung wird durchgeführt false=> Abbruch
     */
    public boolean rcFortsetzen = false;

    /** The rc aktien teilbestand. */
    public long rcAktienTeilbestand = 0;

    /** The rc user login zugeordnet. */
    public boolean[] rcUserLoginZugeordnet = null;

    /** The rc beschreibung. */
    public String rcBeschreibung = "";

    /** The teil bestand. */
    /*++++Input über Parameter++++*/
    private boolean teilBestand = false;

    /** false=Meldebestandszuordnung; true=Aktienregisterzuordnung. */
    //    private boolean aktienregisterZuordnung=false;

    private BlInsti blInsti = null;

    /** The aktuelle insti. */
    private EclInsti aktuelleInsti = null;

    /** Bei teilBestand==true: Daten für die Zuordnungsüberprüfung. */
    private int aktienregisterIdent = 0;

    /** The aktien gesamt. */
    private long aktienGesamt = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage             the eigene stage
     * @param pTeilBestand             the teil bestand
     * @param pAktienregisterZuordnung the aktienregister zuordnung
     * @param pBlInsti                 the bl insti
     * @param pAktuelleInsti           the aktuelle insti
     * @param pAktienregisterIdent     the aktienregister ident
     * @param pAktienGesamt            the aktien gesamt
     */
    public void init(Stage pEigeneStage, boolean pTeilBestand, boolean pAktienregisterZuordnung, BlInsti pBlInsti,
            EclInsti pAktuelleInsti, int pAktienregisterIdent, long pAktienGesamt) {
        eigeneStage = pEigeneStage;
        teilBestand = pTeilBestand;
        //		aktienregisterZuordnung=pAktienregisterZuordnung;
        blInsti = pBlInsti;
        aktuelleInsti = pAktuelleInsti;
        aktienregisterIdent = pAktienregisterIdent;
        aktienGesamt = pAktienGesamt;
    }

}
