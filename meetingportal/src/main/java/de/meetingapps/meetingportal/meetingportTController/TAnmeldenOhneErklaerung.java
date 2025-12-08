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
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TAnmeldenOhneErklaerung {

    private int logDrucken = 3;

    private @Inject TFehlerViewSession tFehlerViewSession;

    private @Inject TWillenserklaerung tWillenserklaerung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TSession tSession;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    private @Inject EclParamM eclParamM;
    private @Inject EclDbM eclDbM;
    
    private @Inject TAuswahl tAuswahl;
    

    public void doAnmeldenOhneWKZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.NUR_ANMELDUNG)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;

    }

    public void doAnmeldenOhneWKAusfuehren() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.NUR_ANMELDUNG)) {
            return;
        }
        eclDbM.openAll();
        if (tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()==false) {
            eclDbM.closeAll();
            return;
        }
 
        boolean brc=anmelden(true);
 
        if (brc == false) {
            eclDbM.closeAll();
            return;
        }

        CaBug.druckeLog("ordnungsgemäßes Ende", logDrucken, 10);

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.NUR_ANMELDUNG_QUITTUNG);
            return;
        } else {
            int rcAuswahl=tAuswahl.startAuswahl(true);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(rcAuswahl);
            return;
        }
    }

    @Deprecated
    public boolean pruefeObZulaessig() {
        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*Neuanmeldung - Anmeldephase?*/
            if (tSession.isPortalErstanmeldungIstMoeglich() == false) {
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.FUNKTION_NICHT_MEHR_VERFUEGBAR__ABBRUCH);
                tFehlerViewSession.setNextView(tFunktionen.waehleAuswahl());
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                return false;
            }
        }
        return true;
    }
    
    public boolean anmelden(boolean pMitBestaetigung) {
        for (EclBesitzAREintrag iEclBesitzAREintrag : tWillenserklaerungSession.getBesitzAREintragListe()) {
            boolean brc = tWillenserklaerung.anmelden1Meldung(iEclBesitzAREintrag, pMitBestaetigung);
            if (brc == false) {
               return false;
            }
        }
        return true;
    }
    
    public void doAnmeldenOhneWKQuittungWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.NUR_ANMELDUNG_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();

        int rcAuswahl=tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(rcAuswahl);
        return;

    }

}
