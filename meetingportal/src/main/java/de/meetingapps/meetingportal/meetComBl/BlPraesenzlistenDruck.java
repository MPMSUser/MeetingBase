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

import java.util.Arrays;
import java.util.Comparator;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompAktien;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompAktionaersnummer;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompEintrittskarte;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompName;
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzlisteCompStimmkarte;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Vorausetzungen/Abarbeitungsablauf:
 * 
 * Grundsätzlich wird davon ausgegangen, dass aus EclWillenserklaerung alle erforderlichen Daten jederzeit
 * restauriert werden können.
 * 
 * D.h.:
 * 
 * Es werden alle Willenserklärungen selektiert, die der zu druckenden Listennummer zugeordnet sind
 * (zuVerzeichnisNrXGedruckt = praesenzlistenNummer)
 * und eine auszudruckende Willenserklärunge haben.
 * 
 * Eintrittskartennummer, Stimmkartennummer und Aktien/Stimmen zum Druckzeitpunkt werden ebenfalls der Willenserklärung
 * entnommen (Aktien/Stimmen wichtig für Sammelkarten, die sich ja während der HV laufend noch verändern können).
 * 
 * Aus EclWillenserklaerung.willenserklaerungGeberIdent kann die Person entnommen werden, die diese Willenserklärung
 * abgegeben hat. Dies ist entweder -1 (=> Aktionär selbst), oder >1 der Vertreter.
 * D.h auch beim Vertreterwechsel muß hier der NEUE Bevollmächtigte (bei der Buchung) eingetragen werden.
 * Bei Aufnahme/Entnahme in Sammelkarten ist hier der Bevollmächtigte der Sammelkarte eingetragen.
 * 
 * Bei Aufnahme/Entnahme in Sammelkarten ist in identMeldungZuSammelkarte die Ident der Sammelkarte enthalten. Aus dieser kann
 * bei Nicht-Offenlegung der Aktionärsname entnommen werden.
 * 
 * Um zu entscheiden, ob eine Sammelkarten-Bewegung (also z.B. Zugang / Abgang der Sammelkarte selbst) gedruckt werden muß, muß
 * beim Select das entsprechende Feld aus EclMeldung der Sammelkarte mit ermittelt werden.
 * 
 * 
 * 
 * 
 */
/**Testfälle
 * Vorbereitungen:
 * Sammelkarte 61, 62, 63(ohneOffenlegung), 64(ohne Offenlegung), 65, 66(ohne Offenlegung)
 * In Sammelkarten aufnahmen:
 * 21001 (I40)- Offenlegung: 21,22, 8
 * 21002 (I41) - Offenlegung:	23,24
 * 21003 (I42) - ohne Offenlegung: 25,26, 9
 * 21004 (I43)- ohne Offenlegung: 27, 28
 * 21005 (I44)- Offenlegung: 29,30, 12
 * 21006 (I45) - ohne Offenlegung: 31, 32, 14
 * In Sammelkarten Vertreter eintragen
 * In 18 und 19 vorgespeicherte Vertreter eintragen
 * 
 * Buchungen
 * 1	Zugang
 * 17	ZugangVertreter
 * 18 	Zugang mit vorgespeichertem Vertreter
 * 19	Zugang selbst, trotz vorgespeichertem Vertreter
 * 2	ZugangVertreter Abgang
 * 3	Zugang Abgang Zugang
 * 4	Zugang Abgang ZUgang Abgang
 * 5	Zugang Abgang Zugang Vertreterwechsel Abgang Zugang Vertreterwechsel Abgang Zugang
 * 21001 	Sammelkarte offenlegung Zugang
 * 21002	Sammelkarte offenlegung Zugang Abgang
 * 21003	Sammelkarte nicht offenlegung Zugang
 * 21004	Sammelkarte nicht offenlegung Zugang Abgang
 * 6	Zugang dannIn Vollmacht-in-Sammelkarte-Offenlegung (21001)
 * 7	Zugang DannIn Vollmacht-In-Sammelkarte-Ohne-Offenlegung (21003)
 * 8	AusSammelkarteOffenlegungPräsbent Raus Zugang (21001)
 * 9	AusSammelkarte-Ohne-Offenlegung-Präsent Raus Zugang (21003)
 * 10	InSammelkarte-Offenlegung-Rein-Präsent (21001)
 * 11	InSammelkarte-Ohne-Offenlegung-Präsent Rein (21003)
 * 12	AusSamemlkarte-Offenlegung-NichtPräsent Raus (21005)
 * 13	AusSammelkarte-Offenlegung-NichtPräsent Rein (21005)
 * 14	AusSammelkarte-ohne-Offenlegung-NichtPräsent raus (21006)
 * 15	AusSammelkarte-Ohne-Offenelgunge_Nichtpräsent rein (21006)
 * 
 * 
 * Special Case: einmal in präsenzliste, in anderem durchlauf in Nachtrag:
 * Buchung in eine "präsent gewesene Sammelkarte". 
 * TODO #1 Abfangen: präsente Aktionär in nicht-präsente Sammemlkarten buchen
 * 
 * 
 * Nachtrag zusätzlich
 * 1 Abgang
 * 3 Abgang Zugang
 * 5 Abgang Zugang Abgang
 * 16 Zugang
 * 2 Wiederzugang
 * 21001 Vertreterwechsel
 * 21003 Vertreterwechsel
 */

