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
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlag;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlagEmpfehlung;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import de.meetingapps.meetingportal.meetComStub.StubRoot;

/**Grundsätzliche Hinweise:
 * 
 * Als Sicht wird immer KonstWeisungserfassungSicht.portalWeisungserfassung verwendet.
 * Gegenanträge werden nie separat ausgewiesen.
 * 
 * Die Weisungsempfehlung wird unter der Sammelkarte SRV Papier mit Gattung 1 gespeichert.
 */

/**TODO Stub grundsätzlich vorbereitet, aber nicht umgesetzt!*/
public class BlAbstimmungsVorschlagEmpfehlung extends StubRoot {

    private int logDrucken = 3;

    /**Enthält immer alle Abstimmungen, unabhängig von Gattung*/
    public EclAbstimmung[] pAngezeigteAbstimmungen = null;
    public int[] rcEmpfehlungsArray = null;

    /**Enthält immer alle Abstimmungen, unabhängig von Gattung - aber nur für die, 
     * für die die aktuelle Abstimmungsweisung abgegeben wurde*/
    public List<EclAbstimmung> rcAngezeigteAbstimmungenListe = null;
    public List<Integer> rcEmpfehlungsListe = null;

    public int rcAbstimmungsIdentWeisungssatz = 0;
    public int rcAbstimmungsIdent = 0;
    public EclAbstimmung rcAbstimmung = null;

    public EclAbstimmungsVorschlagEmpfehlung rcAbstimmungsVorschlagEmpfehlung = null;

