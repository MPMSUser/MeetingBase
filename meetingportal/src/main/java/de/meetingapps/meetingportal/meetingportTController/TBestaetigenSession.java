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
public class TBestaetigenSession implements Serializable {
    private static final long serialVersionUID = -1330010320023753038L;
    
    private int logDrucken=3;

    private @Inject EclPortalTexteM eclTextePortalM;

    private String bestaetigungsCode;
    private String bestaetigungsCode2;

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

    public String getBestaetigungsCode() {
        return bestaetigungsCode;
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        this.bestaetigungsCode = bestaetigungsCode;
    }

    public String getBestaetigungsCode2() {
        return bestaetigungsCode2;
    }

    public void setBestaetigungsCode2(String bestaetigungsCode2) {
        this.bestaetigungsCode2 = bestaetigungsCode2;
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
