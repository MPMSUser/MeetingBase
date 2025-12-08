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

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ISINDaten")
@XmlType(propOrder = { "kurzname", "hvDatum", "hstNr", "isin", "wkn", "nennwert", "aktienartKzf", "aktiengattung",
        "aktienKzf", "prozentNotiz", "grundkapital", "bezeichnung", "eclInhaberImportNummernkreise" })
public class EclInhaberImportIsinDaten {

    private String kurzname;

    private String hvDatum;

    private int hstNr;

    private String isin;

    private String wkn;

    private String nennwert;

    private String aktienartKzf;

    private String aktiengattung;

    private String aktienKzf;

    private String prozentNotiz;

    private int grundkapital;

    private String bezeichnung;

    private List<EclInhaberImportNummernkreise> eclInhaberImportNummernkreise;

    public EclInhaberImportIsinDaten() {

    }

    public EclInhaberImportIsinDaten(String kurzname, String hvDatum, int hstNr, String isin, String wkn, String nennwert,
            String aktienartKzf, String aktiengattung, String aktienKzf, String prozentNotiz, int grundkapital,
            String bezeichnung, List<EclInhaberImportNummernkreise> eclInhaberImportNummernkreise) {
        super();
        this.kurzname = kurzname;
        this.hvDatum = hvDatum;
        this.hstNr = hstNr;
        this.isin = isin;
        this.wkn = wkn;
        this.nennwert = nennwert;
        this.aktienartKzf = aktienartKzf;
        this.aktiengattung = aktiengattung;
        this.aktienKzf = aktienKzf;
        this.prozentNotiz = prozentNotiz;
        this.grundkapital = grundkapital;
        this.bezeichnung = bezeichnung;
        this.eclInhaberImportNummernkreise = eclInhaberImportNummernkreise;
    }

    @XmlElement(name = "Kurzname")
    public String getKurzname() {
        return kurzname;
    }

    public void setKurzname(String kurzname) {
        this.kurzname = kurzname;
    }

    @XmlElement(name = "HVDatum")
    public String getHvDatum() {
        return hvDatum;
    }

    public void setHvDatum(String hvDatum) {
        this.hvDatum = hvDatum;
    }

    @XmlElement(name = "HstNr")
    public int getHstNr() {
        return hstNr;
    }

    public void setHstNr(int hstNr) {
        this.hstNr = hstNr;
    }

    @XmlElement(name = "ISIN")
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    @XmlElement(name = "WKN")
    public String getWkn() {
        return wkn;
    }

    public void setWkn(String wkn) {
        this.wkn = wkn;
    }

    @XmlElement(name = "Nennwert")
    public String getNennwert() {
        return nennwert;
    }

    public void setNennwert(String nennwert) {
        this.nennwert = nennwert;
    }

    @XmlElement(name = "AktienartKzf")
    public String getAktienartKzf() {
        return aktienartKzf;
    }

    public void setAktienartKzf(String aktienartKzf) {
        this.aktienartKzf = aktienartKzf;
    }

    @XmlElement(name = "AktienGattung")
    public String getAktiengattung() {
        return aktiengattung;
    }

    public void setAktiengattung(String aktiengattung) {
        this.aktiengattung = aktiengattung;
    }

    @XmlElement(name = "AktienKzf")
    public String getAktienKzf() {
        return aktienKzf;
    }

    public void setAktienKzf(String aktienKzf) {
        this.aktienKzf = aktienKzf;
    }

    @XmlElement(name = "ProzentNotiz")
    public String getProzentNotiz() {
        return prozentNotiz;
    }

    public void setProzentNotiz(String prozentNotiz) {
        this.prozentNotiz = prozentNotiz;
    }

    @XmlElement(name = "Grundkapital")
    public int getGrundkapital() {
        return grundkapital;
    }

    public void setGrundkapital(int grundkapital) {
        this.grundkapital = grundkapital;
    }

    @XmlElement(name = "Bezeichnung")
    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @XmlElement(name = "Nummernkreise")
    public List<EclInhaberImportNummernkreise> getEclInhaberImportNummernkreise() {
        return eclInhaberImportNummernkreise;
    }

    public void setEclInhaberImportNummernkreise(List<EclInhaberImportNummernkreise> eclInhaberImportNummernkreise) {
        this.eclInhaberImportNummernkreise = eclInhaberImportNummernkreise;
    }

    @Override
    public String toString() {
        return "EclInhaberImportIsinDaten [kurzname=" + kurzname + ", hvDatum=" + hvDatum + ", hstNr=" + hstNr + ", isin="
                + isin + ", wkn=" + wkn + ", nennwert=" + nennwert + ", aktienartKzf=" + aktienartKzf
                + ", aktiengattung=" + aktiengattung + ", prozentNotiz=" + prozentNotiz + ", grundkapital="
                + grundkapital + ", bezeichnung=" + bezeichnung + ", eclInhaberImportNummernkreise=" + eclInhaberImportNummernkreise
                + "]";
    }

}
