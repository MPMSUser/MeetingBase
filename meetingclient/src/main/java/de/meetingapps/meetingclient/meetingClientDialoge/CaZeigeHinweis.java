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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.io.IOException;
import java.util.Random;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CaZeigeHinweis.
 */
public class CaZeigeHinweis {

    /**
     * Zeige.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     */
    public void zeige(Stage pEigeneStage, String pHinweisText) {
        zeige(pEigeneStage, pHinweisText, 0);
    }

    /**
     * Zeige.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     * @param farbe        the farbe
     */
    /*farbe: 0=normal, 1=grün, 2=gelb, 3=rot*/
    public void zeige(Stage pEigeneStage, String pHinweisText, int farbe) {

        Stage stageDialog = new Stage();

        CtrlZeigeHinweis controllerFenster = new CtrlZeigeHinweis();

        controllerFenster.init(stageDialog, pHinweisText);
        controllerFenster.farbe = farbe;

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/ZeigeHinweis.fxml"));
        loader1.setController(controllerFenster);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CaZeigeHinweis.zeige 005");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 600, 350);
        stageDialog.setTitle("Hinweis");
        stageDialog.setScene(scene1);
        stageDialog.initModality(Modality.APPLICATION_MODAL);
        stageDialog.showAndWait();

    }

    /** The stage nur hinweis. */
    private Stage stageNurHinweis = null;

    /**
     * Zeige nur hinweis.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     */
    public void zeigeNurHinweis(Stage pEigeneStage, String pHinweisText) {
        zeigeNurHinweis(pEigeneStage, pHinweisText, 0);
    }

    /**
     * Zeige nur hinweis.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     * @param farbe        the farbe
     */
    /*farbe: 0=normal, 1=grün, 2=gelb, 3=rot*/
    public void zeigeNurHinweis(Stage pEigeneStage, String pHinweisText, int farbe) {

        stageNurHinweis = new Stage();

        CtrlZeigeNurHinweis controllerFenster = new CtrlZeigeNurHinweis();

        controllerFenster.init(stageNurHinweis, pHinweisText);
        controllerFenster.farbe = farbe;

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/ZeigeNurHinweis.fxml"));
        loader1.setController(controllerFenster);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CaZeigeHinweis.zeige 005");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 600, 350);
        stageNurHinweis.setTitle(pHinweisText);
        stageNurHinweis.setScene(scene1);
        //		stageNurHinweis.initModality(Modality.APPLICATION_MODAL);
        stageNurHinweis.show();

    }

    /**
     * Schliesse nur hinweis.
     */
    public void schliesseNurHinweis() {
        stageNurHinweis.hide();
    }

    /**
     * Gibt true zurück, wenn pWeiter-Button gedrückt; false, wenn pAbbruch-Button
     * gedrückt.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     * @param pWeiter      the weiter
     * @param pAbbruch     the abbruch
     * @return true, if successful
     */
    public boolean zeige2Buttons(Stage pEigeneStage, String pHinweisText, String pWeiter, String pAbbruch) {

        Stage stageDialog = new Stage();

        CtrlZeige2Buttons controllerFenster = new CtrlZeige2Buttons();
        controllerFenster.textWeiter = pWeiter;
        controllerFenster.textAbbruch = pAbbruch;

        controllerFenster.init(stageDialog, pHinweisText);

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/Zeige2Buttons.fxml"));
        loader1.setController(controllerFenster);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("CaZeigeHinweis.zeige2Buttons 005");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 800, 500);
        stageDialog.setTitle("Frage");
        stageDialog.setScene(scene1);
        stageDialog.initModality(Modality.APPLICATION_MODAL);
        stageDialog.showAndWait();

