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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;


@SessionScoped
@Named
public class UDatenmanipulationSession implements Serializable {
    private static final long serialVersionUID = -2292297684292401184L;

    /*Wird immer als Überschrifts-Text angezeigt*/
    private String ueberschrift = "";

    /*Wird bei Report-Start als Infotext unter Überschrift angezeigt*/
    private String startText = "";
    private List<String> startZeilen=new LinkedList<String>();

    /* 
     *  1 =Mandant komplett zurücksetzen
     *  2 =Löschen Meldebestand und HV
     *  3 =Löschen Abstimmungsagenda
     *  4 =Löschen Aktienregister
     *  
     *  5 =Zurücksetzen "Hinweise-gelesen"
     *  6 =Rücksetzen "Hinweis Elektronischer Einladungsversand"
     *  7 =Passwort zurücksetzen
     *  
     *  8 =Zusätzliches Initialpasswort
     *  9 =Aktionäre Anmelden ohne EK
     *  10=Aktionäre Anmelden mit EK
     *  11=ku217 Vertreterdaten vorbereiten
     *  12=ku217 Abstimmende präsent setzen
     *  
     *  13=Test-Bestand anlegen
     *  14=Standard-Sammelkarten anlegen
     *  
     *  15=Mandanten Aktionärsdaten löschen
     *  
     *  16=Demo-Aktionäre zurücksetzen
     *  
     *  17=Mandantennummer in Tables ändern
     * */
    private int nummer = 0;

    /*Für Background-Reports: wenn stehtZurVerfuegung=false, dann Refresh aufrufen*/
    private boolean stehtZurVerfuegung = false;
    private Future<String> futureRC=null;

    /*Fehlermeldung*/
    private String fehlermeldung = "";

    /********Für Ergebnis-Anzeige************/
    private String anzeigeText = "";

    
    /***********Hinweise zurücksetzen************/
    
    private boolean ruecksetzenHinweisAktionaersPortal=false;
    private boolean ruecksetzenHinweisHVPortal=false;
    private boolean ruecksetzenPermPortal1=false;
    private boolean ruecksetzenPermPortal2=false;
    
    /**************Passwort*********************************/
    private String initialPasswortVorschlag="Freig@be2o23!";
    private String initialPasswortZusatzVorschlag=""; /* (kleines oh bei 2023 statt Null) */
    private String initialPasswortStandard="";
    private String initialPasswortStandardZusatztext="";
    
    /*1=PW, 2=Standard (siehe oben), 3=Sonstiges)*/
    private String initialPasswortArt="1";
    private String initialPasswortSonstiges="";
    
    
    /************Testbestand anlegen*************/
    private String anzahlAktionaereJeGattung="200";
    private boolean gruppenVerteilen=false;
    private boolean ergaenzungAnlegen=false;
    
    
    /**************Eintrittskarten ergänzen**************/
    private boolean eintrittskarteWieAktionaersnummer=false;
    private boolean eintrittskarteWieAktionaersnummerOhneLetzteZiffer=false;
       
    /***************ku217***************************/
    private String ku217Abstimmvorgang="";
    
    /***************Mandanten - Aktionärsdaten löschen***************/
    private String loeschenBisDatum="";
    
    
    /**************Demo-Aktionäre zurücksetzen*****************/
    private String demoPasswort="";
    
    /*XXX*/
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

    /*Gäste*/
    /*1=Übersicht, 2=Gäste-Präsenz*/
    private String gaestelisteArt="1";
    /*1=ohne Passwort, 2=mit Passwort*/
    private boolean gaestelistePasswoerter=false;
    /*1=nach Zutrittsnummer, 2=nach Name*/
    private String gaestelisteSortierung="1";
    /*1=alle, 2=alle gerade präsenten, 3=alle präsenten oder präsent gewesenen*/
    private String gaestelisteSelektion="1";
    
    
    

    public void clear() {
        ueberschrift = "";
        startText = "";
        startZeilen=new LinkedList<String>();
        
        nummer=0;
        
        stehtZurVerfuegung = false;
        fehlermeldung="";
        
        anzeigeText="";
        
        ruecksetzenHinweisAktionaersPortal=false;
        ruecksetzenHinweisHVPortal=false;
        ruecksetzenPermPortal1=false;
        ruecksetzenPermPortal2=false;
        
        initialPasswortStandard=initialPasswortVorschlag;
        initialPasswortStandardZusatztext=initialPasswortZusatzVorschlag;
        
        initialPasswortArt="1";
        initialPasswortSonstiges="";
        
        anzahlAktionaereJeGattung="200";
        gruppenVerteilen=false;
        ergaenzungAnlegen=false;
        
        eintrittskarteWieAktionaersnummer=false;
        eintrittskarteWieAktionaersnummerOhneLetzteZiffer=false;
        
        ku217Abstimmvorgang="";
        
        demoPasswort="";
     }


    /**Liefert 01, wenn nichts eingegeben*/
    public String liefereReportVariante() {
        if (reportVariante.isEmpty()) {
            return "01";
        }
        return reportVariante;
    }
    
