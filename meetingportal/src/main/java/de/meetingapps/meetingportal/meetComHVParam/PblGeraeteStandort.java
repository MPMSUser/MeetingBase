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
package de.meetingapps.meetingportal.meetComHVParam;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

/*TODO _GeräteStandorte: - Noch komplett durchzugestalten. Derzeit nur für Ku254 fest kodiert. Zu berücksichtigen: Ersatzgeräte, die an unterschiedlichen Standorten eingesetzt werden ...*/

/**Abfrage und Verwaltung der Standorte von Geräten gemäß Parameterdatei.
 * Aktuell noch nicht DB-mäßig parametrisiert.
 * 
 * pDbBundle muß geöffnet sein.
 */
public class PblGeraeteStandort {

    private DbBundle dbBundle = null;

    public PblGeraeteStandort(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Liefert die Anzahl der eingetragenen Standorte (Startend bei Standort 0!)*/
    public int liefereStandortAnzahl() {
        int anzahl = 1;
        if (ParamSpezial.ku254(dbBundle.clGlobalVar.mandant)) {
            anzahl = 7;
        }
        return anzahl;
    }

    /**Liefert Standort-Nummer der übergebenen Arbeitsplatznummer.
     * Aktuell returnwert von 0 bis 9 zulässig - siehe liefereStandortBezeichnung */
    public int liefereStandortZuGeraet(int pArbeitsplatzNummer) {

        if (ParamSpezial.ku254(dbBundle.clGlobalVar.mandant)) {
            /*West 1 301-307*/
            if ((pArbeitsplatzNummer >= 301 && pArbeitsplatzNummer <= 307)
                     ) {
                return 1;
            }
            /*West 2 309/311-316*/
            if (pArbeitsplatzNummer == 309 || 
                    (pArbeitsplatzNummer >= 311 && pArbeitsplatzNummer <= 316)
                    ) {
                return 2;
            }
            /*Ost 1 318, 319, 321-325*/
            if ((pArbeitsplatzNummer == 318 ||
                    pArbeitsplatzNummer == 319 ||
                    (pArbeitsplatzNummer >= 321
                    && pArbeitsplatzNummer <= 325))
                     ) {
                return 3;
            }
            /*Ost 2 308, 317, 326-330*/
            if (pArbeitsplatzNummer == 308 ||
                    pArbeitsplatzNummer == 317 ||
                    (pArbeitsplatzNummer >= 326 && pArbeitsplatzNummer <= 330)
                    ) {
                return 4;
            }
            /*Tunnel*/
            if (pArbeitsplatzNummer == 351) {
                return 5;
            }
            /*? Mitarbeiter*/
            if (pArbeitsplatzNummer == 8002) {
                return 6;
            }
            /*? Ersatzgerät*/
            if (pArbeitsplatzNummer == 8002) {
                return 7;
            }
       }

        return 0;
    }

    /**Hinweise: 
     * > diejenigen, die "" zurückliefern, müssen am Ende sein - d.h. keine Lücken zulässig.
     * > RZ muß immer an Pos 0 sein - wird in der Statistik automatisch ans Ende gehängt
     * 
     * @param pStandortNummer
     * @return
     */
    public String liefereStandortBezeichnung(int pStandortNummer) {

        if (ParamSpezial.ku254(dbBundle.clGlobalVar.mandant)) {
            switch (pStandortNummer) {
            case 0:
                return "Rechenzentrum";
            case 1:
                return "West I";//301-310
            case 2:
                return "West II";//311-320
            case 3:
                 return "Ost I";//321-330
            case 4:
                return "Ost II";//331-340
            case 5:
                return "Brücke";//351
            case 6:
                return "Mitarbeiter";//342
            case 7:
                return "Ersatzgerät";//343
            default:
                return "";
            }
        }

        if (pStandortNummer == 0) {
            return "Standart";
        } else {
            return "Undefiniert";
        }
    }

}
