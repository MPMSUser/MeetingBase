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

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

/**Zum Ermitteln der Summen der eingegangenen Willenserklärungen auf den unterschiedlichen Wegen*/
public class RepAnmeldestelleSummeErklaerungenWeg {

    /**[weg][art][sammelkarte]*/
    private int[][][] anzahlErklaerungen = null;

    /**Max-Werte: höchste Zahl. D.h. länge Array= +1, da 0 (für sonstige) noch dazukommt*/
    private int maxWeg = 8;
    private int maxArt = 8;

    /**Array für Zuordnung von Sammelkartenident in das Array anzahlErklaerungen*/
    private int[] sammelkartenZuordnung = null;
    /**Enthält die zur jeweiligen Sammelkarte gehörende EclMeldung zwecks späterer Ausgabe
     * z.B. von Sammelkartenbezeichnung
     */
    private EclMeldung[] sammelkartenArray = null;

    private DbBundle dbBundle = null;

    /**Returnwert: Dateiname, in dem der Export liegt*/
    public String rcExportDateiname = "";

    /**pDbBundle muß geöffnet sein*/
    public RepAnmeldestelleSummeErklaerungenWeg(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Erstellt die Statistik und legt das Ergebnis in einer Export Datei ab,
     * die in rcExportDateiname zu finden ist.
     * pVonDatum, pBisDatum 
     * > in der Form TT.MM.JJJJ HH:MM:SS. 
     * > Kann leer sein, dann von beginn bzw. bis Ende
     * > kann ohne Uhrzeit sein*/
    public void ermittleAnzahlErklaerungen(String pVonDatum, String pBisDatum) {

        /*Übergabeparameter aufbereiten*/
        if (pVonDatum.length() == 10) {
            pVonDatum = pVonDatum + " 00:00:00";
        }
        if (pBisDatum.length() == 10) {
            pBisDatum = pBisDatum + " 24:00:00";
        }
        String lVonDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pVonDatum);
        String lBisDatum = CaDatumZeit.DatumZeitStringFuerDatenbank(pBisDatum);

        /*Sammelkarten für Sammelkartenarray einlesen/aufbereiten*/
        dbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = 0;
        if (dbBundle.dbMeldungen.meldungenArray != null) {
            anzSammelkarten = dbBundle.dbMeldungen.meldungenArray.length;
        }
        int laengeSammelkartenArray = 1 + anzSammelkarten; /*Gesammtlänge des Arrays*/
        sammelkartenZuordnung = new int[laengeSammelkartenArray];
        sammelkartenArray = new EclMeldung[laengeSammelkartenArray];

        sammelkartenZuordnung[0] = 0;
        sammelkartenArray[0] = null;
        if (anzSammelkarten > 0) {
            for (int i = 0; i < anzSammelkarten; i++) {
                sammelkartenZuordnung[i + 1] = dbBundle.dbMeldungen.meldungenArray[i].meldungsIdent;
                sammelkartenArray[i + 1] = dbBundle.dbMeldungen.meldungenArray[i];
            }
        }

        /*Ergebnis-Array anzahLErklaerungen initialisieren*/
        anzahlErklaerungen = new int[maxWeg + 1][maxArt + 1][laengeSammelkartenArray];
        for (int i = 0; i <= maxWeg; i++) {
            for (int i1 = 0; i1 <= maxArt; i1++) {
                for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
                    anzahlErklaerungen[i][i1][i2] = 0;
                }
            }
        }

        /*Willenserklärungen einlesen*/
        dbBundle.dbWillenserklaerung.leseAbBisZeitstempel(lVonDatum, lBisDatum);
        //		System.out.println("lVonDatum="+lVonDatum+" lBisDatum="+lBisDatum);
        int anzWillenserklaerungen = dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
        //		System.out.println("anzWillenserklaerungen="+anzWillenserklaerungen);
        for (int i = 0; i < anzWillenserklaerungen; i++) {
            EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i];

            int lWeg = lWillenserklaerung.erteiltAufWeg;
            int lArt = lWillenserklaerung.willenserklaerung;
            int lSammelkarte = lWillenserklaerung.identMeldungZuSammelkarte;
            //			if (lSammelkarte!=0){
            //				System.out.println("i="+i+" lSammelkarte="+lSammelkarte);
            //			}