    /**********************Standard getter und setter*****************************************/


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

 
    public String getFehlermeldung() {
        return fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
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

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public Future<String> getFutureRC() {
        return futureRC;
    }

    public void setFutureRC(Future<String> futureRC) {
        this.futureRC = futureRC;
    }

 
    public List<String> getStartZeilen() {
        return startZeilen;
    }

    public void setStartZeilen(List<String> startZeilen) {
        this.startZeilen = startZeilen;
    }

    public boolean isRuecksetzenHinweisAktionaersPortal() {
        return ruecksetzenHinweisAktionaersPortal;
    }

    public void setRuecksetzenHinweisAktionaersPortal(boolean ruecksetzenHinweisAktionaersPortal) {
        this.ruecksetzenHinweisAktionaersPortal = ruecksetzenHinweisAktionaersPortal;
    }

    public boolean isRuecksetzenHinweisHVPortal() {
        return ruecksetzenHinweisHVPortal;
    }

    public void setRuecksetzenHinweisHVPortal(boolean ruecksetzenHinweisHVPortal) {
        this.ruecksetzenHinweisHVPortal = ruecksetzenHinweisHVPortal;
    }

    public boolean isRuecksetzenPermPortal1() {
        return ruecksetzenPermPortal1;
    }

    public void setRuecksetzenPermPortal1(boolean ruecksetzenPermPortal1) {
        this.ruecksetzenPermPortal1 = ruecksetzenPermPortal1;
    }

    public boolean isRuecksetzenPermPortal2() {
        return ruecksetzenPermPortal2;
    }

    public void setRuecksetzenPermPortal2(boolean ruecksetzenPermPortal2) {
        this.ruecksetzenPermPortal2 = ruecksetzenPermPortal2;
    }

    public String getInitialPasswortStandard() {
        return initialPasswortStandard;
    }

    public void setInitialPasswortStandard(String initialPasswortStandard) {
        this.initialPasswortStandard = initialPasswortStandard;
    }

    public String getInitialPasswortStandardZusatztext() {
        return initialPasswortStandardZusatztext;
    }

    public void setInitialPasswortStandardZusatztext(String initialPasswortStandardZusatztext) {
        this.initialPasswortStandardZusatztext = initialPasswortStandardZusatztext;
    }

    public String getInitialPasswortArt() {
        return initialPasswortArt;
    }

    public void setInitialPasswortArt(String initialPasswortArt) {
        this.initialPasswortArt = initialPasswortArt;
    }

    public String getInitialPasswortSonstiges() {
        return initialPasswortSonstiges;
    }

    public void setInitialPasswortSonstiges(String initialPasswortSonstiges) {
        this.initialPasswortSonstiges = initialPasswortSonstiges;
    }

    public String getAnzahlAktionaereJeGattung() {
        return anzahlAktionaereJeGattung;
    }

    public void setAnzahlAktionaereJeGattung(String anzahlAktionaereJeGattung) {
        this.anzahlAktionaereJeGattung = anzahlAktionaereJeGattung;
    }

    public boolean isErgaenzungAnlegen() {
        return ergaenzungAnlegen;
    }

    public void setErgaenzungAnlegen(boolean ergaenzungAnlegen) {
        this.ergaenzungAnlegen = ergaenzungAnlegen;
    }

    public boolean isGruppenVerteilen() {
        return gruppenVerteilen;
    }

    public void setGruppenVerteilen(boolean gruppenVerteilen) {
        this.gruppenVerteilen = gruppenVerteilen;
    }


    public String getku217Abstimmvorgang() {
        return ku217Abstimmvorgang;
    }


    public void setku217Abstimmvorgang(String ku217Abstimmvorgang) {
        this.ku217Abstimmvorgang = ku217Abstimmvorgang;
    }


    public String getLoeschenBisDatum() {
        return loeschenBisDatum;
    }


    public void setLoeschenBisDatum(String loeschenBisDatum) {
        this.loeschenBisDatum = loeschenBisDatum;
    }


    public boolean isEintrittskarteWieAktionaersnummer() {
        return eintrittskarteWieAktionaersnummer;
    }


    public void setEintrittskarteWieAktionaersnummer(boolean eintrittskarteWieAktionaersnummer) {
        this.eintrittskarteWieAktionaersnummer = eintrittskarteWieAktionaersnummer;
    }


    public boolean isEintrittskarteWieAktionaersnummerOhneLetzteZiffer() {
        return eintrittskarteWieAktionaersnummerOhneLetzteZiffer;
    }


    public void setEintrittskarteWieAktionaersnummerOhneLetzteZiffer(boolean eintrittskarteWieAktionaersnummerOhneLetzteZiffer) {
        this.eintrittskarteWieAktionaersnummerOhneLetzteZiffer = eintrittskarteWieAktionaersnummerOhneLetzteZiffer;
    }


    public String getDemoPasswort() {
        return demoPasswort;
    }


    public void setDemoPasswort(String demoPasswort) {
        this.demoPasswort = demoPasswort;
    }

 
}
