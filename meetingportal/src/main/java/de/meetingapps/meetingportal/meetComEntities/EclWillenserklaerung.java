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

import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;

/**Ehemals tbl_praesenzveraenderung*/
public class EclWillenserklaerung implements Serializable {
    private static final long serialVersionUID = -6016266261331771332L;

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant - wird intern vergeben
     * Früherer Name: veraenderungsIdent*/
    public int willenserklaerungIdent = 0;
    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /**Hier wird angegeben, über welchen Schlüssel die Abgabe der
     * Willenserklärung (Identifikation) erfolgte.
     * =0 => piEclMeldung
     * =1 => piMeldungsIdent
     * =2 => piZutrittsIdent und piKlasse
     * =3 => piZutrittsIdent, Klasse wird automatisch ermittelt (piKlasse=-1)
     * =4 => piStimmkarte
     * =5 => piStimmkarteSecond
     */
    public int identifikationDurch = 0;
    public String identifikationZutrittsIdent = ""; /*Length=20*/ /*TODO $ZutrittsIdent Neben*/
    public String identifikationZutrittsIdentNeben = ""; /*Length=2*/

    /**Bei Präsenzbuchungen: gibt an, ob zutrittsIdent eine Aktionärskarte oder eine Gastkarte ist*/
    public int identifikationKlasse = -1; /*in Zusammenhang mit identifkationDurch=2 / piZutrittsIdent*/
    public String identifikationStimmkarte = ""; /*Lenght=20*/
    public String identifikationStimmkarteSecond = ""; /*Length=20*/

    /**Weg, über den Willenserklärung abgegeben wurde. Siehe EnWillenserklaerungWeg
     * =1	Papier (Post), außerhalb der HV (kann aber durchaus während der HV noch sein)
     * =2	Fax , außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * =11	Email , außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * 21 bis 29 wird als "Weg Internet" betrachtet!
     * =21	Portal – außerhalb der HV (kann aber durchaus während der HV noch sein)
     * =22	App – außerhalb der HV (kann aber durchaus während der HV noch sein)
     * 
     * =51  Dienstleister manuell erfaßt
     * 
     * =101	Schnittstelle von Extern
     * 
     * =201	Auf konventionellem Weg - während der HV (Counter, Stimmsammlung Papier ...)
     * =202	Selbstbedienung - während der HV (Terminals, App als präsent aktiviert)
     * =203	Online-Teilnahme
     */
    public int erteiltAufWeg = KonstEingabeQuelle.unbekannt;

    /**Fremdschlüssel zu tbl_meldungen*/
    public int meldungsIdent = 0;
    /**Fremdschlüssel zu tbl_meldungen*/
    public int meldungsIdentGast = 0;

    /**Hinweis zu den folgenden Feldern:
     * in den ersten 4 wird immer eingetragen, zu welcher aktueller Liste der Vorgang erfaßt wurde. Gilt auch für
     * Delayed!
     * Beim Drucken werden alle <aktueller Verzeichnisnummer berücksichtigt, die noch nicht gedruckt wurden, und die noch
     * nicht delayed wurden. In welchem Verzeichnis tatsächlich gedruckt wurde, steht dann in den zweiten 4. Diese können
     * also von den ersten 4 abweichen! (z.B. während 2. Nachtrag delayed erfaßt, ergibt gedruckt im 2. Nachtrag)
     */
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung1, zu der der Vorgang ERFASST wurde*/
    public int zuVerzeichnisNr1 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung2, zu der der Vorgang ERFASST wurde*/
    public int zuVerzeichnisNr2 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung3, zu der der Vorgang ERFASST wurde*/
    public int zuVerzeichnisNr3 = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung4, zu der der Vorgang ERFASST wurde*/
    public int zuVerzeichnisNr4 = 0;

    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung1, zu der der Vorgang GEDRUCKT wurde. 0 = noch nicht gedruckt!*/
    public int zuVerzeichnisNr1Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung2, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    public int zuVerzeichnisNr2Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung3, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    public int zuVerzeichnisNr3Gedruckt = 0;
    /**-1 = Erstpräsenz, ansonsten Nachtragsnummer zu Versammlung4, zu der der Vorgang ERFASST wurde. 0 = noch nicht gedruckt!*/
    public int zuVerzeichnisNr4Gedruckt = 0;

