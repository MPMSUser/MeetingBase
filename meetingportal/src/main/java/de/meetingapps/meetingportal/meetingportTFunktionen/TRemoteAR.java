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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTController.TLoginLogoutSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TRemoteAR {

    private @Inject TSession tSession;
    private @Inject TLoginLogoutSession tLoginLogoutSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclLoginDatenM eclLoginDatenM;

    public boolean pruefeVerfuegbar(int pRc) {
        if (pRc==CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
            tLoginLogoutSession.clearAll();
            tSession.setViewnummer(liefereSeiteLogin());
            tSessionVerwaltung.protokolliereDialogschritt("Remote-Register nicht verfügbar", eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
            tLoginLogoutSession.trageFehlerEin(CaFehler.perRemoteAktienregisterNichtVerfuegbar);
            return false;
        }
        return true;
    }
    
    private int liefereSeiteLogin() {
        if (tSession.isPermanentPortal()) {return KonstPortalView.P_LOGIN;}
        else {
            return KonstPortalView.LOGIN;
        }
    }

}
