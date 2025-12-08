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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzSummen;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenNummer;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzSummen.
 */
public class CtrlPraesenzSummen extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The grd pn aktuell. */
    @FXML
    private GridPane grdPnAktuell;

    /** The scroll pn listen summen. */
    @FXML
    private ScrollPane scrollPnListenSummen;

    /** The btn refresh. */
    @FXML
    private Button btnRefresh;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The lbl aktuelle praesenz nr. */
    @FXML
    private Label lblAktuellePraesenzNr;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert grdPnAktuell != null : "fx:id=\"grdPnAktuell\" was not injected: check your FXML file 'PraesenzSummen.fxml'.";
        assert scrollPnListenSummen != null : "fx:id=\"scrollPnListenSummen\" was not injected: check your FXML file 'PraesenzSummen.fxml'.";
        assert btnRefresh != null : "fx:id=\"btnRefresh\" was not injected: check your FXML file 'PraesenzSummen.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'PraesenzSummen.fxml'.";
        assert lblAktuellePraesenzNr != null : "fx:id=\"lblAktuellePraesenzNr\" was not injected: check your FXML file 'PraesenzSummen.fxml'.";

        /********************************Ab hier individuell*********************************************/

        refreshInhalt();

    }

    /**
     * Refresh inhalt.
     */
    private void refreshInhalt() {

        int aktuellePraesenzNr = 0;

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        /*Präsenzlistennummer*/
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernFuerBuchung();

        aktuellePraesenzNr = lDbBundle.clGlobalVar.zuVerzeichnisNr1;
        lblAktuellePraesenzNr.setText(Integer.toString(aktuellePraesenzNr));

        /*Aktuelle Summen*/
        BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
        lBlPraesenzSummen.leseAktuelleSummen();

        //    	grdPnAktuell=new GridPane();
        //    	grdPnAktuell.setVgap(5);
        //    	grdPnAktuell.setHgap(15);

        grdPnAktuell.getChildren().clear();
        Label lZ1 = new Label();
        lZ1.setText("Präsenzsummen normal");
        grdPnAktuell.add(lZ1, 0, 1);
        Label lZ2 = new Label();
        lZ2.setText("Präsenzsummen delayed");
        grdPnAktuell.add(lZ2, 0, 2);
        for (int i = 1; i <= 5; i++) {
            if (lDbBundle.param.paramBasis.getGattungAktiv(i)) {
                String bezeichnung = lDbBundle.param.paramBasis.getGattungBezeichnung(i);
                String summeNormalGattung = CaString.toStringDE(lBlPraesenzSummen.getPraesenzSummeNormalGattung(i));
                String summeDelayedGattung = CaString.toStringDE(lBlPraesenzSummen.getPraesenzSummeDelayedGattung(i));

                Label lUberschrift = new Label();
                lUberschrift.setText(bezeichnung);
                grdPnAktuell.add(lUberschrift, i, 0);
                GridPane.setHalignment(lUberschrift, HPos.RIGHT);

                Label lSummeNormalGattung = new Label();
                lSummeNormalGattung.setText(summeNormalGattung);
                grdPnAktuell.add(lSummeNormalGattung, i, 1);
                GridPane.setHalignment(lSummeNormalGattung, HPos.RIGHT);

                Label lSummeDelayedGattung = new Label();
                lSummeDelayedGattung.setText(summeDelayedGattung);
                grdPnAktuell.add(lSummeDelayedGattung, i, 2);
                GridPane.setHalignment(lSummeDelayedGattung, HPos.RIGHT);
            }
        }

        GridPane grpnListensummen = new GridPane();
        grpnListensummen.setVgap(5);
        grpnListensummen.setHgap(15);

        /*Überschrift*/
        Label hLabel1 = new Label();
        hLabel1.setText("Liste");
        grpnListensummen.add(hLabel1, 0, 0);

        for (int i = 1; i <= 5; i++) {
            if (lDbBundle.param.paramBasis.getGattungAktiv(i)) {
                Label hLabel2 = new Label();
                hLabel2.setText(lDbBundle.param.paramBasis.getGattungBezeichnung(i));
                grpnListensummen.add(hLabel2, i, 0);

                Label hLabel3 = new Label();
                hLabel3.setText("Briefwahl " + lDbBundle.param.paramBasis.getGattungBezeichnungKurz(i));
                grpnListensummen.add(hLabel3, i + 5, 0);
            }
        }

        //    	Label hLabel2=new Label();
        //    	hLabel2.setText("Gattung 1 berechnet");
        //    	grpnListensummen.add(hLabel2,1,0);
        //
        //    	Label hLabel3=new Label();
        //    	hLabel3.setText("Gattung 1 aus Liste");
        //    	grpnListensummen.add(hLabel3,2,0);
        //
        //    	Label hLabel4=new Label();
        //    	hLabel4.setText("Gattung 2 berechnet");
        //    	grpnListensummen.add(hLabel4,3,0);
        //
        //    	Label hLabel5=new Label();
        //    	hLabel5.setText("Gattung 2 aus Liste");
        //    	grpnListensummen.add(hLabel5,4,0);
        //
        //    	Label hLabel6=new Label();
        //    	hLabel6.setText("Briefwahl Gattung 1");
        //    	grpnListensummen.add(hLabel6,5,0);
        //
        //    	Label hLabel7=new Label();
        //    	hLabel7.setText("Briefwahl Gattung 2");
        //    	grpnListensummen.add(hLabel7,6,0);

        scrollPnListenSummen.setContent(grpnListensummen);

        if (aktuellePraesenzNr != -1) { /*Dann sind Listen vorhanden zum Anzeigen*/
            for (int i = 0; i < aktuellePraesenzNr; i++) {
                int listennr = i;
                if (i == 0) {
                    listennr = -1;
                }
                String listenBezeichnung = "Nachtrag " + Integer.toString(i);
                if (i == 0) {
                    listenBezeichnung = "Erstpräsenz";
                }
                Label eLabel1 = new Label();
                eLabel1.setText(listenBezeichnung);
                grpnListensummen.add(eLabel1, 0, i + 1);

                lBlPraesenzSummen.leseSummenFuerListe(listennr);

                for (int i1 = 1; i1 <= 5; i1++) {
                    if (lDbBundle.param.paramBasis.getGattungAktiv(i1)) {
                        VBox boxPraesenzSumme = new VBox();
                        boxPraesenzSumme.setAlignment(Pos.CENTER_RIGHT);

                        Label eLabel2 = new Label();
                        eLabel2.setText(CaString.toStringDE(lBlPraesenzSummen.getPraesenzSummeBerechnetGattung(i1)));
                        boxPraesenzSumme.getChildren().add(eLabel2);

                        Label eLabel3 = new Label();
                        eLabel3.setText(CaString.toStringDE(lBlPraesenzSummen.getPraesenzSummeAusListeGattung(i1)));
                        boxPraesenzSumme.getChildren().add(eLabel3);

                        grpnListensummen.add(boxPraesenzSumme, i1, i + 1);

                        VBox boxPraesenzSummeBriefwahl = new VBox();
                        boxPraesenzSummeBriefwahl.setAlignment(Pos.CENTER_RIGHT);

                        Label eLabel6 = new Label();
                        eLabel6.setText(CaString.toStringDE(lBlPraesenzSummen.getBriefwahlSummeBerechnetGattung(i1)));
                        boxPraesenzSummeBriefwahl.getChildren().add(eLabel6);

                        Label eLabel7 = new Label();
                        eLabel7.setText(CaString.toStringDE(lBlPraesenzSummen.getBriefwahlSummeBerechnetGattung(i1)));
                        boxPraesenzSummeBriefwahl.getChildren().add(eLabel7);

                        grpnListensummen.add(boxPraesenzSummeBriefwahl, i1 + 5, i + 1);

                    }
                }
            }

        }
    }

    /**
     * Clicked btn refresh.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnRefresh(ActionEvent event) {
        refreshInhalt();
        return;
    }

    /**
     * Clicked btn beenden.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnBeenden(ActionEvent event) {
        eigeneStage.hide();
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
