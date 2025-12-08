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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragStatusAuftragsArt;

public class BlAuftragAnbindungAktienregister {

    private DbBundle dbBundle = null;

    private int logDrucken = 3;

    public BlAuftragAnbindungAktienregister(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernNewsletter(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, int pVersandart) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER;
        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_GLEICHERLEDIGT;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterInt[0] = pVersandart;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernAnschrift(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pAdresszusatz, String pStrasse, String pPLZ, String pOrt, int pLandNummer) {

        String landText = "";
        dbBundle.dbStaaten.mandantenabhaengig = true;
        dbBundle.dbStaaten.readId(pLandNummer);
        if (dbBundle.dbStaaten.anzErgebnis() > 0) {
            landText = dbBundle.dbStaaten.ergebnisPosition(0).nameDE;
        }
        dbBundle.dbStaaten.mandantenabhaengig = false;

        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_ANSCHRIFT;
        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pAdresszusatz;
        lAuftrag.parameterTextLang[1] = pStrasse;
        lAuftrag.parameterTextLang[2] = pPLZ;
        lAuftrag.parameterTextLang[3] = pOrt;
        lAuftrag.parameterTextLang[4] = landText;
        lAuftrag.parameterInt[0] = pLandNummer;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pArt=KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1, 2, 3, EMAIL
     * pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernKontaktdaten(int pArt, String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pWert) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = pArt;
        if (pArt == KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL) {
            /*In diesem Fall: gleich komplett erledigt*/
            lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
            lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_GLEICHERLEDIGT;
        } else {
            lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
            lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_GLEICHERLEDIGT;
        }

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pWert;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernGeburtsdatum(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pGeburtsdatum, int personenIdent) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_GEBURTSDATUM;
        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pGeburtsdatum;
        lAuftrag.parameterInt[0] = personenIdent;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernSteuerId(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pSteuerId, int personenIdent) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_STEUERID;
        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pSteuerId;
        lAuftrag.parameterInt[0] = personenIdent;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int aendernBankverbindung(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pKontoinhaber, String pBankname, String pIban, String pBic) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;
        lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_BANKVERBINDUNG;
        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pKontoinhaber;
        lAuftrag.parameterTextLang[1] = pBankname;
        lAuftrag.parameterTextLang[2] = pIban;
        lAuftrag.parameterTextLang[3] = pBic;

        dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

    /**pAktionaersnummer=Kennung
     * pZeit = Auftragserteilungszeit in der Form JJJJ.MM.TT HH:MM:SS
     */
    public int antragBeteiligungserhoehung(String pAktionaersnummer, int pUserId, String pSchluessel, String pZeit, String pErhoehungsbetrag, String pAuffuellbetrag, int pWeitereAnteile, String art, String pZahlungsart) {
        EclAuftrag lAuftrag = new EclAuftrag();

        lAuftrag.mandant = dbBundle.clGlobalVar.mandant;
        lAuftrag.hvJahr = dbBundle.clGlobalVar.hvJahr;
        lAuftrag.hvNummer = dbBundle.clGlobalVar.hvNummer;
        lAuftrag.datenbereich = dbBundle.clGlobalVar.datenbereich;

        lAuftrag.gehoertZuModul = KonstAuftragModul.ANBINDUNG_AKTIENREGISTER;

        //        lAuftrag.auftragsArt=KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG;
        switch (art) {
        case "0":
            lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_OHNE_AUFFUELLBETRAG;
            break;
        case "1":
            lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_UND_AUFFUELLBETRAG;
            break;
        case "2":
            lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_OHNE_ZEICHNUNG_MIT_AUFFUELLBETRAG;
            break;
        case "3":
            lAuftrag.auftragsArt = KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_OHNE_AUFFUELLBETRAG_TEXTFORM;
            break;
        }

        lAuftrag.status = KonstAuftragStatus.IN_ARBEIT;
        lAuftrag.statusAuftragsArt = KonstAuftragStatusAuftragsArt.ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;

        lAuftrag.userIdAuftraggeber = pUserId;
        lAuftrag.schluessel = pSchluessel;

        lAuftrag.zeitStart = pZeit;
        lAuftrag.zeitLetzterStatuswechsel = pZeit;

        lAuftrag.statusAuftragsArtGelesen = 0;
        lAuftrag.statusAuftragsArtGeloescht = 0;

        lAuftrag.parameterTextLang[0] = pErhoehungsbetrag;
        lAuftrag.parameterTextLang[1] = pAuffuellbetrag;
        lAuftrag.parameterTextLang[2] = String.valueOf(pWeitereAnteile);

        /**
         * 0 = weitere Zeichung ohne Auffuellbetrag
         * 1 = Auffuellbetrag + weitere Zeichnung
         * 2 = Auffuellbetrag ohne weitere Zeichung = ohne GenossenschaftSys
         * 3 = weitere Zeichung ohne Auffuellbetrag Textform
         */
        lAuftrag.parameterTextLang[3] = art;
        lAuftrag.parameterTextLang[4] = pAktionaersnummer;
        /**
         * 0 = Überweisung
         * 1 = SEPA
         */
        lAuftrag.parameterTextLang[5] = pZahlungsart;
        int rc = dbBundle.dbAuftrag.insert(lAuftrag);

        return 1;
    }

}
