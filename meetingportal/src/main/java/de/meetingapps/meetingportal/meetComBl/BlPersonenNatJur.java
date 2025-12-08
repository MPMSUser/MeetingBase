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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;

public class BlPersonenNatJur {

    public List<String> aufbereitenAdresseOhneAnredeMitLandesschluessel(EclPersonenNatJur pPersonenNatJur) {
        List<String> adresszeilen = new ArrayList<String>();

        /* Namensfelder natürliche Person */
        String hFeld = "";

        String hString = pPersonenNatJur.titel;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.vorname;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.adelstitel;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.name;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hFeld = hFeld.trim();
        adresszeilen.add(hFeld);

        /* Straße, PLZ Ort */
        if (!pPersonenNatJur.strasse.isEmpty()) {
            adresszeilen.add(pPersonenNatJur.strasse);
        }

        hFeld = "";
        hString = pPersonenNatJur.land;
        if (!hString.isEmpty() && hString.compareTo("DE") != 0) {
            hFeld = hFeld + hString + "-";
        }
        hString = pPersonenNatJur.plz;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString + " ";
        }
        hString = pPersonenNatJur.ort;
        if (!hString.isEmpty()) {
            hFeld = hFeld + hString;
        }
        adresszeilen.add(hFeld);

        return adresszeilen;
    }

    public EclPersonenNatJur updateAusAktienregister(EclPersonenNatJur pPersonenNatJur,
            EclAktienregister pAktienregister, String laendercode) {

        if (pAktienregister.istJuristischePerson == 1) {
            pPersonenNatJur.name = CaString.trunc(pAktienregister.nameKomplett, 80).trim();
        } else {
            pPersonenNatJur.titel = pAktienregister.titel;
            pPersonenNatJur.name = pAktienregister.nachname;
            pPersonenNatJur.vorname = pAktienregister.vorname;
        }
        pPersonenNatJur.anrede = pAktienregister.anredeId;
        pPersonenNatJur.kurzName = CaString.trunc(pAktienregister.nameKomplett, 80).trim();
        pPersonenNatJur.strasse = pAktienregister.strasse;
        pPersonenNatJur.land = laendercode;
        pPersonenNatJur.plz = pAktienregister.postleitzahl;
        pPersonenNatJur.ort = pAktienregister.ort;
        pPersonenNatJur.kurzOrt = pAktienregister.ort;

        return pPersonenNatJur;
    }
}
