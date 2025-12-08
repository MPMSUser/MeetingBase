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

import de.meetingapps.meetingportal.meetComEclM.EclFragenM;
import de.meetingapps.meetingportal.meetComEclM.EclVirtuellerTeilnehmerM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerFragenSession implements Serializable {
    private static final long serialVersionUID = -1345734918547603747L;

    /**1=Fragen Button
     * 2=Person auswählen
     * 3=Person bestätigen
     * 4=Frage eingeben
     * 5=Frage nicht möglich, da sonstige Person ausgewählt
     * 6=Keine weiteren Fragen zulässig
     */
    private int anzuzeigenderBereich = 0;

    /**Nach einmaligem Fragenstellen bleibt die Person erhalten => keine
     * erneute Abfrage bei weiteren Fragen
     */
    private boolean personBereitsAusgewaehlt = false;
    private String aktuellerFragensteller = "";

    private List<EclVirtuellerTeilnehmerM> virtuelleTeilnehmerListM = null;
    private int ausgewaehlterTeilnehmerIdent = -1;

    private String frageZuTop = "";
    private String frageText = "";
    private int maxZeichen = 999999;
    private int maxFragen = 999999;

    private int anzahlFragenGestellt = 0;

    private List<EclFragenM> fragenGestelltListe = null;

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

    public String getAktuellerFragensteller() {
        return aktuellerFragensteller;
    }

    public void setAktuellerFragensteller(String aktuellerFragensteller) {
        this.aktuellerFragensteller = aktuellerFragensteller;
    }

    public String getFrageZuTop() {
        return frageZuTop;
    }

    public void setFrageZuTop(String frageZuTop) {
        this.frageZuTop = frageZuTop;
    }

    public String getFrageText() {
        return frageText;
    }

    public void setFrageText(String frageText) {
        this.frageText = frageText;
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

    public int getMaxFragen() {
        return maxFragen;
    }

    public void setMaxFragen(int maxFragen) {
        this.maxFragen = maxFragen;
    }

    public int getAnzahlFragenGestellt() {
        return anzahlFragenGestellt;
    }

    public void setAnzahlFragenGestellt(int anzahlFragenGestellt) {
        this.anzahlFragenGestellt = anzahlFragenGestellt;
    }

    public List<EclFragenM> getFragenGestelltListe() {
        return fragenGestelltListe;
    }

    public void setFragenGestelltListe(List<EclFragenM> fragenGestelltListe) {
        this.fragenGestelltListe = fragenGestelltListe;
    }

}
