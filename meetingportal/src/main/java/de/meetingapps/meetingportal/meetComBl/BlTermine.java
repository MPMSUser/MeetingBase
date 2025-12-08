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

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclTermine;

public class BlTermine implements Serializable {
    private static final long serialVersionUID = -5278881491777234104L;

    private EclTermine[] terminListe = null;

    public BlTermine(EclTermine[] pTerminListe) {
        terminListe = pTerminListe;
    }

    public String holePortalText(int pSprache, int pNummer) {
        String hText = "";
        for (int i = 0; i < terminListe.length; i++) {
            if (terminListe[i].identTermin == pNummer) {
                if (pSprache == 1) {
                    return terminListe[i].textDatumZeitFuerPortalDE;
                } else {
                    return terminListe[i].textDatumZeitFuerPortalEN;
                }
            }
        }

        return hText;
    }
}
