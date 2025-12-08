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

import java.io.Serializable;

@Deprecated
public class EclWortmeldungen implements Serializable {
    private static final long serialVersionUID = 3969580093927451992L;

    /**Eindeutig Ident dieser WortmeldungFrage*/
    public int ident = 0;

    /**
     * Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung
     * in Db
     */
    public long db_version;

    /******Wortmelder***************/

    /**LoginKennung
     * Ident der Loginkennung, die die Wortmeldung erteilt hat*/
    public int loginKennungIdent = 0;

    /**Identifikationsstring, z.B. EK-Nr, AktienregisterNummer o.ä.
     * LEN=20
     * Wird beim Einlesen gefüllt mit
     * Aktionär
     * Vertreter
     * Insti
     * */
    public String melderIdentText = "";

    /**********Wortmeldung Inhalt********************/
    /**Name des Wortmelders (je nach Parameterstellung)
     * LEN=100*/
    public String wortmelder = "";

    /**Eingegebene Telefonnummer
     * LEN=100*/
    public String telefonNr = "";

    /**TOP(e), zu denen die Wortmeldung erfolgte
     * LEN=100*/
    public String zuTop = "";

    /**LEN in Datenbank=30.000;
     * Zulässige Länge in Browser = 10.000
     */
    public String wortmeldungtext = "";

    /**LEN=19*/
    public String zeitpunktDerWortmeldung = "";

    public int drucklaufNr = 0;

    /** siehe KonstWortmeldungenStatus
     */
    public int status = 0;

    public int lfdNrInRednerliste = 0;

    /**************Nicht in Datenbank*********************/
    public String loginKennung = "";
    public String loginKennungAnzeige = "";
    public String nameWortmelder = "";
    public String ortWortmelder = "";
    public String zeitpunktFuerAnzeige = "";
    public String statusTextIntern = "";

//    @Deprecated
//    public String liefereStatusText() {
//        return KonstMitteilungStatus.getTextNr(status);
//    }

    /*****************************Standard getter und setter***************************************************************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public long getDb_version() {
        return db_version;
    }

    public void setDb_version(long db_version) {
        this.db_version = db_version;
    }

    public int getLoginKennungIdent() {
        return loginKennungIdent;
    }

    public void setLoginKennungIdent(int loginKennungIdent) {
        this.loginKennungIdent = loginKennungIdent;
    }

    public String getMelderIdentText() {
        return melderIdentText;
    }

    public void setMelderIdentText(String melderIdentText) {
        this.melderIdentText = melderIdentText;
    }

    public String getWortmelder() {
        return wortmelder;
    }

    public void setWortmelder(String wortmelder) {
        this.wortmelder = wortmelder;
    }

    public String getTelefonNr() {
        return telefonNr;
    }

    public void setTelefonNr(String telefonNr) {
        this.telefonNr = telefonNr;
    }

    public String getZuTop() {
        return zuTop;
    }

    public void setZuTop(String zuTop) {
        this.zuTop = zuTop;
    }

    public String getWortmeldungtext() {
        return wortmeldungtext;
    }

    public void setWortmeldungtext(String wortmeldungtext) {
        this.wortmeldungtext = wortmeldungtext;
    }

    public String getZeitpunktDerWortmeldung() {
        return zeitpunktDerWortmeldung;
    }

    public void setZeitpunktDerWortmeldung(String zeitpunktDerWortmeldung) {
        this.zeitpunktDerWortmeldung = zeitpunktDerWortmeldung;
    }

    public int getDrucklaufNr() {
        return drucklaufNr;
    }

    public void setDrucklaufNr(int drucklaufNr) {
        this.drucklaufNr = drucklaufNr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLfdNrInRednerliste() {
        return lfdNrInRednerliste;
    }

    public void setLfdNrInRednerliste(int lfdNrInRednerliste) {
        this.lfdNrInRednerliste = lfdNrInRednerliste;
    }

    public String getLoginKennung() {
        return loginKennung;
    }

    public void setLoginKennung(String loginKennung) {
        this.loginKennung = loginKennung;
    }

    public String getNameWortmelder() {
        return nameWortmelder;
    }

    public void setNameWortmelder(String nameWortmelder) {
        this.nameWortmelder = nameWortmelder;
    }

    public String getOrtWortmelder() {
        return ortWortmelder;
    }

    public void setOrtWortmelder(String ortWortmelder) {
        this.ortWortmelder = ortWortmelder;
    }

    public String getZeitpunktFuerAnzeige() {
        return zeitpunktFuerAnzeige;
    }

    public void setZeitpunktFuerAnzeige(String zeitpunktFuerAnzeige) {
        this.zeitpunktFuerAnzeige = zeitpunktFuerAnzeige;
    }

    public String getStatusTextIntern() {
        return statusTextIntern;
    }

    public void setStatusTextIntern(String statusTextIntern) {
        this.statusTextIntern = statusTextIntern;
    }

    public String getLoginKennungAnzeige() {
        return loginKennungAnzeige;
    }

    public void setLoginKennungAnzeige(String loginKennungAnzeige) {
        this.loginKennungAnzeige = loginKennungAnzeige;
    }

}
