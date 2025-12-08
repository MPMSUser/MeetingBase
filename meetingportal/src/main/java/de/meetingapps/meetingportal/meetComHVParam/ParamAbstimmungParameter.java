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

public class ParamAbstimmungParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**Die IDs der Steuerelemente, die in der Abstimmungssteuer gemäß KonstAbstimmungsAblauf aktiv sein sollen*/
    public int[] ablaufAbstimmung = null; //ab 1000; 1000=Anzahl Elemente

    /**=true => das zugehörige Element in ablaufAbstimmung wurde bereits erfolgreich ausgeführt*/
    public boolean[] ablaufAbstimmungErledigt = null;

    /*++++++++++++Weisung / Abstimmung - Eingabe manuell / Stapel-Scanner+++++++++++++*/
    /*Default-Abgabe speichern*/
    /*XXX*/
    /**Bei manueller / Scan-Eingabe von Weisungen (Bestandsverwaltung): nicht markierte werden gespeichert als
     * (soweit nicht bei einzelner Abstimmung was anderes angegeben ist).
     */
    public int weisungNichtMarkierteSpeichernAlsSRV = 0; //2000
    public int weisungNichtMarkierteSpeichernAlsBriefwahl = 0; //2001
    public int weisungNichtMarkierteSpeichernAlsKIAV = 0; //2002
    public int weisungNichtMarkierteSpeichernAlsDauer = 0; //2003
    public int weisungNichtMarkierteSpeichernAlsOrg = 0; //2004

    /**Bei manueller / Scan-Eingabe von Weisungen (Verlassen HV): nicht markierte werden gespeichert als
     * (soweit nicht bei einzelner Abstimmung was anderes angegeben ist).
     */
    public int weisungHVNichtMarkierteSpeichernAls = 0; //2005

    /**Bei manueller / Scan-Eingabe von Abstimmungen: nicht markierte werden gespeichert als
     * (soweit nicht bei einzelner Abstimmung was anderes angegeben ist).
     */
    public int abstimmungNichtMarkierteSpeichern = 0; //2006

    /**Vorbelegung bei manueller Eingabe**/
    /*XXX*/
    public int weisungVorbelegungMitSRV = 1; //2007
    public int weisungVorbelegungMitBriefwahl = 1; //2008
    public int weisungVorbelegungMitKIAV = 0; //2009
    public int weisungVorbelegungMitDauer = 0; //2010
    public int weisungVorbelegungMitOrg = 0; //2011

    public int weisungHVVorbelegungMit = 0; //2012

    public int abstimmungVorbelegungMit = 0; //2013

    /**Zwingend bei manueller Eingabe**/
    public boolean eingabezwangSRV = false; //2025
    public boolean eingabezwangBriefwahl = false; //2026
    public boolean eingabezwangKIAV = false; //2027
    public boolean eingabezwangDauer = false; //2028
    public boolean eingabezwangOrg = false; //2029

    public boolean eingabezwangWeisungHV = false; //2030
    public boolean eingabezwangAbstimmung = false; //2031

    /*XXX*/
    /**Als undefiniert-gespeicherte Weisungen zählen bei der Abstimmung als Ja/Nein/Enthaltung etc.*/
    public int undefinierteWeisungenZaehlenAls = 3; /*Enthaltung 2014*/

    /*XXX*/
    /**Falls 1, dann werden alle ggf. ungültigen Stimmabgaben / Weisungen auf Enthaltung umdefiniert*/
    public int ungueltigeZaehlenAlsEnthaltung = 0; //2015

    /*XXX*/
    /**Falls 1, dann wird auch Nicht-Teilnahme als Stimmabgabe in den Abstimmungsparametern zugelassen*/
    public int nichtTeilnahmeMoeglich = 0; //2019

    /**Veröffentlicht im Bundesanzeiger vom
     * Länge=200, ParameterLang*/
    public String veroeffentlichtImBundesanzeigerVom = ""; //LLang, 10

    /*XXX*/
    /**0=Sortierung der Druckausgabe nach Anzeige-Reihenfolge Weisungen
     * 1=Sortierung der Druckausgabe nach separatem Feld bei der Zuordnung zu Abstimmungsvorgang
     * 
     * Verifiziert 01.03.2025: dieser Parameter ist zwar in DB und in (alter) Parameter-Oberfläche,
     * wird jedoch nirgends verwendet.
     * 
     * Neue Werte:
     * 0 = aus Kompatibilitätsgründen enthalten. Tablet, Stimmkarten, Position (wie 1)
     * 1 = Tabletabstimmung vorrangig, dann Stimmkarten
     * 2 = Stimmkarten vorrangig, dann Tabletabstimmung
     * 3 = Position in Ausdruck, danach Tabletabstimmung, Stimmkarten
     * 4 = Position in Ausdruck, danach Stimmkarten, Tabletabstimmung
     * 
     * 1 oder 2 passen i.d.R. für alle normalen HVen
     */
    public int sortierungDruckausgabeIndividuell = 0; //2016

    /*XXX*/
    /**Text verwenden für Tablet:
     * 1=kurzBezeichnung
     * 2=anzeigeBezeichnungKurz
     * 3=anzeigeBezeichnungLang
     * 
     * Derzeit ohne Funktion
     */
    public int textVerwendenTablet = 3; //2017

    /**Text verwenden für Formular:
     * 1=kurzBezeichnung
     * 2=anzeigeBezeichnungKurz
     * 3=anzeigeBezeichnungLang
     */
    public int textVerwendenFormular = 3; //2018

    /**1 nur aktuell präsente Meldungen dürfen Stimmen abgeben
     * 2 aktuell präsente, und schon mal präsent gewesene dürfen Stimmen abgegeben
     * 3 auch nicht präsent gewesene dürfen abstimmen - nur zulässig, wenn Abstimmung so erfolgt dass auch zuordenbar (z.B. über Eintrittskartennummer bei Hybrid)
     */
    public int abstimmenDuerfen_nurPraesente_PraesenteUndGewesene = 1; //100

    /**0 => Gleich zu Beginn Fehlermeldung, keine weitere Verarbeitung / Einlesung
     * 1 => wird mit der Tablet-Abstimmung ein Aktionär gelesen, der noch nicht präsent ist/war, wird dieser
     * 		automatisch präsent gesetzt. Achtung - nur bei Tausch 1:1 zulässig! - nicht mehr implementiert!!!
     * 2 => Erfassen ohne weitere Anzeige und senden an Server, ohne Präsent zu setzen
     */
    public int abstimmenTabletNichtPraesenteWerdenPraesentGesetzt = 2; //109

    /**Bei mehrfach abgegebenem unterschiedlichem Stimmmedium gilt:
     * 1 Tablet/App zählt Vorrangig
     * 2 Papier zählt vorrangig.
     * Innerhalb Tablet/App immer letzte Willenserklärung.
     */
    public int priorisierung_ElektronischVorPapier_PapierVorElektronisch = 2; //101

    /**Gilt nur eingeschränkt :-). D.h.:
     * Bei "ungleicheUngueltig" gilt folgendes:
     * 		Wenn die "neue Stimme" E, J, N ist  (und ungleich der bereits gespeicherten Stimme)
     * 		> und die bereits gespeicherte J, N, E, U ist, dann wird es U.
     * 		Ansonsten wird es die neue Stimme  
     * 1=letzte Abgabe gilt
     * 2=ungleiche Ungültig
     */
    public int beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig = 1; //102

    /**1 bei der Abstimmungserfassung kann die Identifikation auch über Eintrittskartennummern erfolgen*/
    public int beiAbstimmungEintrittskartennummerZulaessig = 1; //103

    /**1 = es werden nur die aufgerufenen Stimmzettel/Markierungskarten anerkannt; 0 = jede beliebige Nummer kann zum Scannen
     * verwendet werden. 2 = keine VollmachtWeisungskarte
     */
    public int beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig = 0; //104

    /**1 = auf den Tablets wird die Abstimmung bei der Stimmabgabe aufgeteilt auf die Stimmkarten 101 folgende
     * und Blätterpfeile angeboten 
     */
    public int beiTabletAbstimmungBlaettern = 0; //113

    /**Primitiv-Lösung für Maximale Stimmabgaben bei einer Abstimmungsgruppe.
     * Es gibt 10 Abstimmungsgruppen (1 bis 10), die hier eingetragene Zahl gibt vor, wieviele Abstimmungen
     * der Abstimmungsgruppe mit "Ja" markiert werden dürfen. 
     * Gibt also wieder z.B. n aus m Kandidaten.
     */
    public int[] anzahlJaJeAbstimmungsgruppe = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; //1=131, bis 10=140

    /**Gesamtmarkierung "Alles Ja/Nein/Enthaltung"*/
    public int beiTabletAllesJa = 0; //110
    public int beiTabletAllesNein = 1; //111
    public int beiTabletAllesEnthaltung = 1; //112

    /**Tablet: bei Gesamtmarkierung neben den Buttons den Text "von - bis TOP" anzeigen*/
    public int beiTabletGesamtmarkierungTextAnzeigen = 0; //141

    /**=0 => gilt je Seite; 1 => gilt für alle dieses Blocks*/
    public int beiTabletGesamtmarkierungFuerAlle = 0; //143

    /**1=Kurztexte anzeigen, 2=Langtexte anzeigen
     * 
     * Wird derzeit noch verwendet. Siehe auch textVerwendenTablet (für die Zukunft)*/
    @Deprecated
    public int beiTabletTextKurzOderLang = 2; //144

    /**1=Beim Verändern des Abstimmungsblocks "aktiv" oder "einsammeln" 
     * im Abstimmungsablauf werden Abstimmungen
     * automatisch auf Reload gesetzt
     * TODO Parameter noch nicht in Oberfläche (in Preset-Parametern: erledigt)
     */
    public int abstimmungsLoadBeiAktivierungImAbstimmungsablauf=0; //2041
    
    
    /**Farben für Buttons bei Tablet
     * 
     * Werte zur Orientierung:
     * #00ff00 = grün
     * #ff0000 = rot
     * #A7A7A7 = grau
     * #0080ff = blau
     * 
     * */
    public String beiTabletFarbeJa="00ff00"; //tfBeiTabletFarbeJa 2032
    public String beiTabletFarbeNein="ff0000"; //tfBeiTabletFarbeNein 2033
    public String beiTabletFarbeEnthaltung="fcf800"; //tfBeiTabletFarbeEnthaltung 2034

    /**Farben für Buttons bei Tablet, wenn dies nicht gedrückt wurden*/
    public String beiTabletFarbeUnmarkiertJa="A7A7A7"; //tfBeiTabletFarbeUnmarkiertJa 2035
    public String beiTabletFarbeUnmarkiertNein="A7A7A7"; //tfBeiTabletFarbeUnmarkiertNein 2036
    public String beiTabletFarbeUnmarkiertEnthaltung="A7A7A7"; //tfBeiTabletFarbeUnmarkiertEnthaltung 2037

    /**Farben für Gesamtmarkierung-Buttons*/
    public String beiTabletFarbeGesamtmarkierungJa="A7A7A7"; //tfBeiTabletFarbeGesamtmarkierungJa 2038
    public String beiTabletFarbeGesamtmarkierungNein="A7A7A7"; //tfBeiTabletFarbeGesamtmarkierungNein 2039
    public String beiTabletFarbeGesamtmarkierungEnthaltung="A7A7A7"; //tfBeiTabletFarbeGesamtmarkierungEnthaltung 2040

    
    public int in100PZAuchEnthaltungen = 0; //105
    public int in100PZAuchUngueltige = 0; //106
    public int in100PZAuchNichtStimmberechtigte = 0; //107
    public int in100PZAuchNichtTeilnahme = 0; //108

    /**Auswertung - Powerpoint-Export Liste: 
     * =0 => einfach nur ausgeben
     * >0 => Anzahl der Zeilen pro Folie, => seitenweise aufblättern*/
    public int auswertenPPListeZeilen = 0; //142

    /*AAAAA neuer Parameter Abstimmung Logik erledigt, aber DB und Oberfläche fehlen noch impliziteStimmabgabenAutomatischVerwalten*/
    /**1 => beim Auswerten werden die impliziten Stimmabgaben automatisch am Anfang komplett gelöscht und 
     * anschließend automatisch eingetragen.
     * 
     * 0 => muß separat aufgerufen werden
     */
    public int impliziteStimmabgabenAutomatischVerwalten=1;
    
    /**Verwendung von anzeigeBezeichnungKurz/anzeigeBezeichnungKurzEN in EclAbstimmung
     * aktivieren*/
    public int anzeigeBezeichnungKurzAktiv = 1; //2021

    /*XXX*/
    /**Weisungen für Gegenanträge im separaten Block anzeigen*/
    public boolean weisungenGegenantraegePortalSeparat = true; //2022

    /**Weisungen für Gegenanträge im separaten Block anzeigen*/
    public boolean weisungenGegenantraegeInternSeparat = true; //2023

    /**Weisungen für Gegenanträge im separaten Block anzeigen*/
    public boolean weisungenGegenantraegeVerlassenHVSeparat = true; //2024

    /**Stimmzetteldruck: Abstimmungsblock-Ident, der beim Stimmzetteldruck für Ermittlung der Stimmzettel-Text-Variablen verwendet wird*/
    public int stimmzetteldruckAbstimmungsblockBasis=0; //2042
     
    /**Stimmzetteldruck: Anzahl der Stimmzettel insgesamt auf den Bögen.
     * Aktuell gelebter Standard: 41 für zweiseitig, 21 für einseitig. Maximalzahl=60*/
    public int stimmzetteldruckAnzahlStimmzettel=21; //2043

    /**Wird verwendet für Variable in Abstimmungsformularen.
     * Abfrage über param.liefereBriefwahlAusgebenInAbstimmungsergebnis(), da in dieser Methode auch berücksichtigt wird,
     * falls Modul Briefwahl deaktiv (liefert dann immer 0) 
     */
    public int briefwahlAusgebenInAbstimmungsergebnis=0; //2044
    
}
