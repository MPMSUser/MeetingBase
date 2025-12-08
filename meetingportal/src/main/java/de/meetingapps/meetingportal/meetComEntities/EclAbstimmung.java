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

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

/********************************Dokumentation: Zusammenhang der verschiedenen Abstimmungs-Klassen*************************************/

/*++++++++++++++++++ Abstimmungspunkt - enthält auch Weisungsinfos++++++++++++++++++++*/
/**EclAbstimmung - Enthält Daten für einen einzelnen Abstimmungspunkt (Tagesordnungspunkt, Gegenantrag, Punkt zur Abstimmung).
 * Ident ist eindeutige Identifikation eines Punktes*/

/*++++++++++++ Abstimmungsvorgang+++++++++++++++++++++++++++++++++++*/
/**EclAbstimmungsblock - "Kopfsatz" für einen Abstimmungsvorgang (Einsammelvorgang).
 * 
 * Über EclAbstimmungZuAbstimmungsblock wird EclAbstimmungsblock.ident mit EclAbstimmung.ident verbunden
 */

/**EclAbstimmungZuAbstimmungsblock - Verbindet EclAbstimmung und EclAbstimmungsblock. Enthält die entsprechenden
 * Infos zur tatsächlichen Stimmabgabe in der Abstimmung.
 */

/*++++++++++++++++++++++Elektronischer Stimmkartenblock+++++++++++++++++*/
/**EclStimmkartenBlock - Elektronischer Stimmkartenblock.
 * 
 * Enthält EclStimmkarteInhalt[]*/

/**EclAbstimmungenZuStimmkarte - Nur noch verwendet für elektronischen Stimmkartenblock (Offline-Abstimmungsvariante App).
 * Gibt eine Stimmkarte innerhalb des elektronischen Stimmkartenblocks wieder.
 * In EclStimmkartenBlock enthalten
 * EclStimmkarteInhalt[] stimmkarteInhaltArray=null;
*/

/*+++++++++++++++++++++++++++Sonstiges+++++++++++++++++++++*/
/**Nicht im Set enthalten, da nur für die Auswertung benötigt und deshalb nicht gepuffert:
 * EclAbstimmungenEinzelAusschluss
 * 		Alle Meldungen (identMeldung), die zu einem Punkt abstimmungsIdent=EclAbstimmung.ident nicht stimmberechtigt sind 
 */

/***************************************************************************************************************************************
 * Grundüberlegung - welche Wege stehen für die Erfassung von Daten zu einem Abstimmungspunkt offen und was wird dafür benötigt??
 * 
 * WeisungenSRVBriefwahlExtern
 * > "SRV/Briefwahl außerhalb der HV - extern" - Portal, App
 * WeisungenKIAVExtern
 * > "KIAV außerhalb der HV - extern" - Portal, App (muß zusätzlich noch KIAV-abhängig sein - möglicherweise bietet
 * 		nicht jede KIAV eine Vertretung für Sonderabstimmungen an)
 * 
 * WeisungenSRVBriefwahlHV
 * > "SRV/Briefwahl beim Verlassen der HV - extern + intern" - Portal, App, Weisungsterminal, Erfassung im HVMaster
 * 
 * WeisungenSRVBriefwahlIntern
 * > "SRV/Briefwahl - Interne Erfassung " - Bestandsverwaltung
 * WeisungenKIAVIntern
 * > "KIAV - Interne Erfassung" - Bestandsverwaltung
 * 
 * WeisungenSchnittstelle
 * > "Weisungen über Schnittstelle"
 * 
 * WeisungenAuswertungenExtern
 * > "Weisung in externer Auswertung" - für Bankenvertreter
 * 
 * Abstimmung
 * > "Abstimmung"
 * >> warum ist das erforderlich? Um einen Punkt komplett (einschließlich Zuordnung zur Abstimmung) vorzubereiten, 
 * 		aber noch nicht hin nach außen anzuzeigen 
 * 
 * Weitere Überlegungen:
 * > "interne" Auswertungen (BO + Kunde): Abstimmungspunkt wird immer aufgenommen, wenn einer der Weisungspunkte aktiv.
 * > "Freigabe SRV": nicht abhgängig von Weisungspunkten, sondern von Abstimmungsblock
 * 
 * 
 * aktiv:
 * > WeisungenSRVBriefwahlExtern
 * > WeisungenKIAVExtern
 * > WeisungenSRVBriefwahlHV
 * > WeisungenSRVBriefwahlIntern
 * > WeisungenKIAVIntern
 * > WeisungenSchnittstelle
 * > WeisungenAuswertungenExtern
 * > Abstimmung
 * 
 * anzeigePosition
 * > WeisungenExtern (WeisungenSRVBriefwahlExtern, WeisungenKIAVExtern)
 * > WeisungenSRVBriefwahlHV
 * > WeisungenIntern (WeisungenSRVBriefwahlIntern, WeisungenKIAVIntern)
 * > WeisungenSchnittstelle
 * > WeisungenAuswertungenIntern
 * > WeisungenAuswertungenExtern
 * (Hinweis: Position für Abstimmungen auf der HV in der Entity für die Zuordnung zu Abstimmungsblock) 
 * 
 * stimmart: wie anzeigePosition
 * > Außerdem:
 * >> abstimmungExtern (App, Tablet)
 * >> abstimmungElStiBlo (elektronischer Stimmkartenblock)
 * >> abstimmungIntern (manuelle Erfassung, Scanning)
 * 
 * Text für elektronischen Stimmblock????????? E+D
 * Bedeutung G-M???? => Raus!
 */

