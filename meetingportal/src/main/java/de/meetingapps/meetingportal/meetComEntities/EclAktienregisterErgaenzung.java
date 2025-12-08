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
import java.util.Arrays;
import java.util.Objects;

/**Enthält alle möglichen Daten zum Aktionär, die üblicherweise in der normalen HV-Verarbeitung
 * nicht verwendet werden.
 */
public class EclAktienregisterErgaenzung implements Serializable {
    private static final long serialVersionUID = 8057602757218496280L;

    /**Mandantennummer*/
    public int mandant = 0;

    /** eindeutiger Key für Aktienregistersatz (zusammen mit mandant und satzNummer), der unveränderlich ist. 
     * Wird in diesem Fall nicht neu vergeben, sondern muß bereits gefüllt sein,
     * da dieses Feld der "Foreign-Key" zu EclAktienregisterEintrag ist.
     */
    public int aktienregisterIdent = 0;

    /**0 => Originalsatz wie geliefert
     * >0 => Vom Mitglied geänderte Daten, fortlaufend numeriert
     */
    public int satzNummer = 0;

    /**Versionsnummer - manuell nicht verändern! Ausschließlich durch Verwendung in DbMeldungen*/
    public long db_version;

    /**LEN=120
     * ku178: div. Felder Mitgliedsdaten gemäß KonstAktienregisterErgaenzung*/
    public String[] ergaenzungLangString = null;

    /**
     * Wahrscheinlich für Generalversammlung veraltet! 
     * Für Dialogveranstaltung überprüft - ist noch aktuell.
     * Dialogveranstaltungsnummern maximal 1 bis 23
     * (22.10.2021)
     * LEN=40
     * ku178: 
     * [0] = Anmeldung / Abmeldung zur Generalversammlung
     * [1] bis [x] = Anmeldung / Abmeldung zur Dialogveranstaltung des jeweiligen Bundeslandes
     * 
     * Jeweils: 
     * > 1. Ziffer - aktueller Stand (letzter Stand aus 1. und 2. Ziffer)
     * > 2. Ziffer - durchgeführt durch Mitglied
     * > 3. Ziffer - durchgeführt durch ku178
     * 0=keine Aktion (oder: String ganz leer)
     * 1=angemeldet
     * 2=abgemeldet
     * Für Generalversammlung:
     * 3=mit 2 Personen angemeldet
     * */
    public String[] ergaenzungKurzString = null;

    /**ku178: analog zu ergaenzungKurzString: Anzahl der insgesamt gemeldeten Personen
     * 
     * Zusätzlich: [24]= "Fester Vertreter-Ident" (Verweis auf PersonNatJur).
     * Wird beim Online-Präsenz-Zugang über das Portal automatisch als Bevollmächtigter eingebucht,
     * auch wenn sonst keine Vollmacht vorliegt.
     * [25] != 0 => bei Minderjährigen: Gastkarte für das Kind wurde ausgestellt, Verweis auf meldeIdent
     * [26] != 0 => bei minderjährigen: Gastkarte für zweiten gesetzlichen Vertreter wurde ausgestellt, Verweis auf meldeIdent
     * */
    public int[] ergaenzungKennzeichen = null;

    public EclAktienregisterErgaenzung() {
        ergaenzungLangString = new String[30];
        ergaenzungKurzString = new String[30];
        ergaenzungKennzeichen = new int[30];
        for (int i = 0; i < 30; i++) {
            ergaenzungLangString[i] = "";
            ergaenzungKurzString[i] = "";
            ergaenzungKennzeichen[i] = 0;
        }
    }

    public String getErgaenzungString(int pOffset) {
        if (pOffset < 100) {
            return ergaenzungLangString[pOffset];
        } else {
            return ergaenzungKurzString[pOffset - 100];
        }
    }

    public void setErgaenzungString(int pOffset, String pWert) {
        if (pOffset < 100) {
            ergaenzungLangString[pOffset] = pWert;
        } else {
            ergaenzungKurzString[pOffset - 100] = pWert;
        }
    }

    public int getErgaenzungKennzeichen(int pOffset) {
        return ergaenzungKennzeichen[pOffset];
    }

    public void setErgaenzungKennzeichen(int pOffset, int pWert) {
        ergaenzungKennzeichen[pOffset] = pWert;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(ergaenzungKennzeichen);
        result = prime * result + Arrays.hashCode(ergaenzungKurzString);
        result = prime * result + Arrays.hashCode(ergaenzungLangString);
        result = prime * result + Objects.hash(aktienregisterIdent, db_version, mandant, satzNummer);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EclAktienregisterErgaenzung other = (EclAktienregisterErgaenzung) obj;
        return aktienregisterIdent == other.aktienregisterIdent && db_version == other.db_version
                && Arrays.equals(ergaenzungKennzeichen, other.ergaenzungKennzeichen)
                && Arrays.equals(ergaenzungKurzString, other.ergaenzungKurzString)
                && Arrays.equals(ergaenzungLangString, other.ergaenzungLangString) && mandant == other.mandant
                && satzNummer == other.satzNummer;
    }

    @Override
    public String toString() {
        return "EclAktienregisterErgaenzung [mandant=" + mandant + ", aktienregisterIdent=" + aktienregisterIdent
                + ", satzNummer=" + satzNummer + ", db_version=" + db_version + ", ergaenzungLangString="
                + Arrays.toString(ergaenzungLangString) + ", ergaenzungKurzString="
                + Arrays.toString(ergaenzungKurzString) + ", ergaenzungKennzeichen="
                + Arrays.toString(ergaenzungKennzeichen) + "]";
    }

}
