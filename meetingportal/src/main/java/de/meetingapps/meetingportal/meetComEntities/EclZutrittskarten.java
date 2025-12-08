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

/**
 * 
 *
 *Verwendungshinweise generell:
 *>	Eine ZutrittsIdent (oder auch Stimmkarte), die der Meldung direkt zugeordnet ist, ist grundsätzlich erst mal
 *	dem Aktionär zugeordnet.
 *> Eine ZutrittsIdent (oder auch stimmkarte), die dem Bevollmächtigten zugeordnet ist, ist grundsätzlich erst mal
 *	dem Bevollmächtigten zugeordnet.
 *> Bei Vorliegen (oder Vorlegen) eine Vollmacht, können auch die übergeordneten Idents verwendet werden - werden aber
 *	bei Zugang dann "umgeordnet".
 *> Der Aktionär kann immer auch die "untergeordneten" verwenden - wird dann dem Aktionär umgeordnet, der Bevollmächtigte
 *	verliert erst mal eine Ident. Kann er sich aber später (so sie vom Aktionär nicht mehr genutzt wird) soweit die Vollmacht
 *	weiter besteht, wieder holen!
 *
 *Welche Zutrittskarten oder Stimmkarten für eine Meldung generell vorliegen, ist in EclZutrittskarten etc. gespeichert.
 *Wem sie zugeordnet werden können und konkret sind, ist aufgrund der gespeicherten Willenserklärungen erkennbar.
 *Die aktuelle Nutzung auf der HV ist in meldungen festgehalten. 
 *
 *
 */
public class EclZutrittskarten {

    /**Primary Key = zutrittsIdent, zutrittsIdentNeben, zutrittsIdentKlasse, zutrittsIdentVers, zutrittsIdentVers_Delayed*/

    /**Mandantennummer*/
    public int mandant = 0;

    /*******************ZutrittsIdent*****************************************************/
    /**Zutrittsidentifikation, i.d.R. Eintrittskartennummer.
     * Zusammen mit mandant und klasse und zutritsIdentVers unique.
     */
    public String zutrittsIdent = "";
    public String zutrittsIdentNeben = "";

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in Db-Klasse*/
    public long db_version;

    /** 0 aus Gast-Nummernkreis, 1 = aus aktienrechtlichem Nummernkreis.
     * Zusammen mit zutrittsIdent, zutrittsIdentNeben, zutrittsIdentVers und mandant UNIQUE.
     * 
     * Aus Gast-Nummernkreis kann nur auf Gäste verweisen.
     * 
     * Aus Aktienrechtlichem Nummernkreis kann auf eine Gastmeldung (=z.B. aktueller Status des Aktionärs)
     * und eine Aktionärsmeldung (=z.B. Aktionärsmeldung, für die SRV-Vollmacht erteilt wurde) verweisen.
     */
    public int zutrittsIdentKlasse = 0;

    public int delayedVorhanden = 0;

    /**Zustand der Stimmkarte
     * 0 = aktiv - voll nutzbar - für Präsenzveränderung und Abstimmung
     * 1 = inaktiv - für Präsenzveränderungen nutzbar, nicht jedoch für Abstimmung. Nutzung: Nicht mehr als Stimmkarte verwendbar, weil auf Gast zeigend.
     *          D.h. eine EK-Nummer aus dem aktienrechtlichen Nummernkreis, die jetzt nur noch als Gast-Ident zählt.
     *          TODO 1 ist noch nicht durchgehend implementiert!
     * 2 = gesperrt - nicht mehr nutzbar
     * Siehe: KonstStimmkarteIstGesperrt*/
    public int zutrittsIdentIstGesperrt = 0;

    public boolean zutrittsIdentWurdeGesperrt() {
        if (zutrittsIdentIstGesperrt == KonstStimmkarteIstGesperrt.gesperrt) {
            return true;
        } else {
            return false;
        }
    }

    public int zutrittsIdentIstGesperrt_Delayed = 0;
    public boolean zutrittsIdentWurdeGesperrt_Delayed() {
        if (zutrittsIdentIstGesperrt_Delayed == KonstStimmkarteIstGesperrt.gesperrt) {
            return true;
        } else {
            return false;
        }
    }

