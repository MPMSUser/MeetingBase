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

/**Enthält Kennung, die in der App in der Kennungsliste enthalten ist (also
 * in der App zugeordnet wurde)
 */
public class EclAppZugeordneteKennungen {

    /**aus der App-Applikation*/
    public int interneIdent = 0;

    /**Wird für ggf. erforderliche Spezialfälle (Abgleich
     * der benötigten Mandanten-Liste) mit übergeben. Wird aber
     * nicht als Mandant in der Applikation verwendet!
     */
    public int mandant = 0;

    /**Siehe WELoginVerify:
     * 1 = Aktienregister (aktionärsnummer), 
     * 2 = Aktionärsmeldung ("Eintrittskarte- Inhaberaktien"), 
     * TODO #9 Konsolidieren - geht 2 Aktionärsmeldung überhaupt? Und 3?
     * 3= PersonNatJur
     */
    public int anmeldeKennungArt = 0;

    public String kennung = "";

    public String passwort = "";

    /**Falls anmeldeKennungArt==1: Ident aus dem Aktienregister*/
    public int aktienregisterIdent = 0;

    /**Gefüllt, wenn anmeldeKennungArt==1 oder ==3*/
    public int personNatJurIdent = 0;

    /**Art der Anmeldung:
     * 1 = angemeldet
     * 			(0 heißt:
     * 				für Aktienregisterkennung: eigener Bestand nicht angemeldet;
     * 				für PersonNatJur: nix mehr zugeordnet! Also z.B. Vollmacht oder EK storniert
     * 2 = EK auf mich ausgestellt - "Selbst"
     * 4 = Vertretung delegiert
     * (6=2+4 = EK zur Selbstvertretung, und zusätzlich delegiert)
     * 8 = BEvollmächtigt
     * (12=8+4 = Bevollmächtigt, und zusätzlich weiterdelegiert)
     * 16 = Gastkarte
     * 32=Nullbestand (keine Kombination mit anderen)
     * 64=EK auf mich als Vertreter ausgestellt - Vertreten
     */
    public int vertretungsArt = 0;

    /**Art der Delegation (Kombination möglich!)
     * 1= Dritter
     * 2= SRV
     * 4=KIAV
     * 8=Briefwahl*/
    public int delegationArt = 0;

    /**Name, Vorname Ort*/
    public String aktionaer = "";

    public long stimmen = 0;

    public int gattungNummer = 0;

    public String gattungBezeichnung = "";

    /*Nur bei ausgestellten EKs auf den App-Inhaber, bzw. zugeordneten*/
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    /*Nur bei ausgestellten EKs auf den App-Inhaber, bzw. zugeordneten*/
    public String stimmkartenIdent = "";

    /*Tbd*/
    public int letzterPraesenzstatus = 0;

    /**Nur für die Übertragung zwischen App und Server
     * In diesem Wert wird das Verarbeitungsergebnis
     * zurückgeliefert
     * -1 Kennung unbekannt
     * -2 Passwort unbekannt
     * -3 Eleminiert, weil doppelt
     */
    public int returnVerarbeitung = 0;

}
