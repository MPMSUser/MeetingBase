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

public class EclInstiSammelAnmeldungDetail {

    /**Mandant*/
    public int mandant = 0;

    /**Laufende-Nummer - der eigentliche Key, da ja Aktionärsnummer bei Fix-Anmeldungen öfters vorhanden sein kann*/
    public int laufendeNummer = 0;

    public int identKopfsatz = 0;

    /**Laut Import-Datei*/
    public String aktionaersnummerQuelle = "";

    /**Falls z.B. 0 oder 1 manuell angehängt werden muß zur Verarbeitung*/
    public String aktionersnummerKorrigiert = "";

    /**Vor allem bei Nutzung der Suchfunktion: damit beim Wiederholungssuchlauf nicht jedesmal neu "gedacht" werden muß,
     * kann ein im Suchlauf gefundener Satz für die Zukunft ausgeschlossen werden.
     */
    public boolean explizitAusgeschlossen = false;

    public long aktienQuelle = 0;

    /**=-1 => aktienQuelle wird verwendet; ansonsten die zu verwendende Stückzahl*/
    public long aktienkorrigiert = -1;

    public long aktienImAktienregister = 0;

    public long aktienVerarbeitet = 0;

    public int identAktionaer = 0;

    /**Nur, falls im Kopfsatz keine Weisungen für alle hinterlegt wurden*/
    public int[] weisungen = null;

    /**0 = noch kein Testlauf
     * 1 = Vollständig verarbeitetbar
     * 2 = nur Teilbestand verarbeitet
     * 3 = keine Verarbeitung möglich
     */
    public int verarbeitungsstatusTestlauf = 0;

    /**Ggf. Fehlercode, falls verarbeitungsstatus=2 oder 3*/
    public int fehlercodeTestlauf = 0;

    /**0 = noch keine Verarbeitung
     * 1 = Vollständig verarbeitet
     * 2 = nur Teilbestand verarbeitet
     * 3 = keine Verarbeitung möglich
     */
    public int verarbeitungsstatus = 0;

    /**Ggf. Fehlercode, falls verarbeitungsstatus=2 oder 3*/
    public int fehlercode = 0;

    /**************Nicht in Table, dazugespielt**************************/
    public String bezeichnungAktionaer = "";

}