public class BlPraesenzlistenDruck {

    private boolean logDrucken = true;

    /**Werden in druckePraesenzlisteZusammenstellung belegt*/
    /**True, wenn Höchststimmrecht-Hack aktiv ist, sonst false
     * (pDbBundle.param.paramBasis.hoechststimmrechtHackAktiv==1);**/
    private boolean hoechststimmrechtHackAktiv=false; 
    /**(long)pDbBundle.param.paramBasis.stimmenBeiPraesenzReduzierenUmStimmen*/
    private long stimmenBeiPraesenzReduzierenUmStimmen=0L;

    
    /**verzeichnis = 1 bis 4 ...
     * listenNummer= -2 = Gesamtteilnehmerverzeichnis, -1 = Erstpräsenz, 1 bis .... = Nachtrag
     * gattung = 0 => alle, ansonsten nur die bestimmte Gattung
     * 
     * sortierung:
     * rbSortName=1
     * rbSortEK=2
     * rbSortSK=3
     * rbSortAktien=4
     * 5=Aktienregisternummer
     *
     * pListeFuerVirtuelleHV true => es werden auch die zugeschalteten Teilnehmern mit aufgeführt
     */

    public boolean druckePraesenzliste(DbBundle pDbBundle, RpDrucken rpDrucken, int verzeichnis, int listenNummer, int gattung, String lfdNummer, int sortierung, boolean pListeFuerVirtuelleHV) {
        boolean brc = true;
        BlPraesenzSummen blPraesenzSummen = new BlPraesenzSummen(pDbBundle);
        long[] neueSumme = { 0, 0, 0, 0, 0 };

        RpVariablen rpVariablen = null;

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.praesenzliste(lfdNummer, rpDrucken);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.NrNachtrag", Integer.toString(listenNummer)); /*-1 = Erstpräsenz*/
        
        String praesenzIdent=Integer.toString(listenNummer);
        if (listenNummer==-1) {praesenzIdent="0";}
        if (listenNummer==-2) {praesenzIdent="999";}
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.PraesenzIdent", praesenzIdent);
        
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Sortierung", Integer.toString(sortierung)); 
        blPraesenzSummen.leseSummenFuerListe(listenNummer); /*Für Datum Uhrzeit*/
        String hDatum = CaDatumZeit.DatumZeitStringFuerAnzeige(blPraesenzSummen.datumZeitFeststellung).substring(0, 11);
        String hZeit = CaDatumZeit.DatumZeitStringFuerAnzeige(blPraesenzSummen.datumZeitFeststellung).substring(11, 16);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", hZeit);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.GattungDruck", Integer.toString(gattung));

        brc = rpDrucken.startListe();
        if (brc == false) {
            return false;
        }

        int rc = 0;
        if (listenNummer == -2) {
            rc = pDbBundle.dbJoined.read_Praesenzliste(-1, listenNummer, 0, pListeFuerVirtuelleHV);

        } else {
            rc = pDbBundle.dbJoined.read_Praesenzliste(verzeichnis, listenNummer, 0, pListeFuerVirtuelleHV);
        }

        if (rc > 0) {

            /*Nicht zu druckende ausfiltern*/

            /*Bei offengelegten Sammelkarten: Sammelkarten-Bewegung selbst rausnehmen.
             * Außerdem Sortier-Feld belegen (bei nicht Offenlegung!)*/
            for (int i = 0; i < rc; i++) {
                EclPraesenzliste lEclPraesenzliste = pDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);
                if (
                /*Element ist selbst eine Sammelkarte, die offengelegt wird => nicht drucken*/
                (lEclPraesenzliste.meldungstyp == 2 && lEclPraesenzliste.meldungSkOffenlegung == 1)) {
                    lEclPraesenzliste.drucken = 0;

                }
                if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                    lEclPraesenzliste.sortierName = lEclPraesenzliste.sammelkartenName + " " + lEclPraesenzliste.sammelkartenVorname + " " + lEclPraesenzliste.sammelkartenOrt;
                } else {
                    lEclPraesenzliste.sortierName = lEclPraesenzliste.aktionaerName + " " + lEclPraesenzliste.aktionaerVorname + " " + lEclPraesenzliste.aktionaerOrt;
                }

                /*Gattung ausfiltern*/
                if (gattung != 0 && lEclPraesenzliste.gattung != gattung) {
                    lEclPraesenzliste.drucken = 0;
                }

