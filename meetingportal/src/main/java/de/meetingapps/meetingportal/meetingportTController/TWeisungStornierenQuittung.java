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

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TWeisungStornierenQuittung {

    private @Inject EclDbM eclDbM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TAuswahl tAuswahl;
    private @Inject TFunktionen tFunktionen;

    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_STORNIEREN_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

}
