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

import java.util.LinkedList;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrage;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrageRC;

public class BlPraesenzStatus {

    private boolean logDrucken = true;
    private int logDruckenInt=10;
    
    private DbBundle lDbBundle = null;

    /**Die Meldung, die gerade bearbeitet (und dann in Liste) aufgenommen wird.
     * Ob Gast oder Aktionär, durch entsprechende Kodierung in lEclmeldung.
     * Ersetzt meldungsIdentAktionaer, meldungsIdentGast, bzw. lEclmeldungAktionaer, lEclMeldungGast*/
    private int meldungsIdent;
    private EclMeldung lEclMeldung = null;

    /**Rückgabewerte**/
    public WEPraesenzStatusabfrageRC rWEPraesenzStatusabfrageRC = new WEPraesenzStatusabfrageRC();

    public BlPraesenzStatus(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /************************Initialisieren der restlichen Returnwerte******************/
    private void statusabfrage_initWEPraesenzStatusabfrageRC() {
        rWEPraesenzStatusabfrageRC.meldungen = new LinkedList<EclMeldung>();
        rWEPraesenzStatusabfrageRC.initialPasswort = new LinkedList<String>();
        rWEPraesenzStatusabfrageRC.appVollmachtMussNochVorgelegtWerden = new LinkedList<Boolean>();
        rWEPraesenzStatusabfrageRC.appPersonMussMitAppPersonUeberprueftWerden = new LinkedList<Boolean>();
        rWEPraesenzStatusabfrageRC.appVertretendePerson = new LinkedList<EclPersonenNatJur>();
        rWEPraesenzStatusabfrageRC.zutrittskarten = new LinkedList<EclZutrittskarten>();
        rWEPraesenzStatusabfrageRC.stimmkarten = new LinkedList<EclStimmkarten>();
        rWEPraesenzStatusabfrageRC.stimmkartenSecond = new LinkedList<EclStimmkartenSecond>();
        rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur = new LinkedList<Integer>();
        rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur = new LinkedList<Integer>();
        rWEPraesenzStatusabfrageRC.aktionaerVollmachten = new LinkedList<EclWillensErklVollmachtenAnDritte[]>();

        rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer = new LinkedList<EclZutrittsIdent>();
        rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten = new LinkedList<EclZutrittsIdent[]>();

        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer = new LinkedList<String[]>();
        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer = new LinkedList<String>();
        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten = new LinkedList<String[][]>();
        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten = new LinkedList<String[]>();

        rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer = new LinkedList<int[]>();
        rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten = new LinkedList<int[][]>();
        rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenText = new LinkedList<String[]>();

        rWEPraesenzStatusabfrageRC.inSammelkarten = new LinkedList<EclMeldungZuSammelkarte[]>();
        rWEPraesenzStatusabfrageRC.erforderlicheAktionen = new LinkedList<>();
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer = new LinkedList<>();
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle = new LinkedList<>();
    }

    /*******************Initialisieren der Returnwerte innerhalb einer Liste je Meldung*******************/
    private void statusabfrage_initWEPraesenzStatusabfrageRCJeMeldung() {
        rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur.add(0);
        rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.add(0);
        rWEPraesenzStatusabfrageRC.erforderlicheAktionen.add(new LinkedList<Integer>());
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.add(new LinkedList<Integer>());
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.add(new LinkedList<Integer>());

        rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.add(new EclZutrittsIdent());

        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer.add(new String[] { "", "", "", "", "", "" });
        rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer = new LinkedList<String>();
        /*Kann erst beim Verwenden initialisiert werden, wenn Dimensionen feststehen:
        zugeordneteStimmkartenVollmachten, zugeordneteStimmkartenSecondVollmachten, zugeordneteEintrittskartenVollmachten
        Initialisierung erfolgt bei jeIdentifikation_leseVollmachtenAnDritte*/

        rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.add(new int[] { 0, 0, 0, 0, 0, 0 });
        /*Kann erst beim Verwenden initialisiert werden, wenn Dimensionen feststehen:
        stimmkartenZuordnungenVollmachten
        Initialisierung erfolgt bei jeIdentifikation_leseVollmachtenAnDritte*/

        rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenText.add(new String[] { "", "", "", "", "", "" });

    }

    /**********************Je vorhandener Identifikation: Meldung und Idents********************
     * 
     * Ergänzen von:
     * rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer
     * rWEPraesenzStatusabfrageRC.zutrittskarten
     * rWEPraesenzStatusabfrageRC.stimmkarten
     * rWEPraesenzStatusabfrageRC.stimmkartenSecond
     * 
     * Füllt: 
     * meldungsIdent
     * lEclMeldung
     * anzMeldungenAktionaere
     */
    private int fuerIdentifikation_leseMeldungUndIdent(int iIdentifikation) {
        int rc;
        int returnWertGesamt = 1;
        String hIdentifikationsnummer = "";
        String hIdentifikationsnummerNeben = "";

        if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer
                .get(iIdentifikation) == KonstKartenklasse.aktionaersnummer) {
            if (logDrucken) {
                CaBug.druckeInfo(
                        "BlPraesenzStatus.fuerIdentifikation_leseMeldungUndIdent: KonstKartenklasse.aktionaersnummer");
            }

            /*TODO App: Ist nur temporär, da App bei Eintrittskarte auf Vertreter nur die Aktionärsnummer liefert.
             * Insbesondere ist nicht abgefangen: 2 Meldungen mit Vertreter auf eine Aktionärsnummer!
             */
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.aktionaersnummer = rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation);
            rc = lDbBundle.dbMeldungen.leseZuAktionaersnummer(lMeldung);
            if (rc == 1) {/*Umschießen auf zutrittsIdent*/
                lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                rWEPraesenzStatusabfrageRC.identifikationsnummer.set(iIdentifikation, lMeldung.zutrittsIdent);
                rWEPraesenzStatusabfrageRC.identifikationsnummerNeben.set(iIdentifikation, "00");
                rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.set(iIdentifikation,
                        KonstKartenklasse.eintrittskartennummer);
            } else {
                rWEPraesenzStatusabfrageRC.identifikationsnummer.set(iIdentifikation, "00000");
                rWEPraesenzStatusabfrageRC.identifikationsnummerNeben.set(iIdentifikation, "00");
                rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.set(iIdentifikation,
                        KonstKartenklasse.eintrittskartennummer);
            }
        }