/**Enthält Daten für einen einzelnen Abstimmungspunkt (Tagesordnungspunkt, Gegenantrag, Punkt zur Abstimmung).
 * Ident ist eindeutige Identifikation eines Punktes*/
public class EclAbstimmung implements Serializable {
    private static final long serialVersionUID = 4132993236447912968L;

    public int mandant = 0;

    /**Eindeutige, interne Nummer*/
    public int ident = 0;

    public long db_version = 0;

    /**********************************Indexe*************************************/
    /**++++Internetservice++++/
    /**Nummer des TOPs in der Tagesordnung - wird der Bezeichnung vorangestellt (z.B. 1., 3.2. etc). - für Internetservice oder Listen
     * Maximal 10 Stellen*/
    public String nummer = "";
    public String nummerEN = "";

    /**Index des TOPs in der Tagesordnung - wird der Nummer nachgestellt (z.B. a), b) etc). - für Internetservice
     * Maximal 10 Stellen*/
    public String nummerindex = "";
    public String nummerindexEN = "";

    /**==1 => "nummer" wird im Portal bei diesem Tagesordnungspunkte nicht angezeigt. Dient dazu,
     * um bei 5.1, 5.2 die 5 nicht in der Liste anzuzeigen, aber z.B. bei Widerspruchsliste o.ä. dennoch verfügbar zu haben
     */
    public int nummerUnterdruecken=0;
    
    /**++++Intern++++*/
    /**Nummer des TOPs und Index des TOPs, zur internen Verwendung. 
     * Muß immer gleich formatiert sein, zur internen Verwendung. 
     * Vorgabe: 
     * > nummerKey = Nummer ohne . und irgendwas.
     * > nummerindexKey=Buchstaben/Ziffern-Kommbination ohne irgendetwas
     */
    public String nummerKey = "";
    public String nummerindexKey = "";

    /**++++Verkündungstexte++++*/
    /**Nummer des TOPs und Index des TOPs, für die Anzeige/Verwendung in den Verkündungstexten. 
     */
    public String nummerFormular = "";
    public String nummerindexFormular = "";

    /**************************Texte*********************************************/
    /**Verwendung bevorzugt für interne Masken; max. 160 Stellen (früher: 40).
     * Alternativverwendung: für Tablet-Abstimmung*/
    public String kurzBezeichnung = "";
    public String kurzBezeichnungEN = "";

    /**Verwendung bevorzugt für Portal, Tablet und sonstige kleinere Anzeigen; falls für eine Abstimmung=="", dann muß automatisch
     * anzeigeBezeichnungLang verwendet werden (erfolgt automatisch, wenn Zugriff
     * über lieferenAnzeigeBezeichnungKurz / EN erfolgt dies automatisch). max. 800 Stellen*/
    public String anzeigeBezeichnungKurz = "";
    public String anzeigeBezeichnungKurzEN = "";

    /**Verwendung bevorzugt für Formulare; Vollständige Bezeichnung des Antrags - in Überschriftsform; max. 800 Stellen*/
    public String anzeigeBezeichnungLang = "";
    public String anzeigeBezeichnungLangEN = "";

    /**Kandidatenname. Für Einzelentlastung u.ä.. Verwendung bevorzugt für Formulare,
     * um im Fließtext nochmal den Kandidatennamen einfließen lassen zu können.
     * 160 Stellen*/
    public String kandidat = "";
    public String kandidatEN = "";

    /*********************Aktivierung*****************************/

