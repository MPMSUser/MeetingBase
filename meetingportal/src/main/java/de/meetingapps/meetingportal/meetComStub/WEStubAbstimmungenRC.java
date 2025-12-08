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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubAbstimmungenRC extends WERootRC {

    public EclAbstimmung[] angezeigteAbstimmungen = null;
    public boolean[] abstimmungWurdeVeraendert = null;
    public EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock = null;
    public EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte = null;
    public EclAbstimmungsblock[] abstimmungsblockListe = null;
    public EclStimmkarteInhalt[] stimmkarteInhalt = null;

    /***********Standard Getter und Setter********************/

    public boolean[] getAbstimmungWurdeVeraendert() {
        return abstimmungWurdeVeraendert;
    }

    public void setAbstimmungWurdeVeraendert(boolean[] abstimmungWurdeVeraendert) {
        this.abstimmungWurdeVeraendert = abstimmungWurdeVeraendert;
    }

    public EclAbstimmung[] getAngezeigteAbstimmungen() {
        return angezeigteAbstimmungen;
    }

    public void setAngezeigteAbstimmungen(EclAbstimmung[] angezeigteAbstimmungen) {
        this.angezeigteAbstimmungen = angezeigteAbstimmungen;
    }

    public EclAbstimmungZuAbstimmungsblock[] getAngezeigteAbstimmungZuAbstimmungsblock() {
        return angezeigteAbstimmungZuAbstimmungsblock;
    }

    public void setAngezeigteAbstimmungZuAbstimmungsblock(
            EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock) {
        this.angezeigteAbstimmungZuAbstimmungsblock = angezeigteAbstimmungZuAbstimmungsblock;
    }

    public EclAbstimmungenZuStimmkarte[] getAngezeigteAbstimmungZuStimmkarte() {
        return angezeigteAbstimmungZuStimmkarte;
    }

    public void setAngezeigteAbstimmungZuStimmkarte(EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte) {
        this.angezeigteAbstimmungZuStimmkarte = angezeigteAbstimmungZuStimmkarte;
    }

    public EclAbstimmungsblock[] getAbstimmungsblockListe() {
        return abstimmungsblockListe;
    }

    public void setAbstimmungsblockListe(EclAbstimmungsblock[] abstimmungsblockListe) {
        this.abstimmungsblockListe = abstimmungsblockListe;
    }

    public EclStimmkarteInhalt[] getStimmkarteInhalt() {
        return stimmkarteInhalt;
    }

    public void setStimmkarteInhalt(EclStimmkarteInhalt[] stimmkarteInhalt) {
        this.stimmkarteInhalt = stimmkarteInhalt;
    }

}