            anzahlErklaerungen[wegZuArrayPos(lWeg)][artArrayPos(lArt)][sammelIdentArrayPos(lSammelkarte)]++;
        }

        
        /*Anmeldung (ohen weitere Willenserklärung) berechnen*/
        for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
            for (int i = 0; i <= maxWeg; i++) {
                anzahlErklaerungen[i][2][i2]=anzahlErklaerungen[i][1][i2]-anzahlErklaerungen[i][3][i2]-
                        anzahlErklaerungen[i][4][i2]-anzahlErklaerungen[i][5][i2]-anzahlErklaerungen[i][6][i2]-
                        anzahlErklaerungen[i][7][i2]-anzahlErklaerungen[i][8][i2];
            }
            
        }
        
        /*Nun exportieren*/
        CaDateiWrite caDateiWrite = new CaDateiWrite();
        caDateiWrite.setzeFuerCSV();
        caDateiWrite.oeffne(dbBundle, "AnmeldestErklWeg");
        rcExportDateiname = caDateiWrite.dateiname;

        /*Mandantendaten*/
        String mandantenDaten = dbBundle.eclEmittent.mandant + " " + dbBundle.eclEmittent.hvDatum + " "
                + dbBundle.eclEmittent.bezeichnungLang;
        caDateiWrite.ausgabe(mandantenDaten);
        caDateiWrite.newline();
        caDateiWrite.newline();

        /*Wert für Portoabrechnung*/
        caDateiWrite.ausgabe("Portoabrechnung");
        caDateiWrite.newline();

        int portoSum = 0;
        for (int i1 = 0; i1 <= maxArt; i1++) { /*Einzelne Willenserklärungen*/
            if (i1 == 2 || i1==3 || i1 == 4 || i1 == 5 ||  i1==6 || i1 == 7 || i1==8) {
                for (int i = 0; i <= maxWeg; i++) {
                    if (i == 1) {
                        for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
                            portoSum += anzahlErklaerungen[i][i1][i2];
                        }
                    }
                }
            }
        }
        

        
        caDateiWrite.ausgabe("Vorschlag lt. untriger Statistik:");
        caDateiWrite.ausgabe(Integer.toString(portoSum));
        caDateiWrite.newline();

        caDateiWrite.ausgabe(
                "Wert berechnet sich aus dem Bereich Gesamtsumme, Post, Werte für Anmeldung (ohne weitere Willenserklärung), EK, Vollmacht, SRV, KIAV, Dauerv., Briefwahl");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Achtung: Wert anhand der restlichen Statistik plausibilisieren!");
        caDateiWrite.newline();
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Hinweise:");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("> Die Statistik enthält alle Willenserklärungen und Buchungen aus dem Bereich Anmeldung und Präsenz.");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("> Unter 'Sonstigen Erklärungen' werden z.B. Stornierungen/Widerrufe (hohe Anzahl z.B. bei Sammelkartenumbuchungen),");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Änderungen und Zu-/Abgangsbuchungen zusammengefaßt.");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("> Unter 'Sonstige Wege' werden z.B. Sammelkartenumbuchungen aufgeführt.");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("> Die Zahlen 'Anmeldung (ohne weitere Willenserklärung)' sind geschätzt und geben den Mindestwert an.");
        caDateiWrite.newline();
        caDateiWrite.ausgabe("Sie können sogar negativ sein (z.B. durch Massenumbuchungen von einer Sammelkarte auf eine andere)");
        caDateiWrite.newline();