    /**"Haupt-Aktiv-Schalter. Wenn==0, dann wird diese Abstimmung nirgendwo verwendet (egal wie die anderen "aktiv*" stehen).
     * Die Einzel-"aktiv" sind dann jeweils voneinander unabhängig*/
    public int aktiv = 0;

    /**"Haupt-Aktiv-Schalter Preview. Wie aktiv, aber für Preview-Ansicht beim Monitoring.*/ 
     public int aktivPreview = 0;

    /**==1 => Abstimmung wird in Portal / Apps etc. zum Aktionär hin angezeigt, wenn es um Weisungen geht*/
    public int aktivWeisungenInPortal = 0;

    /**==1 => Abstimmung wird auf der HV in HV-Selbstbedienungstools angezeigt, bzw. bei der SRV-Buchung, wenn es um Weisungen beim Verlassen der HV geht. Also z.B. Weisungsterminals*/
    public int aktivWeisungenAufHV = 0;

    /**==1 => Weisungen werden über die Schnittstelle von einem externen System entgegengenommen*/
    public int aktivWeisungenSchnittstelle = 0;

    /**==1 => Weisungen werden intern bei der Pflege angezeigt*/
    public int aktivWeisungenAnzeige = 0;

    /**1  Weisungen werden bei internen Auswertungen angezeigt*/
    public int aktivWeisungenInterneAuswertungen = 0;

    /**1  Weisungen werden bei externen Auswertungen (=> Bankenvertreter) angezeigt*/
    public int aktivWeisungenExterneAuswertungen = 0;

    /**1 Weisungen können intern erfaßt werden*/
    public int aktivWeisungenPflegeIntern = 0;

    /**==1 => Abstimmung wird in Portal / Apps / Tablet etc. zum Aktionär hin angezeigt, wenn es um Abstimmungen geht.
     * Verwendung: wenn eine Abstimmung in einem Abstimmungsblock bereits vorbereitet wird, aber noch nicht hin zum Aktionär
     * veröffentlicht werden soll*/
    public int aktivAbstimmungInPortal = 0;

    /**==1 Weisung aktiv bei SRV (ergänzend zu obigen Aktivierungen*/
    public int aktivBeiSRV = 1;

    /**==1 Weisung aktiv bei Briefwahl (ergänzend zu obigen Aktivierungen*/
    public int aktivBeiBriefwahl = 1;

    /**==1 Weisung aktiv bei KIAV/Dauer (ergänzend zu obigen Aktivierungen*/
    public int aktivBeiKIAVDauer = 1;

    /**==1 Weisung aktiv bei Fragen im Portal*/
    public int aktivFragen = 1;

    /**==1 Weisung aktiv bei Anträgen im Portal*/
    public int aktivAntraege = 1;

    /**==1 Weisung aktiv bei Widersprüchen im Portal*/
    public int aktivWidersprueche = 1;

    /**==1 Weisung aktiv bei Wortmeldungen im Portal*/
    public int aktivWortmeldungen = 1;

    /**==1 Weisung aktiv bei sonstigen Mitteilungen im Portal*/
    public int aktivSonstMitteilungen = 1;

    /**==1 Weisung aktiv bei Botschaften Einreichen im Portal*/
    public int aktivBotschaftenEinreichen = 1;
 
    /********************Anzeige-Position der Weisung*********************************************/
    /**Anzeige-Position bei internen Verwaltungsprogrammen, interner Weisungserfassung etc.*/
    public int anzeigePositionIntern = 0;

    /**Anzeige-Position bei Weisungserfassung im Portal, App etc.*/
    public int anzeigePositionExternWeisungen = 0;

    /**Anzeige-Position bei Weisungserfassung in HV-Tools (also z.B. Weisungsterminal), Erfassungstool für Weisungen
     * beim Verlassen der HV*/
    public int anzeigePositionExternWeisungenHV = 0;

