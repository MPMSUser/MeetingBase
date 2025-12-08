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
package de.meetingapps.meetingportal.meetingport;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTexte;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbReportKonsistenzpruefung;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbReportMeldungen;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbReportVeranstaltungsManagement;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbReportWillenserklaerungen;
import de.meetingapps.meetingportal.meetComBlManaged.BlMReports;
import de.meetingapps.meetingportal.meetComBlStatistik.BlStatistikArbeitsplatz;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclFuture3String;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenReportElement;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComReports.RepAnmeldestelleSummeErklaerungenWeg;
import de.meetingapps.meetingportal.meetComReports.RepFreiwilligeAnmeldungen;
import de.meetingapps.meetingportal.meetComReports.RepMailRegistrierung;
import de.meetingapps.meetingportal.meetComReports.RepMeldeStatistik;
import de.meetingapps.meetingportal.meetComReports.RepMitgliederportal;
import de.meetingapps.meetingportal.meetComReports.RepNegativ;
import de.meetingapps.meetingportal.meetComReports.RepSammelkarten;
import de.meetingapps.meetingportal.meetComReports.RepTestBarcodes;
import de.meetingapps.meetingportal.meetComReports.RepVeranstaltungen;
import de.meetingapps.meetingportal.meetComReports.RepWillenserklaerungenAktionaer;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UReports {

    private int logDrucken = 10;

    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject USession uSession;
    private @Inject UMenueSession uMenueSession;
    private @Inject UReportsSession uReportsSession;
    private @Inject BlMReports blMReports;
    private @Inject EclParamM eclParamM;
    private @Inject UImportExport uImportExport;

    private @Inject BlMbReportWillenserklaerungen blMbReportWillenserklaerungen;
    private @Inject BlMbReportMeldungen blMbReportMeldungen;
    private @Inject BlMbReportKonsistenzpruefung blMbReportKonsistenzpruefung;
    private @Inject BlMbReportVeranstaltungsManagement blMbReportVeranstaltungsManagement;

    //	private @EJB AsRepWillenserklaerungen asRepWillenserklaerungen;

    /**Start-Button**********/
    public String doStart() {
        if (!xSessionVerwaltung.pruefeUStart("uReportsStart", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        switch (uReportsSession.getReportNummer()) {
        case 106://meldeliste
            if (!pruefeMeldelisteMitAuswahl()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            meldeliste();
            break;
        case 107://negativliste Präsenz
        {
            if (!pruefeNegativlistePraesenzMitAuswahl()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            negativlistePraesenz();
            break;
        }
        case 111://Willenserklärungen (Alle)
        {
            if (!pruefeWillenserklaerungenAlle()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            willenserklaerungenAlle();
            break;
        }

        case 121://Export - eine Spalte J/N/E je TOP
            if (!pruefeWillenserklaerungenAlle()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            sammelkartenExportJNEAuswahl();
            break;

        case 123://Export - Stimmen je TOP
            if (!pruefeWillenserklaerungenAlle()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            sammelkartenExportStimmenAuswahl();
            break;
        
        case 124://Gästeliste
            gaesteliste();
            break;

        case 129://Kontrolliste Weisungen
            kontrolleWeisungen();
            break;

        case 130://elektronischer Einladungsversand - registrierte Aktionäre
            elekEinlRegistrierteAktionaere();
            break;

        case 131://elektronischer Einladungsversand - registrierte Aktionäre nicht zur HV angemeldet
            elekEinlRegistrierteAktionaereNichtHVAngemeldet();
            break;

        case 132://Sonderreports - Arbeitsplatzstatistik
            arbeitsplatzStatistik();
            break;

        case 133://Sonderreports - Eintrittskartenbarcodes
            if (!pruefeBarcodesEintrittskarten()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            barcodesEintrittskarten();
            break;

        case 134://Sonderreports - Einladungsbarcodes
            if (!pruefeBarcodesEinladungen()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            barcodesEinladungen();
            break;


        case 136://Basis-Set-Abgleich
            if (!pruefeBasisSetAbgleich()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }

            basisSetAbgleich();
            break;

        case 137://Freiwillige Anmeldungen
            if (!pruefeFreiwilligeAnmeldungen()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            freiwilligeAnmeldungen();
            break;

        case 138://Veranstaltungen
            if (!pruefeVeranstaltungTeilnehmer()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            veranstaltungTeilnehmer();
            break;

 
         case 148://importGaeste
            if (!uImportExport.pruefeImportGaeste()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }

            uImportExport.importGaeste();
            break;

        case 149://erstregistrierungenPortal
            if (!pruefeErstregistrierungenPortal()) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }

            erstregistrierungenPortal();
            break;

        case 150://Veranstaltungsmanagement

            veranstaltungsManagement();
            break;


        }
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
    }

    public void doStartDirektanzeige() {
        if (!xSessionVerwaltung.pruefeUStart("uReportsStart", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }
        RpDrucken rpDrucken = null;
        switch (uReportsSession.getReportNummer()) {
        case 101://weisungsSummenMitAuswahl
        {
            if (!pruefeWeisungsSummenMitAuswahl()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            int mitGeheimen = 0;
            if (uReportsSession.isGeheimeWeisungenAusgeben()) {
                mitGeheimen = 1;
            }

            int selektionGruppe = -1;
            if (!uReportsSession.getDruckNurGruppe().isEmpty()) {
                selektionGruppe = Integer.parseInt(uReportsSession.getDruckNurGruppe());
            }

            int selektionIdent = -1;
            if (!uReportsSession.getDruckNurSammelkartenIdent().isEmpty()) {
                selektionGruppe = Integer.parseInt(uReportsSession.getDruckNurSammelkartenIdent());
            }
            rpDrucken = blMReports.weisungssummen(4, mitGeheimen, uReportsSession.getReportVariante(), -1, selektionGruppe, selektionIdent);
            break;

        }
        case 108://negativliste Präsenz
        {
            if (!pruefeNegativlisteAnmeldungenAuswahl()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            rpDrucken = negativlisteAnmeldungenAuswahl();
            break;
        }
        case 112://Willenserklärungen (Alle)
        {
            if (!pruefeWillenserklaerungenFuerAktionaer()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            rpDrucken = willenserklaerungenFuerAktionaer();
            break;
        }

        case 115://Sammelkarten Kurz ohne Weisung
        {
            if (!pruefeSammelkarten()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            rpDrucken = sammelkartenKurzOhneWeisungAuswahl();
            break;
        }
        case 117://Sammelkarten Kurz mit Weisung
        {
            if (!pruefeSammelkarten()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            rpDrucken = sammelkartenKurzMitWeisungAuswahl();
            break;
        }
        case 119://Sammelkarten mit Weisung und Aktionären
        {
            if (!pruefeSammelkarten()) {
                xSessionVerwaltung.setzeUEnde();
                return;
            }
            rpDrucken = sammelkartenMitWeisungUndAktionaerenAuswahl();
            break;
        }
        default: {
            rpDrucken = new RpDrucken();
            break;
        }
        }

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);
        xSessionVerwaltung.setzeUEnde();
        return;
    }

    /**Refresh für Background-Reports*/
    public void doRefresh() {
        if (!xSessionVerwaltung.pruefeUStart("uReportsAnzeige", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }

        pruefenObFertig();

        xSessionVerwaltung.setzeUEnde();
        return;
    }

    public void pruefenObFertig() {
        String dateiname="";
        EclFuture3String dateiname3=null;
        String dateinameFuerExport="";
        
        int reportNummer=uReportsSession.getReportNummer();
        switch (reportNummer) {
        case 139://Meldeliste Alle TOP 10 (wg. Refresh)
        case 140://Meldeliste Alle Alpha (wg. Refresh)
        case 141://Meldeliste Nicht in Sammelkarte TOP 10 (wg. Refresh)
        case 142://Meldeliste Nicht in Sammelkarte Alpha (wg. Refresh)
        case 143://Meldeliste SRV Alpha
        case 144://Meldeliste Briefwahl Alpha
        case 106://meldeliste
            Future<EclFuture3String> future3Dateiname=uReportsSession.getFuture3Dateiname();
            try {
                dateiname3=future3Dateiname.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
            }
            break;

        case 145://KOnsistenzprüfung
        case 111:/*Willenserklärungen gesamt*/
        case 148://ImportGästekarten
        case 150://Veranstaltungsmanagement Auswertungen
            Future<String> futureDateiname=uReportsSession.getFutureDateiname();
            try {
                dateiname=futureDateiname.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
            }

            break;
        case 153://Veranstaltungsmanagement Detail
            Future<String> futureDateinameExport=uReportsSession.getFutureDateiname();
            try {
                dateinameFuerExport=futureDateinameExport.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
            }

            break;
        }
        if (!dateiname.isEmpty()) {
            CaBug.druckeLog("Dateiname="+dateiname, logDrucken, 10);
            uReportsSession.setStehtZurVerfuegung(true);
            if (reportNummer!=146 && reportNummer!=147 && reportNummer!=148 && reportNummer!=151) {
                uReportsSession.setDruckDateiname(dateiname);
                uReportsSession.setReportDruck(true);
            }
            else {uReportsSession.setAufgabeFertig(true);}
        }
        if (dateiname3!=null) {
            uReportsSession.setStehtZurVerfuegung(true);
            uReportsSession.setDruckDateiname(dateiname3.ergebnis1);
            uReportsSession.setReportDruck(true);
            uReportsSession.setExportDateiname(dateiname3.ergebnis2);
            uReportsSession.setExportExcelDateiname(dateiname3.ergebnis3);
            uReportsSession.setReportExport(true);
            uReportsSession.setReportExportExcel(true);
        }
        if (!dateinameFuerExport.isEmpty()) {
            uReportsSession.setStehtZurVerfuegung(true);
            uReportsSession.setExportDateiname(dateinameFuerExport);
            uReportsSession.setReportExport(true);
        }

    }
    
    
    
    /*****************Ausgabe-Button****************/
    public void doExportDownload() {
        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(uReportsSession.getExportDateiname());
        } catch (IOException e) {
            CaBug.drucke("UReports.doExportDownload 001");
        }

    }

    public void doExportExcelDownload() {
        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(uReportsSession.getExportExcelDateiname());
        } catch (IOException e) {
            CaBug.drucke("UReports.doExportDownload 001");
        }

    }

    public void doDruck() {
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(uReportsSession.getDruckDateiname());
    }

    /*******************************Allgemeine Reports**********************************************/

    /*++++++++++++++++++++++Meldelisten+++++++++++++++++++++++++++++++++++++++*/
    public void meldelisteStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(106);
        uReportsSession.setUeberschrift("Meldeliste");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    public void meldeliste() {
//        int sortierung = Integer.parseInt(uReportsSession.getMeldelisteSortierung());
//        int selektion = Integer.parseInt(uReportsSession.getMeldelisteSelektion());
//        int klasse = Integer.parseInt(uReportsSession.getMeldelisteKlasse());
//
//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, -1, uReportsSession.getReportVariante(), -1);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
        
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = Integer.parseInt(uReportsSession.getMeldelisteSortierung());
        int selektion = Integer.parseInt(uReportsSession.getMeldelisteSelektion());
        int klasse = Integer.parseInt(uReportsSession.getMeldelisteKlasse());

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, -1, uReportsSession.getReportVariante(), -1);

        uReportsSession.setFuture3Dateiname(future);
        
        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();
    }

    public void meldelisteAlleTop10() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 3;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = 10; /*-1 = alle*/
        int selektion_inSammelkarte = -1;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(139);
        uReportsSession.setUeberschrift("Meldeliste (alle) Top 10");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();
        
//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);

    }

    public void meldelisteAlleAlpha() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 2;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = -1; /*-1 = alle*/
        int selektion_inSammelkarte = -1;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(140);
        uReportsSession.setUeberschrift("Meldeliste (alle) Alpha");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();

//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteNichtInSammelTop10() {

        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 3;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = 10; /*-1 = alle*/
        int selektion_inSammelkarte = 0;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(141);
        uReportsSession.setUeberschrift("Meldeliste (nicht in Sammelkarte) Top 10");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();
        
//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
   }

    public void meldelisteNichtInSammelAlpha() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 2;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = -1; /*-1 = alle*/
        int selektion_inSammelkarte = 0;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(142);
        uReportsSession.setUeberschrift("Meldeliste (nicht in Sammelkarte) Alpha");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();

//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
   }

    public void meldelisteSRVTop10() {
        int sortierung = 3;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = 10; /*-1 = alle*/
        int selektion_inSammelkarte = 2;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setUeberschrift("Meldeliste (Stimmrechtsvertreter) Top 10");

        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
        uReportsSession.setReportExport(true);
        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteSRVAlpha() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 2;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = -1; /*-1 = alle*/
        int selektion_inSammelkarte = 2;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(143);
        uReportsSession.setUeberschrift("Meldeliste (Stimmrechtsvertreter) Alpha");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();

//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteBriefwahlTop10() {
        int sortierung = 3;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = 10; /*-1 = alle*/
        int selektion_inSammelkarte = 4;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setUeberschrift("Meldeliste (Briefwahl) Top 10");

        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
        uReportsSession.setReportExport(true);
        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteBriefwahlAlpha() {
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        int sortierung = 2;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = -1; /*-1 = alle*/
        int selektion_inSammelkarte = 4;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setReportNummer(144);
        uReportsSession.setUeberschrift("Meldeliste (Briefwahl) Alpha");

        Future<EclFuture3String> future=blMbReportMeldungen.erzeugeReport(lDbBundle, sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setFuture3Dateiname(future);

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);

        pruefenObFertig();

//        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
//        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
//        uReportsSession.setReportDruck(true);
//        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
//        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
//        uReportsSession.setReportExport(true);
//        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteSonstSammelTop10() {
        int sortierung = 3;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = 10; /*-1 = alle*/
        int selektion_inSammelkarte = 15;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setUeberschrift("Meldeliste (Sonstige in Sammelkarte) Top 10");

        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
        uReportsSession.setReportExport(true);
        uReportsSession.setReportExportExcel(true);
    }

    public void meldelisteSonstSammelAlpha() {
        int sortierung = 2;
        /*2=Alpha, 3=Aktien absteigend*/;
        int anzahlDrucken = -1; /*-1 = alle*/
        int selektion_inSammelkarte = 15;

        int selektion = 1; /*nur Einzelkarten*/
        int klasse = 1; /*Aktionäre*/

        uReportsSession.setUeberschrift("Meldeliste (Sonstige in Sammelkarte) Top 10");

        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, -1, anzahlDrucken, "01", selektion_inSammelkarte);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
        uReportsSession.setExportExcelDateiname(blMReports.getRcMeldelistenExportExcelDatei());
        uReportsSession.setReportExport(true);
        uReportsSession.setReportExportExcel(true);
    }

    public void negativlisteAnmeldungenAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(108);
        uReportsSession.setUeberschrift("Negativliste (Anmeldungen)");
        uReportsSession.setReportDirektAufrufen(true);
    }

    public void negativlisteAnmeldungenTop10() {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckenAbAktienzahl = 0;
        repNegativ.sortierung = 3;
        repNegativ.anzahlGroessteDrucken = 10;
        repNegativ.druckeNegativliste(eclDbM.getDbBundle(), rpDrucken, "01");

        eclDbM.closeAll();
        uReportsSession.setUeberschrift("Negativliste (Anmeldungen) - Top 10");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
    }

    private boolean pruefeNegativlisteAnmeldungenAuswahl() {
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    RpDrucken negativlisteAnmeldungenAuswahl() {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckenAbAktienzahl = CaString.longParseLong(uReportsSession.getNegativlisteAktienzahl());
        repNegativ.sortierung = Integer.parseInt(uReportsSession.getNegativlisteSortierung());
        repNegativ.anzahlGroessteDrucken = CaString.integerParseInt(uReportsSession.getNegativlisteGroesste());
        repNegativ.druckeNegativliste(eclDbM.getDbBundle(), rpDrucken, uReportsSession.getReportVariante());

        eclDbM.closeAll();
        return rpDrucken;

    }

    public void negativlistePraesenzStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(107);
        uReportsSession.setUeberschrift("Negativliste (Präsenz)");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    /*++++++++++++++++++++++Kontrolle Anmeldungen++++++++++++++++++++++++++++++++++++*/
    public void kontrolleAnmeldungenKleinererBestand() {
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckeWenigerAktienAngemeldet(eclDbM.getDbBundle(), rpDrucken, "01");

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Kontrolle Anmeldungen - weniger Aktien angemeldet als im Aktienregister");
        uReportsSession.setAnzeigeText("");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(125);

        eclDbM.closeAll();

    }

    public void kontrolleAnmeldungenGroessererBestand() {
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckeMehrAktienAngemeldet(eclDbM.getDbBundle(), rpDrucken, "01");

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Kontrolle Anmeldungen - mehr Aktien angemeldet als im Aktienregister");
        uReportsSession.setAnzeigeText("");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(125);

        eclDbM.closeAll();

    }

    public void kontrolleAnmeldungenNullBestand() {
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckeNullBestand(eclDbM.getDbBundle(), rpDrucken, "01");

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Kontrolle Anmeldungen - Anmeldungen mit 0-Bestand");
        uReportsSession.setAnzeigeText("");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(125);

        eclDbM.closeAll();

    }

    public void kontrolleUnbestaetigteEintraegeAktienregister() {
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepNegativ repNegativ = new RepNegativ();
        repNegativ.druckeUnbestaetigtEingetragen(eclDbM.getDbBundle(), rpDrucken, "01");

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Kontrolle Anmeldungen - mehr Aktien angemeldet als im Aktienregister");
        uReportsSession.setAnzeigeText("");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(125);

        eclDbM.closeAll();

    }

    /*++++++++++++++++++Kontrolliste Weisungen****************************************/
    public void kontrolleWeisungenStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(129);
        uReportsSession.setUeberschrift("Kontrolliste Weisungen");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    public void kontrolleWeisungen() {

        String formularNr = uReportsSession.liefereReportVariante();
        
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.weisungKontrolle_abDatum(eclDbM.getDbBundle(), rpDrucken,
                formularNr,
                uReportsSession.getKontrollisteWeisungenDruckenAb());

        eclDbM.closeAll();

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
 
    }

    /*+++++++++++++++++++++++++Konsistenzprüfung++++++++++++++++++++++++++++++++++++++++*/

    public void kontrolleKonsistenzpruefung() {

        uReportsSession.clear();
        uReportsSession.setReportNummer(145);
        uReportsSession.setUeberschrift("Konsistenzprüfung");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);

        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();
        
        Future<String> future=blMbReportKonsistenzpruefung.erzeugeReport(lDbBundle);
        
        uReportsSession.setFutureDateiname(future);
        

        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);
        
        pruefenObFertig();

    }

    
    /*+++++++++++Sammelkarten++++++++++++++++*/
    private boolean pruefeSammelkarten() {
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    public void sammelkartenKurzOhneWeisung() {
        uReportsSession.setUeberschrift("Sammelkarten Kurz ohne Weisung");

        int iSelektionGruppe = -1;
        int iSelektionIdent = -1;
        int mitVerschlossenen = 0;

        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 1, mitVerschlossenen, "01", -1, iSelektionGruppe, iSelektionIdent);
        eclDbM.closeAll();

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
    }

    public void sammelkartenKurzOhneWeisungAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(115);
        uReportsSession.setUeberschrift("Sammelkarten Kurz ohne Weisung");
        uReportsSession.setReportDirektAufrufen(true);
    }

    public RpDrucken sammelkartenKurzOhneWeisungAuswahl() {
        uReportsSession.setUeberschrift("Sammelkarten Kurz ohne Weisung");

        int iSelektionGruppe = CaString.integerParseInt(uReportsSession.getSelektionGruppe());
        if (iSelektionGruppe == 0) {
            iSelektionGruppe = -1;
        }

        int iSelektionIdent = CaString.integerParseInt(uReportsSession.getSelektionIdent());
        if (iSelektionIdent == 0) {
            iSelektionIdent = -1;
        }

        int mitVerschlossenen = 0;
        if (uReportsSession.isGeheimeWeisungenAusgeben()) {
            mitVerschlossenen = 1;
        }

        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 1, mitVerschlossenen, uReportsSession.getReportVariante(), -1, iSelektionGruppe, iSelektionIdent);

        eclDbM.closeAll();

        return rpDrucken;
    }

    public void sammelkartenMitWeisung() {
        uReportsSession.setUeberschrift("Sammelkarten Kurz mit Weisung");

        int iSelektionGruppe = -1;
        int iSelektionIdent = -1;
        int mitVerschlossenen = 0;

        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 2, mitVerschlossenen, "01", -1, iSelektionGruppe, iSelektionIdent);
        eclDbM.closeAll();

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);

    }

    public void sammelkartenMitWeisungAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(117);
        uReportsSession.setUeberschrift("Sammelkarten Kurz mit Weisung");
        uReportsSession.setReportDirektAufrufen(true);
    }

    public RpDrucken sammelkartenKurzMitWeisungAuswahl() {
        uReportsSession.setUeberschrift("Sammelkarten Kurz mit Weisung");

        int iSelektionGruppe = CaString.integerParseInt(uReportsSession.getSelektionGruppe());
        if (iSelektionGruppe == 0) {
            iSelektionGruppe = -1;
        }

        int iSelektionIdent = CaString.integerParseInt(uReportsSession.getSelektionIdent());
        if (iSelektionIdent == 0) {
            iSelektionIdent = -1;
        }

        int mitVerschlossenen = 0;
        if (uReportsSession.isGeheimeWeisungenAusgeben()) {
            mitVerschlossenen = 1;
        }

        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 2, mitVerschlossenen, uReportsSession.getReportVariante(), -1, iSelektionGruppe, iSelektionIdent);

        eclDbM.closeAll();

        return rpDrucken;
    }

    public void sammelkartenMitWeisungUndAktionaere() {
        uReportsSession.setUeberschrift("Sammelkarten mit Weisung und Aktionären");

        int iSelektionGruppe = -1;
        int iSelektionIdent = -1;
        int mitVerschlossenen = 0;

        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);
        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 3, mitVerschlossenen, "01", -1, iSelektionGruppe, iSelektionIdent);
        eclDbM.closeAll();

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
    }

    public void sammelkartenMitWeisungUndAktionaereAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(119);
        uReportsSession.setUeberschrift("Sammelkarten mit Weisung und Aktionären");
        uReportsSession.setReportDirektAufrufen(true);
    }

    public RpDrucken sammelkartenMitWeisungUndAktionaerenAuswahl() {
        uReportsSession.setUeberschrift("Sammelkarten mit Weisung und Aktionären");

        int iSelektionGruppe = CaString.integerParseInt(uReportsSession.getSelektionGruppe());
        if (iSelektionGruppe == 0) {
            iSelektionGruppe = -1;
        }

        int iSelektionIdent = CaString.integerParseInt(uReportsSession.getSelektionIdent());
        if (iSelektionIdent == 0) {
            iSelektionIdent = -1;
        }

        int mitVerschlossenen = 0;
        if (uReportsSession.isGeheimeWeisungenAusgeben()) {
            mitVerschlossenen = 1;
        }

        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 3, mitVerschlossenen, uReportsSession.getReportVariante(), -1, iSelektionGruppe, iSelektionIdent);

        eclDbM.closeAll();

        return rpDrucken;
    }

    public void sammelkartenExportJNE() {
        uReportsSession.setUeberschrift("Export - eine Spalte J/N/E je TOP");

        int mitVerschlossenen = 0;

        eclDbM.openAll();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.exportAKtionaereMitWeisungen(eclDbM.getDbBundle(), mitVerschlossenen, 0);

        eclDbM.closeAll();
        uReportsSession.setExportDateiname(repAusgabe.exportDatei);
        uReportsSession.setReportExport(true);
    }

    public void sammelkartenExportJNEAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(121);
        uReportsSession.setUeberschrift("Export - eine Spalte J/N/E je TOP");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);

    }

    public void sammelkartenExportJNEAuswahl() {
        uReportsSession.setUeberschrift("Export - eine Spalte J/N/E je TOP");

        int mitVerschlossenen = 0;
        if (uReportsSession.isGeheimeWeisungenAusgeben()) {
            mitVerschlossenen = 1;
        }

        eclDbM.openAll();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.exportAKtionaereMitWeisungen(eclDbM.getDbBundle(), mitVerschlossenen, 0);

        eclDbM.closeAll();
        uReportsSession.setExportDateiname(repAusgabe.exportDatei);
        uReportsSession.setReportExport(true);

    }

    public void sammelkartenExportStimmen() {
        uReportsSession.setUeberschrift("Export - Stimmen J/N/E je TOP");

        int mitVerschlossenen = 0;

        eclDbM.openAll();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.exportAKtionaereMitWeisungen(eclDbM.getDbBundle(), mitVerschlossenen, 1);

        eclDbM.closeAll();
        uReportsSession.setExportDateiname(repAusgabe.exportDatei);
        uReportsSession.setReportExport(true);

    }

    public void sammelkartenExportStimmenAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(123);
        uReportsSession.setUeberschrift("Export - Stimmen J/N/E je TOP");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);

    }

    public void sammelkartenExportStimmenAuswahl() {
        uReportsSession.setUeberschrift("Export - Stimmen J/N/E je TOP");

        int mitVerschlossenen = 0;
        if (uReportsSession.isGeheimeWeisungenAusgeben()) {
            mitVerschlossenen = 1;
        }

        eclDbM.openAll();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.exportAKtionaereMitWeisungen(eclDbM.getDbBundle(), mitVerschlossenen, 1);

        eclDbM.closeAll();
        uReportsSession.setExportDateiname(repAusgabe.exportDatei);
        uReportsSession.setReportExport(true);

    }

    /*+++++++++++Willenserklärunen++++++++++++++*/
    public void willenserklaerungenAlleStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(111);
        uReportsSession.setUeberschrift("Willenserklärungen - Alle");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    private boolean pruefeWillenserklaerungenAlle() {
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    public void willenserklaerungenAlle() {

        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();
        
        Future<String> future=blMbReportWillenserklaerungen.erzeugeReport(lDbBundle, uReportsSession.liefereReportVariante(), 
                uReportsSession.getDruckenAb(), uReportsSession.getNurInternetWK());
        
        uReportsSession.setFutureDateiname(future);
        
        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);
        
        pruefenObFertig();

    }

    public void willenserklaerungenFuerAktionaerStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(112);
        uReportsSession.setUeberschrift("Willenserklärungen für Aktionär");
        uReportsSession.setReportDirektAufrufen(true);
    }

    public boolean pruefeWillenserklaerungenFuerAktionaer() {
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        String hAktionaersnummer = uReportsSession.getAktionaersNummer().trim();
        if (hAktionaersnummer.isEmpty()) {
            uSession.setFehlermeldung("Bitte Aktionärsnummer/EK eingeben!");
            return false;
        }
        eclDbM.openAll();
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());

        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.aktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(uReportsSession.getAktionaersNummer().trim());

        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
        if (eclDbM.getDbBundle().dbAktienregister.anzErgebnis() == 0) {
            uSession.setFehlermeldung("Nummer unbekannt!");
            eclDbM.closeAll();
            return false;
        }
        eclDbM.closeAll();
        return true;
    }

    public RpDrucken willenserklaerungenFuerAktionaer() {
        eclDbM.openAll();

        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());

        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.aktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(uReportsSession.getAktionaersNummer().trim());

        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
        lAktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(eclDbM.getDbBundle());
        blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
        blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
        blWillenserklaerungStatus.piSelektionGeberOderAlle = 2;
        blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregisterEintrag.aktienregisterIdent);
        blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientStandardPDF();

        /**Hier noch einbauen, dass in PDF und automatisch angezeigt wird*/

        RepWillenserklaerungenAktionaer lWillenserklaerungenAktionaer = new RepWillenserklaerungenAktionaer(true, eclDbM.getDbBundle());
        lWillenserklaerungenAktionaer.init(rpDrucken);

        lWillenserklaerungenAktionaer.druckeEinzelnenAktionaer(eclDbM.getDbBundle(), lAktienregisterEintrag.aktionaersnummer, lAktienregisterEintrag, blWillenserklaerungStatus);

        rpDrucken.endeListe();

        eclDbM.closeAll();
        return rpDrucken;

    }

    
    public void willenserklaerungenEingangswegeStart() {
        uReportsSession.setUeberschrift("Willenserklärungen - Statistik Eingangswege");

        String hvDatum = eclParamM.getEclEmittent().hvDatum;

        eclDbM.openAll();

        RepAnmeldestelleSummeErklaerungenWeg repAnmeldestelleSummeErklaerungenWeg = new RepAnmeldestelleSummeErklaerungenWeg(
                eclDbM.getDbBundle());
        repAnmeldestelleSummeErklaerungenWeg.ermittleAnzahlErklaerungen("", hvDatum);

        eclDbM.closeAll();
         
        uReportsSession.setExportDateiname(repAnmeldestelleSummeErklaerungenWeg.rcExportDateiname);
        uReportsSession.setReportExport(true);

    }

    
    /*++++++++++++++++++++++*/

    public void negativlistePraesenz() {
        int sortierung = Integer.parseInt(uReportsSession.getMeldelisteSortierung());
        int selektion = Integer.parseInt(uReportsSession.getMeldelisteSelektion());
        int klasse = Integer.parseInt(uReportsSession.getMeldelisteKlasse());
        int negativlisteArt = Integer.parseInt(uReportsSession.getNegativlisteArt());
        if (negativlisteArt == 1) {
            uReportsSession.setUeberschrift("Negativliste (Präsenz) - alle nie präsent gewesenen");
        } else {
            uReportsSession.setUeberschrift("Negativliste (Präsenz) - alle derzeit nicht präsenten");
        }
        RpDrucken rpDrucken = blMReports.meldeliste(sortierung, selektion, klasse, negativlisteArt, -1, uReportsSession.getReportVariante(), -1);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcMeldelistenExportDatei());
        uReportsSession.setReportExport(true);
    }

    private boolean pruefeNegativlistePraesenzMitAuswahl() {
        if (uMenueSession.menueReportsEinzeln(107) == false) {
            return false;
        }
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    public void negativlisteCockpit() {
        RpDrucken rpDrucken = blMReports.meldeliste(3, 1, 1, 2, -1, "01", -1);
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);
    }

    private boolean pruefeMeldelisteMitAuswahl() {
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    public void statistik() {
        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepMeldeStatistik repMeldeStatistik = new RepMeldeStatistik();
        repMeldeStatistik.druckeStatistik(eclDbM.getDbBundle(), rpDrucken, uReportsSession.liefereReportVariante());

        eclDbM.closeAll();

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Gesamt-Statistik");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(105);

    }

    /*+++++++++++++++++++++Weisungssummen++++++++++++++++++++++++*/
    public void weisungsSummenAlleKunde() {
        /*Geheime verbergen*/
        RpDrucken rpDrucken = blMReports.weisungssummen(4, 0, "01", -1, -1, -1);

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Weisungssummen (alle - ohne verdeckte -, ggf. mit Briefwahl)");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(102);
    }

    public void weisungsSummenAlle() {
        /*Geheime mit ausgeben*/
        RpDrucken rpDrucken = blMReports.weisungssummen(4, 1, "01", -1, -1, -1);

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Weisungssummen (alle - einschließlich verdeckten -, ggf. mit Briefwahl)");
        uReportsSession.setAnzeigeText("Achtung - darf (falls verdeckte Weisungen vorhanden) so erst am HV-Tag an den Kunden weitergegeben werden");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(103);
    }

    public void weisungsSummenAlleCockpit() {
        /*Geheime mit ausgeben*/
        RpDrucken rpDrucken = blMReports.weisungssummen(4, 1, "01", -1, -1, -1);
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);

    }

    public void weisungsSummenSRV() {
        RpDrucken rpDrucken = blMReports.weisungssummen(4, 1, "01", -1, 9001, -1);

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Weisungssummen (nur Stimmrechtsvertreter, Gruppe 9001)");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(104);
    }

    public void weisungsSummenSRVCockpit() {
        /*Geheime mit ausgeben*/
        RpDrucken rpDrucken = blMReports.weisungssummen(4, 1, "01", -1, 9001, -1);
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);

    }

    public void weisungsSummenMitAuswahlStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(101);
        uReportsSession.setUeberschrift("Weisungssummen");
        uReportsSession.setReportDirektAufrufen(true);
    }

    private boolean pruefeWeisungsSummenMitAuswahl() {
        if (uMenueSession.menueReportsEinzeln(2) == false) {
            return false;
        }
        if (uReportsSession.pruefe() == false) {
            uSession.setFehlermeldung(uReportsSession.getFehlermeldung());
            return false;
        }
        return true;
    }

    /*+++++++++++++++++++Stimmausschlußlisten++++++++++++++++++++++++++++*/
    public void stimmausschlussliste() {
        RpDrucken rpDrucken = blMReports.stimmausschlussListe("01");

        uReportsSession.clear();
        uReportsSession.setReportDruck(true);

        uReportsSession.setUeberschrift("Stimmausschlussliste");
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportNummer(104);
    }

    public void stimmausschlusslisteCockpit() {
        RpDrucken rpDrucken = blMReports.stimmausschlussListe("01");
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);
    }

    
    /*+++++++++++++++Elektronischer Einladungsversand****************************/
    public void elekEinlRegistrierteAktionaereStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(130);
        uReportsSession.setUeberschrift("Elektron.Einl.Versand - registrierte Aktionäre");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public void elekEinlRegistrierteAktionaere() {
        elekEinl(1);
    }
    
    private void elekEinl(int pSelektion){
        String formularNr = uReportsSession.liefereReportVariante();
 
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();
                
        RepMailRegistrierung repMailRegistrierung = new RepMailRegistrierung();
        repMailRegistrierung.mitExport = true;
        repMailRegistrierung.selektion = pSelektion;
        repMailRegistrierung.mailRegistrierungBis = uReportsSession.getMailRegistrierungBis();

        repMailRegistrierung.druckeMailRegistrierung(eclDbM.getDbBundle(), rpDrucken, formularNr);

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(repMailRegistrierung.exportDatei);
        uReportsSession.setReportExport(true);
        eclDbM.closeAll();
    }

 
    public void elekEinlRegistrierteAktionaereNichtHVAngemeldetStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(131);
        uReportsSession.setUeberschrift("Elektron.Einl.Versand - registrierte Aktionäre, nicht zur HV angemeldet");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public void elekEinlRegistrierteAktionaereNichtHVAngemeldet() {
        elekEinl(2);
    }


    /*++++++++++++++++++++++++++++++++Sonderreports++++++++++++++++++*/
    
    public void arbeitsplatzStatistikStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(132);
        uReportsSession.setUeberschrift("Arbeitsplatzstatistik - die Exports stehen nach Erzeugung in meetingoutput des jeweiligen Mandanten");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public void arbeitsplatzStatistik() {
        eclDbM.openAll();

        BlStatistikArbeitsplatz blStatistikArbeitsplatz = new BlStatistikArbeitsplatz(eclDbM.getDbBundle());
        blStatistikArbeitsplatz.praesenzBuchungenProArbeitsplatz(uReportsSession.getStatistikStartZeit(),
                Integer.parseInt(uReportsSession.getStatistikZeitSlotLaenge()), true);

        eclDbM.closeAll();
    }

    
    public void barcodesEintrittskartenStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(133);
        uReportsSession.setUeberschrift("Eintrittskarten-Barcodes - Ausdruck von reinen Barcodes für Eintrittskarten / Stimmkarten (unabhängig davon, ob die Meldung / die EK / die SK vorhanden ist oder nicht)");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    private boolean pruefeBarcodesEintrittskarten() {
        String vonNummerEK=uReportsSession.getVonNummerEK().trim();
        String bisNummerEK=uReportsSession.getBisNummerEK().trim();
        String nebenNr = uReportsSession.getNebenNummerEK().trim();
        String hVonNrSK = uReportsSession.getVonNummerSK().trim();

        if (!CaString.isNummern(vonNummerEK)) {
            uSession.setFehlermeldung("Bitte Eintrittskartennummer von eingeben");
            return false;
        }
        if (!CaString.isNummern(bisNummerEK)) {
            uSession.setFehlermeldung("Bitte Eintrittskartennummer bis eingeben");
            return false;
        }

        if (!hVonNrSK.isEmpty() && !CaString.isNummern(hVonNrSK)) {
            uSession.setFehlermeldung("Bitte Stimmkartenbr von eine Zahl eingeben oder leer lassen");
            return false;
        }
        
        if (!nebenNr.isEmpty() && !CaString.isNummern(nebenNr)) {
            uSession.setFehlermeldung("Bitte Index eine Zahl eingeben oder leer lassen");
            return false;
        }
                
        return true;
    }

    public void barcodesEintrittskarten() {
        int vonNr = 0, bisNr = 0, vonNrSK = 0;
        
        String nebenNr = uReportsSession.getNebenNummerEK().trim();
        String hVonNrSK = uReportsSession.getVonNummerSK().trim();

        /*Aufbereiten*/
        vonNr = Integer.parseInt(uReportsSession.getVonNummerEK());
        bisNr = Integer.parseInt(uReportsSession.getBisNummerEK());
        if (hVonNrSK.isEmpty()) {
            hVonNrSK = "0";
        }
        vonNrSK = Integer.parseInt(hVonNrSK);

        /*Verarbeiten*/
        eclDbM.openAll();
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepTestBarcodes repTestBarcodes = new RepTestBarcodes();
        repTestBarcodes.druckeAktionaer(eclDbM.getDbBundle(), rpDrucken, uReportsSession.liefereReportVariante(),
                vonNr, bisNr, vonNrSK, nebenNr);

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);

        eclDbM.closeAll();
    }

    
    public void barcodesEinladungenStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(134);
        uReportsSession.setUeberschrift("Einladungs-Barcodes");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    public boolean pruefeBarcodesEinladungen() {
        
        String vonNummerAktionaer=uReportsSession.getVonNummerAktionaer().trim();
        String bisNummerAktionaer=uReportsSession.getBisNummerAktionaer().trim();

        if (!CaString.isNummern(vonNummerAktionaer)) {
            uSession.setFehlermeldung("Bitte Aktionärsnummer von eingeben");
            return false;
        }
        if (!CaString.isNummern(bisNummerAktionaer)) {
            uSession.setFehlermeldung("Bitte Aktionärasnummer bis eingeben");
            return false;
        }
      
        return true;
    }
    
    public void barcodesEinladungen() {
 
        int vonNr = Integer.parseInt(uReportsSession.getVonNummerAktionaer().trim());
        int bisNr = Integer.parseInt(uReportsSession.getBisNummerAktionaer().trim());

        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initFormular(eclDbM.getDbBundle());

        RepTestBarcodes repTestBarcodes=new RepTestBarcodes();
        repTestBarcodes.druckTestEinladungen(eclDbM.getDbBundle(), rpDrucken, uReportsSession.liefereReportVariante(), vonNr, bisNr);

        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        eclDbM.closeAll();
        
        return;
    }

    /*++++++++++++++++++Gästeliste*****************************************/
    public void gaestelisteStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(124);
        uReportsSession.setUeberschrift("Gästeliste");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    public void gaesteliste() {
        int listenArt = Integer.parseInt(uReportsSession.getGaestelisteArt());

        boolean mitPasswoerter = false;
        int selektion = 1;

        String formularNr = uReportsSession.getReportVariante();

        if (listenArt == 1) {
            /*Übersicht*/
            mitPasswoerter = uReportsSession.isGaestelistePasswoerter();
            if (mitPasswoerter) {
                if (!eclUserLoginM.pruefe_uportal_servicelinetools_passwort()) {
                    mitPasswoerter = false;
                }
            }
            if (formularNr.isEmpty()) {
                if (mitPasswoerter) {
                    formularNr = "02";
                } else {
                    formularNr = "01";
                }

            }
        } else {
            /*Präsenz*/
            selektion = Integer.parseInt(uReportsSession.getGaestelisteSelektion());
        }

        if (formularNr.isEmpty()) {
            formularNr = "01";
        }

        int sortierung = Integer.parseInt(uReportsSession.getGaestelisteSortierung());

        RpDrucken rpDrucken = blMReports.gaesteliste(listenArt, mitPasswoerter, sortierung, selektion, formularNr);
        uReportsSession.setDruckDateiname(rpDrucken.drucklaufDatei);
        uReportsSession.setReportDruck(true);
        uReportsSession.setExportDateiname(blMReports.getRcGaestelisteExportDatei());
        uReportsSession.setReportExport(true);

    }

    /***************************Reports Speziell für Gov&Values********************************************************/
    public String doGVSammelkarteMitWeisungenAktionaeren() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uMenue", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initServer();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.druckeSammelkarten(eclDbM.getDbBundle(), rpDrucken, 3, 1, "01", -1, -1, -1, true);

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(rpDrucken);

        eclDbM.closeAll();
        return xSessionVerwaltung.setzeUEnde("uMenue", true, false, eclUserLoginM.getKennung());
    }

    /**J/N/E je TOP eine Spalte*/
    public void doGVExportAktionaereMitWeisungen() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return;
        }
        uSession.clearRequest();
        doExportAktionaereMitWeisungenVariante(0, 0);
    }

    /**Stimmenzahl je J/N/E je TOP eine Spalte*/
    public void doGVExportAktionaereMitWeisungen1() {
        if (eclUserLoginM.pruefe_govVal_admin() == false) {
            return;
        }
        uSession.clearRequest();
        doExportAktionaereMitWeisungenVariante(1, 0);
    }

    /***************Allgemeine Logiken****************************************/

    /*+++++++++++++++++++++Downloads Sammelkarten mit Weisungen++++++++++++++++++++++++*/
    private void doExportAktionaereMitWeisungenVariante(int pVariante, int pMitVerschlossenen) {
        eclDbM.openAll();

        RepSammelkarten repAusgabe = new RepSammelkarten(KonstWeisungserfassungSicht.internWeisungsreports);

        repAusgabe.exportAKtionaereMitWeisungen(eclDbM.getDbBundle(), pMitVerschlossenen, pVariante);

        eclDbM.closeAll();

        RpDownloadDatei rpDownloadDatei = new RpDownloadDatei();
        try {
            rpDownloadDatei.download(repAusgabe.exportDatei);
        } catch (IOException e) {
            CaBug.drucke("UReports.doExportAktionaereMitWeisungenVariante 001");
        }
        return;
    }

 
    /********************************************Veranstaltungen********************************************/
    public void veranstaltungTeilnehmerStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(138);
        uReportsSession.setUeberschrift("Teilnehmer Veranstaltungen");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public boolean pruefeVeranstaltungTeilnehmer() {
        String hVeranstaltungsnummer=uReportsSession.getVeranstaltungsNr().trim();
        if (!hVeranstaltungsnummer.isEmpty() &&
                (
                CaString.isNummern(hVeranstaltungsnummer)==false
                || Integer.parseInt(hVeranstaltungsnummer)<1 
                || Integer.parseInt(hVeranstaltungsnummer)>20000
                )
                ) {
            uSession.setFehlermeldung("Bitte geben Sie eine gültige Veranstaltungsnummer ein!");
            return false;
        }
        return true;
    }
    
    public void veranstaltungTeilnehmer() {
        CaBug.druckeLog(null, logDrucken, 10);
        String hVeranstaltungsnummer=uReportsSession.getVeranstaltungsNr().trim();
        int veranstaltungsNummer=0;
        if (!hVeranstaltungsnummer.isEmpty()) {
            veranstaltungsNummer=Integer.parseInt(hVeranstaltungsnummer);
        }
        int selektion=Integer.parseInt(uReportsSession.getVeranstaltungenTeilnehmerSelektion());
        eclDbM.openAll();
        eclDbM.openWeitere();

        RepVeranstaltungen repVeranstaltungen = new RepVeranstaltungen(eclDbM.getDbBundle());
        repVeranstaltungen.exportTeilnehmer(veranstaltungsNummer, selektion);

        eclDbM.closeAll();

        String hUberschrift="";
        if (veranstaltungsNummer==0) {
            hUberschrift="Alle";
        }
        else {
            hUberschrift=Integer.toString(veranstaltungsNummer);
        }
        uReportsSession.setUeberschrift("Teilnehmer Veranstaltung "+hUberschrift);

        uReportsSession.setExportDateiname(repVeranstaltungen.rcExportDateiname);
        uReportsSession.setReportExport(true);
        
        uReportsSession.setExportExcelDateiname(repVeranstaltungen.rcExportExcelDateiname);
        uReportsSession.setReportExportExcel(true);
     }

    /********************************************VeranstaltungsManagement - Zusammenfassung********************************************/
    public void veranstaltungsManagementStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(150);
        uReportsSession.setUeberschrift("Zusammenfassunga Veranstaltungsmanagement");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    
    public void veranstaltungsManagement() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();
        
        Future<String> future=blMbReportVeranstaltungsManagement.erzeugeReport(lDbBundle);
        
        uReportsSession.setFutureDateiname(future);
        
        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);
        
        pruefenObFertig();
     }

    /********************************************VeranstaltungsManagement - DetailLTeilnehmerlistenm********************************************/
    public void veranstaltungsManagementDetailStart() {
        uReportsSession.clear();
        uReportsSession.setStartButtonBenutzen(false);
        uReportsSession.setReportNummer(153);
        uReportsSession.setUeberschrift("Teilnehmerlisten Veranstaltungsmanagement");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.erzeugeReportListeFuerULoginMenue();
        uReportsSession.setVeranstaltungenReportAuswahl(blVeranstaltungen.rcVeranstaltungenReportUebersicht);
        eclDbM.closeAll();
        
    }
    
    
    public void veranstaltungsManagementDetail(EclVeranstaltungenReportElement pVeranstaltungenReportElement) {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();
        
        Future<String> future=blMbReportVeranstaltungsManagement.erzeugeReportDetail(lDbBundle, pVeranstaltungenReportElement);
        
        uReportsSession.setFutureDateiname(future);
        
        uReportsSession.setBackgroundReport(true);
        uReportsSession.setStehtZurVerfuegung(false);
        
        pruefenObFertig();
     }

    
    
    
    public String doVeranstaltungManagementDetailAusfuehren(EclVeranstaltungenReportElement pVeranstaltungenReportElement) {
        if (!xSessionVerwaltung.pruefeUStart("uReportsStart", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        
        veranstaltungsManagementDetail(pVeranstaltungenReportElement);
        
        return xSessionVerwaltung.setzeUEnde("uReportsAnzeige", true, false, eclUserLoginM.getKennung());
        
    }
    
    
    
    
    
    /********************************************ErstregistrierungenPortal********************************************/
    public void erstregistrierungenPortalStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(149);
        uReportsSession.setUeberschrift("Erstregistrierungen Portal");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }
    
    public boolean pruefeErstregistrierungenPortal() {
        String datumBis=uReportsSession.getRegistriertBis().trim();
        if (datumBis.isEmpty() || datumBis.length()!=19 || CaDatumZeit.DatumZeitStringToLong(datumBis)==0) {
            uSession.setFehlermeldung("Bitte geben Sie eine gültige Zeit ein!");
            return false;
        }
        return true;
    }
    
    public void erstregistrierungenPortal() {
        CaBug.druckeLog(null, logDrucken, 10);
        String datumBis=uReportsSession.getRegistriertBis().trim();
        long bisZeitStamp=CaDatumZeit.DatumZeitStringToLong(datumBis);
        
        eclDbM.openAll();
        eclDbM.openWeitere();

        long anzNutzerAktionaersportal=eclDbM.getDbBundle().dbLoginDaten.read_anzNutzerAktionaersportal(bisZeitStamp);
        long anzNutzerMitgliederportal=eclDbM.getDbBundle().dbLoginDaten.read_anzNutzerMitgliederportal(bisZeitStamp);
        long anzNutzerInsgesamtportal=eclDbM.getDbBundle().dbLoginDaten.read_anzNutzerInsgesamtportal(bisZeitStamp);

        eclDbM.closeAll();

        uReportsSession.setAnzNutzerAktionaersportal(CaString.toStringDE(anzNutzerAktionaersportal));
        uReportsSession.setAnzNutzerMitgliederportal(CaString.toStringDE(anzNutzerMitgliederportal));
        uReportsSession.setAnzNutzerInsgesamtportal(CaString.toStringDE(anzNutzerInsgesamtportal));

        uReportsSession.setUeberschrift("Registrierte Benutzer im Portal bis "+datumBis);
        
        uReportsSession.setReportExport(false);
        uReportsSession.setBackgroundReport(false);

     }

    
    /**************************************Freiwillige Anmeldungen*************************************************/
    public void freiwilligeAnmeldungenStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(137);
        uReportsSession.setUeberschrift("Freiwillige Anmeldungen");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
   }
    
    public boolean pruefeFreiwilligeAnmeldungen() {
        return true;
    }
    
    public void freiwilligeAnmeldungen() {
        CaBug.druckeLog(null, logDrucken, 10);
        int selektion=Integer.parseInt(uReportsSession.getFreiwilligeAnmeldungenTeilnehmemerSelektion());
        eclDbM.openAll();
        eclDbM.openWeitere();

        RepFreiwilligeAnmeldungen repFreiwilligeAnmeldungen = new RepFreiwilligeAnmeldungen(eclDbM.getDbBundle());
        repFreiwilligeAnmeldungen.exportTeilnehmer(selektion);

        eclDbM.closeAll();

        uReportsSession.setUeberschrift("Freiwillige Anmeldungen");

        uReportsSession.setExportDateiname(repFreiwilligeAnmeldungen.rcExportDateiname);
        uReportsSession.setReportExport(true);
        
        uReportsSession.setExportExcelDateiname(repFreiwilligeAnmeldungen.rcExportExcelDateiname);
        uReportsSession.setReportExportExcel(true);
    }
    
    /******************************************Remote-Aktienregister**************************************************/

    public void remoteARTickets() {
        eclDbM.openAll();
        eclDbM.openWeitere();

        RepMitgliederportal repMitgliederportal = new RepMitgliederportal(eclDbM.getDbBundle());
        repMitgliederportal.exportTickets("26.11.2021", "99.99.9999");

        eclDbM.closeAll();

        uReportsSession.setUeberschrift("Tickets");

        uReportsSession.setExportDateiname(repMitgliederportal.rcExportDateiname);
        uReportsSession.setReportExport(true);
        CaBug.druckeLog("repMitgliederportal.rcExportDateiname=" + repMitgliederportal.rcExportDateiname, logDrucken, 10);

    }
    
    
    public void remoteKontaktanfragenTickets() {
        eclDbM.openAll();
        eclDbM.openWeitere();
        
        RepMitgliederportal repMitgliederportal = new RepMitgliederportal(
                eclDbM.getDbBundle());
        repMitgliederportal.exportKontaktanfragenTickets("26.11.2021", "99.99.9999");

        eclDbM.closeAll();

        uReportsSession.setUeberschrift("Kontaktanfrage-Tickets");

        uReportsSession.setExportDateiname(repMitgliederportal.rcExportDateiname);
        uReportsSession.setReportExport(true);
        CaBug.druckeLog("repMitgliederportal.rcExportDateiname="+repMitgliederportal.rcExportDateiname, logDrucken, 10);
       
    }
   
    /****************************Basis-Set-Abgleich und Reports***************************************/
    /*Ist eigentlich Report + Update, aber wegen Nutzung der Dialoge hier rein*/
    
    public void basisSetAbgleichStart() {
        uReportsSession.clear();
        uReportsSession.setReportNummer(136);
        uReportsSession.setUeberschrift("Portaltexte Basis-Set Abgleich");
        uReportsSession.setReportAnzeigeSeiteAufrufen(true);
    }

    public boolean pruefeBasisSetAbgleich() {
        if (!CaString.isNummern(uReportsSession.getVergleichAusgangsset())) {
            uReportsSession.setFehlermeldung("Ausgangsset unzulässiger Wert");
            return false;
        }
        if (!CaString.isNummern(uReportsSession.getVergleichVergleichsset())) {
            uReportsSession.setFehlermeldung("Vergleichsset unzulässiger Wert");
            return false;
        }
       
        return true;
    }

    public void basisSetAbgleich() {
        boolean seiteBezeichnungUebernehmen=uReportsSession.isSeiteBezeichnungUebernehmen();
        boolean fehlendeEintraegeUebernehmen=uReportsSession.isFehlendeEintraegeUebernehmen();
        int vergleichAusgangsset=Integer.parseInt(uReportsSession.getVergleichAusgangsset());
        int vergleichVergleichsset=Integer.parseInt(uReportsSession.getVergleichVergleichsset());
        
        BvTexte bvTexte=new BvTexte();
        eclDbM.openAll();
        
        bvTexte.vergleicheSets(eclDbM.getDbBundle(), vergleichAusgangsset, vergleichVergleichsset, seiteBezeichnungUebernehmen, fehlendeEintraegeUebernehmen);
        uReportsSession.setReportDruck(false);
        uReportsSession.setExportDateiname(bvTexte.rcDateinameVergleichsergebnis);
        uReportsSession.setReportExport(true);
        eclDbM.closeAll();
    }


    public void basisSetAusgeben(boolean pBasisSet) {
        eclDbM.openAll();
        
        BvTexte bvTexte=new BvTexte();
        bvTexte.exportTexte(eclDbM.getDbBundle(), !pBasisSet);
       
        eclDbM.closeAll();

        if (pBasisSet) {
            uReportsSession.setUeberschrift("Ausgabe Basisset");
        }
        else {
            uReportsSession.setUeberschrift("Ausgabe Mandantentexte");
        }

        uReportsSession.setExportDateiname(bvTexte.rcDateinameVergleichsergebnis);
        uReportsSession.setReportExport(true);
       
    }

}
