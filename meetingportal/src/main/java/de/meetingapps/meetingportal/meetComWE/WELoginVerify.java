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
package de.meetingapps.meetingportal.meetComWE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.xml.bind.annotation.XmlRootElement;

/**Kann entweder alleine bei einem Webservice übergeben werden, oder als Element der Parameter-Klasse.
 * Dient zum Verifizieren der Berechtigung bei jedem Web-Service-Aufruf sowie dem Übergeben immerweiderkehrender
 * Parameter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class WELoginVerify {

    /*Daten für Versionsabgleich*/
    private int hoechsteVersionTexteDEUebergreifend = 0;
    private int hoechsteVersionTexteENUebergreifend = 0;

    private int hoechsteVersionTexteDEMandant = 0;
    private int hoechsteVersionTexteENMandant = 0;

    private int aktuelleAppVersion = 0;

    private int mandant = 0;
    private int hvJahr = 0;
    private String hvNummer = "";
    private String datenbereich = "";

    /* =1 => Login über User != Aktionär (uKennung/uPasswort) - interne Softwareverarbeitung (d.h. kein Aktionärspasswort erforderlich)
     * =2 => login über externe Software, vom Aktionär genutzt. D.h. Aktionärspasswort zusätzlich erforderlich (siehe unten)
     * =0 => Aktionär logt sich ein (über kennung/passwort) (derzeit nicht verwendet/unterstützt*/
    private int user = 0;
    private String uKennung = "";
    private String uPasswort = "";

    /**Login-Daten der Person*/
    /**XXX Kennung muß bei jedem erneuten Retry gefüllt sein, früher nur anmeldeIdentAktienregister etc.*/
    private String kennung = "";
    private String passwort = "";

    /**Weg, auf dem Willenserklärung eingegangen ist - siehe EclWillenserklaerung*/
    private int eingabeQuelle = 0;

    /**Zeitpunkt, zu dem die Willenserklärung (bzgl. Priorisierung) gelten soll - siehe EclWillenserklaerung*/
    private String erteiltZeitpunkt = ""; /*JJJJ.MM.TT HH:MM:SS*/

    /**Siehe BlTeilnehmerLogin*/

    /**Art der anmeldeKennung:
     * 1 = Aktienregister (aktionärsnummer),
     * 
     * @Deprecated
     * 2 = Aktionärsmeldung ("Eintrittskarte- Inhaberaktien"),
     * TODO #9 Konsolidieren - geht 2 Aktionärsmeldung überhaupt? und 3?
     * 
     * 3= PersonNatJur*/
    private int anmeldeKennungArt = 0;

    /**Ident, im Aktienregister, auf den die Anmelde-Kennung verweist*/
    private int anmeldeIdentAktienregister = 0;

    /**Ident der natJur-Person, auf die die Anmelde-Kennung verweist (soweit bereits angelegt!)*/
    private int anmeldeIdentPersonenNatJur = 0;
  


    private int benutzernr = 0;
    private int arbeitsplatz = 0;

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public int getAnmeldeKennungArt() {
        return anmeldeKennungArt;
    }

    public void setAnmeldeKennungArt(int anmeldeKennungArt) {
        this.anmeldeKennungArt = anmeldeKennungArt;
    }

    public int getAnmeldeIdentAktienregister() {
        return anmeldeIdentAktienregister;
    }

    public void setAnmeldeIdentAktienregister(int anmeldeIdentAktienregister) {
        this.anmeldeIdentAktienregister = anmeldeIdentAktienregister;
    }

    public int getAnmeldeIdentPersonenNatJur() {
        return anmeldeIdentPersonenNatJur;
    }

    public void setAnmeldeIdentPersonenNatJur(int anmeldeIdentPersonenNatJur) {
        this.anmeldeIdentPersonenNatJur = anmeldeIdentPersonenNatJur;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getuKennung() {
        return uKennung;
    }

    public void setuKennung(String uKennung) {
        this.uKennung = uKennung;
    }

    public String getuPasswort() {
        return uPasswort;
    }

    public void setuPasswort(String uPasswort) {
        this.uPasswort = uPasswort;
    }

    public int getEingabeQuelle() {
        return eingabeQuelle;
    }

    public void setEingabeQuelle(int eingabeQuelle) {
        this.eingabeQuelle = eingabeQuelle;
    }

    public String getErteiltZeitpunkt() {
        return erteiltZeitpunkt;
    }

    public void setErteiltZeitpunkt(String erteiltZeitpunkt) {
        this.erteiltZeitpunkt = erteiltZeitpunkt;
    }

    public int getBenutzernr() {
        return benutzernr;
    }

    public void setBenutzernr(int benutzernr) {
        this.benutzernr = benutzernr;
    }

    public int getArbeitsplatz() {
        return arbeitsplatz;
    }

    public void setArbeitsplatz(int arbeitsplatz) {
        this.arbeitsplatz = arbeitsplatz;
    }

    public int getHoechsteVersionTexteDEUebergreifend() {
        return hoechsteVersionTexteDEUebergreifend;
    }

    public void setHoechsteVersionTexteDEUebergreifend(int hoechsteVersionTexteDEUebergreifend) {
        this.hoechsteVersionTexteDEUebergreifend = hoechsteVersionTexteDEUebergreifend;
    }

    public int getHoechsteVersionTexteENUebergreifend() {
        return hoechsteVersionTexteENUebergreifend;
    }

    public void setHoechsteVersionTexteENUebergreifend(int hoechsteVersionTexteENUebergreifend) {
        this.hoechsteVersionTexteENUebergreifend = hoechsteVersionTexteENUebergreifend;
    }

    public int getHoechsteVersionTexteDEMandant() {
        return hoechsteVersionTexteDEMandant;
    }

    public void setHoechsteVersionTexteDEMandant(int hoechsteVersionTexteDEMandant) {
        this.hoechsteVersionTexteDEMandant = hoechsteVersionTexteDEMandant;
    }

    public int getHoechsteVersionTexteENMandant() {
        return hoechsteVersionTexteENMandant;
    }

    public void setHoechsteVersionTexteENMandant(int hoechsteVersionTexteENMandant) {
        this.hoechsteVersionTexteENMandant = hoechsteVersionTexteENMandant;
    }

    public int getAktuelleAppVersion() {
        return aktuelleAppVersion;
    }

    public void setAktuelleAppVersion(int aktuelleAppVersion) {
        this.aktuelleAppVersion = aktuelleAppVersion;
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

    public String getDatenbereich() {
        return datenbereich;
    }

    public void setDatenbereich(String datenbereich) {
        this.datenbereich = datenbereich;
    }

}
