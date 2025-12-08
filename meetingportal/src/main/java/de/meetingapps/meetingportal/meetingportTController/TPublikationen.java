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
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComBrM.BrMPublikationen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TPublikationen {
    
    private int logDrucken=10;

    private @Inject EclDbM eclDbM;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TMenue tMenue;
    private @Inject TMenueSession tMenueSession;
    private @Inject TPublikationenSession tPublikationenSession;
    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject BrMPublikationen brMPublikationen;
    private @Inject BrMAktionaersdaten brMAktionaersdaten;

    private @Inject TRemoteAR tRemoteAR;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public void init(boolean pOpenDurchfuehren) {
        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }
 
        pAktionaersdatenSession.clear();
        int rc=brMAktionaersdaten.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
            if (pOpenDurchfuehren) {
                eclDbM.closeAll();
            }
            return;
        }

        rc=brMPublikationen.holeAktuelleEinstellungen(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
            if (pOpenDurchfuehren) {
                eclDbM.closeAll();
            }
            return;
        }
        
        brMPublikationen.pruefeObAenderungenInArbeit(eclLoginDatenM.getEclLoginDaten().loginKennung, eclLoginDatenM.getEclLoginDaten().ident*(-1));
        
        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }

    }

    /**Wird beim Initialisieren der Maske aufgerufen, um Änderungsverfolgung zu initialisieren -
     * Änderungsverfolgung dient dazu, um beim Verlassen über menü festzustellen,
     * ob noch Eingaben unabgespeichert sind
     */
    public void aenderungsverfolgungStart() {
        tPublikationenSession.setVorAenderungVersandartNewsletter(tPublikationenSession.getVersandartNewsletter());
    }
    
    /**Liefert beim Verlassen der Maske über Menü true, wenn unabgespeicherte
     * Änderungen vorhanden sind
      */
    public boolean aenderungsverfolgungEtwasUngespeichert() {
        if (!tPublikationenSession.getVorAenderungVersandartNewsletter().equals(tPublikationenSession.getVersandartNewsletter())) {return true;}
        return false;
    }

    public void doAbbrechen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_PUBLIKATIONEN)) {
            return;
        }
        tMenue.clearMenue();
        tMenueSession.setAenderungsmodus(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
   }
    
    
    public void doSenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_PUBLIKATIONEN)) {
            return;
        }

        int versandwegNewsletter=Integer.parseInt(tPublikationenSession.getVersandartNewsletter());
        
        eclDbM.openAll();eclDbM.openWeitere();
        
        
        /*Verarbeitung durchführen*/
        int rc=brMPublikationen.schreibeEinstellungen(eclLoginDatenM.getEclLoginDaten().loginKennung, eclLoginDatenM.getEclLoginDaten().ident*(-1), versandwegNewsletter);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }
         
        eclDbM.closeAll();
        
        CaBug.druckeLog("Publikationen verarbeitet", logDrucken, 10);
        
        tMenueSession.setAenderungsmodus(false);
        tMenue.clearMenue();
        tSession.trageQuittungTextNr(1640, 2);
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);

    }
    
}
