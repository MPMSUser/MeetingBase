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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Class CaController.
 */
public class CaController {

    /**
     * Neue Stage wird erzeugt.
     *
     * @param pController the controller
     * @param pFxml       the fxml
     * @param pBreite     the breite
     * @param pHoehe      the hoehe
     * @param pTitel      the titel
     * @param pModal      the modal
     */
    public void openNeueStage(CtrlRoot pController, String pFxml, int pBreite, int pHoehe, String pTitel,
            boolean pModal) {
        Stage neuerDialog = new Stage();
        this.open(neuerDialog, pController, pFxml, pBreite, pHoehe, pTitel, pModal, false);
    }

    /**
     * Übergebene Stage wird verwendet - UNDECORATED.
     *
     * @param pStage      the stage
     * @param pController the controller
     * @param pFxml       the fxml
     * @param pBreite     the breite
     * @param pHoehe      the hoehe
     * @param pTitel      the titel
     * @param pModal      the modal
     */
    public void openUndecorated(Stage pStage, CtrlRoot pController, String pFxml, int pBreite, int pHoehe,
            String pTitel, boolean pModal) {
        this.open(pStage, pController, pFxml, pBreite, pHoehe, pTitel, pModal, true);
    }

    /**
     * Übergebene Stage wird verwendet - Decorated.
     *
     * @param pStage      the stage
     * @param pController the controller
     * @param pFxml       the fxml
     * @param pBreite     the breite
     * @param pHoehe      the hoehe
     * @param pTitel      the titel
     * @param pModal      the modal
     */
    public void open(Stage pStage, CtrlRoot pController, String pFxml, int pBreite, int pHoehe, String pTitel,
            boolean pModal) {
        this.open(pStage, pController, pFxml, pBreite, pHoehe, pTitel, pModal, false);
    }

    /**
     * Übergebene Stage wird verwendet.
     *
     * @param pStage       the stage
     * @param pController  the controller
     * @param pFxml        the fxml
     * @param pBreite      the breite
     * @param pHoehe       the hoehe
     * @param pTitel       the titel
     * @param pModal       the modal
     * @param pUndecorated the undecorated
     */
    public void open(Stage pStage, CtrlRoot pController, String pFxml, int pBreite, int pHoehe, String pTitel,
            boolean pModal, boolean pUndecorated) {
        this.open(pStage, pController, pFxml, pBreite, pHoehe, pTitel, pModal, pUndecorated, true);
    }

    /**
     * Open.
     *
     * @param pStage       the stage
     * @param pController  the controller
     * @param pFxml        the fxml
     * @param pBreite      the breite
     * @param pHoehe       the hoehe
     * @param pTitel       the titel
     * @param pModal       the modal
     * @param pUndecorated the undecorated
     * @param pMitMandant  the mit mandant
     */
    public void open(Stage pStage, CtrlRoot pController, String pFxml, int pBreite, int pHoehe, String pTitel,
            boolean pModal, boolean pUndecorated, boolean pMitMandant) {

        pController.eigeneStage = pStage;
        pController.eigenerTitel = pTitel;
        pController.eigenerTitelMitMandant = pMitMandant;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(pFxml));
        loader.setController(pController);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.openController 001 " + pFxml);
            e.printStackTrace();
        }

        Scene scene = new Scene(mainPane, pBreite, pHoehe);

        setzeTitel(pStage, pTitel, pMitMandant);

        pStage.setScene(scene);
        if (pUndecorated) {
            pStage.initStyle(StageStyle.UNDECORATED);
        }
        if (pModal) {
            pStage.initModality(Modality.APPLICATION_MODAL);
            pStage.showAndWait();
        } else {
            pStage.show();
        }
    }

    /**
     * Setze titel.
     *
     * @param pStage      the stage
     * @param pTitel      the titel
     * @param pMitMandant the mit mandant
     */
    public void setzeTitel(Stage pStage, String pTitel, boolean pMitMandant) {
        String hTitel = pTitel;
        if (ParamS.clGlobalVar.mandant != 0 && pMitMandant == true) {
            hTitel = hTitel + " - " + Integer.toString(ParamS.clGlobalVar.mandant) + "/"
                    + Integer.toString(ParamS.clGlobalVar.hvJahr) + ParamS.clGlobalVar.hvNummer + "/"
                    + ParamS.clGlobalVar.datenbereich + " " + ParamS.eclEmittent.bezeichnungKurz;
        }
        pStage.setTitle(hTitel);

    }

}
