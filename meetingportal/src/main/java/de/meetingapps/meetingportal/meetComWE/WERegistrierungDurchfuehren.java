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

public class WERegistrierungDurchfuehren extends WERoot {

    /**False: Einstellungen ändern*/
    public boolean erstregistrierung = true;

    public String kommunikationssprache = "1"; /*eclTeilnehmerLoginM.aktienregisterZusatzM.kommunikationssprache*/
    public boolean eMailRegistrierungAnzeige = false; /*eclTeilnehmerLoginM.aktienregisterZusatzM.eMailRegistrierungAnzeige*/

    public String eMailFuerVersand = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.eMailFuerVersand*/
    public String eMailFuerVersandBestaetigen = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.eMailFuerVersandBestaetigen*/

    public String kontaktTelefonPrivat = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonPrivat*/
    public String kontaktTelefonGeschaeftlich = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonGeschaeftlich*/
    public String kontaktTelefonMobil = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonMobil*/
    public String kontaktTelefonFax = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonFax*/

    public boolean ausgewaehltVergabeEigenesPasswort = false; /*aDlgVariablen.ausgewaehltVergabeEigenesPasswort*/
    public boolean ausgewaehltNeuesPasswort = false; /*aDlgVariablen.neuesPasswort*/

    public String neuesPasswort = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.neuesPasswort*/
    public String neuesPasswortBestaetigung = ""; /*eclTeilnehmerLoginM.aktienregisterZusatzM.neuesPasswortBestaetigung*/

    public boolean hinweisAktionaersPortalBestaetigtAnzeige = false; /*eclTeilnehmerLoginM.aktienregisterZusatzM.hinweisAktionaersPortalBestaetigtAnzeige*/
    public boolean hinweisHVPortalBestaetigtAnzeige = false; /*eclTeilnehmerLoginM.aktienregisterZusatzM.hinweisHVPortalBestaetigtAnzeige*/

    /**Mail Bestätigungscode*/
    public String eMailBestaetigungsCode = "";

    /**Wenn true, dann wurde nicht weiter geklickt, sondern muß erneut ein Email-Registrierungscode
     * verschickt werden, dann zurück zur Registrierungsmaske*/
    public boolean eMailBestaetigungVerschicken = false;
}
