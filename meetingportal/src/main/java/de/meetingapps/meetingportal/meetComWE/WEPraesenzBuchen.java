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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;

/**Ausführen einer Präsenzbuchung*/

public class WEPraesenzBuchen {

    public WELoginVerify weLoginVerify = null;

    /**=0 => nicht vorhanden; =1 => bereits geprüft, enthält nur noch Identifikation; =2 => noch zu prüfender String.
     * Bei ==2 (zu prüfender String) nur .zutrittsIdent gefüllt, ansonsten zutrittsIdent und zutrittsIdentNeben*/
    public List<Integer> zutrittsIdentAktionaerArt = null;
    public List<EclZutrittsIdent> zutrittsIdentAktionaer = null;

    /**=0 => nicht vorhanden; =1 => bereits geprüft, enthält nur noch Identifikation; =2 => noch zu prüfender String*/
    public List<Integer> zutrittsIdentGastArt = null;
    public List<EclZutrittsIdent> zutrittsIdentGast = null;

    /**=0 => nicht vorhanden; =1 => bereits geprüft, enthält nur noch Identifikation; =2 => noch zu prüfender String
     * 4 Elemente!*/
    public List<int[]> stimmkartenArt = null;
    public List<String[]> stimmkarten = null;

    /**=0 => nicht vorhanden; =1 => bereits geprüft, enthält nur noch Identifikation; =2 => noch zu prüfender String*/
    public List<Integer> stimmkartenSecondArt = null;
    public List<String> stimmkartenSecond = null;

    /**Zumindest meldungsIdent muß gefüllt sein*/
    //	@Deprecated
    //	public List<EclMeldung> eclMeldungAktionaer=null;
    //	@Deprecated
    //	public List<EclMeldung> eclMeldungGast=null;
    public List<EclMeldung> eclMeldung = null;

    /**Falls gefüllt: "Person ist identisch zur App-Person" setzen.
     * Mögliche Werte: 
     * 0 => nichts setzen
     * -1 => Person der Meldung selbst ergänzen
     * >0 => Bevollmächtigten ergänzen
     */
    public List<Integer> istIdentisch = null;
    /** -1 => keine Vollmacht einzutragen; 
     * 0 => neue Vollmacht (Daten stehen in vertreterName, vertreterVorname, vertreterOrt bzw. in neueVollmachtPersonenNatJurIdent; 
     * >1 => Verweis auf die entsprechende PersonNatJurIdent
     * -2 => bei Wiederzugang, Abgang, SRV: keine Änderung der Person*/
    public List<Integer> vollmachtPersonenNatJurIdent = null;

    public String vertreterName = "";
    public String vertreterVorname = "";
    public String vertreterOrt = "";
    public int neueVollmachtPersonenNatJurIdent = 0;

    /**Funktion, aus der heraus der Aufruf abgesetzt wurde. In Abhängigkeit dieses Wertes erfolgen ggf. Fehlermeldungen,
     * falls die Bearbeitung dieser Karte in dieser Funktion (noch) nicht möglich ist
     * 1 = normale Akkreditierung (Schalter)
     * 2 = Vorab-Akkreditierung (in Vorbereitung!!)
     * 3 = Vertreternacherfassung
     */
    public int programmFunktion = 0;

    /**true -> die übergebenen Daten sind aus einer AppIdent heraus*/
    public boolean appIdent = false;

    /**Präsenzänderungsfunktion
     *  erstzugang
     *  wiederzugang
     *  abgang
     *  vertreterwechsel
     *  
     * */
    public List<Integer> funktion = null;

    /**durchzuführende Zusatzaktionen (z.B. Vollmachtswiderruf o.ä.)*/
    public List<Integer> aktionen = null;

    /**Falls AppIdent: hier wird die Nummer der Person der App eingetragen*/
    public int appIdentPersonNatJurIdent = 0;

    public WEPraesenzBuchen() {
        funktion = new LinkedList<Integer>();
        vollmachtPersonenNatJurIdent = new LinkedList<Integer>();
    }

    /************************Standard Getter und Setter**********************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

}
