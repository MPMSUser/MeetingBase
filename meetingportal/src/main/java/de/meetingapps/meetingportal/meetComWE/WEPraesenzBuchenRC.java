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

import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;

public class WEPraesenzBuchenRC extends WERootRC {

    /**Returncodes:
     * 	pmNummernformZutrittsIdentUngueltig (Eintrittskarte oder Gastkarte)
     *  pmNummernformStimmkarte1Ungueltig
     *  pmNummernformStimmkarte2Ungueltig
     *  pmNummernformStimmkarte3Ungueltig
     *  pmNummernformStimmkarte4Ungueltig
     *  pmStimmkarte1FalschZugeordnet
     *  pmStimmkarte2FalschZugeordnet
     *  pmStimmkarte3FalschZugeordnet
     *  pmStimmkarte4FalschZugeordnet
     *  
     *  pfXyNichtVorhanden Die für die Buchung übergebene Identifikation (ZutrittsIdent, Gast, Stimmkarte, Second, ist nicht vorhanden)
     *  pmZutrittsIdentIstStorniert
     *  pmZuordnungFehlerhaft: die übergebene Identifikations-Ident paßt nicht zur übergebenen Meldung
     *  
     * Für übergebene EclMeldung:
     *  pmMeldungsIdentNichtVorhanden
     *  pmMeldungIstStorniert
     *  
     *  pmStimmkarteNichtVorhanden   Technischer Fehler - Darf eigentlich nicht auftreten
     *  
     * Bereits zugeordnete stimmen nicht mit Neu übergebenen überein
     *  pmZuordnungsfehlerZutrittsIdent
     *  pmZuordnungsfehlerStimmkarte1
     *  pmZuordnungsfehlerStimmkarte2
     *  pmZuordnungsfehlerStimmkarte3
     *  pmZuordnungsfehlerStimmkarte4
     *  pmZuordnungsfehlerStimmkarteSecond
     *  
     * Erforderliches Stimmmaterial ist nicht korrekt zugeordnet
     *  pmStimmkarte1Fehlt
     *  pmStimmkarte2Fehlt
     *  pmStimmkarte3Fehlt
     *  pmStimmkarte4Fehlt
     *  pmStimmkarteSecondFehlt
     *  
     *  pmStimmkarte1BereitsVerwendet
     *  pmStimmkarte2BereitsVerwendet
     *  pmStimmkarte3BereitsVerwendet
     *  pmStimmkarte4BereitsVerwendet
     *  pmStimmkarteSecondBereitsVerwendet
     *  
     *  
     * Bei entsprechenden Funktionen:
     *  pmBereitsAnwesend
     *  pmBevollmaechtigterNichtVorhanden
     *  
     *  Alle Fehlermeldungen, die bei Vollmacht an Dritte auftreten könnnen
     *  TODO $_
     *  
     *  pmNichtAnwesend
     *  
     * "Technische" Meldungen:
     *  pfXyWurdeVonAnderemBenutzerVeraendert
     */

    /**=True: ein Protokoll wurde im Hintergrund erzeugt; zum Bündeln auffordern*/
    public boolean buendeln = false;
    public int buendelnProtokollNr = 0;

    /**Enthält die Eintrittskarten, formatiert "nur Eintrittskarte", z.B. für Labelprint, etc.*/
    public List<EclZutrittsIdent> zutrittsIdent = null;

    /**Enthält die Stimmkarten, formatiert "nur Stimmkartennummer", z.B. für Labelprint, etc.
     * [4] enthält StimmkarteSecond!*/
    public List<String[]> zugeordneteStimmkarten = null;

    /**Gebuchter Vertreter - Ident: Ident des gebuchten Vertreters; zur Weiterverwendung in 
     * "Übernahme letzten Vertreter"
     */
    public int gebuchterVertreterIdent = 0;

    /*****************Standard getter/setter**************************************/

    public boolean isBuendeln() {
        return buendeln;
    }

    public void setBuendeln(boolean buendeln) {
        this.buendeln = buendeln;
    }

    public int getBuendelnProtokollNr() {
        return buendelnProtokollNr;
    }

    public void setBuendelnProtokollNr(int buendelnProtokollNr) {
        this.buendelnProtokollNr = buendelnProtokollNr;
    }

}
