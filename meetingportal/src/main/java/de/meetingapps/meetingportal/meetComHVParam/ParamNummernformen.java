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
import java.util.ArrayList;

public class ParamNummernformen implements Serializable {
    private static final long serialVersionUID = 4901097471978376308L;

    /**Vorgehensdokumentation
     * 
     * Kodieren (=Erstellen des Strings zum Ausdruck oder Anzeigen)
     * ============================================================
     * Programmintern wird immer mit klasse UND art gearbeitet.
     * 
     * Die zu verwendende Nummernform wird aus dem Array nummernFormZuKlasseArt[klasse][art] ermittelt.
     * Wenn dies -1 ergibt (also für diese [klasse][art] keine Nummernform eingetragen, dann wird
     * die Nummernform unter nummernFormZuKlasseArt[klasse][0] genommen. Diese sollte deshalb immer definiert sein!
     * 
     * Der "externe Wert" (für Druck) von klasse wird über klasseZuCode[interner Wert für Klasse] gewonnen.
     * 
     * Analog: artZuCode[interner Wert für Art]
     * 
     * Analog für Kombicode: kombiZuCode[interner Wert für Klasse][interner Wert für Art]. Wenn der daraus erzeugte "externe Kombicode"
     * -1 ergibt, dann wird statt des KombiCodes "-" ausgegeben.
     * 
     * Analog: stimmartZuCode[interner Wert für Stimmart]
     * 
     * 
     * Dekodieren (=String zerlegen und in einzelnen Werten abspeichern)
     * =================================================================
     * Wichtige Bedingungen:
     * > 1. Stelle muß sein: entweder Kombicode, oder Klasse
     * 	 Wenn 1. Stelle Klasse ist, dann muß 2. Stelle Art sein
     * > Es wird vorrangig Kombicode versucht! D.h. wenn art/klasse in Nummernformen verwendet werden, dürfen
     * 		keine Kombicodes eingetragen sein (in kombiZuCode muß dann alles mit -1 belegt sein)
     * 
     */

    /**=1 => Aktionsnummer wird zwar zum Erkennen der Nummernform verwendet, aber ansonsten ignoriert*/
    public int ignoriereKartenart = 0; //85

    /**Ident des "Satzes" in der Nummernform-Datenbank, die für diesen Mandant verwendet wird*/
    public int ident = 0; //80

    /**an klasseZuCode[klasse] steht die einstellige Codezahl (0 bis 9 zulässig), die auf den Dokumenten für klasse verwendet wird*/
    public int[] klasseZuCode = null;

    /**an artZuCode[art] steht die einstellige Codezahl (0 bis 9 zulässig), die auf den Dokumenten für art verwendet wird*/
    public int[] artZuCode = null;

    /**an kombiZuCode[klasse][art] steht die einstellige Codezahl, die auf den Dokumenten als "Kombicode" 
     * (aus Art und Klasse, also sprich eine Einstellige Aktionsnummer) verwendet wird.
     * Array = 8x9 (Art:jeweils 1 bis 8, zuzüglich "neutrale Art" [][0] {neutrale Art = 0 => keine Art definiert!})
     * Achtung, aktueller Stand 16.11.2017: "neutrale Art [0]" wird nicht verwendet!
     */
    public int[][] kombiZuCode = null;

    /**Für die "abzugebenden Stimmarten" J,N,E die entsorechende Codierung.
     * Ist vorbelegt mit Code=Stimmart (1 bis 3)*/
    public int[] stimmartZuCode = null;

    /**Verfahren / Regel zur Festlegung der Nummernform:
     * Das 1. Zeichen des Strings wird als Kartenklasse genommen. Wenn alle Nummernformen zu dieser Kartenklasse identisch ist, wird diese 
     * Nummernform zur Decodierung verwendung. Ansonsten wird die zweite Ziffer als Kartenart hergenommen und die damit bestimmte
     * Nummernform verwendet.
     * Wenn die erste Ziffer ein Kombicode ist, dann wird die dazu definierte Nummernform unter [klasse=kombicode][10] gespeichert.
     * 
     * -1 => für diese Kombi ist keine Nummernform hinterlegt.
     * 
     * In [x][0] sollte/muß immer eine Ident abgelegt sein, diese wird verwendet, wenn [x][y] -1 liefert (also wenn für eine Kartenart
     *  keine Nummernform definiert ist).
     */
    public int[][] nummernformZuKlasseArt = null;

    /**Anzahl der Abstimmungspunkte, die mit der AppIdent übertragen werden - falls Abstimmungskarte
     * Noch nicht parametrisiert! Fest hier kodiert*/
    public int anzahlAbstimmungspunkte = 60;

