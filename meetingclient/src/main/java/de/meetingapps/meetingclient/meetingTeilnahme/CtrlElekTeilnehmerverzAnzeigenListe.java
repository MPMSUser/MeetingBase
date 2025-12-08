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
package de.meetingapps.meetingclient.meetingTeilnahme;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The Class CtrlElekTeilnehmerverzAnzeigenListe.
 */
public class CtrlElekTeilnehmerverzAnzeigenListe {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The pane basis. */
    @FXML
    private Pane paneBasis;

    /** The im logo. */
    @FXML
    private ImageView imLogo;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The scpane basis. */
    @FXML
    private ScrollPane scpaneBasis;

    /** The grpn pane. */
    @FXML
    private GridPane grpnPane;

    /** The btn auswahl. */
    @FXML
    private Button btnAuswahl;

    /** The btn ende. */
    @FXML
    private Button btnEnde;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** The btn vor. */
    @FXML
    private Button btnVor;

    /** The btn anfang. */
    @FXML
    private Button btnAnfang;

    /** The letzte taste. */
    String letzteTaste = "";

    /**
     * On taste.
     *
     * @param event the event
     */
    @FXML
    void onTaste(KeyEvent event) {

        switch (event.getCode()) {
        case DIGIT0:
            letzteTaste = "0";
            break;
        case DIGIT1:
            letzteTaste = "1";
            break;

        case DIGIT2:
            letzteTaste = "2";
            break;
        case DIGIT3:
            letzteTaste = "3";
            break;
        case DIGIT4:
            letzteTaste = "4";
            break;
        case DIGIT5:
            letzteTaste = "5";
            break;
        case DIGIT6:
            letzteTaste = "6";
            break;
        case DIGIT7:
            letzteTaste = "7";
            break;
        case DIGIT8:
            letzteTaste = "8";
            break;
        case DIGIT9:
            letzteTaste = "9";
            break;
        default:
            letzteTaste = "";
            break;
        }
        eigeneStage.hide();
    }

    /**
     * Clicked btn anfang.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAnfang(ActionEvent event) {
        angezeigteSeite = 1;
        anzeigen();
    }

    /**
     * Clicked btn emde.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnEmde(ActionEvent event) {
        angezeigteSeite = anzahlSeiten;
        anzeigen();
    }

    /**
     * Clicked btn vor.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnVor(ActionEvent event) {
        if (angezeigteSeite < anzahlSeiten) {
            angezeigteSeite++;
        }
        anzeigen();
    }

    /**
     * Clicked btn zurueck.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnZurueck(ActionEvent event) {
        if (angezeigteSeite > 1) {
            angezeigteSeite--;
        }
        anzeigen();

    }

    /** The anzeigen alle. */
    private boolean anzeigenAlle = false;

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {

        eigeneStage.hide();
        return;
    }

    /**
     * Clicked btn auswahl.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAuswahl(ActionEvent event) {
        if (anzeigenAlle == false) {
            anzeigenAlle = true;
        } else {
            anzeigenAlle = false;
        }
        anzeigen();
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert paneBasis != null : "fx:id=\"paneBasis\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert imLogo != null : "fx:id=\"imLogo\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert scpaneBasis != null : "fx:id=\"scpaneBasis\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert grpnPane != null : "fx:id=\"grpnPane\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnAuswahl != null : "fx:id=\"btnAuswahl\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnEnde != null : "fx:id=\"btnEnde\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnZurueck != null : "fx:id=\"btnZurueck\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnVor != null : "fx:id=\"btnVor\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert btnAnfang != null : "fx:id=\"btnAnfang\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/
        paneBasis.setStyle("-fx-background-color: #ffffff");
        scpaneBasis.setStyle("-fx-background: #ffffff");
        grpnPane.setStyle("-fx-background-color: #ffffff");

        anzeigenAlle = false;

        anzahlInsgesamt = eingelesenPraesenzliste.length;

        anzahlSeiten = (anzahlInsgesamt - 1) / anzahlProSeite + 1;
        angezeigteSeite = 1;

        anzeigen();

    }

    /** The anzahl pro seite. */
    int anzahlProSeite = 7;

    /** The anzahl insgesamt. */
    int anzahlInsgesamt = 0;

    /** The anzahl seiten. */
    int anzahlSeiten = 0;

    /** The angezeigte seite. */
    int angezeigteSeite = 0;

