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
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlag;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Dokumentationspunkte:
 * 
 * Es können maximal 199 Abstimmungspunkte verwaltet werden pro HV.
 */

/*Test:
 * 
 * 
 * Aktuell nur Subtraktionsverfahren testen
 * 
 * Nur Einzelkarten
 * 1 bis 5
 * 
 * Nur Sammelkarten (normal)
 * 6 (alles nein), 7 (N-E-N-E ...) (20001)
 * 8 (alle Nein), 9 (N-E-N ...) (20002)
 * 
 * Nur Briefwahl (normal)
 * 10 (J/N ...), 11 (alles Nein) (20003)
 * 12 (alles J), 13 (Alles E) (20004)
 * 
 * 
 * Test (30.01.2024)
 * Sammelkarte schmeissen. Dann verändern. Dann erst Auswerten.
 */

/*Test für Auswertung (03.02.2024):
 * TOP3:
 * Vorgang 1: (=Stimmzettelnr) (Sa=Stimmausschluß)
 * (1) - 1 A SV Pos 1 Sa=V
 * (2) - U     
 * (3) - 2 A S  Pos 2 Sa=Einzel
 * (4) - 3 J V  Pos 3 Sa=A
 * (5) - 4 N S  Pos 4 Sa=V+A
 * (6) - 5 A SV Pos 9 Sa=V+Einzel
 * (7) - 6 A SV Pos 10 Sa
 * Vorgang 2:
 * (11) - 11 A SV Pos 5
 * (12) - 12 A S  Pos 6
 * (13) - 13 J V  Pos 7
 * (14) - 14 N S  Pos 8
 * (15) - 15 A SV Pos 11
 * (16) - 16 A SV Pos 12
 * 
 * Einzelaktionäre:
 * ----------------
 * 1/5001 Präsenter Aktionär
 * 2/5002 Präsenter Aktionär (gar nichts abgegeben)
 * 3/5003 Präsent gewesener Aktionär
 * 4/5004 Präsent gewesener Aktionär (gar nichts abgegeben)
 * 5/5005 nie präsenter Aktionär
 * 6/5006 nie präsenter Aktionär (gar nichts abgegeben)
 * 
 * 1. Durchlauf: nur präsente
 * 2. Durchlauf: präsente und präsent gewesene
 * 3. Alle
 * 
 * x.1 Durchlauf: nix abgegeben
 * x.2 Durchlauf: 
 * Abgaben für Stämme:  AbstimmungsNr(SzNr) 1(1) (N), 3(4) (N), 4(5) (J), 5 (6) (J)
 * 1-N, 4-N, 5-J, 6-J
 * Abgaben für Vorzüge: (AbstimmungsNr) 1(1) (N), 5(6) (J)
 * 1-N, 6-J
 * 
 * Einzelaktionäre mit Stimmausschluss:
 * ------------------------------------
 * Präsent:
 * 21 / 5021 V-Ausschluss
 * 22 / 5022 A-Ausschluss
 * 23 / 5023 Einzel-Ausschluss
 * 24 / 5024 Einzel-Ausschluss
 * 25 / 5025 V+A-Ausschluss
 * 26 / 5026 V+Einzel-Ausschluss
 * Präsent gewesen:
 * 31 / 5031 V-Ausschluss
 * 32 / 5032 A-Ausschluss
 * 33 / 5033 Einzel-Ausschluss
 * 34 / 5034 Einzel-Ausschluss
 * 35 / 5035 V+A-Ausschluss
 * 36 / 5036 V+Einzel-Ausschluss
 * nie Präsent:
 * 41 / 5041 V-Ausschluss
 * 42 / 5042 A-Ausschluss
 * 43 / 5043 Einzel-Ausschluss
 * 44 / 5044 Einzel-Ausschluss
 * 45 / 5045 V+A-Ausschluss
 * 46 / 5046 V+Einzel-Ausschluss

 * Abstimmungen: V = 1(1) , Einzel-S = 2(3), A = VZ=3 (4), V+A =4 (5), V+Einzel=5(6)
 * 
 * 1. Durchlauf: nur präsente
 * 2. Durchlauf: präsente und präsent gewesene
 * 3. Alle
 * 
 * x.1 Durchlauf: nix abgegeben
 * x.2 Durchlauf: 
 * Abgaben für Stämme:  AbstimmungsNr(SzNr) 1(1) (N), 3(4) (N), 4(5) (J), 5(6) J
 * 1-N, 4-N, 5-J, 6-J 
 * Abgaben für Vorzüge: (AbstimmungsNr) 1(1) (N), 3(4) (N), 5(6) N
 * 1-N, 4-N, 6-N
 * 
 * 
 * Sammelkarten (erst mal ohne Stimmausschluß)
 * -------------------------------------------
 * 
 * 10006 / 10007 BRIEF_P Briefwahl mit Weisungen
 * 57/67 // 5057/5067
 * 10008 / 10009 BRIEF_I Briefwahl mit Kopfweisung
 * 58/68 // 5058/5068
 * 
 * Keiner Präsent
 * Parameter nur präsente / Parameter auch präsent gewesene / Parameter alle
 * Jeweils mit obiger Schmeißaktion.
 * 
 * Alle Präsent - Dito
 * Keiner mehr präsent - Dito
 * 
 * Sammelkarten - gesamte Sammelkarte mit Stimmausschluß
 * -----------------------------------------------------
 * Sammelkarte V-Ausschluss
 * Sammelkarte Einzel-Ausschluss
 * Sammelkarte V- und Einzel-Ausschluss
 * Sammelkarte V- und A-Ausschluss
 * 
 * => obige Sammelkartenvarianten mit diesen untrigen Stimmausschlüssen kombinieren.
 */



/**Durchführen von Abstimmungen am Tag der HV*/
public class BlAbstimmung {

    private int logDrucken = 3;

    private DbBundle lDbBundle = null;

    /**Alle Abstimmungsblöcke, die angezeigt werden können.
     * Wird gefüllt durch leseAnzuzeigendeAbstimmungsbloecke()*/
    public EclAbstimmungsblock[] anzuzeigendeAbstimmungsbloecke = null;

    /**Abstimmungsblock der eingesammelt wird (elektronisch).
     * Wird gefüllt durch leseEinzusammelndenAbstimmungsblock()*/
    public EclAbstimmungsblock einzusammelnderAbstimmungsblock = null;

    /**Abstimmungsblock der verarbeitet wird.
     * Wird gefüllt durch leseZuVerarbeitendenAbstimmungsblock()*/
    public EclAbstimmungsblock zuVerarbeitenderAbstimmungsblock = null;

    /**Abstimmungsblock der lokal verarbeitet wird.
     * Wird gefüllt durch leseLokalenAbstimmungsblock()
     * Achtung, wenn vorhanden / Aktiv, werden auch andere Werte mit diesem überschrieben,
     * d.h. alle Funktionen gehen dann vorrangig auf diesen Block*/
    public EclAbstimmungsblock lokalerAbstimmungsblock = null;

    /*****Bündel: aktiver Abstimmungsblock mit Abstimmungen, durch leseAktivenAbstimmungsblock() gefüllt****/
    /**Vorbelegen vor leseAktivenAbstimmungsblock() - gibt wieder, wie der Abstimmungsblock
     * eingelesen werden soll
     * Alt (bis 01.03.2025):
     * =1 => .seite, .position (u.a. für Tabletabstimmung, Live-Abstimmung im Portal)
     * =2 => .nummerDerStimmkarte, positionAufStimmkarte
     * =3 => .positionInAusdruck
     * 
     * Neu:
     * 1 = Für Tabletabstimmung, Live-Abstimmung im Portal 
     *      .seite, .position, .nummerDerStimmkarte, .positionAufStimmkarte, .positionInAusdruck
     * 2 = Für Stimmzettel, Stimmkarten
     *      .nummerDerStimmkarte, .positionAufStimmkarte, .seite, .position, .positionInAusdruck
     * 3 = Ausdruck gemäß position in Ausdruck (bzw. Tabletposition)
     *      .positionInAusdruck, .seite, .position, .nummerDerStimmkarte, .positionAufStimmkarte
     * 4 = Ausdruck gemäß position in Ausdruck (bzw. Stimmkartenposition)
     *      .positionInAusdruck, .nummerDerStimmkarte, .positionAufStimmkarte, .seite, .position
     * 
     * Achtung - setzen dieses Wertes nur über setzeAktivenAbstimmungsblock* - Methoden
     * */
    public int aktivenAbstimmungsblockSortierenNach = 1;
    /**Ehemals: 1*/
    public void setzeAktivenAbstimmungsblockSortierenNachAufTable() {
        aktivenAbstimmungsblockSortierenNach=1;
    }
    /**Ehemals: 2*/
    public void setzeAktivenAbstimmungsblockSortierenNachStimmkarten() {
        aktivenAbstimmungsblockSortierenNach=2;
    }
    /**Ehemals: 3*/
    public void setzeAktivenAbstimmungsblockSortierenNachAuswertung() {
        switch (lDbBundle.param.paramAbstimmungParameter.sortierungDruckausgabeIndividuell) {
        case 0: /*alter Standard - aus Kompatibilitätsgründen enthalten
                     Paßt i.d.R. für HVen*/
            aktivenAbstimmungsblockSortierenNach=1;
            break;
        case 1: /*tabletabstimmung vorrangig, dann Stimmkarten*/
            aktivenAbstimmungsblockSortierenNach=1;
            break;
        case 2: /*Stimmkarten vorrangig, dann Tabletabstimmung*/
            aktivenAbstimmungsblockSortierenNach=2;
            break;
        case 3: /*positionInAusdruck, danach wie Tabletabstimmung*/
            aktivenAbstimmungsblockSortierenNach=3;
            break;
        case 4: /*positionInAusdruck, danach wie Stimmzettelnummer*/
            aktivenAbstimmungsblockSortierenNach=4;
            break;
        }
        aktivenAbstimmungsblockSortierenNach=3;
    }
    public void setzeAktivenAbstimmungsblockSortierenNachStimmzettelDruck() {
        aktivenAbstimmungsblockSortierenNach=4;
    }
    
    
    
    /**Wird durch leseAktivenAbstimmungsblock() gefüllt*/
    public EclAbstimmungsblock aktiverAbstimmungsblock = null;
    /**Wird durch leseAktivenAbstimmungsblock() gefüllt*/
    public boolean aktiverAbstimmungsblockIstElektronischAktiv = false;
    /**Wird durch leseAktivenAbstimmungsblock() gefüllt
     * Enthält auch die gefüllten Felder identWeisungssatz und vonidentGesamtweisung*/
    public EclAbstimmungZuAbstimmungsblock[] abstimmungenZuAktivenBlock = null;
    /**Wird durch leseAktivenAbstimmungsblock() gefüllt*/
    public EclAbstimmung[] abstimmungen = null;
    public int abstimmungenVersion=0;

    /**Noch zu obigen Bündel, aber: wird gefüllt durch selektiereAbstimmungenFuerErfassung - selektiert aus
     * abstimmungenZuAktivenBlock und abstimmungen
     */
    public EclAbstimmungZuAbstimmungsblock[] selektierteAbstimmungenZuAktivenBlock = null;
    public EclAbstimmung[] selektierteAbstimmungen = null;

    /**Bisheriges/aktuelles gesamtes Abstimmungsverhalten zu einer Meldung wie derzeit gespeichert.
     * Wird durch liefereAktuelleAbstimmungZuMeldeIdent(meldung) gefüllt.
     */
    public EclAbstimmungMeldung bisherigesAbstimmverhalten = null;

    /******Bündel: zum Speichern einer Stimmabgabe mit starteSpeichernFuerMeldung etc.************************/
    /**Bereits abgegebene Stimmen (kummuliert) der Meldung*/
    private EclAbstimmungMeldung abstimmungMeldung = null;
    /**Neuer Raw-Satz, der für diese Stimmabgabe (immer) erzeugt wird*/
    private EclAbstimmungMeldungRaw abstimmungMeldungRaw = null;
    /**Meldung, für die die Stimmabgabe durchgeführt wird*/
    private EclMeldung meldung = null;
    /**true => abgegebene Stimme wird aufgrund Priorisierung übertragen
     * false => wird deswegen nicht übertragen
     */
    private boolean speichernWgPriorisierung = true;

    /**Abstimmungsvorschlag der Gesellschaft*/
    public EclAbstimmungsVorschlag abstimmungsVorschlagDerGesellschaft = null;

    /**Summenarray zur Ermittlung des Abstimmungsergebnisses - 
     * alle Stimmen - Präsente und Briefwahl (einschließlich)
     * gattung = 1-5 für jewelige Gattung, 0 = Gesamtsumme
     */
    private long[][][] abstimmungsSummen = new long[6][200][10];

    /**Summenarray zur Ermittlung des Abstimmungsergebnisses - 
     * nur (!) Briefwahl 
     * gattung = 1-5 für jewelige Gattung, 0 = Gesamtsumme
     */
    private long[][][] abstimmungsSummenBriefwahl = new long[6][200][10];

    /**Fehlerprotokoll - hier werden (in Textform) Warnungen und Fehler eingetragen und 
     * können anschließend angezeigt werden.
     */
    public List<String> rcWarnungenFehler = null;

    /**Initialisierung und Übergabe von zu verwendendes DBBundle.
     * DbBundle muß vor Aufruf der Folgefunktionen jeweils im aufrufenden
     * Modul geöffnet und ggf. geschlossen werden.
     */
    public BlAbstimmung(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    /**Übergabe des aktuellen DbBundle. Damit kann die Klasse in einem Client-Programm laufend aktiv bleiben, jedoch
     * DbBundle zwischendurch geschlossen bzw. bei jeder Transaktion neu initialisiert werden
     */
    public void initDb(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
    }

    private void eintragenWarnungFehler(String wfText) {
        if (rcWarnungenFehler == null) {
            rcWarnungenFehler = new LinkedList<String>();
        }
        rcWarnungenFehler.add(wfText);
    }

    /********************Prüfen, ob Aktionär an Abstimmung teilnehmen darf**********************/
    private boolean pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(EclMeldung pMeldung) {
        if (pMeldung == null) {
            /*Dann keine vernünftige Meldung übergeben*/
            return false;
        }
        if (pMeldung.statusPraesenz_Delayed == 1) {
            /*Präsente dürfen immer*/
            return true;
        }
        if (lDbBundle.param.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene == 2 && pMeldung.statusWarPraesenz_Delayed == 1) {
            /*Wenn Präsent und präsent gewesene Abstimmen dürfen, dann dürfen auch die die schon präsent waren*/
            return true;
        }
        if (lDbBundle.param.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene == 3) {
            /*Dann dürfen alle, die zuordenbar sind ...*/
            return true;
        }
        return false;
        
    }
    
    /*********************div. Daten - nur für Anzeige - füllen****************************************/

    /**Liest alle anzuzeigende Abstimmungsblöcke ein und legt sie in anzuzeigendeAbstimmungsbloecke ab*/
    public boolean leseAnzuzeigendeAbstimmungsbloecke() {
        anzuzeigendeAbstimmungsbloecke = null;

        lDbBundle.dbAbstimmungsblock.read_anzuzeigendeAbstimmungsbloecke();
        if (lDbBundle.dbAbstimmungsblock.anzErgebnis() < 1) {
            return false;
        }
        anzuzeigendeAbstimmungsbloecke = lDbBundle.dbAbstimmungsblock.ergebnisArray;

        return true;
    }

    /**Liest den einzusammelnden Abstimmungsblock ein und legt ihn in einzusammelnderAstimmungsblock ab*/
    public boolean leseEinzusammelndenAbstimmungsblock() {
        einzusammelnderAbstimmungsblock = null;

        lDbBundle.dbAbstimmungsblock.read_einzusammelndenAbstimmungsblock();
        if (lDbBundle.dbAbstimmungsblock.anzErgebnis() != 1) {
            return false;
        }
        einzusammelnderAbstimmungsblock = lDbBundle.dbAbstimmungsblock.ergebnisArray[0];

        return true;
    }

    /**Liest den zu verarbeitenden Abstimmungsblock ein und legt ihn in zuVerarbeitenderAstimmungsblock ab*/
    public boolean leseZuVerarbeitendenAbstimmungsblock() {
        zuVerarbeitenderAbstimmungsblock = null;

        lDbBundle.dbAbstimmungsblock.read_zuVerarbeitendenAbstimmungsblock();
        CaBug.druckeLog("lDbBundle.dbAbstimmungsblock.anzErgebnis()=" + lDbBundle.dbAbstimmungsblock.anzErgebnis(),
                logDrucken, 3);
        if (lDbBundle.dbAbstimmungsblock.anzErgebnis() != 1) {
            return false;
        }
        zuVerarbeitenderAbstimmungsblock = lDbBundle.dbAbstimmungsblock.ergebnisArray[0];

        return true;
    }

    /**Liest den zu verarbeitenden Abstimmungsblock ein und legt ihn in zuVerarbeitenderAstimmungsblock ab*/
    public boolean leseLokalenAbstimmungsblock(int pLokalenAbstimmungblock) {
        if (pLokalenAbstimmungblock==-1) {
            return false;
        }
        lokalerAbstimmungsblock = null;

        lDbBundle.dbAbstimmungsblock.read(pLokalenAbstimmungblock);
        CaBug.druckeLog("lDbBundle.dbAbstimmungsblock.anzErgebnis()=" + lDbBundle.dbAbstimmungsblock.anzErgebnis(),
                logDrucken, 3);
        if (lDbBundle.dbAbstimmungsblock.anzErgebnis() != 1) {
            return false;
        }
        lokalerAbstimmungsblock = lDbBundle.dbAbstimmungsblock.ergebnisArray[0];

        return true;
    }

    /******************Aktiven Abstimmungsblock - mit zugehörigen Abstimmungen - einlesen und abfragen**********/

    /**Liest den "aktiven" Abstimmungsblock (d.h. ggf. einzusammelnden, in jedem Fall aber
     * zu verarbeitenden, Abstimmungsblock mit zugehörigen Daten zur Abstimmung ein.
     * Gefüllt werden:
     * aktiverAbstimmungsblock
     * aktiverAbstimmungsblockIstElektronischAktiv
     * abstimmungenZuAktivenBlock
     * abstimmungen
     * 
     * False = Fehler beim Initialisieren, z.B. zwei Abstimmungsblöcke aktiv*/
    public boolean leseAktivenAbstimmungsblock() {
        return leseAktivenOderLokalenAbstimmungsblock(-1);
    }
    
    /**Wenn pLokalenAbstimmungsblock == -1, dann wird der laut DB aktive Abstimmungsblock eingelesen,
     * sonst pLokalenAbstimmungsblock.
     * @param pLokalenAbstimmungsblock
     * @return
     */
    public boolean leseAktivenOderLokalenAbstimmungsblock(int pLokalenAbstimmungsblock) {

        if (pLokalenAbstimmungsblock==-1) {
            /*Aktiven Abstimmungsblock bestimmen*/
            lDbBundle.dbAbstimmungsblock.read_zuVerarbeitendenAbstimmungsblock();
            if (lDbBundle.dbAbstimmungsblock.anzErgebnis() != 1) {
                return false;
            }
            aktiverAbstimmungsblock = lDbBundle.dbAbstimmungsblock.ergebnisPosition(0);
        }
        else {
            /*Lokalen Abstimmungsblock als aktiven einlesen*/
            lDbBundle.dbAbstimmungsblock.read(pLokalenAbstimmungsblock);
            if (lDbBundle.dbAbstimmungsblock.anzErgebnis() != 1) {
                return false;
            }
            aktiverAbstimmungsblock = lDbBundle.dbAbstimmungsblock.ergebnisPosition(0);
        }
        if (aktiverAbstimmungsblock.aktiv == 2) {
            aktiverAbstimmungsblockIstElektronischAktiv = true;
        } else {
            aktiverAbstimmungsblockIstElektronischAktiv = false;
        }

        /*Abstimmungen zu Abstimmungsblock einlesen*/
        lDbBundle.dbAbstimmungZuAbstimmungsblock.read_zuAbstimmungsblock(aktiverAbstimmungsblock.ident,
                aktivenAbstimmungsblockSortierenNach);
        if (lDbBundle.dbAbstimmungZuAbstimmungsblock.anzErgebnis() < 1) {
            return false;
        }
        abstimmungenZuAktivenBlock = lDbBundle.dbAbstimmungZuAbstimmungsblock.ergebnis();

        /*EclAbstimmung - abstimmungen - Detaildaten einlesen.
         * abstimmungenZuAktivenBlock um Weisungsposition ergänzen*/
        abstimmungen = new EclAbstimmung[abstimmungenZuAktivenBlock.length];
        for (int i = 0; i < abstimmungenZuAktivenBlock.length; i++) {
            int abstimmungsIdent = abstimmungenZuAktivenBlock[i].identAbstimmung;
            lDbBundle.dbAbstimmungen.leseIdent(abstimmungsIdent);
            if (lDbBundle.dbAbstimmungen.anzAbstimmungenGefunden() != 1) {
                return false;
            }
            abstimmungen[i] = lDbBundle.dbAbstimmungen.abstimmungengGefunden(0);
            abstimmungenZuAktivenBlock[i].identWeisungssatz = abstimmungen[i].identWeisungssatz;
            abstimmungenZuAktivenBlock[i].vonIdentGesamtweisung = abstimmungen[i].vonIdentGesamtweisung;
        }
        
        BvReload bvReload=new BvReload(lDbBundle);
        bvReload.checkReload(lDbBundle.clGlobalVar.mandant);
        abstimmungenVersion=bvReload.reloadAbstimmungen;
        return true;
    }

    /**Liefert Anzahl der Abstimmungen in aktivemAbstimmungsblock. Dieser muß vorher mit
     * leseAktivenAbstimmungsblock() eingelesen worden sein.
     */
    public int getAnzAbstimmungenInAktivemAbstimmungsblock() {
        return abstimmungenZuAktivenBlock.length;
    }

    /**Liefert den offset in den Abstimmungen des aktiven Abstimmungsblocks der Abstimmung, 
     * die durch stimmkartenNr und stimmkartenPosition identifiziert wird.
     * 
     * Für Markierungsskarten müssen stimmkartenNr und stimmkartenPosition angegeben werden.
     * 
     * Für Stimmschnipsel (ohne Markierung!) wird stimmkartenPosition als -1 übergeben - damit wird
     * dann stimmkartenPosition ignoriert und nur stimmkartenNr ausgewertet. Achtung: sind dann mehrere
     * Abstimmungen mit der selben stimmkartenNr enthalten, gibts undefinierte Ergebnisse ...
     *
     * Voraussetzung: Aktiver Abstimmungsblock wurde mit leseAktivenAbstimmungsblock() eingelesen.
     */
    public int liefereLfdAbstimmungsNrZuStimmkartenNrUndPosition(int stimmkartenNr, int stimmkartenPosition) {
        int ergebnis = -1;
        for (int i = 0; i < abstimmungenZuAktivenBlock.length; i++) {
            if (abstimmungenZuAktivenBlock[i].nummerDerStimmkarte == stimmkartenNr) {
                if (abstimmungenZuAktivenBlock[i].positionAufStimmkarte == stimmkartenPosition
                        || stimmkartenPosition == -1) {
                    ergebnis = i;
                }
            }
        }
        return ergebnis;
    }

    /**Liefert die zu zur lfd.(gemeint ist: Offset) Abstimmung im Abstimmungsblockarry gehörende
     * Weisungs/Abstimmungsposition
     */
    public int lieferePosInWeisung(int lfd) {
        if (lfd >= abstimmungenZuAktivenBlock.length) {
            return -1;
        }

        return abstimmungenZuAktivenBlock[lfd].identWeisungssatz;
    }

    /**Überprüfen, ob die Abstimmung mit Offset lft im Abstimmungsblockarray eine Überschrift ist*/
    public boolean istUeberschrift(int lfd) {
        if (abstimmungenZuAktivenBlock[lfd].identWeisungssatz == -1) {
            return true;
        } else {
            return false;
        }
    }

    /******************************Funktionen für selektierte Abstimmungen*******************/

    /**pStimmkarte >0 => alle für diese Stimmkarte.
     * Sonst: alle, die eine Position für Tablet-Abstimmung etc. haben*/
    public void selektiereAbstimmungenFuerErfassung(int pStimmkarte, int pGattung) {
        int anzSelektiert = 0;
        if (CaBug.pruefeLog(logDrucken, 10)) {
            CaBug.druckeInfo("BlAbstimmung.selektiereAbstimmungenFuerErfassung abstimmungenZuAktivenBlock.length="
                    + abstimmungenZuAktivenBlock.length);
        }
        for (int i = 0; i < abstimmungenZuAktivenBlock.length; i++) {
            if (pStimmkarte > 0) {/*Bestimmte Stimmkarte wird selektiert*/
                if (abstimmungenZuAktivenBlock[i].nummerDerStimmkarte == pStimmkarte
                        && (pGattung == 0 || abstimmungen[i].stimmberechtigteGattungen[pGattung - 1] == 1)) {
                    anzSelektiert++;
                }
            } else {/*Tablet-Abstimmung wird selektiert*/
                if (abstimmungenZuAktivenBlock[i].position != 0
                        && (pGattung == 0 || abstimmungen[i].stimmberechtigteGattungen[pGattung - 1] == 1)) {
                    anzSelektiert++;
                }
            }
        }

        if (anzSelektiert == 0) {
            selektierteAbstimmungenZuAktivenBlock = null;
            selektierteAbstimmungen = null;
            return;
        }

        selektierteAbstimmungenZuAktivenBlock = new EclAbstimmungZuAbstimmungsblock[anzSelektiert];
        selektierteAbstimmungen = new EclAbstimmung[anzSelektiert];

        int offset = 0;
        for (int i = 0; i < abstimmungenZuAktivenBlock.length; i++) {
            if (pStimmkarte > 0) {/*Bestimmte Stimmkarte wird selektiert*/
                if (abstimmungenZuAktivenBlock[i].nummerDerStimmkarte == pStimmkarte
                        && (pGattung == 0 || abstimmungen[i].stimmberechtigteGattungen[pGattung - 1] == 1)) {
                    selektierteAbstimmungenZuAktivenBlock[offset] = abstimmungenZuAktivenBlock[i];
                    selektierteAbstimmungen[offset] = abstimmungen[i];
                    offset++;
                }
            } else {/*Tablet-Abstimmung wird selektiert*/
                if (abstimmungenZuAktivenBlock[i].position != 0
                        && (pGattung == 0 || abstimmungen[i].stimmberechtigteGattungen[pGattung - 1] == 1)) {
                    selektierteAbstimmungenZuAktivenBlock[offset] = abstimmungenZuAktivenBlock[i];
                    selektierteAbstimmungen[offset] = abstimmungen[i];
                    offset++;
                }
            }
        }

    }

    /**Liefert Anzahl der Abstimmungen in aktivemAbstimmungsblock. Dieser muß vorher mit
     * leseAktivenAbstimmungsblock() eingelesen worden sein.
     */
    public int getAnzAbstimmungenInSelektiertenAbstimmungen() {
        if (selektierteAbstimmungenZuAktivenBlock == null) {
            return 0;
        }
        return selektierteAbstimmungenZuAktivenBlock.length;
    }

    /**Liefert die zu zur lfd.(gemeint ist: Offset) Abstimmung im selektierteAbstimmungenZuAktivenBlock gehörende
     * Weisungs/Abstimmungsposition
     */
    public int lieferePosInSelektierterWeisung(int lfd) {
        return selektierteAbstimmungenZuAktivenBlock[lfd].identWeisungssatz;
    }

    /**Überprüfen, ob die Abstimmung mit Offset lft im selektierteAbstimmungenZuAktivenBlock eine Überschrift ist*/
    public boolean istUeberschriftSelektierteAbstimmung(int lfd) {
        if (selektierteAbstimmungenZuAktivenBlock[lfd].identWeisungssatz == -1) {
            return true;
        } else {
            return false;
        }
    }

    /*******************Abstimmungsvorschlag der Gesellschaft einlesen****************/
    /**Einlesen Abstimmungsvorschlag der Gesellschaft; Ist in SammelkarteSRVInternet hinterlegt.
     * Wird in abstimmungsVorschlagDerGesellschaft hinterlegt.
     * lDbBundle muß vorher geöffnet sei
     */
    public boolean leseAbstimmungsVorschlagDerGesellschaft() {
        /*TODO #Konsolidierung - Vorschlag der Gesellschaft funktioniert noch nicht für Sammelkarten*/

        abstimmungsVorschlagDerGesellschaft = null;

        lDbBundle.dbMeldungen.leseSammelkarteSRVInternet(
                1); /*Sammelkartennr für Vollmacht/Weisung ermitteln - enthält auch den Abstimmungsvorschlag der Gesellschaft*/
        if (lDbBundle.dbMeldungen.meldungenArray.length > 0) {
            lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent);

            if (lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray.length > 0) { /*Abstimmungsvorschlag vorhanden*/
                abstimmungsVorschlagDerGesellschaft = lDbBundle.dbAbstimmungsVorschlag.abstimmungsVorschlagArray[0];
            }
        }

        return true;
    }

