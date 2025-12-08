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
package de.meetingapps.meetingclient.meetingClientDialoge;

import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * The Class CtrlRootWeisungenNeu.
 */
public abstract class CtrlRootWeisungenNeu extends CtrlRoot {

    /** *********Oberflächen-Elemente - jeweils aus Designer********************. */

    @FXML
    protected ScrollPane scrpnWeisungen;

    /** The cb KIAV. */
    @FXML
    protected ComboBox<CbElement> cbKIAV;

    /** *********Oberflächen-Elemente - individuell***************************. */
    protected MeetingGridPane grpnWeisungen = null;

    /** The lbl agenda TOP. */
    protected Label[] lblAgendaTOP = null;

    /** The lbl gegenantraege TOP. */
    protected Label[] lblGegenantraegeTOP = null;

    /** The tf agenda weisungs eingabe. */
    protected TextField[] tfAgendaWeisungsEingabe = null;

    /** The tf gegenantraege weisungs eingabe. */
    protected TextField[] tfGegenantraegeWeisungsEingabe = null;

    /** The lbl agenda text. */
    protected Label[] lblAgendaText = null;

    /** The lbl gegenantraege text. */
    protected Label[] lblGegenantraegeText = null;

    /** The btn agenda zuruecksetzen. */
    protected Button[] btnAgendaZuruecksetzen = null;

    /** The btn gegenantraege zuruecksetzen. */
    protected Button[] btnGegenantraegeZuruecksetzen = null;

    /** The tg agenda TOP. */
    protected ToggleGroup[] tgAgendaTOP = null;

    /** The tg gegenantraege TOP. */
    protected ToggleGroup[] tgGegenantraegeTOP = null;

    /** The rb agenda ja. */
    protected RadioButton[] rbAgendaJa = null;

    /** The rb gegenantraege ja. */
    protected RadioButton[] rbGegenantraegeJa = null;

    /** The rb agenda nein. */
    protected RadioButton[] rbAgendaNein = null;

    /** The rb gegenantraege nein. */
    protected RadioButton[] rbGegenantraegeNein = null;

    /** The rb agenda enthaltung. */
    protected RadioButton[] rbAgendaEnthaltung = null;

    /** The rb gegenantraege enthaltung. */
    protected RadioButton[] rbGegenantraegeEnthaltung = null;

    /** The rb agenda nicht teilnahme. */
    protected RadioButton[] rbAgendaNichtTeilnahme = null;

    /** The rb gegenantraege nicht teilnahme. */
    protected RadioButton[] rbGegenantraegeNichtTeilnahme = null;

    /** The rb agenda ungueltig. */
    protected RadioButton[] rbAgendaUngueltig = null;

    /** The rb gegenantraege ungueltig. */
    protected RadioButton[] rbGegenantraegeUngueltig = null;

    /**
     * Werte, die nach dem Anzeigen der Agenda für die angezeigte Agenda gefüllt
     * werden.
     */
    protected int anzAgenda = 0;

    /** The anz gegenantraege. */
    protected int anzGegenantraege = 0;

    /**************Globale Variablen**********************************/

    /*++++++++++++++übergreifend++++++++++++++++++++++*/

    /**1=komplette Neuanmeldung; 
     * 2=Eingabe zu bestehender Meldung
     * 3=Ändern
     * 0=bei CtrlHVVollmachtWeisungSRV
     * 
     * Siehe KonstPortalAktion*/
    protected int hauptFunktion = 0;

    /**Wird (falls !=0) in init()Funktion der finalen Klasse gesetzt
     * 0 = In CtrlHVVollmachtWeisungSRV 
     * Ansonsten:
     * 4 = Vollmacht/Weisung an SRV
     * 5 = Briefwahl
     * 6 = KIAV
     * 31= Dauervollmacht
     * 35= Organisatorisch
     * Folgende "Änderungsfunktionen" werden immer über
     * CtrlVollmachtWeisungSRV abgewickelt (auch wenn KIAV oder Orga oder so!):
     * 10= Ändern VollmachtUndWeisungAnSRV
     * 11= Ändern Briefwahl
     * 12= Ändern VollamchtUndWeisungKIAV
     * 39= Ändern Dauervollmacht
     * 40= Ändern OrganistorischeSammelkarte
     * 
     * Siehe KonstPortalAktion
     */
      protected int ausgewaehlteFunktion = 0;

      /**Sammelkartenart, die gerade in Bearbeitung ist. Ist teilweise redundant zu
       * hauptFunktion / ausgewaehlteFunktion - aber eher jetzt das verwenden.
       * Wert wie KonstSkIst
       */
    protected int skIst = 0;

    /**1=ohne Weisung 
     * 2=mit Weisung 
     * 3=wie Vorschlag. */
    protected int ausgewaehlteWeisungsart = -1;

    /*+++++++++++zu aktueller Meldung+++++++++++++++++++++++++*/

    /** Gattung der aktuell zu verarbeitenden Meldung. */
    protected int aktuelleGattung = 0;

    /** The l db bundle. */
    protected DbBundle lDbBundle = null;

    /**
     * Für Änderungsmodus: wird über in init()-Funktion der finalen Klasse gesetzt.
     */
    //	@Deprecated
    //	protected EclAbstimmungenListeM abstimmungenListeM=null;

    protected BlAbstimmungenWeisungen blAbstimmungenWeisungen = null;

    /** The bl sammelkarten. */
    protected BlSammelkarten blSammelkarten = null;

    /** The button alle ja aktivieren. */
    protected boolean buttonAlleJaAktivieren = false;

    /** The button alle nein aktivieren. */
    protected boolean buttonAlleNeinAktivieren = false;

    /** The button alle enthaltung aktivieren. */
    protected boolean buttonAlleEnthaltungAktivieren = false;

    /** The button alle nicht teilnahme aktivieren. */
    protected boolean buttonAlleNichtTeilnahmeAktivieren = false;

    /** The button alle ungueltig aktivieren. */
    protected boolean buttonAlleUngueltigAktivieren = false;

    /** ************Fehlermeldungs-Grid anzeigen****************. */
    private GridPane grpnFehlermeldungen = null;

    /** The anz fehler in grid. */
    private int anzFehlerInGrid = 0;

    /**
     * Zeige gesamt buttons.
     */
    protected abstract void zeigeGesamtButtons();

    /**
     * Verberge gesamt buttons.
     */
    protected abstract void verbergeGesamtButtons();

    /**
     * Inits the fehler grid.
     */
    protected void initFehlerGrid() {
        anzFehlerInGrid = 0;
        grpnFehlermeldungen = new GridPane();
        scrpnWeisungen.setContent(grpnFehlermeldungen);
    }

    /**
     * Eintrag in fehler grid.
     *
     * @param pNummer  the nummer
     * @param pMeldung the meldung
     */
    protected void eintragInFehlerGrid(String pNummer, String pMeldung) {
        Label lblNummer = new Label(pNummer);
        grpnFehlermeldungen.add(lblNummer, 0, anzFehlerInGrid);

        Label lblMeldung = new Label(pMeldung);
        grpnFehlermeldungen.add(lblMeldung, 1, anzFehlerInGrid);
        anzFehlerInGrid++;

        scrpnWeisungen.setVvalue(1.0);
    }

