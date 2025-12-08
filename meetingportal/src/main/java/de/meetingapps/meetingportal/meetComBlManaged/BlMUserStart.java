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
package de.meetingapps.meetingportal.meetComBlManaged;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvUserLogin;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Klasse enthält Methoden, die beim Login eines Users über ein Portal aufgerufen werden können/müssen **/

/*TODO _Login Portal: mandantenabhängige Logins noch vorsehen! Dafür erforderlich: Abfragen des Parameters "Mandant" aus Aufruflink*/

@RequestScoped
@Named
public class BlMUserStart {

    @Inject
    EclDbM eclDbM;
    @Inject
    EclParamM eclParamM;

    /**Initialisierung der (mandentenunabhängigen) Basis-Parameter.
     * Aufzurufen, bevor User-Login-Daten verarbeitet werden.
     */
    public void startMitUnbekanntemMandant() {
        System.out.println("A");
        eclDbM.openAll();
        eclDbM.closeAll();
    }

    /**Prüfen / Verarbeiten User-Login-Daten
     * rc=1 => erfolgreicher Login, nun Mandanten abfragen
     * In eclParamM.eclUserLogin steht der eingeloggte User.
     * 
     * EclDbM wird in dieser Methode verwaltet (d.h. geöffnet/geschlossen).
     * 
     * Fehlermeldung:
     * afKennungUnbekannt
     * afPasswortFalsch
     * afKennungGesperrt
     * afNeuesPasswortErforderlich (in diesem Fall ist eclParamM.eclUserLogin gefüllt, bei den anderen Fehlern nicht)
     * **/
    public int pruefeLoginDaten(String pKennung, String pPasswort) {
        if (pKennung.isEmpty()) {
            if (eclParamM.getParamServer().pLocalInternPasswort == 1) { /**Kennung ist einzugeben => Fehlermeldung*/
                return CaFehler.afKennungUnbekannt;
            }
            pKennung = "LADMIN";
            pPasswort = "B2e!!e0r0ra1n8ge";
        }
        if (pPasswort.isEmpty()) {
            return CaFehler.afPasswortFalsch;
        }

        /*Hier: Passwort und Kennung gefüllt*/
        eclDbM.openAll();
        BvUserLogin lBvUserLogin = new BvUserLogin();
        int rc = lBvUserLogin.pruefeKennung(eclDbM.getDbBundle(), pKennung, pPasswort, true, false, 2);
        eclParamM.setEclUserLogin(lBvUserLogin.rcUserLogin);
        eclDbM.closeAll();

        return rc;
    }

}
