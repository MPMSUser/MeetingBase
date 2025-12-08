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

@XmlRootElement(name = "EKParameter")
@XmlType(propOrder = { "kurzname", "hvDatum", "hstNr", "landKzf", "druckName", "formKzf", "barcodeKzf", "barcodeOben",
        "barcodeLinks", "barcodeHoehe", "logoText1", "logoText2", "einlText1", "einlText2", "einlText3", "einlText4",
        "einlText5", "einlText6", "einlText7", "einlText8", "zusatzText1", "zusatzText2", "barcodeText", "jahreszahl",
        "ekNr1Txt", "ekNr2Txt", "ekNr3Txt", "versandKzf", "unterschrift", "aktienText", "aktionaersdatenKzf",
        "praesenzTxt1", "praesenzTxt2", "kontrollTxt1", "kontrollTxt2", "logoPfad", "eclInhaberImportIsinDaten" })
public class EclInhaberImportEkParameter {

    private String kurzname;

    private String hvDatum;

    private int hstNr;

    private String landKzf;

    private String druckName;

    private String formKzf;

    private String barcodeKzf;

    private int barcodeOben;

    private int barcodeLinks;

    private int barcodeHoehe;

    private String logoText1;

    private String logoText2;

    private String einlText1;

    private String einlText2;

    private String einlText3;

    private String einlText4;

    private String einlText5;

    private String einlText6;

    private String einlText7;

    private String einlText8;

    private String zusatzText1;

    private String zusatzText2;

    private String barcodeText;

    private int jahreszahl;

    private String ekNr1Txt;

    private String ekNr2Txt;

    private String ekNr3Txt;

    private String versandKzf;

    private String unterschrift;

    private String aktienText;

    private String aktionaersdatenKzf;

    private String praesenzTxt1;

    private String praesenzTxt2;

    private String kontrollTxt1;

    private String kontrollTxt2;

    private String logoPfad;

    private List<EclInhaberImportIsinDaten> eclInhaberImportIsinDaten;

    public EclInhaberImportEkParameter() {

    }

