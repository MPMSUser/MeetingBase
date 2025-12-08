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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclAbstimmungSplitM implements Serializable {
    private static final long serialVersionUID = -1783898382339870569L;

    private int ident = 0;
    private String nummer = "";
    private String nummerEN = "";
    private String index = "";
    private String indexEN = "";
    private String anzeigeBezeichnungKurz = "";
    private String anzeigeBezeichnungKurzEN = "";
    private String anzeigeBezeichnungLang = "";
    private String anzeigeBezeichnungLangEN = "";
    private int aktivAbstimmungInPortal = 0;

    private int externJa = 0;
    private int externNein = 0;
    private int externEnthaltung = 0;
    private int externUngueltig = 0;
    private int externNichtTeilnahme = 0;
    private int externSonstiges1 = 0;
    private int externSonstiges2 = 0;
    private int externSonstiges3 = 0;
    private int externLoeschen = 0;
    private int externFrei = 0;
    private String externFreiText = "";
    private boolean ueberschrift = false;
    private int identWeisungssatz = 0;
    private int gegenantrag = 0;
    private int ergaenzungsantrag = 0;

    /**Verbindung zu Split-Stimmen-Felder*/
    private String stimmenJa = "";
    private String stimmenNein = "";
    private String stimmenEnthaltung = "";
    private String stimmenUngueltig = "";
    private String stimmenNichtTeilnahme = "";
    private String stimmenFrei = "";
    private String stimmenStimmausschluss = "";

    /**Quersumme für Sammelkartencheck. Steht nur zur Verfügung in DControllerSammelkarteDetails*/
    private String stimmenCheckSumme = "";

    /**Abstimmungsvorschlag - für KIAV aktuell */
    private String abstimmungsvorschlag = "";

    /**Abstimmungsvorschlag der Gesellschaft*/
    private String abstimmungsvorschlagGesellschaft = "";

    /**Für Gegenanträge*/
    private boolean markiert = false;

    public void copyFrom(EclAbstimmung pAbstimmung) {
        ident = pAbstimmung.ident;
        nummer = pAbstimmung.nummer;
        nummerEN = pAbstimmung.nummerEN;
        index = pAbstimmung.nummerindex;
        indexEN = pAbstimmung.nummerindexEN;
        anzeigeBezeichnungKurz = pAbstimmung.anzeigeBezeichnungKurz;
        anzeigeBezeichnungKurzEN = pAbstimmung.anzeigeBezeichnungKurzEN;
        anzeigeBezeichnungLang = pAbstimmung.anzeigeBezeichnungLang;
        anzeigeBezeichnungLangEN = pAbstimmung.anzeigeBezeichnungLangEN;
        externJa = pAbstimmung.externJa;
        externNein = pAbstimmung.externNein;
        externEnthaltung = pAbstimmung.externEnthaltung;
        externUngueltig = pAbstimmung.externUngueltig;
        externNichtTeilnahme = pAbstimmung.externNichtTeilnahme;
        externSonstiges1 = pAbstimmung.externSonstiges1;
        externSonstiges2 = pAbstimmung.externSonstiges2;
        externSonstiges3 = pAbstimmung.externSonstiges3;
        externLoeschen = pAbstimmung.externLoeschen;
        externFrei = pAbstimmung.externFrei;
        externFreiText = pAbstimmung.externFreiText;
        if (pAbstimmung.identWeisungssatz == -1) {
            ueberschrift = true;
        } else {
            ueberschrift = false;
        }
        identWeisungssatz = pAbstimmung.identWeisungssatz;
        gegenantrag = pAbstimmung.gegenantrag;
        ergaenzungsantrag = pAbstimmung.ergaenzungsantrag;
        stimmenJa = "";
        stimmenNein = "";
        stimmenEnthaltung = "";
        stimmenUngueltig = "";
        stimmenNichtTeilnahme = "";
        stimmenFrei = "";
        stimmenStimmausschluss = "";
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getAnzeigeBezeichnungKurz() {
        return anzeigeBezeichnungKurz;
    }

    public void setAnzeigeBezeichnungKurz(String anzeigeBezeichnungKurz) {
        this.anzeigeBezeichnungKurz = anzeigeBezeichnungKurz;
    }

    public String getAnzeigeBezeichnungLang() {
        return anzeigeBezeichnungLang;
    }

    public void setAnzeigeBezeichnungLang(String anzeigeBezeichnungLang) {
        this.anzeigeBezeichnungLang = anzeigeBezeichnungLang;
    }

    public int getExternJa() {
        return externJa;
    }

    public void setExternJa(int externJa) {
        this.externJa = externJa;
    }

    public int getExternNein() {
        return externNein;
    }

    public void setExternNein(int externNein) {
        this.externNein = externNein;
    }

    public int getExternEnthaltung() {
        return externEnthaltung;
    }

    public void setExternEnthaltung(int externEnthaltung) {
        this.externEnthaltung = externEnthaltung;
    }

    public int getExternUngueltig() {
        return externUngueltig;
    }

    public void setExternUngueltig(int externUngueltig) {
        this.externUngueltig = externUngueltig;
    }

    public int getExternNichtTeilnahme() {
        return externNichtTeilnahme;
    }

    public void setExternNichtTeilnahme(int externNichtTeilnahme) {
        this.externNichtTeilnahme = externNichtTeilnahme;
    }

    public int getExternSonstiges1() {
        return externSonstiges1;
    }

    public void setExternSonstiges1(int externSonstiges1) {
        this.externSonstiges1 = externSonstiges1;
    }

    public int getExternSonstiges2() {
        return externSonstiges2;
    }

    public void setExternSonstiges2(int externSonstiges2) {
        this.externSonstiges2 = externSonstiges2;
    }

    public int getExternSonstiges3() {
        return externSonstiges3;
    }

    public void setExternSonstiges3(int externSonstiges3) {
        this.externSonstiges3 = externSonstiges3;
    }

    public int getExternLoeschen() {
        return externLoeschen;
    }

    public void setExternLoeschen(int externLoeschen) {
        this.externLoeschen = externLoeschen;
    }

    public int getExternFrei() {
        return externFrei;
    }

    public void setExternFrei(int externFrei) {
        this.externFrei = externFrei;
    }

    public String getExternFreiText() {
        return externFreiText;
    }

    public void setExternFreiText(String externFreiText) {
        this.externFreiText = externFreiText;
    }

    public boolean isUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(boolean ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public int getIdentWeisungssatz() {
        return identWeisungssatz;
    }

    public void setIdentWeisungssatz(int identWeisungssatz) {
        this.identWeisungssatz = identWeisungssatz;
    }

    public int getGegenantrag() {
        return gegenantrag;
    }

    public void setGegenantrag(int gegenantrag) {
        this.gegenantrag = gegenantrag;
    }

    public int getErgaenzungsantrag() {
        return ergaenzungsantrag;
    }

    public void setErgaenzungsantrag(int ergaenzungsantrag) {
        this.ergaenzungsantrag = ergaenzungsantrag;
    }

    public boolean isMarkiert() {
        return markiert;
    }

    public void setMarkiert(boolean markiert) {
        this.markiert = markiert;
    }

    public String getAbstimmungsvorschlag() {
        return abstimmungsvorschlag;
    }

    public void setAbstimmungsvorschlag(String abstimmungsvorschlag) {
        this.abstimmungsvorschlag = abstimmungsvorschlag;
    }

    public int getAktivAbstimmungInPortal() {
        return aktivAbstimmungInPortal;
    }

    public void setAktivAbstimmungInPortal(int aktivAbstimmungInPortal) {
        this.aktivAbstimmungInPortal = aktivAbstimmungInPortal;
    }

    public String getAbstimmungsvorschlagGesellschaft() {
        return abstimmungsvorschlagGesellschaft;
    }

    public void setAbstimmungsvorschlagGesellschaft(String abstimmungsvorschlagGesellschaft) {
        this.abstimmungsvorschlagGesellschaft = abstimmungsvorschlagGesellschaft;
    }

    public String getStimmenJa() {
        return stimmenJa;
    }

    public void setStimmenJa(String stimmenJa) {
        this.stimmenJa = stimmenJa;
    }

    public String getStimmenNein() {
        return stimmenNein;
    }

    public void setStimmenNein(String stimmenNein) {
        this.stimmenNein = stimmenNein;
    }

    public String getStimmenEnthaltung() {
        return stimmenEnthaltung;
    }

    public void setStimmenEnthaltung(String stimmenEnthaltung) {
        this.stimmenEnthaltung = stimmenEnthaltung;
    }

    public String getStimmenUngueltig() {
        return stimmenUngueltig;
    }

    public void setStimmenUngueltig(String stimmenUngueltig) {
        this.stimmenUngueltig = stimmenUngueltig;
    }

    public String getStimmenNichtTeilnahme() {
        return stimmenNichtTeilnahme;
    }

    public void setStimmenNichtTeilnahme(String stimmenNichtTeilnahme) {
        this.stimmenNichtTeilnahme = stimmenNichtTeilnahme;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getNummerEN() {
        return nummerEN;
    }

    public void setNummerEN(String nummerEN) {
        this.nummerEN = nummerEN;
    }

    public String getAnzeigeBezeichnungKurzEN() {
        return anzeigeBezeichnungKurzEN;
    }

    public void setAnzeigeBezeichnungKurzEN(String anzeigeBezeichnungKurzEN) {
        this.anzeigeBezeichnungKurzEN = anzeigeBezeichnungKurzEN;
    }

    public String getAnzeigeBezeichnungLangEN() {
        return anzeigeBezeichnungLangEN;
    }

    public void setAnzeigeBezeichnungLangEN(String anzeigeBezeichnungLangEN) {
        this.anzeigeBezeichnungLangEN = anzeigeBezeichnungLangEN;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndexEN() {
        return indexEN;
    }

    public void setIndexEN(String indexEN) {
        this.indexEN = indexEN;
    }

    public String getStimmenCheckSumme() {
        return stimmenCheckSumme;
    }

    public void setStimmenCheckSumme(String stimmenCheckSumme) {
        this.stimmenCheckSumme = stimmenCheckSumme;
    }

    public String getStimmenFrei() {
        return stimmenFrei;
    }

    public void setStimmenFrei(String stimmenFrei) {
        this.stimmenFrei = stimmenFrei;
    }

    public String getStimmenStimmausschluss() {
        return stimmenStimmausschluss;
    }

    public void setStimmenStimmausschluss(String stimmenStimmausschluss) {
        this.stimmenStimmausschluss = stimmenStimmausschluss;
    }

}
