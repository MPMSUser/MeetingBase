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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerRegistrierungSession implements Serializable {
    private static final long serialVersionUID = -3125590656834161813L;

    private int logDrucken = 3;

    private boolean erstregistrierung = false; /*true => Erstregistrierung; false=Einstellungen*/

    /**Wenn true, dann wurde nicht weiter geklickt, sondern muß erneut ein Email-Registrierungscode
     * verschickt werden, dann zurück zur Registrierungsmaske. Wird von Web-Service ggf. auf True gesetzt*/
    private boolean eMailBestaetigungVerschicken = false;
    private boolean eMail2BestaetigungVerschicken = false;

    /**Standardwert False. Wird bei Anforderung über App auf true gesetzt - dann wird nur Code verschickt, nicht Link*/
    private boolean wurdeUeberAppAngefordert = false;

    /**Mail Bestätigungscode (wenn auf Registrierungs/Einstellungsseite eingegeben)*/
    private String eMailBestaetigungsCode = "";
    private String eMail2BestaetigungsCode = "";

    /**Falls das Passwort verändert wurde, steht hier anschließend das verschlüsselte Passwort drin (für Abspeichern
     * in App z.B.)
     */
    private String passwortVerschluesselt = "";

    private boolean esGibtVveraenderungenFuerBestaetigungsanzeige = false;

    /*+++++Werte für Bestätigungsseite++++++++++*/
    private boolean eMailVersandNeuRegistriert = false;
    private boolean eMailVersandDeRegistriert = false;

    private boolean neueEmailAdresseEingetragen = false;
    private String neueEmailAdresse = "";
    private boolean neueEmailAdresseAusgetragen = false;
    /**Registriert für den Versand, und E-Mail-Adresse noch nicht bestätigt*/
    private boolean emailNochNichtBestaetigt = false;

    private boolean neueEmail2AdresseEingetragen = false;
    private String neueEmail2Adresse = "";
    private boolean neueEmail2AdresseAusgetragen = false;
    private boolean email2NochNichtBestaetigt = false;

    private boolean dauerhaftesPasswortAktiviert = false;
    private boolean dauerhaftesPasswortDeAktiviert = false;
    private boolean dauerhaftesPasswortGeaendert = false;

    public void clearBestaetigungsseite() {
        eMailVersandNeuRegistriert = false;
        eMailVersandDeRegistriert = false;

        neueEmailAdresseEingetragen = false;
        neueEmailAdresse = "";
        neueEmailAdresseAusgetragen = false;
        emailNochNichtBestaetigt = false;

        neueEmail2AdresseEingetragen = false;
        neueEmail2Adresse = "";
        neueEmail2AdresseAusgetragen = false;
        email2NochNichtBestaetigt = false;

        dauerhaftesPasswortAktiviert = false;
        dauerhaftesPasswortDeAktiviert = false;
        dauerhaftesPasswortGeaendert = false;

    }

    /*************Standard getter/setter******************/

    public boolean isErstregistrierung() {
        return erstregistrierung;
    }

    public void setErstregistrierung(boolean erstregistrierung) {
        this.erstregistrierung = erstregistrierung;
    }

    public boolean isWurdeUeberAppAngefordert() {
        return wurdeUeberAppAngefordert;
    }

    public void setWurdeUeberAppAngefordert(boolean wurdeUeberAppAngefordert) {
        this.wurdeUeberAppAngefordert = wurdeUeberAppAngefordert;
    }

    public boolean iseMailBestaetigungVerschicken() {
        return eMailBestaetigungVerschicken;
    }

    public void seteMailBestaetigungVerschicken(boolean eMailBestaetigungVerschicken) {
        this.eMailBestaetigungVerschicken = eMailBestaetigungVerschicken;
    }

    public String geteMailBestaetigungsCode() {
        return eMailBestaetigungsCode;
    }

    public void seteMailBestaetigungsCode(String eMailBestaetigungsCode) {
        this.eMailBestaetigungsCode = eMailBestaetigungsCode;
    }

    public boolean iseMail2BestaetigungVerschicken() {
        return eMail2BestaetigungVerschicken;
    }

    public void seteMail2BestaetigungVerschicken(boolean eMail2BestaetigungVerschicken) {
        this.eMail2BestaetigungVerschicken = eMail2BestaetigungVerschicken;
    }

    public String geteMail2BestaetigungsCode() {
        return eMail2BestaetigungsCode;
    }

    public void seteMail2BestaetigungsCode(String eMail2BestaetigungsCode) {
        this.eMail2BestaetigungsCode = eMail2BestaetigungsCode;
    }

    public String getPasswortVerschluesselt() {
        return passwortVerschluesselt;
    }

    public void setPasswortVerschluesselt(String passwortVerschluesselt) {
        this.passwortVerschluesselt = passwortVerschluesselt;
    }

    public boolean iseMailVersandNeuRegistriert() {
        return eMailVersandNeuRegistriert;
    }

    public void seteMailVersandNeuRegistriert(boolean eMailVersandNeuRegistriert) {
        this.eMailVersandNeuRegistriert = eMailVersandNeuRegistriert;
    }

    public boolean isNeueEmailAdresseEingetragen() {
        CaBug.druckeLog("neueEmailAdresseEingetragen=" + neueEmailAdresseEingetragen,
                logDrucken, 10);
        return neueEmailAdresseEingetragen;
    }

    public void setNeueEmailAdresseEingetragen(boolean neueEmailAdresseEingetragen) {
        CaBug.druckeLog("neueEmailAdresseEingetragen=" + neueEmailAdresseEingetragen,
               logDrucken, 10);
        this.neueEmailAdresseEingetragen = neueEmailAdresseEingetragen;
    }

    public boolean isDauerhaftesPasswortAktiviert() {
        return dauerhaftesPasswortAktiviert;
    }

    public void setDauerhaftesPasswortAktiviert(boolean dauerhaftesPasswortAktiviert) {
        this.dauerhaftesPasswortAktiviert = dauerhaftesPasswortAktiviert;
    }

    public boolean isNeueEmail2AdresseEingetragen() {
        return neueEmail2AdresseEingetragen;
    }

    public void setNeueEmail2AdresseEingetragen(boolean neueEmail2AdresseEingetragen) {
        this.neueEmail2AdresseEingetragen = neueEmail2AdresseEingetragen;
    }

    public boolean iseMailVersandDeRegistriert() {
        return eMailVersandDeRegistriert;
    }

    public void seteMailVersandDeRegistriert(boolean eMailVersandDeRegistriert) {
        this.eMailVersandDeRegistriert = eMailVersandDeRegistriert;
    }

    public boolean isNeueEmailAdresseAusgetragen() {
        return neueEmailAdresseAusgetragen;
    }

    public void setNeueEmailAdresseAusgetragen(boolean neueEmailAdresseAusgetragen) {
        this.neueEmailAdresseAusgetragen = neueEmailAdresseAusgetragen;
    }

    public boolean isNeueEmail2AdresseAusgetragen() {
        return neueEmail2AdresseAusgetragen;
    }

    public void setNeueEmail2AdresseAusgetragen(boolean neueEmail2AdresseAusgetragen) {
        this.neueEmail2AdresseAusgetragen = neueEmail2AdresseAusgetragen;
    }

    public boolean isDauerhaftesPasswortDeAktiviert() {
        return dauerhaftesPasswortDeAktiviert;
    }

    public void setDauerhaftesPasswortDeAktiviert(boolean dauerhaftesPasswortDeAktiviert) {
        this.dauerhaftesPasswortDeAktiviert = dauerhaftesPasswortDeAktiviert;
    }

    public boolean isDauerhaftesPasswortGeaendert() {
        return dauerhaftesPasswortGeaendert;
    }

    public void setDauerhaftesPasswortGeaendert(boolean dauerhaftesPasswortGeaendert) {
        this.dauerhaftesPasswortGeaendert = dauerhaftesPasswortGeaendert;
    }

    public String getNeueEmailAdresse() {
        return neueEmailAdresse;
    }

    public void setNeueEmailAdresse(String neueEmailAdresse) {
        this.neueEmailAdresse = neueEmailAdresse;
    }

    public String getNeueEmail2Adresse() {
        return neueEmail2Adresse;
    }

    public void setNeueEmail2Adresse(String neueEmail2Adresse) {
        this.neueEmail2Adresse = neueEmail2Adresse;
    }

    public boolean isEmailNochNichtBestaetigt() {
        return emailNochNichtBestaetigt;
    }

    public void setEmailNochNichtBestaetigt(boolean emailNochNichtBestaetigt) {
        this.emailNochNichtBestaetigt = emailNochNichtBestaetigt;
    }

    public boolean isEmail2NochNichtBestaetigt() {
        return email2NochNichtBestaetigt;
    }

    public void setEmail2NochNichtBestaetigt(boolean email2NochNichtBestaetigt) {
        this.email2NochNichtBestaetigt = email2NochNichtBestaetigt;
    }

    public boolean isEsGibtVveraenderungenFuerBestaetigungsanzeige() {
        return esGibtVveraenderungenFuerBestaetigungsanzeige;
    }

    public void setEsGibtVveraenderungenFuerBestaetigungsanzeige(
            boolean esGibtVveraenderungenFuerBestaetigungsanzeige) {
        this.esGibtVveraenderungenFuerBestaetigungsanzeige = esGibtVveraenderungenFuerBestaetigungsanzeige;
    }

}
