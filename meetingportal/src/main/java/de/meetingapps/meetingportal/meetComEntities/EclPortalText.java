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

public class EclPortalText {

    /**Allgemeines:
     * Portal- und App-texte - mandantenspezifisch: 
     * > im SchemaAllgemein liegen die Standard-Texte (tbl_portaltexte)
     * > im SchemaMandant liegt das "Gerüst" und die Mandantenspezifischen Texte (tbl_portaltexte).
     * 
     * App-Texte (ggf. später auch mal Portaltexte) für den allgemeinen, mandantenübergreifenden Teil:
     * > liegen im SchemaAllgemein (tbl_portaltextebo)
     * 
     * Wichtig: bei den Standard-Texten sowie bei denn übergreifende App-Texte darf "Standardtexte verwenden"
     * NICHT aktiv sein!!!
     * 
     */

    /**0 => übergreifende Texte*/
    public int mandant = 0;

    /**Wird nur bei übergreifenden Texten verwendet. Kennzeichnet das Release der Standardtexte.
     * Im jeweiligen Mandant wird dann das Release wie in den Parametern eingestellt verwendet
     */
    public int basisSet = 0;

    public long db_version = 0;

    /**Zugriff:
     * In EE-Applikation / Portal: über ident (Hintergrund: in EE-Applikation werden alle Texte im Speicher gehalten - in einem fortlaufenden Array)
     * In App: über seitennummer und ident. (Hintergrund: in app wird jeweils nur eine Seite im Speicher gehalten)
     */

    /**Der Text steht im Portal zur Verfügung*/
    public boolean textInPortal = true;

    /**Fortlaufende Nummer - zum Speichern in Array, bzw. Zugriff aus Portal heraus.*/
    public int identGesamt = 0;

    /**Nummer eines Textes, der im Zusammenhang mit diesem Text steht (z.B.: Seiten-Titel in der App hängt mit Überschrift zusammen)*/
    public int verbundenMitIdentGesamt = 0;

    /**Zu einer identGesamt kann es bliebig viele Texte geben, die - sortiert nach lfdNummer - beim Anzeigen
     * aneinandergehängt werden. Dies ist der Tatsache geschuldet, dass SQLite nach vorliegenden Informationen
     * maximal 500 Zeichen in einer Text-Spalte kann.
     * Die aufeinanderfolgenden Texte werden strikt hintereinander gehängt. Achtung: Leerzeichen am Schluß werden
     * (NEU!) nicht (alt: werden) abgeschnitten.
     * TODO _AppTexte: Handling Leerzeichen 
     * Nur für ALT: Soll also zwischen zwei Texten ein Blank sein, dann muß dies am Anfang des zweiten Textes
     * eingefügt werden.
     */
    public int lfdNummer = 0;

    /**Der Text steht in App zur Verfügung*/
    public boolean textInApp = true;

    /**Seiten-Nummer innerhalb der App - für App-Texte*/
    public int seitennummer = 0;

    /**Text-Ident auf der entsprechenden Seite - für AppTexte*/
    public int ident = 0;

    /**Festkodiert: 1=Deutsch, 2=Englisch*/
    public int sprache = 0;

    public String seitenName = "";
    public String beschreibung = "";

    /**Hierüber erfolgt der Update/Übertragung zu App. Es werden immer alle Texte zur App
     * übertragen, deren Versionsnummer höher ist als die aktull in der App hinterlegte
     * Versionsnummer.
     */
    public int letzteVersion = 0;

    /**wenn vonStandarVerwenden==true, dann werden ALLE Texte der jeweiligen Sprache (also Portal,
     * adaptiv, und App) vom Standard verwendet - ansonsten KEINER.
     */
    public boolean portalVonStandardVerwenden = false;

    public String portalText = "";

    /**Bezogen auf portalText*/
    public boolean portalAdaptivAbweichend = false;
    public String portalAdaptivText = "";

    /**Bezogen auf portalText*/
    public boolean appAbweichend = false;
    public String appText = "";

    /*Jeweils in <<< >>> - wird 1:1 als erstes ersetzt
     * siehe auch EclPortalTexteM bzw. Oberfläche - hier vollkommen unvollständig!
     * HVDatum
     * Gesellschaft
     * B /B = Bold
     * I /I = italic
     * NL = New Line
     * H1 /H1, H2 /H2, H3 /H3, H4 /H4 = Überschriften, bzw. bei App
     * Verschiedene Schriftgrößen
     * 
     * Jeweils in $$$ $$$: Befehlsketten
     * LINK LINKTEXT /LINK
     * 
     * Jeweils in [[[ ]]] Bedingungen
     * APPVORHANDEN /APPVORHANDEN
     * BRIEFWAHLVORHANDEN /BRIEFWAHLVORHANDEN
     * 
     * Jeweils in ### ###: Phasen
     * PHASE=0,1,2 ... 9  /PHASE
     * 
     * Wichtig:
     * Verschachtelungen sind nach folgenden Regelungen möglich:
     * > Innerhalb der selben Art (also z.B. [[[ ]]]  nochmal innerhalb von [[[ ]]]): nicht zulässig!
     * > Die jeweils oberen sind in den unteren zulässig
     * > ### ### ist auch innerhalb von [[[ ]]] zulässig
     * 
     * Normale HTML-Kommandos sind grundsätzlich zulässig - jedoch nicht in den App-Texten! D.h. wenn HTML-Kommandos verwendet werden, 
     * müssen die App-Texte grundsätzlich separat gespeichert werden!
     * 
     */

}
