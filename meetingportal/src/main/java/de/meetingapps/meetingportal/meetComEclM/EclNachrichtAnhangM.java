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

import de.meetingapps.meetingportal.meetComEntities.EclNachrichtAnhang;

public class EclNachrichtAnhangM implements Serializable {
    private static final long serialVersionUID = -6216112360339413038L;

    /**Eindeutige Ident der hochgeladenen Datei*/
    private int ident = 0;

    /**Siehe ParamServer
     * Gehört zu ident und identMail*/
    private int dbServerIdent=0;

    /**Ident der Mail, zu der der Anhang gehört*/
    private int identMail = 0;

    private int mandant = 0;
    private int hvJahr = 0;
    private String hvNummer = "A";
    private String dbArt = "P";

    /**Dateiname (ohne Pfad!)
     * LEN=100*/
    private String dateiname = "";

    /**LEN=80*/
    private String beschreibung = "";

    
    public EclNachrichtAnhangM() {
        return;
    }

    public EclNachrichtAnhangM(EclNachrichtAnhang pEclNachrichtAnhang) {
        ident = pEclNachrichtAnhang.ident;
        dbServerIdent = pEclNachrichtAnhang.dbServerIdent;
        identMail = pEclNachrichtAnhang.identMail;

        mandant = pEclNachrichtAnhang.mandant;
        hvJahr = pEclNachrichtAnhang.hvJahr;
        hvNummer = pEclNachrichtAnhang.hvNummer;
        dbArt = pEclNachrichtAnhang.dbArt;

        dateiname = pEclNachrichtAnhang.dateiname;
        beschreibung = pEclNachrichtAnhang.beschreibung;
    }

    /***************Standard getter und setter********************/

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getDateiname() {
        return dateiname;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getDbServerIdent() {
        return dbServerIdent;
    }

    public void setDbServerIdent(int dbServerIdent) {
        this.dbServerIdent = dbServerIdent;
    }

    public int getIdentMail() {
        return identMail;
    }

    public void setIdentMail(int identMail) {
        this.identMail = identMail;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
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

    public String getDbArt() {
        return dbArt;
    }

    public void setDbArt(String dbArt) {
        this.dbArt = dbArt;
    }

}
