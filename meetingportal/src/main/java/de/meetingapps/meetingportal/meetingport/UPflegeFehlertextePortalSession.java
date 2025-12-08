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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UPflegeFehlertextePortalSession implements Serializable {
    private static final long serialVersionUID = -956828738209631415L;

    /**0=Anzeigen, 1=Bearbeiten*/
    private int modus=0;
    
    private boolean mandantenSpezifisch=false;
    private String ueberschrift="";
    
    private String basisSet="";
    
    private String ident="";
    private String kuerzel="";
    private String fehlermeldungDE="";
    private String fehlermeldungEN="";
    
    private String fehlermeldungStandardDE="";
    private String fehlermeldungStandardEN="";
    
    public void clearDaten() {
        modus=0;
        
        ident="";
        kuerzel="";
        fehlermeldungDE="";
        fehlermeldungEN="";
        
        fehlermeldungStandardDE="";
        fehlermeldungStandardEN="";

    }
    
    /********************Standard getter und setter**************************************/
    
    public boolean isMandantenSpezifisch() {
        return mandantenSpezifisch;
    }
    public void setMandantenSpezifisch(boolean mandantenSpezifisch) {
        this.mandantenSpezifisch = mandantenSpezifisch;
    }
    public String getBasisSet() {
        return basisSet;
    }
    public void setBasisSet(String basisSet) {
        this.basisSet = basisSet;
    }
    public String getIdent() {
        return ident;
    }
    public void setIdent(String ident) {
        this.ident = ident;
    }
    public String getKuerzel() {
        return kuerzel;
    }
    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }
    public String getFehlermeldungDE() {
        return fehlermeldungDE;
    }
    public void setFehlermeldungDE(String fehlermeldungDE) {
        this.fehlermeldungDE = fehlermeldungDE;
    }
    public String getFehlermeldungEN() {
        return fehlermeldungEN;
    }
    public void setFehlermeldungEN(String fehlermeldungEN) {
        this.fehlermeldungEN = fehlermeldungEN;
    }
    public String getUeberschrift() {
        return ueberschrift;
    }
    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }
    public int getModus() {
        return modus;
    }
    public void setModus(int modus) {
        this.modus = modus;
    }

    public String getFehlermeldungStandardDE() {
        return fehlermeldungStandardDE;
    }

    public void setFehlermeldungStandardDE(String fehlermeldungStandardDE) {
        this.fehlermeldungStandardDE = fehlermeldungStandardDE;
    }

    public String getFehlermeldungStandardEN() {
        return fehlermeldungStandardEN;
    }

    public void setFehlermeldungStandardEN(String fehlermeldungStandardEN) {
        this.fehlermeldungStandardEN = fehlermeldungStandardEN;
    }
    
   
    

}