        return controllerFenster.ergebnis;

    }

    /**1=ergebnis, 2=abbruch, 3=abbruchAlternativ*/
    public int zeige3Buttons(Stage pEigeneStage, String pHinweisText, String pWeiter, String pAbbruch, String pAbbruchAlternativ) {

        Stage stageDialog = new Stage();

        CtrlZeige3Buttons controllerFenster = new CtrlZeige3Buttons();
        controllerFenster.textWeiter = pWeiter;
        controllerFenster.textAbbruch = pAbbruch;
        controllerFenster.textAbbruchAlternativ = pAbbruchAlternativ;

        controllerFenster.init(stageDialog, pHinweisText);

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/Zeige3Buttons.fxml"));
        loader1.setController(controllerFenster);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("005");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 800, 500);
        stageDialog.setTitle("Frage");
        stageDialog.setScene(scene1);
        stageDialog.initModality(Modality.APPLICATION_MODAL);
        stageDialog.showAndWait();

        if (controllerFenster.ergebnis==true) {
            return 1;
        }
        if (controllerFenster.ergebnisAlternativ==false) {
            return 2;
        }
        return 3;

    }

    
    
    /**
     * Zeige nicht fertig.
     *
     * @param pEigeneStage the eigene stage
     */
    public void zeigeNichtFertig(Stage pEigeneStage) {
        Random zufall = new Random();
        int textNummerViele = zufall.nextInt();
        if (textNummerViele < 0) {
            textNummerViele = textNummerViele * (-1);
        }
        int textNummerKonkret = textNummerViele % 11;
        //		System.out.println("textNummerKonkret="+textNummerKonkret);
        String text1 = "";
        String text2 = "";
        switch (textNummerKonkret) {
        case 0:
            text1 = "Coming soon...";
            text2 = "";
            break;
        case 1:
            text1 = "Coming soon...";
            text2 = "Maybe in about 4 years...";
            break;
        case 2:
            text1 = "Do we really need that?...";
            text2 = "If you think than ask for it :-)";
            break;
        case 3:
            text1 = "Oh, that feature will be really expensive...";
            text2 = "Please hire 2 more developers (at least ...)";
            break;
        case 4:
            text1 = "The developer team will not like this requirement...";
            text2 = "";
            break;
        case 5:
            text1 = "That's a do-it-yourself-feature at this time...";
            text2 = "";
            break;
        case 6:
            text1 = "Let's do it by IBM ....";
            text2 = "Immer Besser Manuell ...";
            break;
        case 7:
            text1 = "Never ever ....";
            text2 = "... will this feature be implemented ...";
            break;
        case 8:
            text1 = "I don't want to develope this feature ....";
            text2 = "... and you wouldn't like to test it ...";
            break;
        case 9:
            text1 = "Do you really use all existing features?...";
            text2 = "... only then you can ask for a new one...";
            break;
        case 10:
            text1 = "Coming soon......";
            text2 = "... maybe at the day before BER will be opened ...";
            break;
        default:
            text1 = "Don't ask for the delivery date ...";
            text2 = "";
            break;
        }

        if (!text1.isEmpty()) {
            zeige(pEigeneStage, text1);
        }
        if (!text2.isEmpty()) {
            zeige(pEigeneStage, text2);
        }
    }

    
    
    public void zeigeHilfetext(Stage pEigeneStage, String pHinweisText) {

        Stage stageDialog = new Stage();

        CtrlZeigeHinweis controllerFenster = new CtrlZeigeHinweis();

        controllerFenster.init(stageDialog, pHinweisText);

        FXMLLoader loader1 = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingClientDialoge/ZeigeHilfetext.fxml"));
        loader1.setController(controllerFenster);
        Parent mainPane1 = null;
        try {
            mainPane1 = (Parent) loader1.load();
        } catch (IOException e) {
            CaBug.drucke("005");
            e.printStackTrace();
        }
        Scene scene1 = new Scene(mainPane1, 1500, 760);
        stageDialog.setTitle("Hilfetext");
        stageDialog.setScene(scene1);
        stageDialog.initModality(Modality.APPLICATION_MODAL);
        stageDialog.showAndWait();

    }

}
