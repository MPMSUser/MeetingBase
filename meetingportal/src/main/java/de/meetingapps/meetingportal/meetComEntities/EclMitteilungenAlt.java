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
package de.meetingapps.meetingportal.meetComEntities;

@Deprecated
public class EclMitteilungenAlt {
    /** Mandantennummer */
    public int mandant = 0;

    /**Eindeutig Ident dieser Frage*/
    public int mitteilungIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    public long db_version;

    /******Mitteilungssteller***************/
    /**Aktionär, unter dessen Besitz diese Frage gestellt wurde*/
    public int aktienregisterIdent = 0;

    /**Vorsorglich: falls Frage von Insti gestellt wurde, dann ist hier drin die ident der EclPersonenNatJur*/
    public int instiIdent = 0;

    /**Vertreter*/
    public int vertreterIdent = 0;

    /**Identifikationsstring, z.B. EK-Nr, AktienregisterNummer o.ä.
     * LEN=20*/
    public String stellerIdent = "";

    /**********Mitteilungsartinhalt********************/
    /**siehe KonstMitteilungArt
     * */
    public int artDerMitteilung = 0;

    /**Nur gefüllt, wenn entsprechende artDerMitteilung*/
    public int[] mitteilungZuTop = new int[200];

    /**LEN in Datenbank=30.000;
     * Zulässige Länge in Browser = 10.000
     */
    public String mitteilungtext = "";

    /**LEN=19*/
    public String zeitpunktDerMitteilung = "";

    public int drucklaufNr = 0;

    /*****************Nicht in Datenbank****************/
    public String aktionaerNummer = "";
    public String aktionaerName = "";
    public String aktionaerOrt = "";
    public String nameSteller = "";
    public String ortSteller = "";
    public long aktien = 0;

    public EclMitteilungenAlt() {
        for (int i = 0; i < 200; i++) {
            mitteilungZuTop[i] = 0;
        }
    }

    /**************Standard getter und setter*************************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getMitteilungIdent() {
        return mitteilungIdent;
    }

    public void setMitteilungIdent(int mitteilungIdent) {
        this.mitteilungIdent = mitteilungIdent;
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

    public int getArtDerMitteilung() {
        return artDerMitteilung;
    }

    public void setArtDerMitteilung(int artDerMitteilung) {
        this.artDerMitteilung = artDerMitteilung;
    }

    public int[] getMitteilungZuTop() {
        return mitteilungZuTop;
    }

    public void setMitteilungZuTop(int[] mitteilungZuTop) {
        this.mitteilungZuTop = mitteilungZuTop;
    }

    public String getMitteilungtext() {
        return mitteilungtext;
    }

    public void setMitteilungtext(String mitteilungtext) {
        this.mitteilungtext = mitteilungtext;
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
