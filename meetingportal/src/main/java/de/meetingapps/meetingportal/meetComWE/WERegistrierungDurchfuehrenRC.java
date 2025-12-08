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

/**Beschreibung der Eigenschaften: siehe BlAppVersionsabgleich*/
public class WERegistrierungDurchfuehrenRC extends WERootRC {

    /**Falls Passwort neu vergeben, dann hier verschlüsseltes Passwort (z.B. zum Speichern in der App)*/
    public String neuesPasswortVerschluesselt = "";

    public boolean esGibtVveraenderungenFuerBestaetigungsanzeige = false;

    /*+++++Werte für Bestätigungsseite++++++++++*/
    public boolean eMailVersandNeuRegistriert = false;
    public boolean eMailVersandDeRegistriert = false;

    public boolean neueEmailAdresseEingetragen = false;
    public String neueEmailAdresse = "";
    public boolean neueEmailAdresseAusgetragen = false;
    /**Registriert für den Versand, und E-Mail-Adresse noch nicht bestätigt*/
    public boolean emailNochNichtBestaetigt = false;

    public boolean neueEmail2AdresseEingetragen = false;
    public String neueEmail2Adresse = "";
    public boolean neueEmail2AdresseAusgetragen = false;
    public boolean email2NochNichtBestaetigt = false;

    public boolean dauerhaftesPasswortAktiviert = false;
    public boolean dauerhaftesPasswortDeAktiviert = false;
    public boolean dauerhaftesPasswortGeaendert = false;

}
