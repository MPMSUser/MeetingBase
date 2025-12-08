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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteKlasse;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 * The Class CtrlParameterGeraeteKlassen.
 */
public class CtrlParameterGeraeteKlassen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf klasse. */
    @FXML
    private TextField tfKlasse;

    /** The btn bearbeiten. */
    @FXML
    private Button btnBearbeiten;

    /** The btn neue klasse. */
    @FXML
    private Button btnNeueKlasse;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** The btn vor. */
    @FXML
    private Button btnVor;

    /** The combo set. */
    @FXML
    private ComboBox<EclGeraeteSet> comboSet;

    /** The tf bezeichnung. */
    @FXML
    private TextField tfBezeichnung;

    /** The tf ausfuehrliche beschreibung. */
    @FXML
    private TextArea tfAusfuehrlicheBeschreibung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn loeschen. */
    @FXML
    private Button btnLoeschen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The scrpn vorhandene klassen. */
    @FXML
    private ScrollPane scrpnVorhandeneKlassen;

    /** The tf abstimmung tablet typ. */
    @FXML
    private TextField tfAbstimmungTabletTyp;

    /** The tf lw pfad allgemein. */
    @FXML
    private TextField tfLwPfadAllgemein;

    /** The tf lw pfad grossdokumente. */
    @FXML
    private TextField tfLwPfadGrossdokumente;

    /** The tf lw pfad sicherung 1. */
    @FXML

    private TextField tfLwPfadSicherung1;

    /** The tf lw pfad sicherung 2. */
    @FXML
    private TextField tfLwPfadSicherung2;

    /** The tf lw pfad export fuer praesentation. */
    @FXML
    private TextField tfLwPfadExportFuerPraesentation;

    /** The tf lw pfad export fuer buehnensystem. */
    @FXML
    private TextField tfLwPfadExportFuerBuehnensystem;

    /** The tf lw pfad export excel fuer powerpoint. */
    @FXML
    private TextField tfLwPfadExportExcelFuerPowerpoint;

    /** The tf lw pfad kundenordner basis. */
    @FXML
    private TextField tfLwPfadKundenordnerBasis;

    /** The cb bon drucker ist zugeordnet. */
    @FXML
    private CheckBox cbBonDruckerIstZugeordnet;

    /** The cb kontroll screen aktiv. */
    @FXML
    private CheckBox cbKontrollScreenAktiv;

    /** The cb vorrangig DB. */
    @FXML
    private CheckBox cbVorrangigDB;

    /** The cb module HV master. */
    @FXML
    private CheckBox cbModuleHVMaster;

    /** The cb module designer. */
    @FXML
    private CheckBox cbModuleDesigner;

    /** The cb module front office. */
    @FXML
    private CheckBox cbModuleFrontOffice;

    /** The cb module tabletabstimmung. */
    @FXML
    private CheckBox cbModuleTabletabstimmung;

    /** The cb module kontrolle. */
    @FXML
    private CheckBox cbModuleKontrolle;

    /** The cb module service desk. */
    @FXML
    private CheckBox cbModuleServiceDesk;

    /** The cb module teilnahmeverzeichnis. */
    @FXML
    private CheckBox cbModuleTeilnahmeverzeichnis;

    /** The cb module anmeldestelle. */
    @FXML
    private CheckBox cbModuleAnmeldestelle;

    /** The cb module hotline. */
    @FXML
    private CheckBox cbModuleHotline;

    /** The cb module aktienregister import. */
    @FXML
    private CheckBox cbModuleAktienregisterImport;

    /** The tf festgelegtes jahr. */
    @FXML
    private TextField tfFestgelegtesJahr;

    /** The combo festgelegter mandant. */
    @FXML
    private ComboBox<EclEmittenten> comboFestgelegterMandant;

    /** The tf festgelegte HV nummer. */
    @FXML
    private TextField tfFestgelegteHVNummer;

    /** The tf festgelegte datenbank. */
    @FXML
    private TextField tfFestgelegteDatenbank;

    /** The tf festgelegter benutzername. */
    @FXML
    private TextField tfFestgelegterBenutzername;

    /** The cb festgelegter mandant fix. */
    @FXML
    private CheckBox cbFestgelegterMandantFix;

    /** The cb festgelegtes jahr fix. */
    @FXML
    private CheckBox cbFestgelegtesJahrFix;

    /** The cb festgelegte HV nummer fix. */
    @FXML
    private CheckBox cbFestgelegteHVNummerFix;

    /** The cb festgelegte datenbank fix. */
    @FXML
    private CheckBox cbFestgelegteDatenbankFix;

    /** The cb festgelegter benutzername fix. */
    @FXML
    private CheckBox cbFestgelegterBenutzernameFix;

    /** The cb akkreditierung scan feld fuer bestaetigung aktiv. */
    @FXML
    private CheckBox cbAkkreditierungScanFeldFuerBestaetigungAktiv;

    /** The cb akkreditierung shortcuts aktiv. */
    @FXML
    private CheckBox cbAkkreditierungShortcutsAktiv;

    /** The cb akkreditierung anzeige belege buendeln. */
    @FXML
    private CheckBox cbAkkreditierungAnzeigeBelegeBuendeln;

    /** The tf protokoll anz max zugaenge. */
    @FXML
    private TextField tfProtokollAnzMaxZugaenge;

    /** The tf protokoll anz max. */
    @FXML
    private TextField tfProtokollAnzMax;

    /** The cb akkreditierung delay ignorieren. */
    @FXML
    private CheckBox cbAkkreditierungDelayIgnorieren;

    /** The cb akkreditierung sammelkarten buchen moeglich. */
    @FXML
    private CheckBox cbAkkreditierungSammelkartenBuchenMoeglich;

    /** The cb akkreditierung labeldruck fuer aktionaer. */
    @FXML
    private CheckBox cbAkkreditierungLabeldruckFuerAktionaer;

    /** The cb akkreditierung suchfunktion aktiv. */
    @FXML
    private CheckBox cbAkkreditierungSuchfunktionAktiv;

    /** The cb vertreter erfassung aktiv. */
    @FXML
    private CheckBox cbVertreterErfassungAktiv;

    /** The cb vertreter korrektur bei kontrollerfassung moeglich. */
    @FXML
    private CheckBox cbVertreterKorrekturBeiKontrollerfassungMoeglich;

    /** The cb abstimmung tablet hochformat. */
    @FXML
    private CheckBox cbAbstimmungTabletHochformat;

    /** The cb abstimmung tablet persoenlich zugeordnet. */
    @FXML
    private CheckBox cbAbstimmungTabletPersoenlichZugeordnet;

    /** The cb abstimmung tablet testmodus. */
    @FXML
    private CheckBox cbAbstimmungTabletTestmodus;

    /** The cb abtimmung tablet full screen. */
    @FXML
    private CheckBox cbAbtimmungTabletFullScreen;

    /** The tf abstimmung tablet X size. */
    @FXML
    private TextField tfAbstimmungTabletXSize;

    /** The tf abstimmung tablet Y size. */
    @FXML
    private TextField tfAbstimmungTabletYSize;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The fehler. */
    private int fehler = 0;

    /** Wird von refresh_verfuegbareGeraetesets gefüllt. */
    private EclGeraeteSet[] arrayGeraeteSet = null;

    /**Pflegemodus für Bearbeitung Geräteset.
     * 0 = nichts in Bearbeitung
     * 1 = Neuaufnahme
     * 2 = Ändern
     * 3 = Löschen
     */
    private int pflegeModus = 0;

    /** Geräte-Klasse, die gerade in Bearbeitung (Pflege) ist. */
    EclGeraeteKlasse eclGeraeteKlasseInBearbeitung = null;

    /** Geräte-Parameter, die gerade in Bearbeitung sind. */
    public ParamGeraet paramGeraetInBearbeitung = null;

    /** The aktives set. */
    private int aktivesSet = 0;

    /** Liste der Mandanten, zur Auswahl. Nur Nr. und (Kurz)Bezeichnung */
    List<EclEmittenten> listEclEmittenten = new LinkedList<EclEmittenten>();

    /** The klassen zu ausgewaehltem set. */
    private int[] klassenZuAusgewaehltemSet = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfKlasse != null : "fx:id=\"tfKlasse\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnBearbeiten != null : "fx:id=\"btnBearbeiten\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnNeueKlasse != null : "fx:id=\"btnNeueKlasse\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnZurueck != null : "fx:id=\"btnZurueck\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnVor != null : "fx:id=\"btnVor\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert comboSet != null : "fx:id=\"comboSet\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfBezeichnung != null : "fx:id=\"tfBezeichnung\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfAusfuehrlicheBeschreibung != null : "fx:id=\"tfAusfuehrlicheBeschreibung\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnLoeschen != null : "fx:id=\"btnLoeschen\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert scrpnVorhandeneKlassen != null : "fx:id=\"scrpnVorhandeneKlassen\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfAbstimmungTabletTyp != null : "fx:id=\"tfAbstimmungTabletTyp\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadAllgemein != null : "fx:id=\"tfLwPfadAllgemein\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadSicherung1 != null : "fx:id=\"tfLwPfadSicherung1\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadSicherung2 != null : "fx:id=\"tfLwPfadSicherung2\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadExportFuerPraesentation != null : "fx:id=\"tfLwPfadExportFuerPraesentation\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadExportFuerBuehnensystem != null : "fx:id=\"tfLwPfadExportFuerBuehnensystem\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadExportExcelFuerPowerpoint != null : "fx:id=\"tfLwPfadExportExcelFuerPowerpoint\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfLwPfadKundenordnerBasis != null : "fx:id=\"tfLwPfadKundenordnerBasis\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbBonDruckerIstZugeordnet != null : "fx:id=\"cbBonDruckerIstZugeordnet\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbKontrollScreenAktiv != null : "fx:id=\"cbKontrollScreenAktiv\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleHVMaster != null : "fx:id=\"cbModuleHVMaster\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleFrontOffice != null : "fx:id=\"cbModuleFrontOffice\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleKontrolle != null : "fx:id=\"cbModuleKontrolle\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleServiceDesk != null : "fx:id=\"cbModuleServiceDesk\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleTeilnahmeverzeichnis != null : "fx:id=\"cbModuleTeilnahmeverzeichnis\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleTabletabstimmung != null : "fx:id=\"cbModuleTabletabstimmung\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleAnmeldestelle != null : "fx:id=\"cbModuleAnmeldestelle\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleHotline != null : "fx:id=\"cbModuleHotline\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbModuleAktienregisterImport != null : "fx:id=\"cbModuleAktienregisterImport\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfFestgelegtesJahr != null : "fx:id=\"tfFestgelegtesJahr\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert comboFestgelegterMandant != null : "fx:id=\"comboFestgelegterMandant\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfFestgelegteHVNummer != null : "fx:id=\"tfFestgelegteHVNummer\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfFestgelegteDatenbank != null : "fx:id=\"tfFestgelegteDatenbank\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfFestgelegterBenutzername != null : "fx:id=\"tfFestgelegterBenutzername\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungScanFeldFuerBestaetigungAktiv != null : "fx:id=\"cbAkkreditierungScanFeldFuerBestaetigungAktiv\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungShortcutsAktiv != null : "fx:id=\"cbAkkreditierungShortcutsAktiv\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungAnzeigeBelegeBuendeln != null : "fx:id=\"cbAkkreditierungAnzeigeBelegeBuendeln\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfProtokollAnzMaxZugaenge != null : "fx:id=\"tfProtokollAnzMaxZugaenge\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert tfProtokollAnzMax != null : "fx:id=\"tfProtokollAnzMax\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungDelayIgnorieren != null : "fx:id=\"cbAkkreditierungDelayIgnorieren\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungSammelkartenBuchenMoeglich != null : "fx:id=\"cbAkkreditierungSammelkartenBuchenMoeglich\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAkkreditierungLabeldruckFuerAktionaer != null : "fx:id=\"cbAkkreditierungLabeldruckFuerAktionaer\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbVertreterKorrekturBeiKontrollerfassungMoeglich != null : "fx:id=\"cbVertreterKorrekturBeiKontrollerfassungMoeglich\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        assert cbAbstimmungTabletHochformat != null : "fx:id=\"cbAbstimmungTabletHochformat\" was not injected: check your FXML file 'ParameterGeraeteKlassen.fxml'.";
        

        /*** Ab hier individuell **/
        lDbBundle = new DbBundle();

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                //                		CaZeigeHinweis zeigeHinweis=new CaZeigeHinweis();
                //                		zeigeHinweis.zeige(eigeneStage, "Achtung - letzte Änderungen werden nicht gespeichert!");
            }
        });

        lDbBundle.openAll();

        refresh_GeraeteSets();
        refreshUebersichtGeraeteKlassen();

        BvMandanten lBvMandanten = new BvMandanten();
        listEclEmittenten = lBvMandanten.liefereEmittentenListeFuerNrAuswahl(lDbBundle);

        setzePflegeModusAuf0();

        lDbBundle.closeAll();
        if (fehler < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, CaFehler.getFehlertext(fehler, 0));
        }

        comboSet.valueProperty().addListener(new ChangeListener<EclGeraeteSet>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, EclGeraeteSet alterWert,
                    EclGeraeteSet neuerWert) {
                lDbBundle.openAll();
                refreshUebersichtGeraeteKlassen();
                lDbBundle.closeAll();
            }
        });

    }

    /**
     * Pruefe klassen nr formale eingabe.
     *
     * @return true, if successful
     */
    //    /************************Logik***************************************************/
    private boolean pruefeKlassenNrFormaleEingabe() {
        String hEingabe = tfKlasse.getText();
        if (hEingabe.isEmpty() || CaString.isNummern(hEingabe) == false || hEingabe.length() > 4
                || Integer.parseInt(hEingabe) > 9999 || Integer.parseInt(hEingabe) < 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bitte geben Sie eine gültige Klassen-Ident zwischen 1 und 9999 ein!");
            tfKlasse.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Einlesen anzeigen klasse zur bearbeitung aus feld.
     */
    private void einlesenAnzeigenKlasseZurBearbeitungAusFeld() {
        String hEingabe = tfKlasse.getText();
        if (pruefeKlassenNrFormaleEingabe() == false) {
            return;
        }

        int hIdent = Integer.parseInt(hEingabe);
        einlesenAnzeigenKlasseZurBearbeitung(hIdent);
    }

    /**
     * Anzeigen klasse zur bearbeitung.
     */
    /*Überträgt die in paramGeraetInBearbeitung enthaltenen Parameter in die Bildschirmfelder und aktiviert diese*/
    private void anzeigenKlasseZurBearbeitung() {
        tfAbstimmungTabletTyp.setText(Integer.toString(paramGeraetInBearbeitung.abstimmungTabletTyp));
        tfLwPfadAllgemein.setText(paramGeraetInBearbeitung.lwPfadAllgemein);
        tfLwPfadGrossdokumente.setText(paramGeraetInBearbeitung.lwPfadGrossdokumente);
        tfLwPfadSicherung1.setText(paramGeraetInBearbeitung.lwPfadSicherung1);
        tfLwPfadSicherung2.setText(paramGeraetInBearbeitung.lwPfadSicherung2);
        tfLwPfadExportFuerPraesentation.setText(paramGeraetInBearbeitung.lwPfadExportFuerPraesentation);
        tfLwPfadExportFuerBuehnensystem.setText(paramGeraetInBearbeitung.lwPfadExportFuerBuehnensystem);
        tfLwPfadExportExcelFuerPowerpoint.setText(paramGeraetInBearbeitung.lwPfadExportExcelFuerPowerpoint);
        tfLwPfadKundenordnerBasis.setText(paramGeraetInBearbeitung.lwPfadKundenordnerBasis);
        cbBonDruckerIstZugeordnet.setSelected(paramGeraetInBearbeitung.bonDruckerIstZugeordnet);
        cbKontrollScreenAktiv.setSelected(paramGeraetInBearbeitung.programmStartKontrollScreenAnzeigen);
        cbVorrangigDB.setSelected(paramGeraetInBearbeitung.dbVorrangig);

        cbModuleHVMaster.setSelected(paramGeraetInBearbeitung.moduleHVMaster);
        cbModuleDesigner.setSelected(paramGeraetInBearbeitung.moduleDesigner);
        cbModuleFrontOffice.setSelected(paramGeraetInBearbeitung.moduleFrontOffice);
        cbModuleKontrolle.setSelected(paramGeraetInBearbeitung.moduleKontrolle);
        cbModuleServiceDesk.setSelected(paramGeraetInBearbeitung.moduleServiceDesk);
        cbModuleTeilnahmeverzeichnis.setSelected(paramGeraetInBearbeitung.moduleTeilnahmeverzeichnis);
        cbModuleTabletabstimmung.setSelected(paramGeraetInBearbeitung.moduleTabletAbstimmung);
        cbModuleAnmeldestelle.setSelected(paramGeraetInBearbeitung.moduleBestandsverwaltung);
        cbModuleHotline.setSelected(paramGeraetInBearbeitung.moduleHotline);
        cbModuleAktienregisterImport.setSelected(paramGeraetInBearbeitung.moduleAktienregisterImport);

        StringConverter<EclEmittenten> sc = new StringConverter<EclEmittenten>() {
            @Override
            public String toString(EclEmittenten pEmittent) {
                if (pEmittent == null) {
                    return "";
                }
                return Integer.toString(pEmittent.mandant) + " " + pEmittent.bezeichnungKurz;
            }

            @Override
            public EclEmittenten fromString(String string) {
                return null;
            }
        };
        comboFestgelegterMandant.setConverter(sc);
        comboFestgelegterMandant.getSelectionModel().clearSelection();
        comboFestgelegterMandant.getItems().clear();
        EclEmittenten lEmittent = new EclEmittenten();
        lEmittent.mandant = 0;
        lEmittent.bezeichnungKurz = "keine Voreinstellung";
        comboFestgelegterMandant.getItems().add(lEmittent);
        if (paramGeraetInBearbeitung.festgelegterMandant == 0) {
            comboFestgelegterMandant.setValue(lEmittent);
        }
        for (int i = 0; i < listEclEmittenten.size(); i++) {
            comboFestgelegterMandant.getItems().add(listEclEmittenten.get(i));
            if (paramGeraetInBearbeitung.festgelegterMandant == listEclEmittenten.get(i).mandant) {
                comboFestgelegterMandant.setValue(listEclEmittenten.get(i));
            }
        }

        if (paramGeraetInBearbeitung.festgelegtesJahr != 0) {
            tfFestgelegtesJahr.setText(Integer.toString(paramGeraetInBearbeitung.festgelegtesJahr));
        } else {
            tfFestgelegtesJahr.setText("");
        }
        tfFestgelegteHVNummer.setText(paramGeraetInBearbeitung.festgelegteHVNummer);
        tfFestgelegteDatenbank.setText(paramGeraetInBearbeitung.festgelegteDatenbank);
        tfFestgelegterBenutzername.setText(paramGeraetInBearbeitung.festgelegterBenutzername);

        cbFestgelegterMandantFix.setSelected(paramGeraetInBearbeitung.festgelegterMandantIstFix);
        cbFestgelegtesJahrFix.setSelected(paramGeraetInBearbeitung.festgelegtesJahrIstFix);
        cbFestgelegteHVNummerFix.setSelected(paramGeraetInBearbeitung.festgelegteHVNummerFix);
        cbFestgelegteDatenbankFix.setSelected(paramGeraetInBearbeitung.festgelegteDatenbankFix);
        cbFestgelegterBenutzernameFix.setSelected(paramGeraetInBearbeitung.festgelegteBenutzernameFix);

        cbAkkreditierungScanFeldFuerBestaetigungAktiv
                .setSelected(paramGeraetInBearbeitung.akkreditierungScanFeldFuerBestaetigungAktiv);
        cbAkkreditierungShortcutsAktiv.setSelected(paramGeraetInBearbeitung.akkreditierungShortcutsAktiv);
        cbAkkreditierungAnzeigeBelegeBuendeln.setSelected(paramGeraetInBearbeitung.akkreditierungAnzeigeBelegeBuendeln);
        tfProtokollAnzMaxZugaenge.setText(Integer.toString(paramGeraetInBearbeitung.protokollAnzMaxZugaenge));
        tfProtokollAnzMax.setText(Integer.toString(paramGeraetInBearbeitung.protokollAnzMax));
        cbAkkreditierungDelayIgnorieren.setSelected(paramGeraetInBearbeitung.akkreditierungDelayIgnorieren);
        cbAkkreditierungSammelkartenBuchenMoeglich
                .setSelected(paramGeraetInBearbeitung.akkreditierungSammelkartenBuchenMoeglich);
        cbAkkreditierungLabeldruckFuerAktionaer
                .setSelected(paramGeraetInBearbeitung.akkreditierungLabeldruckFuerAktionaer);
        cbAkkreditierungSuchfunktionAktiv.setSelected(paramGeraetInBearbeitung.akkreditierungSuchfunktionAktiv);
        cbVertreterErfassungAktiv.setSelected(paramGeraetInBearbeitung.akkreditierungVertreterErfassungAktiv);
        cbVertreterKorrekturBeiKontrollerfassungMoeglich
                .setSelected(paramGeraetInBearbeitung.vertreterKorrekturBeiKontrollerfassungMoeglich);

        cbAbstimmungTabletHochformat.setSelected(paramGeraetInBearbeitung.abstimmungTabletHochformat);
        cbAbstimmungTabletPersoenlichZugeordnet
                .setSelected(paramGeraetInBearbeitung.abstimmungTabletPersoenlichZugeordnet);
        cbAbstimmungTabletTestmodus.setSelected(paramGeraetInBearbeitung.abstimmungTabletTestmodus);
        cbAbtimmungTabletFullScreen.setSelected(paramGeraetInBearbeitung.abtimmungTabletFullScreen);

        tfAbstimmungTabletXSize.setText(paramGeraetInBearbeitung.abstimmungTabletXSize);
        tfAbstimmungTabletYSize.setText(paramGeraetInBearbeitung.abstimmungTabletYSize);

        /*Nun Detailfelder auf "bearbeitbar" setzen*/
        tfAbstimmungTabletTyp.setEditable(true);
        tfLwPfadAllgemein.setEditable(true);
        tfLwPfadGrossdokumente.setEditable(true);
        tfLwPfadSicherung1.setEditable(true);
        tfLwPfadSicherung2.setEditable(true);
        tfLwPfadExportFuerPraesentation.setEditable(true);
        tfLwPfadExportFuerBuehnensystem.setEditable(true);
        tfLwPfadExportExcelFuerPowerpoint.setEditable(true);
        tfLwPfadKundenordnerBasis.setEditable(true);
        cbBonDruckerIstZugeordnet.setDisable(false);
        cbKontrollScreenAktiv.setDisable(false);
        cbVorrangigDB.setDisable(false);

        cbModuleHVMaster.setDisable(false);
        cbModuleDesigner.setDisable(false);
        cbModuleFrontOffice.setDisable(false);
        cbModuleKontrolle.setDisable(false);
        cbModuleServiceDesk.setDisable(false);
        cbModuleTeilnahmeverzeichnis.setDisable(false);
        cbModuleTabletabstimmung.setDisable(false);
        cbModuleAnmeldestelle.setDisable(false);
        cbModuleHotline.setDisable(false);
        cbModuleAktienregisterImport.setDisable(false);

        tfFestgelegtesJahr.setEditable(true);
        tfFestgelegteHVNummer.setEditable(true);
        tfFestgelegteDatenbank.setEditable(true);
        tfFestgelegterBenutzername.setEditable(true);

        cbAkkreditierungScanFeldFuerBestaetigungAktiv.setDisable(false);
        cbAkkreditierungShortcutsAktiv.setDisable(false);
        cbAkkreditierungAnzeigeBelegeBuendeln.setDisable(false);
        tfProtokollAnzMaxZugaenge.setEditable(true);
        tfProtokollAnzMax.setEditable(true);
        cbAkkreditierungDelayIgnorieren.setDisable(false);
        cbAkkreditierungSammelkartenBuchenMoeglich.setDisable(false);
        cbAkkreditierungLabeldruckFuerAktionaer.setDisable(false);
        cbAkkreditierungSuchfunktionAktiv.setDisable(false);
        cbVertreterErfassungAktiv.setDisable(false);
        cbVertreterKorrekturBeiKontrollerfassungMoeglich.setDisable(false);

        cbAbstimmungTabletHochformat.setDisable(false);
        cbAbstimmungTabletPersoenlichZugeordnet.setDisable(false);
        cbAbstimmungTabletTestmodus.setDisable(false);
        cbAbtimmungTabletFullScreen.setDisable(false);

        tfAbstimmungTabletXSize.setEditable(true);
        tfAbstimmungTabletYSize.setEditable(true);

    }

    /**
     * Einlesen anzeigen klasse zur bearbeitung.
     *
     * @param pIdent the ident
     */
    private void einlesenAnzeigenKlasseZurBearbeitung(int pIdent) {
        lDbBundle.openAll();
        lDbBundle.dbGeraeteKlasse.read(pIdent);
        lDbBundle.dbParameter.readGerateKlasse_all(pIdent);
        lDbBundle.closeAll();

        if (lDbBundle.dbGeraeteKlasse.anzErgebnis() == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Geräte-Klasse nicht vorhanden!");
            tfKlasse.requestFocus();
            return;
        }
        eclGeraeteKlasseInBearbeitung = lDbBundle.dbGeraeteKlasse.ergebnisPosition(0);
        paramGeraetInBearbeitung = lDbBundle.dbParameter.ergParamGeraet;

        tfBezeichnung.setText(eclGeraeteKlasseInBearbeitung.kurzBeschreibung);
        tfBezeichnung.setEditable(true);

        tfAusfuehrlicheBeschreibung.setText(eclGeraeteKlasseInBearbeitung.beschreibung);
        tfAusfuehrlicheBeschreibung.setEditable(true);

        tfKlasse.setText(Integer.toString(pIdent));
        tfKlasse.setEditable(false);

        pflegeModus = 2;
        btnSpeichern.setVisible(true);
        btnLoeschen.setVisible(true);
        btnAbbrechen.setVisible(true);

        btnBearbeiten.setVisible(false);
        btnNeueKlasse.setVisible(false);

        tfBezeichnung.requestFocus();

        /*Nun Detailfelder mit Inhalt füllen*/
        anzeigenKlasseZurBearbeitung();

    }

    /**
     * Sets the in bearbeitung speichern.
     */
    private void setInBearbeitungSpeichern() {
        int hLen = 0;
        String hString = "";
        hLen = tfBezeichnung.getText().length();
        if (hLen > 50) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Bezeichnung der Klasse: maximal 50 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfBezeichnung.requestFocus();
            return;
        }
        hLen = tfAusfuehrlicheBeschreibung.getText().length();
        if (hLen > 500) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Ausführliche Beschreibung: maximal 500 Stellen zulässig (derzeit " + Integer.toString(hLen) + "!");
            tfBezeichnung.requestFocus();
            return;
        }

        hString = tfAbstimmungTabletTyp.getText();
        if (!CaString.isNummern(hString) || Integer.parseInt(hString) < 1 || Integer.parseInt(hString) > 3) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Gerätetyp muß eine Zahl zwischen 1 und 3 sein!");
            tfAbstimmungTabletTyp.requestFocus();
            return;
        }

        hLen = tfLwPfadAllgemein.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Arbeits-LW/Pfad darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadAllgemein.requestFocus();
            return;
        }
        hLen = tfLwPfadGrossdokumente.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Großdoumente-LW/Pfad darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadGrossdokumente.requestFocus();
            return;
        }
        hLen = tfLwPfadSicherung1.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "LW/Pfad Sicherung 1 darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadSicherung1.requestFocus();
            return;
        }
        hLen = tfLwPfadSicherung2.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "LW/Pfad Sicherung 2 darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadSicherung2.requestFocus();
            return;
        }
        hLen = tfLwPfadExportFuerPraesentation.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Ex.Pfad Präsentation darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadExportFuerPraesentation.requestFocus();
            return;
        }
        hLen = tfLwPfadExportFuerBuehnensystem.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Ex.Pfad Bühnensystem darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadExportFuerBuehnensystem.requestFocus();
            return;
        }
        hLen = tfLwPfadExportExcelFuerPowerpoint.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Ex.Pfad PP-Excel darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadExportExcelFuerPowerpoint.requestFocus();
            return;
        }
        hLen = tfLwPfadKundenordnerBasis.getText().length();
        if (hLen > 40) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage,
                    "Kunden-LW/Pfad darf maximal 40 Stellen lang sein (derzeit  " + Integer.toString(hLen) + "!");
            tfLwPfadKundenordnerBasis.requestFocus();
            return;
        }

        hString = tfFestgelegtesJahr.getText();
        if (!hString.isEmpty() && (!CaString.isNummern(hString) || Integer.parseInt(hString) < 1990
                || Integer.parseInt(hString) > 2099)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "HV-Jahr muß eine Zahl zwischen 1990 und 2099 oder leer sein!");
            tfFestgelegtesJahr.requestFocus();
            return;
        }

        hString = tfFestgelegteHVNummer.getText();
        if (!hString.isEmpty() && (hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Lfd. HV-Nummer muß zwischen A und Z oder leer sein!");
            tfFestgelegteHVNummer.requestFocus();
            return;
        }

        hString = tfFestgelegteDatenbank.getText();
        if (!hString.isEmpty() && (hString.compareTo("A") < 0 || hString.compareTo("Z") > 0 || hString.length() > 1)) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Datenbereich muß zwischen A und Z oder leer sein!");
            tfFestgelegteDatenbank.requestFocus();
            return;
        }

        /*Auf gültig eingetragenen Benutzer abprüfen*/
        hString = tfFestgelegterBenutzername.getText().trim();
        if (!hString.isEmpty()) {
            lDbBundle.openAll();
            lDbBundle.dbUserLogin.leseZuKennung(hString, false);
            if (lDbBundle.dbUserLogin.anzUserLoginGefunden() < 1) {
                lDbBundle.closeAll();
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Benutzerkennung nicht vorhanden (nur mandanten-unabhängige Kennung zulässig)");
                tfFestgelegterBenutzername.requestFocus();
                return;
            }
            lDbBundle.closeAll();
        }

        hString = tfProtokollAnzMaxZugaenge.getText();
        if (!CaString.isNummern(hString) || Integer.parseInt(hString) < 10 || Integer.parseInt(hString) > 99999) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bündeln nach Erstzugängen muß eine Zahl zwischen 10 und 99999 sein!");
            tfProtokollAnzMaxZugaenge.requestFocus();
            return;
        }

        hString = tfProtokollAnzMax.getText();
        if (!CaString.isNummern(hString) || Integer.parseInt(hString) < 10 || Integer.parseInt(hString) > 99999) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Bündeln nach Vorgängen muß eine Zahl zwischen 10 und 99999 sein!");
            tfProtokollAnzMax.requestFocus();
            return;
        }

        /*Hier: Überprüfung der Eingaben wurde durchgeführt. Nun speichern*/

        /*eclGeraeteKlasse zurückspeichern*/
        eclGeraeteKlasseInBearbeitung.kurzBeschreibung = tfBezeichnung.getText();
        eclGeraeteKlasseInBearbeitung.beschreibung = tfAusfuehrlicheBeschreibung.getText();
        if (pflegeModus == 1) { /*Neuaufnahme*/
            lDbBundle.openAll();
            int rc = lDbBundle.dbGeraeteKlasse.insert(eclGeraeteKlasseInBearbeitung);
            refreshUebersichtGeraeteKlassen();
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Geräte-Klasse kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        } else {/*Ändern*/
            lDbBundle.openAll();
            int rc = lDbBundle.dbGeraeteKlasse.update(eclGeraeteKlasseInBearbeitung);
            refreshUebersichtGeraeteKlassen();
            lDbBundle.closeAll();
            if (rc < 1) {
                CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
                zeigeHinweis.zeige(eigeneStage,
                        "Geräte-Klasse kann nicht gespeichert werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
            }
        }

        /*Nun Parameter für diese Geräteklasse speichern*/
        paramGeraetInBearbeitung.abstimmungTabletTyp = Integer.parseInt(tfAbstimmungTabletTyp.getText());
        paramGeraetInBearbeitung.lwPfadAllgemein = tfLwPfadAllgemein.getText();
        paramGeraetInBearbeitung.lwPfadGrossdokumente = tfLwPfadGrossdokumente.getText();
        paramGeraetInBearbeitung.lwPfadSicherung1 = tfLwPfadSicherung1.getText();
        paramGeraetInBearbeitung.lwPfadSicherung2 = tfLwPfadSicherung2.getText();
        paramGeraetInBearbeitung.lwPfadExportFuerPraesentation = tfLwPfadExportFuerPraesentation.getText();
        paramGeraetInBearbeitung.lwPfadExportFuerBuehnensystem = tfLwPfadExportFuerBuehnensystem.getText();
        paramGeraetInBearbeitung.lwPfadExportExcelFuerPowerpoint = tfLwPfadExportExcelFuerPowerpoint.getText();
        paramGeraetInBearbeitung.lwPfadKundenordnerBasis = tfLwPfadKundenordnerBasis.getText();
        paramGeraetInBearbeitung.bonDruckerIstZugeordnet = cbBonDruckerIstZugeordnet.isSelected();
        paramGeraetInBearbeitung.programmStartKontrollScreenAnzeigen = cbKontrollScreenAktiv.isSelected();
        paramGeraetInBearbeitung.dbVorrangig = cbVorrangigDB.isSelected();

        paramGeraetInBearbeitung.moduleHVMaster = cbModuleHVMaster.isSelected();
        paramGeraetInBearbeitung.moduleDesigner = cbModuleDesigner.isSelected();
        paramGeraetInBearbeitung.moduleFrontOffice = cbModuleFrontOffice.isSelected();
        paramGeraetInBearbeitung.moduleKontrolle = cbModuleKontrolle.isSelected();
        paramGeraetInBearbeitung.moduleServiceDesk = cbModuleServiceDesk.isSelected();
        paramGeraetInBearbeitung.moduleTeilnahmeverzeichnis = cbModuleTeilnahmeverzeichnis.isSelected();
        paramGeraetInBearbeitung.moduleTabletAbstimmung = cbModuleTabletabstimmung.isSelected();
        paramGeraetInBearbeitung.moduleBestandsverwaltung = cbModuleAnmeldestelle.isSelected();
        paramGeraetInBearbeitung.moduleHotline = cbModuleHotline.isSelected();
        paramGeraetInBearbeitung.moduleAktienregisterImport = cbModuleAktienregisterImport.isSelected();

        EclEmittenten lEmittent = comboFestgelegterMandant.getValue();
        if (lEmittent == null) {
            lEmittent = new EclEmittenten();
        }
        paramGeraetInBearbeitung.festgelegterMandant = lEmittent.mandant;
        hString = tfFestgelegtesJahr.getText();
        if (hString.isEmpty()) {
            paramGeraetInBearbeitung.festgelegtesJahr = 0;
        } else {
            paramGeraetInBearbeitung.festgelegtesJahr = Integer.parseInt(hString);
        }
        paramGeraetInBearbeitung.festgelegteHVNummer = tfFestgelegteHVNummer.getText().toUpperCase();
        paramGeraetInBearbeitung.festgelegteDatenbank = tfFestgelegteDatenbank.getText().toUpperCase();
        paramGeraetInBearbeitung.festgelegterBenutzername = tfFestgelegterBenutzername.getText();

        paramGeraetInBearbeitung.festgelegterMandantIstFix = cbFestgelegterMandantFix.isSelected();
        paramGeraetInBearbeitung.festgelegtesJahrIstFix = cbFestgelegtesJahrFix.isSelected();
        paramGeraetInBearbeitung.festgelegteHVNummerFix = cbFestgelegteHVNummerFix.isSelected();
        paramGeraetInBearbeitung.festgelegteDatenbankFix = cbFestgelegteDatenbankFix.isSelected();
        paramGeraetInBearbeitung.festgelegteBenutzernameFix = cbFestgelegterBenutzernameFix.isSelected();

        paramGeraetInBearbeitung.akkreditierungScanFeldFuerBestaetigungAktiv = cbAkkreditierungScanFeldFuerBestaetigungAktiv
                .isSelected();
        paramGeraetInBearbeitung.akkreditierungShortcutsAktiv = cbAkkreditierungShortcutsAktiv.isSelected();
        paramGeraetInBearbeitung.akkreditierungAnzeigeBelegeBuendeln = cbAkkreditierungAnzeigeBelegeBuendeln
                .isSelected();
        paramGeraetInBearbeitung.protokollAnzMaxZugaenge = Integer.parseInt(tfProtokollAnzMaxZugaenge.getText());
        paramGeraetInBearbeitung.protokollAnzMax = Integer.parseInt(tfProtokollAnzMax.getText());
        paramGeraetInBearbeitung.akkreditierungDelayIgnorieren = cbAkkreditierungDelayIgnorieren.isSelected();
        paramGeraetInBearbeitung.akkreditierungSammelkartenBuchenMoeglich = cbAkkreditierungSammelkartenBuchenMoeglich
                .isSelected();
        paramGeraetInBearbeitung.akkreditierungLabeldruckFuerAktionaer = cbAkkreditierungLabeldruckFuerAktionaer
                .isSelected();
        paramGeraetInBearbeitung.akkreditierungSuchfunktionAktiv = cbAkkreditierungSuchfunktionAktiv.isSelected();
        paramGeraetInBearbeitung.akkreditierungVertreterErfassungAktiv = cbVertreterErfassungAktiv.isSelected();
        paramGeraetInBearbeitung.vertreterKorrekturBeiKontrollerfassungMoeglich = cbVertreterKorrekturBeiKontrollerfassungMoeglich
                .isSelected();

        paramGeraetInBearbeitung.abstimmungTabletHochformat = cbAbstimmungTabletHochformat.isSelected();
        paramGeraetInBearbeitung.abstimmungTabletPersoenlichZugeordnet = cbAbstimmungTabletPersoenlichZugeordnet
                .isSelected();
        paramGeraetInBearbeitung.abstimmungTabletTestmodus = cbAbstimmungTabletTestmodus.isSelected();
        paramGeraetInBearbeitung.abtimmungTabletFullScreen = cbAbtimmungTabletFullScreen.isSelected();

        paramGeraetInBearbeitung.abstimmungTabletXSize = tfAbstimmungTabletXSize.getText();
        paramGeraetInBearbeitung.abstimmungTabletYSize = tfAbstimmungTabletYSize.getText();

        /*Parameter zurückspeichern*/
        lDbBundle.openAll();

        lDbBundle.dbParameter.updateGeraete_all(paramGeraetInBearbeitung);
        BvReload bvReload = new BvReload(lDbBundle);
        bvReload.setReloadParameterGeraete();

        lDbBundle.closeAll();

        setzePflegeModusAuf0();
    }

    /**
     * Loesche klasse in bearbeitung.
     */
    private void loescheKlasseInBearbeitung() {
        CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
        boolean brc = zeigeHinweis.zeige2Buttons(eigeneStage,
                "Soll Geräte-Klasse " + Integer.toString(eclGeraeteKlasseInBearbeitung.ident) + " "
                        + eclGeraeteKlasseInBearbeitung.kurzBeschreibung + " gelöscht werden?",
                "Ja", "Nein");
        if (brc == false) {
            return;
        }

        lDbBundle.openAll();

        /*Prüfen, ob Klasse noch Arbeitsplätze zugeordnet sind*/
        lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteKlasse_all(eclGeraeteKlasseInBearbeitung.ident);
        if (lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis() > 0) {
            zeigeHinweis.zeige(eigeneStage,
                    "Geräte-Set kann nicht gelöscht werden, da dem Set noch Geräte zugeordnet sind!");
            lDbBundle.closeAll();
            return;
        }

        int rc = lDbBundle.dbGeraeteKlasse.delete(eclGeraeteKlasseInBearbeitung.ident);
        lDbBundle.dbParameter.deleteGeraeteParameter_all(eclGeraeteKlasseInBearbeitung.ident);

        refreshUebersichtGeraeteKlassen();

        lDbBundle.closeAll();
        if (rc < 1) {
            zeigeHinweis.zeige(eigeneStage,
                    "Geräte-Klasse kann nicht gelöscht werden! Bitte Eingaben überprüfen / ggf. wiederholen!");
        }
        setzePflegeModusAuf0();

    }

    /**
     * Neue klasse vorbereiten.
     */
    private void neueKlasseVorbereiten() {

        String hEingabe = tfKlasse.getText();
        if (pruefeKlassenNrFormaleEingabe() == false) {
            return;
        }

        int hIdent = Integer.parseInt(hEingabe);
        lDbBundle.openAll();
        lDbBundle.dbGeraeteKlasse.read(hIdent);
        lDbBundle.closeAll();
        if (lDbBundle.dbGeraeteKlasse.anzErgebnis() > 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Geräte-Klasse bereits vorhanden!");
            tfKlasse.requestFocus();
            return;
        }

        eclGeraeteKlasseInBearbeitung = new EclGeraeteKlasse();
        eclGeraeteKlasseInBearbeitung.ident = hIdent;

        paramGeraetInBearbeitung = new ParamGeraet();
        paramGeraetInBearbeitung.identKlasse = hIdent;

        tfBezeichnung.setEditable(true);
        tfAusfuehrlicheBeschreibung.setEditable(true);

        tfKlasse.setEditable(false);

        pflegeModus = 1;
        btnSpeichern.setVisible(true);
        btnLoeschen.setVisible(false);
        btnAbbrechen.setVisible(true);

        btnBearbeiten.setVisible(false);
        btnNeueKlasse.setVisible(false);

        tfBezeichnung.requestFocus();
        anzeigenKlasseZurBearbeitung();

    }

    /**
     * Refresh geraete sets.
     */
    private void refresh_GeraeteSets() {

        /*Alle Gerätesets einlesen*/
        lDbBundle.dbGeraeteSet.read_all();
        int anzGeraeteSets = lDbBundle.dbGeraeteSet.anzErgebnis();
        arrayGeraeteSet = null;
        if (anzGeraeteSets > 0) {
            arrayGeraeteSet = lDbBundle.dbGeraeteSet.ergebnisArray;
        }

        /*Aktives Geräteset einlesen*/
        EclParameter lParameter = new EclParameter();
        lParameter.mandant = 0;
        lParameter.ident = 51;
        lDbBundle.dbParameter.readServer(lParameter);
        if (lDbBundle.dbParameter.anzErgebnis() > 0) {
            aktivesSet = Integer.parseInt(lDbBundle.dbParameter.ergebnisPosition(0).wert);
        }

        StringConverter<EclGeraeteSet> sc = new StringConverter<EclGeraeteSet>() {
            @Override
            public String toString(EclGeraeteSet pGeraeteSet) {
                return Integer.toString(pGeraeteSet.ident) + " " + pGeraeteSet.kurzBeschreibung;
            }

            @Override
            public EclGeraeteSet fromString(String string) {
                return null;
            }
        };
        comboSet.setConverter(sc);
        comboSet.getSelectionModel().clearSelection();
        comboSet.getItems().clear();
        /*"Alle Klassen" als Auswahlmöglichkeit hinzufügen und ggf. aktivieren*/
        EclGeraeteSet lGeraeteSet = new EclGeraeteSet();
        lGeraeteSet.ident = 0;
        lGeraeteSet.kurzBeschreibung = "Alle Klassen";
        comboSet.getItems().add(lGeraeteSet);
        if (aktivesSet == 0) {
            comboSet.setValue(lGeraeteSet);
        }
        /*Sets hinzufügen*/
        if (arrayGeraeteSet != null) {
            for (int i = 0; i < arrayGeraeteSet.length; i++) {
                comboSet.getItems().add(arrayGeraeteSet[i]);
                if (aktivesSet == arrayGeraeteSet[i].ident) {
                    comboSet.setValue(arrayGeraeteSet[i]);
                }
            }
        }

    }

    /**
     * Refresh uebersicht geraete klassen.
     */
    private void refreshUebersichtGeraeteKlassen() {
        lDbBundle.dbGeraeteKlasse.read_all();
        int anzGeraeteKlassen = lDbBundle.dbGeraeteKlasse.anzErgebnis();
        scrpnVorhandeneKlassen.setContent(null);
        klassenZuAusgewaehltemSet = null;

        if (anzGeraeteKlassen > 0) {
            GridPane grpnGeraeteKlassen = new GridPane();
            grpnGeraeteKlassen.setVgap(5);
            grpnGeraeteKlassen.setHgap(5);

            for (int i = 0; i < anzGeraeteKlassen; i++) {
                EclGeraeteKlasse lGeraeteKlasse = lDbBundle.dbGeraeteKlasse.ergebnisPosition(i);
                Label lIdent = new Label();
                lIdent.setText(Integer.toString(lGeraeteKlasse.ident));
                grpnGeraeteKlassen.add(lIdent, 0, i);
                Label lKurzBeschreibung = new Label();
                lKurzBeschreibung.setText(lGeraeteKlasse.kurzBeschreibung);
                grpnGeraeteKlassen.add(lKurzBeschreibung, 1, i);
                Label lBeschreibung = new Label();
                lBeschreibung.setText(lGeraeteKlasse.beschreibung);
                grpnGeraeteKlassen.add(lBeschreibung, 2, i);
            }
            scrpnVorhandeneKlassen.setContent(grpnGeraeteKlassen);

        }

        /*Nun die Klassen des zum Blättern ausgewählten Sets einlesen*/
        int ausgewaehltesSet = comboSet.getValue().ident;
        if (ausgewaehltesSet == 0) {
            if (anzGeraeteKlassen > 0) {
                klassenZuAusgewaehltemSet = new int[anzGeraeteKlassen];
                for (int i = 0; i < anzGeraeteKlassen; i++) {
                    klassenZuAusgewaehltemSet[i] = lDbBundle.dbGeraeteKlasse.ergebnisPosition(i).ident;
                }
            }
        } else {
            lDbBundle.dbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all_nurKlasseIdentUnique(ausgewaehltesSet);
            int anz = lDbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis();
            if (anz > 0) {
                klassenZuAusgewaehltemSet = new int[anz];
                for (int i = 0; i < anz; i++) {
                    klassenZuAusgewaehltemSet[i] = lDbBundle.dbGeraeteKlasseSetZuordnung
                            .ergebnisPosition(i).geraeteKlasseIdent;
                }
            }
        }
    }

    /**
     * ************************Anzeigefunktionen**************************************.
     */

    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /**
     * Setze pflege modus auf 0.
     */
    private void setzePflegeModusAuf0() {

        tfKlasse.setEditable(true);
        tfKlasse.setText("");

        btnBearbeiten.setVisible(true);
        btnNeueKlasse.setVisible(true);

        tfBezeichnung.setEditable(false);
        tfBezeichnung.setText("");

        tfAusfuehrlicheBeschreibung.setEditable(false);
        tfAusfuehrlicheBeschreibung.setText("");

        btnSpeichern.setVisible(false);
        btnLoeschen.setVisible(false);
        btnAbbrechen.setVisible(false);

        pflegeModus = 0;
        tfKlasse.requestFocus();

        /*Nun Detailfelder auf "leer" setzen*/
        tfAbstimmungTabletTyp.setText("");
        tfLwPfadAllgemein.setText("");
        tfLwPfadGrossdokumente.setText("");
        tfLwPfadSicherung1.setText("");
        tfLwPfadSicherung2.setText("");
        tfLwPfadExportFuerPraesentation.setText("");
        tfLwPfadExportFuerBuehnensystem.setText("");
        tfLwPfadExportExcelFuerPowerpoint.setText("");
        tfLwPfadKundenordnerBasis.setText("");
        cbBonDruckerIstZugeordnet.setSelected(false);
        cbKontrollScreenAktiv.setSelected(false);
        cbVorrangigDB.setSelected(false);

        cbModuleHVMaster.setSelected(false);
        cbModuleDesigner.setSelected(false);
        cbModuleFrontOffice.setSelected(false);
        cbModuleKontrolle.setSelected(false);
        cbModuleServiceDesk.setSelected(false);
        cbModuleTeilnahmeverzeichnis.setSelected(false);
        cbModuleTabletabstimmung.setSelected(false);
        cbModuleAnmeldestelle.setSelected(false);
        cbModuleHotline.setSelected(false);
        cbModuleAktienregisterImport.setSelected(false);

        comboFestgelegterMandant.getSelectionModel().clearSelection();
        comboFestgelegterMandant.getItems().clear();
        tfFestgelegtesJahr.setText("");
        tfFestgelegteHVNummer.setText("");
        tfFestgelegteDatenbank.setText("");
        tfFestgelegterBenutzername.setText("");

        cbAkkreditierungScanFeldFuerBestaetigungAktiv.setSelected(false);
        cbAkkreditierungShortcutsAktiv.setSelected(false);
        cbAkkreditierungAnzeigeBelegeBuendeln.setSelected(false);
        tfProtokollAnzMaxZugaenge.setText("");
        tfProtokollAnzMax.setText("");
        cbAkkreditierungDelayIgnorieren.setSelected(false);
        cbAkkreditierungSammelkartenBuchenMoeglich.setSelected(false);
        cbAkkreditierungLabeldruckFuerAktionaer.setSelected(false);
        cbAkkreditierungSuchfunktionAktiv.setSelected(false);
        cbVertreterErfassungAktiv.setSelected(false);
        cbVertreterKorrekturBeiKontrollerfassungMoeglich.setSelected(false);

        cbAbstimmungTabletHochformat.setSelected(false);
        cbAbstimmungTabletPersoenlichZugeordnet.setSelected(false);
        cbAbstimmungTabletTestmodus.setSelected(false);
        cbAbtimmungTabletFullScreen.setSelected(false);

        tfAbstimmungTabletXSize.setText("");
        tfAbstimmungTabletYSize.setText("");

        /*Nun Detailfelder auf "nicht bearbeitbar" setzen*/
        tfAbstimmungTabletTyp.setEditable(false);
        tfLwPfadAllgemein.setEditable(false);
        tfLwPfadGrossdokumente.setEditable(false);
        tfLwPfadSicherung1.setEditable(false);
        tfLwPfadSicherung2.setEditable(false);
        tfLwPfadExportFuerPraesentation.setEditable(false);
        tfLwPfadExportFuerBuehnensystem.setEditable(false);
        tfLwPfadExportExcelFuerPowerpoint.setEditable(false);
        tfLwPfadKundenordnerBasis.setEditable(false);
        cbBonDruckerIstZugeordnet.setDisable(true);
        cbKontrollScreenAktiv.setDisable(true);
        cbVorrangigDB.setDisable(true);

        cbModuleHVMaster.setDisable(true);
        cbModuleDesigner.setDisable(true);
        cbModuleFrontOffice.setDisable(true);
        cbModuleKontrolle.setDisable(true);
        cbModuleServiceDesk.setDisable(true);
        cbModuleTeilnahmeverzeichnis.setDisable(true);
        cbModuleTabletabstimmung.setDisable(true);
        cbModuleAnmeldestelle.setDisable(true);
        cbModuleHotline.setDisable(true);
        cbModuleAktienregisterImport.setDisable(true);

        comboFestgelegterMandant.getSelectionModel().clearSelection();
        comboFestgelegterMandant.getItems().clear();
        tfFestgelegtesJahr.setEditable(false);
        tfFestgelegteHVNummer.setEditable(false);
        tfFestgelegteDatenbank.setEditable(false);
        tfFestgelegterBenutzername.setEditable(false);

        cbAkkreditierungScanFeldFuerBestaetigungAktiv.setDisable(true);
        cbAkkreditierungShortcutsAktiv.setDisable(true);
        cbAkkreditierungAnzeigeBelegeBuendeln.setDisable(true);
        tfProtokollAnzMaxZugaenge.setEditable(false);
        tfProtokollAnzMax.setEditable(false);
        cbAkkreditierungDelayIgnorieren.setDisable(true);
        cbAkkreditierungSammelkartenBuchenMoeglich.setDisable(true);
        cbAkkreditierungLabeldruckFuerAktionaer.setDisable(true);
        cbAkkreditierungSuchfunktionAktiv.setDisable(true);
        cbVertreterErfassungAktiv.setDisable(true);
        cbVertreterKorrekturBeiKontrollerfassungMoeglich.setDisable(true);

        cbAbstimmungTabletHochformat.setDisable(true);
        cbAbstimmungTabletPersoenlichZugeordnet.setDisable(true);
        cbAbstimmungTabletTestmodus.setDisable(true);
        cbAbtimmungTabletFullScreen.setSelected(true);

        tfAbstimmungTabletXSize.setEditable(false);
        tfAbstimmungTabletYSize.setEditable(false);
    }

    /**
     * ******************Aktionen auf Oberfläche************************.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {
        setInBearbeitungSpeichern();
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
     * On key pressed set nummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedSetNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && pflegeModus == 0) {
            einlesenAnzeigenKlasseZurBearbeitungAusFeld();
        }

    }

    /**
     * Clicked bearbeiten.
     *
     * @param event the event
     */
    @FXML
    void clickedBearbeiten(ActionEvent event) {
        einlesenAnzeigenKlasseZurBearbeitungAusFeld();
    }

    /**
     * Clicked loeschen.
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschen(ActionEvent event) {
        loescheKlasseInBearbeitung();
    }

    /**
     * Clicked neue klasse.
     *
     * @param event the event
     */
    @FXML
    void clickedNeueKlasse(ActionEvent event) {
        neueKlasseVorbereiten();
    }

    /**
     * Clicked vor.
     *
     * @param event the event
     */
    @FXML
    void clickedVor(ActionEvent event) {
        if (klassenZuAusgewaehltemSet == null || klassenZuAusgewaehltemSet.length == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Keine Klassen zum Blättern!");
        }
        int startKlasse = 0;
        if (pflegeModus != 0) {
            setInBearbeitungSpeichern();
            startKlasse = eclGeraeteKlasseInBearbeitung.ident;
        }
        int gef = 0;
        for (int i = 0; i < klassenZuAusgewaehltemSet.length; i++) {
            if (gef == 0) {/*Noch nichts gefunden*/
                if (klassenZuAusgewaehltemSet[i] > startKlasse) {
                    gef = klassenZuAusgewaehltemSet[i];
                }
            } else {/*Bereits was gefunden - was niedrigeres?*/
                if (klassenZuAusgewaehltemSet[i] > startKlasse && klassenZuAusgewaehltemSet[i] < gef) {
                    gef = klassenZuAusgewaehltemSet[i];
                }
            }
        }
        if (gef == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Keine Klassen zum Blättern!");
            return;
        }
        einlesenAnzeigenKlasseZurBearbeitung(gef);

    }

    /**
     * Clicked zurueck.
     *
     * @param event the event
     */
    @FXML
    void clickedZurueck(ActionEvent event) {
        if (klassenZuAusgewaehltemSet == null || klassenZuAusgewaehltemSet.length == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Keine Klassen zum Blättern!");
        }
        int startKlasse = 0;
        if (pflegeModus != 0) {
            setInBearbeitungSpeichern();
            startKlasse = eclGeraeteKlasseInBearbeitung.ident;
        }
        int gef = 0;
        for (int i = 0; i < klassenZuAusgewaehltemSet.length; i++) {
            if (gef == 0) {/*Noch nichts gefunden*/
                if (klassenZuAusgewaehltemSet[i] < startKlasse) {
                    gef = klassenZuAusgewaehltemSet[i];
                }
            } else {/*Bereits was gefunden - was niedrigeres?*/
                if (klassenZuAusgewaehltemSet[i] < startKlasse && klassenZuAusgewaehltemSet[i] > gef) {
                    gef = klassenZuAusgewaehltemSet[i];
                }
            }
        }
        if (gef == 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Keine Klassen zum Blättern!");
            return;
        }
        einlesenAnzeigenKlasseZurBearbeitung(gef);

    }

}
