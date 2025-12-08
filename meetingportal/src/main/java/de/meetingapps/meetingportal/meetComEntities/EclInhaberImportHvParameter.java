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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "HVParameter")
@XmlType(propOrder = { "kurzname", "hvDatum", "hstNr", "hvArt", "hvOrt", "pruefKzf", "namensKzf", "aendernKzf",
        "laengeName", "defWei", "internet", "bemerkung", "stimmrecht", "vollmachtKzf", "hvAktiv", "zeichensatz",
        "beleglosKzf", "ekLaenge", "verschluesselung", "schlussmeldung", "ekNrAktiv", "eclInhaberImportEkParameter" })
public class EclInhaberImportHvParameter {

    private String kurzname;

    private String hvDatum;

    private int hstNr;

    private String hvArt;

    private String hvOrt;

    private String pruefKzf;

    private String namensKzf;

    private String aendernKzf;

    private int laengeName;

    private String defWei;

    private String internet;

    private String bemerkung;

    private double stimmrecht;

    private String vollmachtKzf;

    private String hvAktiv;

    private int zeichensatz;

    private String beleglosKzf;

    private int ekLaenge;

    private int verschluesselung;

    private String schlussmeldung;

    private int ekNrAktiv;

    private EclInhaberImportEkParameter eclInhaberImportEkParameter;

    public EclInhaberImportHvParameter() {

    }

