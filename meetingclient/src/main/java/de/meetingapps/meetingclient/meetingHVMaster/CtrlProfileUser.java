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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComDb.DbBerechtigungenTexte;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBerechtigungenTexte;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlProfileUser.
 */
public class CtrlProfileUser extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The cb kennung HV spezifisch. */
    @FXML
    private CheckBox cbKennungHVSpezifisch;

    /** The tf kennung. */
    @FXML
    private TextField tfKennung;

    /** The btn bearbeiten. */
    @FXML
    private Button btnBearbeiten;

    /** The btn neue kennung anlegen. */
    @FXML
    private Button btnNeueKennungAnlegen;

    /** The btn suchen. */
    @FXML
    private Button btnSuchen;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf mail adresse. */
    @FXML
    private TextField tfMailAdresse;

    /** The scrpn berechtigungen. */
    @FXML
    private ScrollPane scrpnBerechtigungen;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /**
     * Pflegemodus für Bearbeitung 0 = nichts in Bearbeitung 1 = Neuaufnahme 2 =
     * Ändern 3 = Löschen.
     */
    private int pflegeModus = 0;

    /** User, die gerade in Bearbeitung (Pflege) ist. */
    EclUserLogin eclUserInBearbeitung = null;

    /** Liste der Berechtigungstexte. */
    private List<EclBerechtigungenTexte> listEclBerechtigungenTexte = new LinkedList<EclBerechtigungenTexte>();

    /** The cb berechtigungen. */
    private CheckBox[] cbBerechtigungen = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        
        assert cbKennungHVSpezifisch != null : "fx:id=\"cbKennungHVSpezifisch\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert tfKennung != null : "fx:id=\"tfKennung\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnBearbeiten != null : "fx:id=\"btnBearbeiten\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnNeueKennungAnlegen != null : "fx:id=\"btnNeueKennungAnlegen\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnSuchen != null : "fx:id=\"btnSuchen\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert tfMailAdresse != null : "fx:id=\"tfMailAdresse\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert scrpnBerechtigungen != null : "fx:id=\"scrpnBerechtigungen\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'LoginUser.fxml'.";

        /***Ab hier individuell**/

        lDbBundle = new DbBundle();

        lDbBundle.openAll();

        /*Berechtigungstexte einlesen*/
        DbBerechtigungenTexte lDbBerechtigungenTexte = new DbBerechtigungenTexte(lDbBundle);
        lDbBerechtigungenTexte.read_all();
        int anzBerechtigungen = lDbBerechtigungenTexte.anzErgebnis();
        if (anzBerechtigungen > 0) {
            cbBerechtigungen = new CheckBox[anzBerechtigungen];
            for (int i = 0; i < anzBerechtigungen; i++) {
                listEclBerechtigungenTexte.add(lDbBerechtigungenTexte.ergebnisPosition(i));
                cbBerechtigungen[i] = new CheckBox();
            }
        }

        lDbBundle.closeAll();

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                //                		CaZeigeHinweis zeigeHinweis=new CaZeigeHinweis();
                //                		zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        setzePflegeModusAuf0();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfKennung.requestFocus();
            }
        });

    }

    /************************Logik***************************************************/
    private boolean pruefeKennungFormaleEingabe() {
        String hEingabe = tfKennung.getText();
        if (hEingabe.isEmpty() || hEingabe.length() > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Bitte geben Sie ein gültiges Profil-Kürzel ein (mindestens 1, maximal 40 Zeichen!");
            tfKennung.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Einlesen anzeigen kennung zur bearbeitung aus feld.
     */
    private void einlesenAnzeigenKennungZurBearbeitungAusFeld() {
        String hEingabe = tfKennung.getText();
        if (pruefeKennungFormaleEingabe() == false) {
            return;
        }
        einlesenAnzeigenKennungZurBearbeitung(hEingabe);
    }

    /**
     * Überträgt die in eclUserInBearbeitung enthaltenen Felder in die
     * Bildschirmfelder.
     */
    private void anzeigenUserZurBearbeitung() {

        tfName.setText(eclUserInBearbeitung.name);
        tfMailAdresse.setText(eclUserInBearbeitung.email);

        /*Nun Berechtigungen anzeigen*/
        int anzBerechtigungen = listEclBerechtigungenTexte.size();
        boolean mitMandant = false;
        if (cbKennungHVSpezifisch.isSelected()) {
            mitMandant = true;
        }

        scrpnBerechtigungen.setContent(null);
        if (anzBerechtigungen > 0) {
            GridPane grpnBerechtigungen = new GridPane();
            grpnBerechtigungen.setVgap(5);
            grpnBerechtigungen.setHgap(5);

            int offsetInGrid = 0;
            for (int i = 0; i < anzBerechtigungen; i++) {
                EclBerechtigungenTexte lBerechtigung = listEclBerechtigungenTexte.get(i);
                if ((lBerechtigung.mandantenAbhaengig == 1 && mitMandant) || mitMandant == false) {
                    int hauptoffset = lBerechtigung.hauptOffset;
                    if (hauptoffset != -1) {
                        if (eclUserInBearbeitung.berechtigungen[lBerechtigung.hauptOffset][lBerechtigung.nebenOffset] == 1) {
                            cbBerechtigungen[i].setSelected(true);
                        } else {
                            cbBerechtigungen[i].setSelected(false);
                        }
                        grpnBerechtigungen.add(cbBerechtigungen[i], 0, offsetInGrid);
                        Label lText = new Label();
                        lText.setText(lBerechtigung.beschreibung);
                        grpnBerechtigungen.add(lText, 1, offsetInGrid);
                    } else {
                        Label lText = new Label();
                        lText.setText(lBerechtigung.beschreibung);
                        grpnBerechtigungen.add(lText, 0, offsetInGrid, 2, 1);
                    }
                    offsetInGrid++;
                } else {
                    cbBerechtigungen[i].setSelected(false);
                }
            }
            scrpnBerechtigungen.setContent(grpnBerechtigungen);
        }

    }

    /**
     * Einlesen anzeigen kennung zur bearbeitung.
     *
     * @param pKennung the kennung
     */
    private void einlesenAnzeigenKennungZurBearbeitung(String pKennung) {
        boolean mitMandant = false;
        if (cbKennungHVSpezifisch.isSelected()) {
            mitMandant = true;
        }

        lDbBundle.openAll();
        lDbBundle.dbUserLogin.profileVerarbeiten = true;
        lDbBundle.dbUserLogin.leseZuKennung(pKennung, mitMandant);
        lDbBundle.dbUserLogin.profileVerarbeiten = false;
        lDbBundle.closeAll();

        if (lDbBundle.dbUserLogin.anzUserLoginGefunden() == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Profil nicht vorhanden!");
            tfKennung.requestFocus();
            return;
        }
        eclUserInBearbeitung = lDbBundle.dbUserLogin.userLoginGefunden(0);

        setzeFelderAufBearbeiten();
        anzeigenUserZurBearbeitung();

        pflegeModus = 2;
    }

    /**
     * Kennung in bearbeitung speichern.
     */
    private void kennungInBearbeitungSpeichern() {
        int hLen = 0;
        boolean mitMandant = false;
        if (cbKennungHVSpezifisch.isSelected()) {
            mitMandant = true;
        }

        hLen = tfName.getText().length();
        if (hLen > 80) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Profil-Name: maximal 80 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfName.requestFocus();
            return;
        }
        hLen = tfMailAdresse.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Profil-Beschreibung: maximal 40 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfMailAdresse.requestFocus();
            return;
        }

        /*Hier: Überprüfung der Eingaben wurde durchgeführt. Nun speichern*/

        eclUserInBearbeitung.kennung = tfKennung.getText();
        eclUserInBearbeitung.name = tfName.getText();
        eclUserInBearbeitung.email = tfMailAdresse.getText();

        int anzBerechtigungen = listEclBerechtigungenTexte.size();
        if (anzBerechtigungen > 0) {
            for (int i = 0; i < anzBerechtigungen; i++) {
                EclBerechtigungenTexte lBerechtigung = listEclBerechtigungenTexte.get(i);
                int hauptoffset = lBerechtigung.hauptOffset;
                if (hauptoffset != -1) {
                    if (cbBerechtigungen[i].isSelected()) {
                        eclUserInBearbeitung.berechtigungen[lBerechtigung.hauptOffset][lBerechtigung.nebenOffset] = 1;
                    } else {
                        eclUserInBearbeitung.berechtigungen[lBerechtigung.hauptOffset][lBerechtigung.nebenOffset] = 0;
                    }
                }
            }
        }

        if (pflegeModus == 1) { /*Neuaufnahme*/
            lDbBundle.openAll();
            lDbBundle.dbUserLogin.profileVerarbeiten = true;
            int rc = lDbBundle.dbUserLogin.insert(eclUserInBearbeitung, mitMandant);
            lDbBundle.dbUserLogin.profileVerarbeiten = false;
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Profil kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        } else {/*Ändern*/
            lDbBundle.openAll();
            lDbBundle.dbUserLogin.profileVerarbeiten = true;
            int rc = lDbBundle.dbUserLogin.update(eclUserInBearbeitung);
            lDbBundle.dbUserLogin.profileVerarbeiten = false;
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Profil kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        }

        setzePflegeModusAuf0();
    }

    /**
     * Neue kennung vorbereiten.
     */
    private void neueKennungVorbereiten() {

        String hEingabe = tfKennung.getText();
        if (pruefeKennungFormaleEingabe() == false) {
            return;
        }

        lDbBundle.openAll();
        /*Kennungs-String muß immer eindeutig sein - auch über Mandanten hinweg*/
        lDbBundle.dbUserLogin.profileVerarbeiten = true;
        lDbBundle.dbUserLogin.pruefeKennungVorhanden(hEingabe);
        lDbBundle.dbUserLogin.profileVerarbeiten = false;
        lDbBundle.closeAll();
        if (lDbBundle.dbUserLogin.anzUserLoginGefunden() > 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Profil bereits vorhanden!");
            tfKennung.requestFocus();
            return;
        }

        eclUserInBearbeitung = new EclUserLogin();

        setzeFelderAufBearbeiten();
        pflegeModus = 1;
        anzeigenUserZurBearbeitung();

    }

    /**************************Anzeigefunktionen***************************************/
    
    private void setzeFelderAufBearbeiten() {

        cbKennungHVSpezifisch.setDisable(true);

        tfKennung.setEditable(false);
        btnBearbeiten.setVisible(false);
        btnNeueKennungAnlegen.setVisible(false);
        btnSuchen.setVisible(false);

        btnSpeichern.setVisible(true);
        btnAbbrechen.setVisible(true);

        tfName.setEditable(true);
        tfMailAdresse.setEditable(true);

        scrpnBerechtigungen.setContent(null);

        tfName.requestFocus();

    }

    /**
     * Setze pflege modus auf 0.
     */
    private void setzePflegeModusAuf0() {

        cbKennungHVSpezifisch.setDisable(false);

        tfKennung.setEditable(true);
        btnBearbeiten.setVisible(true);
        btnNeueKennungAnlegen.setVisible(true);

        btnSuchen.setVisible(true);

        btnSpeichern.setVisible(false);
        btnAbbrechen.setVisible(false);

        /*Nun Detailfelder auf "leer" setzen*/
        tfKennung.setText("");
        tfName.setText("");
        tfMailAdresse.setText("");

        /*Nun Detailfelder auf "nicht bearbeitbar" setzen*/
        tfName.setEditable(false);
        tfMailAdresse.setEditable(false);

        scrpnBerechtigungen.setContent(null);

        pflegeModus = 0;
        tfKennung.requestFocus();
    }

    /********************Aktionen auf Oberfläche*************************/

    @FXML
    void onCbKennungHVSpezifisch(ActionEvent event) {
        if (cbKennungHVSpezifisch.isSelected()) {
        } else {
        }
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        kennungInBearbeitungSpeichern();
    }

    /**
     * Clicked abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        setzePflegeModusAuf0();
    }

    /**
     * On key pressed kennung.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedKennung(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && pflegeModus == 0) {
            einlesenAnzeigenKennungZurBearbeitungAusFeld();
        }

    }

    /**
     * Clicked bearbeiten.
     *
     * @param event the event
     */
    @FXML
    void clickedBearbeiten(ActionEvent event) {
        einlesenAnzeigenKennungZurBearbeitungAusFeld();
    }

    /**
     * Clicked neue kennung anlegen.
     *
     * @param event the event
     */
    @FXML
    void clickedNeueKennungAnlegen(ActionEvent event) {
        neueKennungVorbereiten();
    }

    /**
     * Clicked suchen.
     *
     * @param event the event
     */
    @FXML
    void clickedSuchen(ActionEvent event) {
        boolean mitMandant = false;

        lDbBundle.openAll();
        lDbBundle.dbUserLogin.profileVerarbeiten = true;
        if (cbKennungHVSpezifisch.isSelected()) {
            mitMandant = true;
        }
        lDbBundle.dbUserLogin.lese_all(mitMandant);
        int anzUser = lDbBundle.dbUserLogin.anzUserLoginGefunden();

        scrpnBerechtigungen.setContent(null);
        if (anzUser > 0) {
            GridPane grpnUser = new GridPane();
            grpnUser.setVgap(5);
            grpnUser.setHgap(5);

            for (int i = 0; i < anzUser; i++) {
                EclUserLogin lUserLogin = lDbBundle.dbUserLogin.userLoginGefunden(i);
                Label lIdent = new Label();
                lIdent.setText(Integer.toString(lUserLogin.userLoginIdent));
                grpnUser.add(lIdent, 0, i);
                Label lKennung = new Label();
                lKennung.setText(lUserLogin.kennung);
                grpnUser.add(lKennung, 1, i);
                Label lName = new Label();
                lName.setText(lUserLogin.name);
                grpnUser.add(lName, 2, i);
            }
            scrpnBerechtigungen.setContent(grpnUser);

        }

        lDbBundle.dbUserLogin.profileVerarbeiten = false;
        lDbBundle.closeAll();
    }

    /*************Initialisierung*******************/
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
