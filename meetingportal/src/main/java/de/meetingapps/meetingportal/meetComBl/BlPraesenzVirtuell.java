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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

/**Zugang-Abgangs-Buchungen für virtuelle Versammlungen*/
public class BlPraesenzVirtuell {

    private DbBundle lDbBundle = null;

    private int logDrucken = 3;
    private EclWillenserklaerung preparedWillenserklaerung = null;
    private EclWillenserklaerungZusatz preparedWillenserklaerungZusatz = null;
    private EclMeldung hEclMeldung = null;
    private int hVirtuellerTeilnehmer = 0;

    public BlPraesenzVirtuell(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public void buchenAktionaer_abgang(EclMeldung pEclMeldung, int pVirtuellerTeilnehmer) {

        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

        BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
        lBlPraesenzProtokoll.leseProtokollNr();

        /*Meldung neu einlesen, da möglicherweise vorher verändert*/
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(pEclMeldung.meldungsIdent);
        hEclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        hVirtuellerTeilnehmer = pVirtuellerTeilnehmer;

        prepareWillenserklaerung(KonstWillenserklaerung.abgang);

        hEclMeldung.statusPraesenz = 2;
        hEclMeldung.statusPraesenz_Delayed = 2;

        hEclMeldung.statusWarPraesenz = 1;
        hEclMeldung.statusWarPraesenz_Delayed = 1;

        hEclMeldung.virtuellerTeilnehmerIdent = 0;

        lDbBundle.dbMeldungen.update(hEclMeldung);

        lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung, preparedWillenserklaerungZusatz);

        /*Präsenzsummen korrigieren*/
        if (hEclMeldung.meldungIstEinAktionaer()) {
            BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            lBlPraesenzSummen.abgang(hEclMeldung.stueckAktien, hEclMeldung.gattung, 0);
        }

    }

    public void buchenAktionaer_zugang(EclMeldung pEclMeldung, int pVirtuellerTeilnehmer, int pBevollmaechtigter,
            boolean pSindEigeneAktien, String pBbevollmaechtigterName, String pBevollmaechtigterVorname,
            String pBevollmaechtigterOrt) {

        String bevollmaechtigterName = pBbevollmaechtigterName;
        String bevollmaechtigterVorname = pBevollmaechtigterVorname;
        String bevollmaechtigterOrt = pBevollmaechtigterOrt;

        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

        BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
        lBlPraesenzProtokoll.leseProtokollNr();

        /*Meldung neu einlesen, da möglicherweise vorher verändert*/
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(pEclMeldung.meldungsIdent);
        hEclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        hVirtuellerTeilnehmer = pVirtuellerTeilnehmer;

        prepareWillenserklaerung(KonstWillenserklaerung.erstzugang);

        /*Ggf. Bevollmächtigten eintragen*/
        CaBug.druckeLog("pBevollmaechtigter=" + pBevollmaechtigter + " pVirtuellerTeilnehmer=" + pVirtuellerTeilnehmer
                + " pSindEigeneAktien=" + pSindEigeneAktien, logDrucken, 10);
        if (pBevollmaechtigter != pVirtuellerTeilnehmer || pSindEigeneAktien == false) {
            preparedWillenserklaerung.bevollmaechtigterDritterIdent = pBevollmaechtigter;
        } else {
            bevollmaechtigterName = "";
            bevollmaechtigterVorname = "";
            bevollmaechtigterOrt = "";
        }

        hEclMeldung.statusPraesenz = 1;
        hEclMeldung.statusPraesenz_Delayed = 1;

        hEclMeldung.statusWarPraesenz = 1;
        hEclMeldung.statusWarPraesenz_Delayed = 1;

        hEclMeldung.virtuellerTeilnehmerIdent = hVirtuellerTeilnehmer;

        hEclMeldung.vertreterName = bevollmaechtigterName;
        hEclMeldung.vertreterVorname = bevollmaechtigterVorname;
        hEclMeldung.vertreterOrt = bevollmaechtigterOrt;

        lDbBundle.dbMeldungen.update(hEclMeldung);

        lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung, preparedWillenserklaerungZusatz);

        /*Präsenzsummen korrigieren*/
        if (hEclMeldung.meldungIstEinAktionaer()) {
            BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            lBlPraesenzSummen.zugang(hEclMeldung.stueckAktien, hEclMeldung.gattung, 0);
        }

    }

    private void prepareWillenserklaerung(int willensart) {

        EclWillenserklaerung lPreparedWillenserklaerung = new EclWillenserklaerung();
        EclWillenserklaerungZusatz lPreparedWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
        lPreparedWillenserklaerung.willenserklaerung = willensart;
        lPreparedWillenserklaerung.meldungsIdent = hEclMeldung.meldungsIdent;
        lPreparedWillenserklaerung.stimmen = hEclMeldung.stimmen;
        lPreparedWillenserklaerung.aktien = hEclMeldung.stueckAktien;

        lPreparedWillenserklaerung.identifikationDurch = 0;
        lPreparedWillenserklaerung.identifikationKlasse = 0;
        lPreparedWillenserklaerung.identifikationZutrittsIdent = "";
        lPreparedWillenserklaerung.identifikationStimmkarte = "";
        lPreparedWillenserklaerung.identifikationStimmkarteSecond = "";

        lPreparedWillenserklaerung.erteiltAufWeg = KonstWillenserklaerungWeg.onlineTeilnahme;
        lPreparedWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        lPreparedWillenserklaerung.willenserklaerungGeberIdent = hVirtuellerTeilnehmer;

        lPreparedWillenserklaerung.verweisart = 0;
        lPreparedWillenserklaerung.verweisAufWillenserklaerung = 0;

        lPreparedWillenserklaerung.folgeBuchungFuerIdent = 0;
        /*Hinweis: wird ggf. von den einzelnen "Nutzern" nach Aufruf von prepareWillenserklaerung
        
         * nochmal überschrieben - siehe Beschreibung "mehrfach abhängig" bei pFolgeFuerWillenserklaerungIdent
         */

        preparedWillenserklaerung = lPreparedWillenserklaerung;
        preparedWillenserklaerungZusatz = lPreparedWillenserklaerungZusatz;
        return;
    }

}
