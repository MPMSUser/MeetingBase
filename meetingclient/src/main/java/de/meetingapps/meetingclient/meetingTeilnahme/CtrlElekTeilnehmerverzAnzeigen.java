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

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompEintrittskarte;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompName;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * The Class CtrlElekTeilnehmerverzAnzeigen.
 */
public class CtrlElekTeilnehmerverzAnzeigen extends CtrlRoot {

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

    /** The im logo. */
    @FXML
    private ImageView imLogo;

    /**
     * Btn stimmblock.
     *
     * @param event the event
     */
    @FXML
    void btnStimmblock(ActionEvent event) {

    }

    /** The counter service. */
    CounterService counterService = new CounterService();

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

    /** The letzte taste lokal. */
    String letzteTasteLokal = "";

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

            int meldungsIdent = 0;

            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAll();

            /* Einlesen aktuellen Satz etc. */
            String stimmkartennummer = letzteTasteLokal + tfStimmblock.getText();

            letzteTasteLokal = "";

            if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet) { //1:1 - Eintrittskartennummer
                BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                int erg = blNummernformen.dekodiere(stimmkartennummer, KonstKartenklasse.eintrittskartennummer);
                if (erg < 0) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                EclZutrittsIdent zutrittsIdent = new EclZutrittsIdent();
                zutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
                zutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);

                lDbBundle.dbZutrittskarten.read(zutrittsIdent, 1);
                if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                EclZutrittskarten eclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                if (eclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                meldungsIdent = eclZutrittskarten.meldungsIdentAktionaer;
            } else {//Beliebig - Stimmkartennummer
                System.out.println("Teilnehmer A");
                BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                int erg = blNummernformen.dekodiere(stimmkartennummer, KonstKartenklasse.stimmkartennummer);
                if (erg < 0) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                stimmkartennummer = blNummernformen.rcIdentifikationsnummer.get(0);
                System.out.println("Teilnehmer B");

                lDbBundle.dbStimmkarten.read(stimmkartennummer);
                if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                EclStimmkarten eclStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
                if (eclStimmkarte.stimmkarteIstGesperrt == 2) {
                    tfStimmblock.setText("");
                    lDbBundle.closeAll();
                    return;
                }
                meldungsIdent = eclStimmkarte.meldungsIdentAktionaer;
                System.out.println("Teilnehmer C");

            }

            /* Liste neu einlesen */
            anzeigenPraesenzliste = eingelesenPraesenzliste;

            /* Anzeigen Teilnehmerliste */

            Stage neuerDialog = new Stage();

            CtrlElekTeilnehmerverzAnzeigenListe controllerFenster = new CtrlElekTeilnehmerverzAnzeigenListe();

            controllerFenster.init(neuerDialog, anzeigenPraesenzliste, meldungsIdent);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ElekTeilnehmerverzAnzeigenListe.fxml"));
            loader.setController(controllerFenster);
            Parent mainPane = null;
            try {
                mainPane = (Parent) loader.load();
            } catch (IOException e) {
                CaBug.drucke("CtrlElekTeilnehmerverzAnzeigen.onKeyStimmblockNr 001");
                e.printStackTrace();
            }
            Scene scene = new Scene(mainPane, 1400, 900);
            neuerDialog.setTitle("Elektronisches Teilnehmerverzeichnis AnzeigenListe");
            neuerDialog.setScene(scene);
            neuerDialog.initStyle(StageStyle.UNDECORATED);
            neuerDialog.initModality(Modality.APPLICATION_MODAL);
            neuerDialog.showAndWait();

            letzteTasteLokal = controllerFenster.letzteTaste;
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
        assert imLogo != null : "fx:id=\"imLogo\" was not injected: check your FXML file 'ElekTeilnehmerverzAnzeigen.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/

        // Image image1=new Image("/ku164.png",true);
        // imLogo.setImage(image1);

        pnPane.setStyle("-fx-background-color: #ffffff");
        listeLesen();
        counterService.start();
    }

    /** The eingelesen praesenzliste. */
    private EclPraesenzliste[] eingelesenPraesenzliste = null;

    /** The anzeigen praesenzliste. */
    private EclPraesenzliste[] anzeigenPraesenzliste = null;

    /** The date next update. */
    private Date dateNextUpdate = null;

    /** The differenz. */
    private long differenz = 20 * 1000; /* Erste Zahl = Anzahl Sekunden, die gewartet werden soll */

    /**
     * Liste lesen.
     */
    void listeLesen() {

        // Date aktuelleZeit=new Date();
        // if (eingelesenPraesenzliste==null){System.out.println("Null!");
        // dateNextUpdate=new Date();
        // }
        // System.out.println("aktuelle Zeit="+aktuelleZeit.getTime()+ "
        // nextupdate="+dateNextUpdate.getTime());

        System.out.println("Liste Lesen");

        // if (eingelesenPraesenzliste==null ||
        // aktuelleZeit.getTime()>=dateNextUpdate.getTime()+differenz){
        //
        // dateNextUpdate=new Date();
        // System.out.println("Aktualisiert");

        DbBundle pDbBundle = new DbBundle();
        pDbBundle.openAll();

        int rc = pDbBundle.dbPraesenzliste.read_all();

        if (rc > 0) {

            /* Nicht zu druckende ausfiltern */

            /*
             * Bei offengelegten Sammelkarten: Sammelkarten-Bewegung selbst
             * rausnehmen. Außerdem Sortier-Feld belegen (bei nicht
             * Offenlegung!)
             */
            for (int i = 0; i < rc; i++) {
                EclPraesenzliste lEclPraesenzliste = pDbBundle.dbPraesenzliste.ergebnisPosition(i);
                // if (
                // /*Element ist selbst eine Sammelkarte, die offengelegt wird
                // => nicht drucken*/
                // (lEclPraesenzliste.meldungstyp==2 &&
                // lEclPraesenzliste.meldungSkOffenlegung==1)
                // ){
                // lEclPraesenzliste.drucken=0;
                //
                // }
                if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                    lEclPraesenzliste.sortierName = lEclPraesenzliste.sammelkartenName + " "
                            + lEclPraesenzliste.sammelkartenVorname + " " + lEclPraesenzliste.sammelkartenOrt;
                } else {
                    lEclPraesenzliste.sortierName = lEclPraesenzliste.aktionaerName + " "
                            + lEclPraesenzliste.aktionaerVorname + " " + lEclPraesenzliste.aktionaerOrt;
                }
            }

            /* Mehrfach Zu-/Abgänge */

            /* Sortieren */
            int sortierung = 3;
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
        // }
        System.out.println("Liste lesen Ende");
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

    /**
     * The Class CounterService.
     */
    public class CounterService extends javafx.concurrent.ScheduledService {

        /**
         * Instantiates a new counter service.
         */
        public CounterService() {
            super();
            System.out.println("Test1");
            setPeriod(Duration.seconds(150));

        }

        /**
         * Creates the task.
         *
         * @return the task
         */
        @Override
        protected Task<?> createTask() {
            return new CounterTask();
        }

    }

    /**
     * The Class CounterTask.
     */
    public class CounterTask extends Task<Object> {

        /**
         * Instantiates a new counter task.
         */
        public CounterTask() {
            System.out.println("CounterTask");
        }

        /**
         * Call.
         *
         * @return the object
         * @throws Exception the exception
         */
        @Override
        protected Object call() throws Exception {
            System.out.println("call");
            listeLesen();
            return null;
        }

    }

    // public class CounterService extends javafx.concurrent.ScheduledService {
    //
    // public CounterService(){
    // System.out.println("Test1");
    //
    // }
    //
    //
    // @Override
    // protected Task createTask() {
    //// setPeriod(Duration.seconds(10));
    // for (int i=0;i<500;i++){
    // System.out.println("Test "+i);
    //
    // try {
    // Thread.sleep(10);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    //
    // }
    // return null;
    // }
    //
    // }
    //

}
