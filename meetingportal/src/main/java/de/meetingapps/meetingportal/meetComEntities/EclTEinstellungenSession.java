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

/**siehe TEinstellungenSession*/
public class EclTEinstellungenSession {

    public boolean erstregistrierung = false; /*true => Erstregistrierung; false=Einstellungen*/

    /**Irgendeine Meldung muß angezeigt werden*/
    public boolean anzeigeMeldung = false;
    /**Meldungstext 1 muß angezeigt werden - EMail 1 muß noch bestätigt werden*/
    public boolean anzeigeMeldungsText1 = false;
    /**Meldungstext 2 muß angezeigt werden - EMail 2 muß noch bestätigt werden*/
    public boolean anzeigeMeldungsText2 = false;

    /**Buttons für erneutes Verschicken des Email-Bestätigungslinks anzeigen*/
    public boolean emailbestaetigen = false;
    public boolean email2bestaetigen = false;

    /**Für den user wurde bereits ein eigenes Passwort vergeben*/
    public boolean passwortBereitsVergeben = false;

    /*+++++++++Werte für Eingabe-Felder+++++++++++*/

    public String kommunikationssprache = "1";
    public boolean eVersandRegistrierung = false;
    public boolean eMailBestaetigungBeiAllenWillenserklaerungen = false;

    public boolean vergabeEigenesPasswort = false;
    public boolean passwortNeuVergeben = false;

    public String neuesPasswort = "";
    public String neuesPasswortBestaetigung = "";

    public String eMailFuerVersand = "";
    public String eMailFuerVersandBestaetigen = "";
    public boolean eMail2Verwenden = false;

    public String eMail2FuerVersand = "";
    public String eMail2FuerVersandBestaetigen = "";

    /**Mail Bestätigungscode (wenn auf Registrierungs/Einstellungsseite eingegeben) (speziell für App-aber auch sonst verwendbar)*/
    public String eMailBestaetigungsCode = "";
    public String eMail2BestaetigungsCode = "";

    public boolean hinweisHVPortalBestaetigt = false;
    public boolean hinweisAktionaersPortalBestaetigt = false;

    /*+++++++++Rückgabefelder+++++++++++++++++++*/
    /**Falls das Passwort verändert wurde, steht hier anschließend das verschlüsselte Passwort drin (für Abspeichern
     * in App z.B.)
     */
    public String passwortVerschluesselt = "";

    /*++++++++++Werte für Bestätigungsseite+++++++++++++++++
     * Diese Werte werden:
     * > Vor Erstaufruf mit initialisere() auf false initialisert
     * > bei jedem Button in Einstellungsseite mit copyToNachEinstellungen in die Businesslogik übertragen
     * > nach der Verarbeitung mit copyFromVorBestaetigungsSeite wieder einkopiert*/
    public boolean quittungEMailVersandNeuRegistriert = false;
    public boolean quittungEMailVersandDeRegistriert = false;

    public boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenNeuRegistriert = false;
    public boolean quittungEMailBestaetigungBeiAllenWillenserklaerungenDeRegistriert = false;

    public boolean quittungNeueEmailAdresseEingetragen = false;
    public String quittungNeueEmailAdresse = "";
    public boolean quittungNeueEmailAdresseAusgetragen = false;
    /**Registriert für den Versand, und E-Mail-Adresse noch nicht bestätigt*/
    public boolean quittungEmailNochNichtBestaetigt = false;

    public boolean quittungNeueEmail2AdresseEingetragen = false;
    public String quittungNeueEmail2Adresse = "";
    public boolean quittungNeueEmail2AdresseAusgetragen = false;
    public boolean quittungEmail2NochNichtBestaetigt = false;

    public boolean quittungDauerhaftesPasswortAktiviert = false;
    public boolean quittungDauerhaftesPasswortDeAktiviert = false;
    public boolean quittungDauerhaftesPasswortGeaendert = false;

}
