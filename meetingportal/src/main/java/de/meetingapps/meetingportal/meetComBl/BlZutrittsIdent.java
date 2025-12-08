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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;

public class BlZutrittsIdent {

    private DbBundle lDbBundle = null;

    public BlZutrittsIdent(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    public EclZutrittsIdent rEclZutrittsIdent = new EclZutrittsIdent();

    /**pKlasse=0 => Gast, 1=Aktionär
     * Returnwert:
     * 1 => Ok
     * pfAutomatischeVergabeXyNichtMoeglich
     * pfZutrittsIdentNebenZuHoch
     * 
     * Gefüllt:
     * 	rEclzutrittsIdent
     * */
    public int neueZutrittsIdentZuMeldung(int pMeldungsIdent, int pKlasse, int pGattung) {
        lDbBundle.dbZutrittskarten.readZuMeldungsIdent(pMeldungsIdent); //Hier wird davon ausgegangen: es werden alle geliefert, auch gesperrte
        int anzZutrittsIdentInsgesamt = lDbBundle.dbZutrittskarten.anzErgebnis();

        /*Erster Durchlauf: es wird ermittelt, ob bereits (nicht versionierte-gesperrte) Karten vorhanden sind, die verwendet werden.
         * Ergebnis: ersteNichtGesperrte; und ggf. gefundene EK in rEclZutrittsIdent.zutrittsIdent*/
        int ersteNichtGesperrte = -1;
        for (int i = 0; i < anzZutrittsIdentInsgesamt; i++) {
            EclZutrittskarten lEclZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(i);
            if (lEclZutrittskarte.zutrittsIdentVers == 0) {
                if (ersteNichtGesperrte == -1) {//Erste gefundene Nicht-Gesperrte wird "gemerkt" und verwendet
                    ersteNichtGesperrte = i;
                    rEclZutrittsIdent.zutrittsIdent = lEclZutrittskarte.zutrittsIdent;
                }
            }
        }
        if (ersteNichtGesperrte == -1 || lDbBundle.param.paramBasis.liefereEintrittskarteNeuVergeben(pGattung)
        /*ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant)*/) {
            /*Noch keine (gültige) ZutrittsIdent zugeordnet - neue vergeben*/
            int hNeueNummer = 0;
            switch (pKlasse) {
            case 1:
                hNeueNummer = lDbBundle.dbBasis.getEintrittskartennummer(pGattung);
                break;
            case 0:
                hNeueNummer = lDbBundle.dbBasis.getGastkartennummer();
                break;
            }

            if (hNeueNummer < 1) {
                return hNeueNummer;
            }
            String hNeueNummerString = Integer.toString(hNeueNummer);

            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            rEclZutrittsIdent.zutrittsIdent = blNummernformen.formatiereEKNr(hNeueNummerString);
            rEclZutrittsIdent.zutrittsIdentNeben = "00";
            return 1;
        }

        /*Zweiter Durchlauf: höchste "Nebennummer" der zutrittsIdent finden, und um 1 erhöhen*/
        for (int i = 0; i < anzZutrittsIdentInsgesamt; i++) {
            EclZutrittskarten lEclZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(i);
            if (lEclZutrittskarte.zutrittsIdent.compareTo(rEclZutrittsIdent.zutrittsIdent) == 0) {
                if (lEclZutrittskarte.zutrittsIdentNeben.compareTo(rEclZutrittsIdent.zutrittsIdentNeben) > 0) {
                    rEclZutrittsIdent.zutrittsIdentNeben = lEclZutrittskarte.zutrittsIdentNeben;
                }
            }
        }
        int hoechsteNebennummer = Integer.parseInt(rEclZutrittsIdent.zutrittsIdentNeben) + 1;
        if (hoechsteNebennummer > 99) {
            rEclZutrittsIdent.zutrittsIdent = "";
            return CaFehler.pfZutrittsIdentNebenZuHoch;
        } //Höchste Anzahl Nebennummern erreicht!
        String hString = Integer.toString(hoechsteNebennummer);
        rEclZutrittsIdent.zutrittsIdentNeben = CaString.fuelleLinksNull(hString, 2);

        return 1;
    }

    static public int compare(String pErsteZutrittsIdent, String pErsteZutrittsIdentNeben, String pZweiteZutrittsIdent,
            String pZweiteZutrittsIdentNeben) {
        int rc;
        rc = pErsteZutrittsIdent.compareTo(pZweiteZutrittsIdent);
        if (rc != 0) {
            return rc;
        } /*Dann bereits Unterschied in der "Hauptnummer"*/

        return pErsteZutrittsIdentNeben.compareTo(pZweiteZutrittsIdentNeben);

    }

    public static String aufbereitenFuerAnzeige(String pIdent, String pIdentNeben) {
        String anzeige = pIdent;
        if (!pIdentNeben.isEmpty()) {
            pIdentNeben = CaString.fuelleLinksNull(pIdentNeben, 2);
        }
        if (!pIdentNeben.isEmpty() && pIdentNeben.compareTo("00") != 0) {
            anzeige = anzeige + "-" + pIdentNeben;
        }
        return anzeige;
    }

}