    /*************************mögliche Stimmabgaben********************************/
    /**extern* = die Eingabe dieser Stimmart ist über externe Medien (Portal, ...) bei Weisungen möglich
     * externJa, externNein, externEnthaltung, externSonstiges1* bis 3 können für Bewertungen nach dem
     * Schulnoten-System verwendet werden (Umfragen), mit "Nicht-Teilnahme" als "Weiß nicht"
     * 
     * "intern" - alles wie "extern", nur für die interne-Erfassung
     * "elStiBlo" - alles wie "extern", nur die aktiven Buttons für den elektronischen Stimmblock
     * "tablet" - alles wie "extern", nur für die aktiven Buttons für tablets und "normale" App-Abstimmung sowie Portale bei Abstimmung*/
    public int externJa = 0;
    public int externNein = 0;
    public int externEnthaltung = 0;
    public int externUngueltig = 0;
    public int externNichtTeilnahme = 0;
    /**For Future use - derzeit nicht weiter verfolgt. 
     * Aktuelle Verwendung bei normalen Abstimmungen: Gegenantrag wird unterstützt*/
    public int externSonstiges1 = 0;
    /**For Future use - derzeit nicht weiter verfolgt*/
    public int externSonstiges2 = 0;
    /**For Future use - derzeit nicht weiter verfolgt*/
    public int externSonstiges3 = 0;
    /**externLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
    public int externLoeschen = 0;
    /**Extern frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
    /**For Future use - derzeit nicht weiter verfolgt*/
    public int externFrei = 0;
    /**Länge 40*/
    /**For Future use - derzeit nicht weiter verfolgt*/
    public String externFreiText = "";

    /**Intern - Weisungen und Abstimmungsmarkierungen (bei manueller Eingabe)*/
    public int internJa = 0;
    public int internNein = 0;
    public int internEnthaltung = 0;
    public int internUngueltig = 0;
    public int internNichtTeilnahme = 0;
    public int internSonstiges1 = 0;
    public int internSonstiges2 = 0;
    public int internSonstiges3 = 0;
    /**externLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
    public int internLoeschen = 0;
    /**Extern frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
    public int internFrei = 0;
    public String internFreiText = "";

    /** elStiBlo*/
    public int elStiBloJa = 0;
    public int elStiBloNein = 0;
    public int elStiBloEnthaltung = 0;
    public int elStiBloUngueltig = 0;
    public int elStiBloNichtTeilnahme = 0;
    public int elStiBloSonstiges1 = 0;
    public int elStiBloSonstiges2 = 0;
    public int elStiBloSonstiges3 = 0;
    /**elStiBloLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
    public int elStiBloLoeschen = 0;
    /**elStiBlo frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
    public int elStiBloFrei = 0;
    public String elStiBloFreiText = "";

    /** tablet / App (digitales Abstimmungsmedium)*/
    public int tabletJa = 0;
    public int tabletNein = 0;
    public int tabletEnthaltung = 0;
    public int tabletUngueltig = 0;
    public int tabletNichtTeilnahme = 0;
    public int tabletSonstiges1 = 0;
    public int tabletSonstiges2 = 0;
    public int tabletSonstiges3 = 0;
    /**elStiBloLoeschen ist nicht als eigenständiger Abstimmungspunkt, sondern für Portal als "Anzeige eines Lösch-Buttons" gedacht*/
    public int tabletLoeschen = 0;
    /**elStiBlo frei = keine Standardstimmart, sondern freiText wird als Stimmart angezeigt*/
    public int tabletFrei = 0;
    public String tabletFreiText = "";

    /***************Default-Abgabe speichern******************/
    /**Bei manueller / Scan-Eingabe von Weisungen (Bestandsverwaltung): nicht markierte werden gespeichert als.
     * -1 = wie übergreifende Parameter
     */
    public int weisungNichtMarkierteSpeichernAlsSRV = 0;
    public int weisungNichtMarkierteSpeichernAlsBriefwahl = 0;
    public int weisungNichtMarkierteSpeichernAlsKIAV = 0;
    public int weisungNichtMarkierteSpeichernAlsDauer = 0;
    public int weisungNichtMarkierteSpeichernAlsOrg = 0;

    /**Bei manueller / Scan-Eingabe von Weisungen (Verlassen HV): nicht markierte werden gespeichert als.
     * -1 = wie übergreifende Parameter
     */
    public int weisungHVNichtMarkierteSpeichernAls = 0;

    /**Bei manueller / Scan-Eingabe von Abstimmungen: nicht markierte werden gespeichert als.
     * -1 = wie übergreifende Parameter
     */
    public int abstimmungNichtMarkierteSpeichernAls = 0;

    /***********************div. für Weisungssatz***********************************************/
    /**Nr,, mit der die Abstimmung auf die Array-Position im Weisungssatz verweist
     * -1 == Satz dient nur als "Zwischenüberschrift" (z.B. im Portal)
     * -2 == Satz dient nur als Gesamtmarkierung (für Papier-Abstimmung)
     * 
     * identWeisungssatz identifiziert direkt die Position im Array abgabe z.B. in eclAbstimmungMeldung, d.h. zulässige
     * Werte sind 1 (theoretisch: 0) bis 199.
     * 
     * Die Vergabe erfolgt automatisch bei Neuanlage einer Abstimmung. Achtung! Es erfolgt Wiederverwendung!!!
     * */
    public int identWeisungssatz = 0;

