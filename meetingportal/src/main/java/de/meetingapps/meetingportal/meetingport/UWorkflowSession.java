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

import de.meetingapps.meetingportal.meetComEclM.EclBestWorkflowM;
import de.meetingapps.meetingportal.meetComEh.EhVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflowVorlVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmachtFuerAnzeige;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UWorkflowSession implements Serializable {
    private static final long serialVersionUID = -6599895420858637724L;

    /************Basis-Workflow*********************************/
    /**0 = Nur Aufforderung, nächstes Dokument anzuzeigen
     * 1 = DOkument in Bearbeitung
     */
    private int basisStatus = 0;

    private boolean wiederVerwenden = false;

    private boolean erweiterteFreigabe = false;

    /**0=keine Anzeige
     * 1=bereits erteilte Vollmachten
     * 2=Suche nach Namen (Aktionär)
     * 3=Suche nach Namen (SOnstige)
     * 4=Suche nach Mail-Adressen
     */
    private int anzeigeInfoBereich = 0;

    /**Für Info-Bereich*/

    private boolean bereitsVollmachtVorhanden = false;
    private List<EclBestWorkflowVorlVollmacht> bestWorkflowVorlVollmachtListe = null;

    private EclAktienregister[] aktienregisterArray = null;
    private EclPersonenNatJur[] personenNatJurArray = null;
    private EclLoginDaten[] loginDatenArray = null;

    
    private List<EhVorlaeufigeVollmacht> vorhandeneVorgaenge=null;
    public boolean liefereVorhandeneVorgaengeVorhanden() {
        if (vorhandeneVorgaenge==null || vorhandeneVorgaenge.isEmpty()) {
            return false;
        }
        return true;
    }
    
    private String anzuzeigendeDateiPfad="";
    private String anzuzeigendeDatei="";
    
    /**************Erweiterter-Workflow***************************/

    /**1 = Anzeige geprüft und akzeptiert
     * 2 = Anzeige geprüft und abgelehnt
     * 3 = Anzeige geprüft und Wiedervorlage
     */
    private String anzeigeErwWorkflow = "1";

    /**Anzeige nur der Un-Qualitätsgesicherten*/
    private boolean anzeigeNurUnQS = true;

    /**Für Such-Funktion nach Mitgliedsnummer*/
    private String sucheMitgliedsnummer = "";

    private List<EclBestWorkflowM> listSuchergebnis = null;

    /******************Erweiterter Workflow spezial********************/

    private List<EclBestWorkflowM> listVorgaenge = null;
    private String spezialAktionaersnummer = "";
    private String spezialDateiname = "";

    /********************Für Statistik*********************************/
    private int anzahlOffenBasisPruefen = 0;
    private int anzahlOffenBasisPruefenDigital = 0;
    private int anzahlOffenWiedervorlage = 0;
    private int anzahlOffenWiedervorlageDigital = 0;
    private int anzahlAbgelehnt = 0;
    private int anzahlAngenommen = 0;
    private int anzahlInBearbeitung = 0;
    private int anzahlInBearbeitungDigital = 0;
    private int anzahlAndereDokumente = 0;

    private int anzahlAnmeldungenE = 0;
    private int anzahlAnmeldungenF = 0;
    private int anzahlAnmeldungenV = 0;

    
    /*******************Für ku216-Prüfung*******************************************/
    private boolean workflowManuellGesetzlich=false;
    private boolean workflowManuellOhneAufHinterlegteGepruefte=false;

    private List<EclVorlaeufigeVollmachtFuerAnzeige> vollmachtenFuerAuswahl=null;

    
    
    /******************Löschen der Daten bei neuem Vorgang*************************/
    public void clear() {
        vorhandeneVorgaenge=null;
        bestWorkflowVorlVollmachtListe=null;
    }
    
    
    /*********************************Standard Getter und Setter**************************************/

    public int getAnzahlOffenBasisPruefen() {
        return anzahlOffenBasisPruefen;
    }

    public void setAnzahlOffenBasisPruefen(int anzahlOffenBasisPruefen) {
        this.anzahlOffenBasisPruefen = anzahlOffenBasisPruefen;
    }

    public int getAnzahlOffenWiedervorlage() {
        return anzahlOffenWiedervorlage;
    }

    public void setAnzahlOffenWiedervorlage(int anzahlOffenWiedervorlage) {
        this.anzahlOffenWiedervorlage = anzahlOffenWiedervorlage;
    }

    public int getAnzahlAbgelehnt() {
        return anzahlAbgelehnt;
    }

    public void setAnzahlAbgelehnt(int anzahlAbgelehnt) {
        this.anzahlAbgelehnt = anzahlAbgelehnt;
    }

    public int getAnzahlAngenommen() {
        return anzahlAngenommen;
    }

    public void setAnzahlAngenommen(int anzahlAngenommen) {
        this.anzahlAngenommen = anzahlAngenommen;
    }

    public int getBasisStatus() {
        return basisStatus;
    }

    public void setBasisStatus(int basisStatus) {
        this.basisStatus = basisStatus;
    }

    public int getAnzahlInBearbeitung() {
        return anzahlInBearbeitung;
    }

    public void setAnzahlInBearbeitung(int anzahlInBearbeitung) {
        this.anzahlInBearbeitung = anzahlInBearbeitung;
    }

    public String getAnzeigeErwWorkflow() {
        return anzeigeErwWorkflow;
    }

    public void setAnzeigeErwWorkflow(String anzeigeErwWorkflow) {
        this.anzeigeErwWorkflow = anzeigeErwWorkflow;
    }

    public boolean isAnzeigeNurUnQS() {
        return anzeigeNurUnQS;
    }

    public void setAnzeigeNurUnQS(boolean anzeigeNurUnQS) {
        this.anzeigeNurUnQS = anzeigeNurUnQS;
    }

    public String getSucheMitgliedsnummer() {
        return sucheMitgliedsnummer;
    }

    public void setSucheMitgliedsnummer(String sucheMitgliedsnummer) {
        this.sucheMitgliedsnummer = sucheMitgliedsnummer;
    }

    public List<EclBestWorkflowM> getListSuchergebnis() {
        return listSuchergebnis;
    }

    public void setListSuchergebnis(List<EclBestWorkflowM> listSuchergebnis) {
        this.listSuchergebnis = listSuchergebnis;
    }

    public List<EclBestWorkflowM> getListVorgaenge() {
        return listVorgaenge;
    }

    public void setListVorgaenge(List<EclBestWorkflowM> listVorgaenge) {
        this.listVorgaenge = listVorgaenge;
    }

    public String getSpezialDateiname() {
        return spezialDateiname;
    }

    public void setSpezialDateiname(String spezialDateiname) {
        this.spezialDateiname = spezialDateiname;
    }

    public String getSpezialAktionaersnummer() {
        return spezialAktionaersnummer;
    }

    public void setSpezialAktionaersnummer(String spezialAktionaersnummer) {
        this.spezialAktionaersnummer = spezialAktionaersnummer;
    }

    public int getAnzahlAndereDokumente() {
        return anzahlAndereDokumente;
    }

    public void setAnzahlAndereDokumente(int anzahlAndereDokumente) {
        this.anzahlAndereDokumente = anzahlAndereDokumente;
    }

    public boolean isWiederVerwenden() {
        return wiederVerwenden;
    }

    public void setWiederVerwenden(boolean wiederVerwenden) {
        this.wiederVerwenden = wiederVerwenden;
    }

    public int getAnzahlAnmeldungenE() {
        return anzahlAnmeldungenE;
    }

    public void setAnzahlAnmeldungenE(int anzahlAnmeldungenE) {
        this.anzahlAnmeldungenE = anzahlAnmeldungenE;
    }

    public int getAnzahlAnmeldungenF() {
        return anzahlAnmeldungenF;
    }

    public void setAnzahlAnmeldungenF(int anzahlAnmeldungenF) {
        this.anzahlAnmeldungenF = anzahlAnmeldungenF;
    }

    public int getAnzahlAnmeldungenV() {
        return anzahlAnmeldungenV;
    }

    public void setAnzahlAnmeldungenV(int anzahlAnmeldungenV) {
        this.anzahlAnmeldungenV = anzahlAnmeldungenV;
    }

    public int getAnzeigeInfoBereich() {
        return anzeigeInfoBereich;
    }

    public void setAnzeigeInfoBereich(int anzeigeInfoBereich) {
        this.anzeigeInfoBereich = anzeigeInfoBereich;
    }

    public boolean isBereitsVollmachtVorhanden() {
        return bereitsVollmachtVorhanden;
    }

    public void setBereitsVollmachtVorhanden(boolean bereitsVollmachtVorhanden) {
        this.bereitsVollmachtVorhanden = bereitsVollmachtVorhanden;
    }

    public List<EclBestWorkflowVorlVollmacht> getBestWorkflowVorlVollmachtListe() {
        return bestWorkflowVorlVollmachtListe;
    }

    public void setBestWorkflowVorlVollmachtListe(List<EclBestWorkflowVorlVollmacht> bestWorkflowVorlVollmachtListe) {
        this.bestWorkflowVorlVollmachtListe = bestWorkflowVorlVollmachtListe;
    }

    public EclAktienregister[] getAktienregisterArray() {
        return aktienregisterArray;
    }

    public void setAktienregisterArray(EclAktienregister[] aktienregisterArray) {
        this.aktienregisterArray = aktienregisterArray;
    }

    public EclPersonenNatJur[] getPersonenNatJurArray() {
        return personenNatJurArray;
    }

    public void setPersonenNatJurArray(EclPersonenNatJur[] personenNatJurArray) {
        this.personenNatJurArray = personenNatJurArray;
    }

    public EclLoginDaten[] getLoginDatenArray() {
        return loginDatenArray;
    }

    public void setLoginDatenArray(EclLoginDaten[] loginDatenArray) {
        this.loginDatenArray = loginDatenArray;
    }

    public boolean isErweiterteFreigabe() {
        return erweiterteFreigabe;
    }

    public void setErweiterteFreigabe(boolean erweiterteFreigabe) {
        this.erweiterteFreigabe = erweiterteFreigabe;
    }

    public boolean isWorkflowManuellGesetzlich() {
        return workflowManuellGesetzlich;
    }

    public void setWorkflowManuellGesetzlich(boolean workflowManuellGesetzlich) {
        this.workflowManuellGesetzlich = workflowManuellGesetzlich;
    }

    public List<EclVorlaeufigeVollmachtFuerAnzeige> getVollmachtenFuerAuswahl() {
        return vollmachtenFuerAuswahl;
    }

    public void setVollmachtenFuerAuswahl(List<EclVorlaeufigeVollmachtFuerAnzeige> vollmachtenFuerAuswahl) {
        this.vollmachtenFuerAuswahl = vollmachtenFuerAuswahl;
    }

    public boolean isWorkflowManuellOhneAufHinterlegteGepruefte() {
        return workflowManuellOhneAufHinterlegteGepruefte;
    }

    public void setWorkflowManuellOhneAufHinterlegteGepruefte(boolean workflowManuellOhneAufHinterlegteGepruefte) {
        this.workflowManuellOhneAufHinterlegteGepruefte = workflowManuellOhneAufHinterlegteGepruefte;
    }

    public List<EhVorlaeufigeVollmacht> getVorhandeneVorgaenge() {
        return vorhandeneVorgaenge;
    }

    public void setVorhandeneVorgaenge(List<EhVorlaeufigeVollmacht> vorhandeneVorgaenge) {
        this.vorhandeneVorgaenge = vorhandeneVorgaenge;
    }


    public String getAnzuzeigendeDatei() {
        return anzuzeigendeDatei;
    }


    public void setAnzuzeigendeDatei(String anzuzeigendeDatei) {
        this.anzuzeigendeDatei = anzuzeigendeDatei;
    }


    public String getAnzuzeigendeDateiPfad() {
        return anzuzeigendeDateiPfad;
    }


    public void setAnzuzeigendeDateiPfad(String anzuzeigendeDateiPfad) {
        this.anzuzeigendeDateiPfad = anzuzeigendeDateiPfad;
    }


    public int getAnzahlOffenWiedervorlageDigital() {
        return anzahlOffenWiedervorlageDigital;
    }


    public void setAnzahlOffenWiedervorlageDigital(int anzahlOffenWiedervorlageDigital) {
        this.anzahlOffenWiedervorlageDigital = anzahlOffenWiedervorlageDigital;
    }


    public int getAnzahlOffenBasisPruefenDigital() {
        return anzahlOffenBasisPruefenDigital;
    }


    public void setAnzahlOffenBasisPruefenDigital(int anzahlOffenBasisPruefenDigital) {
        this.anzahlOffenBasisPruefenDigital = anzahlOffenBasisPruefenDigital;
    }


    public int getAnzahlInBearbeitungDigital() {
        return anzahlInBearbeitungDigital;
    }


    public void setAnzahlInBearbeitungDigital(int anzahlInBearbeitungDigital) {
        this.anzahlInBearbeitungDigital = anzahlInBearbeitungDigital;
    }


}
