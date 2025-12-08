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

import java.util.Arrays;
import java.util.Comparator;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungCompName;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungCompZutrittsIdent;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepGastkartenliste {

    public int logDrucken = 3;

    public String druckergebnisDatei = "";

    /**ExportDatei , die (je nach Aufrufparameter) bei druckeGastkartenliste erzeugt wurde*/
    public String exportDatei = "";

    /**Wenn auf false gesetzt, dann wird das Passwort-Feld in der Gästeliste immer auf "" gesetzt*/
    public boolean pDruckeMitPasswort = true;

    private CaDateiWrite dateiExport = null;

    /**Verwendet z.B. im UPortal
     *
     * Erzeugt immer auch Export-Datei
     * 
     * sortierung:
     * 1=Gastkartennummer
     * 2=name
     * 3=MeldeIdent
     */
    public void druckeGastkartenUebersichtListe(DbBundle pDbBundle, RpDrucken rpDrucken, int pSortierung, String pLfdNummer) {

        dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(pDbBundle, "expGastkartenListe");
        exportDatei = dateiExport.dateiname;

        CaBug.druckeLog("exportDatei=" + exportDatei, logDrucken, 10);
        dateiExport.ausgabe("Gastkarten-Nr");
        dateiExport.ausgabe("InterneIdent");
        dateiExport.ausgabe("Name");
        dateiExport.ausgabe("Vorname");
        dateiExport.ausgabe("ZuHdCo");
        dateiExport.ausgabe("Zusatz1");
        dateiExport.ausgabe("Strasse");
        dateiExport.ausgabe("Ort");
        dateiExport.ausgabe("InstiIdent");
        dateiExport.ausgabe("Kennung");
        dateiExport.ausgabe("Passwort");
        dateiExport.newline();

        rpDrucken.initListe(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.gastkartenUebersichtListe(pLfdNummer, rpDrucken);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", CaDatumZeit.DatumStringFuerAnzeige());
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", CaDatumZeit.StundenMinutenStringFuerAnzeige());

        rpDrucken.startListe();

        int rc = pDbBundle.dbMeldungen.readinit_gastListe(pSortierung, 0);
        if (rc > 0) {
            while (pDbBundle.dbMeldungen.readnext_gastliste()) {
                EclMeldung eclMeldung = pDbBundle.dbMeldungen.meldungenArray[0];
                int personNatJur = eclMeldung.personenNatJurIdent;
                pDbBundle.dbLoginDaten.read_personNatJurIdent(personNatJur);
                EclLoginDaten eclLoginDaten = pDbBundle.dbLoginDaten.ergebnisPosition(0);
                fuelleGastlisteUebersicht(eclMeldung, eclLoginDaten, rpDrucken, rpVariablen);
                rpDrucken.druckenListe();
            }
        }
        rpDrucken.endeListe();
        dateiExport.schliessen();

    }

    private void fuelleGastlisteUebersicht(EclMeldung lEclMeldung, EclLoginDaten eclLoginDaten, RpDrucken rpDrucken,
            RpVariablen rpVariablen) {

        rpVariablen.fuelleFeld(rpDrucken, "Gast.Intern", Integer.toString(lEclMeldung.meldungsIdent));
        rpVariablen.fuelleFeld(rpDrucken, "Gast.GKNr", lEclMeldung.zutrittsIdent);

        rpVariablen.fuelleFeld(rpDrucken, "Gast.Anrede", Integer.toString(lEclMeldung.anrede));
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Titel", lEclMeldung.titel);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Adelstitel", lEclMeldung.adelstitel);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Name", lEclMeldung.name);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Vorname", lEclMeldung.vorname);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.zuHaenden", lEclMeldung.zuHdCo);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Zusatz1", lEclMeldung.zusatz1);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Zusatz2", lEclMeldung.zusatz2);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Strasse", lEclMeldung.strasse);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Land", lEclMeldung.land);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.PLZ", lEclMeldung.plz);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Ort", lEclMeldung.ort);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.EMail", lEclMeldung.mailadresse);
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Kommunikationssprache",
                Integer.toString(lEclMeldung.kommunikationssprache));
        rpVariablen.fuelleFeld(rpDrucken, "Gast.Gruppe", Integer.toString(lEclMeldung.gruppe));

        rpVariablen.fuelleFeld(rpDrucken, "Gast.Kennung", eclLoginDaten.loginKennung);
        String pwInitial = eclLoginDaten.lieferePasswortInitialClean();
        if (pDruckeMitPasswort) {
            rpVariablen.fuelleFeld(rpDrucken, "Gast.Passwort", pwInitial);
        } else {
            rpVariablen.fuelleFeld(rpDrucken, "Gast.Passwort", "");
        }

        dateiExport.ausgabe(lEclMeldung.zutrittsIdent);
        dateiExport.ausgabe(Integer.toString(lEclMeldung.meldungsIdent));
        dateiExport.ausgabe(lEclMeldung.name);
        dateiExport.ausgabe(lEclMeldung.vorname);
        dateiExport.ausgabe(lEclMeldung.zuHdCo);
        dateiExport.ausgabe(lEclMeldung.zusatz1);
        dateiExport.ausgabe(lEclMeldung.strasse);
        dateiExport.ausgabe(lEclMeldung.ort);
        dateiExport.ausgabe(Integer.toString(lEclMeldung.kommunikationssprache));
        dateiExport.ausgabe(eclLoginDaten.loginKennung);
        if (pDruckeMitPasswort) {
            dateiExport.ausgabe(pwInitial);
        } else {
            dateiExport.ausgabe("");
        }
        dateiExport.newline();

    }

    /**selektion:
     * 1=alle
     * 2=gerade präsente
     * 3=war oder ist präsent
     *
     * sortierung:
     * 1=Gastkartennummer
     * 2=name
     *
     * ausgabeForm (mit "oder" verbunden):
     * 1 = Druck-Aufbereitung
     * 2 = Export csv
     */
    public void druckeGastkartenliste(RpDrucken rpDrucken, DbBundle pDbBundle, int selektion, int sortierung,
            String lfdNummer, int ausgabeForm) {

        RpVariablen rpVariablen = null;

        dateiExport = new CaDateiWrite();

        if ((ausgabeForm & 1) == 1) {
            rpDrucken.initListe(pDbBundle);

            rpVariablen = new RpVariablen(pDbBundle);
            rpVariablen.gastkartenListe(lfdNummer, rpDrucken);
            rpDrucken.startListe();
        }

        if ((ausgabeForm & 2) == 2) {
            dateiExport.trennzeichen = ';';
            dateiExport.dateiart = ".csv";

            dateiExport.oeffne(pDbBundle, "expGastkartenListe");
            exportDatei = dateiExport.dateiname;

            dateiExport.ausgabe("Gastkarten-Nr");
            dateiExport.ausgabe("InterneIdent");
            dateiExport.ausgabe("Name");
            dateiExport.ausgabe("Vorname");
            dateiExport.ausgabe("ZuHdCo");
            dateiExport.ausgabe("Zusatz1");
            dateiExport.ausgabe("Strasse");
            dateiExport.ausgabe("Ort");
            dateiExport.ausgabe("IstPrasent");
            dateiExport.ausgabe("IstOderWarPrasent");
            dateiExport.newline();

        }

        int rc = pDbBundle.dbMeldungen.leseAlleGastkarten();

        /*Sortieren*/
        switch (sortierung) {
        case 1: {
            Comparator<EclMeldung> comp = new EclMeldungCompZutrittsIdent();
            Arrays.sort(pDbBundle.dbMeldungen.meldungenArray, comp);
            break;
        }
        case 2: {
            Comparator<EclMeldung> comp = new EclMeldungCompName();
            Arrays.sort(pDbBundle.dbMeldungen.meldungenArray, comp);
            break;
        }

        }

        if (rc != 0) {

            for (int i = 0; i < rc; i++) {
                EclMeldung lMeldung = pDbBundle.dbMeldungen.meldungenArray[i];

                if (selektion == 1 || (selektion == 2 && lMeldung.statusPraesenz == 1) /*Ist Präsenz*/
                        || (selektion == 3 && lMeldung.statusWarPraesenz == 1) /*Ist oder war präsent*/
                ) {
                    if ((ausgabeForm & 1) == 1) {
                        // add here your preferred DokumentGenerator
                        rpDrucken.druckenListe();
                    }
                    if ((ausgabeForm & 2) == 2) {
                        dateiExport.ausgabe(lMeldung.zutrittsIdent);
                        dateiExport.ausgabe(Integer.toString(lMeldung.meldungsIdent));
                        dateiExport.ausgabe(lMeldung.name);
                        dateiExport.ausgabe(lMeldung.vorname);
                        dateiExport.ausgabe(lMeldung.zuHdCo);
                        dateiExport.ausgabe(lMeldung.zusatz1);
                        dateiExport.ausgabe(lMeldung.strasse);
                        dateiExport.ausgabe(lMeldung.ort);
                        dateiExport.ausgabe(Integer.toString(lMeldung.statusPraesenz));
                        dateiExport.ausgabe(Integer.toString(lMeldung.statusWarPraesenz));
                        dateiExport.newline();
                    }
                }

            }
        }

        if ((ausgabeForm & 1) == 1) {
            rpDrucken.endeListe();
            druckergebnisDatei = rpDrucken.drucklaufDatei;
        }

        if ((ausgabeForm & 2) == 2) {
            dateiExport.schliessen();
        }

    }

}
