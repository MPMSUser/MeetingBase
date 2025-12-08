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
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzAkkreditierung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchen;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlMassenZuAbgang.
 */
public class CtrlMassenZuAbgang {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The btn zugehen. */
    @FXML
    private Button btnZugehen;

    /** The btn abgehen. */
    @FXML
    private Button btnAbgehen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'MassenZuAbgang.fxml'.";
        assert btnZugehen != null : "fx:id=\"btnZugehen\" was not injected: check your FXML file 'MassenZuAbgang.fxml'.";
        assert btnAbgehen != null : "fx:id=\"btnAbgehen\" was not injected: check your FXML file 'MassenZuAbgang.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'MassenZuAbgang.fxml'.";

        /*************** Ab hier individuell **********************************/
    }

    /**
     * Clicked beenden.
     *
     * @param event the event
     */
    @FXML
    void clickedBeenden(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Onbtn abgehen.
     *
     * @param event the event
     */
    @FXML
    void onbtnAbgehen(ActionEvent event) {
        int stimmkartennummer = 45100;

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        int anzahl = lDbBundle.dbMeldungen.leseAllePotentiellenPraesenzaenderungenAktionaere();
        if (anzahl < 1) {
            lblMeldung.setText("Fehler");
            lDbBundle.closeAll();
            return;
        }
        if (anzahl > 300) {
            anzahl = 300;
        }
        EclMeldung[] meldungenArray = lDbBundle.dbMeldungen.meldungenArray;

        System.out.println("Start Abgang");
        for (int i = 0; i < anzahl; i++) {
            if ((i) % 100 == 0) {
                System.out.println(i + " *********************************************** "
                        + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }

            EclMeldung lEclMeldung = meldungenArray[i];

            if (lEclMeldung.statusPraesenz == 1 && !lEclMeldung.zutrittsIdent.isEmpty()) {

                WEPraesenzBuchen lWEPraesenzBuchen = new WEPraesenzBuchen();
                WEPraesenzBuchenRC wePraesenzBuchenRC = new WEPraesenzBuchenRC();

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.abgang);

                /*Start	
                 * 
                 */
                lWEPraesenzBuchen.zutrittsIdentAktionaerArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.zutrittsIdentAktionaer = new LinkedList<EclZutrittsIdent>();
                lWEPraesenzBuchen.zutrittsIdentAktionaerArt.add(2);
                EclZutrittsIdent hEclZutrittsIdent = new EclZutrittsIdent();
                hEclZutrittsIdent.zutrittsIdent = lEclMeldung.zutrittsIdent;
                hEclZutrittsIdent.zutrittsIdentNeben = "00";
                lWEPraesenzBuchen.zutrittsIdentAktionaer.add(hEclZutrittsIdent);

                lWEPraesenzBuchen.zutrittsIdentGastArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.zutrittsIdentGast = new LinkedList<EclZutrittsIdent>();
                hEclZutrittsIdent = new EclZutrittsIdent();
                lWEPraesenzBuchen.zutrittsIdentGastArt.add(0);
                lWEPraesenzBuchen.zutrittsIdentGast.add(hEclZutrittsIdent);

                lWEPraesenzBuchen.stimmkartenArt = new LinkedList<int[]>();
                lWEPraesenzBuchen.stimmkartenArt.add(new int[] { 2, 0, 0, 0 });
                lWEPraesenzBuchen.stimmkarten = new LinkedList<String[]>();
                lWEPraesenzBuchen.stimmkarten.add(new String[] { "", "", "", "" });
                lWEPraesenzBuchen.stimmkarten.get(0)[0] = "1" + lEclMeldung.stimmkarte + "116110";

                lWEPraesenzBuchen.stimmkartenSecondArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.stimmkartenSecondArt.add(0);
                lWEPraesenzBuchen.stimmkartenSecond = new LinkedList<String>();
                lWEPraesenzBuchen.stimmkartenSecond.add("");

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.eclMeldung = new LinkedList<EclMeldung>();
                lWEPraesenzBuchen.eclMeldung.add(lEclMeldung);

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.vollmachtPersonenNatJurIdent.add(-1);
                lWEPraesenzBuchen.vertreterName = "";
                lWEPraesenzBuchen.vertreterVorname = "";
                lWEPraesenzBuchen.vertreterOrt = "";

                /*Ende*/

                BlPraesenzAkkreditierung blPraesenz = new BlPraesenzAkkreditierung(lDbBundle);
                wePraesenzBuchenRC = blPraesenz.buchen(lWEPraesenzBuchen);

                System.out.println("rc=" + wePraesenzBuchenRC.rc);
                stimmkartennummer++;
            }

        }

        System.out.println("Ende Zugang");

        lDbBundle.closeAll();
        lblMeldung.setText("Zugang erledigt");

    }

    /**
     * Onbtn zugehen.
     *
     * @param event the event
     */
    @FXML
    void onbtnZugehen(ActionEvent event) {

        int stimmkartennummer = 64000;

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        int anzahl = lDbBundle.dbMeldungen.leseAllePotentiellenPraesenzaenderungenAktionaere();
        if (anzahl < 1) {
            lblMeldung.setText("Fehler");
            lDbBundle.closeAll();
            return;
        }

        EclMeldung[] meldungenArray = lDbBundle.dbMeldungen.meldungenArray;

        System.out.println("Start Zugang");
        for (int i = 0; i < anzahl; i++) {
            if ((i) % 100 == 0) {
                System.out.println(i + " *********************************************** "
                        + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }

            EclMeldung lEclMeldung = meldungenArray[i];

            if (lEclMeldung.statusPraesenz == 0 && !lEclMeldung.zutrittsIdent.isEmpty()) {

                WEPraesenzBuchen lWEPraesenzBuchen = new WEPraesenzBuchen();
                WEPraesenzBuchenRC wePraesenzBuchenRC = new WEPraesenzBuchenRC();

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.funktion.add(KonstWillenserklaerung.erstzugang);

                /*Start	
                 * 
                 */
                lWEPraesenzBuchen.zutrittsIdentAktionaerArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.zutrittsIdentAktionaer = new LinkedList<EclZutrittsIdent>();
                lWEPraesenzBuchen.zutrittsIdentAktionaerArt.add(2);
                EclZutrittsIdent hEclZutrittsIdent = new EclZutrittsIdent();
                hEclZutrittsIdent.zutrittsIdent = lEclMeldung.zutrittsIdent;
                hEclZutrittsIdent.zutrittsIdentNeben = "00";
                lWEPraesenzBuchen.zutrittsIdentAktionaer.add(hEclZutrittsIdent);

                lWEPraesenzBuchen.zutrittsIdentGastArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.zutrittsIdentGast = new LinkedList<EclZutrittsIdent>();
                hEclZutrittsIdent = new EclZutrittsIdent();
                lWEPraesenzBuchen.zutrittsIdentGastArt.add(0);
                lWEPraesenzBuchen.zutrittsIdentGast.add(hEclZutrittsIdent);

                lWEPraesenzBuchen.stimmkartenArt = new LinkedList<int[]>();
                lWEPraesenzBuchen.stimmkartenArt.add(new int[] { 2, 0, 0, 0 });
                lWEPraesenzBuchen.stimmkarten = new LinkedList<String[]>();
                lWEPraesenzBuchen.stimmkarten.add(new String[] { "", "", "", "" });
                lWEPraesenzBuchen.stimmkarten.get(0)[0] = "1" + Integer.toString(stimmkartennummer) + "115110";

                lWEPraesenzBuchen.stimmkartenSecondArt = new LinkedList<Integer>();
                lWEPraesenzBuchen.stimmkartenSecondArt.add(0);
                lWEPraesenzBuchen.stimmkartenSecond = new LinkedList<String>();
                lWEPraesenzBuchen.stimmkartenSecond.add("");

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.eclMeldung = new LinkedList<EclMeldung>();
                lWEPraesenzBuchen.eclMeldung.add(lEclMeldung);

                /*TODO $Schnellfix*/
                lWEPraesenzBuchen.vollmachtPersonenNatJurIdent.add(-1);
                lWEPraesenzBuchen.vertreterName = "";
                lWEPraesenzBuchen.vertreterVorname = "";
                lWEPraesenzBuchen.vertreterOrt = "";

                /*Ende*/

                BlPraesenzAkkreditierung blPraesenz = new BlPraesenzAkkreditierung(lDbBundle);
                wePraesenzBuchenRC = blPraesenz.buchen(lWEPraesenzBuchen);

                System.out.println("rc=" + wePraesenzBuchenRC.rc);
                stimmkartennummer++;
            }

        }

        System.out.println("Ende Zugang");

        lDbBundle.closeAll();
        lblMeldung.setText("Zugang erledigt");

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
