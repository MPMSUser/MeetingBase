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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiEmittentenMitZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiSubZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufBegriffe;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInsti;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInstiRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Zugrundeliegendes Modell der Zuordnung
 * ======================================
 * EclInsti = Alle Institutionellen "gängigen" werden Mandantenübergreifend angelegt.
 * EclInsti->identSuchlaufBegriffe verweist auf EclSuchlaufBegriffe, in dem die für diesen Insti zuzuordnenden Suchbegriffe gespeichert sind (Mandantenübergreifend)
 *
 * Ablauf ErstZuordnungs-Vorgang je Mandant:
 * > Es wird EclSuchlaufDefinition angelegt, mit 
 * 		> bezeichnung="Insti-Zuordnung"+EclInsti.kurzbezeichnung
 * 		> verwenundg=KonstSuchlaufVerwendung.bestandsZuordnungGovVal/bestandsZuordnungInsti
 * 		> parameter1=EclInsti.ident
 * 		> identSuchlaufBegriffe=EclInsti.identSuchlaufBegriffe
 * 		> identSuchlaufBegriffIstGlobal=1
 * > Das Suchergebnis wird gespeichert in EclSuchlaufErgebnis
 * > Die Bestandszuordnung wird gespeichert in EclInstiBestandsZuordnung
 * 
 * 
 * Zusätzlich können einer Insti noch andere Instis zugeordnet werden - EclInstiSub:
 * Mandantenübergreifend werden "potentielle" Instis zugeordnet, die üblicherweise auch von der Insti vertreten werden.
 * Je Mandant werden dann - basierend auf der Mandantenübergreifenden Zuordnung - die tatsächlich "Sub-Instis" zugeordnet.
 * 
 * Verwendung
 * ==========
 * 1.) Insti-Portal:
 * Die Insti-Login-Kennungen haben Zugriff auf die zugeordneten Bestände, je nach Emittentenfunktion:
 * > Anzeige
 * > Abgabe Willenserklärungen
 * > Ausdruck Gästekarte
 * > Ausdruck Weisungsspiegel, Ausdruck Sammelkartenlisten
 * > Governance-And-Values-Funktionalität
 * 
 * 2.) Sammelanmeldebogen
 * > Ausdruck aller zugeordneten Aktienregistereinträge, wahlweise mit oder ohne Subs (i.d.R. ohne Subs!)
 * > Kennzeichen, falls ein Bestand bereits angemeldet
 * 
 * 3.) Buchen in Sammelkarte
 * > Auswahl der zu bebuchenden Sammelkarte
 * > Anzeige aller zugeordneten Meldungen; es kann noch deselektiert werden
 * > die selektierten Meldungen dann in die Sammelkarte buchen
 * 
 * 
 */

/**Verarbeitung / Erstellen Zuordnung von Beständen zu einer Insti*/
public class BlInsti extends StubRoot {

    public BlInsti(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public EclInsti[] rcInsti = null;
    public EclSuchlaufBegriffe rcSuchlaufBegriffe = null;
    public EclInstiSubZuordnung[] rcEclInstiSubZuordnungUeblichArray = null;
    public EclInstiSubZuordnung[] rcEclInstiSubZuordnungTatsaechlichArray = null;

    /**Ist NICHT die aktuelle Insti!*/
    public EclInsti insti = null;

    public EclUserLogin[] rcUserLoginZuInsti = null;

    public long rcAktienzahl = 0;

    /**Rückgabe für fuelleInstiBestandsZuordnung - 
     * einmal für Aktienregister, und einmal für Meldungen
     */
    public EclInstiBestandsZuordnung[] rcRegInstiBestandsZuordnung = null;
    public EclAktienregister[] rcRegAktienregister = null;
    public EclMeldung[] rcRegMeldung = null;
    public EclUserLogin[] rcRegUserLogin = null;

    public EclInstiBestandsZuordnung[] rcMeldInstiBestandsZuordnung = null;
    public EclAktienregister[] rcMeldAktienregister = null;
    public EclMeldung[] rcMeldMeldung = null;
    public EclUserLogin[] rcMeldUserLogin = null;

    /*1*/
    /**Holt die Insti-Grund-Daten (EclInsti) aus Datenbank.
     * 
     * pInstiIdent=0 => alle, sonst übergebene Ident.
     * 
     * Rückgabe = Anzahl der gelesenen Insti (0 = keine gelesen!)
     * 
     * Ablage erfolgt in:
     * rcInsti
     * 
     * Datenzugriff.
     */
    public int holeInstiDaten(int pInstiIdent) {
        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 1;
            weStubBlInsti.pInstiIdent = pInstiIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }
            rcInsti = weStubBlInstiRC.rcInsti;

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();

        if (pInstiIdent == 0) {
            lDbBundle.dbInsti.readAlle();
        } else {
            lDbBundle.dbInsti.readZuIdent(pInstiIdent);
        }

        int anzInsti = lDbBundle.dbInsti.anzErgebnis();
        if (anzInsti == 0) {
            rcInsti = null;
            dbClose();
            return 0;
        }
        rcInsti = lDbBundle.dbInsti.ergebnisAlsArray();

        dbClose();
        return anzInsti;
    }

