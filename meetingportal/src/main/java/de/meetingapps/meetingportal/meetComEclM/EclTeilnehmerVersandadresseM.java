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

import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclTeilnehmerVersandadresseM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    private int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben*/
    private int ident = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    private long db_version = 0;

    private int identPersonenNatJur = 0;

    private int versandAbweichend = 0;
    private int anredeIdVersand = 0;
    private String titelVersand = "";
    private String name3Versand = "";
    private String name2Versand = "";

    private String nameVersand = "";
    private String vornameVersand = "";
    private String strasseVersand = "";
    private String postleitzahlVersand = "";
    private String ortVersand = "";
    private int staatIdVersand = 0;
    private String staatCodeVersand = "";
    private String staatNameDEVersand = "";

    public void init() {
        mandant = 0;
        ident = 0;
        db_version = 0;
        identPersonenNatJur = 0;
        versandAbweichend = 0;
        nameVersand = "";
        vornameVersand = "";
        strasseVersand = "";
        postleitzahlVersand = "";
        ortVersand = "";
        staatIdVersand = 0;
        staatCodeVersand = "";
        staatNameDEVersand = "";

    }

    public void copyFrom(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {

        mandant = pPersonenNatJurVersandadresse.mandant;
        ident = pPersonenNatJurVersandadresse.ident;
        db_version = pPersonenNatJurVersandadresse.db_version;
        identPersonenNatJur = pPersonenNatJurVersandadresse.identPersonenNatJur;
        versandAbweichend = pPersonenNatJurVersandadresse.versandAbweichend;
        anredeIdVersand = pPersonenNatJurVersandadresse.anredeIdVersand;
        titelVersand = pPersonenNatJurVersandadresse.titelVersand;
        name3Versand = pPersonenNatJurVersandadresse.name3Versand;
        name2Versand = pPersonenNatJurVersandadresse.name2Versand;
        nameVersand = pPersonenNatJurVersandadresse.nameVersand;
        vornameVersand = pPersonenNatJurVersandadresse.vornameVersand;
        strasseVersand = pPersonenNatJurVersandadresse.strasseVersand;
        postleitzahlVersand = pPersonenNatJurVersandadresse.postleitzahlVersand;
        ortVersand = pPersonenNatJurVersandadresse.ortVersand;
        staatIdVersand = pPersonenNatJurVersandadresse.staatIdVersand;

    }

    public void copyFrom(EclStaaten pStaaten) {
        staatCodeVersand = pStaaten.code;
        staatNameDEVersand = pStaaten.nameDE;

    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

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

    public int getIdentPersonenNatJur() {
        return identPersonenNatJur;
    }

    public void setIdentPersonenNatJur(int identPersonenNatJur) {
        this.identPersonenNatJur = identPersonenNatJur;
    }

    public int getVersandAbweichend() {
        return versandAbweichend;
    }

    public void setVersandAbweichend(int versandAbweichend) {
        this.versandAbweichend = versandAbweichend;
    }

    public String getNameVersand() {
        return nameVersand;
    }

    public void setNameVersand(String nameVersand) {
        this.nameVersand = nameVersand;
    }

    public String getVornameVersand() {
        return vornameVersand;
    }

    public void setVornameVersand(String vornameVersand) {
        this.vornameVersand = vornameVersand;
    }

    public String getStrasseVersand() {
        return strasseVersand;
    }

    public void setStrasseVersand(String strasseVersand) {
        this.strasseVersand = strasseVersand;
    }

    public String getPostleitzahlVersand() {
        return postleitzahlVersand;
    }

    public void setPostleitzahlVersand(String postleitzahlVersand) {
        this.postleitzahlVersand = postleitzahlVersand;
    }

    public String getOrtVersand() {
        return ortVersand;
    }

    public void setOrtVersand(String ortVersand) {
        this.ortVersand = ortVersand;
    }

    public int getStaatIdVersand() {
        return staatIdVersand;
    }

    public void setStaatIdVersand(int staatIdVersand) {
        this.staatIdVersand = staatIdVersand;
    }

    public String getStaatCodeVersand() {
        return staatCodeVersand;
    }

    public void setStaatCodeVersand(String staatCodeVersand) {
        this.staatCodeVersand = staatCodeVersand;
    }

    public String getStaatNameDEVersand() {
        return staatNameDEVersand;
    }

    public void setStaatNameDEVersand(String staatNameDEVersand) {
        this.staatNameDEVersand = staatNameDEVersand;
    }

    public int getAnredeIdVersand() {
        return anredeIdVersand;
    }

    public void setAnredeIdVersand(int anredeIdVersand) {
        this.anredeIdVersand = anredeIdVersand;
    }

    public String getTitelVersand() {
        return titelVersand;
    }

    public void setTitelVersand(String titelVersand) {
        this.titelVersand = titelVersand;
    }

    public String getName3Versand() {
        return name3Versand;
    }

    public void setName3Versand(String name3Versand) {
        this.name3Versand = name3Versand;
    }

    public String getName2Versand() {
        return name2Versand;
    }

    public void setName2Versand(String name2Versand) {
        this.name2Versand = name2Versand;
    }

}