    /**Dekodierung
     * 
     * Achtung: 
     * > an erster Stelle muß entweder Kombicode oder Kartenklasse stehen.
     * > falls Kartenklasse nicht appIdent, dann an zweiter Stelle Kartenart.
     * 
     * 
     * a = 	Aktionsnummer - Kombicode (Kartenklasse und Kartenart in 1 Ziffer)
     * 		Abbildung auf Kartenklasse/Kartenart über kombiZuCode
     * 
     * b = 	Aktionsnummer - Kartenklasse
     * 		1 = Eintrittskarte (ZutrittsIdent)
     * 		2 = Gastkartennummer (ZutrittsIdent)
     * 		3 = Stimmnummer
     * 		4 = Stimmnummer Second
     * 		5 = App Ident
     * 		6 = PersonenIdent (nur bei AppIdent in Ident enthalten!)
     * 		7 = Eintrittskarte (mit Neben-Nummer)
     * 
     * c =	Aktionsnummer - Kartenart
     * 		1 = Erstzugang
     * 		2 = Zuordnungs-Etikett (speziell für b=3)
     * 		3 = Wiederzugang
     * 		4 = Abgang
     * 		5 = Vollmacht an Dritte (erteilen)
     * 		6 = Vollmacht an Dritte (empfangen)
     * 		7 = Stimmkartennummer
     * 		8 = Vollmacht/Weisung SRV (d.h. diese Karte beinhaltet Unterschrift)
     * 
     * d =	Stimmkarten-Sub-Nummernkreis - 
     * 		bei Stimmkarten (1 bis 4= erste Aktiengattung, 5 bis 8 = zweite Aktiengattung)
     * 
     * 
     * 
     * e = 	Identifikations-Nummer (Stellen-Anzahl laut Parameter)
     * 
     * f = 	Mandanten-Nummer 3-stellig
     * 
     * g = 	dreistellige Kontrollzahl (fest)
     * 
     * h =  zweistellige Kontrollzahl (fest)
     * 
     * i =  einstellige Kontrollzahl (fest)
     * 
     * bei b ==7 und 8:
     * j =		zweistellige Stimmkartennummer
     * 
     * k =  Prüfziffer
     * 
     * l =  Ignorieren
     * 
     * m =  eigentliche Identifikationsnummer
     * 
     * n = nebennummer (für Eintrittskarte)
     * 
     * o =  Stimmart (für Stimmabschnittsbogen, Ja/Nein/Enthaltung) (ehemals n)
     * 
     * p = Trennzeichen "-" (zu verwenden für trennung zwischen Eintrittskartennr. und Nebennummer)
     * */

    public ArrayList<ArrayList<Character>> nummernDefinition = null;

    /**Falls z.B. Tausch 1:1, dann wird ja automatisch in [0] die Stimmkartennummer wie Eintrittskartennummer
     * eingetragen. Falls =true, dann wird die "neben-Ident" (z.B. 00) an die Stimmkartennummer mit angehängt.
     * Falls false, dann nicht. Ist primär enthalten wg. Kompatibilität
     */
    public boolean beiEintrittskarteWirdStimmkarteNebenAnhaengen = true;

    /*TODO $CodeSanierung Vermutlich veraltet!*/
    /*App-Ident: feste Codierung wie folgt:
     * a 	= Kartenklasse oder Kombicode zu AppIdent
     * b 	= Kartenart, fest ohne Übersetzungstabelle
     * c	= 7 Stellen: PersonenIdent, die die App nutzt
     * d	= 2 Zeichen Anzahl der Idents, die folgen
     * e	= 10 Zeichen App-Version	
     * 
     * danach cc-mal:
     * f	= Kartenklasse
     * g... = 6 Zeichen Identifikation
     * h	= Abstimmungsanzahl Zeichen Stimmart (Nummer)
     * 
     * Rest: Hash-Code
     * 
     */

    public ParamNummernformen() {
        klasseZuCode = new int[10];
        artZuCode = new int[9];
        kombiZuCode = new int[10][9];
        nummernformZuKlasseArt = new int[10][9];

        for (int i = 0; i < 10; i++) {
            klasseZuCode[i] = -1;
            for (int i1 = 0; i1 < 9; i1++) {
                artZuCode[i1] = -1;
                kombiZuCode[i][i1] = -1;
                nummernformZuKlasseArt[i][i1] = -1;
            }
        }

        stimmartZuCode = new int[4];
        for (int i = 0; i < 4; i++) {
            stimmartZuCode[i] = i;
        }
    }

}
