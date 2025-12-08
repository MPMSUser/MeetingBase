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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchen;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrage;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrageRC;

/*TODO #1 Präsenz - Widerruf von Vollmacht an Dritte, wenn die Karte bereits präsent ist. Das ist derzeit in BlWillenserklaerung noch nicht abgefangen.
 * Ist auch schwierig dies aktuell zu ändern, da dann App nicht mehr vorführbar (wobei auch das bei der App nicht vollständig funktioniert!
 */

public class BlPraesenzAkkreditierung {

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /*****Variablen für Statusabfrage und Buchen******************/
    /**Die Meldung, die gerade bearbeitet (und dann in Liste) aufgenommen wird.
     * Ob Gast oder Aktionär, durch entsprechende Kodierung in lEclmeldung.
     * Ersetzt meldungsIdentAktionaer, meldungsIdentGast, bzw. lEclmeldungAktionaer, lEclMeldungGast*/
    private int meldungsIdent;
    private EclMeldung lEclMeldung = null;

    /**Rückgabewerte**/
    public WEPraesenzStatusabfrageRC rWEPraesenzStatusabfrageRC = new WEPraesenzStatusabfrageRC();

    public BlPraesenzAkkreditierung(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    private int kannNichtPersoenlichPraesentWegenSammelkarte = 0; //Ist am Vorrangigsten!

    /**==1 => für iIdentifikation gibt es mindestens eine noch gültige Sammelkartenzuordnung*/
    private int sammelkartenZuordnungVorhanden = 0;

    private int sammelkartenZuordnungKannUndMussWiderrufenWerden = 0;
    private int sammelkartenZuordnungAktivKannUndMussDeaktiviertWerden = 0;

    /**************Setze interne Variablen für Bestimmung von Aktionen wg. Sammelkarten***********/
    private void jeIdentifikation_setzeInterneMoeglichkeitenWegenSammelkarten(int iIdentifikation) {
        sammelkartenZuordnungVorhanden = 0;
        sammelkartenZuordnungKannUndMussWiderrufenWerden = 0;
        sammelkartenZuordnungAktivKannUndMussDeaktiviertWerden = 0;
        if (rWEPraesenzStatusabfrageRC.inSammelkarten.get(iIdentifikation) != null) { //Sammelkarten vorhanden
            /*Alle zugeordneten Sammelkarten prüfen im Hinblick auf Stornierung - egal ob Zugeordnet oder nicht*/
            for (int i1 = 0; i1 < rWEPraesenzStatusabfrageRC.inSammelkarten.get(iIdentifikation).length; i1++) {
                EclMeldungZuSammelkarte lEclMeldungZuSammelkarte = rWEPraesenzStatusabfrageRC.inSammelkarten.get(iIdentifikation)[i1];
                if (lEclMeldungZuSammelkarte.zuordnungIstNochGueltig()) {
                    sammelkartenZuordnungVorhanden = 1;
                    if (lDbBundle.param.paramAkkreditierung.pPraesenzStornierenAusSammelZwingend[lEclMeldungZuSammelkarte.skIst] == 1) {
                        if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzStornierenAusSammelMoeglich[lEclMeldungZuSammelkarte.skIst] == 0) {
                            kannNichtPersoenlichPraesentWegenSammelkarte = 1;
                        } else {
                            sammelkartenZuordnungKannUndMussWiderrufenWerden = 1;
                        }
                    }
                }
            }

            /*Ist eine Sammelkarte aktiv? Wenn ja, dann diese analog oben überprüfen*/
            if (lEclMeldung.meldungEnthaltenInSammelkarte != 0) {
                if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzDeaktivierenAusSammelMoeglich[lEclMeldung.meldungEnthaltenInSammelkarteArt] == 0) {
                    kannNichtPersoenlichPraesentWegenSammelkarte = 1;
                } else {
                    sammelkartenZuordnungAktivKannUndMussDeaktiviertWerden = 1;
                }
            }

        }

    }

    /***************Überprüfen und füllen der erforderlichen Zuordnung Stimmkarte und StimmkarteSecond***
     * abhgängig von Gattung der lEclMeldungAktionaer*/
    private void jeIdentifikation_pruefeFuelleZuordnungStimmkarten(int iIdentifikation, int pGattung) {
        int[] zuVerwendendeParameterZuordnung = null;
        int zuVerwendendeParameterZuordnungSecond = 0;
        String[] zuVerwendendeParameterZuordnungText = null;
        String zuVerwendendeParameterZuordnungTextSecond = "";

        if (rWEPraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.appIdent) {
            /*App-Ident - andere Vorgehensweise*/
            zuVerwendendeParameterZuordnung = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[pGattung - 1];
            zuVerwendendeParameterZuordnungSecond = 0;
            zuVerwendendeParameterZuordnungText = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[pGattung - 1];
            zuVerwendendeParameterZuordnungTextSecond = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattungText[pGattung - 1];

            for (int i1 = 0; i1 < 5; i1++) {
                if (zuVerwendendeParameterZuordnung[i1] != 0) {
                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[i1] = 1;
                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(iIdentifikation)[i1] = zuVerwendendeParameterZuordnungText[i1];
                }
            }
        } else {
            /*Normale Zutrittskarte/Stimmkarte*/
            zuVerwendendeParameterZuordnung = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[pGattung - 1];
            zuVerwendendeParameterZuordnungSecond = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung[pGattung - 1];
            zuVerwendendeParameterZuordnungText = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[pGattung - 1];
            zuVerwendendeParameterZuordnungTextSecond = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattungText[pGattung - 1];

            for (int i1 = 0; i1 < 5; i1++) {
                if (zuVerwendendeParameterZuordnung[i1] != 0) {
                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[i1] = 1;
                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(iIdentifikation)[i1] = zuVerwendendeParameterZuordnungText[i1];

                    if (rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten != null && rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation) != null) {
                        for (int i2 = 0; i2 < rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation).length; i2++) {
                            if (rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation)[i2] != null) {
                                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation)[i2][i1] = 1;
                            }
                        }
                    }
                }
            }
            if (zuVerwendendeParameterZuordnungSecond != 0) {
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[5] = 1;
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(iIdentifikation)[5] = zuVerwendendeParameterZuordnungTextSecond;
                if (rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten != null && rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation) != null) {
                    for (int i2 = 0; i2 < rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation).length; i2++) {
                        if (rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation)[i2] != null) {
                            rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation)[i2][5] = 1;
                        }
                    }
                }
            }
        }

    }

    /*************Überprüfen und eintragen der vorhandenen Stimmkarten/StimmkartenSecond für Gast************
     * sowie zugeordneteEintrittskartenAktionaer und zugeordneteEintrittskartenVollmachten*/
    private void jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast(int iIdentifikation) {
        int rc;

        /*Problematik: 
         * Da auf eine Gastmeldung beliebig viele "Zutrittskarten/Stimmkarten-Bündel" einer Person (die z.B. mehrere Aktionäre vertreten hat)
         * verweisen können, kann nicht einfach Zutritts/Stimmkarte über Gast-MeldeIdent gesucht werden.
         * Sondern:
         * > es muß die eingegebene "Karte" ermittelt werden (EK, Stimmkarte etc.), und aus dieser die Zutrittskarte ermittelt werden
         * > Dann für diese Zutrittskarte die restlichen Stimmkarten einlesen
         */

        CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast", logDrucken, 10);
        CaBug.druckeLog("Gastkarten Identifikation=" + rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation), logDrucken, 10);
        CaBug.druckeLog(rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation), logDrucken, 10);
        CaBug.druckeLog(rWEPraesenzStatusabfrageRC.identifikationsnummerNeben.get(iIdentifikation), logDrucken, 10);

        EclZutrittsIdent lEclZutrittsIdent = new EclZutrittsIdent();

        switch (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation)) {

        case KonstKartenklasse.eintrittskartennummer:
            lEclZutrittsIdent.zutrittsIdent = rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation);
            lEclZutrittsIdent.zutrittsIdentNeben = rWEPraesenzStatusabfrageRC.identifikationsnummerNeben.get(iIdentifikation);
            break;

        case KonstKartenklasse.gastkartennummer:/*Nichts zu tun - keine Zuordnung!*/
            lEclZutrittsIdent = null;
            break;

        case KonstKartenklasse.stimmkartennummer:
            lDbBundle.dbStimmkarten.read(rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation), 1);
            lEclZutrittsIdent.zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdent;
            lEclZutrittsIdent.zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdentNeben;
            break;

        case KonstKartenklasse.stimmkartennummerSecond:
            lDbBundle.dbStimmkartenSecond.read(rWEPraesenzStatusabfrageRC.identifikationsnummer.get(iIdentifikation), 1);
            lEclZutrittsIdent.zutrittsIdent = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).zutrittsIdent;
            lEclZutrittsIdent.zutrittsIdentNeben = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).zutrittsIdentNeben;
            break;

        }

        if (lEclZutrittsIdent != null) {

            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdent = lEclZutrittsIdent.zutrittsIdent;
            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdentNeben = lEclZutrittsIdent.zutrittsIdentNeben;

            rc = lDbBundle.dbStimmkarten.readGueltigeZuZutrittsIdent(lEclZutrittsIdent);
            CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast rc=" + rc, logDrucken, 10);
            if (rc > 5) {
                CaBug.drucke("012");
            }
            for (int i1 = 0; i1 < rc; i1++) {
                int offset = 0;
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = -1;
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = lDbBundle.dbStimmkarten
                        .ergebnisPosition(i1).stimmkarte;
            }
            /*StimmkarteSecond*/
            rc = lDbBundle.dbStimmkartenSecond.readGueltigeZuZutrittsIdent(lEclZutrittsIdent);
            if (rc > 1) {
                CaBug.drucke("BlPraesenzAkkreditierung.jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast 013");
            }
            if (rc == 1) {
                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[5] = -1;
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer.set(iIdentifikation, lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).stimmkarteSecond);
            }
        }

    }

    /*************Überprüfen und eintragen der vorhandenen Stimmkarten/StimmkartenSecond************
     * sowie zugeordneteEintrittskartenAktionaer und zugeordneteEintrittskartenVollmachten*/
    private void jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdent(int iIdentifikation) {
        int rc;

        if (rWEPraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.appIdent) {
            /*+++++AppIdent - Zuordnung "Aktionär" mit der "in der App für diese Meldung ausgewählte Person" füllen++++*/
            /*Stimmkarte*/
            int personIdentFuerStimmkarten = 0; /*In der Stimmkarte ist entweder die PersonNatJurIdent eingetragen (Vertreter), oder -1 (Selbst). Dementsprechend ermitteln,
                                                nach was selektiert werden muß!*/
            //			System.out.println("iIdentifikation="+iIdentifikation);
            //			System.out.println("rWEPraesenzStatusabfrageRC.meldungen.get(iIdentifikation).personenNatJurIdent="+rWEPraesenzStatusabfrageRC.meldungen.get(iIdentifikation).personenNatJurIdent);
            if (/*rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation)==null ||*/
            rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident == rWEPraesenzStatusabfrageRC.meldungen.get(iIdentifikation).personenNatJurIdent) {
                personIdentFuerStimmkarten = -1;
            } else {
                personIdentFuerStimmkarten = rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident;
            }
            rc = lDbBundle.dbStimmkarten.readGueltigeZuMeldungPerson(meldungsIdent, personIdentFuerStimmkarten);
            if (rc > 5) {
                CaBug.drucke("BlPraesenz.jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdent 001");
            }
            for (int i1 = 0; i1 < rc; i1++) {
                int offset = (lEclMeldung.gattung - 1) * 5;
                rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdent;
                rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdentNeben;

                rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = -1;
                rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = lDbBundle.dbStimmkarten
                        .ergebnisPosition(i1).stimmkarte;
            }
            return;
        }

        /***********Für Aktionär**************/
        /*Stimmkarte*/
        rc = lDbBundle.dbStimmkarten.readGueltigeZuMeldungPerson(meldungsIdent, -1/*lEclMeldung.personenNatJurIdent*/);
        if (rc > 5) {
            CaBug.drucke("BlPraesenz.statusabfrage 012");
        }
        for (int i1 = 0; i1 < rc; i1++) {
            int offset = (lEclMeldung.gattung - 1) * 5;
            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdent;
            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdentNeben;

            rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = -1;
            rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer.get(iIdentifikation)[lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = lDbBundle.dbStimmkarten
                    .ergebnisPosition(i1).stimmkarte;

        }
        /*StimmkarteSecond*/
        rc = lDbBundle.dbStimmkartenSecond.readGueltigeZuMeldungPerson(meldungsIdent, -1/*lEclMeldung.personenNatJurIdent*/);
        if (rc > 1) {
            CaBug.drucke("BlPraesenz.statusabfrage 013");
        }
        if (rc == 1) {
            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdent;
            rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(iIdentifikation).zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdentNeben;

            rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[5] = -1;
            rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer.set(iIdentifikation, lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).stimmkarteSecond);
        }

        /*****Für Vollmachten***********/
        if (rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten != null && rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation) != null) {
            for (int i2 = 0; i2 < rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation).length; i2++) {
                /*Stimmkarten*/
                rc = lDbBundle.dbStimmkarten.readGueltigeZuMeldungPerson(meldungsIdent, rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(iIdentifikation)[i2].bevollmaechtigtePerson.ident);
                if (rc > 5) {
                    CaBug.drucke("BlPraesenz.statusabfrage 014");
                }
                for (int i1 = 0; i1 < rc; i1++) {
                    int offset = (lEclMeldung.gattung - 1) * 5;
                    rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.get(iIdentifikation)[i2].zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdent;
                    rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.get(iIdentifikation)[i2].zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(i1).zutrittsIdentNeben;

                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten.get(iIdentifikation)[i2][lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1 - offset] = -1;
                    rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten.get(iIdentifikation)[i2][lDbBundle.dbStimmkarten.ergebnisPosition(i1).ausSubnummernkreis - 1
                            - offset] = lDbBundle.dbStimmkarten.ergebnisPosition(i1).stimmkarte;
                }
                /*StimmkarteSecond*/
                rc = lDbBundle.dbStimmkartenSecond.readGueltigeZuMeldungPerson(meldungsIdent, rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(iIdentifikation)[i2].bevollmaechtigtePerson.ident);
                if (rc > 1) {
                    CaBug.drucke("BlPraesenz.statusabfrage 015");
                }
                if (rc == 1) {
                    rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.get(iIdentifikation)[i2].zutrittsIdent = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdent;
                    rWEPraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.get(iIdentifikation)[i2].zutrittsIdentNeben = lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdentNeben;

                    rWEPraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(iIdentifikation)[5] = -1;
                    rWEPraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten.get(iIdentifikation)[i2] = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).stimmkarteSecond;
                }
            }
        }

    }

    /********Prüfen, ob personNatJur überhaupt noch (für diese Meldung) eine Vertretungsberechtigung besitzt*******
     * sprich: ist es Aktionär, oder noch ein Bevollmächtigter?*/
    private int personNochZulaessig = 0;

    private void jeIdentifikation_pruefeObPersonNatJurVertretungsberechtigt(int iIdentifikation, int pPersonNatJurIdent) {
        personNochZulaessig = 0;
        if (pPersonNatJurIdent == lEclMeldung.personenNatJurIdent || pPersonNatJurIdent == 0 || pPersonNatJurIdent == -1) {
            personNochZulaessig = 1;
        } else {
            for (int i1 = 0; i1 < rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(iIdentifikation).length; i1++) {
                EclWillensErklVollmachtenAnDritte lEclWillensErklVollmachtenAnDritte = rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(iIdentifikation)[i1];
                if (lEclWillensErklVollmachtenAnDritte.wurdeStorniert == false && lEclWillensErklVollmachtenAnDritte.bevollmaechtigtePerson.ident == pPersonNatJurIdent) {
                    personNochZulaessig = 1;
                }
            }
        }

    }

    /***************************Aktionär ist anwesend*************************************************
     * Ergänzt:
     * rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer
     * rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle
     * 
     * Setzt:
     * rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur(iIdentifikation)
     * 
     * */
    private void jeIdentifikation_zulaessigeAktionen_AktionaerIstAnwesend(int iIdentifikation) {
        /*Egal mit welchem Medium - immer alles möglich, außer es ist "gesperrt"*/

        if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet) {
            rWEPraesenzStatusabfrageRC.rcKartenart = KonstKartenart.unbekannt;
        }

        switch (rWEPraesenzStatusabfrageRC.rcKartenart) {
        case KonstKartenart.unbekannt:
            if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummer
                    || rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummerSecond
                    || (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet
                            && rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.eintrittskartennummer
                            && rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).personenNatJurIdent != 0 //Eintrittskarte wurde bereits schon mal verwendet!
                    )) {
                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.abgang);
                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.vertreterwechsel);
                if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[KonstSkIst.srv] == 1) {
                    rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV);
                }
            }
            break;
        case KonstKartenart.abgang:
            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.abgang);
            break;
        case KonstKartenart.vollmachtAnDritteErteilen:
            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.vertreterwechsel);
            break;
        case KonstKartenart.vollmachtWeisungSRV:
            if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[KonstSkIst.srv] == 1) {
                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV);
            }
            break;

        }

        /*TODO _Praesenzerfassung: zulaessigeFunktionenAlle ist so schwachsinn - hier wird nicht die Schnittemenge, sondern die Gesamtmenge eingetragen ...*/
        /*Nun "Alle" füllen*/
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.abgang);
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.vertreterwechsel);

        if (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 1) { /*Tausch Stimmkarte Delayed ist nur während der Abstimmung möglich - für den Fall, dass der 
                                                                      Aktionär bereits "eingeschmissen" hat*/
            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.stimmkartenwechselNachAbstimmung);
        }
        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.stimmkartenwechsel);

        if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[KonstSkIst.kiav] == 1) {
            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnKIAV);
        }
        if (lDbBundle.param.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[KonstSkIst.srv] == 1) {
            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV);
        }

        /*Alle bereits zugeordneten Karten einlesen. Das ist für Abgang etc. eigentlich unnötig, wird jedoch in folgenden Fällen benötigt:
         * > Statusabfrage gesamt (Sonderschalteroberfläche)
         * > Anzeige der richtigen E-Nr./S-Nr. (bei Präsenzakkreditierung normal)
         * > "Nachzuordnungsfunktion" von neuem Stimmmaterial
         * 
         * Für "schnelle" Funktionen, wie z.B. SRV-Erteilen beim Verlassen der HV über Stapelleser, kann das möglicherweise
         * "ausgeschaltet" werden ...
         */
        jeIdentifikation_pruefeFuelleZuordnungStimmkarten(iIdentifikation, lEclMeldung.gattung);
        jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdent(iIdentifikation);

        /*vorbestimmte Person besetzen*/
        switch (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation)) {
        case KonstKartenklasse.eintrittskartennummer: {
            rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.set(iIdentifikation, rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).personenNatJurIdent);
            break;
        }
        case KonstKartenklasse.stimmkartennummer: {
            rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.set(iIdentifikation, rWEPraesenzStatusabfrageRC.stimmkarten.get(iIdentifikation).personenNatJurIdent);
            break;
        }
        case KonstKartenklasse.stimmkartennummerSecond: {
            rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.set(iIdentifikation, rWEPraesenzStatusabfrageRC.stimmkartenSecond.get(iIdentifikation).personenNatJurIdent);
            break;
        }

        }

    }

    /***************************Aktionär ist nicht anwesend*************************************************
     * Ergänzt:
     * rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer
     * rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle
     * 
     * Setzt:
     * rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur(iIdentifikation)
     * 
     * */
    private void jeIdentifikation_zulaessigeAktionen_AktionaerIstNichtAnwesend(int iIdentifikation) {
        jeIdentifikation_setzeInterneMoeglichkeitenWegenSammelkarten(iIdentifikation);

        if (kannNichtPersoenlichPraesentWegenSammelkarte != 0) {/*+++++Erscheinen NICHT möglich wg. Sammelkarte+++++++*/
            rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmKannNichtPersoenlichPraesentWegenSammelkarte);
        } else {/*+++++++++++Erscheinen ist möglich++++++*/
            /*Ggf. aus Sammelkartenzuordnung benötigte Aktionen aufrufen*/
            if (sammelkartenZuordnungVorhanden != 0) {
                rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.hinweisSammelkartenZuordnungVorhanden);
            }
            if (sammelkartenZuordnungKannUndMussWiderrufenWerden != 0) {
                rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.sammelkartenZuordnungMussWiderrufenWerden);
            }
            if (sammelkartenZuordnungAktivKannUndMussDeaktiviertWerden != 0) {
                rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.sammelkartenZuordnungMussDeaktiviertWerden);
            }

            boolean stimmkarteVerwendet = false;
            if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummer
                    || rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummerSecond) {
                stimmkarteVerwendet = true;
            }

            /*Zugang/Wiederzugang mit ZutrittsIdent oder Stimmkarten(Second), mit oder ohne Zuordnung von Stimmaterial*/
            /*Prüfen, ob Stimmkarte/StimmkartenSecond zugeordnet werden müssen; ggf. Zuordnung belegen.
             * Abhängig von Gattung*/
            jeIdentifikation_pruefeFuelleZuordnungStimmkarten(iIdentifikation, lEclMeldung.gattung);

            /* Eintragen, welche Stimmkarten und Eintrittskarten bereits zugeordnet wurden.*/
            jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdent(iIdentifikation);

            if (lEclMeldung.statusWarPraesenz == 0) {/*++++++++++++++Erstzuganng der Meldung+++++++++++*/
                if (rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang || (stimmkarteVerwendet == true
                        && (rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.stimmkartenEtikett || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang))) {
                    rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.erstzugang);
                }
                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.erstzugang);
                if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.eintrittskartennummer) {
                    rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur.set(iIdentifikation, rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).ausgestelltAufPersonenNatJurIdent);
                }

            } else {/*++++++++++++++++Wiederzugang der Meldung+++++++++*/

                if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet || stimmkarteVerwendet) { //Mit Stimmkarte geht immer; mit ZutrittsIdent nur bei entsprechender Parametereinstellung*

                    int hPersonNatJur = 0;
                    switch (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation)) {
                    case KonstKartenklasse.eintrittskartennummer:
                        hPersonNatJur = rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).personenNatJurIdent;
                        break;
                    case KonstKartenklasse.stimmkartennummer:
                        hPersonNatJur = rWEPraesenzStatusabfrageRC.stimmkarten.get(iIdentifikation).personenNatJurIdent;
                        break;
                    case KonstKartenklasse.stimmkartennummerSecond:
                        hPersonNatJur = rWEPraesenzStatusabfrageRC.stimmkartenSecond.get(iIdentifikation).personenNatJurIdent;
                        break;
                    }

                    if (hPersonNatJur == 0) {
                        CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_zulaessigeAktionen_AktionaerIstNichtAnwesend: Erstmaliges Verwenden", logDrucken, 10);
                        /*Erstmaliges Verwenden dieser Identifikation (bei ZutrittsIdent durchaus möglich; bei Stimmkarten eher unwahrscheinlich*/
                        if (rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang //Für Wiederzugang mit Eintrittskartenkopie
                                || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_beliebigePerson);
                        }
                        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_beliebigePerson);
                        if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.eintrittskartennummer) {
                            //							System.out.println("IstEintrittskarte="+rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).ausgestelltAufPersonenNatJurIdent);
                            rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur.set(iIdentifikation, rWEPraesenzStatusabfrageRC.zutrittskarten.get(iIdentifikation).ausgestelltAufPersonenNatJurIdent);
                        }
                    } else {
                        CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_zulaessigeAktionen_AktionaerIstNichtAnwesend: AA", logDrucken, 10);
                        /*Identifikation wurde schon verwendet*/
                        /*Prüfen, ob personNatJur noch als Aktionär oder Bevollmächtigter agieren darf*/
                        jeIdentifikation_pruefeObPersonNatJurVertretungsberechtigt(iIdentifikation, hPersonNatJur);
                        if (personNochZulaessig == 0) {//Vollmacht nicht mehr vorhanden
                            if (rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang //Für Wiederzugang mit Eintrittskartenkopie
                                    || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_nurSelbePersonMitNeuerVollmacht);
                            }
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_nurSelbePersonMitNeuerVollmacht);
                        } else { // Vollmacht noch vorhanden
                            CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_zulaessigeAktionen_AktionaerIstNichtAnwesend: AB", logDrucken, 10);
                            if (rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang //Für Wiederzugang mit Eintrittskartenkopie
                                    || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_nurSelbePerson);
                            }
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugang_nurSelbePerson);
                        }
                        if (hPersonNatJur == lEclMeldung.personenNatJurIdent) {
                            rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.set(iIdentifikation, -1);
                        } else {
                            rWEPraesenzStatusabfrageRC.vorbestimmtePersonNatJur.set(iIdentifikation, hPersonNatJur);
                        }
                    }

                }
            } //Ende Wiederzugang der Meldung

            /*Müsssen ggf. Vollmachten widerrufen werden?*/
            switch (lDbBundle.param.paramAkkreditierung.pPraesenzBeiErscheinenAndereVollmachtenStornieren) {
            case 0:
                break; /*Kein Widerruf erforderlich*/
            case 1: {
                rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.vollmachtenMuessenGgfAutomatischWiderrufenWerden);
                break;
            }
            case 2: {
                rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.vollmachtenMuessenGgfInTextformWiderrufenWerden);
                break;
            }
            }
        }

    }

    /**Füllen der "Personen"-Listen für vertretende Person für AppIdent*/
    private void jeIdentifikation_fuellePersonenabgleichFuerApp(int iIdentifikation) {
        boolean lAppVollmachtMussNochVorgelegtWerden = true;
        boolean lAppPersonMussMitAppPersonUeberprueftWerden = true;

        CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_fuellePersonenabgleichFuerApp", logDrucken, 10);
        CaBug.druckeLog("jeIdentifikation_fuellePersonenabgleichFuerApp fur =" + iIdentifikation, logDrucken, 10);

        if (rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation) == null) {
            CaBug.druckeLog("rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation) ist null", logDrucken, 10);
        } else {
            CaBug.druckeLog("rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident =" + rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident, logDrucken,
                    10);
            CaBug.druckeLog("rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).istSelbePersonWieIdent ="
                    + rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).istSelbePersonWieIdent, logDrucken, 10);
        }

        /*++++Checken, ob schon überprüft wurde, ob die die Meldung vertretende Person identisch mit der App-Person ist+++++*/
        if ( /*Der Meldung zugeordnete vertretende PersonNatJur ist identisch mit der App-PersonNatJur*/
        rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident == rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent ||
        /*Der Meldung zurgerdnete vertretende PersonNatJur wurde die App-PersonNatJur bereits als "übereinstimmend" zugeordnet, und dies
         * wurde auch bereits einmal verifiziert!
         */
                (rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).istSelbePersonWieIdent == rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent
                        && rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).uebereinstimmungSelbePersonWurdeUeberprueft == 1)) {
            lAppPersonMussMitAppPersonUeberprueftWerden = false;
        }

        /*++++Checken, ob die die Meldung vertretende Person eine gültige Vollmacht für die Meldung hinterlegt hat ++++*/
        if (pruefeDarfVertretenMeldung(rWEPraesenzStatusabfrageRC.appVertretendePerson.get(iIdentifikation).ident, iIdentifikation) == true) {
            lAppVollmachtMussNochVorgelegtWerden = false;
        }
        CaBug.druckeLog("BlPraesenzAkkreditierung.jeIdentifikation_fuellePersonenabgleichFuerApp", logDrucken, 10);
        CaBug.druckeLog("lAppVollmachtMussNochVorgelegtWerden=" + lAppVollmachtMussNochVorgelegtWerden, logDrucken, 10);
        CaBug.druckeLog("lAppPersonMussMitAppPersonUeberprueftWerden=" + lAppPersonMussMitAppPersonUeberprueftWerden, logDrucken, 10);
        rWEPraesenzStatusabfrageRC.appVollmachtMussNochVorgelegtWerden.add(lAppVollmachtMussNochVorgelegtWerden);
        rWEPraesenzStatusabfrageRC.appPersonMussMitAppPersonUeberprueftWerden.add(lAppPersonMussMitAppPersonUeberprueftWerden);
    }

    /**Prüft für die Meldung pMeldung, ob die übergebene Person die angemeldete Person selbst ist, oder ein eingetragener Vertreter*/
    private boolean pruefeDarfVertretenMeldung(int pPersonIdent, int pMeldung) {
        /*Ist Person die angemeldete Person selbst?*/
        if (rWEPraesenzStatusabfrageRC.meldungen.get(pMeldung).personenNatJurIdent == pPersonIdent) {
            return true;
        }

        /*Ist die Person in den hinterlegten Vollmachten?*/
        if (rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(pMeldung) != null) {
            for (int i2 = 0; i2 < rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(pMeldung).length; i2++) {//Für alle Vollmachten dieser Meldung*/
                if (rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(pMeldung)[i2].bevollmaechtigtePerson.ident == pPersonIdent) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Prüft für alle Meldungen, ob die übergebene Person die angemeldete Person selbst ist, oder ein eingetragener Vertreter
     * Trägt Ergebnis in hinterlegtePersonPasstZuMeldung ein
     * */
    private void pruefeDarfVertreten(int pPersonIdent) {

        int anzMeldungen = rWEPraesenzStatusabfrageRC.meldungen.size();

        boolean[] lHinterlegtePersonPasstZuMeldung = new boolean[anzMeldungen];

        for (int i = 0; i < anzMeldungen; i++) {
            lHinterlegtePersonPasstZuMeldung[i] = pruefeDarfVertretenMeldung(pPersonIdent, i);
        }

        rWEPraesenzStatusabfrageRC.hinterlegtePersonPasstZuMeldung.add(lHinterlegtePersonPasstZuMeldung);
    }

    /**Prüft, ob pPersonIdent bereits in den hinterlegtePersonen enthalten ist*/
    private boolean pruefenObPersonSchonInHinterlegte(int pPersonIdent) {
        for (int i = 0; i < rWEPraesenzStatusabfrageRC.hinterlegtePersonen.size(); i++) {
            if (rWEPraesenzStatusabfrageRC.hinterlegtePersonen.get(i).ident == pPersonIdent) {
                return true;
            }
        }
        return false;
    }

    /**Ruft BlpraesenzStatus.status auf, und ergänzt dann die noch fehlenden Felder in WEPRaesenzStatusabfrageRC*/
    public WEPraesenzStatusabfrageRC statusabfrage(WEPraesenzStatusabfrage lWEPraesenzStatusabfrage) {

        /*Anzahl der verschiedenen, vorhandenen/gültigen Meldungen, die vertreten werden*/
        int returnWertGesamt = 1;

        BlPraesenzStatus blPraesenzStatus = new BlPraesenzStatus(lDbBundle);
        blPraesenzStatus.statusabfrage(lWEPraesenzStatusabfrage, 1);

        rWEPraesenzStatusabfrageRC = blPraesenzStatus.rWEPraesenzStatusabfrageRC;
        returnWertGesamt = rWEPraesenzStatusabfrageRC.rc;
        CaBug.druckeLog("BlPraesenzAkkreditierung.statusabfrage rc aus blPraesenzStatus.statusabfrage=" + returnWertGesamt, logDrucken, 10);
        //		if (returnWertGesamt==CaFehler.pmNummernformUngueltig ||
        //				returnWertGesamt==CaFehler.pmNummernformAktionsnummerUngueltig ||
        //				returnWertGesamt==CaFehler.pfNichtEindeutig ||
        //				returnWertGesamt==CaFehler.pmNummernformMandantUngueltig ||
        //				returnWertGesamt==CaFehler.pfXyNichtImZulaessigenNummernkreis
        //				){
        //		}
        if (returnWertGesamt < 0) { //Ehemals Detailabfrage siehe obiger Kommentar, aber aktuell nicht sinnvoll!
            return rWEPraesenzStatusabfrageRC;
        }

        /*++++++++++++++++Nun direkt von der Identifikationsnummer abhängige Daten einlesen bzw. ermitteln+++++++++++++*/

        for (iIdentifikation = 0; iIdentifikation < rWEPraesenzStatusabfrageRC.identifikationsnummer.size(); iIdentifikation++) {

            if (rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.get(iIdentifikation) == 1) {

                lEclMeldung = rWEPraesenzStatusabfrageRC.meldungen.get(iIdentifikation);
                if (lEclMeldung != null) {
                    meldungsIdent = lEclMeldung.meldungsIdent;
                }

                /*Abgeleitete Daten ermitteln:
                 * > Mögliche Funktionen allgemein (für Aktionär und Gast!)
                 * > Mögliche Funktionen mit gerade aktueller Buchungsnummer (für Aktionär und Gast!)
                 * > erforderliche Aktionen 
                 * 			> Widerruf Vollmachten (ob das tatsächlich durchgeführt werden muß, kann erst
                 * 				im Client entschieden werden, je nachdem was ausgewählt wird)
                 * 			> Herauslösen aus Sammelkarten (wird nur gesetzt, wenn tatsächlich nötig und möglich)
                 * > erforderliche Zuordnungen von Stimmkarten (aus bis zu 2x4 Nummernkreisen, 
                 * 		d.h. Gattungsabhängig), stimmkartenSecond
                 */

                if (lEclMeldung == null) {
                    /*Über die eingelesene Identifikation kann (noch) keinerlei Meldung identifiziert werden*/
                    if (rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummer
                            || rWEPraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(iIdentifikation) == KonstKartenklasse.stimmkartennummerSecond) {
                        /*Es wurde eine Stimmkarte eingelesen, die noch nicht präsent ist. Wegen unterschiedlicher Konstellationen
                         * ist das nicht möglich (z.B. bei zwei Gattungen etc.). => Erst ZutrittsIdent einlesen*/
                        rWEPraesenzStatusabfrageRC.erforderlicheAktionen.get(iIdentifikation).add(KonstWillenserklaerung.erstZutrittsIdentEinlesenOderKarteGesperrt);
                    }
                }

                /*Aktionen für Aktionär*/
                if (lEclMeldung != null && lEclMeldung.meldungIstEinAktionaer()) {
                    boolean fehlerAufgetreten = false;
                    if (!rWEPraesenzStatusabfrageRC.rcPasswort.isEmpty()) {
                        boolean passwortVerifiziert = false;
                        String hAktionaersnummer = lEclMeldung.aktionaersnummer;
                        lDbBundle.dbLoginDaten.read_loginKennung(hAktionaersnummer);
                        if (lDbBundle.dbLoginDaten.anzErgebnis() != 0) {
                            EclLoginDaten lLoginDaten = lDbBundle.dbLoginDaten.ergebnisPosition(0);
                            String pwInitial = lLoginDaten.lieferePasswortInitialClean();
                            if (pwInitial.equals(rWEPraesenzStatusabfrageRC.rcPasswort)) {
                                passwortVerifiziert = true;
                            }
                        }
                        if (passwortVerifiziert == false) {
                            fehlerAufgetreten = true;
                            rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmPWImQRCodeFalsch);
                            if (returnWertGesamt == 1) {
                                returnWertGesamt = CaFehler.pmPWImQRCodeFalsch;
                            }
                        }
                    }
                    if (fehlerAufgetreten == false) {
                        if (lEclMeldung.meldungstyp == KonstMeldung.KARTENART_SAMMELKARTE && !lDbBundle.paramGeraet.akkreditierungSammelkartenBuchenMoeglich) {//Sammelkartenbuchung auf diesem Gerät nicht möglich
                            rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich);
                            if (returnWertGesamt == 1) {
                                returnWertGesamt = CaFehler.pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich;
                            }
                        } else {
                            if (lEclMeldung.stimmen == 0 && lEclMeldung.meldungstyp != KonstMeldung.KARTENART_SAMMELKARTE
                                    && ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)==false) { //0-Bestand bei normalen Aktionären nicht möglich
                                rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmNullBestandBuchenNichtMoeglich);
                                if (returnWertGesamt == 1) {
                                    returnWertGesamt = CaFehler.pmNullBestandBuchenNichtMoeglich;
                                }
                            } 
                            else {
                                CaBug.druckeLog("lEclMeldung.zusatzfeld1 = "+lEclMeldung.zusatzfeld1, logDrucken, 10);
                                if ((lEclMeldung.zusatzfeld1.equals("-1") || lEclMeldung.zusatzfeld1.equals("-2")) && lDbBundle.param.paramAkkreditierung.ungepruefteKartenNichtBuchen==1) {
                                    rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmMeldungNochNichtGeprueft);
                                    if (returnWertGesamt == 1) {
                                        returnWertGesamt = CaFehler.pmMeldungNochNichtGeprueft;
                                    }
                               }
                                else {
                                    if (lDbBundle.param.paramBasis.liefereZugangMoeglich(lEclMeldung.gattung) == false) {
                                        rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.set(iIdentifikation, CaFehler.pmMitDieserGattungKeinZugangMoeglich);
                                        if (returnWertGesamt == 1) {
                                            returnWertGesamt = CaFehler.pmMitDieserGattungKeinZugangMoeglich;
                                        }
                                    } else {
                                        if (lEclMeldung.statusPraesenz == 1) { /**********************************Aktionär ist gerade anwesend*********************/
                                            jeIdentifikation_zulaessigeAktionen_AktionaerIstAnwesend(iIdentifikation);
                                        } else { /***********************Aktionär ist gerade nicht durch persönliches Erscheinen Selbst oder Vertreter anwesend***************/
                                            jeIdentifikation_zulaessigeAktionen_AktionaerIstNichtAnwesend(iIdentifikation);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } //Ende Aktion Aktionär

                /*Aktionen für Gast*/
                if (lEclMeldung != null && lEclMeldung.meldungIstEinGast()) {
                    if (lEclMeldung.statusPraesenz == 1) {
                        /**********************************Gast ist gerade anwesend*********************/
                        if (lDbBundle.param.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode == false || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.abgang
                                || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt) {
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.abgangGast);
                        }
                        rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.abgangGast);
                        jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast(iIdentifikation);
                    } else {
                        /**************Gast ist gerade nicht anwesend************************************/
                        if (lDbBundle.param.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode == false || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang
                                || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang || rWEPraesenzStatusabfrageRC.rcKartenart == KonstKartenart.unbekannt) {

                            if (lEclMeldung.statusWarPraesenz == 0) {
                                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.zugangGast);
                            } else {
                                rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugangGast_nurSelbePerson);
                            }

                        }
                        if (lEclMeldung.statusWarPraesenz == 0) {
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.zugangGast);
                        } else {
                            rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(iIdentifikation).add(KonstWillenserklaerung.wiederzugangGast_nurSelbePerson);
                        }
                        jeIdentifikation_eintragenVorhandeneStimmkartenZutrittsIdentGast(iIdentifikation);
                    }

                } // Ende Aktion Gast

                /*Nun "Personen" für AppIdent füllen*/
                if (rWEPraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.appIdent) {
                    jeIdentifikation_fuellePersonenabgleichFuerApp(iIdentifikation);
                }

            }
        }
        /*XXX*/

        /*++++++Nun "abgeleitete" - gemeinsame - Daten ermitteln++++++++++++++++++++*/

        /*++++alle "hinterlegten" Personen+++*/
        rWEPraesenzStatusabfrageRC.hinterlegtePersonen = new LinkedList<EclPersonenNatJur>();
        rWEPraesenzStatusabfrageRC.hinterlegtePersonPasstZuMeldung = new LinkedList<boolean[]>();

        //		System.out.println("rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent "+rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent);
        CaBug.druckeLog("BlPraesenzAkkreditierung.statusabfrage rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent " + rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent, logDrucken, 10);
        if (rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent != 0) {
            /*Die Person, die das Smartphone besitzt - wird auch zukünftig benötigt :-)!!!!!!!!!!!!!!!!!!!!*/
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            lPersonenNatJur.ident = rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent;
            int rc = lDbBundle.dbPersonenNatJur.read(lPersonenNatJur.ident);
            if (rc < 1) {
                rWEPraesenzStatusabfrageRC.appIdentPersonNatJurIdent = 0;
            } else {
                lPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                rWEPraesenzStatusabfrageRC.hinterlegtePersonen.add(lPersonenNatJur);
                rWEPraesenzStatusabfrageRC.appIdentPersonNatJur = lPersonenNatJur;
                pruefeDarfVertreten(lPersonenNatJur.ident);
            }

        }
        /*Nun alle Meldungen durcharbeiten - jeweils angemeldete Person und hinterlegte Vollmachten*/
        for (int i = 0; i < rWEPraesenzStatusabfrageRC.meldungen.size(); i++) {
            /*+++Die angemeldete Person+++*/
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            if (rWEPraesenzStatusabfrageRC.meldungen.get(i) != null) {
                lPersonenNatJur.ident = rWEPraesenzStatusabfrageRC.meldungen.get(i).personenNatJurIdent;
                /*Prüfen - schon in hinterlegtePersonen enthalten?*/
                if (!pruefenObPersonSchonInHinterlegte(lPersonenNatJur.ident)) {
                    lDbBundle.dbPersonenNatJur.read(lPersonenNatJur.ident);
                    lPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                    rWEPraesenzStatusabfrageRC.hinterlegtePersonen.add(lPersonenNatJur);
                    pruefeDarfVertreten(lPersonenNatJur.ident);
                }
            }

            /*+++Alle Vollmachten zu dieser Meldung++++*/
            if (rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(i) != null) {
                for (int i2 = 0; i2 < rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(i).length; i2++) {//Für alle Vollmachten dieser Meldung*/
                    lPersonenNatJur = rWEPraesenzStatusabfrageRC.aktionaerVollmachten.get(i)[i2].bevollmaechtigtePerson;
                    /*Prüfen - schon in hinterlegtePersonen enthalten?*/
                    if (!pruefenObPersonSchonInHinterlegte(lPersonenNatJur.ident)) {
                        lDbBundle.dbPersonenNatJur.read(lPersonenNatJur.ident);
                        lPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
                        rWEPraesenzStatusabfrageRC.hinterlegtePersonen.add(lPersonenNatJur);
                        pruefeDarfVertreten(lPersonenNatJur.ident);
                    }
                }

            }
        }

        /*Gemeinsame zulässige Funktionen (mit Aktionsnummer)*/
        rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle = new LinkedList<Integer>();

        if (rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer != null && rWEPraesenzStatusabfrageRC.rcZuIdentifikationsnummer.size() > 0) {
            /*Basis-Funktion anhand der ersten Meldung ermitteln*/
            int zugangMoeglichFuerAlle = -1; /*-1 => noch nicht fixiert; 0 => zugang nicht möglich, da für eine Karte nicht buchbar; 1 => Zugang möglich*/
            int erstZugangMoeglichFuerAlle = -1;
            int abgangMoeglichFuerAlle = -1;

            for (int i1 = 0; i1 < rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.size(); i1++) {
                int zugangMoeglichFuerKarte = -1;
                int erstZzugangMoeglichFuerKarte = -1;
                int abgangMoeglichFuerKarte = -1;
                for (int i = 0; i < rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(0).size(); i++) {
                    int hAktionKarte = rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(0).get(i);
                    switch (hAktionKarte) {
                    case KonstWillenserklaerung.erstzugang:
                        zugangMoeglichFuerKarte = 1;
                        erstZzugangMoeglichFuerKarte = 1;
                        break;
                    case KonstWillenserklaerung.wiederzugang_beliebigePerson:
                    case KonstWillenserklaerung.wiederzugang_nurSelbePerson:
                    case KonstWillenserklaerung.wiederzugang_nurSelbePersonMitNeuerVollmacht:
                    case KonstWillenserklaerung.zugangGast:
                    case KonstWillenserklaerung.wiederzugangGast_nurSelbePerson:
                        zugangMoeglichFuerKarte = 1;
                        break;
                    case KonstWillenserklaerung.abgang:
                    case KonstWillenserklaerung.abgangGast:
                        abgangMoeglichFuerKarte = 1;
                        break;
                    }
                }
                if (zugangMoeglichFuerAlle == -1 && zugangMoeglichFuerKarte == 1) {
                    zugangMoeglichFuerAlle = 1;
                }
                if (zugangMoeglichFuerKarte == 0) {
                    zugangMoeglichFuerAlle = 0;
                }
                if (erstZugangMoeglichFuerAlle == -1 && erstZzugangMoeglichFuerKarte == 1) {
                    erstZugangMoeglichFuerAlle = 1;
                }
                if (erstZzugangMoeglichFuerKarte == 0) {
                    erstZugangMoeglichFuerAlle = 0;
                }
                if (abgangMoeglichFuerAlle == -1 && abgangMoeglichFuerKarte == 1) {
                    abgangMoeglichFuerAlle = 1;
                }
                if (abgangMoeglichFuerKarte == 0) {
                    abgangMoeglichFuerAlle = 0;
                }
            }
            if (zugangMoeglichFuerAlle == 1) {
                //				System.out.println("BlPraesenzAkkreditierung: Wiederzugang für alle");
                rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.add(KonstWillenserklaerung.wiederzugang);
            }
            if (erstZugangMoeglichFuerAlle == 1) {
                //				System.out.println("BlPraesenzAkkreditierung: Erstzugang für alle");
                rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.add(KonstWillenserklaerung.erstzugang);
            }
            if (abgangMoeglichFuerAlle == 1) {
                //				System.out.println("BlPraesenzAkkreditierung: Abgang für alle");
                rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.add(KonstWillenserklaerung.abgang);
            }
        }

        //		if (rWEPraesenzStatusabfrageRC.anzMeldungenAktionaere!=0){
        //			
        //			if (rWEPraesenzStatusabfrageRC.anzMeldungenAktionaere==1){
        //				/*Dann nur eine einzige Meldung - Komplette übernehmen*/
        //				for (int i=0;i<rWEPraesenzStatusabfrageRC.meldungen.size();i++){
        //					EclMeldung lMeldung=rWEPraesenzStatusabfrageRC.meldungen.get(i);
        //					if (lMeldung!=null && lMeldung.meldungIstEinAktionaer()){
        //						for (int i1=0;i1<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i).size();i1++){
        //							rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenMitAktionsnummer.add(rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i).get(i1));
        //						}
        //					}
        //				}
        //			}
        //
        //			else{
        //				/*Mehrere Meldungen - gemeinsame  ermitteln*/
        //				/*Erste Meldung  ermitteln - denn das sind ja die maximalst gemeinsamen */
        //				int ersteMeldung=0;
        //				while (rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(ersteMeldung)==null){ersteMeldung++;}
        //				/*Diese Funktionen nun mit den Funktionen zu allen anderen Meldungen vergleichen - nur die Funktionen,
        //				 * die auch bei allen anderen meldungen enthalten sind, übernehmen.
        //				 * TODO $App: gemeinsameZulaessigeFunktionenMitAktionsnummer - dies sollte nochmal überdacht  werden .... Denn:
        //				 * Es gibt einige Aktionen, die anders lauten aber "gleichartig" sind (verschiedene Zugänge ...). Deshalb
        //				 * Ist diese Routine möglicherweise nicht brauchbar. Für 1 ident geht sie aber auf jeden Fall. Und wie das
        //				 * für Apps läuft muß eh noch geklärt werden. Dito auch für zulaessigeFunktionenAlle
        //				 */
        //				for (int i=0;i<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(ersteMeldung).size();i++){//Für alle  der ersten Meldung
        //					int uebertrag=rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(ersteMeldung).get(i);
        //					int gef=1; /*Wird auf 0 gesetzt, wenn  bei einer Meldung nicht enthalten ist*/
        //					for (int i1=ersteMeldung+1;i1<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.size();i1++){//Für restliche Meldungen die  prüfen
        //						int gef1=0;
        //						for (int i2=0;i2<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i1).size();i2++){//Für alle  dieser Meldung*/
        //							if (rWEPraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i1).get(i2)==uebertrag){gef1=1;}
        //						}
        //						if (gef1==0){gef=0;} //Dann bei dieser Meldung nicht gefunden -> wird sicher nicht übernommen
        //					}
        //					if (gef==1){// übernehmen
        //						rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenMitAktionsnummer.add(uebertrag);
        //					}
        //					
        //				}
        //			}
        //		}
        //		else{/*Kein Aktionär betroffen*/
        //		}
        //
        //		/*Gemeinsame zulässige Funktionen (Alle - d.h. ohne Versionsnummer)*/
        //		rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle=new LinkedList<Integer>();
        //		if (rWEPraesenzStatusabfrageRC.anzMeldungenAktionaere!=0){
        //			if (rWEPraesenzStatusabfrageRC.anzMeldungenAktionaere==1){
        //				/*Dann nur eine einzige Meldung - Komplette übernehmen*/
        //				for (int i=0;i<rWEPraesenzStatusabfrageRC.meldungen.size();i++){
        //					EclMeldung lMeldung=rWEPraesenzStatusabfrageRC.meldungen.get(i);
        //					if (lMeldung!=null && lMeldung.meldungIstEinAktionaer()){
        //						for (int i1=0;i1<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i).size();i1++){
        //							rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.add(rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i).get(i1));
        //						}
        //					}
        //				}
        //			}
        //
        //			else{
        //				/*Mehrere Meldungen - gemeinsame  ermitteln*/
        //				/*Erste Meldung  ermitteln - denn das sind ja die maximalst gemeinsamen */
        //				int ersteMeldung=0;
        //				while (rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(ersteMeldung)==null){ersteMeldung++;}
        //				/*Diese Funktionen nun mit den Funktionen zu allen anderen Meldungen vergleichen - nur die Funktionen,
        //				 * die auch bei allen anderen meldungen enthalten sind, übernehmen.
        //				 * TODO $App: gemeinsameZulaessigeFunktionenAlle - dies sollte nochmal überdacht  werden .... Denn:
        //				 * Es gibt einige Aktionen, die anders lauten aber "gleichartig" sind (verschiedene Zugänge ...). Deshalb
        //				 * Ist diese Routine möglicherweise nicht brauchbar. Für 1 ident geht sie aber auf jeden Fall. Und wie das
        //				 * für Apps läuft muß eh noch geklärt werden. Dito auch für zulaessigeFunktionenAlle
        //				 */
        //				for (int i=0;i<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(ersteMeldung).size();i++){//Für alle  der ersten Meldung
        //					int uebertrag=rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(ersteMeldung).get(i);
        //					int gef=1; /*Wird auf 0 gesetzt, wenn  bei einer Meldung nicht enthalten ist*/
        //					for (int i1=ersteMeldung+1;i1<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.size();i1++){//Für restliche Meldungen die  prüfen
        //						int gef1=0;
        //						for (int i2=0;i2<rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i1).size();i2++){//Für alle  dieser Meldung*/
        //							if (rWEPraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i1).get(i2)==uebertrag){gef1=1;}
        //						}
        //						if (gef1==0){gef=0;} //Dann bei dieser Meldung nicht gefunden -> wird sicher nicht übernommen
        //					}
        //					if (gef==1){// übernehmen
        //						rWEPraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.add(uebertrag);
        //					}
        //					
        //				}
        //			}
        //		}
        //		else{/*Kein Aktionär betroffen*/
        //		}
        //

        /*Gemeinsame vordefiniertePersonNatJur*/
        for (int i = 0; i < rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur.size(); i++) {
            int uebertragPersonNatJur = rWEPraesenzStatusabfrageRC.vordefiniertePersonNatJur.get(i);
            if (rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur == 0) {
                rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur = uebertragPersonNatJur;
            } else {
                if (uebertragPersonNatJur != 0) {
                    if (rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur != uebertragPersonNatJur) {
                        rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur = -1; //Dann nämlich zwei verschiedene vordefinierte

                    }
                }
            }
        }
        if (rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur == -1) {
            rWEPraesenzStatusabfrageRC.gemeinsameVordefiniertePersonNatJur = 0;
        }

        rWEPraesenzStatusabfrageRC.rc = returnWertGesamt;
        return rWEPraesenzStatusabfrageRC;
    }

    /*********************************************Buchen*****************************************************/
    /********************************************************************************************************/
    private WEPraesenzBuchen gWEPraesenzBuchen = null;
    private WEPraesenzBuchenRC rWEPraesenzBuchenRC = null;

    /*Zur Verarbeitung / Verwendung von
     * identifikation*, bereitsZugeordnet*, vorhandene*:
     * 
     * identifikation*: Diese werden beim Aufruf von .buchen aus den übergebenen Parametern ermittelt/gefüllt.
     * Falls mehrere Identifikationen übergeben werden,dann werden diese nach folgender Vorrangigkeit weiter
     * für die Identifikation verwendet:
     * 1. Vorrang: Second (21), 2. Vorrang:Stimmkarte (11 bis 14/15), 3. Vorrang: Gastkarte (2), Letzter Versuch: Eintrittskarte Aktionäre (1).
     * Die art der Identifikation, die vorrangig weiterverwendet wird, wird in identifizierungErfolgUeber durch obige
     * Nummer gespeichert
     * 
     * Je nachdem, welche Identifikation vorrangig verwendet wird, wird anschließend überprüft, welche der anderen Identifikationen
     * dieser bereits zugeordnet sind.
     * Also:
     * >> Wenn Identifikation==1 also Eintrittskarte, dann werden die Stimmkarten und StimmkartenSecond eingelesen, die ggf.
     * dieser EK schon zugeordnet sind, und in bereitsZugeordneteStimmkarten* abgespeichert.
     * >> Wenn Identifikation durch Stimmkarte (oder Second), dann werden dieser Stimmkarte zugeordnete Eintrittskarte
     * und darüber die weiter zugeordneten Stimmkarten eingelesen. Alles wird in bereitsZugeordneteStimmkarten* und 
     * bereitsZugeordneteZutrittsIdentAktionaer abgespeichert.
     *
     * Anschließend werden ALLE bereitsZugeordnete* gegen ALLE identifikation* abgeglichen, ob Widerspruch vorhanden.
     */

    /*Die als Parameter übergebenen Identifikationen für den
     * gerade bearbeiteten Satz
     */

    private EclZutrittsIdent identifikationZutrittsIdentAktionaer = new EclZutrittsIdent();
    private EclZutrittsIdent identifikationZutrittsIdentGast = new EclZutrittsIdent();
    private String[] identifikationStimmkarten = { "", "", "", "", "" };
    private int[] stimmkarteSubNummernkreis = { 0, 0, 0, 0, 0 };
    private String identifikationStimmkarteSecond = "";

    /**belegt aus WEPraesenzBuchen.vollmachtPersonenNatJurIdent.get(iIdentifikation)*/
    private int vollmachtPersonenNatJurIdent = 0;

    /**belegt aus WEPraesenzBuchen.funktion.get(iIdentifikation)*/
    private int funktion = 0;

    /**
     *1 = ZutrittsIdentAktionaer
     *2 = ZutrittsIdentGast
     *11 ... 14 Stimmkarte
     *15 Stimmkarte (virtuell)
     *21 StimmkarteSecond
     *
     */
    private int identifizierungErfolgtUeber = -1;

    /*Identifikationen, die bereits der ersten gültigen Identifikation
     * zugeordnet sind. Nach dem Eintragen sind diese auch mit den "neuen" gefüllt, d.h. diese können
     * dann fürs Speichern der Willenserklärung etc. hergenommen werden*/
    private EclZutrittsIdent bereitsZugeordneteZutrittsIdentAktionaer = new EclZutrittsIdent();
    private EclZutrittsIdent bereitsZugeordneteZutrittsIdentGast = new EclZutrittsIdent();
    private String[] bereitsZugeordneteStimmkarten = { "", "", "", "", "" };
    private String bereitsZugeordneteStimmkartenSecond = "";

    /*Bereits vorhandenen Ecl-Identifikationen*/
    private EclZutrittskarten vorhandenEclZutrittskarteAktionaer = null;
    private EclZutrittskarten vorhandenEclZutrittskarteGast = null;
    private EclStimmkarten[] vorhandeneEclStimmkarten = { null, null, null, null, null };
    private EclStimmkartenSecond vorhandeneStimmkarteSecond = null;

    /*Sind Stimmkarten-Zuordnungen erforderlich?*/
    private int[] zuordnungErforderlich = null;
    private int zuordnungSecondErforderlich = 0;
    private int zuordnungNummernkreisOffset = -1;

    /**Die erklärende Person - auch bei Abgängen etc.
     * Erkläuterung: wird immer mit der "handelnden/erklärenden" Person gesetzt, die
     * dann bei Willenserklärungen bzw. Meldungen eingetragen wird.
     * Auch wenn Bevollmächtigter etc. ==-2 (unveränderte Person)*/
    private int erklaerendePerson = 0;
    private EclPersonenNatJur eclErklaerendePerson = null;

    /**Wichtiger Hinweis zur Benutzung von hEclMeldungAktionaer:
     * Im Verlauf des Buchens kann sich der Aktionärssatz in der Datenbank ändern: z.B. durch Widerruf einer
     * Sammelkartenzuordnung, oder Vollmacht an Dritte.
     * Deshalb muß - vor finaler Buchung der Präsenz - der Meldesatz nochmal eingelesen werden und die
     * Überprüfungen bzgl. Meldesatz nochmal durchgeführt werden (da sich ja auch durch andere Benutzer
     * der Präsenzstatus des Aktionärs verändert haben könnte!*/
    private EclMeldung hEclMeldung = null;

    /**=1 => Der aktuelle Buchungsvorgang wird delayed*/
    private int vorgangWirdDelayed = 0;

    /*******************Vorbereitungs-Routinen*********************************************/

    private void buchenvorbereiten_initialisieren() {
        meldungsIdent = 0;

        rWEPraesenzBuchenRC = new WEPraesenzBuchenRC();
        rWEPraesenzBuchenRC.zutrittsIdent = new LinkedList<EclZutrittsIdent>();
        rWEPraesenzBuchenRC.zugeordneteStimmkarten = new LinkedList<String[]>();
    }

    /**Alle für den jeweiligen Teilnehmer geltenden Werte vorinitialisieren*/
    private void buchenvorbereitenIIdentifikation_initialisieren(int iIdentifikation) {
        identifikationZutrittsIdentAktionaer = new EclZutrittsIdent();
        identifikationZutrittsIdentGast = new EclZutrittsIdent();
        bereitsZugeordneteZutrittsIdentAktionaer = new EclZutrittsIdent();
        bereitsZugeordneteZutrittsIdentGast = new EclZutrittsIdent();
        bereitsZugeordneteStimmkartenSecond = "";
        vorhandenEclZutrittskarteAktionaer = null;
        vorhandenEclZutrittskarteGast = null;
        vorhandeneStimmkarteSecond = null;
        zuordnungErforderlich = null;
        zuordnungSecondErforderlich = 0;
        zuordnungNummernkreisOffset = -1;
        erklaerendePerson = 0;
        hEclMeldung = null;
        vorgangWirdDelayed = 0;

        for (int i = 0; i < 5; i++) {
            identifikationStimmkarten[i] = "";
            stimmkarteSubNummernkreis[i] = 0;
            bereitsZugeordneteStimmkarten[i] = "";
            vorhandeneEclStimmkarten[i] = null;
        }
        identifikationStimmkarteSecond = "";
        identifizierungErfolgtUeber = -1;

    }

    /**Übergebene ZutrittsIdentAktionär aufbereiten und auf formale Gültigkeit prüfen.
     * Ergebnis:
     * > identifikationZutrittsIdentAktionaer und gWEPraesenzBuchen.zutrittsIdentAktionaer
     * 		enthält jeweils aufbereitete/überprüfte ZutrittsIdent (nur Nummer)
     * > gWEPraesenzBuchen.zutrittsIdentAktionaerArt=1
     * Returnwerte:
     * 1=ok
     * pmNummernformZutrittsIdentUngueltig
     */
    private int buchenvorbereitenIIdentifikation_aufbereitenZutrittsIdentAktionaer(int iIdentifikation) {
        int rc;
        CaBug.druckeLog("buchenvorbereitenIIdentifikation_aufbereitenZutrittsIdentAktionaer", logDrucken, 10);
        CaBug.druckeLog("iIdentifikation=" + iIdentifikation, logDrucken, 10);
        CaBug.druckeLog("zutrittsIdentAktionaerArt=" + gWEPraesenzBuchen.zutrittsIdentAktionaerArt.get(iIdentifikation), logDrucken, 10);
        if (gWEPraesenzBuchen.zutrittsIdentAktionaerArt.get(iIdentifikation) == 2) {
            CaBug.druckeLog("zutrittsIdent=" + gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdent, logDrucken, 10);
            CaBug.druckeLog("zutrittsIdentNeben=" + gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdentNeben, logDrucken, 10);

            String hString = gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdent;
            if (!gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdentNeben.isEmpty()) {
                hString = hString + " " + gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdentNeben;
            }
            BlNummernformen lBlNummernformen = new BlNummernformen(lDbBundle);
            rc = lBlNummernformen.dekodiere(hString, KonstKartenklasse.eintrittskartennummer);
            CaBug.druckeLog("rc=" + rc, logDrucken, 10);
            if (rc < 0) {
                return CaFehler.pmNummernformZutrittsIdentUngueltig;
            }
            if (lBlNummernformen.rcKartenklasse != KonstKartenklasse.eintrittskartennummer) {
                return CaFehler.pmNummernformZutrittsIdentUngueltig;
            }
            gWEPraesenzBuchen.zutrittsIdentAktionaerArt.set(iIdentifikation, 1);
            identifikationZutrittsIdentAktionaer.zutrittsIdent = lBlNummernformen.rcIdentifikationsnummer.get(0);
            identifikationZutrittsIdentAktionaer.zutrittsIdentNeben = lBlNummernformen.rcIdentifikationsnummerNeben.get(0);
            gWEPraesenzBuchen.zutrittsIdentAktionaer.set(iIdentifikation, identifikationZutrittsIdentAktionaer);
        }
        if (gWEPraesenzBuchen.zutrittsIdentAktionaerArt.get(iIdentifikation) == 1) {
            identifikationZutrittsIdentAktionaer.zutrittsIdent = gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdent;
            identifikationZutrittsIdentAktionaer.zutrittsIdentNeben = gWEPraesenzBuchen.zutrittsIdentAktionaer.get(iIdentifikation).zutrittsIdentNeben;
            gWEPraesenzBuchen.zutrittsIdentAktionaer.set(iIdentifikation, identifikationZutrittsIdentAktionaer);

        }
        return 1;
    }

    /**Übergebene ZutrittsIdentGast aufbereiten und auf formale Gültigkeit prüfen.
     * Ergebnis:
     * > identifikationZutrittsIdentGast und gWEPraesenzBuchen.zutrittsIdentGast
     * 		enthält jeweils aufbereitete/überprüfte ZutrittsIdent (nur Nummer)
     * > gWEPraesenzBuchen.zutrittsIdentGastArt=1
     * Returnwerte:
     * 1=ok
     * pmNummernformZutrittsIdentUngueltig
     */
    private int buchenvorbereitenIIdentifikation_aufbereitenZutrittsIdentGast(int iIdentifikation) {
        if (gWEPraesenzBuchen.zutrittsIdentGastArt.get(iIdentifikation) == 2) {
            int rc;
            BlNummernformen lBlNummernformen = new BlNummernformen(lDbBundle);
            rc = lBlNummernformen.dekodiere(gWEPraesenzBuchen.zutrittsIdentGast.get(iIdentifikation).zutrittsIdent, KonstKartenklasse.gastkartennummer);
            if (rc < 0) {
                return CaFehler.pmNummernformZutrittsIdentUngueltig;
            }
            if (lBlNummernformen.rcKartenklasse != KonstKartenklasse.gastkartennummer) {
                return CaFehler.pmNummernformZutrittsIdentUngueltig;
            }
            gWEPraesenzBuchen.zutrittsIdentGastArt.set(iIdentifikation, 1);
            identifikationZutrittsIdentGast.zutrittsIdent = lBlNummernformen.rcIdentifikationsnummer.get(0);
            identifikationZutrittsIdentGast.zutrittsIdentNeben = lBlNummernformen.rcIdentifikationsnummerNeben.get(0);
            gWEPraesenzBuchen.zutrittsIdentGast.set(iIdentifikation, identifikationZutrittsIdentGast);
        }
        if (gWEPraesenzBuchen.zutrittsIdentGastArt.get(iIdentifikation) == 1) {
            identifikationZutrittsIdentGast.zutrittsIdent = gWEPraesenzBuchen.zutrittsIdentGast.get(iIdentifikation).zutrittsIdent;
            identifikationZutrittsIdentGast.zutrittsIdentNeben = gWEPraesenzBuchen.zutrittsIdentGast.get(iIdentifikation).zutrittsIdentNeben;
            gWEPraesenzBuchen.zutrittsIdentGast.set(iIdentifikation, identifikationZutrittsIdentGast);

        }
        return 1;
    }

    /**Übergebene Stimmkarten aufbereiten und auf formale Gültigkeit (einschließlich richtigem Sub-Nummernkreis) prüfen.
     * Ergebnis:
     * > identifikationStimmkarten und gWEPraesenzBuchen.stimmkarten
     * 		enthält jeweils aufbereitete/überprüfte ZutrittsIdent (nur Nummer)
     * > gWEPraesenzBuchen.stimmkartenArt=1
     * > gWEPraesenzBuchen.stimmkartenArt und gWEPraesenzBuchen.stimmkarten sind mindestens vorhanden (mit 0 / "" belegt),
     * 		auch wenn sie nicht als Parameter übergeben wurden.
     * Returnwerte:
     * 1=ok
     * pmNummernformStimmkarte<1-4>Ungueltig 
     * pmStimmkarte<1-4>FalschZugeordnet (aus nicht passendem Sub-Nummernkreis)
     */
    private int buchenvorbereitenIIdentifikation_aufbereitenStimmkarten(int iIdentifikation) {
        int rc;
        if (gWEPraesenzBuchen.stimmkartenArt.get(iIdentifikation) != null) {
            for (int i = 0; i < 4; i++) {
                if (gWEPraesenzBuchen.stimmkartenArt.get(iIdentifikation)[i] == 2) {
                    BlNummernformen lBlNummernformen = new BlNummernformen(lDbBundle);
                    rc = lBlNummernformen.dekodiere(gWEPraesenzBuchen.stimmkarten.get(iIdentifikation)[i], KonstKartenklasse.stimmkartennummer);
                    if (rc < 0 || lBlNummernformen.rcKartenklasse != KonstKartenklasse.stimmkartennummer) {
                        switch (i) {
                        case 0:
                            return CaFehler.pmNummernformStimmkarte1Ungueltig;
                        case 1:
                            return CaFehler.pmNummernformStimmkarte2Ungueltig;
                        case 2:
                            return CaFehler.pmNummernformStimmkarte3Ungueltig;
                        case 3:
                            return CaFehler.pmNummernformStimmkarte4Ungueltig;
                        }
                        return -1;
                    }
                    CaBug.druckeLog("lBlNummernformen.rcStimmkarteSubNummernkreis=" + lBlNummernformen.rcStimmkarteSubNummernkreis, logDrucken, 10);
                    if (lBlNummernformen.rcStimmkarteSubNummernkreis != 0) {/*Falsche Stimmkartennummer (aus nicht passendem Nummernkreis?) zugeordnet?*/
                        stimmkarteSubNummernkreis[i] = lBlNummernformen.rcStimmkarteSubNummernkreis;
                        if (lBlNummernformen.rcStimmkarteSubNummernkreis != (i + 1)
                                && lBlNummernformen.rcStimmkarteSubNummernkreis != (i + 1) + 4) {/*Genaueres später überprüfen, wenn Meldung klar ist etc.*/
                            switch (i) {
                            case 0:
                                return CaFehler.pmStimmkarte1FalschZugeordnet;
                            case 1:
                                return CaFehler.pmStimmkarte2FalschZugeordnet;
                            case 2:
                                return CaFehler.pmStimmkarte3FalschZugeordnet;
                            case 3:
                                return CaFehler.pmStimmkarte4FalschZugeordnet;
                            }
                            return -1;
                        }
                    }

                    gWEPraesenzBuchen.stimmkartenArt.get(iIdentifikation)[i] = 1;
                    identifikationStimmkarten[i] = lBlNummernformen.rcIdentifikationsnummer.get(0);
                    gWEPraesenzBuchen.stimmkarten.get(iIdentifikation)[i] = identifikationStimmkarten[i];

                }
            }
        } else {
            gWEPraesenzBuchen.stimmkartenArt.set(iIdentifikation, new int[] { 0, 0, 0, 0, 0 });
            gWEPraesenzBuchen.stimmkarten.set(iIdentifikation, new String[] { "", "", "", "", "" });
        }

        return 1;
    }

    /**Übergebene StimmkartenSecond aufbereiten und auf formale Gültigkeit prüfen.
     * Ergebnis:
     * > identifikationStimmkartenSecond und gWEPraesenzBuchen.stimmkartenSecond
     * 		enthält jeweils aufbereitete/überprüfte StimmkarteSecond
     * > gWEPraesenzBuchen.stimmkartenSecondArt=1
     * Returnwerte:
     * 1=ok
     * pmNummernformStimmkarteSecondUngueltig 
     */
    private int buchenvorbereitenIIdentifikation_aufbereitenStimmkartenSecond(int iIdentifikation) {
        int rc;
        if (gWEPraesenzBuchen.stimmkartenSecondArt.get(iIdentifikation) == 2) {
            BlNummernformen lBlNummernformen = new BlNummernformen(lDbBundle);
            rc = lBlNummernformen.dekodiere(gWEPraesenzBuchen.stimmkartenSecond.get(iIdentifikation), KonstKartenklasse.stimmkartennummerSecond);
            if (rc < 0) {
                return CaFehler.pmNummernformStimmkarteSecondUngueltig;
            }
            if (lBlNummernformen.rcKartenklasse != KonstKartenklasse.stimmkartennummerSecond) {
                return CaFehler.pmNummernformStimmkarteSecondUngueltig;
            }
            gWEPraesenzBuchen.stimmkartenSecondArt.set(iIdentifikation, 1);
            identifikationStimmkarteSecond = lBlNummernformen.rcIdentifikationsnummer.get(0);
            gWEPraesenzBuchen.stimmkartenSecond.set(iIdentifikation, identifikationStimmkarteSecond);
        }

        return 1;
    }

    /**Wenn ZutrittsidentAktionaer übergeben, dann muß diese zu Eclmeldung passen.
     * 
     * Füllt;
     * > meldungsIdent
     * Returnwerte:
     * 1=ok
     * pfXyNichtVorhanden
     * pmZutrittsIdentIstStorniert
     * pmZuordnungFehlerhaft
     * */
    private int buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentAktionaerZuMeldung(int iIdentifikation) {
        int rc;
        if (gWEPraesenzBuchen.zutrittsIdentAktionaerArt.get(iIdentifikation) == 1) {

            rc = lDbBundle.dbZutrittskarten.readAktionaer(identifikationZutrittsIdentAktionaer, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenzAkkreditierung.buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentAktionaerZuMeldung 001");
            }
            if (rc < 1) {/*Zutrittskarte nicht gefunden*/
                return CaFehler.pfXyNichtVorhanden;
            }

            EclZutrittskarten lEclZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            if (lEclZutrittskarte.zutrittsIdentWurdeGesperrt()) {
                return CaFehler.pmZutrittsIdentIstStorniert;
            }
            meldungsIdent = lEclZutrittskarte.liefereGueltigeMeldeIdent();

            if (gWEPraesenzBuchen.eclMeldung.get(iIdentifikation) != null) {
                if (gWEPraesenzBuchen.eclMeldung.get(iIdentifikation).meldungsIdent != meldungsIdent) {
                    return CaFehler.pmZuordnungFehlerhaft;
                }
            }
        }

        return 1;
    }

    /**Wenn ZutrittsidentGast übergeben, dann muß diese zu Eclmeldung passen.
     * 
     * Füllt;
     * > meldungsIdent
     * Returnwerte:
     * 1=ok
     * pfXyNichtVorhanden
     * pmZutrittsIdentIstStorniert
     * pmZuordnungFehlerhaft
     * */
    private int buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentGastZuMeldung(int iIdentifikation) {
        int rc;
        if (gWEPraesenzBuchen.zutrittsIdentGastArt.get(iIdentifikation) == 1) {

            rc = lDbBundle.dbZutrittskarten.readGast(identifikationZutrittsIdentGast, 1);
            if (rc > 1) {
                CaBug.drucke("BlPraesenz.buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentGastZuMeldung 002");
            }
            if (rc < 1) {/*Zutrittskarte nicht gefunden*/
                return CaFehler.pfXyNichtVorhanden;
            }

            EclZutrittskarten lEclZutrittskarte = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            meldungsIdent = lEclZutrittskarte.liefereGueltigeMeldeIdent();
            if (lEclZutrittskarte.zutrittsIdentWurdeGesperrt()) {
                return CaFehler.pmZutrittsIdentIstStorniert;
            }

            if (gWEPraesenzBuchen.eclMeldung.get(iIdentifikation) != null) {
                if (gWEPraesenzBuchen.eclMeldung.get(iIdentifikation).meldungsIdent != meldungsIdent) {
                    return CaFehler.pmZuordnungFehlerhaft;
                }
            }
        }

        return 1;
    }

    /**Ident ermitteln, über die Erst.Identifizierung erfolgt.
     * 
     * Falls mehrere idents gefüllt, dann Priorität:
     * 1. ZutrittsIdentAktionär (=>1)
     * 2. ZutrittsIdentGast (=>2)
     * 3. Stimmkarte (=>11+Subnummernkreis[0 bis 3])
     * 4. StimmkarteSecond (=>21)
     * 
     * Füllt: 
     * > identifizierungErfolgtUeber
     */
    private void buchenvorbereiten_ermittleIdentifizierungErfolgUeber(int iIdentifikation) {
        identifizierungErfolgtUeber = -1;
        if (identifikationZutrittsIdentAktionaer.zutrittsIdent != null && !identifikationZutrittsIdentAktionaer.zutrittsIdent.isEmpty() && identifizierungErfolgtUeber == -1) {
            identifizierungErfolgtUeber = 1;
        }
        if (identifikationZutrittsIdentGast.zutrittsIdent != null && !identifikationZutrittsIdentGast.zutrittsIdent.isEmpty() && identifizierungErfolgtUeber == -1) {
            identifizierungErfolgtUeber = 2;
        }
        for (int i = 0; i < 5; i++) {
            if (!identifikationStimmkarten[i].isEmpty() && identifizierungErfolgtUeber == -1) {
                identifizierungErfolgtUeber = 11 + i;
            }
        }
        if (!identifikationStimmkarteSecond.isEmpty() && identifizierungErfolgtUeber == -1) {
            identifizierungErfolgtUeber = 21;
        }
    }

    /***************Buchungs-Routinen*************************************************/
    /**Ausgelagert, da kurz vor finaler Buchung nochmal erforderlich*/
    /**Für hEclMeldung mit pMeldungsIdent.
     * gastOderAktionaer==0 => Gast, 1=Aktionär*/
    private int buchenvorbereiten_leseEclMeldung(int pMeldungsIdent) {
        hEclMeldung = new EclMeldung();
        hEclMeldung.meldungsIdent = pMeldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(hEclMeldung);
        if (lDbBundle.dbMeldungen.meldungenArray.length < 1) {
            return CaFehler.pmMeldungsIdentNichtVorhanden;
        }
        hEclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        if (hEclMeldung.meldungAktiv != 1) {
            return CaFehler.pmMeldungIstStorniert;
        }

        return 1;
    }

    /**Einlesen aller zugeordneten Stimmkarten und Stimmkartensecond, Suche über die
     * Eintrittskarte pZutrittsIdent
     * 
     * Füllt:
     * bereitsZugeordneteStimmkarten[] und bereitsZugeordneteStimmkartenSecond
     * 
     */
    private int buchenvorbereiten_leseBereitsZugeordneteStimmkartenZuZutrittsIdent(EclZutrittsIdent pZutrittsIdent, boolean pMitStimmkarteSecond) {
        int rc = 1;
        lDbBundle.dbStimmkarten.readGueltigeZuZutrittsIdent(pZutrittsIdent);
        if (lDbBundle.dbStimmkarten.anzErgebnis() > 0) {
            for (int i = 0; i < lDbBundle.dbStimmkarten.anzErgebnis(); i++) {
                EclStimmkarten lStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(i);
                int hSubnummernkreis = lStimmkarte.ausSubnummernkreis;
                while (hSubnummernkreis > 5) {
                    hSubnummernkreis -= 5;
                }
                bereitsZugeordneteStimmkarten[hSubnummernkreis - 1] = lStimmkarte.stimmkarte;
            }
        }
        if (pMitStimmkarteSecond == true) {
            lDbBundle.dbStimmkartenSecond.readGueltigeZuZutrittsIdent(pZutrittsIdent);
            if (lDbBundle.dbStimmkartenSecond.anzErgebnis() > 0) {
                EclStimmkartenSecond lStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0);
                bereitsZugeordneteStimmkartenSecond = lStimmkarteSecond.stimmkarteSecond;
            }
        }
        return rc;
    }

    /**Einlesen der zugeordneten Eintrittskarten, und der restlichen Stimmkarten, Suche über eine
     * Stimmkarte pStimmkartenIdent
     *
     * Fehlermeldung:
     * pmStimmkarteNichtVorhanden (übergebene Stimmkarte gibt es nicht!)
     * 
     * Füllt:
     * bereitsZugeordneteZutrittsIdentAktionaer
     * bereitsZugeordneteStimmkarten[] und bereitsZugeordneteStimmkartenSecond
     * 
     */
    private int buchenvorbereiten_leseBereitsZugeordneteZutrittsUndStimmkartenZuStimmkartenIdent(String pStimmkartenIdent, boolean pMitStimmkarteSecond) {
        int rc = 1;

        lDbBundle.dbStimmkarten.read(pStimmkartenIdent);
        if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
            return CaFehler.pmStimmkarteNichtVorhanden;
        }
        EclStimmkarten lStimmkarte = lDbBundle.dbStimmkarten.ergebnisPosition(0);
        bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent = lStimmkarte.zutrittsIdent;
        bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben = lStimmkarte.zutrittsIdentNeben;
        buchenvorbereiten_leseBereitsZugeordneteStimmkartenZuZutrittsIdent(bereitsZugeordneteZutrittsIdentAktionaer, pMitStimmkarteSecond);

        return rc;
    }

    /**Einlesen der zugeordneten Eintrittskarten, und der restlichen Stimmkarten, Suche über eine
     * StimmkarteSecond pStimmkartenIdent
     *
     * Fehlermeldung:
     * pmStimmkarteNichtVorhanden (übergebene Stimmkarte gibt es nicht!)
     * 
     * Füllt:
     * bereitsZugeordneteZutrittsIdentAktionaer
     * bereitsZugeordneteStimmkarten[] und bereitsZugeordneteStimmkartenSecond
     * 
     */
    private int buchenvorbereiten_leseBereitsZugeordneteZutrittsUndStimmkartenZuStimmkartenSecondIdent(String pStimmkartenIdent, boolean pMitStimmkarteSecond) {
        int rc = 1;

        lDbBundle.dbStimmkartenSecond.read(pStimmkartenIdent);
        if (lDbBundle.dbStimmkartenSecond.anzErgebnis() == 0) {
            return CaFehler.pmStimmkarteSecondNichtVorhanden;
        }
        EclStimmkartenSecond lStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0);
        bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent = lStimmkarteSecond.zutrittsIdent;
        bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben = lStimmkarteSecond.zutrittsIdentNeben;
        buchenvorbereiten_leseBereitsZugeordneteStimmkartenZuZutrittsIdent(bereitsZugeordneteZutrittsIdentAktionaer, false);

        return rc;
    }

    /**Überprüfen, ob bereitsZugeordnete* den neu-zuzordnenden in identifikation* widersprechen
     * 
     * Returnwerte:
     * pmZuordnungsfehlerZutrittsIdent
     * */
    private int buchenvorbereiten_pruefenObBereitsZugeordneteDenNeuenWidersprechen(int iIdentifikation) {

        if (identifikationZutrittsIdentAktionaer != null && !identifikationZutrittsIdentAktionaer.zutrittsIdent.isEmpty() && bereitsZugeordneteZutrittsIdentAktionaer != null
                && !bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
            if (identifikationZutrittsIdentAktionaer.compareTo(bereitsZugeordneteZutrittsIdentAktionaer) != 0) {
                return CaFehler.pmZuordnungsfehlerZutrittsIdent;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!identifikationStimmkarten[i].isEmpty() && !bereitsZugeordneteStimmkarten[i].isEmpty()) {
                if (identifikationStimmkarten[i].compareTo(bereitsZugeordneteStimmkarten[i]) != 0) {
                    switch (i) {
                    case 0:
                        return CaFehler.pmZuordnungsfehlerStimmkarte1;
                    case 1:
                        return CaFehler.pmZuordnungsfehlerStimmkarte2;
                    case 2:
                        return CaFehler.pmZuordnungsfehlerStimmkarte3;
                    case 3:
                        return CaFehler.pmZuordnungsfehlerStimmkarte4;
                    }
                    return -1;
                }
            }

        }

        if (!identifikationStimmkarteSecond.isEmpty() && !bereitsZugeordneteStimmkartenSecond.isEmpty()) {
            if (identifikationStimmkarteSecond.compareTo(bereitsZugeordneteStimmkartenSecond) != 0) {
                return CaFehler.pmZuordnungsfehlerStimmkarteSecond;
            }
        }

        return 1;
    }

    /**Für bereits vorhandene Identifikationen die ecls aus Datenbank einlesen und in vorhandeneEcls
     * ablegen.
     * Dabei: 1. Priorität: die in identifikation* übergebenen Werte vorrangig einlesen, wenn diese leer
     * dann 2. Priorität bereitsZugeordnete* verwenden.
     * 
     * anschließend ist bereitsZugeordnete* neu gefüllt (bzw. überschrieben durch identifikation*), d.h.
     * in bereitsZugeordnete* steht nun entweder die bereits zugeordnete Ident, oder die in identifikation*
     * übergebene Ident.
     * 
     */
    private void buchenvorbereiten_fuerVorhandeneIdentifikationenECLsAusDatenbankFuellen() {
        /*ZutrittsIdentAktionär. Füllt vorhandenEclZutrittskarteAktionaer, sowie bereitsZugeordneteZutrittsIdentAktionaer (falls noch leer)*/
        if (identifikationZutrittsIdentAktionaer != null && !identifikationZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
            lDbBundle.dbZutrittskarten.readAktionaer(identifikationZutrittsIdentAktionaer, 1);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() > 0) {
                vorhandenEclZutrittskarteAktionaer = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            }
            bereitsZugeordneteZutrittsIdentAktionaer = identifikationZutrittsIdentAktionaer;
        } else {
            if (bereitsZugeordneteZutrittsIdentAktionaer != null && !bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
                lDbBundle.dbZutrittskarten.readAktionaer(bereitsZugeordneteZutrittsIdentAktionaer, 1);
                if (lDbBundle.dbZutrittskarten.anzErgebnis() > 0) {
                    vorhandenEclZutrittskarteAktionaer = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                }
            }
        }
        /*ZutrittsIdentGast. Füllt vorhandenEclZutrittskarteGast, sowie bereitsZugeordneteZutrittsIdentGast (falls noch leer)*/
        if (identifikationZutrittsIdentGast != null && !identifikationZutrittsIdentGast.zutrittsIdent.isEmpty()) {
            lDbBundle.dbZutrittskarten.readGast(identifikationZutrittsIdentGast, 1);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() > 0) {
                vorhandenEclZutrittskarteGast = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            }
            bereitsZugeordneteZutrittsIdentGast = identifikationZutrittsIdentGast;
        } else {
            if (bereitsZugeordneteZutrittsIdentGast != null && !bereitsZugeordneteZutrittsIdentGast.zutrittsIdent.isEmpty()) {
                lDbBundle.dbZutrittskarten.readGast(bereitsZugeordneteZutrittsIdentGast, 1);
                if (lDbBundle.dbZutrittskarten.anzErgebnis() > 0) {
                    vorhandenEclZutrittskarteGast = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
                }
            }
        }
        /*Stimmkarten. Füllt vorhandeneEclStimmkarten[], soweit Stimmkarte vorhanden*/
        for (int i = 0; i < 5; i++) {
            if (!identifikationStimmkarten[i].isEmpty()) {
                lDbBundle.dbStimmkarten.read(identifikationStimmkarten[i], 1);
                if (lDbBundle.dbStimmkarten.anzErgebnis() > 0) {
                    vorhandeneEclStimmkarten[i] = lDbBundle.dbStimmkarten.ergebnisPosition(0);
                }
                bereitsZugeordneteStimmkarten[i] = identifikationStimmkarten[i];
            } else {
                if (!bereitsZugeordneteStimmkarten[i].isEmpty()) {
                    lDbBundle.dbStimmkarten.read(bereitsZugeordneteStimmkarten[i], 1);
                    if (lDbBundle.dbStimmkarten.anzErgebnis() > 0) {
                        vorhandeneEclStimmkarten[i] = lDbBundle.dbStimmkarten.ergebnisPosition(0);
                    }
                }
            }
        }
        /*StimmkarteSecond. Füllt vorhandeneStimmkarteSecond*/
        if (!identifikationStimmkarteSecond.isEmpty()) {
            lDbBundle.dbStimmkartenSecond.read(identifikationStimmkarteSecond, 1);
            if (lDbBundle.dbStimmkartenSecond.anzErgebnis() > 0) {
                vorhandeneStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0);
            }
            bereitsZugeordneteStimmkartenSecond = identifikationStimmkarteSecond;
        } else {
            if (!bereitsZugeordneteStimmkartenSecond.isEmpty()) {
                lDbBundle.dbStimmkartenSecond.read(bereitsZugeordneteStimmkartenSecond, 1);
                if (lDbBundle.dbStimmkartenSecond.anzErgebnis() > 0) {
                    vorhandeneStimmkarteSecond = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0);
                }
            }
        }
    }

    /**Anhand von hEclMeldung - auch je nach Gattung - eintragen in zuordnungErforderlich; auch
     * zuordnungNummernkreisOffset füllen (0 oder 5, 10, 15)*/
    private void buchenvorbereiten_zuordnungErforderlichFuellen() {
        CaBug.druckeLog("BlPraesenzAkkreditierung.buchenvorbereiten_zuordnungErforderlichFuellen AppIdent=" + gWEPraesenzBuchen.appIdent, logDrucken, 10);
        if (gWEPraesenzBuchen.appIdent) {
            /*App-Ident - andere Vorgehensweise*/
            zuordnungErforderlich = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[hEclMeldung.gattung - 1];
            zuordnungSecondErforderlich = 0;
            zuordnungNummernkreisOffset = (hEclMeldung.gattung - 1) * 5;
        } else {
            /*Normale Zutrittskarte/Stimmkarte*/
            if (hEclMeldung.meldungIstEinAktionaer()) {
                zuordnungErforderlich = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[hEclMeldung.gattung - 1];
                zuordnungSecondErforderlich = lDbBundle.param.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung[hEclMeldung.gattung - 1];
                zuordnungNummernkreisOffset = (hEclMeldung.gattung - 1) * 5;
            }
        }

    }

    /**Bei Erstzugang/Wiederzugang: Prüfen, ob alle erforderlichen idents (die zugeordnet werden müssen). auch
     * in identifikation* übergeben wurden oder aber bereits vorhanden sind. Sie dürfen auch nicht bereits
     * anderweitig verwendet sein (jedoch natürlich für die untersuchte Verwendung bereits) - wird überprüft
     * bei Stimmkarten durch Abgleich mit bereitsZugeordneteZutrittsIdentAktionaer.
     */
    private int buchenvorbereiten_pruefenObAlleErforderlichenIdentsVorhanden() {
        if (hEclMeldung.meldungIstEinAktionaer()) {

            /*Prüfen: ZutrittsIdent entweder übergeben oder bereits zugeordnet?*/
            if ((identifikationZutrittsIdentAktionaer == null || identifikationZutrittsIdentAktionaer.zutrittsIdent.isEmpty())
                    && (bereitsZugeordneteZutrittsIdentAktionaer == null || bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent.isEmpty())) {
                return CaFehler.pmZuordnungsfehlerZutrittsIdent;
            }

            /*Prüfen: Stimmkarte[] entweder übergeben oder bereits zugeordnet?*/
            for (int i = 0; i < 4; i++) {
                if (zuordnungErforderlich[i] == 1) {
                    /*Falls nichts übergeben, und nichts vorhanden => Fehler*/
                    if (identifikationStimmkarten[i].isEmpty() && bereitsZugeordneteStimmkarten[i].isEmpty()) {
                        switch (i) {
                        case 0:
                            return CaFehler.pmStimmkarte1Fehlt;
                        case 1:
                            return CaFehler.pmStimmkarte2Fehlt;
                        case 2:
                            return CaFehler.pmStimmkarte3Fehlt;
                        case 3:
                            return CaFehler.pmStimmkarte4Fehlt;
                        }
                        return -1;
                    }
                    if (!identifikationStimmkarten[i].isEmpty()) {
                        /*Falls in identifikation etwas übergeben - falls diese schon in Datenbank vorhanden ist,
                         * paßt diese Stimmkarte dann zu dieser Zuordnung?
                         */
                        int erg = lDbBundle.dbStimmkarten.read(identifikationStimmkarten[i]);
                        if (erg > 0) { /*Dann schon vorhanden - stimmt Meldung?*/
                            if (lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdent.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent) != 0
                                    || lDbBundle.dbStimmkarten.ergebnisPosition(0).zutrittsIdentNeben.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben) != 0) {
                                switch (i) {
                                case 0:
                                    return CaFehler.pmStimmkarte1BereitsVerwendet;
                                case 1:
                                    return CaFehler.pmStimmkarte2BereitsVerwendet;
                                case 2:
                                    return CaFehler.pmStimmkarte3BereitsVerwendet;
                                case 3:
                                    return CaFehler.pmStimmkarte4BereitsVerwendet;
                                }
                                return -1;
                            }
                        }
                    }
                }
            }

            if (zuordnungSecondErforderlich == 1) {
                if (identifikationStimmkarteSecond.isEmpty() && bereitsZugeordneteStimmkartenSecond.isEmpty()) {
                    return CaFehler.pmStimmkarteSecondFehlt;
                }

                int erg = lDbBundle.dbStimmkartenSecond.read(identifikationStimmkarteSecond);
                if (erg > 0) { /*Dann schon vorhanden - stimmt Meldung?*/
                    if (lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).zutrittsIdent.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent) != 0
                            || lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).zutrittsIdentNeben.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben) != 0) {
                        return CaFehler.pmStimmkarteSecondBereitsVerwendet;
                    }
                }
            }
        }
        return 1;
    }

    /**Ausgelagert, da kurz vor finaler Buchung nochmal erforderlich*/
    private int buchen_aktionaerPruefenErstzugang() {
        if (hEclMeldung.statusPraesenz == 1) {
            return CaFehler.pmBereitsAnwesend;
        }
        if (hEclMeldung.statusWarPraesenz == 1 && funktion == KonstWillenserklaerung.erstzugang) {
            return CaFehler.pmWarBereitsAnwesend;
        }
        return 1;
    }

    /**Ausgelagert, da kurz vor finaler Buchung nochmal erforderlich*/
    private int buchen_aktionaerPruefenAbgang() {
        /*OK*/
        if (hEclMeldung.statusPraesenz != 1) {
            return CaFehler.pmBereitsAnwesend;
        }
        return 1;
    }

    /**************************************************prepareWillenserklaerung*****************************/
    /**Willenserklaerung mit willensart erzeugen und mit Standard-Werten belegen
     * 
     * */
    private EclWillenserklaerung preparedWillenserklaerung = null;
    private EclWillenserklaerungZusatz preparedWillenserklaerungZusatz = null;

    private EclWillenserklaerung preparedWillenserklaerung2 = null;
    private EclWillenserklaerungZusatz preparedWillenserklaerung2Zusatz = null;

    private void prepareWillenserklaerung(int willensart) {
        prepareWillenserklaerung12(willensart, 1);
    }

    private void prepareWillenserklaerung2(int willensart) {
        prepareWillenserklaerung12(willensart, 2);
    }

    /**pNummer==1 => preparedWillenserklaerung / preparedWillenserklaerungZusatz wird vorbereitet
     * pNummer==2 => preparedWillenserklaerung2 / preparedWillenserklaerungZusatz2 wird vorbereitet
     */
    private void prepareWillenserklaerung12(int willensart, int pNummer) {

        EclWillenserklaerung lPreparedWillenserklaerung = new EclWillenserklaerung();
        EclWillenserklaerungZusatz lPreparedWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
        lPreparedWillenserklaerung.willenserklaerung = willensart;
        if (vorgangWirdDelayed == 1) {
            lPreparedWillenserklaerung.delayed = 1;
        }
        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {
            lPreparedWillenserklaerung.meldungsIdent = hEclMeldung.meldungsIdent;
            lPreparedWillenserklaerung.stimmen = hEclMeldung.stimmen;
            lPreparedWillenserklaerung.aktien = hEclMeldung.stueckAktien;
        }
        if (hEclMeldung != null && hEclMeldung.meldungIstEinGast()) {
            lPreparedWillenserklaerung.meldungsIdentGast = hEclMeldung.meldungsIdent;
        }

        lPreparedWillenserklaerung.identifikationDurch = 0;
        lPreparedWillenserklaerung.identifikationKlasse = 0;
        lPreparedWillenserklaerung.identifikationZutrittsIdent = "";
        lPreparedWillenserklaerung.identifikationStimmkarte = "";
        lPreparedWillenserklaerung.identifikationStimmkarteSecond = "";

        lPreparedWillenserklaerung.erteiltAufWeg = gWEPraesenzBuchen.weLoginVerify.getEingabeQuelle();
        lPreparedWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        lPreparedWillenserklaerung.willenserklaerungGeberIdent = -1;

        lPreparedWillenserklaerung.verweisart = 0;
        lPreparedWillenserklaerung.verweisAufWillenserklaerung = 0;

        lPreparedWillenserklaerung.folgeBuchungFuerIdent = 0;
        /*Hinweis: wird ggf. von den einzelnen "Nutzern" nach Aufruf von prepareWillenserklaerung
         * nochmal überschrieben - siehe Beschreibung "mehrfach abhängig" bei pFolgeFuerWillenserklaerungIdent
         */

        if (pNummer == 1) {
            preparedWillenserklaerung = lPreparedWillenserklaerung;
            preparedWillenserklaerungZusatz = lPreparedWillenserklaerungZusatz;
        }
        if (pNummer == 2) {
            preparedWillenserklaerung2 = lPreparedWillenserklaerung;
            preparedWillenserklaerung2Zusatz = lPreparedWillenserklaerungZusatz;
        }

        return;
    }

    /*****************Div.kleinere Funktionen zum Buchen**********************************/

    /**Aktionär und/oder Gast darf nicht Präsent sein (und Aktionär nicht Präsentgewesen sein
     * falls Erstzugang) */
    private int buchen_pruefenObMeldungAbwesendBzwImmerAbwesendWar() {
        int rc;

        /*Prüfen, ob bereits präsent (gewesen)*/
        if (meldungsIdent != 0 && hEclMeldung.meldungIstEinGast() && hEclMeldung.statusPraesenz == 1) {/*Gast*/
            return CaFehler.pmBereitsAnwesend;
        }
        if (meldungsIdent != 0 && hEclMeldung.meldungIstEinAktionaer()) {/*Aktionär*/
            rc = buchen_aktionaerPruefenErstzugang();
            if (rc < 0) {
                return rc;
            }
        }
        return 1;
    }

    /**Aktionär und/oder Gast hEclMeldung muß anwesend sein.
     * rc<0 => ist abwesend
     * 1 = anwesend */
    private int buchen_pruefenObMeldungAnwesend() {
        int rc;

        if (hEclMeldung.meldungIstEinGast() && hEclMeldung.statusPraesenz != 1) {
            return CaFehler.pmNichtAnwesend;
        }
        if (hEclMeldung.meldungIstEinAktionaer()) {
            rc = buchen_aktionaerPruefenAbgang();
            if (rc < 0) {
                return rc;
            }
        }

        return 1;
    }

    private void buchen_setzenErklaerendePerson() {

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_setzenErklaerendePerson gWEPraesenzBuchen.vollmachtPersonenNatJurIdent=" + gWEPraesenzBuchen.vollmachtPersonenNatJurIdent, logDrucken, 10);
        if (vollmachtPersonenNatJurIdent != -2) {/*Neuer Bevollmächtigter, oder neue handelnde Person oder so*/
            erklaerendePerson = vollmachtPersonenNatJurIdent;
        } else {
            CaBug.druckeLog("identifizierungErfolgtUeber=" + identifizierungErfolgtUeber, logDrucken, 10);
            switch (identifizierungErfolgtUeber) {
            case 1: {
                CaBug.druckeLog("case 1:ZutrittsIdent=" + vorhandenEclZutrittskarteAktionaer.zutrittsIdent, logDrucken, 10);
                CaBug.druckeLog("case 1:ZutrittsIdentNeben=" + vorhandenEclZutrittskarteAktionaer.zutrittsIdentNeben, logDrucken, 10);
                erklaerendePerson = vorhandenEclZutrittskarteAktionaer.personenNatJurIdent;
                break;
            }
            case 2: {
                erklaerendePerson = vorhandenEclZutrittskarteGast.personenNatJurIdent;
                break;
            }
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                erklaerendePerson = vorhandeneEclStimmkarten[identifizierungErfolgtUeber - 11].personenNatJurIdent;
                break;
            }
            case 21: {
                erklaerendePerson = vorhandeneStimmkarteSecond.personenNatJurIdent;
                break;
            }

            }
        }
        if (erklaerendePerson == -1) {
            eclErklaerendePerson = null;
        } else {
            CaBug.druckeLog("erklaerendePerson=" + erklaerendePerson, logDrucken, 10);
            eclErklaerendePerson = new EclPersonenNatJur();
            lDbBundle.dbPersonenNatJur.read(erklaerendePerson);
            eclErklaerendePerson = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
        }
    }

    /**Falls Vollmacht gebucht werden soll, dann wird diese hier eingetragen:
     * > ggf. neu anlegen (dann ist anschließend gWEPraesenzBuchen.vollmachtPersonenNatJurIdent
     * 		mit der entsprechenden personNatJur gefüllt)
     * > ggf. Vollmacht an diese Person neu zuordnen
     */
    private int buchen_VollmachtZuordnen() {
        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_VollmachtZuordnen", logDrucken, 10);
        
        if (ParamSpezial.ku178(lDbBundle.clGlobalVar.mandant)) {
            CaBug.druckeLog("In ku178 hEclMeldung.vertreterIdent="+hEclMeldung.vertreterIdent, logDrucken, 10);
            if (hEclMeldung.vertreterIdent!=0) {
                CaBug.druckeLog("vollmachtPersonenNatJurIdent eintagen ="+vollmachtPersonenNatJurIdent, logDrucken, 10);
                vollmachtPersonenNatJurIdent=hEclMeldung.vertreterIdent;
            }
        }
        
        //			System.out.println("vollmachtPersonenNatJurIdent="+vollmachtPersonenNatJurIdent+" vollmachtPersonenNatJurIdent="+vollmachtPersonenNatJurIdent);
        //			System.out.println("gWEPraesenzBuchen.neueVollmachtPersonenNatJurIdent="+gWEPraesenzBuchen.neueVollmachtPersonenNatJurIdent);
        if (vollmachtPersonenNatJurIdent != -1 && vollmachtPersonenNatJurIdent != -2) {

            if (vollmachtPersonenNatJurIdent == 0 && gWEPraesenzBuchen.neueVollmachtPersonenNatJurIdent == 0) {
                /*Neue Vollmacht, mit neuer Person eintragen*/
                BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pErteiltAufWeg = gWEPraesenzBuchen.weLoginVerify.getEingabeQuelle();

                vmWillenserklaerung.piMeldungsIdentAktionaer = hEclMeldung.meldungsIdent;
                /*TODO $Vision: bei Präsenzerfassung (und auch noch sonstigen Erfassungen) Vollmachtskette mit eingeben lassen! D.h. ggf. auch Zwischenbevollmächtigte eintragen*/
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                EclPersonenNatJur personNatJur = new EclPersonenNatJur();

                personNatJur.ident = 0; /*Neue Person*/
                personNatJur.vorname = gWEPraesenzBuchen.vertreterVorname;
                personNatJur.name = gWEPraesenzBuchen.vertreterName;
                personNatJur.ort = gWEPraesenzBuchen.vertreterOrt;

                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;
                /*TODO $Vision: bei Vertretererfassung - insbesondere auf HV: wie kommt Vertreter an seine Zugangsdaten?*/

                vmWillenserklaerung.vollmachtAnDritte(lDbBundle);
                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    return vmWillenserklaerung.rcGrundFuerUnzulaessig;
                }
                vollmachtPersonenNatJurIdent = vmWillenserklaerung.pEclPersonenNatJur.ident;
                rWEPraesenzBuchenRC.gebuchterVertreterIdent = vmWillenserklaerung.pEclPersonenNatJur.ident;
                erklaerendePerson = vollmachtPersonenNatJurIdent;
                eclErklaerendePerson = vmWillenserklaerung.pEclPersonenNatJur;
            } else {
                /*Bestehender Bevollmächtigter, aber möglicherweise neue Vollmacht*/
                int lPersonNatJur = 0;
                if (vollmachtPersonenNatJurIdent != 0) {
                    lPersonNatJur = vollmachtPersonenNatJurIdent;
                } else {
                    lPersonNatJur = gWEPraesenzBuchen.neueVollmachtPersonenNatJurIdent;
                }
                rWEPraesenzBuchenRC.gebuchterVertreterIdent = lPersonNatJur;

                /*Daten (Vorname etc.) einlesen*/
                lDbBundle.dbPersonenNatJur.read(lPersonNatJur);
                if (lDbBundle.dbPersonenNatJur.anzPersonenNatJurGefunden() == 0) {
                    return CaFehler.pmBevollmaechtigterNichtVorhanden;
                }
                gWEPraesenzBuchen.vertreterVorname = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].vorname;
                gWEPraesenzBuchen.vertreterName = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].name;
                gWEPraesenzBuchen.vertreterOrt = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].ort;

                /*Prüfen, ob angegebene PersonNatJur auch tatsächlich schon Bevollmächtigter ist*/
                BlWillenserklaerung lBlWillenserklaerung = new BlWillenserklaerung();
                lBlWillenserklaerung.setzeDbBundle(lDbBundle);
                lBlWillenserklaerung.piMeldungsIdentAktionaer = hEclMeldung.meldungsIdent;
                lBlWillenserklaerung.einlesenVollmachtenAnDritte();
                int gef = -1;
                if (lBlWillenserklaerung.rcVollmachtenAnDritte != null) {
                    for (int i = 0; i < lBlWillenserklaerung.rcVollmachtenAnDritte.length; i++) {
                        if (lBlWillenserklaerung.rcVollmachtenAnDritte[i].wurdeStorniert == false && lBlWillenserklaerung.rcVollmachtenAnDritte[i].bevollmaechtigtePerson.ident == lPersonNatJur) {
                            gef = 0;
                        }
                    }
                }
                if (gef == -1) {/*Bevollmächtigter hat noch keine Vollmacht - Vollmacht eintragen*/
                    BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                    vmWillenserklaerung.pErteiltAufWeg = gWEPraesenzBuchen.weLoginVerify.getEingabeQuelle();

                    vmWillenserklaerung.piMeldungsIdentAktionaer = hEclMeldung.meldungsIdent;
                    vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                    EclPersonenNatJur personNatJur = new EclPersonenNatJur();
                    personNatJur.ident = lPersonNatJur;
                    vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

                    vmWillenserklaerung.vollmachtAnDritte(lDbBundle);
                    if (vmWillenserklaerung.rcIstZulaessig == false) {
                        return vmWillenserklaerung.rcGrundFuerUnzulaessig;
                    }
                }
                vollmachtPersonenNatJurIdent = lPersonNatJur;

                erklaerendePerson = lPersonNatJur;
                eclErklaerendePerson = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
            }

            /**Wenn aktiviert, dann kann Nebenläufigkeit zwischen Bevollmächtigtem (erst abschicken)
             * und nicht bevollmächtigten (als zweites abschicken) getestet werden*/

            //			System.out.println("Open Wait "+CaDatumZeit.DatumZeitStringFuerDatenbank());
            //			try {
            //				Thread.sleep(10000);
            //			} catch (InterruptedException e) {
            //				e.printStackTrace();
            //			}
            //			System.out.println("Open Ende Wait "+CaDatumZeit.DatumZeitStringFuerDatenbank());

        }
        return 1;
    }

    /**vorhandenEclZutrittskarteAktionaer mit zugehender PersonNatJur updaten*/
    int buchen_updateVorhandenEclZutrittskarteAktionaer() {
        int rc;

        EclZutrittskarten vorhandenEclZutrittskarte = new EclZutrittskarten();
        if (vorhandenEclZutrittskarteAktionaer != null) {
            vorhandenEclZutrittskarte = vorhandenEclZutrittskarteAktionaer;
        }
        if (vorhandenEclZutrittskarteGast != null) {
            vorhandenEclZutrittskarte = vorhandenEclZutrittskarteGast;
        }

        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {

            if (vorhandenEclZutrittskarte != null) {
                if (vorhandenEclZutrittskarte.personenNatJurIdent != vollmachtPersonenNatJurIdent) {
                    if (vollmachtPersonenNatJurIdent != -2) {
                        if (vollmachtPersonenNatJurIdent > 0) {
                            vorhandenEclZutrittskarte.personenNatJurIdent = vollmachtPersonenNatJurIdent;
                        }

                        else {
                            vorhandenEclZutrittskarte.personenNatJurIdent = -1;
                        }
                    } else {
                        if (vorhandenEclZutrittskarte.personenNatJurIdent == 0) {
                            if (vorhandenEclZutrittskarte.ausgestelltAufPersonenNatJurIdent > 0) {
                                vorhandenEclZutrittskarte.personenNatJurIdent = vorhandenEclZutrittskarte.ausgestelltAufPersonenNatJurIdent;
                            }

                            else {
                                vorhandenEclZutrittskarte.personenNatJurIdent = -1;
                            }
                        }
                    }
                }
                rc = lDbBundle.dbZutrittskarten.update(vorhandenEclZutrittskarte);
                if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                    return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                }
            }
        } //Ende Aktionär
        else {/*Meldung ist Gast. Kann mit "Aktionärs-Zutrittskarten" oder Gastkarte zugehen. Allerdings "Aktionärs-Zutrittskarten nur bei
              Wiederzugang möglich!*/
            if (vorhandenEclZutrittskarteGast != null) {//Gast-Zutrittskarte
                vorhandenEclZutrittskarteGast.personenNatJurIdent = -1;
                rc = lDbBundle.dbZutrittskarten.update(vorhandenEclZutrittskarteGast);
                //				System.out.println("erschienene Person Gast=-1");
            } else {//Aktionärs-Zutrittskarte - kann nur bei Wiederzugang auftreten - unverändert lassen!
            }
        }
        return 1;
    }

    /**vorhandeneEclStimmkarten beim Abgang deaktivieren (als Stimmmaterial)*/
    int buchen_updateVorhandenEclStimmkarten_abgang() {
        int rc;

        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {
            /*Stimmkarten. Falls Stimmkarte als Identifikation für Gast verwendet werden, dann sind sie eh schon deaktiviert*/
            for (int i = 0; i < 5; i++) {
                if (vorhandeneEclStimmkarten[i] != null) {/*Bereist zugeordnete ggf. Updaten*/
                    vorhandeneEclStimmkarten[i].stimmkarteIstGesperrt = 1;
                    if (vorgangWirdDelayed == 0) {
                        vorhandeneEclStimmkarten[i].stimmkarteIstGesperrt_Delayed = 1;
                    } else {
                        vorhandeneEclStimmkarten[i].delayedVorhanden = 1;
                    }
                    rc = lDbBundle.dbStimmkarten.update(vorhandeneEclStimmkarten[i]);
                    if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                        return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                    }
                }
            }
        }
        return 1;
    }

    /**Fügt eine neue Stimmkarte in die Datenbank ein. Gleichzeitg ergänzen der neuen Stimmkarte
     * in vorhandeneEclStimmkarten[]*/
    private int buchen_einfuegenNeueEclStimmkarten(int pLfd, String pNummer, int psubKreis) {
        vorhandeneEclStimmkarten[pLfd] = new EclStimmkarten();
        vorhandeneEclStimmkarten[pLfd].stimmkarte = pNummer;
        vorhandeneEclStimmkarten[pLfd].ausSubnummernkreis = psubKreis;
        vorhandeneEclStimmkarten[pLfd].meldungsIdentAktionaer = hEclMeldung.meldungsIdent;
        if (vollmachtPersonenNatJurIdent != -2) { /*Dann übergebenen Bevollmächtigten eintragen*/
            vorhandeneEclStimmkarten[pLfd].personenNatJurIdent = vollmachtPersonenNatJurIdent;
            if (vorgangWirdDelayed == 0) {
                vorhandeneEclStimmkarten[pLfd].personenNatJurIdent_Delayed = vollmachtPersonenNatJurIdent;
            } else {
                vorhandeneEclStimmkarten[pLfd].delayedVorhanden = 1;
            }
        } else {/*Bevollmächtigten aus ZutrittsIdent übernehmen*/
            vorhandeneEclStimmkarten[pLfd].personenNatJurIdent = vorhandenEclZutrittskarteAktionaer.personenNatJurIdent;
            if (vorgangWirdDelayed == 0) {
                vorhandeneEclStimmkarten[pLfd].personenNatJurIdent_Delayed = vorhandenEclZutrittskarteAktionaer.personenNatJurIdent;
            } else {
                vorhandeneEclStimmkarten[pLfd].delayedVorhanden = 1;
            }
        }
        vorhandeneEclStimmkarten[pLfd].zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
        vorhandeneEclStimmkarten[pLfd].zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben;
        vorhandeneEclStimmkarten[pLfd].gueltigeKlasse = 1;
        vorhandeneEclStimmkarten[pLfd].gueltigeKlasse_Delayed = 1;

        int rc = lDbBundle.dbStimmkarten.insert(vorhandeneEclStimmkarten[pLfd]);
        if (rc != 1) {
            return rc;
        }
        bereitsZugeordneteStimmkarten[pLfd] = identifikationStimmkarten[pLfd];

        return 1;
    }

    /**vorhandeneEclStimmkarten mit zugehender PersonNatJur updaten und mit zutrittsIdent zuordnen*/
    private int buchen_updateVorhandenEclStimmkarten() {
        int rc;

        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {
            /*Stimmkarten*/
            /*Hinweis: falls als Gastkarten verwendet, dann ist das sicher kein Erstzugang - und damit kann alles
             * unverändert bleiben.
             */
            for (int i = 0; i < 5; i++) {
                if (zuordnungErforderlich[i] == 1) {

                    if (vorhandeneEclStimmkarten[i] != null) {/*Bereist zugeordnete ggf. Updaten*/
                        int updaten = 0;
                        if (vorhandeneEclStimmkarten[i].personenNatJurIdent != vollmachtPersonenNatJurIdent && vollmachtPersonenNatJurIdent != -2) {
                            vorhandeneEclStimmkarten[i].personenNatJurIdent = vollmachtPersonenNatJurIdent;
                            if (vorgangWirdDelayed == 0) {
                                vorhandeneEclStimmkarten[i].personenNatJurIdent_Delayed = vollmachtPersonenNatJurIdent;
                            } else {
                                vorhandeneEclStimmkarten[i].delayedVorhanden = 1;
                            }
                            updaten = 1;
                        }
                        if (vorhandeneEclStimmkarten[i].stimmkarteIstGesperrt == 1) {
                            vorhandeneEclStimmkarten[i].stimmkarteIstGesperrt = 0;
                            if (vorgangWirdDelayed == 0) {
                                vorhandeneEclStimmkarten[i].stimmkarteIstGesperrt_Delayed = 0;
                            } else {
                                vorhandeneEclStimmkarten[i].delayedVorhanden = 1;
                            }
                            updaten = 1;
                        }
                        if (vorhandeneEclStimmkarten[i].zutrittsIdent.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent) != 0
                                || vorhandeneEclStimmkarten[i].zutrittsIdentNeben.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben) != 0) {
                            vorhandeneEclStimmkarten[i].zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                            vorhandeneEclStimmkarten[i].zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben;
                            updaten = 1;
                        }
                        if (updaten == 1) {
                            rc = lDbBundle.dbStimmkarten.update(vorhandeneEclStimmkarten[i]);
                            if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                            }
                        }
                    }

                    else {/*Neu zugeordnete Speichern*/
                        rc = buchen_einfuegenNeueEclStimmkarten(i, identifikationStimmkarten[i], i + 1 + zuordnungNummernkreisOffset);
                        if (rc != 1) {
                            return rc;
                        }
                    }
                }
            }
        }

        return 1;
    }

    /**vorhandenEclStimmkartenSecond beim Abgang deaktivieren (als Stimmmaterial)*/
    int buchen_updateVorhandenEclStimmkartenSecond_abgang() {
        int rc;

        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {
            if (vorhandeneStimmkarteSecond != null) {/*Bereist zugeordnete ggf. Updaten*/
                vorhandeneStimmkarteSecond.stimmkarteSecondIstGesperrt = 1;
                if (vorgangWirdDelayed == 0) {
                    vorhandeneStimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed = 1;
                } else {
                    vorhandeneStimmkarteSecond.delayedVorhanden = 1;
                }
                rc = lDbBundle.dbStimmkartenSecond.update(vorhandeneStimmkarteSecond);
                if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                    return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                }

            }
        }
        return 1;
    }

    /**vorhandenEclStimmkarten mit zugehender PersonNatJur updaten und mit zutrittsIdent zuordnen*/
    int buchen_updateVorhandenEclStimmkartenSecond() {
        int rc;

        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {

            /*StimmkartenSecond*/
            if (zuordnungSecondErforderlich == 1) {
                if (vorhandeneStimmkarteSecond != null) {/*Bereist zugeordnete ggf. Updaten*/
                    int updaten = 0;
                    if (vorhandeneStimmkarteSecond.personenNatJurIdent != vollmachtPersonenNatJurIdent && vollmachtPersonenNatJurIdent != -2) {
                        vorhandeneStimmkarteSecond.personenNatJurIdent = vollmachtPersonenNatJurIdent;
                        if (vorgangWirdDelayed == 0) {
                            vorhandeneStimmkarteSecond.personenNatJurIdent_Delayed = vollmachtPersonenNatJurIdent;
                        } else {
                            vorhandeneStimmkarteSecond.delayedVorhanden = 1;
                        }
                        updaten = 1;
                    }
                    if (vorhandeneStimmkarteSecond.stimmkarteSecondIstGesperrt == 1) {
                        vorhandeneStimmkarteSecond.stimmkarteSecondIstGesperrt = 0;
                        if (vorgangWirdDelayed == 0) {
                            vorhandeneStimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed = 0;
                        } else {
                            vorhandeneStimmkarteSecond.delayedVorhanden = 1;
                        }
                        updaten = 1;
                    }
                    if (vorhandeneStimmkarteSecond.zutrittsIdent.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent) != 0
                            || vorhandeneStimmkarteSecond.zutrittsIdentNeben.compareTo(bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben) != 0) {
                        vorhandeneStimmkarteSecond.zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                        vorhandeneStimmkarteSecond.zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben;
                        updaten = 1;
                    }
                    if (updaten == 1) {
                        rc = lDbBundle.dbStimmkartenSecond.update(vorhandeneStimmkarteSecond);
                        if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                        }
                    }
                }

                else {/*Neu zugeordnete Speichern*/
                    vorhandeneStimmkarteSecond = new EclStimmkartenSecond();
                    vorhandeneStimmkarteSecond.stimmkarteSecond = identifikationStimmkarteSecond;
                    vorhandeneStimmkarteSecond.meldungsIdentAktionaer = hEclMeldung.meldungsIdent;
                    if (vollmachtPersonenNatJurIdent != -2) { /*Dann übergebenen Bevollmächtigten eintragen*/
                        vorhandeneStimmkarteSecond.personenNatJurIdent = vollmachtPersonenNatJurIdent;
                        if (vorgangWirdDelayed == 0) {
                            vorhandeneStimmkarteSecond.personenNatJurIdent_Delayed = vollmachtPersonenNatJurIdent;
                        } else {
                            vorhandeneStimmkarteSecond.delayedVorhanden = 1;
                        }
                    } else {/*Bevollmächtigten aus ZutrittsIdent übernehmen*/
                        vorhandeneStimmkarteSecond.personenNatJurIdent = vorhandenEclZutrittskarteAktionaer.personenNatJurIdent;
                        if (vorgangWirdDelayed == 0) {
                            vorhandeneStimmkarteSecond.personenNatJurIdent_Delayed = vorhandenEclZutrittskarteAktionaer.personenNatJurIdent;
                        } else {
                            vorhandeneStimmkarteSecond.delayedVorhanden = 1;
                        }
                    }
                    vorhandeneStimmkarteSecond.zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                    vorhandeneStimmkarteSecond.zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben;
                    vorhandeneStimmkarteSecond.gueltigeKlasse = 1;
                    vorhandeneStimmkarteSecond.gueltigeKlasse_Delayed = 1;
                    rc = lDbBundle.dbStimmkartenSecond.insert(vorhandeneStimmkarteSecond);
                    if (rc != 1) {
                        return rc;
                    }
                    bereitsZugeordneteStimmkartenSecond = identifikationStimmkarteSecond;
                }
            }
        }

        return 1;
    }

    /**Protokollnr und Teilnehmerverzeichnisnummern einlesen*/
    private void buchen_leseGlobalePraesenzVariablenHV() {
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

        BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
        lBlPraesenzProtokoll.leseProtokollNr();
    }

    private void buchen_willenserklaerungErzeugen_fuelleZutrittsStimmkartenIdents(EclWillenserklaerung pPreparedWillenserklaerung) {
        EclZutrittsIdent hZutrittsIdentAdd = new EclZutrittsIdent();

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_willenserklaerungErzeugen_fuelleZutrittsStimmkartenIdents", logDrucken, 10);

        if (identifikationZutrittsIdentAktionaer != null && !identifikationZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
            CaBug.druckeLog("buchen_willenserklaerungErzeugen_fuelleZutrittsStimmkartenIdents in erstem IF", logDrucken, 10);

            pPreparedWillenserklaerung.identifikationDurch = 2;
            pPreparedWillenserklaerung.identifikationZutrittsIdent = identifikationZutrittsIdentAktionaer.zutrittsIdent;
            pPreparedWillenserklaerung.identifikationZutrittsIdentNeben = identifikationZutrittsIdentAktionaer.zutrittsIdentNeben;
            pPreparedWillenserklaerung.identifikationKlasse = 1;

            pPreparedWillenserklaerung.zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
            pPreparedWillenserklaerung.zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdentNeben;

            hZutrittsIdentAdd = bereitsZugeordneteZutrittsIdentAktionaer;
        }
        if (identifikationZutrittsIdentGast != null && !identifikationZutrittsIdentGast.zutrittsIdent.isEmpty()) {
            pPreparedWillenserklaerung.identifikationDurch = 2;
            pPreparedWillenserklaerung.identifikationZutrittsIdent = identifikationZutrittsIdentGast.zutrittsIdent;
            pPreparedWillenserklaerung.identifikationZutrittsIdentNeben = identifikationZutrittsIdentGast.zutrittsIdentNeben;
            pPreparedWillenserklaerung.identifikationKlasse = 0;

            pPreparedWillenserklaerung.zutrittsIdent = bereitsZugeordneteZutrittsIdentGast.zutrittsIdent;
            pPreparedWillenserklaerung.zutrittsIdentNeben = bereitsZugeordneteZutrittsIdentGast.zutrittsIdentNeben;

            hZutrittsIdentAdd = bereitsZugeordneteZutrittsIdentGast;
        }

        rWEPraesenzBuchenRC.zutrittsIdent.add(hZutrittsIdentAdd);

        int gef = 0;
        for (int i = 0; i < 5; i++) {
            if (identifikationStimmkarten[i] != null && !identifikationStimmkarten[i].isEmpty()) {
                gef = 1;
            }
        }
        if (gef == 1) {
            if (identifikationStimmkarten[0] != null) {
                pPreparedWillenserklaerung.identifikationStimmkarte = identifikationStimmkarten[0];
            }
            if (identifikationStimmkarten[1] != null) {
                pPreparedWillenserklaerung.identifikationStimmkarte = identifikationStimmkarten[1];
            }
            if (identifikationStimmkarten[2] != null) {
                pPreparedWillenserklaerung.identifikationStimmkarte = identifikationStimmkarten[2];
            }
            if (identifikationStimmkarten[3] != null) {
                pPreparedWillenserklaerung.identifikationStimmkarte = identifikationStimmkarten[3];
            }
            if (identifikationStimmkarten[4] != null) {
                pPreparedWillenserklaerung.identifikationStimmkarte = identifikationStimmkarten[4];
            }
            pPreparedWillenserklaerung.identifikationDurch = 4;
        }

        if (!identifikationStimmkarteSecond.isEmpty()) {
            pPreparedWillenserklaerung.identifikationDurch = 5;
            pPreparedWillenserklaerung.identifikationStimmkarteSecond = identifikationStimmkarteSecond;
        }

        String[] lStimmkarten = new String[] { "", "", "", "", "", "" };
        if (bereitsZugeordneteStimmkarten[0] != null) {
            pPreparedWillenserklaerung.stimmkarte1 = bereitsZugeordneteStimmkarten[0];
            lStimmkarten[0] = bereitsZugeordneteStimmkarten[0];
        }
        if (bereitsZugeordneteStimmkarten[1] != null) {
            pPreparedWillenserklaerung.stimmkarte2 = bereitsZugeordneteStimmkarten[1];
            lStimmkarten[1] = bereitsZugeordneteStimmkarten[1];
        }
        if (bereitsZugeordneteStimmkarten[2] != null) {
            pPreparedWillenserklaerung.stimmkarte3 = bereitsZugeordneteStimmkarten[2];
            lStimmkarten[2] = bereitsZugeordneteStimmkarten[2];
        }
        if (bereitsZugeordneteStimmkarten[3] != null) {
            pPreparedWillenserklaerung.stimmkarte4 = bereitsZugeordneteStimmkarten[3];
            lStimmkarten[3] = bereitsZugeordneteStimmkarten[3];
        }

        pPreparedWillenserklaerung.stimmkarteSecond = bereitsZugeordneteStimmkartenSecond;
        lStimmkarten[5] = bereitsZugeordneteStimmkartenSecond;

        rWEPraesenzBuchenRC.zugeordneteStimmkarten.add(lStimmkarten);
    }

    /**Willenserklärung erzeugen*/
    private int buchen_willenserklaerungErzeugen() {
        int rc = 0;

        /*Präsenz buchen.*/
        prepareWillenserklaerung(funktion);
        buchen_willenserklaerungErzeugen_fuelleZutrittsStimmkartenIdents(preparedWillenserklaerung);

        preparedWillenserklaerung.willenserklaerungGeberIdent = erklaerendePerson;
        /*TODO _Praesenz: Konsolidieren zugehende / abgehende Person durchgängig eintragen / prüfen etc.. Vor ku164 "notdürftig" geflickt. Hat aber die Saison über gehalten.*/
        /* Vorsorglich ...
         * Zum korrekten Eintrag bei Abgängen / Wiederzugängen
         */
        if (erklaerendePerson != -1) {
            preparedWillenserklaerung.bevollmaechtigterDritterIdent = erklaerendePerson;
        }

        /*Nur bei Aktionär: Vertreter eintragen*/
        if (hEclMeldung.meldungIstEinAktionaer()) {
            if (vollmachtPersonenNatJurIdent != -1) { /*sonst Selbst - dann nix eintragen*/
                if (vollmachtPersonenNatJurIdent != -2) { /*Dann Neuer Vertreter*/
                    preparedWillenserklaerung.bevollmaechtigterDritterIdent = vollmachtPersonenNatJurIdent;
                }

            }
        }

        rc = lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung, preparedWillenserklaerungZusatz);
        if (rc < 0) {
            CaBug.drucke("BlPraesenzAkkreditierung.buchen_willenserklaerungErzeugen 001");
        }

        BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
        boolean druckenProtokoll = lBlPraesenzProtokoll.erhoeheAnzahlEintrage(funktion);
        if (druckenProtokoll == true) {
            rWEPraesenzBuchenRC.buendeln = true;
            rWEPraesenzBuchenRC.buendelnProtokollNr = lBlPraesenzProtokoll.buendelnProtokollNr;
            lBlPraesenzProtokoll.drucken();
        }

        return 1;
    }

    private void buchen_updateHEclMeldungIstIdentisch() {
        //		System.out.println("buchen_updateHEclMeldungIstIdentisch fuer ="+iIdentifikation);
        if (gWEPraesenzBuchen.istIdentisch == null || gWEPraesenzBuchen.istIdentisch.size() == 0) {
            return;
        }
        if (gWEPraesenzBuchen.istIdentisch.get(iIdentifikation) == -1 || gWEPraesenzBuchen.istIdentisch.get(iIdentifikation) == hEclMeldung.personenNatJurIdent) {
            //			System.out.println("Update istSelbePersonWieIdent="+gWEPraesenzBuchen.appIdentPersonNatJurIdent);
            hEclMeldung.istSelbePersonWieIdent = gWEPraesenzBuchen.appIdentPersonNatJurIdent;
            hEclMeldung.uebereinstimmungSelbePersonWurdeUeberprueft = 1;
        }
    }

    /*nurAbgang==1 => es werden nur die Daten für Abgang aktualisiert.
     */
    private int buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang(int nurAbgang) {
        int rc = 0;
        if (hEclMeldung != null && hEclMeldung.meldungIstEinAktionaer()) {
            if (hEclMeldung.skIst != 0) {
                /*+++++Buchung betrifft eine Sammelkarte++++++*/
                if (hEclMeldung.liefereOffenlegungTatsaechlich(lDbBundle) == 1) {
                    /*Mit Offenlegung => (ggf EK), SK, Vertreter, Präsenzstation in die einzelnen Aktionäre übertragen*/
                    lDbBundle.dbMeldungZuSammelkarte.leseZuSammelkarte(hEclMeldung.meldungsIdent);
                    int anzMeldungenInSammelkarte = lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();
                    EclMeldungZuSammelkarte[] gefundeneMeldungenZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteArray;
                    for (int i1 = 0; i1 < anzMeldungenInSammelkarte; i1++) {/*Für alle Meldungen in der Sammelkarte*/
                        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang i1=" + i1, logDrucken, 10);
                        //						try {
                        //							Thread.sleep(1000);
                        //						} catch (InterruptedException e) {
                        //							// TODO Auto-generated catch block
                        //							e.printStackTrace();
                        //						}
                        EclMeldungZuSammelkarte eclMeldungZuSammelkarte = gefundeneMeldungenZuSammelkarte[i1];
                        if (eclMeldungZuSammelkarte.aktiv == 1) {
                            EclMeldung einzelMeldung = new EclMeldung();
                            einzelMeldung.meldungsIdent = eclMeldungZuSammelkarte.meldungsIdent;
                            lDbBundle.dbMeldungen.leseZuMeldungsIdent(einzelMeldung);
                            einzelMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                            if (vorgangWirdDelayed == 1) {
                                einzelMeldung.delayedVorhanden = 1;
                            }
                            if (einzelMeldung.zutrittsIdent.isEmpty()) {/*ZutrittsIdent nur übernehmen, wenn nicht schon eigene vergeben*/
                                einzelMeldung.zutrittsIdent = hEclMeldung.zutrittsIdent;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.zutrittsIdent_Delayed = hEclMeldung.zutrittsIdent;
                                }
                            }
                            if (nurAbgang == 0) {
                                einzelMeldung.stimmkarte = hEclMeldung.stimmkarte;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.stimmkarte_Delayed = hEclMeldung.stimmkarte;
                                }
                                einzelMeldung.stimmkarteSecond = hEclMeldung.stimmkarteSecond;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.stimmkarteSecond_Delayed = hEclMeldung.stimmkarteSecond;
                                }
                                einzelMeldung.vertreterName = hEclMeldung.vertreterName;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.vertreterName_Delayed = hEclMeldung.vertreterName;
                                }
                                einzelMeldung.vertreterVorname = hEclMeldung.vertreterVorname;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.vertreterVorname_Delayed = hEclMeldung.vertreterVorname;
                                }
                                einzelMeldung.vertreterOrt = hEclMeldung.vertreterOrt;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.vertreterOrt_Delayed = hEclMeldung.vertreterOrt;
                                }
                                einzelMeldung.vertreterIdent = hEclMeldung.vertreterIdent;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.vertreterIdent_Delayed = hEclMeldung.vertreterIdent;
                                }
                            }
                            if (nurAbgang == 0) {
                                einzelMeldung.statusPraesenz = 4;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.statusPraesenz_Delayed = 4;
                                }
                            } else {
                                einzelMeldung.statusPraesenz = 2;
                                if (vorgangWirdDelayed == 0) {
                                    einzelMeldung.statusPraesenz_Delayed = 2;
                                }
                            }

                            /*ohne PersonNatJur, da sonst beim gleichzeiteigen Präsenzsetzen von zwei EKs des
                             * selben Aktionärs (unterschiedliche Meldungen) "vom anderen Benutzer geändert" kommt!
                             */
                            rc = lDbBundle.dbMeldungen.update(einzelMeldung, false);
                            if (rc < 1) {
                                return rc;
                            }
                            /*Nun noch Willenserklärung für Präsenzliste für diesen Einzelaktionär schreiben*/
                            prepareWillenserklaerung2(funktion);
                            CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang einzelMeldung.meldungsIdent=" + einzelMeldung.meldungsIdent,
                                    logDrucken, 10);
                            CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang preparedWillenserklaerung2.meldungsIdent="
                                    + preparedWillenserklaerung2.meldungsIdent, logDrucken, 10);

                            preparedWillenserklaerung2.meldungsIdent = einzelMeldung.meldungsIdent;
                            preparedWillenserklaerung2.stimmen = einzelMeldung.stimmen;
                            preparedWillenserklaerung2.aktien = einzelMeldung.stueckAktien;

                            buchen_willenserklaerungErzeugen_fuelleZutrittsStimmkartenIdents(preparedWillenserklaerung2);

                            preparedWillenserklaerung2.willenserklaerungGeberIdent = hEclMeldung.vertreterIdent;
                            preparedWillenserklaerung2.bevollmaechtigterDritterIdent = hEclMeldung.vertreterIdent;
                            preparedWillenserklaerung2.folgeBuchungFuerIdent = preparedWillenserklaerung.willenserklaerungIdent;
                            preparedWillenserklaerung2.identMeldungZuSammelkarte = hEclMeldung.meldungsIdent;
                            rc = lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung2, preparedWillenserklaerung2Zusatz);
                            //							lDbBundle.closeAll(); /*wird gemacht, um Sperren aufzuheben*/
                            //							lDbBundle.openAllOhneParameterCheck();
                            //							lDbBundle.dbBasis.beginTransaction();

                        }
                    }

                }
            }
        }
        return 1;
    }

    private int buchen_updateHEclMeldungAktionaer() {
        int rc;

        /*Aktuelle EK, SK, Vertreter, Präsenzstatus*/
        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_updateHEclMeldungAktionaer", logDrucken, 10);
        if (hEclMeldung.meldungIstEinAktionaer()) {

            /*Aktionär erneut einlesen und prüfen - da möglicherweise durch andere Willenserklärungen verändert!*/
            rc = buchenvorbereiten_leseEclMeldung(hEclMeldung.meldungsIdent);
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }

            if (funktion == KonstWillenserklaerung.erstzugang || funktion == KonstWillenserklaerung.wiederzugang) {/*Erstzugang oder Wiederzugang*/
                rc = buchen_aktionaerPruefenErstzugang();
                if (rc < 0) {
                    return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                }
            }
            if (funktion == KonstWillenserklaerung.vertreterwechsel) {/*Vertreterwechsel*/
                rc = buchen_aktionaerPruefenAbgang();
                if (rc < 0) {
                    return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                }
            }

            if (bereitsZugeordneteZutrittsIdentAktionaer != null && !bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
                hEclMeldung.zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.zutrittsIdent_Delayed = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                }
            }
            if (zuordnungErforderlich[0] == 1) {
                hEclMeldung.stimmkarte = bereitsZugeordneteStimmkarten[0];
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.stimmkarte_Delayed = bereitsZugeordneteStimmkarten[0];
                }
            }
            hEclMeldung.stimmkarteSecond = bereitsZugeordneteStimmkartenSecond;
            if (vorgangWirdDelayed == 0) {
                hEclMeldung.stimmkarteSecond_Delayed = bereitsZugeordneteStimmkartenSecond;
            }
            CaBug.druckeLog("gWEPraesenzBuchen.vollmachtPersonenNatJurIdent=" + gWEPraesenzBuchen.vollmachtPersonenNatJurIdent, logDrucken, 10);
            if (erklaerendePerson > 0) {
                hEclMeldung.vertreterName = eclErklaerendePerson.name;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.vertreterName_Delayed = eclErklaerendePerson.name;
                }
                hEclMeldung.vertreterVorname = eclErklaerendePerson.vorname;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.vertreterVorname_Delayed = eclErklaerendePerson.vorname;
                }
                hEclMeldung.vertreterOrt = eclErklaerendePerson.ort;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.vertreterOrt_Delayed = eclErklaerendePerson.ort;
                }
                hEclMeldung.vertreterIdent = erklaerendePerson;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.vertreterIdent_Delayed = erklaerendePerson;
                }
            } else {
                hEclMeldung.vertreterName = "";
                hEclMeldung.vertreterVorname = "";
                hEclMeldung.vertreterOrt = "";
                hEclMeldung.vertreterIdent = 0;
                if (vorgangWirdDelayed == 0) {
                    hEclMeldung.vertreterName_Delayed = "";
                    hEclMeldung.vertreterVorname_Delayed = "";
                    hEclMeldung.vertreterOrt_Delayed = "";
                    hEclMeldung.vertreterIdent_Delayed = 0;
                }
            }
            hEclMeldung.statusPraesenz = 1;
            hEclMeldung.statusWarPraesenz = 1;
            if (vorgangWirdDelayed == 0) {
                hEclMeldung.statusPraesenz_Delayed = 1;
                hEclMeldung.statusWarPraesenz_Delayed = 1;
            }
            if (vorgangWirdDelayed == 1) {
                hEclMeldung.delayedVorhanden = 1;
            }
            buchen_updateHEclMeldungIstIdentisch();
            /*ohne PersonNatJur, da sonst beim gleichzeiteigen Präsenzsetzen von zwei EKs des
             * selben Aktionärs (unterschiedliche Meldungen) "vom anderen Benutzer geändert" kommt!
             */
            rc = lDbBundle.dbMeldungen.update(hEclMeldung, false);
            if (rc < 1) {
                return rc;
            }

            rc = buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang(0);
            if (rc < 1) {
                return rc;
            }
        }

        return 1;
    }

    private int buchen_updateHEclMeldungAktionaer_nurAbgang() {
        int rc;

        /*Aktuelle Präsenzstatus*/
        if (hEclMeldung.meldungIstEinAktionaer()) {

            /*Aktionär erneut einlesen und prüfen - da möglicherweise durch andere Willenserklärungen verändert!*/
            rc = buchenvorbereiten_leseEclMeldung(hEclMeldung.meldungsIdent);
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }
            rc = buchen_aktionaerPruefenAbgang();
            if (rc < 0) {
                return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
            }

            hEclMeldung.statusPraesenz = 2;
            hEclMeldung.statusWarPraesenz = 1;
            if (vorgangWirdDelayed == 0) {
                hEclMeldung.statusPraesenz_Delayed = 2;
                hEclMeldung.statusWarPraesenz_Delayed = 1;
            }

            if (vorgangWirdDelayed == 1) {
                hEclMeldung.delayedVorhanden = 1;
            }

            /*ohne PersonNatJur, da sonst beim gleichzeiteigen Präsenzsetzen von zwei EKs des
             * selben Aktionärs (unterschiedliche Meldungen) "vom anderen Benutzer geändert" kommt!
             */
            rc = lDbBundle.dbMeldungen.update(hEclMeldung, false);
            if (rc < 1) {
                return rc;
            }

            rc = buchen_updateHEclMeldungAktionaer_Sammelkarte_ZugangUndAbgang(1);
            if (rc < 1) {
                return rc;
            }
        }

        return 1;
    }

    private int buchen_updateHEclMeldungGast() {
        int rc;

        /*Aktuelle EK, Präsenzstatus*/
        if (hEclMeldung.meldungIstEinGast()) {

            if (bereitsZugeordneteZutrittsIdentAktionaer != null && !bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent.isEmpty()) {
                hEclMeldung.zutrittsIdent = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
                hEclMeldung.zutrittsIdent_Delayed = bereitsZugeordneteZutrittsIdentAktionaer.zutrittsIdent;
            } else {
                hEclMeldung.zutrittsIdent = bereitsZugeordneteZutrittsIdentGast.zutrittsIdent;
                hEclMeldung.zutrittsIdent_Delayed = bereitsZugeordneteZutrittsIdentGast.zutrittsIdent;
            }
            hEclMeldung.stimmkarte = bereitsZugeordneteStimmkarten[1];
            hEclMeldung.stimmkarte_Delayed = bereitsZugeordneteStimmkarten[1];
            hEclMeldung.stimmkarteSecond = bereitsZugeordneteStimmkartenSecond;
            hEclMeldung.stimmkarteSecond_Delayed = bereitsZugeordneteStimmkartenSecond;
            hEclMeldung.statusPraesenz = 1;
            hEclMeldung.statusPraesenz_Delayed = 1;
            hEclMeldung.statusWarPraesenz = 1;
            hEclMeldung.statusWarPraesenz_Delayed = 1;
            buchen_updateHEclMeldungIstIdentisch();
            /*ohne PersonNatJur, da sonst beim gleichzeiteigen Präsenzsetzen von zwei EKs des
             * selben Aktionärs (unterschiedliche Meldungen) "vom anderen Benutzer geändert" kommt!
             */
            rc = lDbBundle.dbMeldungen.update(hEclMeldung, false);
            if (rc < 1) {
                return rc;
            }
        }
        return 1;
    }

    private int buchen_updateHEclMeldungGast_nurAbgang() {
        int rc;
        /*Aktuelle  Präsenzstatus*/
        if (hEclMeldung.meldungIstEinGast()) {
            hEclMeldung.statusPraesenz = 2;
            hEclMeldung.statusPraesenz_Delayed = 2;
            hEclMeldung.statusWarPraesenz = 1;
            hEclMeldung.statusWarPraesenz_Delayed = 1;
            /*ohne PersonNatJur, da sonst beim gleichzeiteigen Präsenzsetzen von zwei EKs des
             * selben Aktionärs (unterschiedliche Meldungen) "vom anderen Benutzer geändert" kommt!
             */
            rc = lDbBundle.dbMeldungen.update(hEclMeldung, false);
            if (rc < 1) {
                return rc;
            }
        }

        return 1;
    }

    private void buchen_updateVertreterIstIdentisch() {
        if (gWEPraesenzBuchen.istIdentisch == null || gWEPraesenzBuchen.istIdentisch.size() == 0) {
            return;
        }
        if (gWEPraesenzBuchen.istIdentisch.get(iIdentifikation) > 0) {

            lDbBundle.dbPersonenNatJur.read(gWEPraesenzBuchen.istIdentisch.get(iIdentifikation));
            EclPersonenNatJur lPersonNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            lPersonNatJur.istSelbePersonWieIdent = gWEPraesenzBuchen.appIdentPersonNatJurIdent;
            lPersonNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = 1;
            lDbBundle.dbPersonenNatJur.update(lPersonNatJur);
        }

    }

    /**Bucht die Stimmkarten, die automatisch zugeordnet werden.
     * D.h. konkret:
     * > bei Zuordnung 1:1 wird Stimmkarte [0] identisch mit der Eintrittskartennummer gesetzt (auch bei AppIdent)
     * > bei AppIdent wird zusätzlich [3] aus dem virtuellen Nummernkreis vergeben (das ist dann die Stimmkartennummer,
     * 		die bei der Abstimmung mit der App verwendet wird)
     */
    private int buchen_StimmkartenZuordnungAutomatisch() {
        /*Bei Zuordnung 1:1*/
        if (lDbBundle.param.paramAkkreditierung.eintrittskarteWirdStimmkarte) {
            //			/*Prüfen - ist Zuordnung schon vorhanden?*/
            //			if (bereitsZugeordneteStimmkarten[0].isEmpty()){
            //				/*Falls nnoch nicht vorhanden, dann Zuordnen*/
            //				String hStimmkarte=identifikationZutrittsIdentAktionaer.zutrittsIdent;
            //				if (lDbBundle.param.paramNummernformen.beiEintrittskarteWirdStimmkarteNebenAnhaengen){
            //					hStimmkarte=hStimmkarte+identifikationZutrittsIdentAktionaer.zutrittsIdentNeben;
            //				}
            //				int rc=buchen_einfuegenNeueEclStimmkarten(0, hStimmkarte,  0+1+zuordnungNummernkreisOffset);
            //				if (rc!=1){
            //					return rc;
            //				}
            //			}
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.BlPraesenzAkkreditierung A", logDrucken, 10);

        /*AppIdent - [4] wird immer aus virtuellem Kreis vergeben*/
        if (lDbBundle.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[hEclMeldung.liefereGattung() - 1][4] == 1) {
            /*Prüfen - ist Zuordnung schon vorhanden?*/
            if (bereitsZugeordneteStimmkarten[3].isEmpty()) {
                /*Falls nnoch nicht vorhanden, dann Zuordnen*/
                int neueStimmkarte = lDbBundle.dbBasis.getStimmkartennummer(hEclMeldung.liefereGattung());
                /*TODO _Fehlerbehandlung: Abfangen, wenn Nummernkreis überschritten*/

                CaBug.druckeLog("C neueStimmkarte=" + neueStimmkarte, logDrucken, 10);

                String hNeueStimmkarte = Integer.toString(neueStimmkarte);
                BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                String hStimmkarte = blNummernformen.formatiereNr(hNeueStimmkarte, KonstKartenklasse.stimmkartennummer);
                int rc = buchen_einfuegenNeueEclStimmkarten(0, hStimmkarte, 4 + 1 + zuordnungNummernkreisOffset);
                if (rc != 1) {
                    return rc;
                }
            }
        }
        CaBug.druckeLog("BlPraesenzAkkreditierung.BlPraesenzAkkreditierung B", logDrucken, 10);

        return 1;
    }

    private WEPraesenzBuchenRC buchen_erstzugang_wiederzugang(int iIdentifikation) {
        int rc = 0;

        if (lDbBundle.paramGeraet.akkreditierungDelayIgnorieren == false && (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 2
                || (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 1 && (lDbBundle.param.paramAkkreditierung.delayArt == 2 || lDbBundle.param.paramAkkreditierung.delayArt == 3)))) {
            vorgangWirdDelayed = 1;
        }

        /*Zugangsbuchung nicht möglich, wenn bereits präsent*/
        rc = buchen_pruefenObMeldungAbwesendBzwImmerAbwesendWar();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Vollmacht eintragen*/
        rc = buchen_VollmachtZuordnen();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Zuordnung eintragen - ZutrittsIdent, Stimmkarten, StimmkartenSecond*/
        rc = buchen_updateVorhandenEclZutrittskarteAktionaer();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        rc = buchen_updateVorhandenEclStimmkarten();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        rc = buchen_updateVorhandenEclStimmkartenSecond();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        rc = buchen_StimmkartenZuordnungAutomatisch();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        //		buchen_leseGlobalePraesenzVariablenHV(); //Verschoben zu buchen() wg. Dead-Lock-Vermeidung

        buchen_setzenErklaerendePerson();

        rc = buchen_willenserklaerungErzeugen();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Meldungssatz aktualisieren*/
        rc = buchen_updateHEclMeldungAktionaer();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {
            /*Login-Daten freigeben*/
            lDbBundle.dbLoginDaten.update_userGesperrt(hEclMeldung.aktionaersnummer, false);
        }
        
        
        rc = buchen_updateHEclMeldungGast();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        buchen_updateVertreterIstIdentisch();

        /*Präsenzsummen korrigieren*/
        if (hEclMeldung.meldungIstEinAktionaer()) {
            BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            lBlPraesenzSummen.zugang(hEclMeldung.stueckAktien, hEclMeldung.gattung, vorgangWirdDelayed);
        }

        rWEPraesenzBuchenRC.rc = 1;
        return rWEPraesenzBuchenRC;
    }

    private WEPraesenzBuchenRC buchen_abgang(int iIdentifikation) {
        int rc = 0;
        erklaerendePerson = -1;

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_abgang", logDrucken, 10);
        CaBug.druckeLog("Buchen abgang  lDbBundle.param.paramGeraet.akkreditierungDelayIgnorieren=" + lDbBundle.paramGeraet.akkreditierungDelayIgnorieren, logDrucken, 10);
        CaBug.druckeLog("Buchen abgang  lDbBundle.param.parameterAlt.plfdHVDelayed=" + lDbBundle.param.paramAkkreditierung.plfdHVDelayed, logDrucken, 10);
        CaBug.druckeLog("Buchen abgang  lDbBundle.param.paramAkkreditierung.delayArt=" + lDbBundle.param.paramAkkreditierung.delayArt, logDrucken, 10);
        if (lDbBundle.paramGeraet.akkreditierungDelayIgnorieren == false && (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 2
                || (lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 1 && (lDbBundle.param.paramAkkreditierung.delayArt == 1 || lDbBundle.param.paramAkkreditierung.delayArt == 3)))) {
            CaBug.druckeLog("Wird Delayed", logDrucken, 10);
            vorgangWirdDelayed = 1;
        }

        /*Prüfen, ob bereits präsent (gewesen)*/
        rc = buchen_pruefenObMeldungAnwesend();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Ermitteln, welche Person abgeht. Dies erfolgt anhand der PersonNatJur aus der
         * Identfikation, die "gilt" (identifizierungErfolgtUber)
         */
        buchen_setzenErklaerendePerson();

        rc = buchen_updateVorhandenEclStimmkarten_abgang();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        //		buchen_leseGlobalePraesenzVariablenHV(); //Verschoben zu buchen() wg. Dead-Lock-Vermeidung

        rc = buchen_willenserklaerungErzeugen();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Meldungssatz aktualisieren*/
        rc = buchen_updateHEclMeldungAktionaer_nurAbgang();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {
            /*Login-Daten freigeben*/
            lDbBundle.dbLoginDaten.update_userGesperrt(hEclMeldung.aktionaersnummer, true);
        }

        rc = buchen_updateHEclMeldungGast_nurAbgang();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Präsenzsummen korrigieren*/
        if (hEclMeldung.meldungIstEinAktionaer()) {
            BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            lBlPraesenzSummen.abgang(hEclMeldung.stueckAktien, hEclMeldung.gattung, vorgangWirdDelayed);
        }

        rWEPraesenzBuchenRC.rc = 1;
        return rWEPraesenzBuchenRC;
    }

    private WEPraesenzBuchenRC buchen_vertreterwechsel(int iIdentifikation) {
        int rc = 0;

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_vertreterwechsel=" + iIdentifikation, logDrucken, 10);
        if (lDbBundle.paramGeraet.akkreditierungDelayIgnorieren == false && lDbBundle.param.paramAkkreditierung.plfdHVDelayed == 2) {
            vorgangWirdDelayed = 1;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel A", logDrucken, 10);

        rc = buchen_pruefenObMeldungAnwesend();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        /*Vollmacht eintragen*/
        rc = buchen_VollmachtZuordnen();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel B", logDrucken, 10);

        /*Zuordnung eintragen - ZutrittsIdent, Stimmkarten, StimmkartenSecond*/
        rc = buchen_updateVorhandenEclZutrittskarteAktionaer();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel C", logDrucken, 10);

        rc = buchen_updateVorhandenEclStimmkarten();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel D", logDrucken, 10);

        rc = buchen_updateVorhandenEclStimmkartenSecond();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel E", logDrucken, 10);

        //		buchen_leseGlobalePraesenzVariablenHV(); //Verschoben zu buchen() wg. Dead-Lock-Vermeidung

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel F", logDrucken, 10);

        erklaerendePerson = vollmachtPersonenNatJurIdent;
        rc = buchen_willenserklaerungErzeugen();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel G", logDrucken, 10);

        /*Meldungssatz aktualisieren*/
        rc = buchen_updateHEclMeldungAktionaer();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel H", logDrucken, 10);

        rc = buchen_updateHEclMeldungGast();
        if (rc < 0) {
            rWEPraesenzBuchenRC.rc = rc;
            return rWEPraesenzBuchenRC;
        }

        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen_Vertreterwechsel I", logDrucken, 10);

        /*Keine Präsenzsummenkorrektur!*/

        rWEPraesenzBuchenRC.rc = 1;
        return rWEPraesenzBuchenRC;
    }

    private void storniereAusSammelkarten(EclMeldung pEclMeldung) {
        int sammelIdent = pEclMeldung.meldungEnthaltenInSammelkarte;
        int sammelArt = pEclMeldung.meldungEnthaltenInSammelkarteArt;

        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.piEclMeldungAktionaer = pEclMeldung;
        lWillenserklaerung.piMeldungsIdentAktionaer = pEclMeldung.meldungsIdent;
        lWillenserklaerung.pAufnehmendeSammelkarteIdent = sammelIdent;
        switch (sammelArt) {
        case KonstSkIst.kiav:
            lWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV(lDbBundle);
            break;
        case KonstSkIst.srv:
            lWillenserklaerung.widerrufVollmachtUndWeisungAnSRV(lDbBundle);
            break;
        case KonstSkIst.organisatorisch:
            lWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte(lDbBundle);
            break;
        case KonstSkIst.briefwahl:
            lWillenserklaerung.widerrufBriefwahl(lDbBundle);
            break;
        case KonstSkIst.dauervollmacht:
            lWillenserklaerung.widerrufDauervollmachtAnKIAV(lDbBundle);
            break;
        default:
            CaBug.drucke("001");
        }

    }

    private int iIdentifikation = 0;

    public WEPraesenzBuchenRC buchen(WEPraesenzBuchen pWEPraesenzBuchen) {

        int rc;

        gWEPraesenzBuchen = pWEPraesenzBuchen;

        buchen_leseGlobalePraesenzVariablenHV(); //Hier eingefügt wg. Dead-Lock-Vermeidung
        /*Muß auch hier ganz am Anfang sein, weil sonst weitere Parallelitäten zwischen Einlesen und Speichern auftreten können,
         *konkret z.B. Meldung 1 wird eingelesen, meldung 2 wird eingelesen, undn dann meldung 1 und meldung 2
         *upgedatet, aber verweisen beide auf selbe personNatHJur (d.h. 2 Eintrittskarten werden
         *vom selben Aktionär gleichzeitig gebucht! 
         */

        buchenvorbereiten_initialisieren();

        for (iIdentifikation = 0; iIdentifikation < pWEPraesenzBuchen.zutrittsIdentAktionaer.size(); iIdentifikation++) {
            CaBug.druckeLog("BlPraesenzAkkreditierung.buchen iIdentifikation=" + iIdentifikation, logDrucken, 10);

            funktion = gWEPraesenzBuchen.funktion.get(iIdentifikation);
            vollmachtPersonenNatJurIdent = gWEPraesenzBuchen.vollmachtPersonenNatJurIdent.get(iIdentifikation);

            buchenvorbereitenIIdentifikation_initialisieren(iIdentifikation);

            /*+++++++++++++++++Übergebene Nummern aufbereiten und auf formale Gültigkeit prüfen++++++++++++++*/
            /*ZutrittsIdentAktionär*/
            rc = buchenvorbereitenIIdentifikation_aufbereitenZutrittsIdentAktionaer(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }
            /*ZutrittsIdentGast*/
            rc = buchenvorbereitenIIdentifikation_aufbereitenZutrittsIdentGast(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }
            /*Stimmkarten*/
            rc = buchenvorbereitenIIdentifikation_aufbereitenStimmkarten(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }
            /*StimmkarteSecond*/
            rc = buchenvorbereitenIIdentifikation_aufbereitenStimmkartenSecond(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }

            /*An dieser Stelle sind an Verarbeitungsvariablen gefüllt:
             * identifikationZutrittsIdentAktionaer
             * identifikationZutrittsIdentGast
             * identifikationStimmkarten
             * identifikationStimmkarteSecond
             * enthalten die übergebenen Idents, überprüft und aufbereitet für die interne Verwendung.
             */
            CaBug.druckeLog("identifikationZutrittsIdentAktionaer=" + identifikationZutrittsIdentAktionaer, logDrucken, 10);
            CaBug.druckeLog("identifikationZutrittsIdentGast=" + identifikationZutrittsIdentGast, logDrucken, 10);
            for (int i = 0; i < 5; i++) {
                CaBug.druckeLog("identifikationStimmkarten[" + i + "]=" + identifikationStimmkarten[i], logDrucken, 10);
            }
            CaBug.druckeLog("identifikationStimmkarteSecond=" + identifikationStimmkarteSecond, logDrucken, 10);

            /*+++++++++++++++++Wenn ZutrittsIdent Gast oder Aktionär übergeben, DANN MÜSSEN diese zur übergebenen Meldung passen!++++++++++++++*/
            /*ZutrittsIdentAktionär*/
            rc = buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentAktionaerZuMeldung(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }
            /*ZutrittsIdentGast*/
            rc = buchenvorbereiten_pruefenPasstUebergebeneZutrittsidentGastZuMeldung(iIdentifikation);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }

            /*Hier: falls ZutrittsIdentAktionaerOderGast übergeben, dann ist Meldungsident gefüllt*/

            /*+++++++++Falls eclMeldung gefüllt, dann daraus meldungsident füllen++++++++++++++++*/
            if (pWEPraesenzBuchen.eclMeldung.get(iIdentifikation) != null) {
                meldungsIdent = pWEPraesenzBuchen.eclMeldung.get(iIdentifikation).meldungsIdent;
            }
            if (meldungsIdent == 0) {
                rWEPraesenzBuchenRC.rc = CaFehler.pmMeldungsIdentNichtVorhanden;
                return rWEPraesenzBuchenRC;
            }

            /*Hier: meldungsIdent ist in jedem Fall gefüllt, ansonnsten Fehlermeldung zurückgegeben*/
            CaBug.druckeLog("meldungsIdent=" + meldungsIdent, logDrucken, 10);

            /*++++++++++++eclMeldung einlesen++++++++++++*/
            rc = buchenvorbereiten_leseEclMeldung(meldungsIdent);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }
            pWEPraesenzBuchen.eclMeldung.set(iIdentifikation, hEclMeldung);
            if (hEclMeldung.delayedVorhanden == 1) {
                vorgangWirdDelayed = 1;
            }

            /*Hier: vorgangWirdDelayed ist gesetzt, falls EclMeldung bereits Delayed ist*/
            CaBug.druckeLog("vorgangWirdDelayed (lt EclMeldung)=" + vorgangWirdDelayed, logDrucken, 10);

            /*++++++++++++++++ggf. aus Sammelkarte stornieren+++++++*/
            List<Integer> aktionenVorBuchenDurchfuehren = pWEPraesenzBuchen.aktionen;
            if (aktionenVorBuchenDurchfuehren != null) {
                CaBug.druckeLog("BlPraesenzAkkreditierung.buchen pWEPraesenzBuchen.aktionen ist ungleich null", logDrucken, 10);
                if (pWEPraesenzBuchen.aktionen.size() > iIdentifikation) {
                    CaBug.druckeLog("BlPraesenzAkkreditierung.buchen pWEPraesenzBuchen.aktionen[iIdentifkation]=" + aktionenVorBuchenDurchfuehren.get(iIdentifikation), logDrucken, 10);
                    if (aktionenVorBuchenDurchfuehren.get(iIdentifikation) == 1) {
                        CaBug.druckeLog("BlPraesenzAkkreditierung.buchen Sammelkarten stornieren", logDrucken, 10);
                        /*TODO - Achtung - diese Funktion war innerhalb von "logausgabe==true"!!!*/
                        storniereAusSammelkarten(hEclMeldung);
                    }

                }
            }

            /*TODO $Sperren Sammelkarten enthalten? Eigentlich müßte hier jetzt noch überprüft werden, ob in Sammelkarte enthalten oder etc..
             * dies wurde aktuell zurückgestellt - muß im Rahmen der Transaktionssicherheit des Präsenzprozesses nochmal betrachtet werden.*/

            /*++++++++++Ident ermitteln, über die Erst-Identifizierung erfolgt++++++++++*/
            buchenvorbereiten_ermittleIdentifizierungErfolgUeber(iIdentifikation);
            /*Hier gefüllt: identifizierungErfolgtUeber*/
            CaBug.druckeLog("identifizierungErfolgtUeber=" + identifizierungErfolgtUeber, logDrucken, 10);

            /*++++++++++idents einlesen, die der "Identifizierungs-Ident" (laut identifizierungErfolgUber) bereits zugeordnete sind+++++++
             * Die Suche erfolgt dabei über die Verknüpfung durch die ZutrittsIdent.*/
            if (identifizierungErfolgtUeber == 1) { /*ZutrittsIdentAktionär ist "Hauptmerkmal"*/
                buchenvorbereiten_leseBereitsZugeordneteStimmkartenZuZutrittsIdent(identifikationZutrittsIdentAktionaer, true);
            }

            if (identifizierungErfolgtUeber == 2) { /*ZutrittsIdentGast ist "Hauptmerkmal"*/
                /*Da ist vollkommen egal, ob sonst noch was zugeordnet ist - bzw. einer Gastnummer kann aktuell noch nix zugeordnet sein*/
            }

            if (identifizierungErfolgtUeber >= 11 && identifizierungErfolgtUeber <= 15) { /*Stimmkarte ist "Hauptmerkmal"*/
                rc = buchenvorbereiten_leseBereitsZugeordneteZutrittsUndStimmkartenZuStimmkartenIdent(identifikationStimmkarten[identifizierungErfolgtUeber - 11], true);
                if (rc < 1) {
                    rWEPraesenzBuchenRC.rc = rc;
                    return rWEPraesenzBuchenRC;
                }
            }

            if (identifizierungErfolgtUeber == 21) { /*StimmkartenSecond ist "Hauptmerkmal"*/
                rc = buchenvorbereiten_leseBereitsZugeordneteZutrittsUndStimmkartenZuStimmkartenSecondIdent(identifikationStimmkarteSecond, true);
                if (rc < 1) {
                    rWEPraesenzBuchenRC.rc = rc;
                    return rWEPraesenzBuchenRC;
                }
            }
            /*Hier:
             * Alle bereits in der Datenbank zugeordneten Idents sind gefüllt:
             * bereitsZugeordneteZutrittsIdentAktionaer
             * bereitsZugeordneteStimmkarten[] und bereitsZugeordneteStimmkartenSecond
             */
            CaBug.druckeLog("bereitsZugeordneteZutrittsIdentAktionaer=" + bereitsZugeordneteZutrittsIdentAktionaer, logDrucken, 10);
            CaBug.druckeLog("bereitsZugeordneteZutrittsIdentGast=" + bereitsZugeordneteZutrittsIdentGast, logDrucken, 10);
            for (int i = 0; i < 5; i++) {
                CaBug.druckeLog("bereitsZugeordneteStimmkarten[" + i + "]=" + bereitsZugeordneteStimmkarten[i], logDrucken, 10);
            }
            CaBug.druckeLog("bereitsZugeordneteStimmkartenSecond=" + bereitsZugeordneteStimmkartenSecond, logDrucken, 10);

            /*++++++Überprüfen, ob bereitsZugeordnete* den neu-zuzordnenden in identifikation* widersprechen+++++*/
            rc = buchenvorbereiten_pruefenObBereitsZugeordneteDenNeuenWidersprechen(meldungsIdent);
            if (rc < 0) {
                rWEPraesenzBuchenRC.rc = rc;
                return rWEPraesenzBuchenRC;
            }

            /*+++++Für bereits vorhandene Identifikationen die ecls aus Datenbank einlesen und in vorhandeneEcls ablegen+++++*/
            buchenvorbereiten_fuerVorhandeneIdentifikationenECLsAusDatenbankFuellen();
            /*Hier:
             * ggf. neu gefüllt:
             * bereitsZugeordneteZutrittsIdentAktionaer
             * bereitsZugeordneteZutrittsIdentGast
             * bereitsZugeordneteStimmkarten
             * bereitsZugeordneteStimmkartenSecond
             * 
             * Neu gefüllt (falls bereits in Datenbank vorhanden):
             * vorhandenEclZutrittskarteAktionaer
             * vorhandenEclZutrittskarteGast
             * vorhandeneEclStimmkarten
             * vorhandeneStimmkarteSecond
             */
            CaBug.druckeLog("bereitsZugeordneteZutrittsIdentAktionaer(2)=" + bereitsZugeordneteZutrittsIdentAktionaer, logDrucken, 10);
            CaBug.druckeLog("bereitsZugeordneteZutrittsIdentGast(2)=" + bereitsZugeordneteZutrittsIdentGast, logDrucken, 10);
            for (int i = 0; i < 5; i++) {
                CaBug.druckeLog("bereitsZugeordneteStimmkarten[" + i + "](2)=" + bereitsZugeordneteStimmkarten[i], logDrucken, 10);
            }
            CaBug.druckeLog("bereitsZugeordneteStimmkartenSecond(2)=" + bereitsZugeordneteStimmkartenSecond, logDrucken, 10);

            /*+++Variablen zuordnungErforderlich füllen (werden nicht nur zu Überprüfung, sondern später+++++
             * auch für die Eintragung benötigt)
             */
            buchenvorbereiten_zuordnungErforderlichFuellen();

            CaBug.druckeLog("Funktion=" + funktion, logDrucken, 10);

            /*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
             * Bei Erstzugang/Wiederzugang: Prüfen, ob alle erforderlichen idents (die zugeordnet werden müssen). auch
             * in identifikation* übergeben wurden oder aber bereits vorhanden sind. Sie dürfen auch nicht bereits
             * anderweitig verwendet sein (jedoch natürlich für die untersuchte Verwendung bereits).	?*/
            if (funktion == KonstWillenserklaerung.erstzugang || funktion == KonstWillenserklaerung.wiederzugang) {
                rc = buchenvorbereiten_pruefenObAlleErforderlichenIdentsVorhanden();
                if (rc < 1) {
                    rWEPraesenzBuchenRC.rc = rc;
                    return rWEPraesenzBuchenRC;
                }
            }

            /***ZutrittsIdent: Darf - falls bereits verwendet - nicht anderer Person zugeordnet werden!**/
            /*TODO: wurde komplett deaktiviert, da a) aktuell nicht erforderlich, und b) nicht funktionsfähig.
             * Da kam bei Sammelkartenzugängen immer -1105.
             */
            //            if (funktion == KonstWillenserklaerung.erstzugang || funktion == KonstWillenserklaerung.wiederzugang) {
            //                if (vorhandenEclZutrittskarteAktionaer != null) {
            //                    if (vorhandenEclZutrittskarteAktionaer.personenNatJurIdent != 0) {
            //                        //						/*ZutrittsIdent bereits einem Teilnehmer zugeordnet*/
            //                        if (vollmachtPersonenNatJurIdent != -2) {
            //                            if (vollmachtPersonenNatJurIdent != vorhandenEclZutrittskarteAktionaer.personenNatJurIdent) {
            //                                rWEPraesenzBuchenRC.rc = CaFehler.pmZutrittsIdentBereitsVonAndererPersonVerwendet;
            //                                return rWEPraesenzBuchenRC;
            //                            }
            //                        }
            //                    }
            //
            //                }
            //            }

            /*++++++++++++Buchungen durchführen+++++++++++++*/

            if (funktion == KonstWillenserklaerung.erstzugang) {/*Erstzugang*/
                CaBug.druckeLog("Erstzugang", logDrucken, 3);
                buchen_erstzugang_wiederzugang(iIdentifikation);
            }
            if (funktion == KonstWillenserklaerung.abgang) {/*Abgang*/
                CaBug.druckeLog("Abgang", logDrucken, 3);
                buchen_abgang(iIdentifikation);
            }
            if (funktion == KonstWillenserklaerung.wiederzugang) {/*Wiederzugang*/
                /*Verarbeitungshinweis: wenn als Bevollmächtigter !=-2 übergeben wird, dann mit
                 * Vollmachtswechsel!
                 */
                CaBug.druckeLog("Wiederzugang", logDrucken, 3);
                buchen_erstzugang_wiederzugang(iIdentifikation);
            }

            if (funktion == KonstWillenserklaerung.vertreterwechsel) {/*Vertreterwechsel*/
                CaBug.druckeLog("Vertreterwechsel", logDrucken, 3);
                buchen_vertreterwechsel(iIdentifikation);
                /*TODO $_ prüfen: bei VollmachtDritte muß auch personNatJur in ZutrittsIdent mit übertragen werden!*/
            }
        }

        return rWEPraesenzBuchenRC;
    }

}