        hIdentifikationsnummer = rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation);
        hIdentifikationsnummerNeben = rWEPraesenzStatusabfrageRC.identifikationsnummerNeben.get(iIdentifikation);
        EclZutrittsIdent lEclZutrittsIdent = new EclZutrittsIdent();
        lEclZutrittsIdent.zutrittsIdent = hIdentifikationsnummer;
        lEclZutrittsIdent.zutrittsIdentNeben = hIdentifikationsnummerNeben;

        if (logDrucken) {
            CaBug.druckeInfo(
                    "BlPraesenzStatus.fuerIdentifikation_leseMeldungUndIdent: iIdentifikation=" + iIdentifikation);
            CaBug.druckeInfo("BlPraesenzStatus.fuerIdentifikation_leseMeldungUndIdent: hIdentifikationsnummer="
                    + hIdentifikationsnummer);
        }

        /*MeldungsIdent ermitteln - für Einlesen der zugeordneten Meldungen (Gast oder Aktionär)*/
        switch (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation)) {

        case KonstKartenklasse.eintrittskartennummer: {
            if (logDrucken) {
                CaBug.druckeInfo("BlPraesenzStatus.fuerIdentifikation_leseMeldungUndIdent: case eintrittskartennummer");
            }
            EclZutrittskarten lEclZutrittskarten = null;

            rc = lDbBundle.dbZutrittskarten.readAktionaer(lEclZutrittsIdent, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenz.fuerIdentifikation_leseMeldungUndIdents 001");
            }
            if (rc < 1) {/*Zutrittskarte nicht gefunden*/
                rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                        CaFehler.pmZutrittsIdentNichtVorhanden);
                if (returnWertGesamt == 1) {
                    returnWertGesamt = CaFehler.pmZutrittsIdentNichtVorhanden;
                }
            } else {
                lEclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                meldungsIdent = lEclZutrittskarten.liefereGueltigeMeldeIdent();
                if (lEclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmZutrittsIdentIstStorniert);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmZutrittsIdentIstStorniert;
                    }
                }
            }
            rWEPraesenzStatusabfrageRC.zutrittskarten.add(lEclZutrittskarten);
            rWEPraesenzStatusabfrageRC.stimmkarten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkartenSecond.add(null);
            break;
        }
        case KonstKartenklasse.gastkartennummer: {
            EclZutrittskarten lEclZutrittskarten = null;
            rc = lDbBundle.dbZutrittskarten.readGast(lEclZutrittsIdent, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenz.statusabfrage 003");
            }
            if (rc < 1) {/*Zutrittskarte nicht gefunden*/
                rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                        CaFehler.pmZutrittsIdentNichtVorhanden);
                if (returnWertGesamt == 1) {
                    returnWertGesamt = CaFehler.pmZutrittsIdentNichtVorhanden;
                }
            } else {
                lEclZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                meldungsIdent = lEclZutrittskarten.meldungsIdentGast;
                if (lEclZutrittskarten.zutrittsIdentWurdeGesperrt()) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmZutrittsIdentIstStorniert);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmZutrittsIdentIstStorniert;
                    }
                }
            }
            rWEPraesenzStatusabfrageRC.zutrittskarten.add(lEclZutrittskarten);
            rWEPraesenzStatusabfrageRC.stimmkarten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkartenSecond.add(null);
            break;
        }
        case KonstKartenklasse.stimmkartennummer: {
            EclStimmkarten lEclStimmkarten = null;
            rc = lDbBundle.dbStimmkarten.read(hIdentifikationsnummer, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenz.statusabfrage 005");
            }
            if (rc < 1) {/*Stimmkarte nicht gefunden*/
                rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                        CaFehler.pmStimmkarteNichtVorhanden);
                if (returnWertGesamt == 1) {
                    returnWertGesamt = CaFehler.pmStimmkarteNichtVorhanden;
                }
            } else {
                lEclStimmkarten = lDbBundle.dbStimmkarten.ergebnisPosition(0);
                meldungsIdent = lEclStimmkarten.liefereGueltigeMeldeIdent();
                ;
                if (lEclStimmkarten.stimmkarteIstGesperrt == 2) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmStimmkarteGesperrt);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmStimmkarteGesperrt;
                    }
                }
                /*TODO $9 Prüfen, wie "nicht für Abstimmung aktive Stimmkarten" wo überprüft werden sollen*/
                if (lEclStimmkarten.stimmkarteIstGesperrt == 1
                        && rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.stimmabschnittsnummer) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmStimmkarteNichtFuerAbstimmungAktiv);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmStimmkarteNichtFuerAbstimmungAktiv;
                    }
                }
            }
            rWEPraesenzStatusabfrageRC.zutrittskarten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkarten.add(lEclStimmkarten);
            rWEPraesenzStatusabfrageRC.stimmkartenSecond.add(null);
            break;
        }
        case KonstKartenklasse.stimmkartennummerSecond: {
            EclStimmkartenSecond lEclStimmkartenSecond = null;
            rc = lDbBundle.dbStimmkartenSecond.read(hIdentifikationsnummer, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenz.statusabfrage 007");
            }
            if (rc < 1) {/*Stimmkarte nicht gefunden*/
                rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                        CaFehler.pmStimmkarteSecondNichtVorhanden);
                if (returnWertGesamt == 1) {
                    returnWertGesamt = CaFehler.pmStimmkarteSecondNichtVorhanden;
                }
            } else {
                lEclStimmkartenSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0);
                meldungsIdent = lEclStimmkartenSecond.liefereGueltigeMeldeIdent();
                if (lEclStimmkartenSecond.stimmkarteSecondIstGesperrt == 2) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmStimmkarteSecondGesperrt);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmStimmkarteSecondGesperrt;
                    }
                }
                /*TODO $9 Prüfen, wie "nicht für Abstimmung aktive Stimmkarten" wo überprüft werden sollen*/
                if (lEclStimmkartenSecond.stimmkarteSecondIstGesperrt == 1
                        && rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.stimmabschnittsnummer) {
                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation,
                            CaFehler.pmStimmkarteSecondNichtFuerAbstimmungAktiv);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = CaFehler.pmStimmkarteSecondNichtFuerAbstimmungAktiv;
                    }
                }
            }
            rWEPraesenzStatusabfrageRC.zutrittskarten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkarten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkartenSecond.add(lEclStimmkartenSecond);
            break;
        }
        case KonstKartenklasse.appIdent: { /*Kann hier nicht auftreten!*/
            break;
        }
        case KonstKartenklasse.aktionaersnummer: { /*Aktionärsnummer - denkbar! Vorher ZutrittsIdents ermitteln*/

        }

        }
        if (meldungsIdent != 0) {
            lEclMeldung = new EclMeldung();
            lEclMeldung.meldungsIdent = meldungsIdent;
            rc = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lEclMeldung);
            if (rc != 1) {
                CaBug.drucke("BlPraesenz.statusabfrage 009");
            }
            lEclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            if (lEclMeldung.meldungIstEinAktionaer()) {
                rWEPraesenzStatusabfrageRC.anzMeldungenAktionaere++;
            }
            rWEPraesenzStatusabfrageRC.meldungen.add(lEclMeldung);
            
            String lInitialPasswort="";
            if (lEclMeldung.meldungIstEinAktionaer()) {
                if (lEclMeldung.meldungstyp!=KonstMeldung.KARTENART_SAMMELKARTE) {
                    String lKennung=lEclMeldung.aktionaersnummer;
                    lDbBundle.dbLoginDaten.read_loginKennung(lKennung);
                    if (lDbBundle.dbLoginDaten.anzErgebnis()!=0) {
                        lInitialPasswort=lDbBundle.dbLoginDaten.ergebnisPosition(0).lieferePasswortInitialClean();
                    }
                    
                }
            }
            rWEPraesenzStatusabfrageRC.initialPasswort.add(lInitialPasswort);
            
        } else {
            rWEPraesenzStatusabfrageRC.meldungen.add(null);
            rWEPraesenzStatusabfrageRC.initialPasswort.add(null);
        }

        return returnWertGesamt;
    }

    /******************Einlesen aller Vollmachten an Dritte ***********************
     * für lEclMeldung in
     * rWEPraesenzStatusabfrageRC.aktionaerVollmachten. Achtung: enthält Stornierte
     * und gültige!
     * Funktion kann für Gast und Aktionär aufgerufen werden - Abfrage innerhalb dieser
     * Funktion.
     * 
     * Initialisiert gleichzeitig (aber nur Initialisierung!!!):
     *  zugeordneteEintrittskartenVollmachten
     * 	zugeordneteStimmkartenVollmachten, zugeordneteStimmkartenSecondVollmachten
     *	stimmkartenZuordnungenVollmachten, zugeordneteStimmkartenSecondVollmachten
     */
    private void jeIdentifikation_leseVollmachtenAnDritte() {
        if (lEclMeldung != null && lEclMeldung.meldungIstEinAktionaer()) {

            BlWillenserklaerung lBlWillenserklaerung = new BlWillenserklaerung();
            lBlWillenserklaerung.setzeDbBundle(lDbBundle);
            lBlWillenserklaerung.piMeldungsIdentAktionaer = lEclMeldung.meldungsIdent;
            if (logDrucken) {
                CaBug.druckeInfo("BlPraesenzStatus.jeIdentifikation_leseVollmachtenAnDritte meldungsIdent="
                        + lEclMeldung.meldungsIdent);
            }
            lBlWillenserklaerung.einlesenVollmachtenAnDritte();
            rWEPraesenzStatusabfrageRC.aktionaerVollmachten.add(lBlWillenserklaerung.rcVollmachtenAnDritte);

            int anzahlVollmachten = 0;
            if (lBlWillenserklaerung.rcVollmachtenAnDritte != null) {
                anzahlVollmachten = lBlWillenserklaerung.rcVollmachtenAnDritte.length;
            }
            if (logDrucken) {
                CaBug.druckeInfo("BlPraesenzStatus.jeIdentifikation_leseVollmachtenAnDritte anzahlVollmachten="
                        + anzahlVollmachten);
            }
            if (anzahlVollmachten == 0) {
                rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.add(null);
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten.add(null);
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten.add(null);
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.add(null);
            } else {
                EclZutrittsIdent[] hzugeordneteEintrittskartenVollmachten = new EclZutrittsIdent[anzahlVollmachten];
                String[][] hzugeordneteStimmkartenVollmachten = new String[anzahlVollmachten][6];
                String[] hzugeordneteStimmkartenSecondVollmachten = new String[anzahlVollmachten];
                int[][] hstimmkartenZuordnungenVollmachten = new int[anzahlVollmachten][6];
                for (int i = 0; i < anzahlVollmachten; i++) {
                    hzugeordneteEintrittskartenVollmachten[i] = new EclZutrittsIdent();
                    hzugeordneteStimmkartenSecondVollmachten[i] = "";
                    for (int i1 = 0; i1 < 6; i1++) {
                        hzugeordneteStimmkartenVollmachten[i][i1] = "";
                        hstimmkartenZuordnungenVollmachten[i][i1] = 0;
                    }
                }
                rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten
                        .add(hzugeordneteEintrittskartenVollmachten);
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten.add(hzugeordneteStimmkartenVollmachten);
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten
                        .add(hzugeordneteStimmkartenSecondVollmachten);
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.add(hstimmkartenZuordnungenVollmachten);
            }

        } else {
            rWEPraesenzStatusabfrageRC.aktionaerVollmachten.add(null);

            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.add(null);

            rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten.add(null);
            rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten.add(null);
            rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.add(null);

        }
    }

    /*****************Ermitteln: zu Sammelkarten zugeordnet***********************************
     * für lEclMeldungAktionaer in rWEPraesenzStatusabfrageRC.inSammelkarten all die Sammelkarten
     * eintragen, denen die Meldung zugeordnet ist*/
    private void jeIdentifikation_ermittelnZuSammelkarten() {
        int rc, rc1;
        if (lEclMeldung != null && lEclMeldung.meldungIstEinAktionaer()) {
            rc = lDbBundle.dbMeldungZuSammelkarte.leseZuMeldung(lEclMeldung);
            if (rc > 0) {
                int anz = 0;
                for (int i1 = 0; i1 < rc; i1++) {
                    if (lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1).zuordnungIstNochGueltig()) {
                        anz++;
                    }
                }
                EclMeldungZuSammelkarte[] aEclMeldungZuSammelkarte = new EclMeldungZuSammelkarte[anz];
                anz = 0;
                for (int i1 = 0; i1 < rc; i1++) {
                    if (lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1).zuordnungIstNochGueltig()) {

                        EclMeldungZuSammelkarte hEclMeldungZuSammelkarte;
                        hEclMeldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1);
                        aEclMeldungZuSammelkarte[anz] = hEclMeldungZuSammelkarte;

                        EclMeldung lSammelmeldung = new EclMeldung();
                        lSammelmeldung.meldungsIdent = aEclMeldungZuSammelkarte[anz].sammelIdent;
                        rc1 = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lSammelmeldung);
                        if (rc1 != 1) {
                            CaBug.drucke("BlPraesenz.statusabfrage 011");
                        }
                        lSammelmeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                        aEclMeldungZuSammelkarte[anz].name = lSammelmeldung.name;
                        aEclMeldungZuSammelkarte[anz].ort = lSammelmeldung.ort;
                        aEclMeldungZuSammelkarte[anz].skIst = lSammelmeldung.skIst;
                        anz++;
                    }
                }
                rWEPraesenzStatusabfrageRC.inSammelkarten.add(aEclMeldungZuSammelkarte);
            } else {/*Aktionär vorhanden, aber keine Sammelkartenzuordnung*/
                rWEPraesenzStatusabfrageRC.inSammelkarten.add(null);
            }
        } else {/*Kein Aktionär vorhanden => auch keine Sammelkartenzuordnung vorhanden*/
            rWEPraesenzStatusabfrageRC.inSammelkarten.add(null);
        }

    }

    /**Nur bei Appident: ermitteln der Person, die für die Identifikation von der App geliefert wurde (= die Person, die 
     * bei der Frage "Sind Sie" für diese Identifikation ausgewählt wurde).
     * Kann "Null" sein (wenn die AppIdent keine Personen-Ident geliefert hat)
     */
    private int jeIdentifikation_appErmittelnVertretendePerson(int iIdentifikation) {
        if (iIdentifikation > 0) {
            int personIdent = lBlNummernformen.rcAppVertretendePersonIdent.get(iIdentifikation);
            EclPersonenNatJur lEclPersonNatJur = new EclPersonenNatJur();
            int rc = lDbBundle.dbPersonenNatJur.read(personIdent);
            if (rc < 1) {
                rc = CaFehler.pmPersonNichtVorhanden;
            } else {
                rc = 1;
                lEclPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            }

            rWEPraesenzStatusabfrageRC.appVertretendePerson.add(lEclPersonNatJur);
        } else {
            EclPersonenNatJur lEclPersonNatJur = new EclPersonenNatJur();
            lEclPersonNatJur.ident = -1;
            rWEPraesenzStatusabfrageRC.appVertretendePerson.add(lEclPersonNatJur);
        }
        return 1;
    }

    BlNummernformen lBlNummernformen = null;

    /**
     * Hinweis: Dies ist eine "Teil-Status-Ermittlung", die von BlPraesenzAkkreditierung.statusabfrage verwendet wird.
     * Aufgaben:
     * > Ergebnis wird in rWEPraesenzStatusabfrageRC abgelegt (soweit hier ermittelt; restliche Felder in
     * 		rWEPraesenzStatusabfrageRC werden hier initialisiert für die weitere Verwendung)
     * > Ermittelt werden:
     * 
     * 		rWEPraesenzStatusabfrageRC.rc (Gesamtreturn-Wert)
     * 
     * 		Die Listen aus BlNummernform gemäß übergebener Nummernform:
     * 		rWEPraesenzStatusabfrageRC.identifikationsnummer 
     * 		rWEPraesenzStatusabfrageRC.identifikationsnummerNeben 
     * 		rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer
     * 		rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer 
     * 
     * 		Gesamtwerte aus BlNummernform:
     * 		rWEPraesenzStatusabfrageRC.rcKartenklasse
     * 		rWEPraesenzStatusabfrageRC.rcKartenart
     * 		rWEPraesenzStatusabfrageRC.rcStimmkarteSubNummernkreis
     * 		rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent
     * 
     * 		Ermittelt aus
     * 		rWEPraesenzStatusabfrageRC.zutrittskarten (ermittelt aus obigen Feldern)
     * 		rWEPraesenzStatusabfrageRC.stimmkarten (ermittelt aus obigen Feldern)
     * 		rWEPraesenzStatusabfrageRC.stimmkartenSecond (ermittelt aus obigen Feldern)
     * 
     * 		Ergänzt um:
     * 		rWEPraesenzStatusabfrageRC.meldungen (wird anhand der Identifikationssnummern eingelesen)
     * 		rWEPraesenzStatusabfrageRC.aktionaerVollmachten (wird zu den Meldungen hinzugelesen)
     * 		rWEPraesenzStatusabfrageRC.inSammelkarten (wird zu den Meldungen ermittelt)
     * 		rWEPraesenzStatusabfrageRC
     * 		rWEPraesenzStatusabfrageRC
     * 	
     * verwendungszweck:
     * 1= Akkreditierung
     * 2= Abstimmung
     * 3= SRV
     */
    public WEPraesenzStatusabfrageRC statusabfrage(WEPraesenzStatusabfrage lWEPraesenzStatusabfrage,
            int verwendungszweck) {
        int rc;
        int iIdentifikation;

        CaBug.druckeLog("A", logDruckenInt, 10);
        
        /*Anzahl der verschiedenen, vorhandenen/gültigen Meldungen, die vertreten werden*/
        int returnWertGesamt = 1;

        /*Übergebenen String aufbereiten*/
        lBlNummernformen = new BlNummernformen(lDbBundle);
        rc = lBlNummernformen.dekodiere(lWEPraesenzStatusabfrage.identifikationString,
                lWEPraesenzStatusabfrage.kartenklasse);
        if (rc < 0) {
            CaBug.druckeLog("nach blNummernform.dekodiere: rc="+rc, logDruckenInt, 10);
            rWEPraesenzStatusabfrageRC.rc = rc;
            return rWEPraesenzStatusabfrageRC;
        }

        rWEPraesenzStatusabfrageRC.identifikationsnummer = lBlNummernformen.rcIdentifikationsnummer;
        rWEPraesenzStatusabfrageRC.identifikationsnummerNeben = lBlNummernformen.rcIdentifikationsnummerNeben;
        rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer = lBlNummernformen.rcKartenklasseZuIdentifikationsnummer;
        rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer = lBlNummernformen.rcRcZuIdentifikationsnummer;
        
        rWEPraesenzStatusabfrageRC.rcPasswort = lBlNummernformen.rcPasswort;

        if (lBlNummernformen.rcIstAppIdent) {
            rWEPraesenzStatusabfrageRC.rcKartenklasse = KonstKartenklasse.appIdent;
        } else {
            rWEPraesenzStatusabfrageRC.rcKartenklasse = lBlNummernformen.rcKartenklasse;
        }
        rWEPraesenzStatusabfrageRC.rcKartenart = lBlNummernformen.rcKartenart;
        rWEPraesenzStatusabfrageRC.rcStimmkarteSubNummernkreis = lBlNummernformen.rcStimmkarteSubNummernkreis;

        /*++++++++++++++++Nun direkt von der Identifikationsnummer abhängige Daten einlesen bzw. ermitteln+++++++++++++*/
        statusabfrage_initWEPraesenzStatusabfrageRC();
        CaBug.druckeLog("B", logDruckenInt, 10);

        for (iIdentifikation = 0; iIdentifikation < rWEPraesenzStatusabfrageRC.identifikationsnummer
                .size(); iIdentifikation++) {
            CaBug.druckeLog("iIdentifikation="+iIdentifikation, logDruckenInt, 10);
            statusabfrage_initWEPraesenzStatusabfrageRCJeMeldung();

            if (rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.get(iIdentifikation) == 1) {

                rc = fuerIdentifikation_leseMeldungUndIdent(iIdentifikation);
                if (returnWertGesamt == 1) {
                    returnWertGesamt = rc;
                }

                if (verwendungszweck == 1 && lBlNummernformen.rcIstAppIdent) {
                    /*nur bei App: Person, die je EK als "Vertretende Person" in der App gespeichert ist, einlesen*/
                    rc = jeIdentifikation_appErmittelnVertretendePerson(iIdentifikation);
                    if (returnWertGesamt == 1) {
                        returnWertGesamt = rc;
                    }
                }

                if (verwendungszweck == 1) {
                    /*Vollmachten an Dritte zulesen*/
                    jeIdentifikation_leseVollmachtenAnDritte();
                }

                if (verwendungszweck == 1) {
                    /*Zu Sammelkarten zugeordnet*/
                    jeIdentifikation_ermittelnZuSammelkarten();
                }

            }
        }

        /*Bei AppID: handelnde PersonNatJur*/
        rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent = lBlNummernformen.rcPersonenIdent;

        rWEPraesenzStatusabfrageRC.rc = returnWertGesamt;
        return rWEPraesenzStatusabfrageRC;
    }

}
