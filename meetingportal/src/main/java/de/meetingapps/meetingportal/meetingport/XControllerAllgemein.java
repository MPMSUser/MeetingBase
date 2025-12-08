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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuellePuffer;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Controller für allgemeine, portal-übergreifende Funktionen (also für Emittenten-, Aktionärs-, und Administrationsportal)*/

@RequestScoped
@Named
public class XControllerAllgemein {

    /**Auf true setzen => Das Laden und der Zugriff auf die Puffer wird im Systemlog protokolliert*/
    private boolean logDrucken = false;

    @Inject
    EclParamM eclParamM;
    @Inject
    BlMPuffer blMPuffer;
    @Inject
    BlMFuellePuffer blMFuellePuffer;
    @Inject
    EclDbM eclDbM;
    @Inject
    BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    @Inject
    EclPortalTexteM eclPortalTexteM;

    /*****************************************Initialisieren und Reload Mandant (einschließlich parameter)********************
     * Setzt Mandantennummer, liest Parameter (HVParam und ClParameterNS, Fehlertexte) ein und belegt diese in der globalen
     * Verwaltungstabelle (EclParamListeM) und belegt die Beans für die Parameter (EclParamM, EclParameterM, EclFehlerM)
     */

    private String mandant = "";
    private String hvjahr = "";
    private String hvnummer = "";
    private String datenbankbereich = "";

    /**Wenn true, dann konnte die Bearbeitung nicht durchgeführt werden => Fehleranzeige*/

    private boolean schwererFehler = false;

    /**wird eigentlich nicht benötigt .... nur zur Warnungsunterdrückung*/
    public synchronized String getMandant() {
        return mandant;
    }

    /**Muß aus erster JSF-Startseite aus aufgerufen werden - Mandant wird
     * gesetzt, und Parameter werden in Puffer geladen.
     *
     * Bsp. für Inhalt in JSF-Startseite:
     * 		<f:metadata>
            <f:viewParam name="mandant" value="#{xControllerAllgemein.mandant}" />
            <f:viewParam name="sprache" value="#{aLanguage.sprache}" />
            <f:viewParam name="test" value="#{aDlgVariablen.test}" />
        </f:metadata>
     */
    public synchronized void setMandant(String mandant) {

        if (logDrucken) {
            CaBug.druckeInfo("set Mandant=" + mandant);
        }

        if (mandant.isEmpty()) {
            eclParamM.getClGlobalVar().mandant = 0;
            eclParamM.getClGlobalVar().hvJahr = 0;
            eclParamM.getClGlobalVar().hvNummer = "";
            eclParamM.getClGlobalVar().datenbereich = "";
            return;
        }
        //		FacesContext fc = FacesContext.getCurrentInstance();
        //		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        //		String urlString=request.getRequestURL().toString();

        this.mandant = mandant;

        int mandantenNr = 0;
        int hvjahrNr = 0;

        if (mandant.isEmpty() || CaString.isNummern(mandant) == false) {
            mandantenNr = 0;
        } else {
            mandantenNr = Integer.parseInt(mandant);
        }
        if (hvjahr.isEmpty() || CaString.isNummern(hvjahr) == false) {
            hvjahrNr = 0;
        } else {
            hvjahrNr = Integer.parseInt(hvjahr);
        }

        if (eclParamM.getClGlobalVar() == null) {
            eclParamM.setClGlobalVar(new ClGlobalVar());
        }

        if (mandantenNr == 0) {
            CaBug.drucke("XControllerAllgemein.setMandant 001: Mandant ist 0");
            schwererFehler = true;
            return;
        }

        boolean startNurMitMandant = false;
        if (hvjahrNr == 0 || hvnummer.isEmpty() || datenbankbereich.isEmpty()) {
            startNurMitMandant = true;
        }
        EclEmittenten lEmittenten = null;

        /*Ggf. Puffer füllen, sowie Emittenten-Auswahl vorbereiten*/
        eclDbM.openAllOhneParameterCheck();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        if (startNurMitMandant) {
            lEmittenten = blMPuffer.getStandardEmittentFuerEmittentenPortal(mandantenNr);
        } else {
            lEmittenten = blMPuffer.getEmittent(mandantenNr, hvjahrNr, hvnummer, datenbankbereich);
        }
        eclDbM.closeAll();

        if (lEmittenten == null) {
            if (startNurMitMandant) {
                CaBug.drucke("XControllerAllgemein.setMandant 001 - keinen aktiven Portal-Mandanten gefunden");
                eclParamM.getClGlobalVar().mandant = 0;
                eclParamM.getClGlobalVar().hvJahr = 0;
                eclParamM.getClGlobalVar().hvNummer = "";
                eclParamM.getClGlobalVar().datenbereich = "";
                schwererFehler = true;
                return;
            } else {
                CaBug.drucke("XControllerAllgemein.setMandant 002 - Mandant/HV-Jahr etc. nicht gefunden");
                eclParamM.getClGlobalVar().mandant = 0;
                eclParamM.getClGlobalVar().hvJahr = 0;
                eclParamM.getClGlobalVar().hvNummer = "";
                eclParamM.getClGlobalVar().datenbereich = "";
                schwererFehler = true;
                return;
            }
        }

        eclParamM.getClGlobalVar().mandant = mandantenNr;
        eclParamM.getClGlobalVar().hvJahr = lEmittenten.hvJahr;
        eclParamM.getClGlobalVar().hvNummer = lEmittenten.hvNummer;
        eclParamM.getClGlobalVar().datenbereich = lEmittenten.dbArt;

        if (logDrucken) {
            CaBug.druckeInfo("XControllerAllgemein mandant=" + mandantenNr + " lEmittenten.hvJahr=" + lEmittenten.hvJahr
                    + " lEmittenten.hvNummer=" + lEmittenten.hvNummer + " lEmittenten.dbArt=lEmittenten.dbArt");
        }
        /*Nochmal aufrufen, damit ECLs zu aktuellem Emittent gefüllt werden*/
        eclDbM.openAll();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();

    }

