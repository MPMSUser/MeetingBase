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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;

public class BlMonitorVipKZ {

    private DbBundle lDbBundle = null;

    public final static int A_ALLEGEKENNZEICHNETEN = 1;
    public final static int A_NUREXPLIZITGEKENNZEICHNETE = 2;
    public final static int A_NURAUTOMATISCHGEKENNZEICHNETE = 3;

    public final static int B_ALLEEINAUSGEBLENDETEN = 1;
    public final static int B_NURNICHTAUSGEBLENDETE = 2;
    public final static int B_NURAUSGEBLENDETE = 3;

    public final static int C_ALLEMELDUNGUNG = 1;
    public final static int C_TEILNEHMER = 2;
    public final static int C_GAESTE = 3;

    public final static int D_ALLE = 1;
    public final static int D_ANWESEND = 2;
    public final static int D_ABWESEND = 3;
    public final static int D_WARANWESEND = 4;

    /*Anzahl selektierte VIP-Kürzel, mit der jeweiligen Bedingung.
     * Hinweis: 0 enthält nur 0 oder 1 - da diese Anzahlen ja später für Buffer etc. gebraucht werden*/
    private int[] anzVipKZBedingung = { 0, 0, 0, 0, 0 };
    private int anzVipKZBedingung0 = 0;/*Tatsächliche Kürzel in Meldung mit Bedingung=0;Hinweis: 0 enthält auch die mit Bedingung 1 bis 4, da diese ja auch direkt zuordenbar sind*/
    private String[] selektierteKuerzel;

    private class MerkerFuerReadarray {
        public int bedingung = 0;
        public String kuerzel = ""; /*Bei bedingung==0 nicht verwendet*/
        public int anzahlSaetze = 0;

    }

    private MerkerFuerReadarray[] merkerFuerReadarray_array;
    private int gebufferterOffset = -1; /*Merker für getMeldungZusGesamtOffset - damit nicht mehrfach Datenbankoperationen gemacht werden müssen, wenn
                                        verschiedene Felder von clMeldung eingelesen werden*/
    private EclMeldung bufferMeldung = null; /*Meldung in gebufferterOffset*/
    private String bufferKuerzel = ""; /*BipKZKürzel zu bufferMeldung*/

    public BlMonitorVipKZ(DbBundle dbBundle) {
        super();
        lDbBundle = dbBundle;

    }

