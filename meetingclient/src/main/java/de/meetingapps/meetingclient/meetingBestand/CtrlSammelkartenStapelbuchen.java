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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * The Class CtrlSammelkartenStapelbuchen.
 */
public class CtrlSammelkartenStapelbuchen extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn weisung gesamt. */
    @FXML
    private Button btnWeisungGesamt;

    /** The btn loeschen aus sammelkarte. */
    @FXML
    private Button btnLoeschenAusSammelkarte;

    /** The scpn umbuchen. */
    @FXML
    private ScrollPane scpnUmbuchen;

    /** The scpn abstimmungspunkte. */
    @FXML
    private ScrollPane scpnAbstimmungspunkte;

    /** []=jeweilige Zeile. */
    private Button[] btnSammelkarten = null;

    /** The cb weisungen. */
    private List<ComboBox<String>> cbWeisungen = null;

    /** The weisungs liste. */
    private List<String> weisungsListe = null;

    /** The sammelkarten. */
    private EclMeldung[] sammelkarten = null;

    /** The map. */
    private Map<String, String> map = null;

    /** The db bundle. */
    DbBundle dbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert btnAbbrechen != null
                : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'SammelkartenStapelbuchen.fxml'.";
        assert btnLoeschenAusSammelkarte != null
                : "fx:id=\"btnLoeschenAusSammelkarte\" was not injected: check your FXML file 'SammelkartenStapelbuchen.fxml'.";
        assert scpnUmbuchen != null
                : "fx:id=\"scpnUmbuchen\" was not injected: check your FXML file 'SammelkartenStapelbuchen.fxml'.";
        assert scpnAbstimmungspunkte != null
                : "fx:id=\"scpnAbstimmungspunkte\" was not injected: check your FXML file 'SammelkartenStapelbuchen.fxml'.";

        /*************** Ab hier individuell **********************************/
        dbBundle = new DbBundle();
        dbBundle.openAll();

        CaBug.druckeLog("initialize A", logDrucken, 10);
        zeigeSammelkarten();
        CaBug.druckeLog("initialize B", logDrucken, 10);
        zeigeAgenda();
        CaBug.druckeLog("initialize C", logDrucken, 10);

        dbBundle.closeAll();

    }

    /**
     * ************* Click-Reaktionen ****************.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Clicked loeschen aus sammelkarte.
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschenAusSammelkarte(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        String hinweisText = "Die ausgewählten unten angezeigten Aktionäre werden aus der Sammelkarte entfernt (bzw. die entsprechenden Willenserklärungen widerrufen).";
        if (aktuelleSammelMeldung.statusPraesenz == 1) {
            hinweisText += " Die Aktionäre werden in der Präsenz als Abgang gebucht.";
        }
        boolean brc = zeigeHinweis("Aus Sammelkarte löschen", hinweisText, ausgewaehlteAktionaereMeldungWeisung,
                aktuelleSammelMeldung.liefereGattung());
        if (!brc) {
            return;
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.widerrufeMeldungArrayInSammelkarte(aktuelleSammelMeldung.meldungsIdent,
                aktuelleSammelMeldung.skIst, ausgewaehlteAktionaereMeldung);

        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Achtung - Fehler aufgetreten - Aktionäre überprüfen!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Löschung durchgeführt");
        }
        eigeneStage.hide();
    }

    //    @FXML
    //    void clickedWeisungAendern(ActionEvent event) {
    //        int neueWeisungsart = 0;
    //
    //        String sNeueWeisungsart = cbNeueWeisung.getValue();
    //        if (sNeueWeisungsart == null || sNeueWeisungsart.isEmpty()) {
    //            fehlerMeldung("Bitte neue Weisungsart auswählen!");
    //            return;
    //        }
    //        for (int i = 0; i <= 8; i++) {
    //            if (KonstStimmart.getText(i).equals(sNeueWeisungsart)) {
    //                neueWeisungsart = i;
    //            }
    //        }
    //
    //        int rc = findeButton(btnAbstimmung, event);
    //        int abstimmungsoffset = 0;
    //        int offsetInWeisungsstring = 0;
    //        String agendaNr = "";
    //        int offsetUeberschrift = 0;
    //        if (ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {
    //            offsetUeberschrift = 1;
    //        }
    //
    //        EclAbstimmung lAbstimmung = null;
    //        CaBug.druckeLog("rc=" + rc + " offsetUeberschrift=" + offsetUeberschrift, logDrucken, 10);
    //
    //        if (rc < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung) + offsetUeberschrift) {
    //            /* Normale */
    //            abstimmungsoffset = rc - offsetUeberschrift;
    //            lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][abstimmungsoffset];
    //        } else {
    //            /* Gegenanträge */
    //            abstimmungsoffset = abstimmungsoffset
    //                    - blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung].length - 2;
    //
    //            lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][abstimmungsoffset];
    //        }
    //
    //        offsetInWeisungsstring = lAbstimmung.identWeisungssatz;
    //        agendaNr = lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey;
    //
    //        for (int i = 0; i < ausgewaehlteAktionaereMeldungWeisung.length; i++) {
    //            ausgewaehlteAktionaereMeldungWeisung[i].abgabe[offsetInWeisungsstring] = neueWeisungsart;
    //        }
    //
    //        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
    //        String hinweisText = "Für die ausgewählten unten angezeigten Aktionäre wird die Weisung für Abstimmung "
    //                + agendaNr + " auf " + sNeueWeisungsart + " gesetzt";
    //        boolean brc = zeigeHinweis("Weisung buchen", hinweisText, ausgewaehlteAktionaereMeldungWeisung,
    //                aktuelleSammelMeldung.liefereGattung());
    //        if (!brc) {
    //            return;
    //        }
    //
    //        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
    //        rc = blSammelkarten.aendernWeisungMeldungArrayInSammelkarte(aktuelleSammelMeldung.meldungsIdent,
    //                aktuelleSammelMeldung.skIst, ausgewaehlteAktionaereMeldung, ausgewaehlteAktionaereMeldungWeisung);
    //
    //        System.out.println("1: " + Arrays.toString(ausgewaehlteAktionaereMeldung));
    //        System.out.println("2: " + Arrays.toString(ausgewaehlteAktionaereMeldungWeisung[0].abgabe));
    //
    //        if (rc < 1) {
    //            caZeigeHinweis.zeige(eigeneStage, "Achtung - Fehler aufgetreten - Aktionäre überprüfen!");
    //        } else {
    //            caZeigeHinweis.zeige(eigeneStage, "Weisungen gesetzt");
    //        }
    //        eigeneStage.hide();
    //
    //    }

    /**
     * Clicked weisung gesamt.
     *
     * @param event the event
     */
    @FXML
    void clickedWeisungGesamt(ActionEvent event) {

        map = new HashMap<>();

        for (ComboBox<String> cb : cbWeisungen) {

            if (!cb.getSelectionModel().isEmpty()) {

                int neueWeisungsart = 0;

                for (int i = 0; i <= 8; i++)
                    if (KonstStimmart.getText(i).equals(cb.getValue()))
                        neueWeisungsart = i;

                int abstimmungsoffset = 0;
                int offsetInWeisungsstring = 0;

                int rc = cbWeisungen.indexOf(cb);

                EclAbstimmung lAbstimmung = null;

                if (rc < blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung)) {
                    /* Normale */
                    abstimmungsoffset = rc;
                    lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][abstimmungsoffset];
                } else {
                    /* Gegenanträge */
                    abstimmungsoffset = rc - blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung].length;
                    lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][abstimmungsoffset];
                }

                String agendaNr = (lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey).trim();
                String tmp = map.get(cb.getValue());
                if (tmp != null)
                    agendaNr = tmp + ", " + agendaNr;

                map.put(cb.getValue(), agendaNr);

                offsetInWeisungsstring = lAbstimmung.identWeisungssatz;

                for (int i = 0; i < ausgewaehlteAktionaereMeldungWeisung.length; i++)
                    ausgewaehlteAktionaereMeldungWeisung[i].abgabe[offsetInWeisungsstring] = neueWeisungsart;

            }
        }

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = zeigeHinweis("Weisung buchen", hinweisString(), ausgewaehlteAktionaereMeldungWeisung,
                aktuelleSammelMeldung.liefereGattung());
        if (!brc) {
            return;
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        int rc = blSammelkarten.aendernWeisungMeldungArrayInSammelkarte(aktuelleSammelMeldung.meldungsIdent,
                aktuelleSammelMeldung.skIst, ausgewaehlteAktionaereMeldung, ausgewaehlteAktionaereMeldungWeisung);

        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Achtung - Fehler aufgetreten - Aktionäre überprüfen!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Weisungen gesetzt");
        }
        eigeneStage.hide();

    }

    /**
     * Hinweis string.
     *
     * @return the string
     */
    private String hinweisString() {

        String hinweis = "Für die unten angezeigten Aktionäre werden folgende Weisungen gesetzt: \n";

        int i = 0;

        for (String str : map.keySet())
            hinweis += "Abstimmungspunkt: " + map.get(str) + " auf " + str + (i++ % 2 == 0 ? "\t" : "\n");

        return hinweis;
    }

    /**
     * Clicked auf sammelkarte buchen.
     *
     * @param event the event
     */
    @FXML
    void clickedAufSammelkarteBuchen(ActionEvent event) {
        int rc = findeButton(btnSammelkarten, event);
        boolean brc = false;

        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        if (aktuelleSammelMeldung.statusPraesenz != sammelkarten[rc].statusPraesenz) {
            caZeigeHinweis.zeige(eigeneStage,
                    "Ziel-Sammelkarte hat anderen Präsenz-Status als Quell-Sammelkarte - Umbuchung unzulässig!");
            return;
        }
        if ((aktuelleSammelMeldung.skWeisungsartZulaessig & 4) == 4
                && (sammelkarten[rc].skWeisungsartZulaessig & 4) == 0) {
            brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                    "Achtung: Zielsammelkarte nimmt keine Weisungen auf. Einzelweisungen der Aktionäre werden deshalb gelöscht!",
                    "Weiter", "Abbruch");
            if (brc == false) {
                return;
            }
        }
        if ((aktuelleSammelMeldung.skWeisungsartZulaessig & 4) == 0
                && (sammelkarten[rc].skWeisungsartZulaessig & 4) == 4) {
            brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                    "Achtung: Zielsammelkarte benötigt Weisungen auf. Quellsammelkarte enthält keine Weisungen. Einzelweisungen der Aktionäre werden deshalb auf Enthaltung gesetzt!",
                    "Weiter", "Abbruch");
            if (brc == false) {
                return;
            }
        }

        String hinweisText = "Die ausgewählten unten angezeigten Aktionäre werden von der Sammelkarte "
                + aktuelleSammelMeldung.meldungsIdent + " " + aktuelleSammelMeldung.name + " "
                + aktuelleSammelMeldung.zusatzfeld2 + " auf die Sammelkarte " + sammelkarten[rc].meldungsIdent + " "
                + sammelkarten[rc].name + " " + sammelkarten[rc].zusatzfeld2 + "umgebucht. ";
        if (aktuelleSammelMeldung.statusPraesenz == 1) {
            hinweisText += " Die Aktionäre werden in der Präsenz als Abgang und Wiederzugang gebucht.";
        }
        brc = zeigeHinweis("Auf andere Sammelkarte buchen", hinweisText, ausgewaehlteAktionaereMeldungWeisung,
                aktuelleSammelMeldung.liefereGattung());
        if (!brc) {
            return;
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(false, dbBundle);
        rc = blSammelkarten.umbuchenSammelkarteMeldungArray(aktuelleSammelMeldung.meldungsIdent,
                aktuelleSammelMeldung.skIst, sammelkarten[rc].meldungsIdent, sammelkarten[rc].skIst,
                sammelkarten[rc].skWeisungsartZulaessig, ausgewaehlteAktionaereMeldung,
                ausgewaehlteAktionaereMeldungWeisung);

        if (rc < 1) {
            caZeigeHinweis.zeige(eigeneStage, "Achtung - Fehler aufgetreten - Aktionäre überprüfen!");
        } else {
            caZeigeHinweis.zeige(eigeneStage, "Umbuchung durchgeführt");
        }
        eigeneStage.hide();

    }

    /**
     * Zeige hinweis.
     *
     * @param pUeberschrift                         the ueberschrift
     * @param pBestaetigungsText                    the bestaetigungs text
     * @param pAusgewaehlteAktionaereMeldungWeisung the ausgewaehlte aktionaere
     *                                              meldung weisung
     * @param pGattung                              the gattung
     * @return true, if successful
     */
    private boolean zeigeHinweis(String pUeberschrift, String pBestaetigungsText,
            EclWeisungMeldung[] pAusgewaehlteAktionaereMeldungWeisung, int pGattung) {

        Stage newStage = new Stage();
        CtrlSammelkartenStapelbuchenBestaetigung controllerFenster = new CtrlSammelkartenStapelbuchenBestaetigung();
        controllerFenster.init(newStage, pUeberschrift, pBestaetigungsText, pAusgewaehlteAktionaereMeldungWeisung,
                ausgewaehlteMeldungMitAktionaersWeisung, blAbstimmungenWeisungenErfassen, pGattung);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/SammelkartenStapelbuchenBestaetigung.fxml", 1200, 720,
                "Stapel-Buchung Bestätigung", true);

        return (controllerFenster.rc);
    }

    /**
     * ****************** Oberflächen ******************************************.
     */

    private void zeigeSammelkarten() {

        dbBundle.openAll();

        dbBundle.dbMeldungen.leseAlleAktivenSammelkarten(aktuelleGattung);
        sammelkarten = dbBundle.dbMeldungen.meldungenArray;
        dbBundle.closeAll();

        int anzSammelkarten = sammelkarten.length;
        btnSammelkarten = new Button[anzSammelkarten];

        int fuelleTabSammelkartenZeile = 0;
        MeetingGridPane lSammelGridPane = new MeetingGridPane();
        for (int i = 0; i < anzSammelkarten; i++) {
            EclMeldung lSammelkarte = sammelkarten[i];
            btnSammelkarten[i] = new Button("als Ziel definieren");
            btnSammelkarten[i].setOnAction(e -> {
                clickedAufSammelkarteBuchen(e);
            });
            if (lSammelkarte.meldungsIdent != aktuelleSammelMeldung.meldungsIdent) {
                lSammelGridPane.addMeeting(btnSammelkarten[i], 0, fuelleTabSammelkartenZeile);

                Label lblNummer = new Label(Integer.toString(lSammelkarte.meldungsIdent));
                lSammelGridPane.addMeeting(lblNummer, 1, fuelleTabSammelkartenZeile);

                Label lblName = new Label(lSammelkarte.name);
                lSammelGridPane.addMeeting(lblName, 2, fuelleTabSammelkartenZeile);

                Label lblKommentar = new Label(lSammelkarte.zusatzfeld2);
                lSammelGridPane.addMeeting(lblKommentar, 3, fuelleTabSammelkartenZeile);

                String hMitWeisung = "";
                if ((lSammelkarte.skWeisungsartZulaessig & 2) == 2) {
                    hMitWeisung = "ohne Weisung";
                }
                if ((lSammelkarte.skWeisungsartZulaessig & 4) == 4) {
                    if (!hMitWeisung.isEmpty()) {
                        hMitWeisung = hMitWeisung + "/";
                    }
                    hMitWeisung = hMitWeisung + "Dedizierte Weisung";
                }
                Label lblMitWeisung = new Label(hMitWeisung);
                lSammelGridPane.addMeeting(lblMitWeisung, 4, fuelleTabSammelkartenZeile);

                fuelleTabSammelkartenZeile++;
            }

        }
        scpnUmbuchen.setContent(lSammelGridPane);

    }

    /** The fuelle tab weisungssummen zeile. */
    int fuelleTabWeisungssummenZeile = 0;

    /**
     * Zeige agenda.
     */
    private void zeigeAgenda() {
        if ((aktuelleSammelMeldung.skWeisungsartZulaessig & 4) == 4) { /* Sonst keine Weisungen zulässig */

            /** Agendapunkte anzeigen */

            int anzahlAbstimmungenInsgesamt = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
            if (ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat) {
                /* +2 wg. Zwischenüberschrift "Agenda" und "Gegenanträge" */
                anzahlAbstimmungenInsgesamt = anzahlAbstimmungenInsgesamt + 2
                        + blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);
            }
            cbWeisungen = new ArrayList<>();
            weisungsListe = new ArrayList<>();

            MeetingGridPane lAbstimmungenGridPane = new MeetingGridPane();
            fuelleTabWeisungssummenZeile = 0;

            /** Combo-Box füllen */
            for (int i = 0; i <= 8; i++)
                if (i != 6)
                    weisungsListe.add(KonstStimmart.getText(i));

            fuelleAbstimmungPane(lAbstimmungenGridPane, 1);
            fuelleAbstimmungPane(lAbstimmungenGridPane, 2);

            scpnAbstimmungspunkte.setContent(lAbstimmungenGridPane);

        }
    }

    /**
     * pArt= 1 => "Normale" Agenda 2 => Gegenanträge.
     *
     * @param lWeisungenGridPane the l weisungen grid pane
     * @param pArt               the art
     */
    private void fuelleAbstimmungPane(MeetingGridPane lWeisungenGridPane, int pArt) {

        int anzahlAbstimmungen = 0;
        if (pArt == 1) {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        } else {
            anzahlAbstimmungen = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);
        }

        boolean agendaGegenantraegeGetrennt = ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;

        if (agendaGegenantraegeGetrennt) {
            lWeisungenGridPane.addMeeting(new Label("Weisungen"), 1, fuelleTabWeisungssummenZeile);
            lWeisungenGridPane.addMeeting(new Label(pArt == 1 ? "Normale Agenda" : "Gegenanträge"), 2,
                    fuelleTabWeisungssummenZeile);
            fuelleTabWeisungssummenZeile++;

        }

        for (int i = 0; i < anzahlAbstimmungen; i++) {
            EclAbstimmung lAbstimmung = null;
            if (pArt == 1) {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcAgendaArray[aktuelleGattung][i];
            } else {
                lAbstimmung = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray[aktuelleGattung][i];
            }

            int abstimmungsPosition = lAbstimmung.identWeisungssatz;

            Label lblNummer = new Label(lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey);
            lWeisungenGridPane.addMeeting(lblNummer, 0, fuelleTabWeisungssummenZeile);
            MeetingGridPane.setHalignment(lblNummer, HPos.CENTER);
            MeetingGridPane.setValignment(lblNummer, VPos.CENTER);

            ComboBox<String> cbWeisung = new ComboBox<String>(FXCollections.observableArrayList(weisungsListe));
            cbWeisungen.add(cbWeisung);
            if (abstimmungsPosition != -1)
                lWeisungenGridPane.addMeeting(cbWeisung, 1, fuelleTabWeisungssummenZeile);
            else
                lWeisungenGridPane.addMeeting(new Label(""), 1, fuelleTabWeisungssummenZeile);
            ObjectActions.filterComboBox(cbWeisung, null);

            Label lblAgendaText = new Label(lAbstimmung.kurzBezeichnung);
            lblAgendaText.setWrapText(true);
            lblAgendaText.setMaxWidth(500);
            lWeisungenGridPane.addMeeting(lblAgendaText, 2, fuelleTabWeisungssummenZeile);

            fuelleTabWeisungssummenZeile++;
        }
    }

    // private void zeigeNummernkreise() {
    // tfManuellVon.setText(Integer.toString(ParamS.param.paramNummernkreise.vonSubEintrittskartennummer[aktuelleGattung][1]));
    // tfManuellBis.setText(Integer.toString(ParamS.param.paramNummernkreise.bisSubEintrittskartennummer[aktuelleGattung][1]));
    //
    // tfSannekVon.setText(Integer.toString(ParamS.param.paramNummernkreise.vonSammelkartennummer));
    // tfSammelBis.setText(Integer.toString(ParamS.param.paramNummernkreise.bisSammelkartennummer));
    // }

    /**
     * ********************************* Logiken ********************************.
     */

    private EclMeldung aktuelleSammelMeldung = null;

    /** The aktuelle gattung. */
    private int aktuelleGattung = 0;

    /** The ausgewaehlte aktionaere meldung weisung. */
    private EclWeisungMeldung[] ausgewaehlteAktionaereMeldungWeisung = null;

    /** The ausgewaehlte aktionaere meldung. */
    private int[] ausgewaehlteAktionaereMeldung = null;

    /** The ausgewaehlte meldung mit aktionaers weisung. */
    private EclMeldungMitAktionaersWeisung[] ausgewaehlteMeldungMitAktionaersWeisung = null;

    /** The bl abstimmungen weisungen erfassen. */
    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage                             the eigene stage
     * @param pSammelMeldung                           the sammel meldung
     * @param pAusgewaehlteAktionaereMeldung           the ausgewaehlte aktionaere
     *                                                 meldung
     * @param pAusgewaehlteAktionaereMeldungWeisung    the ausgewaehlte aktionaere
     *                                                 meldung weisung
     * @param pAusgewaehlteMeldungMitAktionaersWeisung the ausgewaehlte meldung mit
     *                                                 aktionaers weisung
     * @param pBlAbstimmungenWeisungenErfassen         the bl abstimmungen weisungen
     *                                                 erfassen
     */
    public void init(Stage pEigeneStage, EclMeldung pSammelMeldung, int[] pAusgewaehlteAktionaereMeldung,
            EclWeisungMeldung[] pAusgewaehlteAktionaereMeldungWeisung,
            EclMeldungMitAktionaersWeisung[] pAusgewaehlteMeldungMitAktionaersWeisung,
            BlAbstimmungenWeisungen pBlAbstimmungenWeisungenErfassen) {
        eigeneStage = pEigeneStage;
        aktuelleSammelMeldung = pSammelMeldung;
        aktuelleGattung = aktuelleSammelMeldung.liefereGattung();
        ausgewaehlteAktionaereMeldung = pAusgewaehlteAktionaereMeldung;
        ausgewaehlteAktionaereMeldungWeisung = pAusgewaehlteAktionaereMeldungWeisung;
        ausgewaehlteMeldungMitAktionaersWeisung = pAusgewaehlteMeldungMitAktionaersWeisung;
        blAbstimmungenWeisungenErfassen = pBlAbstimmungenWeisungenErfassen;
    }

}
