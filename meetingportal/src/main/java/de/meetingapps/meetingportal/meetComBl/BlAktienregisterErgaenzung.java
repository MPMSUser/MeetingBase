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

import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Stub vorbereitet, aber nicht umgesetzt
 * 
 * Alle Methoden benötigen openWeitere!*/
public class BlAktienregisterErgaenzung extends StubRoot {

    public EclAktienregisterErgaenzung rcEclAktienregisterErgaenzung = null;

    public boolean rcAenderungenVorhanden = false;
    public boolean[] rcAenderungLangString = null;
    public boolean[] rcAenderungKurzString = null;
    public boolean[] rcAenderungKennzeichen = null;

    public BlAktienregisterErgaenzung(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**Liefert rcEclAktienregisterErgaenzung*/
    public int holeLetztenErgaenzungssatz(int pAktienregisterIdent) {

        dbOpenUndWeitere();

        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktienregisterIdent);
        /**Letzter gefundener Satz ist letzte Änderung*/
        rcEclAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung
                .ergebnisPosition(lDbBundle.dbAktienregisterErgaenzung.anzErgebnis() - 1);
        dbClose();

        return 1;
    }

    /**Liefert:
     * rcAenderungenVorhanden
     * rcAenderungLangString, rcAenderungKurzString, rcAenderungKennzeichen
     * 
     * pEclAktienregisterErgaenzung wird ebenfalls verändert (ergaenzungKennzeichen ergänzt)
     */
    public int speichereAenderung(EclAktienregisterErgaenzung pEclAktienregisterErgaenzung) {
        dbOpenUndWeitere();
        /*Letzten Satz ermitteln (zum Vergleich) - hier werden aber gleichzeitig alle eingelesen, wichtig für Insert (nächste satzNummer bestimmen)*/
        lDbBundle.dbAktienregisterErgaenzung.readZuident(pEclAktienregisterErgaenzung.aktienregisterIdent);
        EclAktienregisterErgaenzung altAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung
                .ergebnisPosition(lDbBundle.dbAktienregisterErgaenzung.anzErgebnis() - 1);

        /*Vergleichs-Ergebnis initialisieren*/
        rcAenderungenVorhanden = false;

        rcAenderungLangString = new boolean[30];
        rcAenderungKurzString = new boolean[30];
        rcAenderungKennzeichen = new boolean[30];
        for (int i = 0; i < 30; i++) {
            rcAenderungLangString[i] = false;
            rcAenderungKurzString[i] = false;
            rcAenderungKennzeichen[i] = false;
        }

        /*Überprüfen, ob Änderungen vorhanden*/
        for (int i = 0; i < 30; i++) {
            if (pEclAktienregisterErgaenzung.ergaenzungLangString[i].trim()
                    .compareTo(altAktienregisterErgaenzung.ergaenzungLangString[i].trim()) != 0) {
                rcAenderungLangString[i] = true;
                rcAenderungenVorhanden = true;
            }
            if (pEclAktienregisterErgaenzung.ergaenzungKurzString[i].trim()
                    .compareTo(altAktienregisterErgaenzung.ergaenzungKurzString[i].trim()) != 0) {
                rcAenderungKurzString[i] = true;
                rcAenderungenVorhanden = true;
            }
            if (pEclAktienregisterErgaenzung.ergaenzungKennzeichen[i] != altAktienregisterErgaenzung.ergaenzungKennzeichen[i]) {
                rcAenderungKennzeichen[i] = true;
                rcAenderungenVorhanden = true;
            }
        }

        if (rcAenderungenVorhanden == true) {
            /*Änderungen vorhanden - diese jetzt abspeichern*/
            pEclAktienregisterErgaenzung.satzNummer = lDbBundle.dbAktienregisterErgaenzung.anzErgebnis();
            pEclAktienregisterErgaenzung.mandant = lDbBundle.clGlobalVar.mandant;
            lDbBundle.dbAktienregisterErgaenzung.insert(pEclAktienregisterErgaenzung);
        }

        dbClose();

        return 1;
    }

    /**Drucken in PDF*/
    public boolean rcDruckenInPDF = true;

    /**Dateinr des erzeugten Formulars für Ausdruck - 6-stellige-Ident + 3-stellige LfdNummer*/
    public String rcPdfName = "";

    /**Formular-Nummer, das für den Druck verwendet werden soll. */
    public int pFormular = 1;

