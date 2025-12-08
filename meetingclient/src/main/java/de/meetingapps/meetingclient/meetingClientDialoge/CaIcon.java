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

import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Zuordnen von Icons zur jeweils zu übergebenden Stage.
 */
public class CaIcon {

    /**
     * Adds the server art.
     *
     * @return the string
     */
    static private String addServerArt() {
        String addServer = "";
        if (ParamS.paramServer != null) {
            switch (ParamS.paramServer.serverArt) {
            case 1:
                addServer = "";
                break; 
            case 2:
                addServer = "T";
                break; /*Einrichtungsserver/Testserver*/
            case 3:
                addServer = "HV";
                break; /*Einzelrechner lokal*/
            case 4:
                addServer = "T";
                break; /*HV-Server*/
            }
        }
        return addServer;
    }

    /**
     * Setze icon.
     *
     * @param pStage the stage
     * @param pName  the name
     */
    static private void setzeIcon(Stage pStage, String pName) {
        pStage.getIcons().clear();
        pStage.getIcons().add(new Image(CaIcon.class.getResourceAsStream(pName + addServerArt() + ".png")));
    }

    /**
     * Standard.
     *
     * @param pStage the stage
     */
    static public void standard(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconBasis");
    }

    /**
     * Front office.
     *
     * @param pStage the stage
     */
    static public void frontOffice(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconFrontOffice");
    }

    /**
     * Kontrolle.
     *
     * @param pStage the stage
     */
    static public void kontrolle(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconKontrolle");
    }

    /**
     * Abstimmung.
     *
     * @param pStage the stage
     */
    static public void abstimmung(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconAbstimmung");
    }

    /**
     * Master.
     *
     * @param pStage the stage
     */
    static public void master(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconMaster");
    }

    /**
     * Service desk.
     *
     * @param pStage the stage
     */
    static public void serviceDesk(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconServiceDesk");
    }

    /**
     * Teilnehmerverzeichnis.
     *
     * @param pStage the stage
     */
    static public void teilnehmerverzeichnis(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconTeilnehmerverzeichnis");
    }

    /**
     * Bestandsverwaltung.
     *
     * @param pStage the stage
     */
    static public void bestandsverwaltung(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconBestandsverwaltung");
    }

    /**
     * Hotline.
     *
     * @param pStage the stage
     */
    static public void hotline(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconHotline");
    }

    /**
     * Register.
     *
     * @param pStage the stage
     */
    static public void register(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconRegister");
    }

    /**
     * Designer.
     *
     * @param pStage the stage
     */
    static public void designer(Stage pStage) {
        setzeIcon(pStage, "/meetingClientImages/IconDesigner");
    }

}