    /**Kennzeichnung der Willenserklärung. Alter Name: veraenderung.
     * In Abhängigkeit der willenserklaerung werden andere Felder gefüllt 
     * oder eben auch nicht.
     * Wenn Willenserklärung=Storno, dann "verweisAufWillenserklaerung*/
    public int willenserklaerung = 0;

    /**Handhabung von Delayed-Willenserklärungen
     * = 0 => ganz normale Willenserklärung
     * = 1 => Willenserklärung ist noch delayed
     * = 2 => Willenserklärung war delayed, ist mittlerweile aufgelöst. Achtung - wird im aktuellen Delay-Verfahren nicht mehr verwendet,
     * und darf auch nicht verwendet werden, da Willenserklärungen mit ==2 im BlWillenserklaerung ignoriert werden!!!
     * Feldname ehemals "delayed"
     */
    public int delayed = 0;

    /**Handhabung von Pending-Willenserklärungen
     * = 0 => ganz normale Willenserklärung
     * = 1 => Willenserklärung ist noch pending
     * = 2 => Willenserklärung war pending, ist mittlerweile aufgelöst.
     */
    public int pending = 0;

    /*TODO $9 Achtung: zutrittsIdent ist hier nicht ermittelbar, ob Gast- oder Aktioärs-ZutrittsIdent. Gelegentlich klären, ob das noch eingebaut werden muß.*/
    /**Bei Zuordnung einer neuen Eintrittskarte: Neu vergebene ZutrittsIdent.
     * Bei Präsenzbuchungen (auch für stimmkarte*): die zum Zeitpunkt der Buchung zugeordneten ident-Dokumente (passend zum ausführenden Aktionär)
     * Length=20*/
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = ""; /*TODO $ZutrittsIdent*/

    /**ersetzte ZutrittsIdent, falls Storno einer solchen
     * Length=20*/
    public String zutrittsIdentErsetzt = "";
    public String zutrittsIdentNebenErsetzt = ""; /*TODO $ZutrittsIdent*/

    /**Lenght=20*/
    public String stimmkarte1 = "";
    /**Lenght=20*/
    public String stimmkarte2 = "";
    /**Lenght=20*/
    public String stimmkarte3 = "";
    /**Lenght=20*/
    public String stimmkarte4 = "";
    /*TODO _EclWIllenserkläerung Hier fehlt noch stimmkarte5 für Auto-Vrgabe*/
    /**Length=20*/
    public String stimmkarteSecond = "";

    /**Stimmen, die aktuell diese Meldung zum Zeitpunkt der Willenserklärung hatte. Erforderlich für 
     * Teilnehmerverzeichnis-Willenserklärungen!
     */
    public long stimmen = 0;

    /**Aktien, die aktuell diese Meldung zum Zeitpunkt der Willenserklärung hatte. Erforderlich für 
     * Teilnehmerverzeichnis-Willenserklärungen!
     */
    public long aktien = 0;

    /**Falls Storno, oder "Delayed-Nachbuchung", oder "Pending-Nachbuchung",
     * dann Verweis auf die stornierte/delayete/Pending Willenserklaerung.
     * Verweisart:
     * =1 => Storno: Verweis auf stornierte Willenserklärung
     * =2 => DelayedNachbuchung: Verweis auf delayete Willenserklärung
     * =3 => Pending-Auflösung: Verweis auf Willenserklärung, die im Pending war
     * =4 => Widerruf: Verweis auf widerrufene Willenserklärung
     * früher: verweisAufVeraenderungsIdemt*/
    public int verweisart = 0;
    /**Falls Storno, oder "Delayed-Nachbuchung", oder "Pending-Nachbuchung",
     * dann Verweis auf die stornierte/delayete/Pending Willenserklaerung.
     * Verweisart:
     * =1 => Storno: Verweis auf stornierte Willenserklärung
     * =2 => DelayedNachbuchung: Verweis auf delayete Willenserklärung
     * =3 => Pending-Auflösung: Verweis auf Willenserklärung, die im Pending war
     * =4 => Widerruf: Verweis auf widerrufene Willenserklärung
     * früher: verweisAufVeraenderungsIdemt*/
    public int verweisAufWillenserklaerung = 0;

