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

@XmlRootElement(name = "XMLHeader")
@XmlType(propOrder = { "verfahren", "InhaberImportVersion", "sstVersion", "datenArt", "erzeugungsZeitpunkt", "ansprechpartner", "datenLieferant"})
public class EclInhaberImportXmlHeader {

    private String verfahren;

    private String InhaberImportVersion;

    private String sstVersion;

    private String datenArt;

    private String erzeugungsZeitpunkt;

    private String ansprechpartner;

    private String datenLieferant;

    public EclInhaberImportXmlHeader() {

    }

    public EclInhaberImportXmlHeader(String verfahren, String InhaberImportVersion, String sstVersion, String datenArt,
            String erzeugungsZeitpunkt, String ansprechpartner, String datenLieferant) {
        super();
        this.verfahren = verfahren;
        this.InhaberImportVersion = InhaberImportVersion;
        this.sstVersion = sstVersion;
        this.datenArt = datenArt;
        this.erzeugungsZeitpunkt = erzeugungsZeitpunkt;
        this.ansprechpartner = ansprechpartner;
        this.datenLieferant = datenLieferant;
    }

    @XmlElement(name = "Verfahren")
    public String getVerfahren() {
        return verfahren;
    }

    public void setVerfahren(String verfahren) {
        this.verfahren = verfahren;
    }

    @XmlElement(name = "InhaberImportVersion")
    public String getInhaberImportVersion() {
        return InhaberImportVersion;
    }

    public void setInhaberImportVersion(String InhaberImportVersion) {
        this.InhaberImportVersion = InhaberImportVersion;
    }

    @XmlElement(name = "SstVersion")
    public String getSstVersion() {
        return sstVersion;
    }

    public void setSstVersion(String sstVersion) {
        this.sstVersion = sstVersion;
    }

    @XmlElement(name = "DatenArt")
    public String getDatenArt() {
        return datenArt;
    }

    public void setDatenArt(String datenArt) {
        this.datenArt = datenArt;
    }

    @XmlElement(name = "ErzeugungsZeitpunkt")
    public String getErzeugungsZeitpunkt() {
        return erzeugungsZeitpunkt;
    }

    public void setErzeugungsZeitpunkt(String erzeugungsZeitpunkt) {
        this.erzeugungsZeitpunkt = erzeugungsZeitpunkt;
    }

    @XmlElement(name = "Ansprechpartner")
    public String getAnsprechpartner() {
        return ansprechpartner;
    }

    public void setAnsprechpartner(String ansprechpartner) {
        this.ansprechpartner = ansprechpartner;
    }

    @XmlElement(name = "DatenLieferant")
    public String getDatenLieferant() {
        return datenLieferant;
    }

    public void setDatenLieferant(String datenLieferant) {
        this.datenLieferant = datenLieferant;
    }

    @Override
    public String toString() {
        return "EclXmlHeader [verfahren=" + verfahren + ", InhaberImportVersion=" + InhaberImportVersion + ", sstVersion=" + sstVersion
                + ", datenArt=" + datenArt + ", erzeugungsZeitpunkt=" + erzeugungsZeitpunkt + ", ansprechpartner="
                + ansprechpartner + ", DatenLieferant=" + datenLieferant + "]";
    }

}
