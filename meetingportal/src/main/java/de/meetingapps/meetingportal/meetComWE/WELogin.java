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

/**Entity für Web-Service - Login-Daten*/
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class WELogin {

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

    /* user=1 => Login über User != Aktionär (uKennung/uPasswort) (z.B. von Client-Software aus, für "interne" Tools);
     * user=0 => Aktionär logt sich ein (über kennung/passwort) (vor allem für Selbstbedienungstools wie die Aktionärs-App*/
    private int user = 0;

    /*uKennung, uPasswort soll (irgendwann: muß) auch gefüllt werden, wenn user=0; damit wird sichergestellt, dass ein Aktionär mit
     * seinen Zugangsdaten (einschließlich Passwort) alleine nicht mit dieser Schnittstelle kommunzieren kann, sondern zusätzlich noch
     * die in der Software hinterlegte technische Kennung erforderlich ist
     */
    private String uKennung = "";
    private String uPasswort = "";

    /*kennungArt gibt an, was in kennung geliefert wird.
     * 0 = "Login-Kennung", System sucht selbstständig was für ein Teilnehmer darüber referenziert wird
     * 1 = Aktionärsnummer
     */
    private int kennungArt = 0;

    /*Login-Daten der Person*/
    private String kennung = "";
    /*Passwort - kann leer bleiben (bzw. wird ignoriert), wenn user==1*/
    private String passwort = "";

    private int benutzernr = 0;
    private int arbeitsplatz = 0;

    /*Ab hier Standard-Setters und Getters*/
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

    public int getKennungArt() {
        return kennungArt;
    }

    public void setKennungArt(int kennungArt) {
        this.kennungArt = kennungArt;
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
