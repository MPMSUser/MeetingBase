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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TWeisungBestaetigung {

    private int logDrucken = 3;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclParamM eclParamM;
    private @Inject EclDbM eclDbM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TWillenserklaerung tWillenserklaerung;
    private @Inject TFehlerViewSession tFehlerViewSession;

    private @Inject TAuswahl tAuswahl;

    /*************************Abspeichern Vollmacht/Weisung an Stimmrechtsvertreter**************************
     * Eingabeparameter:
     * 		tWillenserklaerungSession.ausgewaehlteHauptAktion
     * 
     * 		tWillenserklaerungSession.besitzAREintrag (früher:EclTeilnehmerLoginM.AnmeldeAktionaersnummer) (bei Erstanmeldung)
     * 		tWillenserklaerungSession.zugeordneteMeldungFuerAusfuehrung (früher:EclZugeordneteMeldungM) (bei späteren Willenserklärungen)
     * 
     *      tWillenserklaerungSession.meldungsIdentListe
     *      tWillenserklaerungSession.sammelIdentListe
     *      tWillenserklaerungSession.weisungIdentListe
     *      tWillenserklaerungSession.willenserklaerungIdentListe
     *      
     * 		EclAbstimmungenListeM (mit Weisungen) (Alternative 1 oder Alternative 2)
     * 
     * 
     * Rückgabe:
     * 		tSession.fehlerMeldung/.fehlerNr
     * 
     * DbBundle wird innerhalb der Funktion geregelt
     * 
     * */
    public void doErteilen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_BESTAETIGUNG)) {
            return;
        }

        eclDbM.openAll();
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }
        
        boolean brc = erteilenPortal();
        if (brc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_QUITTUNG);
            return;
        } else {
            int rcAuswahl=tAuswahl.startAuswahl(true);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(rcAuswahl);
            return;
        }
    }

    
    public boolean erteilenPortal() {
         return erteilen(true);
    }
    
    /**False => Abbruch. Fehler und ggf. Folgemaske sind bereits gesetzt*/
    public boolean erteilen(boolean pMitBestaetigung) {

        CaBug.druckeLog("", logDrucken, 5);

        if (tSessionVerwaltung.getStartPruefen() == 1 && !tWillenserklaerungSession.isBestaetigtDassBerechtigt()
                && ((eclParamM.isCheckboxBeiSRV() && (tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_NEU
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_AENDERN))
                        || (eclParamM.isCheckboxBeiBriefwahl() && (tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_NEU
                                || tWillenserklaerungSession
                                        .getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_AENDERN))
                        || (eclParamM.isCheckboxBeiKIAV() && tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_MIT_WEISUNG_NEU))) {
            tSession.trageFehlerEin(CaFehler.afBestaetigungBerechtigungFehlt);
            CaBug.druckeLog("false 1", logDrucken, 10);
            return false;
        }

 
        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            for (EclBesitzAREintrag iEclBesitzAREintrag : tWillenserklaerungSession.getBesitzAREintragListe()) {
                boolean brc = tWillenserklaerung.anmelden1Meldung(iEclBesitzAREintrag, false);
                if (brc == false) {
                    CaBug.druckeLog("false 2", logDrucken, 10);
                    return false;
                }
                brc = tWillenserklaerung.srvBriefwahlBeiErstanmeldung(iEclBesitzAREintrag, pMitBestaetigung);
                if (brc == false) {
                    CaBug.druckeLog("false 3", logDrucken, 10);
                    return false;
                }
            }
        } else {
            for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
                EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                        .getZugeordneteMeldungFuerAusfuehrungListe().get(i);

                if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                        iEclZugeordneteMeldungNeu.getMeldungsIdent(),
                        iEclZugeordneteMeldungNeu.getIdentHoechsteWillenserklaerung()) == false) {
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV);
                    tFehlerViewSession.setNextView(KonstPortalView.LOGIN);
                    tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                    CaBug.druckeLog("false 4", logDrucken, 10);
                   return false;
                }
                boolean brc=false;
                brc = tWillenserklaerung.srvBriefwahlZuBestehenderAnmeldung(iEclZugeordneteMeldungNeu, pMitBestaetigung);
                if (brc == false) {
                    CaBug.druckeLog("false 5", logDrucken, 10);
                    return false;
                }
            }
        }

        return true;
    }

    
    /**Kann für Briefwahl, SRV und KIAV aufgerufen werden*/
    @Deprecated
    public boolean pruefeObZulaessigErteilen() {
        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*Neuanmeldung - Anmeldephase?*/
            if (tSession.isPortalErstanmeldungIstMoeglich() == false) {
                return false;
            }
        }
        
         
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
            if (tSession.isPortalSRVIstMoeglich() == false) {
                return false;
           }
            break;
            
        case KonstPortalAktion.BRIEFWAHL_NEU:
            if (tSession.isPortalBriefwahlIstMoeglich() == false) {
                return false;
           }
            break;
      
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
        case KonstPortalAktion.KIAV_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            if (tSession.isPortalKIAVIstMoeglich() == false) {
                return false;
           }
            break;
           
        case KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            if (tSession.isPortalKIAVIstMoeglich() == false) {
                return false;
           }
            break;
           
        case KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU:
        case KonstPortalAktion.ORGANISATORISCH_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            if (tSession.isPortalKIAVIstMoeglich() == false) {
                return false;
           }
            break;
          
        }

        
        
        return true;
    }
    
    /**Ändern von Weisungen (SRV, Briefwahl, KIAV) - tatsächlich durchführen
     * 
     * Eingabeparameter:
     *      tWillenserklaerungSession.ausgewaehlteHauptAktion
     * 
     *      tWillenserklaerungSession.besitzAREintrag (früher:EclTeilnehmerLoginM.AnmeldeAktionaersnummer) (bei Erstanmeldung)
     *      tWillenserklaerungSession.zugeordneteMeldungFuerAusfuehrung (früher:EclZugeordneteMeldungM) (bei späteren Willenserklärungen)
     * 
     *      tWillenserklaerungSession.meldungsIdentListe
     *      tWillenserklaerungSession.sammelIdentListe
     *      tWillenserklaerungSession.weisungIdentListe
     *      tWillenserklaerungSession.willenserklaerungIdentListe
     *      
     *      EclAbstimmungenListeM (mit Weisungen) (Alternative 1 oder Alternative 2)
     * 
     * 	
     * Ausgabeparameter:
     * 		tSession.fehlerMeldung/.fehlerNr
     * Zum Ende hin wird aFunktione.leseStatusPortal aufgerufen, d.h. Anmeldestatus wird
     * neu ermittelt.
     * 
     * dbBundle wird innerhalb der Funktion gehandled.
     */

    public void doAendernSpeichern() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_BESTAETIGUNG)) {
            return;
        }

        eclDbM.openAll();
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }
        
        boolean brc = aendernErteilen();
        if (brc == false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_QUITTUNG);
            return;
        } else {
            int rcAuswahl=tAuswahl.startAuswahl(true);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(rcAuswahl);
            return;
        }
    }

    public boolean aendernErteilen() {

        CaBug.druckeLog("", logDrucken, 5);

        if (tSessionVerwaltung.getStartPruefen() == 1 && !tWillenserklaerungSession.isBestaetigtDassBerechtigt()
                && ((eclParamM.isCheckboxBeiSRV()
                        && tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_AENDERN)
                        || (eclParamM.isCheckboxBeiBriefwahl() && tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_AENDERN)
                        || (eclParamM.isCheckboxBeiKIAV() && tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_WEISUNG_AENDERN))) {
            tSession.trageFehlerEin(CaFehler.afBestaetigungBerechtigungFehlt);
            return false;
        }

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);

            if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                    iEclZugeordneteMeldungNeu.getMeldungsIdent(),
                    iEclZugeordneteMeldungNeu.getIdentHoechsteWillenserklaerung()) == false) {
                CaBug.druckeLog("reCheckKeineNeueWillenserklaerungen==false", logDrucken, 10);
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV);
                tFehlerViewSession.setNextView(KonstPortalView.LOGIN);
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                return false;
            }

            boolean brc = tWillenserklaerung.srvBriefwahlAendern(i, iEclZugeordneteMeldungNeu);
            if (brc == false) {
                return false;
            }

        }

        return true;
    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_BESTAETIGUNG)) {
            return;
        }
        tWillenserklaerungSession.setBestaetigtDassBerechtigt(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG);
    }

    public void doZurueckAendern() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_BESTAETIGUNG)) {
            return;
        }
        tWillenserklaerungSession.setBestaetigtDassBerechtigt(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_AENDERN);
    }

}