    public boolean zutrittsIdentIstFuerAbstimmungNichtGueltig_Delayed() {
        if (zutrittsIdentIstGesperrt_Delayed!=KonstStimmkarteIstGesperrt.aktiv) {
            return true;
        } else {
            return false;
        }
   }

    /**„Versionierung“ von stornierten, aber wieder freigegebenen zutrittsIdent. Standardmäßig = 0
    * 
    * Wenn eine stornierte Zutrittskarte wieder freigegeben wird, wird der Stornierte Zutrittskartensatz mit der nächsthöchsten verfügbaren
    * Versionsnummern zurückgespeichert und bleibt erhalten. Die Zutrittskarte mit Version==0 kann damit wieder neu vergeben werden.
    * */
    public int zutrittsIdentVers = 0;
    public int zutrittsIdentVers_Delayed = 0;

    /***************************Verweis auf Meldungen*********************************/

    /** Fremdschlüssel zu EclMeldungen - Gast
     * Hinweis: warum auch zu Gast? Erforderlich, da ein bereits anwesender Aktionär, der zum Gast wird, 
     * sich weiterhin über die Eintrittskarte identifiziert*/
    public int meldungsIdentGast = 0;
    /*TODO $Deprecated*/
    @Deprecated
    public int meldungsIdentGast_Delayed = 0;

    /** Fremdschlüssel zu EclMeldungen - Aktionär*/
    public int meldungsIdentAktionaer = 0;
    /*TODO $Deprecated*/
    @Deprecated
    public int meldungsIdentAktionaer_Delayed = 0;

    /**0=Verweis auf Gastmeldung ist aktiv, 1=Verweis auf Aktionaersmeldung ist aktiv*/
    public int gueltigeKlasse = 0;
    public int gueltigeKlasse_Delayed = 0;

    /************************Verweis auf PersonenNatJur**********************************/
    /**"Verwender" der ZutrittsIdent bei der HV-Akkreditierung.*/
    public int personenNatJurIdent = 0;
    public int personenNatJurIdent_Delayed = 0;

    /**Ausgestellt auf.
     * =0 => selbst, d.h. auf Aktionär/Gast.
     * !=0 => ausgestellt auf Bevollmächtigten.
     * Ist wichtig für Portal. Hier werden immer die Rechte dieser Person wahrgenommen (außer es wird bewußt über
     * Abfragen zu einer anderen - Bevollmächtigten - Person gewechselt. 
     */
    public int ausgestelltAufPersonenNatJurIdent = 0;

    /*Verarbeitungshinweis:
     * Vorgänger/Nachfolger nicht mehr erforderlich - ist über Reihenfolge der Willenserklärungen etc. ermittelbar u.I.d.R. eh nicht erforderlich!*/

    /** Kopieren des Objekts nach neu */
    public void copyTo(EclZutrittskarten neu) {
        neu.mandant = this.mandant;
        neu.zutrittsIdent = this.zutrittsIdent;
        neu.zutrittsIdentNeben = this.zutrittsIdentNeben;
        neu.db_version = this.db_version;
        neu.zutrittsIdentKlasse = this.zutrittsIdentKlasse;
        neu.delayedVorhanden = this.delayedVorhanden;
        neu.zutrittsIdentIstGesperrt = this.zutrittsIdentIstGesperrt;
        neu.zutrittsIdentIstGesperrt_Delayed = this.zutrittsIdentIstGesperrt_Delayed;
        neu.zutrittsIdentVers = this.zutrittsIdentVers;
        neu.zutrittsIdentVers_Delayed = this.zutrittsIdentVers_Delayed;
        neu.meldungsIdentGast = this.meldungsIdentGast;
        neu.meldungsIdentAktionaer = this.meldungsIdentAktionaer;
        neu.gueltigeKlasse = this.gueltigeKlasse;
        neu.gueltigeKlasse_Delayed = this.gueltigeKlasse_Delayed;
        neu.personenNatJurIdent = this.personenNatJurIdent;
        neu.personenNatJurIdent_Delayed = this.personenNatJurIdent_Delayed;
        neu.ausgestelltAufPersonenNatJurIdent = this.ausgestelltAufPersonenNatJurIdent;
    }

    public int liefereGueltigeMeldeIdent() {
        if (this.gueltigeKlasse == 0) {
            return this.meldungsIdentGast;
        } else {
            return this.meldungsIdentAktionaer;
        }
    }
}
