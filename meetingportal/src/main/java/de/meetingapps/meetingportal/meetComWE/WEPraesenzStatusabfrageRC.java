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
package de.meetingapps.meetingportal.meetComWE;

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;

public class WEPraesenzStatusabfrageRC extends WERootRC {

    /**Returncode  < 1 => Fehler
     * 
     * Mögliche Fehlercodes:
     * Aus Nummernformprüfung - in diesem Fall ist nur rc gefüllt, sonst nichts!:
     * 	pmNummernformUngueltig
     * 	pmNummernformAktionsnummerUngueltig
     *  pfNichtEindeutig
     *  pmNummernformMandantUngueltig
     * 	pfXyNichtImZulaessigenNummernkreis
     * 	pmNummernformStimmkartenSubNummernkreisUngueltig
     * 	pmNummernformHVIdentNrUngueltig
     * 	pmNummernformKontrollzahlFalsch
     * 	pmNummernformPruefzifferFalsch
     *  pmNummernformStimmartFalsch
     *  
     * Aus Restlicher Prüfung:
     * 
     *  pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich
     *  pmNullBestandBuchenNichtMoeglich (gilt nur für Einzelaktionäre. Sammelkarten mit 0-Bestand können (und müssen!) präsenz buchbar sein.
     *  
     *  pmZutrittsIdentNichtVorhanden 
     *  pmZutrittsIdentIstStorniert
     *  pmStimmkarteNichtVorhanden
     *  pmStimmkarteGesperrt
     *  pmStimmkarteNichtFuerAbstimmungAktiv - noch nicht weiter implementiert!
     *  pmStimmkarteSecondNichtVorhanden
     *  pmStimmkarteSecondGesperrt
     *  pmStimmkarteSecondNichtFuerAbstimmungAktiv - noch nicht weiter implementiert!
     *  
     *  ??????
     *  pmInSammelkarteEnthalten
     *  pmServiceschalter
     * */

    /*********************Grundsätzlicher Hinweis*************************************************
     * Hier ist sehr viel "Vision" enthalten!
     * 
     * nur die mit OK gekennzeichneten Werte sind tatsächlich aktuell verwendbar!
     *********************************************************************************************/

    /******************VVVVV Ab hier: Listen, in der die angegebenen Daten für jede einzelne Identifikationsnummer enthalten ist**********************************/
    /**Aufbereitete Identifikationsnummern*/
    public List<String> identifikationsnummer = null; //Aus BlNummernform - OK
    public List<String> identifikationsnummerNeben = null; //Aus BlNummernform - OK

    /**Kartenklasse zu jeder Identifikationssnummer (insbesondere wichtig bei AppIdent!)
     * 
     * Hinweis: Mischung aus GastkartenIdents und ZutrittsIdents/StimmkartenIdents ist möglich. 
     * Nicht jedoch ZutrittsIdents und StimmkartenIdents gemischt*/
    public List<Integer> kartenklasseZuIdentifikationsnummer = null; //Aus BlNummernform - OK

    /**Fehlermeldung zu jeder einzelnen Identifikationsnummer. Höherwertigste gewinnt. Möglichkeiten:
     * pfXyNichtVorhanden 
     * pmZutrittsIdentIstStorniert / pmStimmkarteGesperrt /pmStimmkarteSecondGesperrt
     * pmStimmkarteNichtAnwesend /pmStimmkarteSecondNichtAnwesend 
     * pmMeldungNichHinreichendSpezifiziert
     * 
     * pmKannNichtPersoenlichPraesentWegenSammelkarte
     */
    public List<Integer> rcZuIdentifikationsnummer = null; //Aus BlNummernform - OK

    /**Liste der Meldungen - je Identifikation. Über jeweilige meldungen festzustellen, ob es
     * eine Gast-Meldung oder eine Aktionärs-Meldung ist.
     * Kann null sein, wenn z.B. Identifikation nicht vorhanden ist o..ä.
     */
    public List<EclMeldung> meldungen = null; //OK
    public List<String> initialPasswort = null; //OK

    /**Für AppIdent: für jede Meldung: darf die App-Person diese Meldung laut
     * hinterlegten Vertretern vertreten, oder muß noch eine separate Vollmacht
     * nachgewiesen werden? 
     */
    public List<Boolean> appVollmachtMussNochVorgelegtWerden = null;