    /**"Übergeordnete" Gesamtweisung, die ggf. übernommen wird - soweit keine Detailweisung vorliegt
     * =-1 => keine Übernahme
     * Enthält den Wert von identWeisungssatz!*/
    public int vonIdentGesamtweisung = -1;
    /*TODO Noch Testen ob das so passiert! - bzw. anders: vermutlich funktioniert das so nicht*/

    /**Stimmausschluß
     * 13 Stellen: V/A/S/1-9,E (E bedeutet: es liegt für diese Karte ein einzelner Ausschluß, Kartenbezogen auf Kandidat, vor)*/
    public String stimmausschluss = "";

    /**Jeweils: Summe Pauschalausschluss, jeweils für die einzelnen Gattungen, mit Angabe ob von N oder J Stimmen abgezogen werden soll.
     * 
     * Achtung - Placebo-Parameter*/
    public long pauschalAusschluss[] = { 0, 0, 0, 0, 0 };
    public int pauschalAusschlussJN[] = { KonstStimmart.ja, KonstStimmart.ja, KonstStimmart.ja, KonstStimmart.ja,
            KonstStimmart.ja };

    /**Diese Abstimmung ist ein Gegenantrag (für Weisungen)*/
    public int gegenantrag = 0;

    /**Für diesen Antrag wurden Gegenanträge auf der HV gestellt
     * 0 = kein
     * 1 = einer
     * 2 = mehrere*/
    public int gegenantraegeGestellt = 0;

    /**Diese Abstimmung ist ein Ergänzungsantrag (für Weisungen)*/
    public int ergaenzungsantrag = 0;

    /**Dieser Beschlussvorschlag wurde gestellt von (KonstBeschlussVorschlagVon):
     * 0=undefiniert
     * 1=Vorstand
     * 2=Aufsichtsrat
     * 3=VorstandAufsichtsrat
     * 4=Sonstige (dann separater Text)
     */
    public int beschlussvorschlagGestelltVon = 3;

    /**Wird gefüllt wenn beschlussvorschlagGestelltVon==4
     * Len=200*/
    public String beschlussvorschlagGestelltVonSonstige = "";

    /**Abstimmung gehört zur Abstimmungsgruppe (z.B.: Wahl)*/
    public int zuAbstimmungsgruppe = 0;

    /**Mehrheit
     *    	"(0) undefiniert",
    		"(1) einfache Stimmen-Mehrheit",
    		"(2) einfache Kapital-Mehrheit",
    		"(3) einfache Stimmen-Mehrheit (je Gattung)",
    		"(4) einfache Kapital-Mehrheit (je Gattung)",
    		"(5) 3/4 Stimmen-Mehrheit",
    		"(6) 3/4 Kapital-Mehrheit",
    		"(7) 3/4 Stimmen-Mehrheit (je Gattung)",
    		"(8) 3/4 Kapital-Mehrheit (je Gattung)",
    		"(9) > 3/4 Stimmen-Mehrheit",
    		"(10) > 3/4 Kapital-Mehrheit",
    		"(11) > 3/4 Stimmen-Mehrheit (je Gattung)",
    		"(12) > 3/4 Kapital-Mehrheit (je Gattung)",
     */
    public int identErforderlicheMehrheit = 0;

    /**stimmberechtigte Gattungen - [gattung-1]*/
    public int[] stimmberechtigteGattungen = { 0, 0, 0, 0, 0 };

    /**Stimmart, die ausgewertet werden soll. -1 = Additionsverfahren*/
    public int stimmenAuswerten = 0;

    /**Formular-Nummer für Kurz-Version
     * Derzeit noch nicht in Funktion (wird wahrscheinlich auch nicht verwendet)*/
    public int formularKurz = 0;
    public int formularLang = 0;
    public int formularBuehnenInfo = 0;

    public String liefereAnzeigeBezeichnungKurz() {
        if (anzeigeBezeichnungKurz.isEmpty()) {
            return anzeigeBezeichnungLang;
        }
        return anzeigeBezeichnungKurz;
    }

    public String liefereAnzeigeBezeichnungKurzEN() {
        if (anzeigeBezeichnungKurzEN.isEmpty()) {
            return anzeigeBezeichnungLangEN;
        }
        return anzeigeBezeichnungKurzEN;
    }

    public boolean liefereIstUeberschift() {
        if (identWeisungssatz == -1) {
            return true;
        } else {
            return false;
        }

    }
}
