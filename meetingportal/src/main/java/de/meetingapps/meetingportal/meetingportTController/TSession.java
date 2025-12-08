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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TSession implements Serializable {
    private static final long serialVersionUID = 5595117439161476267L;

    private int logDrucken = 3;

    private @Inject EclPortalTexteM eclTextePortalM;

    private boolean aufgerufenDurchWebservice=false;
    
    /**true => es wurde das Permanent-Portal aufgerufen*/
    private boolean permanentPortal=false;
    
    /*+++++++++Mandanten-Aufruf++++++++++++++++++++++++++++++*/

    /**=!0 => Das Portal wurde mit expliziter Auswahl von HVJahr, HVNummer und datenbereich aufgerufen.
     * Wird benötigt für Link-Herstellung
     */
    private int hvJahr = 0;
    private String hvNummer = "";
    private String datenbereich = "";

    /*++++++++++Verwaltung der Views+++++++++++++++++++++++++*/
    private int viewnummer = 0;
    public void setViewnummer(int viewnummer) {
        if (fehlerseiteAnzeigen) {
            fehlerseiteAnzeigen=false;
            viewnummer=KonstPortalView.FEHLER_VIEW_TECHNISCH;
        }
        else {
            this.viewnummer = viewnummer;
        }
    }

    private boolean fehlerseiteAnzeigen=false;
    private String technischerFehlertext="";
    
    public void technischerFehlerAufgetreten(String pText) {
        CaBug.drucke("technischerFehlerAufgetreten "+pText);
        technischerFehlertext=pText;
        fehlerseiteAnzeigen=true;
    }

    public String seitenname() {
        return KonstPortalView.getSeitenname(viewnummer);
    }
    /**=1 => Testmodus aktiv, u.a. Anzeige der Fehlernummern (nur wenn Parameter auf enable Testmodus gestellt).
     * =2 => wie 1, aber es werden zusätzlich Infos wie App-Texte und Seiteninfo angezeigt  (nur wenn Parameter auf enable Testmodus gestellt);
     * =3 => nur erweiterte Anzeige für RZ im iMonitoring (unabhängig von Parameter enable Testmodus)
     *          (erweiterte Anzeige für RZ im iMonitoring ist auch bei 1 und 2 aktiv)
     * Ehemals aDlgVariablen.test*/
    private String testModus = "0";

    /**Testmodus 1 oder 2 - Anzeige von App-Texten und Fehlernummern*/
    public boolean testModusNummernAnzeigenAktiv() {
        return testModus.equals("1") || testModus.equals("2");
    }

    public boolean testModusSeitenInfoUndAppAnzeigenAktiv() {
        return testModus.equals("2");
    }

    public boolean testModusRZAktiv() {
        return testModus.equals("1") || testModus.equals("2")  || testModus.equals("3");
            }
    /**Differenz zur aktuellen Zeit - nur falls testModus&2*/
    private long difZeit = 0;

    public String andereZeit() {
        return CaDatumZeit.DatumZeitStringFromLong(difZeit + CaDatumZeit.zeitStempelMS());
    }

    
    /**true => parameterbedingte Portalsperre wird ausgehebelt
     * 
     * Präfix-Kennung: #!OR2oo7OR!# */
    private boolean orPortalsperre=false;
    
    
    /**Aktuelle Java-JSF-Session-ID*/
    private String jsfSessionId = "";

    /**Präfix für meetingSocket (ws oder wss, jenachdem ob https oder nicht)*/
    private String praefixSocket = "ws";

    /**=1 => aktuell ist ein User eingeloggt - Verlassen-Warnung beim Browser schließen bringen
     * =2 => aktuell ist ein User eingeloggt, dieser soll aber nicht ausgeloggt werden. In Broswer-Schließen
     *  Prozedur wird das wieder auf "1" gesetzt. Wird zum Portal-Wechsel (Register <-> HV) benötigt*/
    private String userEingeloggt = "0";

    /*+++++++++++++RückkehrMenü+++++++++++++++++++*/
    /*Wird derzeit nur bei Streaming, Unterlagen, Zugang/Abgang Gast verwendet*/
    private int rueckkehrZuMenue = 0;

    /*++++++++++++Videobereich++++++++++++++++++++++*/
    /**=true => Video wird gerade angezeigt*/
    private boolean streamshow = false;

    /*++++++++++++++++++++++++Status-Verwaltung+++++++++++++++++*/
    /**Wird auf true gesetzt, sobald alle Status-Daten zur Kennung eingelesen wurden.
     * Wird verwendet, um bei "Hochleistungsmitgliedsversammlungen" den Status nur einmal
     * einzulesen.
     */
    private boolean statusIstGeladen = false;

    /*++++++++++++Fehler- und Quittungs-Handling++++++++++++++++++*/
    
    /**Allgemeines:
     * "Fehler" beinhaltet die Anzeige eines kurzen Textes, gemäß CaFehler.
     * "Quittung" beinhaltet die Anzeige eines vollständigen Textbausteines.
     * 
     * Sowohl "Fehler" als auch "Quittung" können als Fehlermeldung, Warnhinweis, oder Quittungstext dargestellt werden.
     * "Fehler" und "Quittung" sind damit an dieser Stelle rein technische, historisch gewachsene :-) Begriffe.
     */
    
    private String fehlerMeldung = "";
    private int fehlerCode = 0;
    private int fehlerTextNr=0;
    private String fehlerText="";
    
    /**1 bis 100: fehlerCode bzw. fehlerMeldung wird angezeigt
     * 101 bis 200: fehlerTextNr wird angezeigt
     * 
     * Jeweils +:
     * 
     * 1=Fehler; 
     * 2=Quittung (ok) 
     * 3=Quittung (Warnung)
     * 
     */
    private int fehlerArt=0;

    public void clearFehler() {
        fehlerMeldung = "";
        fehlerCode = 0;
        
        fehlerTextNr=0;
        fehlerText="";
        fehlerArt=0;
    }

    public void trageFehlerEin(int pFehlernr) {
        trageFehlerEinMitArt(pFehlernr, 1);
    }
    
    /**art=
     * 1=Fehler; 
     * 2=Quittung (ok) 
     * 3=Quittung (Warnung)
     */
    public void trageFehlerEinMitArt(int pFehlernr, int pArt) {
        fehlerMeldung = eclTextePortalM.getFehlertext(pFehlernr);
        fehlerCode = pFehlernr;
        fehlerArt=0+pArt;

        fehlerTextNr=0;
        fehlerText="";
        CaBug.druckeLog("Fehlermeldung="+fehlerMeldung, logDrucken, 10);
    }

    public void trageFehlerEinMitArt(String pFehler, int pArt) {
        fehlerMeldung = pFehler;
        fehlerCode = -1;
        fehlerArt=0+pArt;

        fehlerTextNr=0;
        fehlerText="";
        CaBug.druckeLog("Fehlermeldung="+fehlerMeldung, logDrucken, 10);
        
    }
    
    /**Wird mit Art == 2 eingetragen*/
    @Deprecated
    public void trageQuittungTextNr(int pTextNr) {
        trageQuittungTextNr(pTextNr, 2);
    }
    
    /**art=
     * 1=Fehler; 
     * 2=Quittung (ok) 
     * 3=Quittung (Warnung)
     */
   public void trageQuittungTextNr(int pTextNr, int pArt) {
    
        fehlerTextNr = pTextNr;
        fehlerText=eclTextePortalM.holeITextOhneCRLF(pTextNr);
        fehlerArt=100+pArt;
        
        fehlerCode = 0;
        fehlerMeldung="";
        
        CaBug.druckeLog("QuittungTextNr="+pTextNr+ " " +fehlerText, logDrucken, 10);
    }

    public boolean pruefeFehlerOderQuittungVorhanden() {
        if (fehlerCode==0 && fehlerTextNr==0) {
            return false;
        }
        return true;
    }
    
    public boolean pruefeFehlerMeldungVorhanden() {
        if (fehlerCode == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean pruefeQuittungVorhanden() {
        if (fehlerTextNr==0) {
            return false;
        }
        return true;
    }

    public int liefereMeldungArt() {
        if (fehlerArt>100) {return fehlerArt-100;}
        return fehlerArt;
    }
    /*+++++++++++++++Willenserklärungen verfügbar+++++++++++++++++++++++*/
    private boolean gewinnspielAktiv = false;

    /**Alle Funktionen im Zusammenhang mit einer HV sind enabled/disabled*/
    private boolean hvPortalInBetrieb = false;

    /**1=vor der HV, 2=nach der HV (Parameter Nr. 515)*/
    private int vorDerHVNachDerHV = 0;

    private boolean portalErstanmeldungIstMoeglich = true;
    private boolean portalEKIstMoeglich = true;
    private boolean portalSRVIstMoeglich = true;
    private boolean portalBriefwahlIstMoeglich = true;
    private boolean portalKIAVIstMoeglich = true;
    private boolean portalVollmachtDritteIstMoeglich = true;

    private int aktuellePortalPhaseNr=0;
    
    
    /*++++++++++++++++++++++++++++++++++Rücksetzfunktionen++++++++++++++*/
    /**Zurücksetzen grundsätzlicher Werte nach dem Login*/
    public void clearNachLogin() {
        /**Zurücksetzen insbesondere von auswahlMaske*/
        streamshow = false;
        konferenzshow = false;
        jwtWsToken = "";
        orPortalsperre=false;

    }

    /**Zurücksetzen bei jedem neuen Request*/
    public void clearRequest() {
        clearFehler();
    }

    /**Zurücksetzen aller Session-Variablen - aufzurufen bei Logout*/
    public void clearSession() {
        streamshow = false;
        konferenzshow = false;
        jwtWsToken = "";

    }
    
    /*++++++++++++++++++++++++++++ Konferenz +++++++++++++++++++++++++++++++*/
  
    
    private boolean konferenzshow = false;
    
    /*+++++++++++++++++++++++++++ Websockets ++++++++++++++++++++++++++++++*/
    
    private String jwtWsToken = "";
    

    /*****************Standard getter und setter*************************/

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public int getFehlerCode() {
        return fehlerCode;
    }

    public void setFehlerCode(int fehlerCode) {
        this.fehlerCode = fehlerCode;
    }

    public boolean isStatusIstGeladen() {
        return statusIstGeladen;
    }

    public void setStatusIstGeladen(boolean statusIstGeladen) {
        this.statusIstGeladen = statusIstGeladen;
    }

    public int getViewnummer() {
        return viewnummer;
    }

    public String getTestModus() {
        return testModus;
    }

    public void setTestModus(String testModus) {
        this.testModus = testModus;
    }

    public String getJsfSessionId() {
        return jsfSessionId;
    }

    public void setJsfSessionId(String jsfSessionId) {
        this.jsfSessionId = jsfSessionId;
    }

    public String getUserEingeloggt() {
        CaBug.druckeLog("userEingeloggt=" + userEingeloggt, logDrucken, 10);
        return userEingeloggt;
    }

    public void setUserEingeloggt(String userEingeloggt) {
        CaBug.druckeLog("userEingeloggt=" + userEingeloggt, logDrucken, 10);
        this.userEingeloggt = userEingeloggt;
    }

    public boolean isStreamshow() {
        return streamshow;
    }

    public void setStreamshow(boolean streamshow) {
        this.streamshow = streamshow;
    }

    public long getDifZeit() {
        return difZeit;
    }

    public void setDifZeit(long difZeit) {
        this.difZeit = difZeit;
    }

    public boolean isGewinnspielAktiv() {
        return gewinnspielAktiv;
    }

    public void setGewinnspielAktiv(boolean gewinnspielAktiv) {
        this.gewinnspielAktiv = gewinnspielAktiv;
    }

    public boolean isHvPortalInBetrieb() {
        return hvPortalInBetrieb;
    }

    public void setHvPortalInBetrieb(boolean hvPortalInBetrieb) {
        this.hvPortalInBetrieb = hvPortalInBetrieb;
    }

    public int getVorDerHVNachDerHV() {
        return vorDerHVNachDerHV;
    }

    public void setVorDerHVNachDerHV(int vorDerHVNachDerHV) {
        this.vorDerHVNachDerHV = vorDerHVNachDerHV;
    }

    public boolean isPortalErstanmeldungIstMoeglich() {
        return portalErstanmeldungIstMoeglich;
    }

    public void setPortalErstanmeldungIstMoeglich(boolean portalErstanmeldungIstMoeglich) {
        this.portalErstanmeldungIstMoeglich = portalErstanmeldungIstMoeglich;
    }

    public boolean isPortalEKIstMoeglich() {
        return portalEKIstMoeglich;
    }

    public void setPortalEKIstMoeglich(boolean portalEKIstMoeglich) {
        this.portalEKIstMoeglich = portalEKIstMoeglich;
    }

    public boolean isPortalSRVIstMoeglich() {
        return portalSRVIstMoeglich;
    }

    public void setPortalSRVIstMoeglich(boolean portalSRVIstMoeglich) {
        this.portalSRVIstMoeglich = portalSRVIstMoeglich;
    }

    public boolean isPortalBriefwahlIstMoeglich() {
        return portalBriefwahlIstMoeglich;
    }

    public void setPortalBriefwahlIstMoeglich(boolean portalBriefwahlIstMoeglich) {
        this.portalBriefwahlIstMoeglich = portalBriefwahlIstMoeglich;
    }

    public boolean isPortalKIAVIstMoeglich() {
        return portalKIAVIstMoeglich;
    }

    public void setPortalKIAVIstMoeglich(boolean portalKIAVIstMoeglich) {
        this.portalKIAVIstMoeglich = portalKIAVIstMoeglich;
    }

    public boolean isPortalVollmachtDritteIstMoeglich() {
        return portalVollmachtDritteIstMoeglich;
    }

    public void setPortalVollmachtDritteIstMoeglich(boolean portalVollmachtDritteIstMoeglich) {
        this.portalVollmachtDritteIstMoeglich = portalVollmachtDritteIstMoeglich;
    }

    public String getPraefixSocket() {
        return praefixSocket;
    }

    public void setPraefixSocket(String praefixSocket) {
        this.praefixSocket = praefixSocket;
    }

    public int getRueckkehrZuMenue() {
        return rueckkehrZuMenue;
    }

    public void setRueckkehrZuMenue(int rueckkehrZuMenue) {
        this.rueckkehrZuMenue = rueckkehrZuMenue;
    }

    public int getHvJahr() {
        return hvJahr;
    }

    public void setHvJahr(int hvJahr) {
        this.hvJahr = hvJahr;
    }

    public String getHvNummer() {
        return hvNummer;
    }

    public void setHvNummer(String hvNummer) {
        this.hvNummer = hvNummer;
    }

    public String getDatenbereich() {
        return datenbereich;
    }

    public void setDatenbereich(String datenbereich) {
        this.datenbereich = datenbereich;
    }

    public boolean isAufgerufenDurchWebservice() {
        return aufgerufenDurchWebservice;
    }

    public void setAufgerufenDurchWebservice(boolean aufgerufenDurchWebservice) {
        this.aufgerufenDurchWebservice = aufgerufenDurchWebservice;
    }

    public int getAktuellePortalPhaseNr() {
        return aktuellePortalPhaseNr;
    }

    public void setAktuellePortalPhaseNr(int aktuellePortalPhaseNr) {
        this.aktuellePortalPhaseNr = aktuellePortalPhaseNr;
    }

    public boolean isPermanentPortal() {
        return permanentPortal;
    }

    public void setPermanentPortal(boolean permanentPortal) {
        CaBug.druckeLog("permanentPortal="+permanentPortal, logDrucken, 10);
        this.permanentPortal = permanentPortal;
    }

    public int getFehlerArt() {
        return fehlerArt;
    }

    public void setFehlerArt(int fehlerArt) {
        this.fehlerArt = fehlerArt;
    }

    public int getFehlerTextNr() {
        return fehlerTextNr;
    }

    public void setFehlerTextNr(int fehlerTextNr) {
        this.fehlerTextNr = fehlerTextNr;
    }

    public String getFehlerText() {
        return fehlerText;
    }

    public void setFehlerText(String fehlerText) {
        this.fehlerText = fehlerText;
    }

    public boolean isKonferenzshow() {
        return konferenzshow;
    }

    public void setKonferenzshow(boolean konferenzshow) {
        this.konferenzshow = konferenzshow;
    }

    public String getJwtWsToken() {
        return jwtWsToken;
    }

    public void setJwtWsToken(String jwtWsToken) {
        this.jwtWsToken = jwtWsToken;
    }

    public boolean isFehlerseiteAnzeigen() {
        return fehlerseiteAnzeigen;
    }

    public void setFehlerseiteAnzeigen(boolean fehlerseiteAnzeigen) {
        this.fehlerseiteAnzeigen = fehlerseiteAnzeigen;
    }

    public String getTechnischerFehlertext() {
        return technischerFehlertext;
    }

    public void setTechnischerFehlertext(String technischerFehlertext) {
        this.technischerFehlertext = technischerFehlertext;
    }

    public boolean isOrPortalsperre() {
        return orPortalsperre;
    }

    public void setOrPortalsperre(boolean orPortalsperre) {
        this.orPortalsperre = orPortalsperre;
    }
    

}
