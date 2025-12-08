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
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PersonName3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PersonName3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NmPrfx" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NamePrefix2Code" minOccurs="0"/&gt;
 *         &lt;element name="FrstNm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max350Text"/&gt;
 *         &lt;element name="Srnm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max350Text"/&gt;
 *         &lt;element name="Adr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PostalAddress26" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonName3", propOrder = {
    "nmPrfx",
    "frstNm",
    "srnm",
    "adr"
})
public class PersonName3 {

    @XmlElement(name = "NmPrfx")
    @XmlSchemaType(name = "string")
    protected NamePrefix2Code nmPrfx;
    @XmlElement(name = "FrstNm", required = true)
    protected String frstNm;
    @XmlElement(name = "Srnm", required = true)
    protected String srnm;
    @XmlElement(name = "Adr")
    protected PostalAddress26 adr;

    /**
     * Ruft den Wert der nmPrfx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NamePrefix2Code }
     *     
     */
    public NamePrefix2Code getNmPrfx() {
        return nmPrfx;
    }

    /**
     * Legt den Wert der nmPrfx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NamePrefix2Code }
     *     
     */
    public void setNmPrfx(NamePrefix2Code value) {
        this.nmPrfx = value;
    }

    /**
     * Ruft den Wert der frstNm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrstNm() {
        return frstNm;
    }

    /**
     * Legt den Wert der frstNm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrstNm(String value) {
        this.frstNm = value;
    }

    /**
     * Ruft den Wert der srnm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrnm() {
        return srnm;
    }

    /**
     * Legt den Wert der srnm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrnm(String value) {
        this.srnm = value;
    }

    /**
     * Ruft den Wert der adr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddress26 }
     *     
     */
    public PostalAddress26 getAdr() {
        return adr;
    }

    /**
     * Legt den Wert der adr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddress26 }
     *     
     */
    public void setAdr(PostalAddress26 value) {
        this.adr = value;
    }

}