    /**Für AppIdent: für jede Meldung: es wurde eine "Ich bin-Person" für diese Meldung
     * hinterlegt, die (rein von der Ident her) nicht mit der App-Person übereinstimmt.
     * Deshalb muß augenscheinlich überprüft werden, ob die Person (Namen etc.)
     * identisch ist.
     */
    public List<Boolean> appPersonMussMitAppPersonUeberprueftWerden = null;

    /**Für AppIdent: je Meldung: die Person, die in der App vom Teilnehmer mit
     * "ja die bin ich" bestätigt wurde. 
     * Listenelemente können "null" sein, wenn die App (noch) keine Personen-Ident mitliefert.
     */
    public List<EclPersonenNatJur> appVertretendePerson = null;

    /**Wichtiger Hinweis zu zutrittskarten, stimmkarten, stimmkartenSecond: die verwendete Identifikation
     * spezifiziert nicht zwangsläufig auch den Status "Gast oder Aktionärsmeldung"! So kann z.B. 
     * eine Stimmkarte auch einen "Gast bewegen" (der Vollmacht/Weisung an SRV/KIAV erteilt hat und
     * sein Stimmmaterial behalten hat).
     */

    /**Liste der EclZutrittskarten - für Identifikation, falls Identifikation eine ZutrittsIdent ist (sonst null)*/
    public List<EclZutrittskarten> zutrittskarten = null; //OK
    /**Liste der EclStimmkarten - für Identifikation, falls Identifikation eine Stimmkarte ist (sonst null)*/
    public List<EclStimmkarten> stimmkarten = null; //OK
    /**Liste der EclStimmkartenSecond - für Identifikation, falls Identifikation eine StimmkarteSecond ist (sonst null)*/
    public List<EclStimmkartenSecond> stimmkartenSecond = null; //OK

    /**Liste der PersonenNatJur, die bei den verwendeten Identifikationen bereits eingetragen sind. Diese
     * sollen dann bevorzugt "befragt" werden.
     * Die Daten dieser Person sind entweder (Aktionär) in meldungen oder (Bevollmächtigter) in
     * aktionaerVollmachten enthalten, müssen deshalb nicht separat geliefert werden.
     * Zu verwenden für Erstzugang bzw. Wiederzugang mit noch nicht verwendeter Eintrittskarte
     * (erstzugang, wiederzugangMitFrage_SindSie)
     * */
    public List<Integer> vordefiniertePersonNatJur = null;

    /**Liste der Personen, die bei der verwendeten Identifikation fest eingetragen ist. D.h.
     * die durchzuführende Aktion (konkret: wiederzugangOhneFrage_SindSie) darf nur von dieser
     * Person genutzt werden.
     * -1 => Aktionär selbst.
     * >0 => personNatJurIdent in aktionaerVollmachten
     */
    public List<Integer> vorbestimmtePersonNatJur = null;

    /**Liste der Vollmachten zu jeder Identifikation*/
    public List<EclWillensErklVollmachtenAnDritte[]> aktionaerVollmachten = null; //OK

    /**Liste der zu den bereits zugeordneten Stimmkarten gehörenden Eintrittskartennummern - Aktionär selbst
     * (falls Meldung=Gast, aber Identifikation mit Aktionärs-Karten, dann hier die diesem Gast zugeordneten Eintrittskarten von der Klasse Aktionär)*/
    public List<EclZutrittsIdent> zugeordneteEintrittskartenAktionaer = null;
    /**Liste der zu den bereits zugeordneten Stimmkarten gehörenden Eintrittskartennummern - zu allen Bevollmächtigten*/
    public List<EclZutrittsIdent[]> zugeordneteEintrittskartenVollmachten = null;

    /**Liste der bereits zugeordneten Stimmkarten - zum Aktionär selbst
     * (falls Meldung=Gast, aber Identifikation mit Aktionärs-Karten, dann hier die diesem Gast zugeordneten Stimmkarten)*/
    public List<String[]> zugeordneteStimmkartenAktionaer = null;
    /**Liste der bereits zugeordneten StimmkartenSecond - zum Aktionär selbst
     * (falls Meldung=Gast, aber Identifikation mit Aktionärs-Karten, dann hier die diesem Gast zugeordneten Stimmkarten)*/
    public List<String> zugeordneteStimmkartenSecondAktionaer = null;