    /**
     * 
     * @param SelektionA
     * @param SelektionB
     * @param SelektionC
     * @param SelektionD
     */
    public void readVIPMeldungen(int selektionA, int selektionB, int selektionC, int selektionD) {
        System.out.println("SelektionA=" + selektionA);
        System.out.println("SelektionB=" + selektionB);
        System.out.println("SelektionC=" + selektionC);
        System.out.println("SelektionD=" + selektionD);

        /*TODO _VIPKZ Selektierte VipKZ Analysieren*/
        System.out.println(
                "*****************************************************************************************************************");
        /*Ermitteln in anzVipKZBedingung: wieviele VipKZ gibt es mit der jeweiligen Bedingung?
         * Späterer Verwendungszweck: für [0] => Anzahl der Kürzel in Select.
         * 							  für Rest => Anzahl benötigte DB-Buffer
         */
        int i, offset, anzkuerzel;
        for (i = 0; i <= 4; i++) {
            anzVipKZBedingung[i] = 0;
        }
        anzVipKZBedingung0 = 0;

        gebufferterOffset = -1; /*Erst mal nichts im Buffer - vor allem auch beim Wiederholen des Einlesens*/

        /*Durchforsten: überprüfen, wieviele VIPKZ mit den einzelnen Bedingungen tatsächlich selektiert sind.
         * Hier wird auch noch bedingung=0 im Array aufaddiert, auch wenn dies später auf 1 gesetzt wird
         * (denn die Array-Anzahl dient als Grundlage für die benötigten Buffer, und für bedingung==0 wird 
         * immer nur 1 Buffer benötigt)
         */
        for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
            if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                anzVipKZBedingung[lDbBundle.dbVipKZ.vipKZarray[i].bedingung]++;
                if (lDbBundle.dbVipKZ.vipKZarray[i].bedingung != 0) {
                    anzVipKZBedingung[0]++;
                }
            }

        }
        anzVipKZBedingung0 = anzVipKZBedingung[0];
        if (anzVipKZBedingung0 > 0) {
            anzVipKZBedingung[0] = 1;
        }

        for (i = 0; i <= 4; i++) {
            System.out.println("anzVipKZBedingung i= " + i + " anz=" + anzVipKZBedingung[i]);
        }
        System.out.println("anzVip0 = " + anzVipKZBedingung0);

        /*In anz die benötigten read-Buffer ermitteln*/
        int anz = 0;
        if (selektionA != BlMonitorVipKZ.A_ALLEGEKENNZEICHNETEN
                && selektionA != BlMonitorVipKZ.A_NUREXPLIZITGEKENNZEICHNETE) { /*Dann in jedem Fall keine explizit gekennzeichneten*/
            anzVipKZBedingung[0] = 0;
            anzVipKZBedingung0 = 0;
        } else {
            anz = 1;
        }
        if (selektionA == BlMonitorVipKZ.A_ALLEGEKENNZEICHNETEN
                || selektionA == BlMonitorVipKZ.A_NURAUTOMATISCHGEKENNZEICHNETE) {
            anz = anzVipKZBedingung[0] + anzVipKZBedingung[1] + anzVipKZBedingung[2] + anzVipKZBedingung[3]
                    + anzVipKZBedingung[4];
        }

        System.out.println("Anz merker=" + anz);
        merkerFuerReadarray_array = new MerkerFuerReadarray[anz];
        /*Verschiedene Buffer vorhalten*/
        lDbBundle.dbMeldungen.readarray_SucheVipKZMonitor_initbasis(anz);

        /*Für direkt zugeordnete VIP-Kürzel: Array mit zu berücksichtigtenden Kürzeln erzeugen*/
        anzkuerzel = 0;
        if (anzVipKZBedingung[0] != 0) {
            selektierteKuerzel = new String[anzVipKZBedingung0];
            for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
                if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                    selektierteKuerzel[anzkuerzel] = lDbBundle.dbVipKZ.vipKZarray[i].kuerzel;
                    anzkuerzel++;
                }

            }
        }

        offset = -1;

        if (anzkuerzel != 0 && (selektionA == BlMonitorVipKZ.A_ALLEGEKENNZEICHNETEN
                || selektionA == BlMonitorVipKZ.A_NUREXPLIZITGEKENNZEICHNETE)) {
            /*Alle Meldungen suchen (gemäß Sektion A, B, C, D), denen explizit selektierte VipKZ zugeordnet wurden = Array [0]*/
            offset++;
            merkerFuerReadarray_array[offset] = new MerkerFuerReadarray();
            merkerFuerReadarray_array[offset].anzahlSaetze = lDbBundle.dbMeldungen
                    .readarray_SucheVipKZMonitorKuerzel_init(offset, selektierteKuerzel, selektionB, selektionC,
                            selektionD);
            merkerFuerReadarray_array[offset].bedingung = 0;
            System.out.println("Anzahl direkt gekennzeichneter=" + merkerFuerReadarray_array[offset].anzahlSaetze);
        }

        if (selektionA == BlMonitorVipKZ.A_ALLEGEKENNZEICHNETEN
                || selektionA == BlMonitorVipKZ.A_NURAUTOMATISCHGEKENNZEICHNETE) {

            /*Alle VipKZ mit Bedinung 1 "größer als Aktienzahl" durchgehen und zugehörige Meldungen suchen
             * Dabei VIP-Ausschluesse nicht selektieren*/
            for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
                if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                    if (lDbBundle.dbVipKZ.vipKZarray[i].bedingung == 1) {
                        offset++;
                        merkerFuerReadarray_array[offset] = new MerkerFuerReadarray();
                        merkerFuerReadarray_array[offset].anzahlSaetze = lDbBundle.dbMeldungen
                                .readarray_SucheVipKZMonitorAktien_init(offset,
                                        lDbBundle.dbVipKZ.vipKZarray[i].aktienzahl,
                                        lDbBundle.dbVipKZ.vipKZarray[i].kuerzel, selektionB, selektionC, selektionD);
                        merkerFuerReadarray_array[offset].kuerzel = lDbBundle.dbVipKZ.vipKZarray[i].kuerzel;
                        merkerFuerReadarray_array[offset].bedingung = 1;
                    }
                }
            }

            /*Alle VipKZ mit Bedinung 2 "Stimmausschluß" durchgehen und zugehörige Meldungen suchen
             * Dabei VIP-Ausschluesse nicht selektieren*/
            for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
                if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                    if (lDbBundle.dbVipKZ.vipKZarray[i].bedingung == 2) {
                        offset++;
                        merkerFuerReadarray_array[offset] = new MerkerFuerReadarray();
                        merkerFuerReadarray_array[offset].anzahlSaetze = lDbBundle.dbMeldungen
                                .readarray_SucheVipKZMonitorStimmausschluss_init(offset,
                                        lDbBundle.dbVipKZ.vipKZarray[i].kuerzel, selektionB, selektionC, selektionD);
                        merkerFuerReadarray_array[offset].kuerzel = lDbBundle.dbVipKZ.vipKZarray[i].kuerzel;
                        merkerFuerReadarray_array[offset].bedingung = 2;
                    }
                }
            }

            /*Alle VipKZ mit Bedinung 3 "Vorname, Name" durchgehen und zugehörige Meldungen suchen
             * Dabei VIP-Ausschluesse nicht selektieren*/
            for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
                if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                    if (lDbBundle.dbVipKZ.vipKZarray[i].bedingung == 3) {
                        System.out.println("Bedingung =3 Kuerzel =" + lDbBundle.dbVipKZ.vipKZarray[i].kuerzel);
                        offset++;
                        merkerFuerReadarray_array[offset] = new MerkerFuerReadarray();
                        merkerFuerReadarray_array[offset].anzahlSaetze = lDbBundle.dbMeldungen
                                .readarray_SucheVipKZMonitorNameVorname_init(offset,
                                        lDbBundle.dbVipKZ.vipKZarray[i].name, lDbBundle.dbVipKZ.vipKZarray[i].vorname,
                                        lDbBundle.dbVipKZ.vipKZarray[i].kuerzel, selektionB, selektionC, selektionD);
                        merkerFuerReadarray_array[offset].kuerzel = lDbBundle.dbVipKZ.vipKZarray[i].kuerzel;
                        merkerFuerReadarray_array[offset].bedingung = 3;
                    }
                }
            }

            /*Alle VipKZ mit Bedinung 4 "Listenkarten/Sammelkarten" durchgehen und zugehörige Meldungen suchen
             * Dabei VIP-Ausschluesse nicht selektieren*/
            for (i = 0; i < lDbBundle.dbVipKZ.vipKZarray.length; i++) {
                if (lDbBundle.dbVipKZ.vipKZarray[i].selektiert == 1) {
                    if (lDbBundle.dbVipKZ.vipKZarray[i].bedingung == 4) {
                        offset++;
                        merkerFuerReadarray_array[offset] = new MerkerFuerReadarray();
                        merkerFuerReadarray_array[offset].anzahlSaetze = lDbBundle.dbMeldungen
                                .readarray_SucheVipKZMonitorVirtuelleKarten_init(offset,
                                        lDbBundle.dbVipKZ.vipKZarray[i].kuerzel, selektionB, selektionC, selektionD);
                        merkerFuerReadarray_array[offset].kuerzel = lDbBundle.dbVipKZ.vipKZarray[i].kuerzel;
                        merkerFuerReadarray_array[offset].bedingung = 4;
                    }
                }
            }
        }

    }

    /** Liefert die Anzahl der Meldungen insgesamt, die gemäß aktueller Selektion selektiert wurden*/
    public int anzahlVipMeldungen() {
        int anzahl = 0;
        int i;

        for (i = 0; i < merkerFuerReadarray_array.length; i++) {
            anzahl += merkerFuerReadarray_array[i].anzahlSaetze;
            //			System.out.println("i anzahl[i]"+i+" "+merkerFuerReadarray_array[i].anzahlSaetze);
        }
        //		System.out.println("Anzahl VIP Meldungen="+anzahl);
        return (anzahl);
    }

    /** Liefert für das Gesamt-Offset (d.h. für alle Selektionsläufe nach den verschiedenen Kürzeln zusammengesetzt)
     * die Meldung. Hinweis: in clMeldung.meldungVipKZarray steht nur ein Eintrag - und zwar das Kürzel des VIP-KZ,
     * das aus dem entsprechenden Selektionslauf ist.
     * @param offset
     * @param clMeldung
     * @return
     */
    public int getMeldungZuGesamtOffset(int offset, EclMeldung clMeldung) {
        int buffer = -1;
        int i = 0;
        int bisherigeAnzahlSaetze = -1;
        int tatsaechlicherOffset = 0; /*Offset innerhalb des readarray_buffers*/

        if (gebufferterOffset != offset) { /*buffermeldung neu einlesen - ansonsten ist die gewünschte Meldung schon in bufferMeldung vorhanden*/

            tatsaechlicherOffset = offset;
            /*Ermitteln, in welchem readarray_ buffer der gewünschte Satz mit offset enthalten ist*/
            while (buffer == -1) {
                if (bisherigeAnzahlSaetze + merkerFuerReadarray_array[i].anzahlSaetze >= offset) {
                    buffer = i;
                    bufferKuerzel = merkerFuerReadarray_array[i].kuerzel;
                } else {
                    bisherigeAnzahlSaetze += merkerFuerReadarray_array[i].anzahlSaetze;
                    tatsaechlicherOffset -= merkerFuerReadarray_array[i].anzahlSaetze;
                    i++;
                }

            }
            bufferMeldung = new EclMeldung();
            int erg = lDbBundle.dbMeldungen.readarray_SucheVipKZMonitor_getoffset(buffer, tatsaechlicherOffset,
                    bufferMeldung);
            if (merkerFuerReadarray_array[buffer].bedingung != 0) {
                bufferMeldung.meldungVipKZarray[0].vipKZKuerzel = merkerFuerReadarray_array[buffer].kuerzel;
            }

        }

        /*buffermeldungs-Felder in clMeldung übertragen*/
        bufferMeldung.copyTo(clMeldung);
        clMeldung.meldungVipKZarray = new EclMeldungVipKZ[1];
        clMeldung.meldungVipKZarray[0] = new EclMeldungVipKZ();
        //		System.out.println("bufferkuerzel"+bufferKuerzel);
        clMeldung.meldungVipKZarray[0].vipKZKuerzel = bufferMeldung.meldungVipKZarray[0].vipKZKuerzel;
        return (1);
    }

}
