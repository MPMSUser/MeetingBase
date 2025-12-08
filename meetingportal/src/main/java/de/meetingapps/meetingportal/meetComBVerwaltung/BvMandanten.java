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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;

public class BvMandanten {

    private int logDrucken = 3;

    private int[] mandantenNr = { 0 };

    private String[] mandantenText = { "" };

    public String rcMandantenText = "";

    /**false => Mandant mit dieser Nummer ist nicht vordefiniert.
     * Wird speziell genutzt, um alte Datenbanken mit Mandanteninformation zu versorgen*/
    public boolean findeMandantAusListe(int pNr) {
        for (int i = 0; i < mandantenNr.length; i++) {
            if (mandantenNr[i] == pNr) {
                rcMandantenText = mandantenText[i];
                return true;
            }
        }
        return false;
    }

    public List<EclEmittenten> rcEmittentenListe = null;

    /**Liefert alle die Mandanten, die in der Auswahl der Aktionärsportale aufgenommen werden sollen:
     * Selektion: portalVorhanden=1; inDbVorhanden=1; dbArt siehe pDbArt
     * Sortierung: bezeichnungKurz, Mandanten-Nr, hvJahr, hvNummer, dbArt
     * pDbBundle muß offen sein.
     * pDbArt=0 => alle
     * pDbArt=<String> => es werden alle geliefert, deren dbArt in <String> enthalten sind.
     * 
     * Returnwert: Liste steht in rcEmittentenListe.
     * */
    public boolean liefereMandantenFuerAktionaersPortalauswahl(DbBundle pDbBundle, String pDbArt) {
        rcEmittentenListe = new LinkedList<EclEmittenten>();

        pDbBundle.dbEmittenten.readAll(1);
        for (int i = 0; i < pDbBundle.dbEmittenten.anzErgebnis(); i++) {
            EclEmittenten lEmittent = pDbBundle.dbEmittenten.ergebnisPosition(i);
            if (lEmittent.portalVorhanden == 1 && lEmittent.inDbVorhanden == 1) {
                if (pDbArt.compareTo("0") == 0 || pDbArt.indexOf(lEmittent.dbArt) >= 0) {
                    rcEmittentenListe.add(lEmittent);
                }
            }
        }

        return true;
    }

    public boolean liefereMandantenFuerAdminAuswahl(DbBundle pDbBundle) {
        rcEmittentenListe = new LinkedList<EclEmittenten>();

        pDbBundle.dbEmittenten.readAll(1);
        for (int i = 0; i < pDbBundle.dbEmittenten.anzErgebnis(); i++) {
            EclEmittenten lEmittent = pDbBundle.dbEmittenten.ergebnisPosition(i);
            rcEmittentenListe.add(lEmittent);
        }

        return true;
    }

    public String linkAdmin(DbBundle pDbBundle, EclEmittenten pEmittent, String pBaseLink) {

        String kompletterLink = "";

        /* http://127.0.0.1:8080 */
        kompletterLink = pBaseLink + "/meetingportal";

        kompletterLink += "/DLOGIN/dReportsN.xhtml?mandant="
                + CaString.fuelleLinksNull(Integer.toString(pEmittent.mandant), 3);

        kompletterLink += "&hvjahr=" + CaString.fuelleLinksNull(Integer.toString(pEmittent.hvJahr), 4);
        kompletterLink += "&hvnummer=" + pEmittent.hvNummer;
        kompletterLink += "&datenbankbereich=" + pEmittent.dbArt;

        return kompletterLink;
    }

    /**pDbBundle: wird benötigt für globalVar.
     * pEmittent: aus diesem wird der Link erzeugt.
     * pBaseLink: z.B. http://127.0.0.1:8080/meetingport
     * pTest = true => test=1 wird angehängt
     */
    public String linkAktionaersportalDeutsch(DbBundle pDbBundle, EclEmittenten pEmittent, String pBaseLink,
            boolean pTest) {
        return linkAktionaersportal(pDbBundle, pEmittent, pBaseLink, pTest, "DE");
    }

    public String linkAktionaersportalEnglisch(DbBundle pDbBundle, EclEmittenten pEmittent, String pBaseLink,
            boolean pTest) {
        return linkAktionaersportal(pDbBundle, pEmittent, pBaseLink, pTest, "EN");
    }

    private String linkAktionaersportal(DbBundle pDbBundle, EclEmittenten pEmittent, String pBaseLink, boolean pTest,
            String pSprache) {

        String kompletterLink = "";

        /* http://127.0.0.1:8080/betterport */
        kompletterLink = pBaseLink;

        kompletterLink = kompletterLink + "//" + pDbBundle.param.paramPortal.designKuerzel;

        kompletterLink += "//portal.xhtml?mandant=" + CaString.fuelleLinksNull(Integer.toString(pEmittent.mandant), 3)
                + "&sprache=" + pSprache;

        if (pTest) {
            kompletterLink += "&test=1";
        }

        return kompletterLink;
    }

