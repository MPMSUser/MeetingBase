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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienKapitalRechnungen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldungInSammelArt;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcel;
import de.meetingapps.meetingportal.meetingCoreReport.RpExcelVariablen;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;

public class RepMeldeliste {

    /**true=> es wird eine Export-Datei erzeugt. In exportDatei steht anschließend voller Pfad-/Dateiname*/
    public boolean mitExport = true;

    /**
     * 1=meldeIdent
     * 2=Name
     * 3=Aktien absteigend
     * 4=Zutrittsident, Name
    */
    public int sortierung = 0;

    /**
     * 1=nur EinzelMeldungen
     * 2=nur Sammelkarten
     * 3=alle
     */
    public int selektion = 0;

    /**
     * -1 = alle
     * 0 = alle, die nicht in Sammelkarten sind
     * 1 = alle, die in KIAV-Sammelkarte sind
     * 2 = alle, die in SRV-Sammelkarte sind
     * 3 = alle, die in organisatorischer Sammelkarte sind
     * 4 = alle, die in Briefwahl sind
     * 5 = alle, die in Dauervollmacht sind
     * 
     * 15 = alle, die in Sammelkarte sind die nicht SRV oder Briefwahl ist 
     */
    public int selektion_inSammelkarte = -1;

    /**0 = alle
     * 1 = alle, die gerade nicht präsent sind
     * 2 = alle, die nie präsent waren
     */
    public int selektion_praesenz = 0;

    /**0=mit ausgeben
     * 1=unterdrücken
     */
    public int selektion_aktionaereInSammelkartenUnterdruecken = 0;

    /**>0 => nur die ersten anzahlDrucken-Sätze werden ausgedruckt.
     * Nur sinnvoll, wenn sortierung=3.
     */
    public int anzahlDrucken = -1;

    /**
     * 0=Gäste
     * 1=Aktionäre
     */
    public int klasse = 0;

    public long aktienAnzahl = 0;

    /**Rückgabewert. Falls mitExport, ist hier anschließend der volle Pfad-/Dateiname der erzeugten
     * Export-Datei vorhanden.
     */
    public String exportDatei = "";
    public String exportExcelDatei = "";

    private DbBundle dbBundle = null;

