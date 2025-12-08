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
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TEintrittskarteStornieren {

    private int logDrucken = 3;
   
    private @Inject EclDbM eclDbM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TAuswahl tAuswahl;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TWillenserklaerung tWillenserklaerung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TSession tSession;

    public void doZurueck(){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_STORNIEREN)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
       
    }
    

    /**Tatsächliches Durchführen des Storno einer Eintrittskarte / Eintrittskarte mit Vollmacht / Gastkarte. Bei Eintrittskarte
     * mit Vollmacht wird Vollmacht mit storniert.
     * 
     * Eingabeparameter:
     *      aDlgVariablen.ausgewaehlteAktion
     *      EclWillenserklaerungStatusM
     *      EclZugeordneteMeldungM
     *  
     * Ausgabeparameter:
     *      aDlgVariablen.fehlerMeldung/.fehlerNr
     * Zum Ende hin wird aFunktione.waehleAusgangsmaske aufgerufen, d.h. EclZugeordneteMeldungListeM wird
     * neu ermittelt.
     * 
     * DbBundle wird innerhalb der Funktion gehandled.
     * @return
     */
    public void doStornieren(){
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_STORNIEREN)) {
            return;
        }
        try {

        eclDbM.openAll();
        CaBug.druckeLog("A", logDrucken, 10);
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }
 
        boolean brc=stornierenAusfuehren(true);
        if (brc==false) {
            eclDbM.closeAllAbbruch();
            return;
        }
        
        CaBug.druckeLog("J", logDrucken, 10);

        eclDbM.closeAll();
    } catch (Exception e) {
        CaBug.drucke("Exception");
        System.out.println(e.getMessage());
        e.printStackTrace();
        eclDbM.closeAll();
    }
        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE_STORNIEREN_QUITTUNG);
        return;
    }
    
    public boolean stornierenAusfuehren(boolean pMitBestaetigung) {
        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            CaBug.druckeLog("C", logDrucken, 10);
            EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);
            EclWillenserklaerungStatusNeu iEclWillenserklaerungStatusNeu=
                    tWillenserklaerungSession.getZugeordneteWillenserklaerungStatusListe().get(i);
            CaBug.druckeLog("D", logDrucken, 10);
           boolean brc = tWillenserklaerung
                    .checkKeineNeueWillenserklaerungFuerAnmeldung(iEclZugeordneteMeldungNeu);
           CaBug.druckeLog("E", logDrucken, 10);
            if (brc == false) {
                CaBug.druckeLog("F", logDrucken, 10);
                return false;
            }
            CaBug.druckeLog("G", logDrucken, 10);
            brc=tWillenserklaerung.storniereEintrittskarte(iEclZugeordneteMeldungNeu, iEclWillenserklaerungStatusNeu, pMitBestaetigung);
            CaBug.druckeLog("H", logDrucken, 10);
           if (brc == false) {
               CaBug.druckeLog("I", logDrucken, 10);
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public boolean pruefenObStornierungZulaessig() {
        if (tSession.isPortalEKIstMoeglich() == false) {
            return false;
        }
        return true;
             
    }
    
    /*******************Funktionen für iEintrittskarteStornierenQuittung************/
    public void doWeiterQuittung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.EINTRITTSKARTE_STORNIEREN_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
       
    }
}
