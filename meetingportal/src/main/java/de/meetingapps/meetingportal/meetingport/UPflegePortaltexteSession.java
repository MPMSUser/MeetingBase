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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;


@SessionScoped
@Named
public class UPflegePortaltexteSession implements Serializable {
    private static final long serialVersionUID = 1433218565587646357L;

    /**0=Anzeigen, 1=Bearbeiten*/
    private int modus=0;
    
    private boolean mandantenSpezifisch=false;
    private String ueberschrift="";
    

    /***********************Key-Felder**************************/
    private String basisSet="";
    private String basisSetVoreingestellt="";
    
    private String textnummer = "";

    private String appSeite = "";
    private String appIdent = "";

    /*********Such-Felder*****/
    private String suchenBegriff = "";
    private boolean suchenDeutsch = false;
    private boolean suchenEnglisch = false;
    private boolean suchenPortal = false;
    private boolean suchenApp = false;
    private boolean suchenMandant = false;
    private boolean suchenStandard = false;

    /******Such-Ergebnis*****/
    private boolean ergebnisAnzeigen = false;
    private List<ErgebnisElement> ergebnisListe = null;

    public class ErgebnisElement implements Serializable {
        private static final long serialVersionUID = -5226884332069419624L;

        private int ident = 0;
        private String seitenName = "";
        private String beschreibung = "";

        public int getIdent() {
            return ident;
        }

        public void setIdent(int ident) {
            this.ident = ident;
        }

        public String getSeitenName() {
            return seitenName;
        }

        public void setSeitenName(String seitenName) {
            this.seitenName = seitenName;
        }

        public String getBeschreibung() {
            return beschreibung;
        }

        public void setBeschreibung(String beschreibung) {
            this.beschreibung = beschreibung;
        }

    }

    /******************Anzeigebereich***********************/
    
    private String seitenName = "";
    private String beschreibung = "";

    private boolean textInPortal = false;

    private boolean textInApp = false;

    private String verbundenMitIdentGesamt = "";

    private String seitennummer = "";
    private String ident = "";
 
    private int letzteVersionDE=0;
    private int letzteVersionEN=0;
    
    /**+++++++++++++++Standard-Texte++++++++++++++++++++*/
    private String portalStandardTextDE = "";
    private String portalStandardAdaptivTextDE = "";
    private String appStandardTextDE = "";

    private String portalStandardTextEN = "";
    private String portalStandardAdaptivTextEN = "";
    private String appStandardTextEN = "";

    /**********************Deutsch*************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    //  private int letzteVersionDE=null;

    private boolean portalVonStandardVerwendenDE = false;
    private String portalTextDE = "";

    private boolean portalAdaptivAbweichendDE = false;
    private String portalAdaptivTextDE = "";

    private boolean appAbweichendDE = false;
    private String appTextDE = "";

    /***********************Englisch*****************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    //  private int letzteVersionEN=null;

    private boolean portalVonStandardVerwendenEN = false;
    private String portalTextEN = "";

    private boolean portalAdaptivAbweichendEN = false;
    private String portalAdaptivTextEN = "";

    private boolean appAbweichendEN = false;
    private String appTextEN = "";

    /*************************Standard getter und setter*************************/

    public int getModus() {
        return modus;
    }

    public void setModus(int modus) {
        this.modus = modus;
    }

    public boolean isMandantenSpezifisch() {
        return mandantenSpezifisch;
    }

    public void setMandantenSpezifisch(boolean mandantenSpezifisch) {
        this.mandantenSpezifisch = mandantenSpezifisch;
    }

    public String getUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getBasisSet() {
        return basisSet;
    }

    public void setBasisSet(String basisSet) {
        this.basisSet = basisSet;
    }

    public String getTextnummer() {
        return textnummer;
    }

    public void setTextnummer(String textnummer) {
        this.textnummer = textnummer;
    }

    public String getAppSeite() {
        return appSeite;
    }

