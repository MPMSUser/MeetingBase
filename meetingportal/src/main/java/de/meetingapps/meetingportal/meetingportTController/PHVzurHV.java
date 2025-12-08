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

import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PHVzurHV {

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TLoginLogout tLoginLogout;
    private @Inject TLoginLogoutSession tLoginLogoutSession;
   private @Inject TMenue tMenue;
   private @Inject TSession tSession;
   private @Inject EclLoginDatenM eclLoginDatenM;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public void init(boolean pOpenDurchfuehren) {

    }
    

    public String doAufrufen() {
        int[] zulaessigeMasken= {KonstPortalView.P_HV_ZUR_HV,KonstPortalView.P_HV_ZUR_BEIRATSWAHL}; 
        if (!tSessionVerwaltung.pruefeStart(zulaessigeMasken)) {
            return "";
        }
        
        tLoginLogoutSession.setLoginKennung(eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
        tLoginLogoutSession.setLoginPasswort(eclLoginDatenM.getEclLoginDaten().passwortVerschluesselt);
        
        tSessionVerwaltung.setPermanentPortal("2");
        tLoginLogout.doLoginAusfuehren();
        tMenue.clearMenue();
        tSession.setUserEingeloggt("2");
        return "portal.xhtml";
       
    }

}
