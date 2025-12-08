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

public class EclBestWorkflow implements Serializable {
    private static final long serialVersionUID = 8598078031427179361L;

    /** Mandantennummer */
    public int mandant = 0;

    /**
     * eindeutiger Key. Mit Autoincrement
     */
    public long ident = 0;

    /**orig oder kopie für original oder Kopie
     * LEN=10*/
    public String subverzeichnis = "";

    /**LEN=100
     * Kann leer sein => dann Dummy-Satz für EclVorlaeufigeVollmachtEingabe*/
    public String dateinameBestaetigung = "";

    /**Zeit, zu der der Import der Datei erfolgte
     * LEN=19*/
    public String dateinameImportAm = "";

    /**LEN=20*/
    public String zuAktionaersnummer = "";

    /**Vollmacht wurde im Original (1) oder in Kopie (2) vorgelegt. Wird (theoretisch)
     * aus subverzeichnis geholt, kann aber überschrieben werden
     * 
     *(3)=Dummysatz für EclVorlaeufigeVollmachtEingabe
     */
    public int origOderKopie = 0;

    /**Verweis auf Vorläufige Vollmacht, die durch diese Vollmacht erzeugt wurde*/
    public int vorlVollmachtIdent = 0;

    /**Ist !=0, wenn der Satz gerade in Bearbeitung ist. Achtung, muß dann irgendwie täglich zurückgesetzt
     * werden. ist derzeit nicht die UserNr, sondern eine laufende Id (dbBasis.getInterneIdentBestWorkflowBasis().
     */
    public int inBearbeitungDurchUserNr = 0;

    /**
     * Achtung - muß immer in allen Sätzen mit madant/dateinameBestaetigung/zuAktionaersnummer upgedatet werden!
     * 0= noch vollkommen unbearbeitet
     * 1= geprüft und akzeptiert	Bei Bestandsabgleich: Veränderungen registriert.
     * 2= geprüft und abgelehnt		Bei Bestandsabgleich: keine Veränderungen
     * 3= geprüft und wiedervorlage	Bei Bestandsabgleich: dito
     * 4= Nicht als Nachweis bearbeiten
     * (intern: 99 reserviert) 
     */
    public int pruefstatusErgebnis = 0;

    /**Wie pruefstatusErgebnis, aber nicht als "Ergebnis aller Prüfvorgänge", sondern
     * was dieser User gerade konkret in diesem Vorgang mit gespeichert hat
     */
    public int pruefstatusVorgang = 0;

    /**Vorsorglich mal eingebaut*/
    public int wurdeQualitaetsgesichert = 0;

    /**LEN=400*/
    public String kommentar = "";

    /**LEN=19*/
    public String bearbeitungsZeit = "";

    public int bearbeitungsUser = 0;
    /**LEN=40*/
    public String bearbeitungsUserName = "";

    /*****************Standard getter und setter******************/
    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public long getIdent() {
        return ident;
    }

    public void setIdent(long ident) {
        this.ident = ident;
    }

    public String getSubverzeichnis() {
        return subverzeichnis;
    }

    public void setSubverzeichnis(String subverzeichnis) {
        this.subverzeichnis = subverzeichnis;
    }

    public String getDateinameBestaetigung() {
        return dateinameBestaetigung;
    }

    public void setDateinameBestaetigung(String dateinameBestaetigung) {
        this.dateinameBestaetigung = dateinameBestaetigung;
    }

    public String getDateinameImportAm() {
        return dateinameImportAm;
    }

    public void setDateinameImportAm(String dateinameImportAm) {
        this.dateinameImportAm = dateinameImportAm;
    }

    public String getZuAktionaersnummer() {
        return zuAktionaersnummer;
    }

    public void setZuAktionaersnummer(String zuAktionaersnummer) {
        this.zuAktionaersnummer = zuAktionaersnummer;
    }

    public int getOrigOderKopie() {
        return origOderKopie;
    }

    public void setOrigOderKopie(int origOderKopie) {
        this.origOderKopie = origOderKopie;
    }

    public int getInBearbeitungDurchUserNr() {
        return inBearbeitungDurchUserNr;
    }

    public void setInBearbeitungDurchUserNr(int inBearbeitungDurchUserNr) {
        this.inBearbeitungDurchUserNr = inBearbeitungDurchUserNr;
    }

    public int getPruefstatusErgebnis() {
        return pruefstatusErgebnis;
    }

    public void setPruefstatusErgebnis(int pruefstatusErgebnis) {
        this.pruefstatusErgebnis = pruefstatusErgebnis;
    }

    public int getPruefstatusVorgang() {
        return pruefstatusVorgang;
    }

    public void setPruefstatusVorgang(int pruefstatusVorgang) {
        this.pruefstatusVorgang = pruefstatusVorgang;
    }

    public int getWurdeQualitaetsgesichert() {
        return wurdeQualitaetsgesichert;
    }

    public void setWurdeQualitaetsgesichert(int wurdeQualitaetsgesichert) {
        this.wurdeQualitaetsgesichert = wurdeQualitaetsgesichert;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getBearbeitungsZeit() {
        return bearbeitungsZeit;
    }

    public void setBearbeitungsZeit(String bearbeitungsZeit) {
        this.bearbeitungsZeit = bearbeitungsZeit;
    }

    public int getBearbeitungsUser() {
        return bearbeitungsUser;
    }

    public void setBearbeitungsUser(int bearbeitungsUser) {
        this.bearbeitungsUser = bearbeitungsUser;
    }

    public String getBearbeitungsUserName() {
        return bearbeitungsUserName;
    }

    public void setBearbeitungsUserName(String bearbeitungsUserName) {
        this.bearbeitungsUserName = bearbeitungsUserName;
    }
}
