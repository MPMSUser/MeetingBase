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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlag;
import de.meetingapps.meetingportal.meetComEntities.EclKIAVFuerVollmachtDritte;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclVertreter;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplitRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.StubStatus;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSammelkarten;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSammelkartenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Funktionen zum Verwalten von Sammelkarten - "über" den Db-Funktionen, speziell für Sammelkarten.
 * Als Stub verwendbar*/
public class BlSammelkarten extends StubRoot {

    private int logDrucken=10;
    
    public EclMeldung meldung = null;
    public EclWeisungMeldung weisungMeldung = null;
    public EclWeisungMeldungSplit weisungMeldungSplit = null;

    public EclWeisungMeldungRaw weisungMeldungRaw = null;
    public EclWeisungMeldungSplitRaw weisungMeldungSplitRaw = null;

    public EclAbstimmungsVorschlag abstimmungsvorschlag = null;

    /**Scheiß Übergangsphase:
     * Wenn nur Stub-Funktionen verwendet werden: 
     * > geöffnet bei Server-Funktionalität, sonst nicht geöffnet aber initialisiert wg. Parameter
     * 
     * Sonst:
     * > muß immer geöffnet sein*/
    @Deprecated
    public BlSammelkarten(DbBundle pDbBundle) {
        super(true, pDbBundle);
    }

    public BlSammelkarten(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**Initialisiert die verschiedenen Komponenten für eine neue, "leere" Sammelkarte*/
    public int neueSammelkarte() {
        meldung = new EclMeldung();

        weisungMeldung = new EclWeisungMeldung();
        weisungMeldungSplit = new EclWeisungMeldungSplit();

        weisungMeldung.weisungMeldungSplit = weisungMeldungSplit;

        weisungMeldungRaw = new EclWeisungMeldungRaw();
        weisungMeldungSplitRaw = new EclWeisungMeldungSplitRaw();

        weisungMeldungRaw.weisungMeldungSplitRaw = weisungMeldungSplitRaw;

        abstimmungsvorschlag = new EclAbstimmungsVorschlag();
        return 1;
    }

    /*2*/
    /**Speichern der verschiedenen Komponenten als neue Sammelkarte.
     * Die verschiedenen Komponenten müssen vorher mit neueSammelkarte initialisiert worden sein, und anschließend die "spezifischen Felder"
     * belegt werden, insbesondere:
     * in meldung: name, ort, skIst, skWeiungsartZulaessig, skBuchbarInternet, skBuchbarPapier, skBuchbarHV.
     * Sowie, falls ein Abstimmungsvorschlag mit gespeichert werden soll, die Felder in abstimmungsVorschlag.
     * 
     * weisungMeldung, weisungMeldungSplit, weisungMeldungRaw, weisungMeldungSplitRaw werden
     * verwendet.
     * 
     * Datenzugriff.*/
    public int neueSammelkarteSpeichern() {

        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 2;
            weStubBlSammelkarten.meldung = meldung;
            weStubBlSammelkarten.weisungMeldung = weisungMeldung;
            weStubBlSammelkarten.weisungMeldungSplit = weisungMeldungSplit;
            weStubBlSammelkarten.weisungMeldungRaw = weisungMeldungRaw;
            weStubBlSammelkarten.weisungMeldungSplitRaw = weisungMeldungSplitRaw;
            weStubBlSammelkarten.abstimmungsvorschlag = abstimmungsvorschlag;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        //		meldung.meldungAktiv=1;
        meldung.meldungstyp = 2;
        meldung.klasse = 1;

        meldung.kurzName = meldung.name;
        meldung.kurzOrt = meldung.ort;

        meldung.besitzart = "V";
        //		meldung.skOffenlegung=0;

        lDbBundle.dbMeldungen.insert(meldung);
        if (meldung.skWeisungsartZulaessig != 0) {
            /*Weisungssatz anlegen*/
            weisungMeldung.meldungsIdent = meldung.meldungsIdent;
            weisungMeldung.weisungSplit = 1;
            weisungMeldung.aktiv = 1;
            weisungMeldung.weisungMeldungSplit = weisungMeldungSplit; /*Sicherheitshalber hier nochmal, falls vom User verändert!*/
            weisungMeldungRaw.weisungMeldungSplitRaw = weisungMeldungSplitRaw; /*Sicherheitshalber hier nochmal, falls vom User verändert!*/

            lDbBundle.dbWeisungMeldung.insert(weisungMeldung, weisungMeldungRaw);

            /*Abstimmungsvorschlag anlegen*/
            abstimmungsvorschlag.sammelIdent = meldung.meldungsIdent;
            lDbBundle.dbAbstimmungsVorschlag.insert(abstimmungsvorschlag);
        }

        dbClose();
        return 1;
    }

    /*3*/
    /**Liefert alle Sammelkarten mit Vertreter, die für Vollmacht an Dritte Intern/Auf HV zur Verfügung stehen.
     * Verwendung für rationelle Erfassung Vollmacht an Dritte im Front-Office, oder auch im Anmeldeablauf.
     * 
     * Datenzugriff
     */
    public LinkedList<EclKIAVFuerVollmachtDritte> kiavFuerVollmachtDritteHV() {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 3;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return null;
            }

            return weStubBlSammelkartenRC.eclKIAVFuerVollmachtDritte;
        }

        dbOpen();
        LinkedList<EclKIAVFuerVollmachtDritte> listKIAVFuerVollmachtDritte = new LinkedList<EclKIAVFuerVollmachtDritte>();

        lDbBundle.dbMeldungen.leseSammelkarteKIAVVollmachtDritteHV(-1);
        //		System.out.println("dbBundle.dbMeldungen.meldungenArray.length"+dbBundle.dbMeldungen.meldungenArray.length);
        for (int i = 0; i < lDbBundle.dbMeldungen.meldungenArray.length; i++) {
            //			System.out.println("dbBundle.dbMeldungen.meldungenArray.length"+dbBundle.dbMeldungen.meldungenArray.length);
            EclMeldung sammelkarte = lDbBundle.dbMeldungen.meldungenArray[i];
            if (sammelkarte.vertreterIdent != 0) {
                EclKIAVFuerVollmachtDritte eclKIAVFuerVollmachtDritte = new EclKIAVFuerVollmachtDritte();
                eclKIAVFuerVollmachtDritte.kiavName = sammelkarte.name;
                eclKIAVFuerVollmachtDritte.kiavOrt = sammelkarte.ort;
                eclKIAVFuerVollmachtDritte.vertreterIdent = sammelkarte.vertreterIdent;
                eclKIAVFuerVollmachtDritte.vertreterName = sammelkarte.vertreterName;
                eclKIAVFuerVollmachtDritte.vertreterVorname = sammelkarte.vertreterVorname;
                eclKIAVFuerVollmachtDritte.vertreterOrt = sammelkarte.vertreterOrt;
                listKIAVFuerVollmachtDritte.add(eclKIAVFuerVollmachtDritte);
            }
        }
        dbClose();

