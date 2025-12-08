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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTestdaten;
import de.meetingapps.meetingportal.meetComBlMBackground.BlMbDatenmanipulation;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComHVParam.ParamBasis;
import de.meetingapps.meetingportal.meetComHVParam.ParamNummernkreise;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDownloadDatei;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UDatenmanipulation {

    private int logDrucken = 10;

    private @Inject EclUserLoginM eclUserLoginM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject EclDbM eclDbM;
    private @Inject USession uSession;
    private @Inject UReportsSession uReportsSession;
    private @Inject UDatenmanipulationSession uDatenmanipulationSession;
    private @Inject EclParamM eclParamM;

    private @Inject BlMbDatenmanipulation blMbDatenmanipulation;


    /**Start-Button**********/
    public String doStart() {
        if (!xSessionVerwaltung.pruefeUStart("uDatenmanipulationStart", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        switch (uDatenmanipulationSession.getNummer()) {
        
        
        case 1://Zurücksetzen Mandant
            zuruecksetzenMandant();
            break;

        case 2://Löschen Meldedaten und HV-daten
            loeschenMeldebestandHV();
            break;

        case 3://Löschen Abstimmungsagenda
            loeschenAbstimmungsagenda();
            break;
            
        case 4://Löschen Aktienregister*/
            loeschenAktienregister();
            break;


        case 5://Zurücksetzen Hinweise-gelesen
            zuruecksetzenHinweiseGelesen();
            break;

        case 6://Rücksetzen Hinweis Elektr.Einladungs-Versand
            zuruecksetzenHinweisElekVersand();
            break;

        case 7://Zurücksetzen Passwort
            if (zuruecksetzenPasswortPruefen()==false) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            zuruecksetzenPasswort();
            break;

        case 8://Zusätzliches Initialpasswort
            zusaetzlichesInitialpasswort();
            break;
            
        case 9:
        case 10://Aktionäre Anmelden ohne/mit EK*
            aktionaereAnmelden();
            break;
            
        case 11://ku217 Vertreterdaten vorbereiten
            ku217VertreterVorbereiten();
            break;
            
        case 12://ku217 Abstimmende präsent setzen
            if (ku217AbstimmendePraesentSetzenPruefen()==false) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            ku217AbstimmendePraesentSetzen();
            break;
            
        case 13://Anlegen Testbestand
            if (testbestandAnlegenPruefen()==false) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            testbestandAnlegen();
            break;

        case 14://Standard-Sammelkarten anlegen
            sammelkartenAnlegen();
            break;
            
        case 15://Mandanten - Aktionärsdaten löschen
            if (mandantenAktionaersdatenLoeschenPruefen()==false) {
                xSessionVerwaltung.setzeUEnde();
                return "";
            }
            mandantenAktionaersdatenLoeschen();
            break;
            
        case 16://Aktionärsdaten zurücksetzen
            demoAktionaereZuruecksetzen();
            break;
            
        case 17://Mandantennummer ändern
            mandantennummerAendern();
            break;

       }
  
        
        pruefenObFertig();

        return xSessionVerwaltung.setzeUEnde("uDatenmanipulationAnzeige", true, false, eclUserLoginM.getKennung());
    }


    /**Refresh für Background-Reports*/
    public void doRefresh() {
        if (!xSessionVerwaltung.pruefeUStart("uDatenmanipulationAnzeige", "")) {
            xSessionVerwaltung.setzeUEnde();
            return;
        }
        pruefenObFertig();
        
        xSessionVerwaltung.setzeUEnde();
        return;
    }
    
    private void pruefenObFertig() {
        String ergebnis="";
        switch (uDatenmanipulationSession.getNummer()) {
        case 1:/*Zurücksetzen Mandant*/
        case 2:/*Meldebestand und HV*/
        case 3:/*Löschen Abstimmungsagenda*/
        case 4:/*Aktienregister*/
        case 5://Zurücksetzen Hinweise-gelesen
        case 6://Rücksetzen Hinweis Elektr.Einladungs-Versand
        case 7://Zurücksetzen Passwort
        case 8://Zusätzliches Initialpasswort
        case 9:
        case 10://Aktionäre Anmelden ohne/mit EK*
        case 11://ku217 Vertreterdaten vorbereiten
        case 12://ku217 Abstimmende präsent setzen
        case 13://Testbestand anlegen
        case 14://Standard-Sammelkarten anlegen
        case 15://Mandanten - Aktionärsdaten löschen
        case 16://Demo-Aktionäre zurücksetzen
        case 17://Mandantennummer ändern
            Future<String> futureRC=uDatenmanipulationSession.getFutureRC();
            try {
                ergebnis=futureRC.get(15, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
            }
     
            break;
        }
        if (!ergebnis.isEmpty()) {
            CaBug.druckeLog("ergebnis="+ergebnis, logDrucken, 10);
            uDatenmanipulationSession.setStehtZurVerfuegung(true);
            uDatenmanipulationSession.setAnzeigeText(ergebnis);
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

    public void doDruck() {
        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(uReportsSession.getDruckDateiname());
    }

    
    
    
    /*****************************Datenmanipulation********************************/
    
    public void zuruecksetzenMandantStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(1);
        uDatenmanipulationSession.setUeberschrift("Zurücksetzen Mandant");
    }

    public void zuruecksetzenMandant() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.zuruecksetzenMandant(lDbBundle);

        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    
    
    public void loeschenMeldebestandHVStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(2);
        uDatenmanipulationSession.setUeberschrift("Löschen Meldebestand und HV-Daten");
    }

    public void loeschenMeldebestandHV() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.loescheMeldebestandHV(lDbBundle);

        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }


    public void loeschenAktienregisterStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(4);
        uDatenmanipulationSession.setUeberschrift("Löschen Aktienregister");
    }

    public void loeschenAktienregister() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.loescheAktienregister(lDbBundle);

        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    public void loeschenAbstimmungsagendaStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(3);
        uDatenmanipulationSession.setUeberschrift("Löschen Abstimmungsagenda");
    }

    public void loeschenAbstimmungsagenda() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.loescheAbstimmungsagenda(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }
   
    
    /******************************Zurücksetzen Hinweise********************************************************/
    
    public void zuruecksetzenHinweiseGelesenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(5);
        uDatenmanipulationSession.setUeberschrift("Zurücksetzen Hinweise-gelesen");
    }

    public void zuruecksetzenHinweiseGelesen() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        boolean hinweisAktionaersPortalBestaetigtZuruecksetzen=uDatenmanipulationSession.isRuecksetzenHinweisAktionaersPortal();
        boolean hinweisHVPortalBestaetigtZuruecksetzen=uDatenmanipulationSession.isRuecksetzenHinweisHVPortal();
        int hinweisWeitereBestaetigtZuruecksetzen=0;
        if (uDatenmanipulationSession.isRuecksetzenPermPortal1()) {
            hinweisWeitereBestaetigtZuruecksetzen=(hinweisWeitereBestaetigtZuruecksetzen | 1);
        }
        if (uDatenmanipulationSession.isRuecksetzenPermPortal2()) {
            hinweisWeitereBestaetigtZuruecksetzen=(hinweisWeitereBestaetigtZuruecksetzen | 2);
        }
        
        Future<String> future=blMbDatenmanipulation.zuruecksetzenHinweiseGelesen(lDbBundle, 
                hinweisAktionaersPortalBestaetigtZuruecksetzen, hinweisHVPortalBestaetigtZuruecksetzen,
                hinweisWeitereBestaetigtZuruecksetzen
                );
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    
    public void zuruecksetzenHinweisElekVersandStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(6);
        uDatenmanipulationSession.setUeberschrift("Rücksetzen Hinweis Elektr.Einladungs-Versand");
    }

    public void zuruecksetzenHinweisElekVersand() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.zuruecksetzenHinweisElekVersand(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    
    /*************************Zurücksetzen Passwort*********************************/
    public void zuruecksetzenPasswortStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(7);
        uDatenmanipulationSession.setUeberschrift("Zurücksetzen Passwort");
        uDatenmanipulationSession.setInitialPasswortStandard(eclParamM.getParamServer().portalInitialPasswortTestBestand);
        }

    public boolean zuruecksetzenPasswortPruefen() {
        if (uDatenmanipulationSession.getInitialPasswortArt().equals("3") &&
                uDatenmanipulationSession.getInitialPasswortSonstiges().trim().isEmpty()) {
            uSession.setFehlermeldung("Bitte gewünschtes Passwort unter 'Sonstiges Passwort' eingeben");
            return false;
        }
        return true;
    }
    
    public void zuruecksetzenPasswort() {
        String lPasswort="";
        switch (uDatenmanipulationSession.getInitialPasswortArt()) {
        case "1":lPasswort="PW";break;
        case "2":lPasswort=uDatenmanipulationSession.getInitialPasswortStandard();break;
        case "3":lPasswort=uDatenmanipulationSession.getInitialPasswortSonstiges().trim();break;
        }
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.zuruecksetzenPasswort(lDbBundle, lPasswort);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    /*************************Zusätzliches Initialpasswort*********************************/
    public void zusaetzlichesInitialpasswortStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(8);
        uDatenmanipulationSession.setUeberschrift("Zusätzliches Initialpasswort");
    }

    
    public void zusaetzlichesInitialpasswort() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.zusaetzlichesInitialpasswort(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    /*************************Aktionäre Anmelden ohne/mit EK*********************************/
    public void aktionaereAnmeldenStart(boolean pMitEK) {
        uDatenmanipulationSession.clear();
        if (pMitEK) {
            uDatenmanipulationSession.setNummer(10);
            uDatenmanipulationSession.setUeberschrift("Aktionäre Anmelden mit EK");
         }
        else {
            uDatenmanipulationSession.setNummer(9);
            uDatenmanipulationSession.setUeberschrift("Aktionäre Anmelden ohne EK");
        }
    }

    
    public void aktionaereAnmelden() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        boolean mitEK=false;
        boolean ekWieAktionaersnummer=false;
        boolean ekWieAKtionaersnummerOhneLetzteZiffer=false;
        if (uDatenmanipulationSession.getNummer()==10) {
            mitEK=true;
            ekWieAktionaersnummer=uDatenmanipulationSession.isEintrittskarteWieAktionaersnummer();
            ekWieAKtionaersnummerOhneLetzteZiffer=uDatenmanipulationSession.isEintrittskarteWieAktionaersnummerOhneLetzteZiffer();
        }
        Future<String> future=blMbDatenmanipulation.aktionaereAnmelden(lDbBundle, mitEK, ekWieAktionaersnummer, ekWieAKtionaersnummerOhneLetzteZiffer);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }


    /*************************ku217 Vertreterdaten vorbereiten*********************************/
    public void ku217VertreterVorbereitenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(11);
        uDatenmanipulationSession.setUeberschrift("ku217 Vertreterdaten vorbereiten");
    }

    
    public void ku217VertreterVorbereiten() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.ku217VertreterVorbereiten(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    /*************************ku217 Abstimmende präsent setzen*********************************/
    public void ku217AbstimmendePraesentSetzenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(12);
        uDatenmanipulationSession.setUeberschrift("ku217 Abstimmende präsent setzen");
    }

    public boolean ku217AbstimmendePraesentSetzenPruefen() {
        String hku217Abstimmvorgang=uDatenmanipulationSession.getku217Abstimmvorgang().trim();
        if (hku217Abstimmvorgang.isEmpty() || !CaString.isNummern(hku217Abstimmvorgang)) {
            uSession.setFehlermeldung("Bitte gültige Nummer Abstimmungsvorgang eingeben");
            return false;
        }
        
        return true;
    }

    public void ku217AbstimmendePraesentSetzen() {
        
        int ku217Abstimmvorgang=Integer.parseInt(uDatenmanipulationSession.getku217Abstimmvorgang().trim());
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.ku217AbstimmendePraesentSetzen(lDbBundle, ku217Abstimmvorgang);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    
    /*************************Mandanten - Aktionärsdaten löschen*********************************/
    public void mandantenAktionaersdatenLoeschenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(15);
        uDatenmanipulationSession.setUeberschrift("Mandanten Aktionärsdaten löschen");
    }

    public boolean mandantenAktionaersdatenLoeschenPruefen() {
        String hDatum=uDatenmanipulationSession.getLoeschenBisDatum();
        if (!hDatum.isEmpty() && CaString.isDatum(hDatum)==false) {
            uSession.setFehlermeldung("Bitte gültiges Datum eingeben");
            return false;
        }
         
        return true;
    }

    public void mandantenAktionaersdatenLoeschen() {
        String hDatum=uDatenmanipulationSession.getLoeschenBisDatum();

        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.mandantenAktionaersdatenLoeschen(lDbBundle, hDatum);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    /*************************Testbestand anlegen*********************************/
    public void testbestandAnlegenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(13);
        uDatenmanipulationSession.setUeberschrift("Testbestand anlegen");

        uDatenmanipulationSession.setInitialPasswortStandard(eclParamM.getParamServer().portalInitialPasswortTestBestand);

        BvTestdaten bvTestdaten=new BvTestdaten();
        String startText=bvTestdaten.beschreibung;
        uDatenmanipulationSession.setStartText(startText);
        
        List<String> startZeilen=new LinkedList<String>();

        ParamBasis paramBasis=eclParamM.getParam().paramBasis;
        ParamNummernkreise paramNummernkreise=eclParamM.getParam().paramNummernkreise;

        String hString="";
        
        if (paramBasis.namensaktienAktiv) {
            CaBug.druckeLog("Namensaktien aktiv", logDrucken, 10);
            hString="Namensaktien aktiv";
            startZeilen.add(hString);
            
            int laengeAktionaersnummer=paramBasis.laengeAktionaersnummer;
            hString="Namensaktien: Länge Aktionärsnummer "+Integer.toString(laengeAktionaersnummer);
            startZeilen.add(hString);
        }
        if (paramBasis.inhaberaktienAktiv) {
            CaBug.druckeLog("Inhaberaktien aktiv", logDrucken, 10);
            hString="Inhaberaktien aktiv";
            startZeilen.add(hString);
        }
        
        for (int i=1;i<=5;i++) {
            if (paramBasis.getGattungAktiv(i)) {
                hString=Integer.toString(i)+". Gattung "+eclParamM.getParam().paramBasis.getGattungBezeichnungKurz(i)+" ";
                
                hString+="Nummernkreis von EK "+paramNummernkreise.vonSubEintrittskartennummer[i][2];
                hString+=" bis EK "+paramNummernkreise.bisSubEintrittskartennummer[i][2];
                startZeilen.add(hString);
            }
         }
        
        uDatenmanipulationSession.setStartZeilen(startZeilen);
    }

    public boolean testbestandAnlegenPruefen() {
        String hAnzahlAktionaereJeGattung=uDatenmanipulationSession.getAnzahlAktionaereJeGattung().trim();
        if (hAnzahlAktionaereJeGattung.isEmpty() || !CaString.isNummern(hAnzahlAktionaereJeGattung)) {
            uSession.setFehlermeldung("Bitte gültige Zahl in Anzahl Aktionäre eingeben");
            return false;
        }
        
        int anzahlAktionaereJeGattung=Integer.parseInt(hAnzahlAktionaereJeGattung);
        
        ParamBasis paramBasis=eclParamM.getParam().paramBasis;
        ParamNummernkreise paramNummernkreise=eclParamM.getParam().paramNummernkreise;
        for (int i=1;i<=5;i++) {
            if (paramBasis.getGattungAktiv(i)) {
                if (paramNummernkreise.bisSubEintrittskartennummer[i][2]-paramNummernkreise.vonSubEintrittskartennummer[i][2]<=anzahlAktionaereJeGattung) {
                    uSession.setFehlermeldung("Gattung "+i+": Nummernkreis nicht groß genug");
                    return false;
                }
            }
         }
        
        if (uDatenmanipulationSession.getInitialPasswortArt().equals("3") &&
                uDatenmanipulationSession.getInitialPasswortSonstiges().trim().isEmpty()) {
            uSession.setFehlermeldung("Bitte gewünschtes Passwort unter 'Sonstiges Passwort' eingeben");
            return false;
        }
        return true;
    }
    
    public void testbestandAnlegen() {
        
        String lPasswort="";
        switch (uDatenmanipulationSession.getInitialPasswortArt()) {
        case "1":lPasswort="PW";break;
        case "2":lPasswort=uDatenmanipulationSession.getInitialPasswortStandard();break;
        case "3":lPasswort=uDatenmanipulationSession.getInitialPasswortSonstiges().trim();break;
        }
        
        String hAnzahlAktionaereJeGattung=uDatenmanipulationSession.getAnzahlAktionaereJeGattung().trim();
        int anzahlAktionaereJeGattung=Integer.parseInt(hAnzahlAktionaereJeGattung);
        
        boolean gruppenVerteilen=uDatenmanipulationSession.isGruppenVerteilen();
        boolean ergaenzungAnlegen=uDatenmanipulationSession.isErgaenzungAnlegen();
        
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.testBestandAnlegen(lDbBundle, lPasswort, anzahlAktionaereJeGattung,
                gruppenVerteilen, ergaenzungAnlegen);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

    /*************************Standard-Sammelkarten anlegen*********************************/
    public void sammelkartenAnlegenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(14);
        uDatenmanipulationSession.setUeberschrift("Standard-Sammelkarten anlegen");
    }

    
    public void sammelkartenAnlegen() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.sammelkartenAnlegen(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

   
    /*************************DemoUser zurücksetzen*********************************/
    public void demoAktionaereZuruecksetzenStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(16);
        uDatenmanipulationSession.setUeberschrift("Demo-Aktionäre zurücksetzen");
    }

    
    public void demoAktionaereZuruecksetzen() {
        String neuesPasswort=uDatenmanipulationSession.getDemoPasswort().trim();
        eclDbM.openAll();
        if (!neuesPasswort.isEmpty()) {
            eclDbM.getDbBundle().dbLoginDatenDemo.updatePasswort(neuesPasswort);
        }
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.demoAktionaereZuruecksetzen(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }


    /*************************Mandantennummer ändern********************************/
    public void mandantennummerAendernStart() {
        uDatenmanipulationSession.clear();
        uDatenmanipulationSession.setNummer(17);
        uDatenmanipulationSession.setUeberschrift("Mandantennummer in Tables ändern");
    }

    
    public void mandantennummerAendern() {
        eclDbM.openAll();
        DbBundle lDbBundle=eclDbM.getDbBundle();
        eclDbM.closeAll();

        Future<String> future=blMbDatenmanipulation.mandantennummerAendern(lDbBundle);
        uDatenmanipulationSession.setFutureRC(future);

        uDatenmanipulationSession.setStehtZurVerfuegung(false);
    }

}
