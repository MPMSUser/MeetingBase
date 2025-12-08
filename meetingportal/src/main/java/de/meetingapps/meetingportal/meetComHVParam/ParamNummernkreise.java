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

import java.io.Serializable;

public class ParamNummernkreise implements Serializable {
    private static final long serialVersionUID = 6903831176776624147L;

    /**Übergreifend - nicht in DB*/
    /**"Kürzeste" Nummer (ohne Prüfziffern etc.) - wenn bis zu dieser Länge eingegeben wurde, dann
     * wird davon ausgegangen, dass nur eine Nummer eingegeben wurde.
     */
    public int laengeOhnePruefziffern = 0;

    /**Eine der Nummern ist alphanumerisch => keine Autoerkennung über Nummernkreise möglich*/
    public boolean einNummernkreisIstAlpha = false;

    /**Jeweils für Kartenklasse:*/
    public int[] laengeKartennummer = new int[8];
    public boolean[] istNumerisch = new boolean[8];

    /**Gesamtnummernkreis der jeweiligen Klasse*/
    public int[] vonKartennummerGesamt = new int[8];
    public int[] bisKartennummerGesamt = new int[8];

    /**Nummernkreis für automatische Vergabe durch System.
     * NICHT verwendet für Kartenklassen eintrittskartennummer und stimmkartennummer! Für diese Kartenklassen: Siehe vonSub**/
    public int[] vonKartennummerAuto = new int[8];
    public int[] bisKartennummerAuto = new int[8];

    /**Nummernkreis für manuelle Vergabe
     * NICHT verwendet für Kartenklassen eintrittskartennummer und stimmkartennummer! Für diese Kartenklassen: Siehe vonSub**/
    public int[] vonKartennummerManuell = new int[8];
    public int[] bisKartennummerManuell = new int[8];

    /**Nur für Eintrittskartennummer: "freiwilliger" Nummernkreis für Sammelkarten. 
     * Dieser Nummernkreis muß im Gesamtnummernkreis enthalten sein, darf jedoch nicht in einem der "Sub-Nummernkreise" enthalten sein.
     * Falls !=0, dann wird verhindert, dass EKs aus diesem Nummernkreis für "normale" Eintrittskarten verwendet werden. 
     * Falls von/bis==0, dann wird nur überprüft ob die Sammelkarte in einem der Manuellen Sub-Kreise enthalten ist.
     */
    public int vonSammelkartennummer = 0;
    public int bisSammelkartennummer = 0;

    /**Nur für Stimmkarten: Nummernkreis für "Sub"-Kreise, jeweils pro Gattung.
     * Verwendung nur, um Zuordnung des "richtigen" Stimmaterials zur EK der jeweiligen
     * Gattung zu überwachen.
     * 
     * [Gattung][Sub-Kreis]; Gattung=1 bis 5, Sub-Kries 1 bis 5 ( 1 bis 4 = Stimmkarten; 5=Virtueller Nummernkreis für App- und Online-Teilnahme) ;*/
    public int[][] vonSubStimmkartennummer = null;
    public int[][] bisSubStimmkartennummer = null;

    /**Nur für Eintrittskarten: Nummernkreis für "Sub"-Kreise, jeweils pro Gattung.
     * 
     * [Gattung][Sub-Kreis]; Gattung=1 bis 5, Sub-Kreis 1 bis 2 ( 1=Manuell vergebene Karten; 2=Automatisch vergebene Karten) ;*/
    public int[][] vonSubEintrittskartennummer = null;
    public int[][] bisSubEintrittskartennummer = null;

    public ParamNummernkreise() {

        for (int i = 0; i < 8; i++) {
            laengeKartennummer[i] = 5;
            istNumerisch[i] = true;
            vonKartennummerGesamt[i] = 0;
            bisKartennummerGesamt[i] = 0;
            vonKartennummerAuto[i] = 0;
            bisKartennummerAuto[i] = 0;
            vonKartennummerManuell[i] = 0;
            bisKartennummerManuell[i] = 0;
        }

        vonSubStimmkartennummer = new int[6][6];
        bisSubStimmkartennummer = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int i1 = 0; i1 < 6; i1++) {
                vonSubStimmkartennummer[i][i1] = 0;
                bisSubStimmkartennummer[i][i1] = 0;
            }
        }

        vonSubEintrittskartennummer = new int[6][3];
        bisSubEintrittskartennummer = new int[6][3];
        for (int i = 0; i < 6; i++) {
            for (int i1 = 0; i1 < 3; i1++) {
                vonSubEintrittskartennummer[i][i1] = 0;
                bisSubEintrittskartennummer[i][i1] = 0;
            }
        }

    }
}