    /**********************Abstimmungsverhalten zu meldung**************************************/

    /**Einlesen der bisherigen/aktuellen Abstimmung einer Meldung in
     * bisherigesAbstimmverhalten
     */
    public boolean liefereAktuelleAbstimmungZuMeldeIdent(int meldeIdent) {
        int rc = lDbBundle.dbAbstimmungMeldung.lese_Ident(meldeIdent);
        if (rc < 1) {
            bisherigesAbstimmverhalten = null;
            return false;
        }
        bisherigesAbstimmverhalten = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
        return true;
    }

    /**Prüft, ob für die Sammelkarte meldeIdent Weisungen für diesen Abstimmungsblock vorliegen.
     * Wenn ja, dann true, sonst false
     * Weisungen sind Ja, Nein, Enthaltung
     */
    public boolean pruefeObWeisungenZuSammelkarte(int meldeIdent) {

        lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(meldeIdent, false);
        if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() == 0) {
            return false;
        }

        EclWeisungMeldungSplit aktuelleWeisungMeldungSplit = lDbBundle.dbWeisungMeldung
                .weisungMeldungGefunden(0).weisungMeldungSplit;
        for (int i = 0; i < abstimmungen.length; i++) {
            int pos = abstimmungen[i].identWeisungssatz;
            if (pos != -1) {
                if (aktuelleWeisungMeldungSplit.abgabe[pos][KonstStimmart.ja] > 0) {
                    return true;
                }
                if (aktuelleWeisungMeldungSplit.abgabe[pos][KonstStimmart.nein] > 0) {
                    return true;
                }
                if (aktuelleWeisungMeldungSplit.abgabe[pos][KonstStimmart.enthaltung] > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int aktuellerAbgabewegZuAbstimmungsPosition=0;
    /**Liefert aus  übergebenen Abstimmverhalten das Abstimmungsverhalten zu einer einzelnen
     * Abstimmung abstimmungenZuAktivenBlock[lfd].
     * Belegt außerdem aktuellerAbgabewegZuAbstimmungPosition.
     */
    public int liefereAktuelleMarkierungZuAbstimmungsPosition(int lfd, EclAbstimmungMeldung pAbstimmungMeldung) {
        int abstimmverhalten = 0;

        if (pAbstimmungMeldung == null) {
            return -1;
        }

        int abstimmposition = lieferePosInWeisung(lfd);
        if (abstimmposition == -1) {
            return -1;
        }

        abstimmverhalten = pAbstimmungMeldung.abgabe[abstimmposition];
        aktuellerAbgabewegZuAbstimmungsPosition=pAbstimmungMeldung.abstimmungsweg[abstimmposition];
        return abstimmverhalten;
    }

    /**Liefert aus  übergebenen Abstimmverhalten das Abstimmungsverhalten zu einer einzelnen
     * Abstimmung abstimmungenZuAktivenBlock[lfd].
     */
    public int liefereAktuelleMarkierungZuSelektierterAbstimmungsPosition(int lfd,
            EclAbstimmungMeldung pAbstimmungMeldung) {
        int abstimmverhalten = 0;

        if (pAbstimmungMeldung == null) {
            return -1;
        }

        int abstimmposition = lieferePosInSelektierterWeisung(lfd);
        if (abstimmposition == -1) {
            return -1;
        }

        abstimmverhalten = pAbstimmungMeldung.abgabe[abstimmposition];
        return abstimmverhalten;
    }

    /**Setze Stimmen in Splitposition in Split-Satz**/
    public int setzeStimmartInSplitSatz(int lfd, EclAbstimmungMeldungSplit pAbstimmungMeldungSplit, int stimmart,
            long stimmen) {
        pAbstimmungMeldungSplit.abgabe[lieferePosInWeisung(lfd)][stimmart] = stimmen;
        return 1;
    }

    /**Liefert aus dem bisherigen Abstimmverhalten bisherigesAbstimmverhalten das Abstimmungsverhalten zu
     * abstimmungenZuAktivenBlock[lfd]
     */
    public int liefereAktuelleMarkierungZuAbstimmungsPosition(int lfd) {
        return liefereAktuelleMarkierungZuAbstimmungsPosition(lfd, bisherigesAbstimmverhalten);
    }

    /**Liefert das neue Abstimmungsverhalten zurück, das aus dem aktuellen Abstimmungsverhalten und der neuen
     * Stimmabgabe neues Abstimmungsverhalten
     * @param pAktuellesAbstimmungsverhalten
     * @param pNeuesAbstimmungsverhalten
     * @return
     */
    private int liefereNeuesAbstimmungsverhalten(int pAktuellesAbstimmungsverhalten, int pNeueStimmabgabe) {
        int neuesAbstimmungsverhalten = pNeueStimmabgabe;
        
        if (pNeueStimmabgabe==KonstStimmart.nichtAufMedium) {
            neuesAbstimmungsverhalten=pAktuellesAbstimmungsverhalten;
        }

        if (lDbBundle.param.paramAbstimmungParameter.beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig == 2) {
            /* Bei "ungleicheUngueltig" gilt folgendes:
             * 		Wenn die "neue Stimme" E, J, N ist  (und ungleich der bereits gespeicherten Stimme)
             * 		> und die bereits gespeicherte J, N, E, U ist, dann wird es U.
             * 		Ansonsten wird es die neue Stimme  
             */
            if (neuesAbstimmungsverhalten != pAktuellesAbstimmungsverhalten) {
                if ((neuesAbstimmungsverhalten == KonstStimmart.ja || neuesAbstimmungsverhalten == KonstStimmart.nein
                        || neuesAbstimmungsverhalten == KonstStimmart.enthaltung)
                        && (pAktuellesAbstimmungsverhalten == KonstStimmart.ja
                                || pAktuellesAbstimmungsverhalten == KonstStimmart.nein
                                || pAktuellesAbstimmungsverhalten == KonstStimmart.enthaltung
                                || pAktuellesAbstimmungsverhalten == KonstStimmart.ungueltig)) {
                    neuesAbstimmungsverhalten = KonstStimmart.ungueltig;
                }
            }
        }

        return neuesAbstimmungsverhalten;
    }

    /**True => die in pAbstimmungMeldungRaw enthaltene Stimmabgabe ist höher priorisiert als die aktuell
     * gespeichert, d.h. die Stimmabgabe aus pAbstimmungMeldungRaw muß anschließend als aktuelle
     * Stimmabgabe übernommen werden.
     */
    private boolean abstimmungRawIstHoeherPriorisiertAlsAbstimmungBereitsGespeichert(
            EclAbstimmungMeldungRaw pAbstimmungMeldungRaw, boolean pInJedemFallSpeichern) {
        if (pInJedemFallSpeichern == true) {
            /*"Forciertes Speichern"*/
            return true;
        }
        if (abstimmungMeldung.erteiltAufWeg == 0) {
            /*in abstimmungMeldung noch nichts eingetragen*/
            return true;
        }
        if (pAbstimmungMeldungRaw.erteiltAufWeg == KonstWillenserklaerungWeg.bedingungslosesSpeichern) {
            /*Raw-Wert muß bedingungslos übernommen werden, d.h. ohne jegliche Priorisierung*/
            return true;
        }
        if (KonstWillenserklaerungWeg
                .getAbstimmwegPapierOderElektronisch(abstimmungMeldung.erteiltAufWeg) == KonstWillenserklaerungWeg
                        .getAbstimmwegPapierOderElektronisch(pAbstimmungMeldungRaw.erteiltAufWeg)) {
            /*Willenserklärungen wurden quasi auf gleichem (äquivalenten) Weg abgegeben - nun zählt Zeitstempel*/
            if (abstimmungMeldung.zeitstempelraw <= pAbstimmungMeldungRaw.zeitstempelraw) {
                return true;
            } else {
                return false;
            }
        }
        /*Unterschiedlicher Abstimmungsweg - nun nach Weg gemäß Parametrisierung priorisieren*/
        if ((KonstWillenserklaerungWeg.getAbstimmwegPapierOderElektronisch(pAbstimmungMeldungRaw.erteiltAufWeg) == 2
                && lDbBundle.param.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch == 1)
                || (KonstWillenserklaerungWeg
                        .getAbstimmwegPapierOderElektronisch(pAbstimmungMeldungRaw.erteiltAufWeg) == 1
                        && lDbBundle.param.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch == 2)) {
            return true;
        } else {
            return false;
        }
    }

    /**Prüft, ob die übergebene ZutrittsIdent abstimmen darf. D.h. konkret:
     * > Ist sie vorhanden?
     * > Ist sie gesperrt?
     * > Verweist sie auf Aktioär?
     * 
     * lDbBundle muß geöffnet sein.
     * 
     * Returncodes:
     * >1 = meldungsIdent
     * pmZutrittsIdentNichtVorhanden
     * pmZutrittsIdentGesperrt
     * pmEintrittskarteVerweistAufGast
     */
    public int pruefeZutrittsIdentZulaessigFuerAbstimmung(EclZutrittsIdent pZutrittsIdent) {
        lDbBundle.dbZutrittskarten.readAktionaer(pZutrittsIdent, 2);
        if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) { /*Eintrittskarte nicht vorhanden*/
            return CaFehler.pmZutrittsIdentNichtVorhanden;
        }

        /*Prüfen - gesperrt?*/
        EclZutrittskarten lZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
        if (lZutrittskarten.zutrittsIdentWurdeGesperrt_Delayed()) {
            return CaFehler.pmZutrittsIdentGesperrt;
        }

        /*Prüfen - verweist die Eintrittskarte auf Aktionär?*/
        if (lZutrittskarten.gueltigeKlasse == 0) {
            return CaFehler.pmEintrittskarteVerweistAufGast;
        }
        return lZutrittskarten.meldungsIdentAktionaer;
    }

    /**Prüft, ob die übergebene Stimmkarte abstimmen darf. D.h. konkret:
     * > Ist sie vorhanden?
     * > Ist sie gesperrt?
     * > Verweist sie auf Aktioär?
     * 
     * lDbBundle muß geöffnet sein.
     * 
     * Returncodes:
     * >1 = meldungsIdent
     * pmStimmkarteNichtVorhanden
     * pmStimmkarteGesperrt
     * pmStimmkarteVerweistAufGast
     */
    public int pruefeStimmkarteZulaessigFuerAbstimmung(String pStimmkartenNr) {
        lDbBundle.dbStimmkarten.read(pStimmkartenNr, 2);
        if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) { /*Stimmkarte nicht vorhanden*/
            return CaFehler.pmStimmkarteNichtVorhanden;
        }

        /*Prüfen - gesperrt?*/
        EclStimmkarten lStimmkarten = lDbBundle.dbStimmkarten.ergebnisPosition(0);
        if (lStimmkarten.stimmkarteIstGesperrt_Delayed == 1) {
            return CaFehler.pmStimmkarteGesperrt;
        }

        /*Prüfen - verweist die Stimmkarte auf Aktionär?*/
        if (lStimmkarten.gueltigeKlasse == 0) {
            return CaFehler.pmStimmkarteVerweistAufGast;
        }
        return lStimmkarten.meldungsIdentAktionaer;
    }

    /**Starten / Initialisieren der Abstimmungsspeicherung (für eine Abstimmungstransaktion) für den Aktionär mit meldeIdent:
     * > Prüfen ob Stimmabgabe möglich (präsent, in Sammelkarte, ...)
     * > "Standardabstimmungssatz" einlesen, bzw. falls noch nicht vorhanden initialisieren
     * > "neuen RawStandardabstimmungssatz" initialisieren
     * @param meldeIdent
     * @return
     */
    public int starteSpeichernFuerMeldung(EclMeldung pMeldung) {

        long zeitstempelraw = 0;

        return starteSpeichernFuerMeldung(pMeldung, zeitstempelraw, true);

    }

    /**Meldung wird vorher eingelesen*/
    public int starteSpeichernFuerMeldungsIdent(int pMeldungsIdent) {
        lDbBundle.dbMeldungen.leseZuIdent(pMeldungsIdent);
        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        return starteSpeichernFuerMeldung(lMeldung);
    }

    public int starteSpeichernFuerMeldung(EclMeldung pMeldung, long zeitstempelraw, boolean pInJedemFallSpeichern) {
        return starteSpeichernFuerMeldung(pMeldung, zeitstempelraw, pInJedemFallSpeichern, 0, 0, "", 1);
    }

    /**
     * Raw-Satz abstimmungMeldungRaw wird auch erzeugt, wenn Stimmabgabe für diese Meldung nicht möglich ist.
     * Abhängig von abstimmungMeldungRaw.wurdeVerarbeitet wird in den Folge-Funktionen dann die
     * aktuelle Abstimmung gespeichert oder nicht.
     * 
     * Priorisierung:
     * Wenn pInJedemFallSpeichern = true, dann erfolgt keine Priorisierung - die Abstimmung wird auf jeden Fall aktuell gewertet.
     * Ansonsten:
     * Gemäß priorisierung_ElektronischVorPapier_PapierVorElektronisch, bzw. bei gleichem Weg: Zeitstempel
     * 
     * Parameter
     * pMeldung - kann null sein, dann muß aber auch pFehlercode gefüllt sein - in diesem Fall nur Raw-Satz, keine Zuordnung zu einer meldung.
     * pZeitstempelraw = 0, dann wird aktuelle Zeit gespeichert, sonst pZeitstempelraw 
     * pInJedemFallSpeichern = true, dann wird die übergebene Abstimmung unabhängig vom Zeitstempelvergleich immer gespeichert. Wichtig:
     * 		In diesem Fall wird erteiltAufWeg automatisch auf bedingungslosesSpeichern gesetzt!
     * pFehlercode < 1 => nur raw-Satz wird erstellt, und Fehlercode wird mitgespeichert.
     * 
     * Rückgabe:
     * 1 ok, Stimmabgabe möglich
     * pmInSammelkarteEnthalten
     * pmNichtPraesent
     */
    public int starteSpeichernFuerMeldung(EclMeldung pMeldung, long pZeitstempelraw, boolean pInJedemFallSpeichern,
            int pErteiltAufWeg, int pVerwendeteKartenklasse, String pVerwendeteNummer, int pFehlercode) {
        long zeitstempelrawFuerDieseStimmabgabe = 0;
        speichernWgPriorisierung = true;
        int rcFunktion = 1; /*Return-Wert*/

        /*Zeitstempel für spätere Speicherung erzeugen*/
        if (pZeitstempelraw != 0) {
            zeitstempelrawFuerDieseStimmabgabe = pZeitstempelraw;
        } else {
            zeitstempelrawFuerDieseStimmabgabe = CaDatumZeit.zeitStempelMS();
        }

        /*Meldung zwischenspeichern, für die die Abstimmung erfolgt*/
        meldung = pMeldung;

        /*Auf jeden Fall neuen Raw-Satz anlegen*/
        abstimmungMeldungRaw = new EclAbstimmungMeldungRaw();
        if (pMeldung != null) {
            abstimmungMeldungRaw.meldungsIdent = pMeldung.meldungsIdent;
        } else {
            abstimmungMeldungRaw.meldungsIdent = 0;
        }
        CaBug.druckeLog("aktiverAbstimmungsblock.ident=" + aktiverAbstimmungsblock.ident, logDrucken, 10);
        abstimmungMeldungRaw.abstimmungsblock = aktiverAbstimmungsblock.ident;
        abstimmungMeldungRaw.arbeitsplatzNr = lDbBundle.clGlobalVar.arbeitsplatz;
        abstimmungMeldungRaw.zeitstempel = CaDatumZeit.DatumZeitStringFuerDatenbank();
        abstimmungMeldungRaw.zeitstempelraw = zeitstempelrawFuerDieseStimmabgabe;
        abstimmungMeldungRaw.erteiltAufWeg = pErteiltAufWeg;
        if (pInJedemFallSpeichern) {
            abstimmungMeldungRaw.erteiltAufWeg = KonstWillenserklaerungWeg.bedingungslosesSpeichern;
        }
        abstimmungMeldungRaw.verwendeteKartenklasse = pVerwendeteKartenklasse;
        abstimmungMeldungRaw.verwendeteNummer = pVerwendeteNummer;

        /*Abstimmungssatz nur, falls auch Abstimmung "aktiv abgegeben" werden kann (also präsent oder präsent gewesen je nach Parameter; nicht in Sammelkarte; fehlercode==1*/
        if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldung)==true
                && meldung.meldungstyp != 3 && pFehlercode == 1) {
            /*Falls vorhanden: Abstimmungssatz einlesen, sonst neuen Satz als "leer" anlegen*/
            initAbstimmungMeldung(zeitstempelrawFuerDieseStimmabgabe, pErteiltAufWeg, pInJedemFallSpeichern);
            abstimmungMeldungRaw.wurdeVerarbeitet = 1;
        } else {
            abstimmungMeldungRaw.wurdeVerarbeitet = 0;
            if (pFehlercode == 1) {
                if (meldung.meldungstyp == 3) {
                    rcFunktion = CaFehler.pmInSammelkarteEnthalten;
                } else {
                    rcFunktion = CaFehler.pmNichtPraesent;
                }
                pFehlercode = rcFunktion;
            }
            abstimmungMeldungRaw.fehlercode = pFehlercode;
        }

