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
import de.meetingapps.meetingportal.meetComEntities.EclPersonen;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;

public class BlPersonenZaehlung {

    private DbBundle dbBundle = null;

    public BlPersonenZaehlung(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    public int zaehleTeilnehmerInsgesamt() {
        dbBundle.openWeitere();
        dbBundle.dbWillenserklaerung.leseZugaengeWechsel();
        int anzWillenserklaerungen = dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();

        dbBundle.dbPersonen.deleteAll();
        for (int i = 0; i < anzWillenserklaerungen; i++) {
            EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i];
            int personNatJurIdent = lWillenserklaerung.bevollmaechtigterDritterIdent;
            int meldeIdent = lWillenserklaerung.meldungsIdent;
            if (meldeIdent != 0) { /*TODO Personenzählung Gäste noch berücksichtigen*/
                if (personNatJurIdent == 0) {
                    System.out.println("meldeIdent=" + meldeIdent + " willenserklaerung="
                            + lWillenserklaerung.willenserklaerungIdent);
                    dbBundle.dbMeldungen.leseZuIdent(meldeIdent);
                    personNatJurIdent = dbBundle.dbMeldungen.meldungenArray[0].personenNatJurIdent;
                }
                //			System.out.println("personNatJurIdent="+personNatJurIdent);
                dbBundle.dbPersonenNatJur.read(personNatJurIdent);
                EclPersonenNatJur lPersonNatJur = dbBundle.dbPersonenNatJur.personenNatJurArray[0];

                EclPersonen lPerson = new EclPersonen();
                lPerson.name = lPersonNatJur.name;
                lPerson.vorname = lPersonNatJur.vorname;
                lPerson.ort = lPersonNatJur.ort;
                dbBundle.dbPersonen.insert(lPerson);
            }
        }

        return dbBundle.dbPersonen.ermittleAnzahlPersonen();
    }
}
