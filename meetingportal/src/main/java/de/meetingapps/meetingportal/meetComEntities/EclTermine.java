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

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class EclTermine implements Serializable {

    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(EclTermine.class.getName());

    private static final long serialVersionUID = 5816572851606356371L;

    public int mandant = 0;

    /**Primärschlüssel zusammen mit mandant. Bedeutung gemäß KonstTermine*/
    public int identTermin = 0;

    /**Versionsnummerierung zum Erkennen, ob DB-Satz von anderem User upgedatet wurde.
     * Darf nur von Db-Verwaltung selbst verwendet werden!*/
    public long db_version = 0;

    /**Nummer, die manuell (oder im Batch automatisch durchnummeriert) vergeben wird, aber
     * grundsätzlich fix ist und für die Reihenfolge (reine Darstellung) bzw. zur Identifikation
     * durch die User ("wir reden über Nr. 114") im elektronischen Terminplan verwendet wird.
     * Aus Gründen der Sortierbarkeit "Hauptnummer" und "Unternummer" getrennt, also z.B. 1.3
     */
    public int oeffentlicheFixIdent = 0;
    public int oeffentlicheFixSubIdent = 0;

    /**1 => dieser Termin ist für betterMeeting technisch erforderlich und wird deshalb
     * im "kurzen" Puffer eingelesen.
     * 0 => Termin lediglich für die Terminplanung erforderlich. Im kurzen Puffer nicht eingelesen.
     * Verwendung z.B. später im Online-Terminplan vorgesehen.
     */
    public int technischErforderlicherTermin = 0;

    /**Datum - verarbeitbar, JJJJTTMM (wg. Sortierbarkeit bei Selekt)
     * LEN=8*/
    public String terminDatum = "";

    /**Zeit - verarbeitbar: HH:MM:SS. Kann leer sein, wenn Tagestermin.
     * Uhrzeit immer im Vergleich zur Rechnerzeit (im Portal: Serverzeit)
     * LEN=8*/
    public String terminZeit = "";

    /**Ausgeschriebenes, freies Format, wie der Termin im Portal dargestellt werden soll.
     * Kann z.B. auch sein Samstag, 10. Oktober 2014, 13:00 Uhr (MESZ)
     * LEN=100
     */
    public String textDatumZeitFuerPortalDE = "";
    public String textDatumZeitFuerPortalEN = "";

    /**Text-Beschreibung (freier Text) des Termins
     * LEN=2000*/
    public String beschreibung = "";

    /*Vorgedanken zur Erweiterung zum elektronischen Terminplan:
     *
     * VERANTWORTLICH, VERANTWORTLICH KUNDEN INTERN
     * Da mehrere Verantwortliche möglich, und diese auch durch Idents (leichter Selektierbarkeit; Zuordnung auch von Erinnerungs-Mails) oder Freitext zugeordnet werden können müssen,
     * in separatem Rucksack-Table.
     *
     * STATUSTEXT, STATUSFARBE etc.
     * Da historisch (mit Veränderungszeit) wichtig, in separatem Rucksacktable.
     *
     *
     */

    public EclTermine() {
        super();
//        if (LOGGER.isTraceEnabled()) {
//            LOGGER.trace("EclTermine called:");
//        }
    }

    public EclTermine(int mandant, int identTermin, long db_version, int oeffentlicheFixIdent,
            int oeffentlicheFixSubIdent, int technischErforderlicherTermin, String terminDatum, String terminZeit,
            String textDatumZeitFuerPortalDE, String textDatumZeitFuerPortalEN, String beschreibung) {
        super();
        this.mandant = mandant;
        this.identTermin = identTermin;
        this.db_version = db_version;
        this.oeffentlicheFixIdent = oeffentlicheFixIdent;
        this.oeffentlicheFixSubIdent = oeffentlicheFixSubIdent;
        this.technischErforderlicherTermin = technischErforderlicherTermin;
        this.terminDatum = terminDatum;
        this.terminZeit = terminZeit;
        this.textDatumZeitFuerPortalDE = textDatumZeitFuerPortalDE;
        this.textDatumZeitFuerPortalEN = textDatumZeitFuerPortalEN;
        this.beschreibung = beschreibung;
    }

    // protected void finalize() {
    //    if (LOGGER.isTraceEnabled()) {
    //        LOGGER.trace("EclTermine destructed:");
    //    }
    //}
}
