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
package de.meetingapps.meetingportal.meetComReports;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenEinzelAusschluss;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepStimmausschluss {

    private RpVariablen rpVariablen = null;
    private DbBundle dbBundle = null;

    public void druckeStimmausschlussliste(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        dbBundle = pDbBundle;

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.stimmausschlussliste(pLfdNummer, rpDrucken);

        rpDrucken.startListe();

        ausschlussKennzeichen(rpDrucken, "V");
        ausschlussKennzeichen(rpDrucken, "A");
        ausschlussKennzeichen(rpDrucken, "S");
        ausschlussKennzeichen(rpDrucken, "1");
        ausschlussKennzeichen(rpDrucken, "2");
        ausschlussKennzeichen(rpDrucken, "3");
        ausschlussKennzeichen(rpDrucken, "4");
        ausschlussKennzeichen(rpDrucken, "5");
        ausschlussKennzeichen(rpDrucken, "6");
        ausschlussKennzeichen(rpDrucken, "7");
        ausschlussKennzeichen(rpDrucken, "8");
        ausschlussKennzeichen(rpDrucken, "9");

        ausschlussEinzel(rpDrucken);

        rpDrucken.endeListe();
    }

    private void ausschlussKennzeichen(RpDrucken rpDrucken, String pAusschlussKennzeichen) {
        int rc = dbBundle.dbMeldungen.leseStimmausschlussKZ(pAusschlussKennzeichen);

        for (int i = 0; i < rc; i++) {
            EclMeldung lMeldung = dbBundle.dbMeldungen.meldungenArray[i];
            // add here your preferred DokumentGenerator
            rpDrucken.druckenListe();
        }
    }

    private void ausschlussEinzel(RpDrucken rpDrucken) {
        int rc = dbBundle.dbAbstimmungenEinzelAusschluss.readAlle();

        for (int i = 0; i < rc; i++) {
            EclAbstimmungenEinzelAusschluss lAbstimmungenEinzelAusschluss = dbBundle.dbAbstimmungenEinzelAusschluss
                    .ergebnisPosition(i);

            int anz=dbBundle.dbMeldungen.leseZuIdent(lAbstimmungenEinzelAusschluss.identMeldung);
            dbBundle.dbAbstimmungen.leseIdent(lAbstimmungenEinzelAusschluss.abstimmungsIdent);
            EclAbstimmung lAbstimmung = dbBundle.dbAbstimmungen.abstimmungengGefunden(0);
            
            if (anz!=0) {
                EclMeldung lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
                // add here your preferred DokumentGenerator
                rpDrucken.druckenListe();
            }
            else {
                // add here your preferred DokumentGenerator
                rpDrucken.druckenListe();
            }
        }
    }
}
