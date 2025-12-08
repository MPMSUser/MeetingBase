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
package de.meetingapps.meetingportal.meetComBlManaged;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzRaw;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**Funktionen für die Präsenzbuchung für die virtuelle Teilnahme*/
@RequestScoped
public class BlMPraesenz {

    private int logDrucken = 3;

    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclParamM eclParamM;
    @Inject
    private EclLoginDatenM eclLoginDatenM;
    @Inject
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    /**eclDbM ist in aufrufender Funktion zu handeln
     * 1=ok
     * afUpdatePraesentNichtMoeglich => Fehler beim Update
     */
    @Deprecated
    public int bbbbucheZugang() {
        if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung == 1) {
            int teilnehmerIdent = eclLoginDatenM.getEclLoginDaten().ident;
            int meldeIdent = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0)
                    .getMeldungsIdent();
            BlPraesenzRaw blPraesenzRaw = new BlPraesenzRaw(eclDbM.getDbBundle());
            int rc = blPraesenzRaw.setzePraesent(meldeIdent, teilnehmerIdent);
            if (rc == 1) {
                eclZugeordneteMeldungListeM.setNichtPraesenteVorhanden(false);
                eclZugeordneteMeldungListeM.setPraesenteVorhanden(true);
            }
            return rc;
        }

        return 1;
    }

    public int bbbbucheAbgang() {
        if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung == 1) {
            int meldeIdent = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0)
                    .getMeldungsIdent();
            BlPraesenzRaw blPraesenzRaw = new BlPraesenzRaw(eclDbM.getDbBundle());
            int rc = blPraesenzRaw.setzeAbwesend(meldeIdent);
            if (rc == 1) {
                eclZugeordneteMeldungListeM.setNichtPraesenteVorhanden(true);
                eclZugeordneteMeldungListeM.setPraesenteVorhanden(false);
            }
            return rc;
        }
        return 1;
    }

    /**Darf nur aufgerufen werden, wenn Session noch aktiv ist*/
    public int bbbbbucheAbgangBeiEnde(int pUserIdent) {
        CaBug.druckeLog("", logDrucken, 10);
        CaBug.druckeLog("-------------------Abgang buchen---------------", logDrucken, 10);
        if (eclParamM.getParam().paramPortal.nurRawLiveAbstimmung == 1) {
            int teilnehmerIdent = eclLoginDatenM.getEclLoginDaten().ident;
            BlPraesenzRaw blPraesenzRaw = new BlPraesenzRaw(eclDbM.getDbBundle());
            blPraesenzRaw.bucheAbgangAlleFuerTeilnehmer(teilnehmerIdent);
            return 1;
        }

        return 1;
    }
}
