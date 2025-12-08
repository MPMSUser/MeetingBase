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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclPortalTexteEditM implements Serializable {
    private static final long serialVersionUID = 1778546766472610634L;

    private String[] seitenName = null;
    private String[] beschreibung = null;

    private boolean[] textInPortal = null;
    private boolean[] textInApp = null;

    private int[] verbundenMitIdentGesamt = null;

    private int[] seitennummer = null;
    private int[] ident = null;

    /*************Standard-Texte**************************/
    private String[] portalStandardTextDE = null;
    private String[] portalStandardAdaptivTextDE = null;
    private String[] appStandardTextDE = null;

    private String[] portalStandardTextEN = null;
    private String[] portalStandardAdaptivTextEN = null;
    private String[] appStandardTextEN = null;

    /**********************Deutsch*************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    private int[] letzteVersionDE = null;

    private boolean[] portalVonStandardVerwendenDE = null;
    private String[] portalTextDE = null;

    private boolean[] portalAdaptivAbweichendDE = null;
    private String[] portalAdaptivTextDE = null;

    private boolean[] appAbweichendDE = null;
    private String[] appTextDE = null;

    /***********************Englisch*****************************************/
    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    private int[] letzteVersionEN = null;

    private boolean[] portalVonStandardVerwendenEN = null;
    private String[] portalTextEN = null;

    private boolean[] portalAdaptivAbweichendEN = null;
    private String[] portalAdaptivTextEN = null;

    private boolean[] appAbweichendEN = null;
    private String[] appTextEN = null;

    public String doPortalVonStandardDE(String pWert) {
        int iWert = Integer.parseInt(pWert);
        portalTextDE[iWert] = portalStandardTextDE[iWert];
        return "";
    }

    public String doAdaptivVonPortalDE(String pWert) {
        int iWert = Integer.parseInt(pWert);
        portalAdaptivTextDE[iWert] = portalTextDE[iWert];
        return "";
    }

    public String doAppVonPortalDE(String pWert) {
        int iWert = Integer.parseInt(pWert);
        appTextDE[iWert] = portalTextDE[iWert];
        return "";
    }

    public String doPortalVonStandardEN(String pWert) {
        int iWert = Integer.parseInt(pWert);
        portalTextEN[iWert] = portalStandardTextEN[iWert];
        return "";
    }

    public String doAdaptivVonPortalEN(String pWert) {
        int iWert = Integer.parseInt(pWert);
        portalAdaptivTextEN[iWert] = portalTextEN[iWert];
        return "";
    }

    public String doAppVonPortalEN(String pWert) {
        int iWert = Integer.parseInt(pWert);
        appTextEN[iWert] = portalTextEN[iWert];
        return "";
    }

    /*******Standard Getter und Setter*************************************/

    public String[] getSeitenName() {
        return seitenName;
    }

    public void setSeitenName(String[] seitenName) {
        this.seitenName = seitenName;
    }

    public String[] getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String[] beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String[] getPortalStandardTextDE() {
        return portalStandardTextDE;
    }

    public void setPortalStandardTextDE(String[] portalStandardTextDE) {
        this.portalStandardTextDE = portalStandardTextDE;
    }

    public String[] getPortalStandardAdaptivTextDE() {
        return portalStandardAdaptivTextDE;
    }

    public void setPortalStandardAdaptivTextDE(String[] portalStandardAdaptivTextDE) {
        this.portalStandardAdaptivTextDE = portalStandardAdaptivTextDE;
    }

    public String[] getAppStandardTextDE() {
        return appStandardTextDE;
    }

    public void setAppStandardTextDE(String[] appStandardTextDE) {
        this.appStandardTextDE = appStandardTextDE;
    }

    public String[] getPortalStandardTextEN() {
        return portalStandardTextEN;
    }

    public void setPortalStandardTextEN(String[] portalStandardTextEN) {
        this.portalStandardTextEN = portalStandardTextEN;
    }

    public String[] getPortalStandardAdaptivTextEN() {
        return portalStandardAdaptivTextEN;
    }

    public void setPortalStandardAdaptivTextEN(String[] portalStandardAdaptivTextEN) {
        this.portalStandardAdaptivTextEN = portalStandardAdaptivTextEN;
    }

    public String[] getAppStandardTextEN() {
        return appStandardTextEN;
    }

    public void setAppStandardTextEN(String[] appStandardTextEN) {
        this.appStandardTextEN = appStandardTextEN;
    }

    public int[] getLetzteVersionDE() {
        return letzteVersionDE;
    }

    public void setLetzteVersionDE(int[] letzteVersionDE) {
        this.letzteVersionDE = letzteVersionDE;
    }

    public boolean[] getPortalVonStandardVerwendenDE() {
        return portalVonStandardVerwendenDE;
    }

    public void setPortalVonStandardVerwendenDE(boolean[] portalVonStandardVerwendenDE) {
        this.portalVonStandardVerwendenDE = portalVonStandardVerwendenDE;
    }

    public String[] getPortalTextDE() {
        return portalTextDE;
    }

    public void setPortalTextDE(String[] portalTextDE) {
        this.portalTextDE = portalTextDE;
    }

    public boolean[] getPortalAdaptivAbweichendDE() {
        return portalAdaptivAbweichendDE;
    }

    public void setPortalAdaptivAbweichendDE(boolean[] portalAdaptivAbweichendDE) {
        this.portalAdaptivAbweichendDE = portalAdaptivAbweichendDE;
    }

    public String[] getPortalAdaptivTextDE() {
        return portalAdaptivTextDE;
    }

    public void setPortalAdaptivTextDE(String[] portalAdaptivTextDE) {
        this.portalAdaptivTextDE = portalAdaptivTextDE;
    }

    public boolean[] getAppAbweichendDE() {
        return appAbweichendDE;
    }

    public void setAppAbweichendDE(boolean[] appAbweichendDE) {
        this.appAbweichendDE = appAbweichendDE;
    }

    public String[] getAppTextDE() {
        return appTextDE;
    }

    public void setAppTextDE(String[] appTextDE) {
        this.appTextDE = appTextDE;
    }

    public int[] getLetzteVersionEN() {
        return letzteVersionEN;
    }

    public void setLetzteVersionEN(int[] letzteVersionEN) {
        this.letzteVersionEN = letzteVersionEN;
    }

    public boolean[] getPortalVonStandardVerwendenEN() {
        return portalVonStandardVerwendenEN;
    }

    public void setPortalVonStandardVerwendenEN(boolean[] portalVonStandardVerwendenEN) {
        this.portalVonStandardVerwendenEN = portalVonStandardVerwendenEN;
    }

    public String[] getPortalTextEN() {
        return portalTextEN;
    }

    public void setPortalTextEN(String[] portalTextEN) {
        this.portalTextEN = portalTextEN;
    }

    public boolean[] getPortalAdaptivAbweichendEN() {
        return portalAdaptivAbweichendEN;
    }

    public void setPortalAdaptivAbweichendEN(boolean[] portalAdaptivAbweichendEN) {
        this.portalAdaptivAbweichendEN = portalAdaptivAbweichendEN;
    }

    public String[] getPortalAdaptivTextEN() {
        return portalAdaptivTextEN;
    }

    public void setPortalAdaptivTextEN(String[] portalAdaptivTextEN) {
        this.portalAdaptivTextEN = portalAdaptivTextEN;
    }

    public boolean[] getAppAbweichendEN() {
        return appAbweichendEN;
    }

    public void setAppAbweichendEN(boolean[] appAbweichendEN) {
        this.appAbweichendEN = appAbweichendEN;
    }

    public String[] getAppTextEN() {
        return appTextEN;
    }

    public void setAppTextEN(String[] appTextEN) {
        this.appTextEN = appTextEN;
    }

    public int[] getSeitennummer() {
        return seitennummer;
    }

    public void setSeitennummer(int[] seitennummer) {
        this.seitennummer = seitennummer;
    }

    public int[] getIdent() {
        return ident;
    }

    public void setIdent(int[] ident) {
        this.ident = ident;
    }

    public boolean[] getTextInPortal() {
        return textInPortal;
    }

    public void setTextInPortal(boolean[] textInPortal) {
        this.textInPortal = textInPortal;
    }

    public boolean[] getTextInApp() {
        return textInApp;
    }

    public void setTextInApp(boolean[] textInApp) {
        this.textInApp = textInApp;
    }

    public int[] getVerbundenMitIdentGesamt() {
        return verbundenMitIdentGesamt;
    }

    public void setVerbundenMitIdentGesamt(int[] verbundenMitIdentGesamt) {
        this.verbundenMitIdentGesamt = verbundenMitIdentGesamt;
    }

}
