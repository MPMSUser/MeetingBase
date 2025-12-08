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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;

public class BlDelayedAufloesen {

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-Willenserklärungen**********************************************/
    /****************************************************************************************************************************************/
    /* Einfach-Version:
    	 > Alle Willenserklärungen und sonstige Sätze durchrennen und Delayed "übertragen".
     */

    private DbBundle lDbBundle = null;

    /******************************Ab hier neue Funktionen************************************/

    /**pDbBundle muß außerhalb dieser Klasse geöffnet / geschlossen werden*/
    public BlDelayedAufloesen(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public void aufloesenDurchfueren() {
        int gef = 1;
        while (gef == 1) {
            gef = 0;
            if (aufloesenWillenserklaerungen() == true) {
                gef = 1;
            }
            if (aufloesenMeldunen() == true) {
                gef = 1;
            }

        }

    }

    private boolean aufloesenWillenserklaerungen() {
        int anz = lDbBundle.dbWillenserklaerung.initAufloesenDelayed();
        if (anz < 1) {
            return false;
        }
        while (lDbBundle.dbWillenserklaerung.readNextDelayed() != 0) {
            EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.rcDelayedWillenserklaerung;
            lWillenserklaerung.delayed = 0;
            lDbBundle.dbWillenserklaerung.update(lWillenserklaerung);
        }

        return true;
    }

    private boolean aufloesenMeldunen() {
        int anz = lDbBundle.dbMeldungen.initAufloesenDelayed();
        if (anz < 1) {
            return false;
        }
        while (lDbBundle.dbMeldungen.readNextDelayed() != 0) {
            EclMeldung lMeldung = lDbBundle.dbMeldungen.rcDelayedMeldung;
            lMeldung.delayedVorhanden = 0;
            lMeldung.zutrittsIdent = lMeldung.zutrittsIdent_Delayed;
            lMeldung.stimmkarte = lMeldung.stimmkarte_Delayed;
            lMeldung.stimmkarteSecond = lMeldung.stimmkarteSecond_Delayed;
            lMeldung.willenserklaerung = lMeldung.willenserklaerung_Delayed;
            lMeldung.willenserklaerungIdent = lMeldung.willenserklaerungIdent_Delayed;
            lMeldung.veraenderungszeit = lMeldung.veraenderungszeit_Delayed;
            lMeldung.erteiltAufWeg = lMeldung.erteiltAufWeg_Delayed;
            lMeldung.meldungEnthaltenInSammelkarte = lMeldung.meldungEnthaltenInSammelkarte_Delayed;
            lMeldung.weisungVorhanden = lMeldung.weisungVorhanden_Delayed;
            lMeldung.vertreterName = lMeldung.vertreterName_Delayed;
            lMeldung.vertreterVorname = lMeldung.vertreterVorname_Delayed;
            lMeldung.vertreterOrt = lMeldung.vertreterOrt_Delayed;
            lMeldung.vertreterIdent = lMeldung.vertreterIdent_Delayed;
            lMeldung.statusPraesenz = lMeldung.statusPraesenz_Delayed;
            lMeldung.statusWarPraesenz = lMeldung.statusWarPraesenz_Delayed;
            lDbBundle.dbMeldungen.update(lMeldung);
        }

        return true;

    }

}
