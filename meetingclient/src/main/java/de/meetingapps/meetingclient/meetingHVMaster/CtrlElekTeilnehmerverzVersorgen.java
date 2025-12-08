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

import de.meetingapps.meetingportal.meetComBl.BlPraesenzSummen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Class CtrlElekTeilnehmerverzVersorgen.
 */
public class CtrlElekTeilnehmerverzVersorgen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn updaten. */
    @FXML
    private Button btnUpdaten;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnUpdaten != null : "fx:id=\"btnUpdaten\" was not injected: check your FXML file 'ElekTeilnehmerverzVersorgen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ElekTeilnehmerverzVersorgen.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'ElekTeilnehmerverzVersorgen.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/

        lblMeldung.setText("");

    }

    /** The update anzahl. */
    private int updateAnzahl = 1;

    /** The update laeuft. */
    private int updateLaeuft = 0;

    /** The versorgen service. */
    VersorgenService versorgenService = new VersorgenService();

    /**
     * Clicked btn updaten.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnUpdaten(ActionEvent event) {
        if (updateLaeuft == 1) {
            btnUpdaten.setText(Integer.toString(updateAnzahl));
        } else {
            versorgenService.start();
            updateLaeuft = 1;
        }
    }

    /**
     * Aktualisieren.
     */
    void aktualisieren() {

        int letzteWillenserklaerung = 0;
        int rc = 0;

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        BlPraesenzSummen blPraesenzsummen = new BlPraesenzSummen(lDbBundle);
        letzteWillenserklaerung = blPraesenzsummen.leseWillenserklaerungElektronischesTeilnehmerverzeichnis();

        /*TODO VidKonf  Elektronisches Teilnehmerverzeichnis noch nicht für virtuelle HV umgebaut*/
        rc = lDbBundle.dbJoined.read_Praesenzliste(-1, -1, letzteWillenserklaerung, false);
        System.out.println("rc=" + rc);
        if (rc > 0) {

            for (int i = 0; i < rc; i++) {
                EclPraesenzliste lEclPraesenzliste = lDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);
                if (
                /*Element ist selbst eine Sammelkarte, die offengelegt wird => nicht drucken*/
                (lEclPraesenzliste.meldungstyp == 2 && lEclPraesenzliste.meldungSkOffenlegung == 1)) {
                    lEclPraesenzliste.drucken = 0;

                }
                if (lEclPraesenzliste.drucken != 0) {
                    lDbBundle.dbPraesenzliste.insert(lEclPraesenzliste);
                }
                if (lEclPraesenzliste.willenserklaerungIdent > letzteWillenserklaerung) {
                    letzteWillenserklaerung = lEclPraesenzliste.willenserklaerungIdent;
                }
            }

        }

        /*Nun noch Sammelkarten - ohne Offenlegung - verarbeiten*/
        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i = 0; i < anzSammelkarten; i++) {
            System.out.println("i=" + i);
            EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[i];
            System.out.println("skOffenlegung=" + lMeldung.liefereOffenlegungTatsaechlich(lDbBundle));
            System.out.println("statusPraesenz=" + lMeldung.statusPraesenz);

            if (lMeldung.liefereOffenlegungTatsaechlich(lDbBundle) != 1 && lMeldung.statusPraesenz == 1) {
                /*Sammelkarte ohne Offenlegung, und präsent: => ermitteln, wie Eintrag in elektronischem Teilnehmerverzeichnis im Vergleich zu
                 * der tatsächlichen Präsenz ist
                 */
                System.out.println("Sammelkarte verändern");
                EclPraesenzliste lPraesenzliste = new EclPraesenzliste();
                lPraesenzliste.meldeIdentAktionaer = lMeldung.meldungsIdent;
                lDbBundle.dbPraesenzliste.read(lPraesenzliste);
                int anzInElekPraesenz = lDbBundle.dbPraesenzliste.anzErgebnis();
                EclPraesenzliste lHaupteintrag = null;
                for (int i1 = 0; i1 < anzInElekPraesenz; i1++) {
                    if (lDbBundle.dbPraesenzliste
                            .ergebnisPosition(i1).willenserklaerung == KonstWillenserklaerung.erstzugang) {
                        lHaupteintrag = lDbBundle.dbPraesenzliste.ergebnisPosition(i1);
                    }
                }
                /*Beide Einträge löschen erst mal löschen (eigentlich nur Veränderungseintrag, das geht aber leider nicht ...)*/
                EclWillenserklaerung lWillenserklaerung = new EclWillenserklaerung();
                lWillenserklaerung.willenserklaerungIdent = lHaupteintrag.willenserklaerungIdent;
                lDbBundle.dbPraesenzliste.delete(lWillenserklaerung);

                /*Nun Präsenzlisteneintrag updaten. Falls aktueller Eintrag > oder = Haupteintrag,
                 * dann einfach Haupteintrag mit aktueller Stimmenzahl neu eintragen. Ansonsten
                 * Haupteintrag mit bisherigem Eintrag wiederherstellen, und dann Ergänzungseintrag
                 * "Ab" zusätzlich eintragen
                 */
                if (lHaupteintrag.aktien <= lMeldung.stueckAktien) { /*Nur Haupteintrag*/
                    lHaupteintrag.aktien = lMeldung.stueckAktien;
                    lHaupteintrag.stimmen = lMeldung.stimmen;
                    lDbBundle.dbPraesenzliste.insert(lHaupteintrag);
                } else {/*Haupteintrag und Nebeneintrag als Abgang*/
                    /*Haupteintrag*/
                    lDbBundle.dbPraesenzliste.insert(lHaupteintrag);
                    /*Haupteintrag verändern zum Nebeneintrag*/
                    lHaupteintrag.aktien = lHaupteintrag.aktien - lMeldung.stueckAktien;
                    lHaupteintrag.stimmen = lHaupteintrag.stimmen - lMeldung.stimmen;
                    switch (lMeldung.skIst) {
                    case 1:
                        /*KIAV*/ lHaupteintrag.willenserklaerung = KonstWillenserklaerung.abgangAusKIAV;
                        break;
                    case 2:
                        /*SRV*/ lHaupteintrag.willenserklaerung = KonstWillenserklaerung.abgangAusSRV;
                        break;
                    case 3:
                        /*Orga*/ lHaupteintrag.willenserklaerung = KonstWillenserklaerung.abgangAusOrga;
                        break;
                    case 5:
                        /*Dauervollmacht*/ lHaupteintrag.willenserklaerung = KonstWillenserklaerung.abgangAusDauervollmacht;
                        break;
                    }
                    lDbBundle.dbPraesenzliste.insert(lHaupteintrag);
                }
            }
        }

        blPraesenzsummen.schreibeWillenserklaerungElektronischesTeilnehmerverzeichnis(letzteWillenserklaerung);
        lDbBundle.closeAll();

    }

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {
        eigeneStage.close();
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
     * The Class VersorgenService.
     */
    public class VersorgenService extends javafx.concurrent.ScheduledService {

        /**
         * Instantiates a new versorgen service.
         */
        public VersorgenService() {
            super();
            System.out.println("Test1");
            setPeriod(Duration.seconds(90));

        }

        /**
         * Creates the task.
         *
         * @return the task
         */
        @Override
        protected Task<?> createTask() {
            return new VersorgenTask();
        }

    }

    /**
     * The Class VersorgenTask.
     */
    public class VersorgenTask extends Task<Object> {

        /**
         * Instantiates a new versorgen task.
         */
        public VersorgenTask() {
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
            aktualisieren();
            updateAnzahl++;
            return null;
        }

    }

}
