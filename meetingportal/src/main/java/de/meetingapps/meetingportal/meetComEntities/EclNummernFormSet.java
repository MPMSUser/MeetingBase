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
package de.meetingapps.meetingportal.meetComEntities;

public class EclNummernFormSet {

    /*233*/

    public int mandant = 0;

    /**Es gibt zwei Zählweisen:
     * > Mandantenübergreifend für die "global verfügbaren" (Mandant=0)
     * > je Mandant für ggf. lokal angefügte Nummernformen (noch nicht implementiert!)
     */
    public int ident = 0;
    /**1 => wurde gelöscht. In diesem Fall ist i.d.R. in ersetztDurch die ident der
     * Nummernform eingetragen, auf die die gelöschte Nummernform "umgeroutet" wurde
     * (muß immer vom mandant 0 sein)
     */
    public int geloescht = 0;
    /**Bei gelöscht-gekennzeichneten Nummernformen: hier wird die Ident eingetragen,
     * die verwendet wird statt der gelöschten
     */
    public int ersetztDurch = 0;
    /**LEN=20*/
    public String name = "";
    /**LEN=500*/
    public String beschreibung = "";

    /**1=einstellige Klassifizierung, 2 = zweistellige Klassifiezierung*/
    public int klassifizierung = 1;

    /******Ab hier: Kommentierung siehe Param Nummernkreise!*****/

    public int[] laengeKartennummer = new int[8]; /*In Datenbank: 1 bis 6; 7 wird wie 1 gelesen*/
    public boolean[] istNumerisch = new boolean[8]; /*In Datenbank: 1 bis 6; 7 wird wie 1 gelesen*/

    public int[] vonKartennummerGesamt = new int[8]; /*Nummernkreise nur 1 bis 3 in Datenbank*/
    public int[] bisKartennummerGesamt = new int[8];

    public int[] vonKartennummerAuto = new int[8];
    public int[] bisKartennummerAuto = new int[8];

    public int[] vonKartennummerManuell = new int[8];
    public int[] bisKartennummerManuell = new int[8];

    public int vonSammelkartennummer = 0;
    public int bisSammelkartennummer = 0;

    public int[][] vonSubStimmkartennummer = new int[6][6];
    public int[][] bisSubStimmkartennummer = new int[6][6];

    public int[][] vonSubEintrittskartennummer = new int[6][3];
    public int[][] bisSubEintrittskartennummer = new int[6][3];

    /******Bis hier: Kommentierung siehe Param Nummernkreise!*******/

    public int kodierungJa = 1;
    public int kodierungNein = 2;
    public int kodierungEnthaltung = 3;

    /**Länge der Eintrittskarte*/
    //	public int laengeEK=5;

    /**Länge der Stimmkarte*/
    //	public int laengeSK=5;

    /**0=nein, 1=ja*/
    public int beiEintrittskarteWirdStimmkarteNebenAnhaengen = 0;

    /**Siehe ParamPruefzahlen*/
    public int berechnungsVerfahrenPruefziffer = 0;

    /**10 x 10*/
    public int kombiZuCode[][] = null;

    /**10*/
    public int klasseZuCode[] = null;

    /**10*/
    public int artZuCode[] = null;

    /**10x10*/
    public int nummernformZuKlasseArt[][] = null;

    public EclNummernFormSet() {
        kombiZuCode = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                kombiZuCode[i][i1] = -1;
            }
        }

        klasseZuCode = new int[10];
        for (int i = 0; i < 10; i++) {
            klasseZuCode[i] = -1;
        }
        for (int i = 1; i <= 7; i++) {
            klasseZuCode[i] = i;
        }

        artZuCode = new int[10];
        for (int i = 0; i < 10; i++) {
            artZuCode[i] = -1;
        }
        for (int i = 1; i <= 8; i++) {
            artZuCode[i] = i;
        }

        nummernformZuKlasseArt = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                nummernformZuKlasseArt[i][i1] = -1;
            }
        }
    }
}
