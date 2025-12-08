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

import java.util.List;

/**Intititionelle Anmeldung - Kopfsatzt.*/
public class EclInstiSammelAnmeldungKopf {

    /*********Identifikation***************/

    /**Mandant*/
    public int mandant = 0;

    /** eindeutiger Key*/
    public int ident = 0;

    public long db_version = 0;

    /**In Vorbereitung: Verweis auf anfordernde - mandantenunabhängige - Stellw*/
    public int identAnforderndeStelle = 0;

    public String bezeichnung = "";

    /*********************Quelle******************/

    /**1: die Bestände werden über die Suchfunktion zusammengesucht (Basis: suchbegriffe)
     * 2: die Bestände werden über Aktionärsnummer importiert (quelleDateiname)
     * 3: manuelles Sammeln der Aktionärsnummer*/
    public int quelle = 0;

    /**Begriffe, die in der "Anmeldung" enthalten sind. NICHT die Suchbegriffe! Reine Information*/
    public String vorgabeBegriff = "";

    /**All die Begriffe, nach denen die Suchliste aufgebaut wird*/
    public List<String> suchbegriffe = null;

    public String quelleDateiname = "";

    /**einzugebender, erläuternder Text, z.B. per Mail am soundsovielten eingegangen*/
    public String erlaeuterung = "";

    /***************Gewünschte Aktion*******************/

    /**Willenserklärung:
     * vollmachtUndWeisungAnSRV
     * vollmachtUndWeisungAnKIAV
     * briefwahl
     */
    public int willenserklaerung = 0;

    public boolean fixAnmelden = false;

    /**0 => keine Weisung
     * 1 => nachfolgende Gesamtweisung für jede Anmeldung übernehmen
     * 2 => einzelne Weisung je Anmeldung berücksichtigen
     */
    public int weisungenHinterlegen = 0;

    /**Werden verwendet, falls weisungenHinterlegen==1*/
    public int[] weisungenFuerAlle = null;

    /**True => die Willenserklärungen sollen auf eine eigene Sammelkarte gebucht werden
     * (diese muß aber vorher entsprechend angelegt werden!)*/
    public boolean eigeneSammelkarte = true;

    /**Sammelkarte, auf die die Willenserklärungen gebucht werden*/
    public int sammelIdent = 0;

    /**Nach der HV muß eine Stimmrechtsbestätigung verschickt werden*/
    public boolean stimmrechtsbestaetigungGewuenscht = false;

    /*******Verarbeitungsprotokoll*****/

    /**einzugebender, erläuternder Text zum Stand der Verarbeitung*/
    public String verarbeitungsvermerk = "";

    boolean erstanmeldungDurchgefuehrt = false;

    boolean finaleAnmeldungDurchgefuehrt = false;

    boolean stimmrechtsbestaetigungIstVerschickt = false;

    /****************Infos zu Details**************/
    /**Höchste vergebene Ident in detailAnmeldungenListe*/

    public int maxIdentDetail = 0;

    /***************In separatem Table**************************/

    /**Zugeordnete Aktionäre**/
    public List<EclInstiSammelAnmeldungDetail> detailAnmeldungenListe = null;

}
