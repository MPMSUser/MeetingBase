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

import de.meetingapps.meetingclient.meetingClientAllg.CALabelPrint;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktionaerWechselGast;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzSummen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLabelPrint;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstAutoDrucker;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * The Class CtrlDivAktionaer.
 */
public class CtrlDivAktionaer extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf stimmkartennummer. */
    @FXML
    private TextField tfStimmkartennummer;

    /** The tf fehlermeldung. */
    @FXML
    private TextArea tfFehlermeldung;

    /** The tf name vorname. */
    @FXML
    private TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The btn einlesen. */
    @FXML
    private Button btnEinlesen;

    /** The tf zuletzt bearbeitet. */
    @FXML
    private TextField tfZuletztBearbeitet;

    /** The btn label druck. */
    @FXML
    private Button btnLabelDruck;

    /** The btn zugangsformular druck. */
    @FXML
    private Button btnZugangsformularDruck;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The grd pn anzeige. */
    @FXML
    private GridPane grdPnAnzeige;

    /** The btn storno ausfuehren. */
    @FXML
    private Button btnStornoAusfuehren;

    /** The btn idents aktionaer zu gast. */
    @FXML
    private Button btnIdentsAktionaerZuGast;

    /** The btn idents gast zu aktonaer. */
    @FXML
    private Button btnIdentsGastZuAktonaer;

    /** The rb stimmkarten nr. */
    @FXML
    private RadioButton rbStimmkartenNr;

    /** The tg karten nr. */
    @FXML
    private ToggleGroup tgKartenNr;

    /** The rb eintrittskarten nr. */
    @FXML
    private RadioButton rbEintrittskartenNr;

    /** The tf vertreter name. */
    @FXML
    private TextField tfVertreterName;

    /** The tf vertreter vorname. */
    @FXML
    private TextField tfVertreterVorname;

    /** The tf vertreter ort. */
    @FXML
    private TextField tfVertreterOrt;

    /** The btn speichern vertreter. */
    @FXML
    private Button btnSpeichernVertreter;

    /** Labeldaten. */
    String lekNummer = "";

    /** The lsk nummer. */
    String lskNummer = "";

    /** The lsk nummer bar. */
    String lskNummerBar = "";

    /** The lvorname aktionaer. */
    String lvornameAktionaer = "";

    /** The lnachname aktionaer. */
    String lnachnameAktionaer = "";

    /** The lort aktionaer. */
    String lortAktionaer = "";

    /** The lvorname vertreter. */
    String lvornameVertreter = "";

    /** The lname vertreter. */
    String lnameVertreter = "";

    /** The lort vertreter. */
    String lortVertreter = "";

    /** The laktienzahl. */
    String laktienzahl = "";

    /** ****************Individuelle Anfang****************. */

    private int meldungsIdent = 0;

    /** The stimmkartennummer. */
    private String stimmkartennummer = "";

    /** The eintrittskartennummer. */
    private String eintrittskartennummer = "";

    /** The zutritts ident. */
    private EclZutrittsIdent zutrittsIdent = null;

    /** Nummer der Person, die die verwendete Stimmkarte verwendet. */
    int personNatJurIdent = 0;

    /** The ecl meldung. */
    EclMeldung eclMeldung = null;

    /** The vertreter name. */
    private String vertreterName = "";

    /** The vertreter vorname. */
    private String vertreterVorname = "";

    /** The vertreter ort. */
    private String vertreterOrt = "";

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /**
     * ***************Individuell Ende***********************.
     */

    @FXML
    void initialize() {
        assert tfStimmkartennummer != null : "fx:id=\"tfStimmkartennummer\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfFehlermeldung != null : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfNameVorname != null : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnEinlesen != null : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfZuletztBearbeitet != null : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnLabelDruck != null : "fx:id=\"btnLabelDruck\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert grdPnAnzeige != null : "fx:id=\"grdPnAnzeige\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnStornoAusfuehren != null : "fx:id=\"btnStornoAusfuehren\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnIdentsAktionaerZuGast != null : "fx:id=\"btnIdentsAktionaerZuGast\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnIdentsGastZuAktonaer != null : "fx:id=\"btnIdentsGastZuAktonaer\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert rbStimmkartenNr != null : "fx:id=\"rbStimmkartenNr\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tgKartenNr != null : "fx:id=\"tgKartenNr\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert rbEintrittskartenNr != null : "fx:id=\"rbEintrittskartenNr\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfVertreterName != null : "fx:id=\"tfVertreterName\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfVertreterVorname != null : "fx:id=\"tfVertreterVorname\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert tfVertreterOrt != null : "fx:id=\"tfVertreterOrt\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        assert btnSpeichernVertreter != null : "fx:id=\"btnSpeichernVertreter\" was not injected: check your FXML file 'DivAktionaer.fxml'.";
        

        /*************** Ab hier individuell **********************************/
        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        setzeStatusAktionaersnummerEingeben();
        lDbBundle = new DbBundle();

        if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdStimmkarte) {
            rbEintrittskartenNr.setSelected(true);
        } else {
            rbStimmkartenNr.setSelected(true);
        }
    }

    /** The l willenserklaerung array. */
    EclWillenserklaerung[] lWillenserklaerungArray = null;

    /**
     * On btn storno ausfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnStornoAusfuehren(ActionEvent event) {
        lDbBundle.openAll();

        /*TODO #Konsolidierung: Storno: Visionen im Hinblick auf App usw. mit Personenwechsel usw. nicht berücksichtigt*/

        /*Willenserklärungen stornieren*/
        int anzahl = lWillenserklaerungArray.length;

        for (int i = 0; i < anzahl; i++) {
            EclWillenserklaerung lWillenserklaerung = lWillenserklaerungArray[i];
            lDbBundle.dbWillenserklaerung.delete(lWillenserklaerung);

            /*Aus elektronischem Teilnehmerverzeichnis löschen*/
            lDbBundle.dbPraesenzliste.delete(lWillenserklaerung);
        }

        /*EclMeldung korrigieren (statusPraesenz, statusWarPraesenz, stimmkarte, dito delayed*/
        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = meldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
        eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        int istPraesent = eclMeldung.statusPraesenz;
        long stimmen = eclMeldung.stimmen;

        eclMeldung.statusPraesenz = 0;
        eclMeldung.statusPraesenz_Delayed = 0;
        eclMeldung.stimmkarte = "";
        eclMeldung.stimmkarte_Delayed = "";
        eclMeldung.statusWarPraesenz = 0;
        eclMeldung.statusWarPraesenz_Delayed = 0;

        lDbBundle.dbMeldungen.update(eclMeldung);

        /*Präsenzsumme korrigieren, falls bisher präsent*/
        if (istPraesent == 1) {
            BlPraesenzSummen blPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            blPraesenzSummen.abgang(stimmen, eclMeldung.gattung, 0);
        }

        /*Stimmkarte Eintrag löschen*/
        lDbBundle.dbStimmkarten.readZuMeldungsIdent(meldungsIdent);
        int anzStimmkarten = lDbBundle.dbStimmkarten.anzErgebnis();
        if (anzStimmkarten > 0) {
            for (int i = 0; i < anzStimmkarten; i++) {
                EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(i);
                lDbBundle.dbStimmkarten.delete(eclStimmkarte);
            }
        }

        /*Aus ZutrittsIdent: Personenzuordnung raushaun ...*/
        lDbBundle.dbZutrittskarten.readZuMeldungsIdent(meldungsIdent);
        int anzEintrittskarten = lDbBundle.dbZutrittskarten.anzErgebnis();
        if (anzEintrittskarten > 0) {
            for (int i = 0; i < anzEintrittskarten; i++) {
                EclZutrittskarten eclZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(i);
                eclZutrittskarte.personenNatJurIdent = 0;
                lDbBundle.dbZutrittskarten.update(eclZutrittskarte);
            }
        }

        lDbBundle.closeAll();
        grdPnAnzeige.getChildren().clear();

    }

    /**
     * On key aktionaersnummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyAktionaersnummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doEinlesen();
        }
    }

    /**
     * Clicked einlesen.
     *
     * @param event the event
     */
    @FXML
    void clickedEinlesen(ActionEvent event) {
        doEinlesen();
    }

    /**
     * On btn speichern vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichernVertreter(ActionEvent event) {
        lDbBundle.openAll();

        vertreterName = tfVertreterName.getText();
        vertreterVorname = tfVertreterVorname.getText();
        vertreterOrt = tfVertreterOrt.getText();

        /*Meldung Updaten*/
        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = meldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
        eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        eclMeldung.vertreterName = vertreterName;
        eclMeldung.vertreterName_Delayed = vertreterName;
        eclMeldung.vertreterVorname = vertreterVorname;
        eclMeldung.vertreterVorname_Delayed = vertreterVorname;
        eclMeldung.vertreterOrt = vertreterOrt;
        eclMeldung.vertreterOrt_Delayed = vertreterOrt;

        lDbBundle.dbMeldungen.update(eclMeldung);

        /*PersonNatJur*/
        int vertreterIdent = eclMeldung.vertreterIdent;
        lDbBundle.dbPersonenNatJur.read(vertreterIdent);
        EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
        lPersonNatJur.name = vertreterName;
        lPersonNatJur.vorname = vertreterVorname;
        lPersonNatJur.ort = vertreterOrt;
        lDbBundle.dbPersonenNatJur.update(lPersonNatJur);

        /*Elektronisches Teilnehmerverzeichnis*/
        int anzahl = lWillenserklaerungArray.length;
        for (int i = 0; i < anzahl; i++) {
            System.out.println("i=" + i);
            EclWillenserklaerung lWillenserklaerung = lWillenserklaerungArray[i];
            if (/*lWillenserklaerung.willenserklaerung==KonstWillenserklaerung.vollmachtAnDritte &&*/
            lWillenserklaerung.bevollmaechtigterDritterIdent == vertreterIdent) {
                System.out.println("in If");
                /*In elektronischem Teilnehmerverzeichnis korrigieren*/
                EclPraesenzliste lPraesenzliste = new EclPraesenzliste();
                lPraesenzliste.willenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;
                lDbBundle.dbPraesenzliste.read(lPraesenzliste);
                lPraesenzliste = lDbBundle.dbPraesenzliste.ergebnisPosition(0);
                lPraesenzliste.vertreterName = vertreterName;
                lPraesenzliste.vertreterVorname = vertreterVorname;
                lPraesenzliste.vertreterOrt = vertreterOrt;
                lDbBundle.dbPraesenzliste.update(lPraesenzliste);
            }
        }

        lDbBundle.closeAll();
        tfFehlermeldung.setText("Vertreter gespeichert");
    }

    /**
     * Do einlesen.
     */
    private void doEinlesen() {

        lDbBundle.openAll();

        boolean rc = einlesen();
        lDbBundle.closeAll();

        if (rc == false) {
            tfFehlermeldung.setText(fehlerMeldungSpeichern);
            setzeStatusAbbruch();
            return;
        } else {
            tfFehlermeldung.setText("");
            setzeStatusVerarbeitung();
        }
    }

    /**
     * Einlesen.
     *
     * @return true, if successful
     */
    private boolean einlesen() {

        if (rbStimmkartenNr.isSelected()) {
            /*Stimmkartennummer einlesen*/
            eintrittskartennummer = "";
            stimmkartennummer = tfStimmkartennummer.getText().trim();

            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            int erg = blNummernformen.dekodiere(stimmkartennummer, KonstKartenklasse.stimmkartennummer);
            if (erg < 0) {
                fehlerMeldungSpeichern = CaFehler.getFehlertext(erg, 0) + ": Bitte gültige Stimmkartennummer eingeben!";
                return false;
            }
            stimmkartennummer = blNummernformen.rcIdentifikationsnummer.get(0);

            lDbBundle.dbStimmkarten.read(stimmkartennummer);
            if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
                fehlerMeldungSpeichern = "Stimmkartennummer nicht vorhanden!";
                return false;
            }
            EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
            if (eclStimmkarte.stimmkarteIstGesperrt == 2) {
                fehlerMeldungSpeichern = "Stimmkartennummer gesperrt!";
                return false;
            }
            personNatJurIdent = eclStimmkarte.personenNatJurIdent;
            meldungsIdent = eclStimmkarte.meldungsIdentAktionaer;
        } else {//Eintrittskartennummer einlesen
            stimmkartennummer = "";
            eintrittskartennummer = tfStimmkartennummer.getText().trim();

            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            int erg = blNummernformen.dekodiere(eintrittskartennummer, KonstKartenklasse.eintrittskartennummer);
            if (erg < 0) {
                fehlerMeldungSpeichern = CaFehler.getFehlertext(erg, 0)
                        + ": Bitte gültige Eintrittskartennummer eingeben!";
                return false;
            }
            zutrittsIdent = new EclZutrittsIdent();
            zutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
            zutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);

            lDbBundle.dbZutrittskarten.read(zutrittsIdent, 1);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
                fehlerMeldungSpeichern = "Eintrittskartennummer nicht vorhanden!";
                return false;
            }
            EclZutrittskarten eclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            if (eclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                fehlerMeldungSpeichern = "Eintrittskartennummer gesperrt!";
                return false;
            }
            personNatJurIdent = eclZutrittskarten.personenNatJurIdent;
            meldungsIdent = eclZutrittskarten.meldungsIdentAktionaer;
        }

        eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = meldungsIdent;

        lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
        eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        tfNameVorname.setText(eclMeldung.name + " " + eclMeldung.vorname);
        tfOrt.setText(eclMeldung.ort);
        tfAktien.setText(CaString.toStringDE(eclMeldung.stimmen));

        vertreterName = eclMeldung.vertreterName;
        tfVertreterName.setText(vertreterName);
        vertreterVorname = eclMeldung.vertreterVorname;
        tfVertreterVorname.setText(vertreterVorname);
        vertreterOrt = eclMeldung.vertreterOrt;
        tfVertreterOrt.setText(vertreterOrt);

        /*Noch Willenserklärungen einlesen*/
        /*Einlesen von:
         * erstzugang, abgang, wiederzugang, vertreterwechsel, wechslenInSRV, wechselInOrga/Dauer/KIAV
         * 
         * Achtung: Storniert werden darf nur, wenn nur erstzugang, abgang, wiederzugang, vertreterwechsel vorhanden sind!
         */
        int anzahl = lDbBundle.dbWillenserklaerung.lesePraesenzZuIdent(meldungsIdent);
        lWillenserklaerungArray = lDbBundle.dbWillenserklaerung.willenserklaerungArray;

        for (int i = 0; i < anzahl; i++) {
            Label label1 = new Label();
            label1.setText(Integer.toString(lWillenserklaerungArray[i].willenserklaerungIdent));
            grdPnAnzeige.add(label1, 0, i + 1);

            Label label2 = new Label();
            label2.setText(KonstWillenserklaerung.getText(lWillenserklaerungArray[i].willenserklaerung));
            grdPnAnzeige.add(label2, 1, i + 1);

            Label label3 = new Label();
            label3.setText(lWillenserklaerungArray[i].stimmkarte1);
            grdPnAnzeige.add(label3, 2, i + 1);

            Label label4 = new Label();
            label4.setText(lWillenserklaerungArray[i].zutrittsIdent);
            grdPnAnzeige.add(label4, 3, i + 1);

            Label label5 = new Label();
            label5.setText(Integer.toString(lWillenserklaerungArray[i].zuVerzeichnisNr1Gedruckt));
            grdPnAnzeige.add(label5, 4, i + 1);

            Label label6 = new Label();
            label6.setText(Integer.toString(lWillenserklaerungArray[i].arbeitsplatz));
            grdPnAnzeige.add(label6, 5, i + 1);

            Label label7 = new Label();
            label7.setText(lWillenserklaerungArray[i].veraenderungszeit);
            grdPnAnzeige.add(label7, 6, i + 1);

        }

        return true;
    }

    /**
     * Clicked beenden.
     *
     * @param event the event
     */
    @FXML
    void clickedBeenden(ActionEvent event) {
        setzeStatusAktionaersnummerEingeben();
    }

    /**
     * Clicked idents aktionaer zu gast.
     *
     * @param event the event
     */
    @FXML
    void clickedIdentsAktionaerZuGast(ActionEvent event) {
        lDbBundle.openAll();
        BlAktionaerWechselGast blAktionaerWechselGast = new BlAktionaerWechselGast(lDbBundle);
        blAktionaerWechselGast.nichtAnwesenderAktionaerZuGast(eclMeldung, personNatJurIdent);
        lDbBundle.closeAll();
    }

    /**
     * Clicked idents gast zu aktonaer.
     *
     * @param event the event
     */
    @FXML
    void clickedIdentsGastZuAktonaer(ActionEvent event) {
        lDbBundle.openAll();
        BlAktionaerWechselGast blAktionaerWechselGast = new BlAktionaerWechselGast(lDbBundle);
        blAktionaerWechselGast.nichtAnwesenderGastZuAktionaer(eclMeldung, personNatJurIdent);
        lDbBundle.closeAll();

    }

    /**
     * Clicked label druck.
     *
     * @param event the event
     */
    @FXML
    void clickedLabelDruck(ActionEvent event) {

        BlNummernformen blNummernform = new BlNummernformen(lDbBundle);

        /*Nun Label ausdrucken - erst mal technisch reingebaut*/
        lekNummer = "Eintrittskarten Nr. " + eclMeldung.zutrittsIdent;
        lskNummer = "Stimmkarten Nr. " + eclMeldung.stimmkarte;
        
        blNummernform.rcStimmkarteSubNummernkreis = 0;
        if (eclMeldung.stimmkarte.isBlank()==false) {
            lskNummerBar = blNummernform.formatiereNrKomplett(
                    eclMeldung.stimmkarte, "",
                KonstKartenklasse.stimmkartennummer, KonstKartenart.abgang);
        }
        else {
            lskNummerBar="";
        }
        
//        lskNummerBar = blNummernform.formatiereEKNr(eclMeldung.stimmkarte);
//        lskNummerBar = "4" + lskNummerBar + "115110";

        lvornameAktionaer = eclMeldung.vorname;
        lnachnameAktionaer = eclMeldung.name;
        lortAktionaer = eclMeldung.ort;

        lvornameVertreter = eclMeldung.vertreterVorname;
        lnameVertreter = eclMeldung.vertreterName;
        lortVertreter = eclMeldung.vertreterOrt;
        laktienzahl = eclMeldung.stimmen + " Aktie(n) " + eclMeldung.besitzart;

        if (!ParamS.paramGeraet.labelDruckerIPAdresse.equals("0")) {

            EclLabelPrint datenLabelPrint=new EclLabelPrint();
            
            datenLabelPrint.ekNummer = "Eintrittskarten Nr. " + lekNummer;
            datenLabelPrint.skNummer = "Stimmkarten Nr. " +lskNummer;
            datenLabelPrint.skNummerBar=lskNummerBar;
            
            datenLabelPrint.vornameAktionaer = lvornameAktionaer;
            datenLabelPrint.nachnameAktionaer = lnachnameAktionaer;
            datenLabelPrint.ortAktionaer = lortAktionaer;

            datenLabelPrint.vornameVertreter = lvornameVertreter;
            datenLabelPrint.nameVertreter = lnameVertreter;
            datenLabelPrint.ortVertreter = lortVertreter;
            datenLabelPrint.aktienzahl = laktienzahl;

            CALabelPrint caLabelPrint = new CALabelPrint();
            caLabelPrint.druckenAktionaer(datenLabelPrint);

        }

    }

    /**
     * Clicked zugangsformular druck.
     *
     * @param event the event
     */
    @FXML
    void clickedZugangsformularDruck(ActionEvent event) {

        if (ParamS.param.paramAkkreditierung.formularNachErstzugang == 0) {
            return;
        }

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        String kennung = eclMeldung.aktionaersnummer;
        lDbBundle.dbLoginDaten.read_loginKennung(kennung);
        String initialPasswort = "";
        if (lDbBundle.dbLoginDaten.anzErgebnis() > 0) {
            initialPasswort = lDbBundle.dbLoginDaten.ergebnisPosition(0).lieferePasswortInitialClean();
        }

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.setzeAutoDrucker(
                ParamS.param.paramBasis.autoDruckerVerwendung[KonstAutoDrucker.FORMULAR_BEI_ERSTZUGANG]);

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.formularErstzugang("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz", eclMeldung.zutrittsIdent);

        rpVariablen.fuelleVariable(rpDrucken, "Zugangsdaten.Kennung", eclMeldung.aktionaersnummer);
        rpVariablen.fuelleVariable(rpDrucken, "Zugangsdaten.Initialpasswort", initialPasswort);

        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", eclMeldung.name);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", eclMeldung.vorname);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", eclMeldung.ort);
        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Gattung", Integer.toString(eclMeldung.gattung));

        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", eclMeldung.vertreterName);
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", eclMeldung.vertreterVorname);
        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", eclMeldung.vertreterOrt);

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        lDbBundle.closeAll();

    }

    /** The fehler meldung speichern. */
    private String fehlerMeldungSpeichern = "";

    /**
     * Setze status aktionaersnummer eingeben.
     */
    private void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnBeenden.setDisable(true);
        tfStimmkartennummer.setText("");
        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        tfFehlermeldung.setText("");
        tfStimmkartennummer.setEditable(true);
        tfStimmkartennummer.requestFocus();
        btnLabelDruck.setDisable(true);
        btnZugangsformularDruck.setDisable(true);
        btnIdentsAktionaerZuGast.setDisable(true);
        btnIdentsGastZuAktonaer.setDisable(true);

        tfVertreterName.setText("");
        tfVertreterName.setEditable(false);
        tfVertreterVorname.setText("");
        tfVertreterVorname.setEditable(false);
        tfVertreterOrt.setText("");
        tfVertreterOrt.setEditable(false);

        btnSpeichernVertreter.setDisable(true);
        btnStornoAusfuehren.setDisable(true);

        grdPnAnzeige.getChildren().clear();

    }

    /**
     * Setze status verarbeitung.
     */
    private void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnBeenden.setDisable(false);
        tfStimmkartennummer.setEditable(false);
        btnBeenden.requestFocus();
        btnLabelDruck.setDisable(false);
        btnZugangsformularDruck.setDisable(false);
        btnIdentsAktionaerZuGast.setDisable(false);
        btnIdentsGastZuAktonaer.setDisable(false);

        btnStornoAusfuehren.setDisable(false);

        if (!vertreterName.isEmpty()) {
            btnSpeichernVertreter.setDisable(false);
            tfVertreterName.setEditable(true);
            tfVertreterVorname.setEditable(true);
            tfVertreterOrt.setEditable(true);
        }

    }

    /**
     * Setze status abbruch.
     */
    private void setzeStatusAbbruch() {
        btnEinlesen.setDisable(true);
        btnBeenden.setDisable(false);
        tfStimmkartennummer.setEditable(false);
        btnBeenden.requestFocus();

    }
}
