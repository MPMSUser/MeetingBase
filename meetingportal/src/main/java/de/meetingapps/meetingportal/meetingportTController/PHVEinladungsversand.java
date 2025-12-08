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
import de.meetingapps.meetingportal.meetComBrM.BrMHVEinladungsversand;
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
public class PHVEinladungsversand {

    private int logDrucken=3;

    private @Inject EclDbM eclDbM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TMenue tMenue;
    private @Inject TMenueSession tMenueSession;
    private @Inject PHVEinladungsversandSession pHVEinladungsversandSession;

    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject BrMAktionaersdaten brMAktionaersdaten;
    private @Inject BrMHVEinladungsversand brMHVEinladungsversand;

    private @Inject TRemoteAR tRemoteAR;

    /**pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
     */
    public void init(boolean pOpenDurchfuehren) {
        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }
     
        int rc=brMAktionaersdaten.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
            if (pOpenDurchfuehren) {
                eclDbM.closeAll();
            }
           return;
        }
        brMHVEinladungsversand.holeAktuelleEinstellungen(eclLoginDatenM.getEclLoginDaten().loginKennung);

        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }

    }
    
    
    /**Wird beim Initialisieren der Maske aufgerufen, um Änderungsverfolgung zu initialisieren -
     * Änderungsverfolgung dient dazu, um beim Verlassen über menü festzustellen,
     * ob noch Eingaben unabgespeichert sind
     */
    public void aenderungsverfolgungStart() {
        pHVEinladungsversandSession.setVorAendungAuswahl(pHVEinladungsversandSession.getAuswahl());
    }
    
    /**Liefert beim Verlassen der Maske über Menü true, wenn unabgespeicherte
     * Änderungen vorhanden sind
      */
    public boolean aenderungsverfolgungEtwasUngespeichert() {
        CaBug.druckeLog("pHVEinladungsversandSession.getVorAendungAuswahl()="+pHVEinladungsversandSession.getVorAendungAuswahl(), logDrucken, 10);
        CaBug.druckeLog("pHVEinladungsversandSession..getAuswahl()="+pHVEinladungsversandSession.getAuswahl(), logDrucken, 10);
        if (!pHVEinladungsversandSession.getVorAendungAuswahl().equals(pHVEinladungsversandSession.getAuswahl())) {return true;}
        return false;
    }

    
    public void doAbbrechen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_HV_EINLADUNGSVERSAND)) {
            return;
        }
        tMenue.clearMenue();
        tMenueSession.setAenderungsmodus(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
   }

    public void doSenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_HV_EINLADUNGSVERSAND)) {
            return;
        }
        eclDbM.openAll();
        int rc=brMHVEinladungsversand.speichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        CaBug.druckeLog("rc speichern="+rc, logDrucken, 10);
        if (rc==1 || rc==2 || rc==3) {
            /*Nun noch Meldung setzen: 
             * ==1 => ok, Post wurde gespeichert
             * ==2 => ok, Mail wurde gespeichert
             * 
             * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
             */
            switch (rc) {
            case 1:tSession.trageQuittungTextNr(1651, 2);break;
            case 2:tSession.trageQuittungTextNr(1652, 2);break;
            case 3:tSession.trageQuittungTextNr(1653, 2);break;
            }
            
          }
        eclDbM.closeAll();
        tMenueSession.setAenderungsmodus(false);
        tMenue.clearMenue();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_AUSWAHL);
       
    }

}
