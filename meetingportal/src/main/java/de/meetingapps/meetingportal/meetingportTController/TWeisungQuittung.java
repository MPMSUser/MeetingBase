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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstHinweisWeitere;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TWeisungQuittung {

    private @Inject EclDbM eclDbM;
   
    private @Inject EclParamM eclParamM;
    private @Inject TSession tSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TAuswahl tAuswahl;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TQuittungen tQuittungen;

    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
            return;
        }
        
        String hJnEingabe=tWillenserklaerungSession.getJnBeiQuittung();
        
        if (eclParamM.isJnBeiQuittung()) {
            if (hJnEingabe==null || hJnEingabe.isEmpty() || hJnEingabe.equals("")){
                tSession.trageFehlerEin(CaFehler.afJnWeisungBestaetigungFehlt); 
                tSessionVerwaltung.setzeEnde();
                return;
                
            }
        }
        
        eclDbM.openAll();
        if (eclParamM.isJnBeiQuittung()) {
            boolean lSetzen=false;
            if (hJnEingabe.equals("1")) {
                lSetzen=true;
            }
            int loginIdent=eclLoginDatenM.getEclLoginDaten().ident;
            int rcHinweisWeitereBestaetigt=eclDbM.getDbBundle().dbLoginDaten.updateHinweisWeitere(loginIdent, KonstHinweisWeitere.WEISUNG_QUITTUNG_JN, lSetzen);
            eclLoginDatenM.getEclLoginDaten().hinweisWeitereBestaetigt=rcHinweisWeitereBestaetigt;
        }
        
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

    

    public void doEmail() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
            return;
        }
        
        String hMail=tWillenserklaerungSession.getEmailFuerBestaetigung();
        if (hMail==null || hMail.isEmpty() || !CaString.isMailadresse(hMail)){
           tSession.trageFehlerEin(CaFehler.afEMailBestaetigungFalsch);
           tSessionVerwaltung.setzeEnde();
           return;
        }

        eclDbM.openAll();
        
        tQuittungen.weisungsBestaetigungErzeugePDF();

        eclDbM.closeAll();

        int rc=tQuittungen.weisungsBestaetigungAufAnforderung(hMail);
        
        if (rc==1) {
            tSession.trageFehlerEinMitArt(CaFehler.afBestaetigungsEMailVerschickt, 2);
        }
        else {
            /*TODO Mailfehlerbehandlung*/
        }

        tSessionVerwaltung.setzeEnde();
    }
    
    public void doPdf() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();
        tQuittungen.weisungsBestaetigungErzeugePDF();
        eclDbM.closeAll();

        String dateiName=eclParamM.getClGlobalVar().lwPfadAllgemein+
                "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                + "ausdrucke\\"+eclParamM.getMandantPfad()+
                "\\bes"+tWillenserklaerungSession.getNummerBestaetigung()+".pdf";
        
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(dateiName);
        
        tSessionVerwaltung.setzeEnde();
    }
    
    
}
