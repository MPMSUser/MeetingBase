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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

/**Ein Bestand, der dem Insti zugeordnet ist. Wird
 * in EclInstiZugeordneterBestandListeM dann zusammengefaßt zu allen Beständen
 * des eingeloggten Instis.
 */
public class EclInstiZugeordneterBestandM implements Serializable {
    private static final long serialVersionUID = 610830649111901779L;

    private int aktienregisterIdent = 0;
    private String aktionaersnummer = "";
    private String aktionaersname = "";
    private Long aktien = 0L;

    private EclZugeordneteMeldungListeM zugeordneteMeldungListeM = null;

    /****************Standard getter und setter*******************************/

    public String getAktionaersnummer() {
        return aktionaersnummer;
    }

    public void setAktionaersnummer(String aktionaersnummer) {
        this.aktionaersnummer = aktionaersnummer;
    }

    public String getAktionaersname() {
        return aktionaersname;
    }

    public void setAktionaersname(String aktionaersname) {
        this.aktionaersname = aktionaersname;
    }

    public Long getAktien() {
        return aktien;
    }

    public void setAktien(Long aktien) {
        this.aktien = aktien;
    }

    public EclZugeordneteMeldungListeM getZugeordneteMeldungListeM() {
        return zugeordneteMeldungListeM;
    }

    public void setZugeordneteMeldungListeM(EclZugeordneteMeldungListeM zugeordneteMeldungListeM) {
        this.zugeordneteMeldungListeM = zugeordneteMeldungListeM;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

}
