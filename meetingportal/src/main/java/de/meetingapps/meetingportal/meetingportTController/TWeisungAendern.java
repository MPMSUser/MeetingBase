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
public class TWeisungAendern {

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject TAuswahl tAuswahl;
    private @Inject TWeisung tWeisung;
    private @Inject TWeisungBestaetigung tWeisungBestaetigung;
    private @Inject EclParamM eclParamM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;


    public void doWeiter() {
        try {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_AENDERN)) {
            return;
        }

        boolean brc = tWeisung.verarbeitenAllgemein();
        if (brc == false) {
            return;
        }

        if (eclParamM.getParam().paramPortal.bestaetigenDialog == 1) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG_BESTAETIGUNG);
            return;
        } else {
            eclDbM.openAll();
            
            if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
                eclDbM.closeAll();
                return;
            }

            brc = tWeisungBestaetigung.aendernErteilen();
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
    } catch (Exception e) {
        CaBug.drucke("Exception");
        System.out.println(e.getMessage());
        e.printStackTrace();
        eclDbM.closeAll();
    }
    }

    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_AENDERN)) {
            return;
        }
        /*Öffnen*/
        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(tFunktionen.waehleAuswahl())) {
            eclDbM.closeAll();
            return;
        }

        tAuswahl.startAuswahl(true);

        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

}
