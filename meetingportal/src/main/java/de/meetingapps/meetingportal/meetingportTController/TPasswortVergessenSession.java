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
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TPasswortVergessenSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private int logDrucken=3;
    
    private @Inject EclPortalTexteM eclTextePortalM;

    private String loginKennung = "";

    private String emailAdresse = "";

    private String strasse = "";
    private String ort = "";

    private String passwort = "";

    private String passwortBestaetigung = "";
    private String bestaetigungsCode = "";

    /**Für Ablaufvariante 2 - Abfrage des Geburtsdatums als Identifizierung*/
    private String geburtsdatum = "";

    /**Falls sowohl bei E-Mail-Registrierung als auch bei Nicht-Email-Registrierung der (halb)automatische Weg
     * angeboten wird (also Eingabe von Email, oder Eingabe von Straße / Ort), dann muß eine Auswahl für den
     * Aktionär angeboten werden => auswahlAnbieten=true;
     */
    private boolean auswahlAnbieten = false;

    /**Falls rein manuelles Verfahren, dann keinerlei Eingabe ermöglichen.*/
    private boolean eingabeAnbieten = false;

    /**Falls auswahlAnbieten=true, dann wird über dieses Feld gesteuert welche der beiden Varianten (E-Mail oder Straße/Ort)
     * vom Aktionär ausgewählt wurde.
     * 1=Email-Adresse
     * 2=Strasse / Ort
     */
    private String eingabeSelektiert = "0";

    /**1 = Text 231, "nichts einzugeben"
     * 2 = Text 208. "nur Email eingeben"
     * 3 = Text 233, "nur Anschrift einzugeben"
     * 4 = Text 232, ""Auswahl möglich"
     */
    private int textVariante = 0;

    /**1 = Email verschickt, Text 238
     * 2 = Post wird verschickt, Text 239
     * 3 = User ist für das Portal gesperrt
     */
    private int textVarianteQuittung = 0;

    /**Standardwert False. Wird bei Anforderung über App auf true gesetzt - dann wird nur Code verschickt, nicht Link*/
    private boolean wurdeUeberAppAngefordert = false;

    /*Fehlermeldung für aPwZurueck.xhtml*/
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
        CaBug.druckeLog("QuittungTextNr="+pTextNr, logDrucken, 10);
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

    
    
    public void clearDlgVariablen() {
        this.loginKennung = "";
        this.emailAdresse = "";
        this.strasse = "";
        this.ort = "";
        this.geburtsdatum = "";

    }

    /*************Standard setter/getter********************************************/

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getPasswortBestaetigung() {
        return passwortBestaetigung;
    }

    public void setPasswortBestaetigung(String passwortBestaetigung) {
        this.passwortBestaetigung = passwortBestaetigung;
    }

    public String getBestaetigungsCode() {
        return bestaetigungsCode;
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        this.bestaetigungsCode = bestaetigungsCode;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public boolean isAuswahlAnbieten() {
        return auswahlAnbieten;
    }

    public void setAuswahlAnbieten(boolean auswahlAnbieten) {
        this.auswahlAnbieten = auswahlAnbieten;
    }

    public boolean isEingabeAnbieten() {
        return eingabeAnbieten;
    }

    public void setEingabeAnbieten(boolean eingabeAnbieten) {
        this.eingabeAnbieten = eingabeAnbieten;
    }

    public String getEingabeSelektiert() {
        return eingabeSelektiert;
    }

    public void setEingabeSelektiert(String eingabeSelektiert) {
        this.eingabeSelektiert = eingabeSelektiert;
    }

    public int getTextVariante() {
        return textVariante;
    }

    public void setTextVariante(int textVariante) {
        this.textVariante = textVariante;
    }

    public boolean isWurdeUeberAppAngefordert() {
        return wurdeUeberAppAngefordert;
    }

    public void setWurdeUeberAppAngefordert(boolean wurdeUeberAppAngefordert) {
        this.wurdeUeberAppAngefordert = wurdeUeberAppAngefordert;
    }

    public int getTextVarianteQuittung() {
        return textVarianteQuittung;
    }

    public void setTextVarianteQuittung(int textVarianteQuittung) {
        this.textVarianteQuittung = textVarianteQuittung;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getFehlerCode() {
        return fehlerCode;
    }

    public void setFehlerCode(int fehlerCode) {
        this.fehlerCode = fehlerCode;
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

    public int getFehlerArt() {
        return fehlerArt;
    }

    public void setFehlerArt(int fehlerArt) {
        this.fehlerArt = fehlerArt;
    }

}
