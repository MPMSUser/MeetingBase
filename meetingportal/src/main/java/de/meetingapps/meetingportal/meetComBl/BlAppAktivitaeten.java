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
import de.meetingapps.meetingportal.meetComEntities.EclAppAktivitaeten;
import de.meetingapps.meetingportal.meetComKonst.KonstAppAktivitaet;

public class BlAppAktivitaeten {

    private DbBundle lDbBundle = null;

    public String rcNeuerKey = "";

    /**Benötigt openundweitere*/
    public BlAppAktivitaeten(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public void erzeuge_elternvollmachtAnzeigen() {
        EclAppAktivitaeten lAppAktivitaeten = new EclAppAktivitaeten();
        lAppAktivitaeten.funktion = KonstAppAktivitaet.elternvollmachtAnzeigen;
        lDbBundle.dbAppAktivitaeten.insert(lAppAktivitaeten);
        rcNeuerKey = lAppAktivitaeten.eindeutigerKey;
    }

}
