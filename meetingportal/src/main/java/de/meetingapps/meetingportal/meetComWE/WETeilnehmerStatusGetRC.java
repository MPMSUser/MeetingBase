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
package de.meetingapps.meetingportal.meetComWE;

import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;

public class WETeilnehmerStatusGetRC extends WERootRC {

    /**Liste aller Kennungen, die zusammengefaßt wurden. [0]=die Kennung, mit der
     * gerade eingeloggt ist
     */
    public List<EclBesitzJeKennung> besitzJeKennungListe = null;

    
    
    /*Anmeldestatus / bisher vorliegende Willenserklärungen zum Aktienbesitz bzw. zur Person - 
     * Beschreibung siehe EclZugeordneteMeldungListeM
     */
    @Deprecated
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM = null;

    /*Unklar wofür das verwendet wird. Müßte eigentlich auch aus EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur aus 
     * loginCheck verwendbar sein!*/
    @Deprecated
    private int anmeldeIdentPersonenNatJur = 0; /*früher: ameldeIdentPersonenNatJur*/

    /*Öffentliche ID der Person, für den der Status angefordert wurde*/
    @Deprecated
    private String oeffentlicheID = "";

    /*bereitsAngemeldet: Ersetzt ausgewaehlteHauptAktion gegenüber WETeilnehmerStatusGetRC*/

    /* ="1" => Aktionär ist noch nicht angemeldet, d.h. Erstanmeldung muß erst erfolgen bevor weitere
     * 			Willenserklärungen abgegeben werden können
     * ="2" => zusätzliche Willenserklärungen sind möglich (bei Aktionär: ist bereits gangemeledet*/
    @Deprecated
    private String ausgewaehlteHauptAktion = "";

    /*********************Standard Getter / Setter*************************************************/
    @Deprecated
   public EclZugeordneteMeldungListeM getEclZugeordneteMeldungListeM() {
        return eclZugeordneteMeldungListeM;
    }

    @Deprecated
    public void setEclZugeordneteMeldungListeM(EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM) {
        this.eclZugeordneteMeldungListeM = eclZugeordneteMeldungListeM;
    }

    @Deprecated
    public int getAnmeldeIdentPersonenNatJur() {
        return anmeldeIdentPersonenNatJur;
    }

    @Deprecated
    public void setAnmeldeIdentPersonenNatJur(int anmeldeIdentPersonenNatJur) {
        this.anmeldeIdentPersonenNatJur = anmeldeIdentPersonenNatJur;
    }

    @Deprecated
    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    @Deprecated
   public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    @Deprecated
   public String getOeffentlicheID() {
        return oeffentlicheID;
    }

    @Deprecated
    public void setOeffentlicheID(String oeffentlicheID) {
        this.oeffentlicheID = oeffentlicheID;
    }

}
