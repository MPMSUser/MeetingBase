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

import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The grpn pane. */
    @FXML
    private GridPane grpnPane;

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
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";
        assert grpnPane != null : "fx:id=\"grpnPane\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigenListe.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/
        anzeigen();

    }

    /**
     * Anzeigen.
     */
    private void anzeigen() {
        for (int i = 0; i < eingelesenPraesenzliste.length; i++) {

            EclPraesenzliste lEclPraesenzliste = eingelesenPraesenzliste[i];

            if (lEclPraesenzliste.drucken == 1) {

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

                Label label1 = new Label();
                label1.setText(lEclPraesenzliste.stimmkarte);
                grpnPane.add(label1, 0, i * 2);

                Label label2 = new Label();
                label2.setText(lEclPraesenzliste.zutrittsIdent);
                grpnPane.add(label2, 1, i * 2);

                Label label3 = new Label();
                String hString3 = hname;
                if (!hvorname.isEmpty()) {
                    hString3 = hString3 + ", " + hvorname;
                }
                label3.setText(hString3);
                grpnPane.add(label3, 2, i * 2);

                Label label3a = new Label();
                label3a.setText(hort);
                grpnPane.add(label3a, 2, i * 2 + 1);

                if (lEclPraesenzliste.vertreterName != null) {
                    Label label4 = new Label();
                    String hString4 = lEclPraesenzliste.vertreterName;
                    if (!lEclPraesenzliste.vertreterVorname.isEmpty()) {
                        hString4 = hString4 + ", " + lEclPraesenzliste.vertreterVorname;
                    }
                    label4.setText(hString4);
                    grpnPane.add(label4, 3, i * 2);

                    Label label4a = new Label();
                    label4a.setText(lEclPraesenzliste.vertreterOrt);
                    grpnPane.add(label4a, 3, i * 2 + 1);
                }

                Label label5 = new Label();
                label5.setText(Long.toString(lEclPraesenzliste.stimmen));
                grpnPane.add(label5, 4, i * 2);

                Label label6 = new Label();
                label6.setText(hbesitz);
                grpnPane.add(label6, 5, i * 2);

            }

        }

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The eingelesen praesenzliste. */
    private EclPraesenzliste[] eingelesenPraesenzliste = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage             the eigene stage
     * @param pEingelesenPraesenzliste the eingelesen praesenzliste
     */
    public void init(Stage pEigeneStage, EclPraesenzliste[] pEingelesenPraesenzliste) {
        eigeneStage = pEigeneStage;
        eingelesenPraesenzliste = pEingelesenPraesenzliste;
    }

}
