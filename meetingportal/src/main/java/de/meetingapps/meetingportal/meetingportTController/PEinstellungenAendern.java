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

import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PEinstellungenAendern {

//    private int logDrucken=10;

    private @Inject EclDbM eclDbM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TMenue tMenue;
    private @Inject TMenueSession tMenueSession;

    private @Inject PEinstellungenAendernSession pEinstellungenAendernSession;

    private @Inject EclLoginDatenM eclLoginDatenM;

    public void init() {
        pEinstellungenAendernSession.setPasswortNeuVergeben(false);
        pEinstellungenAendernSession.setNeuesPasswort("");
        pEinstellungenAendernSession.setNeuesPasswortBestaetigung("");
    }

    /**Wird beim Initialisieren der Maske aufgerufen, um Änderungsverfolgung zu initialisieren -
     * Änderungsverfolgung dient dazu, um beim Verlassen über menü festzustellen,
     * ob noch Eingaben unabgespeichert sind
     */
    public void aenderungsverfolgungStart() {
        
    }
    
    /**Liefert beim Verlassen der Maske über Menü true, wenn unabgespeicherte
     * Änderungen vorhanden sind
      */
    public boolean aenderungsverfolgungEtwasUngespeichert() {
        if (pEinstellungenAendernSession.isPasswortNeuVergeben()) {return true;}
        return false;
    }

    

    public void doAbbrechen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_EINSTELLUNG_AENDERN)) {
            return;
        }
        tMenue.clearMenue();
        tMenueSession.setAenderungsmodus(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
   }

    public void doSenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_EINSTELLUNG_AENDERN)) {
            return;
        }
        if (pEinstellungenAendernSession.isPasswortNeuVergeben()==false) {
            tSession.trageQuittungTextNr(1653, 2);
            tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
           return;
        }

        eclDbM.openAll();
        BlTeilnehmerLoginNeu blTeilnehmerLoginNeu=new BlTeilnehmerLoginNeu();
        blTeilnehmerLoginNeu.initDB(eclDbM.getDbBundle());
        blTeilnehmerLoginNeu.neuesPasswort=pEinstellungenAendernSession.getNeuesPasswort();
        blTeilnehmerLoginNeu.neuesPasswortBestaetigung=pEinstellungenAendernSession.getNeuesPasswortBestaetigung();
        int rc=blTeilnehmerLoginNeu.passwortEingabePruefen();
        if (rc<1) {
            tSession.trageFehlerEin(rc);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        blTeilnehmerLoginNeu.speichereNeuesPasswort(eclLoginDatenM.getEclLoginDaten().loginKennung, pEinstellungenAendernSession.getNeuesPasswort());
        tSession.trageQuittungTextNr(1661, 2);
        eclDbM.closeAll();
        tMenueSession.setAenderungsmodus(false);
        tMenue.clearMenue();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
    }
}
