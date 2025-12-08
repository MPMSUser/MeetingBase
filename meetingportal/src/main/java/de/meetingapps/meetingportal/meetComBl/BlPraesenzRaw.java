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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;

/**Hoch-Effiziente "Raw"-Präsenzfunktionen, nur für virtuelle HV von Vereine etc. (wenn nurRawLiveAbstimmung aktiv).
 * 
 * Keine Sammelkarten, keine Vollmachten, keine EK-Zuordnung, rein virtuell, keine Summierung, kein Pending; alle anderen Präsenzfunktionen (insbesondere
 * Front-Office) können nicht verwendet werden.
 */
public class BlPraesenzRaw {

    private DbBundle dbBundle = null;

    public BlPraesenzRaw(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Return:
     * afUpdatePraesentNichtMoeglich => Fehler beim Update
     */
    public int setzePraesent(int pMeldeIdent, int pKennungIdent) {
        int rc = dbBundle.dbMeldungen.updatePraesenz(pMeldeIdent, pKennungIdent, 1);
        if (rc != 1) {
            return CaFehler.afUpdatePraesentNichtMoeglich;
        }
        return 1;
    }

    /**Return:
     * afUpdatePraesentNichtMoeglich => Fehler beim Update
     */
    public int setzeAbwesend(int pMeldeIdent) {
        int rc = dbBundle.dbMeldungen.updatePraesenz(pMeldeIdent, -1, 2);
        if (rc != 1) {
            return CaFehler.afUpdatePraesentNichtMoeglich;
        }
        return 1;
    }

    public int bucheAbgangAlleFuerTeilnehmer(int teilnehmerIdent) {
        dbBundle.dbMeldungen.updatePraesenzAbgangFuerTeilnehmerIdent(teilnehmerIdent);
        return 1;
    }

}
