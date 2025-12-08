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

import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;

/**Verwendung für Anzeige im Portal / App*/
public class EclPortalUnterlagenM implements Serializable {
    private static final long serialVersionUID = -4716909749198443821L;

    private int reihenfolgeLoginOben=0;
    private int reihenfolgeLoginUnten=0;
    private int reihenfolgeExterneSeite=0;
    private int reihenfolgeUnterlagen=0;
    private int reihenfolgeBotschaften=0;

    /**Für Mitgliederportal*/
    private int unterlagenbereichMenueNr=0;
    /**Für Mitgliederportal*/
    private int reihenfolgeUnterlagenbereich=0;

    /**0=Button; 1=Überschrift; 2=Text*/
    private int art = 0;
    /**abhängig von art - ggf. eine Style-Info dazu, siehe KonstPortalUnterlagen.ARTSTYLE_BUTTON_ALS_BUTTON*/
    private int artStyle=0;

    /**LEN=8, wird mit DE / EN ergänzt*/
    private String dateiname = "";
    private int dateiMehrsprachigVorhanden = 0;

    private String bezeichnungDE = "";
    private String bezeichnungEN = "";
    
    private String iFrameUrl = "";

    /*******************Nicht in Datenbank*******************/
    private boolean ursprungIstEinVerweis=false;
    private int verweisAufIdent=0;
    
    

    public EclPortalUnterlagenM(EclPortalUnterlagen pPortalUnterlagen) {
        reihenfolgeLoginOben = pPortalUnterlagen.reihenfolgeLoginOben;
        reihenfolgeLoginUnten = pPortalUnterlagen.reihenfolgeLoginUnten;
        reihenfolgeExterneSeite = pPortalUnterlagen.reihenfolgeExterneSeite;
        reihenfolgeUnterlagen = pPortalUnterlagen.reihenfolgeUnterlagen;
        reihenfolgeBotschaften = pPortalUnterlagen.reihenfolgeBotschaften;
        unterlagenbereichMenueNr = pPortalUnterlagen.unterlagenbereichMenueNr;
        reihenfolgeUnterlagenbereich = pPortalUnterlagen.reihenfolgeUnterlagenbereich;
        art = pPortalUnterlagen.art;
        artStyle = pPortalUnterlagen.artStyle;
        dateiname = pPortalUnterlagen.dateiname;
        dateiMehrsprachigVorhanden = pPortalUnterlagen.dateiMehrsprachigVorhanden;
        bezeichnungDE = pPortalUnterlagen.bezeichnungDE;
        bezeichnungEN = pPortalUnterlagen.bezeichnungEN;
        
        ursprungIstEinVerweis=pPortalUnterlagen.ursprungIstEinVerweis;
        verweisAufIdent=pPortalUnterlagen.verweisAufIdent;
    }
    
    /**Nimmt die "Inhaltsfelder" aus pPortalUnterlagenQuelle. Nur dann zulässig, wenn pPortalUnterlagen ein "Verweissatz" ist.
     * Belegt ursprungIstEinVerweis und verweisAufIdent passend aus pPortalUnterlagenQuelle*/
    public EclPortalUnterlagenM(EclPortalUnterlagen pPortalUnterlagen, EclPortalUnterlagen pPortalUnterlagenQuelle) {
        reihenfolgeLoginOben = pPortalUnterlagen.reihenfolgeLoginOben;
        reihenfolgeLoginUnten = pPortalUnterlagen.reihenfolgeLoginUnten;
        reihenfolgeExterneSeite = pPortalUnterlagen.reihenfolgeExterneSeite;
        reihenfolgeUnterlagen = pPortalUnterlagen.reihenfolgeUnterlagen;
        reihenfolgeBotschaften = pPortalUnterlagen.reihenfolgeBotschaften;
        unterlagenbereichMenueNr = pPortalUnterlagen.unterlagenbereichMenueNr;
        reihenfolgeUnterlagenbereich = pPortalUnterlagen.reihenfolgeUnterlagenbereich;
        art = pPortalUnterlagenQuelle.art;
        artStyle = pPortalUnterlagenQuelle.artStyle;
        dateiname = pPortalUnterlagenQuelle.dateiname;
        dateiMehrsprachigVorhanden = pPortalUnterlagenQuelle.dateiMehrsprachigVorhanden;
        bezeichnungDE = pPortalUnterlagenQuelle.bezeichnungDE;
        bezeichnungEN = pPortalUnterlagenQuelle.bezeichnungEN;
        
        ursprungIstEinVerweis=true;
        verweisAufIdent=pPortalUnterlagenQuelle.ident;
    }