    /**
     * Checks if is t abstimmung aktiv fuer eingabe.
     *
     * @param pAbstimmung the abstimmung
     * @return true, if is t abstimmung aktiv fuer eingabe
     */
    protected boolean istAbstimmungAktivFuerEingabe(EclAbstimmung pAbstimmung) {

        if (pAbstimmung.aktivWeisungenPflegeIntern == 0 && ausgewaehlteFunktion != 0) {
            return false;
        } //Pflege generell nicht aktiv

        if (pAbstimmung.aktivBeiSRV == 0
                && (ausgewaehlteFunktion == 0 || ausgewaehlteFunktion == 4 || ausgewaehlteFunktion == 10)) {
            return false; //Bei SRV nicht aktiv
        }

        if (pAbstimmung.aktivBeiBriefwahl == 0 && (ausgewaehlteFunktion == 5 || ausgewaehlteFunktion == 11)) {
            return false; //Bei Briefwahl nicht aktiv
        }

        if (pAbstimmung.aktivBeiKIAVDauer == 0 && (ausgewaehlteFunktion == 6 || ausgewaehlteFunktion == 12
                || ausgewaehlteFunktion == 31 || ausgewaehlteFunktion == 39)) {
            return false; //Bei KIAV+Dauervollmachten nicht aktiv
        }

        return true;
    }

