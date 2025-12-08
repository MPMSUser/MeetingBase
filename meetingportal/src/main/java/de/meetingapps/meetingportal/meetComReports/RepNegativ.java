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

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepNegativ {

    /**Ab dieser Aktienzahl (d.h. größer) werden die Aktionäre in die 
     * Liste aufgenommen.
     * Gilt als Selektionskriterium vorrangig - d.h. wenn druckenAbAktienzahl>0,
     * dann wird anzahlGroessteDrucken ignoriert
     */
    public long druckenAbAktienzahl = 0;

    /**Wenn >0: dann wird nur diese Anzahl der gefundenen Aktionäre gedruckt
     * (nur zulässig wenn sortierung=3)*/
    public int anzahlGroessteDrucken = 0;

    /**1=Aktionärsnummer
     * 2=Name
     * 3=Aktien Anzahl absteigend
     */
    public int sortierung = 0;

    private RpVariablen rpVariablen = null;

    public void druckeNegativliste(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.negativliste(pLfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Negativliste - Nicht angemeldete Bestände");

        int rc = pDbBundle.dbJoined.read_negativliste(druckenAbAktienzahl, sortierung);
        if (rc > anzahlGroessteDrucken && anzahlGroessteDrucken > 0 && druckenAbAktienzahl == 0) {
            rc = anzahlGroessteDrucken;
        }
        if (druckenAbAktienzahl > 0) {
            rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail",
                    "Aktionäre ab " + CaString.toStringDE(druckenAbAktienzahl) + " Aktien");
        } else {
            if (anzahlGroessteDrucken > 0) {
                rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail",
                        "Die größten " + CaString.toStringDE(rc) + " Aktionäre");
            } else {
                rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail", " ");
            }
        }

        rpDrucken.startListe();
        druckschleife(pDbBundle, rpDrucken, rc);
        rpDrucken.endeListe();
    }

    public void druckeWenigerAktienAngemeldet(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.negativliste(pLfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Kontroll-Liste");
        rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail",
                "Aktionäre - geringerer Bestand angemeldet als vorhanden");

        int rc = pDbBundle.dbJoined.read_vergleichAngemeldeteAktienZuAktienregister(1);

        rpDrucken.startListe();
        druckschleife(pDbBundle, rpDrucken, rc);
        rpDrucken.endeListe();
    }

    public void druckeMehrAktienAngemeldet(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.negativliste(pLfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Kontroll-Liste");
        rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail",
                "Aktionäre - größerer Bestand angemeldet als vorhanden");

        int rc = pDbBundle.dbJoined.read_vergleichAngemeldeteAktienZuAktienregister(2);

        rpDrucken.startListe();
        druckschleife(pDbBundle, rpDrucken, rc);
        rpDrucken.endeListe();
    }

    public void druckeNullBestand(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.negativliste(pLfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Kontroll-Liste");
        rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail", "Aktionäre - angemeldet mit Null-Bestand");

        int rc = pDbBundle.dbJoined.read_nullAnmeldungen();

        rpDrucken.startListe();
        druckschleife(pDbBundle, rpDrucken, rc);
        rpDrucken.endeListe();
    }

    public void druckeUnbestaetigtEingetragen(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.negativliste(pLfdNummer, rpDrucken);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift", "Kontroll-Liste");
        rpVariablen.fuelleVariable(rpDrucken, "UeberschriftDetail", "Aktionäre - unbestätigt im Register");

        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.besitzart = "#";
        int rc = pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);

        rpDrucken.startListe();
        druckschleife(pDbBundle, rpDrucken, rc);
        rpDrucken.endeListe();
    }

    private void druckschleife(DbBundle pDbBundle, RpDrucken rpDrucken, int rc) {
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                String hString = pDbBundle.dbJoined.ergebnisKeyPosition(i);
                if (!hString.isEmpty()) {
                    EclAktienregister lAktienregisterEintrag = new EclAktienregister();
                    lAktienregisterEintrag.aktienregisterIdent = Integer.parseInt(hString);
                    pDbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
                    lAktienregisterEintrag = pDbBundle.dbAktienregister.ergebnisPosition(0);

                    rpVariablen.fuelleFeld(rpDrucken, "Nummern.Aktionaersnummer",
                            lAktienregisterEintrag.aktionaersnummer);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lAktienregisterEintrag.nameKomplett);
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", "");
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lAktienregisterEintrag.ort);

                    rpVariablen.fuelleFeld(rpDrucken, "Besitz.Stimmen", Long.toString(lAktienregisterEintrag.stimmen));
                    rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenDE",
                            CaString.toStringDE(lAktienregisterEintrag.stimmen));

                    rpDrucken.druckenListe();
                }
            }
        }

    }

}