    public EclInhaberImportEkParameter(String kurzname, String hvDatum, int hstNr, String landKzf, String druckName,
            String formKzf, String barcodeKzf, int barcodeOben, int barcodeLinks, int barcodeHoehe, String logoText1,
            String logoText2, String einlText1, String einlText2, String einlText3, String einlText4, String einlText5,
            String einlText6, String einlText7, String einlText8, String zusatzText1, String zusatzText2,
            String barcodeText, int jahreszahl, String ekNr1Txt, String ekNr2Txt, String ekNr3Txt, String versandKzf,
            String unterschrift, String aktienText, String aktionaersdatenKzf, String praesenzTxt1, String praesenzTxt2,
            String kontrollTxt1, String kontrollTxt2, String logoPfad, List<EclInhaberImportIsinDaten> eclInhaberImportIsinDaten) {
        super();
        this.kurzname = kurzname;
        this.hvDatum = hvDatum;
        this.hstNr = hstNr;
        this.landKzf = landKzf;
        this.druckName = druckName;
        this.formKzf = formKzf;
        this.barcodeKzf = barcodeKzf;
        this.barcodeOben = barcodeOben;
        this.barcodeLinks = barcodeLinks;
        this.barcodeHoehe = barcodeHoehe;
        this.logoText1 = logoText1;
        this.logoText2 = logoText2;
        this.einlText1 = einlText1;
        this.einlText2 = einlText2;
        this.einlText3 = einlText3;
        this.einlText4 = einlText4;
        this.einlText5 = einlText5;
        this.einlText6 = einlText6;
        this.einlText7 = einlText7;
        this.einlText8 = einlText8;
        this.zusatzText1 = zusatzText1;
        this.zusatzText2 = zusatzText2;
        this.barcodeText = barcodeText;
        this.jahreszahl = jahreszahl;
        this.ekNr1Txt = ekNr1Txt;
        this.ekNr2Txt = ekNr2Txt;
        this.ekNr3Txt = ekNr3Txt;
        this.versandKzf = versandKzf;
        this.unterschrift = unterschrift;
        this.aktienText = aktienText;
        this.aktionaersdatenKzf = aktionaersdatenKzf;
        this.praesenzTxt1 = praesenzTxt1;
        this.praesenzTxt2 = praesenzTxt2;
        this.kontrollTxt1 = kontrollTxt1;
        this.kontrollTxt2 = kontrollTxt2;
        this.logoPfad = logoPfad;
        this.eclInhaberImportIsinDaten = eclInhaberImportIsinDaten;
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

    @XmlElement(name = "LandKzf")
    public String getLandKzf() {
        return landKzf;
    }

    public void setLandKzf(String landKzf) {
        this.landKzf = landKzf;
    }

    @XmlElement(name = "DruckName")
    public String getDruckName() {
        return druckName;
    }

    public void setDruckName(String druckName) {
        this.druckName = druckName;
    }

    @XmlElement(name = "FormKzf")
    public String getFormKzf() {
        return formKzf;
    }

    public void setFormKzf(String formKzf) {
        this.formKzf = formKzf;
    }

    @XmlElement(name = "BarcodeKzf")
    public String getBarcodeKzf() {
        return barcodeKzf;
    }

    public void setBarcodeKzf(String barcodeKzf) {
        this.barcodeKzf = barcodeKzf;
    }

    @XmlElement(name = "BarcodeOben")
    public int getBarcodeOben() {
        return barcodeOben;
    }

    public void setBarcodeOben(int barcodeOben) {
        this.barcodeOben = barcodeOben;
    }

    @XmlElement(name = "BarcodeLinks")
    public int getBarcodeLinks() {
        return barcodeLinks;
    }

    public void setBarcodeLinks(int barcodeLinks) {
        this.barcodeLinks = barcodeLinks;
    }

    @XmlElement(name = "BarcodeHoehe")
    public int getBarcodeHoehe() {
        return barcodeHoehe;
    }

    public void setBarcodeHoehe(int barcodeHoehe) {
        this.barcodeHoehe = barcodeHoehe;
    }

    @XmlElement(name = "LogoText1")
    public String getLogoText1() {
        return logoText1;
    }

    public void setLogoText1(String logoText1) {
        this.logoText1 = logoText1;
    }

    @XmlElement(name = "LogoText2")
    public String getLogoText2() {
        return logoText2;
    }

    public void setLogoText2(String logoText2) {
        this.logoText2 = logoText2;
    }

    @XmlElement(name = "EinlText1")
    public String getEinlText1() {
        return einlText1;
    }

    public void setEinlText1(String einlText1) {
        this.einlText1 = einlText1;
    }

    @XmlElement(name = "EinlText2")
    public String getEinlText2() {
        return einlText2;
    }

    public void setEinlText2(String einlText2) {
        this.einlText2 = einlText2;
    }

    @XmlElement(name = "EinlText3")
    public String getEinlText3() {
        return einlText3;
    }

    public void setEinlText3(String einlText3) {
        this.einlText3 = einlText3;
    }

    @XmlElement(name = "EinlText4")
    public String getEinlText4() {
        return einlText4;
    }

    public void setEinlText4(String einlText4) {
        this.einlText4 = einlText4;
    }

    @XmlElement(name = "EinlText5")
    public String getEinlText5() {
        return einlText5;
    }

    public void setEinlText5(String einlText5) {
        this.einlText5 = einlText5;
    }

    @XmlElement(name = "EinlText6")
    public String getEinlText6() {
        return einlText6;
    }

    public void setEinlText6(String einlText6) {
        this.einlText6 = einlText6;
    }

    @XmlElement(name = "EinlText7")
    public String getEinlText7() {
        return einlText7;
    }

    public void setEinlText7(String einlText7) {
        this.einlText7 = einlText7;
    }

    @XmlElement(name = "EinlText8")
    public String getEinlText8() {
        return einlText8;
    }

    public void setEinlText8(String einlText8) {
        this.einlText8 = einlText8;
    }

    @XmlElement(name = "ZusatzText1")
    public String getZusatzText1() {
        return zusatzText1;
    }

    public void setZusatzText1(String zusatzText1) {
        this.zusatzText1 = zusatzText1;
    }

    @XmlElement(name = "ZusatzText2")
    public String getZusatzText2() {
        return zusatzText2;
    }

    public void setZusatzText2(String zusatzText2) {
        this.zusatzText2 = zusatzText2;
    }

    @XmlElement(name = "BarcodeText")
    public String getBarcodeText() {
        return barcodeText;
    }

    public void setBarcodeText(String barcodeText) {
        this.barcodeText = barcodeText;
    }

    @XmlElement(name = "Jahreszahl")
    public int getJahreszahl() {
        return jahreszahl;
    }

    public void setJahreszahl(int jahreszahl) {
        this.jahreszahl = jahreszahl;
    }

    @XmlElement(name = "EKNr1Txt")
    public String getEkNr1Txt() {
        return ekNr1Txt;
    }

    public void setEkNr1Txt(String ekNr1Txt) {
        this.ekNr1Txt = ekNr1Txt;
    }

    @XmlElement(name = "EKNr2Txt")
    public String getEkNr2Txt() {
        return ekNr2Txt;
    }

    public void setEkNr2Txt(String ekNr2Txt) {
        this.ekNr2Txt = ekNr2Txt;
    }

    @XmlElement(name = "EKNr3Txt")
    public String getEkNr3Txt() {
        return ekNr3Txt;
    }

    public void setEkNr3Txt(String ekNr3Txt) {
        this.ekNr3Txt = ekNr3Txt;
    }

    @XmlElement(name = "VersandKzf")
    public String getVersandKzf() {
        return versandKzf;
    }

    public void setVersandKzf(String versandKzf) {
        this.versandKzf = versandKzf;
    }

    @XmlElement(name = "Unterschrift")
    public String getUnterschrift() {
        return unterschrift;
    }

    public void setUnterschrift(String unterschrift) {
        this.unterschrift = unterschrift;
    }

    @XmlElement(name = "AktienText")
    public String getAktienText() {
        return aktienText;
    }

    public void setAktienText(String aktienText) {
        this.aktienText = aktienText;
    }

    @XmlElement(name = "AktionaersdatenKzf")
    public String getAktionaersdatenKzf() {
        return aktionaersdatenKzf;
    }

    public void setAktionaersdatenKzf(String aktionaersdatenKzf) {
        this.aktionaersdatenKzf = aktionaersdatenKzf;
    }

    @XmlElement(name = "PraesenzTxt1")
    public String getPraesenzTxt1() {
        return praesenzTxt1;
    }

    public void setPraesenzTxt1(String praesenzTxt1) {
        this.praesenzTxt1 = praesenzTxt1;
    }

    @XmlElement(name = "PraesenzTxt2")
    public String getPraesenzTxt2() {
        return praesenzTxt2;
    }

    public void setPraesenzTxt2(String praesenzTxt2) {
        this.praesenzTxt2 = praesenzTxt2;
    }

    @XmlElement(name = "KontrollTxt1")
    public String getKontrollTxt1() {
        return kontrollTxt1;
    }

    public void setKontrollTxt1(String kontrollTxt1) {
        this.kontrollTxt1 = kontrollTxt1;
    }

    @XmlElement(name = "KontrollTxt2")
    public String getKontrollTxt2() {
        return kontrollTxt2;
    }

    public void setKontrollTxt2(String kontrollTxt2) {
        this.kontrollTxt2 = kontrollTxt2;
    }

    @XmlElement(name = "LogoPfad")
    public String getLogoPfad() {
        return logoPfad;
    }

    public void setLogoPfad(String logoPfad) {
        this.logoPfad = logoPfad;
    }

    @XmlElement(name = "ISINDaten")
    public List<EclInhaberImportIsinDaten> getEclInhaberImportIsinDaten() {
        return eclInhaberImportIsinDaten;
    }

    public void setEclInhaberImportIsinDaten(List<EclInhaberImportIsinDaten> eclInhaberImportIsinDaten) {
        this.eclInhaberImportIsinDaten = eclInhaberImportIsinDaten;
    }

    @Override
    public String toString() {
        return "EclInhaberImportEkParameter [kurzname=" + kurzname + ", hvDatum=" + hvDatum + ", hstNr=" + hstNr + ", landKzf="
                + landKzf + ", druckName=" + druckName + ", formKzf=" + formKzf + ", barcodeKzf=" + barcodeKzf
                + ", barcodeOben=" + barcodeOben + ", barcodeLinks=" + barcodeLinks + ", barcodeHoehe=" + barcodeHoehe
                + ", logoText1=" + logoText1 + ", logoText2=" + logoText2 + ", einlText1=" + einlText1 + ", einlText2="
                + einlText2 + ", einlText3=" + einlText3 + ", einlText4=" + einlText4 + ", einlText5=" + einlText5
                + ", einlText6=" + einlText6 + ", einlText7=" + einlText7 + ", einlText8=" + einlText8
                + ", zusatzText1=" + zusatzText1 + ", zusatzText2=" + zusatzText2 + ", barcodeText=" + barcodeText
                + ", jahreszahl=" + jahreszahl + ", ekNr1Txt=" + ekNr1Txt + ", ekNr2Txt=" + ekNr2Txt + ", ekNr3Txt="
                + ekNr3Txt + ", versandKzf=" + versandKzf + ", unterschrift=" + unterschrift + ", aktienText="
                + aktienText + ", aktionaersdatenKzf=" + aktionaersdatenKzf + ", praesenzTxt1=" + praesenzTxt1
                + ", praesenzTxt2=" + praesenzTxt2 + ", kontrollTxt1=" + kontrollTxt1 + ", kontrollTxt2=" + kontrollTxt2
                + ", logoPfad=" + logoPfad + ", eclInhaberImportIsinDaten=" + eclInhaberImportIsinDaten + "]";
    }

}