    public BlAbstimmungsVorschlagEmpfehlung(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**Erzeugt für Anzeige:
     * Liste der vorhandenen Abstimmungen pAngezeigteAbstimmungen
     * Liste der dazugehörigen Weisungsempfehlungen rcEmpfehlungsArray
     */
    public int erzeugeUebersicht() {

        CaBug.druckeLog("BlAbstimmungsVorschlagEmpfehlung.erzeugeUebersicht", logDrucken, 10);

        dbOpen();
        /*Liste der vorhandenen Weisungen*/
        StubAbstimmungen stubAbstimmungen = new StubAbstimmungen(true, lDbBundle);
        stubAbstimmungen.zeigeSortierungAbstimmungenInit(0, 1, 0);
        pAngezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;
        CaBug.druckeLog("Anzahl pAngezeigteAbstimmungen=" + pAngezeigteAbstimmungen.length, logDrucken, 10);

        /*Sammelkarte SRV Papier einlesen - das ist die, zu der die Weisungsempfehlungen gespeichert werden*/
        lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(1);
        int identSammelkarteFuerAbstimmungsvorschlag = lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent;

        /*Abstimmungsvorschlag einlesen*/
        lDbBundle.dbAbstimmungsVorschlag.lese(identSammelkarteFuerAbstimmungsvorschlag);
        EclAbstimmungsVorschlag lAbstimmungsVorschlag = lDbBundle.dbAbstimmungsVorschlag
                .abstimmungsVorschlagGefunden(0);

        /*Liste der Weisungsempfehlungen*/
        rcEmpfehlungsArray = new int[pAngezeigteAbstimmungen.length];
        for (int i = 0; i < pAngezeigteAbstimmungen.length; i++) {
            int pos = pAngezeigteAbstimmungen[i].identWeisungssatz;
            if (pos == -1) {
                rcEmpfehlungsArray[i] = -1;
            } else {
                rcEmpfehlungsArray[i] = lAbstimmungsVorschlag.abgabe[pos];
            }
        }
        dbClose();
        return 1;
    }

    /**Erzeugt für Anzeige:
     * Für alle Abstimmungen:
     * Liste der vorhandenen Abstimmungen pAngezeigteAbstimmungen
     * Liste der dazugehörigen Weisungsempfehlungen rcEmpfehlungsArray
     * 
     * Für die Abstimmungen, für die eine Empfehlung gemäß pAbstimmungsVorschlagEmpfehlung gegeben wurde:
     * Liste der vorhandenen Abstimmungen rcAngezeigteAbstimmungenListe
     * Liste der dazugehörigen Weisungsempfehlungen rcEmpfehlungsListe 
     */
    public int erzeugeUebersichtFuerEmpfehlung(EclAbstimmungsVorschlagEmpfehlung pAbstimmungsVorschlagEmpfehlung) {

        CaBug.druckeLog("BlAbstimmungsVorschlagEmpfehlung.erzeugeUebersichtFuerEmpfehlung", logDrucken, 10);

        dbOpen();
        /*Liste der vorhandenen Weisungen*/
        StubAbstimmungen stubAbstimmungen = new StubAbstimmungen(true, lDbBundle);
        stubAbstimmungen.zeigeSortierungAbstimmungenInit(0, 1, 0);
        pAngezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;
        CaBug.druckeLog("Anzahl pAngezeigteAbstimmungen=" + pAngezeigteAbstimmungen.length, logDrucken, 10);

        /*Sammelkarte SRV Papier einlesen - das ist die, zu der die Weisungsempfehlungen gespeichert werden*/
        lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(1);
        int identSammelkarteFuerAbstimmungsvorschlag = lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent;

        /*Abstimmungsvorschlag einlesen*/
        lDbBundle.dbAbstimmungsVorschlag.lese(identSammelkarteFuerAbstimmungsvorschlag);
        EclAbstimmungsVorschlag lAbstimmungsVorschlag = lDbBundle.dbAbstimmungsVorschlag
                .abstimmungsVorschlagGefunden(0);

        /*Liste der Weisungsempfehlungen*/
        rcEmpfehlungsArray = new int[pAngezeigteAbstimmungen.length];
        for (int i = 0; i < pAngezeigteAbstimmungen.length; i++) {
            int pos = pAngezeigteAbstimmungen[i].identWeisungssatz;
            if (pos == -1) {
                rcEmpfehlungsArray[i] = -1;
            } else {
                rcEmpfehlungsArray[i] = lAbstimmungsVorschlag.abgabe[pos];
            }
        }

        /*Nun in die Listen übertragen*/
        rcAngezeigteAbstimmungenListe = new LinkedList<EclAbstimmung>();
        rcEmpfehlungsListe = new LinkedList<Integer>();
        for (int i = 0; i < pAngezeigteAbstimmungen.length; i++) {
            if (pAbstimmungsVorschlagEmpfehlung.empfehlungFuerAbstimmungsIdent[pAngezeigteAbstimmungen[i].ident] > 0) {
                rcAngezeigteAbstimmungenListe.add(pAngezeigteAbstimmungen[i]);
                rcEmpfehlungsListe.add(
                        pAbstimmungsVorschlagEmpfehlung.empfehlungFuerAbstimmungsIdent[pAngezeigteAbstimmungen[i].ident]);
            }
        }

        dbClose();
        return 1;
    }

    /**Lege neue Abstimmung an - für GoVVal, vorbereitet für Weisungsempfehlungsabgabe
     * 
     * pAngezeigteAbstimmungen muß gefüllt sein.
     * 
     * Return = -1 - Einfügen nicht möglich, z.B. weil zu viele Abstimmungen
     * rcAbstimmungsIdentWeisungssatz
     * rcAbstimmungsIdent
     * */
    public int legeNeueAbstimmungAn(String pTOP, String pTOPIndex, String pTextDeutsch, String pTextEnglisch,
            int[] pGattung, boolean pIstUeberschrift, int pEmpfehlung) {
        dbOpen();

        StubAbstimmungen stubAbstimmungen = new StubAbstimmungen(true, lDbBundle);
        stubAbstimmungen.zeigeSortierungAbstimmungenInit(0, 1, 0);
        pAngezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;

        BlAbstimmungsListeBearbeiten blAbstimmungsListeBearbeiten = new BlAbstimmungsListeBearbeiten(
                pAngezeigteAbstimmungen);

        EclAbstimmung neueAbstimmung = new EclAbstimmung();

        rcAbstimmungsIdentWeisungssatz = -1;
        if (pIstUeberschrift) {
            neueAbstimmung.identWeisungssatz = -1;
        } else {
            rcAbstimmungsIdentWeisungssatz = blAbstimmungsListeBearbeiten.liefereFreieIdentWeisungssatz();
            if (rcAbstimmungsIdentWeisungssatz == -1) {
                return -1;
            }
            neueAbstimmung.identWeisungssatz = rcAbstimmungsIdentWeisungssatz;
        }

        neueAbstimmung.nummer = pTOP;
        neueAbstimmung.nummerEN = pTOP;
        neueAbstimmung.nummerKey = pTOP;
        neueAbstimmung.nummerFormular = pTOP;

        neueAbstimmung.nummerindex = pTOPIndex;
        neueAbstimmung.nummerindexEN = pTOPIndex;
        neueAbstimmung.nummerindexKey = pTOPIndex;
        neueAbstimmung.nummerindexFormular = pTOPIndex;

        neueAbstimmung.kurzBezeichnung = "";
        neueAbstimmung.kurzBezeichnungEN = "";

        neueAbstimmung.anzeigeBezeichnungKurz = pTextDeutsch;
        neueAbstimmung.anzeigeBezeichnungKurzEN = pTextEnglisch;

        neueAbstimmung.anzeigeBezeichnungLang = "";
        neueAbstimmung.anzeigeBezeichnungLangEN = "";

        neueAbstimmung.aktiv = 1;
        neueAbstimmung.aktivWeisungenInPortal = 1;
        neueAbstimmung.aktivWeisungenAnzeige = 1;
        neueAbstimmung.aktivWeisungenInterneAuswertungen = 1;
        neueAbstimmung.aktivWeisungenExterneAuswertungen = 1;
        neueAbstimmung.aktivWeisungenPflegeIntern = 1;

        neueAbstimmung.aktivBeiSRV = 1;
        neueAbstimmung.aktivBeiBriefwahl = 1;
        neueAbstimmung.aktivBeiKIAVDauer = 1;

        neueAbstimmung.internJa = 1;
        neueAbstimmung.internNein = 1;
        neueAbstimmung.internEnthaltung = 1;
        neueAbstimmung.internUngueltig = 0;

        /*Am Ende aufnehmen*/
        neueAbstimmung.anzeigePositionIntern = blAbstimmungsListeBearbeiten.liefere_AnzeigePositionIntern_AmEnde();
        neueAbstimmung.anzeigePositionExternWeisungen = blAbstimmungsListeBearbeiten
                .liefere_AnzeigePositionExternWeisungen_AmEnde();
        neueAbstimmung.anzeigePositionExternWeisungenHV = blAbstimmungsListeBearbeiten
                .liefere_AnzeigePositionExternWeisungenHV_AmEnde();

        stubAbstimmungen.insertAbstimmung(neueAbstimmung, null);

        rcAbstimmungsIdent = neueAbstimmung.ident;

        /*+++++Abstimmungsvorschlag speichern++++++*/
        /*Sammelkarte SRV Papier einlesen - das ist die, zu der die Weisungsempfehlungen gespeichert werden*/
        lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(1);
        int identSammelkarteFuerAbstimmungsvorschlag = lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent;

        /*Abstimmungsvorschlag einlesen*/
        lDbBundle.dbAbstimmungsVorschlag.lese(identSammelkarteFuerAbstimmungsvorschlag);
        EclAbstimmungsVorschlag lAbstimmungsVorschlag = lDbBundle.dbAbstimmungsVorschlag
                .abstimmungsVorschlagGefunden(0);
        lAbstimmungsVorschlag.abgabe[rcAbstimmungsIdentWeisungssatz] = pEmpfehlung;
        lDbBundle.dbAbstimmungsVorschlag.update(lAbstimmungsVorschlag);

        dbClose();
        return 1;
    }

    /**Belegt:
     * rcAbstimmungsVorschlagEmpfehlung
     */
    public int legeAbstimmungsVorschlagEmpfehlungAn(String pKurzBeschreibung, List<Integer> pAbstimmungsIdent,
            List<Integer> pWeisung, int pUserId) {

        dbOpenUndWeitere();
        rcAbstimmungsVorschlagEmpfehlung = new EclAbstimmungsVorschlagEmpfehlung();
        rcAbstimmungsVorschlagEmpfehlung.mandant = lDbBundle.clGlobalVar.mandant;
        rcAbstimmungsVorschlagEmpfehlung.erstellungszeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();
        rcAbstimmungsVorschlagEmpfehlung.erstelltVonUserId = pUserId;
        rcAbstimmungsVorschlagEmpfehlung.kurzBeschreibung = pKurzBeschreibung;
        for (int i = 0; i < pAbstimmungsIdent.size(); i++) {
            rcAbstimmungsVorschlagEmpfehlung.empfehlungFuerAbstimmungsIdent[pAbstimmungsIdent.get(i)] = pWeisung.get(i);
        }
        lDbBundle.dbAbstimmungsVorschlagEmpfehlung.insert(rcAbstimmungsVorschlagEmpfehlung);
        dbClose();

        return 1;
    }

    public int updateAbstimmungsVorschlagEmpfehlung(EclAbstimmungsVorschlagEmpfehlung pAbstimmungsVorschlagEmpfehlung) {

        dbOpenUndWeitere();
        lDbBundle.dbAbstimmungsVorschlagEmpfehlung.update(pAbstimmungsVorschlagEmpfehlung);
        dbClose();
        return 1;
    }

    public int updateAbstimmungsVorschlagFuerAbstimmungen(List<Integer> pAbstimmungsIdent, List<Integer> pWeisungsIdent,
            List<Integer> pWeisung) {
        dbOpenUndWeitere();

        /*Sammelkarte SRV Papier einlesen - das ist die, zu der die Weisungsempfehlungen gespeichert werden*/
        lDbBundle.dbMeldungen.leseSammelkarteSRVPapier(1);
        int identSammelkarteFuerAbstimmungsvorschlag = lDbBundle.dbMeldungen.meldungenArray[0].meldungsIdent;

        /*Abstimmungsvorschlag einlesen*/
        lDbBundle.dbAbstimmungsVorschlag.lese(identSammelkarteFuerAbstimmungsvorschlag);
        EclAbstimmungsVorschlag lAbstimmungsVorschlag = lDbBundle.dbAbstimmungsVorschlag
                .abstimmungsVorschlagGefunden(0);
        for (int i = 0; i < pAbstimmungsIdent.size(); i++) {
            lAbstimmungsVorschlag.abgabe[pWeisungsIdent.get(i)] = pWeisung.get(i);
        }
        lDbBundle.dbAbstimmungsVorschlag.update(lAbstimmungsVorschlag);

        dbClose();
        return 1;
    }

    public int leseAbstimmung(int pAbstimmungsIdent) {
        dbOpen();
        lDbBundle.dbAbstimmungen.leseIdent(pAbstimmungsIdent);
        rcAbstimmung = lDbBundle.dbAbstimmungen.abstimmungengGefunden(0);
        dbClose();
        return 1;
    }

    /**Liest Abstimmung mit pIdent ein und speichert sie mit den veränderten Parametern zurück*/
    public int aendereAbstimmung(int pAbstimmungsIdent, String pTOP, String pTOPIndex, String pTextDeutsch,
            String pTextEnglisch) {
        dbOpen();

        lDbBundle.dbAbstimmungen.leseIdent(pAbstimmungsIdent);
        rcAbstimmung = lDbBundle.dbAbstimmungen.abstimmungengGefunden(0);

        rcAbstimmung.nummer = pTOP;
        rcAbstimmung.nummerEN = pTOP;
        rcAbstimmung.nummerKey = pTOP;
        rcAbstimmung.nummerFormular = pTOP;

        rcAbstimmung.nummerindex = pTOPIndex;
        rcAbstimmung.nummerindexEN = pTOPIndex;
        rcAbstimmung.nummerindexKey = pTOPIndex;
        rcAbstimmung.nummerindexFormular = pTOPIndex;

        rcAbstimmung.anzeigeBezeichnungKurz = pTextDeutsch;
        rcAbstimmung.anzeigeBezeichnungKurzEN = pTextEnglisch;

        lDbBundle.dbAbstimmungen.update(rcAbstimmung);

        dbClose();
        return 1;
    }

}
