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
package de.meetingapps.meetingportal.meetingportTController;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TFehlerView {

    private int logDrucken=10;
    
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TFehlerViewSession tFehlerViewSession;

    /*++++++++++++++++++++++Buttons für FEHLER_VIEW+++++++++++++++++*/
    public void doZumNextView() {
        CaBug.druckeLog("", logDrucken, 10);
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.FEHLER_VIEW)) {
            return;
        }
        CaBug.druckeLog("tFehlerViewSession.getNextView()="+tFehlerViewSession.getNextView(), logDrucken, 10);
        tSessionVerwaltung.setzeEnde(tFehlerViewSession.getNextView());
    }

    
    
    /*++++++++++++++++++++++Buttons für FEHLER_DIALOG+++++++++++++++++*/
    public void doFehlerDialogZumLogin() {
        tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
        return;
    }

    /*++++++++++++++++++++++Buttons für FEHLER_VERÄNDERT+++++++++++++++++*/
    public void doFehlerVeraendertZumLogin() {
        tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
        return;
    }

}