    /**Für Nummernpflege-Bereich: Die PortalTexte werden neu geladen, die bestehende Seite mit den neuen Texten angezeigt*/
    public String doRefreshTexte() {
        eclDbM.openAll();
        BvReload lBvReload = new BvReload(eclDbM.getDbBundle());
        lBvReload.checkReload(eclDbM.getDbBundle().clGlobalVar.mandant);
        blMFuellePuffer.fuelleMandantenTexte(lBvReload.reloadPortalAppTexte);

        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        eclDbM.closeAll();
        return "";
    }

    /**Für Nummernpflege-Bereich: die Nummern zu den Portal-Texten werden angezeigt*/
    public String doNummernEin() {
        eclPortalTexteM.setNummernAnzeigen(true);
        return "";
    }

    /**Für Nummernpflege-Bereich: die Nummern zu den Portal-Texten werden ausgeblendet*/
    public String doNummernAus() {
        eclPortalTexteM.setNummernAnzeigen(false);
        return "";
    }

    public void setDatenbankbereich(String datenbankbereich) {
        if (logDrucken) {
            CaBug.druckeInfo("setDatenbankbereich=" + datenbankbereich);
        }
        this.datenbankbereich = datenbankbereich;
    }

    /**Einzige, derzeit sicher funktionierende Reload-Funktion!*/
    public synchronized String ddoReloadAllePufferAlleMandanten() {
        blMPuffer.clearAllPuffer();
        return "";
    }

    /**Alle Parameter (global + aktueller Mandant) werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterAlle() {
        doReloadParameterGlobal();
        doReloadParameterAktuellenMandant();
        return "";
    }

    /**Alle globalen Parameter werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterGlobal() {
        eclDbM.openAllOhneParameterCheck();
        BvReload bvReload = new BvReload(eclDbM.getDbBundle());
        bvReload.setReloadParameterGeraete();
        bvReload.setReloadParameterServer();
        bvReload.setReloadEmittenten();
        bvReload.setReloadUserLogin();
        eclDbM.closeAll();
        /*Nochmal öffnen zum Nachladen*/
        eclDbM.openAll();
        eclDbM.closeAll();

        CaBug.druckeInfo("XControllerAllgemein.doReloadParameterGlobal 001 Reload durchgeführt");
        return "";
    }

    /**Alle globalen Parameter werden auf "Reload" gesetzt - und mit EclOpen auch gleich neu eingelesen.
     * Kann direkt aus JSF-Oberfläche aufgerufen werden*/
    public synchronized String doReloadParameterAktuellenMandant() {
        int lMandantenNr = eclParamM.getClGlobalVar().mandant;
        eclDbM.openAll();
        BvReload bvReload = new BvReload(eclDbM.getDbBundle());
        bvReload.setReloadTexte(lMandantenNr);
        bvReload.setReloadParameter(lMandantenNr);
        bvReload.setReloadPortalAppTexte(lMandantenNr);
        eclDbM.closeAll();
        /*Nochmal öffnen zum Nachladen*/
        eclDbM.openAll();
        eclDbM.closeAll();

        CaBug.druckeInfo("XControllerAllgemein.doReloadParameterAktuellenMandant 001 Mandant " + lMandantenNr
                + "  Reload durchgeführt");
        return "";
    }

    /****************Standard Setter und Getter**********************************/
    synchronized  public boolean isSchwererFehler() {
        return schwererFehler;
    }

    synchronized  public void setSchwererFehler(boolean schwererFehler) {
        this.schwererFehler = schwererFehler;
    }

    public String getDatenbankbereich() {
        return datenbankbereich;
    }

    synchronized  public String getHvjahr() {
        return hvjahr;
    }

    synchronized public void setHvjahr(String hvjahr) {
        this.hvjahr = hvjahr;
    }

    public String getHvnummer() {
        return hvnummer;
    }

    public void setHvnummer(String hvnummer) {
        this.hvnummer = hvnummer;
    }

}