    /**Liste der bereits zugeordneten Stimmkarten - zu allen Bevollmächtigten*/
    public List<String[][]> zugeordneteStimmkartenVollmachten = null;
    /**Liste der bereits zugeordneten StimmkartenSecond - zu allen Bevollmächtigten*/
    public List<String[]> zugeordneteStimmkartenSecondVollmachten = null;

    /**Liste der Zuordnungen, die durchgeführt werden müssen (zu Stimmkarten)
     * früher: [0-3]=Stimmkarte; [4]=Stimmkartesecond
     * Jetzt: [0-3]=Stimmkarte; [4]=Stimmkarte virtuell; [5]=Stimmkartesecond
     * Wert: 0 = keine Zuordnung erforderlich; 1=Zuordnung erforderlich; -1=Zuordnung bereits vorhanden
     */
    public List<int[]> stimmkartenZuordnungenAktionaer = null;
    public List<int[][]> stimmkartenZuordnungenVollmachten = null;

    /**Feldbezeichnung für Zuordnung  (6 Elemente (früher: 5)!). Muß für Aktionäre und Bevollmächtigte gleich sein, 
     * da nur von Gattung abhängig*/
    public List<String[]> stimmkartenZuordnungenText = null;

    /**Liste der Sammelkarten, in der die meldung enthalten ist*/
    public List<EclMeldungZuSammelkarte[]> inSammelkarten = null; //OK

    /**Erforderliche Aktionen, bevor Zulässige Funktionen ausgeführt werden können:
     * hinweisSammelkartenZuordnungVorhanden
     * sammelkartenZuordnungMussWiderrufenWerden
     * sammelkartenZuordnungMussDeaktiviertWerden
     * 
     * vollmachtenMuessenGgfAutomatischWiderrufenWerden
     * vollmachtenMuessenGgfInTextformWiderrufenWerden
     */
    public List<List<Integer>> erforderlicheAktionen = null;

    /**Zulässige Funktionen (unter Auswertung von Kartenklasse und Kartenart)*/
    public List<List<Integer>> zulaessigeFunktionenMitAktionsnummer = null; //OK

    /**Zulässige Funktionen (unter Ignorierung von Kartenklasse und Kartenart; beinhaltet alle auch
     * in zulaessigeFunktionenMitAktionsnummer eingetragenen!):
     * 
     * abgang
     * vertreterwechsel
     * stimmkartenwechselNachAbstimmung
     * stimmkartenwechsel
     * verlassenUndVollmachtUndWeisungAnKIAV
     * verlassenUndVollmachtUndWeisungAnSRV
     * 
     * erstzugang
     * 
     * wiederzugang_beliebigePerson (Wiederzugang, aber mit unverbrauchter Eintrittskarte)
     * wiederzugang_nurSelbePerson (verbrauchte EK, oder bereits benutzte Stimmkarte)
     * wiederzugang_nurSelbePersonMitNeuerVollmacht
     * 
     * abgangGast
     * zugangGast
     * wiederzugangGast_nurSelbePerson
     * 
     * Hinweis: nicht mehr verwendet wird derzeit:
     * gastAnlegenUndZugangAlsGast. Begründung: falls Papierweg, dann Sonderschalter. Denn EK muß durch Gastkarte ersetzt werden. Falls App, dann Entscheidung übergreifend, falls
     * noch keine Gastkarte in der App vorhanden.
     * TODO $App
     * */
    public List<List<Integer>> zulaessigeFunktionenAlle = null; //OK

    /*TODO $App
     * Zulässige Funktionen - gemeinsame Liste - mit dieser Aktionsnummer
     * 
     * Handelnde PersonNatJur
     */

    /****************** Bis hier: Listen, in der die angegebenen Daten für jede einzelne Identifikationsnummer enthalten ist**********************************/

    /**Anzahl der Aktionäre, die in den obigen Listen enthalten sind*/
    public int anzMeldungenAktionaere = 0;

    /**Liste der gemeinsamen Vollmachten (d.h. der Vollmachten, die für alle Identifikationsnummern gemeinsam zulässig sind*/
    @Deprecated
    public List<EclWillensErklVollmachtenAnDritte> gemeinsameVollmachten = null;

