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

@XmlRootElement(name = "Nummernkreise")
@XmlType(propOrder = { "kurzname", "hvDatum", "hstNr", "isin", "ekNrNr", "ekNrVon", "ekNrBis", "letzeEKNr" })
public class EclInhaberImportNummernkreise {

    private String kurzname;

    private String hvDatum;

    private int hstNr;

    private String isin;

    private int ekNrNr;

    private int ekNrVon;

    private int ekNrBis;

    private int letzeEKNr;

    public EclInhaberImportNummernkreise() {

    }

    public EclInhaberImportNummernkreise(String kurzname, String hvDatum, int hstNr, String isin, int ekNrNr, int ekNrVon,
            int ekNrBis, int letzeEKNr) {
        super();
        this.kurzname = kurzname;
        this.hvDatum = hvDatum;
        this.hstNr = hstNr;
        this.isin = isin;
        this.ekNrNr = ekNrNr;
        this.ekNrVon = ekNrVon;
        this.ekNrBis = ekNrBis;
        this.letzeEKNr = letzeEKNr;
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

    @XmlElement(name = "EKNrNr")
    public int getEkNrNr() {
        return ekNrNr;
    }

    public void setEkNrNr(int ekNrNr) {
        this.ekNrNr = ekNrNr;
    }

    @XmlElement(name = "EKNrVon")
    public int getEkNrVon() {
        return ekNrVon;
    }

    public void setEkNrVon(int ekNrVon) {
        this.ekNrVon = ekNrVon;
    }

    @XmlElement(name = "EKNrBis")
    public int getEkNrBis() {
        return ekNrBis;
    }

    public void setEkNrBis(int ekNrBis) {
        this.ekNrBis = ekNrBis;
    }

    @XmlElement(name = "LetzteEKNr")
    public int getLetzeEKNr() {
        return letzeEKNr;
    }

    public void setLetzeEKNr(int letzeEKNr) {
        this.letzeEKNr = letzeEKNr;
    }

    @Override
    public String toString() {
        return "EclInhaberImportNummernkreise [kurzname=" + kurzname + ", hvDatum=" + hvDatum + ", hstNr=" + hstNr + ", isin="
                + isin + ", ekNrNr=" + ekNrNr + ", ekNrVon=" + ekNrVon + ", ekNrBis=" + ekNrBis + ", letzeEKNr="
                + letzeEKNr + "]";
    }

}
