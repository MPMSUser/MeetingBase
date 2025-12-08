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

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterPraesenzliste.
 */
public class CtrlParameterPraesenzliste extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane anzeige. */
    @FXML
    private TabPane tbPaneAnzeige;

    /** The tp stapeldruck. */
    @FXML
    private Tab tpStapeldruck;

    /** The tp einzeldruck. */
    @FXML
    private Tab tpEinzeldruck;

    /** The gp einzeldruck. */
    @FXML
    private GridPane gpEinzeldruck;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The lbl fehlermeldung. */
    @FXML
    private Label lblFehlermeldung;

    /***************Ab hier individuelle Deklarationen*******************/

    /**[0=Gesamtliste,1=Erstpräsenz,2=Nachtrag][0=Name,1=Ek,2=SK,3=Aktien Aufsteigend][Gattung 0-4]*/
    private CheckBox[][][] cbEinzeldruckInPDFaufnehmen = null;

    /** The tf einzeldruck format liste. */
    private TextField[][][] tfEinzeldruckFormatListe = null;

    /** The tf einzeldruck format zusammenstellung. */
    private TextField[][][] tfEinzeldruckFormatZusammenstellung = null;

    /** The l db bundle. */
    DbBundle lDbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tpStapeldruck != null : "fx:id=\"tpStapeldruck\" was not injected: check your FXML file 'ParameterPraesenzliste.fxml'.";
        assert tpEinzeldruck != null : "fx:id=\"tpEinzeldruck\" was not injected: check your FXML file 'ParameterPraesenzliste.fxml'.";
        assert gpEinzeldruck != null : "fx:id=\"gpEinzeldruck\" was not injected: check your FXML file 'ParameterPraesenzliste.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterPraesenzliste.fxml'.";
        assert lblFehlermeldung != null : "fx:id=\"lblFehlermeldung\" was not injected: check your FXML file 'ParameterPraesenzliste.fxml'.";

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
            }
        });

        lDbBundle = new DbBundle();
        lDbBundle.openAll();

        /****** Einzeldruck ********/
        cbEinzeldruckInPDFaufnehmen = new CheckBox[3][4][6];
        tfEinzeldruckFormatListe = new TextField[3][4][6];
        tfEinzeldruckFormatZusammenstellung = new TextField[3][4][6];
        int gridZeile = 1;
        for (int i = 0; i < 3; i++) {
            String ueberschriftText = "";
            switch (i) {
            case 0:
                ueberschriftText = "Gesamtpräsenz";
                break;
            case 1:
                ueberschriftText = "Erstpräsenz";
                break;
            case 2:
                ueberschriftText = "Nachtrag";
                break;
            }
            Label hLabel1 = new Label();
            hLabel1.setText(ueberschriftText);
            hLabel1.setStyle("-fx-font-weight: bold;");
            gpEinzeldruck.add(hLabel1, 0, gridZeile);
            gridZeile++;
            for (int i2 = 0; i2 <= 5; i2++) {/*0=alle, i2 = Gattung*/
                if (i2 == 0 || lDbBundle.param.paramBasis.getGattungAktiv(i2)) {
                    HBox hBox2 = new HBox();
                    Label hLabel2 = new Label();
                    if (i2 > 0) {
                        hLabel2.setText("   " + lDbBundle.param.paramBasis.getGattungBezeichnungKurz(i2) + "   ");
                    } else {
                        hLabel2.setText("   Alle");
                    }
                    hBox2.getChildren().add(hLabel2);

                    VBox vBox2 = new VBox();
                    Label hLabel2a = new Label();
                    hLabel2a.setText("FormatNr Liste");
                    vBox2.getChildren().add(hLabel2a);
                    Label hLabel2b = new Label();
                    hLabel2b.setText("FormatNr Zusammenst.");
                    vBox2.getChildren().add(hLabel2b);
                    Label hLabel2c = new Label();
                    hLabel2c.setText("in PDF aufn.");
                    vBox2.getChildren().add(hLabel2c);
                    hBox2.getChildren().add(vBox2);

                    gpEinzeldruck.add(hBox2, 0, gridZeile);

                    for (int i1 = 0; i1 < 4; i1++) {/*Sortierung*/
                        VBox vBox3 = new VBox();

                        tfEinzeldruckFormatListe[i][i1][i2] = new TextField();
                        tfEinzeldruckFormatListe[i][i1][i2]
                                .setText(lDbBundle.param.paramPraesenzliste.einzeldruckFormatListe[i][i1][i2]);
                        vBox3.getChildren().add(tfEinzeldruckFormatListe[i][i1][i2]);

                        tfEinzeldruckFormatZusammenstellung[i][i1][i2] = new TextField();
                        tfEinzeldruckFormatZusammenstellung[i][i1][i2].setText(
                                lDbBundle.param.paramPraesenzliste.einzeldruckFormatZusammenstellung[i][i1][i2]);
                        vBox3.getChildren().add(tfEinzeldruckFormatZusammenstellung[i][i1][i2]);

                        cbEinzeldruckInPDFaufnehmen[i][i1][i2] = new CheckBox();
                        if (lDbBundle.param.paramPraesenzliste.einzeldruckInPDFaufnehmen[i][i1][i2] == 1) {
                            cbEinzeldruckInPDFaufnehmen[i][i1][i2].setSelected(true);
                        }
                        vBox3.getChildren().add(cbEinzeldruckInPDFaufnehmen[i][i1][i2]);

                        gpEinzeldruck.add(vBox3, i1 + 1, gridZeile);

                    }
                    gridZeile++;
                }
            }
        }

        lDbBundle.closeAll();
    }

    /************************Logik***************************************************/
    private boolean speichernParameter() {

        /**************** Prüfen ***********************/
        String lFehlertext = "";

        /****** Einzeldruck ********/
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 <= 5; i2++) {/*0=alle, i2 = Gattung*/
                if (i2 == 0 || lDbBundle.param.paramBasis.getGattungAktiv(i2)) {
                    for (int i1 = 0; i1 < 4; i1++) {/*Sortierung*/
                        String hString = "";

                        hString = tfEinzeldruckFormatZusammenstellung[i][i1][i2].getText();
                        if ((!hString.isEmpty() && !CaString.isNummern(hString)) || hString.length() > 2) {
                            lFehlertext = "Format Zusammenstellung unzulässig (01 bis 99 oder leer)";
                        }

                        hString = tfEinzeldruckFormatListe[i][i1][i2].getText();
                        if ((!hString.isEmpty() && !CaString.isNummern(hString)) || hString.length() > 2) {
                            lFehlertext = "Format Liste unzulässig (01 bis 99 oder leer)";
                        }
                    }
                }
            }
        }

        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return false;
        }

        lDbBundle.openAll();
        /****** Einzeldruck ********/
        for (int i = 0; i < 3; i++) {
            for (int i2 = 0; i2 <= 5; i2++) {/*0=alle, i2 = Gattung*/
                if (i2 == 0 || lDbBundle.param.paramBasis.getGattungAktiv(i2)) {
                    for (int i1 = 0; i1 < 4; i1++) {/*Sortierung*/
                        String hString = "";

                        hString = tfEinzeldruckFormatZusammenstellung[i][i1][i2].getText();
                        if (hString.isEmpty()) {
                            hString = "00";
                        } else {
                            hString = CaString.fuelleLinksNull(Integer.toString(Integer.parseInt(hString)), 2);
                        }
                        ParamS.param.paramPraesenzliste.einzeldruckFormatListe[i][i1][i2] = hString;

                        hString = tfEinzeldruckFormatZusammenstellung[i][i1][i2].getText();
                        if (hString.isEmpty()) {
                            hString = "00";
                        } else {
                            hString = CaString.fuelleLinksNull(Integer.toString(Integer.parseInt(hString)), 2);
                        }
                        ParamS.param.paramPraesenzliste.einzeldruckFormatZusammenstellung[i][i1][i2] = hString;

                        if (cbEinzeldruckInPDFaufnehmen[i][i1][i2].isSelected()) {
                            ParamS.param.paramPraesenzliste.einzeldruckInPDFaufnehmen[i][i1][i2] = 1;
                        } else {
                            ParamS.param.paramPraesenzliste.einzeldruckInPDFaufnehmen[i][i1][i2] = 0;
                        }
                    }
                }
            }
        }

        /************ Speichern ******/

        lDbBundle.dbParameter.ergHVParam = ParamS.param;
        int rc = lDbBundle.dbParameter.updateHVParam_all();

        lDbBundle.closeAll();
        if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        }

        return true;
    }

    /**************************Anzeigefunktionen***************************************/
    
    public void init() {
    }

    /********************Aktionen auf Oberfläche*************************/

    @FXML
    void clickedSpeichern(ActionEvent event) {

        boolean rc = speichernParameter();

        if (rc == false) {
            return;
        }

        eigeneStage.hide();

    }

    /**
     * On tp changed.
     *
     * @param event the event
     */
    @FXML
    void onTpChanged(MouseEvent event) {

    }

}