    public void setAppSeite(String appSeite) {
        this.appSeite = appSeite;
    }

    public String getAppIdent() {
        return appIdent;
    }

    public void setAppIdent(String appIdent) {
        this.appIdent = appIdent;
    }

    public String getBasisSetVoreingestellt() {
        return basisSetVoreingestellt;
    }

    public void setBasisSetVoreingestellt(String basisSetVoreingestellt) {
        this.basisSetVoreingestellt = basisSetVoreingestellt;
    }

    public String getSuchenBegriff() {
        return suchenBegriff;
    }

    public void setSuchenBegriff(String suchenBegriff) {
        this.suchenBegriff = suchenBegriff;
    }

    public boolean isSuchenDeutsch() {
        return suchenDeutsch;
    }

    public void setSuchenDeutsch(boolean suchenDeutsch) {
        this.suchenDeutsch = suchenDeutsch;
    }

    public boolean isSuchenEnglisch() {
        return suchenEnglisch;
    }

    public void setSuchenEnglisch(boolean suchenEnglisch) {
        this.suchenEnglisch = suchenEnglisch;
    }

    public boolean isSuchenPortal() {
        return suchenPortal;
    }

    public void setSuchenPortal(boolean suchenPortal) {
        this.suchenPortal = suchenPortal;
    }

    public boolean isSuchenApp() {
        return suchenApp;
    }

    public void setSuchenApp(boolean suchenApp) {
        this.suchenApp = suchenApp;
    }

    public boolean isSuchenMandant() {
        return suchenMandant;
    }

    public void setSuchenMandant(boolean suchenMandant) {
        this.suchenMandant = suchenMandant;
    }

    public boolean isSuchenStandard() {
        return suchenStandard;
    }

    public void setSuchenStandard(boolean suchenStandard) {
        this.suchenStandard = suchenStandard;
    }

    public boolean isErgebnisAnzeigen() {
        return ergebnisAnzeigen;
    }

    public void setErgebnisAnzeigen(boolean ergebnisAnzeigen) {
        this.ergebnisAnzeigen = ergebnisAnzeigen;
    }

    public List<ErgebnisElement> getErgebnisListe() {
        return ergebnisListe;
    }

    public void setErgebnisListe(List<ErgebnisElement> ergebnisListe) {
        this.ergebnisListe = ergebnisListe;
    }

    public String getSeitenName() {
        return seitenName;
    }

