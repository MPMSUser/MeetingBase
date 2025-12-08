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

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclFuture3String;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenReportElement;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UReportsSession implements Serializable {
    private static final long serialVersionUID = -2292297684292401184L;

    /*Wird immer als Überschrifts-Text angezeigt*/
    private String ueberschrift = "";

    /*Wird bei Report-Start als Infotext unter Überschrift angezeigt*/
    private String startText = "";

    /**true => Start des Reports erfolgt über den Startbutton, sonst über Einzel-Buttons in der Parametereingabe*/
    private boolean startButtonBenutzen=true;
    
    /* 101=weisungsSummenMitAuswahl
     * 102=weisungssummenAlleKunde
     * 103=weisungssummenAlle (intern)
     * 103=weisungssummenSRV (intern)
     * 104=stimmausschlussliste
     * 105=Gesamt-Statistik
     * 106=Meldeliste
     * 107=Negativliste (Präsenz)
     * 108=Negativliste (Anmeldungen) - Auswahl
     * 109=Negativliste (Anmeldungen) - Top 10
     * 110=Willenserklärung (Menüpunkt)
     * 111=Willenserklärung - Gesamtliste (Background)
     * 112=Willenserklärung - für Einzelaktionär
     * 113=Willenserklärung - für Aktionäre mit Mitteilung
     * 114=Sammelkarten - kurz ohne Weisung
     * 115=Sammelkarten - kurz ohne Weisung - mit Auswahl
     * 116=Sammelkarten - mit Weisung
     * 117=Sammelkarten - mit Weisung - mit Auswahl
     * 118=Sammelkarten - mit Weisung und Aktionären
     * 119=Sammelkarten - mit Weisung unt Aktionären - mit Auswahl
     * 120=Sammelkarten Export - J/N/E in Spalte
     * 121=Sammelkarten Export - J/N/E in Spalte - mit Auswahl
     * 122=Sammelkarten Export - Stimmen in Spalte
     * 123=Sammelkarten Export - Stimmen in Spalte - mit Auswahl
     * 124=Gästeliste
     * 125=Kontrolliste Anmeldungen - Aktionäre mit kleinerem Bestand angemeldet
     * 126=Kontrolliste Anmeldungen - Aktionäre mit größerem Bestand angemeldet
     * 127=Kontrolliste Anmeldungen - Aktionäre mit 0-Bestand angemeldet
     * 128=Kontrolliste Anmeldungen - Unbestätigte Einträge im Aktienregister
     * 129=Kontrolliste Weisungen
     * 130=Elekt.Einladungsversand - alle registrierten Aktionäre
     * 131=Elekt.Einladungsversand - alle nicht zur HV angemeldeten aber für den Elek.Versand registrierten Aktionäre
     * 132=Sonder-Report: Arbeitsplatzstatistik
     * 133=Sonder-Report: Eintrittskarten-Barcodes
     * 134=Sonder-Report: Einladungs-Barcodes
     * 
      * 
     * 136=Basis-Set Abgleich
     * 
     * 137=Freiwillige Anmeldungen
     * 138=Veranstaltungen
     * 
     * 139=Meldeliste Alle TOP 10 (wg. Refresh)
     * 140=Meldeliste Alle Alpha (wg. Refresh)
     * 141=Meldeliste Nicht in Sammelkarte TOP 10 (wg. Refresh)
     * 142=Meldeliste Nicht in Sammelkarte Alpha (wg. Refresh)
     * 143=Meldeliste SRV Alpha
     * 144=Meldeliste Briefwahl Alpha
     * 
     * 145=Konsistenzprüfung
     * 
     * 
     * 148=Export/Import: Gästekarten Import
     * 
     * 149=erstRegistrierungenPortal
     * 
     * 150=Auswertung Veranstaltungsmanagement (Zusammenfassung)
     * 
     * 151=Export/Import: ku310 Bestand Import
     * 
     * 153=Auswertung Veranstaltungsmanagement (Teilnehmerlisten)
     * */
    private int reportNummer = 0;

    private boolean reportDirektAufrufen = false;
    private boolean reportAnzeigeSeiteAufrufen = false;

    /*Für Background-Reports: wenn stehtZurVerfuegung=false, dann Refresh aufrufen*/
    private boolean backgroundReport=false;
    private boolean stehtZurVerfuegung = false;
    private Future<String> futureDateiname=null;
    private Future<EclFuture3String> future3Dateiname=null;

    /*Allgemeine Variablen*/
    private String reportVariante = "";

    /*Meldeliste*/
    /*1=meldeIdent, 2=Name, 3=Aktien absteigend, 4=EK-Nr*/
    private String meldelisteSortierung = "1";
    /*1=nur Aktionärsmeldungen, 2=nur Sammelkarten, 3=Alle*/
    private String meldelisteSelektion = "1";
    /*1= Aktionäre, 0=Gäste*/
    private String meldelisteKlasse = "1";

    /*Negativliste(Anmeldungen)*/
    private String negativlisteSortierung = "1";
    private String negativlisteAktienzahl = "";
    private String negativlisteGroesste = "";

    /*Negativliste (Präsenz)*/
    /*1=alle noch nie präsenten*/
    /*2=alle derzeit nicht präsenten*/
    private String negativlisteArt = "1";

    /*Weisungen*/
    private String druckNurSammelkartenIdent = "";
    private String druckNurGruppe = "";
    private boolean geheimeWeisungenAusgeben = false;

    /*Sammelkarten*/
    private String selektionGruppe = "";
    private String selektionIdent = "";
    //	private boolean selektionMitVerdeckten=false; => geheimeWeisungenAusgeben

    /*Willenserklärungen*/

    private String druckenAb = "";
    private String nurInternetWK = "1"; /*1=alle, 2=nur Nicht-Internet*/

    private String aktionaersNummer = "";

    
    
    /*Kontrolliste Weisungen*/
    private String kontrollisteWeisungenDruckenAb = "";
    
    /*Elektronischer Einladungsversand*/
    /*Stichtag - nur die ausgeben, die bis zu diesem Datum registriert sind*/
    private String mailRegistrierungBis = "";

    
    /*SonderReports*/
    private String vonNummerEK = "";
    private String bisNummerEK = "";
    private String vonNummerSK = "";
    private String nebenNummerEK = "";

    private String vonNummerAktionaer = "";
    private String bisNummerAktionaer = "";

    private String statistikStartZeit = "2018.06.03 10:00:00";
    private String statistikZeitSlotLaenge = "5";

    /*Basis-Set-Abgleich*/
    private String vergleichAusgangsset="";
    private String vergleichVergleichsset="";
    private boolean seiteBezeichnungUebernehmen=false;
    private boolean fehlendeEintraegeUebernehmen=false;
    
    /*Gäste*/
    /*1=Übersicht, 2=Gäste-Präsenz*/
    private String gaestelisteArt="1";
    /*1=ohne Passwort, 2=mit Passwort*/
    private boolean gaestelistePasswoerter=false;
    /*1=nach Zutrittsnummer, 2=nach Name*/
    private String gaestelisteSortierung="1";
    /*1=alle, 2=alle gerade präsenten, 3=alle präsenten oder präsent gewesenen*/
    private String gaestelisteSelektion="1";
    
    /*Veranstaltungen*/
    private String veranstaltungsNr="";
    /*1=alle aktiven; 2=angemeldete; 3=abgemeldete*/
    private String veranstaltungenTeilnehmerSelektion="1";
 
    private List<EclVeranstaltungenReportElement> veranstaltungenReportAuswahl=null;
    public boolean liefereVeranstaltungenReportAuswahlIstLeer(){
        if (veranstaltungenReportAuswahl==null || veranstaltungenReportAuswahl.size()==0) {
            return true;
        }
        return false;
    }


    
    
    /*Freiwillige Anmeldung*/
    /*1=alle aktiven; 2=alle angemeldeten; 3=abgemeldete 4=Präsenz-angemeldete; 5=Virtuell-Angemeldete*/
    private String freiwilligeAnmeldungenTeilnehmemerSelektion="1";

    /*Import Dateiname*/
    private String importDateiname="";

    /*Registriert im Portal bis*/
    private String registriertBis="";
    
    private String anzNutzerAktionaersportal;
    private String anzNutzerMitgliederportal;
    private String anzNutzerInsgesamtportal;


    /*Fehlermeldung*/
    private String fehlermeldung = "";

    /********Für Anzeige************/
    private String anzeigeText = "";
    private boolean reportExport = false;
    private boolean reportExportExcel = false;
    private boolean reportDruck = false;
    /*Wird verwendet, wenn keine Datei zum Anzeigen / Download zur Verfügung steht, sondern nur einfach "Fertig"*/
    private boolean aufgabeFertig = false;
    
    private String exportDateiname = "";
    private String exportExcelDateiname = "";
    private String druckDateiname = "";

    public void clear() {
        ueberschrift = "";
        startText = "";

        startButtonBenutzen=true;
        
        reportDirektAufrufen = false;
        reportAnzeigeSeiteAufrufen = false;
        
        backgroundReport=false;
        stehtZurVerfuegung = false;
        
        reportVariante = "";

        /*Meldeliste*/
        meldelisteSortierung = "1";
        meldelisteSelektion = "1";
        meldelisteKlasse = "1";

        /*Negativliste(Anmeldungen)*/
        /*1=nach Aktionärsnummer, 2=nach Nachme, 3=nach Aktien absteigend*/
        negativlisteSortierung = "1";
        negativlisteAktienzahl = "";
        negativlisteGroesste = "";

        /*Negativliste (Präsenz)*/
        negativlisteArt = "1";

        /*Weisungen*/
        druckNurSammelkartenIdent = "";
        druckNurGruppe = "";
        geheimeWeisungenAusgeben = false;

        /*Sammelkarten*/
        selektionGruppe = "";
        selektionIdent = "";

        /*Willenserklärungen*/
        druckenAb = "";
        nurInternetWK = "1";
        aktionaersNummer = "";
        
        /*Registriert für elektronischen Einladungsversand*/
        mailRegistrierungBis = "";
        
        /*Sonder-Reports*/
        vonNummerEK = "";
        bisNummerEK = "";
        vonNummerSK = "";
        nebenNummerEK = "";
        vonNummerAktionaer = "";
        bisNummerAktionaer = "";
        statistikStartZeit = "2018.06.03 10:00:00";
        statistikZeitSlotLaenge = "5";
        
        /*Gästeliste*/
        gaestelisteArt="1";
        gaestelistePasswoerter=false;
        gaestelisteSortierung="1";
        gaestelisteSelektion="1";

        
        druckenAb = "";
        nurInternetWK = "1"; /*1=alle, 2=nur Nicht-Internet*/

        reportExport = false;
        reportExportExcel = false;
       reportDruck = false;
       aufgabeFertig = false;

       
        exportDateiname = "";
        exportExcelDateiname = "";
        druckDateiname = "";
        anzeigeText = "";
        
        /*Basis-Set-Abgleich*/
        seiteBezeichnungUebernehmen=false;
        fehlendeEintraegeUebernehmen=false;
        vergleichAusgangsset="";
        vergleichVergleichsset="";

        /*Veranstaltungen*/
        veranstaltungsNr="";
        veranstaltungenTeilnehmerSelektion="1";
        veranstaltungenReportAuswahl=null;
        
        
        /*Freiwillige Anmeldung*/
        freiwilligeAnmeldungenTeilnehmemerSelektion="1";
        
        /*Import Dateiname*/
        importDateiname="";
        
        /*Erstregistrierung Portal*/
        registriertBis="";

    }

    public boolean pruefe() {
        fehlermeldung = "";
        reportVariante = reportVariante.trim();
        druckNurSammelkartenIdent = druckNurSammelkartenIdent.trim();
        druckNurGruppe = druckNurGruppe.trim();

        /*Report-Variante*/
        if (reportVariante.isEmpty()) {
            reportVariante = "01";
        }
        if (!CaString.isNummern(reportVariante)) {
            fehlermeldung = "Report-Variante unzulässiger Wert";
            return false;
        }
        if (reportVariante.length() > 2) {
            fehlermeldung = "Report-Variante unzulässiger Wert";
            return false;
        }
        reportVariante = Integer.toString(Integer.parseInt(reportVariante));
        reportVariante = CaString.fuelleLinksNull(reportVariante, 2);

        /*druckNurSammelkartenIdent*/
        if (!druckNurSammelkartenIdent.isEmpty()) {
            if (!CaString.isNummern(druckNurSammelkartenIdent)) {
                fehlermeldung = "SammelIdent unzulässiger Wert";
                return false;
            }
            if (druckNurSammelkartenIdent.length() > 5) {
                fehlermeldung = "SammelIdent unzulässiger Wert";
                return false;
            }
        }

        /*druckNurGruppe*/
        if (!druckNurGruppe.isEmpty()) {
            if (!CaString.isNummern(druckNurGruppe)) {
                fehlermeldung = "Gruppe unzulässiger Wert";
                return false;
            }
            if (druckNurGruppe.length() > 4) {
                fehlermeldung = "Gruppe unzulässiger Wert";
                return false;
            }
        }

        if (!druckNurSammelkartenIdent.isEmpty() && !druckNurGruppe.isEmpty()) {
            fehlermeldung = "Nur Selektion nach SammelkartenIdent oder Gruppe möglich, nicht beide";
            return false;
        }

        /*++++++++Negativliste+++++++++++++*/
        negativlisteAktienzahl = negativlisteAktienzahl.trim();
        negativlisteGroesste = negativlisteGroesste.trim();

        /*negativlisteAktienzahl*/
        if (!negativlisteAktienzahl.isEmpty()) {
            if (!CaString.isNummern(negativlisteAktienzahl)) {
                fehlermeldung = "Aktienzahl unzulässiger Wert";
                return false;
            }
            if (druckNurGruppe.length() > 8) {
                fehlermeldung = "Aktienzahl unzulässiger Wert";
                return false;
            }
        }

        /*negativlisteGroesste*/
        if (!negativlisteGroesste.isEmpty()) {
            if (!CaString.isNummern(negativlisteGroesste)) {
                fehlermeldung = "Anzahl unzulässiger Wert";
                return false;
            }
            if (negativlisteGroesste.length() > 5) {
                fehlermeldung = "Anzahl unzulässiger Wert";
                return false;
            }
        }

        if (negativlisteSortierung.compareTo("3") != 0) {
            if (!negativlisteGroesste.isEmpty() && Integer.parseInt(negativlisteGroesste) > 0) {
                fehlermeldung = "Eingabe größten nur bei Sortierung nach Aktien zulässig";
                return false;
            }
        }

        /*++++++++Sammelkarten+++++++++++++*/
        selektionGruppe = selektionGruppe.trim();
        selektionIdent = selektionIdent.trim();

        /*selektionGruppe*/
        if (!selektionGruppe.isEmpty()) {
            if (!CaString.isNummern(selektionGruppe)) {
                fehlermeldung = "Selektion Gruppe - unzulässiger Wert";
                return false;
            }
            if (selektionGruppe.length() > 5) {
                fehlermeldung = "Selektion Gruppe - unzulässiger Wert";
                return false;
            }
        }

        /*selektionIdent*/
        if (!selektionIdent.isEmpty()) {
            if (!CaString.isNummern(selektionIdent)) {
                fehlermeldung = "Anzahl unzulässiger Wert";
                return false;
            }
            if (selektionIdent.length() > 5) {
                fehlermeldung = "Anzahl unzulässiger Wert";
                return false;
            }
        }

        return true;
    }

    /**Liefert 01, wenn nichts eingegeben*/
    public String liefereReportVariante() {
        if (reportVariante.isEmpty()) {
            return "01";
        }
        return reportVariante;
    }
    
    /**********************Standard getter und setter*****************************************/

    public int getReportNummer() {
        return reportNummer;
    }

    public void setReportNummer(int reportNummer) {
        this.reportNummer = reportNummer;
    }

    public String getUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getStartText() {
        return startText;
    }

    public void setStartText(String startText) {
        this.startText = startText;
    }

    public String getReportVariante() {
        return reportVariante;
    }

    public void setReportVariante(String reportVariante) {
        this.reportVariante = reportVariante;
    }

    public String getDruckNurSammelkartenIdent() {
        return druckNurSammelkartenIdent;
    }

    public void setDruckNurSammelkartenIdent(String druckNurSammelkartenIdent) {
        this.druckNurSammelkartenIdent = druckNurSammelkartenIdent;
    }

    public String getDruckNurGruppe() {
        return druckNurGruppe;
    }

    public void setDruckNurGruppe(String druckNurGruppe) {
        this.druckNurGruppe = druckNurGruppe;
    }

    public boolean isGeheimeWeisungenAusgeben() {
        return geheimeWeisungenAusgeben;
    }

    public void setGeheimeWeisungenAusgeben(boolean geheimeWeisungenAusgeben) {
        this.geheimeWeisungenAusgeben = geheimeWeisungenAusgeben;
    }

    public boolean isReportDirektAufrufen() {
        return reportDirektAufrufen;
    }

    public void setReportDirektAufrufen(boolean reportDirektAufrufen) {
        this.reportDirektAufrufen = reportDirektAufrufen;
    }

    public boolean isReportAnzeigeSeiteAufrufen() {
        return reportAnzeigeSeiteAufrufen;
    }

    public void setReportAnzeigeSeiteAufrufen(boolean reportAnzeigeSeiteAufrufen) {
        this.reportAnzeigeSeiteAufrufen = reportAnzeigeSeiteAufrufen;
    }

    public String getFehlermeldung() {
        return fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    public String getExportDateiname() {
        return exportDateiname;
    }

    public void setExportDateiname(String exportDateiname) {
        this.exportDateiname = exportDateiname;
    }

    public boolean isReportExport() {
        return reportExport;
    }

    public void setReportExport(boolean reportExport) {
        this.reportExport = reportExport;
    }

    public boolean isReportDruck() {
        return reportDruck;
    }

    public void setReportDruck(boolean reportDruck) {
        this.reportDruck = reportDruck;
    }

    public String getDruckDateiname() {
        return druckDateiname;
    }

    public void setDruckDateiname(String druckDateiname) {
        this.druckDateiname = druckDateiname;
    }

    public String getAnzeigeText() {
        return anzeigeText;
    }

    public void setAnzeigeText(String anzeigeText) {
        this.anzeigeText = anzeigeText;
    }

    public String getMeldelisteSortierung() {
        return meldelisteSortierung;
    }

    public void setMeldelisteSortierung(String meldelisteSortierung) {
        this.meldelisteSortierung = meldelisteSortierung;
    }

    public String getMeldelisteSelektion() {
        return meldelisteSelektion;
    }

    public void setMeldelisteSelektion(String meldelisteSelektion) {
        this.meldelisteSelektion = meldelisteSelektion;
    }

    public String getMeldelisteKlasse() {
        return meldelisteKlasse;
    }

    public void setMeldelisteKlasse(String meldelisteKlasse) {
        this.meldelisteKlasse = meldelisteKlasse;
    }

    public String getNegativlisteArt() {
        return negativlisteArt;
    }

    public void setNegativlisteArt(String negativlisteArt) {
        this.negativlisteArt = negativlisteArt;
    }

    public String getNegativlisteGroesste() {
        return negativlisteGroesste;
    }

    public void setNegativlisteGroesste(String negativlisteGroesste) {
        this.negativlisteGroesste = negativlisteGroesste;
    }

    public String getNegativlisteSortierung() {
        return negativlisteSortierung;
    }

    public void setNegativlisteSortierung(String negativlisteSortierung) {
        this.negativlisteSortierung = negativlisteSortierung;
    }

    public String getNegativlisteAktienzahl() {
        return negativlisteAktienzahl;
    }

    public void setNegativlisteAktienzahl(String negativlisteAktienzahl) {
        this.negativlisteAktienzahl = negativlisteAktienzahl;
    }

    public boolean isStehtZurVerfuegung() {
        return stehtZurVerfuegung;
    }

    public void setStehtZurVerfuegung(boolean stehtZurVerfuegung) {
        this.stehtZurVerfuegung = stehtZurVerfuegung;
    }

    public String getDruckenAb() {
        return druckenAb;
    }

    public void setDruckenAb(String druckenAb) {
        this.druckenAb = druckenAb;
    }

    public String getNurInternetWK() {
        return nurInternetWK;
    }

    public void setNurInternetWK(String nurInternetWK) {
        this.nurInternetWK = nurInternetWK;
    }

    public String getSelektionGruppe() {
        return selektionGruppe;
    }

    public void setSelektionGruppe(String selektionGruppe) {
        this.selektionGruppe = selektionGruppe;
    }

    public String getSelektionIdent() {
        return selektionIdent;
    }

    public void setSelektionIdent(String selektionIdent) {
        this.selektionIdent = selektionIdent;
    }

    public String getAktionaersNummer() {
        return aktionaersNummer;
    }

    public void setAktionaersNummer(String aktionaersNummer) {
        this.aktionaersNummer = aktionaersNummer;
    }
 
    public String getGaestelisteSortierung() {
        return gaestelisteSortierung;
    }

    public void setGaestelisteSortierung(String gaestelisteSortierung) {
        this.gaestelisteSortierung = gaestelisteSortierung;
    }

    public String getGaestelisteSelektion() {
        return gaestelisteSelektion;
    }

    public void setGaestelisteSelektion(String gaestelisteSelektion) {
        this.gaestelisteSelektion = gaestelisteSelektion;
    }

    public boolean isGaestelistePasswoerter() {
        return gaestelistePasswoerter;
    }

    public void setGaestelistePasswoerter(boolean gaestelistePasswoerter) {
        this.gaestelistePasswoerter = gaestelistePasswoerter;
    }

    public String getGaestelisteArt() {
        return gaestelisteArt;
    }

    public void setGaestelisteArt(String gaestelisteArt) {
        this.gaestelisteArt = gaestelisteArt;
    }

    public String getKontrollisteWeisungenDruckenAb() {
        return kontrollisteWeisungenDruckenAb;
    }

    public void setKontrollisteWeisungenDruckenAb(String kontrollisteWeisungenDruckenAb) {
        this.kontrollisteWeisungenDruckenAb = kontrollisteWeisungenDruckenAb;
    }

    public boolean isBackgroundReport() {
        return backgroundReport;
    }

    public void setBackgroundReport(boolean backgroundReport) {
        this.backgroundReport = backgroundReport;
    }

    public Future<String> getFutureDateiname() {
        return futureDateiname;
    }

    public void setFutureDateiname(Future<String> futureDateiname) {
        this.futureDateiname = futureDateiname;
    }

    public String getMailRegistrierungBis() {
        return mailRegistrierungBis;
    }

    public void setMailRegistrierungBis(String mailRegistrierungBis) {
        this.mailRegistrierungBis = mailRegistrierungBis;
    }

    public String getVonNummerEK() {
        return vonNummerEK;
    }

    public void setVonNummerEK(String vonNummerEK) {
        this.vonNummerEK = vonNummerEK;
    }

    public String getBisNummerEK() {
        return bisNummerEK;
    }

    public void setBisNummerEK(String bisNummerEK) {
        this.bisNummerEK = bisNummerEK;
    }

    public String getVonNummerSK() {
        return vonNummerSK;
    }

    public void setVonNummerSK(String vonNummerSK) {
        this.vonNummerSK = vonNummerSK;
    }

    public String getNebenNummerEK() {
        return nebenNummerEK;
    }

    public void setNebenNummerEK(String nebenNummerEK) {
        this.nebenNummerEK = nebenNummerEK;
    }

    public String getVonNummerAktionaer() {
        return vonNummerAktionaer;
    }

    public void setVonNummerAktionaer(String vonNummerAktionaer) {
        this.vonNummerAktionaer = vonNummerAktionaer;
    }

    public String getBisNummerAktionaer() {
        return bisNummerAktionaer;
    }

    public void setBisNummerAktionaer(String bisNummerAktionaer) {
        this.bisNummerAktionaer = bisNummerAktionaer;
    }

    public String getStatistikStartZeit() {
        return statistikStartZeit;
    }

    public void setStatistikStartZeit(String statistikStartZeit) {
        this.statistikStartZeit = statistikStartZeit;
    }

    public String getStatistikZeitSlotLaenge() {
        return statistikZeitSlotLaenge;
    }

    public void setStatistikZeitSlotLaenge(String statistikZeitSlotLaenge) {
        this.statistikZeitSlotLaenge = statistikZeitSlotLaenge;
    }

    public boolean isSeiteBezeichnungUebernehmen() {
        return seiteBezeichnungUebernehmen;
    }

    public void setSeiteBezeichnungUebernehmen(boolean seiteBezeichnungUebernehmen) {
        this.seiteBezeichnungUebernehmen = seiteBezeichnungUebernehmen;
    }

    public boolean isFehlendeEintraegeUebernehmen() {
        return fehlendeEintraegeUebernehmen;
    }

    public void setFehlendeEintraegeUebernehmen(boolean fehlendeEintraegeUebernehmen) {
        this.fehlendeEintraegeUebernehmen = fehlendeEintraegeUebernehmen;
    }

    public String getVergleichAusgangsset() {
        return vergleichAusgangsset;
    }

    public void setVergleichAusgangsset(String vergleichAusgangsset) {
        this.vergleichAusgangsset = vergleichAusgangsset;
    }

    public String getVergleichVergleichsset() {
        return vergleichVergleichsset;
    }

    public void setVergleichVergleichsset(String vergleichVergleichsset) {
        this.vergleichVergleichsset = vergleichVergleichsset;
    }

    public boolean isReportExportExcel() {
        return reportExportExcel;
    }

    public void setReportExportExcel(boolean reportExportExcel) {
        this.reportExportExcel = reportExportExcel;
    }

    public String getExportExcelDateiname() {
        return exportExcelDateiname;
    }

    public void setExportExcelDateiname(String exportExcelDateiname) {
        this.exportExcelDateiname = exportExcelDateiname;
    }

    public String getVeranstaltungenTeilnehmerSelektion() {
        return veranstaltungenTeilnehmerSelektion;
    }

    public void setVeranstaltungenTeilnehmerSelektion(String veranstaltungenTeilnehmerSelektion) {
        this.veranstaltungenTeilnehmerSelektion = veranstaltungenTeilnehmerSelektion;
    }

    public String getFreiwilligeAnmeldungenTeilnehmemerSelektion() {
        return freiwilligeAnmeldungenTeilnehmemerSelektion;
    }

    public void setFreiwilligeAnmeldungenTeilnehmemerSelektion(String freiwilligeAnmeldungenTeilnehmemerSelektion) {
        this.freiwilligeAnmeldungenTeilnehmemerSelektion = freiwilligeAnmeldungenTeilnehmemerSelektion;
    }

    public String getVeranstaltungsNr() {
        return veranstaltungsNr;
    }

    public void setVeranstaltungsNr(String veranstaltungsNr) {
        this.veranstaltungsNr = veranstaltungsNr;
    }

    public Future<EclFuture3String> getFuture3Dateiname() {
        return future3Dateiname;
    }

    public void setFuture3Dateiname(Future<EclFuture3String> future3Dateiname) {
        this.future3Dateiname = future3Dateiname;
    }

    public String getImportDateiname() {
        return importDateiname;
    }

    public void setImportDateiname(String importDateiname) {
        this.importDateiname = importDateiname;
    }

    public boolean isAufgabeFertig() {
        return aufgabeFertig;
    }

    public void setAufgabeFertig(boolean aufgabeFertig) {
        this.aufgabeFertig = aufgabeFertig;
    }

    public String getRegistriertBis() {
        return registriertBis;
    }

    public void setRegistriertBis(String registriertBis) {
        this.registriertBis = registriertBis;
    }

    public String getAnzNutzerAktionaersportal() {
        return anzNutzerAktionaersportal;
    }

    public void setAnzNutzerAktionaersportal(String anzNutzerAktionaersportal) {
        this.anzNutzerAktionaersportal = anzNutzerAktionaersportal;
    }

    public String getAnzNutzerMitgliederportal() {
        return anzNutzerMitgliederportal;
    }

    public void setAnzNutzerMitgliederportal(String anzNutzerMitgliederportal) {
        this.anzNutzerMitgliederportal = anzNutzerMitgliederportal;
    }

    public String getAnzNutzerInsgesamtportal() {
        return anzNutzerInsgesamtportal;
    }

    public void setAnzNutzerInsgesamtportal(String anzNutzerInsgesamtportal) {
        this.anzNutzerInsgesamtportal = anzNutzerInsgesamtportal;
    }

    public boolean isStartButtonBenutzen() {
        return startButtonBenutzen;
    }

    public void setStartButtonBenutzen(boolean startButtonBenutzen) {
        this.startButtonBenutzen = startButtonBenutzen;
    }

    public List<EclVeranstaltungenReportElement> getVeranstaltungenReportAuswahl() {
        return veranstaltungenReportAuswahl;
    }

    public void setVeranstaltungenReportAuswahl(List<EclVeranstaltungenReportElement> veranstaltungenReportAuswahl) {
        this.veranstaltungenReportAuswahl = veranstaltungenReportAuswahl;
    }



}
