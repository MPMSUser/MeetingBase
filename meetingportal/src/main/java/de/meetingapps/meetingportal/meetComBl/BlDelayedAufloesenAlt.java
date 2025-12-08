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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class BlDelayedAufloesenAlt {

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-Willenserklärungen**********************************************/
    /****************************************************************************************************************************************/
    /**Grundsätzlicher Ablauf:
     * 1. es werden alle Delayed-Willenserklärungen durchgearbeitet und - soweit möglich - verbucht.
     * 		Diese werden dabei zu Anfangs komplett eingelesen, und dann jeweils einzeln in separaten Transaktionen verbucht.
     * 		Dabei können "Störungen" auftreten:
     * 		> die aktuell zu verarbeitende Willenserklärung hat sich verändert und kann nicht zurückgeschrieben werden.
     * 				Umgang damit?: Nach derzeitiger Logik darf das nicht auftreten, da Willenserklärungen nur duch Delayed- oder 
     * 				Pending-Auflösung verändert werden. => Programmfehler 
     * 		> die zur Willenserklärung gehörenden MeldungsIdent können nicht zurückgeschrieben werden
     * 				Umgang damit?: kann nicht auftreten, da lesen/schreiben der MeldungsIdent innerhalb einer Transaktion erfolgt.
     * 		> Achtung - es können parallel weitere Delayed-Willenserklärungen angefügt werden, da ja für Meldungen, die eine
     * 			Delayed-Willenserklärungen haben, neue automatisch auch delayed werden (auch wenn Delay schon längst deaktiviert)
     * 2. es werden alle Meldungen, dienen Delayed-Status haben, mit den zugehörigen Delayed-Willenserklärungen (Join) durchgearbeitet.
     * 		> bei denjenigen, die keine Delayed-Willenserklärung mehr haben, wird der Delayed-Status zurückgesetzt
     * 		> Alle anderen bleiben erstmals (diese können entstehen, wenn während 1.) neue Delayed-Willenserklärungen gespeichert werden)
     * 	Mögliche Störungen:
     * 		> Meldung kann nicht zurückgeschrieben werden, da verändert. Dies kann bedeuten, dass neue Delayed-Willenserklärungen für
     * 			diese Meldung gebucht wurden. => keine Wiederholung! Sondern Überspringen!
     * 
     * Aufgrund der möglichen Störungen (neue Delayed-Willenserklärungen können während 1./2. entstehen, es können nicht alle meldungen
     * von delayed auf nicht-delayed zurückgesetzt werden):
     * 1. und 2. müssen solange wiederholt werden, bis bei einem Durchlauf weder bei 1. noch bei 2. noch Sätze gefunden wurden!
     * *
     * 
     * 
     */

    /**************************************************pendingAufloesenKomplett********************************/

    /**Prozentualer Fertigstellungswert von pendingAufloesenSchritt*/
    public int delayAufloesenProzentualFertig = 0;

    private DbBundle lDbBundle = null;

    private int delayAufloesenStartWillenserklaerungen;
    private int delayAufloesenEndeWillenserklaerungen;

    private int delayAufloesenStartMeldungen;
    private int delayAufloesenEndeMeldungen;

    private int delayAufloesenStartZutrittskarten;
    private int delayAufloesenEndeZutrittskarten;

    private int delayAufloesenStartStimmkarten;
    private int delayAufloesenEndeStimmkarten;

    private int delayAufloesenStartStimmkartenSecond;
    private int delayAufloesenEndeStimmkartenSecond;

    private int bearbeitungsschritt = 0;

    /**High-Level-Funktion. Alle Pendings werden aufgelöst - es erfolgt keinerlei Zwischenmeldung o.ä.. D.h. das
     * Programm "hängt" solange.
     * Sollte nur in Server-Anwendung, zu Tests, oder als Muster für die Verwendung der "Schritt-Funktionen" verwendet werden.
     * 
     * @return =-1 => Fehler aufgetreten
     */
    public int delayAufloesenKomplett(DbBundle pDbBundle) {
        int erg;
        delayAufloesenSchrittInit(pDbBundle);
        do {
            erg = delayAufloesenNaechsterSchritt();
            System.out.print("<Fertig von 100=" + delayAufloesenProzentualFertig + ">");
        } while (erg == 1);
        System.out.println();
        return (1);
    }

    /**Lower-Level-Funktion zum Pending-Auflösen aus einer Oberfläche heraus, die Rückmeldungen über den Zeitverlauf gibt.
     * delayAufloesenSchrittInit = Start, alles wird initialisiert.
     * @return = -1 => Fehler aufgetreten
     * delayAufloesenNaechsterSchritt = der nächste Block wird aufgelöst
     * @return = 0 => alles abgearbeitet.
     * 			= -1 => Fehler!
     * 			=1 Nur Teilschritt erfolgt - nochmal aufrufen
     * 
     * In delayAufloesenProzentualFertig steht jeweils (in ca. 10er Schritten) ein Wert von 0 bis 100, der den Fertigstellungsgrad des
     * aktuellen Durchlaufs wiedergibt. Achtung - es können mehrere Durchläufe auftreten - startet dann jedesmal wieder
     * von neuem!
     * 
     */
    public int delayAufloesenSchrittInit(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
        bearbeitungsschritt = 1;
        return (1);
    }

    public int delayAufloesenNaechsterSchritt() {

        int fortschritt;
        do {

            /*Durchgang Schritt 1: alle Delayed-Willenserklärungen einlesen und als non-Delayed neu buchen*/

            if (bearbeitungsschritt == 1) {
                lDbBundle.dbWillenserklaerung.initAufloesenDelayed();
                delayAufloesenStartWillenserklaerungen = 0;
                delayAufloesenEndeWillenserklaerungen = lDbBundle.dbWillenserklaerung.rcDelayedAnzahl;
                bearbeitungsschritt = 2;
                delayAufloesenProzentualFertig = 10;
                return (1);
            }

            if (bearbeitungsschritt == 2) {
                fortschritt = 0;
                while (delayAufloesenStartWillenserklaerungen < delayAufloesenEndeWillenserklaerungen) {

                    lDbBundle.dbBasis.beginTransaction();

                    lDbBundle.dbWillenserklaerung.readNextDelayed();
                    EclWillenserklaerung delayedWillen = lDbBundle.dbWillenserklaerung.rcDelayedWillenserklaerung; /*nur zum weniger Schreiben*/
                    System.out.println("delay aufloesen");
                    System.out.println("WillenserklärungIdent:"
                            + lDbBundle.dbWillenserklaerung.rcDelayedWillenserklaerung.willenserklaerungIdent);
                    System.out.println("Willenserklärung:"
                            + lDbBundle.dbWillenserklaerung.rcDelayedWillenserklaerung.willenserklaerung);

                    /*Neue Willenserklärung erstellen ("forced-nondelayed")*/

                    BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();

                    /*Technik - Eingabeparameter für "Delayed-Nachbuchung" setzen*/
                    blWillenserklaerung.ptForceNonDelay = true;
                    blWillenserklaerung.ptVerweisart = 2; /*Delayed-Nachbuchung*/
                    blWillenserklaerung.ptVerweisAufWillenserklaerung = delayedWillen.willenserklaerungIdent;

                    /*Für Identifikation*/
                    blWillenserklaerung.ptForceIdentifikationIstMeldungsIdent = true;
                    blWillenserklaerung.ptUebernehmeIdentifikation = delayedWillen.identifikationDurch;

                    blWillenserklaerung.piEclMeldungAktionaer = null;
                    blWillenserklaerung.piMeldungsIdentAktionaer = delayedWillen.meldungsIdent;
                    blWillenserklaerung.piEclMeldungGast = null;
                    blWillenserklaerung.piMeldungsIdentGast = delayedWillen.meldungsIdentGast;

                    blWillenserklaerung.piZutrittsIdent.zutrittsIdent = delayedWillen.identifikationZutrittsIdent;
                    blWillenserklaerung.piZutrittsIdent.zutrittsIdentNeben = delayedWillen.identifikationZutrittsIdentNeben;
                    blWillenserklaerung.piKlasse = delayedWillen.identifikationKlasse;
                    blWillenserklaerung.piStimmkarte = delayedWillen.identifikationStimmkarte;
                    blWillenserklaerung.piStimmkarteSecond = delayedWillen.identifikationStimmkarteSecond;

                    blWillenserklaerung.pErteiltAufWeg = delayedWillen.erteiltAufWeg;

                    /*Felder für die spezifische Willenserklärung füllen*/

                    /*Willenserklärung aufrufen bzw. Buchen*/
                    switch (delayedWillen.willenserklaerung) {
                    case KonstWillenserklaerung.zugangGast: {
                        blWillenserklaerung.zugangGast(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.abgangGast: {
                        blWillenserklaerung.abgangGast(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.sperrenZutrittsIdent: {
                        blWillenserklaerung.sperrenZutrittsIdent(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.freigebenGesperrteZutrittsIdent: {
                        blWillenserklaerung.freigebenGesperrteZutrittsIdent(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.sperrenStimmkarte: {
                        blWillenserklaerung.sperrenStimmkarte(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.freigebenGesperrteStimmkarte: {
                        blWillenserklaerung.freigebenGesperrteStimmkarte(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.sperrenStimmkarteSecond: {
                        blWillenserklaerung.sperrenStimmkarteSecond(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.freigebenGesperrteStimmkarteSecond: {
                        blWillenserklaerung.freigebenGesperrteStimmkarteSecond(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.vollmachtAnDritte: {
                        blWillenserklaerung.pEclPersonenNatJur.ident = delayedWillen.bevollmaechtigterDritterIdent;
                        blWillenserklaerung.pWillenserklaerungGeberIdent = delayedWillen.willenserklaerungGeberIdent;
                        blWillenserklaerung.vollmachtAnDritte(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.widerrufVollmachtAnDritte: {
                        if (delayedWillen.folgeBuchungFuerIdent != 0) {
                            /*Folge-Willenserklärung - diese wird nicht separat aufgelöst, da automatisch wieder neu
                             * aufgebaut
                             */
                        } else {
                            blWillenserklaerung.pEclPersonenNatJur.ident = delayedWillen.bevollmaechtigterDritterIdent;
                            blWillenserklaerung.pWillenserklaerungGeberIdent = delayedWillen.willenserklaerungGeberIdent;
                            blWillenserklaerung.widerrufVollmachtAnDritte(lDbBundle);

                        }
                        break;
                    }

                    default: {
                        /*Darf nicht vorkommen*/
                        CaBug.drucke("BlDelayedAufloesen.delayAufloesenNaechsterSchritt 001");
                        lDbBundle.dbBasis.rollbackTransaction();
                        return (-1);
                    }
                    }

                    /*Willenserklaerung als "Delayed verarbeitet" kennzeichnen und zurückspeichern*/
                    delayedWillen.delayed = 2;
                    lDbBundle.dbWillenserklaerung.update(delayedWillen);

                    lDbBundle.dbBasis.endTransaction();
                    delayAufloesenStartWillenserklaerungen++;
                    fortschritt++;
                    if (fortschritt > delayAufloesenEndeWillenserklaerungen / 4 + 1) {
                        delayAufloesenProzentualFertig += 10;
                        return (1);
                    }
                }
                bearbeitungsschritt = 3;
                delayAufloesenProzentualFertig = 50;
                return (1);

            }

            /*Durchgang Schritt 2: alle Delayed-Meldungen einlesen, checken ob Delayed-
             * Willenserklärung vorhanden ist, und ggf auf Status non-Delayed setzen*/
            if (bearbeitungsschritt == 3) {
                lDbBundle.dbMeldungen.initAufloesenDelayed();
                delayAufloesenStartMeldungen = 0;
                delayAufloesenEndeMeldungen = lDbBundle.dbMeldungen.rcDelayedAnzahl;
                bearbeitungsschritt = 4;
                delayAufloesenProzentualFertig = 60;
                return (1);

            }

            if (bearbeitungsschritt == 4) {
                fortschritt = 0;
                while (delayAufloesenStartMeldungen < delayAufloesenEndeMeldungen) {
                    lDbBundle.dbBasis.beginTransaction();

                    lDbBundle.dbMeldungen.readNextDelayed();
                    EclMeldung delayedMeldung = lDbBundle.dbMeldungen.rcDelayedMeldung; /*nur zum weniger Schreiben*/

                    int erg = 0;
                    if (delayedMeldung.klasse == 0) {
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(delayedMeldung);
                    }
                    if (delayedMeldung.klasse == 1) {
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(delayedMeldung);
                    }
                    int i1;
                    int gefDelayed = 0; /*Hierin wird ermittelt, ob es mittlerweile noch Delayed-Willenserklärungen zu
                                        dieser Meldung gibt*/
                    for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                        if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                            gefDelayed = 1;
                            break;
                        }
                    }
                    if (gefDelayed == 0) {/*Keine Delayed-Willenserklärung mehr vorhanden - Delayed in meldung zurücksetzen*/
                        delayedMeldung.delayedVorhanden = 0;
                        delayedMeldung.zutrittsIdent_Delayed = "";
                        delayedMeldung.stimmkarte_Delayed = "";
                        delayedMeldung.stimmkarteSecond_Delayed = "";
                        delayedMeldung.meldungEnthaltenInSammelkarte_Delayed = 0;
                        delayedMeldung.meldungEnthaltenInSammelkarteArt_Delayed = 0;
                        delayedMeldung.weisungVorhanden_Delayed = 0;
                        delayedMeldung.vertreterName_Delayed = "";
                        delayedMeldung.vertreterOrt_Delayed = "";
                        delayedMeldung.willenserklaerungMitVollmacht_Delayed = 0;
                        delayedMeldung.statusPraesenz_Delayed = 0;
                        delayedMeldung.statusWarPraesenz_Delayed = 0;

                        erg = lDbBundle.dbMeldungen.update(delayedMeldung);
                        /*erg wird nicht näher berücksichtigt, mögliche Ursachen können z.B. sein
                         * >durch anderen Benutzer verändert
                         * 
                         * Es erfolgt jedoch auf jeden Fall noch ein weiterer Lauf!
                         */

                    }
                    lDbBundle.dbBasis.endTransaction();
                    delayAufloesenStartMeldungen++;
                    fortschritt++;
                    if (fortschritt > delayAufloesenEndeMeldungen / 4 + 1) {
                        delayAufloesenProzentualFertig += 10;
                        return (1);
                    }

                }
                delayAufloesenProzentualFertig = 100;
                bearbeitungsschritt = 5;
                return (1);

            }

            /*Durchgang Schritt 3: alle Delayed-zutrittsIdent einlesen, checken ob Delayed-
             * Willenserklärung vorhanden ist, und ggf auf Status non-Delayed setzen*/
            /*TODO _Delay: analog für Stimmkarten und StimmkartenSecond durchführen!*/
            if (bearbeitungsschritt == 5) {
                System.out.println("<5>");
                lDbBundle.dbZutrittskarten.initAufloesenDelayed();
                delayAufloesenStartZutrittskarten = 0;
                delayAufloesenEndeZutrittskarten = lDbBundle.dbZutrittskarten.rcDelayedAnzahl;
                bearbeitungsschritt = 6;
                delayAufloesenProzentualFertig = 60;
                return (1);

            }

            if (bearbeitungsschritt == 6) {
                fortschritt = 0;
                while (delayAufloesenStartZutrittskarten < delayAufloesenEndeZutrittskarten) {
                    System.out.println("<6>");
                    lDbBundle.dbBasis.beginTransaction();

                    lDbBundle.dbZutrittskarten.readNextDelayed();
                    EclZutrittskarten delayedZutrittskarte = lDbBundle.dbZutrittskarten.rcDelayedZutrittskarten; /*nur zum weniger Schreiben*/
                    EclMeldung meldungsBuffer = null;

                    int erg = 0, i1;
                    int gefDelayed = 0; /*Hierin wird ermittelt, ob es mittlerweile noch Delayed-Willenserklärungen zu
                                        dieser Meldung gibt*/
                    if (delayedZutrittskarte.meldungsIdentGast != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedZutrittskarte.meldungsIdentGast;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedZutrittskarte.meldungsIdentGast_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedZutrittskarte.meldungsIdentGast_Delayed;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedZutrittskarte.meldungsIdentAktionaer != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedZutrittskarte.meldungsIdentAktionaer;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedZutrittskarte.meldungsIdentAktionaer_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedZutrittskarte.meldungsIdentAktionaer_Delayed;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }

                    if (gefDelayed == 0) {/*Keine Delayed-Willenserklärung mehr vorhanden - Delayed in meldung zurücksetzen*/
                        delayedZutrittskarte.delayedVorhanden = 0;
                        delayedZutrittskarte.zutrittsIdentIstGesperrt_Delayed = 0;
                        delayedZutrittskarte.zutrittsIdentVers_Delayed = 0;
                        delayedZutrittskarte.meldungsIdentGast_Delayed = 0;
                        delayedZutrittskarte.meldungsIdentAktionaer_Delayed = 0;

                        erg = lDbBundle.dbZutrittskarten.update(delayedZutrittskarte);
                        /*erg wird nicht näher berücksichtigt, mögliche Ursachen können z.B. sein
                         * >durch anderen Benutzer verändert
                         * 
                         * Es erfolgt jedoch auf jeden Fall noch ein weiterer Lauf!
                         */

                    }
                    lDbBundle.dbBasis.endTransaction();
                    delayAufloesenStartZutrittskarten++;
                    fortschritt++;
                    if (fortschritt > delayAufloesenEndeZutrittskarten / 4 + 1) {
                        delayAufloesenProzentualFertig += 10;
                        return (1);
                    }

                }
                delayAufloesenProzentualFertig = 100;
                bearbeitungsschritt = 7;
                return (1);

            }

            /*Durchgang Schritt 4: alle Delayed-Stimmkarten einlesen, checken ob Delayed-
             * Willenserklärung vorhanden ist, und ggf auf Status non-Delayed setzen*/
            if (bearbeitungsschritt == 7) {
                System.out.println("<7>");
                lDbBundle.dbStimmkarten.initAufloesenDelayed();
                delayAufloesenStartStimmkarten = 0;
                delayAufloesenEndeStimmkarten = lDbBundle.dbStimmkarten.rcDelayedAnzahl;
                System.out.println("Start" + delayAufloesenStartStimmkarten + "Ende" + delayAufloesenEndeStimmkarten);
                bearbeitungsschritt = 8;
                delayAufloesenProzentualFertig = 60;
                return (1);

            }

            if (bearbeitungsschritt == 8) {
                fortschritt = 0;
                while (delayAufloesenStartStimmkarten < delayAufloesenEndeStimmkarten) {
                    System.out.println("<8>");
                    lDbBundle.dbBasis.beginTransaction();

                    lDbBundle.dbStimmkarten.readNextDelayed();
                    EclStimmkarten delayedStimmkarte = lDbBundle.dbStimmkarten.rcDelayedStimmkarten; /*nur zum weniger Schreiben*/
                    EclMeldung meldungsBuffer = null;

                    int erg = 0, i1;
                    int gefDelayed = 0; /*Hierin wird ermittelt, ob es mittlerweile noch Delayed-Willenserklärungen zu
                                        dieser Meldung gibt*/
                    if (delayedStimmkarte.meldungsIdentGast != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarte.meldungsIdentGast;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarte.meldungsIdentGast_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarte.meldungsIdentGast_Delayed;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarte.meldungsIdentAktionaer != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarte.meldungsIdentAktionaer;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarte.meldungsIdentAktionaer_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarte.meldungsIdentAktionaer_Delayed;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }

                    if (gefDelayed == 0) {/*Keine Delayed-Willenserklärung mehr vorhanden - Delayed in meldung zurücksetzen*/
                        delayedStimmkarte.delayedVorhanden = 0;
                        delayedStimmkarte.stimmkarteIstGesperrt_Delayed = 0;
                        delayedStimmkarte.stimmkarteVers_Delayed = 0;
                        delayedStimmkarte.meldungsIdentGast_Delayed = 0;
                        delayedStimmkarte.meldungsIdentAktionaer_Delayed = 0;

                        erg = lDbBundle.dbStimmkarten.update(delayedStimmkarte);
                        /*erg wird nicht näher berücksichtigt, mögliche Ursachen können z.B. sein
                         * >durch anderen Benutzer verändert
                         * 
                         * Es erfolgt jedoch auf jeden Fall noch ein weiterer Lauf!
                         */

                    }
                    lDbBundle.dbBasis.endTransaction();
                    delayAufloesenStartStimmkarten++;
                    fortschritt++;
                    if (fortschritt > delayAufloesenEndeStimmkarten / 4 + 1) {
                        delayAufloesenProzentualFertig += 10;
                        return (1);
                    }

                }
                delayAufloesenProzentualFertig = 100;
                bearbeitungsschritt = 9;
                return (1);

            }

            /*Durchgang Schritt 5: alle Delayed-StimmkartenSecond einlesen, checken ob Delayed-
             * Willenserklärung vorhanden ist, und ggf auf Status non-Delayed setzen*/
            if (bearbeitungsschritt == 9) {
                System.out.println("<9>");
                lDbBundle.dbStimmkartenSecond.initAufloesenDelayed();
                delayAufloesenStartStimmkartenSecond = 0;
                delayAufloesenEndeStimmkartenSecond = lDbBundle.dbStimmkartenSecond.rcDelayedAnzahl;
                bearbeitungsschritt = 10;
                delayAufloesenProzentualFertig = 60;
                return (1);

            }

            if (bearbeitungsschritt == 10) {
                System.out.println("<10>");
                fortschritt = 0;
                while (delayAufloesenStartStimmkartenSecond < delayAufloesenEndeStimmkartenSecond) {
                    lDbBundle.dbBasis.beginTransaction();

                    lDbBundle.dbStimmkartenSecond.readNextDelayed();
                    EclStimmkartenSecond delayedStimmkarteSecond = lDbBundle.dbStimmkartenSecond.rcDelayedStimmkartenSecond; /*nur zum weniger Schreiben*/
                    EclMeldung meldungsBuffer = null;

                    int erg = 0, i1;
                    int gefDelayed = 0; /*Hierin wird ermittelt, ob es mittlerweile noch Delayed-Willenserklärungen zu
                                        		dieser Meldung gibt*/
                    if (delayedStimmkarteSecond.meldungsIdentGast != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarteSecond.meldungsIdentGast;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarteSecond.meldungsIdentGast_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarteSecond.meldungsIdentGast_Delayed;
                        meldungsBuffer.klasse = 0;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldungGast(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarteSecond.meldungsIdentAktionaer != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarteSecond.meldungsIdentAktionaer;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }
                    if (delayedStimmkarteSecond.meldungsIdentAktionaer_Delayed != 0) {
                        meldungsBuffer = new EclMeldung();
                        meldungsBuffer.meldungsIdent = delayedStimmkarteSecond.meldungsIdentAktionaer_Delayed;
                        meldungsBuffer.klasse = 1;
                        erg = lDbBundle.dbWillenserklaerung.leseZuMeldung(meldungsBuffer);
                        for (i1 = 0; i1 < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i1++) {
                            if (lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i1).delayed == 1) {
                                gefDelayed = 1;
                                break;
                            }
                        }

                    }

                    if (gefDelayed == 0) {/*Keine Delayed-Willenserklärung mehr vorhanden - Delayed in meldung zurücksetzen*/
                        delayedStimmkarteSecond.delayedVorhanden = 0;
                        delayedStimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed = 0;
                        delayedStimmkarteSecond.stimmkarteSecondVers_Delayed = 0;
                        delayedStimmkarteSecond.meldungsIdentGast_Delayed = 0;
                        delayedStimmkarteSecond.meldungsIdentAktionaer_Delayed = 0;

                        erg = lDbBundle.dbStimmkartenSecond.update(delayedStimmkarteSecond);
                        /*erg wird nicht näher berücksichtigt, mögliche Ursachen können z.B. sein
                         * >durch anderen Benutzer verändert
                         * 
                         * Es erfolgt jedoch auf jeden Fall noch ein weiterer Lauf!
                         */

                    }
                    lDbBundle.dbBasis.endTransaction();
                    delayAufloesenStartStimmkartenSecond++;
                    fortschritt++;
                    if (fortschritt > delayAufloesenEndeStimmkartenSecond / 4 + 1) {
                        delayAufloesenProzentualFertig += 10;
                        return (1);
                    }

                }
                delayAufloesenProzentualFertig = 100;
                bearbeitungsschritt = 11;
                return (1);

            }

            bearbeitungsschritt = 1;
        } while (delayAufloesenEndeWillenserklaerungen != 0 || delayAufloesenEndeMeldungen != 0
                || delayAufloesenEndeZutrittskarten != 0 || delayAufloesenEndeStimmkarten != 0
                || delayAufloesenEndeStimmkartenSecond != 0);

        return (0);
    }

}