    public void setSeitenName(String seitenName) {
        this.seitenName = seitenName;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public boolean isTextInPortal() {
        return textInPortal;
    }

    public void setTextInPortal(boolean textInPortal) {
        this.textInPortal = textInPortal;
    }

    public boolean isTextInApp() {
        return textInApp;
    }

    public void setTextInApp(boolean textInApp) {
        this.textInApp = textInApp;
    }

    public String getVerbundenMitIdentGesamt() {
        return verbundenMitIdentGesamt;
    }

    public void setVerbundenMitIdentGesamt(String verbundenMitIdentGesamt) {
        this.verbundenMitIdentGesamt = verbundenMitIdentGesamt;
    }

    public String getSeitennummer() {
        return seitennummer;
    }

    public void setSeitennummer(String seitennummer) {
        this.seitennummer = seitennummer;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getPortalStandardTextDE() {
        return portalStandardTextDE;
    }

    public void setPortalStandardTextDE(String portalStandardTextDE) {
        this.portalStandardTextDE = portalStandardTextDE;
    }

    public String getPortalStandardAdaptivTextDE() {
        return portalStandardAdaptivTextDE;
    }

    public void setPortalStandardAdaptivTextDE(String portalStandardAdaptivTextDE) {
        this.portalStandardAdaptivTextDE = portalStandardAdaptivTextDE;
    }

    public String getAppStandardTextDE() {
        return appStandardTextDE;
    }

    public void setAppStandardTextDE(String appStandardTextDE) {
        this.appStandardTextDE = appStandardTextDE;
    }

    public String getPortalStandardTextEN() {
        return portalStandardTextEN;
    }

    public void setPortalStandardTextEN(String portalStandardTextEN) {
        this.portalStandardTextEN = portalStandardTextEN;
    }

    public String getPortalStandardAdaptivTextEN() {
        return portalStandardAdaptivTextEN;
    }

    public void setPortalStandardAdaptivTextEN(String portalStandardAdaptivTextEN) {
        this.portalStandardAdaptivTextEN = portalStandardAdaptivTextEN;
    }

    public String getAppStandardTextEN() {
        return appStandardTextEN;
    }

    public void setAppStandardTextEN(String appStandardTextEN) {
        this.appStandardTextEN = appStandardTextEN;
    }

    public boolean isPortalVonStandardVerwendenDE() {
        return portalVonStandardVerwendenDE;
    }

    public void setPortalVonStandardVerwendenDE(boolean portalVonStandardVerwendenDE) {
        this.portalVonStandardVerwendenDE = portalVonStandardVerwendenDE;
    }

    public String getPortalTextDE() {
        return portalTextDE;
    }

    public void setPortalTextDE(String portalTextDE) {
        this.portalTextDE = portalTextDE;
    }

    public boolean isPortalAdaptivAbweichendDE() {
        return portalAdaptivAbweichendDE;
    }

    public void setPortalAdaptivAbweichendDE(boolean portalAdaptivAbweichendDE) {
        this.portalAdaptivAbweichendDE = portalAdaptivAbweichendDE;
    }

    public String getPortalAdaptivTextDE() {
        return portalAdaptivTextDE;
    }

    public void setPortalAdaptivTextDE(String portalAdaptivTextDE) {
        this.portalAdaptivTextDE = portalAdaptivTextDE;
    }

    public boolean isAppAbweichendDE() {
        return appAbweichendDE;
    }

    public void setAppAbweichendDE(boolean appAbweichendDE) {
        this.appAbweichendDE = appAbweichendDE;
    }

    public String getAppTextDE() {
        return appTextDE;
    }

    public void setAppTextDE(String appTextDE) {
        this.appTextDE = appTextDE;
    }

    public boolean isPortalVonStandardVerwendenEN() {
        return portalVonStandardVerwendenEN;
    }

    public void setPortalVonStandardVerwendenEN(boolean portalVonStandardVerwendenEN) {
        this.portalVonStandardVerwendenEN = portalVonStandardVerwendenEN;
    }

    public String getPortalTextEN() {
        return portalTextEN;
    }

    public void setPortalTextEN(String portalTextEN) {
        this.portalTextEN = portalTextEN;
    }

    public boolean isPortalAdaptivAbweichendEN() {
        return portalAdaptivAbweichendEN;
    }

    public void setPortalAdaptivAbweichendEN(boolean portalAdaptivAbweichendEN) {
        this.portalAdaptivAbweichendEN = portalAdaptivAbweichendEN;
    }

    public String getPortalAdaptivTextEN() {
        return portalAdaptivTextEN;
    }

    public void setPortalAdaptivTextEN(String portalAdaptivTextEN) {
        this.portalAdaptivTextEN = portalAdaptivTextEN;
    }

    public boolean isAppAbweichendEN() {
        return appAbweichendEN;
    }

    public void setAppAbweichendEN(boolean appAbweichendEN) {
        this.appAbweichendEN = appAbweichendEN;
    }

    public String getAppTextEN() {
        return appTextEN;
    }

    public void setAppTextEN(String appTextEN) {
        this.appTextEN = appTextEN;
    }

    public int getLetzteVersionDE() {
        return letzteVersionDE;
    }

    public void setLetzteVersionDE(int letzteVersionDE) {
        this.letzteVersionDE = letzteVersionDE;
    }

    public int getLetzteVersionEN() {
        return letzteVersionEN;
    }

    public void setLetzteVersionEN(int letzteVersionEN) {
        this.letzteVersionEN = letzteVersionEN;
    }

    

}