    /**Liste der für diesen Vorgang insgesamt hinterlegten Personen*/
    @Deprecated
    public List<EclPersonenNatJur> hinterlegtePersonen = null;

    /**Tabelle, welche hinterlegte Person welche Karten bereits laut System vertreten darf*/
    @Deprecated
    public List<boolean[]> hinterlegtePersonPasstZuMeldung = null;

    /**Zulässige Funktionen - gemeinsam für alle Identifikationen (kleinste gemeinsame Menge):
     * 
     * */
    @Deprecated
    public List<Integer> gemeinsameZulaessigeFunktionenMitAktionsnummer = null;

    /**Zulässige Funktionen - gemeinsam für alle Identifikationen (kleinste gemeinsame Menge), passend zur Aktionsnummer:
     * 
     * wiederzugang (=Erstzugang oder Wiederzugang für Aktionäre oder Gäste)
     * abgang (=Abgang Aktonäre oder Gäste)
     * */
    public List<Integer> gemeinsameZulaessigeFunktionenAlle = null;

    /**Gemeinsame, vordefinierte PersonnatJur - die vorrangig zu behandeln ist*/
    public int gemeinsameVordefiniertePersonNatJur = 0;

    /**Aktionsnummer - aufgeteilt auf 3 Stellen*/
    /**Übergreifende Kartenklasse. Insbesondere appIdent.*/
    public int rcKartenklasse = 0; //OK

    /**Wird auf unbekannt gesetzt, wenn Eintrittskarte auch für Abgang/Wiederzugang verwendet werden kann -
     * d.h. in diesem Fall wird die Kartenart ignoriert
     */
    public int rcKartenart = 0; //OK
    public int rcStimmkarteSubNummernkreis = 0; //OK

    /**Falls AppIdent: handelnde von der App übergebende PersonNatJur. Kann aber 0 sein*/
    public int appIdentPersonNatJurIdent = 0; //OK
    public EclPersonenNatJur appIdentPersonNatJur = null;

    /**Passwort, dass ggf. mit der Nummer eingelesen wurde*/
    public String rcPasswort="";
    
    /**Vip-Aktionen*/
    /*TODO #9 VIP-Aktionen*/

    /**Aktueller Präsenzmodues
     */

    /**StimmkartenIdent*/

    /***********************Standard Getter und Setter******************************************************/

    public List<String> getIdentifikationsnummer() {
        return identifikationsnummer;
    }

    public void setIdentifikationsnummer(List<String> identifikationsnummer) {
        this.identifikationsnummer = identifikationsnummer;
    }

    public List<Integer> getKartenklasseZuIdentifikationsnummer() {
        return kartenklasseZuIdentifikationsnummer;
    }

    public void setKartenklasseZuIdentifikationsnummer(List<Integer> kartenklasseZuIdentifikationsnummer) {
        this.kartenklasseZuIdentifikationsnummer = kartenklasseZuIdentifikationsnummer;
    }

    public List<Integer> getRcZuIdentifikationsnummer() {
        return rcZuIdentifikationsnummer;
    }

    public void setRcZuIdentifikationsnummer(List<Integer> rcZuIdentifikationsnummer) {
        this.rcZuIdentifikationsnummer = rcZuIdentifikationsnummer;
    }

    public List<EclZutrittskarten> getZutrittskarten() {
        return zutrittskarten;
    }

    public void setZutrittskarten(List<EclZutrittskarten> zutrittskarten) {
        this.zutrittskarten = zutrittskarten;
    }

    public List<EclStimmkarten> getStimmkarten() {
        return stimmkarten;
    }

    public void setStimmkarten(List<EclStimmkarten> stimmkarten) {
        this.stimmkarten = stimmkarten;
    }

    public List<EclStimmkartenSecond> getStimmkartenSecond() {
        return stimmkartenSecond;
    }

    public void setStimmkartenSecond(List<EclStimmkartenSecond> stimmkartenSecond) {
        this.stimmkartenSecond = stimmkartenSecond;
    }

    public List<Integer> getVordefiniertePersonNatJur() {
        return vordefiniertePersonNatJur;
    }

