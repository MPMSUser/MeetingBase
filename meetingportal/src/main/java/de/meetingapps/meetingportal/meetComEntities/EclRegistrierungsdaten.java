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

/**Dies ist keine "übliche" Ecl-Klasse mit Abspeicherung in Datenbank,
 * sondern sie dient als "Sammler" für die Übermittlung der
 * Registrierungsdaten (Portal) - insbesondere über
 * Webservices hin zur App.
 * 
 * Registrierungsdaten für Erst-Registrierung bzw. Einstellungen des Aktionärs
 * im Portal bzw. in der App
 * */
public class EclRegistrierungsdaten {
    /**Bereich für Meldungen nach dem Login; z.B. - Emailadresse noch zu bestätigen*/
    public boolean anzeigeMeldung = false; /*Registrierung+Einstellung*/
    public boolean anzeigeMeldungsText1 = false; /*Registrierung+Einstellung*/
    public boolean anzeigeMeldungsText2 = false; /*Registrierung+Einstellung*/

    /**Bereich für eMailVersand+Registrierung anzeigen*/
    /*Nur für Maske "Einstellungen"*/
    public boolean emailbestaetigen = false; /*Einstellung*/
    public boolean email2bestaetigen = false; /*Einstellung*/

    /**Bereich für Passwort-Eigenvergabe anzeigen*/
    public boolean passwortBereitsVergeben = false; /*Registrierung+Einstellung*/
    public boolean ausgewaehltVergabeEigenesPasswort = false; /*Registrierung+Einstellung*/
    public boolean neuesPasswort = false; /*Registrierung+Einstellung*/

    /**Bereich für Hinweis Datenschutzerklaerung*/
    public boolean anzeigeHinweisDatenschutzerklaerung = false; /*Registrierung+Einstellung*/

    /**Bereich für Hinweis Aktionärsportal*/
    public boolean anzeigeHinweisAktionaersPortalBestaetigen = false; /*Registrierung+Einstellung*/

    /**Bereich für Hinweis HV-Portal*/
    public boolean anzeigeHinweisHVPortalBestaetigen = false; /*Registrierung+Einstellung*/

    /********Nur für Einstellungen Daten des Aktionärs, aus EclTeilnehmerLoginM.aktienregisterZusatzM******/
    public String tlKommunikationssprache = "";
    /**Email-Registrierungs-Feld, aufbereitet für Anzeigemaske*/
    public boolean tlEMailRegistrierungAnzeige = false;
    public String tlEMailFuerVersand = "";
    public String tlEMailFuerVersandBestaetigen = "";

    public String tlKontaktTelefonPrivat = "";
    public String tlKontaktTelefonGeschaeftlich = "";
    public String tlKontaktTelefonMobil = "";
    public String tlKontaktTelefonFax = "";

    public String[] eingeloggtePersonArray = null;

}