    /**
     * Der "neue" Satz muß vor Aufruf dieser Funktion abgespeichert worden sein!
     * Verwendet:
     * rcDruckenInPDF
     * 
     * Erzeugt:
     */
    public int druckeFormular(int pAktienregisterIdent, String pMitgliedsnummer) {
        dbOpenUndWeitere();
        RpDrucken rpDrucken = new RpDrucken();

        lDbBundle.dbAktienregisterErgaenzung.readZuident(pAktienregisterIdent);
        EclAktienregisterErgaenzung neuAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung
                .ergebnisPosition(lDbBundle.dbAktienregisterErgaenzung.anzErgebnis() - 1);
        EclAktienregisterErgaenzung altAktienregisterErgaenzung = lDbBundle.dbAktienregisterErgaenzung
                .ergebnisPosition(lDbBundle.dbAktienregisterErgaenzung.anzErgebnis() - 2);

        liefereVerzeichnisDateiname(pAktienregisterIdent, neuAktienregisterErgaenzung.satzNummer);
        //		String pdfIdent=CaString.fuelleLinksNull(Integer.toString(pAktienregisterIdent), 6)+CaString.fuelleLinksNull(Integer.toString(neuAktienregisterErgaenzung.satzNummer), 3);

        if (rcDruckenInPDF) {
            rpDrucken.initServer();

            rpDrucken.exportFormat = 1;
            rpDrucken.exportVerzeichnis = rcExportVerzeichnis;
            rpDrucken.exportDatei = rcExportDatei;
            rcPdfName = rcExportVerzeichnisDatei;
        }

        rpDrucken.initFormular(lDbBundle);

        /*Variablen füllen - sowie Dokumentvorlage*/
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        if (pFormular == 0) {
            pFormular = 1;
        }
        String formularNummer = Integer.toString(pFormular);
        rpVariablen.datenpflegeMeldung(formularNummer, rpDrucken);

        String hDatum = CaDatumZeit.DatumStringFuerAnzeigeMonatAusgeschrieben();
        rpVariablen.fuelleVariable(rpDrucken, "AktuellesDatum", hDatum);

        rpVariablen.fuelleVariable(rpDrucken, "ku178MitgliedsNr", pMitgliedsnummer);

        rpVariablen.fuelleVariable(rpDrucken, "ku178Name",
                neuAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_Name));
        rpVariablen.fuelleVariable(rpDrucken, "ku178Vorname",
                neuAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_Vorname));
        rpVariablen.fuelleVariable(rpDrucken, "Neu.ku178Strasse", neuAktienregisterErgaenzung
                .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_StrasseHausnummer));
        rpVariablen.fuelleVariable(rpDrucken, "Alt.ku178Strasse", altAktienregisterErgaenzung
                .getErgaenzungString(KonstAktienregisterErgaenzung.ku178_StrasseHausnummer));

        rpVariablen.fuelleVariable(rpDrucken, "Neu.ku178PLZ",
                neuAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_PLZ));
        rpVariablen.fuelleVariable(rpDrucken, "Alt.ku178PLZ",
                altAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_PLZ));

        rpVariablen.fuelleVariable(rpDrucken, "Neu.ku178Ort",
                neuAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_Ort));
        rpVariablen.fuelleVariable(rpDrucken, "Alt.ku178Ort",
                altAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_Ort));

        rpVariablen.fuelleVariable(rpDrucken, "Neu.ku178Mail",
                neuAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_EMailAdresse));
        rpVariablen.fuelleVariable(rpDrucken, "Alt.ku178Mail",
                altAktienregisterErgaenzung.getErgaenzungString(KonstAktienregisterErgaenzung.ku178_EMailAdresse));

        rpDrucken.startFormular();
        rpDrucken.druckenFormular();
        rpDrucken.endeFormular();

        dbClose();
        return 1;
    }

    public String rcExportVerzeichnis = "";
    public String rcExportDatei = "";
    public String rcExportVerzeichnisDatei = "";

    public void liefereVerzeichnisDateiname(int pAktienregisterIdent, int pSatznummer) {
        String pdfIdent = CaString.fuelleLinksNull(Integer.toString(pAktienregisterIdent), 6)
                + CaString.fuelleLinksNull(Integer.toString(pSatznummer), 3);

        rcExportVerzeichnis = lDbBundle.lieferePfadMeetingAusdrucke();
        rcExportDatei = "aenderungsdokumentM" + lDbBundle.getMandantString() + pdfIdent;
        rcExportVerzeichnisDatei = rcExportVerzeichnis + "\\" + rcExportDatei;

    }

}
