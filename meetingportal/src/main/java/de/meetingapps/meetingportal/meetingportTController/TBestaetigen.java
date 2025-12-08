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
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TBestaetigen {

    private @Inject TBestaetigenSession tBestaetigenSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    @Inject
    private TPruefeStartNachOpen tPruefeStartNachOpen;

    /*Bestätigung der E-Mail-Adresse*/
    public void doBestaetigen() {

        tBestaetigenSession.clearFehler();
        /*Keine Überprüfung auf aufrufende Maske erforderlich. Kann beliebig oft aufgerufen werden*/
        eclDbM.openAll();

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerLinkAufrufBestaetigen);
            return;
        }

        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }


        EclLoginDaten lLoginDaten = new EclLoginDaten();
        int erg;
        erg = eclDbM.getDbBundle().dbLoginDaten.read_emailBestaetigenLink(tBestaetigenSession.getBestaetigungsCode());
        if (erg == 1) {
            lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
            lLoginDaten.emailBestaetigt = 1;
            lLoginDaten.emailBestaetigenLink="";
            eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
            tBestaetigenSession.trageFehlerEin(CaFehler.afEmailBestaetigt);
        } else {
            eclDbM.closeAllAbbruch();
            tBestaetigenSession.trageFehlerEin(CaFehler.afEmailBestaetigungsCodeUnbekannt);
            tSessionVerwaltung.setzeEnde();
            return;
        }

        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.BESTAETIGT);
        return;
    }

    /*Bestätigung der 2. E-Mail-Adresse*/
    public void doBestaetigen2() {
        /*Keine Überprüfung auf aufrufende Maske erforderlich. Kann beliebig oft aufgerufen werden*/

        tBestaetigenSession.clearFehler();
        eclDbM.openAll();

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("Mandant ist 0");
            tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerLinkAufrufBestaetigen);
            return;
        }

        if (tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()==false) {
            eclDbM.closeAll();
            return;
        }

        EclLoginDaten lLoginDaten = new EclLoginDaten();
        int erg;
        erg = eclDbM.getDbBundle().dbLoginDaten.read_email2BestaetigenLink(tBestaetigenSession.getBestaetigungsCode2());
        if (erg == 1) {
            lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
            lLoginDaten.setzeEMail2AdresseBestaetigt(true);
            lLoginDaten.email2BestaetigenLink="";
            eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
            tBestaetigenSession.trageFehlerEin(CaFehler.afEmailBestaetigt);
        } else {
            tBestaetigenSession.trageFehlerEin(CaFehler.afEmail2BestaetigungsCodeUnbekannt);
        }

        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.BESTAETIGT2);
        return;
    }

}
