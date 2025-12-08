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
package de.meetingapps.meetingclient.meetingClientOberflaechen;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlWaehleModule;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.stage.Stage;

public class CaOpenWindow {

    public static void openModulauswahl(Stage stage) {

        Stage newStage = new Stage();
        CaIcon.standard(newStage);

        CtrlWaehleModule controllerFenster = new CtrlWaehleModule();
        controllerFenster.init(newStage);
        controllerFenster.eingeloggterUserLogin = ParamS.eclUserLogin;

        CaController caController = new CaController();
        
        if (stage != null)
            Platform.runLater(() -> stage.hide());

        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/WaehleModule.fxml", controllerFenster.width,
                controllerFenster.height, "Modul auswählen", false);
    }
}