        return rcFunktion;
    }

    /**Setzt als neues zusätzliches Abstimmverhalten das Abstimmungsverhalten zu
     * abstimmungenZuAktivenBlock[lfd]
     * 
     * Return-Wert:
     * 1 ok
     * -1 technischer Fehler
     * pmDoppeltNunUngueltig
     * pmGattungNichtStimmberechtigt
     */
    public int setzeMarkierungZuAbstimmungsPosition(int abstimmungsverhalten, int lfd, int pWeg) {
        int rcFunktion = 1;

        int abstimmposition = lieferePosInWeisung(lfd);
        if (abstimmposition == -1) {
            /*TODO _Abstimmung - erst mal deaktiviert, da das immer kommt ... evtl. bereits beim Aufruf von setzeMarkierung für Überschriftszeile checken*/
            //			CaBug.drucke("BlAbstimmung.setzeMarkierungZuAbstimmungsPosition 001");
            return -1;
        }

        int neuesAbstimmungsverhalten = abstimmungsverhalten;
        int altesAbstimmungsverhalten = 0;

        /*Raw immer einfügen*/
        abstimmungMeldungRaw.abgabe[abstimmposition] = abstimmungsverhalten;

        if (speichernWgPriorisierung && abstimmungMeldungRaw.wurdeVerarbeitet == 1) {
            if (meldung != null && meldung.gattung != 0
                    && abstimmungen[lfd].stimmberechtigteGattungen[meldung.gattung - 1] != 1) {
                rcFunktion = CaFehler.pmGattungNichtStimmberechtigt;
                /*wurdeVerarbeitet bleibt auf 1: kann ja sein, dass bei Markierungen nur teile nicht stimmberechtigt sind!*/
                abstimmungMeldungRaw.fehlercode = rcFunktion;
                return rcFunktion;
            }

            altesAbstimmungsverhalten = abstimmungMeldung.abgabe[abstimmposition];
            /*Abstimmungssatz nur, wenn die Abstimmung auch möglich ist*/

            neuesAbstimmungsverhalten = liefereNeuesAbstimmungsverhalten(altesAbstimmungsverhalten,
                    abstimmungsverhalten);
            if (neuesAbstimmungsverhalten == KonstStimmart.ungueltig
                    && abstimmungsverhalten != KonstStimmart.ungueltig) {
                rcFunktion = CaFehler.pmDoppeltNunUngueltig;
            }
            abstimmungMeldung.setzeStimmabgabeAktivAbgegeben(abstimmposition, neuesAbstimmungsverhalten, pWeg);
        }
        return rcFunktion;
    }

    private int liefereMarkierungZuAbstimmungsPosition(int lfd) {
        int abstimmposition = lieferePosInWeisung(lfd);
        return abstimmungMeldung.abgabe[abstimmposition];
       
    }
    /**Setzt als neues zusätzliches Abstimmverhalten das Abstimmungsverhalten zu
     * abstimmungenZuAktivenBlock zu der Abstimmung, die die Ident ident hat
     */
    public int setzeMarkierungZuAbstimmungsIdent(int abstimmungsverhalten, int ident, int pWeg) {

        int lfd = -1;
        for (int i = 0; i < abstimmungen.length; i++) {
            if (abstimmungen[i].ident == ident) {
                lfd = i;
            }
        }
        if (lfd == -1) {
            CaBug.drucke("BlAbstimmung.setzeMarkierungZuAbstimmungsIdent 001");
            return -1;
        }

        return setzeMarkierungZuAbstimmungsPosition(abstimmungsverhalten, lfd, pWeg);
    }

    /**Returnwert = gespeicherter RAW-Wert, sonst 0;*/
    public int beendeSpeichernFuerMeldung() {
        int identRaw = 0;
        /*Raw Satz speichern*/
        lDbBundle.dbAbstimmungMeldungRaw.insert(abstimmungMeldungRaw);
        identRaw = abstimmungMeldungRaw.ident;

        /*Abstimmungssatz nur, falls auch Abstimmung "aktiv abgegeben" werden kann*/
        if (abstimmungMeldungRaw.wurdeVerarbeitet == 1 && speichernWgPriorisierung) {
            /*Abstimmungssatz updaten - ist ja auf jeden Fall schon da*/
            lDbBundle.dbAbstimmungMeldung.update(abstimmungMeldung);
        }

        return identRaw;
    }

    
    /********************************Durchführen Markierungssetzen für implizite Stimmabgabe****************************************
     * Implizit bedeutet: Stimme nicht direkt abgegeben, sondern über Sammelkarte, o.ä.
     * D.h: Raw wird nicht gespeichert.
     * EclAbstimmungMeldung wird nur dann in das Table geschrieben, wenn Veränderungen stattgefunden haben.
     */
    private EclAbstimmungMeldung abstimmungMeldungImplizit=null;
    private boolean abstimmungMeldungImplizitWurdeVeraendert=false;
    private boolean abstimmungMeldungImplizitNeuanlage=false;
    
    private int starteSpeichernFuerMeldungImplizit(EclMeldung pMeldung) {
        int rc=lDbBundle.dbAbstimmungMeldung.lese_Ident(pMeldung.meldungsIdent);
        if (rc==0) {
            abstimmungMeldungImplizit=new EclAbstimmungMeldung();
            abstimmungMeldungImplizit.meldungsIdent=pMeldung.meldungsIdent;
            abstimmungMeldungImplizit.stimmen=liefereMeldungStimmenGgfMitHoechststimmrecht(pMeldung);
            abstimmungMeldungImplizit.gattung=pMeldung.liefereGattung();
            abstimmungMeldungImplizit.aktiv=1;
            abstimmungMeldungImplizitNeuanlage=true;
        }
        else {
            abstimmungMeldungImplizit=lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
            abstimmungMeldungImplizitNeuanlage=false;
        }
        abstimmungMeldungImplizitWurdeVeraendert=false;
        return 1;
    }
    
    private boolean liefereMarkierungZuAbstimmungsPositionVorhandenImmplizit(int pLfd) {
        if (abstimmungMeldungImplizit.abgabe[pLfd]!=0) {
            return true;
        }
        return false;
    }
    
    private int liefereMarkierungZuAbstimmungsPositionImmplizit(int pLfd) {
        return abstimmungMeldungImplizit.abgabe[pLfd];
     }

    private int setzeMarkierungZuAbstimmungsPositionImplizit(int pAbstimmungsverhalten, int pLfd, int pWeg, int pSammelkarte) {
        abstimmungMeldungImplizit.abgabe[pLfd]=pAbstimmungsverhalten;
        abstimmungMeldungImplizit.abstimmungsweg[pLfd]=pWeg;
        abstimmungMeldungImplizit.abstimmungDurchSammelkarte[pLfd]=pSammelkarte;
        
        abstimmungMeldungImplizitWurdeVeraendert=true;
        return 1;
    }
    
    private int beendeSpeichernFuerMeldungImplizit() {
        if (abstimmungMeldungImplizitWurdeVeraendert==true) {
            if (abstimmungMeldungImplizitNeuanlage) {
                lDbBundle.dbAbstimmungMeldung.insert(abstimmungMeldungImplizit);
            }
            else {
                lDbBundle.dbAbstimmungMeldung.update(abstimmungMeldungImplizit);
            }
        }
        return 1;
    }
    
    /**Ermittelt - aus den RAW-Datensätzen - das aktuelle Abstimmverhalten neu und speichert dieses.
     * DBBundle muß geöffnet sein.
     * Der zu bearbeitende Abstimmungsblock muß eingelesen sein
     * 
     * Wichtiger Hinweis: bei Abstimmung in mehreren Blöcken hintereinander gibts Schwierigkeiten!
     * Denn: Zeitstempel/Weg gelten für alle Blöcke gemeinsam! D.h. konkret:
     * > Wenn diese Funktion für einen Block A durchgeführt wird, während schon Block B
     * 		eingelesen oder verarbeitet wird, dann kommt Block B durcheinander!
     * D.h.: wenn die Abstimmung für einen Block A neu ermittelt wird, dann muß (soweit schon aktiv)
     * auch der Block B für diese Meldung neu ermittelt werden. Leider kann das Programm das nicht
     * riechen - d.h. das muß manuell erfolgen ...*/
    public int ermittleAktuelleAbstimmungNeu(EclMeldung pMeldung) {

        /*Lesen und Zurücksetzen des aktuellen Abstimmungsverhaltens*/
        boolean rcBool = this.liefereAktuelleAbstimmungZuMeldeIdent(pMeldung.meldungsIdent);
        if (rcBool == false) {/**Dann noch kein Abstimmungssatz vorhanden - anlegen*/
            abstimmungMeldung = new EclAbstimmungMeldung();
            abstimmungMeldung.meldungsIdent = pMeldung.meldungsIdent;
            abstimmungMeldung.gattung=pMeldung.liefereGattung();
            abstimmungMeldung.stimmen = liefereMeldungStimmenGgfMitHoechststimmrecht(pMeldung);
            abstimmungMeldung.aktiv = 1;
            abstimmungMeldung.zeitstempelraw = 0;
            lDbBundle.dbAbstimmungMeldung.insert(abstimmungMeldung);
        } else {
            /*Abstimmungssatz war bereits vorhanden - die Abstimmungen des aktullen Abstimmungsblockes zurücksetzen*/
            abstimmungMeldung = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
            for (int i = 0; i < this.getAnzAbstimmungenInAktivemAbstimmungsblock(); i++) {
                int position = this.lieferePosInWeisung(i);
                if (position != -1) {
                    abstimmungMeldung.zuruecksetzenStimmabgabe(position);
                }
            }
            abstimmungMeldung.zeitstempelraw = 0;
            abstimmungMeldung.erteiltAufWeg = 0;
            abstimmungMeldung.stimmen = liefereMeldungStimmenGgfMitHoechststimmrecht(pMeldung);
        }

        /*Lesen der RAW-Dateien zu diesem Abstimmungsblock zu dieser Meldung*/
        lDbBundle.dbAbstimmungMeldungRaw.lese_MeldungsIdent(pMeldung.meldungsIdent, this.aktiverAbstimmungsblock.ident);
        EclAbstimmungMeldungRaw[] lAbstimmungenMeldungRaw = lDbBundle.dbAbstimmungMeldungRaw.ergebnisArray;

        if (lAbstimmungenMeldungRaw != null && lAbstimmungenMeldungRaw.length > 0) {
            /*Durcharbeiten aller RAW-Datensätze. Stornierte und Nicht-Verarbeitet werden nicht berücksichtigt!*/
            for (int i = 0; i < lAbstimmungenMeldungRaw.length; i++) {
                if (lAbstimmungenMeldungRaw[i].wurdeStorniert != 1
                        && lAbstimmungenMeldungRaw[i].wurdeVerarbeitet == 1) {
                    /*Nun prüfen, ob wg. Zeitstempel/Abstimmungspriorisierung der Raw-Satz überhaupt übertragen werden soll*/
                    if (abstimmungRawIstHoeherPriorisiertAlsAbstimmungBereitsGespeichert(lAbstimmungenMeldungRaw[i],
                            false)) {

                        /*Durcharbeiten aller Abstimmungen dieses Abstimmungsblockes und aktualisieren des aktuellen Abstimmungsverhaltens*/
                        for (int i1 = 0; i1 < this.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                            int position = this.lieferePosInWeisung(i1);
                            if (position != -1) {
                                int abstimmungsverhalten = lAbstimmungenMeldungRaw[i].abgabe[position];
                                int neuesAbstimmungsverhalten = abstimmungsverhalten;
                                int altesAbstimmungsverhalten = 0;
                                if (abstimmungMeldung != null) {/*War vorher nicht drin - nötig?*/
                                    abstimmungMeldung.zeitstempelraw = lAbstimmungenMeldungRaw[i].zeitstempelraw;
                                    abstimmungMeldung.erteiltAufWeg = lAbstimmungenMeldungRaw[i].erteiltAufWeg;
                                }
                                if (abstimmungMeldung != null && ((pMeldung.gattung != 0
                                        && abstimmungen[i1].stimmberechtigteGattungen[pMeldung.gattung - 1] == 1)
                                        || pMeldung.gattung == 0)) {
                                    altesAbstimmungsverhalten = abstimmungMeldung.abgabe[position];
                                    neuesAbstimmungsverhalten = liefereNeuesAbstimmungsverhalten(
                                            altesAbstimmungsverhalten, abstimmungsverhalten);
                                    if (neuesAbstimmungsverhalten == KonstStimmart.ungueltig
                                            && abstimmungsverhalten != KonstStimmart.ungueltig) {
                                    }

                                    abstimmungMeldung.setzeStimmabgabeAktivAbgegeben(position, neuesAbstimmungsverhalten, abstimmungMeldung.erteiltAufWeg);
                                }
                            }
                        }
                    }
                }
            }
        }

        /*Speichern des aktuellen Abstimmungsverhaltens*/
        /*Abstimmungssatz updaten - ist ja auf jeden Fall schon da*/
        lDbBundle.dbAbstimmungMeldung.update(abstimmungMeldung);

        return 1;
    }

    private EclAbstimmungMeldungRaw[] fehlerhafteAbstimmungMeldungRaw = null;
    private EclMeldung meldungZumUeberpruefen = null;;

    /**Sub-Routine für arbeiteFehlerDurchUndUeberpruefe.
     * Überprüfen:
     * > Meldung in Sammelkarte?
     * > Meldung präsent?
     * > Gattung stimmberechtigt?
     * 
     * Ergebnis: meldungZumUeberpruefen ist gefüllt (oder weiter null)
     * Fehlercode oder 1.
     * Fehlercodes:
     * pmZutrittsIdentNichtVorhanden (darf eigentlich nicht auftreten!!)
     * pmInSammelkarteEnthalten
     * pmNichtPraesent
     * pmGattungNichtStimmberechtigt
     */
    private int pruefeMeldung(int pMeldungsIdent, EclAbstimmungMeldungRaw pAbstimmungMeldungRaw) {
        meldungZumUeberpruefen = new EclMeldung();
        meldungZumUeberpruefen.meldungsIdent = pMeldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(meldungZumUeberpruefen);
        if (lDbBundle.dbMeldungen.meldungenArray.length == 0) {
            CaBug.drucke("BlAbstimmung.pruefeMeldung 001");
            meldungZumUeberpruefen = null;
            return CaFehler.pmZutrittsIdentNichtVorhanden;
        }
        meldungZumUeberpruefen = lDbBundle.dbMeldungen.meldungenArray[0];

        /*Meldung in Sammelkarte?*/
        if (meldungZumUeberpruefen.meldungstyp == 3) {
            return CaFehler.pmInSammelkarteEnthalten;
        }

        /*Meldung präsent für Abstimmung?*/
        if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungZumUeberpruefen)==true) {
            /*Dann verarbeitbar*/
        } else {
            return CaFehler.pmNichtPraesent;
        }

        /*Gattung stimmberechtigt?*/
        for (int i1 = 0; i1 < this.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
            int position = this.lieferePosInWeisung(i1);
            if (position != -1) {
                if (pAbstimmungMeldungRaw.abgabe[position] != KonstStimmart.nichtMarkiert
                        && pAbstimmungMeldungRaw.abgabe[position] != KonstStimmart.nichtAufMedium
                        && meldungZumUeberpruefen.gattung != 0
                        && abstimmungen[i1].stimmberechtigteGattungen[meldungZumUeberpruefen.gattung - 1] != 1) {
                    return CaFehler.pmGattungNichtStimmberechtigt;
                }
            }
        }
        return 1;
    }

    /**Arbeitet alle Raw-Felder des Abstimmungsblocks mit Fehlercodes durch und versucht diese, erneut
     * zu verarbeiten. Gelingt dies, wird die aktuelle Abstimmung der jeweiligen Meldung "neu 
     * durchforstet".
     * 
     * lDbBundle muß geöffnet sein*/
    public int arbeiteFehlerDurchUndUeberpruefe() {
        /*Alle fehlerhaften RAW-Sätze zum Abstimmungsblock einlesen*/
        lDbBundle.dbAbstimmungMeldungRaw.lese_allNichtVerarbeitet(aktiverAbstimmungsblock.ident);
        fehlerhafteAbstimmungMeldungRaw = lDbBundle.dbAbstimmungMeldungRaw.ergebnisArray;

        /*alle fehlerhaften Sätze durcharbeiten*/
        int anzFehlerhafteAbstimmungMeldungRaw = 0;
        if (fehlerhafteAbstimmungMeldungRaw != null) {
            anzFehlerhafteAbstimmungMeldungRaw = fehlerhafteAbstimmungMeldungRaw.length;
        }
        for (int i = 0; i < anzFehlerhafteAbstimmungMeldungRaw; i++) {
            /*Falls zuUeberarbeitendeMeldungsIdent nach der Analyse >0, dann die dazugehörenden Abstimmungen neu durcharbeiten*/
            int zuUeberarbeitendeMeldungsIdent = 0;
            /*Abhängig von Fehlerart: überprüfen, ob Fehler immer noch auftritt; wenn nein, dann Raws des entsprechenden Meldungsatzes durcharbeiten*/
            EclAbstimmungMeldungRaw lFehlerhafteAbstimmungMeldungRaw = fehlerhafteAbstimmungMeldungRaw[i];
            switch (lFehlerhafteAbstimmungMeldungRaw.fehlercode) {
            case CaFehler.pmZutrittsIdentNichtVorhanden:
            case CaFehler.pmZutrittsIdentGesperrt:
            case CaFehler.pmEintrittskarteVerweistAufGast:
            case CaFehler.pmStimmkarteNichtVorhanden:
            case CaFehler.pmStimmkarteGesperrt:
            case CaFehler.pmStimmkarteVerweistAufGast: {
                int rcEK = 0;
                if (lFehlerhafteAbstimmungMeldungRaw.fehlercode == CaFehler.pmZutrittsIdentNichtVorhanden
                        || lFehlerhafteAbstimmungMeldungRaw.fehlercode == CaFehler.pmZutrittsIdentGesperrt
                        || lFehlerhafteAbstimmungMeldungRaw.fehlercode == CaFehler.pmEintrittskarteVerweistAufGast) {
                    String zutrittsIdent = lFehlerhafteAbstimmungMeldungRaw.verwendeteNummer.replace('-', ' ');
                    BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
                    blNummernformen.dekodiere(zutrittsIdent, 0);
                    EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
                    lZutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
                    lZutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);
                    rcEK = pruefeZutrittsIdentZulaessigFuerAbstimmung(lZutrittsIdent);
                } else { /*Stimmkarten-Fehler*/
                    String stimmkartenNr = lFehlerhafteAbstimmungMeldungRaw.verwendeteNummer;
                    rcEK = pruefeStimmkarteZulaessigFuerAbstimmung(stimmkartenNr);
                }

                if (rcEK < 1) {
                    /*Immer noch Fehler vorhanden (aber anderer) - unkorrigierbar*/
                    if (rcEK != lFehlerhafteAbstimmungMeldungRaw.fehlercode) {
                        /*Anderer Fehler - deshalb updaten*/
                        lFehlerhafteAbstimmungMeldungRaw.fehlercode = rcEK;
                        lDbBundle.dbAbstimmungMeldungRaw
                                .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                    }
                    zuUeberarbeitendeMeldungsIdent = -1;
                } else {
                    zuUeberarbeitendeMeldungsIdent = rcEK; /*MeldungsIdent ist auf jeden Fall jetzt schon identifiziert, und wird dann upgedated*/
                    int rcPM = pruefeMeldung(zuUeberarbeitendeMeldungsIdent, lFehlerhafteAbstimmungMeldungRaw);
                    switch (rcPM) {
                    case 1: {
                        lFehlerhafteAbstimmungMeldungRaw.fehlercode = 0;
                        lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 1;
                        lFehlerhafteAbstimmungMeldungRaw.meldungsIdent = zuUeberarbeitendeMeldungsIdent;
                        lDbBundle.dbAbstimmungMeldungRaw
                                .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                        break;
                    }
                    case CaFehler.pmGattungNichtStimmberechtigt: {/*Fehlercode schreiben, aber verarbeiten!*/
                        lFehlerhafteAbstimmungMeldungRaw.fehlercode = CaFehler.pmGattungNichtStimmberechtigt;
                        lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 1;
                        lFehlerhafteAbstimmungMeldungRaw.meldungsIdent = zuUeberarbeitendeMeldungsIdent;
                        lDbBundle.dbAbstimmungMeldungRaw
                                .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                        break;
                    }
                    default: {/*Fehlercode, nicht verarbeiten*/
                        lFehlerhafteAbstimmungMeldungRaw.fehlercode = rcPM;
                        lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 0;
                        lFehlerhafteAbstimmungMeldungRaw.meldungsIdent = zuUeberarbeitendeMeldungsIdent;
                        lDbBundle.dbAbstimmungMeldungRaw
                                .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                        zuUeberarbeitendeMeldungsIdent = -1;
                        break;
                    }
                    }
                }
                break;
            }
            case CaFehler.pmInSammelkarteEnthalten:
            case CaFehler.pmNichtPraesent:
            case CaFehler.pmGattungNichtStimmberechtigt: {
                zuUeberarbeitendeMeldungsIdent = lFehlerhafteAbstimmungMeldungRaw.meldungsIdent;
                int rcPM = pruefeMeldung(zuUeberarbeitendeMeldungsIdent, lFehlerhafteAbstimmungMeldungRaw);
                switch (rcPM) {
                case 1: {
                    lFehlerhafteAbstimmungMeldungRaw.fehlercode = 1;
                    lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 1;
                    lDbBundle.dbAbstimmungMeldungRaw
                            .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                    break;
                }
                case CaFehler.pmGattungNichtStimmberechtigt: {/*Fehlercode schreiben, aber verarbeiten!*/
                    lFehlerhafteAbstimmungMeldungRaw.fehlercode = CaFehler.pmGattungNichtStimmberechtigt;
                    lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 1;
                    lDbBundle.dbAbstimmungMeldungRaw
                            .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                    break;
                }
                default: {/*Fehlercode, nicht verarbeiten*/
                    lFehlerhafteAbstimmungMeldungRaw.fehlercode = rcPM;
                    lFehlerhafteAbstimmungMeldungRaw.wurdeVerarbeitet = 0;
                    lDbBundle.dbAbstimmungMeldungRaw
                            .updateVerwendetFehlerCodeMeldungsIdent(lFehlerhafteAbstimmungMeldungRaw);
                    zuUeberarbeitendeMeldungsIdent = -1;
                    break;
                }
                }
                break;
            }
            }

            if (zuUeberarbeitendeMeldungsIdent > 0) {
                /*Bei der vorherigen Analyse des fehlerhaften Raw-Satzes wurde festgestellt, dass der Raw-Satz jetzt verarbeitbar ist.
                 * Das jetzt hier durchführen.*/
                lDbBundle.dbMeldungen.leseZuIdent(zuUeberarbeitendeMeldungsIdent);
                EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                ermittleAktuelleAbstimmungNeu(lMeldung);
            }
        }
        return 1;
    }

    /*************************Ab hier: Funktionen für Abstimmungsauswerten******************************************************/

    /**Für eine Meldung ..... in der Sammelkarte ..... wird für die Abstimmungsposition ..... Stimmausschluß gesetzt*/
    private void setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition(
            /*Daten des Einzelaktionärs*/
            EclMeldung meldungStimmausschluss, EclWeisungMeldung weisungStimmausschluss,
            /*Sammelkarte*/
            EclWeisungMeldungSplit aktuelleWeisungSammelkarteSplit,
            EclAbstimmungMeldung eclAbstimmungGeworfenSammelkarte, //kann null sein, dann nichts geworfen! 
            EclAbstimmungMeldungSplit sammelkarteAbstimmungSplit,
            /*Sonstige Parameter*/
            int abstimmposition) {
        boolean geworfen = false;
        if (eclAbstimmungGeworfenSammelkarte != null) {
            int geworfeneStimmabgabe = eclAbstimmungGeworfenSammelkarte.abgabe[abstimmposition];
                    /*liefereAktuelleMarkierungZuAbstimmungsPosition(abstimmposition,
                    eclAbstimmungGeworfenSammelkarte);*/
            CaBug.druckeLog("setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition abstimmposition="+abstimmposition+" geworfeneStimmabgabe="
                    + geworfeneStimmabgabe, logDrucken, 10);
            if (geworfeneStimmabgabe == KonstStimmart.ja || geworfeneStimmabgabe == KonstStimmart.nein
                    || geworfeneStimmabgabe == KonstStimmart.enthaltung
                    || geworfeneStimmabgabe == KonstStimmart.ungueltig) {
                /*Für diese Stimmausschlußposition liegt eine geworfene Stimmabgabe vor, mit der Art geworfeneStimmabgabe. Nun Weisungen entsprechend korrigieren*/
                sammelkarteAbstimmungSplit.abgabe[abstimmposition][geworfeneStimmabgabe] -= meldungStimmausschluss.stueckAktien;
                sammelkarteAbstimmungSplit.abgabe[abstimmposition][KonstStimmart.stimmausschluss] += meldungStimmausschluss.stueckAktien;
                geworfen = true;
                lDbBundle.dbAbstimmungMeldungSplit.update(sammelkarteAbstimmungSplit);
            }
        }
        CaBug.druckeLog("setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition geworfen=" + geworfen, logDrucken, 10);

        if (geworfen == false) {/*Restliche Fälle nur untersuchen, wenn nicht geworfen wurde*/
            if (aktuelleWeisungSammelkarteSplit.nichtBerechnen[abstimmposition] == 1) {
                /**Sammelkarte mit Weisungen auf Kopfebene - kann nicht berücksichtigt werden.
                 * Aktuell ignorieren und in Protokoll eintragen.*/
                eintragenWarnungFehler("Warnung - Stimmausschluß MeldeIdent "
                        + Integer.toString(meldungStimmausschluss.meldungsIdent) + " kann nicht "
                        + "berücksichtigt werden, da in Sammelkarte mit Weisungen auf Kopfebene!");

            } else {
                int weisungsArtZuAbstimmungStimmausschluss = weisungStimmausschluss.abgabe[abstimmposition];
                long stimmenkorrektur = meldungStimmausschluss.stueckAktien;
                sammelkarteAbstimmungSplit.abgabe[abstimmposition][weisungsArtZuAbstimmungStimmausschluss] -= stimmenkorrektur;
                sammelkarteAbstimmungSplit.abgabe[abstimmposition][KonstStimmart.stimmausschluss] += stimmenkorrektur;
            }
            lDbBundle.dbAbstimmungMeldungSplit.update(sammelkarteAbstimmungSplit);
        }

    }

    /*1. Schritt: Weisung / Sammelkarten in abstimmungMeldungSplit übertragen:
     * 
     * für alle aktiven Sammelkarten:
     *  
     * > abstimmungMeldungSplit für jede (präsente) Sammelkarte sowie Briefwahl anlegen (soweit nicht schon vorhanden)
     * für jeden aktiven Abstimmungspunkt: 
     * > falls Stimmabgabe für Sammelkarte vorhanden (d.h. abstimmungMeldung vorhanden, und für die Abstimmung liegt J/N/E/U vor:
     * 		> dann für diesen Abstimmungsblock die "abgegebenen Stimmen" übertragen
     * 				und abstimmungMeldung für die Sammelkarte auf inaktiv setzen (da ja Split vorhanden!)
     * 		> ansonsten: Split-Summen aus Weisung übertragen (falls vorhanden)
     * 				> Wenn diese auch nicht vorhanden: dann nix abgegeben und keine Weisungen vorhanden!
     */
    private void sammelKartenInAbstimmungMeldungSplitUebertragen() {
        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i = 0; i < anzSammelkarten; i++) {
            EclMeldung eclMeldungSammelkarte = lDbBundle.dbMeldungen.meldungenArray[i];

            int sammelIdent=eclMeldungSammelkarte.meldungsIdent;

            belegeSammelAbstimmungMeldungSplit(sammelIdent);
            belegeSammelAbstimmungMeldung(sammelIdent, eclMeldungSammelkarte);
            belegeSammelWeisungMeldungSplit(sammelIdent);

            if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(eclMeldungSammelkarte) || 
                    eclMeldungSammelkarte.skIst == KonstSkIst.briefwahl) {
                /**Ggf. AbstimmungMeldungSplit für Sammelkarte anlegen, wenn Karte präsent (oder Briefwahl) ist
                 * und noch nicht vorhanden.
                 */

                if (sammelAbstimmungMeldungSplitVorhanden==false) { /*Noch nicht vorhanden - neu anlegen, wenn Stimmen dieser Sammelkarte gezählt werden soll*/
                    sammelAbstimmungMeldungSplit = new EclAbstimmungMeldungSplit();
                    sammelAbstimmungMeldungSplit.meldungsIdent = eclMeldungSammelkarte.meldungsIdent;
                    lDbBundle.dbAbstimmungMeldungSplit.insert(sammelAbstimmungMeldungSplit);
                    sammelAbstimmungMeldungSplitVorhanden=true;
                }

                /***Für alle aktiven Abstimmungspunkte***/
                for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                    int abstimmposition = lieferePosInWeisung(i1);
                    if (abstimmposition != -1) {
                        if (sammelAbstimmungMeldungSplitVorhanden) {
                            for (int i2 = 0; i2 <= 9; i2++) { /*Bestehende Stimmabgabe überschreiben*/
                                setzeStimmartInSplitSatz(i1, sammelAbstimmungMeldungSplit, i2, 0);
                            }
                        }

                        /**Wenn Anwesend oder Briefwahl, dann Neue Stimmabgaben eintragen*/
                        int stimmabgabeLiegtVor = 0;
                        int aktuelleStimmabgabe = 0;
                        if (sammelAbstimmungMeldung != null) {
                            aktuelleStimmabgabe = liefereAktuelleMarkierungZuAbstimmungsPosition(i1,
                                    sammelAbstimmungMeldung);
                            if (aktuelleStimmabgabe == KonstStimmart.ja || aktuelleStimmabgabe == KonstStimmart.nein
                                    || aktuelleStimmabgabe == KonstStimmart.enthaltung
                                    || aktuelleStimmabgabe == KonstStimmart.ungueltig) {
                                stimmabgabeLiegtVor = 1;
                            }
                        }
                        if (stimmabgabeLiegtVor == 1) { /*Dann abgegebene Stimmen übertragen in Split-Satz*/
                            setzeStimmartInSplitSatz(i1, sammelAbstimmungMeldungSplit, aktuelleStimmabgabe,
                                    eclMeldungSammelkarte.stimmen);
                        } else {
                            /*Weisungen übertragen - nur wenn wirklich präsent, oder Briefwahl. D.h. Weisungen zählen nicht
                            bei "mal präsent gewesenen" oder "nie präsenten", selbst wenn der Parameter so steht*/
                            if (eclMeldungSammelkarte.statusPraesenz_Delayed == 1 || 
                                    eclMeldungSammelkarte.skIst == KonstSkIst.briefwahl) {

                                int weisungKopieren = 1;
                                /*Prüfen: falls möglicherweise von übergeordnetem Punkt übernehmen, dann aktuell Weisungen vorhanden, oder von übergeordnetem Punkt übernehmen?*/
                                int quellPosition = abstimmungenZuAktivenBlock[i1].vonIdentGesamtweisung;
                                if (quellPosition > 0) {
                                    int aktuellGefunden = 0;
                                    for (int i2 = 1; i2 < 10; i2++) {
                                        if (sammelWeisungMeldungSplit.abgabe[abstimmposition][i2] > 0) {
                                            aktuellGefunden = 1;
                                        }
                                    }
                                    if (aktuellGefunden == 0) {/*Dann von übergeordneter Position kopieren*/
                                        for (int i2 = 0; i2 < 10; i2++) {
                                            sammelAbstimmungMeldungSplit.abgabe[abstimmposition][i2] = sammelWeisungMeldungSplit.abgabe[quellPosition][i2];
                                        }
                                        weisungKopieren = 0;
                                    }

                                }

                                if (weisungKopieren == 1) {
                                    for (int i2 = 0; i2 < 10; i2++) {
                                        sammelAbstimmungMeldungSplit.abgabe[abstimmposition][i2] = sammelWeisungMeldungSplit.abgabe[abstimmposition][i2];
                                    }
                                }
                            }
                        }
                    }

                }
                if (sammelAbstimmungMeldungSplitVorhanden==true) {
                    lDbBundle.dbAbstimmungMeldungSplit.update(sammelAbstimmungMeldungSplit);
                }
            }
        }
    }

    /*2. Schritt - Stimmausschluß für Meldungen, die mit Stimmausschluß gekennzeichnet sind*/
    private void stimmausschlussFuerGekennzeichneteMeldungen() {

        int anzStimmausschluss = 0;

        CaBug.druckeLog("Stimmausschluß anfang", logDrucken, 3);
        lDbBundle.dbMeldungen.leseStimmausschluss();
        /*Zwischenspeichern der gefundenen Meldungen, da zwischendurch noch Sammelkarten-Meldungen 
         * eingelesen werden müssen.*/
        EclMeldung[] aktionaersMeldungenMitStimmausschluss = lDbBundle.dbMeldungen.meldungenArray;
        anzStimmausschluss = aktionaersMeldungenMitStimmausschluss.length;
        CaBug.druckeLog("anzStimmausschluss=" + anzStimmausschluss, logDrucken, 3);

        /*a) Für alle Einzelaktionäre (nicht in Sammelkarte, keine Sammelkarte!), 
         * mit Ausschlußkennzeichen: einfach
         * eine zusätzliche Stimmabgabe nur mit dem Stimmausschluß eingetragen.*/
        for (int sj = 0; sj < anzStimmausschluss; sj++) {//Für alle Meldungen mit Stimmausschluß
            EclMeldung meldungStimmausschluss = aktionaersMeldungenMitStimmausschluss[sj];
            if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungStimmausschluss)==true) {
                if (meldungStimmausschluss.meldungstyp == 1) {
                    String[] ausschlussKennzeichen = meldungStimmausschluss.liefereStimmausschlussArray();

                    CaBug.druckeLog("Stimmausschluss Einzelaktionär Meldung=" + meldungStimmausschluss.meldungsIdent, logDrucken, 10);
                    starteSpeichernFuerMeldung(meldungStimmausschluss);

                    for (int sj1 = 0; sj1 < ausschlussKennzeichen.length; sj1++) {//Für alle Stimmausschlußkennzeichen einer Meldung
                        ausschlussKennzeichen[sj1] = ausschlussKennzeichen[sj1].trim();
                        CaBug.druckeLog("ZeileSplit " + ausschlussKennzeichen[sj1],logDrucken, 3);
                        for (int sj2 = 0; sj2 < abstimmungen.length; sj2++) {//Für alle Abstimmungen: prüfen, ob Stimmausschlußkennzeichen dieser Abstimmung
                            if (abstimmungen[sj2].stimmberechtigteGattungen[meldungStimmausschluss.liefereGattung()-1]==1) {
                                if (abstimmungen[sj2].stimmausschluss.indexOf(ausschlussKennzeichen[sj1]) != -1) {
                                    if (liefereMarkierungZuAbstimmungsPosition(sj2)!=0 || meldungStimmausschluss.statusPraesenz_Delayed == 1 ) {
                                        /**Nur Stimmausschlußkennzeichen setzen, wenn bereits was abgestimmt wurde - oder wenn er tatsächlich aktuell präsent ist*/
                                        setzeMarkierungZuAbstimmungsPosition(KonstStimmart.stimmausschluss, sj2, KonstWillenserklaerungWeg.abstMaschinell);
                                        CaBug.druckeLog("Setze Markierung stimmausschluss für AbstimmungLfd " + sj2, logDrucken, 3);
                                    }
                                    else {
                                        CaBug.druckeLog("Keine StimmausschlußMarkierung für AbstimmungLfd "+sj2+" da nicht abgestimmt und nicht aktuell präsent", logDrucken, 3);
                                    }
                                }
                            }
                        }
                    }

                    beendeSpeichernFuerMeldung();
                }

            }
        }

        /*b) Alle Einzelaktionäre, die in Sammelkarten enthalten sind, mit Stimmausschluß bei Einzelaktionär:
         * 
         * Falls Sammelkarte "geworfen" hat (also ein Satz in abtimmungMeldung vorhanden ist): (in eclAbstimmungGeworfenSammelkarte)
         * >> Bei Splitsumme die Stimmabgabeart um die Stimmzahl des Einzelaktionärs reduzieren,
         * 		und die Stimmausschluß-Stimmzahl entsprechend erhöhen.
         * 
         * Falls Sammelkarte nur auf "Kopfsatz-Ebene": (kofsatzebeneVorhanden=true)
         * >> Da kann nichts gemacht werden! Das muß gewußt werden! Vorher anhand Stimmausschlußliste
         * überprüfen
         * 
         * Falls Sammelkarte normal über Weisungen: 
         * >> Einzelweisungssatz des Aktionärs lesen, Weisung für den jeweiligen Aktionär holen, in
         * 		Sammelkarten-StimmabgabenSplit die Summen reduzieren (abgegebene Weisung) bzw. erhöhen
         * 		(Stimmausschluß)
         */
        for (int sj = 0; sj < anzStimmausschluss; sj++) {//Für alle Meldungen mit Stimmausschluß
            EclMeldung meldungStimmausschluss = aktionaersMeldungenMitStimmausschluss[sj];
            if (meldungStimmausschluss.meldungstyp == 3) {/*Meldung ist in Sammelkarte*/
                CaBug.druckeLog(
                        "Stimmausschluss in Sammelkarte: meldungsIdent=" + meldungStimmausschluss.meldungsIdent, logDrucken, 10);
                /*Weisung zu dieser meldung einlesen*/
                lDbBundle.dbWeisungMeldung.leseZuMeldungsIdentNurAktive(meldungStimmausschluss.meldungsIdent, false);
                EclWeisungMeldung weisungStimmausschlussAktionaer = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

                /*Sammelkarte einlesen*/
                int sammelIdent = meldungStimmausschluss.meldungEnthaltenInSammelkarte;
                lDbBundle.dbMeldungen.leseZuIdent(sammelIdent);
                EclMeldung meldungSammelkarte = lDbBundle.dbMeldungen.meldungenArray[0];

                starteSpeichernFuerMeldungImplizit(meldungStimmausschluss);
                
                /*Nur durchführen,wenn Sammelkarte präsent ist, oder wenn sie Briefwahl ist
                 * 
                 * Hinweis: wenn nicht präsent, aber Parameter auf "Präsente oder Präsent gewesene" steht,
                 * darf das bei dem einzelnen Aktionär nur eingetragen werden, wenn Sammelkarte geworfen ist 
                 * (da Sammelkarte nur mit Weisungen ja in diesem Fall nicht zählen). Das wird dort dann nochmal überprüft*/
                if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungSammelkarte)==true
                        || (meldungSammelkarte.skIst == 4)) {

                    
                    CaBug.druckeLog("Stimmausschluss wird durchgeführt für Aktionär in Sammelkarte", logDrucken, 10);
                    /*Gesplittete Stimmabgabe der Sammelkarte einlesen - für Korrektur und späteren Update*/
                    lDbBundle.dbAbstimmungMeldungSplit.leseIdent(sammelIdent);
                    EclAbstimmungMeldungSplit sammelkarteAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit
                            .ergebnisPositionGefunden(0);

                    /*Falls vorhanden: "Geworfene Stimmabgabe" einlesen*/
                    EclAbstimmungMeldung eclAbstimmungGeworfenSammelkarte = null;
                    lDbBundle.dbAbstimmungMeldung.lese_Ident(sammelIdent);
                    if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() != 0) {
                        CaBug.druckeLog("eclAbstimmungGeworfenSammelkarte gefunden", logDrucken, 10);
                        eclAbstimmungGeworfenSammelkarte = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
                    }

                    /*Split-Weisung zu Sammelkarte einlesen, um zu prüfen ob Kopfsatz-Weisung*/
                    lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(sammelIdent, false);
                    EclWeisungMeldung aktuelleWeisungSammlkarte = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    EclWeisungMeldungSplit aktuelleWeisungSammelkarteSplit = aktuelleWeisungSammlkarte.weisungMeldungSplit;

                    String[] ausschlussKennzeichen = meldungStimmausschluss.liefereStimmausschlussArray();
                    for (int sj1 = 0; sj1 < ausschlussKennzeichen.length; sj1++) {//Für alle Stimmausschlußkennzeichen einer Meldung
                        ausschlussKennzeichen[sj1] = ausschlussKennzeichen[sj1].trim();
                        for (int sj2 = 0; sj2 < abstimmungen.length; sj2++) {//Für alle Abstimmungen: prüfen, ob Stimmausschlußkennzeichen dieser Abstimmung
                            CaBug.druckeLog("sj2="+sj2, logDrucken, 10);
                            if (abstimmungen[sj2].stimmausschluss.indexOf(ausschlussKennzeichen[sj1]) != -1) {
                                if (abstimmungen[sj2].stimmberechtigteGattungen[meldungStimmausschluss.liefereGattung()-1]==1) {

                                    int abstimmposition = lieferePosInWeisung(sj2);

                                    /*Summen etc. nur reduzieren, wenn nicht bereits als Stimmausschluß eingetragen*/
                                    if (liefereMarkierungZuAbstimmungsPositionImmplizit(abstimmposition)!=KonstStimmart.stimmausschluss) {

                                        if (meldungSammelkarte.statusPraesenz_Delayed == 1 || 
                                                meldungSammelkarte.skIst == 4 || 
                                                (eclAbstimmungGeworfenSammelkarte!=null && eclAbstimmungGeworfenSammelkarte.abgabe[abstimmposition]!=0)) {
                                            CaBug.druckeLog("setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition", logDrucken, 10);

                                            setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition(
                                                    /*Daten des Einzelaktionärs*/
                                                    meldungStimmausschluss, weisungStimmausschlussAktionaer,
                                                    /*Sammelkarte*/
                                                    aktuelleWeisungSammelkarteSplit, eclAbstimmungGeworfenSammelkarte,
                                                    sammelkarteAbstimmungSplit,
                                                    /*Sonstige Parameter*/
                                                    abstimmposition);
                                            setzeMarkierungZuAbstimmungsPositionImplizit(KonstStimmart.stimmausschluss, abstimmposition, KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(meldungSammelkarte.skIst), meldungSammelkarte.meldungsIdent);
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
                beendeSpeichernFuerMeldungImplizit();

            }
        }

        /*c) Sammelkarte mit Gesamtausschluß:
         * >> Weisungen für den jeweiligen Tagesordnungspunkt komplett auf 0 setzen.
         * 		Gesamte Summe auf Weisung Stimmausschluß setzen.
         */
        for (int sj = 0; sj < anzStimmausschluss; sj++) {//Für alle Meldungen mit Stimmausschluß
            EclMeldung meldungStimmausschluss = aktionaersMeldungenMitStimmausschluss[sj];
            if ((pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungStimmausschluss)==true
                    || (meldungStimmausschluss.skIst == 4))
                    && meldungStimmausschluss.meldungstyp == 2 /*Sammelkarte*/) {
                CaBug.druckeLog(
                        "Stimmausschluss für gesamte Sammelkarte: meldungsIdent=" + meldungStimmausschluss.meldungsIdent, logDrucken, 10);
                String[] ausschlussKennzeichen = meldungStimmausschluss.liefereStimmausschlussArray();
                int sammelIdent = meldungStimmausschluss.meldungsIdent;

                /*Falls vorhanden: "Geworfene Stimmabgabe" einlesen*/
                EclAbstimmungMeldung eclAbstimmungGeworfenSammelkarte = null;
                lDbBundle.dbAbstimmungMeldung.lese_Ident(sammelIdent);
                if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() != 0) {
                    CaBug.druckeLog("eclAbstimmungGeworfenSammelkarte gefunden", logDrucken, 10);
                    eclAbstimmungGeworfenSammelkarte = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
                }

                /*Gesplittete Stimmabgabe der Sammelkarte einlesen - für Korrektur und späteren Update*/
                lDbBundle.dbAbstimmungMeldungSplit.leseIdent(sammelIdent);
                EclAbstimmungMeldungSplit sammelkarteAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit
                        .ergebnisPositionGefunden(0);
                
                boolean[] fuerDieseAbstimmungStimmausschlussEintragen=new boolean[abstimmungen.length];
                for (int sj2 = 0; sj2 < abstimmungen.length; sj2++) {
                    fuerDieseAbstimmungStimmausschlussEintragen[sj2]=false;
                }
                
                for (int sj1 = 0; sj1 < ausschlussKennzeichen.length; sj1++) {//Für alle Stimmausschlußkennzeichen einer Meldung
                    CaBug.druckeLog("Stimmausschluss="+ausschlussKennzeichen[sj1], logDrucken, 10);
                    for (int sj2 = 0; sj2 < abstimmungen.length; sj2++) {//Für alle Abstimmungen: prüfen, ob Stimmausschlußkennzeichen dieser Abstimmung
                        int abstimmposition = lieferePosInWeisung(sj2);
                        CaBug.druckeLog("Stimmausschluss für Abstimmung sj2="+sj2+"=<"+abstimmungen[sj2].stimmausschluss+"> ausschlussKennzeichen[sj1]=<"+ausschlussKennzeichen[sj1]
                                +"> indexof="+abstimmungen[sj2].stimmausschluss.indexOf(ausschlussKennzeichen[sj1]), logDrucken, 10);
                        if (abstimmungen[sj2].stimmausschluss.indexOf(ausschlussKennzeichen[sj1]) != -1) {
                            if (abstimmungen[sj2].stimmberechtigteGattungen[meldungStimmausschluss.liefereGattung()-1]==1) {
                                CaBug.druckeLog("Stimmausschlußkennzeichen in Abstimmung sj2="+sj2+" gefunden", logDrucken, 10);
                                if (meldungStimmausschluss.statusPraesenz_Delayed == 1 ||
                                        meldungStimmausschluss.skIst == 4 
                                        || (eclAbstimmungGeworfenSammelkarte!=null && eclAbstimmungGeworfenSammelkarte.abgabe[abstimmposition]!=0)) {
                                    fuerDieseAbstimmungStimmausschlussEintragen[sj2]=true;
                                    CaBug.druckeLog("für Abstimmung "+sj2+" Stimmausschluß Gesamt setzen", logDrucken, 10);
                                    for (int i = 0; i < 10; i++) {
                                        if (i != KonstStimmart.stimmausschluss) {
                                            sammelkarteAbstimmungSplit.abgabe[abstimmposition][i] = 0;
                                        } else {
                                            sammelkarteAbstimmungSplit.abgabe[abstimmposition][i] = meldungStimmausschluss.stueckAktien;
                                        }
                                    }
                                    lDbBundle.dbAbstimmungMeldungSplit.update(sammelkarteAbstimmungSplit);
                                }
                            }
                        }
                    }
                }

                /*Nun noch für alle Aktionäre in dieser Sammelkarte das Stimmausschlußkennzeichen setzen*/
                int anzInSammelkarte=lDbBundle.dbMeldungZuSammelkarte.leseZuSammelkarteNurAktive(meldungStimmausschluss.meldungsIdent);
                for (int i1=0;i1<anzInSammelkarte;i1++) {
                    EclMeldungZuSammelkarte lMeldungZuSammelkarte=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1);
                    int meldungsIdent=lMeldungZuSammelkarte.meldungsIdent;
                    /*Meldung mit meldungsIdent in meldung einlesen*/
                    leseMeldung(meldungsIdent);
                    
                    starteSpeichernFuerMeldungImplizit(meldung);

                    /***Für alle aktiven Abstimmungspunkte***/
                    for (int i2 = 0; i2 < abstimmungenZuAktivenBlock.length; i2++) {
                        if (fuerDieseAbstimmungStimmausschlussEintragen[i2]) {
                            int abstimmposition = lieferePosInWeisung(i2);
                            setzeMarkierungZuAbstimmungsPositionImplizit(KonstStimmart.stimmausschluss, abstimmposition, KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(meldungStimmausschluss.skIst), sammelIdent);
                        }
                    }
                    beendeSpeichernFuerMeldungImplizit();
                }
            }

        }
        CaBug.druckeLog("Stimmausschluß ende", logDrucken, 3);
    }

    /*3. Schritt - Einzel-Stimmausschluß*/
    private void stimmausschlussFuerEinzelAusschluss() {
        int anzStimmausschluss = 0;

        CaBug.druckeLog("Einzelausschluß Anfang", logDrucken, 3);
        for (int sj2 = 0; sj2 < abstimmungen.length; sj2++) {//Für alle Abstimmungen: prüfen, ob Einzelausschluß dafür vorhanden
            lDbBundle.dbAbstimmungenEinzelAusschluss.readZuIdentAbstimmung(abstimmungen[sj2].ident);
            anzStimmausschluss = lDbBundle.dbAbstimmungenEinzelAusschluss.anzErgebnis();
            if (anzStimmausschluss > 0) {
                for (int sj = 0; sj < anzStimmausschluss; sj++) {//Für alle Meldungen mit Stimmausschluß
                    int meldeIdent = lDbBundle.dbAbstimmungenEinzelAusschluss.ergebnisPosition(sj).identMeldung;
                    int rc = lDbBundle.dbMeldungen.leseZuIdent(meldeIdent);
                    if (rc > 0) {
                        EclMeldung meldungStimmausschluss = lDbBundle.dbMeldungen.meldungenArray[0];
                        if (abstimmungen[sj2].stimmberechtigteGattungen[meldungStimmausschluss.liefereGattung()-1]==1) {

                            /*a) Einzelaktionäre (nicht in Sammelkarte, keine Sammelkarte!): 
                             * einfach eine zusätzliche Stimmabgabe nur mit dem Stimmausschluß eingetragen.*/
                            if (meldungStimmausschluss.meldungstyp == 1) {
                                //                            starteSpeichernFuerMeldung(meldungStimmausschluss);
                                //                            if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungStimmausschluss)==true &&
                                //                                    (meldungStimmausschluss.statusPraesenz_Delayed == 1
                                //                                    || abstimmungMeldung.abgabe[sj2]!=0)
                                //                                ) {
                                //                                setzeMarkierungZuAbstimmungsIdent(KonstStimmart.stimmausschluss,
                                //                                        abstimmungen[sj2].ident, KonstWillenserklaerungWeg.abstMaschinell);
                                //                            }
                                //                            beendeSpeichernFuerMeldung();
                                starteSpeichernFuerMeldung(meldungStimmausschluss);
                                if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungStimmausschluss)==true &&
                                        (meldungStimmausschluss.statusPraesenz_Delayed == 1
                                        || liefereMarkierungZuAbstimmungsPosition(sj2)!=0)
                                        ) {
                                    setzeMarkierungZuAbstimmungsPosition(KonstStimmart.stimmausschluss,
                                            sj2, KonstWillenserklaerungWeg.abstMaschinell);
                                }
                                beendeSpeichernFuerMeldung();
                            }

                            /*b) Einzelaktionär, der in Sammelkarte enthalten ist*/
                            if (meldungStimmausschluss.meldungstyp == 3) {
                                starteSpeichernFuerMeldungImplizit(meldungStimmausschluss);

                                /*Weisung zu dieser meldung einlesen*/
                                lDbBundle.dbWeisungMeldung
                                .leseZuMeldungsIdentNurAktive(meldungStimmausschluss.meldungsIdent, false);
                                EclWeisungMeldung weisungStimmausschluss = lDbBundle.dbWeisungMeldung
                                        .weisungMeldungGefunden(0);

                                /*Sammelkarte einlesen*/
                                int sammelIdent = meldungStimmausschluss.meldungEnthaltenInSammelkarte;
                                lDbBundle.dbMeldungen.leseZuIdent(sammelIdent);
                                EclMeldung meldungSammelkarte = lDbBundle.dbMeldungen.meldungenArray[0];

                                /*Nur durchführen,wenn Sammelkarte präsent ist, oder wenn sie Briefwahl ist
                                 * 
                                 * Hinweis: wenn nicht präsent, aber Parameter auf "Präsente oder Präsent gewesene" steht,
                                 * darf das bei dem einzelnen Aktionär nur eingetragen werden, wenn Sammelkarte geworfen ist 
                                 * (da Sammelkarte nur mit Weisungen ja in diesem Fall nicht zählen). Das wird dort dann nochmal überprüft*/
                                if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungSammelkarte)==true
                                        || meldungSammelkarte.skIst == 4) {

                                    /*Gesplittete Stimmabgabe der Sammelkarte einlesen - für Korrektur und späteren Update*/
                                    lDbBundle.dbAbstimmungMeldungSplit.leseIdent(sammelIdent);
                                    EclAbstimmungMeldungSplit sammelkarteAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit
                                            .ergebnisPositionGefunden(0);

                                    /*Falls vorhanden: "Geworfene Stimmabgabe" einlesen*/
                                    EclAbstimmungMeldung eclAbstimmungGeworfenSammelkarte = null;
                                    lDbBundle.dbAbstimmungMeldung.lese_Ident(sammelIdent);
                                    if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() != 0) {
                                        eclAbstimmungGeworfenSammelkarte = lDbBundle.dbAbstimmungMeldung
                                                .ergebnisGefunden(0);
                                    }

                                    /*Split-Weisung zu Sammelkarte einlesen, um zu prüfen ob Kopfsatz-Weisung*/
                                    lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(sammelIdent, false);
                                    EclWeisungMeldung aktuelleWeisungSammlkarte = lDbBundle.dbWeisungMeldung
                                            .weisungMeldungGefunden(0);
                                    EclWeisungMeldungSplit aktuelleWeisungSammelkarteSplit = aktuelleWeisungSammlkarte.weisungMeldungSplit;

                                    int abstimmposition = lieferePosInWeisung(sj2);
                                    /*Summen etc. nur reduzieren, wenn nicht bereits als Stimmausschluß eingetragen*/
                                    if (liefereMarkierungZuAbstimmungsPositionImmplizit(abstimmposition)!=KonstStimmart.stimmausschluss) {
                                        if (meldungSammelkarte.statusPraesenz_Delayed == 1 || 
                                                meldungSammelkarte.skIst == 4 || 
                                                (eclAbstimmungGeworfenSammelkarte!=null && eclAbstimmungGeworfenSammelkarte.abgabe[abstimmposition]!=0)) {
                                            setzeStimmausschlussMeldungInSammelkarteFuerAbstimmungsposition(
                                                    /*Daten des Einzelaktionärs*/
                                                    meldungStimmausschluss, weisungStimmausschluss,
                                                    /*Sammelkarte*/
                                                    aktuelleWeisungSammelkarteSplit, eclAbstimmungGeworfenSammelkarte,
                                                    sammelkarteAbstimmungSplit,
                                                    /*Sonstige Parameter*/
                                                    abstimmposition);
                                            setzeMarkierungZuAbstimmungsPositionImplizit(KonstStimmart.stimmausschluss, abstimmposition, KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(meldungSammelkarte.skIst), meldungSammelkarte.meldungsIdent);
                                       }

                                    }

                                }
                                beendeSpeichernFuerMeldungImplizit();

                            }

                            /*c) Sammelkarte mit Gesamtausschluß:*/
                            if (meldungStimmausschluss.meldungstyp == 2) {
                                if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(meldungStimmausschluss)==true
                                        || meldungStimmausschluss.skIst == 4) {
                                    int sammelIdent = meldungStimmausschluss.meldungsIdent;
                                    CaBug.druckeLog("Sammelkarte mit EinzelStimmausschluß gesamt sammelIdent="+sammelIdent, logDrucken, 10);

                                    /*Falls vorhanden: "Geworfene Stimmabgabe" einlesen*/
                                    EclAbstimmungMeldung eclAbstimmungGeworfenSammelkarte = null;
                                    lDbBundle.dbAbstimmungMeldung.lese_Ident(sammelIdent);
                                    if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() != 0) {
                                        CaBug.druckeLog("eclAbstimmungGeworfenSammelkarte gefunden", logDrucken, 10);
                                        eclAbstimmungGeworfenSammelkarte = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
                                    }
                                    int abstimmposition = lieferePosInWeisung(sj2);

                                    if (meldungStimmausschluss.statusPraesenz_Delayed == 1 ||
                                            meldungStimmausschluss.skIst == 4 
                                            || (eclAbstimmungGeworfenSammelkarte!=null && eclAbstimmungGeworfenSammelkarte.abgabe[abstimmposition]!=0)) {

                                        /*Gesplittete Stimmabgabe der Sammelkarte einlesen - für Korrektur und späteren Update*/
                                        lDbBundle.dbAbstimmungMeldungSplit.leseIdent(sammelIdent);
                                        EclAbstimmungMeldungSplit sammelkarteAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit
                                                .ergebnisPositionGefunden(0);

                                        for (int i = 0; i < 10; i++) {
                                            if (i != KonstStimmart.stimmausschluss) {
                                                sammelkarteAbstimmungSplit.abgabe[abstimmposition][i] = 0;
                                            } else {
                                                sammelkarteAbstimmungSplit.abgabe[abstimmposition][i] = meldungStimmausschluss.stueckAktien;
                                            }
                                        }
                                        lDbBundle.dbAbstimmungMeldungSplit.update(sammelkarteAbstimmungSplit);

                                        /*Nun noch für alle Aktionäre in dieser Sammelkarte das Stimmausschlußkennzeichen setzen*/
                                        int anzInSammelkarte=lDbBundle.dbMeldungZuSammelkarte.leseZuSammelkarteNurAktive(meldungStimmausschluss.meldungsIdent);
                                        for (int i1=0;i1<anzInSammelkarte;i1++) {
                                            EclMeldungZuSammelkarte lMeldungZuSammelkarte=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1);
                                            int meldungsIdent=lMeldungZuSammelkarte.meldungsIdent;
                                            /*Meldung mit meldungsIdent in meldung einlesen*/
                                            leseMeldung(meldungsIdent);

                                            starteSpeichernFuerMeldungImplizit(meldung);

                                            /***Für alle aktiven Abstimmungspunkte***/
                                            setzeMarkierungZuAbstimmungsPositionImplizit(KonstStimmart.stimmausschluss, abstimmposition, KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(meldungStimmausschluss.skIst), sammelIdent);

                                            beendeSpeichernFuerMeldungImplizit();
                                        }

                                    }
                                }
                            }
                        }

                    }
                }

            }
        }

        System.out.println("Einzelausschluß Ende");

    }

    public int auswerten() {

        
        
        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {
            return auswertenku310();
        }
        
        if (lDbBundle.param.paramAbstimmungParameter.impliziteStimmabgabenAutomatischVerwalten==1) {
            entferneAutomatischEingetrageneStimmabgaben();
        }
       
        /*1. Schritt: Weisung / Sammelkarten in abstimmungMeldungSplit übertragen*/
        sammelKartenInAbstimmungMeldungSplitUebertragen();

        /*2. Schritt - Stimmausschluß für Meldungen, die mit Stimmausschluß gekennzeichnet sind*/
        stimmausschlussFuerGekennzeichneteMeldungen();

        /*3. Schritt - Einzel-Stimmausschluß*/
        stimmausschlussFuerEinzelAusschluss();

        /*4. Schritt: Für die Abstimmung gültige Präsenzsummen, Briefwahlsummen, Gesamtstimmenzahl ermitteln: 
         * > aus letzter festgestellter Präsenz
         * > in DbHVDatenLfd Abstimmungsblock mit Präsenznummer "verknüpfen" (für spätere Ausdrucke)
         * */
        BlPraesenzlistenNummer blPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        blPraesenzlistenNummer.leseAktuelleNummern();
        int verwendetePraesenznummer = lDbBundle.clGlobalVar.zuVerzeichnisNr1 - 1;
        if (verwendetePraesenznummer == 0) {
            verwendetePraesenznummer--;
        }

        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 72;
        lHVDatenLfd.benutzer = aktiverAbstimmungsblock.ident;
        lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            lHVDatenLfd.benutzer = aktiverAbstimmungsblock.ident;
            lHVDatenLfd.wert = Integer.toString(verwendetePraesenznummer);
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {/*Neuen Eintrag erzeugen*/
            lHVDatenLfd.wert = Integer.toString(verwendetePraesenznummer);
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }

        /*5. Schritt: abgegebene Stimmen summieren
         * > Summe aus allen abstimmungMeldung, die aktiv sind.
         * > summe aus allen abstimmungMeldungSplit
         * > beide addieren, in abstimmungMeldungSplit (-1) eintragen für späteren Ausdruck
         * 
         */

        /*Summen initialisieren*/
        for (int gattung = 0; gattung <= 5; gattung++) {
            for (int j = 0; j < 200; j++) {
                for (int j1 = 0; j1 < 10; j1++) {
                    abstimmungsSummen[gattung][j][j1] = 0;
                    abstimmungsSummenBriefwahl[gattung][j][j1] = 0;
                }
            }
        }

        /*Summe aus Einzelkarten*/
        int anzahlAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.readinit_all(true);
        //		System.out.println("A");
        if (anzahlAbstimmungMeldung > 0) {
            //			System.out.println("B");
            while (lDbBundle.dbAbstimmungMeldung.readnext_all()) {
                //				System.out.println("C");
                EclAbstimmungMeldung eclAbstimmungMeldungAktionaer = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
                if (eclAbstimmungMeldungAktionaer.aktiv == 1) {
                    //					System.out.println("D");
                    for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                        if (!istUeberschrift(i1)) {
                            int aktuelleStimmabgabe = liefereAktuelleMarkierungZuAbstimmungsPosition(i1,
                                    eclAbstimmungMeldungAktionaer);
                            //							System.out.println("F aktuelleStimmabgabe="+aktuelleStimmabgabe);
                            if (KonstWillenserklaerungWeg.wegIstKeineErgaenzung(aktuellerAbgabewegZuAbstimmungsPosition)) {
                                abstimmungsSummen[eclAbstimmungMeldungAktionaer.gattung][lieferePosInWeisung(
                                        i1)][aktuelleStimmabgabe] += eclAbstimmungMeldungAktionaer.stimmen;
                                //							System.out.println("Einzelkarten i1="+i1+" meldeident="+eclAbstimmungMeldungAktionaer.meldungsIdent+" aktuelleStimmabgabe="+aktuelleStimmabgabe+" stimmen="+eclAbstimmungMeldungAktionaer.stimmen);
                            }
                        }
                    }
                }
            }
        }

        /*Summe aus Sammelkarten*/
        int anzahlAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit.leseIdent(-99);
        CaBug.druckeLog("anzahlAbstimmungSplit=" + anzahlAbstimmungSplit, logDrucken, 10);
        if (anzahlAbstimmungSplit > 0) {
            for (int i = 0; i < anzahlAbstimmungSplit; i++) {
                EclAbstimmungMeldungSplit eclAbstimmungMeldungSammel = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(i);
                if (eclAbstimmungMeldungSammel.meldungsIdent >= 0) {
                    /*Sammelkarte einlesen, um Briefwahl festzustellen ...*/
                    lDbBundle.dbMeldungen.leseZuIdent(eclAbstimmungMeldungSammel.meldungsIdent);
                    EclMeldung eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

                    if (eclAbstimmungMeldungSammel.meldungsIdent > 0) { /*sonst Summensatz*/
                        for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                            if (!istUeberschrift(i1)) {
                                for (int i2 = 0; i2 < 10; i2++) {
                                    abstimmungsSummen[eclMeldung.liefereGattung()][lieferePosInWeisung(i1)][i2] += 
                                            eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2];
                                    if (eclMeldung.skIst == 4) {
                                        abstimmungsSummenBriefwahl[eclMeldung.liefereGattung()][lieferePosInWeisung(i1)][i2] += eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2];
                                    }
                                    //								System.out.println("Sammelk i="+i+" i1="+i1+" i2="+i2+" eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2]"+eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2]);
                                }
                            }
                        }
                    }
                }
            }
        }

        /**Pauschalausschluss von Stimmen abziehen*/
        for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {//Für alle Abstimmungen
            if (!istUeberschrift(i1)) {
                for (int gattung=1; gattung<=5; gattung++) {
                    long abzuziehendeAktien=abstimmungen[i1].pauschalAusschluss[gattung-1];
                    if (abzuziehendeAktien>0) {
                        int abzuziehenBeiStimmenart=abstimmungen[i1].pauschalAusschlussJN[gattung-1];
                        abstimmungsSummen[gattung][lieferePosInWeisung(i1)][abzuziehenBeiStimmenart]-=abzuziehendeAktien;
                        abstimmungsSummen[gattung][lieferePosInWeisung(i1)][KonstStimmart.stimmausschluss]+=abzuziehendeAktien;
                    }
                }
            }
        }
        
        
        /*abstimmungsSummen[0] (Summe aller Gattungen) und abstimmungsSummenBriefwahl[0] füllen*/
        for (int gattung = 1; gattung <= 5; gattung++) {
            for (int j = 0; j < 200; j++) {
                for (int j1 = 0; j1 < 10; j1++) {
                    abstimmungsSummen[0][j][j1] += abstimmungsSummen[gattung][j][j1];
                    abstimmungsSummenBriefwahl[0][j][j1] += abstimmungsSummenBriefwahl[gattung][j][j1];
                }
            }
        }

        /*Summen in Satz mit Ident -10 bis -15 speichern, briefwahl in Satz mit -20 bis -25*/

        /* -10 bis -15 (Summen) einlesen bzw. anlegen*/
        EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummen = new EclAbstimmungMeldungSplit[6];
        for (int i = 0; i <= 5; i++) {
            int ident = (i + 10) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden - neu anlegen*/
                eclAbstimmungMeldungSummen[i] = new EclAbstimmungMeldungSplit();
                eclAbstimmungMeldungSummen[i].meldungsIdent = ident;
                lDbBundle.dbAbstimmungMeldungSplit.insert(eclAbstimmungMeldungSummen[i]);
            } else { /*Abstimmungssplit schon vorhanden*/
                eclAbstimmungMeldungSummen[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        /* -20 bis -25 (Briefwahl) einlesen bzw. anlegen*/
        EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummenBriefwahl = new EclAbstimmungMeldungSplit[6];
        for (int i = 0; i <= 5; i++) {
            int ident = (i + 20) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden - neu anlegen*/
                eclAbstimmungMeldungSummenBriefwahl[i] = new EclAbstimmungMeldungSplit();
                eclAbstimmungMeldungSummenBriefwahl[i].meldungsIdent = ident;
                lDbBundle.dbAbstimmungMeldungSplit.insert(eclAbstimmungMeldungSummenBriefwahl[i]);
            } else { /*Abstimmungssplit schon vorhanden*/
                eclAbstimmungMeldungSummenBriefwahl[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        for (int i = 0; i <= 5; i++) {
            for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                if (!istUeberschrift(i1)) {
                    for (int i2 = 0; i2 < 10; i2++) {
                        eclAbstimmungMeldungSummen[i].abgabe[lieferePosInWeisung(
                                i1)][i2] = abstimmungsSummen[i][lieferePosInWeisung(i1)][i2];
                        eclAbstimmungMeldungSummenBriefwahl[i].abgabe[lieferePosInWeisung(
                                i1)][i2] = abstimmungsSummenBriefwahl[i][lieferePosInWeisung(i1)][i2];
                    }
                }
            }
        }

        for (int i = 0; i <= 5; i++) {
            lDbBundle.dbAbstimmungMeldungSplit.update(eclAbstimmungMeldungSummen[i]);
        }
        for (int i = 0; i <= 5; i++) {
            lDbBundle.dbAbstimmungMeldungSplit.update(eclAbstimmungMeldungSummenBriefwahl[i]);
        }

        if (lDbBundle.param.paramAbstimmungParameter.impliziteStimmabgabenAutomatischVerwalten==1) {
            ergaenzeAbstimmungMeldungFuerArchiv(); 
        }
        
        return 1;

    }

//    @Deprecated
//    public int exportieren() {
//
//        CaDateiWrite exportDatei = new CaDateiWrite();
//        exportDatei.trennzeichen = ';';
//        exportDatei.dateiart = ".csv";
//        exportDatei.oeffne(lDbBundle, "einzelkarten" + Integer.toString(aktiverAbstimmungsblock.ident));
//
//        exportDatei.ausgabe("MeldeIdent");
//        exportDatei.ausgabe("Stimmen");
//        exportDatei.newline();
//
//        /*Einzelkarten*/
//        int anzahlAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.readinit_all(false);
//        if (anzahlAbstimmungMeldung > 0) {
//            while (lDbBundle.dbAbstimmungMeldung.readnext_all()) {
//                EclAbstimmungMeldung eclAbstimmungMeldungAktionaer = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
//                if (eclAbstimmungMeldungAktionaer.aktiv == 1) {
//                    exportDatei.ausgabe(Integer.toString(eclAbstimmungMeldungAktionaer.meldungsIdent));
//                    exportDatei.ausgabe(Long.toString(eclAbstimmungMeldungAktionaer.stimmen));
//                    for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
//                        if (!istUeberschrift(i1)) {
//                            int aktuelleStimmabgabe = liefereAktuelleMarkierungZuAbstimmungsPosition(i1,
//                                    eclAbstimmungMeldungAktionaer);
//                            exportDatei.ausgabe(KonstStimmart.getText(aktuelleStimmabgabe));
//                            //							abstimmungsSummen[lieferePosInWeisung(i1)][aktuelleStimmabgabe]+=eclAbstimmungMeldungAktionaer.stimmen;
//                        }
//                    }
//                    exportDatei.newline();
//                }
//
//            }
//        }
//        exportDatei.schliessen();
//
//        exportDatei = new CaDateiWrite();
//        exportDatei.trennzeichen = ';';
//        exportDatei.dateiart = ".csv";
//        exportDatei.oeffne(lDbBundle, "sammelkarten" + Integer.toString(aktiverAbstimmungsblock.ident));
//
//        exportDatei.ausgabe("MeldeIdent");
//        exportDatei.newline();
//
//        /*Sammelkarten*/
//        int anzahlAbstimmungSplit = lDbBundle.dbAbstimmungMeldungSplit.leseIdent(-99);
//        if (anzahlAbstimmungSplit > 0) {
//            for (int i = 0; i < anzahlAbstimmungSplit; i++) {
//                EclAbstimmungMeldungSplit eclAbstimmungMeldungSammel = lDbBundle.dbAbstimmungMeldungSplit
//                        .ergebnisPositionGefunden(i);
//                if (eclAbstimmungMeldungSammel.meldungsIdent > 0) { /*sonst Summensatz*/
//                    exportDatei.ausgabe(Integer.toString(eclAbstimmungMeldungSammel.meldungsIdent));
//
//                    for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
//                        if (!istUeberschrift(i1)) {
//                            for (int i2 = 1; i2 < 10; i2++) {
//                                //								abstimmungsSummen[lieferePosInWeisung(i1)][i2]+=eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2];
//                                exportDatei.ausgabe(KonstStimmart.getText(i2) + ":" + Long
//                                        .toString(eclAbstimmungMeldungSammel.abgabe[lieferePosInWeisung(i1)][i2]));
//                            }
//                            exportDatei.ausgabe("XXX");
//                        }
//                    }
//                    exportDatei.newline();
//                }
//            }
//        }
//        exportDatei.schliessen();
//
//        return 1;
//    }

    /*Hinweis: 
     * In DbHVDatenLfd steht zugrundeliegende Präsenznummer für diesen Abstimmungslauf.
     * In abstimmungMeldungSplit (-10 bis -15) stehen die errechneten Aktiensummen je Stimmart insgesamt. => nur noch %-Werte sowie ggf. beim 
     * In abstimmungMeldungSplit (-20 bis -25) stehen die errechneten Aktiensummen je Stimmart nur für Briefwahl. => nur noch %-Werte sowie ggf. beim 
     * Subtraktionsverfahren auszuwertende Stimmart muß beim Ausdruck berechnet werden.
     * 
     */

    public String exportVerzeichnis = "";
    public String exportDateiname = "";

    /*********Übergreifende Variablen für drucken() und druckenEinzelnenAbstimmungspunkt();*/

    private RpVariablen rpVariablen = null;

    private EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummen = null;
    private EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummenBriefwahl = null;;

    private BlPraesenzSummen blPraesenzSummen = null;

    private CaDateiWrite dateiExport = null;

    /**Für Abstimmungsgruppen*/

    private class PclAbstimmungsGruppeGedruckt {
        boolean gedruckt = false;
    }

    private class PclAbstimmungsGruppe {
        List<Long> jaStimmen = null;
        List<Integer> gruppenOffset = null;
        List<PclAbstimmungsGruppeGedruckt> gedruckt = null;
        /*Anzahl der maximalen Stimmen*/
        int anzahlMax = 0;
    }

    private PclAbstimmungsGruppe[] abstimmungsgruppen = null;

    private void abstimmungsGruppenInitBasis() {
        abstimmungsgruppen = new PclAbstimmungsGruppe[11];
    }

    private void abstimmungsGruppeReInit(int gruppe) {
        abstimmungsgruppen[gruppe] = new PclAbstimmungsGruppe();
        abstimmungsgruppen[gruppe].jaStimmen = new LinkedList<Long>();
        abstimmungsgruppen[gruppe].gruppenOffset = new LinkedList<Integer>();
        abstimmungsgruppen[gruppe].gedruckt = new LinkedList<PclAbstimmungsGruppeGedruckt>();
    }

    private void abstimmungsGruppeInit(int gruppe, int anzahlMax) {
        abstimmungsGruppeReInit(gruppe);
        abstimmungsgruppen[gruppe].anzahlMax = anzahlMax;
    }

    private long abstimmungsGruppeLiefeMinimumStimmen(int gruppe) {
        long minimum = 0;
        int gef = -1;
        for (int i = 0; i < abstimmungsgruppen[gruppe].jaStimmen.size(); i++) {
            if (gef == -1) {//Noch nix gefunden bisher
                minimum = abstimmungsgruppen[gruppe].jaStimmen.get(i);
                gef = 1;
            } else {
                if (abstimmungsgruppen[gruppe].jaStimmen.get(i) < minimum) {
                    minimum = abstimmungsgruppen[gruppe].jaStimmen.get(i);
                }
            }

        }

        return minimum;
    }

    private void abstimmungsGruppeAddKandidat(int gruppe, int abstimmungsOffset, long stimmen) {
        abstimmungsgruppen[gruppe].jaStimmen.add(stimmen);
        CaBug.druckeLog("abstimmungsOffset=" + abstimmungsOffset, logDrucken, 10);
        abstimmungsgruppen[gruppe].gruppenOffset.add(abstimmungsOffset);
        abstimmungsgruppen[gruppe].gedruckt.add(new PclAbstimmungsGruppeGedruckt());
        return;
    }

    /**Ermittelt, wieviel Einträge mit stimmen vorhanden sind*/
    private int abstimmungsGruppeAnzahlMitStimmen(int gruppe, long stimmen) {
        int anz = 0;
        for (int i = 0; i < abstimmungsgruppen[gruppe].jaStimmen.size(); i++) {
            if (abstimmungsgruppen[gruppe].jaStimmen.get(i) == stimmen) {
                anz++;
            }
        }

        return anz;
    }

    private void abstimmungsGruppeEntferne(int gruppe, long stimmen) {
        List<Long> jaStimmen = abstimmungsgruppen[gruppe].jaStimmen;
        List<Integer> gruppenOffset = abstimmungsgruppen[gruppe].gruppenOffset;

        abstimmungsGruppeReInit(gruppe);

        for (int i = 0; i < jaStimmen.size(); i++) {
            if (jaStimmen.get(i) != stimmen) {
                abstimmungsGruppeAddKandidat(gruppe, gruppenOffset.get(i), jaStimmen.get(i));
            }
        }

    }

    /**Prüft, ob Kandidat aufgenommen werden kann, und wenn ja nimmt ihn auf*/
    private void abstimmungsGruppePruefeAddKandidat(int gruppe, int abstimmungsOffset, long stimmen) {
        if (abstimmungsgruppen[gruppe].jaStimmen.size() < abstimmungsgruppen[gruppe].anzahlMax) {
            /**Maximale Kandidatenzahl noch nicht erreicht - auf jeden Fall ergänzen*/
            abstimmungsGruppeAddKandidat(gruppe, abstimmungsOffset, stimmen);
            return;
        }

        /*Nun Minimum bestimmen*/
        long minimumStimmen = abstimmungsGruppeLiefeMinimumStimmen(gruppe);

        if (minimumStimmen > stimmen) {
            /*Aktuelle Stimmenzahl kleiner als alle bereits eingetragenen => keine Veränderung*/
            return;
        }
        if (minimumStimmen == stimmen) {
            /*Aktuelle Stimmenzahl gleich wie Minimum => Ergänzen, Stimmengleichheit*/
            abstimmungsGruppeAddKandidat(gruppe, abstimmungsOffset, stimmen);
            return;
        }
        /*Aktuelle Stimmenzahl größer als Minimum => Löschen (wenn möglich), und Aufnehmen*/
        int anzMinimum = abstimmungsGruppeAnzahlMitStimmen(gruppe, minimumStimmen);
        if (abstimmungsgruppen[gruppe].jaStimmen.size() + 1 - anzMinimum >= abstimmungsgruppen[gruppe].anzahlMax) {
            /*Entfernen*/
            abstimmungsGruppeEntferne(gruppe, minimumStimmen);
        }
        abstimmungsGruppeAddKandidat(gruppe, abstimmungsOffset, stimmen);

    }

    /**Holt den nächsten Kandidaten zum Ergebnisdrucken*/
    private int abstimmungsGruppeHoleNaechstenKandidatNachGroesse(int gruppe) {
        int gef = -1;
        long maxStimmen = -1;
        for (int i = 0; i < abstimmungsgruppen[gruppe].jaStimmen.size(); i++) {
            if (abstimmungsgruppen[gruppe].gedruckt.get(i).gedruckt == false) {
                long stimmen = abstimmungsgruppen[gruppe].jaStimmen.get(i);
                if (stimmen > maxStimmen) {
                    maxStimmen = stimmen;
                    gef = i;
                }
            }
        }
        if (gef == -1) {
            CaBug.drucke("BlAbstimmung.abstimmungsGruppeHoleNaechstenKandidatZumDruck 001");
        }
        abstimmungsgruppen[gruppe].gedruckt.get(gef).gedruckt = true;
        return gef;
    }

    private void abstimmungsGruppeSortiere(int gruppe) {
        List<Long> jaStimmen = new LinkedList<Long>();
        List<Integer> gruppenOffset = new LinkedList<Integer>();
        List<PclAbstimmungsGruppeGedruckt> gedruckt = new LinkedList<PclAbstimmungsGruppeGedruckt>();

        for (int i = 0; i < abstimmungsgruppen[gruppe].jaStimmen.size(); i++) {
            int offset = abstimmungsGruppeHoleNaechstenKandidatNachGroesse(gruppe);
            gruppenOffset
                    .add(abstimmungsgruppen[gruppe].gruppenOffset.get(offset)); /*Früher: gruppenOffset.add(offset)*/
            jaStimmen.add(abstimmungsgruppen[gruppe].jaStimmen.get(offset));
            gedruckt.add(new PclAbstimmungsGruppeGedruckt());
        }

        abstimmungsgruppen[gruppe].jaStimmen = jaStimmen;
        abstimmungsgruppen[gruppe].gruppenOffset = gruppenOffset;
        abstimmungsgruppen[gruppe].gedruckt = gedruckt;
    }

    private int abstimmungsGruppeAnzahl(int gruppe) {
        return abstimmungsgruppen[gruppe].jaStimmen.size();
    }

    
    
    /** Überprüfen, ob Subtraktion / Addion in der Auszuwertenden Liste vorhanden, dann
     * Variable entsprechend füllen
     */
   private void belegeAdditionSubtraktion(RpDrucken rpDrucken, RpVariablen rpVariablen) {
       boolean subtraktionGefunden=false;
       boolean additionGefunden=false;
       for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
           if (abstimmungen[i1].liefereIstUeberschift()==false) {
               if (abstimmungen[i1].stimmenAuswerten==-1) {
                   additionGefunden=true;
               }
               else {
                   subtraktionGefunden=true;
               }
           }
       } /*Ende Schleife für alle Abstimmungen*/
       String additionSubtraktion="";
       if (subtraktionGefunden) {
           additionSubtraktion+="S"; 
       }
       if (additionGefunden) {
           additionSubtraktion+="A"; 
       }
       rpVariablen.fuelleVariable(rpDrucken, "AdditionSubtraktion", additionSubtraktion);
        
    }
    
    /**
     * rpDrucken: kann null sein, wenn listeOderBlattOderExport==3
     * 
     * listeOderBlattOderExport: 1 => Ergebnisliste; 2=Verkündungsblatt; 3=Nur fester Text-Eport für Power-Point
     * bei listeOderBlattOderExport==3 müssen exportVerzeichnis und exportDateiname gefüllt werden!
     * 
     * lfdNummer wird verwendet für Listendruck und Formular
     * 
     * powerPointErzeugen=true => bei Listendruck wird ggf. "aufgeblättert"
     * 
     * */
    public int drucken(RpDrucken rpDrucken, int listeOderBlattOderExport, String lfdNummer,
            boolean powerPointErzeugen) {

        /*Gruppen initialisieren*/
        abstimmungsGruppenInitBasis();
        for (int i = 1; i <= 10; i++) {
            abstimmungsGruppeInit(i, lDbBundle.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i]);
        }

        rpVariablen = null;

        dateiExport = new CaDateiWrite();
        if (listeOderBlattOderExport == 3) {
            dateiExport.trennzeichen = ';';
            dateiExport.dateiart = ".csv";
            dateiExport.oeffneNameExplizit(lDbBundle, exportVerzeichnis + exportDateiname);
        }

        /*Summensatz einlesen
         * Gesamtsumme 0 wird - aus technischen Gründen - auf [6] gelegt*/
        eclAbstimmungMeldungSummen = new EclAbstimmungMeldungSplit[7];
        eclAbstimmungMeldungSummenBriefwahl = new EclAbstimmungMeldungSplit[7];

        for (int i = 0; i <= 5; i++) {
            int ident = (i + 10) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden - Fehler*/
                return -1;
            }

            /*Abstimmungssplit vorhanden*/
            if (i != 0) {
                eclAbstimmungMeldungSummen[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            } else {
                eclAbstimmungMeldungSummen[6] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        for (int i = 0; i <= 5; i++) {
            int ident = (i + 20) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (i != 0) {
                eclAbstimmungMeldungSummenBriefwahl[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            } else {
                eclAbstimmungMeldungSummenBriefwahl[6] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        /*Zu Basis liegende Präsenz einlesen*/
        int verwendetePraesenznummer = 0;
        /*AAAAA - AZugriff auf EclHVDatenLfd eigentlich nicht nötig! (11.05.2023)*/
        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 72;
        lHVDatenLfd.benutzer = aktiverAbstimmungsblock.ident;
        lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
        lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
        verwendetePraesenznummer = Integer.parseInt(lHVDatenLfd.wert);

        /*Präsenzsummen einlesen*/
        blPraesenzSummen = new BlPraesenzSummen(lDbBundle);
        blPraesenzSummen.leseSummenFuerListe(verwendetePraesenznummer);

        if (listeOderBlattOderExport == 1) {/*Liste initialisieren*/
            rpDrucken.initListe(lDbBundle/*, true, false*/);

            rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.abstimmungErgebnisListe(lfdNummer, rpDrucken);

            belegeAdditionSubtraktion(rpDrucken, rpVariablen);
            rpDrucken.startListe(/*true*/);
        }
        if (listeOderBlattOderExport == 2) { /*Formular initialisieren*/
            rpDrucken.initFormular(lDbBundle);

            rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.abstimmungVerleseblatt(lfdNummer, rpDrucken);

            belegeAdditionSubtraktion(rpDrucken, rpVariablen);
            rpDrucken.startFormular(/*true*/);

        }
        if (listeOderBlattOderExport == 3) {
            dateiExport.ausgabe("TOP");
            dateiExport.ausgabe("gültigeStimmen");
            dateiExport.ausgabe("Ja");
            dateiExport.ausgabe("Nein");
            dateiExport.ausgabe("Ja-Prozent");
            dateiExport.ausgabe("Nein-Prozent");
            dateiExport.ausgabe("Enthaltung");
            dateiExport.ausgabe("GültigeStimmenProzentvomGK");
            dateiExport.newline();
        }

        if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
            rpVariablen.fuelleVariable(rpDrucken, "Gruppenergebnis", "0");
        }

        
        
        int auswertenPPListeZeilen = lDbBundle.param.paramAbstimmungParameter.auswertenPPListeZeilen;
        if (listeOderBlattOderExport != 1 || auswertenPPListeZeilen == 0 || powerPointErzeugen == false) {
            for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                druckenEinzelnenAbstimmungspunkt(i1, rpDrucken, listeOderBlattOderExport, lfdNummer, true);
            } /*Ende Schleife für alle Abstimmungen*/
        } else {
            int anzahlGedruckt = 0;
            int offsetBeginnSeite = 0;
            for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                if (anzahlGedruckt == auswertenPPListeZeilen) {
                    anzahlGedruckt = 0;
                    offsetBeginnSeite = i1;
                }
                if (i1 != 0) {
                    rpDrucken.newPageListe();
                }
                for (int i3 = offsetBeginnSeite; i3 < i1; i3++) {
                    druckenEinzelnenAbstimmungspunkt(i3, rpDrucken, listeOderBlattOderExport, lfdNummer, false);
                }
                druckenEinzelnenAbstimmungspunkt(i1, rpDrucken, listeOderBlattOderExport, lfdNummer, true);
                anzahlGedruckt++;
            } /*Ende Schleife für alle Abstimmungen*/

        }

        /**Nun noch ggf. Gruppen-Ergebnisse ausgeben*/
        for (int i = 1; i <= 10; i++) {
            if (lDbBundle.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i] != 0) {
                if (abstimmungsGruppeAnzahl(i) > 0) {
                    if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
                        rpVariablen.fuelleVariable(rpDrucken, "Gruppenergebnis", "1");
                    }
                    if (listeOderBlattOderExport == 1) {
                        /*bei Liste: neue Seite*/
                        rpDrucken.newPageListe();
                    }
                    abstimmungsGruppeSortiere(i);

                    if (listeOderBlattOderExport != 1 || auswertenPPListeZeilen == 0 || powerPointErzeugen == false) {
                        CaBug.druckeLog("Variante 1", logDrucken, 10);
                        for (int i2 = 0; i2 < abstimmungsgruppen[i].gruppenOffset.size(); i2++) {
                            int i1 = abstimmungsgruppen[i].gruppenOffset.get(i2);
                            druckenEinzelnenAbstimmungspunkt(i1, rpDrucken, listeOderBlattOderExport, lfdNummer, false);
                        }
                    } else {
                        CaBug.druckeLog("Variante 2", logDrucken, 10);
                        int anzahlGedruckt = 0;
                        int offsetBeginnSeite = 0;
                        for (int i2 = 0; i2 < abstimmungsgruppen[i].gruppenOffset.size(); i2++) {

                            if (anzahlGedruckt == auswertenPPListeZeilen) {
                                anzahlGedruckt = 0;
                                offsetBeginnSeite = i2;
                            }
                            if (i2 != 0) {
                                rpDrucken.newPageListe();
                            }
                            for (int i3 = offsetBeginnSeite; i3 < i2; i3++) {
                                int i1 = abstimmungsgruppen[i].gruppenOffset.get(i3);
                                druckenEinzelnenAbstimmungspunkt(i1, rpDrucken, listeOderBlattOderExport, lfdNummer,
                                        false);
                            }

                            int i1 = abstimmungsgruppen[i].gruppenOffset.get(i2);
                            druckenEinzelnenAbstimmungspunkt(i1, rpDrucken, listeOderBlattOderExport, lfdNummer, false);
                            anzahlGedruckt++;
                        } /*Ende Schleife für alle Abstimmungen*/

                    }
                }

            }

        }

        if (listeOderBlattOderExport == 1) {/*Liste Beenden*/
            rpDrucken.endeListe();
        }
        if (listeOderBlattOderExport == 2) { /*Formular beenden*/
            rpDrucken.endeFormular();

        }
        if (listeOderBlattOderExport == 3) {

            dateiExport.schliessen();
        }
        return 1;
    }

    /**kandidatEintragen=true => normale Ergebnisausgabe (d.h. bei Gruppen wird Kandidat in das Gruppen-Ergebnis eingetragen)
     * kandidatEintragen=false => Kandidat wird nicht in das Gruppen-Ergebnis eingetragen (entweder bei Wiederholungszeilen, oder bei Gruppen-Ergebnis-Ausdruck)
     */
    private void druckenEinzelnenAbstimmungspunkt(int i1, RpDrucken rpDrucken, int listeOderBlattOderExport,
            String lfdNummer, boolean kandidatEintragen) {
        System.out.println("Auswerte-Druck = " + i1);
        /*[0]=unbelegt; [1..5] für jeweilige Gattung; [6]=Summen*/
        long[] gesamtPraesenz = { 0, 0, 0, 0, 0, 0, 0 };
        double[] gesamtPraesenzKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] wertEinerAktie = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] gesamtKapitalStimmen = { 0, 0, 0, 0, 0, 0, 0 };
        double[] gesamtKapitalKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] gesamtKapitalVermindertStimmen = { 0, 0, 0, 0, 0, 0, 0 };
        double[] gesamtKapitalVermindertKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] stimmenJa = { 0, 0, 0, 0, 0, 0, 0 };
        double[] stimmenJaKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] stimmenNein = { 0, 0, 0, 0, 0, 0, 0 };
        double[] stimmenNeinKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] jaUndNeinStimmen = { 0, 0, 0, 0, 0, 0, 0 };
        double[] jaUndNeinStimmenKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] prozentJa = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] prozentJaKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] prozentNein = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] prozentNeinKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] gesamtPraesenzBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        long[] stimmenJaBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        long[] stimmenNeinBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        long[] jaUndNeinStimmenBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        double[] prozentJaBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] prozentNeinBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        long[] stimmenJaOhneBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        long[] stimmenNeinOhneBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        long[] jaUndNeinStimmenOhneBriefwahl = { 0, 0, 0, 0, 0, 0, 0 };
        double[] prozentJaOhneBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] prozentNeinOhneBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] abgegebeneGueltigeStimmenKapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapital = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalGesamt = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalVermindert = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] abgegebeneGueltigeStimmenKapitalBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahlGesamt = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

        double[] abgegebeneGueltigeStimmenKapitalOhneBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        double[] abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahlGesamt = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0 };

        int angenommen = 0;
        String topImmer = abstimmungen[i1].nummer;
        int z = i1;
        while (topImmer.isEmpty() && z > 0) {
            z--;
            topImmer = abstimmungen[z].nummer;
        }

        if (abstimmungen[i1].stimmenAuswerten != -1) { //Wird bei Subtraktionsverfahren in der Schleife gefüllt.
            eclAbstimmungMeldungSummen[6].abgabe[lieferePosInWeisung(i1)][abstimmungen[i1].stimmenAuswerten] = 0;
            eclAbstimmungMeldungSummenBriefwahl[6].abgabe[lieferePosInWeisung(
                    i1)][abstimmungen[i1].stimmenAuswerten] = 0;
        }

        /**Schleife: Werte für die einzelnen Gattungen berechnen. [6] wird dabei per Summen gefüllt*/
        for (int gattung = 1; gattung <= 5; gattung++) {

            if (!istUeberschrift(i1)) {
//                gesamtPraesenz[gattung] = blPraesenzSummen.praesenzSummeBerechnetGattung[gattung - 1] + blPraesenzSummen.briefwahlSummeBerechnetGattung[gattung - 1];
                gesamtPraesenz[gattung] = blPraesenzSummen.praesenzSummeAusListeGattung[gattung - 1] + blPraesenzSummen.briefwahlSummeBerechnetGattung[gattung - 1];
                gesamtPraesenzKapital[gattung] = lDbBundle.param.paramBasis.getKapitalWertFuerAktienzahl(gesamtPraesenz[gattung], gattung);
                gesamtPraesenz[6] += gesamtPraesenz[gattung];
                gesamtPraesenzKapital[6] += gesamtPraesenzKapital[gattung];

                gesamtPraesenzBriefwahl[gattung] = blPraesenzSummen.briefwahlSummeBerechnetGattung[gattung - 1];
                gesamtPraesenzBriefwahl[6] += gesamtPraesenzBriefwahl[gattung];

                wertEinerAktie[gattung] = lDbBundle.param.paramBasis.getWertEinerAktie(gattung);

                gesamtKapitalStimmen[gattung] = lDbBundle.param.paramBasis.getGrundkapitalStueck(gattung);
                gesamtKapitalStimmen[6] += gesamtKapitalStimmen[gattung];

                gesamtKapitalKapital[gattung] = lDbBundle.param.paramBasis.getGrundkapitalEuro(gattung);
                gesamtKapitalKapital[6] += gesamtKapitalKapital[gattung];

                gesamtKapitalVermindertStimmen[gattung] = lDbBundle.param.paramBasis.getGrundkapitalVermindertStueck(gattung);
                gesamtKapitalVermindertStimmen[6] += gesamtKapitalVermindertStimmen[gattung];

                gesamtKapitalVermindertKapital[gattung] = lDbBundle.param.paramBasis.getGrundkapitalVermindertEuro(gattung);
                gesamtKapitalVermindertKapital[6] += gesamtKapitalVermindertKapital[gattung];

                /*Falls Subtraktionsverfahren: Stimmen berechnen*/
                if (abstimmungen[i1].stimmenAuswerten != -1) {

                    long ermittelteStimmen = gesamtPraesenz[gattung];
                    long ermittelteStimmenBriefwahl = gesamtPraesenzBriefwahl[gattung];
                    for (int i2 = 1; i2 <= 9; i2++) {
                        if (i2 != KonstStimmart.frei) {
                            if (abstimmungen[i1].stimmenAuswerten != i2) {
                                ermittelteStimmen -= eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                                        i1)][i2];
                                ermittelteStimmenBriefwahl -= eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                                        i1)][i2];
                            }
                        }
                    }
                    eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                            i1)][abstimmungen[i1].stimmenAuswerten] = ermittelteStimmen;
                    eclAbstimmungMeldungSummen[6].abgabe[lieferePosInWeisung(
                            i1)][abstimmungen[i1].stimmenAuswerten] += ermittelteStimmen;

                    eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                            i1)][abstimmungen[i1].stimmenAuswerten] = ermittelteStimmenBriefwahl;
                    eclAbstimmungMeldungSummenBriefwahl[6].abgabe[lieferePosInWeisung(
                            i1)][abstimmungen[i1].stimmenAuswerten] += ermittelteStimmenBriefwahl;
                }

                /*Prozent berechnen - Gesamt*/
                stimmenJa[gattung] = eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.ja];
                stimmenJaKapital[gattung] = lDbBundle.param.paramBasis.getKapitalWertFuerAktienzahl(stimmenJa[gattung],
                        gattung);
                stimmenJa[6] += stimmenJa[gattung];
                stimmenJaKapital[6] += stimmenJaKapital[gattung];

                stimmenNein[gattung] = eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.nein];
                stimmenNeinKapital[gattung] = lDbBundle.param.paramBasis
                        .getKapitalWertFuerAktienzahl(stimmenNein[gattung], gattung);
                stimmenNein[6] += stimmenNein[gattung];
                stimmenNeinKapital[6] += stimmenNeinKapital[gattung];

                jaUndNeinStimmen[gattung] = stimmenJa[gattung] + stimmenNein[gattung];
                CaBug.druckeLog("BlAbstimmung Berechnung gattung=" + gattung + " stimmenJa[gattung]="
                        + stimmenJa[gattung] + " stimmenNein[gattung]=" + stimmenNein[gattung]
                                + " jaUndNeinStimmen[gattung]=" + jaUndNeinStimmen[gattung], logDrucken, 10);
                jaUndNeinStimmenKapital[gattung] = lDbBundle.param.paramBasis
                        .getKapitalWertFuerAktienzahl(jaUndNeinStimmen[gattung], gattung);
                jaUndNeinStimmen[6] += jaUndNeinStimmen[gattung];
                jaUndNeinStimmenKapital[6] += jaUndNeinStimmenKapital[gattung];

                if (jaUndNeinStimmen[gattung]!=0) {
                    prozentJa[gattung] = (double) stimmenJa[gattung] / (double) jaUndNeinStimmen[gattung] * 100;
                    prozentJa[gattung] = Math.round(prozentJa[gattung] * 100) / 100.0;
                    if (prozentJa[gattung] == 0.0 && stimmenJa[gattung] > 0) {
                        prozentJa[gattung] = 0.01;
                    }

                    prozentNein[gattung] = (double) stimmenNein[gattung] / (double) jaUndNeinStimmen[gattung] * 100;
                    prozentNein[gattung] = Math.round(prozentNein[gattung] * 100) / 100.0;
                    if (prozentNein[gattung] == 0.0 && stimmenNein[gattung] > 0) {
                        prozentNein[gattung] = 0.01;
                    }

                    if (stimmenJa[gattung] > stimmenNein[gattung]) {
                        prozentJa[gattung] = 100.0 - prozentNein[gattung];
                    } else {
                        prozentNein[gattung] = 100.0 - prozentJa[gattung];
                    }
                }
                else {
                    prozentJa[gattung]=0.0;
                    prozentNein[gattung]=0.0;
                }
                
                prozentJaKapital[gattung] = prozentJa[gattung];
                prozentNeinKapital[gattung] = prozentNein[gattung];

                /*Prozent berechnen - Briefwahl*/
                stimmenJaBriefwahl[gattung] = eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.ja];
                stimmenJaBriefwahl[6] += stimmenJaBriefwahl[gattung];

                stimmenNeinBriefwahl[gattung] = eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.nein];
                stimmenNeinBriefwahl[6] += stimmenNeinBriefwahl[gattung];

                jaUndNeinStimmenBriefwahl[gattung] = stimmenJaBriefwahl[gattung] + stimmenNeinBriefwahl[gattung];
                jaUndNeinStimmenBriefwahl[6] += jaUndNeinStimmenBriefwahl[gattung];

                if (jaUndNeinStimmenBriefwahl[gattung]!=0) {

                    prozentJaBriefwahl[gattung] = (double) stimmenJaBriefwahl[gattung]
                            / (double) jaUndNeinStimmenBriefwahl[gattung] * 100;
                    prozentJaBriefwahl[gattung] = Math.round(prozentJaBriefwahl[gattung] * 100) / 100.0;
                    if (prozentJaBriefwahl[gattung] == 0.0 && stimmenJaBriefwahl[gattung] > 0) {
                        prozentJaBriefwahl[gattung] = 0.01;
                    }

                    prozentNeinBriefwahl[gattung] = (double) stimmenNeinBriefwahl[gattung]
                            / (double) jaUndNeinStimmenBriefwahl[gattung] * 100;
                    prozentNeinBriefwahl[gattung] = Math.round(prozentNeinBriefwahl[gattung] * 100) / 100.0;
                    if (prozentNeinBriefwahl[gattung] == 0.0 && stimmenNeinBriefwahl[gattung] > 0) {
                        prozentNeinBriefwahl[gattung] = 0.01;
                    }

                    if (stimmenJaBriefwahl[gattung] > stimmenNeinBriefwahl[gattung]) {
                        prozentJaBriefwahl[gattung] = 100.0 - prozentNeinBriefwahl[gattung];
                    } else {
                        prozentNeinBriefwahl[gattung] = 100.0 - prozentJaBriefwahl[gattung];
                    }
                }
                else {
                    prozentJaBriefwahl[gattung]=0.0;
                    prozentNeinBriefwahl[gattung]=0.0;
                }
                
                /*Prozent berechnen - ohneBriefwahl*/
                stimmenJaOhneBriefwahl[gattung] = eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.ja]
                        - eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                                i1)][KonstStimmart.ja];
                stimmenJaOhneBriefwahl[6] += stimmenJaOhneBriefwahl[gattung];

                stimmenNeinOhneBriefwahl[gattung] = eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                        i1)][KonstStimmart.nein]
                        - eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                                i1)][KonstStimmart.nein];
                stimmenNeinOhneBriefwahl[6] += stimmenNeinOhneBriefwahl[gattung];

                jaUndNeinStimmenOhneBriefwahl[gattung] = stimmenJaOhneBriefwahl[gattung]
                        + stimmenNeinOhneBriefwahl[gattung];
                jaUndNeinStimmenOhneBriefwahl[6] += jaUndNeinStimmenOhneBriefwahl[gattung];

                if (jaUndNeinStimmenOhneBriefwahl[gattung]!=0) {
                prozentJaOhneBriefwahl[gattung] = (double) stimmenJaOhneBriefwahl[gattung]
                        / (double) jaUndNeinStimmenOhneBriefwahl[gattung] * 100;
                prozentJaOhneBriefwahl[gattung] = Math.round(prozentJaOhneBriefwahl[gattung] * 100) / 100.0;
                if (prozentJaOhneBriefwahl[gattung] == 0.0 && stimmenJaOhneBriefwahl[gattung] > 0) {
                    prozentJaOhneBriefwahl[gattung] = 0.01;
                }

                prozentNeinOhneBriefwahl[gattung] = (double) stimmenNeinOhneBriefwahl[gattung]
                        / (double) jaUndNeinStimmenOhneBriefwahl[gattung] * 100;
                prozentNeinOhneBriefwahl[gattung] = Math.round(prozentNeinOhneBriefwahl[gattung] * 100) / 100.0;
                if (prozentNeinOhneBriefwahl[gattung] == 0.0 && stimmenNeinOhneBriefwahl[gattung] > 0) {
                    prozentNeinOhneBriefwahl[gattung] = 0.01;
                }

                if (stimmenJaOhneBriefwahl[gattung] > stimmenNeinOhneBriefwahl[gattung]) {
                    prozentJaOhneBriefwahl[gattung] = 100.0 - prozentNeinOhneBriefwahl[gattung];
                } else {
                    prozentNeinOhneBriefwahl[gattung] = 100.0 - prozentJaOhneBriefwahl[gattung];
                }
                }
                else {
                    prozentJaOhneBriefwahl[gattung]=0.0;
                    prozentNeinOhneBriefwahl[gattung]=0.0;
                }

                /*Kapital-Stimmen*/
                abgegebeneGueltigeStimmenKapital[gattung] = lDbBundle.param.paramBasis
                        .getKapitalWertFuerAktienzahl(jaUndNeinStimmen[gattung], gattung);
                abgegebeneGueltigeStimmenKapital[6] += abgegebeneGueltigeStimmenKapital[gattung];

                abgegebeneGueltigeStimmenKapitalBriefwahl[gattung] = lDbBundle.param.paramBasis.getKapitalWertFuerAktienzahl(jaUndNeinStimmenBriefwahl[gattung], gattung);
                abgegebeneGueltigeStimmenKapitalBriefwahl[6] += abgegebeneGueltigeStimmenKapitalBriefwahl[gattung];

                abgegebeneGueltigeStimmenKapitalOhneBriefwahl[gattung] = lDbBundle.param.paramBasis
                        .getKapitalWertFuerAktienzahl(jaUndNeinStimmenOhneBriefwahl[gattung], gattung);
                abgegebeneGueltigeStimmenKapitalOhneBriefwahl[6] += abgegebeneGueltigeStimmenKapitalOhneBriefwahl[gattung];

                /*Kapital-% - im Vergleich zum Gesamtkapital der Gattung*/
                abgegebeneGueltigeStimmenProzentGrundkapital[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmen[gattung], lDbBundle.param.paramBasis.getGrundkapitalStueck(gattung));
                abgegebeneGueltigeStimmenProzentGrundkapital[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapital[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmen[gattung], lDbBundle.param.paramBasis.getGrundkapitalVermindertStueck(gattung));
                abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmenBriefwahl[gattung], lDbBundle.param.paramBasis.getGrundkapitalStueck(gattung));
                abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmenOhneBriefwahl[gattung],
                        lDbBundle.param.paramBasis.getGrundkapitalStueck(gattung));
                abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[gattung]);

                /*Kapital-% - im Vergleich zum Gesamtkapital (Summe aller Gattungen)*/
                abgegebeneGueltigeStimmenProzentGrundkapitalGesamt[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmen[gattung], lDbBundle.param.paramBasis.grundkapitalStueckGesamt());
                abgegebeneGueltigeStimmenProzentGrundkapitalGesamt[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalGesamt[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmen[gattung], lDbBundle.param.paramBasis.grundkapitalVermindertStueckGesamt());
                abgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahlGesamt[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmenBriefwahl[gattung], lDbBundle.param.paramBasis.grundkapitalStueckGesamt());
                abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahlGesamt[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahlGesamt[gattung]);

                abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahlGesamt[gattung] = CaString.berechneProzent(
                        jaUndNeinStimmenOhneBriefwahl[gattung], lDbBundle.param.paramBasis.grundkapitalStueckGesamt());
                abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahlGesamt[gattung] = CaString
                        .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahlGesamt[gattung]);

            } /**Ende !isueberschrift*/
        } /*Ende Gattungsschleife*/

        /**Restliche Werte in [6] ablegen*/

        /*Prozent berechnen - Gesamt*/
        if (jaUndNeinStimmen[6]!=0) {
            prozentJa[6] = (double) stimmenJa[6] / (double) jaUndNeinStimmen[6] * 100;
            prozentJa[6] = Math.round(prozentJa[6] * 100) / 100.0;
            if (prozentJa[6] == 0.0 && stimmenJa[6] > 0) {
                prozentJa[6] = 0.01;
            }

            prozentNein[6] = (double) stimmenNein[6] / (double) jaUndNeinStimmen[6] * 100;
            prozentNein[6] = Math.round(prozentNein[6] * 100) / 100.0;
            if (prozentNein[6] == 0.0 && stimmenNein[6] > 0) {
                prozentNein[6] = 0.01;
            }

            if (stimmenJa[6] > stimmenNein[6]) {
                prozentJa[6] = 100.0 - prozentNein[6];
            } else {
                prozentNein[6] = 100.0 - prozentJa[6];
            }
        }
        else {
            prozentJa[6]=0.0;
            prozentNein[6]=0.0;
        }

        /*Prozent berechnen - Briefwahl*/
        if (jaUndNeinStimmenBriefwahl[6]!=0) {
            prozentJaBriefwahl[6] = (double) stimmenJaBriefwahl[6] / (double) jaUndNeinStimmenBriefwahl[6] * 100;
            prozentJaBriefwahl[6] = Math.round(prozentJaBriefwahl[6] * 100) / 100.0;
            if (prozentJaBriefwahl[6] == 0.0 && stimmenJaBriefwahl[6] > 0) {
                prozentJaBriefwahl[6] = 0.01;
            }

            prozentNeinBriefwahl[6] = (double) stimmenNeinBriefwahl[6] / (double) jaUndNeinStimmenBriefwahl[6] * 100;
            prozentNeinBriefwahl[6] = Math.round(prozentNeinBriefwahl[6] * 100) / 100.0;
            if (prozentNeinBriefwahl[6] == 0.0 && stimmenNeinBriefwahl[6] > 0) {
                prozentNeinBriefwahl[6] = 0.01;
            }

            if (stimmenJaBriefwahl[6] > stimmenNeinBriefwahl[6]) {
                prozentJaBriefwahl[6] = 100.0 - prozentNeinBriefwahl[6];
            } else {
                prozentNeinBriefwahl[6] = 100.0 - prozentJaBriefwahl[6];
            }
        }
        else {
            prozentJaBriefwahl[6]=0.0;
            prozentNeinBriefwahl[6]=0.0;
        }

        /*Prozent berechnen - ohneBriefwahl*/
        if (jaUndNeinStimmenOhneBriefwahl[6]!=0) {
            prozentJaOhneBriefwahl[6] = (double) stimmenJaOhneBriefwahl[6] / (double) jaUndNeinStimmenOhneBriefwahl[6]
                    * 100;
            prozentJaOhneBriefwahl[6] = Math.round(prozentJaOhneBriefwahl[6] * 100) / 100.0;
            if (prozentJaOhneBriefwahl[6] == 0.0 && stimmenJaOhneBriefwahl[6] > 0) {
                prozentJaOhneBriefwahl[6] = 0.01;
            }

            prozentNeinOhneBriefwahl[6] = (double) stimmenNeinOhneBriefwahl[6] / (double) jaUndNeinStimmenOhneBriefwahl[6]
                    * 100;
            prozentNeinOhneBriefwahl[6] = Math.round(prozentNeinOhneBriefwahl[6] * 100) / 100.0;
            if (prozentNeinOhneBriefwahl[6] == 0.0 && stimmenNeinOhneBriefwahl[6] > 0) {
                prozentNeinOhneBriefwahl[6] = 0.01;
            }

            if (stimmenJaOhneBriefwahl[6] > stimmenNeinOhneBriefwahl[6]) {
                prozentJaOhneBriefwahl[6] = 100.0 - prozentNeinOhneBriefwahl[6];
            } else {
                prozentNeinOhneBriefwahl[6] = 100.0 - prozentJaOhneBriefwahl[6];
            }
        }
        else {
            prozentJaOhneBriefwahl[6]=0.0;
            prozentNeinOhneBriefwahl[6]=0.0;
        }

        /*Prozent berechnen - Ja+Nein-Kapital*/
        if (jaUndNeinStimmenKapital[6]!=0) {
            prozentJaKapital[6] = stimmenJaKapital[6] / jaUndNeinStimmenKapital[6] * 100;
            prozentJaKapital[6] = Math.round(prozentJaKapital[6] * 100) / 100.0;
            if (prozentJaKapital[6] == 0.0 && stimmenJaKapital[6] > 0) {
                prozentJaKapital[6] = 0.01;
            }

            prozentNeinKapital[6] = stimmenNeinKapital[6] / jaUndNeinStimmenKapital[6] * 100;
            prozentNeinKapital[6] = Math.round(prozentNeinKapital[6] * 100) / 100.0;
            if (prozentNeinKapital[6] == 0.0 && stimmenNeinKapital[6] > 0) {
                prozentNeinKapital[6] = 0.01;
            }

            if (stimmenJaKapital[6] > stimmenNeinKapital[6]) {
                prozentJaKapital[6] = 100.0 - prozentNeinKapital[6];
            } else {
                prozentNeinKapital[6] = 100.0 - prozentJaKapital[6];
            }
        }
        else {
            prozentJaKapital[6]=0.0;
            prozentNeinKapital[6]=0.0;
        }

        /*Kapital-%*/
        abgegebeneGueltigeStimmenProzentGrundkapital[6] = CaString.berechneProzent(abgegebeneGueltigeStimmenKapital[6],
                gesamtKapitalKapital[6]);
        System.out.println("abgegebeneGueltigeStimmenProzentGrundkapital[6]");
        System.out.println("abgegebeneGueltigeStimmenKapital[6]=" + abgegebeneGueltigeStimmenKapital[6]);
        System.out.println("gesamtKapitalKapital[6]=" + (long) gesamtKapitalKapital[6]);
        System.out.println(
                "abgegebeneGueltigeStimmenProzentGrundkapital[6]=" + abgegebeneGueltigeStimmenProzentGrundkapital[6]);

        abgegebeneGueltigeStimmenProzentGrundkapital[6] = CaString
                .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapital[6]);

        abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[6] = CaString
                .berechneProzent(abgegebeneGueltigeStimmenKapital[6], gesamtKapitalVermindertKapital[6]);
        abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[6] = CaString
                .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[6]);

        abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[6] = CaString.berechneProzent(abgegebeneGueltigeStimmenKapitalBriefwahl[6], gesamtKapitalKapital[6]);
        abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[6] = CaString.runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[6]);

        abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[6] = CaString
                .berechneProzent(abgegebeneGueltigeStimmenKapitalOhneBriefwahl[6], gesamtKapitalKapital[6]);
        abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[6] = CaString
                .runde2Stellen(abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[6]);

        /*Schleife: Variablen füllen*/

        if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
            switch (lDbBundle.param.paramAbstimmungParameter.textVerwendenFormular) {
            case 1:
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Standardtext",
                        abstimmungen[i1].kurzBezeichnung, listeOderBlattOderExport);
                break;
            case 2:
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Standardtext",
                        abstimmungen[i1].anzeigeBezeichnungKurz, listeOderBlattOderExport);
                break;
            default:
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Standardtext",
                        abstimmungen[i1].anzeigeBezeichnungLang, listeOderBlattOderExport);
                break;
            }
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Minitext", abstimmungen[i1].kurzBezeichnung, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Kurztext", abstimmungen[i1].liefereAnzeigeBezeichnungKurz(), listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Langtext", abstimmungen[i1].anzeigeBezeichnungLang, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Kandidat", abstimmungen[i1].kandidat, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPIdent", Integer.toString(abstimmungen[i1].ident), listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOP", abstimmungen[i1].nummer, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPimmer", topImmer, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Index", abstimmungen[i1].nummerindex, listeOderBlattOderExport);
            
            if (abstimmungen[i1].liefereIstUeberschift()) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.istUeberschrift", "1", listeOderBlattOderExport);
            }
            else {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.istUeberschrift", "0", listeOderBlattOderExport);
            }

            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPKey", abstimmungen[i1].nummerKey, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPIndexKey", abstimmungen[i1].nummerindexKey, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPFormular", abstimmungen[i1].nummerFormular, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPIndexFormular", abstimmungen[i1].nummerindexFormular, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPListe", abstimmungen[i1].nummer, listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.TOPIndexListe", abstimmungen[i1].nummerindex, listeOderBlattOderExport);

            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.VeroeffentlichtBundesanzeiger", lDbBundle.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom,
                    listeOderBlattOderExport);
            if (lDbBundle.param.paramModuleKonfigurierbar.briefwahl) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.BriefwahlIstAktiv", "1", listeOderBlattOderExport);
            } else {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.BriefwahlIstAktiv", "0", listeOderBlattOderExport);
            }
            if (lDbBundle.param.liefereBriefwahlAusgebenInAbstimmungsergebnis()==1) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.BriefwahlAusgebenInAbstimmungsergebnis", "1", listeOderBlattOderExport);
            } else {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.BriefwahlAusgebenInAbstimmungsergebnis", "0", listeOderBlattOderExport);
            }

            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.gestelltVon", Integer.toString(abstimmungen[i1].beschlussvorschlagGestelltVon), listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.gestelltVonSonstige", abstimmungen[i1].beschlussvorschlagGestelltVonSonstige, listeOderBlattOderExport);

            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.ErforderlicheMehrheit", Integer.toString(abstimmungen[i1].identErforderlicheMehrheit), listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.GegenantraegeGestellt", Integer.toString(abstimmungen[i1].gegenantraegeGestellt), listeOderBlattOderExport);

            if (abstimmungen[i1].stimmenAuswerten==-1) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.VerfahrenSOderA", "A", listeOderBlattOderExport);
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.StimmartAuswerten", "", listeOderBlattOderExport);
            }
            else {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.VerfahrenSOderA", "S", listeOderBlattOderExport);
                String lStimmenAuswerten="";
                switch (abstimmungen[i1].stimmenAuswerten) {
                case KonstStimmart.ja:
                    lStimmenAuswerten="J";
                    break;
                case KonstStimmart.nein:
                    lStimmenAuswerten="N";
                    break;
                case KonstStimmart.enthaltung:
                    lStimmenAuswerten="E";
                    break;
                case KonstStimmart.nichtTeilnahme:
                    lStimmenAuswerten="NT";
                    break;
                }
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.StimmartAuswerten", lStimmenAuswerten, listeOderBlattOderExport);
            }

            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.IstGegenantrag", Integer.toString(abstimmungen[i1].gegenantrag), listeOderBlattOderExport);
            rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.IstErgaenzungsantrag", Integer.toString(abstimmungen[i1].ergaenzungsantrag), listeOderBlattOderExport);

        }

        for (int gattung = 1; gattung <= 6; gattung++) {

            String praefix = "G" + Integer.toString(gattung) + ".";
            if (gattung == 6) {
                praefix = "GA.";
            }

            if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
                if (!istUeberschrift(i1)) {
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.PräsenzStimmen",
                            CaString.toStringDE(gesamtPraesenz[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.PräsenzKapital",
                            CaString.toEuroStringDE(gesamtPraesenzKapital[gattung]), listeOderBlattOderExport);
                    long praesenzStimmenStimmberechtigt = gesamtPraesenz[gattung]
                            - eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(i1)][9];
                    /*TODO Abstimmung: wahrscheinlich Gesamt.PräsenzStimmenStimmberechtigt noch nicht je Gattung!*/
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.PräsenzStimmenStimmberechtigt",
                            CaString.toStringDE(praesenzStimmenStimmberechtigt), listeOderBlattOderExport);

                    double hProzent = CaString.berechneProzent(praesenzStimmenStimmberechtigt,
                            gesamtKapitalStimmen[gattung]);
                    hProzent = CaString.runde2Stellen(hProzent);
                    String hProzentString = CaString.prozentToString(hProzent);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.PräsenzStimmenStimmberechtigtProzent", hProzentString,
                            listeOderBlattOderExport);

                    hProzent = CaString.berechneProzent(praesenzStimmenStimmberechtigt,
                            gesamtKapitalVermindertStimmen[gattung]);
                    hProzent = CaString.runde2Stellen(hProzent);
                    hProzentString = CaString.prozentToString(hProzent);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.PräsenzStimmenStimmberechtigtProzentVermindert", hProzentString,
                            listeOderBlattOderExport);

                    CaBug.druckeLog("BlAbstimmung vor Eintrag in Variable gattung=" + gattung
                            + " jaUndNeinStimmen[gattung]=" + jaUndNeinStimmen[gattung], logDrucken, 10);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmen",
                            CaString.toStringDE(jaUndNeinStimmen[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmenEuro",
                            Double.toString(abgegebeneGueltigeStimmenKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmenEuroDE",
                            CaString.doubleToStringDE(abgegebeneGueltigeStimmenKapital[gattung]),
                            listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmen", CaString.toStringDE(jaUndNeinStimmenBriefwahl[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmenEuro", Double.toString(abgegebeneGueltigeStimmenKapitalBriefwahl[gattung]),
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmenEuroDE", CaString.doubleToStringDE(abgegebeneGueltigeStimmenKapitalBriefwahl[gattung]),
                            listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmen",
                            CaString.toStringDE(jaUndNeinStimmenOhneBriefwahl[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenEuro",
                            Double.toString(abgegebeneGueltigeStimmenKapitalOhneBriefwahl[gattung]),
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenEuroDE",
                            CaString.doubleToStringDE(abgegebeneGueltigeStimmenKapitalOhneBriefwahl[gattung]),
                            listeOderBlattOderExport);

                    for (int j = 1; j <= 9; j++) {
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmen." + KonstStimmart.getText(j),
                                CaString.toStringDE(
                                        eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(i1)][j]),
                                listeOderBlattOderExport);
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmenBriefwahl." + KonstStimmart.getText(j),
                                CaString.toStringDE(
                                        eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                                                i1)][j]),
                                listeOderBlattOderExport);
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmenOhneBriefwahl." + KonstStimmart.getText(j),
                                CaString.toStringDE(eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                                        i1)][j]
                                        - eclAbstimmungMeldungSummenBriefwahl[gattung].abgabe[lieferePosInWeisung(
                                                i1)][j]),
                                listeOderBlattOderExport);
                    }

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenKapital.Ja",
                            CaString.toEuroStringDE(stimmenJaKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenKapital.Nein",
                            CaString.toEuroStringDE(stimmenNeinKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenKapital.JaUndNein",
                            CaString.toEuroStringDE(jaUndNeinStimmenKapital[gattung]), listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.Ja",
                            CaString.prozentToString(prozentJa[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.Nein",
                            CaString.prozentToString(prozentNein[gattung]), listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.JaKapital",
                            CaString.prozentToString(prozentJaKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.NeinKapital",
                            CaString.prozentToString(prozentNeinKapital[gattung]), listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentBriefwahl.Ja",
                            CaString.prozentToString(prozentJaBriefwahl[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentBriefwahl.Nein",
                            CaString.prozentToString(prozentNeinBriefwahl[gattung]), listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentOhneBriefwahl.Ja",
                            CaString.prozentToString(prozentJaOhneBriefwahl[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "AbgegebeneStimmenProzentOhneBriefwahl.Nein",
                            CaString.prozentToString(prozentNeinOhneBriefwahl[gattung]), listeOderBlattOderExport);

                    /**In Relation zum Grundkapital der jeweiligen Gattung*/
                    hProzentString = CaString.prozentToString(abgegebeneGueltigeStimmenProzentGrundkapital[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapital", hProzentString,
                            listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalVermindert[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapitalVermindert", hProzentString,
                            listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahl[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Briefwahl.AbgegebeneGueltigeStimmenProzentGrundkapital", hProzentString,
                            listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahl[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenProzentGrundkapital", hProzentString,
                            listeOderBlattOderExport);

                    /**In Relation zum wirklich gesamtn Grundkapital*/
                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalGesamt[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapitalGesamt", hProzentString,
                            listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapitalVermindertGesamt",
                            hProzentString, listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalBriefwahlGesamt[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Briefwahl.AbgegebeneGueltigeStimmenProzentGrundkapitalGesamt", hProzentString,
                            listeOderBlattOderExport);

                    hProzentString = CaString
                            .prozentToString(abgegebeneGueltigeStimmenProzentGrundkapitalOhneBriefwahlGesamt[gattung]);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenProzentGrundkapitalGesamt",
                            hProzentString, listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtStimmen",
                            Long.toString(gesamtKapitalStimmen[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtStimmenDE",
                            CaString.toStringDE(gesamtKapitalStimmen[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtEuro",
                            Double.toString(gesamtKapitalKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtEuroDE",
                            CaString.doubleToStringDE(gesamtKapitalKapital[gattung]), listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtStimmen",
                            Long.toString(gesamtKapitalVermindertStimmen[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtStimmenDE",
                            CaString.toStringDE(gesamtKapitalVermindertStimmen[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtEuro",
                            Double.toString(gesamtKapitalVermindertKapital[gattung]), listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtEuroDE",
                            CaString.doubleToStringDE(gesamtKapitalVermindertKapital[gattung]),
                            listeOderBlattOderExport);

                } else { /*Nur Überschrift drucken!*/
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.PräsenzStimmen", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.PräsenzStimmenStimmberechtigt", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmen", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmenEuro", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Gesamt.AbgegebeneGueltigeStimmenEuroDE",
                            "", listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmen", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmenEuro",
                            "", listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Briefwahl.AbgegebeneGueltigeStimmenEuroDE",
                            "", listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmen",
                            "", listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenEuro", "", listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenEuroDE", "", listeOderBlattOderExport);

                    for (int j = 1; j <= 9; j++) {
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmen." + KonstStimmart.getText(j), "",
                                listeOderBlattOderExport);
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmenBriefwahl." + KonstStimmart.getText(j), "",
                                listeOderBlattOderExport);
                        rpVariablen.fuelleFeldOderVariable(rpDrucken,
                                praefix + "AbgegebeneStimmenOhneBriefwahl." + KonstStimmart.getText(j), "",
                                listeOderBlattOderExport);
                    }
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.Ja", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzent.Nein", "",
                            listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentBriefwahl.Ja", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentBriefwahl.Nein",
                            "", listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "AbgegebeneStimmenProzentOhneBriefwahl.Ja",
                            "", listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "AbgegebeneStimmenProzentOhneBriefwahl.Nein", "", listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapital", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Briefwahl.AbgegebeneGueltigeStimmenProzentGrundkapital", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "OhneBriefwahl.AbgegebeneGueltigeStimmenProzentGrundkapital", "",
                            listeOderBlattOderExport);

                    rpVariablen.fuelleFeldOderVariable(rpDrucken,
                            praefix + "Gesamt.AbgegebeneGueltigeStimmenProzentGrundkapitalVermindert", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtStimmen", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtStimmenDE", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtEuro", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "Grundkapital.GesamtEuroDE", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtStimmen", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtStimmenDE",
                            "", listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtEuro", "",
                            listeOderBlattOderExport);
                    rpVariablen.fuelleFeldOderVariable(rpDrucken, praefix + "GrundkapitalVermindert.GesamtEuroDE", "",
                            listeOderBlattOderExport);

                }
            }

            if (listeOderBlattOderExport == 3) {
                if (gattung == 6) {
                    if (!istUeberschrift(i1)) {
                        dateiExport.ausgabe(topImmer + abstimmungen[i1].nummerindex);
                        dateiExport.ausgabe(Long.toString(jaUndNeinStimmen[gattung]));
                        dateiExport.ausgabe(Long.toString(
                                eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(i1)][KonstStimmart.ja]));
                        dateiExport
                                .ausgabe((Long.toString(eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                                        i1)][KonstStimmart.nein])));
                        dateiExport.ausgabe(CaString.prozentToString(prozentJa[gattung]));
                        dateiExport.ausgabe(CaString.prozentToString(prozentNein[gattung]));
                        dateiExport
                                .ausgabe(Long.toString(eclAbstimmungMeldungSummen[gattung].abgabe[lieferePosInWeisung(
                                        i1)][KonstStimmart.enthaltung]));
                        dateiExport.ausgabe(
                                CaString.prozentToString(abgegebeneGueltigeStimmenProzentGrundkapital[gattung]));

                        dateiExport.newline();
                    }
                }
            }

        } /*Ende Gattungs-Schleife*/

        /*Gruppen-Ergebnis ggf. füllen - aus Gattung 6*/
        if (kandidatEintragen) {
            int gruppe = abstimmungen[i1].zuAbstimmungsgruppe;
            if (gruppe != 0) {
                CaBug.druckeLog("abstimmungsGruppePruefeAddKandidat i1=" + i1, logDrucken, 10);
                abstimmungsGruppePruefeAddKandidat(gruppe, i1,
                        eclAbstimmungMeldungSummen[6].abgabe[lieferePosInWeisung(i1)][KonstStimmart.ja]);
            }
        }

        if (!istUeberschrift(i1)) {
            angenommen = 0;
            switch (abstimmungen[i1].identErforderlicheMehrheit) {
            case 1:
            case 2:
            case 101:
            case 102:
                /*Einfache Mehrheit (mehr Ja-Stimmen als Nein-Stimmen)*/
                if (stimmenJa[6] > stimmenNein[6]) {
                    angenommen = 1;
                } else {
                    angenommen = 0;
                }
                break;

            case 4:
            case 5:
            case 6:
            case 7:
            case 104:
            case 105:
            case 106:
            case 107:
                /*3/4 Mehrheit >= */

                if (stimmenJa[6] >= stimmenNein[6] * 3) {
                    angenommen = 1;
                } else {
                    angenommen = 0;
                }
                break;

            case 8:
            case 9:
            case 10:
            case 11:
            case 108:
            case 109:
            case 110:
            case 111:
                /*>3/4 Mehrheit >= */

                if (stimmenJa[6] > stimmenNein[6] * 3) {
                    angenommen = 1;
                } else {
                    angenommen = 0;
                }
                break;

            }

            if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Angenommen", Integer.toString(angenommen),
                        listeOderBlattOderExport);
            }
        } else {
            if (listeOderBlattOderExport == 1 || listeOderBlattOderExport == 2) {
                rpVariablen.fuelleFeldOderVariable(rpDrucken, "Abstimmung.Angenommen", "", listeOderBlattOderExport);
            }
        }

        if (listeOderBlattOderExport == 1) {/*Liste*/
            rpDrucken.druckenListe();
        }
        if (listeOderBlattOderExport == 2) { /*Formular*/
            if (!istUeberschrift(i1)) {
//                int anzSeiten=abstimmungenZuAktivenBlock[i1].beschlussvorschlagGestelltVon;
                
                rpDrucken.druckenFormular();
            }
        }

    }

    /**Zurücksetzen des aktiven Abstimmungsblocks:
     * > Löschen aller eingelesenen Einzelstimmabgaben zu diesem Block (in Raw)
     * > Löschen der tatsächlich verarbeiteten Einzelstimmabgaben für diesen Block
     * > Löschen der übertragenen Sammelweisungen für diesen Block
     * > Löschen der Summen für diesen Block
     */
    public void zuruecksetzen() {
        /*Löschen aller eingelesenen Einzelstimmabgaben zu diesem Block (in Raw)*/
        lDbBundle.dbAbstimmungMeldungRaw.deleteFuerAbstimmungsblock(aktiverAbstimmungsblock.ident);

        /*Löschen der übertragenen Sammelweisungen - sowie Summe - für diese Position*/
        lDbBundle.dbAbstimmungMeldungSplit.leseIdent(-99);
        if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() > 0) {
            for (int i = 0; i < lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden(); i++) {
                EclAbstimmungMeldungSplit eclAbstimmungMeldungSplitSumme = lDbBundle.dbAbstimmungMeldungSplit
                        .ergebnisPositionGefunden(i);
                for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                    int abstimmposition = lieferePosInWeisung(i1);
                    if (abstimmposition != -1) {
                        for (int i2 = 0; i2 < 10; i2++) {
                            eclAbstimmungMeldungSplitSumme.abgabe[abstimmposition][i2] = 0;
                        }
                    }
                }
                lDbBundle.dbAbstimmungMeldungSplit.update(eclAbstimmungMeldungSplitSumme);
            }
        }

        /*Löschen der tatsächlich verarbeiteten Einzelstimmabgaben für diese Position*/

        for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
            int abstimmposition = lieferePosInWeisung(i1);
            if (abstimmposition != -1) {
                lDbBundle.dbAbstimmungMeldung.update_allPosition(abstimmposition);
            }
        }

    }

    /*****************************Abstimmungsverhalten*********************************************/

    /**Ergebnis für Abstimmungsverhalten, Meldung Einzelkarten.*/
    public List<EclMeldung> ergebnisEclMeldungEinzelkarten = null;

    public int anzErgebnisEclMeldungEinzelkarten() {
        if (ergebnisEclMeldungEinzelkarten == null) {
            return 0;
        }
        return ergebnisEclMeldungEinzelkarten.size();
    }

    /**Ergebnis für Abstimmungsverhalten, Meldung Sammelkarten.*/
    public List<EclMeldung> ergebnisEclMeldungSammelkarten = null;

    public int anzErgebnisEclMeldungSammelkarten() {
        if (ergebnisEclMeldungSammelkarten == null) {
            return 0;
        }
        return ergebnisEclMeldungSammelkarten.size();
    }

    /**TOP-Numerierung für die einzelnen, Rückübergebenen TOPe*/
    public String[] ergebnisUeberschriften = null;
    public boolean[] ergebnisUeberschriftenIstUeberschrift=null;

    public int anzErgebnisUeberschriften() {
        if (ergebnisUeberschriften == null) {
            return 0;
        }
        return ergebnisUeberschriften.length;
    }

    /**Ergebnis für Abstimmungsverhalten Einzelkarten.*/
    public List<int[]> ergebnisEclAbstimmungEinzelkarten = null;
    public List<int[]> ergebnisEclAbstimmungEinzelkartenWeg = null;
    public List<int[]> ergebnisEclAbstimmungEinzelkartenDurchSammelkarte = null;

    public int anzErgebnisEclAbstimmungMeldung() {
        if (ergebnisEclAbstimmungEinzelkarten == null) {
            return 0;
        }
        return ergebnisEclAbstimmungEinzelkarten.size();
    }

    /**Ergebnis für AbstimmungsverhaltenSammelkarten.
     * [top][Stimmart]*/
    public List<long[][]> ergebnisEclAbstimmungSplitSammelkarte = null;

    public int anzErgebnisEclAbstimmungSplitSammelkarte() {
        if (ergebnisEclAbstimmungSplitSammelkarte == null) {
            return 0;
        }
        return ergebnisEclAbstimmungSplitSammelkarte.size();
    }

    /*TODO _Abstimmung: beim Subtraktionsverfahren wird derzeit nicht gespeichert, wer mit "J" gestimmt hat - dementsprechend kann das Abstimmungsverhalten dann nicht komplett ausgedruckt werden*/
    /*TODO _Abstimmungsverhalten: derzeit nur für aktiven Abstimmungsblock ausdruckbar*/
    /**Füllen der ergebnisArrays für das Abstimmungsverhalten. Sowohl Einzelkarten als auch Sammelkarten
     * Parameter:
     * pSortierung: Sortierung nach
     *	1 = meldeIdent
     *	2 = zutrittsIdent (aktuelle)
     *	3 = StimmkartenIdent (aktuelle)
     *	4 = Name Meldung
     *	5 = Größe Aktienzahl (absteigend)
     *
     * pNurAktivenAbstimmungsblock:
     * 	true = nur Stimmabgaben des derzeit aktiven Abstimmungsblockes
     * 	false = Stimmabgaben für alle Abstimmungen
     * Verarbeitungshinweise: 
     * > falls true: 
     * 		> Einzelaktionäre werden nur aufgeführt, falls für diesen Block aktive Stimmabgabe!
     * 		> Sammelkarten werden immer aufgeführt
     * Achtung - derzeit nur true zulässig! Siehe ToDo! 
     *  
     * pSchreibenInDatei = true => Ergebnis wird in Dateien geschrieben:
     * 
     * Ergebnis in:
     * ergebnisEclMeldungEinzelkarten, ergebnisEclAbstimmungEinzelkarten
     */
    public void fuelleAbstimmungsverhalten(int pSortierung, boolean pNurAktivenAbstimmungsblock,
            boolean pSchreibenInDatei) {
        fuelleAbstimmungsverhaltenEinzelkarten(pSortierung, pNurAktivenAbstimmungsblock, pSchreibenInDatei);
        fuelleAbstimmungsverhaltenSammelkarten(pSortierung, pNurAktivenAbstimmungsblock, pSchreibenInDatei);
    }

    /**Wie fuelleAbstimmungsverhalten, allerdings nur Einzelkarten (ohne Sammelkarten)*/
    public void fuelleAbstimmungsverhaltenEinzelkarten(int pSortierung, boolean pNurAktivenAbstimmungsblock,
            boolean pSchreibenInDatei) {
        int anzAbstimmungen = getAnzAbstimmungenInAktivemAbstimmungsblock();
        fuelleAbstimmungsverhaltenUeberschrift();
        ergebnisEclMeldungEinzelkarten = new LinkedList<EclMeldung>();
        ergebnisEclAbstimmungEinzelkarten = new LinkedList<int[]>();
        ergebnisEclAbstimmungEinzelkartenWeg = new LinkedList<int[]>();
        ergebnisEclAbstimmungEinzelkartenDurchSammelkarte = new LinkedList<int[]>();
        int anzahl = lDbBundle.dbJoined.read_abstimmungsVerhaltenEinzelkarten(pSortierung);
        for (int i = 0; i < anzahl; i++) {
            EclMeldung lMeldung=lDbBundle.dbJoined.ergebnisMeldungPosition(i);
            if (lMeldung.meldungstyp!=KonstMeldung.KARTENART_SAMMELKARTE) {
                int[] abgegebeneAbstimmungen = new int[anzAbstimmungen];
                int[] abgegebeneAbstimmungenWeg = new int[anzAbstimmungen];
                int[] abgegebeneAbstimmungenDurchSammelkarte = new int[anzAbstimmungen];

                /*Übertragen und Prüfen, ob eine abgegebene Abstimmung für diese Meldung vorliegt. Nur dann in Ergebnis eintragen*/
                int gefAbgabe = -1;
                for (int i1 = 0; i1 < anzAbstimmungen; i1++) {
                    int abstimmposition=lieferePosInWeisung(i1);
                    if (abstimmposition!=-1) {
                        EclAbstimmungMeldung lAbstimmungMeldung=lDbBundle.dbJoined.ergebnisAbstimmungMeldungPosition(i);
                        int aktuellesVerhalten = 
                                lAbstimmungMeldung.abgabe[abstimmposition];
                        int weg=lAbstimmungMeldung.abstimmungsweg[abstimmposition];
                        int durchSammelkarte=lAbstimmungMeldung.abstimmungDurchSammelkarte[abstimmposition];
                        if (aktuellesVerhalten != 0) {
                            gefAbgabe = 1;
                        }
                        abgegebeneAbstimmungen[i1] = aktuellesVerhalten;
                        abgegebeneAbstimmungenWeg[i1] = weg;
                        abgegebeneAbstimmungenDurchSammelkarte[i1] = durchSammelkarte;
                    }
                }
                if (gefAbgabe != -1) {
                    ergebnisEclAbstimmungEinzelkarten.add(abgegebeneAbstimmungen);
                    ergebnisEclAbstimmungEinzelkartenWeg.add(abgegebeneAbstimmungenWeg);
                    ergebnisEclAbstimmungEinzelkartenDurchSammelkarte.add(abgegebeneAbstimmungenDurchSammelkarte);
                    ergebnisEclMeldungEinzelkarten.add(lMeldung);
                }
            }

        }
        if (pSchreibenInDatei) {
            CaDateiWrite caDateiJNE = new CaDateiWrite();
            caDateiJNE.dateiart = ".csv";
            caDateiJNE.trennzeichen = ';';
            caDateiJNE.oeffne(lDbBundle, "abstimmverhaltenEinzelkartenJNE" + Integer.toString(aktiverAbstimmungsblock.ident));

            CaDateiWrite caDateiStimmen = new CaDateiWrite();
            caDateiStimmen.dateiart = ".csv";
            caDateiStimmen.trennzeichen = ';';
            caDateiStimmen.oeffne(lDbBundle, "abstimmverhaltenEinzelkartenStimmen" + Integer.toString(aktiverAbstimmungsblock.ident));


            /*Überschrift*/
            caDateiJNE.ausgabe("MeldungsIdent");
            caDateiJNE.ausgabe("AktionärsNummer");
            caDateiJNE.ausgabe("Name");
            caDateiJNE.ausgabe("Vorname");
            caDateiJNE.ausgabe("Ort");
            caDateiJNE.ausgabe("Aktien");
            caDateiJNE.ausgabe("Besitzart");
            caDateiJNE.ausgabe("Gattung");
            caDateiJNE.ausgabe("EK");
            caDateiJNE.ausgabe("SK");
            for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                caDateiJNE.ausgabe(ergebnisUeberschriften[i1]);
                if (ergebnisUeberschriftenIstUeberschrift[i1]==false) {
                    caDateiJNE.ausgabe(ergebnisUeberschriften[i1]+" Hinweis");
                    caDateiJNE.ausgabe(ergebnisUeberschriften[i1]+" Durch SammelIdent");
                }
            }
            caDateiJNE.newline();
            
            caDateiStimmen.ausgabe("MeldungsIdent");
            caDateiStimmen.ausgabe("AktionärsNummer");
            caDateiStimmen.ausgabe("Name");
            caDateiStimmen.ausgabe("Vorname");
            caDateiStimmen.ausgabe("Ort");
            caDateiStimmen.ausgabe("Aktien");
            caDateiStimmen.ausgabe("Besitzart");
            caDateiStimmen.ausgabe("Gattung");
            caDateiStimmen.ausgabe("EK");
            caDateiStimmen.ausgabe("SK");
            for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                if (ergebnisUeberschriftenIstUeberschrift[i1]==false) {
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Stimmart");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Ja");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Nein");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Enthaltung");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Ungültig");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Stimmausschluss");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Hinweis");
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]+" Durch SammelIdent");
                }
                else {
                    caDateiStimmen.ausgabe(ergebnisUeberschriften[i1]);
                }
            }
            caDateiStimmen.newline();


            anzahl = ergebnisEclMeldungEinzelkarten.size();
            for (int i = 0; i < anzahl; i++) {
                EclMeldung lMeldung = ergebnisEclMeldungEinzelkarten.get(i);
                
                caDateiJNE.ausgabe(Integer.toString(lMeldung.meldungsIdent));
                caDateiJNE.ausgabe(lMeldung.aktionaersnummer);
                caDateiJNE.ausgabe(lMeldung.name);
                caDateiJNE.ausgabe(lMeldung.vorname);
                caDateiJNE.ausgabe(lMeldung.ort);
                caDateiJNE.ausgabe(Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                caDateiJNE.ausgabe(lMeldung.besitzart);
                caDateiJNE.ausgabe(lDbBundle.param.paramBasis.getGattungBezeichnungKurz(lMeldung.liefereGattung()));
                caDateiJNE.ausgabe(lMeldung.zutrittsIdent);
                caDateiJNE.ausgabe(lMeldung.stimmkarte);
                for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                    if (ergebnisUeberschriftenIstUeberschrift[i1]==false) {
                        int aktuellesVerhalten = ergebnisEclAbstimmungEinzelkarten.get(i)[i1];
                        if (aktuellesVerhalten!=0) {
                            caDateiJNE.ausgabe(KonstStimmart.getTextKurz(aktuellesVerhalten));
                            caDateiJNE.ausgabe(KonstWillenserklaerungWeg.getTextFuerStimmabgabe(ergebnisEclAbstimmungEinzelkartenWeg.get(i)[i1]));
                            if (ergebnisEclAbstimmungEinzelkartenDurchSammelkarte.get(i)[i1]!=0) {
                                caDateiJNE.ausgabe(Integer.toString(ergebnisEclAbstimmungEinzelkartenDurchSammelkarte.get(i)[i1]));
                            }
                            else{
                                caDateiJNE.ausgabe("");
                            }
                        }
                        else {
                            caDateiJNE.ausgabe("");
                            caDateiJNE.ausgabe("");
                            caDateiJNE.ausgabe("");
                        }
                    }
                    else {
                        caDateiJNE.ausgabe("");
                    }
                }
                caDateiJNE.newline();
                
                caDateiStimmen.ausgabe(Integer.toString(lMeldung.meldungsIdent));
                caDateiStimmen.ausgabe(lMeldung.aktionaersnummer);
                caDateiStimmen.ausgabe(lMeldung.name);
                caDateiStimmen.ausgabe(lMeldung.vorname);
                caDateiStimmen.ausgabe(lMeldung.ort);
                caDateiStimmen.ausgabe(Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                caDateiStimmen.ausgabe(lMeldung.besitzart);
                caDateiStimmen.ausgabe(lDbBundle.param.paramBasis.getGattungBezeichnungKurz(lMeldung.liefereGattung()));
                caDateiStimmen.ausgabe(lMeldung.zutrittsIdent);
                caDateiStimmen.ausgabe(lMeldung.stimmkarte);
                for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                    if (ergebnisUeberschriftenIstUeberschrift[i1]==false) {
                        int aktuellesVerhalten = ergebnisEclAbstimmungEinzelkarten.get(i)[i1];
                        if (aktuellesVerhalten!=0) {
                            caDateiStimmen.ausgabe(KonstStimmart.getTextKurz(aktuellesVerhalten));

                            switch (ergebnisEclAbstimmungEinzelkarten.get(i)[i1]) {
                            case KonstStimmart.ja: 
                                caDateiStimmen.ausgabe(
                                        Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                break;
                            case KonstStimmart.nein: 
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe(
                                        Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                break;
                            case KonstStimmart.enthaltung: 
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe(
                                        Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                break;
                            case KonstStimmart.ungueltig: 
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe(
                                        Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                                caDateiStimmen.ausgabe("0");
                                break;
                            case KonstStimmart.stimmausschluss: 
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe(
                                        Long.toString(liefereMeldungStimmenGgfMitHoechststimmrecht(lMeldung)));
                                break;
                            default: 
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                caDateiStimmen.ausgabe("0");
                                break;
                            }
                            caDateiStimmen.ausgabe(KonstWillenserklaerungWeg.getTextFuerStimmabgabe(ergebnisEclAbstimmungEinzelkartenWeg.get(i)[i1]));
                            if (ergebnisEclAbstimmungEinzelkartenDurchSammelkarte.get(i)[i1]!=0) {
                                caDateiStimmen.ausgabe(Integer.toString(ergebnisEclAbstimmungEinzelkartenDurchSammelkarte.get(i)[i1]));
                            }
                            else{
                                caDateiStimmen.ausgabe("");
                            }
                        }
                        else {
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                            
                            caDateiStimmen.ausgabe("");
                            caDateiStimmen.ausgabe("");
                        }
                    }
                    else {
                        caDateiStimmen.ausgabe("");
                    }

                }
                caDateiStimmen.newline();


            }

            caDateiJNE.schliessen();
            caDateiStimmen.schliessen();
        }
    }

    /**Wie fuelleAbstimmungsverhalten, allerdings nur Sammelkarten*/
    public void fuelleAbstimmungsverhaltenSammelkarten(int pSortierung, boolean pNurAktivenAbstimmungsblock,
            boolean pSchreibenInDatei) {
        int anzAbstimmungen = getAnzAbstimmungenInAktivemAbstimmungsblock();
        fuelleAbstimmungsverhaltenUeberschrift();
        ergebnisEclMeldungSammelkarten = new LinkedList<EclMeldung>();
        ergebnisEclAbstimmungSplitSammelkarte = new LinkedList<long[][]>();
        int anzahl = lDbBundle.dbJoined.read_abstimmungsVerhaltenSammelkarten(pSortierung);
        for (int i = 0; i < anzahl; i++) {
            long[][] abgegebeneAbstimmungenSplit = new long[anzAbstimmungen][10];
            for (int i1 = 0; i1 < anzAbstimmungen; i1++) {
                int hPos = lieferePosInWeisung(i1);
                for (int i2 = 0; i2 < 10; i2++) {
                    if (hPos != -1) {
                        abgegebeneAbstimmungenSplit[i1][i2] = lDbBundle.dbJoined
                                .ergebnisAbstimmungMeldungSplitPosition(i).abgabe[hPos][i2];
                    } else {
                        abgegebeneAbstimmungenSplit[i1][i2] = 0;
                    }
                }
            }
            ergebnisEclAbstimmungSplitSammelkarte.add(abgegebeneAbstimmungenSplit);
            ergebnisEclMeldungSammelkarten.add(lDbBundle.dbJoined.ergebnisMeldungPosition(i));

        }
        if (pSchreibenInDatei) {
            CaDateiWrite caDateiJNE = new CaDateiWrite();
            caDateiJNE.dateiart = ".csv";
            caDateiJNE.trennzeichen = ';';
            caDateiJNE.oeffne(lDbBundle, "abstimmverhaltenSammelkartenJNE" + Integer.toString(aktiverAbstimmungsblock.ident));

            /*Überschrift*/
            caDateiJNE.ausgabe("MeldungsIdent");
            caDateiJNE.ausgabe("EK");
            caDateiJNE.ausgabe("SK");
            caDateiJNE.ausgabe("Sammelkarte");
            for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                caDateiJNE.ausgabe(ergebnisUeberschriften[i1]);
            }
            caDateiJNE.newline();

            for (int i = 0; i < anzahl; i++) {
                EclMeldung lMeldung = ergebnisEclMeldungSammelkarten.get(i);
                caDateiJNE.ausgabe(Integer.toString(lMeldung.meldungsIdent));
                caDateiJNE.ausgabe(lMeldung.zutrittsIdent);
                caDateiJNE.ausgabe(lMeldung.stimmkarte);
                caDateiJNE.ausgabe(lMeldung.name);
                caDateiJNE.newline();
                for (int i2 = 0; i2 < 10; i2++) {
                    if (i2 <= 4 || i2 == 9) {
                        caDateiJNE.ausgabe("");
                        caDateiJNE.ausgabe("");
                        caDateiJNE.ausgabe("");
                        caDateiJNE.ausgabe(KonstStimmart.getText(i2));
                        for (int i1 = 0; i1 < getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                            if (ergebnisUeberschriftenIstUeberschrift[i1]==false) {
                        
                                caDateiJNE.ausgabe(Long.toString(ergebnisEclAbstimmungSplitSammelkarte.get(i)[i1][i2]));
                            }
                            else {
                                caDateiJNE.ausgabe("");
                            }
                        }
                        caDateiJNE.newline();
                    }
                }
                caDateiJNE.newline();
            }
            caDateiJNE.schliessen();
        }

    }

    public void fuelleAbstimmungsverhaltenUeberschrift() {
        ergebnisUeberschriften = new String[getAnzAbstimmungenInAktivemAbstimmungsblock()];
        ergebnisUeberschriftenIstUeberschrift = new boolean[getAnzAbstimmungenInAktivemAbstimmungsblock()];
        for (int i = 0; i < getAnzAbstimmungenInAktivemAbstimmungsblock(); i++) {
            ergebnisUeberschriften[i] = abstimmungen[i].nummerKey + abstimmungen[i].nummerindexKey;
            if (abstimmungen[i].liefereIstUeberschift()) {
                ergebnisUeberschriftenIstUeberschrift[i]=true;
            }
            else {
                ergebnisUeberschriftenIstUeberschrift[i]=false;
            }
        }
    }
    
    
    /**Nach der Auswertung: "nicht explizit abgegebene Stimmen" in AbstimmungMeldung zur Dokumentation und späteren Verwendung eintragen.
     * 
     * Hinsweis: diese Funktion muß "isoliert" lauffähig sein, da sie aktuell automatisch aufgerufen wird, was allerdings bei größeren HVen
     * eine Zeitlang dauern kann. Für diese HVen dann ggf. parameter-gesteuert nur "nach Aufforderung" durchlaufen, d.h. erst wird
     * ausgewertet und ausgedruckt, und dann diese Funktion aufgerufen*/
    public void ergaenzeAbstimmungMeldungFuerArchiv() {

        /*AAAAA*/
//        if (1==1) {return;}

        /*Als erstes die präsenten Sammelkarten, die nicht geschmissen haben und keine Weisungen hinterlegt haben, mit impliziter
         * Stimmabgabe markieren. Muß als erstes passieren, damit auch diese impliziten Stimmabgaben dann auf die Einzelkarten übertragen werden
         */
        CaBug.druckeLog("vor ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenSammelkarten", logDrucken, 3);
        ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenSammelkarten();

        CaBug.druckeLog("vor ergaenzeAbstimmungMeldungFuerArchiv_SammelkartenAufEinzelkartenUebertragen", logDrucken, 3);
        ergaenzeAbstimmungMeldungFuerArchiv_SammelkartenAufEinzelkartenUebertragen();
 
        CaBug.druckeLog("vor ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenEinzelkarten", logDrucken, 3);
        ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenEinzelkarten();

        /**Stimmausschluß bei Ergänzten Stimmabgaben eintragen*/
        /*AAAAA*/
    }

    private void ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenSammelkarten(){
        /*Alle anwesenden Sammelkarten, für die noch keine Abstimmung eingetragen ist, mit
         * der für diese Abstimmung nicht-eingesammelten Stimmart füllen
         */
        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i = 0; i < anzSammelkarten; i++) {
            EclMeldung eclMeldungSammelkarte = lDbBundle.dbMeldungen.meldungenArray[i];

            if (eclMeldungSammelkarte.statusPraesenz_Delayed==1  || eclMeldungSammelkarte.skIst==KonstSkIst.briefwahl) {
                int sammelIdent=eclMeldungSammelkarte.meldungsIdent;
                CaBug.druckeLog("sammelIdent="+sammelIdent, logDrucken, 10);

                belegeSammelAbstimmungMeldungSplit(sammelIdent);
                belegeSammelAbstimmungMeldung(sammelIdent, eclMeldungSammelkarte);
                boolean updateAbstimmungMeldung=false;
                for (int i2 = 0; i2 < abstimmungenZuAktivenBlock.length; i2++) {
                    /*int identAbstimmung=abstimmungenZuAktivenBlock[Gi2].identAbstimmung;*/
                    int abstimmposition = lieferePosInWeisung(i2);
                    if (abstimmposition != -1) {

                        if (abstimmungen[i2/*identAbstimmung*/].stimmberechtigteGattungen[eclMeldungSammelkarte.liefereGattung()-1]==1) {
                           if (sammelAbstimmungMeldung.abgabe[abstimmposition]==0 && (eclMeldungSammelkarte.sammelkarteIstWeisungskarte()==false)) {
                               CaBug.druckeLog("Sammelkarte "+sammelIdent+" hat implizit geworfen", logDrucken, 10);
                                int stimmenAuswerten=abstimmungen[i2/*identAbstimmung*/].stimmenAuswerten;
                                if (stimmenAuswerten==-1) {stimmenAuswerten=KonstStimmart.enthaltung;}
                                sammelAbstimmungMeldung.abgabe[abstimmposition]=stimmenAuswerten;
                                sammelAbstimmungMeldung.abstimmungsweg[abstimmposition]=KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_ANWESENHEIT;
                                updateAbstimmungMeldung=true;
                            }
                        }

                    }
                }
                if (updateAbstimmungMeldung) {
                    if (sammelAbstimmungMeldungVorhanden) {
                        lDbBundle.dbAbstimmungMeldung.update(sammelAbstimmungMeldung);
                    }
                    else {
                        lDbBundle.dbAbstimmungMeldung.insert(sammelAbstimmungMeldung);
                    }
                }
            }

        }
    }

    private void ergaenzeAbstimmungMeldungFuerArchiv_ImpliziteStimmabgabenEinzelkarten(){
        /*Alle anwesenden Aktionäre, für die noch keine Abstimmung eingetragen ist, mit
         * der für diese Abstimmung nicht-eingesammelten Stimmart füllen
         */
        lDbBundle.dbMeldungen.leseAlleMeldungenAnwesend(-1, false);
        int anzMeldungen=lDbBundle.dbMeldungen.meldungenArray.length;
        for (int i=0;i<anzMeldungen;i++) {
            EclMeldung lMeldung=lDbBundle.dbMeldungen.meldungenArray[i];
            
            starteSpeichernFuerMeldungImplizit(lMeldung);
            for (int i2 = 0; i2 < abstimmungenZuAktivenBlock.length; i2++) {
                int abstimmposition = lieferePosInWeisung(i2);
                if (abstimmposition != -1) {
                    if (liefereMarkierungZuAbstimmungsPositionVorhandenImmplizit(abstimmposition)==false) {
                        /*int identAbstimmung=abstimmungenZuAktivenBlock[i2].identAbstimmung;*/
                        if (abstimmungen[i2/*identAbstimmung*/].stimmberechtigteGattungen[lMeldung.liefereGattung()-1]==1) {
                            int stimmenAuswerten=abstimmungen[i2/*identAbstimmung*/].stimmenAuswerten;
                            if (stimmenAuswerten==-1) {stimmenAuswerten=KonstStimmart.enthaltung;}
                            setzeMarkierungZuAbstimmungsPositionImplizit(stimmenAuswerten, abstimmposition, KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_ANWESENHEIT, 0);
                        }
                    }
                }
            }
            beendeSpeichernFuerMeldungImplizit();
       
        }
    }

     
    private void ergaenzeAbstimmungMeldungFuerArchiv_SammelkartenAufEinzelkartenUebertragen() {
        /*Alle Sammelkarten, die anwesend sind, sowie Briefwahl-Sammelkarten eintragen.
         * Dies muß vor dem Durcharbeiten der Einzelaktionäre passieren, da sonst bei der Situation "Aktionär ist anwesend, gleichzeitig
         * auch in Sammelkarte (wie er bei virtuellen Versammlungen üblich ist) falsch archiviert werden würde
         */

        lDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        int anzSammelkarten = lDbBundle.dbMeldungen.meldungenArray.length;
        CaBug.druckeLog("anzSammelkarten="+anzSammelkarten, logDrucken, 10);
        EclMeldung[] sammelkarten=lDbBundle.dbMeldungen.meldungenArray;
        for (int i = 0; i < anzSammelkarten; i++) {
            CaBug.druckeLog("Sammelkarte "+i+"von "+anzSammelkarten, logDrucken, 3);
            EclMeldung eclMeldungSammelkarte = sammelkarten[i];
            if (pruefePraesenzstatus_AktionaerDarfAnAbstimmungTeilnehmen(eclMeldungSammelkarte)==true || 
                    eclMeldungSammelkarte.skIst == KonstSkIst.briefwahl) {
                int sammelkartenIdent=eclMeldungSammelkarte.meldungsIdent;
                CaBug.druckeLog("sammelkartenIdent="+sammelkartenIdent, logDrucken, 10);
                belegeSammelAbstimmungMeldung(sammelkartenIdent, eclMeldungSammelkarte);
                belegeSammelAbstimmungMeldungSplit(sammelkartenIdent);
                lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(sammelkartenIdent, false);
                EclWeisungMeldungSplit aktuelleSammelkarteWeisungMeldungSplit = lDbBundle.dbWeisungMeldung
                        .weisungMeldungGefunden(0).weisungMeldungSplit;

                int anzInSammelkarte=lDbBundle.dbMeldungZuSammelkarte.leseZuSammelkarteNurAktive(sammelkartenIdent);
                CaBug.druckeLog("anzInSammelkarte="+anzInSammelkarte, logDrucken, 10);
                for (int i1=0;i1<anzInSammelkarte;i1++) {
                    EclMeldungZuSammelkarte lMeldungZuSammelkarte=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i1);
                    int meldungsIdent=lMeldungZuSammelkarte.meldungsIdent;
                    /*Meldung mit meldungsIdent in meldung einlesen*/
                    leseMeldung(meldungsIdent);
                    /*Meldung Weisung einlesen*/
                    int anz=lDbBundle.dbWeisungMeldung.leseAktiveZuMeldung(meldungsIdent);
                    if (anz<1) {
                        CaBug.drucke("002 Weisung zu Meldung "+meldungsIdent+" nicht vorhanden");
                    }
                    else {
                        EclWeisungMeldung aktionaerWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                        
                        starteSpeichernFuerMeldungImplizit(meldung);
                        
                        /***Für alle aktiven Abstimmungspunkte***/
                        for (int i2 = 0; i2 < abstimmungenZuAktivenBlock.length; i2++) {
                            int abstimmposition = lieferePosInWeisung(i2);
                            if (abstimmungen[i2].stimmberechtigteGattungen[meldung.liefereGattung()-1]==1) {
                                if (abstimmposition != -1) {
                                    int neuesAbstimmungsverhalten=0;
                                    int neuerAbstimmungsweg=0;
                                    int altesAbstimmungsverhalten = liefereMarkierungZuAbstimmungsPositionImmplizit(abstimmposition);
                                    if (altesAbstimmungsverhalten ==0) {
                                        /*Sammelkarte geworfen, oder Stimmabgabe durch Anwesenheit?*/
                                        int sammelAbstimmungMeldungPos=sammelAbstimmungMeldung.abgabe[abstimmposition];
                                        boolean sammelkartenWeisungenSindNurStimmausschluss=false;
                                        if (sammelAbstimmungMeldungSplitVorhanden) {
                                            sammelkartenWeisungenSindNurStimmausschluss=sammelAbstimmungMeldungSplit.pruefeObNurStimmausschluss(abstimmposition);
                                        }
                                        if ((sammelAbstimmungMeldungPos!=KonstStimmart.splitLiegtVor && sammelAbstimmungMeldungPos!=0)
                                                || sammelkartenWeisungenSindNurStimmausschluss) {
                                            CaBug.druckeLog("Geworfen, Sammelkarte kompletter Stimmausschluss, oder Stimmabgabe durch Anwesenheit", logDrucken, 10);
                                            if (sammelkartenWeisungenSindNurStimmausschluss) {
                                                neuesAbstimmungsverhalten=KonstStimmart.stimmausschluss;
                                            }
                                            else {
                                                neuesAbstimmungsverhalten=sammelAbstimmungMeldungPos;
                                            }
                                            if (sammelAbstimmungMeldung.abstimmungsweg[abstimmposition]==KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_ANWESENHEIT) {
                                                neuerAbstimmungsweg=KonstWillenserklaerungWeg.getStimmabgabeSammelkarteAnwesenheit(eclMeldungSammelkarte.skIst);
                                            }
                                            else {
                                                neuerAbstimmungsweg=KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(eclMeldungSammelkarte.skIst);
                                            }
                                        }
                                        else {
                                            /*Nur durchführen, wenn Sammelkarte wirklich präsent ist (unabhängig von Parameter)*/
                                            if (eclMeldungSammelkarte.statusPraesenz==1 || eclMeldungSammelkarte.skIst==KonstSkIst.briefwahl) {
                                                /*Splitsatz muß vorliegen*/
                                                CaBug.druckeLog("Splitsatz muß vorliegen", logDrucken, 10);
                                                if (sammelAbstimmungMeldungSplitVorhanden==false) {
                                                    CaBug.drucke("001");
                                                }
                                                else {

                                                    /*Sammelkarte auf Kopfweisungen?*/
                                                    if (aktuelleSammelkarteWeisungMeldungSplit.nichtBerechnen[abstimmposition]==1) {
                                                        neuesAbstimmungsverhalten=KonstStimmart.splitLiegtVor;
                                                        neuerAbstimmungsweg=KonstWillenserklaerungWeg.getStimmabgabeSammelkarteKopfweisung(eclMeldungSammelkarte.skIst);
                                                    }
                                                    else {
                                                        /*Weisung des Einzelaktionärs übernehmen*/
                                                        neuesAbstimmungsverhalten=aktionaerWeisungMeldung.abgabe[abstimmposition];
                                                        neuerAbstimmungsweg=KonstWillenserklaerungWeg.getStimmabgabeSammelkarte(eclMeldungSammelkarte.skIst);
                                                    }
                                                }
                                            }
                                        }
                                        setzeMarkierungZuAbstimmungsPositionImplizit(neuesAbstimmungsverhalten, abstimmposition, neuerAbstimmungsweg, sammelkartenIdent);
                                    }
                                    else {
                                        if (altesAbstimmungsverhalten!=KonstStimmart.stimmausschluss) {
                                            /*altesAbstimmungsverhalten !=0 => irgendein Konflikt, wahrscheinlich aus Altstand noch Stimmabgabe für die Einzelkarte,
                                             * z.B. weil EInzelkarte abgestimmt hat und danach in Sammelkarte aufgenommen wurde und dann ausgewertet wurde -> Aktionär
                                             * zählt doppelt
                                             */
                                            CaBug.druckeLog("Für MeldungsIdent "+meldung.meldungsIdent+" AbstimmungsIdent "+abstimmungenZuAktivenBlock[i2].identAbstimmung+" liegt Einzelabstimmungsergebnis vor, aber ist in Sammelkarte", logDrucken, 3);
                                            eintragenWarnungFehler("Für MeldungsIdent "+meldung.meldungsIdent+" AbstimmungsIdent "+abstimmungenZuAktivenBlock[i2].identAbstimmung+" liegt Einzelabstimmungsergebnis vor, aber ist in Sammelkarte");
                                        }
                                    }
                                }
                            }
                        }
                        beendeSpeichernFuerMeldungImplizit();
                    }

                }

            }
        }
    }
    
    private void leseMeldung(int pMeldungsIdent) {
        lDbBundle.dbMeldungen.leseZuIdent(pMeldungsIdent);
        meldung = lDbBundle.dbMeldungen.meldungenArray[0];
    }
    
    /**Falls vorhanden: Abstimmungssatz einlesen, sonst neuen Satz als "leer" anlegen.
     * Voraussetzung: in meldung steht die zu bearbeitende EclMeldung*/
    private void initAbstimmungMeldung(long pZeitstempelrawFuerDieseStimmabgabe, int pErteiltAufWeg, boolean pInJedemFallSpeichern) {
        int rc = lDbBundle.dbAbstimmungMeldung.lese_Ident(meldung.meldungsIdent);
        if (rc < 1) {
            abstimmungMeldung = new EclAbstimmungMeldung();
            abstimmungMeldung.meldungsIdent = meldung.meldungsIdent;
            abstimmungMeldung.stimmen = liefereMeldungStimmenGgfMitHoechststimmrecht(meldung);
            abstimmungMeldung.aktiv = 1;
            abstimmungMeldung.gattung = meldung.liefereGattung();
            abstimmungMeldung.zeitstempelraw = pZeitstempelrawFuerDieseStimmabgabe;
            abstimmungMeldung.erteiltAufWeg = pErteiltAufWeg;
            if (pInJedemFallSpeichern) {
                abstimmungMeldung.erteiltAufWeg = KonstWillenserklaerungWeg.bedingungslosesSpeichern;
            }
            lDbBundle.dbAbstimmungMeldung.insert(abstimmungMeldung);
            speichernWgPriorisierung = true;
        } else {
            abstimmungMeldung = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
            abstimmungMeldung.stimmen = liefereMeldungStimmenGgfMitHoechststimmrecht(meldung); /*Bei jeder Abstimmung sicherheitshalber neu updaten*/
            if (pInJedemFallSpeichern == true) {
                speichernWgPriorisierung = true;
            } else {
                speichernWgPriorisierung = abstimmungRawIstHoeherPriorisiertAlsAbstimmungBereitsGespeichert(
                        abstimmungMeldungRaw, pInJedemFallSpeichern);
            }
            if (speichernWgPriorisierung) {
                abstimmungMeldung.zeitstempelraw = pZeitstempelrawFuerDieseStimmabgabe;
                abstimmungMeldung.erteiltAufWeg = pErteiltAufWeg;
            }
        }
        
    }
    
    private boolean sammelAbstimmungMeldungSplitVorhanden = true;
    private EclAbstimmungMeldungSplit sammelAbstimmungMeldungSplit = null;

    /**Für Sammelkarte pSammelIdent: Setzt sammelAbstimmungMeldungSplitVorhanden und liegt
     * ggf. sammelAbstimmungMeldungSplit ein (sonst Vorbelegung mit null).
     */
    private void belegeSammelAbstimmungMeldungSplit(int pSammelIdent) {
        sammelAbstimmungMeldungSplitVorhanden = true;
        /***abstimmungssplit schon vorhanden? Wenn ja dann einlesen***/
        sammelAbstimmungMeldungSplit = null;
        lDbBundle.dbAbstimmungMeldungSplit.leseIdent(pSammelIdent);
        if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden*/
            sammelAbstimmungMeldungSplitVorhanden = false;
        } else {
            sammelAbstimmungMeldungSplit = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
        }
    }

    private boolean sammelAbstimmungMeldungVorhanden=true;
    private EclAbstimmungMeldung sammelAbstimmungMeldung = null;
    /**Für Sammelkarte pSammelIdent: Setzt sammelAbstimmungMeldungVorhanden und liegt
     * ggf. sammelAbstimmungMeldung ein (sonst Vorbelegung mit null).
     */
    private void belegeSammelAbstimmungMeldung(int pSammelIdent, EclMeldung pMeldungSammelkarte) {
        sammelAbstimmungMeldungVorhanden=true;
        /***Prüfen / Einlesen: ist Abstimmungsverhalten meldungAbstimmung vorhanden? Wenn ja dann einlesen und ggf. aktiv auf 0 setzen***/
        sammelAbstimmungMeldung = null;
        lDbBundle.dbAbstimmungMeldung.lese_Ident(pSammelIdent);
        if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() == 0) {
            sammelAbstimmungMeldungVorhanden=false;
            sammelAbstimmungMeldung = new EclAbstimmungMeldung();
            sammelAbstimmungMeldung.gattung=pMeldungSammelkarte.gattung;
            sammelAbstimmungMeldung.meldungsIdent=pMeldungSammelkarte.meldungsIdent;
            sammelAbstimmungMeldung.stimmen=pMeldungSammelkarte.stimmen;
        }
        if (sammelAbstimmungMeldungVorhanden) { /*Vorhanden, dann einlesen und ggf. auf deaktiv setzen*/
             sammelAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);
             if (sammelAbstimmungMeldung.aktiv!=0) {
                 sammelAbstimmungMeldung.aktiv = 0;
                 lDbBundle.dbAbstimmungMeldung.update(sammelAbstimmungMeldung);
             }
        }
    }

    private EclWeisungMeldungSplit sammelWeisungMeldungSplit = null;
    /**Split-Weisung für Sammelkarte einlesen**/
    private void belegeSammelWeisungMeldungSplit(int pSammelIdent) {
        lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(pSammelIdent, false);
        sammelWeisungMeldungSplit = lDbBundle.dbWeisungMeldung
                .weisungMeldungGefunden(0).weisungMeldungSplit;
    }
    
    
    /**Für Neuauswertung: diese Funktion aufrufen.
     * Sie löscht Stimmausschluß, sowie alle nur für die Archivierung eingetragenen Stimmabgaben
    * Hinsweis: diese Funktion muß "isoliert" lauffähig sein, da sie aktuell automatisch aufgerufen wird, was allerdings bei größeren HVen
     * eine Zeitlang dauern kann. Für diese HVen dann ggf. parameter-gesteuert nur "nach Aufforderung" durchlaufen.
     */
    public void entferneAutomatischEingetrageneStimmabgaben() {
        /*Stimmausschluß (abstMaschinell) und ABSTIMMUNGSERGAENZUNG_ auf 0 setzen*/ 
        for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
            int abstimmposition = lieferePosInWeisung(i1);
            if (abstimmposition != -1) {
                lDbBundle.dbAbstimmungMeldung.update_allPositionMaschinelleAbgaben(abstimmposition);
            }
        }
    }
    
    /*******************************************************************ku310 Spezial*****************************************************************************/
    
    /**Wird bei ku310 über Oberfläche eingegeben beim Auswerten
     * Wenn hier ein Wert drin steht, dann gilt Subtraktionsverfahren. Sonst Additionsverfahren.*/
    public long anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer=-1L;
    public long anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber=-1L;
    
    public int auswertenku310() {

        CaBug.druckeLog("anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer="+anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer+
                "anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber="+anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber, logDrucken, 3);
        lDbBundle.openWeitere();

        /*4. Schritt: Für die Abstimmung gültige Präsenzsummen, Briefwahlsummen, Gesamtstimmenzahl ermitteln: 
         * > aus letzter festgestellter Präsenz
         * > in DbHVDatenLfd Abstimmungsblock mit Präsenznummer "verknüpfen" (für spätere Ausdrucke)
         * */
        BlPraesenzlistenNummer blPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        blPraesenzlistenNummer.leseAktuelleNummern();
        int verwendetePraesenznummer = lDbBundle.clGlobalVar.zuVerzeichnisNr1 - 1;
        if (verwendetePraesenznummer == 0) {
            verwendetePraesenznummer--;
        }

        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();
        lHVDatenLfd.mandant = lDbBundle.clGlobalVar.mandant;
        lHVDatenLfd.ident = 72;
        lHVDatenLfd.benutzer = aktiverAbstimmungsblock.ident;
        lDbBundle.dbHVDatenLfd.read(lHVDatenLfd);
        if (lDbBundle.dbHVDatenLfd.anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = lDbBundle.dbHVDatenLfd.ergebnisPosition(0);
            lHVDatenLfd.benutzer = aktiverAbstimmungsblock.ident;
            lHVDatenLfd.wert = Integer.toString(verwendetePraesenznummer);
            lDbBundle.dbHVDatenLfd.update(lHVDatenLfd);
        } else {/*Neuen Eintrag erzeugen*/
            lHVDatenLfd.wert = Integer.toString(verwendetePraesenznummer);
            lDbBundle.dbHVDatenLfd.insert(lHVDatenLfd);
        }

        /*5. Schritt: abgegebene Stimmen summieren
         * > Summe aus allen abstimmungMeldung, die aktiv sind.
         * > summe aus allen abstimmungMeldungSplit
         * > beide addieren, in abstimmungMeldungSplit (-1) eintragen für späteren Ausdruck
         * 
         * 
         * Gattung 1 = Stimmen
         * Gattung 2 = Aktien
         * 
         */

        /*Summen initialisieren*/
        for (int gattung = 0; gattung <= 5; gattung++) {
            for (int j = 0; j < 200; j++) {
                for (int j1 = 0; j1 < 10; j1++) {
                    abstimmungsSummen[gattung][j][j1] = 0;
                    abstimmungsSummenBriefwahl[gattung][j][j1] = 0;
                }
            }
        }

        /*Summe aus Einzelkarten*/
        for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {

            if (!istUeberschrift(i1)) {
                int identWeisungssatz=lieferePosInWeisung(i1);
                lDbBundle.dbAbstimmungku310.read_alleFuerBestimmteAbstimmung(identWeisungssatz);
                for (int i2=0;i2<lDbBundle.dbAbstimmungku310.anzErgebnis();i2++) {
                    EclAbstimmungku310 lAbstimmungku310=lDbBundle.dbAbstimmungku310.ergebnisPosition(i2);
                    if (lAbstimmungku310.stimmabgabeDurchgefuehrt==1) {
                        abstimmungsSummen[lAbstimmungku310.geberOderNehmer][identWeisungssatz]
                                [1] += lAbstimmungku310.jaStimmen;
                        abstimmungsSummen[lAbstimmungku310.geberOderNehmer][identWeisungssatz]
                                [2] += lAbstimmungku310.neinStimmen;
                        abstimmungsSummen[lAbstimmungku310.geberOderNehmer][identWeisungssatz]
                                [3] += lAbstimmungku310.enthaltungStimmen;

                    }
                }
                /*Falls Subtraktionsverfahren, dann Ja-Stimmen ermitteln*/
                if (anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber!=-1) {
                    abstimmungsSummen[1][identWeisungssatz][1]=
                            anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber-
                            abstimmungsSummen[1][identWeisungssatz][2]-
                            abstimmungsSummen[1][identWeisungssatz][3];
                    abstimmungsSummen[2][identWeisungssatz][1]=
                            anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer-
                            abstimmungsSummen[2][identWeisungssatz][2]-
                            abstimmungsSummen[2][identWeisungssatz][3];
//                    abstimmungsSummen[3][identWeisungssatz][1]=
//                            anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber+anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer-
//                            abstimmungsSummen[3][identWeisungssatz][2]-
//                            abstimmungsSummen[3][identWeisungssatz][3];
                }
            }

        }

        
        
        
        /*abstimmungsSummen[0] (Summe aller Gattungen) und abstimmungsSummenBriefwahl[0] füllen*/
        for (int gattung = 1; gattung <= 5; gattung++) {
            for (int j = 0; j < 200; j++) {
                for (int j1 = 0; j1 < 10; j1++) {
                    abstimmungsSummen[0][j][j1] += abstimmungsSummen[gattung][j][j1];
                    abstimmungsSummenBriefwahl[0][j][j1] += abstimmungsSummenBriefwahl[gattung][j][j1];
                }
            }
        }

        /*Summen in Satz mit Ident -10 bis -15 speichern, briefwahl in Satz mit -20 bis -25*/

        /* -10 bis -15 (Summen) einlesen bzw. anlegen*/
        EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummen = new EclAbstimmungMeldungSplit[6];
        for (int i = 0; i <= 5; i++) {
            int ident = (i + 10) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden - neu anlegen*/
                eclAbstimmungMeldungSummen[i] = new EclAbstimmungMeldungSplit();
                eclAbstimmungMeldungSummen[i].meldungsIdent = ident;
                lDbBundle.dbAbstimmungMeldungSplit.insert(eclAbstimmungMeldungSummen[i]);
            } else { /*Abstimmungssplit schon vorhanden*/
                eclAbstimmungMeldungSummen[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        /* -20 bis -25 (Briefwahl) einlesen bzw. anlegen*/
        EclAbstimmungMeldungSplit[] eclAbstimmungMeldungSummenBriefwahl = new EclAbstimmungMeldungSplit[6];
        for (int i = 0; i <= 5; i++) {
            int ident = (i + 20) * (-1);
            lDbBundle.dbAbstimmungMeldungSplit.leseIdent(ident);
            if (lDbBundle.dbAbstimmungMeldungSplit.anzErgebnisGefunden() < 1) { /*Noch nicht vorhanden - neu anlegen*/
                eclAbstimmungMeldungSummenBriefwahl[i] = new EclAbstimmungMeldungSplit();
                eclAbstimmungMeldungSummenBriefwahl[i].meldungsIdent = ident;
                lDbBundle.dbAbstimmungMeldungSplit.insert(eclAbstimmungMeldungSummenBriefwahl[i]);
            } else { /*Abstimmungssplit schon vorhanden*/
                eclAbstimmungMeldungSummenBriefwahl[i] = lDbBundle.dbAbstimmungMeldungSplit.ergebnisPositionGefunden(0);
            }
        }

        for (int i = 0; i <= 5; i++) {
            for (int i1 = 0; i1 < abstimmungenZuAktivenBlock.length; i1++) {
                if (!istUeberschrift(i1)) {
                    for (int i2 = 0; i2 < 10; i2++) {
                        eclAbstimmungMeldungSummen[i].abgabe[lieferePosInWeisung(
                                i1)][i2] = abstimmungsSummen[i][lieferePosInWeisung(i1)][i2];
                        eclAbstimmungMeldungSummenBriefwahl[i].abgabe[lieferePosInWeisung(
                                i1)][i2] = abstimmungsSummenBriefwahl[i][lieferePosInWeisung(i1)][i2];
                    }
                }
            }
        }

        for (int i = 0; i <= 5; i++) {
            lDbBundle.dbAbstimmungMeldungSplit.update(eclAbstimmungMeldungSummen[i]);
        }
        for (int i = 0; i <= 5; i++) {
            lDbBundle.dbAbstimmungMeldungSplit.update(eclAbstimmungMeldungSummenBriefwahl[i]);
        }

        return 1;

    }

    private long liefereMeldungStimmenGgfMitHoechststimmrecht(EclMeldung pMeldung) {
        long stimmen=pMeldung.stimmen;
        if (lDbBundle.param.paramBasis.hoechststimmrechtHackAktiv==1) {
            if (!pMeldung.zusatzfeld3.isEmpty()) {
                if (CaString.isNummern(pMeldung.zusatzfeld3)) {
                    stimmen=Long.parseLong(pMeldung.zusatzfeld3);
                }
            }
        }
        return stimmen;
    }
    
}
