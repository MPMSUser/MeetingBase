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
package de.meetingapps.meetingportal.meetComReports;

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlDrucklauf;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbWeisungMeldung.PclAktionaersWeisungenZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldungInSammelArt;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelkarten;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelkartenRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

/**Nur teilweise Stub-fähig!*/
public class RepSammelkarten extends StubRoot {

    private int sicht = 0;

    /**pSicht gemäß KonstWeisungserfassungSicht*/
    public RepSammelkarten(boolean pIstServer, DbBundle pDbBundle, int pSicht) {
        super(pIstServer, pDbBundle);
        sicht = pSicht;
    }

    public RepSammelkarten(int pSicht) {
        super(true, null);
        sicht = pSicht;
    }

    public String druckergebnisDatei = "";

    /**Rückgabewert. Falls Export, ist hier anschließend der volle Pfad-/Dateiname der erzeugten
     * Export-Datei vorhanden.
     */
    public String exportDatei = "";

    /**Variante: 0 = J/N/ in Export, in einer Spalte
     * 1 = Summe in getrennten Spalten für J/N/E/U, für Kontrolle der Sammelkartensummen
     */
    public void exportAKtionaereMitWeisungen(DbBundle pDbBundle, int mitVerschlossenen, int variante) {

        BlAbstimmungenWeisungen blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(istServer, pDbBundle);
        blAbstimmungenWeisungen.leseAgendaWeisungen(sicht,
                pDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat, 1);

        int anzahlAktionaere = pDbBundle.dbJoined.readinit_aktionaereMitWeisung(0);

        CaDateiWrite dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(pDbBundle, "AktionaereMitWeisung");

        dateiExport.ausgabe("MeldungsIdent");
        dateiExport.ausgabe("AktionärsNummer");
        dateiExport.ausgabe("Name");
        dateiExport.ausgabe("Vorname");
        dateiExport.ausgabe("Ort");
        dateiExport.ausgabe("Aktien");
        dateiExport.ausgabe("Besitzart");
        dateiExport.ausgabe("SammelkarteIdent");
        dateiExport.ausgabe("nichtFreigegeben");
        dateiExport.ausgabe("Art");
        for (int i1 = 1; i1 <= 2; i1++) {/*Abstimmungen, Gegenanträge*/
            for (int i2 = 0; i2 < blAbstimmungenWeisungen.liefereAnzArray(i1, 0); i2++) {
                if (blAbstimmungenWeisungen.pruefeObUeberschrift(i1, i2, 0) == false) {
                    if (variante == 0) {
                        dateiExport.ausgabe(blAbstimmungenWeisungen.liefereTOPNummerIntern(i1, i2, 0));
                    } else {
                        dateiExport.ausgabe(blAbstimmungenWeisungen.liefereTOPNummerIntern(i1, i2, 0) + " Ja");
                        dateiExport.ausgabe(blAbstimmungenWeisungen.liefereTOPNummerIntern(i1, i2, 0) + " Nein");
                        dateiExport.ausgabe(blAbstimmungenWeisungen.liefereTOPNummerIntern(i1, i2, 0) + " Enthaltung");
                        dateiExport.ausgabe(blAbstimmungenWeisungen.liefereTOPNummerIntern(i1, i2, 0) + " Ungueltig");

                    }
                }
            }
        }

        dateiExport.newline();

        for (int i = 0; i < anzahlAktionaere; i++) {
            pDbBundle.dbJoined.readnext_aktionaereMitWeisung();
            EclWeisungMeldung lWeisungMeldung = pDbBundle.dbJoined.ergebnisWeisungMeldungPosition(0);

            if (lWeisungMeldung.aktiv == 1) {
                dateiExport.ausgabe(Integer.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).meldungsIdent));
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldungPosition(0).aktionaersnummer);
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldungPosition(0).name);
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldungPosition(0).vorname);
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldungPosition(0).ort);
                dateiExport.ausgabe(Long.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).stimmen));
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldungPosition(0).besitzart);

                dateiExport.ausgabe(Integer.toString(pDbBundle.dbJoined.ergebnisMeldung1Position(0).meldungsIdent));
                dateiExport.ausgabe(pDbBundle.dbJoined.ergebnisMeldung1Position(0).zusatzfeld1);
                dateiExport
                        .ausgabe(KonstMeldungInSammelArt.getText(pDbBundle.dbJoined.ergebnisMeldung1Position(0).skIst));

                if (pDbBundle.dbJoined.ergebnisMeldung1Position(0).zusatzfeld1.isEmpty() || mitVerschlossenen == 1) {
                    for (int i1 = 1; i1 <= 2; i1++) {/*Abstimmungen, Gegenanträge*/
                        for (int i2 = 0; i2 < blAbstimmungenWeisungen.liefereAnzArray(i1, 0); i2++) {
                            if (blAbstimmungenWeisungen.pruefeObUeberschrift(i1, i2, 0) == false) {
                                int position = blAbstimmungenWeisungen.liefereWeisungsPosition(i1, i2, 0);
                                String weisungText = KonstStimmart.getTextKurz(lWeisungMeldung.abgabe[position]);
                                if (variante == 0) {
                                    dateiExport.ausgabe(weisungText);
                                } else {
                                    switch (weisungText) {
                                    case "J": {
                                        dateiExport.ausgabe(
                                                Long.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).stimmen));
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        break;
                                    }
                                    case "N": {
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe(
                                                Long.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).stimmen));
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        break;
                                    }
                                    case "E": {
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe(
                                                Long.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).stimmen));
                                        dateiExport.ausgabe("0");
                                        break;
                                    }
                                    case "U": {
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe(
                                                Long.toString(pDbBundle.dbJoined.ergebnisMeldungPosition(0).stimmen));
                                        break;
                                    }
                                    default: {
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        dateiExport.ausgabe("0");
                                        break;
                                    }
                                    }
                                }
                            }
                        }
                    }

                }

                dateiExport.newline();
            }
        }

        dateiExport.schliessen();
        exportDatei = dateiExport.dateiname;
    }

    /**art:
     * 1 = Kurzübersicht aller Sammelkarten (sammelKurzUebersicht) - ohne Weisungsverhalten
     * 2 = normale Übersicht aller Sammelkarten, mit Weisungsverhalten
     * 3 = ausführlich, wie 2, mit Aktionären
     * 4 = Summen der Weisungen
     */
    public void druckeSammelkarten(DbBundle pDbBundle, RpDrucken rpDrucken, int art, int mitVerschlossenen,
            String lfdNummer) {
        druckeSammelkarten(pDbBundle, rpDrucken, art, mitVerschlossenen, lfdNummer, -1, -1, -1);
    }

    public void druckeSammelkarten(DbBundle pDbBundle, RpDrucken rpDrucken, int art, int mitVerschlossenen,
            String lfdNummer, int nurVertreterNr) {
        druckeSammelkarten(pDbBundle, rpDrucken, art, mitVerschlossenen, lfdNummer, nurVertreterNr, -1, -1);
    }

    /**art:
     * 1 = Kurzübersicht aller Sammelkarten (sammelKurzUebersicht) - ohne Weisungsverhalten
     * 2 = normale Übersicht aller Sammelkarten, mit Weisungsverhalten
     * 3 = ausführlich, wie 2, mit Aktionären
     * 4 = Summen der Weisungen
     */
    public void druckeSammelkarten(DbBundle pDbBundle, RpDrucken rpDrucken, int art, int mitVerschlossenen,
            String lfdNummer, int nurVertreterNr, int selektionGruppe, int selektionIdent) {
        druckeSammelkarten(pDbBundle, rpDrucken, art, mitVerschlossenen, lfdNummer, nurVertreterNr, selektionGruppe,
                selektionIdent, false);
    }

    /**art:
     * 1 = Kurzübersicht aller Sammelkarten (sammelKurzUebersicht) - ohne Weisungsverhalten
     * 2 = normale Übersicht aller Sammelkarten, mit Weisungsverhalten
     * 3 = ausführlich, wie 2, mit Aktionären
     * 4 = Summen der Weisungen
     */
    public void druckeSammelkarten(DbBundle pDbBundle, RpDrucken rpDrucken, int art, int mitVerschlossenen,
            String lfdNummer, int nurVertreterNr, int selektionGruppe, int selektionIdent,
            boolean pGlobalesFormularVerwenden) {

        long[] summeJa = new long[200];
        long[] summeNein = new long[200];
        long[] summeEnthaltung = new long[200];
        long[] summeUngueltig = new long[200];
        long[] summeNichtTeilnahme = new long[200];
        long[] summeStimmausschluss = new long[200];
        long[] summeNichtMarkiert = new long[200];
        for (int i = 0; i < 200; i++) {
            summeJa[i] = 0;
            summeNein[i] = 0;
            summeEnthaltung[i] = 0;
            summeUngueltig[i] = 0;
            summeNichtTeilnahme[i] = 0;
            summeStimmausschluss[i] = 0;
            summeNichtMarkiert[i] = 0;
        }

        BlAbstimmungenWeisungen blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(istServer, pDbBundle);
        blAbstimmungenWeisungen.leseAgendaWeisungen(sicht,
                pDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat, 1);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.globalesFormularVerwenden = pGlobalesFormularVerwenden;

        switch (art) {
        case 1: {
            rpDrucken.initListe(pDbBundle);
            rpVariablen.sammelKurzUebersicht(lfdNummer, rpDrucken);
            rpDrucken.startListe();
            break;
        }
        case 2: {
            rpDrucken.initListe(pDbBundle);
            rpVariablen.sammelUebersichtMitWeisungen(lfdNummer, rpDrucken);
            rpDrucken.startListe();
            break;
        }
        case 3: {
            rpDrucken.initListe(pDbBundle);
            rpVariablen.sammelUebersichtMitWeisungenAktionaeren(lfdNummer, rpDrucken);
            rpDrucken.startListe();
            break;
        }
        case 4: {
            rpDrucken.initListe(pDbBundle);
            rpVariablen.weisungsSummen(lfdNummer, rpDrucken);
            rpDrucken.startListe();
            break;
        }

        }
        pDbBundle.dbMeldungen.leseAlleAktivenSammelkarten(-1);
        if (pDbBundle.dbMeldungen.meldungenArray.length != 0) {
            for (int i = 0; i < pDbBundle.dbMeldungen.meldungenArray.length; i++) {

                /*Sammelkarte in eclMeldungSammelkarte füllen*/
                EclMeldung eclMeldungSammelkarte = pDbBundle.dbMeldungen.meldungenArray[i];

                if ((nurVertreterNr == -1 || eclMeldungSammelkarte.vertreterIdent == nurVertreterNr)
                        && (selektionGruppe == -1 || eclMeldungSammelkarte.gruppe == selektionGruppe)
                        && (selektionIdent == -1 || eclMeldungSammelkarte.meldungsIdent == selektionIdent)) {

                    switch (art) {
                    case 3:
                    case 2:
                    case 1: {
                        // add here your preferred DokumentGenerator
                        rpDrucken.druckenListe();
                        break;
                    }
                    }

                    /*Bevollmächtigte hinzulesen (auch stornierte!)*/
                    BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
                    lWillenserklaerung.setzeDbBundle(pDbBundle);
                    lWillenserklaerung.piMeldungsIdentAktionaer = eclMeldungSammelkarte.meldungsIdent;
                    lWillenserklaerung.einlesenVollmachtenAnDritte();

                    if (lWillenserklaerung.rcVollmachtenAnDritte != null) {
                        for (int i1 = 0; i1 < lWillenserklaerung.rcVollmachtenAnDritte.length; i1++) {
                            if (lWillenserklaerung.rcVollmachtenAnDritte[i1].wurdeStorniert != true) {
                                switch (art) {
                                case 3:
                                case 2:
                                case 1: {
                                    // add here your preferred DokumentGenerator
                                    rpDrucken.druckenListe();
                                    break;
                                }

                                }

                            }
                        }
                    }

                    /*SammelkartenWeisungen Summen lesen*/
                    /*Weisungssatz einlesen*/
                    pDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(eclMeldungSammelkarte.meldungsIdent, false);
                    EclWeisungMeldung lWeisungMeldung = pDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

                    for (int i1 = 1; i1 <= 2; i1++) {/*Abstimmungen, Gegenanträge*/
                        for (int i2 = 0; i2 < blAbstimmungenWeisungen.liefereAnzArray(i1, 0); i2++) {
                            if (blAbstimmungenWeisungen.pruefeObUeberschrift(i1, i2, 0) == false) {
                                int position = blAbstimmungenWeisungen.liefereWeisungsPosition(i1, i2, 0);

                                long sammelJa = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.ja];
                                long sammelNein = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.nein];
                                long sammelEnthaltung = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.enthaltung];
                                long sammelUngueltig = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.ungueltig];
                                long sammelNichtTeilnahme = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.nichtTeilnahme];
                                long sammelStimmausschluss = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.stimmausschluss];
                                long sammelNichtMarkiert = lWeisungMeldung.weisungMeldungSplit.abgabe[position][KonstStimmart.nichtMarkiert];

                                if (eclMeldungSammelkarte.zusatzfeld1.isEmpty()
                                        || eclMeldungSammelkarte.zusatzfeld1.compareTo("1") == 0
                                        || eclMeldungSammelkarte.zusatzfeld1.compareTo("2") == 0
                                        || mitVerschlossenen == 1) {
                                    summeJa[position] += sammelJa;
                                    summeNein[position] += sammelNein;
                                    summeEnthaltung[position] += sammelEnthaltung;
                                    summeUngueltig[position] += sammelUngueltig;
                                    summeNichtTeilnahme[position] += sammelNichtTeilnahme;
                                    summeStimmausschluss[position] += sammelStimmausschluss;
                                    summeNichtMarkiert[position] += sammelNichtMarkiert;
                                }

                                /*Ausgeben Weisungen*/
                                if (art == 2 || art == 3) {
                                    if (eclMeldungSammelkarte.zusatzfeld1.isEmpty() || eclMeldungSammelkarte.zusatzfeld1.compareTo("2") == 0 || mitVerschlossenen == 1) {
                                        // add here your preferred DokumentGenerator
                                        rpDrucken.druckenListe();
                                    }
                                }
                            } else {
                                if (art == 2 || art == 3) {
                                    if (eclMeldungSammelkarte.zusatzfeld1.isEmpty() || eclMeldungSammelkarte.zusatzfeld1.compareTo("2") == 0 || mitVerschlossenen == 1) {
                                        // add here your preferred DokumentGenerator
                                        /*Ausgeben Überschrift*/
                                        rpDrucken.druckenListe();
                                    }
                                }
                            }

                        }
                    }

                    /*Einzelaktionäre anzeigen*/
                    if (art == 3) {
                        // add here your preferred DokumentGenerator
                        pDbBundle.dbWeisungMeldung
                                .leseAktionaersWeisungZuSammelkarte(eclMeldungSammelkarte.meldungsIdent, 1);
                        for (int i1 = 0; i1 < pDbBundle.dbWeisungMeldung
                                .aktionaersWeisungenZuSammelkarteAnzGefunden(); i1++) {
                            PclAktionaersWeisungenZuSammelkarte lAktionaerZuSammelkarte = pDbBundle.dbWeisungMeldung.aktionaersWeisungenZuSammelkarteGefunden(i1);
                            // add here your preferred DokumentGenerator
                            rpDrucken.druckenListe();
                        }
                    }
                    if (art == 2 || art == 3) {
                        rpDrucken.newPageListe();
                    }
                }

            }

        }

        if (art == 4) {
            for (int i1 = 1; i1 <= 2; i1++) {/*Abstimmungen, Gegenanträge*/
                for (int i2 = 0; i2 < blAbstimmungenWeisungen.liefereAnzArray(i1, 0); i2++) {
                    if (blAbstimmungenWeisungen.pruefeObUeberschrift(i1, i2, 0) == false) {
                        int position = blAbstimmungenWeisungen.liefereWeisungsPosition(i1, i2, 0);
                        // add here your preferred DokumentGenerator
                        rpDrucken.druckenListe();
                    } else {
                        // add here your preferred DokumentGenerator
                        rpDrucken.druckenListe();
                    }
                }
            }
        }

        switch (art) {
        case 1: {
            rpDrucken.endeListe();
            druckergebnisDatei = rpDrucken.drucklaufDatei;
            break;
        }
        case 2: {
            rpDrucken.endeListe();
            druckergebnisDatei = rpDrucken.drucklaufDatei;
            break;
        }
        case 3: {
            rpDrucken.endeListe();
            druckergebnisDatei = rpDrucken.drucklaufDatei;
            break;
        }
        case 4: {
            rpDrucken.endeListe();
            druckergebnisDatei = rpDrucken.drucklaufDatei;
            break;
        }

        }

    }

    /**Rückgabe von weisungKontrolleDrucklauf*/
    public List<String> rcListeDerExportDateien = null;

    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;
    private RpVariablen rpVariablen = null;
    private boolean lAusgabePrint = false;
    private boolean lAusgabeExport = false;
    private RpDrucken lRpDrucken = null;
    private int lDrucklaufNr;

    /**Weisungs-Kontroll-Liste - mit Drucklaufverwaltung
     * drucklaufNr==0 => neuen Drucklauf erzeugen
     * 
     *  Return-Wert=Anzahl der Sätze insgesamt
     *  
     *  Sonstige Return-Variablen:
     *  rcListeDerExportDateien=Liste der erzeugten Export-Dateien*/
    public int weisungKonstrolleDrucklauf(boolean pAusgabePrint, boolean pAusgabeExport, RpDrucken rpDrucken,
            String lfdNummer, int drucklaufNr, int arbeitsplatznr, int benutzernr) {
        lAusgabePrint = pAusgabePrint;
        lAusgabeExport = pAusgabeExport;
        lRpDrucken = rpDrucken;

        rcListeDerExportDateien = new LinkedList<String>();

        int anzahlSaetze = 0;
        if (drucklaufNr == 0) {/*Neuen Drucklauf erzeugen*/
            anzahlSaetze = weisungsKontrolle_sub_erzeugeNeuenDrucklauf(arbeitsplatznr, benutzernr);
            drucklaufNr = neuerDrucklaufNr;
            System.out.println("A drucklaufNr=" + drucklaufNr);
        }

        lDrucklaufNr = drucklaufNr;
        anzahlSaetze = weisungsKontrolle_sub_leseDaten(drucklaufNr);
        if (anzahlSaetze == 0) {
            return 0;
        }

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(istServer, lDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung(); //Intern-Erfassung ist gewollt, da ja für Kontrolle derselben!

        /*Druck initialisieren*/
        if (lAusgabePrint) {
            rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.weisungsKontrolle(lfdNummer, rpDrucken);
        }

        for (int gattung = 1; gattung <= lDbBundle.param.paramBasis.anzahlGattungen; gattung++) {
            bereitsSeiteGedruckt = false;
            weisungsKontrolle_sub_druckListe(gattung, KonstSkIst.srv);
            weisungsKontrolle_sub_druckListe(gattung, KonstSkIst.briefwahl);
            weisungsKontrolle_sub_druckListe(gattung, KonstSkIst.kiav);/*und andere*/
        }

        /*Druck beenden*/
        if (lAusgabePrint) {
            rpDrucken.endeListe();
        }

        return anzahlSaetze;
    }

    public int neuerDrucklaufNr = 0;

    /*1*/
    /**Stub-Fähig*/
    public int weisungsKontrolle_sub_erzeugeNeuenDrucklauf(int arbeitsplatznr, int benutzernr) {

        EclDrucklauf lDrucklauf = null;

        if (verwendeWebService()) {
            WEStubRepSammelkarten weStubRepSammelkarten = new WEStubRepSammelkarten();
            weStubRepSammelkarten.stubFunktion = 1;
            weStubRepSammelkarten.arbeitsplatznr = arbeitsplatznr;
            weStubRepSammelkarten.benutzernr = benutzernr;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubRepSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubRepSammelkartenRC weStubRepSammelkartenRC = wsClient.stubRepSammelkarten(weStubRepSammelkarten);

            if (weStubRepSammelkartenRC.rc < 1) {
                return weStubRepSammelkartenRC.rc;
            }

            neuerDrucklaufNr = weStubRepSammelkartenRC.neuerDrucklaufNr;
            return weStubRepSammelkartenRC.anzahlSaetze;
        }

        BlDrucklauf blDrucklauf = new BlDrucklauf(istServer, lDbBundle);
        blDrucklauf.erzeugeNeuenDrucklauf(arbeitsplatznr, benutzernr, KonstVerarbeitungslaufArt.kontrollisteWeisungen,
                0);
        lDrucklauf = blDrucklauf.rcDrucklauf;
        neuerDrucklaufNr = lDrucklauf.drucklaufNr;

        dbOpenUndWeitere();
        int anzahl = lDbBundle.dbJoined.setzeLauf_kontrollisteWeisung(neuerDrucklaufNr);
        lDrucklauf.anzahlSaetze = anzahl;
        dbClose();

        blDrucklauf.updateAnzDrucklauf(anzahl);
        return anzahl;
    }

    /**Direkt-Zugriff nur aus WAINTERN wg. Stub erlaubt*/
    public EclMeldung[] ergebnisArrayMeldung = null;
    /**Direkt-Zugriff nur aus WAINTERN wg. Stub erlaubt*/
    public EclWillenserklaerung[] ergebnisArrayWillenserklaerung = null;
    /**Direkt-Zugriff nur aus WAINTERN wg. Stub erlaubt*/
    public EclWeisungMeldung[] ergebnisArrayWeisungMeldung = null;

    /**Stub-fähig*/
    public int weisungsKontrolle_sub_leseDaten(int pDrucklaufnr) {
        if (verwendeWebService()) {
            WEStubRepSammelkarten weStubRepSammelkarten = new WEStubRepSammelkarten();
            weStubRepSammelkarten.stubFunktion = 2;
            weStubRepSammelkarten.pDrucklaufnr = pDrucklaufnr;
            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubRepSammelkarten.setWeLoginVerify(weLoginVerify);

            WEStubRepSammelkartenRC weStubRepSammelkartenRC = wsClient.stubRepSammelkarten(weStubRepSammelkarten);

            if (weStubRepSammelkartenRC.rc < 1) {
                return weStubRepSammelkartenRC.rc;
            }
            ergebnisArrayMeldung = weStubRepSammelkartenRC.ergebnisArrayMeldung;
            ergebnisArrayWillenserklaerung = weStubRepSammelkartenRC.ergebnisArrayWillenserklaerung;
            ergebnisArrayWeisungMeldung = weStubRepSammelkartenRC.ergebnisArrayWeisungMeldung;

            return weStubRepSammelkartenRC.anzahlSaetze;
        }

        dbOpen();
        int anzahl = lDbBundle.dbJoined.read_kontrollisteWeisung(pDrucklaufnr);
        ergebnisArrayMeldung = lDbBundle.dbJoined.ergebnisMeldung();
        ergebnisArrayWillenserklaerung = lDbBundle.dbJoined.ergebnisWillenserklaerung();
        ergebnisArrayWeisungMeldung = lDbBundle.dbJoined.ergebnisWeisungMeldung();
        dbClose();
        return anzahl;
    }

    private boolean weisungsKontrolle_sub_pruefeObDrucken(int i, int pGattung, int pSkArt) {

        if (ergebnisArrayMeldung[i].liefereGattung() == pGattung && (pSkArt == ergebnisArrayWeisungMeldung[i].skIst
                || (pSkArt == KonstSkIst.kiav && (ergebnisArrayWeisungMeldung[i].skIst == KonstSkIst.dauervollmacht
                        || ergebnisArrayWeisungMeldung[i].skIst == KonstSkIst.organisatorisch)))) {
            return true;
        }
        return false;

    }

    private boolean bereitsSeiteGedruckt = false;

    private void weisungsKontrolle_sub_druckListe(int pGattung, int pSkArt) {
        int anzahlGesamt = ergebnisArrayMeldung.length;
        int anzahlTatsaechlich = 0;

        /*Ermitteln, ob überhaupt für diese Selektion was existiert*/
        for (int i = 0; i < anzahlGesamt; i++) {
            if (weisungsKontrolle_sub_pruefeObDrucken(i, pGattung, pSkArt)) {
                anzahlTatsaechlich++;
            }
        }

        /*Druck und ggf. Export dann, wenn mindestens 1 Exemplar*/
        if (anzahlTatsaechlich == 0) {
            return;
        }

        /*Export-Datei starten*/
        CaDateiWrite dateiExport = null;
        if (lAusgabeExport) {
            dateiExport = new CaDateiWrite();
            dateiExport.trennzeichen = ';';
            dateiExport.dateiart = ".csv";

            String exportDateiname = "weisungsKontrolle" + Integer.toString(lDrucklaufNr) + "_"
                    + lDbBundle.param.paramBasis.getGattungBezeichnungKurz(pGattung) + "_";
            switch (pSkArt) {
            case KonstSkIst.briefwahl:
                exportDateiname += "brief";
                break;
            case KonstSkIst.srv:
                exportDateiname += "srv";
                break;
            case KonstSkIst.kiav:
                exportDateiname += "sonstige";
                break;
            }
            dateiExport.oeffne(lDbBundle, exportDateiname);
            rcListeDerExportDateien.add(dateiExport.dateiname);

            /*Überschrift ausgeben in Export-Datei*/
            weisungsKontrolle_sub_exportUeberschrift(dateiExport, lDbBundle, pGattung);

            dateiExport.ausgabe("VertreterOrt");
            dateiExport.newline();
        }

        if (lAusgabePrint) {
            String hSelektion = "Gattung: " + lDbBundle.param.paramBasis.getGattungBezeichnungKurz(pGattung) + " ";
            switch (pSkArt) {
            case KonstSkIst.srv:
                hSelektion += "SRV";
                break;
            case KonstSkIst.briefwahl:
                hSelektion += "Briefwahl";
                break;
            case KonstSkIst.kiav:
                hSelektion += "KIAV/Dauer/Orga";
                break;
            }
            rpVariablen.fuelleVariable(lRpDrucken, "Selektion", hSelektion);
            if (bereitsSeiteGedruckt) {
                lRpDrucken.newPageListe();
            } else {
                lRpDrucken.startListe();
                bereitsSeiteGedruckt = true;
            }
        }

        for (int i = 0; i < anzahlGesamt; i++) {
            if (weisungsKontrolle_sub_pruefeObDrucken(i, pGattung, pSkArt)) {
                /*Satz exportieren / drucken*/
                weisungKontrolle_sub_druckeSatz(lAusgabePrint, lAusgabeExport, lRpDrucken, dateiExport, lDbBundle,
                        ergebnisArrayMeldung[i], ergebnisArrayWeisungMeldung[i], ergebnisArrayWillenserklaerung[i]);
            }
        }

        /*Export-Datei beenden*/
        if (lAusgabeExport) {
            dateiExport.schliessen();
        }

    }

    /**Weisungs-Kontroll-Liste - ab Datum
     * selektion=1 alle
     * 	=2 nur gescannte
     *  =3 nur manuelle
     *  
     *  Nicht Stub-Fähig!*/
    public void weisungKontrolle_abDatum(DbBundle pDbBundle, RpDrucken rpDrucken, String lfdNummer,
            String abDatum) {

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(istServer, pDbBundle);
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung(); //Intern-Erfassung ist gewollt, da ja für Kontrolle derselben!

        //		BlAbstimmungen blAbstimmungen=new BlAbstimmungen(pDbBundle);
        //		blAbstimmungen.leseAbstimmungen();

        rpDrucken.initListe(pDbBundle);

        rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.weisungsKontrolle(lfdNummer, rpDrucken);
        rpDrucken.startListe();

        rpVariablen.fuelleVariable(rpDrucken, "Selektion", "Alle");

        int rc = pDbBundle.dbJoined.readinit_kontrollisteWeisung(abDatum);

        if (rc != 0) {

            while (pDbBundle.dbJoined.readnext_kontrollisteWeisung() == true) {
                EclMeldung lMeldung = pDbBundle.dbJoined.ergebnisMeldungPosition(0);
                EclWeisungMeldung lWeisungmeldung = pDbBundle.dbJoined.ergebnisWeisungMeldungPosition(0);
                EclWillenserklaerung lWillenserklaerung = pDbBundle.dbJoined.ergebnisWillenserklaerungPosition(0);

                weisungKontrolle_sub_druckeSatz(true, false, rpDrucken, null, pDbBundle, lMeldung, lWeisungmeldung,
                        lWillenserklaerung);
            }

        }

        rpDrucken.endeListe();
        druckergebnisDatei = rpDrucken.drucklaufDatei;

    }

    private void weisungsKontrolle_sub_exportUeberschrift(CaDateiWrite dateiExport, DbBundle pDbBundle, int pGattung) {

        boolean agendaGegenantraegeGetrennt = pDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;

        dateiExport.ausgabe("AktNr");
        dateiExport.ausgabe("AktName");
        dateiExport.ausgabe("AktVorname");
        dateiExport.ausgabe("AktOrt");
        dateiExport.ausgabe("Stimmen");

        dateiExport.ausgabe("aktiv/inaktiv");

        dateiExport.ausgabe("Willenserkl.");
        dateiExport.ausgabe("Benutzer");

        int aktuelleGattung = pGattung;

        int anzAgenda = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        int anzGegenantraege = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);

        int agendaMaxArt = 2;
        if (anzGegenantraege == 0 || agendaGegenantraegeGetrennt == false) {
            agendaMaxArt = 1;
        }
        for (int agendaArt = 1; agendaArt <= agendaMaxArt; agendaArt++) {
            int hAnzAgenda=anzAgenda;
            if (agendaArt==2) {
                hAnzAgenda=anzGegenantraege;
            }
            for (int i = 0; i < hAnzAgenda; i++) {
                EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.liefereRcAgendaArtArray(agendaArt,
                        aktuelleGattung, i);
                dateiExport.ausgabe(lAbstimmung.nummerKey + lAbstimmung.nummerindexKey);
            }
        }
        dateiExport.newline();
    }

    private void weisungKontrolle_sub_druckeSatz(boolean lAusgabePrint, boolean lAusgabeExport, RpDrucken rpDrucken,
            CaDateiWrite dateiExport, DbBundle pDbBundle, EclMeldung lMeldung, EclWeisungMeldung lWeisungmeldung,
            EclWillenserklaerung lWillenserklaerung) {

        boolean agendaGegenantraegeGetrennt = pDbBundle.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat;

        if (lAusgabePrint) {
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Aktionaersnummer", lMeldung.aktionaersnummer);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lMeldung.name);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lMeldung.vorname);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lMeldung.ort);
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Stimmen", Long.toString(lMeldung.stimmen));
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.StimmenDE", CaString.toStringDE(lMeldung.stimmen));

            rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.Art",
                    KonstWillenserklaerung.getText(lWillenserklaerung.willenserklaerung));
            rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.Benutzer",
                    Integer.toString(lWillenserklaerung.benutzernr));

            if (lWeisungmeldung.aktiv != 1) {
                rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.aktiv", "inaktiv");
            } else {
                rpVariablen.fuelleFeld(rpDrucken, "Willenserklaerung.aktiv", "");
            }

        }
        if (lAusgabeExport) {
            dateiExport.ausgabe(lMeldung.aktionaersnummer);
            dateiExport.ausgabe(lMeldung.name);
            dateiExport.ausgabe(lMeldung.vorname);
            dateiExport.ausgabe(lMeldung.ort);
            dateiExport.ausgabe(Long.toString(lMeldung.stimmen));

            if (lWeisungmeldung.aktiv != 1) {
                dateiExport.ausgabe("inaktiv");
            } else {
                dateiExport.ausgabe("");
            }

            dateiExport.ausgabe(KonstWillenserklaerung.getText(lWillenserklaerung.willenserklaerung));
            dateiExport.ausgabe(Integer.toString(lWillenserklaerung.benutzernr));
        }

        int aktuelleGattung = lMeldung.liefereGattung();

        int anzAgenda = blAbstimmungenWeisungenErfassen.liefereAnzAgendaArray(aktuelleGattung);
        int anzGegenantraege = blAbstimmungenWeisungenErfassen.liefereAnzGegenantraegeArray(aktuelleGattung);

        blAbstimmungenWeisungenErfassen.initWeisungMeldung(1);
        blAbstimmungenWeisungenErfassen.rcWeisungMeldung[0] = lWeisungmeldung;

        if (lAusgabePrint) {
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Weisungen", "");
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.WeisungenGegen", "");

            for (int i = 1; i <= 200; i++) {
                rpVariablen.fuelleFeld(rpDrucken, "W.T" + Integer.toString(i), ""); //TOP
                rpVariablen.fuelleFeld(rpDrucken, "W.TI" + Integer.toString(i), ""); //TOP-Index
                rpVariablen.fuelleFeld(rpDrucken, "W.W" + Integer.toString(i), ""); //Weisung
            }
        }

        int offset = 1;

        int agendaMaxArt = 2;
        if (anzGegenantraege == 0 || agendaGegenantraegeGetrennt == false) {
            agendaMaxArt = 1;
        }
        for (int agendaArt = 1; agendaArt <= agendaMaxArt; agendaArt++) {
            String weisungText = "";
            int hAnzAgenda=anzAgenda;
            if (agendaArt==2) {
                hAnzAgenda=anzGegenantraege;
            }
            for (int i = 0; i < hAnzAgenda; i++) {
                EclAbstimmung lAbstimmung = blAbstimmungenWeisungenErfassen.liefereRcAgendaArtArray(agendaArt,
                        aktuelleGattung, i);

                if (lAusgabePrint) {
                    rpVariablen.fuelleFeld(rpDrucken, "W.T" + Integer.toString(offset), lAbstimmung.nummerKey); //TOP
                    rpVariablen.fuelleFeld(rpDrucken, "W.TI" + Integer.toString(offset), lAbstimmung.nummerindexKey); //TOP-Index
                }
                weisungText = weisungText + " --" + lAbstimmung.nummerKey + lAbstimmung.nummerindexKey + ": ";

                String abstimmungsKuerzel = "";
                if (!lAbstimmung.liefereIstUeberschift()) {
                    abstimmungsKuerzel = KonstStimmart.getTextKurz(blAbstimmungenWeisungenErfassen
                            .holeAgendaArtWeisungMeldungPos(agendaArt, 0, i, aktuelleGattung));
                } else {
                    abstimmungsKuerzel = "-";
                }

                if (lAusgabePrint) {
                    rpVariablen.fuelleFeld(rpDrucken, "W.WI" + Integer.toString(offset), abstimmungsKuerzel); //Weisungsverhalten
                }
                if (lAusgabeExport) {
                    dateiExport.ausgabe(abstimmungsKuerzel);
                }
                weisungText = weisungText + abstimmungsKuerzel;

                offset++;

            }
            if (lAusgabePrint) {
                if (agendaArt == 1) {
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Weisungen", weisungText);
                } else {
                    rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.WeisungenGegen", weisungText);
                }
            }
        }
        if (lAusgabePrint) {
            rpDrucken.druckenListe();
        }
        if (lAusgabeExport) {
            dateiExport.newline();
        }

    }

}
