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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclTEinstellungenSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TEinstellungenSession implements Serializable {
    private static final long serialVersionUID = -3125590656834161813L;

    private @Inject EclParamM eclParamM;
    
    private int logDrucken=3;
    
    /*++++++++Steuerungsfelder+++++++++++*/

    private boolean erstregistrierung = false; /*true => Erstregistrierung; false=Einstellungen*/

    /**Irgendeine Meldung muß angezeigt werden*/
    private boolean anzeigeMeldung = false;
    /**Meldungstext 1 muß angezeigt werden - EMail 1 muß noch bestätigt werden*/
    private boolean anzeigeMeldungsText1 = false;
    /**Meldungstext 2 muß angezeigt werden - EMail 2 muß noch bestätigt werden*/
    private boolean anzeigeMeldungsText2 = false;

    /**Buttons für erneutes Verschicken des Email-Bestätigungslinks anzeigen*/
    private boolean emailbestaetigen = false;
    private boolean email2bestaetigen = false;

    /**Für den user wurde bereits ein eigenes Passwort vergeben*/
    private boolean passwortBereitsVergeben = false;

    /**Standardwert False. Wird bei Anforderung über App auf true gesetzt - dann wird nur Code verschickt, nicht Link*/
    private boolean wurdeUeberAppAngefordert = false;

    /*+++++++++Werte für Eingabe-Felder+++++++++++*/

    private String kommunikationssprache = "1";
    private boolean eVersandRegistrierung = false;

    private boolean eMailBestaetigungBeiAllenWillenserklaerungen = false;

    private boolean vergabeEigenesPasswort = false;
    private boolean passwortNeuVergeben = false;

    private String neuesPasswort = "";
    private String neuesPasswortBestaetigung = "";

    private String eMailFuerVersand = "";
    private String eMailFuerVersandBestaetigen = "";

    private String eMailInRemoteRegister="";
    private boolean eMailInRemoteRegisterWeichtAb=false;
    private boolean eMailNurInRemoteRegister=false;
    private boolean eMailInRemoteRegisterinArbeit=false;

    private String eMail2FuerVersand = "";
    private String eMail2FuerVersandBestaetigen = "";
    private boolean eMail2Verwenden = false;

    /**Mail Bestätigungscode (wenn auf Registrierungs/Einstellungsseite eingegeben) (speziell für App-aber auch sonst verwendbar)*/
    private String eMailBestaetigungsCode = "";
    private String eMail2BestaetigungsCode = "";

    /**Mail Bestätigungscode (wenn auf Einstellungs-Bestätigungsseite eingegeben) (speziell für App-aber auch sonst verwendbar)*/
    private String eMailBestaetigungsCodeBestaetigung = "";
    private String eMail2BestaetigungsCodeBestaetigung = "";

    private boolean hinweisHVPortalBestaetigt = false;
    private boolean hinweisAktionaersPortalBestaetigt = false;
    
    /**Eingabefelder  Bestätigung durchgeführt für Permanent-Portal*/
    private boolean hinweisWeitere1Bestaetigt = false;
    private boolean hinweisWeitere2Bestaetigt = false;

    /*+++++++++Rückgabefelder+++++++++++++++++++*/
    /**Falls das Passwort verändert wurde, steht hier anschließend das verschlüsselte Passwort drin (für Abspeichern
     * in App z.B.)
     */
    private String passwortVerschluesselt = "";

    /*++++++++++Werte für Bestätigungsseite+++++++++++++++++
     * Diese Werte werden:
     * > Vor Erstaufruf mit initialisere() auf false initialisert
     * > bei jedem Button in Einstellungsseite mit copyToNachEinstellungen in die Businesslogik übertragen
     * > nach der Verarbeitung mit copyFromVorBestaetigungsSeite wieder einkopiert*/
    private boolean quittungEMailVersandNeuRegistriert = false;
    private boolean quittungEMailVersandDeRegistriert = false;

    private boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = false;
    private boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = false;

    private boolean quittungNeueEmailAdresseEingetragen = false;
    private String quittungNeueEmailAdresse = "";
    private boolean quittungNeueEmailAdresseAusgetragen = false;
    /**Registriert für den Versand, und E-Mail-Adresse noch nicht bestätigt*/
    private boolean quittungEmailNochNichtBestaetigt = false;

    private boolean quittungNeueEmail2AdresseEingetragen = false;
    private String quittungNeueEmail2Adresse = "";
    private boolean quittungNeueEmail2AdresseAusgetragen = false;
    private boolean quittungEmail2NochNichtBestaetigt = false;

    private boolean quittungEmailWurdeBestaetigt=false;
    private boolean quittungEmail2WurdeBestaetigt=false;

    private boolean quittungDauerhaftesPasswortAktiviert = false;
    private boolean quittungDauerhaftesPasswortDeAktiviert = false;
    private boolean quittungDauerhaftesPasswortGeaendert = false;

    /**Muß aufgerufen werden, wenn die Einstellungs-Seite erstmalig gestartet wird.
     * 
     * Initialisiert die Quittungsfelder
     */
    public void initialisiere() {
        quittungEMailVersandNeuRegistriert = false;
        quittungEMailVersandDeRegistriert = false;
        quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = false;
        quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = false;
        quittungEmailNochNichtBestaetigt = false;
        quittungEmail2NochNichtBestaetigt = false;
        quittungDauerhaftesPasswortAktiviert = false;
        quittungDauerhaftesPasswortDeAktiviert = false;
        quittungDauerhaftesPasswortGeaendert = false;
        quittungNeueEmailAdresseAusgetragen = false;
        quittungNeueEmail2AdresseAusgetragen = false;
        quittungNeueEmailAdresseEingetragen = false;
        quittungNeueEmail2AdresseEingetragen = false;
        quittungEmailWurdeBestaetigt = false;
        quittungEmail2WurdeBestaetigt = false;
        quittungNeueEmailAdresse = "";
        quittungNeueEmail2Adresse = "";

    }

    /*+++++++++++++++++++Kopier-Funktionen von Business-Logik++++++++++++++++++++*/
    public void copyFromVorEinstellungen(BlTeilnehmerLoginNeu blTeilnehmerLogin) {
        anzeigeMeldung = blTeilnehmerLogin.anzeigeMeldung;
        anzeigeMeldungsText1 = blTeilnehmerLogin.anzeigeMeldungsText1;
        anzeigeMeldungsText2 = blTeilnehmerLogin.anzeigeMeldungsText2;

        emailbestaetigen = blTeilnehmerLogin.emailbestaetigen;
        email2bestaetigen = blTeilnehmerLogin.email2bestaetigen;

        kommunikationssprache = Integer.toString(blTeilnehmerLogin.kommunikationssprache);
        
        if (eclParamM.liefereEmailVersandRegistrierungOderWiderspruch()==1) {
            eVersandRegistrierung = blTeilnehmerLogin.eVersandRegistrierung;
        }
        else {
            eVersandRegistrierung = (!blTeilnehmerLogin.eVersandRegistrierung);
        }

        eMailBestaetigungBeiAllenWillenserklaerungen=blTeilnehmerLogin.eMailBestaetigungBeiAllenWillenserklaerungen;
        
        passwortBereitsVergeben = blTeilnehmerLogin.passwortBereitsVergeben;
        passwortNeuVergeben = false;
        vergabeEigenesPasswort = blTeilnehmerLogin.vergabeEigenesPasswort;

        neuesPasswort = "";
        neuesPasswortBestaetigung = "";
        
        eMailBestaetigungsCode="";
        eMail2BestaetigungsCode="";

        eMailFuerVersand = blTeilnehmerLogin.eMailFuerVersand;
        eMailFuerVersandBestaetigen = blTeilnehmerLogin.eMailFuerVersand;
        
        eMailInRemoteRegister = blTeilnehmerLogin.eMailInRemoteRegister;
        eMailInRemoteRegisterWeichtAb = blTeilnehmerLogin.eMailInRemoteRegisterWeichtAb;
        eMailNurInRemoteRegister = blTeilnehmerLogin.eMailNurInRemoteRegister;
        eMailInRemoteRegisterinArbeit = blTeilnehmerLogin.eMailInRemoteRegisterinArbeit;
        CaBug.druckeLog("eMailNurInRemoteRegister="+eMailNurInRemoteRegister, logDrucken, 10);
        CaBug.druckeLog("eMailInRemoteRegisterWeichtAb="+eMailInRemoteRegisterWeichtAb, logDrucken, 10);
        if (eMailNurInRemoteRegister) {
            /*Dann auch E-Mail aus Remote-Register in das Eingabefeld übertragen*/
            eMailFuerVersand = blTeilnehmerLogin.eMailInRemoteRegister;
            
        }
       
        eMail2FuerVersand = blTeilnehmerLogin.eMail2FuerVersand;
        eMail2FuerVersandBestaetigen = blTeilnehmerLogin.eMail2FuerVersand;
        eMail2Verwenden=blTeilnehmerLogin.eMail2Verwenden;

        hinweisHVPortalBestaetigt = false;
        hinweisAktionaersPortalBestaetigt = false;
        hinweisWeitere1Bestaetigt = false;
        hinweisWeitere2Bestaetigt = false;
   }

    public void copyToNachEinstellungen(BlTeilnehmerLoginNeu blTeilnehmerLogin) {
        blTeilnehmerLogin.kommunikationsspracheRaw = kommunikationssprache;
        if (eclParamM.liefereEmailVersandRegistrierungOderWiderspruch()==1) {
            blTeilnehmerLogin.eVersandRegistrierung = eVersandRegistrierung;
        }
        else {
            blTeilnehmerLogin.eVersandRegistrierung = (!eVersandRegistrierung);
        }
        blTeilnehmerLogin.eMailBestaetigungBeiAllenWillenserklaerungen = eMailBestaetigungBeiAllenWillenserklaerungen;

        blTeilnehmerLogin.passwortBereitsVergeben = passwortBereitsVergeben;
        blTeilnehmerLogin.vergabeEigenesPasswort = vergabeEigenesPasswort;
        blTeilnehmerLogin.passwortNeuVergeben = passwortNeuVergeben;

        blTeilnehmerLogin.neuesPasswort = neuesPasswort;
        blTeilnehmerLogin.neuesPasswortBestaetigung = neuesPasswortBestaetigung;

        blTeilnehmerLogin.eMailFuerVersand = eMailFuerVersand;
        blTeilnehmerLogin.eMailFuerVersandBestaetigen = eMailFuerVersandBestaetigen;
        
        blTeilnehmerLogin.eMailInRemoteRegister = eMailInRemoteRegister;
        blTeilnehmerLogin.eMailInRemoteRegisterWeichtAb = eMailInRemoteRegisterWeichtAb;
        blTeilnehmerLogin.eMailNurInRemoteRegister = eMailNurInRemoteRegister;
        blTeilnehmerLogin.eMailInRemoteRegisterinArbeit = eMailInRemoteRegisterinArbeit;
      
        blTeilnehmerLogin.eMail2FuerVersand = eMail2FuerVersand;
        blTeilnehmerLogin.eMail2FuerVersandBestaetigen = eMail2FuerVersandBestaetigen;

        blTeilnehmerLogin.emailbestaetigen = emailbestaetigen;
        blTeilnehmerLogin.email2bestaetigen = email2bestaetigen;
        blTeilnehmerLogin.eMail2Verwenden=eMail2Verwenden;


        blTeilnehmerLogin.eMailBestaetigungsCode = eMailBestaetigungsCode;
        blTeilnehmerLogin.eMail2BestaetigungsCode = eMail2BestaetigungsCode;

        blTeilnehmerLogin.hinweisHVPortalBestaetigt = hinweisHVPortalBestaetigt;
        blTeilnehmerLogin.hinweisAktionaersPortalBestaetigt = hinweisAktionaersPortalBestaetigt;
        blTeilnehmerLogin.hinweisWeitere1Bestaetigt = hinweisWeitere1Bestaetigt;
        blTeilnehmerLogin.hinweisWeitere2Bestaetigt = hinweisWeitere2Bestaetigt;

        /*Fortschreibung der Werte für Bestätigungsseite*/
        blTeilnehmerLogin.quittungEMailVersandNeuRegistriert = quittungEMailVersandNeuRegistriert;
        blTeilnehmerLogin.quittungEMailVersandDeRegistriert = quittungEMailVersandDeRegistriert;
        blTeilnehmerLogin.quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert;
        blTeilnehmerLogin.quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert;
        blTeilnehmerLogin.quittungEmailNochNichtBestaetigt = quittungEmailNochNichtBestaetigt;
        blTeilnehmerLogin.quittungEmail2NochNichtBestaetigt = quittungEmail2NochNichtBestaetigt;

        blTeilnehmerLogin.quittungEmailWurdeBestaetigt = quittungEmailWurdeBestaetigt;
        blTeilnehmerLogin.quittungEmail2WurdeBestaetigt = quittungEmail2WurdeBestaetigt;
        
        blTeilnehmerLogin.quittungDauerhaftesPasswortAktiviert = quittungDauerhaftesPasswortAktiviert;
        blTeilnehmerLogin.quittungDauerhaftesPasswortDeAktiviert = quittungDauerhaftesPasswortDeAktiviert;
        blTeilnehmerLogin.quittungDauerhaftesPasswortGeaendert = quittungDauerhaftesPasswortGeaendert;
        blTeilnehmerLogin.quittungNeueEmailAdresseAusgetragen = quittungNeueEmailAdresseAusgetragen;
        blTeilnehmerLogin.quittungNeueEmail2AdresseAusgetragen = quittungNeueEmail2AdresseAusgetragen;
        blTeilnehmerLogin.quittungNeueEmailAdresseEingetragen = quittungNeueEmailAdresseEingetragen;
        blTeilnehmerLogin.quittungNeueEmail2AdresseEingetragen = quittungNeueEmail2AdresseEingetragen;
        blTeilnehmerLogin.quittungNeueEmailAdresse = quittungNeueEmailAdresse;
        blTeilnehmerLogin.quittungNeueEmail2Adresse = quittungNeueEmail2Adresse;

    }

    public void copyFromNachEinstellungenWeiter(BlTeilnehmerLoginNeu blTeilnehmerLogin) {
        blTeilnehmerLogin.passwortVerschluesselt = passwortVerschluesselt;
    }

    public void copyFromVorBestaetigungsSeite(BlTeilnehmerLoginNeu blTeilnehmerLogin) {
        quittungEMailVersandNeuRegistriert = blTeilnehmerLogin.quittungEMailVersandNeuRegistriert;
        quittungEMailVersandDeRegistriert = blTeilnehmerLogin.quittungEMailVersandDeRegistriert;
        quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = blTeilnehmerLogin.quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert;
        quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = blTeilnehmerLogin.quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert;
        quittungEmailNochNichtBestaetigt = blTeilnehmerLogin.quittungEmailNochNichtBestaetigt;
        quittungEmail2NochNichtBestaetigt = blTeilnehmerLogin.quittungEmail2NochNichtBestaetigt;
        quittungEmailWurdeBestaetigt = blTeilnehmerLogin.quittungEmailWurdeBestaetigt;
        quittungEmail2WurdeBestaetigt = blTeilnehmerLogin.quittungEmail2WurdeBestaetigt;
       quittungDauerhaftesPasswortAktiviert = blTeilnehmerLogin.quittungDauerhaftesPasswortAktiviert;
        quittungDauerhaftesPasswortDeAktiviert = blTeilnehmerLogin.quittungDauerhaftesPasswortDeAktiviert;
        quittungDauerhaftesPasswortGeaendert = blTeilnehmerLogin.quittungDauerhaftesPasswortGeaendert;
        quittungNeueEmailAdresseAusgetragen = blTeilnehmerLogin.quittungNeueEmailAdresseAusgetragen;
        quittungNeueEmailAdresseEingetragen = blTeilnehmerLogin.quittungNeueEmailAdresseEingetragen;
        quittungNeueEmail2AdresseAusgetragen = blTeilnehmerLogin.quittungNeueEmail2AdresseAusgetragen;
        quittungNeueEmail2AdresseEingetragen = blTeilnehmerLogin.quittungNeueEmail2AdresseEingetragen;
        quittungNeueEmailAdresse = blTeilnehmerLogin.quittungNeueEmailAdresse;
        quittungNeueEmail2Adresse = blTeilnehmerLogin.quittungNeueEmail2Adresse;
    }

    
    
    /**+++++++++++++++++Kopierfunktionen für Webservice*********************/
    public void copyToEclTEinstellungenSession(EclTEinstellungenSession eclTEinstellungenSession) {
        eclTEinstellungenSession.erstregistrierung=erstregistrierung;
        
        eclTEinstellungenSession.anzeigeMeldung=anzeigeMeldung;
        eclTEinstellungenSession.anzeigeMeldungsText1=anzeigeMeldungsText1;
        eclTEinstellungenSession.anzeigeMeldungsText2=anzeigeMeldungsText2;
        
        eclTEinstellungenSession.emailbestaetigen=emailbestaetigen;
        eclTEinstellungenSession.email2bestaetigen=email2bestaetigen;
        
        eclTEinstellungenSession.passwortBereitsVergeben=passwortBereitsVergeben;
        
        eclTEinstellungenSession.kommunikationssprache=kommunikationssprache;
        eclTEinstellungenSession.eVersandRegistrierung=eVersandRegistrierung;
        eclTEinstellungenSession.eMailBestaetigungBeiAllenWillenserklaerungen=eMailBestaetigungBeiAllenWillenserklaerungen;
        
        eclTEinstellungenSession.vergabeEigenesPasswort=vergabeEigenesPasswort;
        eclTEinstellungenSession.passwortNeuVergeben=passwortNeuVergeben;
        
        eclTEinstellungenSession.neuesPasswort=neuesPasswort;
        eclTEinstellungenSession.neuesPasswortBestaetigung=neuesPasswortBestaetigung;
        
        eclTEinstellungenSession.eMailFuerVersand=eMailFuerVersand;
        eclTEinstellungenSession.eMailFuerVersandBestaetigen=eMailFuerVersandBestaetigen;
        eclTEinstellungenSession.eMail2FuerVersand=eMail2FuerVersand;
        eclTEinstellungenSession.eMail2FuerVersandBestaetigen=eMail2FuerVersandBestaetigen;

        eclTEinstellungenSession.eMail2Verwenden=eMail2Verwenden;
        eclTEinstellungenSession.eMailBestaetigungsCode=eMailBestaetigungsCode;
        eclTEinstellungenSession.eMail2BestaetigungsCode=eMail2BestaetigungsCode;
        
        eclTEinstellungenSession.hinweisHVPortalBestaetigt=hinweisHVPortalBestaetigt;
        eclTEinstellungenSession.hinweisAktionaersPortalBestaetigt=hinweisAktionaersPortalBestaetigt;
//        eclTEinstellungenSession.hinweisWeitere1Bestaetigt=hinweisWeitere1Bestaetigt;
//        eclTEinstellungenSession.hinweisWeitere2Bestaetigt=hinweisWeitere2Bestaetigt;
       
        eclTEinstellungenSession.passwortVerschluesselt=passwortVerschluesselt;
        
        eclTEinstellungenSession.quittungEMailVersandNeuRegistriert=quittungEMailVersandNeuRegistriert;
        eclTEinstellungenSession.quittungEMailVersandDeRegistriert=quittungEMailVersandDeRegistriert;

        eclTEinstellungenSession.quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert=quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert;
        eclTEinstellungenSession.quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert=quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert;

        eclTEinstellungenSession.quittungNeueEmailAdresseEingetragen=quittungNeueEmailAdresseEingetragen;
        eclTEinstellungenSession.quittungNeueEmailAdresse=quittungNeueEmailAdresse;
        eclTEinstellungenSession.quittungNeueEmailAdresseAusgetragen=quittungNeueEmailAdresseAusgetragen;
        eclTEinstellungenSession.quittungEmailNochNichtBestaetigt=quittungEmailNochNichtBestaetigt;
        
        eclTEinstellungenSession.quittungNeueEmail2AdresseEingetragen=quittungNeueEmail2AdresseEingetragen;
        eclTEinstellungenSession.quittungNeueEmail2Adresse=quittungNeueEmail2Adresse;
        eclTEinstellungenSession.quittungNeueEmail2AdresseAusgetragen=quittungNeueEmail2AdresseAusgetragen;
        eclTEinstellungenSession.quittungEmail2NochNichtBestaetigt=quittungEmail2NochNichtBestaetigt;
        
        eclTEinstellungenSession.quittungDauerhaftesPasswortAktiviert=quittungDauerhaftesPasswortAktiviert;
        eclTEinstellungenSession.quittungDauerhaftesPasswortDeAktiviert=quittungDauerhaftesPasswortDeAktiviert;
        eclTEinstellungenSession.quittungDauerhaftesPasswortGeaendert=quittungDauerhaftesPasswortGeaendert;

        /*TODO:
         * quittungEmailWurdeBestaetigt, quittungEmail2WurdeBestaetigt
         */
    }
    /*************Standard getter/setter******************/

    public boolean isErstregistrierung() {
        return erstregistrierung;
    }

    public void setErstregistrierung(boolean erstregistrierung) {
        this.erstregistrierung = erstregistrierung;
    }

    public String geteMailBestaetigungsCode() {
        return eMailBestaetigungsCode;
    }

    public void seteMailBestaetigungsCode(String eMailBestaetigungsCode) {
        this.eMailBestaetigungsCode = eMailBestaetigungsCode;
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

    public boolean isAnzeigeMeldung() {
        return anzeigeMeldung;
    }

    public void setAnzeigeMeldung(boolean anzeigeMeldung) {
        this.anzeigeMeldung = anzeigeMeldung;
    }

    public boolean isAnzeigeMeldungsText1() {
        return anzeigeMeldungsText1;
    }

    public void setAnzeigeMeldungsText1(boolean anzeigeMeldungsText1) {
        this.anzeigeMeldungsText1 = anzeigeMeldungsText1;
    }

    public boolean isAnzeigeMeldungsText2() {
        return anzeigeMeldungsText2;
    }

    public void setAnzeigeMeldungsText2(boolean anzeigeMeldungsText2) {
        this.anzeigeMeldungsText2 = anzeigeMeldungsText2;
    }

    public boolean isEmailbestaetigen() {
        return emailbestaetigen;
    }

    public void setEmailbestaetigen(boolean emailbestaetigen) {
        this.emailbestaetigen = emailbestaetigen;
    }

    public boolean isEmail2bestaetigen() {
        return email2bestaetigen;
    }

    public void setEmail2bestaetigen(boolean email2bestaetigen) {
        this.email2bestaetigen = email2bestaetigen;
    }

    public String getKommunikationssprache() {
        return kommunikationssprache;
    }

    public void setKommunikationssprache(String kommunikationssprache) {
        this.kommunikationssprache = kommunikationssprache;
    }

    public boolean iseVersandRegistrierung() {
        return eVersandRegistrierung;
    }

    public void seteVersandRegistrierung(boolean eVersandRegistrierung) {
        this.eVersandRegistrierung = eVersandRegistrierung;
    }

    public boolean isPasswortBereitsVergeben() {
        return passwortBereitsVergeben;
    }

    public void setPasswortBereitsVergeben(boolean passwortBereitsVergeben) {
        this.passwortBereitsVergeben = passwortBereitsVergeben;
    }

    public boolean isVergabeEigenesPasswort() {
        return vergabeEigenesPasswort;
    }

    public void setVergabeEigenesPasswort(boolean vergabeEigenesPasswort) {
        this.vergabeEigenesPasswort = vergabeEigenesPasswort;
    }

    public String getNeuesPasswort() {
        return neuesPasswort;
    }

    public void setNeuesPasswort(String neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }

    public String getNeuesPasswortBestaetigung() {
        return neuesPasswortBestaetigung;
    }

    public void setNeuesPasswortBestaetigung(String neuesPasswortBestaetigung) {
        this.neuesPasswortBestaetigung = neuesPasswortBestaetigung;
    }

    public boolean isPasswortNeuVergeben() {
        return passwortNeuVergeben;
    }

    public void setPasswortNeuVergeben(boolean passwortNeuVergeben) {
        this.passwortNeuVergeben = passwortNeuVergeben;
    }

    public String geteMailFuerVersand() {
        return eMailFuerVersand;
    }

    public void seteMailFuerVersand(String eMailFuerVersand) {
        this.eMailFuerVersand = eMailFuerVersand;
    }

    public String geteMail2FuerVersand() {
        return eMail2FuerVersand;
    }

    public void seteMail2FuerVersand(String eMail2FuerVersand) {
        this.eMail2FuerVersand = eMail2FuerVersand;
    }

    public String geteMailFuerVersandBestaetigen() {
        return eMailFuerVersandBestaetigen;
    }

    public void seteMailFuerVersandBestaetigen(String eMailFuerVersandBestaetigen) {
        this.eMailFuerVersandBestaetigen = eMailFuerVersandBestaetigen;
    }

    public String geteMail2FuerVersandBestaetigen() {
        return eMail2FuerVersandBestaetigen;
    }

    public void seteMail2FuerVersandBestaetigen(String eMail2FuerVersandBestaetigen) {
        this.eMail2FuerVersandBestaetigen = eMail2FuerVersandBestaetigen;
    }

    public boolean isHinweisHVPortalBestaetigt() {
        return hinweisHVPortalBestaetigt;
    }

    public void setHinweisHVPortalBestaetigt(boolean hinweisHVPortalBestaetigt) {
        this.hinweisHVPortalBestaetigt = hinweisHVPortalBestaetigt;
    }

    public boolean isHinweisAktionaersPortalBestaetigt() {
        return hinweisAktionaersPortalBestaetigt;
    }

    public void setHinweisAktionaersPortalBestaetigt(boolean hinweisAktionaersPortalBestaetigt) {
        this.hinweisAktionaersPortalBestaetigt = hinweisAktionaersPortalBestaetigt;
    }

    public boolean isQuittungEMailVersandNeuRegistriert() {
        return quittungEMailVersandNeuRegistriert;
    }

    public void setQuittungEMailVersandNeuRegistriert(boolean quittungEMailVersandNeuRegistriert) {
        this.quittungEMailVersandNeuRegistriert = quittungEMailVersandNeuRegistriert;
    }

    public boolean isQuittungEMailVersandDeRegistriert() {
        return quittungEMailVersandDeRegistriert;
    }

    public void setQuittungEMailVersandDeRegistriert(boolean quittungEMailVersandDeRegistriert) {
        this.quittungEMailVersandDeRegistriert = quittungEMailVersandDeRegistriert;
    }

    public boolean isQuittungNeueEmailAdresseEingetragen() {
        return quittungNeueEmailAdresseEingetragen;
    }

    public void setQuittungNeueEmailAdresseEingetragen(boolean quittungNeueEmailAdresseEingetragen) {
        this.quittungNeueEmailAdresseEingetragen = quittungNeueEmailAdresseEingetragen;
    }

    public String getQuittungNeueEmailAdresse() {
        return quittungNeueEmailAdresse;
    }

    public void setQuittungNeueEmailAdresse(String quittungNeueEmailAdresse) {
        this.quittungNeueEmailAdresse = quittungNeueEmailAdresse;
    }

    public boolean isQuittungNeueEmailAdresseAusgetragen() {
        return quittungNeueEmailAdresseAusgetragen;
    }

    public void setQuittungNeueEmailAdresseAusgetragen(boolean quittungNeueEmailAdresseAusgetragen) {
        this.quittungNeueEmailAdresseAusgetragen = quittungNeueEmailAdresseAusgetragen;
    }

    public boolean isQuittungEmailNochNichtBestaetigt() {
        return quittungEmailNochNichtBestaetigt;
    }

    public void setQuittungEmailNochNichtBestaetigt(boolean quittungEmailNochNichtBestaetigt) {
        this.quittungEmailNochNichtBestaetigt = quittungEmailNochNichtBestaetigt;
    }

    public boolean isQuittungNeueEmail2AdresseEingetragen() {
        return quittungNeueEmail2AdresseEingetragen;
    }

    public void setQuittungNeueEmail2AdresseEingetragen(boolean quittungNeueEmail2AdresseEingetragen) {
        this.quittungNeueEmail2AdresseEingetragen = quittungNeueEmail2AdresseEingetragen;
    }

    public String getQuittungNeueEmail2Adresse() {
        return quittungNeueEmail2Adresse;
    }

    public void setQuittungNeueEmail2Adresse(String quittungNeueEmail2Adresse) {
        this.quittungNeueEmail2Adresse = quittungNeueEmail2Adresse;
    }

    public boolean isQuittungNeueEmail2AdresseAusgetragen() {
        return quittungNeueEmail2AdresseAusgetragen;
    }

    public void setQuittungNeueEmail2AdresseAusgetragen(boolean quittungNeueEmail2AdresseAusgetragen) {
        this.quittungNeueEmail2AdresseAusgetragen = quittungNeueEmail2AdresseAusgetragen;
    }

    public boolean isQuittungEmail2NochNichtBestaetigt() {
        return quittungEmail2NochNichtBestaetigt;
    }

    public void setQuittungEmail2NochNichtBestaetigt(boolean quittungEmail2NochNichtBestaetigt) {
        this.quittungEmail2NochNichtBestaetigt = quittungEmail2NochNichtBestaetigt;
    }

    public boolean isQuittungDauerhaftesPasswortAktiviert() {
        return quittungDauerhaftesPasswortAktiviert;
    }

    public void setQuittungDauerhaftesPasswortAktiviert(boolean quittungDauerhaftesPasswortAktiviert) {
        this.quittungDauerhaftesPasswortAktiviert = quittungDauerhaftesPasswortAktiviert;
    }

    public boolean isQuittungDauerhaftesPasswortDeAktiviert() {
        return quittungDauerhaftesPasswortDeAktiviert;
    }

    public void setQuittungDauerhaftesPasswortDeAktiviert(boolean quittungDauerhaftesPasswortDeAktiviert) {
        this.quittungDauerhaftesPasswortDeAktiviert = quittungDauerhaftesPasswortDeAktiviert;
    }

    public boolean isQuittungDauerhaftesPasswortGeaendert() {
        return quittungDauerhaftesPasswortGeaendert;
    }

    public void setQuittungDauerhaftesPasswortGeaendert(boolean quittungDauerhaftesPasswortGeaendert) {
        this.quittungDauerhaftesPasswortGeaendert = quittungDauerhaftesPasswortGeaendert;
    }

    public boolean isWurdeUeberAppAngefordert() {
        return wurdeUeberAppAngefordert;
    }

    public void setWurdeUeberAppAngefordert(boolean wurdeUeberAppAngefordert) {
        this.wurdeUeberAppAngefordert = wurdeUeberAppAngefordert;
    }

    public boolean isQuittungEmailWurdeBestaetigt() {
        return quittungEmailWurdeBestaetigt;
    }

    public void setQuittungEmailWurdeBestaetigt(boolean quittungEmailWurdeBestaetigt) {
        this.quittungEmailWurdeBestaetigt = quittungEmailWurdeBestaetigt;
    }

    public boolean isQuittungEmail2WurdeBestaetigt() {
        return quittungEmail2WurdeBestaetigt;
    }

    public void setQuittungEmail2WurdeBestaetigt(boolean quittungEmail2WurdeBestaetigt) {
        this.quittungEmail2WurdeBestaetigt = quittungEmail2WurdeBestaetigt;
    }

    public String geteMailBestaetigungsCodeBestaetigung() {
        return eMailBestaetigungsCodeBestaetigung;
    }

    public void seteMailBestaetigungsCodeBestaetigung(String eMailBestaetigungsCodeBestaetigung) {
        this.eMailBestaetigungsCodeBestaetigung = eMailBestaetigungsCodeBestaetigung;
    }

    public String geteMail2BestaetigungsCodeBestaetigung() {
        return eMail2BestaetigungsCodeBestaetigung;
    }

    public void seteMail2BestaetigungsCodeBestaetigung(String eMail2BestaetigungsCodeBestaetigung) {
        this.eMail2BestaetigungsCodeBestaetigung = eMail2BestaetigungsCodeBestaetigung;
    }

    public boolean isHinweisWeitere1Bestaetigt() {
        return hinweisWeitere1Bestaetigt;
    }

    public void setHinweisWeitere1Bestaetigt(boolean hinweisWeitere1Bestaetigt) {
        this.hinweisWeitere1Bestaetigt = hinweisWeitere1Bestaetigt;
    }

    public boolean isHinweisWeitere2Bestaetigt() {
        return hinweisWeitere2Bestaetigt;
    }

    public void setHinweisWeitere2Bestaetigt(boolean hinweisWeitere2Bestaetigt) {
        this.hinweisWeitere2Bestaetigt = hinweisWeitere2Bestaetigt;
    }

    public String geteMailInRemoteRegister() {
        return eMailInRemoteRegister;
    }

    public void seteMailInRemoteRegister(String eMailInRemoteRegister) {
        this.eMailInRemoteRegister = eMailInRemoteRegister;
    }

    public boolean iseMailInRemoteRegisterWeichtAb() {
        return eMailInRemoteRegisterWeichtAb;
    }

    public void seteMailInRemoteRegisterWeichtAb(boolean eMailInRemoteRegisterWeichtAb) {
        this.eMailInRemoteRegisterWeichtAb = eMailInRemoteRegisterWeichtAb;
    }

    public boolean iseMailNurInRemoteRegister() {
        return eMailNurInRemoteRegister;
    }

    public void seteMailNurInRemoteRegister(boolean eMailNurInRemoteRegister) {
        this.eMailNurInRemoteRegister = eMailNurInRemoteRegister;
    }

    public boolean iseMailInRemoteRegisterinArbeit() {
        return eMailInRemoteRegisterinArbeit;
    }

    public void seteMailInRemoteRegisterinArbeit(boolean eMailInRemoteRegisterinArbeit) {
        this.eMailInRemoteRegisterinArbeit = eMailInRemoteRegisterinArbeit;
    }

    public boolean iseMailBestaetigungBeiAllenWillenserklaerungen() {
        return eMailBestaetigungBeiAllenWillenserklaerungen;
    }

    public void seteMailBestaetigungBeiAllenWillenserklaerungen(boolean eMailBestaetigungBeiAllenWillenserklaerungen) {
        this.eMailBestaetigungBeiAllenWillenserklaerungen = eMailBestaetigungBeiAllenWillenserklaerungen;
    }

    public boolean isQuittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert() {
        return quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert;
    }

    public void setQuittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert(boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert) {
        this.quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert;
    }

    public boolean isQuittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert() {
        return quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert;
    }

    public void setQuittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert(boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert) {
        this.quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert;
    }

    public boolean iseMail2Verwenden() {
        return eMail2Verwenden;
    }

    public void seteMail2Verwenden(boolean eMail2Verwenden) {
        this.eMail2Verwenden = eMail2Verwenden;
    }


}