//        /*Gesamtsumme ausgeben*/
//        caDateiWrite.ausgabe("Gesamtsumme");
        caDateiWrite.newline();
        caDateiWrite.newline();
        ausgabeUeberschriftWeg(caDateiWrite);

        caDateiWrite.ausgabe(ueberschriftZuArtOffset(-1)); /*Summe*/
        for (int i = 0; i <= maxWeg; i++) {
            int sum = 0;
            for (int i1 = 0; i1 <= maxArt; i1++) {
                for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
                    sum += anzahlErklaerungen[i][i1][i2];
                }
            }
            caDateiWrite.ausgabe(Integer.toString(sum));
        }
        caDateiWrite.newline();

        for (int i1 = 0; i1 <= maxArt; i1++) { /*Einzelne Willenserklärungen*/
            caDateiWrite.ausgabe(ueberschriftZuArtOffset(i1));
            for (int i = 0; i <= maxWeg; i++) {
                int sum = 0;
                for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
                    sum += anzahlErklaerungen[i][i1][i2];
                }
                caDateiWrite.ausgabe(Integer.toString(sum));
            }
            caDateiWrite.newline();
        }
        caDateiWrite.newline();

        /*Alle Sammelkarten ausgeben*/
        for (int i2 = 0; i2 < laengeSammelkartenArray; i2++) {
            String ueberschrift = "";
            if (i2 == 0) {
                ueberschrift = "Einzelerklärungen";
            } else {
                ueberschrift = "SammelIdent " + sammelkartenZuordnung[i2] + " " + sammelkartenArray[i2].zutrittsIdent
                        + " " + sammelkartenArray[i2].name;
            }
            caDateiWrite.ausgabe(ueberschrift);
            caDateiWrite.newline();

            /*Summe für diese Sammelkarte ausgeben*/
            caDateiWrite.ausgabe(ueberschriftZuArtOffset(-1)); /*Summe*/
            for (int i = 0; i <= maxWeg; i++) {
                int sum = 0;
                for (int i1 = 0; i1 <= maxArt; i1++) {
                    sum += anzahlErklaerungen[i][i1][i2];
                }
                caDateiWrite.ausgabe(Integer.toString(sum));
            }
            caDateiWrite.newline();
            for (int i1 = 0; i1 <= maxArt; i1++) { /*Einzelne Willenserklärungen*/
                caDateiWrite.ausgabe(ueberschriftZuArtOffset(i1));
                for (int i = 0; i <= maxWeg; i++) {
                    int sum = 0;
                    sum = anzahlErklaerungen[i][i1][i2];
                    caDateiWrite.ausgabe(Integer.toString(sum));
                }
                caDateiWrite.newline();
            }

            caDateiWrite.newline();

        }

        caDateiWrite.schliessen();

    }

    private void ausgabeUeberschriftWeg(CaDateiWrite caDateiWrite) {
        caDateiWrite.ausgabe("");
        for (int i = 0; i < maxWeg + 1; i++) {
            caDateiWrite.ausgabe(ueberschriftZuWegOffset(i));
        }
        caDateiWrite.newline();
    }

    /**Bildet KonstWillenserklaerungWeg auf Array-Position ab*/
    private int wegZuArrayPos(int pWeg) {
        int arrayPos = 0; // Default-Weg

        switch (pWeg) {
        case KonstWillenserklaerungWeg.papierPost:
            arrayPos = 1;
            break;
        case KonstWillenserklaerungWeg.fax:
            arrayPos = 2;
            break;
        case KonstWillenserklaerungWeg.eMail:
            arrayPos = 3;
            break;
        case KonstWillenserklaerungWeg.portal:
            arrayPos = 4;
            break;
        case 22:
            arrayPos = 5;
            break;
        case KonstWillenserklaerungWeg.anmeldestelleManuell:
            arrayPos = 6;
            break;
        case KonstWillenserklaerungWeg.schnittstelleExtern:
            arrayPos = 7;
            break;
        case KonstWillenserklaerungWeg.counterAufHV:
            arrayPos = 8;
            break;
        /*TODO Konsolidierung: Willenserkläerungsweg für Portal und App ist derzeit gleich - beide 21*/
        //		case KonstWillenserklaerungWeg.app:arrayPos=5;break;
        }
        return arrayPos;
    }

    private String ueberschriftZuWegOffset(int pOffset) {
        switch (pOffset) {
        case 0:
            return "Sonstige Wege";
        case 1:
            return "Post";
        case 2:
            return "Fax";
        case 3:
            return "EMail";
        case 4:
            return "Portal/App";
        case 5:
            return ""; //App
        case 6:
            return "Anmeldestelle Eingabe";
        case 7:
            return "Einspielung Schnittstelle";
        case 8:
            return "auf HV";
        }
        return "";
    }

    /**Bildet KonstWillenserklaerung auf Array-Position ab*/
    private int artArrayPos(int pArt) {
        int arrayPos = 0; // Default-Art

        switch (pArt) {
        case KonstWillenserklaerung.anmeldungAusAktienregister:
            arrayPos = 1;
            break;
            
        /*2 = nur reine Anmeldung. 
         * Wird rechnerisch ermittelt, aus Gesamtanmeldung - Restliche Willenserklärungen (Briefwahl etc)
         */
            
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung:
            arrayPos = 3;
            break;
        case KonstWillenserklaerung.vollmachtAnDritte:
            arrayPos = 4;
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
            arrayPos = 5;
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            arrayPos = 6;
            break;
        case KonstWillenserklaerung.dauervollmachtAnKIAV:
            arrayPos = 7;
            break;
        case KonstWillenserklaerung.briefwahl:
            arrayPos = 8;
            break;
        }
        return arrayPos;
    }

    private String ueberschriftZuArtOffset(int pOffset) {
        switch (pOffset) {
        case -1:
            return "Summe";
        case 0:
            return "Sonstige Erklärung";
        case 1:
            return "Anmeldung (alle)";
        case 2:
            return "Anmeldung (ohne weitere Willenserklärung)";
        case 3:
            return "Eintrittskarte";
        case 4:
            return "Vollmacht Dritte";
        case 5:
            return "Vollmacht/Weisung SRV";
        case 6:
            return "Vollmacht KIAV";
        case 7:
            return "Dauervollmacht";
        case 8:
            return "Briefwahl";
        }
        return "";
    }

    /**Bildet SammelkartenIdent auf Array-Position ab*/
    private int sammelIdentArrayPos(int pSammelIdent) {
        int arrayPos = 0; /*Default*/

        for (int i = 1; i < sammelkartenZuordnung.length; i++) {
            if (pSammelIdent == sammelkartenZuordnung[i]) {
                arrayPos = i;
            }
        }

        return arrayPos;
    }

}
