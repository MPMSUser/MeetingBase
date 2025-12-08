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
package de.meetingapps.meetingclient.meetingClient;

import de.meetingapps.meetingclient.meetingClientAllg.CaProgrammStart;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Class MainClientMeeting.
 */
public class MainClientMeeting extends Application {

    /** The start args. */
    static String[] startArgs = null;

    /** The primary stage. */
    private Stage primaryStage;

    /**
     * Start.
     *
     * @param pPrimaryStage the primary stage
     */
    @Override
    public void start(Stage pPrimaryStage) {

        primaryStage = pPrimaryStage;
        CaIcon.standard(primaryStage);

        CaProgrammStart caProgrammStart = new CaProgrammStart();

        if (!caProgrammStart.programmstart(startArgs))
            return;

        waehleModulAus();

    }

    /**
     * Waehle modul aus.
     */
    private void waehleModulAus() {
        CaOpenWindow.openModulauswahl(null);
    }

    /**
     * ****************Fehlermeldungsdialog anzeigen****************************.
     *
     * @param fehlertext the fehlertext
     */
    protected void fehlerMeldung(String fehlertext) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(primaryStage, fehlertext);
        return;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        //        CaLogger.erzeugen();

        startArgs = args;

        //        if (CaLogger.logger.isErrorEnabled()) {
        //            CaLogger.logger.error("Test");
        //        }
        //
        launch(args);

    }

}