    /**
     * Setzt buttonAlleJa* - muß dann in der aufrufenden Oberfläche aktiviert oder
     * deaktiviert werden.
     *
     * @param stimmartVorbelegenAgenda        the stimmart vorbelegen agenda
     * @param stimmartVorbelegenGegenantraege the stimmart vorbelegen gegenantraege
     * @param pNurAnzeige                     the nur anzeige
     */
    protected void zeigeAbstimmungFuerGattungAnNeu(int stimmartVorbelegenAgenda, int stimmartVorbelegenGegenantraege,
            boolean pNurAnzeige) {

        buttonAlleJaAktivieren = false;
        buttonAlleNeinAktivieren = false;
        buttonAlleEnthaltungAktivieren = false;
        buttonAlleNichtTeilnahmeAktivieren = false;
        buttonAlleUngueltigAktivieren = false;

        anzAgenda = blAbstimmungenWeisungen.liefereAnzAgendaArray(aktuelleGattung);
        anzGegenantraege = blAbstimmungenWeisungen.liefereAnzGegenantraegeArray(aktuelleGattung);

        lblAgendaTOP = new Label[anzAgenda];
        lblGegenantraegeTOP = new Label[anzGegenantraege];

        tfAgendaWeisungsEingabe = new TextField[anzAgenda];
        tfGegenantraegeWeisungsEingabe = new TextField[anzGegenantraege];

        lblAgendaText = new Label[anzAgenda];
        lblGegenantraegeText = new Label[anzGegenantraege];

        btnAgendaZuruecksetzen = new Button[anzAgenda];
        btnGegenantraegeZuruecksetzen = new Button[anzGegenantraege];

        tgAgendaTOP = new ToggleGroup[anzAgenda];
        tgGegenantraegeTOP = new ToggleGroup[anzGegenantraege];

        rbAgendaJa = new RadioButton[anzAgenda];
        rbGegenantraegeJa = new RadioButton[anzGegenantraege];

        rbAgendaNein = new RadioButton[anzAgenda];
        rbGegenantraegeNein = new RadioButton[anzGegenantraege];

        rbAgendaEnthaltung = new RadioButton[anzAgenda];
        rbGegenantraegeEnthaltung = new RadioButton[anzGegenantraege];

        rbAgendaNichtTeilnahme = new RadioButton[anzAgenda];
        rbGegenantraegeNichtTeilnahme = new RadioButton[anzGegenantraege];

        rbAgendaUngueltig = new RadioButton[anzAgenda];
        rbGegenantraegeUngueltig = new RadioButton[anzGegenantraege];

        grpnWeisungen = new MeetingGridPane(5, 15, false, false, true, false);

        boolean agendaGegenantraegeGetrennt = true;
        if (ausgewaehlteFunktion == 0) {
            agendaGegenantraegeGetrennt = ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat;
        } else {
            agendaGegenantraegeGetrennt = ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;
        }

        if (agendaGegenantraegeGetrennt && anzAgenda != 0 && anzGegenantraege != 0) {
            Label lblAgenda = new Label();
            lblAgenda.setText("Normale Agenda");
            grpnWeisungen.addMeeting(lblAgenda, 1, 0);
        }

        for (int i = 0; i < anzAgenda; i++) {
            EclAbstimmung lAbstimmung = blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i];

            lblAgendaTOP[i] = new Label();
            lblAgendaTOP[i].setText(lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey);
            grpnWeisungen.addMeeting(lblAgendaTOP[i], 0, i + 1);
            GridPane.setValignment(lblAgendaTOP[i], VPos.TOP);

            tfAgendaWeisungsEingabe[i] = new TextField();
            tfAgendaWeisungsEingabe[i].setMaxWidth(50);
            //			tfAgendaWeisungsEingabe[i].setOnKeyPressed(keyEventHandler);

            tfAgendaWeisungsEingabe[i].setOnKeyPressed(event -> triggerManuelleEingabe(event));
            TextField ltfAgendaWeisungsEingabe = tfAgendaWeisungsEingabe[i];
            tgAgendaTOP[i] = new ToggleGroup();

            rbAgendaJa[i] = new RadioButton("Ja");
            rbAgendaJa[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaJa = rbAgendaJa[i];
            rbAgendaJa[i].setOnAction(e -> stimmartGeKlicked(e));
            if (stimmartVorbelegenAgenda == KonstStimmart.ja && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                markierenAgendaJa(i);
            }

            rbAgendaNein[i] = new RadioButton("Nein");
            rbAgendaNein[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaNein = rbAgendaNein[i];
            rbAgendaNein[i].setOnAction(e -> stimmartGeKlicked(e));
            if (stimmartVorbelegenAgenda == KonstStimmart.nein && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                markierenAgendaNein(i);
            }

            rbAgendaEnthaltung[i] = new RadioButton("Enthaltung");
            rbAgendaEnthaltung[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaEnthaltung = rbAgendaEnthaltung[i];
            rbAgendaEnthaltung[i].setOnAction(e -> stimmartGeKlicked(e));
            if (stimmartVorbelegenAgenda == KonstStimmart.enthaltung && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                markierenAgendaEnthaltung(i);
            }

            rbAgendaNichtTeilnahme[i] = new RadioButton("Nicht-Teiln.");
            rbAgendaNichtTeilnahme[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaNichtTeilnahme = rbAgendaEnthaltung[i];
            rbAgendaNichtTeilnahme[i].setOnAction(e -> stimmartGeKlicked(e));
            if (stimmartVorbelegenAgenda == KonstStimmart.nichtTeilnahme
                    && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                markierenAgendaNichtTeilnahme(i);
            }

            rbAgendaUngueltig[i] = new RadioButton("Ungültig");
            rbAgendaUngueltig[i].setToggleGroup(tgAgendaTOP[i]);
            RadioButton lrbAgendaUngueltig = rbAgendaUngueltig[i];
            rbAgendaUngueltig[i].setOnAction(e -> stimmartGeKlicked(e));
            if (stimmartVorbelegenAgenda == KonstStimmart.ungueltig && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                markierenAgendaUngueltig(i);
            }

            btnAgendaZuruecksetzen[i] = new Button("Zurücksetzen");
            btnAgendaZuruecksetzen[i].setOnAction(e -> {
                lrbAgendaJa.setSelected(false);
                lrbAgendaNein.setSelected(false);
                lrbAgendaEnthaltung.setSelected(false);
                lrbAgendaNichtTeilnahme.setSelected(false);
                lrbAgendaUngueltig.setSelected(false);
                ltfAgendaWeisungsEingabe.setText(".");
            });

            if (!lAbstimmung.liefereIstUeberschift()) {
                grpnWeisungen.addMeeting(tfAgendaWeisungsEingabe[i], 1, i + 1);
                GridPane.setValignment(tfAgendaWeisungsEingabe[i], VPos.TOP);

                if (lAbstimmung.internJa == 1) {
                    buttonAlleJaAktivieren = true;
                    grpnWeisungen.addMeeting(rbAgendaJa[i], 2, i + 1);
                    GridPane.setValignment(rbAgendaJa[i], VPos.TOP);
                } else {
                    grpnWeisungen.addMeeting(new Label(""), 2, i + 1);
                }
                if (lAbstimmung.internNein == 1) {
                    buttonAlleNeinAktivieren = true;
                    grpnWeisungen.addMeeting(rbAgendaNein[i], 3, i + 1);
                    GridPane.setValignment(rbAgendaNein[i], VPos.TOP);
                } else {
                    grpnWeisungen.addMeeting(new Label(""), 32, i + 1);
                }
                if (lAbstimmung.internEnthaltung == 1) {
                    buttonAlleEnthaltungAktivieren = true;
                    grpnWeisungen.addMeeting(rbAgendaEnthaltung[i], 4, i + 1);
                    GridPane.setValignment(rbAgendaEnthaltung[i], VPos.TOP);
                } else {
                    grpnWeisungen.addMeeting(new Label(""), 4, i + 1);
                }
                if (lAbstimmung.internNichtTeilnahme == 1) {
                    buttonAlleNichtTeilnahmeAktivieren = true;
                    grpnWeisungen.addMeeting(rbAgendaNichtTeilnahme[i], 5, i + 1);
                    GridPane.setValignment(rbAgendaNichtTeilnahme[i], VPos.TOP);
                } else {
                    grpnWeisungen.addMeeting(new Label(""), 5, i + 1);
                }
                if (lAbstimmung.internUngueltig == 1) {
                    buttonAlleUngueltigAktivieren = true;
                    grpnWeisungen.addMeeting(rbAgendaUngueltig[i], 6, i + 1);
                    GridPane.setValignment(rbAgendaUngueltig[i], VPos.TOP);
                } else {
                    grpnWeisungen.addMeeting(new Label(""), 6, i + 1);
                }

                grpnWeisungen.addMeeting(btnAgendaZuruecksetzen[i], 7, i + 1);
                GridPane.setValignment(btnAgendaZuruecksetzen[i], VPos.TOP);

                if (!istAbstimmungAktivFuerEingabe(lAbstimmung) || pNurAnzeige) {
                    /*Ggf. Eingabe deaktivieren*/
                    if (pNurAnzeige) {
                        tfAgendaWeisungsEingabe[i].setEditable(false);
                        btnAgendaZuruecksetzen[i].setVisible(false);
                    } else {
                        tfAgendaWeisungsEingabe[i].setDisable(true);
                        btnAgendaZuruecksetzen[i].setDisable(true);
                    }
                    rbAgendaJa[i].setDisable(true);
                    rbAgendaNein[i].setDisable(true);
                    rbAgendaEnthaltung[i].setDisable(true);
                    rbAgendaNichtTeilnahme[i].setDisable(true);
                    rbAgendaUngueltig[i].setDisable(true);
                }

                if (hauptFunktion == 3) {/*Ändern*/
                    int abgebeneStimmart = blAbstimmungenWeisungen.holeAgendaWeisungMeldungPos(0, i, aktuelleGattung);
                    switch (abgebeneStimmart) {
                    case KonstStimmart.ja: {
                        markierenAgendaJa(i);
                        rbAgendaJa[i].setDisable(false);
                        break;
                    }
                    case KonstStimmart.nein: {
                        markierenAgendaNein(i);
                        rbAgendaNein[i].setDisable(false);
                        break;
                    }
                    case KonstStimmart.enthaltung: {
                        markierenAgendaEnthaltung(i);
                        rbAgendaEnthaltung[i].setDisable(false);
                        break;
                    }
                    case KonstStimmart.ungueltig: {
                        markierenAgendaUngueltig(i);
                        rbAgendaUngueltig[i].setDisable(false);
                        break;
                    }
                    case KonstStimmart.nichtTeilnahme: {
                        markierenAgendaNichtTeilnahme(i);
                        rbAgendaNichtTeilnahme[i].setDisable(false);
                        break;
                    }
                    }
                }

            }
            lblAgendaText[i] = new Label();
            lblAgendaText[i].setText(lAbstimmung.kurzBezeichnung);
            lblAgendaText[i].setWrapText(true);
            lblAgendaText[i].setMaxWidth(400);
            grpnWeisungen.addMeeting(lblAgendaText[i], 8, i + 1);
            GridPane.setValignment(lblAgendaText[i], VPos.TOP);
        }

        if (agendaGegenantraegeGetrennt) {

            if (anzGegenantraege > 0) {
                Label lblGegenantraege = new Label();
                lblGegenantraege.setText("Gegenanträge");
                grpnWeisungen.addMeeting(lblGegenantraege, 1, anzAgenda + 1);
            }

            for (int i = 0; i < anzGegenantraege; i++) {
                EclAbstimmung lAbstimmung = blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i];

                lblGegenantraegeTOP[i] = new Label();
                lblGegenantraegeTOP[i].setText(lAbstimmung.nummerKey + " " + lAbstimmung.nummerindexKey);
                grpnWeisungen.addMeeting(lblGegenantraegeTOP[i], 0, i + anzAgenda + 2);
                GridPane.setValignment(lblGegenantraegeTOP[i], VPos.TOP);

                tfGegenantraegeWeisungsEingabe[i] = new TextField();
                tfGegenantraegeWeisungsEingabe[i].setMaxWidth(50);
                tfGegenantraegeWeisungsEingabe[i].setOnKeyPressed(event -> triggerManuelleEingabe(event));
                TextField ltfGegenantraegeWeisungsEingabe = tfGegenantraegeWeisungsEingabe[i];

                tgGegenantraegeTOP[i] = new ToggleGroup();

                rbGegenantraegeJa[i] = new RadioButton("Ja");
                rbGegenantraegeJa[i].setToggleGroup(tgGegenantraegeTOP[i]);
                RadioButton lrbGegenantraegeJa = rbGegenantraegeJa[i];
                rbGegenantraegeJa[i].setOnAction(e -> stimmartGeKlicked(e));
                if (stimmartVorbelegenGegenantraege == KonstStimmart.ja && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                    markierenGegenantraegeJa(i);
                }

                rbGegenantraegeNein[i] = new RadioButton("Nein");
                rbGegenantraegeNein[i].setToggleGroup(tgGegenantraegeTOP[i]);
                RadioButton lrbGegenantraegeNein = rbGegenantraegeNein[i];
                rbGegenantraegeNein[i].setOnAction(e -> stimmartGeKlicked(e));
                if (stimmartVorbelegenGegenantraege == KonstStimmart.nein
                        && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                    markierenGegenantraegeNein(i);
                }

                rbGegenantraegeEnthaltung[i] = new RadioButton("Enthaltung");
                rbGegenantraegeEnthaltung[i].setToggleGroup(tgGegenantraegeTOP[i]);
                RadioButton lrbGegenantraegeEnthaltung = rbGegenantraegeEnthaltung[i];
                rbGegenantraegeEnthaltung[i].setOnAction(e -> stimmartGeKlicked(e));
                if (stimmartVorbelegenGegenantraege == KonstStimmart.enthaltung
                        && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                    markierenGegenantraegeEnthaltung(i);
                }

                rbGegenantraegeNichtTeilnahme[i] = new RadioButton("Nicht-Teiln.");
                rbGegenantraegeNichtTeilnahme[i].setToggleGroup(tgGegenantraegeTOP[i]);
                RadioButton lrbGegenantraegeNichtTeilnahme = rbGegenantraegeNichtTeilnahme[i];
                rbGegenantraegeNichtTeilnahme[i].setOnAction(e -> stimmartGeKlicked(e));
                if (stimmartVorbelegenGegenantraege == KonstStimmart.nichtTeilnahme
                        && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                    markierenGegenantraegeNichtTeilnahme(i);
                }

                rbGegenantraegeUngueltig[i] = new RadioButton("Ungültig");
                rbGegenantraegeUngueltig[i].setToggleGroup(tgGegenantraegeTOP[i]);
                RadioButton lrbGegenantraegeUngueltig = rbGegenantraegeUngueltig[i];
                rbGegenantraegeUngueltig[i].setOnAction(e -> stimmartGeKlicked(e));
                if (stimmartVorbelegenGegenantraege == KonstStimmart.ungueltig
                        && istAbstimmungAktivFuerEingabe(lAbstimmung)) {
                    markierenGegenantraegeUngueltig(i);
                }

                btnGegenantraegeZuruecksetzen[i] = new Button("Zurücksetzen");
                btnGegenantraegeZuruecksetzen[i].setOnAction(e -> {
                    lrbGegenantraegeJa.setSelected(false);
                    lrbGegenantraegeNein.setSelected(false);
                    lrbGegenantraegeEnthaltung.setSelected(false);
                    lrbGegenantraegeNichtTeilnahme.setSelected(false);
                    ;
                    lrbGegenantraegeUngueltig.setSelected(false);
                    ltfGegenantraegeWeisungsEingabe.setText(".");
                });

                if (!lAbstimmung.liefereIstUeberschift()) {
                    grpnWeisungen.addMeeting(tfGegenantraegeWeisungsEingabe[i], 1, i + anzAgenda + 2);
                    GridPane.setValignment(tfGegenantraegeWeisungsEingabe[i], VPos.TOP);

                    if (lAbstimmung.internJa == 1) {
                        buttonAlleJaAktivieren = true;
                        grpnWeisungen.addMeeting(rbGegenantraegeJa[i], 2, i + anzAgenda + 2);
                        GridPane.setValignment(rbGegenantraegeJa[i], VPos.TOP);
                    } else {
                        grpnWeisungen.addMeeting(new Label(""), 2, i + 1);
                    }
                    if (lAbstimmung.internNein == 1) {
                        buttonAlleNeinAktivieren = true;
                        grpnWeisungen.addMeeting(rbGegenantraegeNein[i], 3, i + anzAgenda + 2);
                        GridPane.setValignment(rbGegenantraegeNein[i], VPos.TOP);
                    } else {
                        grpnWeisungen.addMeeting(new Label(""), 3, i + 1);
                    }
                    if (lAbstimmung.internEnthaltung == 1) {
                        buttonAlleEnthaltungAktivieren = true;
                        grpnWeisungen.addMeeting(rbGegenantraegeEnthaltung[i], 4, i + anzAgenda + 2);
                        GridPane.setValignment(rbGegenantraegeEnthaltung[i], VPos.TOP);
                    } else {
                        grpnWeisungen.addMeeting(new Label(""), 4, i + 1);
                    }
                    if (lAbstimmung.internNichtTeilnahme == 1) {
                        buttonAlleNichtTeilnahmeAktivieren = true;
                        grpnWeisungen.addMeeting(rbGegenantraegeNichtTeilnahme[i], 5, i + anzAgenda + 2);
                        GridPane.setValignment(rbGegenantraegeNichtTeilnahme[i], VPos.TOP);
                    } else {
                        grpnWeisungen.addMeeting(new Label(""), 5, i + 1);
                    }
                    if (lAbstimmung.internUngueltig == 1) {
                        buttonAlleUngueltigAktivieren = true;
                        grpnWeisungen.addMeeting(rbGegenantraegeUngueltig[i], 6, i + anzAgenda + 2);
                        GridPane.setValignment(rbGegenantraegeUngueltig[i], VPos.TOP);
                    } else {
                        grpnWeisungen.addMeeting(new Label(""), 6, i + 1);
                    }

                    grpnWeisungen.addMeeting(btnGegenantraegeZuruecksetzen[i], 7, i + anzAgenda + 2);
                    GridPane.setValignment(btnGegenantraegeZuruecksetzen[i], VPos.TOP);

                    if (!istAbstimmungAktivFuerEingabe(lAbstimmung) || pNurAnzeige) {
                        /*Ggf. Eingabe deaktivieren*/
                        if (pNurAnzeige) {
                            tfGegenantraegeWeisungsEingabe[i].setEditable(false);
                            btnGegenantraegeZuruecksetzen[i].setVisible(false);
                        } else {
                            tfGegenantraegeWeisungsEingabe[i].setDisable(true);
                            btnGegenantraegeZuruecksetzen[i].setDisable(true);
                        }
                        rbGegenantraegeJa[i].setDisable(true);
                        rbGegenantraegeNein[i].setDisable(true);
                        rbGegenantraegeEnthaltung[i].setDisable(true);
                        rbGegenantraegeNichtTeilnahme[i].setDisable(true);
                        rbGegenantraegeUngueltig[i].setDisable(true);
                    }

                    if (hauptFunktion == 3) {/*Ändern*/
                        int abgebeneStimmart = blAbstimmungenWeisungen.holeGegenantraegeWeisungMeldungPos(0, i,
                                aktuelleGattung);
                        switch (abgebeneStimmart) {
                        case KonstStimmart.ja: {
                            markierenGegenantraegeJa(i);
                            rbGegenantraegeJa[i].setDisable(false);
                            break;
                        }
                        case KonstStimmart.nein: {
                            markierenGegenantraegeNein(i);
                            rbGegenantraegeNein[i].setDisable(false);
                            break;
                        }
                        case KonstStimmart.enthaltung: {
                            markierenGegenantraegeEnthaltung(i);
                            rbGegenantraegeEnthaltung[i].setDisable(false);
                            break;
                        }
                        case KonstStimmart.ungueltig: {
                            markierenGegenantraegeUngueltig(i);
                            rbGegenantraegeUngueltig[i].setDisable(false);
                            break;
                        }
                        case KonstStimmart.nichtTeilnahme: {
                            markierenGegenantraegeNichtTeilnahme(i);
                            rbGegenantraegeNichtTeilnahme[i].setDisable(false);
                            break;
                        }
                        }
                    }
                }
                lblGegenantraegeText[i] = new Label();
                lblGegenantraegeText[i].setText(lAbstimmung.kurzBezeichnung);
                lblGegenantraegeText[i].setWrapText(true);
                lblGegenantraegeText[i].setMaxWidth(400);
                grpnWeisungen.addMeeting(lblGegenantraegeText[i], 8, i + anzAgenda + 2);
                GridPane.setValignment(lblGegenantraegeText[i], VPos.TOP);
            }
        }
        scrpnWeisungen.setContent(grpnWeisungen);
        focusAufErsteZeile();
    }

    /**
     * Prüft, ob - gemäß ausgewaehlteWeisungsart und Parameter für Sammelkartenart -
     * alle Weisungen eingegeben wurden oder nicht. Bringt ggf. Fehlermeldung.
     *
     * @return true, if successful
     */
    protected boolean pruefeWeisungenVollstaendig() {
        /*Ermitteln, ob alle Weisungen selektiert, bzw. keine; zum Abgleich mit Sammelkartenart*/
        Boolean gefMarkiert = false;
        Boolean gefNichtmarkiert = false;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()
                    && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                Boolean gefEinzelMarkiert = false;
                if (rbAgendaJa[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbAgendaNein[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbAgendaEnthaltung[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbAgendaUngueltig[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (gefEinzelMarkiert == true) {
                    gefMarkiert = true;
                } else {
                    gefNichtmarkiert = true;
                }
            }
        }
        /*Gegenanträge - nur checken ob markiert - kein Zwang!*/
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()
                    && istAbstimmungAktivFuerEingabe(
                            blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                Boolean gefEinzelMarkiert = false;
                if (rbGegenantraegeJa[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbGegenantraegeNein[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbGegenantraegeEnthaltung[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (rbGegenantraegeUngueltig[i].isSelected()) {
                    gefEinzelMarkiert = true;
                }
                if (gefEinzelMarkiert == true) {
                    gefMarkiert = true;
                }
            }
        }

        if (ausgewaehlteWeisungsart == 2) {/*Weisungen zwingend*/
            boolean zwingendFuerArt = false;
            switch (ausgewaehlteFunktion) {
            case 0:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangWeisungHV;
                break;
            case 4:
            case 10:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangSRV;
                break;
            case 5:
            case 11:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangBriefwahl;
                break;
            case 6:
            case 12:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangKIAV;
                break;
            case 31:
            case 39:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangDauer;
                break;
            case 35:
            case 36:
                zwingendFuerArt = ParamS.param.paramAbstimmungParameter.eingabezwangOrg;
                break;
            }
            if (gefNichtmarkiert && zwingendFuerArt) {
                fehlerMeldung("Weisungen für diese Sammelkarte für die Standard-Agenda zwingend!");
                return false;
            }
        }
        if (ausgewaehlteWeisungsart == 1 && gefMarkiert) {/*Keine Weisungen zulässig*/
            fehlerMeldung("Weisungen für diese Sammelkarte nicht zulässig!");
            return false;
        }

        return true;
    }

    /**
     * pImmerKorrigieren=true => falls ein Fehler auftritt, wird automatisch das
     * betroffene auf ungültig gesetzt,
     * 
     * Returnwert: true=fortsetzen, false=abbrechen und manuelle korrigieren.
     *
     * @param pMeldeNr          the melde nr
     * @param pGattung          the gattung
     * @param pImmerKorrigieren the immer korrigieren
     * @return true, if successful
     */
    protected boolean pruefeGruppen(int pMeldeNr, int pGattung, boolean pImmerKorrigieren) {
        String fehlerString = "";
        blAbstimmungenWeisungen.pruefeGruppen(pMeldeNr, pGattung);
        for (int i = 1; i <= 10; i++) {
            if (blAbstimmungenWeisungen.gruppeIstGroesserMax(i)) {
                if (fehlerString.isEmpty()) {
                    fehlerString = "Fehler bei Abstimmungsgruppe: ";
                }
                fehlerString += " Gruppe " + Integer.toString(i) + ": Ist="
                        + Integer.toString(blAbstimmungenWeisungen.rcGruppenIst[i]) + " Max="
                        + Integer.toString(blAbstimmungenWeisungen.rcGruppenMax[i]);
            }
            if (pImmerKorrigieren) {
                blAbstimmungenWeisungen.korrigiereGruppe(pMeldeNr, pGattung, i);
            }
        }
        if (fehlerString.isEmpty() || pImmerKorrigieren) {
            return true;
        }
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        fehlerString += " (Abbruch = manuell korrigieren, Ungültig = Abstimmungen dieser Gruppen auf Ungültig setzen)";
        boolean bRC = caZeigeHinweis.zeige2Buttons(eigeneStage, fehlerString, "Abbruch", "Ungültig");
        if (bRC == false) {
            for (int i = 1; i <= 10; i++) {
                if (blAbstimmungenWeisungen.gruppeIstGroesserMax(i)) {
                    blAbstimmungenWeisungen.korrigiereGruppe(pMeldeNr, pGattung, i);
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Weisungseingaben speichern direkt.
     */
    protected void weisungseingabenSpeichernDirekt() {

        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                int stimmartEingabe = KonstStimmart.nichtMarkiert;
                if (rbAgendaJa[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.ja;
                }
                if (rbAgendaNein[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.nein;
                }
                if (rbAgendaEnthaltung[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.enthaltung;
                }
                if (rbAgendaUngueltig[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.ungueltig;
                }
                if (rbAgendaNichtTeilnahme[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.nichtTeilnahme;
                }
                blAbstimmungenWeisungen.speichereAgendaWeisungMeldungPos(0, i, aktuelleGattung, stimmartEingabe, skIst);
            }
        }

        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                int stimmartEingabe = KonstStimmart.nichtMarkiert;
                if (rbGegenantraegeJa[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.ja;
                }
                if (rbGegenantraegeNein[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.nein;
                }
                if (rbGegenantraegeEnthaltung[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.enthaltung;
                }
                if (rbGegenantraegeUngueltig[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.ungueltig;
                }
                if (rbGegenantraegeNichtTeilnahme[i].isSelected()) {
                    stimmartEingabe = KonstStimmart.nichtTeilnahme;
                }
                blAbstimmungenWeisungen.speichereGegenantraegeWeisungMeldungPos(0, i, aktuelleGattung, stimmartEingabe,
                        skIst);
            }
        }
    }

    /**
     * **************Funktionen für einzelne Markierung*********************.
     *
     * @param i the i
     */
    protected void markierenAgendaJa(int i) {
        rbAgendaJa[i].setSelected(true);
        tfAgendaWeisungsEingabe[i].setText("J");
    }

    /**
     * Markieren agenda nein.
     *
     * @param i the i
     */
    protected void markierenAgendaNein(int i) {
        rbAgendaNein[i].setSelected(true);
        tfAgendaWeisungsEingabe[i].setText("N");
    }

    /**
     * Markieren agenda enthaltung.
     *
     * @param i the i
     */
    protected void markierenAgendaEnthaltung(int i) {
        rbAgendaEnthaltung[i].setSelected(true);
        tfAgendaWeisungsEingabe[i].setText("E");
    }

    /**
     * Markieren agenda ungueltig.
     *
     * @param i the i
     */
    protected void markierenAgendaUngueltig(int i) {
        rbAgendaUngueltig[i].setSelected(true);
        tfAgendaWeisungsEingabe[i].setText("U");
    }

    /**
     * Markieren agenda nicht teilnahme.
     *
     * @param i the i
     */
    protected void markierenAgendaNichtTeilnahme(int i) {
        rbAgendaNichtTeilnahme[i].setSelected(true);
        tfAgendaWeisungsEingabe[i].setText("T");
    }

    /**
     * Markieren gegenantraege ja.
     *
     * @param i the i
     */
    protected void markierenGegenantraegeJa(int i) {
        rbGegenantraegeJa[i].setSelected(true);
        tfGegenantraegeWeisungsEingabe[i].setText("J");
    }

    /**
     * Markieren gegenantraege nein.
     *
     * @param i the i
     */
    protected void markierenGegenantraegeNein(int i) {
        rbGegenantraegeNein[i].setSelected(true);
        tfGegenantraegeWeisungsEingabe[i].setText("N");
    }

    /**
     * Markieren gegenantraege enthaltung.
     *
     * @param i the i
     */
    protected void markierenGegenantraegeEnthaltung(int i) {
        rbGegenantraegeEnthaltung[i].setSelected(true);
        tfGegenantraegeWeisungsEingabe[i].setText("E");
    }

    /**
     * Markieren gegenantraege ungueltig.
     *
     * @param i the i
     */
    protected void markierenGegenantraegeUngueltig(int i) {
        rbGegenantraegeUngueltig[i].setSelected(true);
        tfGegenantraegeWeisungsEingabe[i].setText("U");
    }

    /**
     * Markieren gegenantraege nicht teilnahme.
     *
     * @param i the i
     */
    protected void markierenGegenantraegeNichtTeilnahme(int i) {
        rbGegenantraegeNichtTeilnahme[i].setSelected(true);
        tfGegenantraegeWeisungsEingabe[i].setText("T");
    }

    /**
     * *****************************Funktionen für
     * Gesamtmarkierung*************************************.
     */
    protected void markierenAlleJa() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].internJa == 1
                        && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    markierenAgendaJa(i);
                } else {
                    gefNicht = 1;
                }
            }
        }

        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].internJa == 1
                        && istAbstimmungAktivFuerEingabe(
                                blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    markierenGegenantraegeJa(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten mit Ja belegt werden!");
        }
    }

    /**
     * Markieren alle nein.
     */
    protected void markierenAlleNein() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].internNein == 1
                        && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    markierenAgendaNein(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].internNein == 1
                        && istAbstimmungAktivFuerEingabe(
                                blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    markierenGegenantraegeNein(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten mit Nein belegt werden!");
        }
    }

    /**
     * Markieren alle enthaltung.
     */
    protected void markierenAlleEnthaltung() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].internEnthaltung == 1
                        && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    markierenAgendaEnthaltung(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].internEnthaltung == 1
                        && istAbstimmungAktivFuerEingabe(
                                blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    markierenGegenantraegeEnthaltung(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten mit Enthaltung belegt werden!");
        }
    }

    /**
     * Markieren alle nicht teilnahme.
     */
    protected void markierenAlleNichtTeilnahme() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].internNichtTeilnahme == 1
                        && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    markierenAgendaNichtTeilnahme(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {

                if (blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].internNichtTeilnahme == 1
                        && istAbstimmungAktivFuerEingabe(
                                blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    markierenGegenantraegeNichtTeilnahme(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten mit Nicht-Teilnahme belegt werden!");
        }
    }

    /**
     * Markieren alle ungueltig.
     */
    protected void markierenAlleUngueltig() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].internUngueltig == 1
                        && istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    markierenAgendaUngueltig(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].internUngueltig == 1
                        && istAbstimmungAktivFuerEingabe(
                                blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    markierenGegenantraegeUngueltig(i);
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten mit Ungültig belegt werden!");
        }
    }

    /**
     * Markieren alle zuruecksetzen.
     */
    protected void markierenAlleZuruecksetzen() {
        int gefNicht = 0;
        for (int i = 0; i < anzAgenda; i++) {
            if (!blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i].liefereIstUeberschift()) {

                if (istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i])) {
                    rbAgendaJa[i].setSelected(false);
                    rbAgendaNein[i].setSelected(false);
                    rbAgendaEnthaltung[i].setSelected(false);
                    rbAgendaUngueltig[i].setSelected(false);
                    tfAgendaWeisungsEingabe[i].setText(".");
                } else {
                    gefNicht = 1;
                }
            }
        }
        for (int i = 0; i < anzGegenantraege; i++) {
            if (!blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i].liefereIstUeberschift()) {
                if (istAbstimmungAktivFuerEingabe(blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i])) {
                    rbGegenantraegeJa[i].setSelected(false);
                    rbGegenantraegeNein[i].setSelected(false);
                    rbGegenantraegeEnthaltung[i].setSelected(false);
                    rbGegenantraegeUngueltig[i].setSelected(false);
                    tfGegenantraegeWeisungsEingabe[i].setText(".");
                } else {
                    gefNicht = 1;
                }
            }
        }
        if (gefNicht == 1) {
            fehlerMeldung("Achtung, nicht alle Punkte konten zurückgesetzt werden!");
        }
    }

    /**
     * Kiav anzeige aufbereiten neu.
     *
     * @param pSammelkarte the sammelkarte
     * @return the string
     */
    private String kiavAnzeigeAufbereitenNeu(EclMeldung pSammelkarte) {
        String hNummer = "<" + CaString.fuelleLinksNull(Integer.toString(pSammelkarte.meldungsIdent), 6) + ">";
        if (pSammelkarte.akzeptiertDedizierteWeisung()) {
            hNummer += "<mW>";
        }
        if (pSammelkarte.akzeptiertOhneWeisung()) {
            hNummer += "<oW>";
        }
        if (pSammelkarte.akzeptiertWieVorschlag()) {
            hNummer += "<gV>";
        }
        hNummer = hNummer + " " + pSammelkarte.liefereSammelkartenBezeichnungKomplettIntern();
        return hNummer;
    }

    /**
     * *******************************KIAV**********************************************************.
     * 
     * Zeigt ggf. Fehlermeldung an, wenn keine einzige entsprechende Karte, oder
     * wenn keine Standard-Karte findbar
     * 
     * Als "ausgewählt" wird gesetzt (in folgender Reihenfolge): 
     * > Wenn ausgewaehlteKIAV!=1, und diese Karte in der Auswahl, dann diese karte 
     * > Die Karte "Buchbar Papier", "Buchbar Internet", bzw. "Buchbar HV"
     *
     * auswahl: 1=Papier, 2=Internet, 3=HV
     * 
     * @param mussPraesentSein the muss praesent sein
     * @return true, if successful
     */
    public boolean zeigeKIAVFuerGattungAnNeu(boolean mussPraesentSein) {
        int ausgewaehlteKIAVAlt = blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung];
        blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] = -1;

        CbAllgemein cbAllgemein = new CbAllgemein(cbKIAV);
        int gefKarteVorbelegung = -1;
        int gefKarte = -1;
        for (int i = 0; i < blSammelkarten.rcSammelkartenFuerWeisungserfassungAnzahl[aktuelleGattung]; i++) {
            EclMeldung lMeldungSammelkarte = blSammelkarten.rcSammelkartenFuerWeisungserfassung[aktuelleGattung][i];

            if (mussPraesentSein == false || lMeldungSammelkarte.meldungIstPraesent() == 1) {
                CbElement eintrag = new CbElement();
                eintrag.anzeige = kiavAnzeigeAufbereitenNeu(lMeldungSammelkarte);
                eintrag.ident1 = lMeldungSammelkarte.meldungsIdent;
                if (blSammelkarten.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[aktuelleGattung] == lMeldungSammelkarte.meldungsIdent
                        && gefKarteVorbelegung == -1) {
                    cbAllgemein.addElementAusgewaehlt(eintrag);
                    gefKarteVorbelegung = 1;
                    gefKarte = 1;
                } else {
                    if (ausgewaehlteKIAVAlt == lMeldungSammelkarte.meldungsIdent) {
                        cbAllgemein.addElementAusgewaehlt(eintrag);
                        gefKarteVorbelegung = 1;
                        gefKarte = 1;
                    } else {
                        cbAllgemein.addElement(eintrag);
                        gefKarte = 1;
                    }
                }
            }
        }

        String fehlertext = "";
        if (gefKarte == -1) {
            fehlertext = "Keine Sammelkarte gefunden";
            if (mussPraesentSein) {
                fehlertext += " - ggf. Präsenz der Sammelkarten prüfen!";
            }
            fehlerMeldung(fehlertext);
            return false;
        }
        if (gefKarteVorbelegung == -1 && (skIst == KonstSkIst.srv || skIst == KonstSkIst.briefwahl
                || blSammelkarten.rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV == 3)) {
            fehlertext = "Keine Standard-Sammelkarte ";
            switch (blSammelkarten.rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV) {
            case 1: {
                fehlertext += "Internet";
                break;
            }
            case 2: {
                fehlertext += "Papier";
                break;
            }
            case 3: {
                fehlertext += "HV";
                break;
            }
            }
            fehlertext += " gefunden";
            if (mussPraesentSein) {
                fehlertext += " - ggf. Präsenz der Sammelkarten prüfen!";
            }
            fehlerMeldung(fehlertext);
            return false;
        }
        kiavChanged();
        return true;
    }

    /**
     * Kiav changed.
     */
    protected void kiavChanged() {
        CbElement erg = cbKIAV.getValue();
        if (erg == null) {
            return;
        } /*Entsteht beim "Clearen" der ComboBox*/
        blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] = erg.ident1;
        int offsetGewaehlteSammelkarte = blSammelkarten
                .sammelkartenListeFuerWeisungenLiefereOffsetAusgewaehlte(aktuelleGattung);

        if (blSammelkarten.rcSammelkartenFuerWeisungserfassung[aktuelleGattung][offsetGewaehlteSammelkarte]
                .akzeptiertDedizierteWeisung()) {
            scrpnWeisungen.setVisible(true);
            ausgewaehlteWeisungsart = 2;
            zeigeGesamtButtons();
        }
        if (blSammelkarten.rcSammelkartenFuerWeisungserfassung[aktuelleGattung][offsetGewaehlteSammelkarte]
                .akzeptiertOhneWeisung()) {
            scrpnWeisungen.setVisible(false);
            ausgewaehlteWeisungsart = 1;
            verbergeGesamtButtons();
        }
        if (blSammelkarten.rcSammelkartenFuerWeisungserfassung[aktuelleGattung][offsetGewaehlteSammelkarte]
                .akzeptiertWieVorschlag()) {
            scrpnWeisungen.setVisible(false);
            ausgewaehlteWeisungsart = 3;
            verbergeGesamtButtons();
        }
    }

    /**
     * Trigger manuelle eingabe.
     *
     * @param event the event
     */
    void triggerManuelleEingabe(KeyEvent event) {
        EclAbstimmung lAbstimmung = null;
        int lfdAgenda = -1;
        for (int i = 0; i < anzAgenda; i++) {
            if (event.getSource() == tfAgendaWeisungsEingabe[i]) {
                lfdAgenda = i;
                lAbstimmung = blAbstimmungenWeisungen.rcAgendaArray[aktuelleGattung][i];
            }
        }

        int lfdGegenantraege = -1;
        for (int i = 0; i < anzGegenantraege; i++) {
            if (event.getSource() == tfGegenantraegeWeisungsEingabe[i]) {
                lfdGegenantraege = i;
                lAbstimmung = blAbstimmungenWeisungen.rcGegenantraegeArray[aktuelleGattung][i];
            }
        }

        KeyCode keyEingabe = event.getCode();
        String neuerInhalt = "";

        //    	if (keyEingabe==KeyCode.ENTER){
        //    		enterInWeisungGedrueckt();
        //    		event.consume();
        //    		return;
        //    	}

        if ((keyEingabe == KeyCode.J && lAbstimmung.internJa == 1)
                || (keyEingabe == KeyCode.N && lAbstimmung.internNein == 1)
                || (keyEingabe == KeyCode.E && lAbstimmung.internEnthaltung == 1)
                || (keyEingabe == KeyCode.U && lAbstimmung.internUngueltig == 1)
                || (keyEingabe == KeyCode.T && lAbstimmung.internNichtTeilnahme == 1) || keyEingabe == KeyCode.PERIOD
                || keyEingabe == KeyCode.SPACE) {
            if (keyEingabe == KeyCode.J) {
                neuerInhalt = "J";
                if (lfdAgenda != -1) {
                    rbAgendaJa[lfdAgenda].setSelected(true);
                } else {
                    rbGegenantraegeJa[lfdGegenantraege].setSelected(true);
                }
            }
            if (keyEingabe == KeyCode.N) {
                neuerInhalt = "N";
                if (lfdAgenda != -1) {
                    rbAgendaNein[lfdAgenda].setSelected(true);
                } else {
                    rbGegenantraegeNein[lfdGegenantraege].setSelected(true);
                }
            }
            if (keyEingabe == KeyCode.E) {
                neuerInhalt = "E";
                if (lfdAgenda != -1) {
                    rbAgendaEnthaltung[lfdAgenda].setSelected(true);
                } else {
                    rbGegenantraegeEnthaltung[lfdGegenantraege].setSelected(true);
                }
            }
            if (keyEingabe == KeyCode.U) {
                neuerInhalt = "U";
                if (lfdAgenda != -1) {
                    rbAgendaUngueltig[lfdAgenda].setSelected(true);
                } else {
                    rbGegenantraegeUngueltig[lfdGegenantraege].setSelected(true);
                }
            }
            if (keyEingabe == KeyCode.T) {
                neuerInhalt = "T";
                if (lfdAgenda != -1) {
                    rbAgendaNichtTeilnahme[lfdAgenda].setSelected(true);
                } else {
                    rbGegenantraegeNichtTeilnahme[lfdGegenantraege].setSelected(true);
                }
            }
            if (keyEingabe == KeyCode.PERIOD) {
                neuerInhalt = ".";
                if (lfdAgenda != -1) {
                    rbAgendaJa[lfdAgenda].setSelected(false);
                    rbAgendaNein[lfdAgenda].setSelected(false);
                    rbAgendaEnthaltung[lfdAgenda].setSelected(false);
                    rbAgendaUngueltig[lfdAgenda].setSelected(false);
                    rbAgendaNichtTeilnahme[lfdAgenda].setSelected(false);
                } else {
                    rbGegenantraegeJa[lfdGegenantraege].setSelected(false);
                    rbGegenantraegeNein[lfdGegenantraege].setSelected(false);
                    rbAgendaEnthaltung[lfdGegenantraege].setSelected(false);
                    rbGegenantraegeUngueltig[lfdGegenantraege].setSelected(false);
                    rbGegenantraegeNichtTeilnahme[lfdGegenantraege].setSelected(false);
                }
            }
            if (lfdAgenda != -1) {
                btnAgendaZuruecksetzen[lfdAgenda].requestFocus();
                //  	       	 	if (lfdAgenda<anzAgenda-1 || anzGegenantraege>0) {
                //       			}
                //       			else {//Ende der Agenda erreicht
                //       				setzeFocusAufSpeichern();
                //       			}
            } else {
                btnGegenantraegeZuruecksetzen[lfdGegenantraege].requestFocus();
                //       			if (lfdGegenantraege<anzGegenantraege-1) {
                //        			}
                //       			else {
                //       				setzeFocusAufSpeichern();
                //       			}
            }
            if (lfdAgenda != -1) {
                tfAgendaWeisungsEingabe[lfdAgenda].setText(neuerInhalt);
            } else {
                tfGegenantraegeWeisungsEingabe[lfdGegenantraege].setText(neuerInhalt);
            }
        } else {
            return;
        }
        String eingabe = event.getCharacter();
        System.out.println("lfdAgenda=" + lfdAgenda + "lfdGegenantraege=" + lfdGegenantraege + " Eingabe=" + eingabe);

    }

    /**
     * Stimmart ge klicked.
     *
     * @param event the event
     */
    @FXML
    void stimmartGeKlicked(ActionEvent event) {
        for (int i = 0; i < tfAgendaWeisungsEingabe.length; i++) {
            if (event.getSource() == rbAgendaJa[i] || event.getSource() == rbAgendaNein[i]
                    || event.getSource() == rbAgendaEnthaltung[i] || event.getSource() == rbAgendaNichtTeilnahme[i]
                    || event.getSource() == rbAgendaUngueltig[i]) {
                if (rbAgendaJa[i].isSelected()) {
                    tfAgendaWeisungsEingabe[i].setText("J");
                }
                if (rbAgendaNein[i].isSelected()) {
                    tfAgendaWeisungsEingabe[i].setText("N");
                }
                if (rbAgendaEnthaltung[i].isSelected()) {
                    tfAgendaWeisungsEingabe[i].setText("E");
                }
                if (rbAgendaNichtTeilnahme[i].isSelected()) {
                    tfAgendaWeisungsEingabe[i].setText("T");
                }
                if (rbAgendaUngueltig[i].isSelected()) {
                    tfAgendaWeisungsEingabe[i].setText("U");
                }
            }
        }

        for (int i = 0; i < tfGegenantraegeWeisungsEingabe.length; i++) {
            if (event.getSource() == rbGegenantraegeJa[i] || event.getSource() == rbGegenantraegeNein[i]
                    || event.getSource() == rbGegenantraegeEnthaltung[i]
                    || event.getSource() == rbGegenantraegeNichtTeilnahme[i]
                    || event.getSource() == rbGegenantraegeUngueltig[i]) {
                if (rbGegenantraegeJa[i].isSelected()) {
                    tfGegenantraegeWeisungsEingabe[i].setText("J");
                }
                if (rbGegenantraegeNein[i].isSelected()) {
                    tfGegenantraegeWeisungsEingabe[i].setText("N");
                }
                if (rbGegenantraegeEnthaltung[i].isSelected()) {
                    tfGegenantraegeWeisungsEingabe[i].setText("E");
                }
                if (rbGegenantraegeNichtTeilnahme[i].isSelected()) {
                    tfGegenantraegeWeisungsEingabe[i].setText("T");
                }
                if (rbGegenantraegeUngueltig[i].isSelected()) {
                    tfGegenantraegeWeisungsEingabe[i].setText("U");
                }
            }
        }
    }

    /**
     * Focus auf erste zeile.
     */
    protected void focusAufErsteZeile() {
        if (tfAgendaWeisungsEingabe.length > 0) {
            tfAgendaWeisungsEingabe[0].requestFocus();
            return;
        }
        if (tfGegenantraegeWeisungsEingabe.length > 0) {
            tfGegenantraegeWeisungsEingabe[0].requestFocus();
            return;
        }
        return;
    }

}
