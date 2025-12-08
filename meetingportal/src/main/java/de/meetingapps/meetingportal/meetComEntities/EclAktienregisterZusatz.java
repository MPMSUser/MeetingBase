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

@Deprecated
public class EclAktienregisterZusatz {

    /**Mandantennummer*/
    public int mandant = 0;

    /** eindeutiger Key für Aktienregistersatz (zusammen mit mandant), der unveränderlich ist. 
     * Wird in diesem Fall bei DBAktienregisterZusatz.insert nicht neu vergeben, sondern muß bereits gefüllt sein,
     * da dieses Feld der "Foreign-Key" zu EclAktienregisterEintrag ist.
     */
    public int aktienregisterIdent = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    public long db_version;

    /**Für Portal: Nutzungshinweise für das Aktionärsportal bestätigt
     * 0 = noch nichts bestätigt
     * 1 = bestätigt
     * 2 = zwar schon mal bestätigt, aber Hinweise haben sich geändert, dementsprechend nochmal bestätigen lassen
     */
    public int hinweisAktionaersPortalBestaetigt = 0;

    /**Für Portal: Nutzungshinweise für das HVportal bestätigt
     * 0 = noch nichts bestätigt
     * 1 = bestätigt
     * 2 = zwar schon mal bestätigt, aber Hinweise haben sich geändert, dementsprechend nochmal bestätigen lassen
     */
    public int hinweisHVPortalBestaetigt = 0;

    /**Für Portal: Zur Email-Registrierung für E-Versand bereits aufgefordert
     * 0 = noch nicht aufgefordert
     * 1 = aufgefordert, nicht registriert
     * 2 = zwar schon mal aufgefordert (z.B. im letzten Jahr), aber jetzt nochmal auffordern
     * (Hinweis zu 2: z.B. können bei einer neuen HV alle "1er" auf "2" gesetzt werden, so dass sie nochmal aufgefordert werden.
     * Damit läßt sich also abbilden, dass sie jährlich einmal zur Registrierung aufgefordert werden)
     * 99 = bereits registriert
     */
    public int eMailRegistrierung = 0;

    /**Zeitpunkt der ersten Email-Registrierung für den elektronischen Einladungsversand. Wird benötigt für Verlosung
     * Länge=19*/
    public String eMailRegistrierungErstZeitpunkt = "";

    /**Für Portal: Zur PasswortÄnderung bereits aufgefordert
     * 0 = noch nicht aufgefordert
     * 1 = aufgefordert, nicht geändert
     * 2 = zwar schon mal aufgefordert (z.B. im letzten Jahr), aber jetzt nochmal auffordern
     * (Hinweis zu 2: z.B. können bei einer neuen HV alle "1er" auf "2" gesetzt werden, so dass sie nochmal aufgefordert werden.
     * Damit läßt sich also abbilden, dass sie jährlich einmal zur eigenen Paswwortvergabe aufgefordert werden)
     * 98 = Passwort-Vergessen-Anforderung ist ausstehend
     * 99 = bereits eigenes Passwort
     */
    public int eigenesPasswort = 0;

    /**Key aus Passwort-Vergessen-Link
     * Länge=30
     */
    public String passwortVergessenLink = "";

    /**Passwort-Vergessen-Link erzeugt zum Zeitpunkt
     * Länge=19
     */
    public String passwortVergessenZeitpunkt = "";

    /**Kommunikationssprache
     * 1 = Deutsch
     * 2 = Englisch
     * weitere auf Anfrage :-)*/
    public int kommunikationssprache = 1;

    /**EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige Publikationsverteilungen
     * Länge = 80*/
    public String eMailFuerVersand = "";

    /**Email-Adresse wurde bereits bestätigt
     * 0 = noch nicht
     * 1 = Ja*/
    public int emailBestaetigt = 0;

    /**Key aus Email-Bestätigen-Link
     * Länge=30
     */
    public String emailBestaetigenLink = "";

    /**2. EMail-Adresse für elektronischen Einladungsversand und ggf. auch sonstige Publikationsverteilungen
     * Genaue Verwendung noch nicht ganz geklärt ... aber sicherheitshalber mal eingebaut, da im ku164-Portal vorhanden
     * Länge = 40*/
    public String eMail2FuerVersand = "";

    /**Email-Adresse wurde bereits bestätigt
     * 0 = noch nicht
     * 1 = Ja*/
    public int email2Bestaetigt = 0;

    /**Key aus Email-Bestätigen-Link
     * Länge=30
     */
    public String email2BestaetigenLink = "";

    /**Publikationen
     * Aktuell für 10 verschiedene Publikationen Platz in Datenbank. Position in Array entspricht der Publikationssnummer.
     * Der Wert jeder Position gibt wieder, ob bzw. wie diese Publikation an diesen Aktionär verschickt werden soll
     * 0 = kein Versand dieser Publikation
     * Bit 1 bis Bit 10: Zustellung über den diesem Bit zugeordneten Versandweg.*/
    public int[] publikationenZustellung = new int[10];

    /**Kontaktdaten - optional
     * Länge =40*/
    public String kontaktTelefonPrivat = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    public String kontaktTelefonGeschaeftlich = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    public String kontaktTelefonMobil = "";

    /**Kontaktdaten - optional
     * Länge =40*/
    public String kontaktTelefonFax = "";

}