    /**Hinweis: diese Funktion nimmt momentan die Mandanten in der Datenbank (tbl_emittent),
     * und liefert daraus nur gefüllt Mandantenummer und Mandantenbezeichnung (Unique) zurück.
     * Das ist nicht ganz korrekt, weil daraus nur aktuell vorhandene Mandanten angezeigt werden,
     * nicht potentiell anzulegende. Eigentlich müßte eine übergreifende Mandantentabelle
     * verwendet werden.
     */
    public List<EclEmittenten> liefereEmittentenListeFuerNrAuswahl(DbBundle pDbBundle) {

        pDbBundle.dbEmittenten.readAll(1);
        return liefereEmittentenListeFuerNrAuswahl(pDbBundle.dbEmittenten.ergebnisArray);
    }

    /**Kein Datenbankzugriff - Ermittlung aus vorher eingelesenem mandantenArray*/
    public List<EclEmittenten> liefereEmittentenListeFuerNrAuswahl(EclEmittenten[] mandantenArray) {
        List<EclEmittenten> listEclEmittenten = new LinkedList<EclEmittenten>();
        if (mandantenArray == null) {
            return null;
        }
        int anzEmittenten = mandantenArray.length;
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = mandantenArray[i];
            /*Nun prüfen, ob Emittent ggf.bereits in Liste - falls nein, dann anhängen, falls ja, dann "erneuern"*/
            int gef = 0;
            for (int i1 = 0; i1 < listEclEmittenten.size(); i1++) {
                if (listEclEmittenten.get(i1).mandant == lEmittent.mandant) {
                    gef = 1;
                    listEclEmittenten.get(i1).bezeichnungKurz = lEmittent.bezeichnungKurz;
                    listEclEmittenten.get(i1).bezeichnungLang = lEmittent.bezeichnungLang;
                }
            }
            if (gef == 0) { /*Anhängen*/
                EclEmittenten hEmittent = new EclEmittenten();
                hEmittent.mandant = lEmittent.mandant;
                hEmittent.bezeichnungKurz = lEmittent.bezeichnungKurz;
                hEmittent.bezeichnungLang = lEmittent.bezeichnungLang;
                listEclEmittenten.add(hEmittent);
            }
        }

