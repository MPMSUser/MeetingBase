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
package de.meetingapps.meetingportal.meetComBr;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;

public class BrLiefereMitgliedArt {

    private int logDrucken=10;
    
    public int liefereMitgliedstatus(String pVorname, String pNachname, List<EclAktienregisterWeiterePerson> pZugeordneteWeiterPersonen) {
        CaBug.druckeLog("pVorname="+pVorname+" pNachname="+pNachname, logDrucken, logDrucken);

        /*
         * Erbengemeinschaften: Es ist kein Vorname angegeben. Das Wort „Erben“ befindet
         * sich in Form von „Erben des/der/von“ oder „Erbengemeinschaft“ im Nachnamen
         */
        if (pVorname.isEmpty() && (pNachname.contains("Erben des")
                || pNachname.contains("Erben der")
                || pNachname.contains("Erben von")
                || pNachname.contains("Erbengemeinschaft"))) {
            return KonstGruppen.erbengemeinschaft;
        }

        /*
         * Gesamthandsgemeinschaften: Es ist kein Vorname angegeben. Das Wort
         * „Gesamthandsgemeinschaft“ befindet sich im Nachnamen
         */
        if (pVorname.isEmpty()
                && pNachname.contains("Gesamthandsgemeinschaft")) {
            return KonstGruppen.eheleuteGesamthans;
        }

        if (pZugeordneteWeiterPersonen.size() > 0) {
            for (EclAktienregisterWeiterePerson iEclAktienregisterWeiterePerson : pZugeordneteWeiterPersonen) {
                if (iEclAktienregisterWeiterePerson.personenArt.contains("juristische")) {
                    /* Juristische Personen: Filterung über Kennung in GenossenschaftSys */
                    return KonstGruppen.firmen;
                }
                CaBug.druckeLog("iEclAktienregisterWeiterePerson.geburtsdatum="+iEclAktienregisterWeiterePerson.geburtsdatum, logDrucken, 10);
                if (!iEclAktienregisterWeiterePerson.geburtsdatum.isEmpty()) {
                    if (CaDatumZeit.alterInJahren(iEclAktienregisterWeiterePerson.geburtsdatum,
                            CaDatumZeit.DatumStringFuerAnzeige()) < 18) {
                        /* Minderjährige Mitglieder: Filterung über Geburtsdatum */
                        return KonstGruppen.minderjaehrigesEinzelmitglied;
                    }
                }
            }
        }
        return KonstGruppen.einzelmitglied;
    }
    
    

}
