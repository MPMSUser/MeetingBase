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
package de.meetingapps.meetingportal.meetingport;

import de.meetingapps.meetingportal.meetComEclM.EclAnredeListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/*Weitere Controllerfunktionen für GGGastStatus in aControllerEintrittskarteQuittung!*/

@RequestScoped
@Named
@Deprecated
public class AControllerGGGastStatus {

//    @Inject
//    EDlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclAnredeListeM lAnredeListeM;
    @Inject
    EclGastM lGastM;

    public String doRegistrieren(String pGastIdent) {
        if (!aFunktionen.pruefeStart("aGGGastStatus")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();

        lGastM.init();

        /*Achtung: noch nicht berücksichtigt, dass storniert, mehrere ZutrittsIdents, etc. ....*/
        eclDbM.getDbBundle().dbZutrittskarten.readZuMeldungsIdentGast(Integer.parseInt(pGastIdent));

        lGastM.setNummer(eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0).zutrittsIdent);
        lGastM.setMeldeIdent(Integer.parseInt(pGastIdent));

        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aGGNeuerGastRegistrieren", true, false);

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aGGGastStatus")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde(naechsteMaske, true, false);
    }

    public String doAbmelden() {
//        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
