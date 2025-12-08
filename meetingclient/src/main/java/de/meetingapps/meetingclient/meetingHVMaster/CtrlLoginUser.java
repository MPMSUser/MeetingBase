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
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComDb.DbBerechtigungenTexte;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBerechtigungenTexte;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComEntities.EclUserProfile;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlLoginUser.
 */
public class CtrlLoginUser extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The cb kennung HV spezifisch. */
    @FXML
    private CheckBox cbKennungHVSpezifisch;

    @FXML
    private Label lblAufDreiServern;
    
    @FXML
    private CheckBox cbAufDreiServern;
    
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

    /** The cb kennung durch software gesperrt. */
    @FXML
    private CheckBox cbKennungDurchSoftwareGesperrt;

    /** The cb kennung manuell gesperrt. */
    @FXML
    private CheckBox cbKennungManuellGesperrt;

    /** The cb trivial passwort zulaessig. */
    @FXML
    private CheckBox cbTrivialPasswortZulaessig;

    /** The cb ist gruppen kennung HV. */
    @FXML
    private CheckBox cbIstGruppenKennungHV;

    /** The tf neues passwort. */
    @FXML
    private TextField tfNeuesPasswort;

    /** The scrpn berechtigungen. */
    @FXML
    private ScrollPane scrpnBerechtigungen;

    /** The scrpn profile. */
    @FXML
    private ScrollPane scrpnProfile;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The cb kennung institutionelle. */
    @FXML
    private CheckBox cbKennungInstitutionelle;

    /** The lbl gehoert zu insti. */
    @FXML
    private Label lblGehoertZuInsti;

    /** The cb BO mitarbeiter. */
    @FXML
    private CheckBox cbBOMitarbeiter;

    /** The lbl BO mitarbeiter. */
    @FXML
    private Label lblBOMitarbeiter;

    /** The cb undefiniert. */
    @FXML
    private RadioButton cbUndefiniert;

    /** The tg gehoert zu. */
    @FXML
    private ToggleGroup tgGehoertZu;

    /** The cb emittent. */
    @FXML
    private RadioButton cbEmittent;

    /** The cb dritter. */
    @FXML
    private RadioButton cbDritter;

    /** The cb gehoert zu insti. */
    @FXML
    private ComboBox<CbElement> cbGehoertZuInsti;

    /** The cba gehoert zu insti. */
    private CbAllgemein cbaGehoertZuInsti = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /**Pflegemodus für Bearbeitung
     * 0 = nichts in Bearbeitung
     * 1 = Neuaufnahme
     * 2 = Ändern
     * 3 = Löschen
     */
    private int pflegeModus = 0;

    /** User, die gerade in Bearbeitung (Pflege) ist. */
    EclUserLogin eclUserInBearbeitung = null;

    /** Liste der Berechtigungstexte. */
    private List<EclBerechtigungenTexte> listEclBerechtigungenTexte = new LinkedList<EclBerechtigungenTexte>();

    /**
     * Liste der Profile; Feld ausgewaehlt kennzeichnet, ob aktuell zugeordnet oder
     * nicht.
     */
    private EclUserLogin[] listProfile = null;

    /** The cb berechtigungen. */
    private CheckBox[] cbBerechtigungen = null;

    /** The cb profile. */
    private CheckBox[] cbProfile = null;

    /** Liste der Institutionellen. */
    EclInsti[] instiListe = null;

    /** true, wenn der aufrufende Benutzer ALLE Rechte zur Benutzerpflege hat. */
    private boolean volleFunktionen = false;

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
        assert cbKennungDurchSoftwareGesperrt != null : "fx:id=\"cbKennungDurchSoftwareGesperrt\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert cbKennungManuellGesperrt != null : "fx:id=\"cbKennungManuellGesperrt\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert cbTrivialPasswortZulaessig != null : "fx:id=\"cbTrivialPasswortZulaessig\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert cbIstGruppenKennungHV != null : "fx:id=\"cbIstGruppenKennungHV\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert tfNeuesPasswort != null : "fx:id=\"tfNeuesPasswort\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert scrpnBerechtigungen != null : "fx:id=\"scrpnBerechtigungen\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'LoginUser.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'LoginUser.fxml'.";
        
        /*** Ab hier individuell **/

        if (ParamS.eclUserLogin.pruefe_hvMaster_kennungsPflegeVollstaendig()) {
            volleFunktionen = true;
        }

        lDbBundle = new DbBundle();

        /*Instis einrichten*/
        BlInsti blInsti = new BlInsti(false, lDbBundle);
        blInsti.holeInstiDaten(0);
        instiListe = blInsti.rcInsti;

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

        if (ParamS.clGlobalVar.datenbankPfadNrIstP02()) {
            lblAufDreiServern.setVisible(true);
            cbAufDreiServern.setVisible(true);
        }
        else {
            lblAufDreiServern.setVisible(false);
            cbAufDreiServern.setVisible(false);
        }
        
        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                //                		CaZeigeHinweis zeigeHinweis=new CaZeigeHinweis();
                //                		zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        setzeNichtInsti();

        setzePflegeModusAuf0();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfKennung.requestFocus();
            }
        });

    }

    /**
     * **********************Logik**************************************************.
     *
     * @return true, if successful
     */
    private boolean pruefeKennungFormaleEingabe() {
        String hEingabe = tfKennung.getText();
        if (hEingabe.isEmpty() || hEingabe.length() > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Bitte geben Sie eine gültige Kennung ein (mindestens 1, maximal 40 Zeichen!");
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
        if (eclUserInBearbeitung.kennungGesperrtDurchSoftware) {
            cbKennungDurchSoftwareGesperrt.setSelected(true);
        }
        if (eclUserInBearbeitung.kennungGesperrtManuell) {
            cbKennungManuellGesperrt.setSelected(true);
        }
        if (eclUserInBearbeitung.trivialPasswortZulaessig) {
            cbTrivialPasswortZulaessig.setSelected(true);
        }
        if (eclUserInBearbeitung.gruppenKennungHV) {
            cbIstGruppenKennungHV.setSelected(true);
        }

        /** Ggf. Institutionelle */
        if (cbKennungInstitutionelle.isSelected()) {
            lblGehoertZuInsti.setVisible(true);
            cbGehoertZuInsti.setVisible(true);

            cbaGehoertZuInsti = new CbAllgemein(cbGehoertZuInsti);
            for (int i = 0; i < instiListe.length; i++) {
                CbElement lElement = new CbElement();
                lElement.anzeige = instiListe[i].kurzBezeichnung;
                lElement.ident1 = instiListe[i].ident;
                if (lElement.ident1 == eclUserInBearbeitung.gehoertZuInsti) {
                    cbaGehoertZuInsti.addElementAusgewaehlt(lElement);
                } else {
                    cbaGehoertZuInsti.addElement(lElement);
                }
            }
        }

        cbBOMitarbeiter.setSelected(false);
        switch (eclUserInBearbeitung.gehoertZuInsti) {
        case -1:
            cbUndefiniert.setSelected(true);
            cbBOMitarbeiter.setSelected(true);
            break;
        case -2:
            cbEmittent.setSelected(true);
            break;
        case -3:
            cbDritter.setSelected(true);
            break;
        default:
            cbUndefiniert.setSelected(true);
            break;
        }

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
                if (volleFunktionen == false) {
                    cbBerechtigungen[i].setDisable(true);
                }
            }
            scrpnBerechtigungen.setContent(grpnBerechtigungen);

        }

        scrpnProfile.setContent(null);
        if (listProfile != null) {
            GridPane grpnProfile = new GridPane();
            grpnProfile.setVgap(5);
            grpnProfile.setHgap(5);

            int anzProfile = listProfile.length;
            cbProfile = new CheckBox[anzProfile];

            for (int i = 0; i < anzProfile; i++) {
                EclUserLogin lProfil = listProfile[i];
                cbProfile[i] = new CheckBox();
                if (lProfil.ausgewaehlt) {
                    cbProfile[i].setSelected(true);
                } else {
                    cbProfile[i].setSelected(false);
                }
                grpnProfile.add(cbProfile[i], 0, i);

                Label lName = new Label(lProfil.kennung);
                grpnProfile.add(lName, 1, i);

                Label lBeschreibung = new Label(lProfil.name);
                grpnProfile.add(lBeschreibung, 2, i);

            }
            scrpnProfile.setContent(grpnProfile);
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
        lDbBundle.dbUserLogin.leseZuKennung(pKennung, mitMandant);

        if (lDbBundle.dbUserLogin.anzUserLoginGefunden() == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Kennung nicht vorhanden!");
            tfKennung.requestFocus();
            lDbBundle.closeAll();
            return;
        }
        eclUserInBearbeitung = lDbBundle.dbUserLogin.userLoginGefunden(0);

        /*verfügbare Profile einlesen*/
        lDbBundle.dbUserLogin.profileVerarbeiten = true;
        if (eclUserInBearbeitung.mandant == 0) {
            lDbBundle.dbUserLogin.lese_alleProfileMandantenUnabhaenig();
        } else {
            lDbBundle.dbUserLogin.lese_alleProfileZuKlasse(ParamS.param.paramBasis.profileKlasse);
        }
        lDbBundle.dbUserLogin.profileVerarbeiten = false;
        listProfile = lDbBundle.dbUserLogin.userLoginArray;

        /*Alle dem User zugeordneten Profile einlesen*/
        lDbBundle.dbUserProfile.readUser(eclUserInBearbeitung.userLoginIdent);
        List<EclUserProfile> lZugeordneteProfileList = lDbBundle.dbUserProfile.ergebnis();

        /*Nun die zugeordneten Profile in listProfile auf ausgewaehlt setzen*/
        if (lZugeordneteProfileList != null) {
            for (int i = 0; i < lZugeordneteProfileList.size(); i++) {
                EclUserProfile lZugeordnetesProfil = lZugeordneteProfileList.get(i);
                int gef = -1;
                for (int i1 = 0; i1 < listProfile.length; i1++) {
                    if (listProfile[i1].userLoginIdent == lZugeordnetesProfil.profilIdent) {
                        gef = i1;
                    }
                }
                if (gef == -1) {
                    CaBug.drucke("001 - zugeordnetes Profil nicht gefunden");
                } else {
                    listProfile[gef].ausgewaehlt = true;
                }
            }
        }

        setzeFelderAufBearbeiten();
        if (eclUserInBearbeitung.gehoertZuInsti > 0) {
            setzeInsti();
            cbKennungInstitutionelle.setSelected(true);
        } else {
            setzeNichtInsti();
            cbKennungInstitutionelle.setSelected(false);
        }
        anzeigenUserZurBearbeitung();

        pflegeModus = 2;
        lDbBundle.closeAll();

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
        if (hLen > 200) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Name: maximal 200 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfName.requestFocus();
            return;
        }
        hLen = tfMailAdresse.getText().length();
        if (hLen > 200) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Mail: maximal 200 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfMailAdresse.requestFocus();
            return;
        }

        if (cbKennungInstitutionelle.isSelected()) {
            if (cbGehoertZuInsti.getValue() == null) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage, "Institutioneller: Bitte zuordnen!");
                return;
            }
        }

        /*Hier: Überprüfung der Eingaben wurde durchgeführt. Nun speichern*/

        eclUserInBearbeitung.kennung = tfKennung.getText();
        eclUserInBearbeitung.name = tfName.getText();
        eclUserInBearbeitung.email = tfMailAdresse.getText();

        if (cbKennungInstitutionelle.isSelected()) {
            eclUserInBearbeitung.gehoertZuInsti = cbGehoertZuInsti.getValue().ident1;
        } else {
            eclUserInBearbeitung.gehoertZuInsti = 0;
            if (cbBOMitarbeiter.isSelected()) {
                eclUserInBearbeitung.gehoertZuInsti = -1;
            }
            if (cbEmittent.isSelected()) {
                eclUserInBearbeitung.gehoertZuInsti = -2;
            }
            if (cbDritter.isSelected()) {
                eclUserInBearbeitung.gehoertZuInsti = -3;
            }
        }

        eclUserInBearbeitung.kennungGesperrtDurchSoftware = cbKennungDurchSoftwareGesperrt.isSelected();
        eclUserInBearbeitung.kennungGesperrtManuell = cbKennungManuellGesperrt.isSelected();
        eclUserInBearbeitung.trivialPasswortZulaessig = cbTrivialPasswortZulaessig.isSelected();
        eclUserInBearbeitung.gruppenKennungHV = cbIstGruppenKennungHV.isSelected();

        if (!tfNeuesPasswort.getText().isEmpty()) {/*Neues Passwort wurde vergeben*/
            eclUserInBearbeitung.neuesPasswortErforderlich = true;
            if (!eclUserInBearbeitung.passwort.isEmpty()) {
                eclUserInBearbeitung.altesPasswort3 = eclUserInBearbeitung.altesPasswort2;
                eclUserInBearbeitung.altesPasswort2 = eclUserInBearbeitung.altesPasswort1;
                eclUserInBearbeitung.altesPasswort1 = eclUserInBearbeitung.passwort;

            }
            eclUserInBearbeitung.passwort = CaPasswortVerschluesseln.verschluesseln(tfNeuesPasswort.getText());
        }

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

        lDbBundle.openAll();
        if (pflegeModus == 1) { /*Neuaufnahme*/
            int rc = lDbBundle.dbUserLogin.insert(eclUserInBearbeitung, mitMandant);
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        } else {/*Ändern*/
            int rc = lDbBundle.dbUserLogin.update(eclUserInBearbeitung);
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        }

        /*Zugeordnete Profile speichern: erst bestehende Löschen, dann neue speichern*/
        lDbBundle.dbUserProfile.delete(eclUserInBearbeitung.userLoginIdent);
        if (listProfile != null) {
            for (int i = 0; i < listProfile.length; i++) {
                if (cbProfile[i].isSelected()) {
                    EclUserProfile lZugeordnetesProfil = new EclUserProfile();
                    lZugeordnetesProfil.userIdent = eclUserInBearbeitung.userLoginIdent;
                    lZugeordnetesProfil.profilIdent = listProfile[i].userLoginIdent;
                    lDbBundle.dbUserProfile.insert(lZugeordnetesProfil);
                }
            }
        }

        lDbBundle.closeAll();
        
        if (ParamS.clGlobalVar.datenbankPfadNrIstP02() && cbAufDreiServern.isSelected()) {
            /*Server P01*/
            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP01();
            lDbBundle.openAllOhneParameterCheck();
            int rcVorhanden=0;
            if (pflegeModus == 1) { /*Neuaufnahme*/
                int rc = lDbBundle.dbUserLogin.insert(eclUserInBearbeitung, mitMandant, false);
                if (rc < 1) {
                    CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                    zeigeHinweis.zeige(eigeneStage,
                            "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
                }
            } else {/*Ändern*/
                rcVorhanden=lDbBundle.dbUserLogin.leseZuIdent(eclUserInBearbeitung.userLoginIdent);
                if (rcVorhanden>0) {
                    eclUserInBearbeitung.passwort=lDbBundle.dbUserLogin.userLoginArray[0].passwort;
                    int rc = lDbBundle.dbUserLogin.updateUnabhaenigVonVersion(eclUserInBearbeitung);
                    if (rc < 1) {
                        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                        zeigeHinweis.zeige(eigeneStage,
                                "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
                    }
                }

            }

            if (pflegeModus==1 || rcVorhanden>0) {
                /*Zugeordnete Profile speichern: erst bestehende Löschen, dann neue speichern*/
                lDbBundle.dbUserProfile.delete(eclUserInBearbeitung.userLoginIdent);
                if (listProfile != null) {
                    for (int i = 0; i < listProfile.length; i++) {
                        if (cbProfile[i].isSelected()) {
                            EclUserProfile lZugeordnetesProfil = new EclUserProfile();
                            lZugeordnetesProfil.userIdent = eclUserInBearbeitung.userLoginIdent;
                            lZugeordnetesProfil.profilIdent = listProfile[i].userLoginIdent;
                            lDbBundle.dbUserProfile.insert(lZugeordnetesProfil);
                        }
                    }
                }
            }

            lDbBundle.closeAll();

            /*Server P03*/
            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP03();
            lDbBundle.openAllOhneParameterCheck();
            rcVorhanden=0;
            if (pflegeModus == 1) { /*Neuaufnahme*/
                int rc = lDbBundle.dbUserLogin.insert(eclUserInBearbeitung, mitMandant, false);
                if (rc < 1) {
                    CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                    zeigeHinweis.zeige(eigeneStage,
                            "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
                }
            } else {/*Ändern*/
                rcVorhanden=lDbBundle.dbUserLogin.leseZuIdent(eclUserInBearbeitung.userLoginIdent);
                if (rcVorhanden>0) {
                    eclUserInBearbeitung.passwort=lDbBundle.dbUserLogin.userLoginArray[0].passwort;
                    int rc = lDbBundle.dbUserLogin.updateUnabhaenigVonVersion(eclUserInBearbeitung);
                    if (rc < 1) {
                        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                        zeigeHinweis.zeige(eigeneStage,
                                "Kennung kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
                    }
                }

            }

            if (pflegeModus==1 || rcVorhanden>0) {
                /*Zugeordnete Profile speichern: erst bestehende Löschen, dann neue speichern*/
                lDbBundle.dbUserProfile.delete(eclUserInBearbeitung.userLoginIdent);
                if (listProfile != null) {
                    for (int i = 0; i < listProfile.length; i++) {
                        if (cbProfile[i].isSelected()) {
                            EclUserProfile lZugeordnetesProfil = new EclUserProfile();
                            lZugeordnetesProfil.userIdent = eclUserInBearbeitung.userLoginIdent;
                            lZugeordnetesProfil.profilIdent = listProfile[i].userLoginIdent;
                            lDbBundle.dbUserProfile.insert(lZugeordnetesProfil);
                        }
                    }
                }
            }

            lDbBundle.closeAll();

            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP02();

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

        boolean gefAufHauptServer=false;
        boolean gefAuf01=false;
        boolean gefAuf03=false;
        
        
        lDbBundle.openAll();
        /*Kennungs-String muß immer eindeutig sein - auch über Mandanten hinweg*/
        lDbBundle.dbUserLogin.pruefeKennungVorhanden(hEingabe);
        if (lDbBundle.dbUserLogin.anzUserLoginGefunden() > 0) {
            gefAufHauptServer=true;
        }
        lDbBundle.closeAll();

        if (ParamS.clGlobalVar.datenbankPfadNrIstP02() && cbAufDreiServern.isSelected()) {
            /*Prüfen, ob auf anderen Servern vorhanden*/
            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP01();
            lDbBundle.openAllOhneParameterCheck();
            lDbBundle.dbUserLogin.pruefeKennungVorhanden(hEingabe);
            if (lDbBundle.dbUserLogin.anzUserLoginGefunden() > 0) {
                gefAuf01=true;
            }
            lDbBundle.closeAll();
            
            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP03();
            lDbBundle.openAllOhneParameterCheck();
            lDbBundle.dbUserLogin.pruefeKennungVorhanden(hEingabe);
            if (lDbBundle.dbUserLogin.anzUserLoginGefunden() > 0) {
                gefAuf03=true;
            }
            lDbBundle.closeAll();
            
            ParamS.clGlobalVar.setzeDatenbankPfadNrAufP02();
        }
        
        if (gefAufHauptServer || gefAuf01 || gefAuf03) {
            String hinweis="Kennung bereits vorhanden ";
            if (gefAuf01) {
                hinweis=hinweis+"P01 ";
            }
            if (gefAuf03) {
                hinweis=hinweis+"P03 ";
            }
            hinweis=hinweis+"!";
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, hinweis);
            tfKennung.requestFocus();
            return;
            
        }
        
        
        eclUserInBearbeitung = new EclUserLogin();

        lDbBundle.openAll();

        /*verfügbare Profile einlesen*/
        lDbBundle.dbUserLogin.profileVerarbeiten = true;
        if (cbKennungHVSpezifisch.isSelected() == false) {
            lDbBundle.dbUserLogin.lese_alleProfileMandantenUnabhaenig();
        } else {
            lDbBundle.dbUserLogin.lese_alleProfileZuKlasse(ParamS.param.paramBasis.profileKlasse);
        }
        lDbBundle.dbUserLogin.profileVerarbeiten = false;
        listProfile = lDbBundle.dbUserLogin.userLoginArray;

        lDbBundle.closeAll();

        setzeFelderAufBearbeiten();
        pflegeModus = 1;
        anzeigenUserZurBearbeitung();

    }

    /**
     * ************************Anzeigefunktionen**************************************.
     */

    private void setzeFelderAufBearbeiten() {

        cbKennungHVSpezifisch.setDisable(true);
        cbKennungInstitutionelle.setDisable(true);

        tfKennung.setEditable(false);
        btnBearbeiten.setVisible(false);
        btnNeueKennungAnlegen.setVisible(false);
        btnSuchen.setVisible(false);

        btnSpeichern.setVisible(true);
        btnAbbrechen.setVisible(true);

        if (volleFunktionen) {
            tfName.setEditable(true);
        }
        if (volleFunktionen) {
            tfMailAdresse.setEditable(true);
        }

        cbKennungDurchSoftwareGesperrt.setDisable(false);
        cbKennungManuellGesperrt.setDisable(false);
        if (volleFunktionen) {
            cbTrivialPasswortZulaessig.setDisable(false);
        }
        if (volleFunktionen) {
            cbIstGruppenKennungHV.setDisable(false);
        }
        if (volleFunktionen) {
            cbUndefiniert.setDisable(false);
        }
        if (volleFunktionen) {
            cbEmittent.setDisable(false);
        }
        if (volleFunktionen) {
            cbDritter.setDisable(false);
        }
        if (volleFunktionen) {
            cbBOMitarbeiter.setDisable(false);
        }

        tfNeuesPasswort.setEditable(true);
        scrpnBerechtigungen.setContent(null);
        scrpnProfile.setContent(null);

        tfName.requestFocus();

    }

    /**
     * Setze pflege modus auf 0.
     */
    private void setzePflegeModusAuf0() {

        cbKennungHVSpezifisch.setDisable(false);
        cbKennungInstitutionelle.setDisable(false);

        tfKennung.setEditable(true);
        btnBearbeiten.setVisible(true);
        if (volleFunktionen) {
            btnNeueKennungAnlegen.setVisible(true);
        } else {
            btnNeueKennungAnlegen.setVisible(false);
        }

        btnSuchen.setVisible(true);

        btnSpeichern.setVisible(false);
        btnAbbrechen.setVisible(false);

        /*Nun Detailfelder auf "leer" setzen*/
        tfKennung.setText("");
        tfName.setText("");
        tfMailAdresse.setText("");

        cbaGehoertZuInsti = new CbAllgemein(cbGehoertZuInsti);

        cbKennungDurchSoftwareGesperrt.setSelected(false);
        cbKennungManuellGesperrt.setSelected(false);
        cbTrivialPasswortZulaessig.setSelected(false);
        cbIstGruppenKennungHV.setSelected(false);

        tfNeuesPasswort.setText("");

        cbUndefiniert.setSelected(false);
        cbEmittent.setSelected(false);
        cbDritter.setSelected(false);
        cbBOMitarbeiter.setSelected(false);

        /*Nun Detailfelder auf "nicht bearbeitbar" setzen*/
        tfName.setEditable(false);
        tfMailAdresse.setEditable(false);

        cbKennungDurchSoftwareGesperrt.setDisable(true);
        cbKennungManuellGesperrt.setDisable(true);
        cbTrivialPasswortZulaessig.setDisable(true);
        cbIstGruppenKennungHV.setDisable(true);

        cbUndefiniert.setDisable(true);
        cbEmittent.setDisable(true);
        cbDritter.setDisable(true);
        cbBOMitarbeiter.setDisable(true);

        tfNeuesPasswort.setEditable(false);
        scrpnBerechtigungen.setContent(null);
        scrpnProfile.setContent(null);

        pflegeModus = 0;
        tfKennung.requestFocus();

    }

    /**
     * Setze insti.
     */
    private void setzeInsti() {
        lblGehoertZuInsti.setVisible(true);
        cbGehoertZuInsti.setVisible(true);

        cbUndefiniert.setVisible(false);
        cbEmittent.setVisible(false);
        cbDritter.setVisible(false);

        lblBOMitarbeiter.setVisible(false);
        cbBOMitarbeiter.setVisible(false);
        cbBOMitarbeiter.setSelected(false);

    }

    /**
     * Setze nicht insti.
     */
    private void setzeNichtInsti() {
        lblGehoertZuInsti.setVisible(false);
        cbGehoertZuInsti.setVisible(false);

        if (cbKennungHVSpezifisch.isSelected()) {
            cbUndefiniert.setVisible(true);
            cbEmittent.setVisible(true);
            cbDritter.setVisible(true);

            lblBOMitarbeiter.setVisible(false);
            cbBOMitarbeiter.setVisible(false);
            cbBOMitarbeiter.setSelected(false);
        } else {
            cbUndefiniert.setSelected(true);
            cbUndefiniert.setVisible(false);
            cbEmittent.setVisible(false);
            cbDritter.setVisible(false);

            lblBOMitarbeiter.setVisible(true);
            cbBOMitarbeiter.setVisible(true);
        }
    }

    /**
     * ******************Aktionen auf Oberfläche************************.
     *
     * @param event the event
     */

    @FXML
    void onCbKennungHVSpezifisch(ActionEvent event) {
        if (cbKennungHVSpezifisch.isSelected()) {
            cbKennungInstitutionelle.setSelected(false);
            cbKennungInstitutionelle.setDisable(true);
        } else {
            cbKennungInstitutionelle.setSelected(false);
            cbKennungInstitutionelle.setDisable(false);
        }
        setzeNichtInsti();
    }

    /**
     * On cb kennung institutionelle.
     *
     * @param event the event
     */
    @FXML
    void onCbKennungInstitutionelle(ActionEvent event) {
        if (cbKennungInstitutionelle.isSelected()) {
            cbKennungHVSpezifisch.setSelected(false);
            cbKennungHVSpezifisch.setDisable(true);

            setzeInsti();
        } else {
            cbKennungHVSpezifisch.setSelected(false);
            cbKennungHVSpezifisch.setDisable(false);

            setzeNichtInsti();
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

        lDbBundle.closeAll();
    }

    /**
     * ***********Initialisierung******************.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
