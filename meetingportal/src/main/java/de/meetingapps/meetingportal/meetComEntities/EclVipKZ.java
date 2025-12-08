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

import java.util.regex.Pattern;

public class EclVipKZ {

    public String kuerzel = "";
    public String beschreibung = "";

    /**Aktion auslösen bei:
     * 1.	Erstzugang
     * 2.	Wiederzugang
     * 3.	Abgang
     * 4.	Vollmachtswechsel
     * 5.	Akkreditierung
     * 6.	Aufruf im Meldebestand
     * 7.	Aktionen in Meldebestand
     * 8.	Import
     * 9.	Aktionen in Online-Schnittstelle (1)
     * 10.	Weisungsveränderung
     * 11.	Wortmeldung
     */
    public int[] aktion = new int[31];

    /**Hinweise für die folgenden Text-Felder:
     * 
     * 01 = Eintritts-/Gastkarten-Nr
     * 02 = S-Nr
     * 03 = Name Aktionär / Gast
     * 04 = Vorname
     * 05 = Ort
     * 06 = Vertretername
     * 07 = Vertreterort
     * 08 = Aktion
     * 09 = Uhrzeit
     * 10 = Arbeitsplatz-Nr. von dem das ausgelöst wurde
     * 11 = Bezeichnung des Arbeitsplatzes
     * 12 = Angemeldeter Benutzer
     * 
     */

    /** Erfassendes Gesellschaftspersonal, z.B. am Zugangsschalter o.ä. - Standard Benutzer*/
    public String textErfassenderInternStandard = "";

    /** Erfassendes Gesellschaftspersonal, z.B. am Zugangsschalter o.ä. - Privilegierter Benutzer*/
    public String textErfassenderInternPrivilegiert = "";

    /** Erfassender Externer, d.h. konkret z.B. über Online-Schnittstelle - hierfür separater Monitor erforderlich*/
    public String textErfassenderExtern = "";

    /** Meldung an Selbstbedienungsgeräten - z.B. Online-HV, Zugangsschleusen, Smartphone, Portal*/
    public String textSelbstbedienung = "";

    /** "Detailliertes VIP-Monitoring" - separate Monitor-Anwendung*/
    public String textVIPMonitor = "";

    /** Message an internes Chat-System - Verteiler anzugeben*/
    public String textMCMessage = "";
    /** Verteiler zu textMCMessage - durch ; getrennt*/
    public String textMCMessageVerteiler = "";

    /** Anzeige / Meldung an Bühneninformationssystem - Verteiler und Schnittstelle noch zu regeln*/
    public String textBuehneninformation = "";

    /**Versand von Emails - Verteiler anzugeben*/
    public String textMail = "";
    /** Verteiler zu textMail - durch ; getrennt*/
    public String textMailVerteiler;

    /**Info, ob bei einer entsprechenden Meldung die Aktion fortgesetzt werden darf (1), oder nicht (0)
     * (0 kann mit entsprechender Berechtigung "overruled" werden)
     * */
    public int abbruchZwingendStandard = 0;

    /**Info, ob bei einer entsprechenden Meldung die Aktion fortgesetzt werden darf (1), oder nicht (0)
     * (0 kann mit entsprechender Berechtigung "overruled" werden)
     * */
    public int abbruchZwingendPrivilegiert = 0;

    /**Die folgenden Felder sind nur im Falle von "allgemeingültigen" VIP-KZ (d.h. solchen, die nicht an Meldungen geküpft sind, sondern immer überprüft werden) gefüllt
     * "normale" zuordenbare VIP-KZ haben bedingung=0*/

    /** 0 => "Normales" VIPKZ, das nur arbeitet wenn es konkret einer Meldung zugeordnet wurde
     * != 0 => allgemeingültiges VIPKZ, dass immer arbeitet - wenn die definierte Bedingung erfüllt ist:
     *  1 => größer als bestimmte Aktienzahl
     *  2 => Meldung hat Stimmrechtsausschluß
     *  3 => Vorname, Name in Feldern Name, Vorname enthalten (Aktionär oder Bevollmächtigter)
     *  4 => Sammelkarte/Listenkarte
     * */
    public int bedingung = 0;

    /** nur bei bedingung==0 verwendet*/
    public int aktienzahl = 0;

    /** nur bei bedingung==3 verwendet*/
    public String name = "";
    /** nur bei bedingung==3 verwendet*/
    public String vorname = "";

    /**Zu allgemeinen VIP-KZ: Liste der Ausschlüsse, bei denen KEINE
     * Aktion durchgeführt werden soll
     */
    public EclMeldungenVipKZAusgeblendet listeAusschluss[] = null;

    /**Nicht in DbVipKZ Datenbank: für die Auswertung selektiert / Deselektiert*/
    public int selektiert = 0;

