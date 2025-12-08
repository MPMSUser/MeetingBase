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

import java.util.LinkedList;
import java.util.List;

/**Für Array: Meldung, die zugeordnet wurde. Konkrete:
 * > Meldung, die zu einer AktienregisterIdent generiert wurde
 * > Meldung, die zu einem Bevollmächtigten oder einer Kennung oder ... gehört
 * */

public class EclZugeordneteMeldung {

    /**Gibt an, in welcher "Beziehung" diese Meldung zu dem "aufrufenden" Teilnehmer ist (also sprich:
     * in welchem der 3 Array diese Meldung von BlWillenserklaerungStatus abgelegt wurde). Wird benötigt,
     * da beim Aufruf JSF einer Detailsicher nicht mehr ersichtlich ist, in welchem Array die Meldung drin
     * ist.
     * =1 => zugeordneteMeldungenEigeneAktienArray
     * =2 => zugeordneteMeldungenEigeneGastkartenArray
     * =3 => zugeordneteMeldungenBevollmaechtigtArray
     */
    public int artBeziehung = 0;

    /**aktienregisterIdent, zu der diese Meldung zugeordnet wurde (von der diese erzeugt wurde)
     * Wird nur in der Funktion leseMeldungenZuAktienregisterIdent gefüllt. Wäre sicher auch in anderen
     * Funktionen einfach füllbar*/
    public int aktienregisterIdent = 0;

    /**personNatJurIdent, die dieser Meldung direkt zugeordnet ist (d.h. gehört). Wird aktuell nur in der Funktion
     * leseMeldungenEigeneAktienZuPersonNatJur gefüllt. Wäre in anderen Funktionen sicher einfach füllbar
     */
    public int personNatJurIdent = 0;

    /**Meldung, die zugeordnet wurde*/
    public int meldungsIdent = 0;

    /** Wird nur gefüllt, wenn piRueckgabeKurzOderLang==2
     * Nicht in EclZugeordneteMeldungM !*/
    public EclMeldung eclMeldung = null;

    /** 0 Gast, 1 = aktienrechtliche Anmeldung; Achtung, früher umgekehrt!
     * Zusammen mit zutrittsIdent und mandant UNIQUE
     */
    public int klasse = 0;

    /**Präsenzkennzeichen*/
    //	public int kartenart=0;
    //	public int kartennr=0;
    public int identPersonNatJur = 0;
    /**wird aus dem Meldesatz gefüllt. 
     * 
     * Achtung, bei nurRawLiveAbstimmung wird das Präsenzkennzeichen
     * in der Datenbank nie gesetzt - sondern immer nur im Speicher gehalten. D.h. nach dem
     * Einlesen des Status ist istPraesent immer 0!
     * 
     * =1 wenn aktuell präsent
     */
    public int istPraesent = 0;
    public int istPraesentNeu = 0; // istPraesent wg. Kompatibilität zur App. Neu geht bereits auf neue Mechanismen
    /*TODO $App Das ganze funktioniert nur, wenn nciht zwischendurch ein anderer Teilnehmer mit dieser Meldung präsent war
     *  vermutlich muß hier eine Liste der ZutrittsIdents zu dieser Meldung mit der jeweiligen zugeordneten Person rein
     *  
     *  Wird bei nurRawLiveAbstimmung==1 nicht gefüllt!
     *  */
    public String praesenteZutrittsIdent = "";
    public String praesenteZutrittsIdentNeben = "";

    /**Angemeldeter Aktionär*/

    public long aktionaerStimmen = 0;
    public String aktionaerStimmenDE = "";
    /**String formatiert im Deutschen Format*/
    public String aktionaerStimmenEN = "";
    /**String formatiert im Englischen Format*/
    public String aktionaerAnredeDE = "";
    /**Herr, Frau etc. gemäß Schlüssel*/
    public String aktionaerAnredeEN = "";
    public String aktionaerTitel = "";
    public String aktionaerName = "";
    public String aktionaerVorname = "";
    public String aktionaerPlz = "";
    public String aktionaerOrt = "";
    public String aktionaerLandeskuerzel = "";
    /**DE*/
    public String aktionaerLand = "";
    /**Deutschland*/
    public String aktionaerStrasse = "";
    public String aktionaerBriefanredeDE = "";
    /**wie aus anreden-Datei*/
    public String aktionaerBriefanredeEN = "";
    /**wie aus Anreden Datei*/
    public String aktionaerTitelVornameName = "";
    /**Dr. Hans Müller**/
    public String aktionaerNameVornameTitel = "";
    /**Müller, Hans Dr.*/
    public String aktionaerKompletteAnredeDE = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String aktionaerKompletteAnredeEN = "";
    /**"Sehr geehrter Herr Dr. Müller" oder bei juristische Person "Sehr geehrte Damen und Herren"*/
    public String aktionaerBesitzArtKuerzel = "";
    public String aktionaerBesitzArt = "";
    public String aktionaerBesitzArtEN = "";
    public int gattung = 0;

    public long stueckAktien = 0;

    public int anzZutrittsIdentSelbst = 0;
    public int anzZutrittsIdentVollmacht = 0;
    public int anzVollmachtenDritte = 0;

    /**Summe aus KIAV, SRV, Briefwahl*/
    public int anzKIAVSRV = 0;

    public int anzSRV = 0;
    public int anzKIAV = 0;
    public int anzBriefwahl = 0;

    public boolean zweiEKMoeglich = false;

    public boolean willenserklaerungenVorhanden = false;

    public boolean fixAnmeldung = false;

    public List<EclWillenserklaerungStatus> zugeordneteWillenserklaerungenList = new LinkedList<>();

    /**Nr der höchsten vergebenen Willenserklärung für diese Meldung. Dient dazu, um festzustellen, ob seit Einlesen
     * der Willenserklärungen (in einem früheren  Transaktionsschritt) neue Willenserklärungen dazugekommen sind
     */
    public int identHoechsteWillenserklaerung = 0;

    /**
     * Für Portal (speziell ku178):
     * Div. Infos für Anmeldung.
     * Pos 1 = Anmelden (1), Abmelden (2), sonst undefiniert
     * 
     * Für Verein: Bonuskartennummer
     * 
     *Length=40*/
    public String zusatzfeld3 = "";

}
