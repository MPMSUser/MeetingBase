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

import de.meetingapps.meetingportal.meetComKonst.KonstStimmkarteIstGesperrt;

public class EclStimmkarten {

    /**Primary Key = stimmkarte, stimmkarteVers, stimmkarteVers_Delayed*/
    
    /**Mandantennummer*/
    public int mandant = 0;

    /**********************Stimmkarten**********************************************/
    /**Lenght=20*/
    public String stimmkarte = "";

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in Db-Klasse*/
    public long db_version;

    /**Spezifiziert, aus welchem der Stimmkarten-Subnummernkreise die Stimmkarte ist.
     * Werte können 1 bis 5 sein, jeweils +(Gattung-1)*5 
     * 
     * Grundsätzlich ist dies eine redundante Information, da eine Stimmkarte eindeutig einem Subnummernkreis zugeordnet werden könnte.
     * Allerdings ist dieses Feld aus Performancegründen sinnvoll, da damit eine aufwändige Bestimmung, zu welchem Subnummernkreis eine Stimmkarte
     * gehört, beim Einlesen entfällt.
     * */
    public int ausSubnummernkreis = 0;

    public int delayedVorhanden = 0;

    /**Zustand der Stimmkarte
     * 0 = aktiv - voll nutzbar - für Präsenzveränderung und Abstimmung
     * 1 = inaktiv - für Präsenzveränderungen nutzbar, nicht jedoch für Abstimmung. Nutzung: Nicht mehr als Stimmkarte verwendbar, weil auf Gast zeigend.
     *          TODO 1 ist noch nicht durchgehend implementiert!
     * 2 = gesperrt - nicht mehr nutzbar
     * Siehe: KonstStimmkarteIstGesperrt*/
    public int stimmkarteIstGesperrt = 0;

    public boolean stimmkarteWurdeGesperrt() {
        if (stimmkarteIstGesperrt == KonstStimmkarteIstGesperrt.gesperrt) {
            return true;
        } else {
            return false;
        }
    }

    public int stimmkarteIstGesperrt_Delayed = 0;
    public boolean stimmkarteWurdeGesperrt_Delayed() {
        if (stimmkarteIstGesperrt_Delayed == KonstStimmkarteIstGesperrt.gesperrt) {
            return true;
        } else {
            return false;
        }
    }

    /**„Versionierung“ von stornierten, aber wieder freigegebenen Stimmkarten. Standardmäßig = 0
     * 
     * Wenn eine stornierte Stimmkarte wieder freigegeben wird, wird der Stornierte Stimmkartensatz mit der nächsthöchsten verfügbaren
     * Versionsnummern zurückgespeichert und bleibt erhalten. Die Stimmkarte mit Version==0 kann damit wieder neu vergeben werden.
     * */
    public int stimmkarteVers = 0;
    public int stimmkarteVers_Delayed = 0;

    /*zu Delayed: 
     * > Sperren während der Abstimmung: hier wird i.d.R. nicht delayed, da der Aktionär ja mit dem neuen Block noch
     * 		abstimmen kann.
     * > Sperren nach der Abstimmung - während Auszählungsphase: hier wird delayed gesperrt. 
     */

    /***************************Verweis auf Meldungen*********************************/

    /** Fremdschlüssel zu EclMeldungen - Gast
     * Hinweis: warum auch zu Gast? Erforderlich, da ein bereits anwesender Aktionär, der zum Gast wird, 
     * sich weiterhin über die Stimmkarte identifiziert*/
    public int meldungsIdentGast = 0;

    @Deprecated
    public int meldungsIdentGast_Delayed = 0;

    /** Fremdschlüssel zu EclMeldungen - Aktionär*/
    public int meldungsIdentAktionaer = 0;

    @Deprecated
    public int meldungsIdentAktionaer_Delayed = 0;

    /*Zu Delayed: Verweis auf meldungsIdent verändert sich nicht. Wenn, dann wird gesperrt bzw. eine neue Version angelegt*/

    /**0=Verweis auf Gastmeldung ist aktiv, 1=Verweis auf Aktionaersmeldung ist aktiv*/
    public int gueltigeKlasse = 0;
    public int gueltigeKlasse_Delayed = 0;

    /************************Verweis auf PersonenNatJur**********************************/
    /**Inhaber des Stimmmarterials. Bei Vollmacht an Dritte wird Stimmaterial weitergegeben (wenn es Papierstimmaterial ist). Bei
     * KIAV bleibt das Stimmaterial der Person zugeordnet, diese kann damit weiter als Gast teilnehmen und Widerrufen etc.
     * 
     * Delayed ist erforderlich, da bei Vollmacht an Dritte das Stimmaterial weitergegeben wird
     * >0 => Bevollmächtigter mit dieser Ident
     * -1 => Aktionär(Gast) selbst
     * */
    public int personenNatJurIdent = 0;
    public int personenNatJurIdent_Delayed = 0;

    /**************************Verweis auf ZutrittsIdent*******************************/
    /**ZutrittsIdent, aufgrund derer die Stimmkarte ausgegeben wurde. Grundsätzlich eigentlich nicht erforderlich. Aber ggf. für
     * Ausdruck der Eintrittskartennummer im Teilnehmerverzeichnis ggf. erforderlich.
     * 
     * Delayed nicht erforderlich, da sich diese Zuordnung nicht ändert.
     * 
     * Achtung: Zu einer ZutrittsIdent können mehrere Stimmkarten ausgegeben werden. Z.B. wenn Zutrittsident von mehreren Personen 
     * genutzt wird (nacheinander)
      */
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    public int liefereGueltigeMeldeIdent() {
        if (this.gueltigeKlasse == 0) {
            return this.meldungsIdentGast;
        } else {
            return this.meldungsIdentAktionaer;
        }
    }

}
