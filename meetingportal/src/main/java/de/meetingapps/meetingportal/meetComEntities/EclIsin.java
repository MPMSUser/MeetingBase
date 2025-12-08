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

import java.util.Objects;

/**
 * 
 * Hinweise zur Verwendung generell von ISINs:
 * ===========================================
 * > In tbl_isin sollten alle ISIN mit zugeordneter Gattung eingetragen sein, die in dem Mandant
 * verwendet werden.
 * > Bei der Emittentenpflege wird - je Gattung - die Standard-ISIN festgelegt (und über ParamBasis gespeichert). Dies ist nötig, um
 * Kompatibilität mit der bisherigen Logik sicherzustellen - denn diese Standard-ISIN wird verwendet, wenn im Aktienregister bei einem
 * Aktionär keine ISIN eingetragen ist.
 * > Ist bei einerm Aktionär eine ISIN eingetragen, dann wird diese vorrangig zur Standard-ISIN verwendet.  
 * 
 * 
 * Hinweise zum Ändern / Löschen von ISINs:
 * ========================================
 * "Low-Level":
 * > ein Ändern einer ISIN ist nicht möglich.
 * > vor dem Löschen einer ISIN muß mit der entsprechenden Funktion in DbAktienregister 
 *      abgefragt werden, ob die ISIN bereits zugeordnet ist - in diesem Fall sollte das Löschen
 *      unterbunden werden.
 *      
 *      
 * "High-Level":
 * Was tun, wenn eine falsche ISIN eingegeben wurde, und schon Aktionäre zugeordnet sind?
 * 1.) Den/die Verantwortlichen enthaupten.
 * 2.) Dann wirds je nach Verfahren Komplex (siehe die folgenden Ausführungen).
 * 
 * a) Wenn die ISIN der falschen Gattung zugeordnet ist, und bereits Aktionäre eingetragen sind, und 
 * diese möglicherweise bereits angemeldet und in Sammelkarten sind,  dann gibts nur eins: schnell weg.
 * Denn: Damit haben die Aktionäre möglicherweise falsche Gattungen, sind in den falschen Sammelkarten,
 * haben falsche Weisungsmöglichkeiten erhalten usw. usf. ....
 * 
 * b) Wenn nur Aktionäre mit der falschen ISIN eingetragen sind, aber der richtigen Gattung zugeordnet sind,
 * könnten einfach die ISIN-Einträge im Aktienregister geändert werden. Für diesen Fall könnte es also ein
 * Tool geben, einfach die ISIN im Aktienregister zu ändern - aber eben nur diese, keinesfalls die Gattung!
 * 
 * Weitere Fälle wurden nicht durchdacht - deshalb grundsätzliche siehe oben 1.) ...
 *
 */
public class EclIsin {

    /*Id (1 bis 5) der Gattung, zu der diese ISIN gehört*/
    public int gaettungId = 0;

    public String isin = "";

    public EclIsin() {

    }

    public EclIsin(int gaettungId, String isin) {
        super();
        this.gaettungId = gaettungId;
        this.isin = isin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gaettungId, isin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclIsin other = (EclIsin) obj;
        return gaettungId == other.gaettungId && Objects.equals(isin, other.isin);
    }

    @Override
    public String toString() {
        return "EclIsin [gaettungId=" + gaettungId + ", isin=" + isin + "]";
    }
}