    public void setVordefiniertePersonNatJur(List<Integer> vordefiniertePersonNatJur) {
        this.vordefiniertePersonNatJur = vordefiniertePersonNatJur;
    }

    public List<EclWillensErklVollmachtenAnDritte[]> getAktionaerVollmachten() {
        return aktionaerVollmachten;
    }

    public void setAktionaerVollmachten(List<EclWillensErklVollmachtenAnDritte[]> aktionaerVollmachten) {
        this.aktionaerVollmachten = aktionaerVollmachten;
    }

    public List<EclWillensErklVollmachtenAnDritte> getGemeinsameVollmachten() {
        return gemeinsameVollmachten;
    }

    public void setGemeinsameVollmachten(List<EclWillensErklVollmachtenAnDritte> gemeinsameVollmachten) {
        this.gemeinsameVollmachten = gemeinsameVollmachten;
    }

    public List<EclMeldungZuSammelkarte[]> getInSammelkarten() {
        return inSammelkarten;
    }

    public void setInSammelkarten(List<EclMeldungZuSammelkarte[]> inSammelkarten) {
        this.inSammelkarten = inSammelkarten;
    }

    public List<List<Integer>> getErforderlicheAktionen() {
        return erforderlicheAktionen;
    }

    public void setErforderlicheAktionen(List<List<Integer>> erforderlicheAktionen) {
        this.erforderlicheAktionen = erforderlicheAktionen;
    }

    public List<List<Integer>> getZulaessigeFunktionen() {
        return zulaessigeFunktionenMitAktionsnummer;
    }

    public void setZulaessigeFunktionen(List<List<Integer>> zulaessigeFunktionen) {
        this.zulaessigeFunktionenMitAktionsnummer = zulaessigeFunktionen;
    }

    public List<String[]> getStimmkartenZuordnungenText() {
        return stimmkartenZuordnungenText;
    }

    public void setStimmkartenZuordnungenText(List<String[]> stimmkartenZuordnungenText) {
        this.stimmkartenZuordnungenText = stimmkartenZuordnungenText;
    }

    public List<Integer> getGemeinsameZulaessigeFunktionen() {
        return gemeinsameZulaessigeFunktionenMitAktionsnummer;
    }

    public void setGemeinsameZulaessigeFunktionen(List<Integer> gemeinsameZulaessigeFunktionen) {
        this.gemeinsameZulaessigeFunktionenMitAktionsnummer = gemeinsameZulaessigeFunktionen;
    }

    public List<Integer> getGemeinsameZulaessigeFunktionenMitAktionsnummer() {
        return gemeinsameZulaessigeFunktionenAlle;
    }

    public void setGemeinsameZulaessigeFunktionenMitAktionsnummer(
            List<Integer> gemeinsameZulaessigeFunktionenMitAktionsnummer) {
        this.gemeinsameZulaessigeFunktionenAlle = gemeinsameZulaessigeFunktionenMitAktionsnummer;
    }

    public int getGemeinsameVordefiniertePersonNatJur() {
        return gemeinsameVordefiniertePersonNatJur;
    }

    public void setGemeinsameVordefiniertePersonNatJur(int gemeinsameVordefiniertePersonNatJur) {
        this.gemeinsameVordefiniertePersonNatJur = gemeinsameVordefiniertePersonNatJur;
    }

    public int getRcKartenklasse() {
        return rcKartenklasse;
    }

    public void setRcKartenklasse(int rcKartenklasse) {
        this.rcKartenklasse = rcKartenklasse;
    }

    public int getRcKartenart() {
        return rcKartenart;
    }

    public void setRcKartenart(int rcKartenart) {
        this.rcKartenart = rcKartenart;
    }

    public int getRcStimmkarteSubNummernkreis() {
        return rcStimmkarteSubNummernkreis;
    }

    public void setRcStimmkarteSubNummernkreis(int rcStimmkarteSubNummernkreis) {
        this.rcStimmkarteSubNummernkreis = rcStimmkarteSubNummernkreis;
    }

    public int getAppIdentPersonNatJurIdent() {
        return appIdentPersonNatJurIdent;
    }

    public void setAppIdentPersonNatJurIdent(int appIdentPersonNatJurIdent) {
        this.appIdentPersonNatJurIdent = appIdentPersonNatJurIdent;
    }

}
