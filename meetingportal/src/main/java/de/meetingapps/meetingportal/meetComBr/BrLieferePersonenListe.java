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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenResult;

public class BrLieferePersonenListe {

    public List<EclAktienregisterWeiterePerson> rcZugeordneteWeiterPersonen = null;

    /**Es ist eine oder mehrere Person in der Liste rcZugeordneteWeiterPersonen.
     * Es wird nur eines der beiden als true geliefert!*/
    public boolean rcZugeordnetePersonenVorhanden = false;
    public boolean rcZugeordnetePersonVorhanden = false;

    public boolean rcSteuerIdDarfGeaendertWerden = false;
    /**Bei irgendeinem Eintrag fehlt die SteuerId, oder es ist kein Eintrag vorhanden*/
    public boolean rcSteuerIdFehlt = false;

    public boolean rcGeburtsdatumDarfGeaendertWerden = false;
    /**Bei irgendeinem Eintrag fehlt das Geburtsdatum, oder es ist kein Eintrag vorhanden*/
    public boolean rcGeburtsdatumFehlt = false;

    public List<EclAktienregisterWeiterePerson> rcVollmachtenPostempfaenger = null;
    /**Es ist eine oder mehrere Person in der Liste rcVollmachtenPostempfaenger*/
    public boolean rcVollmachtenPostempfaengerVorhanden = false;

    public List<EclAktienregisterWeiterePerson> rcPostempfaenger = null;
    /**Es ist eine oder mehrere Person in der Liste rcPostempfaenger*/
    public boolean rcPostempfaengerVorhanden = false;

    public int liefereAusGeDixListe(EgxGetPersonendatenRC egxGetPersonendatenRC) {
        rcZugeordneteWeiterPersonen = new LinkedList<EclAktienregisterWeiterePerson>();
        rcZugeordnetePersonenVorhanden = false;
        rcSteuerIdDarfGeaendertWerden = false;
        rcGeburtsdatumDarfGeaendertWerden = false;

        rcVollmachtenPostempfaenger = new LinkedList<EclAktienregisterWeiterePerson>();
        rcPostempfaenger = new LinkedList<EclAktienregisterWeiterePerson>();

        if (egxGetPersonendatenRC.result.size() > 0) {

            for (EgxGetPersonendatenResult result : egxGetPersonendatenRC.result) {

                if (result.art.equals("natürliche Person (Mitglied / Teil des Mitglieds)")
                        || result.art.equals("juristische Person (Mitglied / Teil des Mitglieds)")) {
                    EclAktienregisterWeiterePerson lAktienregisterWeiterePerson = null;
                    lAktienregisterWeiterePerson = new EclAktienregisterWeiterePerson();
                    lAktienregisterWeiterePerson.ident = result.lfnr;
                    lAktienregisterWeiterePerson.nachname = result.nachname;
                    lAktienregisterWeiterePerson.vorname = result.vorname;
                    lAktienregisterWeiterePerson.geburtsdatum = CaDatumZeit.datumJJJJ_MM_TTzuNormal(result.geburt);
                    lAktienregisterWeiterePerson.steuerId = result.steuer;
                    lAktienregisterWeiterePerson.personenArt = result.art;
                    rcZugeordneteWeiterPersonen.add(lAktienregisterWeiterePerson);
                    rcZugeordnetePersonenVorhanden = true;
                    if (lAktienregisterWeiterePerson.steuerId.isEmpty()) {
                        rcSteuerIdDarfGeaendertWerden = true;
                        rcSteuerIdFehlt = true;
                    }
                    if (lAktienregisterWeiterePerson.geburtsdatum.isEmpty()) {
                        rcGeburtsdatumDarfGeaendertWerden = true;
                        rcGeburtsdatumFehlt = true;
                    }

                } else {
                    if (!result.status.equals("Abweichender Kontoinhaber")) {
                        EclAktienregisterWeiterePerson lAktienregisterWeiterePerson = null;
                        lAktienregisterWeiterePerson = new EclAktienregisterWeiterePerson();
                        lAktienregisterWeiterePerson.ident = result.lfnr;
                        lAktienregisterWeiterePerson.nachname = result.nachname;
                        lAktienregisterWeiterePerson.vorname = result.vorname;
                        lAktienregisterWeiterePerson.strasse = result.strasse;
                        lAktienregisterWeiterePerson.plz = String.valueOf(result.plz);
                        lAktienregisterWeiterePerson.ort = result.ort;
                        lAktienregisterWeiterePerson.land = result.Land;
                        rcVollmachtenPostempfaenger.add(lAktienregisterWeiterePerson);
                        rcVollmachtenPostempfaengerVorhanden = true;
                        if (result.postempfaenger) {
                            rcPostempfaenger.add(lAktienregisterWeiterePerson);
                            rcPostempfaengerVorhanden = true;
                        }
                    }
                }

            }

        }

        if (rcZugeordnetePersonenVorhanden == true) {
            if (rcZugeordneteWeiterPersonen.size() == 1) {
                rcZugeordnetePersonenVorhanden = false;
                rcZugeordnetePersonVorhanden = true;
            }
        }

        if (rcZugeordnetePersonenVorhanden == false && rcZugeordnetePersonVorhanden == false) {
            rcSteuerIdFehlt = true;
            rcGeburtsdatumFehlt = true;
        }
        return 1;

    }
}