    /**Nicht in DbVipKZ Datenbank: Messaging-Empfänger-Liste*/
    public String[] messagingAdressListe = null;

    /**Nicht in DbVipKZ Datenbank: Mail-Empfänger-Liste*/
    public String[] mailAdressListe = null;

    public EclVipKZ() {
        super();
        int i;
        for (i = 0; i <= 30; i++) {
            aktion[i] = 0;
        }
    }

    /** Kopieren des Objekts nach neu */
    public void copyTo(EclVipKZ neu) {
        neu.kuerzel = this.kuerzel;
        neu.beschreibung = this.beschreibung;
        neu.aktion = new int[31];
        int i;
        for (i = 0; i <= 30; i++) {
            neu.aktion[i] = this.aktion[i];
        }
        neu.textErfassenderInternStandard = this.textErfassenderInternStandard;
        neu.textErfassenderInternPrivilegiert = this.textErfassenderInternPrivilegiert;
        neu.textErfassenderExtern = this.textErfassenderExtern;
        neu.textSelbstbedienung = this.textSelbstbedienung;
        neu.textVIPMonitor = this.textVIPMonitor;
        neu.textMCMessage = this.textMCMessage;
        neu.textMCMessageVerteiler = this.textMCMessageVerteiler;
        neu.textBuehneninformation = this.textBuehneninformation;
        neu.textMail = this.textMail;
        neu.textMailVerteiler = this.textMailVerteiler;
        neu.abbruchZwingendStandard = this.abbruchZwingendStandard;
        neu.abbruchZwingendPrivilegiert = this.abbruchZwingendPrivilegiert;
        neu.bedingung = this.bedingung;
        neu.aktienzahl = this.aktienzahl;
        neu.name = this.name;
        neu.vorname = this.vorname;

    }

    /** Vergleicht das Objekt vipKZ mit der vipKZ vergleich
     * Wichtig: Es wird nur die "basis" verglichen, d.h. keine Anhängsel wie z.B.
     * ausgeschlossene Meldungen.
     * Verwendung: vor "aufwändigeren Operationen" Prüfen, ob Basis gespeichert werden muß
     * @param vergleich
     * @return
     */
    public boolean equalsTo(EclVipKZ vergleich) {

        if (!vergleich.kuerzel.equals(this.kuerzel)) {
            return false;
        }
        if (!vergleich.beschreibung.equals(this.beschreibung)) {
            return false;
        }
        int i;
        for (i = 1; i <= 30; i++) {
            if (vergleich.aktion[i] != this.aktion[i]) {
                return false;
            }
        }
        if (!vergleich.textErfassenderInternStandard.equals(this.textErfassenderInternStandard)) {
            return false;
        }
        if (!vergleich.textErfassenderInternPrivilegiert.equals(this.textErfassenderInternPrivilegiert)) {
            return false;
        }
        if (!vergleich.textErfassenderExtern.equals(this.textErfassenderExtern)) {
            return false;
        }
        if (!vergleich.textSelbstbedienung.equals(this.textSelbstbedienung)) {
            return false;
        }
        if (!vergleich.textVIPMonitor.equals(this.textVIPMonitor)) {
            return false;
        }
        if (!vergleich.textMCMessage.equals(this.textMCMessage)) {
            return false;
        }
        if (!vergleich.textMCMessageVerteiler.equals(this.textMCMessageVerteiler)) {
            return false;
        }
        if (!vergleich.textBuehneninformation.equals(this.textBuehneninformation)) {
            return false;
        }
        if (!vergleich.textMail.equals(this.textMail)) {
            return false;
        }
        if (!vergleich.textMailVerteiler.equals(this.textMailVerteiler)) {
            return false;
        }
        if (vergleich.abbruchZwingendStandard != this.abbruchZwingendStandard) {
            return false;
        }
        if (vergleich.abbruchZwingendPrivilegiert != this.abbruchZwingendPrivilegiert) {
            return false;
        }
        if (vergleich.bedingung != this.bedingung) {
            return false;
        }
        if (vergleich.aktienzahl != this.aktienzahl) {
            return false;
        }
        if (!vergleich.name.equals(this.name)) {
            return false;
        }
        if (!vergleich.vorname.equals(this.vorname)) {
            return false;
        }

        return true;
    }

    public void fuelleMessagingAdressListe() {

        if (this.textMCMessageVerteiler.isEmpty()) {
            this.messagingAdressListe = null;
            return;
        }
        this.messagingAdressListe = this.textMCMessageVerteiler.split(Pattern.quote(";"));
        return;

    }

    public void fuelleMailAdressListe() {

        if (this.textMailVerteiler.isEmpty()) {
            this.mailAdressListe = null;
            return;
        }
        this.mailAdressListe = this.textMailVerteiler.split(Pattern.quote(";"));
        return;

    }

}
