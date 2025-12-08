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
package de.meetingapps.meetingportal.meetComBlStatistik;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclS.EclSBuchungenHV;
import de.meetingapps.meetingportal.meetComHVParam.PblGeraeteStandort;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class BlStatistikArbeitsplatz {

    private DbBundle dbBundle = null;

    public BlStatistikArbeitsplatz(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /***************ergebnisEclSBuchungenHV*************************/
    private EclSBuchungenHV[] ergebnisEclSBuchungenHV = null;

    /**Anzahl der Ergebnisse*/
    public int ergebnisAnzEclSBuchungenHV() {
        if (ergebnisEclSBuchungenHV == null) {
            return 0;
        }
        return ergebnisEclSBuchungenHV.length;
    }

    /**Liefert pN-tes Element des Ergebnisses. pN geht von 0 bis anzErgebnis-1*/
    public EclSBuchungenHV ergebnisPositionEclSBuchungenHV(int pN) {
        if (ergebnisEclSBuchungenHV == null) {
            CaBug.drucke("BlStatistik.ergebnisPositionEclSBuchungenHV 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("BlStatistik.ergebnisPositionEclSBuchungenHV 002");
            return null;
        }
        if (pN >= ergebnisEclSBuchungenHV.length) {
            CaBug.drucke("BlStatistik.ergebnisPositionEclSBuchungenHV 003");
            return null;
        }
        return ergebnisEclSBuchungenHV[pN];
    }

    /**Liefert Array zurück*/
    public EclSBuchungenHV[] ggetErgebnisEclSBuchungenHV() {
        return ergebnisEclSBuchungenHV;
    }

    /**************************ergebnisArbeitsplatzKumuliert***************************/
    private List<int[]> ergebnisArbeitsplatzKumuliert = null;

    /**Anzahl der Ergebnisse*/
    public int ergebnisAnzArbeitsplatzKumuliert() {
        if (ergebnisArbeitsplatzKumuliert == null) {
            return 0;
        }
        return ergebnisArbeitsplatzKumuliert.size();
    }

    /**Liefert pN-tes Element des Ergebnisses. pN geht von 0 bis anzErgebnis-1.
     * [0]=ZeitSlot-Nummer
     * [1]=Arbeitsplatz-Nummer
     * [2]=Buchungen insgesamt im Slot
     * [3]=Anzahl Zugänge im Slot
     * [4]=Anzahl Wiederzugänge im Slot
     * [5]=Anzahl Abgänge im Slot
     * */
    public int[] ergebnisPositionArbeitsplatzKumuliert(int pN) {
        if (ergebnisEclSBuchungenHV == null) {
            CaBug.drucke("BlStatistik.ergebnisPositionArbeitsplatzKumuliert 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("BlStatistik.ergebnisPositionArbeitsplatzKumuliert 002");
            return null;
        }
        if (pN >= ergebnisEclSBuchungenHV.length) {
            CaBug.drucke("BlStatistik.ergebnisPositionArbeitsplatzKumuliert 003");
            return null;
        }
        return ergebnisArbeitsplatzKumuliert.get(pN);
    }

    /**Liefert Array zurück*/
    public List<int[]> getErgebnisArbeitsplatzKumuliert() {
        return ergebnisArbeitsplatzKumuliert;
    }

    /*****************Buchungen pro Standort*******************************/
    private int[][] ergebnisJeStandortBuchungenGesamt = null;
    private int[][] ergebnisJeStandortBuchungenZugaenge = null;
    private int[][] ergebnisJeStandortBuchungenWiederzugaenge = null;
    private int[][] ergebnisJeStandortBuchungenAbgaenge = null;

    /**Liefert AnzahlBuchungenGesamt[Zeitslot][StandortNr] zurück*/
    public int[][] getErgebnisJeStandortBuchungenGesamt() {
        return ergebnisJeStandortBuchungenGesamt;
    }

    /**Liefert AnzahlBuchungenZugänge[Zeitslot][StandortNr] zurück*/
    public int[][] getErgebnisJeStandortBuchungenZugaenge() {
        return ergebnisJeStandortBuchungenZugaenge;
    }

    /**Liefert AnzahlBuchungenWiederzugänge[Zeitslot][StandortNr] zurück*/
    public int[][] getErgebnisJeStandortBuchungenWiederzugaenge() {
        return ergebnisJeStandortBuchungenWiederzugaenge;
    }

    /**Liefert AnzahlBuchungenGesamt[Zeitslot][StandortNr] zurück*/
    public int[][] getErgebnisJeStandortBuchungenAbgaenge() {
        return ergebnisJeStandortBuchungenAbgaenge;
    }

    /**************Gesamtsummen je Standort************************/
    private int[] ergebnisSummeJeStandortBuchungenGesamt = null;
    private int[] ergebnisSummeJeStandortBuchungenZugaenge = null;
    private int[] ergebnisSummeJeStandortBuchungenWiederzugaenge = null;
    private int[] ergebnisSummeJeStandortBuchungenAbgaenge = null;

    /**Liefert AnzahlSummeBuchungenGesamt[StandortNr] zurück*/
    public int[] getErgebnisSummeJeStandortBuchungenGesamt() {
        return ergebnisSummeJeStandortBuchungenGesamt;
    }

    /**Liefert AnzahlSummeBuchungenZugänge[StandortNr] zurück*/
    public int[] getErgebnisSummeJeStandortBuchungenZugaenge() {
        return ergebnisSummeJeStandortBuchungenZugaenge;
    }

    /**Liefert AnzahlSummeBuchungenWiederzugänge[StandortNr] zurück*/
    public int[] getErgebnisSummeJeStandortBuchungenWiederzugaenge() {
        return ergebnisSummeJeStandortBuchungenWiederzugaenge;
    }

    /**Liefert AnzahlSummeBuchungenGesamt[StandortNr] zurück*/
    public int[] getErgebnisSummeJeStandortBuchungenAbgaenge() {
        return ergebnisSummeJeStandortBuchungenAbgaenge;
    }

    /*TODO _Arbeitsplatzstatistik - die einzelnen Willenserklärungen werden entgegen untriger Beschreibung nicht in eine Datei geschrieben!'/
     * 
     */
    /********************************************************************************************
     * Auswertung: Präsenzbuchungen pro Arbeitsplatz.
     * 
     * pStartZeit=JJJJ.MM.JJ HH:MM:SS, ab der die Statistik geschrieben werden soll. Alle Willenserklärungen 
     * davor werden in der ersten Zeiteinheit zusammengefaßt
     * 
     * pMinutenProZeiteinheit=Länge einer kumulierten Einheit in Minuten
     * 
     * pSchreibenInDatei = true => Ergebnis wird in Dateien geschrieben:
     * 
     * StatistikPraesenzBuchungenProArbeitsplatz
     * Format:
     * <Art der Willenserklärung>;<ArbeitsplatzNr>;<Zeiteinheit> 
     * (wird noch nicht erzeugt - siehe obiges ToDo!)
     * 
     * praesenzBuchungenProArbeitsplatz_Summen
     * > Summen in CSV-Format. Dateiformat selbstbeschreibend
     * 
     * praesenzBuchungenVerlaufJeStandort_*
     * Die jeweiligen kumulierten Werte je Slot und je Gerätestandort werden eingetragen.
     * Nach unten: Slots. Nach rechts: Gerätestandorte.
     * Datei kann in Excel weiterverarbeitet werden und z.B. in Grafiken verarbeitet werden.
     * 
     * 
     * Liefert ergebnis in: 
     * > ergebnisEclSBuchungenHV (Sortierung nach Arbeitsplatznr, Uhrzeit)
     * > ergebnisArbeitsplatzKumuliert (zusammengefaßt in Zeitslots der Länge pMinutenProZeiteinheit,
     * 		sortiert nach Arbeitsplätzen, Zeitslotnummer
     * > ergebnisJeStandortBuchungenGesamt, ergebnisJeStandortBuchungenZugaenge, ergebnisJeStandortBuchungenWiderzugaenge,
     * 		ergebnisJeStandortBuchungenAbgaenge; jeweils [Zeitslot][StandortNr]
     * > ergebnisSummeJeStandortBuchungenGesamt, ergebnisSummeJeStandortBuchungenZugaenge,
     * 		ergebnisSummeJeStandortBuchungenWiederzugaenge, ergebnisSummeJeStandortBuchungenAbgaenge; jeweils [StandortNr]
     */
    public void praesenzBuchungenProArbeitsplatz(String pStartZeit, int pMinutenProZeiteinheit,
            boolean pSchreibenInDatei) {
        /*Willenserklärungen und zugehörige Meldung einlesen*/
        int anzahl = dbBundle.dbJoined.read_praesenzBuchungenProArbeitsplatz();

        /*++++++++++Übertragen in EclSBuchungenHV, Zeitslot berechnen++++++++++++*/
        ergebnisEclSBuchungenHV = new EclSBuchungenHV[anzahl];
        //		anzahl=100;
        for (int i = 0; i < anzahl; i++) {
            ergebnisEclSBuchungenHV[i] = new EclSBuchungenHV();
            ergebnisEclSBuchungenHV[i].eclMeldung = dbBundle.dbJoined.ergebnisMeldungPosition(i);
            ergebnisEclSBuchungenHV[i].eclWillenserklaerung = dbBundle.dbJoined.ergebnisWillenserklaerungPosition(i);
            ergebnisEclSBuchungenHV[i].kumulierteZeiteinheit = CaDatumZeit.zeitSlot(pStartZeit,
                    ergebnisEclSBuchungenHV[i].eclWillenserklaerung.veraenderungszeit, pMinutenProZeiteinheit);
            //			System.out.println("ergebnisEclSBuchungenHV[i].kumulierteZeiteinheit="+ergebnisEclSBuchungenHV[i].kumulierteZeiteinheit);
            //			System.out.println("pStartZeit="+pStartZeit+" ergebnisEclSBuchungenHV[i].eclWillenserklaerung.veraenderungszeit="+ergebnisEclSBuchungenHV[i].eclWillenserklaerung.veraenderungszeit+" pMinutenProZeiteinheit="+pMinutenProZeiteinheit);

        }

        /*+++++++++++Kumulierte Werte je Slot berechnen+++++++++++++*/
        int maxSlot = 0; //Nummer des höchsten verwendeten Slots

        int letzterArbeitsplatz = -1;
        int letzterSlot = -1;
        int[] summen = null;
        ergebnisArbeitsplatzKumuliert = new LinkedList<>();

        for (int i = 0; i < anzahl; i++) {

            int aktuellerArbeitsplatz = ergebnisEclSBuchungenHV[i].eclWillenserklaerung.arbeitsplatz;
            if (aktuellerArbeitsplatz != letzterArbeitsplatz) {//Arbeitsplatzwechsel
                if (letzterArbeitsplatz != -1) {
                    ergebnisArbeitsplatzKumuliert.add(summen);
                }
                summen = new int[6];
                for (int i1 = 0; i1 < 6; i1++) {
                    summen[i1] = 0;
                }
                summen[1] = aktuellerArbeitsplatz;
                letzterArbeitsplatz = aktuellerArbeitsplatz;
                letzterSlot = -1;
            }

            int aktuellerSlot = ergebnisEclSBuchungenHV[i].kumulierteZeiteinheit;
            if (aktuellerSlot != letzterSlot) {//Slotwechsel innerhalb eines Arbeitsplatzes
                if (letzterSlot != -1) {
                    ergebnisArbeitsplatzKumuliert.add(summen);
                }
                summen = new int[6];
                for (int i1 = 0; i1 < 6; i1++) {
                    summen[i1] = 0;
                }
                summen[1] = aktuellerArbeitsplatz;
                summen[0] = aktuellerSlot;
                letzterSlot = aktuellerSlot;
                if (letzterSlot > maxSlot) {
                    maxSlot = letzterSlot;
                }
            }

            summen[2]++; //Buchungen insgesamt
            switch (ergebnisEclSBuchungenHV[i].eclWillenserklaerung.willenserklaerung) {
            case KonstWillenserklaerung.erstzugang:
                summen[3]++;
                break;
            case KonstWillenserklaerung.abgang:
                summen[5]++;
                break;
            case KonstWillenserklaerung.wiederzugang:
                summen[4]++;
                break;
            }

        }

        if (letzterArbeitsplatz != -1 && letzterSlot != -1) {
            ergebnisArbeitsplatzKumuliert.add(summen);
        }

        /*+++++++++Kumulieren zu Arbeitsplatzgruppen+++++++++++++*/
        PblGeraeteStandort pblGeraeteStandort = new PblGeraeteStandort(dbBundle);

        int anzahlStandorte = pblGeraeteStandort.liefereStandortAnzahl();

        ergebnisJeStandortBuchungenGesamt = new int[maxSlot + 1][anzahlStandorte];
        ergebnisJeStandortBuchungenZugaenge = new int[maxSlot + 1][anzahlStandorte];
        ergebnisJeStandortBuchungenWiederzugaenge = new int[maxSlot + 1][anzahlStandorte];
        ergebnisJeStandortBuchungenAbgaenge = new int[maxSlot + 1][anzahlStandorte];

        ergebnisSummeJeStandortBuchungenGesamt = new int[anzahlStandorte];
        ergebnisSummeJeStandortBuchungenZugaenge = new int[anzahlStandorte];
        ergebnisSummeJeStandortBuchungenWiederzugaenge = new int[anzahlStandorte];
        ergebnisSummeJeStandortBuchungenAbgaenge = new int[anzahlStandorte];

        for (int i1 = 0; i1 < anzahlStandorte; i1++) {
            for (int i = 0; i < maxSlot + 1; i++) {
                ergebnisJeStandortBuchungenGesamt[i][i1] = 0;
                ergebnisJeStandortBuchungenZugaenge[i][i1] = 0;
                ergebnisJeStandortBuchungenWiederzugaenge[i][i1] = 0;
                ergebnisJeStandortBuchungenAbgaenge[i][i1] = 0;
            }
            ergebnisSummeJeStandortBuchungenGesamt[i1] = 0;
            ergebnisSummeJeStandortBuchungenZugaenge[i1] = 0;
            ergebnisSummeJeStandortBuchungenWiederzugaenge[i1] = 0;
            ergebnisSummeJeStandortBuchungenAbgaenge[i1] = 0;
        }

        for (int i = 0; i < ergebnisAnzArbeitsplatzKumuliert(); i++) {
            int[] hErgebnisKumuliert = ergebnisPositionArbeitsplatzKumuliert(i);
            int lStandort = pblGeraeteStandort.liefereStandortZuGeraet(hErgebnisKumuliert[1]);
            int lZeitslot = hErgebnisKumuliert[0];

            //			System.out.println("i="+i+" lStandort="+lStandort+" lZeitslot="+lZeitslot);
            ergebnisJeStandortBuchungenGesamt[lZeitslot][lStandort] += hErgebnisKumuliert[2];
            ergebnisSummeJeStandortBuchungenGesamt[lStandort] += hErgebnisKumuliert[2];

            ergebnisJeStandortBuchungenZugaenge[lZeitslot][lStandort] += hErgebnisKumuliert[3];
            ergebnisSummeJeStandortBuchungenZugaenge[lStandort] += hErgebnisKumuliert[3];

            ergebnisJeStandortBuchungenWiederzugaenge[lZeitslot][lStandort] += hErgebnisKumuliert[4];
            ergebnisSummeJeStandortBuchungenWiederzugaenge[lStandort] += hErgebnisKumuliert[4];

            ergebnisJeStandortBuchungenAbgaenge[lZeitslot][lStandort] += hErgebnisKumuliert[5];
            ergebnisSummeJeStandortBuchungenAbgaenge[lStandort] += hErgebnisKumuliert[5];
        }

        if (pSchreibenInDatei) {
            /*+++++Datei erzeugen+++++++*/

            /*Gesamtsummen insgesamt, sowie Anzahl Buchungen je Standort*/
            int summeBuchungenGesamt = 0;
            int summeBuchungenZugaenge = 0;
            int summeBuchungenWiederzugaenge = 0;
            int summeBuchungenAbgaenge = 0;
            for (int i = 0; i < anzahlStandorte; i++) {
                summeBuchungenGesamt += ergebnisSummeJeStandortBuchungenGesamt[i];
                summeBuchungenZugaenge += ergebnisSummeJeStandortBuchungenZugaenge[i];
                summeBuchungenWiederzugaenge += ergebnisSummeJeStandortBuchungenWiederzugaenge[i];
                summeBuchungenAbgaenge += ergebnisSummeJeStandortBuchungenAbgaenge[i];
            }
            CaDateiWrite caDateiSummen = new CaDateiWrite();
            caDateiSummen.dateiart = ".csv";
            caDateiSummen.trennzeichen = ';';
            caDateiSummen.oeffne(dbBundle, "praesenzBuchungenProArbeitsplatz_Summen");
            caDateiSummen.ausgabe("Gesamt Buchungen");
            caDateiSummen.ausgabe(Integer.toString(summeBuchungenGesamt));
            caDateiSummen.newline();
            caDateiSummen.ausgabe("Gesamt Zugänge");
            caDateiSummen.ausgabe(Integer.toString(summeBuchungenZugaenge));
            caDateiSummen.newline();
            caDateiSummen.ausgabe("Gesamt Wiederzugänge");
            caDateiSummen.ausgabe(Integer.toString(summeBuchungenWiederzugaenge));
            caDateiSummen.newline();
            caDateiSummen.ausgabe("Gesamt Abgänge");
            caDateiSummen.ausgabe(Integer.toString(summeBuchungenAbgaenge));
            caDateiSummen.newline();

            caDateiSummen.ausgabe("je Standort");
            caDateiSummen.newline();
            for (int i = 0; i < anzahlStandorte; i++) {
                caDateiSummen.ausgabe(Integer.toString(i));
                caDateiSummen.ausgabe(pblGeraeteStandort.liefereStandortBezeichnung(i));
                caDateiSummen.newline();
                caDateiSummen.ausgabe("Buchungen insgesamt");
                caDateiSummen.ausgabe(Integer.toString(ergebnisSummeJeStandortBuchungenGesamt[i]));
                caDateiSummen.newline();
                caDateiSummen.ausgabe("Zugänge");
                caDateiSummen.ausgabe(Integer.toString(ergebnisSummeJeStandortBuchungenZugaenge[i]));
                caDateiSummen.newline();
                caDateiSummen.ausgabe("Wiederzugänge");
                caDateiSummen.ausgabe(Integer.toString(ergebnisSummeJeStandortBuchungenWiederzugaenge[i]));
                caDateiSummen.newline();
                caDateiSummen.ausgabe("Abgänge");
                caDateiSummen.ausgabe(Integer.toString(ergebnisSummeJeStandortBuchungenAbgaenge[i]));
                caDateiSummen.newline();

            }

            caDateiSummen.schliessen();

            /*Verlauf je Zeitslot je Standort.
             * 4 Dateien:
             * praesenzBuchungenProArbeitsplatz_VerlaufBuchungen
             * praesenzBuchungenProArbeitsplatz_VerlaufZugaenge
             * praesenzBuchungenProArbeitsplatz_VerlaufWiederzugaenge
             * praesenzBuchungenProArbeitsplatz_VerlaufAbgaenge
             * 
             * Jeweils:
             * > nach unten: Buchungen je Slot
             * > nach rechts: Buchungen je Standort
             */

            CaDateiWrite caDateiBuchungen = new CaDateiWrite();
            caDateiBuchungen.dateiart = ".csv";
            caDateiBuchungen.trennzeichen = ';';
            caDateiBuchungen.oeffne(dbBundle, "praesenzBuchungenVerlaufJeStandort_Gesamt");
            CaDateiWrite caDateiZugaenge = new CaDateiWrite();
            caDateiZugaenge.dateiart = ".csv";
            caDateiZugaenge.trennzeichen = ';';
            caDateiZugaenge.oeffne(dbBundle, "praesenzBuchungenVerlaufJeStandort_Zugaenge");
            CaDateiWrite caDateiWiederzugaenge = new CaDateiWrite();
            caDateiWiederzugaenge.dateiart = ".csv";
            caDateiWiederzugaenge.trennzeichen = ';';
            caDateiWiederzugaenge.oeffne(dbBundle, "praesenzBuchungenVerlaufJeStandort_Wiederzugaenge");
            CaDateiWrite caDateiAbgaenge = new CaDateiWrite();
            caDateiAbgaenge.dateiart = ".csv";
            caDateiAbgaenge.trennzeichen = ';';
            caDateiAbgaenge.oeffne(dbBundle, "praesenzBuchungenVerlaufJeStandort_Abgaenge");

            caDateiBuchungen.ausgabe("");
            caDateiZugaenge.ausgabe("");
            caDateiWiederzugaenge.ausgabe("");
            caDateiAbgaenge.ausgabe("");
            for (int i = 0; i < anzahlStandorte; i++) {
                caDateiBuchungen.ausgabe(pblGeraeteStandort.liefereStandortBezeichnung(i));
                caDateiZugaenge.ausgabe(pblGeraeteStandort.liefereStandortBezeichnung(i));
                caDateiWiederzugaenge.ausgabe(pblGeraeteStandort.liefereStandortBezeichnung(i));
                caDateiAbgaenge.ausgabe(pblGeraeteStandort.liefereStandortBezeichnung(i));
            }
            caDateiBuchungen.newline();
            caDateiZugaenge.newline();
            caDateiWiederzugaenge.newline();
            caDateiAbgaenge.newline();

            for (int i = 0; i < maxSlot + 1; i++) {
                //				System.out.println("pStartZeit="+pStartZeit+" slotZeit="+CaDatumZeit.addMinuten(pStartZeit, pMinutenProZeiteinheit*i));
                String zeitSlot = CaDatumZeit.addMinuten(pStartZeit, pMinutenProZeiteinheit * i).substring(11, 16);
                caDateiBuchungen.ausgabe(zeitSlot);
                caDateiZugaenge.ausgabe(zeitSlot);
                caDateiWiederzugaenge.ausgabe(zeitSlot);
                caDateiAbgaenge.ausgabe(zeitSlot);

                for (int i1 = 0; i1 < anzahlStandorte; i1++) {
                    caDateiBuchungen.ausgabe(Integer.toString(ergebnisJeStandortBuchungenGesamt[i][i1]));
                    caDateiZugaenge.ausgabe(Integer.toString(ergebnisJeStandortBuchungenZugaenge[i][i1]));
                    caDateiWiederzugaenge.ausgabe(Integer.toString(ergebnisJeStandortBuchungenWiederzugaenge[i][i1]));
                    caDateiAbgaenge.ausgabe(Integer.toString(ergebnisJeStandortBuchungenAbgaenge[i][i1]));
                }
                caDateiBuchungen.newline();
                caDateiZugaenge.newline();
                caDateiWiederzugaenge.newline();
                caDateiAbgaenge.newline();
            }

            caDateiBuchungen.schliessen();
            caDateiZugaenge.schliessen();
            caDateiWiederzugaenge.schliessen();
            caDateiAbgaenge.schliessen();

        }
    }
}
