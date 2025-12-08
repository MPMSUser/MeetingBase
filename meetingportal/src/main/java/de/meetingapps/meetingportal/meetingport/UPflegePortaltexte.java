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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTexte;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteEditM;
import de.meetingapps.meetingportal.meetingport.UPflegePortaltexteSession.ErgebnisElement;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPflegePortaltexte {

    private int logDrucken=10;
    

    private @Inject UPflegePortaltexteSession uPflegePortaltexteSession;
    private @Inject EclParamM eclParamM;
    private @Inject XSessionVerwaltung xSessionVerwaltung;
    private @Inject USession uSession;
    private @Inject EclDbM eclDbM;
    private @Inject EclPortalTexteEditM eclPortalTexteEditM;

    public void init(int standardOderMandant) {
        
        if (standardOderMandant==1) {
            uPflegePortaltexteSession.setMandantenSpezifisch(false);
            uPflegePortaltexteSession.setUeberschrift("Portaltexte Standard");
        }
        else {
            /*Mandanten-Texte*/
            uPflegePortaltexteSession.setMandantenSpezifisch(true);
            uPflegePortaltexteSession.setUeberschrift("Portaltexte Mandant");
        }
        uPflegePortaltexteSession.setBasisSet(Integer.toString(eclParamM.getParam().paramPortal.basisSetStandardTexteVerwenden));
        uPflegePortaltexteSession.setBasisSetVoreingestellt(Integer.toString(eclParamM.getParam().paramPortal.basisSetStandardTexteVerwenden));
        
        geheInStatusNummerEingeben();
        initSuche();
        initSuchergebnis();
    }
    
    private void initSuche() {
        uPflegePortaltexteSession.setSuchenBegriff("");
        uPflegePortaltexteSession.setSuchenDeutsch(false);
        uPflegePortaltexteSession.setSuchenEnglisch(false);
        uPflegePortaltexteSession.setSuchenPortal(false);
        uPflegePortaltexteSession.setSuchenApp(false);
        uPflegePortaltexteSession.setSuchenMandant(false);
        uPflegePortaltexteSession.setSuchenStandard(false);
    }
    
    private void initSuchergebnis() {
        uPflegePortaltexteSession.setErgebnisAnzeigen(false);
        uPflegePortaltexteSession.setErgebnisListe(new LinkedList<ErgebnisElement>());
    }
    
    /**Vorbereiten der Felder für das Eingeben einer neuen Text-Nummer*/
    private void geheInStatusNummerEingeben() {
        uPflegePortaltexteSession.setModus(0);
        
        uPflegePortaltexteSession.setTextnummer("");
        uPflegePortaltexteSession.setAppSeite("");
        uPflegePortaltexteSession.setAppIdent("");
    }
    
    /**Vorbereiten der Felder für das Anzeigen/Eingeben eines Textes*/
    private void geheInStatusTextEingeben() {
        uPflegePortaltexteSession.setModus(1);
    }
    
    /**Einlesen über Text-Nr*/
    public String doTextNrBearbeiten() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (CaString.isNummern(uPflegePortaltexteSession.getBasisSet().trim())==false){
            uSession.setFehlermeldung("Gültige Basis-Set-Nummer eingeben");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        if (CaString.isNummern(uPflegePortaltexteSession.getTextnummer().trim())==false){
            uSession.setFehlermeldung("Gültige Text-Nummer eingeben");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
         
        
        eclDbM.openAll();
        int iText = Integer.parseInt(uPflegePortaltexteSession.getTextnummer().trim());
        int pRelease = Integer.parseInt(uPflegePortaltexteSession.getBasisSet().trim());
        BvTexte bvTexte=new BvTexte();
        bvTexte.leseEinzelnenPortalTextFuerBearbeitung(iText, eclDbM.getDbBundle(), uPflegePortaltexteSession.isMandantenSpezifisch(), false, pRelease);
        eclDbM.closeAll();
        
        uPflegePortaltexteSession.setSeitenName(bvTexte.rcSeitenName[0]);
        uPflegePortaltexteSession.setBeschreibung(bvTexte.rcBeschreibung[0]);
 
        uPflegePortaltexteSession.setVerbundenMitIdentGesamt(Integer.toString(bvTexte.rcVerbundenMitIdentGesamt[0])); //ok
        uPflegePortaltexteSession.setSeitennummer(Integer.toString(bvTexte.rcSeitennummer[0]));//ok
        uPflegePortaltexteSession.setIdent(Integer.toString(bvTexte.rcIdent[0]));//ok

        uPflegePortaltexteSession.setTextInPortal(bvTexte.rcTextInPortal[0]);//ok
        uPflegePortaltexteSession.setTextInApp(bvTexte.rcTextInApp[0]); //ok

        uPflegePortaltexteSession.setPortalStandardTextDE(bvTexte.rcPortalStandardTextDE[0]);
        uPflegePortaltexteSession.setPortalStandardAdaptivTextDE(bvTexte.rcPortalStandardAdaptivTextDE[0]);
        uPflegePortaltexteSession.setAppStandardTextDE(bvTexte.rcAppStandardTextDE[0]);

        uPflegePortaltexteSession.setPortalStandardTextEN(bvTexte.rcPortalStandardTextEN[0]);
        uPflegePortaltexteSession.setPortalStandardAdaptivTextEN(bvTexte.rcPortalStandardAdaptivTextEN[0]);
        uPflegePortaltexteSession.setAppStandardTextEN(bvTexte.rcAppStandardTextEN[0]);
        
        /*Die beiden folgenden Felder werden nur zwischengespeichert, weil beim Rückspeichern wieder benötigt*/
        uPflegePortaltexteSession.setLetzteVersionDE(bvTexte.rcLetzteVersionDE[0]);
        uPflegePortaltexteSession.setLetzteVersionEN(bvTexte.rcLetzteVersionEN[0]);

        if (uPflegePortaltexteSession.isMandantenSpezifisch()) {

            uPflegePortaltexteSession.setPortalVonStandardVerwendenDE(bvTexte.rcPortalVonStandardVerwendenDE[0]); //ok
            uPflegePortaltexteSession.setPortalTextDE(bvTexte.rcPortalTextDE[0]);//ok

            uPflegePortaltexteSession.setPortalAdaptivAbweichendDE(bvTexte.rcPortalAdaptivAbweichendDE[0]);//ok
            uPflegePortaltexteSession.setPortalAdaptivTextDE(bvTexte.rcPortalAdaptivTextDE[0]);//ok

            uPflegePortaltexteSession.setAppAbweichendDE(bvTexte.rcAppAbweichendDE[0]);//ok
            uPflegePortaltexteSession.setAppTextDE(bvTexte.rcAppTextDE[0]);//ok

            uPflegePortaltexteSession.setPortalVonStandardVerwendenEN(bvTexte.rcPortalVonStandardVerwendenEN[0]);
            uPflegePortaltexteSession.setPortalTextEN(bvTexte.rcPortalTextEN[0]);

            uPflegePortaltexteSession.setPortalAdaptivAbweichendEN(bvTexte.rcPortalAdaptivAbweichendEN[0]);
            uPflegePortaltexteSession.setPortalAdaptivTextEN(bvTexte.rcPortalAdaptivTextEN[0]);

            uPflegePortaltexteSession.setAppAbweichendEN(bvTexte.rcAppAbweichendEN[0]);
            uPflegePortaltexteSession.setAppTextEN(bvTexte.rcAppTextEN[0]);
        }

        else {


            uPflegePortaltexteSession.setPortalVonStandardVerwendenDE(false); //ok
            uPflegePortaltexteSession.setPortalTextDE(bvTexte.rcPortalStandardTextDE[0]);//ok
            
            uPflegePortaltexteSession.setPortalAdaptivAbweichendDE(bvTexte.rcPortalStandardAdaptivAbweichendDE[0]);//ok
            uPflegePortaltexteSession.setPortalAdaptivTextDE(bvTexte.rcPortalStandardAdaptivTextDE[0]);
            
            uPflegePortaltexteSession.setAppAbweichendDE(bvTexte.rcAppStandardAbweichendDE[0]);
            uPflegePortaltexteSession.setAppTextDE(bvTexte.rcAppStandardTextDE[0]);

            
            uPflegePortaltexteSession.setPortalVonStandardVerwendenEN(false);
            uPflegePortaltexteSession.setPortalTextEN(bvTexte.rcPortalStandardTextEN[0]);
                             
            uPflegePortaltexteSession.setPortalAdaptivAbweichendEN(bvTexte.rcPortalStandardAdaptivAbweichendEN[0]);
            uPflegePortaltexteSession.setPortalAdaptivTextEN(bvTexte.rcPortalStandardAdaptivTextEN[0]);
            
            uPflegePortaltexteSession.setAppAbweichendEN(bvTexte.rcAppStandardAbweichendEN[0]);
            uPflegePortaltexteSession.setAppTextEN(bvTexte.rcAppStandardTextEN[0]);
        }

        
        geheInStatusTextEingeben();
        xSessionVerwaltung.setzeUEnde();
        return "";
    }
    
    
    /**Einlesen über App-ident*/
    public String doAppidentBearbeiten() {
        /*XXX*/
        return "";
    }
    
    public String doPortalVonStandardDE(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setPortalTextDE(uPflegePortaltexteSession.getPortalStandardTextDE());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }
    
    public String doAdaptivVonPortalDE(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setPortalAdaptivTextDE(uPflegePortaltexteSession.getPortalTextDE());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }
    
    public String doAppVonPortalDE(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setAppTextDE(uPflegePortaltexteSession.getPortalTextDE());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }

    public String doPortalVonStandardEN(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setPortalTextEN(uPflegePortaltexteSession.getPortalStandardTextEN());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }
    
    public String doAdaptivVonPortalEN(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setPortalAdaptivTextEN(uPflegePortaltexteSession.getPortalTextEN());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }
    
    public String doAppVonPortalEN(){
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegePortaltexteSession.setAppTextEN(uPflegePortaltexteSession.getPortalTextEN());
        xSessionVerwaltung.setzeUEnde();
        return "";
        
    }

    /**Suchfunktion auslösen*/
    public String doSuchen() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        if (CaString.isNummern(uPflegePortaltexteSession.getBasisSet().trim())==false){
            uSession.setFehlermeldung("Gültige Basis-Set-Nummer eingeben");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }

        initSuchergebnis();

        String suchbegriff = uPflegePortaltexteSession.getSuchenBegriff();
        if (suchbegriff.isEmpty()) {
            xSessionVerwaltung.setzeUEnde();
           return "";
        }

        /*Alle Texte in Puffer einlesen. Ist erforderlich, da direktes Suchen in Datenbank wg.
         * Verteilung eines Textes auf mehrere Sätze nicht funktioniert (wenn Such-Wort gerade an
         * Trennstelle ist!) 
         */
        BvTexte bvTexte = new BvTexte();
        eclDbM.openAll();
        int pRelease = Integer.parseInt(uPflegePortaltexteSession.getBasisSet().trim());
        CaBug.druckeLog("suchbegriff="+suchbegriff+" pRelease="+pRelease, logDrucken, 10);
        bvTexte.lesePortalTexteFuerBearbeitung(eclDbM.getDbBundle(), true, false, pRelease);
        eclDbM.closeAll();

        eclPortalTexteEditM.setSeitenName(bvTexte.rcSeitenName);
        eclPortalTexteEditM.setBeschreibung(bvTexte.rcBeschreibung);

        eclPortalTexteEditM.setTextInPortal(bvTexte.rcTextInPortal);
        eclPortalTexteEditM.setTextInApp(bvTexte.rcTextInApp);

        eclPortalTexteEditM.setVerbundenMitIdentGesamt(bvTexte.rcVerbundenMitIdentGesamt);
        eclPortalTexteEditM.setSeitennummer(bvTexte.rcSeitennummer);
        eclPortalTexteEditM.setIdent(bvTexte.rcIdent);

        eclPortalTexteEditM.setPortalStandardTextDE(bvTexte.rcPortalStandardTextDE);
        eclPortalTexteEditM.setPortalStandardAdaptivTextDE(bvTexte.rcPortalStandardAdaptivTextDE);
        eclPortalTexteEditM.setAppStandardTextDE(bvTexte.rcAppStandardTextDE);
        eclPortalTexteEditM.setPortalStandardTextEN(bvTexte.rcPortalStandardTextEN);
        eclPortalTexteEditM.setPortalStandardAdaptivTextEN(bvTexte.rcPortalStandardAdaptivTextEN);
        eclPortalTexteEditM.setAppStandardTextEN(bvTexte.rcAppStandardTextEN);

        eclPortalTexteEditM.setLetzteVersionDE(bvTexte.rcLetzteVersionDE);
        eclPortalTexteEditM.setPortalVonStandardVerwendenDE(bvTexte.rcPortalVonStandardVerwendenDE);
        eclPortalTexteEditM.setPortalTextDE(bvTexte.rcPortalTextDE);
        eclPortalTexteEditM.setPortalAdaptivAbweichendDE(bvTexte.rcPortalAdaptivAbweichendDE);
        eclPortalTexteEditM.setPortalAdaptivTextDE(bvTexte.rcPortalAdaptivTextDE);
        eclPortalTexteEditM.setAppAbweichendDE(bvTexte.rcAppAbweichendDE);
        eclPortalTexteEditM.setAppTextDE(bvTexte.rcAppTextDE);

        eclPortalTexteEditM.setLetzteVersionEN(bvTexte.rcLetzteVersionEN);
        eclPortalTexteEditM.setPortalVonStandardVerwendenEN(bvTexte.rcPortalVonStandardVerwendenEN);
        eclPortalTexteEditM.setPortalTextEN(bvTexte.rcPortalTextEN);
        eclPortalTexteEditM.setPortalAdaptivAbweichendEN(bvTexte.rcPortalAdaptivAbweichendEN);
        eclPortalTexteEditM.setPortalAdaptivTextEN(bvTexte.rcPortalAdaptivTextEN);
        eclPortalTexteEditM.setAppAbweichendEN(bvTexte.rcAppAbweichendEN);
        eclPortalTexteEditM.setAppTextEN(bvTexte.rcAppTextEN);

        /*Such-Funktion ausführen*/
        boolean suchenDeutsch = uPflegePortaltexteSession.isSuchenDeutsch();
        boolean suchenEnglisch = uPflegePortaltexteSession.isSuchenEnglisch();
        boolean suchenPortal = uPflegePortaltexteSession.isSuchenPortal();
        boolean suchenApp = uPflegePortaltexteSession.isSuchenApp();
        boolean suchenMandant = uPflegePortaltexteSession.isSuchenMandant();
        boolean suchenStandard = uPflegePortaltexteSession.isSuchenStandard();

        List<ErgebnisElement> hListe = new LinkedList<ErgebnisElement>();
        uPflegePortaltexteSession.setErgebnisAnzeigen(false);
        /*Wenn Suchen in Mandant: dann werden bei SuchenApp auch diejenigen gefunden, die keinen abweichenden
         * App-Text haben und den Suchbegriff beinhalten.
         * 
         * Bei Suchen nach Standard: werden bei SuchenApp nur rein die separaten App-Texte durchsucht
         */
        for (int i = 1; i <= eclPortalTexteEditM.getPortalTextDE().length - 1; i++) {
            int gef = -1;
            if (suchenDeutsch) {
                if (suchenMandant) {
                    if (eclPortalTexteEditM.getPortalTextDE()[i] != null) {
                        if (suchenPortal) {
                            if (eclPortalTexteEditM.getPortalTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (eclPortalTexteEditM.getPortalAdaptivTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                    if (suchenApp) {
                        if (eclPortalTexteEditM.getAppTextDE()[i] != null) {
                            if (eclPortalTexteEditM.getAppTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (!eclPortalTexteEditM.getAppAbweichendDE()[i]) {
                                if (eclPortalTexteEditM.getPortalTextDE()[i].indexOf(suchbegriff) >= 0) {
                                    gef = 1;
                                }
                            }
                        }
                    }
                }
                if (suchenStandard) {
                    if (eclPortalTexteEditM.getPortalStandardTextDE()[i] != null) {
                        if (suchenPortal) {
                            if (eclPortalTexteEditM.getPortalStandardTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (eclPortalTexteEditM.getPortalStandardAdaptivTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                    if (suchenApp) {
                        if (eclPortalTexteEditM.getAppStandardTextDE()[i] != null) {
                            if (eclPortalTexteEditM.getAppStandardTextDE()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                }
            }
            if (suchenEnglisch) {
                if (suchenMandant) {
                    if (suchenPortal) {
                        if (eclPortalTexteEditM.getPortalTextEN()[i] != null) {
                            if (eclPortalTexteEditM.getPortalTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (eclPortalTexteEditM.getPortalAdaptivTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                    if (suchenApp) {
                        if (eclPortalTexteEditM.getAppTextEN()[i] != null) {
                            if (eclPortalTexteEditM.getAppTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (!eclPortalTexteEditM.getAppAbweichendEN()[i]) {
                                if (eclPortalTexteEditM.getPortalTextEN()[i].indexOf(suchbegriff) >= 0) {
                                    gef = 1;
                                }
                            }
                        }
                    }
                }
                if (suchenStandard) {
                    if (suchenPortal) {
                        if (eclPortalTexteEditM.getPortalStandardTextEN()[i] != null) {
                            if (eclPortalTexteEditM.getPortalStandardTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                            if (eclPortalTexteEditM.getPortalStandardAdaptivTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                    if (suchenApp) {
                        if (eclPortalTexteEditM.getAppStandardTextEN()[i] != null) {
                            if (eclPortalTexteEditM.getAppStandardTextEN()[i].indexOf(suchbegriff) >= 0) {
                                gef = 1;
                            }
                        }
                    }
                }
            }

            if (gef != -1) {
                ErgebnisElement ergebnisElement = uPflegePortaltexteSession.new ErgebnisElement();
                ergebnisElement.setIdent(i);
                ergebnisElement.setSeitenName(eclPortalTexteEditM.getSeitenName()[i]);
                ergebnisElement.setBeschreibung(eclPortalTexteEditM.getBeschreibung()[i]);
                hListe.add(ergebnisElement);
            }

        }

        uPflegePortaltexteSession.setErgebnisListe(hListe);
        if (hListe.size() > 0) {
            uPflegePortaltexteSession.setErgebnisAnzeigen(true);
        }
       
       xSessionVerwaltung.setzeUEnde();
       return "";
    }

    public String doAbbrechen() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        geheInStatusNummerEingeben();
        xSessionVerwaltung.setzeUEnde();
        return "";
     }

    public String doSpeichern() {
        if (!xSessionVerwaltung.pruefeUStart("uPflegePortaltexte", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        
        if (eclParamM.getParamServer().standardPortaltextePflegbar==0)
        {
           if (uPflegePortaltexteSession.isMandantenSpezifisch()==false) {
               uSession.setFehlermeldung("Standardtexte dürfen auf diesem Server nicht bearbeitet werden");
               xSessionVerwaltung.setzeUEnde();
               return "";
               
           }
        }
        
        BvTexte bvTexte = new BvTexte();
        bvTexte.initRCLaengeEins();

        
        String hText = uPflegePortaltexteSession.getTextnummer().trim();
        int iText = Integer.parseInt(hText);

        int pRelease = Integer.parseInt(uPflegePortaltexteSession.getBasisSet().trim());

        String hNummer = uPflegePortaltexteSession.getVerbundenMitIdentGesamt();
        if (CaString.isNummern(hNummer) == false) {
            uSession.setFehlermeldung("Verbunden mit muß numerisch sein");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        bvTexte.rcVerbundenMitIdentGesamt[0] = Integer.parseInt(hNummer);

        hNummer = uPflegePortaltexteSession.getSeitennummer();
        if (CaString.isNummern(hNummer) == false) {
            uSession.setFehlermeldung("Seitennummer App muß numerisch sein");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        bvTexte.rcSeitennummer[0] = Integer.parseInt(hNummer);

        hNummer = uPflegePortaltexteSession.getIdent();
        if (CaString.isNummern(hNummer) == false) {
            uSession.setFehlermeldung("Ident App muß numerisch sein");
            xSessionVerwaltung.setzeUEnde();
            return "";
        }
        bvTexte.rcIdent[0] = Integer.parseInt(hNummer);

        bvTexte.rcTextInPortal[0] = uPflegePortaltexteSession.isTextInPortal();
        bvTexte.rcTextInApp[0] = uPflegePortaltexteSession.isTextInApp();

        bvTexte.rcPortalVonStandardVerwendenDE[0] = uPflegePortaltexteSession.isPortalVonStandardVerwendenDE();
        if (uPflegePortaltexteSession.isPortalVonStandardVerwendenDE()) {
            bvTexte.rcPortalTextDE[0] = "";
            bvTexte.rcPortalAdaptivAbweichendDE[0] = false;
            bvTexte.rcPortalAdaptivTextDE[0] = "";
            bvTexte.rcAppAbweichendDE[0] = false;
            bvTexte.rcAppTextDE[0] = "";
        } else {
            bvTexte.rcPortalTextDE[0] = uPflegePortaltexteSession.getPortalTextDE();

            bvTexte.rcPortalAdaptivAbweichendDE[0] = uPflegePortaltexteSession.isPortalAdaptivAbweichendDE();
            if (uPflegePortaltexteSession.isPortalAdaptivAbweichendDE()) {
                bvTexte.rcPortalAdaptivTextDE[0] = uPflegePortaltexteSession.getPortalAdaptivTextDE();
            } else {
                bvTexte.rcPortalAdaptivTextDE[0] = "";
            }

            bvTexte.rcAppAbweichendDE[0] = uPflegePortaltexteSession.isAppAbweichendDE();
            if (uPflegePortaltexteSession.isAppAbweichendDE()) {
                bvTexte.rcAppTextDE[0] = uPflegePortaltexteSession.getAppTextDE();
            } else {
                bvTexte.rcAppTextDE[0] = "";
            }
        }

        bvTexte.rcPortalVonStandardVerwendenEN[0] = uPflegePortaltexteSession.isPortalVonStandardVerwendenEN();
        if (uPflegePortaltexteSession.isPortalVonStandardVerwendenEN()) {
            bvTexte.rcPortalTextEN[0] = "";
            bvTexte.rcPortalAdaptivAbweichendEN[0] = false;
            bvTexte.rcPortalAdaptivTextEN[0] = "";
            bvTexte.rcAppAbweichendEN[0] = false;
            bvTexte.rcAppTextEN[0] = "";

        } else {
            bvTexte.rcPortalTextEN[0] = uPflegePortaltexteSession.getPortalTextEN();

            bvTexte.rcPortalAdaptivAbweichendEN[0] = uPflegePortaltexteSession.isPortalAdaptivAbweichendEN();
            if (uPflegePortaltexteSession.isPortalAdaptivAbweichendEN()) {
                bvTexte.rcPortalAdaptivTextEN[0] = uPflegePortaltexteSession.getPortalAdaptivTextEN();
            }

            bvTexte.rcAppAbweichendEN[0] = uPflegePortaltexteSession.isAppAbweichendEN();
            if (uPflegePortaltexteSession.isAppAbweichendEN()) {
                bvTexte.rcAppTextEN[0] = uPflegePortaltexteSession.getAppTextEN();
            } else {
                bvTexte.rcAppTextEN[0] = "";
            }
        }

        /*Nun Veränderten Text in Table zurückspeichern*/
        eclDbM.openAll();

        bvTexte.rcSeitenName[0] = uPflegePortaltexteSession.getSeitenName();
        bvTexte.rcBeschreibung[0] = uPflegePortaltexteSession.getBeschreibung();

        bvTexte.rcPortalStandardTextDE[0] = uPflegePortaltexteSession.getPortalStandardTextDE();
        bvTexte.rcPortalStandardAdaptivTextDE[0] = uPflegePortaltexteSession.getPortalStandardAdaptivTextDE();
        bvTexte.rcAppStandardTextDE[0] = uPflegePortaltexteSession.getAppStandardTextDE();

        bvTexte.rcPortalStandardTextEN[0] = uPflegePortaltexteSession.getPortalStandardTextEN();
        bvTexte.rcPortalStandardAdaptivTextEN[0] = uPflegePortaltexteSession.getPortalStandardAdaptivTextEN();
        bvTexte.rcAppStandardTextEN[0] = uPflegePortaltexteSession.getAppStandardTextEN();

        bvTexte.rcLetzteVersionDE = eclPortalTexteEditM.getLetzteVersionDE();
        
        bvTexte.rcLetzteVersionEN = eclPortalTexteEditM.getLetzteVersionEN();
        
        if (uPflegePortaltexteSession.isMandantenSpezifisch()==false) {
            bvTexte.schreibePortalTexteEinzeln(eclDbM.getDbBundle(), iText, false, false, pRelease);
        } else {
            bvTexte.schreibePortalTexteEinzeln(eclDbM.getDbBundle(), iText, true, false, 0);
        }
        eclDbM.closeAll();

        geheInStatusNummerEingeben();
        xSessionVerwaltung.setzeUEnde();
        return "";
     }
 
}