        return listEclEmittenten;
    }

    public List<Integer> liefereHVJahrListe(EclEmittenten[] mandantenArray, int mandant) {
        List<Integer> jahresListe = new LinkedList<Integer>();

        if (mandantenArray == null) {
            return null;
        }
        int anzEmittenten = mandantenArray.length;
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = mandantenArray[i];
            if (lEmittent.mandant == mandant) {
                /*Nun prüfen, ob Jahr ggf.bereits in Liste - falls nein, dann anhängen, falls ja, dann "erneuern"*/
                int gef = 0;
                for (int i1 = 0; i1 < jahresListe.size(); i1++) {
                    if (jahresListe.get(i1) == lEmittent.hvJahr) {
                        gef = 1;
                    }
                }
                if (gef == 0) { /*Anhängen*/
                    jahresListe.add(lEmittent.hvJahr);
                }
            }
        }
        return jahresListe;
    }

    public List<String> liefereHVNummerListe(EclEmittenten[] mandantenArray, int mandant, int hvJahr) {
        List<String> nummerListe = new LinkedList<String>();

        if (mandantenArray == null) {
            return null;
        }
        int anzEmittenten = mandantenArray.length;
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = mandantenArray[i];
            if (lEmittent.mandant == mandant && lEmittent.hvJahr == hvJahr) {
                /*Nun prüfen, ob HVNummer ggf.bereits in Liste - falls nein, dann anhängen, falls ja, dann "erneuern"*/
                int gef = 0;
                for (int i1 = 0; i1 < nummerListe.size(); i1++) {
                    if (nummerListe.get(i1).equals(lEmittent.hvNummer)) {
                        gef = 1;
                    }
                }
                if (gef == 0) { /*Anhängen*/
                    nummerListe.add(lEmittent.hvNummer);
                }
            }
        }
        return nummerListe;
    }

    public List<String> liefereDatenbankListe(EclEmittenten[] mandantenArray, int mandant, int hvJahr,
            String hvNummer) {
        List<String> bereichListe = new LinkedList<String>();

        if (mandantenArray == null) {
            return null;
        }
        int anzEmittenten = mandantenArray.length;
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = mandantenArray[i];
            if (lEmittent.mandant == mandant && lEmittent.hvJahr == hvJahr && lEmittent.hvNummer.equals(hvNummer)) {
                /*Nun prüfen, ob HVNummer ggf.bereits in Liste - falls nein, dann anhängen, falls ja, dann "erneuern"*/
                int gef = 0;
                for (int i1 = 0; i1 < bereichListe.size(); i1++) {
                    if (bereichListe.get(i1).equals(lEmittent.dbArt)) {
                        gef = 1;
                    }
                }
                if (gef == 0) { /*Anhängen*/
                    bereichListe.add(lEmittent.dbArt);
                }
            }
        }
        return bereichListe;
    }

    /**Liefert alle Emittenten (eigentlich: Mandanten, also mehrfach mit Jahr etc.), die in der Datenbank enthalten sind*/
    public EclEmittenten[] liefereEmittentenListeAlle(DbBundle pDbBundle) {
        pDbBundle.dbEmittenten.readAll(1);
        return pDbBundle.dbEmittenten.ergebnisArray;
    }

    public EclEmittenten[] liefereEmittentenMitAktiverApp(DbBundle pDbBundle) {
        pDbBundle.dbEmittenten.read_appAktiv();
        return pDbBundle.dbEmittenten.ergebnisArray;
    }

    public List<EclEmittenten> liefereMandanten(DbBundle pDbBundle, int pStandardOderNurPOderAlle) {
        List<EclEmittenten> lEmittentenListe = new LinkedList<EclEmittenten>();
        pDbBundle.dbEmittenten.readAll(1);
        for (int i = 0; i < pDbBundle.dbEmittenten.anzErgebnis(); i++) {
            EclEmittenten lEmittenten = pDbBundle.dbEmittenten.ergebnisPosition(i);
            if ((lEmittenten.portalStandard == 1 && pStandardOderNurPOderAlle == 1)
                    || (lEmittenten.dbArt.equals("P") && pStandardOderNurPOderAlle == 2)
                    || (pStandardOderNurPOderAlle == 3)) {
                lEmittentenListe.add(lEmittenten);
            }
        }
        return lEmittentenListe;
    }

    public List<EclEmittenten> liefereMandantenZuInsti(DbBundle pDbBundle, int pInsti) {
        List<EclEmittenten> lEmittentenListe = new LinkedList<EclEmittenten>();
        pDbBundle.dbEmittenten.readAllZuInsti(1, pInsti);
        for (int i = 0; i < pDbBundle.dbEmittenten.anzErgebnis(); i++) {
            EclEmittenten lEmittenten = pDbBundle.dbEmittenten.ergebnisPosition(i);
            lEmittentenListe.add(lEmittenten);
        }
        return lEmittentenListe;
    }

    /**Liefert null, wenn nicht gefunden*/
    public EclEmittenten pruefeHVVorhanden(DbBundle pDbBundle, int pMandant, int pHVJahr, String pHVNummer,
            String pDatenbereich) {

        pDbBundle.dbEmittenten.readNurMandant(pMandant);
        int anz = pDbBundle.dbEmittenten.anzErgebnis();
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclEmittenten lEmittent = pDbBundle.dbEmittenten.ergebnisPosition(i);
                if (lEmittent.mandant == pMandant && lEmittent.hvJahr == pHVJahr
                        && lEmittent.hvNummer.compareTo(pHVNummer) == 0
                        && lEmittent.dbArt.compareTo(pDatenbereich) == 0) {
                    return lEmittent;
                }
            }

        }

        return null;
    }

    /**Besetzt pDbBundle.globalVar mit den Mandanten-Parametern. Falls globalVar noch nicht initialisiert, wird es initialisiert.*/
    public void besetzeGlobalVarInDbBundle(DbBundle pDbBundle, int pMandant, int pHVJahr, String pHVNummer,
            String pDatenbereich) {
        if (pDbBundle.clGlobalVar == null) {
            pDbBundle.clGlobalVar = new ClGlobalVar();
        }

        pDbBundle.clGlobalVar.mandant = pMandant;
        pDbBundle.clGlobalVar.hvJahr = pHVJahr;
        pDbBundle.clGlobalVar.hvNummer = pHVNummer;
        pDbBundle.clGlobalVar.datenbereich = pDatenbereich;
    }

    /**Anlegen eines neuen Mandanten:
     * 
     * pMitEclEmittentEintragen==true: tbl_emittent wird überprüft und gefüllt
     * 			=false: einfach Schema und Table anlegen, tbl_emittent wird nicht überprüft und nicht gefüllt
     * 
     * > Datenbank-Schema
     * > Tables grundsätzlich füllen:
     * >> tbd
     * 
     * Returncode:
     * 1 = alles ok
     * pfdXyBereitsVorhanden
     */
    public int legeMandantNeuAn(DbBundle pDbBundle, EclEmittenten pEmittenten, boolean pMitEclEmittentEintragen) {
        int rc = 0;
        speichereMandantAktuell(pDbBundle);
        besetzeGlobalVarInDbBundle(pDbBundle, pEmittenten.mandant, pEmittenten.hvJahr, pEmittenten.hvNummer,
                pEmittenten.dbArt);

        
        /*Datenbank-Schema anlegen*/
        String mandantSchema = "db_mc" + CaString.fuelleLinksNull(Integer.toString(pEmittenten.mandant), 3) + "j"
                + CaString.fuelleLinksNull(Integer.toString(pEmittenten.hvJahr), 4) + pEmittenten.hvNummer
                + pEmittenten.dbArt;
        rc = pDbBundle.dbDatenbankVerwaltung.createMandantenSchema(mandantSchema);

        if (rc < 0) {
            CaBug.drucke("BvMandanten.legeMandantNeuAn 001");
            return rc;
        }

        /*Tables (leer) anlegen*/
        BvDatenbank lBvDatenbank = new BvDatenbank(pDbBundle);
        lBvDatenbank.legeMandantenTablesAn();

        /*Tables mit erforderlichen Grunddaten füllen*/
        lBvDatenbank.initialisiereMandantenTables();

        if (pMitEclEmittentEintragen) {
            /*Prüfen: ist anzulegender Mandant schon vorhanden?*/
            pDbBundle.dbEmittenten.readEmittentHV(pEmittenten.mandant, pEmittenten.hvJahr, pEmittenten.hvNummer,
                    pEmittenten.dbArt);
            if (pDbBundle.dbEmittenten.anzErgebnis() != 0) {
                restoreMandantAktull(pDbBundle);
                return CaFehler.pfdXyBereitsVorhanden;
            }

            /*Emittent in Table eintragen*/
            pDbBundle.dbEmittenten.insert(pEmittenten);
        }


        /*Ggf. Grunddaten einkopieren*/
        /*Bei Bedarf ergänzen*/

        /*Parameter neu initialisieren*/
        pDbBundle.dbParameter.ergHVParam = new HVParam();
        pDbBundle.param = new HVParam();
        pDbBundle.dbParameter.updateHVParam_all();

        /**Pfade anlegen*/
        legePfadeNeuAn(pDbBundle);

        restoreMandantAktull(pDbBundle);
        return 1;
    }

    public int legePfadeNeuAn(DbBundle pDbBundle) {
        String rootpfad = pDbBundle.clGlobalVar.lwPfadAllgemein;
        String pfadTeilMandant = pDbBundle.getMandantPfad();
        Path gesamtpfad = null;

        gesamtpfad = Paths.get(pDbBundle.lieferePfadMeetingReports());
        CaBug.druckeLog("gesamtpfad=" + gesamtpfad, logDrucken, 10);
        try {
            Files.createDirectories(gesamtpfad);
        } catch (IOException e) {
        }

        gesamtpfad = Paths.get(pDbBundle.lieferePfadMeetingAusdrucke());
        try {
            Files.createDirectories(gesamtpfad);
        } catch (IOException e) {
        }

        gesamtpfad = Paths.get(pDbBundle.lieferePfadMeetingAusdruckeIntern());
        try {
            Files.createDirectories(gesamtpfad);
        } catch (IOException e) {
        }

        gesamtpfad = Paths.get(pDbBundle.lieferePfadMeetingOutput());
        try {
            Files.createDirectories(gesamtpfad);
        } catch (IOException e) {
        }
        gesamtpfad = Paths.get(pDbBundle.lieferePfadMeetingPdf());
        try {
            Files.createDirectories(gesamtpfad);
        } catch (IOException e) {
        }
        return 1;
    }

    /**Zum Zwischenspeichern des aktuellen Mandanten in ClGlobalVar*/
    private int zMandant, zHVJahr;
    private String zHVNummer, zDatenbereich;

    private void speichereMandantAktuell(DbBundle pDbBundle) {
        zMandant = pDbBundle.clGlobalVar.mandant;
        zHVJahr = pDbBundle.clGlobalVar.hvJahr;
        zHVNummer = pDbBundle.clGlobalVar.hvNummer;
        zDatenbereich = pDbBundle.clGlobalVar.datenbereich;
    }

    private void restoreMandantAktull(DbBundle pDbBundle) {
        pDbBundle.clGlobalVar.mandant = zMandant;
        pDbBundle.clGlobalVar.hvJahr = zHVJahr;
        pDbBundle.clGlobalVar.hvNummer = zHVNummer;
        pDbBundle.clGlobalVar.datenbereich = zDatenbereich;
    }

}
