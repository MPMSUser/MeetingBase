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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufDefinition;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufVerwendung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchlauf;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchlaufRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Grundsätzliches zu Suchläufen
 * =============================
 * Suchläufe müssen getrennt aufgerufen und verarbeitet werden für:
 * > Suche im Aktienregister
 * > Suche im Meldeverzeichnis
 * >> Optional: Suche im Vertretername / Aktionärsname
 *
 * Suche wird kombiniert aus:
 * > Suchlauf Suchbegriffe
 * > Individual-Suche
 * >> Aktienregisternummer
 * >> Eintrittskartennummer
 * >> Stimmkartennummer
 * >> Einzelname
 * >>>> Vertreter, Aktionär
 * >> MeldeIdent
 *
 * Parametrisierung der Suche (bei Suche nach Meldedaten! bei Suche nach Aktienregister irrelevant):
 * BlSuchen:
 * > hier werden grundsätzlich inaktive mit angezeigt. Ob inaktive auch ausgewählt werden dürfen, wird in der
 * 		Oberfläche geregelt. Hintergrund: es soll bei Einzelsuche einfach alles gefunden werden
 * > Sammelkarten, inSammelkarten, Gäste ist parametrisiert
 * BlSuchlauf, Suchen nach den Suchbegriffen:
 * > hier ist Sammelkarten, inSammelkarten, Gäste, inaktive parametrisiert
 * > Sammelkarten, inSammelkarten, Gäste werden dabei direkt beim Select ausgefiltert
 * > inaktive werden im Select immer gefunden, jedoch dann in BlSuchlauf ausgefiltert
 * 		(Hintergrund: bei Suche im Aktienregister sollen ja alle gefunden werden, auch wenn die
 * 		Meldung inaktiv)
 *
 *
 * Gefundener Satz erhält Status:
 * > in Suchlauf gefunden, aber noch nicht weiter gekennzeichnet
 * > ausgeblendet
 * > bereits verarbeitet
 *
 * Grundsätzlich werden alle gefundenen "SuchlaufErgebnisse" gespeichert. D.h. auch, dass z.B. ein Aktienregistersatz
 * mehrfach in Suchlaufergebnissen enthalten sein kann. Damit sind auch komplexe Mehrfachverarbeitungen möglich (z.B.
 * eine Aktienregisternummer beim Verarbeiten in mehrere Meldungen verteilen.
 *
 * Achtung:
 * Verarbeitet heißt: das gefundene Suchlaufergebnis WURDE verarbeitet, d.h.
 * NICHT, dass im Verarbeitungsergebnis der mit der Verarbeitung erzeugte Satz noch
 * vorhanden ist - er könnte dort ja gelöscht worden sein.
 *
 * D.h: Suchlaufergebnisse sind ein Pool, zur Erzeugung und zur Überwachung.
 * Aber Löschungen müssen im VERARBEITUNGSERGEBNIS durchgeführt werden.
 *
 * D.h. insbesondere: falls in einem Suchlaufergebnise Sätze NICHT MEHR
 * VORHANDEN sind, muß manuell überprüft werden, ob diese auch im
 * Verarbeitungsergebnis storniert werden müssen.
 *
 * Spezialfall: Einzelentlastung
 * -----------------------------
 * Suchlaufdefinition für Einzelentlastung besteht aus:
 * > Stimmausschlußkennzeichen (V, A, etc.)
 * > abstimmungsident.
 *
 * Es gibt dann einen "Kombi-Suchlauf", der alle Suchläufe zu "V" bzw. "A" etc. als "Suchlauf Suchbegriffe" Basis nimmt.
 *
 */
public class BlSuchlauf extends StubRoot {

    private int logDrucken = 3;

    public EclSuchlaufDefinition eclSuchlaufDefinition = null;
    public String suchlaufBegriffe = null;

    public List<EclSuchlaufErgebnis> suchlaufErgebnisAlleListe = null;
    public List<EclAktienregister> aktienregisterAlleListe = null;
    public List<EclMeldung> meldungAlleListe = null;

    public List<EclSuchlaufErgebnis> suchlaufErgebnisVeraenderungListe = null;
    public List<EclAktienregister> aktienregisterVeraenderungListe = null;
    public List<EclMeldung> meldungVeraenderungListe = null;

    public List<EclSuchlaufErgebnis> suchlaufErgebnisUnbearbeiteteListe = null;
    public List<EclAktienregister> aktienregisterUnbearbeiteteListe = null;
    public List<EclMeldung> meldungUnbearbeiteteListe = null;

    public List<EclSuchlaufErgebnis> suchlaufVerarbeiteteListe = null;
    public List<EclAktienregister> aktienregisterVerarbeiteteListe = null;
    public List<EclMeldung> meldungVerarbeiteteListe = null;

    public List<EclSuchlaufErgebnis> suchlaufErgebnisAusgeblendeteListe = null;
    public List<EclAktienregister> aktienregisterAusgeblendeteListe = null;
    public List<EclMeldung> meldungAusgeblendeteListe = null;

    /**Benutzt, um die Elemente des Suchlaufergebnisses, die verarbeitet werden sollen, zu erzeugen*/
    public List<EclSuchlaufErgebnis> suchlaufErgebnisAusgewaehltListe = null;
    public List<EclAktienregister> aktienregisterAusgewaehltListe = null;
    public List<EclMeldung> meldungAusgewaehltListe = null;

    public BlSuchlauf(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /*******************************************************************Funktionsübergreifende Methoden********************************************************************/
    /**********************************************************************************************************************************************************************/

    /*1*/
    /**Füllt eclSuchlaufDefinition; ==null => nichts gefunden*/
    public int leseSuchlaufDefinition(int pVerwendung, String pParameter1, String pParameter2, String pParameter3,
            String pParameter4, String pParameter5) {

        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 1;
            weStubBlSuchlauf.pVerwendung = pVerwendung;
            weStubBlSuchlauf.pParameter1 = pParameter1;
            weStubBlSuchlauf.pParameter2 = pParameter2;
            weStubBlSuchlauf.pParameter3 = pParameter3;
            weStubBlSuchlauf.pParameter4 = pParameter4;
            weStubBlSuchlauf.pParameter5 = pParameter5;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            eclSuchlaufDefinition = weStubBlSuchlaufRC.eclSuchlaufDefinition;
            suchlaufBegriffe = weStubBlSuchlaufRC.suchlaufBegriffe;

            return weStubBlSuchlaufRC.rc;
        }

        dbOpenUndWeitere();
        eclSuchlaufDefinition = null;
        int rc = lDbBundle.dbSuchlaufDefinition.readVerwendung(pVerwendung, pParameter1, pParameter2, pParameter3,
                pParameter4, pParameter5);
        if (rc > 1) {
            dbClose();
            CaBug.drucke("BlSuchlauf.leseSuchlaufDefinition 001");
            return -1;
        }
        if (rc == 1) {
            eclSuchlaufDefinition = lDbBundle.dbSuchlaufDefinition.ergebnisPosition(0);
            boolean global = false;
            if (eclSuchlaufDefinition.identSuchlaufBegriffIstGlobal == 1) {
                global = true;
            }
            lDbBundle.dbSuchlaufBegriffe.read(global, eclSuchlaufDefinition.identSuchlaufBegriffe);
            suchlaufBegriffe = lDbBundle.dbSuchlaufBegriffe.ergebnisPosition(0).suchbegriffe;
        }
        dbClose();
        return 1;
    }

    /*2*/
    /**speichert eclSuchlaufDefinition als neue Definition ab.
     * eclSuchlaufDefinition wird "insofern" korrigiert, als anschließend ident eingetragen ist*/
    public int erzeugeSuchlaufDefinition(EclSuchlaufDefinition pSuchlaufDefinition) {

        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 2;
            weStubBlSuchlauf.pSuchlaufDefinition = pSuchlaufDefinition;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            eclSuchlaufDefinition = weStubBlSuchlaufRC.eclSuchlaufDefinition;

            return weStubBlSuchlaufRC.rc;
        }

        dbOpenUndWeitere();
        eclSuchlaufDefinition = null;
        int rc = lDbBundle.dbSuchlaufDefinition.insert(pSuchlaufDefinition);
        if (rc < 1) {
            dbClose();
            CaBug.drucke("BlSuchlauf.erzeugeSuchlaufDefinition 001");
            return -1;
        }
        eclSuchlaufDefinition = pSuchlaufDefinition;
        dbClose();
        return 1;
    }

    /*3*/
    /**Input: eclSuchlaufDefinition
     *
     * output:
     * suchlaufErgebnisAlleListe
     * aktienregisterAlleListe (bei Suche nach Register)
     * meldungAlleListe (bei Suche nach Meldungen)
     * */
    public int leseSuchlaufErgebnisEin() {
        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 3;
            weStubBlSuchlauf.pSuchlaufDefinition = eclSuchlaufDefinition;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            suchlaufErgebnisAlleListe = weStubBlSuchlaufRC.suchlaufErgebnisAlleListe;
            aktienregisterAlleListe = weStubBlSuchlaufRC.aktienregisterAlleListe;
            meldungAlleListe = weStubBlSuchlaufRC.meldungAlleListe;

            if (suchlaufErgebnisAlleListe == null) {
                CaBug.drucke("BlSuchlauf.leseSuchlaufErgebnisEin 001 - suchlaufErgebnisAlleListe=null");
            }
            return weStubBlSuchlaufRC.rc;
        }

        CaBug.druckeLog("eclSuchlaufDefinition.verwendung="
                + eclSuchlaufDefinition.verwendung, logDrucken, 10);

        dbOpenUndWeitere();

        suchlaufErgebnisAlleListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterAlleListe = new LinkedList<EclAktienregister>();
        meldungAlleListe = new LinkedList<EclMeldung>();
        int anz = 0;

        /**Einlesen - je nach Register oder Meldebestandssuche*/
        if (KonstSuchlaufVerwendung.isRegisterSuche(eclSuchlaufDefinition.verwendung)) {
            System.out.println("KonstSuchlaufVerwendung.isRegisterSuche");
            anz = lDbBundle.dbJoined.read_suchlaufErgebnisRegister(eclSuchlaufDefinition.ident);
        } else {
            System.out.println("KonstSuchlaufVerwendung.isMeldebestandSuche");
            anz = lDbBundle.dbJoined.read_suchlaufErgebnisMeldungen(eclSuchlaufDefinition.ident);
        }

        for (int i = 0; i < anz; i++) {
            EclSuchlaufErgebnis lSuchlaufErgebnis = lDbBundle.dbJoined.ergebnisSuchlaufErgebnisPosition(i);
            lSuchlaufErgebnis.bereitsInDatenbank = true;
            lSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf = 0;
            suchlaufErgebnisAlleListe.add(lSuchlaufErgebnis);
            aktienregisterAlleListe.add(lDbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i));
            meldungAlleListe.add(lDbBundle.dbJoined.ergebnisMeldungPosition(i));
        }

        dbClose();
        return 1;
    }

    /**Kein Datenzugriff*/
    public int belegeDetailErgebnisse() {

        suchlaufErgebnisVeraenderungListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterVeraenderungListe = new LinkedList<EclAktienregister>();
        meldungVeraenderungListe = new LinkedList<EclMeldung>();

        suchlaufErgebnisUnbearbeiteteListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterUnbearbeiteteListe = new LinkedList<EclAktienregister>();
        meldungUnbearbeiteteListe = new LinkedList<EclMeldung>();

        suchlaufVerarbeiteteListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterVerarbeiteteListe = new LinkedList<EclAktienregister>();
        meldungVerarbeiteteListe = new LinkedList<EclMeldung>();

        suchlaufErgebnisAusgeblendeteListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterAusgeblendeteListe = new LinkedList<EclAktienregister>();
        meldungAusgeblendeteListe = new LinkedList<EclMeldung>();

        for (int i = 0; i < suchlaufErgebnisAlleListe.size(); i++) {
            EclSuchlaufErgebnis lSuchlaufErgebnis = suchlaufErgebnisAlleListe.get(i);
            if (lSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf != 0) {
                suchlaufErgebnisVeraenderungListe.add(suchlaufErgebnisAlleListe.get(i));
                aktienregisterVeraenderungListe.add(aktienregisterAlleListe.get(i));
                meldungVeraenderungListe.add(meldungAlleListe.get(i));
            }
            if (lSuchlaufErgebnis.wurdeVerarbeitet == 0 && lSuchlaufErgebnis.wurdeAusSucheAusgegrenzt == 0) {
                suchlaufErgebnisUnbearbeiteteListe.add(suchlaufErgebnisAlleListe.get(i));
                aktienregisterUnbearbeiteteListe.add(aktienregisterAlleListe.get(i));
                meldungUnbearbeiteteListe.add(meldungAlleListe.get(i));
            }
            if (lSuchlaufErgebnis.wurdeVerarbeitet != 0) {
                suchlaufVerarbeiteteListe.add(suchlaufErgebnisAlleListe.get(i));
                aktienregisterVerarbeiteteListe.add(aktienregisterAlleListe.get(i));
                meldungVerarbeiteteListe.add(meldungAlleListe.get(i));
            }
            if (lSuchlaufErgebnis.wurdeAusSucheAusgegrenzt != 0) {
                suchlaufErgebnisAusgeblendeteListe.add(suchlaufErgebnisAlleListe.get(i));
                aktienregisterAusgeblendeteListe.add(aktienregisterAlleListe.get(i));
                meldungAusgeblendeteListe.add(meldungAlleListe.get(i));
            }
        }
        return 1;
    }

    /**++++++++++++++++++++++++Update des Suchlaufergebnisses mit neuen Registereinträgen / Meldungen+++++++++++++++++++++++++++++++++++++++++*/
    public EclAktienregister[] ergaenzeAktienregisterArray = null;
    public EclMeldung[] ergaenzeMeldungenArray = null;
    public String[] ergaenzeVollmacht = null;

    /**
     * =0 => wurde nicht in Suchlaufergebnis aufgenommen
     * =1 => wurde aufgenommen
     */
    public int[] rcErgaenzungHatStattgefunden = null;

    /**Anzahl der Sätze, die in das Suchlaufergebnis aufgenommen wurden*/
    public int rcAnzahlErgaenzt = 0;

    /**Anzahl der Sätze, die nicht in das Suchlaufergebnis aufgenommen wurden,
     * weil schon vorhanden*/
    public int rcSchonVorhanden = 0;

    /**Wird nur geliefert, wenn nichtErgaenzteInSuchlaufBegriffeMarkieren==true*/
    public int rcNichtMehrEnthalten = 0;

    /*6*/
    /**Parameter-Relevanz:
     * aktienregisterOderMeldungen==1
     * 		nurAktionaereOderNurVertreterOderAktionaereVertreter darf nur 1 oder 3 sein
     * 		restliche Parameter nicht relevant
     *
     * Benötigt als zusätzlichen Input:
     * > suchlaufErgebnisAlleListe
     * > aktienregisterAlleListe
     * > meldungAlleListe
     * > suchlaufBegriffe
     * > eclSuchlaufDefinition
     *
     * Liefert als Output:
     * > suchlaufErgebnisAlleListe
     * > aktienregisterAlleListe
     * > meldungAlleListe
     * > rcNichtMehrEnthalten
     * > rcAnzahlErgaenzt
     * > rcSchonVorhanden
     */
    public int sucheNachSuchbegriffen(int aktienregisterOderMeldungen,
            int nurAktionaereOderNurVertreterOderAktionaereVertreter, //
            boolean durchsuchenSammelkarten, //nur Relevant bei aktienregisterOderMeldungen==2
            boolean durchsuchenInSammelkarten, //nur Relevant bei aktienregisterOderMeldungen==2
            boolean durchsuchenGaeste, //nur Relevant bei aktienregisterOderMeldungen==2
            boolean auchInaktiveAufnehmen //nur Relevant bei aktienregisterOderMeldungen==2
    ) {

        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 6;
            weStubBlSuchlauf.aktienregisterOderMeldungen = aktienregisterOderMeldungen;
            weStubBlSuchlauf.nurAktionaereOderNurVertreterOderAktionaereVertreter = nurAktionaereOderNurVertreterOderAktionaereVertreter;
            weStubBlSuchlauf.durchsuchenSammelkarten = durchsuchenSammelkarten;
            weStubBlSuchlauf.durchsuchenInSammelkarten = durchsuchenInSammelkarten;
            weStubBlSuchlauf.durchsuchenGaeste = durchsuchenGaeste;
            weStubBlSuchlauf.auchInaktiveAufnehmen = auchInaktiveAufnehmen;
            weStubBlSuchlauf.suchlaufErgebnisAlleListe = suchlaufErgebnisAlleListe;
            weStubBlSuchlauf.aktienregisterAlleListe = aktienregisterAlleListe;
            weStubBlSuchlauf.meldungAlleListe = meldungAlleListe;
            weStubBlSuchlauf.suchlaufBegriffe = suchlaufBegriffe;
            weStubBlSuchlauf.pSuchlaufDefinition = eclSuchlaufDefinition;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            suchlaufErgebnisAlleListe = weStubBlSuchlaufRC.suchlaufErgebnisAlleListe;
            aktienregisterAlleListe = weStubBlSuchlaufRC.aktienregisterAlleListe;
            meldungAlleListe = weStubBlSuchlaufRC.meldungAlleListe;
            rcNichtMehrEnthalten = weStubBlSuchlaufRC.rcNichtMehrEnthalten;
            rcAnzahlErgaenzt = weStubBlSuchlaufRC.rcAnzahlErgaenzt;
            rcSchonVorhanden = weStubBlSuchlaufRC.rcSchonVorhanden;

            return weStubBlSuchlaufRC.rc;
        }

        dbOpen();
        BlSuchlaufVertreterFuerSuchErgebnisListe lVertreterFuerSuchErgebnisListe = new BlSuchlaufVertreterFuerSuchErgebnisListe();
        lVertreterFuerSuchErgebnisListe.fuerKompletteListeSplitteVertreterAuf(suchlaufErgebnisAlleListe);

        String[] einzelBegriffe = suchlaufBegriffe.split(";");
        for (int i = 0; i < einzelBegriffe.length; i++) {
            einzelBegriffe[i] = einzelBegriffe[i].trim();
        }

        /*Immer 2 Arrays initialisieren - es werden möglicherweise aber nicht alle verwendet.
         * In anzahl0/1 steht, ob Einträge drin sind*/
        EclAktienregister[][] zAktienregister = new EclAktienregister[2][];
        EclMeldung[][] zMeldungen = new EclMeldung[2][];
        String[][] zSonstigeVollmachten = new String[2][];
        int anzahl[] = { 0, 0 };

        /**Grundsätzliche Logik:
         * 1. Lauf: Suche nach Namen im Register oder Meldebestand
         * 2. Lauf: Suche nach Namen in Vollmachten (falls Vertreter mit durchsucht werden)
         *
         * Werden anschließend "zusammengemerged". Hat ein Melde/Aktienregistereintrag dabei mehrere
         * gefundene Vollmachten, werden diese in gefundeneVollmachtName zusammengefaßt.
         * Dabei Logik mit +, -, (-) siehe EclSuchlaufErgebnis.gefundeneVollmachtName
         */

        if (aktienregisterOderMeldungen == 1) {
            CaBug.druckeLog("Suchen im Aktienregister", logDrucken, 10);
            /**+++++Im Aktienregister suchen++++++*/
            /**
             * Falls nurAktionaereOderNurVertreterOderAktionaereVertreter==1: Ein Suchlauf:
             * > Namen erst im Aktienregister => es werden alle Namen gefunden, die im Register eingetragen sind
             *
             * Falls nurAktionaereOderNurVertreterOderAktionaereVertreter==3: Zwei Suchläufe:
             * > Namen erst im Aktienregister => es werden alle Namen gefunden, die im Register eingetragen sind
             * > Namen in Vollmachten => es werden alle Vollmachten gefunden
             */

            anzahl[0] = lDbBundle.dbJoined.read_suchenErstAktienregister(KonstSuchlaufSuchbegriffArt.nameAktionaer,
                    einzelBegriffe);
            if (anzahl[0] != 0) {
                zAktienregister[0] = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                zMeldungen[0] = lDbBundle.dbJoined.ergebnisMeldung();
                zSonstigeVollmachten[0] = new String[anzahl[0]];
            }

            if (nurAktionaereOderNurVertreterOderAktionaereVertreter == 3) {
                anzahl[1] = lDbBundle.dbJoined.read_suchenErstVertreter(einzelBegriffe, false, false);
                if (anzahl[1] != 0) {
                    zAktienregister[1] = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                    zMeldungen[1] = lDbBundle.dbJoined.ergebnisMeldung();
                    zSonstigeVollmachten[1] = lDbBundle.dbJoined.ergebnisKey();
                }
            }
        } else {
            CaBug.druckeLog("Suchen im Meldeverzeichnis", logDrucken, 10);
            /**+++++Im Meldeverzeichnis suchen+++++*/
            if (nurAktionaereOderNurVertreterOderAktionaereVertreter != 2) {
                int lSuchlaufbegriffArt = KonstSuchlaufSuchbegriffArt.nameAktionaer;
                if (nurAktionaereOderNurVertreterOderAktionaereVertreter == 3) {
                    lSuchlaufbegriffArt = KonstSuchlaufSuchbegriffArt.nameAktionaerOderAktuellerVertreter;
                }
                anzahl[0] = lDbBundle.dbJoined.read_suchenErstMeldungen(lSuchlaufbegriffArt, einzelBegriffe,
                        durchsuchenSammelkarten, durchsuchenInSammelkarten, durchsuchenGaeste, false);
                if (anzahl[0] != 0) {
                    zAktienregister[0] = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                    zMeldungen[0] = lDbBundle.dbJoined.ergebnisMeldung();
                    zSonstigeVollmachten[0] = new String[anzahl[0]];
                }
            }
            if (nurAktionaereOderNurVertreterOderAktionaereVertreter != 1) {
                anzahl[1] = lDbBundle.dbJoined.read_suchenErstVertreter(einzelBegriffe, false, false);
                if (anzahl[1] != 0) {
                    zAktienregister[1] = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
                    zMeldungen[1] = lDbBundle.dbJoined.ergebnisMeldung();
                    zSonstigeVollmachten[1] = lDbBundle.dbJoined.ergebnisKey();
                }
            }
        }

        /**Nun in das bestehende Suchlaufergebnis einmergen*/
        lVertreterFuerSuchErgebnisListe.setzeVeraenderungenZurueck();

        /**Alle bereits enthaltenen Ergebnisse aus dem Begriffs-Suchlauf auf
         * "nicht mehr enthalten" setzen (werden beim Hinzufügen dann
         * ggf. wieder auf 0 gesetzt)
         */
        for (int i1 = 0; i1 < suchlaufErgebnisAlleListe.size(); i1++) {
            if (suchlaufErgebnisAlleListe.get(i1).entstandenAus == 0) {
                suchlaufErgebnisAlleListe.get(i1).veraenderungGegenueberLetztemSuchlauf = -1;
            }
        }

        rcAnzahlErgaenzt = 0;
        rcSchonVorhanden = 0;
        rcNichtMehrEnthalten = 0;

        for (int i_selectNummer = 0; i_selectNummer < 2; i_selectNummer++) {
            if (anzahl[i_selectNummer] != 0) {
                for (int i_ergebnisNeuerSuchlauf = 0; i_ergebnisNeuerSuchlauf < zAktienregister[i_selectNummer].length; i_ergebnisNeuerSuchlauf++) {
                    int bereitsVorhanden = -1;
                    for (int i_suchlaufErgebnisAlle = 0; i_suchlaufErgebnisAlle < suchlaufErgebnisAlleListe
                            .size(); i_suchlaufErgebnisAlle++) {
                        EclSuchlaufErgebnis lSuchlaufErgebnis = suchlaufErgebnisAlleListe.get(i_suchlaufErgebnisAlle);
                        if (lSuchlaufErgebnis.entstandenAus == 0 && //Nur Sätze korrigieren, die aus Namenssuche entstanden sind
                                ((aktienregisterOderMeldungen == 1
                                        && lSuchlaufErgebnis.identAktienregister == zAktienregister[i_selectNummer][i_ergebnisNeuerSuchlauf].aktienregisterIdent)
                                        || (aktienregisterOderMeldungen == 2
                                                && lSuchlaufErgebnis.identMelderegister == zMeldungen[i_selectNummer][i_ergebnisNeuerSuchlauf].meldungsIdent))) { /*gefundenes Element ist bereits in Suchlaufergebnis vorhanden*/
                            bereitsVorhanden = i_suchlaufErgebnisAlle;
                        }
                    }
                    if (bereitsVorhanden != -1) { /*gefundenes Element ist bereits in Suchlaufergebnis vorhanden*/
                        suchlaufErgebnisAlleListe.get(bereitsVorhanden).veraenderungGegenueberLetztemSuchlauf = 0;
                        if (zSonstigeVollmachten[i_selectNummer][i_ergebnisNeuerSuchlauf] != null
                                && !zSonstigeVollmachten[i_selectNummer][i_ergebnisNeuerSuchlauf].isEmpty()) {
                            lVertreterFuerSuchErgebnisListe.trageNeuenVertreternameInBestehendemErgebnisEin(
                                    bereitsVorhanden, zSonstigeVollmachten[i_selectNummer][i_ergebnisNeuerSuchlauf]);
                            rcSchonVorhanden++;
                        }
                    } else { /*gefundenes Element anfügen*/
                        if (aktienregisterOderMeldungen == 1
                                || zMeldungen[i_selectNummer][i_ergebnisNeuerSuchlauf].meldungAktiv == 1
                                || auchInaktiveAufnehmen) {
                            rcAnzahlErgaenzt++;
                            /**Anhängen*/
                            EclSuchlaufErgebnis neuesSuchlaufErgebnis = new EclSuchlaufErgebnis();
                            neuesSuchlaufErgebnis.mandant = lDbBundle.clGlobalVar.mandant;
                            neuesSuchlaufErgebnis.ident = -1;
                            neuesSuchlaufErgebnis.identSuchlaufDefinition = eclSuchlaufDefinition.ident;
                            neuesSuchlaufErgebnis.entstandenAus = 0;
                            neuesSuchlaufErgebnis.einzelSuchBegriff = "";
                            neuesSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf = 1;
                            neuesSuchlaufErgebnis.identAktienregister = zAktienregister[i_selectNummer][i_ergebnisNeuerSuchlauf].aktienregisterIdent;
                            neuesSuchlaufErgebnis.identMelderegister = zMeldungen[i_selectNummer][i_ergebnisNeuerSuchlauf].meldungsIdent;
                            neuesSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
                            neuesSuchlaufErgebnis.gefundeneVollmachtName = "";

                            suchlaufErgebnisAlleListe.add(neuesSuchlaufErgebnis);
                            aktienregisterAlleListe.add(zAktienregister[i_selectNummer][i_ergebnisNeuerSuchlauf]);
                            meldungAlleListe.add(zMeldungen[i_selectNummer][i_ergebnisNeuerSuchlauf]);

                            String hVertretername = "";
                            if (zSonstigeVollmachten[i_selectNummer][i_ergebnisNeuerSuchlauf] != null) {
                                hVertretername = zSonstigeVollmachten[i_selectNummer][i_ergebnisNeuerSuchlauf];
                            }
                            lVertreterFuerSuchErgebnisListe.trageNeuenVertreternameMitNeuemErgebnisEin(hVertretername);
                        }
                    }
                }
            }
        }
        lVertreterFuerSuchErgebnisListe.fuerKompletteListeSetzeVertreterZusammen(suchlaufErgebnisAlleListe);

        /*Nun ergänzte EclSuchlaufErgebnisse mit Idents versehen*/
        verseheErgaenzteSuchlaufErgebnisseMitIdents();

        /**Durchzählen aller nicht mehr enthaltenen*/
        for (int i1 = 0; i1 < suchlaufErgebnisAlleListe.size(); i1++) {
            if (suchlaufErgebnisAlleListe.get(i1).entstandenAus == -1) {
                if (suchlaufErgebnisAlleListe.get(i1).wurdeVerarbeitet == 1) {
                    suchlaufErgebnisAlleListe.get(i1).verarbeitetNichtMehrInSuchergebnis = 1;
                }
                rcNichtMehrEnthalten++;
            }
        }

        dbClose();
        return 1;
    }

    /**Voraussetzungen:
     *
     * Mit leseSuchlaufErgebnisEin() müssen vorbelegt sein:
     * suchlaufErgebnisAlleListe
     * aktienregisterAlleListe (bei Suche nach Register)
     * meldungAlleListe (bei Suche nach Meldungen)
     *
     * ergaenzeAktienregisterArray, ergaenzeMeldungenArray müssen gefüllt sein.
     *
     * suchquelle
     *
     * Ergebnis:
     *
     * suchlaufErgebnisAlleListe
     * aktienregisterAlleListe (bei Suche nach Register)
     * meldungAlleListe (bei Suche nach Meldungen)
     * Sind entsprechend ergänzt.
     * Die mit belegeDetailErgebnisse erzeugten Listen werden NICHT verändert und müssen
     * dementsprechend anschließend neu erzeugt werden.
     *
     * rcErgaenzungHatStattgefunden, rcAnzahlErgaenzt, rcSchonVorhanden sind belegt falls entstandenAus==0 d.h. Suchbegriffe.
     *
     * Die Einträge werden immer ZUSÄTZLICH eingetragen, außer bei entstandenAus==0 d.h. Suchbegriffe! (Suchbegriffe können
     * aktualisiert werden, Rest nicht).
     *
     * D.h. die Funktion darf nur fürs "Einzelbegriffsuchen" verwendet werden!
     * (Für Suchlaufbegriffe: sucheNachSuchbegriffen verwenden!)
     */
    public int ergaenzeSuchlaufErgebnis(int entstandenAus, String einzelSuchBegriff) {
        CaBug.druckeLog("Start", logDrucken, 10);
        rcAnzahlErgaenzt = 0;
        rcSchonVorhanden = 0;
        rcNichtMehrEnthalten = 0;

        if (KonstSuchlaufVerwendung.isRegisterSuche(eclSuchlaufDefinition.verwendung)) {
            CaBug.druckeLog("isRegisterSuche", logDrucken, 10);
            /******Suchlauf über Aktienregister****/
            if (ergaenzeAktienregisterArray == null || ergaenzeAktienregisterArray.length == 0) {
                CaBug.druckeLog("Return, weil ergaenze-Array = leer", logDrucken, 10);
                return 1;
            }
            for (int i = 0; i < ergaenzeAktienregisterArray.length; i++) {
                CaBug.druckeLog("Wird ergänzt", logDrucken, 10);
                rcAnzahlErgaenzt++;
                /**Anhängen*/
                EclSuchlaufErgebnis neuesSuchlaufErgebnis = new EclSuchlaufErgebnis();
                neuesSuchlaufErgebnis.mandant = lDbBundle.clGlobalVar.mandant;
                neuesSuchlaufErgebnis.ident = -1;
                neuesSuchlaufErgebnis.identSuchlaufDefinition = eclSuchlaufDefinition.ident;
                neuesSuchlaufErgebnis.entstandenAus = entstandenAus;
                neuesSuchlaufErgebnis.einzelSuchBegriff = einzelSuchBegriff;
                neuesSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf = 1;
                neuesSuchlaufErgebnis.identAktienregister = ergaenzeAktienregisterArray[i].aktienregisterIdent;
                neuesSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
                neuesSuchlaufErgebnis.gefundeneVollmachtName = ergaenzeVollmacht[i];

                suchlaufErgebnisAlleListe.add(neuesSuchlaufErgebnis);
                aktienregisterAlleListe.add(ergaenzeAktienregisterArray[i]);
                EclMeldung lMeldung = new EclMeldung();
                meldungAlleListe.add(lMeldung);
            }
        } else {
            CaBug.druckeLog("isMeldebestandSuche", logDrucken, 10);
            /******Suchlauf über Meldungen****/
            if (ergaenzeMeldungenArray == null || ergaenzeMeldungenArray.length == 0) {
                CaBug.druckeLog("Return, weil ergaenze-Array = leer", logDrucken, 10);
                return 1;
            }
            for (int i = 0; i < ergaenzeMeldungenArray.length; i++) {
                CaBug.druckeLog("Wird ergänzt", logDrucken, 10);
                rcAnzahlErgaenzt++;
                /**Anhängen*/
                EclSuchlaufErgebnis neuesSuchlaufErgebnis = new EclSuchlaufErgebnis();
                neuesSuchlaufErgebnis.mandant = lDbBundle.clGlobalVar.mandant;
                neuesSuchlaufErgebnis.ident = -1;
                neuesSuchlaufErgebnis.identSuchlaufDefinition = eclSuchlaufDefinition.ident;
                neuesSuchlaufErgebnis.entstandenAus = entstandenAus;
                neuesSuchlaufErgebnis.einzelSuchBegriff = einzelSuchBegriff;
                neuesSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf = 1;
                neuesSuchlaufErgebnis.identMelderegister = ergaenzeMeldungenArray[i].meldungsIdent;
                neuesSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
                neuesSuchlaufErgebnis.gefundeneVollmachtName = ergaenzeVollmacht[i];

                suchlaufErgebnisAlleListe.add(neuesSuchlaufErgebnis);
                meldungAlleListe.add(ergaenzeMeldungenArray[i]);
                aktienregisterAlleListe.add(ergaenzeAktienregisterArray[i]);
            }
        }

        /*Nun ergänzte EclSuchlaufErgebnisse mit Idents versehen*/
        verseheErgaenzteSuchlaufErgebnisseMitIdents();

        return 1;
    }

    /**Datenzugriff nur über Sub-Funktion*/
    private void verseheErgaenzteSuchlaufErgebnisseMitIdents() {
        holeNeueSuchlaufIdents(rcAnzahlErgaenzt);
        CaBug.druckeLog("rcAnzahlErgaenzt=" + rcAnzahlErgaenzt,
                logDrucken, 10);
        int offset = 0;
        for (int i1 = 0; i1 < suchlaufErgebnisAlleListe.size(); i1++) {
            if (suchlaufErgebnisAlleListe.get(i1).ident == -1) {
                suchlaufErgebnisAlleListe.get(i1).ident = neueSuchlaufIdents[offset];
                CaBug.druckeLog("i1=" + i1 + " neueSuchlaufIdents[i1]=" + neueSuchlaufIdents[offset], logDrucken, 10);
                offset++;
            }
        }
    }

    /**Darf nur wg. Stub von extern - WAIntern - zugegriffen werden!*/
    public int[] neueSuchlaufIdents;

    /*4*/
    /**nur public wg. Zugriff aus WAIntern wg. Stub!*/
    public int holeNeueSuchlaufIdents(int pAnzahl) {
        CaBug.druckeLog("pAnzahl=" + pAnzahl, logDrucken, 10);
        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 4;
            weStubBlSuchlauf.pAnzahl = pAnzahl;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            neueSuchlaufIdents = weStubBlSuchlaufRC.neueSuchlaufIdents;

            return weStubBlSuchlaufRC.rc;
        }

        neueSuchlaufIdents = new int[pAnzahl];
        dbOpen();
        lDbBundle.dbBasis.getInterneIdentSuchlaufErgebnisReserviereMehrere(pAnzahl);
        for (int i = 0; i < pAnzahl; i++) {
            neueSuchlaufIdents[i] = lDbBundle.dbBasis.getInterneIdentSuchlaufErgebnisNext();
            CaBug.druckeLog("i=" + i + " neueSuchlaufIdents[i]=" + neueSuchlaufIdents[i], logDrucken, 10);
        }

        dbClose();
        return 1;
    }

    public boolean pruefenObSpeichernErforderlich() {
        boolean rc = false;
        for (int i = 0; i < suchlaufErgebnisAlleListe.size(); i++) {
            if (suchlaufErgebnisAlleListe.get(i).seitLetztemSpeichernVeraendert == true) {
                rc = true;
            }
        }
        return rc;
    }

    /*5*/
    /**pMitReset=true: veraenderungGegenueberLetztemSuchlauf wird auf 0 gesetzt
     *
     * Input: suchlaufErgebnisAlleListe
     *
     * Output: suchlaufErgebnisAlleListe
     * */
    public int speichern(boolean pMitReset) {
        if (verwendeWebService()) {
            WEStubBlSuchlauf weStubBlSuchlauf = new WEStubBlSuchlauf();
            weStubBlSuchlauf.stubFunktion = 5;
            weStubBlSuchlauf.pMitReset = pMitReset;
            weStubBlSuchlauf.suchlaufErgebnisAlleListe = suchlaufErgebnisAlleListe;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlSuchlauf.setWeLoginVerify(weLoginVerify);

            WEStubBlSuchlaufRC weStubBlSuchlaufRC = wsClient.stubBlSuchlauf(weStubBlSuchlauf);

            if (weStubBlSuchlaufRC.rc < 1) {
                return weStubBlSuchlaufRC.rc;
            }
            suchlaufErgebnisAlleListe = weStubBlSuchlaufRC.suchlaufErgebnisAlleListe;

            return weStubBlSuchlaufRC.rc;
        }

        dbOpenUndWeitere();

        for (int i = 0; i < suchlaufErgebnisAlleListe.size(); i++) {
            EclSuchlaufErgebnis lSuchlaufErgebnis = suchlaufErgebnisAlleListe.get(i);
            if (pMitReset) {
                if (lSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf != 0) {
                    lSuchlaufErgebnis.veraenderungGegenueberLetztemSuchlauf = 0;
                    lSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
                }
            }
            if (lSuchlaufErgebnis.seitLetztemSpeichernVeraendert == true) {
                if (lSuchlaufErgebnis.bereitsInDatenbank == true) {
                    /*Updaten*/
                    lDbBundle.dbSuchlaufErgebnis.update(lSuchlaufErgebnis);
                } else {
                    /*Insert*/
                    lDbBundle.dbSuchlaufErgebnis.insert(lSuchlaufErgebnis);
                    lSuchlaufErgebnis.bereitsInDatenbank = true;
                }
                lSuchlaufErgebnis.seitLetztemSpeichernVeraendert = false;
            }
        }

        dbClose();

        return 1;
    }

    /*++++++++++++++++++++++++++++++Ausgewählte Suchlaufergebnisse - zur weiteren Verarbeitung++++++++++++++++++++++++++++*/

    public void ausgewaehlteInit() {
        suchlaufErgebnisAusgewaehltListe = new LinkedList<EclSuchlaufErgebnis>();
        aktienregisterAusgewaehltListe = new LinkedList<EclAktienregister>();
        meldungAusgewaehltListe = new LinkedList<EclMeldung>();
    }

    public void ausgewaehlteTrageElementEin(int pIdent) {
        for (int i1 = 0; i1 < suchlaufErgebnisAlleListe.size(); i1++) {
            EclSuchlaufErgebnis lSuchlaufErgebnis = suchlaufErgebnisAlleListe.get(i1);
            if (pIdent == lSuchlaufErgebnis.ident) {
                suchlaufErgebnisAusgewaehltListe.add(suchlaufErgebnisAlleListe.get(i1));
                aktienregisterAusgewaehltListe.add(aktienregisterAlleListe.get(i1));
                meldungAusgewaehltListe.add(meldungAlleListe.get(i1));
            }
        }

    }

    /*++++++++++++++++++++++++++Weitere Verarbeitung++++++++++++++++++++++++++++++*/
    public int setzeSuchlaufAufAusgeblendet(String pWeil) {
        for (int i = 0; i < suchlaufErgebnisAusgewaehltListe.size(); i++) {
            EclSuchlaufErgebnis pSuchlaufErgebnis = suchlaufErgebnisAusgewaehltListe.get(i);
            pSuchlaufErgebnis.wurdeAusSucheAusgegrenzt = 1;
            pSuchlaufErgebnis.ausgegrenztWeil = CaString.trunc(pWeil, 100);
            pSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
        }
        return 1;
    }

    public int setzeSuchlaufAufEingeblendet() {
        for (int i = 0; i < suchlaufErgebnisAusgewaehltListe.size(); i++) {
            EclSuchlaufErgebnis pSuchlaufErgebnis = suchlaufErgebnisAusgewaehltListe.get(i);
            pSuchlaufErgebnis.wurdeAusSucheAusgegrenzt = 0;
            pSuchlaufErgebnis.ausgegrenztWeil = "";
            pSuchlaufErgebnis.seitLetztemSpeichernVeraendert = true;
        }

        return 1;
    }

    /*+++++++++++++++++++++++++Div++++++++++++++++++++++++++++*/
    private void setzeMeldungsTextZurueck() {
        for (int i = 0; i < suchlaufErgebnisAlleListe.size(); i++) {
            suchlaufErgebnisAlleListe.get(i).meldungsText = "";
        }

    }

    /*******************************************************************Funktionspezifische Methoden********************************************************************/
    /**********************************************************************************************************************************************************************/

    /*********************************InstiBestandsZuordnung***************************************************************************************************************/

    /**Suchlauf liegt anschließend in eclSuchlaufDefinition,
     * Suchbegriffe in eclSuchlaufBegriffe
     *
     * In pEclInsti muß auch suchlaufBegriffe gefüllt sein.*/
    public int leseEinGgfAnlegeSuchlaufDefinition(EclInsti pEclInsti, int registerOderMeldung) {

        int verwendung = 0;
        if (registerOderMeldung == 1) {
            verwendung = KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister;
        } else {
            verwendung = KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen;
        }

        leseSuchlaufDefinition(verwendung, Integer.toString(pEclInsti.ident), "", "", "", "");
        if (eclSuchlaufDefinition == null) {
            System.out.println("leseEinGgfAnlegeSuchlaufDefinition ist null");
            eclSuchlaufDefinition = new EclSuchlaufDefinition();
            eclSuchlaufDefinition.mandant = lDbBundle.clGlobalVar.mandant;
            eclSuchlaufDefinition.bezeichnung = CaString
                    .trunc("Zuordn.Bestände Institutionelle: " + pEclInsti.kurzBezeichnung, 100);
            eclSuchlaufDefinition.identSuchlaufBegriffe = pEclInsti.identSuchlaufBegriffe;
            eclSuchlaufDefinition.identSuchlaufBegriffIstGlobal = 1;
            eclSuchlaufDefinition.verwendung = verwendung;
            eclSuchlaufDefinition.sucheNachAktionaer = 1;
            eclSuchlaufDefinition.sucheNachVertreter = 0;
            eclSuchlaufDefinition.parameter1 = Integer.toString(pEclInsti.ident);

            this.erzeugeSuchlaufDefinition(eclSuchlaufDefinition);
        }

        System.out.println("leseEinGgfAnlegeSuchlaufDefinition eclSuchlaufDefinition.verwendung="
                + eclSuchlaufDefinition.verwendung);

        suchlaufBegriffe = pEclInsti.suchlaufBegriffe;
        return 1;
    }

    /*++++++++++++++++++++++++++++++++++++++++++++Bestandszuordnung durchführen+++++++++++*/

    /*Parameter, die an ordneBestandZu übergeben werden, bzw. lokale Variablen in derselben,
     * zur Weiterverwendund in den lokalen Subroutinen
     */

 
    /**Zuordnung des Bestandes
     *
     * Neben der Zuordnungsdurchführung werden die Suchlaufergebnisse auch zwischengespeichert.
     *
     * Input:
     * pEclInsti: Institutioneller, dem die Bestände zugeordnet werden sollen
     * pRegisterOderMeldung: 1=Register-Bestände werden zugeordnet, 2=Melde-Bestände werden zugeordnet
     * pTeilbestand=true: es wird nur der Teilbestand pAktienTeilbestand zugeordnet
     * 		(bei pRegisteroderMeldung==2 irrelevant)
     * 		In diesem Fall darf suchlaufErgebnisAusgewaehltListe nur EIN Element enthalten!
     * pBeschreibung: wird bei jeder Zuordnung als Beschreibung eingetragen
     * pUserLoginZuInsti: Liste ALLER Kennungen zu dem Insti
     * pUserLoginZugeordnet: true => die jeweilige Kennung wird explizit zugeordnet.
     *
     * Außerdem verwendet:
     * suchlaufErgebnisAusgewaehltListe/aktienregisterAusgewaehltListe/meldungAusgewaehltListe
     *
     * Output:
     * suchlaufErgebnisAlleListe, aktienregisterAlleListe, meldungAlleListe sind entsprechend
     * 		korrigiert (Verarbeitungszeichen).
     */
    public int ordneBestandZu(EclInsti pEclInsti, int pRegisterOderMeldung, boolean pTeilbestand,
            long pAktienTeilbestand, String pBeschreibung, EclUserLogin[] pUserLoginZuInsti,
            boolean[] pUserLoginZugeordnet) {

        dbOpenUndWeitere();

        /*Fehlertexte in suchlaufErgebnisAlleListe zurücksetzen*/
        setzeMeldungsTextZurueck();


        BlInstiRawLevel blInstiRawLevel=new BlInstiRawLevel(lDbBundle); 
        blInstiRawLevel.initEintragen();


        for (int i = 0; i < suchlaufErgebnisAusgewaehltListe.size(); i++) {
            /*Zu verarbeitendes Suchlaufelement*/
            EclSuchlaufErgebnis lSuchlaufErgebnis = suchlaufErgebnisAusgewaehltListe.get(i);
            EclAktienregister lAktienregister = aktienregisterAusgewaehltListe.get(i);
            EclMeldung lMeldung = meldungAusgewaehltListe.get(i);

            if (pRegisterOderMeldung == 1) { /*Register*/
                blInstiRawLevel.verarbeiteHauptZuordnungAktienregister(pEclInsti, lAktienregister, pBeschreibung, pTeilbestand, pAktienTeilbestand,
                        pUserLoginZuInsti, pUserLoginZugeordnet );
            } 
            else { /*Meldung*/
                blInstiRawLevel.verarbeiteHauptZuordnungMeldung(pEclInsti, lMeldung, pBeschreibung, pTeilbestand, pAktienTeilbestand,
                        pUserLoginZuInsti, pUserLoginZugeordnet);
            } 
            lSuchlaufErgebnis.meldungsText=blInstiRawLevel.meldungsText;

            if (blInstiRawLevel.wurdeVerarbeitet==1) {
                lSuchlaufErgebnis.wurdeVerarbeitet = 1;
            }

            if (pRegisterOderMeldung == 1 && pTeilbestand) {
                lSuchlaufErgebnis.parameter1 = "Teilbestand: " + CaString.toStringDE(pAktienTeilbestand);
            }
        }
        /*Zwischenspeichern*/
        speichern(false);

        dbClose();
        return 1;
    }


    /*********************Reine Testfunktionen*****************************************/
    /**********************************************************************************/
    public void testClassBlSuchlaufVertreterFuerSuchErgebnisListe() {
        /*Voraussetzung: zwei Suchlaufergebnis-Einträge*/

        System.out.println("Lauf 1");
        BlSuchlaufVertreterFuerSuchErgebnisListe lBlSuchlaufVertreterFuerSuchErgebnisListe = new BlSuchlaufVertreterFuerSuchErgebnisListe();
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 15;
        suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName = "V1";
        suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName = "V1;V2;V3";

        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSplitteVertreterAuf(suchlaufErgebnisAlleListe);
        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSetzeVertreterZusammen(suchlaufErgebnisAlleListe);

        System.out.println("suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName);
        System.out.println("suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName);

        System.out.println("Lauf 2");
        lBlSuchlaufVertreterFuerSuchErgebnisListe = new BlSuchlaufVertreterFuerSuchErgebnisListe();
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 15;
        suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName = "-V1";
        suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName = "+V1;-V2;(-V3)";

        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSplitteVertreterAuf(suchlaufErgebnisAlleListe);
        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSetzeVertreterZusammen(suchlaufErgebnisAlleListe);

        System.out.println("suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName);
        System.out.println("suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName);

        System.out.println("Lauf 3");
        lBlSuchlaufVertreterFuerSuchErgebnisListe = new BlSuchlaufVertreterFuerSuchErgebnisListe();
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 15;
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 20;
        suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName = "Unvollständig;-VVV1;";
        suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName = "Unvollständig;(-V1);";

        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSplitteVertreterAuf(suchlaufErgebnisAlleListe);
        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSetzeVertreterZusammen(suchlaufErgebnisAlleListe);

        System.out.println("suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName);
        System.out.println("suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName);

        System.out.println("Lauf 4");
        lBlSuchlaufVertreterFuerSuchErgebnisListe = new BlSuchlaufVertreterFuerSuchErgebnisListe();
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 15;
        lBlSuchlaufVertreterFuerSuchErgebnisListe.maximaleLaengeVertreterString = 20;
        suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName = "Unvollständig;V2;(-";
        suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName = "Unvollständig;VV2;(";

        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSplitteVertreterAuf(suchlaufErgebnisAlleListe);
        lBlSuchlaufVertreterFuerSuchErgebnisListe.fuerKompletteListeSetzeVertreterZusammen(suchlaufErgebnisAlleListe);

        System.out.println("suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(0).gefundeneVollmachtName);
        System.out.println("suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName="
                + suchlaufErgebnisAlleListe.get(1).gefundeneVollmachtName);

    }
}