    /********************Standard getter und setter*******************************/
    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public String getDateiname() {
        return dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public String getBezeichnungDE() {
        return bezeichnungDE;
    }

    public void setBezeichnungDE(String bezeichnungDE) {
        this.bezeichnungDE = bezeichnungDE;
    }

    public String getBezeichnungEN() {
        return bezeichnungEN;
    }

    public void setBezeichnungEN(String bezeichnungEN) {
        this.bezeichnungEN = bezeichnungEN;
    }

    public int getArtStyle() {
        return artStyle;
    }

    public void setArtStyle(int artStyle) {
        this.artStyle = artStyle;
    }

    public int getDateiMehrsprachigVorhanden() {
        return dateiMehrsprachigVorhanden;
    }

    public void setDateiMehrsprachigVorhanden(int dateiMehrsprachigVorhanden) {
        this.dateiMehrsprachigVorhanden = dateiMehrsprachigVorhanden;
    }

    public int getReihenfolgeLoginOben() {
        return reihenfolgeLoginOben;
    }

    public void setReihenfolgeLoginOben(int reihenfolgeLoginOben) {
        this.reihenfolgeLoginOben = reihenfolgeLoginOben;
    }

    public int getReihenfolgeLoginUnten() {
        return reihenfolgeLoginUnten;
    }

    public void setReihenfolgeLoginUnten(int reihenfolgeLoginUnten) {
        this.reihenfolgeLoginUnten = reihenfolgeLoginUnten;
    }

    public int getReihenfolgeExterneSeite() {
        return reihenfolgeExterneSeite;
    }

    public void setReihenfolgeExterneSeite(int reihenfolgeExterneSeite) {
        this.reihenfolgeExterneSeite = reihenfolgeExterneSeite;
    }

    public int getReihenfolgeUnterlagen() {
        return reihenfolgeUnterlagen;
    }

    public void setReihenfolgeUnterlagen(int reihenfolgeUnterlagen) {
        this.reihenfolgeUnterlagen = reihenfolgeUnterlagen;
    }

    public int getReihenfolgeBotschaften() {
        return reihenfolgeBotschaften;
    }

    public void setReihenfolgeBotschaften(int reihenfolgeBotschaften) {
        this.reihenfolgeBotschaften = reihenfolgeBotschaften;
    }

    public String getiFrameUrl() {
        return iFrameUrl;
    }

    public void setiFrameUrl(String iFrameUrl) {
        this.iFrameUrl = iFrameUrl;
    }

    public int getUnterlagenbereichMenueNr() {
        return unterlagenbereichMenueNr;
    }

    public void setUnterlagenbereichMenueNr(int unterlagenbereichMenueNr) {
        this.unterlagenbereichMenueNr = unterlagenbereichMenueNr;
    }

    public int getReihenfolgeUnterlagenbereich() {
        return reihenfolgeUnterlagenbereich;
    }

    public void setReihenfolgeUnterlagenbereich(int reihenfolgeUnterlagenbereich) {
        this.reihenfolgeUnterlagenbereich = reihenfolgeUnterlagenbereich;
    }
    
    public boolean isUrsprungIstEinVerweis() {
        return ursprungIstEinVerweis;
    }

    public void setUrsprungIstEinVerweis(boolean ursprungIstEinVerweis) {
        this.ursprungIstEinVerweis = ursprungIstEinVerweis;
    }

    public int getVerweisAufIdent() {
        return verweisAufIdent;
    }

    public void setVerweisAufIdent(int verweisAufIdent) {
        this.verweisAufIdent = verweisAufIdent;
    }

}