    /**wenn >0, dann wurde diese Willenserklärung automatisch generiert aufgrund der Willenserklärung mit ident folgebuchungFuerIdent*/
    public int folgeBuchungFuerIdent = 0;

    /**Ident dessen, der die Willenserklärung abgibt. Durchgängige Verwendung noch unklar. In jedem Fall jedoch zu verwenden bei:
     * > Vollmacht an Dritte - der Vollmachtsgeber
     * 
     * Ident=	Verweis auf tbl_vertreter-Ident, falls von einem Vertreter ausgehend
     * Ident=   -1, falls von Aktionär selbst ausgehend
     * Ident=	0, falls undefiniert.	
     * 
     * Bei Ein-/Ausbuchungen in Sammelkarten (AbgangAus/ZugangIn) ist hier der Vertreter der Sammelkarte eingetragen
     * */
    public int willenserklaerungGeberIdent = 0;

    /**Bei Vollmacht an Dritte: Verweis auf Ident in tbl_vertreter
     * Bei Vollmacht-Storno an sammelkarten: pAufnehmendeSammelkarteIdent (Eingabeparameter übernommen!)
     * Bei Präsenzbuchung: !=0 => mit dieser Präsenzbuchung wurde diese Vollmacht neu eingetragen (separate Willenserklärung) -
     * 		dient als "Merker" für Stornierung dieser Präsenzbuchung*/
    public int bevollmaechtigterDritterIdent = 0;

    /**Bei Vollmachten an Sammelkarten etc.: Verweis auf Ident der Sammelkarte, in der die Aufnahme erfolgt / herausgenommen wird etc.*/
    public int identMeldungZuSammelkarte = 0;

    /**Bei Änderung Vollmachten an Sammelkarten etc.: Verweis auf ident, die geändert wurde
     * (also auf die ursprüngliche, vorhergehende Vollmacht/Weisung*/
    public int identGeaenderteMeldungZuSammelkarte = 0;

    /**Length=19
     * YYYY-MM-DD HH:MM:SS
     */
    public String veraenderungszeit = "";

    /** Ehemals user*/
    public int benutzernr = 0;
    public int arbeitsplatz = 0;

    /**Bei Willenserklärungen Präsenzerfassung: 
     * 		=1 => wurde Qualitätsgesichert mit Kontrollerfassung
     * Bei Willenserklärungen Weisungserfassung in Bestandsverwaltung:
     * 		>0 => ist in Kontrolliste mit der betreffenden lfd. Nr. vorhanden
     * */
    public int istKontrolliert = 0;

    /**=0 => noch nicht im Protokoll gedruckt - wird aber anders verwendet werden, nämlich hier wird die aktuelle Protokollnummer eingetragen*/
    public int protokollnr = 0;

    /**1 => diese Willenserklaerung wurde storniert.
     * Achtung - wird nicht durchgehend gesetzt. Auswertung deshalb mit Vorsicht!
     * Wird derzeit gesetzt bei:
     * > Stornierung einer Vollmacht an Dritte
     */
    public int storniert = 0;
    public int storniert_delayed = 0;

    /**In Planung: Hier wird gekennzeichnet, wie die Willenserklärung erzeugt wurde, z.B. über Batchlauf, um
     * diese dann auch in Gesamt wieder stornieren zu können.
     */
    public int erzeugtUeber = 0;
    public int erzeugtUeberLauf = 0;

    /**In Planung: Kennzeichen, in welchem Export-Lauf diese Willenserklärung erzeugt wurde*/
    public int exportiertUeberLauf=0;
}
