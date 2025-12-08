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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

/**In dieser Klasse sind die Portalparameter, die NICHT an die App übermittelt werden dürfen!*/

public class ParamPortalServer implements Serializable {
    private static final long serialVersionUID = 2145750403617331975L;

    /***************************Phasen*********************************************/

    /**LEN=40*/
    public String[] phasenNamen = new String[21]; //221 bis 240

    /***************Steuerung Login, Passwort, Passwortvergessen***********************************************/

    /**Mail-Empfänger für Mails bei Passwortvergessen nach Verfahren 2 und 4.
     * Wird in parameterLocal gespeichert
     * 
     * Wurde ersetzt durch "Aufgaben"
     */
    @Deprecated
    public String passwortVergessenMailAnAdresse = "";

    /**Falls 1, wird auf der Login-Seite der Text geteilt in
     * > Textbereich 1
     * > Button IOS
     * > Button Android
     * > Textbereich 2 
     */
    public int appInstallButtonsAnzeigen = 1; //OK 243

    /*************Link, über den Portal aufgerufen werden kann.*******************************************/

    /**Zwischen Teil 1 und Teil 2 Mandantennummer (nur Ziffern, 3-stellig mit führenden 0) einfügen.
     * Nach Teil 2 noch ggf. weitere Parameter:
     * mandant=115 (verpflichtend, 115 durch mandantennummer ersetzen)
     * &
     * sprache=DE (oder EN)
     * &
     * nummer=1234565 (Aktionärsnummer)
     * &
     * p=WRZLGRMP (Passwort)
     * 
     * Derzeit keine Verwendung dieser Parameter?
     */
    public String httpPfadTeil1 = "https://www.portal.better-orange.de/betterport/M"; //Lang 1
    /**Beschreibung siehe httpPfadTeil1*/
    public String httpPfadTeil2 = "/aLogin.xhtml?"; //Lang 2

    /********************************Schrott*******************************************
     * diese Parameter funktionieren so nicht - muß mal überarbeitet werden
     */
    /**TODO _Konsolidierung: Anzeige Willenserklärung Online-Ticket oder App - funktioniert so nicht!*/
    /**Mit OR verknüpft:
     * 1 = Online-Ticket zum Selbstdruck
     * 2 = In App bereitgestellt
     */
    @Deprecated
    public int statusOnlineTicket = 1;

    /**Nr des auf der Login-Seite des Portals anzuzeigenden StartTextes. Soll zukünftig über Phasen geregelt werden*/
    @Deprecated
    public int pLfdPortalStartNr = 0;

    public ParamPortalServer() {
        for (int i = 0; i < 21; i++) {
            phasenNamen[i] = "";
        }
    }

}