                /*Bewegungen rausfiltern - je nachdem ob virtuelleListe oder nicht*/
                if (pListeFuerVirtuelleHV) {
                    /*TODO VidKonf  Hinweis / Testen virtuelle Präsenzliste: hier ist eine Unsauberkeit, nämlich wenn jemand aus SRV raus geht, und dann wieder rein.
                     * Dann steht er zweimal in der Liste .... Nochmal genau austesten, vor allem was ist bei Stimmenänderung? Ist das kurzfristig akzeptabel?
                     * Nachbesserung?
                     */
                    switch (lEclPraesenzliste.willenserklaerung) {
                    case KonstWillenserklaerung.abgang:
                    case KonstWillenserklaerung.wiederzugang:
                    case KonstWillenserklaerung.abgangAusSRV:
                    case KonstWillenserklaerung.abgangAusOrga:
                    case KonstWillenserklaerung.abgangAusDauervollmacht:
                    case KonstWillenserklaerung.abgangAusKIAV:
                    case KonstWillenserklaerung.virtWiederzugangSelbst:
                    case KonstWillenserklaerung.virtAbgang:
                    case KonstWillenserklaerung.virtWiederzugangVollmacht:
                    case KonstWillenserklaerung.virtAbgangVollmacht:
                        lEclPraesenzliste.drucken = 0;
                        break;
                    }

                } else { /*Normale Präsenzliste - virtuelle Bewegungen ausfiltern*/
                    switch (lEclPraesenzliste.willenserklaerung) {
                    case KonstWillenserklaerung.virtZugangSelbst:
                    case KonstWillenserklaerung.virtWiederzugangSelbst:
                    case KonstWillenserklaerung.virtAbgang:
                    case KonstWillenserklaerung.virtZugangVollmacht:
                    case KonstWillenserklaerung.virtWiederzugangVollmacht:
                    case KonstWillenserklaerung.virtAbgangVollmacht:
                        lEclPraesenzliste.drucken = 0;
                        break;
                    }
                }

            }

            /*Mehrfach Zu-/Abgänge*/

            /*Sortieren*/
            ergebnisSortieren(pDbBundle, sortierung);

            /*Hier SUmmen laut Liste ermitteln*/
            long[] difGattung = { 0, 0, 0, 0, 0 };

            if (!pListeFuerVirtuelleHV) {
                for (int i = 0; i < rc; i++) {
                    EclPraesenzliste lEclPraesenzliste = pDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);

                    if (lEclPraesenzliste.drucken == 1) {
                        int faktor = 0;
                        switch (lEclPraesenzliste.willenserklaerung) {

                        case KonstWillenserklaerung.wiederzugang:

                        case KonstWillenserklaerung.zugangInSRV:
                        case KonstWillenserklaerung.zugangInOrga:
                        case KonstWillenserklaerung.zugangInDauervollmacht:
                        case KonstWillenserklaerung.zugangInKIAV:

                        case KonstWillenserklaerung.erstzugang: {
                            faktor = 1;
                            break;
                        }

                        case KonstWillenserklaerung.abgangAusSRV:
                        case KonstWillenserklaerung.abgangAusOrga:
                        case KonstWillenserklaerung.abgangAusDauervollmacht:
                        case KonstWillenserklaerung.abgangAusKIAV:

                        case KonstWillenserklaerung.abgang:

                        {
                            faktor = -1;
                            break;
                        }

                        case KonstWillenserklaerung.wechselInSRV:
                        case KonstWillenserklaerung.wechselInOrga:
                        case KonstWillenserklaerung.wechselInDauervollmacht:
                        case KonstWillenserklaerung.wechselInKIAV:

                        case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV:
                        case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnKIAV:

                        case KonstWillenserklaerung.vertreterwechsel: {
                            faktor = 0;
                            break;
                        }

                        }

                        difGattung[lEclPraesenzliste.gattung - 1] += faktor * lEclPraesenzliste.stimmen;
                    }

                }
                for (int i = 0; i < 5; i++) {
                    neueSumme[i] = difGattung[i];
                }
                if (listenNummer != -1 && listenNummer!=-2) {
                    int vorherigeListe = listenNummer - 1;
                    if (vorherigeListe == 0) {
                        vorherigeListe = -1;
                    }
                    blPraesenzSummen.leseSummenFuerListe(vorherigeListe);
                    for (int i = 0; i < 5; i++) {
                        neueSumme[i] += blPraesenzSummen.praesenzSummeAusListeGattung[i];
                    }
                }
                long[] summen = { 0, 0, 0, 0, 0 };
                for (int i = 0; i < 5; i++) {
                    summen[i] = neueSumme[i];
                }
                blPraesenzSummen.speichereSummenAusListe(listenNummer, verzeichnis, summen, gattung);
            } /*Summen-Ermittlung Ende*/

            druckenAusfuehren(pDbBundle, rc, rpDrucken);

        } else { /*Es wurde gar nix gedruckt*/

            if (!pListeFuerVirtuelleHV) {

                for (int i = 0; i < 5; i++) {
                    neueSumme[i] = 0;
                }
                if (listenNummer != -1 && listenNummer!=-2) {
                    int vorherigeListe = listenNummer - 1;
                    if (vorherigeListe == 0) {
                        vorherigeListe = -1;
                    }
                    blPraesenzSummen.leseSummenFuerListe(vorherigeListe);
                    for (int i = 0; i < 5; i++) {
                        neueSumme[i] += blPraesenzSummen.praesenzSummeAusListeGattung[i];
                    }
                }
                long[] summen = { 0, 0, 0, 0, 0 };
                for (int i = 0; i < 5; i++) {
                    summen[i] = neueSumme[i];
                }
                blPraesenzSummen.speichereSummenAusListe(listenNummer, verzeichnis, summen, gattung);
            }
        }

        rpDrucken.endeListe();
        return true;
    }

    /**longZahlen=Array der Länge 5, für jede Gattung.
     * gattung definiert, welcher Wert zurückgegeben werden soll.
     * 		0=Summe aus allen
     * 		sonst: Wert der Gattung (1-5)
     */
    private double liefereDoubleWertFuerGattung(double[] doubleZahlen, int gattung) {
        if (gattung == 0) {
            double hSumme = 0;
            for (int i = 0; i < 5; i++) {
                hSumme += doubleZahlen[i];
            }
            return hSumme;
        }
        return doubleZahlen[gattung - 1];
    }

    private long liefereWertFuerGattung(long[] longZahlen, int gattung) {
        if (gattung == 0) {
            long hSumme = 0;
            for (int i = 0; i < 5; i++) {
                hSumme += longZahlen[i];
            }
            return hSumme;
        }
        return longZahlen[gattung - 1];
    }

    /**Wie liefereWertFuerGattung, nur dass die Long-Zahl um die Höchststimmrechts-Korrektur reduziert wird*/
    private long liefereHSWertFuerGattung(long[] longZahlen, int gattung) {
        return liefereWertFuerGattung(longZahlen, gattung)-stimmenBeiPraesenzReduzierenUmStimmen;
    }

    private String liefereWertFuerGattungToString(long[] longZahlen, int gattung) {
        return Long.toString(liefereWertFuerGattung(longZahlen, gattung));
    }
    
    /**Wie liefereWertFuerGattungToString, nur dass die Long-Zahl um die Höchststimmrechts-Korrektur reduziert wird*/
    private String liefereHSWertFuerGattungToString(long[] longZahlen, int gattung) {
        return Long.toString(liefereWertFuerGattung(longZahlen, gattung)-stimmenBeiPraesenzReduzierenUmStimmen);
    }

    private String liefereWertFuerGattungToStringDE(long[] longZahlen, int gattung) {
        return CaString.toStringDE(liefereWertFuerGattung(longZahlen, gattung));
    }

    /**Wie liefereWertFuerGattungToStringDE, nur dass die Long-Zahl um die Höchststimmrechts-Korrektur reduziert wird*/
    private String liefereHSWertFuerGattungToStringDE(long[] longZahlen, int gattung) {
        return CaString.toStringDE(liefereWertFuerGattung(longZahlen, gattung)-stimmenBeiPraesenzReduzierenUmStimmen);
    }

    public boolean druckePraesenzlisteZusammenstellung(DbBundle pDbBundle, RpDrucken rpDrucken, int verzeichnis, int listenNummer, int gattung, String lfdNummer, int sortierung) {
        return druckePraesenzlisteZusammenstellung(pDbBundle, rpDrucken, verzeichnis, listenNummer, gattung, lfdNummer, sortierung, "", "");
    }

    /**Noch nicht sicher, aber derzeitige Annahme:
     * listennummer:
     * -2 => Gesamtteilnehmerverzeichnis
     * -1 => Erstpräsenz
     * >0 => Nachtrag
     */
    public boolean druckePraesenzlisteZusammenstellung(DbBundle pDbBundle, RpDrucken rpDrucken, int verzeichnis, int listenNummer, int gattung, String lfdNummer, int sortierung, String pText1,
            String pText2) {

        boolean rc = true;

        hoechststimmrechtHackAktiv=(pDbBundle.param.paramBasis.hoechststimmrechtHackAktiv==1);
        stimmenBeiPraesenzReduzierenUmStimmen=(long)pDbBundle.param.paramBasis.stimmenBeiPraesenzReduzierenUmStimmen;
        
        rpDrucken.initFormular(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.praesenzZusammenstellung(lfdNummer, rpDrucken);
        System.out.println("lfdNummer=" + lfdNummer);
        rc = rpDrucken.startFormular();
        if (rc == false) {
            return false;
        }

        /**Summe virtuelle Präsenz ermitteln*/
        /*TODO VidKonf  VirtuellSpäter Derzeit virtuelle HV noch nicht für einzelne Gattungen implementiert!*/
        long anzTeilnehmerVirtuell = 0;
        long anzAktienVirtuell = 0;

        anzTeilnehmerVirtuell = pDbBundle.dbWillenserklaerung.ermittleSummeVirtuellTeilnehmer(listenNummer);
        anzAktienVirtuell = pDbBundle.dbWillenserklaerung.ermittleSummeVirtuellAktien(listenNummer);
        double hProzentVirtuell = CaString.berechneProzent(anzAktienVirtuell, liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, 0 /*gattung*/));
        String hProzentStringVirtuell = CaString.prozentToString(hProzentVirtuell);

        rpVariablen.fuelleVariable(rpDrucken, "AnzahlTeilnehmerVirtuell", CaString.toStringDE(anzTeilnehmerVirtuell));
        rpVariablen.fuelleVariable(rpDrucken, "AnzahlAktienVirtuell", CaString.toStringDE(anzAktienVirtuell));
        rpVariablen.fuelleVariable(rpDrucken, "AnzahlAktienVirtuellProzent", hProzentStringVirtuell);

        String lBriefwahlMoeglich="0";
        if (pDbBundle.param.paramModuleKonfigurierbar.briefwahl==true) {
            lBriefwahlMoeglich="1";
        }
        rpVariablen.fuelleVariable(rpDrucken, "BriefwahlMoeglich", lBriefwahlMoeglich);

        /**Div. Summen über alle Gattungen hinweis einlesen / Bereitstellen*/
        BlPraesenzSummen blPraesenzSummen = new BlPraesenzSummen(pDbBundle);

        for (int i = 0; i <= 5; i++) {/*Für Summen, und für alle Gattungen*/
            String hPraefix = "";
            if (i > 0) {
                hPraefix = "G" + Integer.toString(i) + ".";
            }
            if (i == 0 || pDbBundle.param.paramBasis.getGattungAktiv(i)) {

                if (listenNummer != -1 && listenNummer!=-2) {
                    /*Vorherige Lesen*/
                    int vorherige = listenNummer - 1;
                    if (vorherige == 0) {
                        vorherige = -1;
                    }
                    blPraesenzSummen.leseSummenFuerListe(vorherige);

                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.Briefwahl", liefereWertFuerGattungToString(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.BriefwahlDE", liefereWertFuerGattungToStringDE(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.Stimmen", liefereWertFuerGattungToString(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.StimmenDE", liefereWertFuerGattungToStringDE(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    
                    if (hoechststimmrechtHackAktiv) {
                        rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorherHS.Stimmen", liefereHSWertFuerGattungToString(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                        rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorherHS.StimmenDE", liefereHSWertFuerGattungToStringDE(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    }
                    
                    
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.Briefwahl", "");
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.BriefwahlDE", "");
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.Stimmen", "");
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorher.StimmenDE", "");

                    if (hoechststimmrechtHackAktiv) {
                        rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorherHS.Stimmen", "");
                        rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeVorherHS.StimmenDE", "");
                    }

                }

                blPraesenzSummen.leseSummenFuerListe(listenNummer);

                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.Briefwahl", liefereWertFuerGattungToString(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.BriefwahlDE", liefereWertFuerGattungToStringDE(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.Stimmen", liefereWertFuerGattungToString(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenDE", liefereWertFuerGattungToStringDE(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenUndBriefwahl", Long.toString(
                        liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/)));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenUndBriefwahlDE", CaString.toStringDE(
                        liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/)));

                if (hoechststimmrechtHackAktiv) {
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuellHS.Stimmen", liefereHSWertFuerGattungToString(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuellHS.StimmenDE", liefereHSWertFuerGattungToStringDE(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuellHS.StimmenUndBriefwahl", Long.toString(
                            liefereHSWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/)));
                    rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuellHS.StimmenUndBriefwahlDE", CaString.toStringDE(
                            liefereHSWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/)));
                }
                
                /*Prozentwerte vom Grundkapital*/
                String hProzentString = "";

                double hProzentBriefwahl = 0.0;
                hProzentBriefwahl = CaString.berechneProzent(liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                hProzentBriefwahl = CaString.runde2Stellen(hProzentBriefwahl);

                double hProzentStimmenUndBriefwahl = 0.0;
                hProzentStimmenUndBriefwahl = CaString.berechneProzent(
                        liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                if (logDrucken) {
                    CaBug.druckeInfo("BlPraesenzlistenDruck.druckePraesenzlisteZusammenstellung blPraesenzSummen.briefwahlSummeBerechnetGattung[i=" + Integer.toString(i) + "]"
                            + liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/));
                    CaBug.druckeInfo("blPraesenzSummen.praesenzSummeAusListeGattung[]=" + liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/));
                    CaBug.druckeInfo("pDbBundle.parameter.grundkapitalStueck=" + liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                }
                hProzentStimmenUndBriefwahl = CaString.runde2Stellen(hProzentStimmenUndBriefwahl);

                /*****************************/
                double hProzentStimmenReal = 0.0;
                hProzentStimmenReal = CaString.berechneProzent(liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                hProzentStimmenReal = CaString.runde2Stellen(hProzentStimmenReal);
                /*****************************/

                double hProzentStimmen = hProzentStimmenUndBriefwahl - hProzentBriefwahl;

                if (logDrucken) {
                    CaBug.druckeInfo("hProzentBriefwahl=" + hProzentBriefwahl);
                    CaBug.druckeInfo("hProzentStimmenUndBriefwahl=" + hProzentStimmenUndBriefwahl);
                    CaBug.druckeInfo("hProzentStimmen=" + hProzentStimmen);
                }

                hProzentString = CaString.prozentToString(hProzentBriefwahl);
                //        System.out.println("ProzentString="+hProzentString);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.BriefwahlProzentGrundkapital", hProzentString);

                hProzentString = CaString.prozentToString(hProzentStimmen);
                //        System.out.println("ProzentString="+hProzentString);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenProzentGrundkapital", hProzentString);

                /*****************************/
                hProzentString = CaString.prozentToString(hProzentStimmenReal);
                //        System.out.println("ProzentString="+hProzentString);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenProzentGrundkapitalReal", hProzentString);
                /*****************************/

                hProzentString = CaString.prozentToString(hProzentStimmenUndBriefwahl);
                //        System.out.println("ProzentString="+hProzentString);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenUndBriefwahlProzentGrundkapital", hProzentString);

                /*Prozentwerte vom Verminderten Grundkapital*/

                hProzentBriefwahl = 0.0;
                hProzentBriefwahl = CaString.berechneProzent(liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/));
                hProzentBriefwahl = CaString.runde2Stellen(hProzentBriefwahl);

                hProzentStimmenUndBriefwahl = 0.0;
                hProzentStimmenUndBriefwahl = CaString.berechneProzent(
                        liefereWertFuerGattung(blPraesenzSummen.briefwahlSummeBerechnetGattung, i /*gattung*/) + liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/));
                hProzentStimmenUndBriefwahl = CaString.runde2Stellen(hProzentStimmenUndBriefwahl);

                /*****************************/
                hProzentStimmenReal = 0.0;
                hProzentStimmenReal = CaString.berechneProzent(liefereWertFuerGattung(blPraesenzSummen.praesenzSummeAusListeGattung, i /*gattung*/),
                        liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/));
                hProzentStimmenReal = CaString.runde2Stellen(hProzentStimmenReal);
                /*****************************/

                hProzentStimmen = hProzentStimmenUndBriefwahl - hProzentBriefwahl;

                hProzentString = CaString.prozentToString(hProzentBriefwahl);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.BriefwahlProzentGrundkapitalVermindert", hProzentString);

                hProzentString = CaString.prozentToString(hProzentStimmen);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenProzentGrundkapitalVermindert", hProzentString);

                /*****************************/
                hProzentString = CaString.prozentToString(hProzentStimmenReal);
                //        System.out.println("ProzentString="+hProzentString);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenProzentGrundkapitalVermindertReal", hProzentString);
                /*****************************/

                hProzentString = CaString.prozentToString(hProzentStimmenUndBriefwahl);
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "SummeAktuell.StimmenUndBriefwahlProzentGrundkapitalVermindert", hProzentString);

                /*Jetzt restliche Werte*/
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "Grundkapital.GesamtStimmen", liefereWertFuerGattungToString(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "Grundkapital.GesamtStimmenDE", liefereWertFuerGattungToStringDE(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/));
                double grundkapitalEuro = 0.0;
                if (i /*gattung*/ > 0) {
                    grundkapitalEuro = liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i /*gattung*/)
                            * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i /*gattung*/);
                } else {
                    for (int i1 = 1; i1 <= 5; i1++) {
                        grundkapitalEuro += liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalStueck, i1) * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i1);
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "Grundkapital.GesamtEuro", Double.toString(grundkapitalEuro));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "Grundkapital.GesamtEuroDE", CaString.doubleToStringDE(grundkapitalEuro));

                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalVermindert.GesamtStimmen",
                        liefereWertFuerGattungToString(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalVermindert.GesamtStimmenDE",
                        liefereWertFuerGattungToString(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/));
                double grundkapitalVermindertEuro = 0.0;
                if (i /*gattung*/ > 0) {
                    grundkapitalVermindertEuro = liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i /*gattung*/)
                            * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i /*gattung*/);
                } else {
                    for (int i1 = 1; i1 <= 5; i1++) {
                        grundkapitalVermindertEuro += liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalVermindertStueck, i1)
                                * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i1);
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalVermindert.GesamtEuro", Double.toString(grundkapitalVermindertEuro));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalVermindert.GesamtEuroDE", CaString.doubleToStringDE(grundkapitalVermindertEuro));

               /*++++++Eigene Aktien+++++*/
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalEigeneAktien.GesamtStimmen",
                        liefereWertFuerGattungToString(pDbBundle.param.paramBasis.grundkapitalEigeneAktienStueck, i /*gattung*/));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalEigeneAktien.GesamtStimmenDE",
                        liefereWertFuerGattungToStringDE(pDbBundle.param.paramBasis.grundkapitalEigeneAktienStueck, i /*gattung*/));
                double grundkapitalEigeneAktienEuro = 0.0;
                if (i /*gattung*/ > 0) {
                    grundkapitalEigeneAktienEuro = liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalEigeneAktienStueck, i /*gattung*/)
                            * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i /*gattung*/);
                } else {
                    for (int i1 = 1; i1 <= 5; i1++) {
                        grundkapitalEigeneAktienEuro += liefereWertFuerGattung(pDbBundle.param.paramBasis.grundkapitalEigeneAktienStueck, i1)
                                * liefereDoubleWertFuerGattung(pDbBundle.param.paramBasis.wertEinerAktie, i1);
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalEigeneAktien.GesamtEuro", Double.toString(grundkapitalEigeneAktienEuro));
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GrundkapitalEigeneAktien.GesamtEuroDE", CaString.doubleToStringDE(grundkapitalEigeneAktienEuro));

                
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GattungVorhanden", "1");/*1 => die Gattung ist grundsätzlich vorhanden*/
            } else {
                rpVariablen.fuelleVariable(rpDrucken, hPraefix + "GattungVorhanden", "0");/*1 => die Gattung ist grundsätzlich vorhanden*/

            }
        }

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.NrNachtrag", Integer.toString(listenNummer)); /*-1 = Erstpräsenz*/
        
        String praesenzIdent=Integer.toString(listenNummer);
        if (listenNummer==-1) {praesenzIdent="0";}
        if (listenNummer==-2) {praesenzIdent="999";}
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.PraesenzIdent", praesenzIdent);

        
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Sortierung", Integer.toString(sortierung));
        blPraesenzSummen.leseSummenFuerListe(listenNummer); /*Für Datum Uhrzeit*/
        String hDatum = CaDatumZeit.DatumZeitStringFuerAnzeige(blPraesenzSummen.datumZeitFeststellung).substring(0, 11);
        String hZeit = CaDatumZeit.DatumZeitStringFuerAnzeige(blPraesenzSummen.datumZeitFeststellung).substring(11, 16);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", hZeit);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.GattungDruck", Integer.toString(gattung));

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Text1", pText1);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Text2", pText2);

        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();

        return true;

    }

    private void ergebnisSortieren(DbBundle pDbBundle, int sortierung) {
        switch (sortierung) {
        case 1: {
            Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompName();
            Arrays.sort(pDbBundle.dbJoined.ergebnisPraesenzliste(), comp);
            break;
        }
        case 2: {
            Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompEintrittskarte();
            Arrays.sort(pDbBundle.dbJoined.ergebnisPraesenzliste(), comp);
            break;
        }
        case 3: {
            Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompStimmkarte();
            Arrays.sort(pDbBundle.dbJoined.ergebnisPraesenzliste(), comp);
            break;
        }
        case 4: {
            Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompAktien();
            Arrays.sort(pDbBundle.dbJoined.ergebnisPraesenzliste(), comp);
            break;
        }
        case 5: {
            Comparator<EclPraesenzliste> comp = new EclPraesenzlisteCompAktionaersnummer();
            Arrays.sort(pDbBundle.dbJoined.ergebnisPraesenzliste(), comp);
            break;
        }

        }

    }

    private void druckenAusfuehren(DbBundle pDbBundle, int rc, RpDrucken rpDrucken) {
        for (int i = 0; i < rc; i++) {

            EclPraesenzliste lEclPraesenzliste = pDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);

            if (lEclPraesenzliste.drucken == 1) {

                String kurzbezeichnung = "";
                switch (lEclPraesenzliste.willenserklaerung) {

                case KonstWillenserklaerung.virtZugangSelbst:
                    kurzbezeichnung = "Zu Virt.";
                    break;
                case KonstWillenserklaerung.virtZugangVollmacht:
                    kurzbezeichnung = "Zu Virt.";
                    break;

                case KonstWillenserklaerung.wiederzugang:
                    kurzbezeichnung = "Zu";
                    break;
                case KonstWillenserklaerung.zugangInSRV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInOrga: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInDauervollmacht: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.zugangInKIAV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TZu";
                    } else {
                        kurzbezeichnung = "Zu";
                    }
                    break;
                }
                case KonstWillenserklaerung.erstzugang: {
                    kurzbezeichnung = "Zu";
                    break;
                }
                case KonstWillenserklaerung.abgangAusSRV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusOrga: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusDauervollmacht: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgangAusKIAV: {
                    if (lEclPraesenzliste.skOffenlegung == -1 && lEclPraesenzliste.meldungstyp == 3) {
                        kurzbezeichnung = "TAb";
                    } else {
                        kurzbezeichnung = "Ab";
                    }
                    break;
                }
                case KonstWillenserklaerung.abgang: {
                    kurzbezeichnung = "Ab";
                    break;
                }
                case KonstWillenserklaerung.wechselInSRV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInOrga: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInDauervollmacht: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.wechselInKIAV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnKIAV: {
                    kurzbezeichnung = "We";
                    break;
                }
                case KonstWillenserklaerung.vertreterwechsel: {
                    kurzbezeichnung = "We";
                    break;
                }

                }
                
                // add here your preferred DokumentGenerator
                rpDrucken.druckenListe();
            }
        }

    }

    /**Druckt alle im Zeitraum pBeginn, pEnde mal anwesenden Aktionäre in einem Teilnehmerverzeichnis.
     * 
     * Achtung, derzeit vereinfacht realisiert: es ziehen nur Zu-/Abgänge; Vertreter wird der in tbl_meldungen gespeicherte
     * genommen. Ist allzu in diesem Zustand nur für Vereine zuverlässig einsetzbar.
     * 
     * TODO VidKonf  für virtuelle HV noch nicht durchdacht!
     * 
     * pBeginn, pEnde in der Form JJJJ.TT.MM HH:MM:SS 
     * 
     * Wenn pEnde=="", dann wird nur zum Stichhzteitpunkt pBeginn gedruckt.
     * 
     * rbSortName=1
     * rbSortEK=2
     * rbSortSK=3
     * rbSortAktien=4
     * 5=Aktienregisternummer
     */
    public boolean druckeTeilnehmerverzeichnisZeitraum(DbBundle pDbBundle, RpDrucken rpDrucken, String pBeginn, String pEnde, String lfdNummer, int sortierung) {

        RpVariablen rpVariablen = null;

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.praesenzliste(lfdNummer, rpDrucken);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.NrNachtrag", Integer.toString(-2));
        
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.PraesenzIdent", "999");

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Sortierung", Integer.toString(sortierung));
        String hDatum = CaDatumZeit.DatumZeitStringFuerAnzeige(pBeginn).substring(0, 11);
        String hZeit = CaDatumZeit.DatumZeitStringFuerAnzeige(pBeginn).substring(11, 16);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Datum", hDatum);
        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.Zeit", hZeit);

        rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.GattungDruck", Integer.toString(0));

        String lIrgendeineGattungTauschBeliebig="0";
        
        for (int i = 1; i <= 5; i++) {/*Für alle Gattungen*/
            String lGattungAktiv="0";
            if (pDbBundle.param.paramBasis.getGattungAktiv(i)) {
                lGattungAktiv="1";
            }
            rpVariablen.fuelleVariable(rpDrucken, "G" + Integer.toString(i) + ".GattungVorhanden", lGattungAktiv);
            
            String lGattungTauschBeliebig="0";
            if (pDbBundle.param.paramAkkreditierung.stimmkartenTauschBeliebigVorhanden(i)) {
                lGattungTauschBeliebig="1";
                lIrgendeineGattungTauschBeliebig="1";
            }
            rpVariablen.fuelleVariable(rpDrucken, "G" + Integer.toString(i) + ".TauschBeliebig", lGattungTauschBeliebig);
        }
        rpVariablen.fuelleVariable(rpDrucken, "G.TauschBeliebig", lIrgendeineGattungTauschBeliebig);
        
        boolean brc = rpDrucken.startListe();
        if (brc == false) {
            return false;
        }

        if (pEnde.isEmpty()) {
            pEnde = pBeginn;
        }

        /*Einlesen Gesamt-Teilnehmerverzeichnis*/
        int anzErklaerungen = pDbBundle.dbJoined.read_Praesenzliste(-1, 1, 0, false);
        if (anzErklaerungen > 0) {
            /*Nicht zu druckende ausfiltern*/
            EclPraesenzliste lEclPraesenzlisteAktuellerEintrag = new EclPraesenzliste();
            int offsetAktuellerEintrag = -1;

            pDbBundle.dbJoined.ergebnisPraesenzlistePosition(0);

            for (int i = 0; i < anzErklaerungen; i++) {
                EclPraesenzliste lEclPraesenzlisteZuBearbeitenderEintrag = pDbBundle.dbJoined.ergebnisPraesenzlistePosition(i);
                lEclPraesenzlisteZuBearbeitenderEintrag.drucken = 0;

                if (offsetAktuellerEintrag == -1 || lEclPraesenzlisteAktuellerEintrag.meldeIdentAktionaer != lEclPraesenzlisteZuBearbeitenderEintrag.meldeIdentAktionaer) {
                    /*Wechsel*/
                    lEclPraesenzlisteAktuellerEintrag = lEclPraesenzlisteZuBearbeitenderEintrag;
                    offsetAktuellerEintrag = i;
                }

                if (lEclPraesenzlisteZuBearbeitenderEintrag.veraenderungszeit.compareTo(pEnde) <= 0 && (lEclPraesenzlisteZuBearbeitenderEintrag.willenserklaerung == KonstWillenserklaerung.erstzugang
                        || lEclPraesenzlisteZuBearbeitenderEintrag.willenserklaerung == KonstWillenserklaerung.wiederzugang)) {
                    /*Rechtzeitiger Zugang -> zählt*/
                    lEclPraesenzlisteAktuellerEintrag.drucken = 1;
                }
                if (lEclPraesenzlisteZuBearbeitenderEintrag.veraenderungszeit.compareTo(pBeginn) < 0 && lEclPraesenzlisteZuBearbeitenderEintrag.willenserklaerung == KonstWillenserklaerung.abgang) {
                    /*Rechtzeitiger Abgang -> zählt nicht*/
                    lEclPraesenzlisteAktuellerEintrag.drucken = 0;
                }
                if (lEclPraesenzlisteZuBearbeitenderEintrag.aktionaersnummer.isEmpty()) {
                    lEclPraesenzlisteAktuellerEintrag.drucken = 0;
                }

            }

        }

        /*Sortieren*/
        ergebnisSortieren(pDbBundle, sortierung);

        /*Drucken*/
        druckenAusfuehren(pDbBundle, anzErklaerungen, rpDrucken);

        rpDrucken.endeListe();

        return true;
    }

}
