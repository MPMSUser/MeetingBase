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
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TWeisungStornieren {

    private int logDrucken = 5;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;

    private @Inject TAuswahl tAuswahl;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclParamM eclParamM;
    private @Inject TSession tSession;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TQuittungen tQuittungen;

    /**Stornieren von Weisungen - tatsächlich durchführen
     * 
     * Eingabeparameter:
     * 		TWillenserklaerungSession.ausgewaehlteAktion
     * 		TWillenserklaerungSession.meldungsIdentListe
     * 		TWillenserklaerungSession.sammelIdentListe
     * 		TWillenserklaerungSession.willenserklaerungIdentListe
     * 	
     * Ausgabeparameter:
     * 		tSession.fehlerMeldung/.fehlerNr
     * Zum Ende hin wird 
     * 		tAuswahl.startAuswahl(true);
     * aufgerufen, d.h. Statusanzeige wird
     * neu ermittelt.
     * 
     * dbBundle wird innerhalb der Funktion gehandled.
     */
    public void doStornieren() {
        try {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_STORNIEREN)) {
            return;
        }

        if (tSessionVerwaltung.getStartPruefen() == 1 && !tWillenserklaerungSession.isBestaetigtDassBerechtigt()
                && ((eclParamM.isCheckboxBeiSRV()
                        && tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_STORNIEREN)
                        || (eclParamM.isCheckboxBeiBriefwahl() && tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_STORNIEREN)
                        || (eclParamM.isCheckboxBeiKIAV() && tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_STORNIEREN))) {
            tSession.trageFehlerEin(CaFehler.afBestaetigungBerechtigungFehlt);
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.openAll();
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }
       

        boolean brc=stornierenAusfuehren(true);
        if (brc==false) {
            eclDbM.closeAllAbbruch();
            tSessionVerwaltung.setzeEnde();
            return;

        }

 
        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_STORNIEREN_QUITTUNG);
            eclDbM.closeAll();
           return;
        } else {
            int rcAuswahl=tAuswahl.startAuswahl(true);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(rcAuswahl);
            return;
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

    }

    
    public boolean stornierenAusfuehren(boolean pMitBestaetigung) {
        
        for (int i = 0; i < tWillenserklaerungSession.getZugeordneteMeldungFuerAusfuehrungListe().size(); i++) {
            EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu = tWillenserklaerungSession
                    .getZugeordneteMeldungFuerAusfuehrungListe().get(i);

            if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                    iEclZugeordneteMeldungNeu.getMeldungsIdent(),
                    iEclZugeordneteMeldungNeu.getIdentHoechsteWillenserklaerung()) == false) {
                 tSession.trageFehlerEin(CaFehler.afAndererUserAktiv);
            }

            CaBug.druckeLog("iEclZugeordneteMeldungNeu.artBeziehung=" + iEclZugeordneteMeldungNeu.artBeziehung,
                    logDrucken, 10);
            CaBug.druckeLog("iEclZugeordneteMeldungNeu.vollmachtsart=" + iEclZugeordneteMeldungNeu.vollmachtsart,
                    logDrucken, 10);

            BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();
            willenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
            willenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

            willenserklaerung.piMeldungsIdentAktionaer = tWillenserklaerungSession.getMeldungsIdentListe().get(i);
            willenserklaerung.pWillenserklaerungGeberIdent = iEclZugeordneteMeldungNeu.personNatJurIdent;
            CaBug.druckeLog(
                    "willenserklaerung.pWillenserklaerungGeberIdent=" + willenserklaerung.pWillenserklaerungGeberIdent,
                    logDrucken, 10);
            //          willenserklaerung.pWillenserklaerungGeberIdent=-1; /*Aktionär*/
            willenserklaerung.pAufnehmendeSammelkarteIdent = tWillenserklaerungSession.getSammelIdentListe().get(i);

            EclMeldungZuSammelkarte meldungZuSammelkarte = new EclMeldungZuSammelkarte();
            meldungZuSammelkarte.willenserklaerungIdent = tWillenserklaerungSession.getWillenserklaerungIdentListe()
                    .get(i);
            willenserklaerung.pEclMeldungZuSammelkarte = meldungZuSammelkarte;

            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_STORNIEREN) {
                willenserklaerung.widerrufVollmachtUndWeisungAnSRV(eclDbM.getDbBundle());
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_STORNIEREN) {
                willenserklaerung.widerrufBriefwahl(eclDbM.getDbBundle());
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_STORNIEREN) {
                willenserklaerung.widerrufVollmachtUndWeisungAnKIAV(eclDbM.getDbBundle());
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN) {
                willenserklaerung.widerrufDauervollmachtAnKIAV(eclDbM.getDbBundle());
            }
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ORGANISATORISCH_STORNIEREN) {
                willenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte(eclDbM.getDbBundle());
            }

            /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
            if (willenserklaerung.rcIstZulaessig == false) {
                tSession.trageFehlerEin(willenserklaerung.rcGrundFuerUnzulaessig);
                return false;
            }
            if (pMitBestaetigung) {
                tQuittungen.bestaetigenSrvBriefwahlStornieren(
                        willenserklaerung.rcWillenserklaerungIdent, 
                        iEclZugeordneteMeldungNeu.eclMeldung);
            }

        }

     
        
        return true;
    }
    
    @Deprecated
    public boolean pruefenWeisungStornierenZulaessig() {
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_STORNIEREN) {
            if (tSession.isPortalSRVIstMoeglich() == false) {
                return false;
            }
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_STORNIEREN) {
            if (tSession.isPortalBriefwahlIstMoeglich() == false) {
                 return false;
            }
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_STORNIEREN) {
            if (tSession.isPortalKIAVIstMoeglich() == false) {
                return false;
            }
        }
        return true;
       
    }
    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_STORNIEREN)) {
            return;
        }
        /*Öffnen*/
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(tFunktionen.waehleAuswahl())) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        tAuswahl.startAuswahl(true);

        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

}
