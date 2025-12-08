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
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterBestandsaenderungen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PBestand {

    private @Inject EclDbM eclDbM;

    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject BrMBestand brMBesitz;

    private @Inject TRemoteAR tRemoteAR;
    
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    
    private @Inject PBeteiligungserhoehung pBeteiligungserhoehung;
    
    private @Inject PBestandSession pBestandSession;
    
    private @Inject TSession tSession;
   
    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public int init(boolean pOpenDurchfuehren) {
        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }
     
        int rc=brMBesitz.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);

        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return CaFehler.perRemoteAktienregisterNichtVerfuegbar;}
        return 1;
    }
    
    public void doBeteiligungserhoehung() {
        int rc=pBeteiligungserhoehung.init(true);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {return;}
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_BETEILIGUNGSERHOEHUNG);
    }
    
    public void doKuendigungsRuecknahme(EclAktienregisterBestandsaenderungen eclAktienregisterBestandsaenderungen) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BESTAND)) {
            return;
        }
        
        pBestandSession.setGewaehlteBestandsaenderung(eclAktienregisterBestandsaenderungen);
        pBestandSession.setShowKuendigungModal(true);
        tSessionVerwaltung.setzeEnde();
        return;
    }
    
    public void doZurueckKuendigung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BESTAND)) {
            return;
        }
        
        pBestandSession.setGewaehlteBestandsaenderung(null);
        pBestandSession.setShowKuendigungModal(false);
        tSessionVerwaltung.setzeEnde();
        return;
    }
    
    public void doKuendigungZuruecknehmen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BESTAND)) {
            return;
        }
        
        int result = brMBesitz.kuendigungZuruecknehmen(eclLoginDatenM.getEclLoginDaten().loginKennung);
        
        if (result == 1) {
            pBestandSession.setShowKuendigungErfolgModal(true);
        } else {
            tSession.trageFehlerEinMitArt("Bitte versuchen Sie es in wenigen Augenblicken erneut.", 1);
        }
        pBestandSession.setGewaehlteBestandsaenderung(null);
        pBestandSession.setShowKuendigungModal(false);
        tSessionVerwaltung.setzeEnde();
        return;
    }
    
    public void doKuendigungRuecknahmeEnde() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_BESTAND)) {
            return;
        }
        eclDbM.openAll();
        int rc=brMBesitz.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        eclDbM.closeAll();
        pBestandSession.setShowKuendigungErfolgModal(false);
        tSessionVerwaltung.setzeEnde();
        return;
    }

    
}
