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

import de.meetingapps.meetingportal.meetComBlManaged.BlMUserListen;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Eintragen / Austragen und überprüfen von Usern
 * Abfragen von eingeloggten Usern
 * 
 * Alles zum Verhindern von Doppeltanmeldungen etc.
 *
 */
@RequestScoped
@Named
public class TUserSessionManagement {

    private @Inject EclParamM eclParamM;
    private @Inject TSession tSession;
    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject BlMUserListen blmUserListen;

    public void eintragenUser() {
        if (eclParamM.getParam().paramPortal.doppelLoginGesperrt == 0) {
            return;
        }

        blmUserListen.trageUserEin(eclLoginDatenM.getEclLoginDaten().loginKennung, eclParamM.getClGlobalVar().mandant,
                eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                eclParamM.getClGlobalVar().datenbereich, tSession.getJsfSessionId());
    }

    public void austragenUser() {
        if (eclParamM.getParam().paramPortal.doppelLoginGesperrt == 0) {
            return;
        }

        blmUserListen.trageUserAus(eclLoginDatenM.getEclLoginDaten().loginKennung, eclParamM.getClGlobalVar().mandant,
                eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                eclParamM.getClGlobalVar().datenbereich, tSession.getJsfSessionId());

    }

    /**Prüft, ob diese Session durch eine zweite Session auf dem selben Server gesperrt wurde*/
    public boolean pruefenObSessionMittlerweileGesperrt() {
        if (eclParamM.getParam().paramPortal.doppelLoginGesperrt == 0) {
            return false;
        }

        if (eclLoginDatenM.getEclLoginDaten().ident == 0) {
            return false;
        } //Dann noch kein User eingetragen

        boolean brc = blmUserListen.pruefeObUserEingetragen(eclLoginDatenM.getEclLoginDaten().loginKennung,
                eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr,
                eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich,
                tSession.getJsfSessionId());
        return !brc;
    }

    /**Prüfen, ob Ablauf noch in selber Session*/
    public boolean pruefenObSessionNochGleich() {
        //		String hJsfSession=BsEndpoint.userToJsf.get(BmSocketUserKey.erzeugeKey(eclLoginDatenM.getEclLoginDaten().loginKennung, eclParamM.getClGlobalVar().mandant, eclParamM.getClGlobalVar().hvJahr, 
        //				eclParamM.getClGlobalVar().hvNummer, eclParamM.getClGlobalVar().datenbereich));
        //		if (!hJsfSession.equals(tSession.getJsfSessionId())) {
        //			return false;
        //		}
        return true;
    }

}