    /**
     * Anzeigen.
     */
    private void anzeigen() {

        grpnPane.getChildren().clear();

        if (eingelesenPraesenzliste == null) {
            return;
        }

        int von, bis;
        if (anzeigenAlle) {
            von = (angezeigteSeite - 1) * 7;
            bis = angezeigteSeite * 7;
            if (bis > eingelesenPraesenzliste.length) {
                bis = eingelesenPraesenzliste.length;
            }
        } else {
            von = 0;
            bis = eingelesenPraesenzliste.length;
        }
        int anzeigeOffset = 0;
        for (int i = von; i < bis; i++) {

            EclPraesenzliste lEclPraesenzliste = eingelesenPraesenzliste[i];
            if (anzeigenAlle == true || lEclPraesenzliste.meldeIdentAktionaer == meldeIdent) {

                //			if (lEclPraesenzliste.drucken==1){

                String kurzbezeichnung = "";
                switch (lEclPraesenzliste.willenserklaerung) {

                case KonstWillenserklaerung.wiederzugang: {
                    kurzbezeichnung = "Zu";
                    break;
                }
                case KonstWillenserklaerung.zugangInSRV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInOrga: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInDauervollmacht: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInKIAV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.erstzugang: {
                    kurzbezeichnung = "Zu";
                    break;
                }
                case KonstWillenserklaerung.abgangAusSRV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusOrga: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusDauervollmacht: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusKIAV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgang: {
                    kurzbezeichnung = "Ab";
                    break;
                }
                case KonstWillenserklaerung.wechselInSRV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInOrga: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInDauervollmacht: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInKIAV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnKIAV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.vertreterwechsel: {
                    kurzbezeichnung = "We";
                    break;
                }

                }

                String hname = lEclPraesenzliste.aktionaerName;
                String hvorname = lEclPraesenzliste.aktionaerVorname;
                String hort = lEclPraesenzliste.aktionaerOrt;
                String hbesitz = lEclPraesenzliste.besitzartKuerzel;
                if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                    hname = lEclPraesenzliste.sammelkartenName;
                    hvorname = lEclPraesenzliste.sammelkartenVorname;
                    hort = lEclPraesenzliste.sammelkartenOrt;
                    hbesitz = "V";

                }

                int fontGroesse = 16;
                String fontName = "System";

                Label label1 = new Label();
                label1.setText(lEclPraesenzliste.stimmkarte);
                label1.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label1, 0, anzeigeOffset * 2);

                Label label2 = new Label();
                label2.setText(lEclPraesenzliste.zutrittsIdent);
                label2.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label2, 1, anzeigeOffset * 2);

                Label label3 = new Label();
                String hString3 = hname;
                if (!hvorname.isEmpty()) {
                    hString3 = hString3 + ", " + hvorname;
                }
                label3.setText(hString3);
                label3.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label3, 2, anzeigeOffset * 2);

                Label label3a = new Label();
                label3a.setText(hort);
                label3a.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label3a, 2, anzeigeOffset * 2 + 1);

                if (lEclPraesenzliste.vertreterName != null) {
                    Label label4 = new Label();
                    String hString4 = lEclPraesenzliste.vertreterName;
                    if (!lEclPraesenzliste.vertreterVorname.isEmpty()) {
                        hString4 = hString4 + ", " + lEclPraesenzliste.vertreterVorname;
                    }
                    label4.setText(hString4);
                    label4.setFont(Font.font(fontName, fontGroesse));
                    grpnPane.add(label4, 3, anzeigeOffset * 2);

                    Label label4a = new Label();
                    label4a.setText(lEclPraesenzliste.vertreterOrt);
                    label4a.setFont(Font.font(fontName, fontGroesse));
                    grpnPane.add(label4a, 3, anzeigeOffset * 2 + 1);
                }

                Label label5 = new Label();
                label5.setText(Long.toString(lEclPraesenzliste.stimmen));
                label5.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label5, 4, anzeigeOffset * 2);

                Label label6 = new Label();
                label6.setText(hbesitz);
                label6.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label6, 5, anzeigeOffset * 2);

                Label label7 = new Label();
                label7.setText(kurzbezeichnung);
                label7.setFont(Font.font(fontName, fontGroesse));
                grpnPane.add(label7, 6, anzeigeOffset * 2);

                anzeigeOffset++;

                //			}
            }
        }
        if (anzeigenAlle == true) {
            btnAuswahl.setText("Nur ausgewählten Stimmblock anzeigen");
            if (angezeigteSeite != 1) {
                btnAnfang.setVisible(true);
                btnZurueck.setVisible(true);
            } else {
                btnAnfang.setVisible(false);
                btnZurueck.setVisible(false);
            }

            if (angezeigteSeite != anzahlSeiten) {
                btnEnde.setVisible(true);
                btnVor.setVisible(true);
            } else {
                btnEnde.setVisible(false);
                btnVor.setVisible(false);
            }

        } else {
            btnAuswahl.setText("Alle anzeigen");
            btnEnde.setVisible(false);
            btnZurueck.setVisible(false);
            btnVor.setVisible(false);
            btnAnfang.setVisible(false);
        }

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The eingelesen praesenzliste. */
    private EclPraesenzliste[] eingelesenPraesenzliste = null;

    /** The melde ident. */
    private int meldeIdent = 0;

    /**
     * Inits the.
     *
     * @param pEigeneStage             the eigene stage
     * @param pEingelesenPraesenzliste the eingelesen praesenzliste
     * @param pMeldeIdent              the melde ident
     */
    public void init(Stage pEigeneStage, EclPraesenzliste[] pEingelesenPraesenzliste, int pMeldeIdent) {
        eigeneStage = pEigeneStage;
        eingelesenPraesenzliste = pEingelesenPraesenzliste;
        meldeIdent = pMeldeIdent;
    }

}
