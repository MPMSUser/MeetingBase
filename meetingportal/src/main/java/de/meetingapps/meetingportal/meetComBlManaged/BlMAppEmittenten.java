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

import de.meetingapps.meetingportal.meetComEntities.EclAppZugeordneteEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**Enthält die Funktionen, die zum Verwalten der Emittenten in der App erforderlich sind*/
@RequestScoped
public class BlMAppEmittenten {

    @Inject
    BlMPuffer blMPuffer;

    /**Eingabe für Emittentenabgleich*/
    private EclAppZugeordneteEmittenten[] eclAppZugeordneteEmittenten = null;

    public void aktualisiereEmittenten() {
        for (int i = 0; i < eclAppZugeordneteEmittenten.length; i++) {
            EclEmittenten lEmittenten = blMPuffer
                    .getStandardEmittentFuerEmittentenPortal(eclAppZugeordneteEmittenten[i].mandant);
            if (lEmittenten == null) {
                eclAppZugeordneteEmittenten[i].returnVerarbeitung = 0;
            } else {
                eclAppZugeordneteEmittenten[i].returnVerarbeitung = 1;
                eclAppZugeordneteEmittenten[i].emittentenName = lEmittenten.holeBezeichnungApp();
                eclAppZugeordneteEmittenten[i].hvDatum = lEmittenten.hvDatum;
                eclAppZugeordneteEmittenten[i].hvOrt = lEmittenten.hvOrt;
            }
        }
    }

    public EclAppZugeordneteEmittenten[] getEclAppZugeordneteEmittenten() {
        return eclAppZugeordneteEmittenten;
    }

    public void setEclAppZugeordneteEmittenten(EclAppZugeordneteEmittenten[] eclAppZugeordneteEmittenten) {
        this.eclAppZugeordneteEmittenten = eclAppZugeordneteEmittenten;
    }

}
