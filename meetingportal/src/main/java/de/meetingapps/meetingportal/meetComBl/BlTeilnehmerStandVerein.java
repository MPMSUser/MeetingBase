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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclTeilnehmerStandVerein;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComHVParam.PblGeraeteStandort;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class BlTeilnehmerStandVerein {

    private DbBundle lDbBundle = null;

    public BlTeilnehmerStandVerein() {
        /*FIXME Überarbeiten wg. Datenbankaufteilung!*/
        lDbBundle = new DbBundle();
    }

    private EclTeilnehmerStandVerein lTeilnehmerStandVerein = null;

    /******Eintragen eines neuen Standes. manuell=true => die Ermittlung wurde manuell ausgelöst.
     * manuell=false => die Ermittlung wurde durch Automatismus ausgelöst
     * 
     * Rückgabe: Struktur mit aktuell ermitteltem Stand
     */
    public EclTeilnehmerStandVerein neuerStand(boolean manuell) {

        lDbBundle.openAll();

        lTeilnehmerStandVerein = new EclTeilnehmerStandVerein();
        if (manuell) {
            lTeilnehmerStandVerein.autoOrManuell = 2;
        } else {
            lTeilnehmerStandVerein.autoOrManuell = 1;
        }

        lTeilnehmerStandVerein.standZuZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        ermittleAnzahlAnwesender();
        ermittleWarnungen();
        ermittleBereichsAuslastung();

        lDbBundle.dbTeilnehmerStandVerein.insert(lTeilnehmerStandVerein);

        lDbBundle.closeAll();

        return lTeilnehmerStandVerein;
    }

    private void ermittleAnzahlAnwesender() {
        lTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederPraesent = lDbBundle.dbMeldungen
                .anzahlMitgliederStimmberechtigtAktuell();
        lTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederJemalsPraesent = lDbBundle.dbMeldungen
                .anzahlMitgliederStimmberechtigtJemals();
        lTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederPraesent = lDbBundle.dbMeldungen
                .anzahlMitgliederNichtStimmberechtigtAktuell();
        lTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederJemalsPraesent = lDbBundle.dbMeldungen
                .anzahlMitgliederNichtStimmberechtigtJemals();
        lTeilnehmerStandVerein.anzahlGaestePraesent = lDbBundle.dbMeldungen.anzahlGaesteNichtStimmberechtigtAktuell();
        lTeilnehmerStandVerein.anzahlGaesteJemalsPraesent = lDbBundle.dbMeldungen
                .anzahlGaesteNichtStimmberechtigtJemals();

    }

    private void ermittleWarnungen() {
        lTeilnehmerStandVerein.anzahlWarnungen = lDbBundle.dbMeldungen.anzahlWarnungenPraesent();

    }

    private void ermittleBereichsAuslastung() {

        PblGeraeteStandort pblGeraeteStandort = new PblGeraeteStandort(lDbBundle);

        int[] anzahlZugangsBuchungenProStandort = new int[10];
        for (int i = 0; i < 10; i++) {
            anzahlZugangsBuchungenProStandort[i] = 0;
        }

        String startzeit = CaDatumZeit.subtrahiereSekunden(lTeilnehmerStandVerein.standZuZeit, 150);
        lDbBundle.dbWillenserklaerung.leseAbZeitstempel(startzeit);
        int anzahl = lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
        for (int i = 0; i < anzahl; i++) {
            EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungArray[i];
            if (lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.erstzugang
                    || lWillenserklaerung.willenserklaerung == KonstWillenserklaerung.wiederzugang) {
                int arbeitsplatzGruppe = pblGeraeteStandort.liefereStandortZuGeraet(lWillenserklaerung.arbeitsplatz);
                anzahlZugangsBuchungenProStandort[arbeitsplatzGruppe]++;
            }

        }

        lTeilnehmerStandVerein.anzahlBuchungenZuB0 = anzahlZugangsBuchungenProStandort[0];
        lTeilnehmerStandVerein.anzahlBuchungenZuB1 = anzahlZugangsBuchungenProStandort[1];
        lTeilnehmerStandVerein.anzahlBuchungenZuB2 = anzahlZugangsBuchungenProStandort[2];
        lTeilnehmerStandVerein.anzahlBuchungenZuB3 = anzahlZugangsBuchungenProStandort[3];
        lTeilnehmerStandVerein.anzahlBuchungenZuB4 = anzahlZugangsBuchungenProStandort[4];
        lTeilnehmerStandVerein.anzahlBuchungenZuB5 = anzahlZugangsBuchungenProStandort[5];
        lTeilnehmerStandVerein.anzahlBuchungenZuB6 = anzahlZugangsBuchungenProStandort[6];
        lTeilnehmerStandVerein.anzahlBuchungenZuB7 = anzahlZugangsBuchungenProStandort[7];
        lTeilnehmerStandVerein.anzahlBuchungenZuB8 = anzahlZugangsBuchungenProStandort[8];
        lTeilnehmerStandVerein.anzahlBuchungenZuB9 = anzahlZugangsBuchungenProStandort[9];

    }

    /**Liefert im Array alle bisher ermittelten Stände zurück. Null, wenn nichts vorhanden*/
    public EclTeilnehmerStandVerein[] liefereHistorie() {
        EclTeilnehmerStandVerein[] lTeilnehmerStandVerein = null;
        lDbBundle.openAll();
        lDbBundle.dbTeilnehmerStandVerein.read_all();
        if (lDbBundle.dbTeilnehmerStandVerein.anzErgebnis() > 0) {
            lTeilnehmerStandVerein = lDbBundle.dbTeilnehmerStandVerein.ergebnis();
        }

        lDbBundle.closeAll();
        return lTeilnehmerStandVerein;

    }
}
