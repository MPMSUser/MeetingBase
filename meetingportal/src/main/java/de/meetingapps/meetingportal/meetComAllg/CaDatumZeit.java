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
package de.meetingapps.meetingportal.meetComAllg;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CaDatumZeit {

    private static int logDrucken = 3;

    /** Liefert aktuelle Systemzeit in einem Format zum Abspeichern in einem String-Format, z.B. für Datenbank;
     * Ergebnis ist in YYYY.MM.DD HH:MM:SS (LEN=19)
     */
    static public String DatumZeitStringFuerDatenbank() {
        String DatumZeit;

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        DatumZeit = "20" + DatumZeit.substring(6, 8) + "." + DatumZeit.substring(3, 5) + "." + DatumZeit.substring(0, 2)
                + " " + sdf.format(new Date());

        return (DatumZeit);
    }

    /**Liefert aus der übergebenen Zeit (in der Form TT.MM.JJJJ HH:MM:SS) einen String 
     * zum Speichern in der Datenbank (19 Stellen) in der Form JJJJ.MM.TT HH:MM:SS (LEN=19)
     */
    static public String DatumZeitStringFuerDatenbank(String inhalt) {
        String neudatum = "";

        if (!inhalt.isEmpty()) {
            neudatum = inhalt.substring(6, 10) + "." + inhalt.substring(3, 6) + inhalt.substring(0, 2) + " "
                    + inhalt.substring(11, 19);
        }
        return (neudatum);
    }

    /**Liefert aus der übergebenen Zeit (in der Form TT.MM.JJJJ) einen String 
     * zum Speichern in der Datenbank (10 Stellen) in der Form JJJJ.MM.TT
     */
    static public String DatumStringFuerDatenbank(String inhalt) {
        String neudatum = "";

        if (!inhalt.isEmpty()) {
            neudatum = inhalt.substring(6, 10) + "." + inhalt.substring(3, 6) + inhalt.substring(0, 2);
        }
        return (neudatum);
    }

    /** Liefert aktuelle Systemzeit in einem Format zum Aufnahmen in einen Dateinamen;
     * Ergebnis ist in YYYYMMDDHHMMSS 
     * 
     */
    static public String DatumZeitStringFuerDateiname() {

        String DatumZeit;
        String zeit = "";

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        zeit = sdf.format(new Date());
        DatumZeit = "20" + DatumZeit.substring(6, 8) + DatumZeit.substring(3, 5) + DatumZeit.substring(0, 2)
                + zeit.substring(0, 2) + zeit.substring(3, 5) + zeit.substring(6, 8);

        //		System.out.println(DatumZeit);

        return (DatumZeit);

    }

    /** Liefert aktuelle Systemzeit in einem Format zum Aufnahmen in einen Dateinamen;
     * Ergebnis ist in YYYY-MM-DD 
     * 
     */
    static public String DatumStringFuerRequest() {

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String datum = sdf.format(new Date());

        return (datum);

    }

    static public String DatumZeitStringFromLong(long pMillisek) {

        String datumZeit = "";
        String zeit = "";

        Date datum = new Date(pMillisek);
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        datumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        zeit = sdf.format(new Date(pMillisek));
        datumZeit = datumZeit + " " + zeit;
        //		"20"+datumZeit.substring(6, 8)+"."
        //				datumZeit.substring(3, 5)+
        //				datumZeit.substring(0,2)+
        //				zeit.substring(0, 2)+zeit.substring(3, 5)+
        //				zeit.substring(6, 8);

        return datumZeit;
    }

    /*
     * Input: 01.01.1970 00:00:00
     * 
     * 0 => falsches Format
     */
    static public long DatumZeitStringToLong(String zeitString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        Date d = null;
        long timestamp = 0;
        try {
            d = formatter.parse(zeitString);
            timestamp = d.getTime();
        } catch (ParseException e) {
            //            CaBug.drucke("001");
        }
        CaBug.druckeLog("Input=" + zeitString, logDrucken, 10);
        CaBug.druckeLog("Rückübersetzt=" + DatumZeitStringFromLong(timestamp), logDrucken, 10);
        return timestamp;
    }

    /*
     * Input: 1970-01-01
     */
    static public long DatumDbZeitStringToLong(String zeitString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

        Date d = null;
        long timestamp = 0;
        try {
            d = formatter.parse(zeitString);
            timestamp = d.getTime();
        } catch (ParseException e) {
            CaBug.drucke("001");
        }
        CaBug.druckeLog("Input=" + zeitString, logDrucken, 10);
        CaBug.druckeLog("Rückübersetzt=" + DatumZeitStringFromLong(timestamp), logDrucken, 10);
        return timestamp;
    }

    /** Liefert aktuelle Datum/Zeit in einem Format zum Anzeigen;
     * Ergebnis ist in DD.MM.YYYY HH:MM:SS 
     * 
     */
    @Deprecated
    static public String DatumZeitStringFuerAnzeige() {

        String DatumZeit;
        String zeit = "";

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        zeit = sdf.format(new Date());
        DatumZeit = DatumZeit.substring(0, 2) + "." + DatumZeit.substring(3, 5) + "." + "20" + DatumZeit.substring(6, 8)
                + zeit.substring(0, 2) + ":" + zeit.substring(3, 5) + ":" + zeit.substring(6, 8);
        return (DatumZeit);
    }

    /** Liefert aktuelle Datum/Zeit in einem Format zum Anzeigen;
     * Ergebnis ist in DD.MM.YYYY HH:MM:SS 
     * 
     * Achtung, Funktion ohne "neu" Fehlerhaft. Blank zwischen Datum und Uhrzeit fehlt dort. Deshalb DatumZeitStringFuerAnzeigeNeu verwenden!*/
    static public String DatumZeitStringFuerAnzeigeNeu() {

        String DatumZeit;
        String zeit = "";

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        zeit = sdf.format(new Date());
        DatumZeit = DatumZeit.substring(0, 2) + "." + DatumZeit.substring(3, 5) + "." + "20" + DatumZeit.substring(6, 8)
                + " " + zeit.substring(0, 2) + ":" + zeit.substring(3, 5) + ":" + zeit.substring(6, 8);
        return (DatumZeit);
    }

    static public String DatumStringFuerAnzeige() {

        String DatumZeit;

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        DatumZeit = DatumZeit.substring(0, 2) + "." + DatumZeit.substring(3, 5) + "." + "20"
                + DatumZeit.substring(6, 8);
        return (DatumZeit);
    }

    /**Verwendet aktuelles Systemdatum*/
    static public String DatumStringFuerAnzeigeMonatAusgeschrieben() {

        String DatumZeit;

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        int iMonat = Integer.parseInt(DatumZeit.substring(3, 5));
        String sMonat = "";
        switch (iMonat) {
        case 1:
            sMonat = "Januar";
            break;
        case 2:
            sMonat = "Februar";
            break;
        case 3:
            sMonat = "März";
            break;
        case 4:
            sMonat = "April";
            break;
        case 5:
            sMonat = "Mai";
            break;
        case 6:
            sMonat = "Juni";
            break;
        case 7:
            sMonat = "Juli";
            break;
        case 8:
            sMonat = "August";
            break;
        case 9:
            sMonat = "September";
            break;
        case 10:
            sMonat = "Oktober";
            break;
        case 11:
            sMonat = "November";
            break;
        case 12:
            sMonat = "Dezember";
            break;
        }

        DatumZeit = DatumZeit.substring(0, 2) + ". " + sMonat + " " + "20" + DatumZeit.substring(6, 8);
        return (DatumZeit);
    }

    /**Verwendet übergebenes Datum in der Form TT.MM.JJJJ*/
    static public String DatumStringFuerAnzeigeMonatAusgeschrieben(String pDatum) {

        String DatumZeit;

        int iMonat = Integer.parseInt(pDatum.substring(3, 5));
        String sMonat = "";
        switch (iMonat) {
        case 1:
            sMonat = "Januar";
            break;
        case 2:
            sMonat = "Februar";
            break;
        case 3:
            sMonat = "März";
            break;
        case 4:
            sMonat = "April";
            break;
        case 5:
            sMonat = "Mai";
            break;
        case 6:
            sMonat = "Juni";
            break;
        case 7:
            sMonat = "Juli";
            break;
        case 8:
            sMonat = "August";
            break;
        case 9:
            sMonat = "September";
            break;
        case 10:
            sMonat = "Oktober";
            break;
        case 11:
            sMonat = "November";
            break;
        case 12:
            sMonat = "Dezember";
            break;
        }

        DatumZeit = pDatum.substring(0, 2) + ". " + sMonat + " " + pDatum.substring(6);
        return (DatumZeit);
    }

    static public String StundenMinutenStringFuerAnzeige() {

        String DatumZeit;
        String zeit = "";

        Date datum = new Date();
        DateFormat df;
        df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMANY);
        DatumZeit = df.format(datum) + " ";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        zeit = sdf.format(new Date());
        DatumZeit = zeit.substring(0, 2) + ":" + zeit.substring(3, 5);
        return (DatumZeit);
    }

    /**Formatiert String in der Form jjjj?mm?tt HH:MM:SS in tt.mm.jjjj HH:MM:SS*/
    static public String DatumZeitStringFuerAnzeige(String inhalt) {

        String neudatum = "";

        if (!inhalt.isEmpty()) {

            neudatum = inhalt.substring(8, 10) + "." + inhalt.substring(5, 7) + "." + inhalt.substring(0, 4) + " "
                    + inhalt.substring(11, 19);
        }
        return (neudatum);
    }

    /**Formatiert String in der Form jjjj?mm?tt in tt.mm.jjjj*/
    static public String DatumStringFuerAnzeige(String inhalt) {

        String neudatum = "";

        if (!inhalt.isEmpty()) {

            neudatum = inhalt.substring(8, 10) + "." + inhalt.substring(5, 7) + "." + inhalt.substring(0, 4);
        }
        return (neudatum);
    }

    static public String ZeitStringFuerAnzeige(String inhalt) {
        String neudatum = "";

        if (!inhalt.isEmpty()) {

            neudatum = inhalt.substring(11, 19);
        }
        return (neudatum);

    }

    /**Liefert Zeistempel in Long der aktuellen Maschinen-Zeit*/
    static public long zeitStempelMS() {
        long stempel = new Date().getTime();
        return stempel;
    }

    static public long addSekundenZuStempel(long pZeitStempel, int pAnzSekunden) {
        long ergebnis = pZeitStempel + pAnzSekunden * 1000;
        return ergebnis;
    }

    /**Liefert 0, wenn pZeitStempel in Vergangenheit liegt*/
    static public int ermittleSekundenBisStempel(long pZeitStempel) {
        long aktuellerZeitstempel = zeitStempelMS();
        long dif = pZeitStempel - aktuellerZeitstempel;
        if (dif <= 0) {
            return 0;
        }
        int sekunden = (int) (dif / (long) 1000);
        return sekunden;
    }

    /**Liefert die Anzahl der Minuten, die seit dem Zeitstempel pZeitStempel verganen sind*/
    static public double difMinutenZuStempel(long pZeitStempel) {
        long aktuellerStempel = zeitStempelMS();

        double minuten = (aktuellerStempel - pZeitStempel) / 1000.0 / 60.0;

        return minuten;
    }

    /**Addiert pAnzMinuten zur Zeit in pStartZeit JJJJ.MM.TT HH:MM:SS
     * 
     * Achtung - funktioniert nur, wenn am selben Tag! D.h. nach 24:00 Uhr geht nicht ....
     * */
    static public String addMinuten(String pStartZeit, int pAnzMinuten) {
        String neueZeit = "";

        int stunde = Integer.parseInt(pStartZeit.substring(11, 13));
        int minute = Integer.parseInt(pStartZeit.substring(14, 16));

        minute += pAnzMinuten;
        while (minute >= 60) {
            minute -= 60;
            stunde++;
        }

        String hMinute = CaString.fuelleLinksNull(Integer.toString(minute), 2);
        String hStunde = CaString.fuelleLinksNull(Integer.toString(stunde), 2);

        neueZeit = pStartZeit.substring(0, 11) + hStunde + ":" + hMinute + ":" + pStartZeit.substring(17);
        return neueZeit;
    }

    /**Liefert die Differenz zwischen erstem Zeitstempel und zweitem Zeitstempel in Minuten;
     * Zeitstempel jeweils in der Form JJJJ.MM.TT HH:MM:SS
     * Sekunden werden ignoriert (d.h. nur Differenz zwischen HH:MM).
     * 
     * Achtung: funktioniert derzeit nur, wenn am selben Tag!
     */
    static public int minutenDifferenz(String pFruehereZeit, String pSpaetereZeit) {

        int fruehereStunde = Integer.parseInt(pFruehereZeit.substring(11, 13));
        int fruehereMinute = Integer.parseInt(pFruehereZeit.substring(14, 16));

        int spaetereStunde = Integer.parseInt(pSpaetereZeit.substring(11, 13));
        int spaetereMinute = Integer.parseInt(pSpaetereZeit.substring(14, 16));

        int differenz = (spaetereStunde - fruehereStunde) * 60 + (spaetereMinute - fruehereMinute);

        return differenz;
    }

    /**Subtrahiert von pZeit (in der From JJJJ.MM.TT HH:MM:SS) die 
     * Anzahl Sekunden anzSekunden.
     * Achtung - funktioniert nur innerhalb eines Tages
     */
    static public String subtrahiereSekunden(String pZeit, int anzSekunden) {

        String neueZeit = "";

        int stunde = Integer.parseInt(pZeit.substring(11, 13));
        int minute = Integer.parseInt(pZeit.substring(14, 16));
        int sekunde = Integer.parseInt(pZeit.substring(17, 19));

        sekunde = sekunde - anzSekunden;
        int difMinuten = 0;
        while (sekunde < 0) {
            sekunde += 60;
            difMinuten++;
        }

        minute = minute - difMinuten;
        int difStunden = 0;
        while (minute < 0) {
            minute += 60;
            difStunden++;
        }

        stunde = stunde - difStunden;

        String hMinute = CaString.fuelleLinksNull(Integer.toString(minute), 2);
        String hStunde = CaString.fuelleLinksNull(Integer.toString(stunde), 2);
        String hSekunden = CaString.fuelleLinksNull(Integer.toString(sekunde), 2);

        neueZeit = pZeit.substring(0, 11) + hStunde + ":" + hMinute + ":" + hSekunden;
        return neueZeit;
    }

    /**Monate immer zu 31 Tage, Jahr immer zu 365 Tage - ist vereinfacht.
     * Parameter jeweils im Format JJJJ.MM.TT HH:MM:SS
     */
    static public int tageDifferenz(String pFruehereZeit, String pSpaetereZeit) {
        int frueheresJahr = Integer.parseInt(pFruehereZeit.substring(0, 4));
        int spaeteresJahr = Integer.parseInt(pSpaetereZeit.substring(0, 4));

        int frueheresMonat = Integer.parseInt(pFruehereZeit.substring(5, 7));
        int spaeteresMonat = Integer.parseInt(pSpaetereZeit.substring(5, 7));

        int frueheresTag = Integer.parseInt(pFruehereZeit.substring(8, 10));
        int spaeteresTag = Integer.parseInt(pSpaetereZeit.substring(8, 10));

        int diffTage = 0;

        System.out.println("CaDatumZeit Früher=" + frueheresJahr + " " + frueheresMonat + " " + frueheresTag);
        System.out.println("CaDatumZeit Später=" + spaeteresJahr + " " + spaeteresMonat + " " + spaeteresTag);

        if (spaeteresTag < frueheresTag) {
            spaeteresTag += 31;
            spaeteresMonat -= 1;
        }
        if (spaeteresMonat < frueheresMonat) {
            spaeteresMonat += 12;
            spaeteresJahr -= 1;
        }
        if (spaeteresJahr < frueheresJahr) {
            CaBug.drucke("CaDatumZeit.tageDifferenz 001");
            return -1;
        }

        diffTage = (spaeteresTag - frueheresTag) + (spaeteresMonat - frueheresMonat) * 12
                + (spaeteresJahr - frueheresJahr) * 365;

        return diffTage;
    }

    /**Ermittelt den Zeitslot, in dem pZeit in relation zu pStartZeit fällt.
     * ZeitSlot ist dabei eine Anzahl von Minuten (pSlotLaenge), also z.B. 5 Minuten.
     * Bezogen auf Startzeit 2017.05.01 10:00:00
     * hat z.B. die Zeit 2017.05.01. 10:08:30 den Slot 1 bei einer Slotlänge von 5
     * (Slotzählung beginnt bei 0)
     */
    static public int zeitSlot(String pStartZeit, String pZeit, int pSlotLaenge) {
        int slot = 0;
        if (pZeit.compareTo(pStartZeit) > 0) {
            slot = minutenDifferenz(pStartZeit, pZeit) / pSlotLaenge;
        }

        return slot;
    }

    /**TT.MM.JJJJ zu JJJJMMTT
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumNormalZuJJJJMMTT(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 10) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(6, 10) + pDatum.substring(3, 5) + pDatum.substring(0, 2);
    }

    /**TT.MM.JJJJ zu JJJJ-MM-TT
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumNormalZuJJJJ_MM_TT(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 10) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(6, 10) + "-" + pDatum.substring(3, 5) + "-" + pDatum.substring(0, 2);
    }

    /**JJJJMMTT zu TT.MM.JJJJ
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumJJJJMMTTzuNormal(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 8) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(6) + "." + pDatum.substring(4, 6) + "." + pDatum.substring(0, 4);
    }

    /**JJJJMMTT zu JJJJ-MM-TT
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumJJJJMMTTzuJJJJ_MM_TT(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 8) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(0, 4) + "-" + pDatum.substring(4, 6) + "-" + pDatum.substring(6);
    }

    /**JJJJ-MM-TT zu TT.MM.JJJJ
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumJJJJ_MM_TTzuNormal(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 8) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(8) + "." + pDatum.substring(5, 7) + "." + pDatum.substring(0, 4);
    }

    /**JJJJ-MM-TT_HH:MM:SS????? zu TT.MM.JJJJ HH:MM:SS 
     * pDatum kann leer sein, dann wird leer zurückgeliefert*/
    static public String datumJJJJ_MM_TT_HH_MM_SSzuNormal(String pDatum) {
        if (pDatum == null) {
            return "";
        }
        if (pDatum.isEmpty()) {
            return "";
        }
        if (pDatum.length() < 24) {
            CaBug.drucke("001");
            return "";
        }
        return pDatum.substring(8, 10) + "." + pDatum.substring(5, 7) + "." + pDatum.substring(0, 4) + " "
                + pDatum.substring(11, 13) + ":" + pDatum.substring(14, 16) + ":" + pDatum.substring(17, 19);
    }

    /**Beider Parameter in der Form TT.MM.JJJJ*/
    static public int alterInJahren(String pGeburtsdatum, String pVergleichsdatum) {
        int jahrGeburt = Integer.parseInt(pGeburtsdatum.substring(6, 10));
        int jahrVergleich = Integer.parseInt(pVergleichsdatum.substring(6, 10));
        int monatGeburt = Integer.parseInt(pGeburtsdatum.substring(3, 5));
        int monatVergleich = Integer.parseInt(pVergleichsdatum.substring(3, 5));
        int tagGeburt = Integer.parseInt(pGeburtsdatum.substring(0, 2));
        int tagVergleich = Integer.parseInt(pVergleichsdatum.substring(0, 2));

        int jahrDifferenz = jahrVergleich - jahrGeburt;
        /*Prüfen - wenn Geburtsmonat nach aktuellem Monat liegt, dann um 1 Jahr reduzieren*/
        if (monatGeburt > monatVergleich) {
            jahrDifferenz--;
            return jahrDifferenz;
        }
        if (monatGeburt < monatVergleich) {
            return jahrDifferenz;
        }

        /*Hier: Geburtsmonat=vergleichsmonat, nun Tag berücksichtigen*/
        if (tagGeburt > tagVergleich) {
            jahrDifferenz--;
            return jahrDifferenz;
        }
        if (tagGeburt < tagVergleich) {
            return jahrDifferenz;
        }

        return jahrDifferenz;
    }

    /*
     * Datum zu Localdate
     */
    public static LocalDate localDateVonDatum(String pDatum) {

        String[] datumSplit = pDatum.split("-");

        int jahr = Integer.valueOf(datumSplit[0]);
        int monat = Integer.valueOf(datumSplit[1]);
        int tag = Integer.valueOf(datumSplit[2]);

        LocalDate date = LocalDate.of(jahr, monat, tag);
        return date;

    }

    /*
     * Datum zu Array {jahr, monat, tag}
     */
    public static int[] datumNormalZuArrayJMTInteger(String pDatum) {
        int[] arr = new int[3];
        String[] split = pDatum.split("[.]");
        arr[0] = Integer.parseInt(split[2]);
        arr[1] = Integer.parseInt(split[1]);
        arr[2] = Integer.parseInt(split[0]);
        return arr;
    }

    /**Vergleich von zwei Datums in der Form TT.MM.JJJJ*/
    static public int vergleicheAnzeigeDatums(String pDatum1, String pDatum2) {
        String lDatum1 = DatumStringFuerDatenbank(pDatum1);
        String lDatum2 = DatumStringFuerDatenbank(pDatum2);
        return lDatum1.compareTo(lDatum2);
    }
    
    /**
     * LocalDate zu ISO 8601
     */
    public static String currentDateTimeToIso() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public static String toGermanDate(Timestamp ts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime dateTime = ts.toLocalDateTime();

        return dateTime.format(formatter);
    }

    public static String toGermanTime(Timestamp ts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime dateTime = ts.toLocalDateTime();

        return dateTime.format(formatter);
    }
    
    public static String toGermanFormat(Timestamp ts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        LocalDateTime localDateTime = ts.toLocalDateTime();
        
        return localDateTime.format(formatter);
    }

}
