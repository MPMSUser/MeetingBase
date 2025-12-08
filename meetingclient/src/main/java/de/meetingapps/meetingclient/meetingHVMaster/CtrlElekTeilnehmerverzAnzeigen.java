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

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompEintrittskarte;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompName;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompStimmkarte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlElekTeilnehmerverzAnzeigen.
 */
public class CtrlElekTeilnehmerverzAnzeigen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The pn pane. */
    @FXML
    private Pane pnPane;

    /** The lbl praesenz nr drucken. */
    @FXML
    private Label lblPraesenzNrDrucken;

    /** The tf stimmblock. */
    @FXML
    private TextField tfStimmblock;

    /**
     * Btn stimmblock.
     *
     * @param event the event
     */
    @FXML
    void btnStimmblock(ActionEvent event) {

    }

    /** The abbruch. */
    String abbruch = "";

    /**
     * On key pane.
     *
     * @param event the event
     */
    @SuppressWarnings("incomplete-switch")
    @FXML
    void onKeyPane(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (abbruch.compareTo("2007") == 0) {
                eigeneStage.hide();
                return;
            } else {
                tfStimmblock.requestFocus();
                return;
            }
        }
        switch (event.getCode()) {
        case DIGIT7: {
            abbruch = abbruch + "7";
            break;
        }
        case DIGIT2: {
            abbruch = abbruch + "2";
            break;
        }
        case DIGIT0: {
            abbruch = abbruch + "0";
            break;
        }
        }
        System.out.println(abbruch);
    }

    /**
     * On key stimmblock nr.
     *
     * @param event the event
     */
    @FXML
    void onKeyStimmblockNr(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfStimmblock.getText().compareTo("ENDE") == 0) {
                pnPane.requestFocus();
                abbruch = "";
                return;
            }

            /*Einlesen aktuellen Satz etc.*/

            /*Liste neu einlesen*/
            listeLesen();

            /*Anzeigen Teilnehmerliste*/

            Stage neuerDialog = new Stage();

            CtrlElekTeilnehmerverzAnzeigenListe controllerFenster = new CtrlElekTeilnehmerverzAnzeigenListe();

            controllerFenster.init(neuerDialog, eingelesenPraesenzliste);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ElekTeilnehmerverzAnzeigenListe.fxml"));
            loader.setController(controllerFenster);
            Parent mainPane = null;
            try {
                mainPane = (Parent) loader.load();
            } catch (IOException e) {
                CaBug.drucke("CtrlElekTeilnehmerverzAnzeigen.onKeyStimmblockNr 001");
                e.printStackTrace();
            }
            Scene scene = new Scene(mainPane, 1200, 850);
            neuerDialog.setTitle("Elektronisches Teilnehmerverzeichnis AnzeigenListe");
            neuerDialog.setScene(scene);
            neuerDialog.initModality(Modality.APPLICATION_MODAL);
            neuerDialog.showAndWait();

            tfStimmblock.setText("");

        }

    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert pnPane != null : "fx:id=\"pnPane\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigen.fxml'.";
        assert lblPraesenzNrDrucken != null : "fx:id=\"lblPraesenzNrDrucken\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigen.fxml'.";
        assert tfStimmblock != null : "fx:id=\"tfStimmblock\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigen.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/

    }

    /** The eingelesen praesenzliste. */
    private EclPraesenzliste[] eingelesenPraesenzliste = null;

    /** The date next update. */
    private Date dateNextUpdate = null;

    /** The differenz. */
    private long differenz = 20 * 1000; /*Erste Zahl = Anzahl Sekunden, die gewartet werden soll*/

    /**
     * Liste lesen.
     */
    void listeLesen() {

        Date aktuelleZeit = new Date();

        if (eingelesenPraesenzliste == null) {
            System.out.println("Null!");
            dateNextUpdate = new Date();
        }
        System.out.println("aktuelle Zeit=" + aktuelleZeit.getTime() + "  nextupdate=" + dateNextUpdate.getTime());

        if (eingelesenPraesenzliste == null || aktuelleZeit.getTime() >= dateNextUpdate.getTime() + differenz) {

            dateNextUpdate = new Date();
            System.out.println("Aktualisiert");

            DbBundle pDbBundle = new DbBundle();
            pDbBundle.openAll();

            int rc = pDbBundle.dbPraesenzliste.read_all();

            if (rc > 0) {

                /*Nicht zu druckende ausfiltern*/

                /*Bei offengelegten Sammelkarten: Sammelkarten-Bewegung selbst rausnehmen.
                 * Außerdem Sortier-Feld belegen (bei nicht Offenlegung!)*/
                for (int i = 0; i < rc; i++) {
                    EclPraesenzliste lEclPraesenzliste = pDbBundle.dbPraesenzliste.ergebnisPosition(i);
                    if (
                    /*Element ist selbst eine Sammelkarte, die offengelegt wird => nicht drucken*/
                    (lEclPraesenzliste.meldungstyp == 2 && lEclPraesenzliste.meldungSkOffenlegung == 1)) {
                        lEclPraesenzliste.drucken = 0;

                    }
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        lEclPraesenzliste.sortierName = lEclPraesenzliste.sammelkartenName + " "
                                + lEclPraesenzliste.sammelkartenVorname + " " + lEclPraesenzliste.sammelkartenOrt;
                    } else {
                        lEclPraesenzliste.sortierName = lEclPraesenzliste.aktionaerName + " "
                                + lEclPraesenzliste.aktionaerVorname + " " + lEclPraesenzliste.aktionaerOrt;
                    }
                }

                /*Mehrfach Zu-/Abgänge*/

                /*Sortieren*/
                int sortierung = 1;
                switch (sortierung) {
                case 1: {
                    Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompName();
                    Arrays.sort(pDbBundle.dbPraesenzliste.ergebnis(), comp);
                    break;
                }
                case 2: {
                    Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompEintrittskarte();
                    Arrays.sort(pDbBundle.dbPraesenzliste.ergebnis(), comp);
                    break;
                }
                case 3: {
                    Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompStimmkarte();
                    Arrays.sort(pDbBundle.dbPraesenzliste.ergebnis(), comp);
                    break;
                }

                }
                eingelesenPraesenzliste = pDbBundle.dbPraesenzliste.ergebnis();
            }
            pDbBundle.closeAll();
        }

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
