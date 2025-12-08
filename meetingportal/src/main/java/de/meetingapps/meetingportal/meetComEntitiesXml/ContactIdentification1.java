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
 * <p>Java-Klasse für ContactIdentification1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContactIdentification1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Nm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text"/&gt;
 *         &lt;element name="NmPrfx" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}NamePrefix1Code" minOccurs="0"/&gt;
 *         &lt;element name="GvnNm" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="Role" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="PhneNb" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PhoneNumber" minOccurs="0"/&gt;
 *         &lt;element name="FaxNb" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}PhoneNumber" minOccurs="0"/&gt;
 *         &lt;element name="EmailAdr" type="{urn:iso:std:iso:20022:tech:xsd:seev.001.001.11}Max256Text" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactIdentification1", propOrder = {
    "nm",
    "nmPrfx",
    "gvnNm",
    "role",
    "phneNb",
    "faxNb",
    "emailAdr"
})
public class ContactIdentification1 {

    @XmlElement(name = "Nm", required = true)
    protected String nm;
    @XmlElement(name = "NmPrfx")
    @XmlSchemaType(name = "string")
    protected NamePrefix1Code nmPrfx;
    @XmlElement(name = "GvnNm")
    protected String gvnNm;
    @XmlElement(name = "Role")
    protected String role;
    @XmlElement(name = "PhneNb")
    protected String phneNb;
    @XmlElement(name = "FaxNb")
    protected String faxNb;
    @XmlElement(name = "EmailAdr")
    protected String emailAdr;

    /**
     * Ruft den Wert der nm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNm() {
        return nm;
    }

    /**
     * Legt den Wert der nm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNm(String value) {
        this.nm = value;
    }

    /**
     * Ruft den Wert der nmPrfx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link NamePrefix1Code }
     *     
     */
    public NamePrefix1Code getNmPrfx() {
        return nmPrfx;
    }

    /**
     * Legt den Wert der nmPrfx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link NamePrefix1Code }
     *     
     */
    public void setNmPrfx(NamePrefix1Code value) {
        this.nmPrfx = value;
    }

    /**
     * Ruft den Wert der gvnNm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGvnNm() {
        return gvnNm;
    }

    /**
     * Legt den Wert der gvnNm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGvnNm(String value) {
        this.gvnNm = value;
    }

    /**
     * Ruft den Wert der role-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Legt den Wert der role-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Ruft den Wert der phneNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhneNb() {
        return phneNb;
    }

    /**
     * Legt den Wert der phneNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhneNb(String value) {
        this.phneNb = value;
    }

    /**
     * Ruft den Wert der faxNb-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNb() {
        return faxNb;
    }

    /**
     * Legt den Wert der faxNb-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNb(String value) {
        this.faxNb = value;
    }

    /**
     * Ruft den Wert der emailAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAdr() {
        return emailAdr;
    }

    /**
     * Legt den Wert der emailAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAdr(String value) {
        this.emailAdr = value;
    }

}
