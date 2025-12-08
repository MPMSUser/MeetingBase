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

import java.io.IOException;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {
    
    @FXML
    private AnchorPane rootPane;

    /** The tf scan EK. */
    @FXML
    private TextField tfScanEK;

    /** The tf scan SK. */
    @FXML
    private TextField tfScanSK;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The lbl scan SK. */
    @FXML
    private Label lblScanSK;

    /** The lbl richtig. */
    @FXML
    private Label lblRichtig;

    /** The lbl anzeige SK. */
    @FXML
    private Label lblAnzeigeSK;

    /** The tf anzeige EK. */
    @FXML
    private TextField tfAnzeigeEK;

    /** The tf anzeige SK. */
    @FXML
    private TextField tfAnzeigeSK;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The lbl fehler meldung. */
    @FXML
    private Label lblFehlerMeldung;

    /** The btn offene positionen. */
    @FXML
    private Button btnOffenePositionen;

    /** The btn aktionaersstatus. */
    @FXML
    private Button btnAktionaersstatus;

    /** The btn vertreter eingabe. */
    @FXML
    private Button btnVertreterEingabe;

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /** The farbcodes OK. */
    private String farbcodesOK[] = { "#98F5FF", "#66CDAA", "#CAFF70" };

    /** The verwendeter farbcode OK. */
    private int verwendeterFarbcodeOK = 0;

    /** The farbcodes fehler. */
    private String farbcodesFehler[] = { "#FF0000", "#BC8F8F", "#FA8072" };

    /** The verwendeter farbcode fehler. */
    private int verwendeterFarbcodeFehler = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfScanEK != null : "fx:id=\"tfScanEK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfScanSK != null : "fx:id=\"tfScanSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfVorname != null : "fx:id=\"tfVorname\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblScanSK != null : "fx:id=\"lblScanSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblRichtig != null : "fx:id=\"lblRichtig\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblAnzeigeSK != null : "fx:id=\"lblAnzeigeSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfAnzeigeEK != null : "fx:id=\"tfAnzeigeEK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tfAnzeigeSK != null : "fx:id=\"tfAnzeigeSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblFehlerMeldung != null : "fx:id=\"lblFehlerMeldung\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnOffenePositionen != null : "fx:id=\"btnOffenePositionen\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnAktionaersstatus != null : "fx:id=\"btnAktionaersstatus\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnVertreterEingabe != null : "fx:id=\"btnVertreterEingabe\" was not injected: check your FXML file 'HauptStage.fxml'.";

        setzeAnzeige();
        lblRichtig.setText("");

        /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung*/
        btnVertreterEingabe.setVisible(false);

        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        if (lDbBundle.mindestensEineStimmkarteWirdZugeordnet() == false) {
            lblScanSK.setVisible(false);
            tfScanSK.setVisible(false);

            lblAnzeigeSK.setVisible(false);
            tfAnzeigeSK.setVisible(false);
        }
        lDbBundle.closeAll();

        tfScanEK.requestFocus();
        
        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown() && e.getCode().equals(KeyCode.M)) {
                CaOpenWindow.openModulauswahl(eigeneStage);
            }
        });

        ObjectActions.createInfoButton(rootPane, new String[] { "Strg-M - Modulauswahl" }, 30.0, 5.0);

    }

    /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung von hier*/

    /** The vertreternacherfassung. */
    private boolean vertreternacherfassung = false;

    /** The vertreternacherfassung ist aktiv. */
    private boolean vertreternacherfassungIstAktiv = false;

    /** The melde ident. */
    private int meldeIdent = 0;
    /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung bis hier*/

    /**
     * On btn vertreter eingabe.
     *
     * @param event the event
     */
    @FXML
    void onBtnVertreterEingabe(ActionEvent event) {
        /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung*/

        if (vertreternacherfassung == false) {
            return;
        }

        if (vertreternacherfassungIstAktiv == false) {
            vertreternacherfassungIstAktiv = true;
            tfScanEK.setDisable(true);
            btnVertreterEingabe.setText("Vertreter speichern");
            tfName.setEditable(true);
            tfVorname.setEditable(true);
            tfOrt.setEditable(true);

            tfName.requestFocus();
        }

        else {

            /*Prüfen*/
            if (tfName.getText().isEmpty() || tfVorname.getText().isEmpty() || tfOrt.getText().isEmpty()) {
                return;
            }

            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAll();

            /*Vollmacht eintragen*/
            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pErteiltAufWeg = 201;

            vmWillenserklaerung.piMeldungsIdentAktionaer = meldeIdent;
            vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

            EclPersonenNatJur personNatJur = new EclPersonenNatJur();

            personNatJur.ident = 0; /*Neue Person*/
            personNatJur.vorname = tfVorname.getText();
            personNatJur.name = tfName.getText();
            personNatJur.ort = tfOrt.getText();
            vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

            vmWillenserklaerung.vollmachtAnDritte(lDbBundle);
            if (vmWillenserklaerung.rcIstZulaessig == false) {
                lblFehlerMeldung.setText("Fehler bei Vollmachtseintrag " + vmWillenserklaerung.rcGrundFuerUnzulaessig);
                lDbBundle.closeAll();
                return;
            }

            int vertreterIdent = vmWillenserklaerung.pEclPersonenNatJur.ident;

            EclMeldung eclMeldung = new EclMeldung();
            eclMeldung.meldungsIdent = meldeIdent;
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
            eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            eclMeldung.vertreterName = tfName.getText();
            eclMeldung.vertreterVorname = tfVorname.getText();
            eclMeldung.vertreterOrt = tfOrt.getText();
            eclMeldung.vertreterIdent = vertreterIdent;
            lDbBundle.dbMeldungen.update(eclMeldung);

            /*Speichern*/
            int anz = lDbBundle.dbWillenserklaerung.lesePraesenzZuIdent(meldeIdent);
            for (int i = 0; i < anz; i++) {
                EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
                if (lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.erstzugang
                        || lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.abgang
                        || lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.wiederzugang) {
                    lWillenserklaerung.willenserklaerungGeberIdent = vertreterIdent;
                    lWillenserklaerung.bevollmaechtigterDritterIdent = vertreterIdent;
                    lDbBundle.dbWillenserklaerung.update(lWillenserklaerung);

                }

            }

            if (!eclMeldung.zutrittsIdent.isEmpty()) {
                EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
                lZutrittsIdent.zutrittsIdent = eclMeldung.zutrittsIdent;
                lZutrittsIdent.zutrittsIdentNeben = "00";
                lDbBundle.dbZutrittskarten.read(lZutrittsIdent, 1);
                EclZutrittskarten lZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                lZutrittskarte.personenNatJurIdent = vertreterIdent;
                lDbBundle.dbZutrittskarten.update(lZutrittskarte);

                //    			/*Nur ku036-Tauglich!*/
                //    			/*Stimmkarte EK+"00" auch bereinigen*/
                //    			lDbBundle.dbStimmkarten.read(eclMeldung.zutrittsIdent+"00", 1);
                //    			EclStimmkarten lStimmkarte=lDbBundle.dbStimmkarten.ergebnisPosition(0);
                //    			lStimmkarte.personenNatJurIdent=vertreterIdent;
                //    			lDbBundle.dbStimmkarten.update(lStimmkarte);
            }

            lDbBundle.closeAll();

            /*Normalen Status wiederherstellen*/
            tfScanEK.setDisable(false);
            btnVertreterEingabe.setText("Vertretereingabe");
            btnVertreterEingabe.setVisible(false);
            tfName.setEditable(false);
            tfVorname.setEditable(false);
            tfOrt.setEditable(false);

            tfScanEK.requestFocus();
            vertreternacherfassungIstAktiv = false;

        }

    }

    /**
     * On key pressed EK.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedEK(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doBtnEinlesenEK();
        }

    }

    /**
     * On key pressed SK.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedSK(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doBtnEinlesenSK();
        }

    }

    /**
     * On btn aktionaersstatus.
     *
     * @param event the event
     */
    @FXML
    void onBtnAktionaersstatus(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlAktionaersStatus controllerFenster = new CtrlAktionaersStatus();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AktionaersStatus.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHautStage.onBtnAktionaersstatus 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 600);
        neuerDialog.setTitle("Aktionärsstatus");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * On btn offene positionen.
     *
     * @param event the event
     */
    @FXML
    void onBtnOffenePositionen(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlOffenePositionen controllerFenster = new CtrlOffenePositionen();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("OffenePositionen.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlElekTeilnehmerverzAnzeigen.onBtnOffenePositionen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 600);
        neuerDialog.setTitle("Offene Positionen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

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

    /** The sk gescanned. */
    private String skGescanned = "";

    /** The ek gescanned. */
    private EclZutrittsIdent ekGescanned = null;

    /**
     * Do btn einlesen EK.
     */
    private void doBtnEinlesenEK() {

        lDbBundle = new DbBundle();

        lblRichtig.setText("");
        lblRichtig.setStyle("-fx-background-color: #ffffff");
        tfAnzeigeEK.setText("");
        tfAnzeigeSK.setText("");
        tfName.setText("");
        tfVorname.setText("");
        tfOrt.setText("");
        lblFehlerMeldung.setText("");

        String hString = tfScanEK.getText().trim();

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int erg = blNummernformen.dekodiere(hString, KonstKartenklasse.eintrittskartennummer);

        if (erg < 0) {
            lblFehlerMeldung.setText("Ungültiges Format");
            return;
        }

        ekGescanned = new EclZutrittsIdent();
        ekGescanned.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
        ekGescanned.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);

        /*TODO #Konsolidierung: das muß alles noch für unterschiedliche Gattungen und eintrittskarteWirdStimmkarte sauber durchdacht und entwickelt werden*/

        if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdStimmkarte
                || lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[1 - 1][0] == 0) {
            //1:1 - nun verarbeiten
            verarbeiten();
        } else {//Tausch beliebig - Stimmkarte einlesen
            tfScanSK.requestFocus();
        }
    }

    /**
     * Do btn einlesen SK.
     */
    private void doBtnEinlesenSK() {
        lblFehlerMeldung.setText("");
        if (ekGescanned == null || ekGescanned.zutrittsIdent.isEmpty()) {
            lblFehlerMeldung.setText("Erst Eintrittskarte lesen!");
            tfScanEK.requestFocus();
            return;
        }

        String hString = tfScanSK.getText();

        /*Sonderverarbeitung ku164 2018*/
        //		if (hString.length()>6){
        //			hString=hString.substring(1, 6);
        //		}

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int erg = blNummernformen.dekodiere(hString, KonstKartenklasse.stimmkartennummer);
        if (erg < 0) {
            lblFehlerMeldung.setText("Ungültiges Format");
            return;
        }
        skGescanned = blNummernformen.rcIdentifikationsnummer.get(0);
        verarbeiten();
    }

    /**
     * Verarbeiten.
     */
    private void verarbeiten() {

        lDbBundle.openAll();

        int anz = lDbBundle.dbWillenserklaerung.leseZuEk(ekGescanned);
        if (anz < 1) {
            lblRichtig.setText("Falsch");
            //			lblRichtig.setStyle("-fx-background-color: #ff0000");
            lblRichtig.setStyle("-fx-background-color: " + farbcodesFehler[verwendeterFarbcodeFehler]);
            verwendeterFarbcodeFehler++;
            if (verwendeterFarbcodeFehler >= farbcodesFehler.length) {
                verwendeterFarbcodeFehler = 0;
            }
            lblFehlerMeldung.setText("Eintrittskartennummer nicht als Erstzugang gefunden!");
            tfScanEK.setText("");
            tfScanSK.setText("");
            tfScanEK.requestFocus();
            lDbBundle.closeAll();
            return;
        }

        EclWillenserklaerung lWillenserklaerung = new EclWillenserklaerung();
        lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungArray[0];

        tfAnzeigeEK.setText(lWillenserklaerung.zutrittsIdent + "-" + lWillenserklaerung.zutrittsIdentNeben);
        if (!lDbBundle.param.paramAkkreditierung.eintrittskarteWirdStimmkarte
                || lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[1 - 1][0] == 0) {
            tfAnzeigeSK.setText(lWillenserklaerung.stimmkarte1);
        }
        if (lWillenserklaerung.bevollmaechtigterDritterIdent == 0) {
            tfName.setText("");
            tfVorname.setText("");
            tfOrt.setText("");
        } else {
            lDbBundle.dbPersonenNatJur.read(lWillenserklaerung.bevollmaechtigterDritterIdent);
            EclPersonenNatJur lPersonenNatJur = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
            tfName.setText(lPersonenNatJur.name);
            tfVorname.setText(lPersonenNatJur.vorname);
            tfOrt.setText(lPersonenNatJur.ort);
        }
        if (skGescanned.compareTo(lWillenserklaerung.stimmkarte1) == 0
                || lDbBundle.param.paramAkkreditierung.eintrittskarteWirdStimmkarte
                || lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[1
                        - 1][0]/*Früher: .pPraesenzStimmkartenZuordnenAppGattung1[0]*/ == 0) {
            /* Richtig! */
            lblRichtig.setText("Richtig");
            //			lblRichtig.setStyle("-fx-background-color: #00ff00");
            lblRichtig.setStyle("-fx-background-color: " + farbcodesOK[verwendeterFarbcodeOK]);
            verwendeterFarbcodeOK++;
            if (verwendeterFarbcodeOK >= farbcodesOK.length) {
                verwendeterFarbcodeOK = 0;
            }

            lWillenserklaerung.istKontrolliert = 1;
            lDbBundle.dbWillenserklaerung.update(lWillenserklaerung);
        } else {
            /* Falsch */
            lblRichtig.setText("Falsch");
            //			lblRichtig.setStyle("-fx-background-color: #ff0000");
            lblRichtig.setStyle("-fx-background-color: " + farbcodesFehler[verwendeterFarbcodeFehler]);
            verwendeterFarbcodeFehler++;
            if (verwendeterFarbcodeFehler >= farbcodesFehler.length) {
                verwendeterFarbcodeFehler = 0;
            }

        }
        tfScanEK.setText("");
        tfScanSK.setText("");
        tfScanEK.requestFocus();

        /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung von hier*/
        if (lWillenserklaerung.zuVerzeichnisNr1Gedruckt == 0 && lWillenserklaerung.willenserklaerungGeberIdent == -1
                && lWillenserklaerung.bevollmaechtigterDritterIdent == 0
                && lDbBundle.paramGeraet.vertreterKorrekturBeiKontrollerfassungMoeglich) {
            vertreternacherfassung = true;
            btnVertreterEingabe.setVisible(true);
        } else {
            vertreternacherfassung = false;
            btnVertreterEingabe.setVisible(false);

        }
        meldeIdent = lWillenserklaerung.meldungsIdent;
        /*TODO _VertreternacherfassungKontrolle: Nur rein für Vertretererfassung bis hier*/

        lDbBundle.closeAll();

    }

    // private void clearFehlermeldung(){
    // lblFehlerMeldung.setText("");return;
    //
    // }

    /*
     * Farben: #00ff00 = grün #ff0000 = rot #A7A7A7 = grau #0080ff = blau
     */

    /**
     * Setze anzeige.
     */
    private void setzeAnzeige() {

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
