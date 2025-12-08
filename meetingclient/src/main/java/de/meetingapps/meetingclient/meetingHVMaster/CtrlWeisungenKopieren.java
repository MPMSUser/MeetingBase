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
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlSammelkartenSummenKorrektur;
import de.meetingapps.meetingportal.meetComBl.BlWeisungenKopieren;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlWeisungenKopieren.
 */
public class CtrlWeisungenKopieren extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn weiter. */
    @FXML
    private Button btnWeiter;

    /** The btn weiter initialisieren. */
    @FXML
    private Button btnWeiterInitialisieren;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The scpn vorgaenge. */
    @FXML
    private ScrollPane scpnVorgaenge;

    /** The btn ausfuehren. */
    @FXML
    private Button btnAusfuehren;

    /** The btn sammelkarten summen. */
    @FXML
    private Button btnSammelkartenSummen;

    /** The tf J. */
    @FXML
    private TextField tfJ;

    /** The tf N. */
    @FXML
    private TextField tfN;

    /** The tf E. */
    @FXML
    private TextField tfE;

    /** The tf U. */
    @FXML
    private TextField tfU;

    /** The tf NT. */
    @FXML
    private TextField tfNT;

    /** The grpn abstimmungen. */
    /*Ab hier individuell*/
    private MeetingGridPane grpnAbstimmungen = null;

    /** The abstimmungsliste. */
    public EclAbstimmung[] abstimmungsliste = null;

    /** The abstimmungsliste gegen. */
    public EclAbstimmung[] abstimmungslisteGegen = null;

    /** The cb quelle. */
    private CheckBox[] cbQuelle = null;

    /** The cb ziel. */
    private CheckBox[] cbZiel = null;

    /** ********Ab hier individuell***********. */

    private DbBundle lDbBundle = null;

    /**
     * 1 = Quelle-Ziel-Auswahl 2 = Bestätigung Quelle-Ziel 3 = Quittung 4 = Quittung
     * Initialisieren.
     */
    private int aktiveAnzeige = 0;

    /** The anz top. */
    private int anzTop = 0;

    /** The anz gegen. */
    private int anzGegen = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        /**********Ab hier individuell*******************/

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        initAnfang();
        initQuelleZielAuswahl();

        lDbBundle.closeAll();

    }

    /**
     * Erst-Initialisierung bei Programmstart.
     */
    private void initAnfang() {
        tfJ.setText("J");
        tfN.setText("N");
        tfE.setText("E");
        tfU.setText("U");
        tfNT.setText("NT");
    }

    /**
     * Initialisieren Darstellung für Quelle-Ziel-Auswahl.
     */
    private void initQuelleZielAuswahl() {
        BlSammelkartenSummenKorrektur blSammelkartenSummenKorrektur = new BlSammelkartenSummenKorrektur(lDbBundle);
        blSammelkartenSummenKorrektur.einlesenAbstimmungsliste();
        abstimmungsliste = blSammelkartenSummenKorrektur.abstimmungsliste;
        abstimmungslisteGegen = blSammelkartenSummenKorrektur.abstimmungslisteGegen;

        if (abstimmungsliste == null && abstimmungslisteGegen == null) {
            return;
        }

        anzTop = 0;
        anzGegen = 0;

        if (abstimmungsliste != null) {
            anzTop = abstimmungsliste.length;
        }
        if (abstimmungslisteGegen != null) {
            anzGegen = abstimmungslisteGegen.length;
        }

        grpnAbstimmungen = new MeetingGridPane();

        cbQuelle = new CheckBox[anzTop + anzGegen];
        cbZiel = new CheckBox[anzTop + anzGegen];

        for (int i = 0; i < anzTop; i++) {
            trageAbstimmungspunktInAnzeigeEin(i, abstimmungsliste[i]);
        }

        for (int i = 0; i < anzGegen; i++) {
            trageAbstimmungspunktInAnzeigeEin(anzTop + i, abstimmungslisteGegen[i]);
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Quelle");
        ueberschriftList.add("Ziel");
        ueberschriftList.add("TOP");
        ueberschriftList.add("");
        ueberschriftList.add("Beschreibung");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scpnVorgaenge);

        tfJ.setEditable(true);
        tfN.setEditable(true);
        tfE.setEditable(true);
        tfU.setEditable(true);
        tfNT.setEditable(true);

        btnAusfuehren.setDisable(true);
        btnWeiterInitialisieren.setDisable(false);
        btnWeiter.setDisable(false);

        aktiveAnzeige = 1;
    }

    /**
     * Trage abstimmungspunkt in anzeige ein.
     *
     * @param pOffset     the offset
     * @param pAbstimmung the abstimmung
     */
    private void trageAbstimmungspunktInAnzeigeEin(int pOffset, EclAbstimmung pAbstimmung) {
        cbQuelle[pOffset] = new CheckBox();
        cbQuelle[pOffset].setSelected(false);
        if (pAbstimmung.liefereIstUeberschift() == false) {
            grpnAbstimmungen.addMeeting(cbQuelle[pOffset], 0, pOffset + 1);
        }

        cbZiel[pOffset] = new CheckBox();
        cbZiel[pOffset].setSelected(false);
        if (pAbstimmung.liefereIstUeberschift() == false) {
            grpnAbstimmungen.addMeeting(cbZiel[pOffset], 1, pOffset + 1);
        }

        Label lNummerKey = new Label(pAbstimmung.nummerKey);
        grpnAbstimmungen.addMeeting(lNummerKey, 2, pOffset + 1);

        Label lNummerindexKey = new Label(pAbstimmung.nummerindexKey);
        grpnAbstimmungen.addMeeting(lNummerindexKey, 3, pOffset + 1);

        Label lKurzBezeichnung = new Label(pAbstimmung.kurzBezeichnung);
        grpnAbstimmungen.addMeeting(lKurzBezeichnung, 4, pOffset + 1);

        Label lAnzeigeBezeichnungKurz = new Label(pAbstimmung.anzeigeBezeichnungKurz);
        grpnAbstimmungen.addMeeting(lAnzeigeBezeichnungKurz, 5, pOffset + 1);

        Label lAnzeigeBezeichnungLang = new Label(pAbstimmung.anzeigeBezeichnungLang);
        grpnAbstimmungen.addMeeting(lAnzeigeBezeichnungLang, 6, pOffset + 1);
    }

    /****************Eingabe***************************************/
    
    @FXML
    void onBtnWeiter(ActionEvent event) {
        /** Prüfen */
        if (pruefeWeisungsArt(tfJ) == false) {
            return;
        }
        if (pruefeWeisungsArt(tfN) == false) {
            return;
        }
        if (pruefeWeisungsArt(tfE) == false) {
            return;
        }
        if (pruefeWeisungsArt(tfU) == false) {
            return;
        }
        if (pruefeWeisungsArt(tfNT) == false) {
            return;
        }

        int anzQuelle = 0;
        for (int i = 0; i < cbQuelle.length; i++) {
            if (cbQuelle[i].isSelected()) {
                anzQuelle++;
            }
        }
        if (anzQuelle != 1) {
            fehlerMeldung("Genau eine Quelle auswählen");
            return;
        }

        int anzZiel = 0;
        for (int i = 0; i < cbZiel.length; i++) {
            if (cbZiel[i].isSelected()) {
                anzZiel++;
            }
        }
        if (anzZiel == 0) {
            fehlerMeldung("Mindestens ein Ziel auswählen");
            return;
        }

        /*Quittung anzeigen*/
        grpnAbstimmungen = new MeetingGridPane();
        int lOffset = 1;
        Label lQuelle = new Label("Quelle:");
        grpnAbstimmungen.addMeeting(lQuelle, 0, lOffset);
        lOffset++;

        for (int i = 0; i < cbQuelle.length; i++) {
            if (cbQuelle[i].isSelected()) {
                trageAbstimmungspunktInQuittungEin(lOffset, holeAbstimmung(i));
                lOffset++;
            }
        }

        Label lZiel = new Label("Ziel:");
        grpnAbstimmungen.addMeeting(lZiel, 0, lOffset);
        lOffset++;

        for (int i = 0; i < cbZiel.length; i++) {
            if (cbZiel[i].isSelected()) {
                trageAbstimmungspunktInQuittungEin(lOffset, holeAbstimmung(i));
                lOffset++;
            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("TOP");
        ueberschriftList.add("");
        ueberschriftList.add("Beschreibung");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scpnVorgaenge);

        btnWeiter.setDisable(true);
        btnAusfuehren.setDisable(false);
        btnWeiterInitialisieren.setDisable(false);
        tfJ.setEditable(false);
        tfN.setEditable(false);
        tfE.setEditable(false);
        tfU.setEditable(false);
        tfNT.setEditable(false);

        aktiveAnzeige = 2;
    }

    /**
     * On btn weiter initialisieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeiterInitialisieren(ActionEvent event) {
        if (aktiveAnzeige == 1) {
            /** Prüfen */

            int anzZiel = 0;
            for (int i = 0; i < cbZiel.length; i++) {
                if (cbZiel[i].isSelected()) {
                    anzZiel++;
                }
            }
            if (anzZiel == 0) {
                fehlerMeldung("Mindestens ein Ziel auswählen");
                return;
            }

            /*Quittung anzeigen*/
            grpnAbstimmungen = new MeetingGridPane();
            int lOffset = 1;

            Label lZiel = new Label("Initialisieren:");
            grpnAbstimmungen.addMeeting(lZiel, 0, lOffset);
            lOffset++;

            for (int i = 0; i < cbZiel.length; i++) {
                if (cbZiel[i].isSelected()) {
                    trageAbstimmungspunktInQuittungEin(lOffset, holeAbstimmung(i));
                    lOffset++;
                }
            }

            List<String> ueberschriftList = new LinkedList<String>();
            ueberschriftList.add("TOP");
            ueberschriftList.add("");
            ueberschriftList.add("Beschreibung");

            String[] uberschriftString = new String[ueberschriftList.size()];
            for (int i = 0; i < ueberschriftList.size(); i++) {
                uberschriftString[i] = ueberschriftList.get(i);
            }
            grpnAbstimmungen.setzeUeberschrift(uberschriftString, scpnVorgaenge);

            btnWeiter.setDisable(false);
            btnAusfuehren.setDisable(false);

            tfJ.setEditable(false);
            tfN.setEditable(false);
            tfE.setEditable(false);
            tfU.setEditable(false);
            tfNT.setEditable(false);

            aktiveAnzeige = 4;
        } else {
            /*Ausführen*/
            /*Verarbeiten*/
            lDbBundle.openAll();

            BlWeisungenKopieren blWeisungenKopieren = new BlWeisungenKopieren(lDbBundle);

            for (int i = 0; i < cbZiel.length; i++) {
                if (cbZiel[i].isSelected()) {
                    EclAbstimmung lAbstimmung = holeAbstimmung(i);
                    blWeisungenKopieren.identWeisungssatzZiel.add(lAbstimmung.identWeisungssatz);
                }
            }

            blWeisungenKopieren.initialisieren();

            lDbBundle.closeAll();

            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Initialisierung erledigt");

            lDbBundle.openAll();
            initQuelleZielAuswahl();
            lDbBundle.closeAll();
        }
    }

    /**
     * On btn ausfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnAusfuehren(ActionEvent event) {
        /*Verarbeiten*/
        lDbBundle.openAll();

        BlWeisungenKopieren blWeisungenKopieren = new BlWeisungenKopieren(lDbBundle);

        for (int i = 0; i < cbQuelle.length; i++) {
            if (cbQuelle[i].isSelected()) {
                EclAbstimmung lAbstimmung = holeAbstimmung(i);
                blWeisungenKopieren.identWeisungssatzQuelle = lAbstimmung.identWeisungssatz;
            }
        }

        for (int i = 0; i < cbZiel.length; i++) {
            if (cbZiel[i].isSelected()) {
                EclAbstimmung lAbstimmung = holeAbstimmung(i);
                blWeisungenKopieren.identWeisungssatzZiel.add(lAbstimmung.identWeisungssatz);
            }
        }

        blWeisungenKopieren.umsetzungWeisungen[KonstStimmart.ja] = KonstStimmart.getIntVonTextKurz(tfJ.getText());
        blWeisungenKopieren.umsetzungWeisungen[KonstStimmart.nein] = KonstStimmart.getIntVonTextKurz(tfN.getText());
        blWeisungenKopieren.umsetzungWeisungen[KonstStimmart.enthaltung] = KonstStimmart
                .getIntVonTextKurz(tfE.getText());
        blWeisungenKopieren.umsetzungWeisungen[KonstStimmart.ungueltig] = KonstStimmart
                .getIntVonTextKurz(tfU.getText());
        blWeisungenKopieren.umsetzungWeisungen[KonstStimmart.nichtTeilnahme] = KonstStimmart
                .getIntVonTextKurz(tfNT.getText());

        blWeisungenKopieren.kopieren();

        lDbBundle.closeAll();

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Umkopieren erledigt");

        lDbBundle.openAll();
        initQuelleZielAuswahl();
        lDbBundle.closeAll();
    }

    /**
     * Pruefe weisungs art.
     *
     * @param pTextField the text field
     * @return true, if successful
     */
    private boolean pruefeWeisungsArt(TextField pTextField) {
        String hInhalt = pTextField.getText().trim();
        if (hInhalt.isEmpty()) {
            fehlerhaftesFeld = pTextField;
            fehlerMeldung("Stimmart muß ausgefüllt werden");
            return false;
        }

        if (!hInhalt.equalsIgnoreCase("J") &&
                !hInhalt.equalsIgnoreCase("N") &&
                !hInhalt.equalsIgnoreCase("E") &&
                !hInhalt.equalsIgnoreCase("U") &&
                !hInhalt.equalsIgnoreCase("NT")) {
            fehlerhaftesFeld = pTextField;
            fehlerMeldung("Stimmart ist unzulässig");
            return false;
        }

        return true;
    }

    /**
     * Trage abstimmungspunkt in quittung ein.
     *
     * @param pOffset     the offset
     * @param pAbstimmung the abstimmung
     */
    private void trageAbstimmungspunktInQuittungEin(int pOffset, EclAbstimmung pAbstimmung) {

        Label lNummerKey = new Label(pAbstimmung.nummerKey);
        grpnAbstimmungen.addMeeting(lNummerKey, 0, pOffset);

        Label lNummerindexKey = new Label(pAbstimmung.nummerindexKey);
        grpnAbstimmungen.addMeeting(lNummerindexKey, 1, pOffset);

        Label lKurzBezeichnung = new Label(pAbstimmung.kurzBezeichnung);
        grpnAbstimmungen.addMeeting(lKurzBezeichnung, 2, pOffset);

        Label lAnzeigeBezeichnungKurz = new Label(pAbstimmung.anzeigeBezeichnungKurz);
        grpnAbstimmungen.addMeeting(lAnzeigeBezeichnungKurz, 3, pOffset);

        Label lAnzeigeBezeichnungLang = new Label(pAbstimmung.anzeigeBezeichnungLang);
        grpnAbstimmungen.addMeeting(lAnzeigeBezeichnungLang, 4, pOffset);
    }

    /**
     * Hole abstimmung.
     *
     * @param pOffset the offset
     * @return the ecl abstimmung
     */
    private EclAbstimmung holeAbstimmung(int pOffset) {
        if (pOffset < anzTop) {
            return abstimmungsliste[pOffset];
        }
        return abstimmungslisteGegen[pOffset - anzTop];
    }

    /**
     * On btn sammelkarten summen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSammelkartenSummen(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Sammelkarten Summen korrigieren", "Summen korrigieren",
                "Abbrechen");

        if (brc == false) {
            return;
        }

        lDbBundle.openAll();
        BlSammelkartenSummenKorrektur blSammelkartenSummenKorrektur = new BlSammelkartenSummenKorrektur(lDbBundle);
        blSammelkartenSummenKorrektur.einlesenAbstimmungsliste();
        blSammelkartenSummenKorrektur.verarbeiteAlleSammelkarten();
        lDbBundle.closeAll();

        caZeigeHinweis.zeige(eigeneStage, "Summenkorrektur erledigt");

    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
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
