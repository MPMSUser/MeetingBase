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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclMitteilungenM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerMitteilungenSession implements Serializable {
    private static final long serialVersionUID = -1345734918547603747L;

    /**1=Mitteilungen Button
     * 2=Person auswählen
     * 3=Person bestätigen
     * 4=Frage eingeben
     * 5=Frage nicht möglich, da sonstige Person ausgewählt
     * 6=Keine weiteren Mitteilungen zulässig
     */
    private int anzuzeigenderBereich = 0;

    /**Nach einmaligem Mitteilungenstellen bleibt die Person erhalten => keine
     * erneute Abfrage bei weiteren Mitteilungen
     */
    private boolean personBereitsAusgewaehlt = false;
    private String aktuellerMitteilungensteller = "";

    private List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = null;
    private int ausgewaehlterTeilnehmerIdent = -1;

    private String mitteilungText = "";
    private int maxZeichen = 999999;
    private int maxMitteilungen = 999999;

    private int anzahlMitteilungenGestellt = 0;

    private List<EclMitteilungenM> mitteilungenGestelltListe = null;

    /*************Standard-Getter und Setter*************************/

    public int getAnzuzeigenderBereich() {
        return anzuzeigenderBereich;
    }

    public void setAnzuzeigenderBereich(int anzuzeigenderBereich) {
        this.anzuzeigenderBereich = anzuzeigenderBereich;
    }

    public boolean isPersonBereitsAusgewaehlt() {
        return personBereitsAusgewaehlt;
    }

    public void setPersonBereitsAusgewaehlt(boolean personBereitsAusgewaehlt) {
        this.personBereitsAusgewaehlt = personBereitsAusgewaehlt;
    }

    public String getAktuellerMitteilungensteller() {
        return aktuellerMitteilungensteller;
    }

    public void setAktuellerMitteilungensteller(String aktuellerMitteilungensteller) {
        this.aktuellerMitteilungensteller = aktuellerMitteilungensteller;
    }

    public List<EclVirtuellerTeilnehmerM> getVirtuelleTeilnehmerListM() {
        return virtuelleTeilnehmerListM;
    }

    public void setVirtuelleTeilnehmerListM(List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM) {
        this.virtuelleTeilnehmerListM = virtuelleTeilnehmerListM;
    }

    public int getAusgewaehlterTeilnehmerIdent() {
        return ausgewaehlterTeilnehmerIdent;
    }

    public void setAusgewaehlterTeilnehmerIdent(int ausgewaehlterTeilnehmerIdent) {
        this.ausgewaehlterTeilnehmerIdent = ausgewaehlterTeilnehmerIdent;
    }

    public int getMaxZeichen() {
        return maxZeichen;
    }

    public void setMaxZeichen(int maxZeichen) {
        this.maxZeichen = maxZeichen;
    }

    public int getMaxMitteilungen() {
        return maxMitteilungen;
    }

    public void setMaxMitteilungen(int maxMitteilungen) {
        this.maxMitteilungen = maxMitteilungen;
    }

    public int getAnzahlMitteilungenGestellt() {
        return anzahlMitteilungenGestellt;
    }

    public void setAnzahlMitteilungenGestellt(int anzahlMitteilungenGestellt) {
        this.anzahlMitteilungenGestellt = anzahlMitteilungenGestellt;
    }

    public List<EclMitteilungenM> getMitteilungenGestelltListe() {
        return mitteilungenGestelltListe;
    }

    public void setMitteilungenGestelltListe(List<EclMitteilungenM> mitteilungenGestelltListe) {
        this.mitteilungenGestelltListe = mitteilungenGestelltListe;
    }

    public String getMitteilungText() {
        return mitteilungText;
    }

    public void setMitteilungText(String mitteilungText) {
        this.mitteilungText = mitteilungText;
    }

}