    public void druckeMeldeliste(DbBundle pDbBundle, RpDrucken rpDrucken, String pLfdNummer) {

        dbBundle = pDbBundle;

        CaDateiWrite dateiExport = null;
        RpExcel rpExcel = null;

        if (mitExport) {
            dateiExport = new CaDateiWrite();
            dateiExport.trennzeichen = ';';
            dateiExport.dateiart = ".csv";

            dateiExport.oeffne(pDbBundle, "meldeliste");

            dateiExport.ausgabe("MeldeIdent");
            dateiExport.ausgabe("AktionärsNummer");
            dateiExport.ausgabe("Eintrittskarte");
            dateiExport.ausgabe("Name");
            dateiExport.ausgabe("Vorname");
            dateiExport.ausgabe("Ort");
            dateiExport.ausgabe("Aktien");
            dateiExport.ausgabe("Besitzart");
            dateiExport.ausgabe("InSammelArt");
            dateiExport.ausgabe("InSammelIdent");
            dateiExport.ausgabe("Vertretername");
            dateiExport.ausgabe("VertreterVorname");
            dateiExport.ausgabe("VertreterOrt");
            dateiExport.newline();

            rpExcel = new RpExcel();
            rpExcel.fillHeader(new String[] { "Aktionärsnummer", "Eintrittskarte", "Name", "Vorname", "Ort", "Aktien",
                    "Besitzart", "InSammelArt", "InSammelIdent", "Vertretername", "VertreterVorname", "VertreterOrt" });
        }

        rpDrucken.initListe(pDbBundle);

        RpVariablen rpVariablen = new RpVariablen(pDbBundle);
        rpVariablen.meldeliste(pLfdNummer, rpDrucken);
        rpDrucken.startListe();

        int anzahlGedruckt = 0;

        int rc = pDbBundle.dbJoined.readinit_meldeliste(sortierung, selektion, klasse);
        if (rc > 0) {
            while (pDbBundle.dbJoined.readnext_meldeliste()
                    && (anzahlDrucken == -1 || anzahlDrucken > anzahlGedruckt)) {
                EclMeldung eclMeldung = pDbBundle.dbJoined.ergebnisMeldungPosition(0);
                if (selektion_inSammelkarte == -1
                        || (selektion_inSammelkarte == 0 && eclMeldung.meldungEnthaltenInSammelkarteArt == 0) || //alle nicht in Sammelkarte
                        (selektion_inSammelkarte == 1 && eclMeldung.meldungEnthaltenInSammelkarteArt == 1) || //KIAV
                        (selektion_inSammelkarte == 2 && eclMeldung.meldungEnthaltenInSammelkarteArt == 2) || //SRV
                        (selektion_inSammelkarte == 3 && eclMeldung.meldungEnthaltenInSammelkarteArt == 3) || //Orga
                        (selektion_inSammelkarte == 4 && eclMeldung.meldungEnthaltenInSammelkarteArt == 4) || //Briefwahl
                        (selektion_inSammelkarte == 5 && eclMeldung.meldungEnthaltenInSammelkarteArt == 5) || //Dauervollmacht
                        (selektion_inSammelkarte == 15 && (eclMeldung.meldungEnthaltenInSammelkarteArt != 0
                                && eclMeldung.meldungEnthaltenInSammelkarteArt != 2
                                && eclMeldung.meldungEnthaltenInSammelkarteArt != 4)) //"Sonstige Sammelkarten (nicht SRV und nicht Briefwahl)
                ) {
                    if (selektion_praesenz == 0
                            || (selektion_praesenz == 1
                                    && (eclMeldung.statusPraesenz == 0 || eclMeldung.statusPraesenz == 2))
                            || (selektion_praesenz == 2 && eclMeldung.statusPraesenz == 0)) {
                        if (selektion_aktionaereInSammelkartenUnterdruecken == 0
                                || eclMeldung.meldungEnthaltenInSammelkarte == 0) {
                            fuelleMeldelisteAktionaer(eclMeldung, rpDrucken, rpVariablen, rpExcel, dateiExport);
                            rpDrucken.druckenListe();
                            anzahlGedruckt++;
                        }
                    }
                }
            }
        }
        rpDrucken.endeListe();

        if (mitExport) {
            dateiExport.schliessen();
            exportDatei = dateiExport.dateiname;

//          Summe unter der Liste > Gesamt: ###
            RpExcelVariablen rpExcelVar = new RpExcelVariablen("Gesamt:", 4, RpExcel.TEXT_BOLD, 1);
            rpExcel.formulas.add(rpExcelVar);
            rpExcelVar = new RpExcelVariablen(RpExcel.SUMME, 5, RpExcel.NUMBER_FORMAT_BOLD,
                    String.valueOf(aktienAnzahl).length());
            rpExcel.formulas.add(rpExcelVar);

            rpExcel.create(pDbBundle, "meldeliste");
            exportExcelDatei = rpExcel.dateiName;
        }
    }

