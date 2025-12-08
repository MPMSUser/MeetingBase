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

import de.meetingapps.meetingportal.meetComEntities.EclMitteilungenAlt;

public class EclMitteilungenM implements Serializable {
    private static final long serialVersionUID = 840607627274652477L;

    /** Mandantennummer */
    private int mandant = 0;

    /**Eindeutig Ident dieser Frage*/
    private int mitteilungIdent = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in DbMeldungen
     */
    private long db_version;

    /******Mitteilungssteller***************/
    /**Aktionär, unter dessen Besitz diese Frage gestellt wurde*/
    private int aktienregisterIdent = 0;

    /**Vorsorglich: falls Frage von Insti gestellt wurde*/
    private int instiIdent = 0;

    /**Vertreter*/
    private int vertreterIdent = 0;

    /**Identifikationsstring, z.B. EK-Nr, AktienregisterNummer o.ä.
     * LEN=20*/
    private String stellerIdent = "";

    /**********Mitteilungsartinhalt********************/
    /**siehe KonstMitteilungArt
     * */
    private int artDerMitteilung = 0;

    /**Nur gefüllt, wenn entsprechende artDerMitteilung*/
    private int[] mitteilungZuTop = new int[200];

    /**LEN in Datenbank=30.000;
     * Zulässige Länge in Browser = 10.000
     */
    private String mitteilungtext = "";

    /**LEN=19*/
    private String zeitpunktDerMitteilung = "";

    private int drucklaufNr = 0;

    /*****************Nicht in Datenbank****************/
    private String nameSteller = "";
    private String ortSteller = "";

    public EclMitteilungenM() {
    }

    public EclMitteilungenM(EclMitteilungenAlt pMitteilung) {
        this.mandant = pMitteilung.mandant;
        this.mitteilungIdent = pMitteilung.mitteilungIdent;
        this.db_version = pMitteilung.db_version;
        this.aktienregisterIdent = pMitteilung.aktienregisterIdent;
        this.instiIdent = pMitteilung.instiIdent;
        this.vertreterIdent = pMitteilung.vertreterIdent;
        this.stellerIdent = pMitteilung.stellerIdent;
        this.artDerMitteilung = pMitteilung.artDerMitteilung;
        for (int i = 0; i < 200; i++) {
            this.mitteilungZuTop[i] = pMitteilung.mitteilungZuTop[i];
        }

        this.mitteilungtext = pMitteilung.mitteilungtext;
        this.zeitpunktDerMitteilung = pMitteilung.zeitpunktDerMitteilung;
        this.drucklaufNr = pMitteilung.drucklaufNr;
        this.nameSteller = pMitteilung.nameSteller;
        this.ortSteller = pMitteilung.ortSteller;
    }

    /***************************Standard getter und setter*************************************/

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
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

    public String getZeitpunktDerMitteilung() {
        return zeitpunktDerMitteilung;
    }

    public void setZeitpunktDerMitteilung(String zeitpunktDerMitteilung) {
        this.zeitpunktDerMitteilung = zeitpunktDerMitteilung;
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

    public int getMitteilungIdent() {
        return mitteilungIdent;
    }

    public void setMitteilungIdent(int mitteilungIdent) {
        this.mitteilungIdent = mitteilungIdent;
    }

}
