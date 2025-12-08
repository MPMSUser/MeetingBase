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
//
// Diese Datei wurde mit der Eclipse Implementation of JAXB, v3.0.0 generiert 
// Siehe https://eclipse-ee4j.github.io/jaxb-ri 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2024.11.15 um 11:09:47 AM CET 
//


package de.meetingapps.meetingportal.meetComEntitiesXml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für IncentivePremium5 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="IncentivePremium5"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Desc" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max350Text" minOccurs="0"/&gt;
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PriceRateOrAmount3Choice"/&gt;
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}IncentivePremiumType2Choice"/&gt;
 *         &lt;element name="PmtDt" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}DateFormat3Choice" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IncentivePremium5", propOrder = {
    "desc",
    "amt",
    "tp",
    "pmtDt"
})
public class IncentivePremium5 {

    @XmlElement(name = "Desc")
    protected String desc;
    @XmlElement(name = "Amt", required = true)
    protected PriceRateOrAmount3Choice amt;
    @XmlElement(name = "Tp", required = true)
    protected IncentivePremiumType2Choice tp;
    @XmlElement(name = "PmtDt")
    protected DateFormat3Choice pmtDt;

    /**
     * Ruft den Wert der desc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Legt den Wert der desc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PriceRateOrAmount3Choice }
     *     
     */
    public PriceRateOrAmount3Choice getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PriceRateOrAmount3Choice }
     *     
     */
    public void setAmt(PriceRateOrAmount3Choice value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IncentivePremiumType2Choice }
     *     
     */
    public IncentivePremiumType2Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IncentivePremiumType2Choice }
     *     
     */
    public void setTp(IncentivePremiumType2Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der pmtDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DateFormat3Choice }
     *     
     */
    public DateFormat3Choice getPmtDt() {
        return pmtDt;
    }

    /**
     * Legt den Wert der pmtDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DateFormat3Choice }
     *     
     */
    public void setPmtDt(DateFormat3Choice value) {
        this.pmtDt = value;
    }

}
