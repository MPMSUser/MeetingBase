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

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;

public class WETeilnehmerStatusIRC { /*entspricht WETeilnehmerStatusGetRC*/

    /*Return-Code zum Abprüfen, ob Aktion erfolgreich*/
    private int rc;

    /*Anmeldestatus / bisher vorliegende Willenserklärungen zum Aktienbesitz bzw. zur Person - 
     * Beschreibung siehe EclZugeordneteMeldungListeM
     */
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM = null;

    /*Unklar wofür das verwendet wird. Müßte eigentlich auch aus EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur aus 
     * loginCheck verwendbar sein!*/
    @Deprecated
    private int anmeldeIdentPersonenNatJur = 0; /*früher: ameldeIdentPersonenNatJur*/

    /*Öffentliche ID der Person, für den der Status angefordert wurde*/
    private String oeffentlicheID = "";

    /*bereitsAngemeldet: Ersetzt ausgewaehlteHauptAktion gegenüber WETeilnehmerStatusGetRC*/

    /* =0 => Aktionär ist noch nicht angemeldet, d.h. Erstanmeldung muß erst erfolgen bevor weitere
     * 			Willenserklärungen abgegeben werden können
     * =1 => zusätzliche Willenserklärungen sind möglich (bei Aktionär: ist bereits gangemeledet*/
    private int bereitsAngemeldet;

    /*********************Standard Getter / Setter*************************************************/
    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public EclZugeordneteMeldungListeM getEclZugeordneteMeldungListeM() {
        return eclZugeordneteMeldungListeM;
    }

    public void setEclZugeordneteMeldungListeM(EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM) {
        this.eclZugeordneteMeldungListeM = eclZugeordneteMeldungListeM;
    }

    @Deprecated
    public int getAnmeldeIdentPersonenNatJur() {
        return anmeldeIdentPersonenNatJur;
    }

    @Deprecated
    public void setAnmeldeIdentPersonenNatJur(int ameldeIdentPersonenNatJur) {
        this.anmeldeIdentPersonenNatJur = ameldeIdentPersonenNatJur;
    }

    public String getOeffentlicheID() {
        return oeffentlicheID;
    }

    public void setOeffentlicheID(String oeffentlicheID) {
        this.oeffentlicheID = oeffentlicheID;
    }

    public int getBereitsAngemeldet() {
        return bereitsAngemeldet;
    }

    public void setBereitsAngemeldet(int bereitsAngemeldet) {
        this.bereitsAngemeldet = bereitsAngemeldet;
    }

}
