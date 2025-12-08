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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TLoginLogoutSession implements Serializable {
    private static final long serialVersionUID = 5595117439161476267L;

    private int logDrucken=3;
    
    private @Inject EclPortalTexteM eclTextePortalM;
    private @Inject EclParamM eclParamM;

    private List<EclPortalUnterlagenM> unterlagenListeOben = null;
    private boolean unterlagenListeObenVorhanden=false;
    
    private List<EclPortalUnterlagenM> unterlagenListeUnten = null;
    private boolean unterlagenListeUntenVorhanden=false;

    /**Für Login-Maske separate Fehlermeldung. Soll verhindern, dass bei einer Fehlersituation innerhalb des Portals
     * der Teilnehmer den Login-Link direkt aufruft und dann wieder die Fehlermeldung von intern bekommt.
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

    public void clearAll() {
        CaBug.druckeLog("", logDrucken, 10);
        fehlerMeldung = "";
        fehlerCode = 0;
        
        fehlerTextNr=0;
        fehlerText="";
        fehlerArt=0;

        loginKennung = "";
        loginPasswort = "";
        loginName="";
        loginDatum="";
    }

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

    public void trageFehlerEinMitArt(int pFehlernrAnfang, String pZwischentext, int pFehlernrEnde, int pArt) {
        fehlerMeldung = eclTextePortalM.getFehlertext(pFehlernrAnfang)+pZwischentext+eclTextePortalM.getFehlertext(pFehlernrEnde);
        fehlerCode = pFehlernrAnfang;
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

    private String loginKennung = "";
    private String loginPasswort = "";

    /**Für Anmeldeverfahren 99 - ku216-Bank**/
    private String loginName = "";
    private String loginDatum = "";
    
    /**++++++++++++++Verwaltung der Login-Fehlversuche++++++++++++++++++++++++*/
    /**Anzahl der fehlgeschlagenen Versuch.
     * Wird auf 0 gesetzt, 
     */
    private int anzahlLoginFehlversuche=0;
    
    private long naechsterZuleassigerLoginTimeStamp=0L;
    
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

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        CaBug.druckeLog("loginKennung="
                +loginKennung, logDrucken, 10);
         this.loginKennung = loginKennung;
    }

    public String getLoginPasswort() {
        return loginPasswort;
    }

    public void setLoginPasswort(String loginPasswort) {
        this.loginPasswort = loginPasswort;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginDatum() {
        return loginDatum;
    }

    public void setLoginDatum(String loginDatum) {
        this.loginDatum = loginDatum;
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

    public List<EclPortalUnterlagenM> getUnterlagenListeOben() {
        return unterlagenListeOben;
    }

    public void setUnterlagenListeOben(List<EclPortalUnterlagenM> unterlagenListeOben) {
        this.unterlagenListeOben = unterlagenListeOben;
    }

    public List<EclPortalUnterlagenM> getUnterlagenListeUnten() {
        return unterlagenListeUnten;
    }

    public void setUnterlagenListeUnten(List<EclPortalUnterlagenM> unterlagenListeUnten) {
        this.unterlagenListeUnten = unterlagenListeUnten;
    }

    public boolean isUnterlagenListeObenVorhanden() {
        return unterlagenListeObenVorhanden;
    }

    public void setUnterlagenListeObenVorhanden(boolean unterlagenListeObenVorhanden) {
        this.unterlagenListeObenVorhanden = unterlagenListeObenVorhanden;
    }

    public boolean isUnterlagenListeUntenVorhanden() {
        return unterlagenListeUntenVorhanden;
    }

    public void setUnterlagenListeUntenVorhanden(boolean unterlagenListeUntenVorhanden) {
        this.unterlagenListeUntenVorhanden = unterlagenListeUntenVorhanden;
    }

    public int getAnzahlLoginFehlversuche() {
        return anzahlLoginFehlversuche;
    }

    public void setAnzahlLoginFehlversuche(int anzahlLoginFehlversuche) {
        this.anzahlLoginFehlversuche = anzahlLoginFehlversuche;
    }

    public long getNaechsterZuleassigerLoginTimeStamp() {
        return naechsterZuleassigerLoginTimeStamp;
    }

    public void setNaechsterZuleassigerLoginTimeStamp(long naechsterZuleassigerLoginTimeStamp) {
        this.naechsterZuleassigerLoginTimeStamp = naechsterZuleassigerLoginTimeStamp;
    }

}
