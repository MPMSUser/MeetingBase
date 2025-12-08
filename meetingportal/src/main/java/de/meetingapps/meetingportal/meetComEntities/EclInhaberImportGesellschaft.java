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

@XmlRootElement(name = "Gesellschaft")
@XmlType(propOrder = { "kurzname", "gesName", "strasse", "plz", "ort", "landKzf", "abteilung", "beaName", "beaTelefon",
        "beaEmail1", "beaEmail2", "grundkaptial", "InhaberImportVersion", "waehrungKzf", "internetAdr", "hstNr", "hstName",
        "hstOrt", "hstLangbezeichnung", "eclInhaberImportHvParamerter" })
public class EclInhaberImportGesellschaft {

    private String kurzname;

    private String gesName;

    private String strasse;

    private String plz;

    private String ort;

    private String landKzf;

    private String abteilung;

    private String beaName;

    private String beaTelefon;

    private String beaEmail1;

    private String beaEmail2;

    private int grundkaptial;

    private String InhaberImportVersion;

    private String waehrungKzf;

    private String internetAdr;

    private int hstNr;

    private String hstName;

    private String hstOrt;

    private String hstLangbezeichnung;

    private EclInhaberImportHvParameter eclInhaberImportHvParamerter;

    public EclInhaberImportGesellschaft() {

    }

    public EclInhaberImportGesellschaft(String kurzname, String gesName, String strasse, String plz, String ort, String landKzf,
            String abteilung, String beaName, String beaTelefon, String beaEmail1, String beaEmail2, int grundkaptial,
            String InhaberImportVersion, String waehrungKzf, String internetAdr, int hstNr, String hstName, String hstOrt,
            String hstLangbezeichnung, EclInhaberImportHvParameter eclInhaberImportHvParamerter) {
        super();
        this.kurzname = kurzname;
        this.gesName = gesName;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.landKzf = landKzf;
        this.abteilung = abteilung;
        this.beaName = beaName;
        this.beaTelefon = beaTelefon;
        this.beaEmail1 = beaEmail1;
        this.beaEmail2 = beaEmail2;
        this.grundkaptial = grundkaptial;
        this.InhaberImportVersion = InhaberImportVersion;
        this.waehrungKzf = waehrungKzf;
        this.internetAdr = internetAdr;
        this.hstNr = hstNr;
        this.hstName = hstName;
        this.hstOrt = hstOrt;
        this.hstLangbezeichnung = hstLangbezeichnung;
        this.eclInhaberImportHvParamerter = eclInhaberImportHvParamerter;
    }
    
    @XmlElement(name = "Kurzname")
    public String getKurzname() {
        return kurzname;
    }

    public void setKurzname(String kurzname) {
        this.kurzname = kurzname;
    }

    @XmlElement(name = "GesName")
    public String getGesName() {
        return gesName;
    }

    public void setGesName(String gesName) {
        this.gesName = gesName;
    }

    @XmlElement(name = "Strasse")
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    @XmlElement(name = "PLZ")
    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    @XmlElement(name = "Ort")
    public String getOrt() {
        return ort;
    }

    
    public void setOrt(String ort) {
        this.ort = ort;
    }

    @XmlElement(name = "LandKzf")
    public String getLandKzf() {
        return landKzf;
    }

    public void setLandKzf(String landKzf) {
        this.landKzf = landKzf;
    }

    @XmlElement(name = "Abteilung")
    public String getAbteilung() {
        return abteilung;
    }

    public void setAbteilung(String abteilung) {
        this.abteilung = abteilung;
    }

    @XmlElement(name = "BeaName")
    public String getBeaName() {
        return beaName;
    }

    public void setBeaName(String beaName) {
        this.beaName = beaName;
    }

    @XmlElement(name = "BeaTelefon")
    public String getBeaTelefon() {
        return beaTelefon;
    }

    public void setBeaTelefon(String beaTelefon) {
        this.beaTelefon = beaTelefon;
    }

    @XmlElement(name = "BeaEmail1")
    public String getBeaEmail1() {
        return beaEmail1;
    }

    public void setBeaEmail1(String beaEmail1) {
        this.beaEmail1 = beaEmail1;
    }

    @XmlElement(name = "BeaEmail2")
    public String getBeaEmail2() {
        return beaEmail2;
    }

    public void setBeaEmail2(String beaEmail2) {
        this.beaEmail2 = beaEmail2;
    }

    @XmlElement(name = "Grundkapital")
    public int getGrundkaptial() {
        return grundkaptial;
    }

    public void setGrundkaptial(int grundkaptial) {
        this.grundkaptial = grundkaptial;
    }

    @XmlElement(name = "InhaberImportVersion")
    public String getInhaberImportVersion() {
        return InhaberImportVersion;
    }

    public void setInhaberImportVersion(String InhaberImportVersion) {
        this.InhaberImportVersion = InhaberImportVersion;
    }

    @XmlElement(name = "WaehrungKzf")
    public String getWaehrungKzf() {
        return waehrungKzf;
    }

    public void setWaehrungKzf(String waehrungKzf) {
        this.waehrungKzf = waehrungKzf;
    }

    @XmlElement(name = "InternetAdr")
    public String getInternetAdr() {
        return internetAdr;
    }

    public void setInternetAdr(String internetAdr) {
        this.internetAdr = internetAdr;
    }

    @XmlElement(name = "HstNr")
    public int getHstNr() {
        return hstNr;
    }

    public void setHstNr(int hstNr) {
        this.hstNr = hstNr;
    }
    
    @XmlElement(name = "HstName")
    public String getHstName() {
        return hstName;
    }

    public void setHstName(String hstName) {
        this.hstName = hstName;
    }

    @XmlElement(name = "HstOrt")
    public String getHstOrt() {
        return hstOrt;
    }

    public void setHstOrt(String hstOrt) {
        this.hstOrt = hstOrt;
    }

    @XmlElement(name = "HstLangbezeichnung")
    public String getHstLangbezeichnung() {
        return hstLangbezeichnung;
    }

    public void setHstLangbezeichnung(String hstLangbezeichnung) {
        this.hstLangbezeichnung = hstLangbezeichnung;
    }

    @XmlElement(name = "HVParameter")
    public EclInhaberImportHvParameter getEclInhaberImportHvParamerter() {
        return eclInhaberImportHvParamerter;
    }

    public void setEclInhaberImportHvParamerter(EclInhaberImportHvParameter eclInhaberImportHvParamerter) {
        this.eclInhaberImportHvParamerter = eclInhaberImportHvParamerter;
    }

    @Override
    public String toString() {
        return "EclInhaberImportGesellschaft [kurzname=" + kurzname + ", gesName=" + gesName + ", strasse=" + strasse + ", plz="
                + plz + ", ort=" + ort + ", landKzf=" + landKzf + ", abteilung=" + abteilung + ", beaName=" + beaName
                + ", beaTelefon=" + beaTelefon + ", beaEmail1=" + beaEmail1 + ", beaEmail2=" + beaEmail2
                + ", grundkaptial=" + grundkaptial + ", InhaberImportVersion=" + InhaberImportVersion + ", waehrungKzf=" + waehrungKzf
                + ", internetAdr=" + internetAdr + ", hstNr=" + hstNr + ", hstName=" + hstName + ", hstOrt=" + hstOrt
                + ", hstLangbezeichnung=" + hstLangbezeichnung + ", eclInhaberImportHvParamerter=" + eclInhaberImportHvParamerter + "]";
    }

}
