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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEh.EhIdStringFuerAuswahl;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TMonitoring {

    private int logDrucken = 3;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;

    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TMonitoringSession tMonitoringSession;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    private @Inject TAbstimmungserg tAbstimmungserg;
    private @Inject TTeilnehmerverz tTeilnehmerverz;
 
    private @Inject BlMAbstimmung blMAbstimmung;

    /**eclDbM öffnen/schließen in aufrufender Funktion*/
    public int startMonitoring() {
        tMonitoringSession.setAnzeigenAbstimmungen(false);
        return refreshMonitoring();
    }
    
    public int refreshMonitoring() {
        
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        CaBug.druckeLog("tSessionAuswahl.isStreamAngebotenUB()" + tAuswahlSession.isStreamAngebotenUB(), logDrucken, 10);
        tMonitoringSession.setDatumZeit(CaDatumZeit.DatumZeitStringFuerAnzeigeNeu());

        tAbstimmungserg.init(true);
        tTeilnehmerverz.init(true);

        tMonitoringSession.setMail1Fragen(eclParamM.getParam().paramPortal.fragenMailVerteiler1);
        tMonitoringSession.setMail2Fragen(eclParamM.getParam().paramPortal.fragenMailVerteiler2);
        tMonitoringSession.setMail3Fragen(eclParamM.getParam().paramPortal.fragenMailVerteiler3);

        tMonitoringSession.setMail1Wortmeldungen(eclParamM.getParam().paramPortal.wortmeldungMailVerteiler1);
        tMonitoringSession.setMail2Wortmeldungen(eclParamM.getParam().paramPortal.wortmeldungMailVerteiler2);
        tMonitoringSession.setMail3Wortmeldungen(eclParamM.getParam().paramPortal.wortmeldungMailVerteiler3);

        tMonitoringSession.setMail1Widersprueche(eclParamM.getParam().paramPortal.widerspruecheMailVerteiler1);
        tMonitoringSession.setMail2Widersprueche(eclParamM.getParam().paramPortal.widerspruecheMailVerteiler2);
        tMonitoringSession.setMail3Widersprueche(eclParamM.getParam().paramPortal.widerspruecheMailVerteiler3);

        tMonitoringSession.setMail1Antraege(eclParamM.getParam().paramPortal.antraegeMailVerteiler1);
        tMonitoringSession.setMail2Antraege(eclParamM.getParam().paramPortal.antraegeMailVerteiler2);
        tMonitoringSession.setMail3Antraege(eclParamM.getParam().paramPortal.antraegeMailVerteiler3);

        tMonitoringSession.setMail1SonstigeMitteilungen(eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler1);
        tMonitoringSession.setMail2SonstigeMitteilungen(eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler2);
        tMonitoringSession.setMail3SonstigeMitteilungen(eclParamM.getParam().paramPortal.sonstMitteilungenMailVerteiler3);

        tMonitoringSession.setMail1Botschaften(eclParamM.getParam().paramPortal.botschaftenMailVerteiler1);
        tMonitoringSession.setMail2Botschaften(eclParamM.getParam().paramPortal.botschaftenMailVerteiler2);
        tMonitoringSession.setMail3Botschaften(eclParamM.getParam().paramPortal.botschaftenMailVerteiler3);
       
        /*++++++++Briefwahl / SRV-Werte füllen+++++++++++++++*/
        int anzahlSRVBriefwahlAngeboten=0;
        int rc=0;
        if (eclParamM.getBriefwahlAngeboten()==1) {
            rc=eclDbM.getDbBundle().dbMeldungen.leseSammelkarteBriefwahlInternet(-1);
            tMonitoringSession.setSammelkarteBriefwahlVorhanden(rc>0);
            anzahlSRVBriefwahlAngeboten++;
        }

        if (eclParamM.getSrvAngeboten()==1) {
            rc=eclDbM.getDbBundle().dbMeldungen.leseSammelkarteSRVInternet(-1);
            tMonitoringSession.setSammelkarteSRVVorhanden(rc>0);
            anzahlSRVBriefwahlAngeboten++;
        }

        if (anzahlSRVBriefwahlAngeboten==1) {
            tMonitoringSession.setAuswahlSRVBriefwahlMoeglich(false);
        }
        else {
            tMonitoringSession.setAuswahlSRVBriefwahlMoeglich(true);
        }

    
        /*+++++++++++++++++Gattungen füllen+++++++++++++++++++*/
        if (eclParamM.getParam().paramBasis.mehrereGattungenAktiv()) {
            tMonitoringSession.setAuswahlGattungMoeglich(true);
            List<EhIdStringFuerAuswahl> auswahlGattungen=new LinkedList<EhIdStringFuerAuswahl>();
            int gef=0;
            for (int i=1;i<=5;i++) {
                if (eclParamM.getParam().paramBasis.getGattungAktiv(i)) {
                    EhIdStringFuerAuswahl ehIdStringFuerAuswahl=new EhIdStringFuerAuswahl(i, eclParamM.getParam().paramBasis.getGattungBezeichnung(i));
                    auswahlGattungen.add(ehIdStringFuerAuswahl);
                    if (gef==0) {
                        gef=1;
                        tMonitoringSession.setAusgewaehlteGattung(Integer.toString(i));
                    }
                }
            }
            
            tMonitoringSession.setAuswahlGattungen(auswahlGattungen);
        }
        else {
            tMonitoringSession.setAuswahlGattungMoeglich(false);
        }
        
        fuelleAbstimmungsanzeige();
        
        return 1;
    }

    private void fuelleAbstimmungsanzeige() {
        int srvOderBriefwahl=0;
        if (tMonitoringSession.isAuswahlSRVBriefwahlMoeglich()) {
            srvOderBriefwahl=Integer.parseInt(tMonitoringSession.getAnzeigenSRVBriefwahl());
        }
        else {
            if (eclParamM.getBriefwahlAngeboten()==1) {
                srvOderBriefwahl=2;
            }
            else {
                srvOderBriefwahl=1;
            }
        }
        
        String ueberschrift="";
        if (srvOderBriefwahl==1) {
            ueberschrift+="Stimmrechtsvertreter ";
        }
        else {
            ueberschrift+="Briefwahl ";
        }

        int gattung[]= {0, 0, 0, 0, 0, 0};
        if (tMonitoringSession.isAuswahlGattungMoeglich()) {
            int ausgewaehlteGattung=Integer.parseInt(tMonitoringSession.getAusgewaehlteGattung());
            gattung[ausgewaehlteGattung]=1;
            ueberschrift+=eclParamM.getParam().paramBasis.getGattungBezeichnung(ausgewaehlteGattung)+" ";
        }
        else {
            gattung[1]=1;
        }
        
        if (tMonitoringSession.isAnzeigenPreview()) {
            ueberschrift+=" Vorschau";
        }
        
        if (srvOderBriefwahl == 2) {
            blMAbstimmung.leseWeisungsliste(gattung, KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung, tMonitoringSession.isAnzeigenPreview());
        } else {
            blMAbstimmung.leseWeisungsliste(gattung, KonstSkIst.srv, KonstWeisungserfassungSicht.portalWeisungserfassung, tMonitoringSession.isAnzeigenPreview());
        }
        
        tMonitoringSession.setAbstimmungUeberschrift(ueberschrift);
    }
    
    
    /*******************************Buttons**************************************************/

    public void doRefresh() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }

        eclDbM.openAll();
        boolean brc=tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(KonstPortalFunktionen.monitoring, false);
        if (brc==false) {
            eclDbM.closeAll();
            return;
        }

        startMonitoring();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;

    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;

    }
    
    public void doAbstimmungenAusblenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }
        tMonitoringSession.setAnzeigenAbstimmungen(false);
        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;
    }

    public void doAbstimmungenSRVEinblenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }
        
        tMonitoringSession.setAnzeigenAbstimmungen(true);
        tMonitoringSession.setAnzeigenSRVBriefwahl("1");
        tMonitoringSession.setAusgewaehlteGattung("1");
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;
    }
 
    public void doAbstimmungenBriefwahlEinblenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }
        tMonitoringSession.setAnzeigenAbstimmungen(true);
        tMonitoringSession.setAnzeigenSRVBriefwahl("2");
        tMonitoringSession.setAusgewaehlteGattung("1");
       
        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;
    }

    public void doAbstimmungenRefresh() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.MONITORING)) {
            return;
        }
        fuelleAbstimmungsanzeige();
        tSessionVerwaltung.setzeEnde(KonstPortalView.MONITORING);
        return;
    }

}