    private void fuelleMeldelisteAktionaer(EclMeldung lEclMeldung, RpDrucken rpDrucken, RpVariablen rpVariablen,
            RpExcel rpExcel, CaDateiWrite dateiExport) {

        if (mitExport) {
            dateiExport.ausgabe(Integer.toString(lEclMeldung.meldungsIdent));
            dateiExport.ausgabe(lEclMeldung.aktionaersnummer);
            dateiExport.ausgabe(lEclMeldung.zutrittsIdent);
            dateiExport.ausgabe(lEclMeldung.name);
            dateiExport.ausgabe(lEclMeldung.vorname);
            dateiExport.ausgabe(lEclMeldung.ort);
            dateiExport.ausgabe(Long.toString(lEclMeldung.stimmen));
            dateiExport.ausgabe(lEclMeldung.besitzart);
            dateiExport.ausgabe(KonstMeldungInSammelArt.getText(lEclMeldung.meldungEnthaltenInSammelkarteArt));
            dateiExport.ausgabe(Integer.toString(lEclMeldung.meldungEnthaltenInSammelkarte));
            dateiExport.ausgabe(lEclMeldung.vertreterName);
            dateiExport.ausgabe(lEclMeldung.vertreterVorname);
            dateiExport.ausgabe(lEclMeldung.vertreterOrt);
            dateiExport.newline();

            /*
             * Fuelle Variablen fuer den Datensatz
             */
            int col = 0;
            List<RpExcelVariablen> rpExcelVar = new ArrayList<>();

            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.aktionaersnummer, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.zutrittsIdent, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.name, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.vorname, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.ort, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.stimmen, col++, RpExcel.NUMBER_FORMAT));
//          Summe addieren um Spaltenbreite festzulegen
            aktienAnzahl += lEclMeldung.stimmen;
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.besitzart, col++, RpExcel.TEXT));
            rpExcelVar.add(
                    new RpExcelVariablen(KonstMeldungInSammelArt.getText(lEclMeldung.meldungEnthaltenInSammelkarteArt),
                            col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.meldungEnthaltenInSammelkarte, col++, RpExcel.NUMBER));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.vertreterName, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.vertreterVorname, col++, RpExcel.TEXT));
            rpExcelVar.add(new RpExcelVariablen(lEclMeldung.vertreterOrt, col++, RpExcel.TEXT));

            rpExcel.content.add(rpExcelVar);

        }

        rpVariablen.fuelleFeld(rpDrucken, "Nummern.ZutrittsIdent", lEclMeldung.zutrittsIdent);
        rpVariablen.fuelleFeld(rpDrucken, "Nummern.Aktionaersnummer", lEclMeldung.aktionaersnummer);
        rpVariablen.fuelleFeld(rpDrucken, "Nummern.MeldeIdent", Integer.toString(lEclMeldung.meldungsIdent));

        rpVariablen.fuelleFeld(rpDrucken, "Besitz.Stimmen", Long.toString(lEclMeldung.stimmen));
        rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(lEclMeldung.stimmen));
        rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(lEclMeldung.stimmen));

        BlAktienKapitalRechnungen blAktienKapitalRechnungen = new BlAktienKapitalRechnungen(dbBundle.param.paramBasis);

        blAktienKapitalRechnungen.berechneProzentAktienVomJeweiligenKapitalStimmen(lEclMeldung.gattung,
                lEclMeldung.stimmen);
        double prozentGattungskapital = blAktienKapitalRechnungen.rcProzentVomJeweiligenKapitalStimmen;

        blAktienKapitalRechnungen.berechneProzentAktienVomGesamtKapitalStimmen(lEclMeldung.gattung,
                lEclMeldung.stimmen);
        double prozentGesamtstimmen = blAktienKapitalRechnungen.rcProzentVomGesamtKapitalStimmen;

        rpVariablen.fuelleFeld(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(lEclMeldung.stimmen));

        rpVariablen.fuelleFeld(rpDrucken, "Besitz.ProzentGattungskapital",
                CaString.prozentToString(prozentGattungskapital));
        rpVariablen.fuelleFeld(rpDrucken, "Besitz.ProzentGesamt", CaString.prozentToString(prozentGesamtstimmen));

        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Titel", lEclMeldung.titel);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Name", lEclMeldung.name);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Vorname", lEclMeldung.vorname);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Plz", lEclMeldung.plz);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Ort", lEclMeldung.ort);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.Strasse", lEclMeldung.strasse);

        String besitzArtKuerzel = lEclMeldung.besitzart;
        String besitzArt = "", besitzArtEN = "";
        switch (besitzArtKuerzel) {
        case "E": {
            besitzArt = "Eigenbesitz";
            besitzArtEN = "Proprietary Possession";
            break;
        }
        case "F": {
            besitzArt = "Fremdbesitz";
            besitzArtEN = "Minority Interests";
            break;
        }
        case "V": {
            besitzArt = "Vollmachtsbesitz";
            besitzArtEN = "Proxy Possession";
            break;
        }
        }
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);

        rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.istInSammler",
                KonstMeldungInSammelArt.getText(lEclMeldung.meldungEnthaltenInSammelkarteArt));
        if (lEclMeldung.meldungEnthaltenInSammelkarte != 0) {
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.istInSammelIdent",
                    Integer.toString(lEclMeldung.meldungEnthaltenInSammelkarte));

        } else {
            rpVariablen.fuelleFeld(rpDrucken, "Aktionaer.istInSammelIdent", "");
        }

        rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Name", lEclMeldung.vertreterName);
        rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Vorname", lEclMeldung.vertreterVorname);
        rpVariablen.fuelleFeld(rpDrucken, "Vertreter.Ort", lEclMeldung.vertreterOrt);

    }

}