    /**Initialisiert die verschiedenen Komponenten für einen neuen, "leeren" Insti*/
    public int neueInsti() {
        insti = new EclInsti();
        return 1;
    }

    /*2*/
    /**Speichern der verschiedenen Komponenten als neue Insti.
     * Die verschiedenen Komponenten müssen vorher mit neueInsti initialisiert worden sein, und anschließend die "spezifischen Felder"
     * belegt werden.
     * 
     * Datenzugriff.*/
    public int neueInstiSpeichern() {

        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 2;
            weStubBlInsti.insti = insti;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();
        EclSuchlaufBegriffe suchlaufBegriffe = new EclSuchlaufBegriffe();
        suchlaufBegriffe.bezeichnung = "Insti. " + insti.kurzBezeichnung;
        suchlaufBegriffe.suchbegriffe = insti.suchlaufBegriffe;

        lDbBundle.dbSuchlaufBegriffe.insert(true, suchlaufBegriffe);
        insti.identSuchlaufBegriffe = suchlaufBegriffe.ident;

        lDbBundle.dbInsti.insert(insti);

        dbClose();
        return 1;
    }

    /*5*/
    /**Ändern der Insti.
     * Ergebnis steht in this.insti (geänderte dbVersion!) und rcSuchlaufBegriffe.
     * 
     * Datenzugriff.*/
    public int aendernInstiSpeichern(EclInsti pInsti, EclSuchlaufBegriffe pSuchlaufBegriffe) {

        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 5;
            weStubBlInsti.insti = pInsti;
            weStubBlInsti.suchlaufBegriffe = pSuchlaufBegriffe;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            insti = weStubBlInstiRC.insti;
            rcSuchlaufBegriffe = weStubBlInstiRC.rcSuchlaufBegriffe;

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();

        int rc = lDbBundle.dbInsti.update(pInsti);
        if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            dbClose();
            return rc;
        }
        rcSuchlaufBegriffe = pSuchlaufBegriffe;
        rcSuchlaufBegriffe.suchbegriffe = pInsti.suchlaufBegriffe;
        lDbBundle.dbSuchlaufBegriffe.update(true, rcSuchlaufBegriffe);