        return listKIAVFuerVollmachtDritte;

    }

    public EclMeldung[] rcSammelMeldung = null;
    public EclZutrittskarten[][] rcZutrittskartenArray = null;
    public EclStimmkarten[][] rcStimmkartenArray = null;
    public EclWillensErklVollmachtenAnDritte[][] rcWillensErklVollmachtenAnDritteArray = null;

    /*4*/
    /**Holt die Sammelkarten-Grund-Daten (EclMeldung) aus Datenbank.
     * 
     * pSammelIdent=0 => alle, sonst übergebene Ident.
     * 
     * Rückgabe = Anzahl der gelesenen Sammelkarten (0 = keine gelesen!)
     * 
     * Ablage erfolgt in:
     * rcSammelMeldung
     * 
     * Datenzugriff.
     */
    public int holeSammelkartenMeldeDaten(boolean pNurAktive, int pSammelIdent) {
        if (verwendeWebService()) {
            CaBug.druckeLog("Anfang Verbindung über Web", logDrucken, 10);
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 4;
            weStubBlSammelkarten.pNurAktive = pNurAktive;
            weStubBlSammelkarten.pSammelIdent = pSammelIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }
            rcSammelMeldung = weStubBlSammelkartenRC.rcSammelMeldung;

            CaBug.druckeLog("Ende Verbindung über Web", logDrucken, 10);
            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();

        if (pSammelIdent == 0) {
            if (pNurAktive) {
                lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
            } else {
                lDbBundle.dbMeldungen.leseWirklichAlleSammelkarten(-1);
            }
        } else {
            lDbBundle.dbMeldungen.leseZuIdent(pSammelIdent);
        }

        int anzSammelkarten = lDbBundle.dbMeldungen.anzErgebnis();
        if (anzSammelkarten == 0) {
            rcSammelMeldung = null;
            dbClose();
            return 0;
        }
        rcSammelMeldung = lDbBundle.dbMeldungen.meldungenArray;

        dbClose();
        return anzSammelkarten;
    }

    /*5*/
    /**Holt die Sammelkarten"Grund-"daten (EclMeldung) - einschließlich zugeordneten EKs, SKs, und Vollmachten
     * (sowohl stornierter als auch gesperrter) aus Datenbank.
     *  
     * pSammelIdent=0 => alle, sonst übergebene Ident.
     * 
     * Rückgabe = Anzahl der gelesenen Sammelkarten (0 = keine gelesen!)
     * 
     * Ablage erfolgt in:
     * rcSammelMeldung
     * rcZutrittskartenArray
     * rcStimmkartenArray
     * rcWillensErklVollmachtenAnDritteArray
     */
    public int holeSammelkartenDaten(boolean pNurAktive, int pSammelIdent) {
        if (verwendeWebService()) {
            CaBug.druckeLog("Anfang Verbindung über Web", logDrucken, 10);
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 5;
            weStubBlSammelkarten.pNurAktive = pNurAktive;
            weStubBlSammelkarten.pSammelIdent = pSammelIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }
            rcSammelMeldung = weStubBlSammelkartenRC.rcSammelMeldung;
            rcZutrittskartenArray = weStubBlSammelkartenRC.rcZutrittskartenArray;
            rcStimmkartenArray = weStubBlSammelkartenRC.rcStimmkartenArray;
            rcWillensErklVollmachtenAnDritteArray = weStubBlSammelkartenRC.rcWillensErklVollmachtenAnDritteArray;

            CaBug.druckeLog("Ende Verbindung über Web", logDrucken, 10);
           return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        /*Sammelkarten selbst einlesen*/
        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        int anzSammelkarten = holeSammelkartenMeldeDaten(pNurAktive, pSammelIdent);

        istServer = stubStatus.popIstServer();

        if (anzSammelkarten == 0) {
            return 0;
        }

        rcZutrittskartenArray = new EclZutrittskarten[anzSammelkarten][];
        rcStimmkartenArray = new EclStimmkarten[anzSammelkarten][];
        rcWillensErklVollmachtenAnDritteArray = new EclWillensErklVollmachtenAnDritte[anzSammelkarten][];

        /*Zutrittskarten, Stimmkarten, Vollmachten ergänzen zu allen Sammelkarten*/
        for (int i = 0; i < anzSammelkarten; i++) {
            int meldeIdent = rcSammelMeldung[i].meldungsIdent;

            /*Zutrittskarten einlesen*/
            lDbBundle.dbZutrittskarten.readZuMeldungsIdent(meldeIdent);
            rcZutrittskartenArray[i] = lDbBundle.dbZutrittskarten.ergebnis();

            /*Stimmkarten einlesen*/
            lDbBundle.dbStimmkarten.readZuMeldungsIdent(meldeIdent);
            rcStimmkartenArray[i] = lDbBundle.dbStimmkarten.ergebnis();

            /*Bevollmächtigte hinzulesen (auch stornierte!)*/
            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.setzeDbBundle(lDbBundle);
            lWillenserklaerung.piMeldungsIdentAktionaer = meldeIdent;
            lWillenserklaerung.einlesenVollmachtenAnDritte();

            rcWillensErklVollmachtenAnDritteArray[i] = lWillenserklaerung.rcVollmachtenAnDritte;
        }
        dbClose();

        return anzSammelkarten;
    }

    public EclWeisungMeldung rcWeisungenSammelkopf = null;
    public EclWeisungMeldung rcAktionaersSummen = null;

    /*6*/
    /**
     * Liest für die Sammelkarte ein:
     * > Kopfweisungen der Sammelkarte (rcWeisungenSammelkopf)
     * > Summen der Weisungen aus den aktiven Aktionären (rcAktionaersSummen)
     * > zugeordnete AKtionäre in
     *  	aktionaereAktiv, aktionaereInaktiv, aktionaereWiderrufen, aktionaereGeaendert
     */
    public int leseKopfWeisungUndAktionaereZuSammelkarte(EclMeldung pSammelMeldung) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 6;
            weStubBlSammelkarten.meldung = pSammelMeldung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }
            rcWeisungenSammelkopf = weStubBlSammelkartenRC.rcWeisungenSammelkopf;
            rcAktionaersSummen = weStubBlSammelkartenRC.rcAktionaersSummen;
            aktionaereAktiv = weStubBlSammelkartenRC.aktionaereAktiv;
            aktionaereInaktiv = weStubBlSammelkartenRC.aktionaereInaktiv;
            aktionaereWiderrufen = weStubBlSammelkartenRC.aktionaereWiderrufen;
            aktionaereGeaendert = weStubBlSammelkartenRC.aktionaereGeaendert;

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        /**Holen Weisungen wie in Sammelkopf abgelegt*/
        lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(pSammelMeldung.meldungsIdent, false);
        rcWeisungenSammelkopf = lDbBundle.dbWeisungMeldung
                .weisungMeldungGefunden(0); /*Für Sammelkarte nur ein Satz möglich*/

        /**Aktionäre einlesen*/
        rcAktionaersSummen = new EclWeisungMeldung();
        rcAktionaersSummen.weisungMeldungSplit = new EclWeisungMeldungSplit();

        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        leseAktionaereZuSammelkarte(pSammelMeldung, 1);
        leseAktionaereZuSammelkarte(pSammelMeldung, 0);
        leseAktionaereZuSammelkarte(pSammelMeldung, -1);
        leseAktionaereZuSammelkarte(pSammelMeldung, -2);
        istServer = stubStatus.popIstServer();
        dbClose();

        return 1;
    }

    /**Liefert zur pZutrittsIdent die Sammelkarte mit dieser Eintrittskartennummer.
     * Voraussetzung: rcSammelMeldung wurde mit holeSammelkartenDaten oder holeSammelkartenMeldeDaten
     * gefüllt.
     */
    public EclMeldung holeSammelkarteAusRcSammelMeldungMitZutrittsIdent(int pZutrittsIdent) {
        if (rcSammelMeldung == null) {
            return null;
        }
        for (int i = 0; i < rcSammelMeldung.length; i++) {
            String lEk = rcSammelMeldung[i].zutrittsIdent;
            if (!lEk.isEmpty()) {
                if (Integer.parseInt(lEk) == pZutrittsIdent) {
                    return rcSammelMeldung[i];
                }
            }
        }
        return null;
    }

    public List<EclMeldungMitAktionaersWeisung> aktionaereAktiv = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereInaktiv = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereWiderrufen = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereGeaendert = null;

    /**
     * pAktiv=
     * 	1 = alle aktiven
     * 	0 = alle inaktiven
     *  -1 = Widerrufen
     *  -2 = geändert
     *  
     *  Füllt (je nach pAktiv):
     *  aktionaereAktiv, aktionaereInaktiv, aktionaereWiderrufen, aktionaereGeaendert
     *  Bei pAktiv=1 werden zusätzlich hochgezählt: rcAktionaersSummen
     */
    private void leseAktionaereZuSammelkarte(EclMeldung pSammelmeldung, int pAktiv) {

        switch (pAktiv) {
        case 1: {
            aktionaereAktiv = new LinkedList<EclMeldungMitAktionaersWeisung>();
            break;
        }
        case 0: {
            aktionaereInaktiv = new LinkedList<EclMeldungMitAktionaersWeisung>();
            break;
        }
        case -1: {
            aktionaereWiderrufen = new LinkedList<EclMeldungMitAktionaersWeisung>();
            break;
        }
        case -2: {
            aktionaereGeaendert = new LinkedList<EclMeldungMitAktionaersWeisung>();
            break;
        }
        }

        lDbBundle.dbWeisungMeldung.leseAktionaersWeisungZuSammelkarte(pSammelmeldung.meldungsIdent, pAktiv);
        for (int i1 = 0; i1 < lDbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteAnzGefunden(); i1++) {
            EclMeldungMitAktionaersWeisung lAktionaerZuSammelkarte = new EclMeldungMitAktionaersWeisung();

            lAktionaerZuSammelkarte.meldungsIdent = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).weisungMeldung.meldungsIdent;
            lAktionaerZuSammelkarte.aktionaersnummer = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).aktionaersnummer;
            lAktionaerZuSammelkarte.gattung = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).gattung;
            lAktionaerZuSammelkarte.stueckAktien = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).stueckAktien;
            lAktionaerZuSammelkarte.stimmen = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).stimmen;
            lAktionaerZuSammelkarte.besitzart = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).besitzart;
            lAktionaerZuSammelkarte.stimmausschluss = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).stimmausschluss;
            lAktionaerZuSammelkarte.name = lDbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteGefunden(i1).name;
            lAktionaerZuSammelkarte.vorname = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).vorname;
            lAktionaerZuSammelkarte.ort = lDbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteGefunden(i1).ort;
            lAktionaerZuSammelkarte.zutrittsIdent = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).zutrittsIdent;
            lAktionaerZuSammelkarte.eclWeisungMeldung = lDbBundle.dbWeisungMeldung
                    .aktionaersWeisungenZuSammelkarteGefunden(i1).weisungMeldung;

            for (int i = 0; i < 200; i++) {
                int abstimmung = lDbBundle.dbWeisungMeldung
                        .aktionaersWeisungenZuSammelkarteGefunden(i1).weisungMeldung.abgabe[i];
                lAktionaerZuSammelkarte.abgabe[i] = abstimmung;
                if (pAktiv == 1) {/*In Summe dieses Agendapunktes aufnahmen*/
                    rcAktionaersSummen.weisungMeldungSplit.abgabe[i][abstimmung] += lAktionaerZuSammelkarte.stimmen;
                }
            }

            switch (pAktiv) {
            case 1: {
                aktionaereAktiv.add(lAktionaerZuSammelkarte);
                break;
            }
            case 0: {
                aktionaereInaktiv.add(lAktionaerZuSammelkarte);
                break;
            }
            case -1: {
                aktionaereWiderrufen.add(lAktionaerZuSammelkarte);
                break;
            }
            case -2: {
                aktionaereGeaendert.add(lAktionaerZuSammelkarte);
                break;
            }
            }
        }

    }

    /*7*/
    public List<EclVertreter> rcVertreterListe = null;

    /**Liest alle Vertreter, die Sammelkarten zugeordnet sind, in
     * rcVertreterListe ein. Ergebnis ist aufsteigend nach Vertretername.
     * Dupletten sind eliminiert. Vertritt ein Vertreter mehrere unterschiedlichen
     * Sammelkarten, so sind die Sammelkartennamen durch ";" getrennt im entsprechenden Feld
     */
    public int leseVertreterAllerSammelkarten() {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 7;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }
            rcVertreterListe = weStubBlSammelkartenRC.rcVertreterListe;

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        rcVertreterListe = new LinkedList<EclVertreter>();

        lDbBundle.dbPersonenNatJur.leseAlleSammelkartenVertreter();
        int anzahlVertreter = lDbBundle.dbPersonenNatJur.personenNatJurArray.length;
        if (anzahlVertreter > 0) {
            for (int i = 0; i < anzahlVertreter; i++) {
                EclPersonenNatJur vertreter = lDbBundle.dbPersonenNatJur.personenNatJurArray[i];
                String kiavBezeichnung = lDbBundle.dbPersonenNatJur.kiavArray[i];
                /*Prüfen - ist VertreterIdent schon in Liste enthalten?*/
                int offset = -1;
                for (int i1 = 0; i1 < rcVertreterListe.size(); i1++) {
                    if (rcVertreterListe.get(i1).vertreter.ident == vertreter.ident) {
                        offset = i1;
                    }
                }
                if (offset == -1) {
                    /*Vertreter noch nicht vorhanden - anhängen*/
                    EclVertreter neuerVertreter = new EclVertreter();
                    neuerVertreter.vertreter = vertreter;
                    neuerVertreter.bemerkung = kiavBezeichnung;
                    rcVertreterListe.add(neuerVertreter);
                } else {
                    /*Vertreter vorhanden, nun prüfen ob Sammelkartenbezeichnung schon in 
                     * bemerkungen enthalten
                     */
                    if (!rcVertreterListe.get(offset).bemerkung.contains(kiavBezeichnung)) {
                        if (!rcVertreterListe.get(offset).bemerkung.isEmpty()) {
                            rcVertreterListe.get(offset).bemerkung += "; ";
                        }
                        rcVertreterListe.get(offset).bemerkung += kiavBezeichnung;
                    }
                }
            }
        }
        dbClose();
        return 1;
    }

    /*8*/
    /**Rückgabewert: 1 => ok, ansonsten Fehlercode aus BlWillenserklaerung.vollmachtAnDritte
     * DbBundle wird benötigt*/
    public int neuerVertreterFuerSammelkarte(int sammelIdent, String vertreterName, String vertreterVorname,
            String vertreterOrt) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 8;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.vertreterName = vertreterName;
            weStubBlSammelkarten.vertreterVorname = vertreterVorname;
            weStubBlSammelkarten.vertreterOrt = vertreterOrt;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.piMeldungsIdentAktionaer = sammelIdent;
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        EclPersonenNatJur neuerVertreter = new EclPersonenNatJur();
        neuerVertreter.name = vertreterName;
        neuerVertreter.vorname = vertreterVorname;
        neuerVertreter.ort = vertreterOrt;
        lWillenserklaerung.pEclPersonenNatJur = neuerVertreter;
        lWillenserklaerung.vollmachtAnDritte(lDbBundle);
        if (!lWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return lWillenserklaerung.rcGrundFuerUnzulaessig;
        }
        dbClose();
        return 1;
    }

    /*9*/
    /**Rückgabewert: 1 => ok, ansonsten Fehlercode aus BlWillenserklaerung.vollmachtAnDritte
     * oder afBevollmaechtigterBereitsBevollmaechtigt.
     * DbBundle wird benötigt*/
    public int neuerBestehenderVertreterFuerSammelkarte(int sammelIdent, int vertreterIdent) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 9;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.vertreterIdent = vertreterIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.setzeDbBundle(lDbBundle);
        lWillenserklaerung.piMeldungsIdentAktionaer = sammelIdent;
        lWillenserklaerung.einlesenVollmachtenAnDritte();
        lWillenserklaerung.rcIstZulaessig = true;
        lWillenserklaerung.pWillenserklaerungGeberIdent = vertreterIdent;
        lWillenserklaerung.pruefenObVollmachtgeberVollmachtHat();
        if (lWillenserklaerung.rcIstZulaessig) {/*Vollmacht ist schon vorhanden*/
            dbClose();
            return CaFehler.afBevollmaechtigterBereitsBevollmaechtigt;
        }

        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.piMeldungsIdentAktionaer = sammelIdent;
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        EclPersonenNatJur alterVertreter = new EclPersonenNatJur();
        alterVertreter.ident = vertreterIdent;
        lWillenserklaerung.pEclPersonenNatJur = alterVertreter;
        lWillenserklaerung.vollmachtAnDritte(lDbBundle);
        if (!lWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return lWillenserklaerung.rcGrundFuerUnzulaessig;
        }
        dbClose();
        return 1;
    }

    /*10*/
    /**Rückgabewert: 1 => ok, ansonsten Fehlercode aus BlWillenserklaerung.vollmachtAnDritte
     * DbBundle wird benötigt*/
    public int storniereBestehenderVertreterFuerSammelkarte(int sammelIdent, int vertreterIdent) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 10;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.vertreterIdent = vertreterIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.piMeldungsIdentAktionaer = sammelIdent;
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        EclPersonenNatJur neuerVertreter = new EclPersonenNatJur();
        neuerVertreter.ident = vertreterIdent;
        lWillenserklaerung.pEclPersonenNatJur = neuerVertreter;
        lWillenserklaerung.widerrufVollmachtAnDritte(lDbBundle);
        if (!lWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return lWillenserklaerung.rcGrundFuerUnzulaessig;
        }
        dbClose();
        return 1;
    }

    /*11*/
    /**
     * Aufbereitung der Nummernform erfolgt in dieser Funktion.
     * Besonderer Returnwert:
     * > CaFehler.pfXyNichtImZulaessigenNummernkreis
     */
    public int neueEKFuerSammelkarte(int sammelident, String ekNummer) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 11;
            weStubBlSammelkarten.pSammelIdent = sammelident;
            weStubBlSammelkarten.ekNummer = ekNummer;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        ekNummer = blNummernformen.formatiereEKNr(ekNummer);

        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.piMeldungsIdentAktionaer = sammelident;
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        lWillenserklaerung.pZutrittsIdent.zutrittsIdent = ekNummer;
        lWillenserklaerung.pZutrittsIdent.zutrittsIdentNeben = "00";
        lWillenserklaerung.neueZutrittsIdentZuMeldungV2(lDbBundle);
        if (!lWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return lWillenserklaerung.rcGrundFuerUnzulaessig;
        }

        dbClose();
        return 1;
    }

    /*12*/
    public int neueEKFuerAlleSammelkarten() {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 12;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.anzErgebnis();
        EclMeldung[] alleSammelkarten = lDbBundle.dbMeldungen.meldungenArray;

        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        for (int i = 0; i < anzSammelkarten; i++) {
            String neueNummer = sucheNeueEKFuerSammelkarte();
            if (neueNummer.isEmpty()) {
                return -1;
            }
            int rc = neueEKFuerSammelkarte(alleSammelkarten[i].meldungsIdent, neueNummer);
            if (rc < 0) {
                return rc;
            }
        }

        istServer = stubStatus.popIstServer();

        dbClose();

        return 1;
    }

    /*13*/
    /**Returnwert "" => keine verfügbar; sonst: gefundene EK aus Sammelkartennummernkreis*/
    public String sucheNeueEKFuerSammelkarte() {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 13;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return "";
            }

            return weStubBlSammelkartenRC.rcEintrittskarte;
        }

        dbOpen();
        int vonEkNrNummernkreis = lDbBundle.param.paramNummernkreise.vonSammelkartennummer;
        int bisEkNrNummernkreis = lDbBundle.param.paramNummernkreise.bisSammelkartennummer;
        if (vonEkNrNummernkreis == 0 || bisEkNrNummernkreis == 0) {
            dbClose();
            return "";
        } //Nummernkreis nicht definiert

        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);

        String vonEK = Integer.toString(vonEkNrNummernkreis);
        vonEK = blNummernformen.formatiereEKNr(vonEK);

        String bisEK = Integer.toString(bisEkNrNummernkreis);
        bisEK = blNummernformen.formatiereEKNr(bisEK);

        int rc = lDbBundle.dbZutrittskarten.readVonBis(vonEK, bisEK);
        if (rc == 0) {
            dbClose();
            return vonEK;
        }

        int gef = -1;
        int aktuellerWertAusNummernkreis = vonEkNrNummernkreis;
        int aktuellerOffsetInDatenbankErgebnis = 0;
        int anzahlInDatenbankErgebnis = lDbBundle.dbZutrittskarten.anzErgebnis();
        //		System.out.println("anzahlInDatenbankErgebnis"+anzahlInDatenbankErgebnis);

        while (gef == -1) {
            String sAktuellerWertAusNummernkreis = blNummernformen
                    .formatiereEKNr(Integer.toString(aktuellerWertAusNummernkreis));
            if (aktuellerOffsetInDatenbankErgebnis >= anzahlInDatenbankErgebnis //Dann alle durchgearbeitet, sozusagen hintendran hängen
                    || sAktuellerWertAusNummernkreis.compareTo(lDbBundle.dbZutrittskarten
                            .ergebnisPosition(aktuellerOffsetInDatenbankErgebnis).zutrittsIdent) < 0 //Dann ist dieser Wert noch nicht verwendet
            ) {
                dbClose();
                return blNummernformen.formatiereEKNr(Integer.toString(aktuellerWertAusNummernkreis));
            }

            /*Nun hochzählen*/
            while (aktuellerOffsetInDatenbankErgebnis < anzahlInDatenbankErgebnis
                    && sAktuellerWertAusNummernkreis.compareTo(lDbBundle.dbZutrittskarten
                            .ergebnisPosition(aktuellerOffsetInDatenbankErgebnis).zutrittsIdent) == 0) {
                /*Auch die nächste gefundenene vergebene Zutrittsnummer ist gleich der aktuellen Nummer (kann ja sein, durch mehrfache, gesperrte etc.)*/
                aktuellerOffsetInDatenbankErgebnis++;
            }
            aktuellerWertAusNummernkreis++;
        }
        dbClose();
        return "";
    }

    /*14*/
    /**Sperren der ZutrittsIdent zu Sammelkarte*/
    public int sperreEKderSammelkarte(int sammelident, String ekNummer) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 14;
            weStubBlSammelkarten.pSammelIdent = sammelident;
            weStubBlSammelkarten.ekNummer = ekNummer;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        ekNummer = blNummernformen.formatiereEKNr(ekNummer);
        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = 51;
        lWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        lWillenserklaerung.piKlasse = 1;
        lWillenserklaerung.piZutrittsIdent.zutrittsIdent = ekNummer;
        lWillenserklaerung.piZutrittsIdent.zutrittsIdentNeben = "00";
        lWillenserklaerung.sperrenZutrittsIdent(lDbBundle);
        if (!lWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return lWillenserklaerung.rcGrundFuerUnzulaessig;
        }

        dbClose();
        return 1;
    }

    /*15*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int widerrufeMeldungInSammelkarte(int sammelIdent, int skIst, int meldungIdent) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 15;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.skIst = skIst;
            weStubBlSammelkarten.meldungIdent = meldungIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();
        blWillenserklaerung.piMeldungsIdentAktionaer = meldungIdent;
        System.out.println("meldungsIdent=" + meldungIdent);
        blWillenserklaerung.pWillenserklaerungGeberIdent = 0;
        blWillenserklaerung.pAufnehmendeSammelkarteIdent = sammelIdent;
        switch (skIst) {
        case 1:
            blWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV(lDbBundle);
            break;
        case 2:
            blWillenserklaerung.widerrufVollmachtUndWeisungAnSRV(lDbBundle);
            break;
        case 3:
            blWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte(lDbBundle);
            break;
        case 4:
            blWillenserklaerung.widerrufBriefwahl(lDbBundle);
            break;
        case 5:
            blWillenserklaerung.widerrufDauervollmachtAnKIAV(lDbBundle);
            break;

        }

        if (blWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return 1;
        } else {
            System.out.println(
                    "blWillenserklaerung.rcGrundFuerUnzulaessig=" + blWillenserklaerung.rcGrundFuerUnzulaessig);
            dbClose();
            return blWillenserklaerung.rcGrundFuerUnzulaessig;
        }
    }

    /*16*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int widerrufeMeldungArrayInSammelkarte(int sammelIdent, int skIst, int[] meldungIdent) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 16;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.skIst = skIst;
            weStubBlSammelkarten.meldungIdentArray = meldungIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        boolean fehler = false;
        for (int i = 0; i < meldungIdent.length; i++) {

            StubStatus stubStatus = new StubStatus();
            stubStatus.pushIstServer(istServer);
            istServer = true;
            int rc = widerrufeMeldungInSammelkarte(sammelIdent, skIst, meldungIdent[i]);
            istServer = stubStatus.popIstServer();

            if (rc < 1) {
                dbClose();
                fehler = true;
            }
        }

        if (fehler == true) {
            dbClose();
            return -1;
        }
        dbClose();
        return 1;
    }

    /*17*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int aendereWeisungMeldungInSammelkarte(int sammelIdent, int skIst, int meldungIdent,
            EclWeisungMeldung pEclWeisungMeldung, int[] abgabe) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 17;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.skIst = skIst;
            weStubBlSammelkarten.meldungIdent = meldungIdent;
            weStubBlSammelkarten.weisungMeldung = pEclWeisungMeldung;
            weStubBlSammelkarten.abgabe = abgabe;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();
        blWillenserklaerung.piMeldungsIdentAktionaer = meldungIdent;
        //		System.out.println("meldungsIdent="+meldungIdent);
        blWillenserklaerung.pWillenserklaerungGeberIdent = -1;
        blWillenserklaerung.pAufnehmendeSammelkarteIdent = sammelIdent;

        blWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
        blWillenserklaerung.pEclWeisungMeldung.weisungIdent = pEclWeisungMeldung.weisungIdent;
        blWillenserklaerung.pEclWeisungMeldung.willenserklaerungIdent = pEclWeisungMeldung.willenserklaerungIdent;
        for (int i = 0; i < 200; i++) {
            blWillenserklaerung.pEclWeisungMeldung.abgabe[i] = abgabe[i];
        }
        blWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();

        switch (skIst) {
        case 1:
            blWillenserklaerung.aendernWeisungAnKIAV(lDbBundle);
            break;
        case 2:
            blWillenserklaerung.aendernWeisungAnSRV(lDbBundle);
            break;
        case 3:
            blWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte(lDbBundle);
            break;
        case 4:
            blWillenserklaerung.aendernBriefwahl(lDbBundle);
            break;
        case 5:
            blWillenserklaerung.aendernWeisungDauervollmachtAnKIAV(lDbBundle);
            break;

        }

        if (blWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return 1;
        } else {
            //			System.out.println("blWillenserklaerung.rcGrundFuerUnzulaessig="+blWillenserklaerung.rcGrundFuerUnzulaessig);
            dbClose();
            return blWillenserklaerung.rcGrundFuerUnzulaessig;
        }
    }

    /*18*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int aendernWeisungMeldungArrayInSammelkarte(int sammelIdent, int skIst, int[] meldungIdent,
            EclWeisungMeldung[] pEclWeisungMeldung) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 18;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.skIst = skIst;
            weStubBlSammelkarten.meldungIdentArray = meldungIdent;
            weStubBlSammelkarten.weisungMeldungArray = pEclWeisungMeldung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        boolean fehler = false;
        for (int i = 0; i < meldungIdent.length; i++) {
            int rc = aendereWeisungMeldungInSammelkarte(sammelIdent, skIst, meldungIdent[i], pEclWeisungMeldung[i],
                    pEclWeisungMeldung[i].abgabe);
            if (rc < 1) {
                istServer = stubStatus.popIstServer();
                dbClose();
                fehler = true;
            }
        }

        if (fehler == true) {
            istServer = stubStatus.popIstServer();
            dbClose();
            return -1;
        }
        istServer = stubStatus.popIstServer();
        dbClose();
        return 1;
    }

    /*19*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int neuInSammelkarteMeldung(int sammelIdentNeu, int skIstNeu, int skWeisungsartZulaessigNeu,
            int meldungIdent, EclWeisungMeldung pEclWeisungMeldung, int[] abgabe) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 19;
            weStubBlSammelkarten.pSammelIdent = sammelIdentNeu;
            weStubBlSammelkarten.skIst = skIstNeu;
            weStubBlSammelkarten.skWeisungsartZulaessigNeu = skWeisungsartZulaessigNeu;
            weStubBlSammelkarten.meldungIdent = meldungIdent;
            weStubBlSammelkarten.weisungMeldung = weisungMeldung;
            weStubBlSammelkarten.abgabe = abgabe;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();
        blWillenserklaerung.piMeldungsIdentAktionaer = meldungIdent;
        //		System.out.println("meldungsIdent="+meldungIdent);
        blWillenserklaerung.pWillenserklaerungGeberIdent = -1;
        blWillenserklaerung.pAufnehmendeSammelkarteIdent = sammelIdentNeu;

        blWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
        for (int i = 0; i < 200; i++) {
            if ((skWeisungsartZulaessigNeu & 4) == 4) {
                blWillenserklaerung.pEclWeisungMeldung.abgabe[i] = abgabe[i];
                //				if (abgabe[i]==KonstStimmart.nichtMarkiert || abgabe[i]==KonstStimmart.frei) {
                //					blWillenserklaerung.pEclWeisungMeldung.abgabe[i]=KonstStimmart.enthaltung;
                //				}
            } else {
                blWillenserklaerung.pEclWeisungMeldung.abgabe[i] = 0;
            }
        }
        blWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();

        switch (skIstNeu) {
        case 1:
            blWillenserklaerung.vollmachtUndWeisungAnKIAV(lDbBundle);
            break;
        case 2:
            blWillenserklaerung.vollmachtUndWeisungAnSRV(lDbBundle);
            break;
        case 3:
            blWillenserklaerung.organisatorischMitWeisungInSammelkarte(lDbBundle);
            break;
        case 4:
            blWillenserklaerung.briefwahl(lDbBundle);
            break;
        case 5:
            blWillenserklaerung.dauervollmachtAnKIAV(lDbBundle);
            break;
        }

        if (blWillenserklaerung.rcIstZulaessig) {
            dbClose();
            return 1;
        } else {
            //			System.out.println("blWillenserklaerung.rcGrundFuerUnzulaessig="+blWillenserklaerung.rcGrundFuerUnzulaessig);
            dbClose();
            return blWillenserklaerung.rcGrundFuerUnzulaessig;
        }
    }

    /*20*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int umbuchenSammelkarteMeldungArray(int sammelIdentAlt, int skIstAlt, int sammelIdentNeu, int skIstNeu,
            int skWeisungsartZulaessigNeu, int[] meldungIdent, EclWeisungMeldung[] pEclWeisungMeldung) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 20;
            weStubBlSammelkarten.pSammelIdent = sammelIdentAlt;
            weStubBlSammelkarten.skIst = skIstAlt;
            weStubBlSammelkarten.sammelIdentNeu = sammelIdentNeu;
            weStubBlSammelkarten.skIstNeu = skIstNeu;
            weStubBlSammelkarten.skWeisungsartZulaessigNeu = skWeisungsartZulaessigNeu;
            weStubBlSammelkarten.meldungIdentArray = meldungIdent;
            weStubBlSammelkarten.weisungMeldungArray = pEclWeisungMeldung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        boolean fehler = false;
        for (int i = 0; i < meldungIdent.length; i++) {
            int rc = widerrufeMeldungInSammelkarte(sammelIdentAlt, skIstAlt, meldungIdent[i]);
            if (rc < 1) {
                istServer = stubStatus.popIstServer();
                dbClose();
                fehler = true;
            }

            rc = neuInSammelkarteMeldung(sammelIdentNeu, skIstNeu, skWeisungsartZulaessigNeu, meldungIdent[i],
                    pEclWeisungMeldung[i], pEclWeisungMeldung[i].abgabe);
            if (rc < 1) {
                fehler = true;
            }
        }

        if (fehler == true) {
            istServer = stubStatus.popIstServer();
            dbClose();
            return -1;
        }
        istServer = stubStatus.popIstServer();
        dbClose();
        return 1;
    }

    /*1*/
    /*************Setzen ALLER Weisungen für einen bestimmten Tagesordnungspunkt********************************
     * pWeisungsPos = Position der Weisung innerhalb abgabe[]
     * artFuerKIAV etc.: KonstStimmart.ja, nein, enthaltung. Oder -1 => für diese Sammelkartenart wird es nicht gesetzt
     * 
     * Hinweis: wenn Summenberechnung für den TOP gesperrt, wird zwar die Weisung für den Aktionär eingetragen, nicht jedoch 
     * die Summe der Sammelkarte korrigiert.
     * */
    public int setzeWeisungFuerTOPFuerAlleAktionaereMitWeisung(int pWeisungsPos, int artFuerKIAV, int artFuerSRV,
            int artFuerOrganisatorisch, int artFuerBriefwahl, int artFuerDauervollmacht) {

        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 1;
            weStubBlSammelkarten.weisungsPos = pWeisungsPos;
            weStubBlSammelkarten.artFuerKIAV = artFuerKIAV;
            weStubBlSammelkarten.artFuerSRV = artFuerSRV;
            weStubBlSammelkarten.artFuerOrganisatorisch = artFuerOrganisatorisch;
            weStubBlSammelkarten.artFuerBriefwahl = artFuerBriefwahl;
            weStubBlSammelkarten.artFuerDauervollmacht = artFuerDauervollmacht;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        lDbBundle.dbMeldungen.leseWirklichAlleSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.anzErgebnis();
        if (anzSammelkarten == 0) {
            return 0;
        }
        rcSammelMeldung = lDbBundle.dbMeldungen.meldungenArray;

        for (int i = 0; i < anzSammelkarten; i++) {
            int sammelIdent = rcSammelMeldung[i].meldungsIdent;
            long sammelStimmen = rcSammelMeldung[i].stimmen;

            int skIst = rcSammelMeldung[i].skIst;
            int stimmart = -1;
            switch (skIst) {
            case 1:
                stimmart = artFuerKIAV;
                break;
            case 2:
                stimmart = artFuerSRV;
                break;
            case 3:
                stimmart = artFuerOrganisatorisch;
                break;
            case 4:
                stimmart = artFuerBriefwahl;
                break;
            case 5:
                stimmart = artFuerDauervollmacht;
                break;
            }

            if ((rcSammelMeldung[i].skWeisungsartZulaessig & 4) != 4) {
                /**Für die Sammelkarte wird die stimmart übernommen, falls Weisungen hinterlegt; sonst "frei"*/
                stimmart = KonstStimmart.nichtMarkiert;
            }

            if (stimmart != -1) {
                /*Holen Weisungen wie in Sammelkopf abgelegt*/
                lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(sammelIdent, false);
                rcWeisungenSammelkopf = lDbBundle.dbWeisungMeldung
                        .weisungMeldungGefunden(0); /*Für Sammelkarte nur ein Satz möglich*/
                for (int i1 = 0; i1 < 10; i1++) {
                    if (rcWeisungenSammelkopf.weisungMeldungSplit.nichtBerechnen[pWeisungsPos]==0) {
                        if (i1 == stimmart) {
                            rcWeisungenSammelkopf.weisungMeldungSplit.abgabe[pWeisungsPos][i1] = sammelStimmen;
                        } else {
                            rcWeisungenSammelkopf.weisungMeldungSplit.abgabe[pWeisungsPos][i1] = 0;
                        }
                    }
                }
                lDbBundle.dbWeisungMeldung.update(rcWeisungenSammelkopf, null, false);

                /*Alle Weisungen zu Sammelkarten updaten*/
                System.out.println("BlSammelkarten sammelIdent, pWeisungsPos, stimmart " + sammelIdent + " "
                        + pWeisungsPos + " " + stimmart);
                lDbBundle.dbWeisungMeldung.updateEinzelWeisungZuSammelkartenMeldungen(sammelIdent, pWeisungsPos,
                        stimmart);
                //				lDbBundle.dbWeisungMeldung.leseZuSammelIdent(sammelIdent, false);
                //				int anzWeisungenInSammelkarte=lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden();
                //				EclWeisungMeldung[] eclWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungArray;
                //				for (int i1=0;i1<anzWeisungenInSammelkarte;i1++) {
                //					EclWeisungMeldung lWeisungMeldung=eclWeisungMeldung[i1];
                //					lWeisungMeldung.abgabe[pWeisungsPos]=stimmart;
                //					lDbBundle.dbWeisungMeldung.update(lWeisungMeldung, null, false);
                //				}
            }
        }
        dbClose();
        return 1;
    }

    /************************************Sammelkarten-Listen für Weisungserfassung***********************************************************************/

    /**Hier werden alle Sammelkarten der gewählten Art geliefert:
     * [0]=alle, [1] bis [5] = Gattung
     */
    public EclMeldung[][] rcSammelkartenFuerWeisungserfassung = null;
    public int[] rcSammelkartenFuerWeisungserfassungAnzahl = null;
    /**Ident der Sammelkarte, die Standard für den Weg ist
     * [0]=undefiniert
     * [1] bis [5] für die jeweilige Gattung
     * 
     * -1 => kein Standard festgelegt*/
    public int rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[] = null;
    /**Ident der Sammelkarte, die zuletzt zur Bearbeitung ausgewählt wurde
     * [0]=undefiniert
     * [1] bis [5] für die jeweilige Gattung
     * 
     * -1 => noch nichts verwendet*/
    public int rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[] = null;
    /**Hier wird einfach der Parameter internetOderPapierOderHV für spätere Auswertungen gespeichert*/
    public int rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV = 0;

    /*21*/
    /**
     * Verwendet Datenbankzugriff
     * 
     * art: KonstSkIst
     * internetOderPapierOderHV
     */
    public int leseSammelkartenlisteFuerWeisungen(int art, int internetOderPapierOderHV) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 21;
            weStubBlSammelkarten.art = art;
            weStubBlSammelkarten.internetOderPapierOderHV = internetOderPapierOderHV;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            rcSammelkartenFuerWeisungserfassung = weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassung;
            rcSammelkartenFuerWeisungserfassungAnzahl = weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungAnzahl;
            rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard = weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard;
            rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent = weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent;
            rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV = weStubBlSammelkartenRC.rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV;

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV = internetOderPapierOderHV;
        rcSammelkartenFuerWeisungserfassung = new EclMeldung[6][];
        rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard = new int[6];
        rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent = new int[6];
        rcSammelkartenFuerWeisungserfassungAnzahl = new int[6];
        for (int i = 0; i < 6; i++) {
            rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[i] = -1;
            rcSammelkartenFuerWeisungserfassungAnzahl[i] = 0;
            rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[i] = -1;
        }

        switch (art) {
        case KonstSkIst.briefwahl:
            lDbBundle.dbMeldungen.leseSammelkartenBriefwahl(-1);
            break;
        case KonstSkIst.dauervollmacht:
            lDbBundle.dbMeldungen.leseSammelkarteDauervollmacht(-1);
            break;
        case KonstSkIst.kiav:
            lDbBundle.dbMeldungen.leseSammelkarteKIAV(-1);
            break;
        case KonstSkIst.organisatorisch:
            lDbBundle.dbMeldungen.leseSammelkarteOrga(-1);
            break;
        case KonstSkIst.srv:
            lDbBundle.dbMeldungen.leseSammelkarteSRV(-1);
            break;
        }

        rcSammelkartenFuerWeisungserfassung[0] = lDbBundle.dbMeldungen.meldungenArray;
        rcSammelkartenFuerWeisungserfassungAnzahl[0] = lDbBundle.dbMeldungen.meldungenArray.length;

        for (int i = 0; i < rcSammelkartenFuerWeisungserfassungAnzahl[0]; i++) {
            int hGattung = rcSammelkartenFuerWeisungserfassung[0][i].liefereGattung();
            rcSammelkartenFuerWeisungserfassungAnzahl[hGattung]++;
        }

        for (int i = 1; i <= 5; i++) {
            rcSammelkartenFuerWeisungserfassung[i] = new EclMeldung[rcSammelkartenFuerWeisungserfassungAnzahl[i]];
            rcSammelkartenFuerWeisungserfassungAnzahl[i] = 0; //Wird nochmal auf 0 gesetzt und dann neu aufgebaut
        }

        for (int i = 0; i < rcSammelkartenFuerWeisungserfassungAnzahl[0]; i++) {
            int hGattung = rcSammelkartenFuerWeisungserfassung[0][i].liefereGattung();
            rcSammelkartenFuerWeisungserfassung[hGattung][rcSammelkartenFuerWeisungserfassungAnzahl[hGattung]] = rcSammelkartenFuerWeisungserfassung[0][i];
            rcSammelkartenFuerWeisungserfassungAnzahl[hGattung]++;
            if (rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[hGattung] == -1) {
                if (internetOderPapierOderHV == 1 && rcSammelkartenFuerWeisungserfassung[0][i].skBuchbarInternet == 1) {
                    rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[hGattung] = rcSammelkartenFuerWeisungserfassung[0][i].meldungsIdent;
                }
                if (internetOderPapierOderHV == 2 && rcSammelkartenFuerWeisungserfassung[0][i].skBuchbarPapier == 1) {
                    rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[hGattung] = rcSammelkartenFuerWeisungserfassung[0][i].meldungsIdent;
                }
                if (internetOderPapierOderHV == 3 && rcSammelkartenFuerWeisungserfassung[0][i].skBuchbarHV == 1) {
                    rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard[hGattung] = rcSammelkartenFuerWeisungserfassung[0][i].meldungsIdent;
                }
            }
        }
        dbClose();
        return 1;
    }

    /**Liefert das Offset in rcSammelkartenFuerWeisungserfassung[pAktuelleGattung][] für
     * die rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[pAktuelleGattung].
     * -1 => nichts gefunden.
     * @param pAktuelleGattung
     * @return
     */
    public int sammelkartenListeFuerWeisungenLiefereOffsetAusgewaehlte(int pAktuelleGattung) {
        int gefOffset = -1;
        for (int i = 0; i < rcSammelkartenFuerWeisungserfassungAnzahl[pAktuelleGattung]; i++) {
            if (rcSammelkartenFuerWeisungserfassung[pAktuelleGattung][i].meldungsIdent == rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[pAktuelleGattung]) {
                gefOffset = i;
            }
        }
        return gefOffset;
    }

    
    /*22*/
    /**
     * skIst:
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht
     **/
    public int aktivierenSammelkarteMeldungArray(int sammelIdent, int skIst,
            int[] meldungIdent, EclWeisungMeldung[] pEclWeisungMeldung) {
        if (verwendeWebService()) {
            WEStubBlSammelkarten weStubBlSammelkarten = new WEStubBlSammelkarten();
            weStubBlSammelkarten.stubFunktion = 22;
            weStubBlSammelkarten.pSammelIdent = sammelIdent;
            weStubBlSammelkarten.skIst = skIst;
            weStubBlSammelkarten.meldungIdentArray = meldungIdent;
            weStubBlSammelkarten.weisungMeldungArray = pEclWeisungMeldung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubBlSammelkartenRC weStubBlSammelkartenRC = wsClient.stubBlSammelkarten(weStubBlSammelkarten);

            if (weStubBlSammelkartenRC.rc < 1) {
                return weStubBlSammelkartenRC.rc;
            }

            return weStubBlSammelkartenRC.rc;
        }

        dbOpen();
        StubStatus stubStatus = new StubStatus();
        stubStatus.pushIstServer(istServer);
        istServer = true;

        boolean fehler = false;
        for (int i = 0; i < meldungIdent.length; i++) {
            /*AAAAA*/
            /*Alle aktiven Weisungen des Aktionärs deaktivieren*/

            int neueWillenserklaerungInMeldung=0;
            int neueSammelIdent=0;
            int neueSkIst=0;
            
            lDbBundle.dbMeldungZuSammelkarte.leseZuMeldungIdent(meldungIdent[i]);
            for (int i1=0;i1<lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();i1++) {
                EclMeldungZuSammelkarte lMeldungZuSammelkarte=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteArray[i1];
                
                boolean weisungAktivieren=false;
                int aktivierungscode=0;
                /*Ist es diese Weisung, die aktiviert werden muß?*/
                if (lMeldungZuSammelkarte.weisungIdent==pEclWeisungMeldung[i].weisungIdent) {
                    weisungAktivieren=true;
                    aktivierungscode=1;
                    neueWillenserklaerungInMeldung=pEclWeisungMeldung[i].willenserklaerungIdent;
                    neueSammelIdent=pEclWeisungMeldung[i].sammelIdent;
                    neueSkIst=pEclWeisungMeldung[i].skIst;
                }
                    
                if (lMeldungZuSammelkarte.aktiv==1 || weisungAktivieren==true) {
                    /*Falls Weisung gerade aktiv, dann  inaktiv setzen. Wenn Weisung die zu aktivierende ist, dann ebenfalls setzen*/
                    lMeldungZuSammelkarte.aktiv=aktivierungscode;
                    lDbBundle.dbMeldungZuSammelkarte.update(lMeldungZuSammelkarte);

                    int lWeisungIdent=lMeldungZuSammelkarte.weisungIdent;
                    lDbBundle.dbWeisungMeldung.leseZuWeisungIdent(lWeisungIdent, false);
                    EclWeisungMeldung lWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    lWeisungMeldung.aktiv=aktivierungscode;
                    lDbBundle.dbWeisungMeldung.update(lWeisungMeldung, null, false);
                }
            }
            
            
            /*Die Nummer der zuletzt aktivierten Willenserklärung in meldungen eintragen*/
            lDbBundle.dbMeldungen.leseZuIdent(meldungIdent[i]);
            EclMeldung lMeldung=lDbBundle.dbMeldungen.meldungenArray[0];
            lMeldung.willenserklaerungIdent=neueWillenserklaerungInMeldung;
            lMeldung.meldungEnthaltenInSammelkarte=neueSammelIdent;
            lMeldung.meldungEnthaltenInSammelkarteArt=neueSkIst;

            lMeldung.willenserklaerungIdent_Delayed=neueWillenserklaerungInMeldung;
            lMeldung.meldungEnthaltenInSammelkarte_Delayed=neueSammelIdent;
            lMeldung.meldungEnthaltenInSammelkarteArt_Delayed=neueSkIst;
            
            lDbBundle.dbMeldungen.update(lMeldung);

        }

        if (fehler == true) {
            istServer = stubStatus.popIstServer();
            dbClose();
            return -1;
        }
        istServer = stubStatus.popIstServer();
        dbClose();
        return 1;
    }

    /*Testfälle für Singulus
     * 
     * Alles leer machen
     * 
     * 11: 2 Aktien
     * Mit SRV, dann SRV ändern, dann Briefwahl, dann Briefwahl ändern, dann SRV
     * 
     * 12: 13 Aktien
     * Mit SRV, dann Briefwahl.
     * 
     * 13: 14 Aktien
     * mit SRV, dann Briefwahl. Eingeloggt bleiben.
     * 
     * Vor Abstimmungsauswertung: 11 und 12 hin zu Briefwahl aktivieren. Sammelkartensummen korrigieren. Prüfen.
     * 
     * Dann ist jetzt quasi Abstimmung
     * 
     * Dann Alle SRV wieder aktiv setzen.
     * 
     * Dann bei 13, der noch eingeloggt ist, Briefwahl wieder ändern.
     * 
     * Wieder SRV aktivieren bei 13
     * 
     * 
     * 
     */
    
}
