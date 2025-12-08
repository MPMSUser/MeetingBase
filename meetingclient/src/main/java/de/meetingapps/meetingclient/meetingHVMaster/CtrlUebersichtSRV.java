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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComReports.RepSammelkarten;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlUebersichtSRV.
 */
public class CtrlUebersichtSRV {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /** The tf formular normaluebersicht. */
    @FXML
    private TextField tfFormularNormaluebersicht;

    /** The btn jurz uebersicht. */
    @FXML
    private Button btnJurzUebersicht;

    /** The btn normal uebersicht. */
    @FXML
    private Button btnNormalUebersicht;

    /** The tf formular kurzuebersicht. */
    @FXML
    private TextField tfFormularKurzuebersicht;

    /** The tf formular weisungssummen. */
    @FXML
    private TextField tfFormularWeisungssummen;

    /** The btn weisungssummen. */
    @FXML
    private Button btnWeisungssummen;

    /** The tf vertreter ident. */
    @FXML
    private TextField tfVertreterIdent;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert tfFormularNormaluebersicht != null : "fx:id=\"tfFormularNormaluebersicht\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert btnJurzUebersicht != null : "fx:id=\"btnJurzUebersicht\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert btnNormalUebersicht != null : "fx:id=\"btnNormalUebersicht\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert tfFormularKurzuebersicht != null : "fx:id=\"tfFormularKurzuebersicht\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert tfFormularWeisungssummen != null : "fx:id=\"tfFormularWeisungssummen\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert tfVertreterIdent != null : "fx:id=\"tfVertreterIdent\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";
        assert btnWeisungssummen != null : "fx:id=\"btnWeisungssummen\" was not injected: check your FXML file 'UebersichtSRV.fxml'.";

        /********************************Ab hier individuell*********************************************/

        lblMeldung.setText("");
        tfFormularKurzuebersicht.setText("01");
        tfFormularNormaluebersicht.setText("01");
        tfFormularWeisungssummen.setText("01");

    }

    /**
     * Gets the vertreter ident.
     *
     * @return the vertreter ident
     */
    private int getVertreterIdent() {
        int vertreterIdent;
        String vertreter = tfVertreterIdent.getText();
        vertreterIdent = Integer.parseInt(vertreter);
        return vertreterIdent;
    }

    /**
     * Clicked btn schliessen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnSchliessen(ActionEvent event) {
        eigeneStage.close();

    }

    /**
     * Clicked btn kurz uebersicht.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnKurzUebersicht(ActionEvent event) {
        int vertreterIdent = getVertreterIdent();
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();

        RepSammelkarten repSammelkarten = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repSammelkarten.druckeSammelkarten(lDbBundle, rpDrucken, 1, 1, tfFormularKurzuebersicht.getText(),
                vertreterIdent);

        lDbBundle.closeAll();
        lblMeldung.setText("Kurzübersicht gedruckt");
    }

    /**
     * Clicked btn normal uebersicht.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnNormalUebersicht(ActionEvent event) {
        int vertreterIdent = getVertreterIdent();
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();

        RepSammelkarten repSammelkarten = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repSammelkarten.druckeSammelkarten(lDbBundle, rpDrucken, 2, 1, tfFormularKurzuebersicht.getText(),
                vertreterIdent);

        lDbBundle.closeAll();
        lblMeldung.setText("Normalübersicht gedruckt");

    }

    /**
     * Clicked btn weisungssummen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnWeisungssummen(ActionEvent event) {
        int vertreterIdent = getVertreterIdent();
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        RpDrucken rpDrucken = new RpDrucken();

        RepSammelkarten repSammelkarten = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repSammelkarten.druckeSammelkarten(lDbBundle, rpDrucken, 2, 1, tfFormularKurzuebersicht.getText(),
                vertreterIdent);

        lDbBundle.closeAll();
        lblMeldung.setText("Summen gedruckt");

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