        dbClose();
        return 1;
    }

    /*3*/
    /**Holen Zusatzdaten zu Insti pAktuelleInsti.
     * 
     * belegt werden:
     * rcSuchlaufBegriffe
     * 
     * Datenzugriff.*/
    public int leseInstiZusatzdaten(EclInsti pAktuelleInsti) {

        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 3;
            weStubBlInsti.insti = pAktuelleInsti;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            rcSuchlaufBegriffe = weStubBlInstiRC.rcSuchlaufBegriffe;
            rcEclInstiSubZuordnungUeblichArray = weStubBlInstiRC.rcEclInstiSubZuordnungUeblichArray;
            rcEclInstiSubZuordnungTatsaechlichArray = weStubBlInstiRC.rcEclInstiSubZuordnungTatsaechlichArray;
            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();

        lDbBundle.dbSuchlaufBegriffe.read(true, pAktuelleInsti.identSuchlaufBegriffe);
        rcSuchlaufBegriffe = lDbBundle.dbSuchlaufBegriffe.ergebnisPosition(0);

        lDbBundle.dbInstiSubZuordnung.read(true, pAktuelleInsti.ident);
        rcEclInstiSubZuordnungUeblichArray = lDbBundle.dbInstiSubZuordnung.ergebnis();

        lDbBundle.dbInstiSubZuordnung.read(false, pAktuelleInsti.ident);
        rcEclInstiSubZuordnungTatsaechlichArray = lDbBundle.dbInstiSubZuordnung.ergebnis();

        dbClose();
        return 1;
    }

    /*6*/
    /**Holt alle Benutzerkennungen, die der Insti zugeordnet sind.
     * Rückgabe in rcUserLoginZuInsti*/
    public int leseInstiKennungen(EclInsti pAktuelleInsti) {

        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 6;
            weStubBlInsti.insti = pAktuelleInsti;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            rcUserLoginZuInsti = weStubBlInstiRC.rcUserLoginZuInsti;
            return weStubBlInstiRC.rc;
        }

        dbOpen();
        lDbBundle.dbUserLogin.lese_allZuInsti(pAktuelleInsti.ident, 0);
        rcUserLoginZuInsti = lDbBundle.dbUserLogin.userLoginArray;
        dbClose();
        return 1;
    }

    /*4*/
    /**Speichere InstiSubZuordnung.
     * 
     * Global (pGlobal=true), oder Lokal, je nach Parameter.
     */
    public int speichereInstiSubZuordnung(boolean pGlobal, int pInstiIdent,
            List<EclInstiSubZuordnung> pInstiSubZuordnung) {

        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 4;
            weStubBlInsti.pGlobal = pGlobal;
            weStubBlInsti.pInstiIdent = pInstiIdent;
            weStubBlInsti.pInstiSubZuordnung = pInstiSubZuordnung;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();
        lDbBundle.dbInstiSubZuordnung.delete(pGlobal, pInstiIdent);
        for (int i = 0; i < pInstiSubZuordnung.size(); i++) {
            lDbBundle.dbInstiSubZuordnung.insert(pGlobal, pInstiSubZuordnung.get(i));
        }
        dbClose();
        return 1;
    }

    /*7*/
    /**Ergebnis wird in rcAktienzahl abgelegt*/
    public int liefereZugeordneteAktienZahlFuerAktionaersnummer(int pInstiIdent, int pAktionaersIdent) {
        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 7;
            weStubBlInsti.pInstiIdent = pInstiIdent;
            weStubBlInsti.pAktionaersIdent = pAktionaersIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }
            rcAktienzahl = weStubBlInstiRC.rcAktienzahl;

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();
        rcAktienzahl = 0;
        int rc = lDbBundle.dbInstiBestandsZuordnung.readAktienregisterIdent(pAktionaersIdent);
        if (rc > 0) {
            for (int i = 0; i < rc; i++) {
                /*Denkhinweis: funktioniert auch, wenn alle zugeordnet (dann 0+ (-1) =-1 = alle)*/
                rcAktienzahl += lDbBundle.dbInstiBestandsZuordnung.ergebnisPosition(i).zugeordneteStimmen;
            }
        }
        dbClose();
        return 1;
    }

    /*8*/
    /**Liest die Zuordnungen für Aktienregister und Meldungen ein.
     * Ablage in
     * rcRegInstiBestandsZuordnung, rcRegAktienregister, rcRegMeldung, rcRegUserLogin,
     * rcMeldInstiBestandsZuordnung, rcMeldAktienregister, rcMeldMeldung, rcMeldUserLogin
     * 
     * Führt gleichzeitig einen Update von EclInstiEmittentenMitZuordnung aus (je nachdem 
     * ob noch eine Zuordnung vorhanden ist oder nicht)
     */
    public int fuelleInstiBestandsZuordnung(int pInstiIdent) {
        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 8;
            weStubBlInsti.pInstiIdent = pInstiIdent;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }
            rcRegInstiBestandsZuordnung = weStubBlInstiRC.rcRegInstiBestandsZuordnung;
            rcRegAktienregister = weStubBlInstiRC.rcRegAktienregister;
            rcRegMeldung = weStubBlInstiRC.rcRegMeldung;
            rcRegUserLogin = weStubBlInstiRC.rcRegUserLogin;

            rcMeldInstiBestandsZuordnung = weStubBlInstiRC.rcMeldInstiBestandsZuordnung;
            rcMeldAktienregister = weStubBlInstiRC.rcMeldAktienregister;
            rcMeldMeldung = weStubBlInstiRC.rcMeldMeldung;
            rcMeldUserLogin = weStubBlInstiRC.rcMeldUserLogin;

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();

        lDbBundle.dbJoined.read_instiBestandsZuordnung_Aktienregister(pInstiIdent);
        rcRegInstiBestandsZuordnung = lDbBundle.dbJoined.ergebnisInstiBestandsZuordnung();
        rcRegAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
        rcRegMeldung = lDbBundle.dbJoined.ergebnisMeldung();
        rcRegUserLogin = lDbBundle.dbJoined.ergebnisUserLogin();

        lDbBundle.dbJoined.read_instiBestandsZuordnung_Meldungen(pInstiIdent);
        rcMeldInstiBestandsZuordnung = lDbBundle.dbJoined.ergebnisInstiBestandsZuordnung();
        rcMeldAktienregister = lDbBundle.dbJoined.ergebnisAktienregisterEintrag();
        rcMeldMeldung = lDbBundle.dbJoined.ergebnisMeldung();
        rcMeldUserLogin = lDbBundle.dbJoined.ergebnisUserLogin();

        int anzahlZuordnungen = rcRegAktienregister.length + rcMeldMeldung.length;
        EclInstiEmittentenMitZuordnung lInstiEmittentenMitZuordnung = new EclInstiEmittentenMitZuordnung();
        lInstiEmittentenMitZuordnung.identInsti = pInstiIdent;
        lInstiEmittentenMitZuordnung.mandant = lDbBundle.clGlobalVar.mandant;
        lInstiEmittentenMitZuordnung.hvJahr = lDbBundle.clGlobalVar.hvJahr;
        lInstiEmittentenMitZuordnung.hvNummer = lDbBundle.clGlobalVar.hvNummer;
        lInstiEmittentenMitZuordnung.dbArt = lDbBundle.clGlobalVar.datenbereich;
        int anzDBEintraege = lDbBundle.dbInstiEmittentenMitZuordnung.read(lInstiEmittentenMitZuordnung);
        if (anzahlZuordnungen == 0 && anzDBEintraege != 0) {
            lDbBundle.dbInstiEmittentenMitZuordnung.delete(lInstiEmittentenMitZuordnung);
        }
        if (anzahlZuordnungen != 0 && anzDBEintraege == 0) {
            lDbBundle.dbInstiEmittentenMitZuordnung.insert(lInstiEmittentenMitZuordnung);
        }

        dbClose();
        return 1;
    }

    /*9*/
    public int loeschenZuordnung(EclInstiBestandsZuordnung[] pZuordnungenLoeschen) {
        if (verwendeWebService()) {
            WEStubBlInsti weStubBlInsti = new WEStubBlInsti();
            weStubBlInsti.stubFunktion = 9;
            weStubBlInsti.pZuordnungenLoeschen = pZuordnungenLoeschen;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlInsti.setWeLoginVerify(weLoginVerify);

            WEStubBlInstiRC weStubBlInstiRC = wsClient.stubBlInsti(weStubBlInsti);

            if (weStubBlInstiRC.rc < 1) {
                return weStubBlInstiRC.rc;
            }

            return weStubBlInstiRC.rc;
        }

        dbOpenUndWeitere();
        for (int i = 0; i < pZuordnungenLoeschen.length; i++) {
            lDbBundle.dbInstiBestandsZuordnung.delete(pZuordnungenLoeschen[i].ident,
                    pZuordnungenLoeschen[i].identUserLogin);
        }
        dbClose();

        return 1;
    }
}
