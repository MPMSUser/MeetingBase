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
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstDBAbstimmungen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * The Class CtrlAbstimmungArchiv.
 */
public class CtrlAbstimmungArchiv extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn erstellen. */
    @FXML
    private Button btnErstellen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The scpn vorgaenge. */
    @FXML
    private ScrollPane scpnVorgaenge;

    /** The cb weisungen kopfebene veraendert. */
    @FXML
    private CheckBox cbWeisungenKopfebeneVeraendert;

    /** The grpn abstimmungen. */
    /*Ab hier individuell*/
    private MeetingGridPane grpnAbstimmungen = null;

    /** The cb abstimmungsvorgang ausgewaehlt. */
    private CheckBox[] cbAbstimmungsvorgangAusgewaehlt = null; /*0, 1, 2, 3 möglich*/

    /** The abstimmungsbloecke. */
    private EclAbstimmungsblock abstimmungsbloecke[] = null;

    /** ********Ab hier individuell***********. */

    private DbBundle lDbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /********** Ab hier individuell *******************/

        lDbBundle = new DbBundle();

        /*Abstimmungsblöcke einlesen*/
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungsblock.read_all();

        abstimmungsbloecke = lDbBundle.dbAbstimmungsblock.ergebnisArray;
        lDbBundle.closeAll();

        if (abstimmungsbloecke == null || abstimmungsbloecke.length == 0) {
            return;
        }

        grpnAbstimmungen = new MeetingGridPane();
        //      grpnAbstimmungen.setVgap(5);
        //      grpnAbstimmungen.setHgap(15);

        cbAbstimmungsvorgangAusgewaehlt = null;
        cbAbstimmungsvorgangAusgewaehlt = new CheckBox[abstimmungsbloecke.length];

        for (int i = 0; i < abstimmungsbloecke.length; i++) {
            cbAbstimmungsvorgangAusgewaehlt[i] = new CheckBox();
            cbAbstimmungsvorgangAusgewaehlt[i].setSelected(false);
            grpnAbstimmungen.addMeeting(cbAbstimmungsvorgangAusgewaehlt[i], 0, i + 1);

            Label hLabel1 = new Label(Integer.toString(abstimmungsbloecke[i].ident));
            grpnAbstimmungen.addMeeting(hLabel1, 1, i + 1);

            Label hLabel2 = new Label(abstimmungsbloecke[i].kurzBeschreibung);
            grpnAbstimmungen.addMeeting(hLabel2, 2, i + 1);

            Label hLabel3 = new Label(abstimmungsbloecke[i].beschreibung);
            grpnAbstimmungen.addMeeting(hLabel3, 3, i + 1);
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ausgewählt");
        ueberschriftList.add("Ident");
        ueberschriftList.add("Kurzbeschreibung (intern)");
        ueberschriftList.add("Beschreibung");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scpnVorgaenge);

    }

    /**
     * **************Eingabe**************************************.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn erstellen.
     *
     * @param event the event
     */
    @FXML
    void onBtnErstellen(ActionEvent event) {
        /*Prüfen, ob angehakt*/
        boolean wasAusgewaehlt = false;
        for (int i = 0; i < abstimmungsbloecke.length; i++) {
            if (cbAbstimmungsvorgangAusgewaehlt[i].isSelected()) {
                wasAusgewaehlt = true;
            }
        }

        if (wasAusgewaehlt == false) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte wählen Sie mindestens einen Abstimmungsvorgang aus!");
            return;
        }

        EclHVDatenLfd lHVDatenLfd = null;

        lDbBundle.openAll();

        /*Archiv-Datenbank ggf. löschen, dann initialisieren*/
        lDbBundle.dbHVDatenLfd.deleteArchivierteAbstimmungen();
        lDbBundle.dbAbstimmungMeldungArchiv.initialisiereArchivTable();

        /*Sammelkarten einlesen*/
        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        EclMeldung[] sammelkartenArray = lDbBundle.dbMeldungen.meldungenArray;

        for (int i = 0; i < abstimmungsbloecke.length; i++) {
            if (cbAbstimmungsvorgangAusgewaehlt[i].isSelected()) {

                /*++++++++++++Abstimmungen einlesen+++++++++*/
                lDbBundle.dbAbstimmungen.leseAbstimmungenZuBlock(abstimmungsbloecke[i].ident,
                        KonstDBAbstimmungen.sortierung_interneIdent, true);
                EclAbstimmung[] lAbstimmungenArray = lDbBundle.dbAbstimmungen.abstimmungenArray;

                if (lAbstimmungenArray != null && lAbstimmungenArray.length > 0) {
                    /*+++++++++++++Schritt 1: Weisungen übertragen++++++++++++*/

                    if (sammelkartenArray != null && sammelkartenArray.length != 0) {

                        for (int i1 = 0; i1 < sammelkartenArray.length; i1++) {
                            /*Nur verarbeiten, wenn für die TOPe irgendwas abgegeben (sonst ja entweder
                             * nicht präsent, oder keine Weisungen enthalten)
                             */
                            int sammelIdent = sammelkartenArray[i1].meldungsIdent;
                            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(sammelIdent);
                            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() >= 1) {
                                /*Zwischenspeichern - wird später benötigt, um festzstellen ob bzw. welche Weisungen
                                 * geholt werden sollen
                                 */
                                EclAbstimmungMeldungSplit eclAbstimmungMeldungSplit = lDbBundle.dbAbstimmungMeldungSplit
                                        .ergebnisPositionGefunden(0);

                                /*Wenn Archiv vorhanden, dann aus Archiv holen, sonst aus Weisungsdatei*/
                                EclWeisungMeldung[] weisungMeldungArray = null;
                                if (lDbBundle.dbWeisungMeldung.pruefeArchivVorhanden(abstimmungsbloecke[i].ident)) {
                                    /*Weisungen aus Archiv holen*/
                                    lDbBundle.dbWeisungMeldung.leseZuSammelkarteAusArchiv(sammelIdent,
                                            abstimmungsbloecke[i].ident);
                                    weisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
                                } else {
                                    /*Weisungen aus Weisungstable holen*/
                                    lDbBundle.dbWeisungMeldung.leseZuSammelIdent(sammelIdent, false);
                                    weisungMeldungArray = lDbBundle.dbWeisungMeldung.weisungMeldungArray;
                                }

                                if (weisungMeldungArray != null && weisungMeldungArray.length != 0) {
                                    for (int i2 = 0; i2 < weisungMeldungArray.length; i2++) {//Schleife für alle Aktionäre

                                        /*Nun prüfen für die gerade untersuchten Abstimmung:
                                         * > wurde über die Sammelkarte überhaupt eine Stimme abgegeben? 
                                         *      Sonst nicht weiter berücksichtigen, da dann Sammelkarte z.B. nicht präsent,
                                         *      oder keine Einzelkarten enthalten
                                         * > wurde "Split"-Stimmen für die Sammelkarten gespeichert? 
                                         *      Denn ansonsten möglicherweise Weisungen durch Stimmabgabe überschrieben,
                                         *      d.h. für Einzelkarten dann nicht Weisung übertragen, sondern diese "einheitliche Stimmabgabe" setzen
                                         *      (unabhängig davon, ob Einzelweisungen vorhanden sind oder nicht)
                                         */

                                        /*Weisungen übertragen*/
                                        /*XXX*/
                                    }
                                }
                            }
                        }
                    }

                    /*+++++++++++++Schritt 2: Abgegebene Stimmen übertragen+++++++++*/
                    /*XXX*/

                    /*+++++++++Speichern, welche Abstimmungsvorgänge ausgewertet wurden+++++++++*/
                    /*XXX*/
                }

            }
        }

        /*Nun noch speichern, dass Archiv erzeugt wurde*/
        lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = ParamS.clGlobalVar.mandant;
        lHVDatenLfd.ident = 1000;
        if (cbWeisungenKopfebeneVeraendert.isSelected()) {
            lHVDatenLfd.wert = "2";
        } else {
            lHVDatenLfd.wert = "1";
        }
        lHVDatenLfd.beschreibung = "Abstimmungsarchiv erzeugt";
        lDbBundle.dbHVDatenLfd.insertOrUpdate(lHVDatenLfd, lHVDatenLfd.wert);

        lDbBundle.closeAll();

        /*XXX*/
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
