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


import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBrM.BrMBestand;
import de.meetingapps.meetingportal.meetComBrM.BrMBestandhistorie;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PBestandhistorie {

    private @Inject EclDbM eclDbM;

    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject BrMBestandhistorie brMBestandhistorie;
    
    private @Inject BrMBestand brMBestand;

    private @Inject PBestandhistorieSession pBestandhistorieSession;
    
    private @Inject TRemoteAR tRemoteAR;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public int init(boolean pOpenDurchfuehren) {

        pBestandhistorieSession.clear();

        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }

        /*
         * Aktueller Stand = Alle Umsätze
         */
        int rc = brMBestandhistorie.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        /*int rc2 = */ brMBestand.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);

        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }
        if (tRemoteAR.pruefeVerfuegbar(rc) == false) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        return 1;
    }
    
    /**
     * Hole Stand für bestimmten Zeitraum
     */
    public int doLadeUmsaetze() {
        pBestandhistorieSession.setBestandshistorie(null);
        pBestandhistorieSession.setDatumVonMobil(pBestandhistorieSession.getDatumVon());
        pBestandhistorieSession.setDatumBisMobil(pBestandhistorieSession.getDatumBis());
        eclDbM.openAll();
        int rc = brMBestandhistorie.holeUmsaetze(eclLoginDatenM.getEclLoginDaten().loginKennung);
        eclDbM.closeAll();
        if (tRemoteAR.pruefeVerfuegbar(rc) == false) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        return 1;
    }
    
    public int doLadeUmsaetzeMobil() {
        pBestandhistorieSession.setBestandshistorie(null);
        pBestandhistorieSession.setDatumVon(pBestandhistorieSession.getDatumVonMobil());
        pBestandhistorieSession.setDatumBis(pBestandhistorieSession.getDatumBisMobil());
        eclDbM.openAll();
        int rc = brMBestandhistorie.holeUmsaetzeMobil(eclLoginDatenM.getEclLoginDaten().loginKennung);
        eclDbM.closeAll();
        if (tRemoteAR.pruefeVerfuegbar(rc) == false) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        return 1;
    }
}
