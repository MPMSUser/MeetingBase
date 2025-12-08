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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;

/**Funktionen zur Verwaltung der Tablets*/
public class BvTablets {

    private DbBundle dbBundle = null;

    public BvTablets(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    public boolean pruefeObRuecksetzen(int pGeraeteIdent) {

        EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = leseGeraeteZuordnung(pGeraeteIdent);
        if (lGeraetKlasseSetZuordnung == null) {
            return false;
        }
        if (lGeraetKlasseSetZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart == 1) {
            return true;
        }

        return false;
    }

    public void ruecksetzenErledigt(int pGeraeteIdent) {
        EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = leseGeraeteZuordnung(pGeraeteIdent);
        if (lGeraetKlasseSetZuordnung == null) {
            return;
        }
        lGeraetKlasseSetZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart = 0;
        dbBundle.dbGeraeteKlasseSetZuordnung.update(lGeraetKlasseSetZuordnung);

    }

    private EclGeraetKlasseSetZuordnung leseGeraeteZuordnung(int pGeraeteIdent) {
        int geraeteSet = dbBundle.paramServer.geraeteSetIdent;
        dbBundle.dbGeraeteKlasseSetZuordnung.read(geraeteSet, pGeraeteIdent);
        if (dbBundle.dbGeraeteKlasseSetZuordnung.anzErgebnis() != 1) {
            CaBug.drucke("BvTablets.pruefeObRuecksetzen 001");
            return null;
        }

        EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = dbBundle.dbGeraeteKlasseSetZuordnung
                .ergebnisPosition(0);
        return lGeraetKlasseSetZuordnung;
    }
}
