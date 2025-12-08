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

import de.meetingapps.meetingportal.meetComEntities.EclFragen;

@Deprecated
public class EclFragenM implements Serializable {
    private static final long serialVersionUID = 840607627274652477L;

    /** Mandantennummer */
    private int mandant = 0;

    /**Eindeutig Ident dieser Frage*/
    private int frageIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    private long db_version;

    /******Fragesteller***************/
    /**Aktionär, unter dessen Besitz diese Frage gestellt wurde*/
    private int aktienregisterIdent = 0;

    /**Vorsorglich: falls Frage von Insti gestellt wurde*/
    private int instiIdent = 0;

    /**Vertreter*/
    private int vertreterIdent = 0;

    /**Identifikationsstring, z.B. EK-Nr, AktienregisterNummer o.ä.
     * LEN=20*/
    private String stellerIdent = "";

    /**********Frageninhalt********************/
    /**TOP(e), zu denen die Frage gestellt wird
     * LEN=100*/
    private String zuTop = "";

    /**LEN in Datenbank=30.000;
     * Zulässige Länge in Browser = 10.000
     */
    private String fragentext = "";

    /**LEN=19*/
    private String zeitpunktDerFrage = "";

    private int drucklaufNr = 0;

    /*****************Nicht in Datenbank****************/
    private String nameSteller = "";
    private String ortSteller = "";

    public EclFragenM() {
    }

    public EclFragenM(EclFragen pFrage) {
        this.mandant = pFrage.mandant;
        this.frageIdent = pFrage.frageIdent;
        this.db_version = pFrage.db_version;
        this.aktienregisterIdent = pFrage.aktienregisterIdent;
        this.instiIdent = pFrage.instiIdent;
        this.vertreterIdent = pFrage.vertreterIdent;
        this.stellerIdent = pFrage.stellerIdent;
        this.zuTop = pFrage.zuTop;
        this.fragentext = pFrage.fragentext;
        this.zeitpunktDerFrage = pFrage.zeitpunktDerFrage;
        this.drucklaufNr = pFrage.drucklaufNr;
        this.nameSteller = pFrage.nameSteller;
        this.ortSteller = pFrage.ortSteller;
    }

    /***************************Standard getter und setter*************************************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getFrageIdent() {
        return frageIdent;
    }

    public void setFrageIdent(int frageIdent) {
        this.frageIdent = frageIdent;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public int getInstiIdent() {
        return instiIdent;
    }

    public void setInstiIdent(int instiIdent) {
        this.instiIdent = instiIdent;
    }

    public int getVertreterIdent() {
        return vertreterIdent;
    }

    public void setVertreterIdent(int vertreterIdent) {
        this.vertreterIdent = vertreterIdent;
    }

    public String getStellerIdent() {
        return stellerIdent;
    }

    public void setStellerIdent(String stellerIdent) {
        this.stellerIdent = stellerIdent;
    }

    public String getZuTop() {
        return zuTop;
    }

    public void setZuTop(String zuTop) {
        this.zuTop = zuTop;
    }

    public String getFragentext() {
        return fragentext;
    }

    public void setFragentext(String fragentext) {
        this.fragentext = fragentext;
    }

    public String getZeitpunktDerFrage() {
        return zeitpunktDerFrage;
    }

    public void setZeitpunktDerFrage(String zeitpunktDerFrage) {
        this.zeitpunktDerFrage = zeitpunktDerFrage;
    }

    public int getDrucklaufNr() {
        return drucklaufNr;
    }

    public void setDrucklaufNr(int drucklaufNr) {
        this.drucklaufNr = drucklaufNr;
    }

    public String getNameSteller() {
        return nameSteller;
    }

    public void setNameSteller(String nameSteller) {
        this.nameSteller = nameSteller;
    }

    public String getOrtSteller() {
        return ortSteller;
    }

    public void setOrtSteller(String ortSteller) {
        this.ortSteller = ortSteller;
    }

}