    public EclInhaberImportHvParameter(String kurzname, String hvDatum, int hstNr, String hvArt, String hvOrt, String pruefKzf,
            String namensKzf, String aendernKzf, int laengeName, String defWei, String internet, String bemerkung,
            double stimmrecht, String vollmachtKzf, String hvAktiv, int zeichensatz, String beleglosKzf, int ekLaenge,
            int verschluesselung, String schlussmeldung, int ekNrAktiv, EclInhaberImportEkParameter eclInhaberImportEkParameter) {
        super();
        this.kurzname = kurzname;
        this.hvDatum = hvDatum;
        this.hstNr = hstNr;
        this.hvArt = hvArt;
        this.hvOrt = hvOrt;
        this.pruefKzf = pruefKzf;
        this.namensKzf = namensKzf;
        this.aendernKzf = aendernKzf;
        this.laengeName = laengeName;
        this.defWei = defWei;
        this.internet = internet;
        this.bemerkung = bemerkung;
        this.stimmrecht = stimmrecht;
        this.vollmachtKzf = vollmachtKzf;
        this.hvAktiv = hvAktiv;
        this.zeichensatz = zeichensatz;
        this.beleglosKzf = beleglosKzf;
        this.ekLaenge = ekLaenge;
        this.verschluesselung = verschluesselung;
        this.schlussmeldung = schlussmeldung;
        this.ekNrAktiv = ekNrAktiv;
        this.eclInhaberImportEkParameter = eclInhaberImportEkParameter;
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

    @XmlElement(name = "HVArt")
    public String getHvArt() {
        return hvArt;
    }

    public void setHvArt(String hvArt) {
        this.hvArt = hvArt;
    }

    @XmlElement(name = "HVOrt")
    public String getHvOrt() {
        return hvOrt;
    }

    public void setHvOrt(String hvOrt) {
        this.hvOrt = hvOrt;
    }

    @XmlElement(name = "PruefKzf")
    public String getPruefKzf() {
        return pruefKzf;
    }

    public void setPruefKzf(String pruefKzf) {
        this.pruefKzf = pruefKzf;
    }

    @XmlElement(name = "NamensKzf")
    public String getNamensKzf() {
        return namensKzf;
    }

    public void setNamensKzf(String namensKzf) {
        this.namensKzf = namensKzf;
    }

    @XmlElement(name = "AendernKzf")
    public String getAendernKzf() {
        return aendernKzf;
    }

    public void setAendernKzf(String aendernKzf) {
        this.aendernKzf = aendernKzf;
    }

    @XmlElement(name = "LaengeName")
    public int getLaengeName() {
        return laengeName;
    }

    public void setLaengeName(int laengeName) {
        this.laengeName = laengeName;
    }

    @XmlElement(name = "DefWei")
    public String getDefWei() {
        return defWei;
    }

    public void setDefWei(String defWei) {
        this.defWei = defWei;
    }

    @XmlElement(name = "Internet")
    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    @XmlElement(name = "Bemerkung")
    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    @XmlElement(name = "Stimmrecht")
    public double getStimmrecht() {
        return stimmrecht;
    }

    public void setStimmrecht(double stimmrecht) {
        this.stimmrecht = stimmrecht;
    }

    @XmlElement(name = "VollmachtKzf")
    public String getVollmachtKzf() {
        return vollmachtKzf;
    }

    public void setVollmachtKzf(String vollmachtKzf) {
        this.vollmachtKzf = vollmachtKzf;
    }

    @XmlElement(name = "HVAktiv")
    public String getHvAktiv() {
        return hvAktiv;
    }

    public void setHvAktiv(String hvAktiv) {
        this.hvAktiv = hvAktiv;
    }

    @XmlElement(name = "Zeichensatz")
    public int getZeichensatz() {
        return zeichensatz;
    }

    public void setZeichensatz(int zeichensatz) {
        this.zeichensatz = zeichensatz;
    }

    @XmlElement(name = "BeleglosKzf")
    public String getBeleglosKzf() {
        return beleglosKzf;
    }

    public void setBeleglosKzf(String beleglosKzf) {
        this.beleglosKzf = beleglosKzf;
    }

    @XmlElement(name = "EKLaenge")
    public int getEkLaenge() {
        return ekLaenge;
    }

    public void setEkLaenge(int ekLaenge) {
        this.ekLaenge = ekLaenge;
    }

    @XmlElement(name = "Verschluesselung")
    public int getVerschluesselung() {
        return verschluesselung;
    }

    public void setVerschluesselung(int verschluesselung) {
        this.verschluesselung = verschluesselung;
    }

    @XmlElement(name = "Schlussmeldung")
    public String getSchlussmeldung() {
        return schlussmeldung;
    }

    public void setSchlussmeldung(String schlussmeldung) {
        this.schlussmeldung = schlussmeldung;
    }

    @XmlElement(name = "EKNrAktiv")
    public int getEkNrAktiv() {
        return ekNrAktiv;
    }

    public void setEkNrAktiv(int ekNrAktiv) {
        this.ekNrAktiv = ekNrAktiv;
    }

    @XmlElement(name = "EKParameter")
    public EclInhaberImportEkParameter getEclInhaberImportEkParameter() {
        return eclInhaberImportEkParameter;
    }

    public void setEclInhaberImportEkParameter(EclInhaberImportEkParameter eclInhaberImportEkParameter) {
        this.eclInhaberImportEkParameter = eclInhaberImportEkParameter;
    }

    @Override
    public String toString() {
        return "EclInhaberImportHvParameter [kurzname=" + kurzname + ", hvDatum=" + hvDatum + ", hstNr=" + hstNr + ", hvArt="
                + hvArt + ", hvOrt=" + hvOrt + ", pruefKzf=" + pruefKzf + ", namensKzf=" + namensKzf + ", aendernKzf="
                + aendernKzf + ", laengeName=" + laengeName + ", defWei=" + defWei + ", internet=" + internet
                + ", bemerkung=" + bemerkung + ", stimmrecht=" + stimmrecht + ", vollmachtKzf=" + vollmachtKzf
                + ", hvAktiv=" + hvAktiv + ", zeichensatz=" + zeichensatz + ", beleglosKzf=" + beleglosKzf
                + ", ekLaenge=" + ekLaenge + ", verschluesselung=" + verschluesselung + ", schlussmeldung="
                + schlussmeldung + ", ekNrAktiv=" + ekNrAktiv + ", eclInhaberImportEkParameter=" + eclInhaberImportEkParameter + "]";
    }

}
