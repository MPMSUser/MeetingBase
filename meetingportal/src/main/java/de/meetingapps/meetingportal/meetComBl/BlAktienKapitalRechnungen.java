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

import de.meetingapps.meetingportal.meetComHVParam.ParamBasis;

public class BlAktienKapitalRechnungen {

    private ParamBasis paramBasis = null;

    public long rcGesamtKapital = 0;
    public long rcGesamtStimmen = 0;
    public double rcProzentVomJeweiligenKapitalStimmen = 0.0;
    public double rcProzentVomGesamtKapitalStimmen = 0.0;

    public BlAktienKapitalRechnungen(ParamBasis pParamBasis) {
        paramBasis = pParamBasis;
    }

    /**Ergebnis in:
     * rcGesamtSapital
     */
    public void berechneGesamtKapital() {
        rcGesamtKapital = 0;
        for (int i = 0; i < 5; i++) {
            if (paramBasis.gattungAktiv[i]) {
                rcGesamtKapital += paramBasis.grundkapitalStueck[i];
            }
        }
    }

    /**Ergebnis in:
     * rcGesamtStimmen
     */
    public void berechneGesamtStimmen() {
        rcGesamtStimmen = 0;
        for (int i = 0; i < 5; i++) {
            if (paramBasis.gattungAktiv[i]) {
                long hGattungsGrundkapital = paramBasis.grundkapitalStueck[i];
                double hGattungsWertEinerAktie = paramBasis.wertEinerAktie[i];
                if (hGattungsGrundkapital != 0 && hGattungsWertEinerAktie != 0.0) {
                    rcGesamtStimmen += (hGattungsGrundkapital / hGattungsWertEinerAktie);
                }
            }
        }
    }

    /**Einzelwert=z.B. Stimmen eines Aktionärs.
     * gattung=1 bis 5.
     * 
     * Kapital wird in Stimmen umgerechnet, daraus dann %.
     * Ergebnis in rcProzentVomJeweiligenKapitalStimmen*/
    public void berechneProzentAktienVomJeweiligenKapitalStimmen(int gattung, long stimmenEinzelWert) {
        rcProzentVomJeweiligenKapitalStimmen = 0.0;
        long hGattungsGrundkapital = paramBasis.grundkapitalStueck[gattung - 1];
        double hGattungsWertEinerAktie = paramBasis.wertEinerAktie[gattung - 1];
        if (hGattungsGrundkapital != 0 && hGattungsWertEinerAktie != 0.0) {
            rcProzentVomJeweiligenKapitalStimmen = stimmenEinzelWert / (hGattungsGrundkapital / hGattungsWertEinerAktie)
                    * 100;
        }
    }

    /**Einzelwert=z.B. Stimmen eines Aktionärs.
     * gattung=1 bis 5.
     * 
     * Gesamtstimmen werden berechnet.
     * Kapital wird in Stimmen umgerechnet, daraus dann %.
     * Ergebnis in rcProzentVomGesamtKapitalStimmen*/
    public void berechneProzentAktienVomGesamtKapitalStimmen(int gattung, long stimmenEinzelWert) {
        berechneGesamtStimmen();
        rcProzentVomGesamtKapitalStimmen = 0.0;
        if (rcGesamtStimmen != 0) {
            rcProzentVomGesamtKapitalStimmen = (double) stimmenEinzelWert / (double) rcGesamtStimmen * 100;
        }
    }

}
